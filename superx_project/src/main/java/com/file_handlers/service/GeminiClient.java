package com.file_handlers.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

public class GeminiClient{
    private static final String MODEL="gemini-3.6-flash";
    private static final String API_URL="https://generativelanguage.googleapis.com/v1beta/models/"+MODEL+":generateContent";
    private final String apiKey;
    private final HttpClient client;

    public GeminiClient(){
        apiKey=System.getenv("GEMINI_API_KEY");
        if(apiKey==null||apiKey.isBlank())throw new IllegalStateException("GEMINI_API_KEY environment variable is not set.");
        client=HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
    }

    public String classify(String fileName,String extractedText)throws IOException,InterruptedException{
        String prompt="Analyze this file for OneSpace.\n"
                +"File name: "+fileName+"\n"
                +"Content:\n"+extractedText+"\n\n"
                +"Return ONLY valid JSON.\n"
                +"Use exactly one category from: Personal, College, Office, Finance, Entertainment, Other.\n"
                +"confidence must be between 0 and 1.\n"
                +"description must be a short meaningful description of the file in 2-4 sentences.\n"
                +"smartTags must contain exactly 5 or 6 useful short tags.\n"
                +"Format:\n"
                +"{\"category\":\"Office\",\"confidence\":0.95,\"description\":\"Short description.\",\"smartTags\":[\"tag1\",\"tag2\",\"tag3\",\"tag4\",\"tag5\"]}";
        return sendRequest(prompt);
    }

    public String chat(String userMessage,String conversationContext)throws IOException,InterruptedException{
        String prompt="You are OneSpace AI Assistant, a helpful and concise assistant inside a desktop file-management application.\n"
                +"Answer the user's question naturally and clearly.\n"
                +"Do not claim access to files unless their content is provided.\n\n"
                +"Conversation:\n"+(conversationContext==null?"":conversationContext)
                +"\nUser: "+userMessage+"\nAssistant:";
        return sendRequest(prompt);
    }

    private String sendRequest(String prompt)throws IOException,InterruptedException{
        JSONObject body=new JSONObject()
                .put("contents",new JSONArray()
                .put(new JSONObject()
                .put("parts",new JSONArray()
                .put(new JSONObject().put("text",prompt)))));

        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create(API_URL+"?key="+apiKey))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString(),StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());

        if(response.statusCode()!=200)
            throw new IOException("Gemini API "+response.statusCode()+": "+response.body());

        JSONObject root=new JSONObject(response.body());

        if(!root.has("candidates")||root.getJSONArray("candidates").isEmpty())
            throw new IOException("Gemini returned no response: "+response.body());

        JSONObject candidate=root.getJSONArray("candidates").getJSONObject(0);

        if(!candidate.has("content"))
            throw new IOException("Gemini returned no text: "+response.body());

        JSONArray parts=candidate.getJSONObject("content").getJSONArray("parts");

        for(int i=0;i<parts.length();i++){
            JSONObject part=parts.getJSONObject(i);
            if(part.has("text"))return part.getString("text").trim();
        }

        throw new IOException("Gemini response contained no text.");
    }
}