package com.file_handlers.service;

import java.util.List;

public class EmbeddingService{
    private final GeminiClient geminiClient;

    public EmbeddingService(){
        geminiClient=new GeminiClient();
    }

    public List<Double> embedDocument(String text)throws Exception{
        return geminiClient.embedDocument(text);
    }

    public List<Double> embedQuery(String text)throws Exception{
        return geminiClient.embedQuery(text);
    }

    public String buildFileText(
            String fileName,
            String description,
            List<String> smartTags,
            String category,
            String fileType){
        return "File Name: "+safe(fileName)
                +"\nDescription: "+safe(description)
                +"\nTags: "+(smartTags==null?"":String.join(", ",smartTags))
                +"\nCategory: "+safe(category)
                +"\nFile Type: "+safe(fileType);
    }

    private String safe(String value){
        return value==null?"":value;
    }
}