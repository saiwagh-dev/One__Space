package com.file_handlers.model;

import com.google.cloud.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileData{
    private String fileName;
    private String localPath;
    private long fileSize;
    private String fileType;
    private String fileHash;
    private String uploadedBy;
    private Timestamp uploadedAt;
    private Timestamp lastAccessedAt;
    private String extractedSnippet;
    private String aiCategory;
    private double aiConfidence;
    private String description;
    private List<String> smartTags;
    private String spaceId;
    private boolean deleted;
    private Timestamp deletedAt;

    public FileData(){}

    public String getFileName(){return fileName;}
    public void setFileName(String fileName){this.fileName=fileName;}
    public String getLocalPath(){return localPath;}
    public void setLocalPath(String localPath){this.localPath=localPath;}
    public long getFileSize(){return fileSize;}
    public void setFileSize(long fileSize){this.fileSize=fileSize;}
    public String getFileType(){return fileType;}
    public void setFileType(String fileType){this.fileType=fileType;}
    public String getFileHash(){return fileHash;}
    public void setFileHash(String fileHash){this.fileHash=fileHash;}
    public String getUploadedBy(){return uploadedBy;}
    public void setUploadedBy(String uploadedBy){this.uploadedBy=uploadedBy;}
    public Timestamp getUploadedAt(){return uploadedAt;}
    public void setUploadedAt(Timestamp uploadedAt){this.uploadedAt=uploadedAt;}
    public Timestamp getLastAccessedAt(){return lastAccessedAt;}
    public void setLastAccessedAt(Timestamp lastAccessedAt){this.lastAccessedAt=lastAccessedAt;}
    public String getExtractedSnippet(){return extractedSnippet;}
    public void setExtractedSnippet(String extractedSnippet){this.extractedSnippet=extractedSnippet;}
    public String getAiCategory(){return aiCategory;}
    public void setAiCategory(String aiCategory){this.aiCategory=aiCategory;}
    public double getAiConfidence(){return aiConfidence;}
    public void setAiConfidence(double aiConfidence){this.aiConfidence=aiConfidence;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
    public List<String> getSmartTags(){return smartTags;}
    public void setSmartTags(List<String> smartTags){this.smartTags=smartTags;}
    public String getSpaceId(){return spaceId;}
    public void setSpaceId(String spaceId){this.spaceId=spaceId;}
    public boolean isDeleted(){return deleted;}
    public void setDeleted(boolean deleted){this.deleted=deleted;}
    public Timestamp getDeletedAt(){return deletedAt;}
    public void setDeletedAt(Timestamp deletedAt){this.deletedAt=deletedAt;}

    public Map<String,Object> toMap(){
        Map<String,Object> data=new HashMap<>();
        data.put("fileName",fileName);
        data.put("localPath",localPath);
        data.put("fileSize",fileSize);
        data.put("fileType",fileType);
        data.put("fileHash",fileHash);
        data.put("uploadedBy",uploadedBy);
        data.put("uploadedAt",uploadedAt);
        data.put("lastAccessedAt",lastAccessedAt);
        data.put("extractedSnippet",extractedSnippet);
        data.put("aiCategory",aiCategory);
        data.put("aiConfidence",aiConfidence);
        data.put("description",description);
        data.put("smartTags",smartTags);
        data.put("spaceId",spaceId);
        data.put("deleted",deleted);
        data.put("deletedAt",deletedAt);
        return data;
    }
}