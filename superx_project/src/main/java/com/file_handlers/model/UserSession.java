package com.file_handlers.model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONObject;

public class UserSession {

    // ---------------------------------------------------------
    // Current session
    // ---------------------------------------------------------

    private static UserSession instance;

    private final String uid;
    private final String idToken;
    private final String email;
    private String displayName;
    private final boolean admin;

    // ---------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------

    private UserSession(
            String uid,
            String idToken,
            String email,
            String displayName,
            boolean admin
    ) {

        this.uid = uid;
        this.idToken = idToken;
        this.email = email;
        this.displayName = displayName;
        this.admin = admin;
    }

    // ---------------------------------------------------------
    // NEW SESSION METHOD
    //
    // Use this when UID is already available.
    // ---------------------------------------------------------

    public static void setInstance(
            String uid,
            String idToken,
            String email,
            String displayName,
            boolean admin
    ) {

        instance = new UserSession(
                uid,
                idToken,
                email,
                displayName,
                admin
        );
    }

    // ---------------------------------------------------------
    // BACKWARD-COMPATIBLE SESSION METHOD
    //
    // Existing AdminAuthController and UserSignupPage
    // can continue using this method.
    //
    // UID is extracted from the Firebase ID token.
    // ---------------------------------------------------------

    public static void setInstance(
            String idToken,
            String email,
            String displayName,
            boolean admin
    ) {

        String uid = extractUidFromToken(idToken);

        instance = new UserSession(
                uid,
                idToken,
                email,
                displayName,
                admin
        );
    }

    // ---------------------------------------------------------
    // Get current session
    // ---------------------------------------------------------

    public static UserSession getInstance() {

        return instance;
    }

    // ---------------------------------------------------------
    // UID
    // ---------------------------------------------------------

    public String getUid() {

        return uid;
    }

    // ---------------------------------------------------------
    // ID Token
    // ---------------------------------------------------------

    public String getIdToken() {

        return idToken;
    }

    // ---------------------------------------------------------
    // Email
    // ---------------------------------------------------------

    public String getEmail() {

        return email;
    }

    // ---------------------------------------------------------
    // Display Name
    // ---------------------------------------------------------

    public String getDisplayName() {

        return displayName;
    }

    public void setDisplayName(
            String displayName
    ) {

        this.displayName = displayName;
    }

    // ---------------------------------------------------------
    // Admin
    // ---------------------------------------------------------

    public boolean isAdmin() {

        return admin;
    }

    // ---------------------------------------------------------
    // Login check
    // ---------------------------------------------------------

    public static boolean isLoggedIn() {

        return instance != null
                && instance.uid != null
                && !instance.uid.isBlank();
    }

    // ---------------------------------------------------------
    // Logout
    // ---------------------------------------------------------

    public static void clearSession() {

        instance = null;
    }

    // ---------------------------------------------------------
    // Extract Firebase UID from ID token
    // ---------------------------------------------------------

    private static String extractUidFromToken(
            String idToken
    ) {

        if (idToken == null ||
                idToken.isBlank()) {

            return null;
        }

        try {

            String[] parts =
                    idToken.split("\\.");

            if (parts.length < 2) {

                return null;
            }

            String payload =
                    parts[1];

            byte[] decoded =
                    Base64.getUrlDecoder()
                            .decode(payload);

            String json =
                    new String(
                            decoded,
                            StandardCharsets.UTF_8
                    );

            JSONObject payloadJson =
                    new JSONObject(json);

            // Firebase UID is normally available
            // as "user_id".

            String uid =
                    payloadJson.optString(
                            "user_id",
                            null
                    );

            if (uid == null ||
                    uid.isBlank()) {

                // "sub" is also the Firebase
                // authenticated user's UID.

                uid =
                        payloadJson.optString(
                                "sub",
                                null
                        );
            }

            return uid;

        } catch (Exception e) {

            System.out.println(
                    "Unable to extract user UID."
            );

            return null;
        }
    }
}