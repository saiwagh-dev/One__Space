package com.file_handlers.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.file_handlers.config.FirebaseConfig;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;

public class AdminAlertDAO {

    private static final String COLLECTION = "adminAlerts";

    private final Firestore db;

    public AdminAlertDAO() {
        db = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // SAVE ALERT
    // =========================================================

    public void logAlert(
            String title,
            String description
    ) throws Exception {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Alert title is required."
            );
        }

        Map<String, Object> data = new HashMap<>();

        data.put("title", title.trim());
        data.put(
                "description",
                description == null ? "" : description.trim()
        );
        data.put("createdAt", Timestamp.now());

        db.collection(COLLECTION)
                .add(data)
                .get();
    }

    // =========================================================
    // FAILED LOGIN COUNT
    // =========================================================

    public int getFailedLoginCount(int days)
            throws Exception {

        if (days <= 0) {
            days = 30;
        }

        long cutoff =
                System.currentTimeMillis()
                        - (days * 24L * 60 * 60 * 1000);

        var snapshot =
                db.collection(COLLECTION)
                        .whereEqualTo(
                                "title",
                                "Failed Sign-In"
                        )
                        .get()
                        .get();

        int count = 0;

        for (DocumentSnapshot document :
                snapshot.getDocuments()) {

            Timestamp createdAt =
                    document.getTimestamp("createdAt");

            if (createdAt == null) {
                continue;
            }

            if (createdAt.toDate().getTime() >= cutoff) {
                count++;
            }
        }

        return count;
    }

    // =========================================================
    // GET RECENT ALERTS
    // =========================================================

    public List<Map<String, Object>> getRecentAlerts(
            int limit
    ) throws Exception {

        if (limit <= 0) {
            limit = 10;
        }

        List<Map<String, Object>> alerts =
                new ArrayList<>();

        var snapshot =
                db.collection(COLLECTION)
                        .orderBy(
                                "createdAt",
                                Query.Direction.DESCENDING
                        )
                        .limit(limit)
                        .get()
                        .get();

        for (DocumentSnapshot document :
                snapshot.getDocuments()) {

            Map<String, Object> alert =
                    new HashMap<>();

            alert.put(
                    "title",
                    document.getString("title")
            );

            alert.put(
                    "description",
                    document.getString("description")
            );

            alert.put(
                    "createdAt",
                    document.getTimestamp("createdAt")
            );

            alerts.add(alert);
        }

        return alerts;
    }
}
