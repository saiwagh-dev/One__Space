package com.file_handlers.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.file_handlers.config.FirebaseConfig;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;

public class AdminFileStatsDAO {

    private final Firestore db;
    private final FirebaseAuth auth;

    public AdminFileStatsDAO() {
        db = FirebaseConfig.getFirestore();
        auth = FirebaseAuth.getInstance();
    }

    // =========================================================
    // TOTAL FILES
    // =========================================================

    public int getTotalFiles() throws Exception {
        return db.collectionGroup("files")
                .get()
                .get()
                .size();
    }

    // =========================================================
    // TOTAL USERS
    // =========================================================

    public int getTotalUsers() throws Exception {

        int count = 0;
        ListUsersPage page = auth.listUsers(null);

        while (page != null) {

            for (UserRecord user : page.getValues()) {
                count++;
            }

            String token = page.getNextPageToken();

            if (token == null || token.isBlank()) {
                break;
            }

            page = auth.listUsers(token);
        }

        return count;
    }

    // =========================================================
    // WEEKLY UPLOAD COUNTS
    // =========================================================

    public Map<String, Integer> getWeeklyUploadCounts(
            String period
    ) throws Exception {

        QuerySnapshot snapshot =
                db.collectionGroup("files")
                        .get()
                        .get();

        int weeks = getWeekCount(period);

        Map<String, Integer> counts =
                new LinkedHashMap<>();

        for (int i = 1; i <= weeks; i++) {
            counts.put("Week " + i, 0);
        }

        long now = System.currentTimeMillis();
        long weekMillis = 7L * 24 * 60 * 60 * 1000;

        for (DocumentSnapshot document :
                snapshot.getDocuments()) {

            Timestamp uploadedAt =
                    document.getTimestamp("uploadedAt");

            if (uploadedAt == null) {
                continue;
            }

            long age =
                    now - uploadedAt.toDate().getTime();

            if (age < 0) {
                continue;
            }

            int weekFromNow =
                    (int) (age / weekMillis) + 1;

            if (weekFromNow <= weeks) {

                int chartWeek =
                        weeks - weekFromNow + 1;

                String key =
                        "Week " + chartWeek;

                counts.put(
                        key,
                        counts.get(key) + 1
                );
            }
        }

        return counts;
    }

    private int getWeekCount(String period) {

        if ("Last Month".equals(period)) {
            return 4;
        }

        if ("Last 3 Months".equals(period)) {
            return 12;
        }

        if ("This Year".equals(period)) {
            return 52;
        }

        return 4;
    }

    // =========================================================
    // FILE TYPE COUNTS
    // =========================================================

    public Map<String, Integer> getFileTypeCounts()
            throws Exception {

        QuerySnapshot snapshot =
                db.collectionGroup("files")
                        .get()
                        .get();

        Map<String, Integer> counts =
                createFileTypeMap();

        for (DocumentSnapshot document :
                snapshot.getDocuments()) {

            String type =
                    normalizeFileType(
                            document.getString("fileType")
                    );

            counts.put(
                    type,
                    counts.get(type) + 1
            );
        }

        return counts;
    }

    // =========================================================
    // CATEGORY COUNTS
    // =========================================================

    public Map<String, Integer> getCategoryCounts()
            throws Exception {

        QuerySnapshot snapshot =
                db.collectionGroup("files")
                        .get()
                        .get();

        Map<String, Integer> counts =
                createCategoryMap();

        for (DocumentSnapshot document :
                snapshot.getDocuments()) {

            String category =
                    normalizeCategory(
                            document.getString("aiCategory")
                    );

            counts.put(
                    category,
                    counts.get(category) + 1
            );
        }

        return counts;
    }

    private String normalizeFileType(String value) {

        if (value == null || value.isBlank()) {
            return "Others";
        }

        String type =
                value.trim().toLowerCase();

        if (type.contains("pdf")) {
            return "PDF";
        }

        if (type.startsWith("image/") ||
                type.matches(
                        ".*\\.(jpg|jpeg|png|gif|webp|bmp|svg)$")) {
            return "Images";
        }

        if (type.startsWith("video/") ||
                type.matches(
                        ".*\\.(mp4|mov|avi|mkv|webm)$")) {
            return "Videos";
        }

        if (type.startsWith("audio/") ||
                type.matches(
                        ".*\\.(mp3|wav|m4a|aac|flac|ogg)$")) {
            return "Audio";
        }

        if (type.startsWith("text/") ||
                type.contains("word") ||
                type.contains("document") ||
                type.contains("spreadsheet") ||
                type.contains("excel") ||
                type.contains("presentation") ||
                type.matches(
                        ".*\\.(doc|docx|xls|xlsx|ppt|pptx|txt|csv)$")) {
            return "Documents";
        }

        return "Others";
    }

    private String normalizeCategory(String value) {

        if (value == null || value.isBlank()) {
            return "Other";
        }

        return switch (value.trim().toLowerCase()) {
            case "college" -> "College";
            case "personal" -> "Personal";
            case "office" -> "Office";
            case "finance" -> "Finance";
            case "entertainment" -> "Entertainment";
            default -> "Other";
        };
    }

    private Map<String, Integer> createFileTypeMap() {

        Map<String, Integer> map =
                new LinkedHashMap<>();

        map.put("PDF", 0);
        map.put("Images", 0);
        map.put("Documents", 0);
        map.put("Videos", 0);
        map.put("Audio", 0);
        map.put("Others", 0);

        return map;
    }

    private Map<String, Integer> createCategoryMap() {

        Map<String, Integer> map =
                new LinkedHashMap<>();

        map.put("College", 0);
        map.put("Personal", 0);
        map.put("Office", 0);
        map.put("Finance", 0);
        map.put("Entertainment", 0);
        map.put("Other", 0);

        return map;
    }
}
