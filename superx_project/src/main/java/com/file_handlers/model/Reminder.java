package com.file_handlers.model;

import com.google.cloud.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Reminder{
    private String id;
    private String title;
    private String description;
    private String type;
    private Timestamp date;
    private String time;
    private String repeat;
    private String priority;
    private String linkedFileId;
    private String linkedFileName;
    private Timestamp createdAt;
    private String source;

    public Reminder(){}

    public String getId(){return id;}
    public void setId(String id){this.id=id;}

    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}

    public String getType(){return type;}
    public void setType(String type){this.type=type;}

    public Timestamp getDate(){return date;}
    public void setDate(Timestamp date){this.date=date;}

    public String getTime(){return time;}
    public void setTime(String time){this.time=time;}

    public String getRepeat(){return repeat;}
    public void setRepeat(String repeat){this.repeat=repeat;}

    public String getPriority(){return priority;}
    public void setPriority(String priority){this.priority=priority;}

    public String getLinkedFileId(){return linkedFileId;}
    public void setLinkedFileId(String linkedFileId){this.linkedFileId=linkedFileId;}

    public String getLinkedFileName(){return linkedFileName;}
    public void setLinkedFileName(String linkedFileName){this.linkedFileName=linkedFileName;}

    public Timestamp getCreatedAt(){return createdAt;}
    public void setCreatedAt(Timestamp createdAt){this.createdAt=createdAt;}

    public String getSource(){return source;}
    public void setSource(String source){this.source=source;}

    public boolean isAiExtracted(){
        return "ai_extracted".equalsIgnoreCase(source);
    }

    public Map<String,Object> toMap(){
        Map<String,Object> data=new HashMap<>();

        data.put("title",title);
        data.put("description",description);
        data.put("type",type);
        data.put("date",date);
        data.put("time",time);
        data.put("repeat",repeat);
        data.put("priority",priority);
        data.put("linkedFileId",linkedFileId);
        data.put("linkedFileName",linkedFileName);
        data.put("createdAt",createdAt);
        data.put("source",source);

        return data;
    }
}