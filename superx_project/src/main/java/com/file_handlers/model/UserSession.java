package com.file_handlers.model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONObject;

public class UserSession {

    
    private static UserSession instance;

    private final String uid;
    private final String idToken;
    private final String email;
    private String displayName;
    private final boolean admin;

   

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

  

    public static UserSession getInstance() {

        return instance;
    }


    public String getUid() {

        return uid;
    }

   

    public String getIdToken() {

        return idToken;
    }

    

    public String getEmail() {

        return email;
    }

    
    public String getDisplayName() {

        return displayName;
    }

    public void setDisplayName(
            String displayName
    ) {

        this.displayName = displayName;
    }

    

    public boolean isAdmin() {

        return admin;
    }

   
    public static boolean isLoggedIn() {

        return instance != null
                && instance.uid != null
                && !instance.uid.isBlank();
    }

   
    public static void clearSession() {

        instance = null;
    }

    

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



    public void setEmail(String emailVal) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEmail'");
    }
}