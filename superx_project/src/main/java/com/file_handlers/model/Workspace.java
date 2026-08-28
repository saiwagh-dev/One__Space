package com.file_handlers.model;

public class Workspace {

    private String workspaceId;
    private String name;
    private String ownerId;
    private int memberCount;
    private int fileCount;

    public Workspace() {
    }

    public Workspace(String workspaceId, String name, String ownerId) {
        this.workspaceId = workspaceId;
        this.name = name;
        this.ownerId = ownerId;
        this.memberCount = 1;
        this.fileCount = 0;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }
}