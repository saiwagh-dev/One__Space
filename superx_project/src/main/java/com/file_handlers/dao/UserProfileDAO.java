package com.file_handlers.dao;

import java.util.HashMap;
import java.util.Map;

import com.file_handlers.config.FirebaseConfig;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

public class UserProfileDAO {

    private static final String USERS_COLLECTION =
            "users";

    private final Firestore db;

    // ---------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------

    public UserProfileDAO() {

        db = FirebaseConfig.getFirestore();
    }

    // ---------------------------------------------------------
    // Get user profile
    //
    // Firestore:
    //
    // users/{uid}
    // ---------------------------------------------------------

    public Map<String, Object> getProfile(
            String uid
    ) throws Exception {

        validateUid(uid);

        DocumentSnapshot document =
                db.collection(USERS_COLLECTION)
                        .document(uid)
                        .get()
                        .get();

        if (!document.exists()) {
            return new HashMap<>();
        }

        Map<String, Object> data =
                document.getData();

        if (data == null) {
            return new HashMap<>();
        }

        return new HashMap<>(data);
    }

    // ---------------------------------------------------------
    // Save user profile
    //
    // Uses merge so that other fields such as theme
    // preferences are not accidentally overwritten later.
    //
    // Firestore:
    //
    // users/{uid}
    // ---------------------------------------------------------

    public boolean saveProfile(
            String uid,
            Map<String, Object> data
    ) throws Exception {

        validateUid(uid);

        if (data == null) {
            throw new IllegalArgumentException(
                    "Profile data cannot be null."
            );
        }

        db.collection(USERS_COLLECTION)
                .document(uid)
                .set(
                        data,
                        com.google.cloud.firestore.SetOptions.merge()
                )
                .get();

        System.out.println(
                "User profile saved: " + uid
        );

        return true;
    }

    // ---------------------------------------------------------
    // Validate UID
    // ---------------------------------------------------------

    private void validateUid(
            String uid
    ) {

        if (uid == null ||
                uid.isBlank()) {

            throw new IllegalArgumentException(
                    "User UID is required."
            );
        }
    }

    public boolean deleteProfile(
            String uid
    ) throws Exception {

        validateUid(uid);

        db.collection(USERS_COLLECTION)
                .document(uid)
                .delete()
                .get();

        System.out.println(
                "User profile deleted: " + uid
        );

        return true;
    }



}