package com.file_handlers.model;

import java.util.Arrays;
import java.util.List;

public class AIResult{

    private String category;
    private double confidence;
    private String description;
    private List<String> smartTags;
    private String customSpaceId;
    private List<ExtractedEvent> extractedEvents;

    public AIResult(){
        this.smartTags=List.of();
        this.extractedEvents=List.of();
    }

    public AIResult(
            String category,
            double confidence
    ){
        this.category=category;
        this.confidence=confidence;
        this.description="";
        this.smartTags=List.of();
        this.extractedEvents=List.of();
    }

    public AIResult(
            String category,
            double confidence,
            String description,
            String[] smartTags
    ){
        this.category=category;
        this.confidence=confidence;
        this.description=description;
        this.smartTags=smartTags==null
                ?List.of()
                :Arrays.asList(smartTags);
        this.extractedEvents=List.of();
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category=category;
    }

    public double getConfidence(){
        return confidence;
    }

    public void setConfidence(double confidence){
        this.confidence=confidence;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public List<String> getSmartTags(){
        return smartTags;
    }

    public void setSmartTags(List<String> smartTags){
        this.smartTags=smartTags==null
                ?List.of()
                :smartTags;
    }

    public String getCustomSpaceId(){
        return customSpaceId;
    }

    public void setCustomSpaceId(String customSpaceId){
        this.customSpaceId=customSpaceId;
    }

    public List<ExtractedEvent> getExtractedEvents(){
        return extractedEvents;
    }

    public void setExtractedEvents(
            List<ExtractedEvent> extractedEvents
    ){
        this.extractedEvents=extractedEvents==null
                ?List.of()
                :extractedEvents;
    }
}