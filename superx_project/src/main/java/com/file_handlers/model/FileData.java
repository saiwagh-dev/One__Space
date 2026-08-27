package com.file_handlers.model;

import com.google.cloud.Timestamp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileData {

    // =========================================================
    // BASIC FILE INFORMATION
    // =========================================================

    private String fileName;
    private String localPath;
    private long fileSize;
    private String fileType;
    private String fileHash;

    // =========================================================
    // USER INFORMATION
    // =========================================================

    /*
     * Firestore Timestamp is used instead of java.time.Instant.
     *
     * This keeps the model directly compatible with
     * Firestore Timestamp values.
     */
    private String uploadedBy;
    private Timestamp uploadedAt;

    // =========================================================
    // EXTRACTED CONTENT
    // =========================================================

    private String extractedSnippet;

    // =========================================================
    // AI INFORMATION
    // =========================================================

    private String aiCategory;
    private double aiConfidence;
    private String description;
    private List<String> smartTags;

    // =========================================================
    // SPACE
    // =========================================================

    private String spaceId;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    /*
     * Required by Firestore's object mapper.
     */
    public FileData() {
    }

    // =========================================================
    // FILE NAME
    // =========================================================

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    // =========================================================
    // LOCAL PATH
    // =========================================================

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    // =========================================================
    // FILE SIZE
    // =========================================================

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    // =========================================================
    // FILE TYPE
    // =========================================================

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    // =========================================================
    // FILE HASH
    // =========================================================

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    // =========================================================
    // UPLOADED BY
    // =========================================================

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    // =========================================================
    // UPLOADED AT
    // =========================================================

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    // =========================================================
    // EXTRACTED SNIPPET
    // =========================================================

    public String getExtractedSnippet() {
        return extractedSnippet;
    }

    public void setExtractedSnippet(
            String extractedSnippet
    ) {
        this.extractedSnippet = extractedSnippet;
    }

    // =========================================================
    // AI CATEGORY
    // =========================================================

    public String getAiCategory() {
        return aiCategory;
    }

    public void setAiCategory(
            String aiCategory
    ) {
        this.aiCategory = aiCategory;
    }

    // =========================================================
    // AI CONFIDENCE
    // =========================================================

    public double getAiConfidence() {
        return aiConfidence;
    }

    public void setAiConfidence(
            double aiConfidence
    ) {
        this.aiConfidence = aiConfidence;
    }

    // =========================================================
    // DESCRIPTION
    // =========================================================

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description
    ) {
        this.description = description;
    }

    // =========================================================
    // SMART TAGS
    // =========================================================

    public List<String> getSmartTags() {
        return smartTags;
    }

    /*
     * IMPORTANT:
     *
     * Keep ONLY ONE setter for smartTags.
     *
     * DO NOT add:
     *
     * setSmartTags(String[] smartTags)
     *
     * or any other overloaded setSmartTags method.
     *
     * Firestore's JavaBean mapper can otherwise report:
     *
     * "Class FileData has multiple setter overloads
     *  with name setSmartTags"
     */
    public void setSmartTags(
            List<String> smartTags
    ) {
        this.smartTags = smartTags;
    }

    // =========================================================
    // SPACE
    // =========================================================

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(
            String spaceId
    ) {
        this.spaceId = spaceId;
    }

    // =========================================================
    // FIRESTORE MAP
    // =========================================================

    /*
     * Converts this FileData object into a Firestore document.
     *
     * uploadedAt remains a Firestore Timestamp.
     */
    public Map<String, Object> toMap() {

        Map<String, Object> data =
                new HashMap<>();

        // -----------------------------------------------------
        // BASIC FILE INFORMATION
        // -----------------------------------------------------

        data.put(
                "fileName",
                fileName
        );

        data.put(
                "localPath",
                localPath
        );

        data.put(
                "fileSize",
                fileSize
        );

        data.put(
                "fileType",
                fileType
        );

        data.put(
                "fileHash",
                fileHash
        );

        // -----------------------------------------------------
        // USER INFORMATION
        // -----------------------------------------------------

        data.put(
                "uploadedBy",
                uploadedBy
        );

        /*
         * Firestore-compatible Timestamp.
         */
        data.put(
                "uploadedAt",
                uploadedAt
        );

        // -----------------------------------------------------
        // EXTRACTED CONTENT
        // -----------------------------------------------------

        data.put(
                "extractedSnippet",
                extractedSnippet
        );

        // -----------------------------------------------------
        // AI INFORMATION
        // -----------------------------------------------------

        data.put(
                "aiCategory",
                aiCategory
        );

        data.put(
                "aiConfidence",
                aiConfidence
        );

        data.put(
                "description",
                description
        );

        data.put(
                "smartTags",
                smartTags
        );

        // -----------------------------------------------------
        // SPACE
        // -----------------------------------------------------

        data.put(
                "spaceId",
                spaceId
        );

        return data;
    }
}