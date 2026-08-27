package com.file_handlers.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class GeminiClient {

    private static final String MODEL = "gemini-3.6-flash";
    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/"
            + MODEL + ":generateContent?key=";

    private final String apiKey;
    private final HttpClient client;

    public GeminiClient() {
        apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "GEMINI_API_KEY environment variable is not set."
            );
        }

        client = HttpClient.newHttpClient();
    }

    public String classify(
            String fileName,
            String extractedText
    ) throws IOException, InterruptedException {

        String prompt =
                "Analyze this file for OneSpace.\n"
                + "File name: " + fileName + "\n"
                + "Content:\n" + extractedText + "\n\n"
                + "Return ONLY valid JSON.\n"
                + "Use exactly one category from: "
                + "Personal, College, Office, Finance, Entertainment, Other.\n"
                + "confidence must be between 0 and 1.\n"
                + "description must be a short meaningful description of the file "
                + "in 2-4 sentences.\n"
                + "smartTags must contain exactly 5 or 6 useful short tags.\n"
                + "Format:\n"
                + "{"
                + "\"category\":\"Office\","
                + "\"confidence\":0.95,"
                + "\"description\":\"Short description.\","
                + "\"smartTags\":[\"tag1\",\"tag2\",\"tag3\",\"tag4\",\"tag5\"]"
                + "}";

        JSONObject textPart = new JSONObject();
        textPart.put("text", prompt);

        JSONObject parts = new JSONObject();
        parts.put("parts", new org.json.JSONArray().put(textPart));

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(API_URL + apiKey))
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(
                                        new JSONObject()
                                                .put(
                                                        "contents",
                                                        new org.json.JSONArray()
                                                                .put(parts)
                                                )
                                                .toString(),
                                        StandardCharsets.UTF_8
                                )
                        )
                        .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Gemini request failed: "
                    + response.statusCode()
                    + " "
                    + response.body()
            );
        }

        JSONObject root =
                new JSONObject(response.body());

        String result =
                root.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                        .trim();

        return cleanJson(result);
    }

    private String cleanJson(String text) {

        if (text.startsWith("```json")) {
            text = text.substring(7);
        } else if (text.startsWith("```")) {
            text = text.substring(3);
        }

        if (text.endsWith("```")) {
            text = text.substring(
                    0,
                    text.length() - 3
            );
        }

        return text.trim();
    }
}