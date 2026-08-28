package com.file_handlers.model;


public class CollaborationMemberData {
    public String initials;
    public String name;
    public String email;
    public String role;
    public String avatarBackground;
    public String avatarColor;
    public String status;

    public CollaborationMemberData() {} // Required for Firestore mapping

    public CollaborationMemberData(String initials, String name, String email, String role, String avatarBackground, String avatarColor, String status) {
        this.initials = initials;
        this.name = name;
        this.email = email;
        this.role = role;
        this.avatarBackground = avatarBackground;
        this.avatarColor = avatarColor;
        this.status = status;
    }
}