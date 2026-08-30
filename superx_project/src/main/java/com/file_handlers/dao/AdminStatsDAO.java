package com.file_handlers.dao;

import com.file_handlers.config.FirebaseConfig;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;

public class AdminStatsDAO {

    private final FirebaseAuth auth;
    private final Firestore db;

    public AdminStatsDAO() {

        FirebaseConfig.getFirebaseApp();

        auth = FirebaseAuth.getInstance();
        db = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // TOTAL USERS
    // =========================================================

    public int getTotalUsers() throws Exception {

        int count = 0;

        ListUsersPage page =
                auth.listUsers(null);

        while (page != null) {

            for (var user : page.getValues()) {
                count++;
            }

            String nextPageToken =
                    page.getNextPageToken();

            if (nextPageToken == null ||
                    nextPageToken.isBlank()) {

                break;
            }

            page =
                    auth.listUsers(nextPageToken);
        }

        return count;
    }

    // =========================================================
    // TOTAL FILES
    // =========================================================

    public int getTotalFiles() throws Exception {

        QuerySnapshot snapshot =
                db.collectionGroup("files")
                        .get()
                        .get();

        return snapshot.size();
    }
}