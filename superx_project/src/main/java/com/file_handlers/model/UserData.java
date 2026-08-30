package com.file_handlers.model;

public class UserData {

    private String uid;
    private String name;
    private String email;
    private String status;
    private String lastLogin;

    public UserData() {
    }

    public UserData(
            String uid,
            String name,
            String email,
            String status,
            String lastLogin
    ) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.status = status;
        this.lastLogin = lastLogin;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
}
