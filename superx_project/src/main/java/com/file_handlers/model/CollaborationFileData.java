package com.file_handlers.model;


public class CollaborationFileData {
    public String icon;
    public String fileName;
    public String size;
    public String uploadedOn;
    public String iconColor;
    public String secureUrl;
    public String uploaderName;

    // Default constructor required for Firestore toObject() mapping
    public CollaborationFileData() {}

    public CollaborationFileData(String icon, String fileName, String size, String uploadedOn, String iconColor, String secureUrl, String uploaderName) {
        this.icon = icon;
        this.fileName = fileName;
        this.size = size;
        this.uploadedOn = uploadedOn;
        this.iconColor = iconColor;
        this.secureUrl = secureUrl;
        this.uploaderName = uploaderName;
    }
}