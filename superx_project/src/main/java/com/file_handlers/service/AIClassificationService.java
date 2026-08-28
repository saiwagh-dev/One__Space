package com.file_handlers.service;

import org.json.JSONArray;
import org.json.JSONObject;

import com.file_handlers.model.AIResult;
import com.file_handlers.model.FileData;

public class AIClassificationService {

    private final GeminiClient geminiClient;

    public AIClassificationService() {
        geminiClient = new GeminiClient();
    }

    public AIResult classify(FileData file) {

        try {
            String response =
                    geminiClient.classify(
                            file.getFileName(),
                            file.getExtractedSnippet()
                    );

            JSONObject json =
                    new JSONObject(response);

            String category =
                    normalizeCategory(
                            json.optString(
                                    "category",
                                    "Other"
                            )
                    );

            double confidence =
                    json.optDouble(
                            "confidence",
                            0.0
                    );

            String description =
                    json.optString(
                            "description",
                            ""
                    ).trim();

            JSONArray tags =
                    json.optJSONArray(
                            "smartTags"
                    );

            String[] smartTags =
                    extractTags(tags);

            if (description.isBlank()) {
                description =
                        "No AI description was generated.";
            }

            return new AIResult(
                    category,
                    confidence,
                    description,
                    smartTags
            );

        } catch (Exception e) {

            System.out.println(
                    "[AI] Failed: " +
                    e.getMessage()
            );

            return new AIResult(
                    "Other",
                    0.0,
                    "AI understanding failed.",
                    new String[0]
            );
        }
    }

    private String normalizeCategory(String category) {

        if (category == null) {
            return "Other";
        }

        return switch (
                category.trim().toLowerCase()
        ) {
            case "personal" ->
                    "Personal";
            case "college" ->
                    "College";
            case "office" ->
                    "Office";
            case "finance" ->
                    "Finance";
            case "entertainment" ->
                    "Entertainment";
            case "other", "others" ->
                    "Other";
            default ->
                    "Other";
        };
    }

    private String[] extractTags(JSONArray tags) {

        if (tags == null) {
            return new String[0];
        }

        int count =
                Math.min(
                        tags.length(),
                        6
                );

        String[] result =
                new String[count];

        for (int i = 0; i < count; i++) {
            result[i] =
                    tags.optString(i, "").trim();
        }

        return result;
    }
}