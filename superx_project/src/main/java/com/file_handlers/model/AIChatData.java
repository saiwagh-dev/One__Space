package com.file_handlers.model;

import java.time.Instant;

public class AIChatData {
    private String chatId;
    private String title;
    private Instant createdAt;
    private Instant updatedAt;

    public AIChatData(){}

    public AIChatData(String chatId,String title,Instant createdAt,Instant updatedAt){
        this.chatId=chatId;
        this.title=title;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }

    public String getChatId(){return chatId;}
    public void setChatId(String chatId){this.chatId=chatId;}

    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}

    public Instant getCreatedAt(){return createdAt;}
    public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}

    public Instant getUpdatedAt(){return updatedAt;}
    public void setUpdatedAt(Instant updatedAt){this.updatedAt=updatedAt;}
}