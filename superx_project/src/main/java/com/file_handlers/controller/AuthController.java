package com.file_handlers.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.file_handlers.model.UserSession;
import com.google.firebase.auth.FirebaseAuth;

public class AuthController {

    private static final String API_KEY =
            "AIzaSyBzqJUI39goTF-9Mz14gbxsxKYs-pAHVlY";

    // ---------------------------------------------------------
    // Sign Up
    // ---------------------------------------------------------

    public String signUpAndGetToken(
            String email,
            String password,
            String fullName,
            String bio
    ) {
        try {
            JSONObject payload =
                    new JSONObject()
                            .put("email", email.trim())
                            .put("password", password)
                            .put("returnSecureToken", true);

            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());

                String uid = json.optString("localId", null);
                String idToken = json.optString("idToken", null);
                String trimmedEmail = email.trim();
                String displayName = (fullName != null && !fullName.isBlank()) ? fullName.trim() : "User";

                if (uid != null && idToken != null) {
                    createUserInFirestore(uid, trimmedEmail, idToken, fullName, bio);

                    // Initialize session so the app recognizes the user as logged in immediately after sign up
                    UserSession.setInstance(
                            uid,
                            idToken,
                            trimmedEmail,
                            displayName,
                            false
                    );
                }

                return idToken;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------------------------------------------------
    // Sign In
    // ---------------------------------------------------------

    public boolean signIn(
            String email,
            String password
    ) {

        try {

            String trimmedEmail =
                    email.trim();

            // -------------------------------------------------
            // Block admin accounts from user portal
            // -------------------------------------------------

            boolean isAdminEmail =
                    trimmedEmail.equalsIgnoreCase(
                            "sai12@gmail.com"
                    )
                            ||
                            trimmedEmail.equalsIgnoreCase(
                                    "xavierwagh@gmail.com"
                            )
                            ||
                            trimmedEmail.equalsIgnoreCase(
                                    "vaishnavi@gmail.com"
                            )||
                            trimmedEmail.equalsIgnoreCase(
                            "pratiksha@gmail.com"
                    )
                            ||
                            trimmedEmail.equalsIgnoreCase(
                                    "ananta22@gmail.com"
                            )
                            ||
                            trimmedEmail.equalsIgnoreCase(
                                    "mohite@gmail.com"
                            );

            if (isAdminEmail) {

                System.out.println(
                        "Access denied: use Admin Portal."
                );

                return false;
            }

            // -------------------------------------------------
            // Login request
            // -------------------------------------------------

            JSONObject payload =
                    new JSONObject()
                            .put(
                                    "email",
                                    trimmedEmail
                            )
                            .put(
                                    "password",
                                    password
                            )
                            .put(
                                    "returnSecureToken",
                                    true
                            );

            HttpClient client =
                    HttpClient.newHttpClient();

            URI uri =
                    URI.create(
                            "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                                    + API_KEY
                    );

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    payload.toString()
                                            )
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            // -------------------------------------------------
            // Successful login
            // -------------------------------------------------

            if (response.statusCode() == 200) {

                JSONObject json =
                        new JSONObject(
                                response.body()
                        );

                String uid =
                        json.optString(
                                "localId",
                                null
                        );

                String idToken =
                        json.optString(
                                "idToken",
                                null
                        );

                String displayName =
                        json.optString(
                                "displayName",
                                "User"
                        );

                if (displayName == null ||
                        displayName.trim().isEmpty()) {

                    displayName = "User";
                }

                // -------------------------------------------------
                // Ensure existing users populate in Firestore on login
                // -------------------------------------------------
                if (uid != null && idToken != null) {
                    checkAndCreateUserInFirestore(uid, trimmedEmail, idToken, displayName);
                }

                // -------------------------------------------------
                // Store COMPLETE authenticated session
                // -------------------------------------------------

                UserSession.setInstance(
                        uid,
                        idToken,
                        trimmedEmail,
                        displayName,
                        false
                );

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public void fetchAllUsersFromFirestore(String idToken) {
    try {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(
            "https://firestore.googleapis.com/v1/projects/onespace-cb2a1/databases/(default)/documents/users"
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer " + idToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            if (jsonResponse.has("documents")) {
                JSONArray documents = jsonResponse.getJSONArray("documents");
                
                for (int i = 0; i < documents.length(); i++) {
                    JSONObject doc = documents.getJSONObject(i);
                    String namePath = doc.getString("name"); // Contains the full document path including UID
                    String uid = namePath.substring(namePath.lastIndexOf("/") + 1);
                    
                    JSONObject fields = doc.getJSONObject("fields");
                    String email = fields.has("email") ? fields.getJSONObject("email").getString("stringValue") : "";
                    String fullName = fields.has("fullName") ? fields.getJSONObject("fullName").getString("stringValue") : "";
                    
                    System.out.println("UID: " + uid + " | Email: " + email + " | Name: " + fullName);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // ---------------------------------------------------------
    // Helper: Check if user document exists in Firestore
    // ---------------------------------------------------------

    private void checkAndCreateUserInFirestore(String uid, String email, String idToken, String fullName) {
        try {
            String defaultUsername = "@" + email.substring(0, email.indexOf("@"));

            JSONObject fields = new JSONObject();
            fields.put("email", new JSONObject().put("stringValue", email));
            fields.put("fullName", new JSONObject().put("stringValue", fullName != null ? fullName : "User"));
            fields.put("username", new JSONObject().put("stringValue", defaultUsername));
            fields.put("bio", new JSONObject().put("stringValue", "OneSpace user"));

            JSONObject firestorePayload = new JSONObject();
            firestorePayload.put("fields", fields);

            HttpClient client = HttpClient.newHttpClient();
            // Using PATCH with updateMask forces Firestore to overwrite or add these fields for existing users
            URI uri = URI.create(
                    "https://firestore.googleapis.com/v1/projects/onespace-cb2a1/databases/(default)/documents/users/" + uid
                    + "?updateMask.fieldPaths=email&updateMask.fieldPaths=fullName&updateMask.fieldPaths=username&updateMask.fieldPaths=bio"
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + idToken)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(firestorePayload.toString()))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // Helper: Create User Document in Firestore via REST API
    // ---------------------------------------------------------

    private void createUserInFirestore(String uid, String email, String idToken, String fullName, String bio) {
        try {
            String defaultUsername = "@" + email.substring(0, email.indexOf("@"));

            JSONObject fields = new JSONObject();
            fields.put("email", new JSONObject().put("stringValue", email));
            fields.put("fullName", new JSONObject().put("stringValue", fullName != null ? fullName : "User"));
            fields.put("username", new JSONObject().put("stringValue", defaultUsername));
            fields.put("bio", new JSONObject().put("stringValue", bio != null && !bio.isBlank() ? bio : "OneSpace user"));

            JSONObject firestorePayload = new JSONObject();
            firestorePayload.put("fields", fields);

            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create(
                    "https://firestore.googleapis.com/v1/projects/onespace-cb2a1/databases/(default)/documents/users?documentId=" + uid
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + idToken)
                    .POST(HttpRequest.BodyPublishers.ofString(firestorePayload.toString()))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // Password Reset
    // ---------------------------------------------------------

   // ---------------------------------------------------------
    // Password Reset
    // ---------------------------------------------------------

    public String sendPasswordResetEmail(String email) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Send password reset email directly via Firebase Auth REST API
            JSONObject resetPayload = new JSONObject()
                    .put("requestType", "PASSWORD_RESET")
                    .put("email", email.trim());

            URI resetUri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=" + API_KEY
            );

            HttpRequest resetRequest = HttpRequest.newBuilder()
                    .uri(resetUri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(resetPayload.toString()))
                    .build();

            HttpResponse<String> resetResponse = client.send(
                    resetRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (resetResponse.statusCode() == 200) {
                return "SUCCESS";
            } else {
                // If Firebase returns an error (e.g., email not found)
                return "NOT_REGISTERED";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ERROR";
    }
    // ---------------------------------------------------------
    // Update Profile
    // ---------------------------------------------------------

    public boolean updateProfile(
            String idToken,
            String displayName
    ) {

        try {

            JSONObject payload =
                    new JSONObject()
                            .put(
                                    "idToken",
                                    idToken
                            )
                            .put(
                                    "displayName",
                                    displayName
                            )
                            .put(
                                    "returnSecureToken",
                                    true
                            );

            HttpClient client =
                    HttpClient.newHttpClient();

            URI uri =
                    URI.create(
                            "https://identitytoolkit.googleapis.com/v1/accounts:update?key="
                                    + API_KEY
                    );

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    payload.toString()
                                            )
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            return response.statusCode() == 200;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // ---------------------------------------------------------
    // Change Password
    // ---------------------------------------------------------

    public boolean changePassword(
            String email,
            String currentPassword,
            String newPassword
    ) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            JSONObject loginPayload = new JSONObject()
                    .put("email", email.trim())
                    .put("password", currentPassword)
                    .put("returnSecureToken", true);

            URI loginUri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                            + API_KEY
            );

            HttpRequest loginRequest = HttpRequest.newBuilder()
                    .uri(loginUri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(loginPayload.toString()))
                    .build();

            HttpResponse<String> loginResponse = client.send(
                    loginRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (loginResponse.statusCode() != 200)
                return false;

            JSONObject loginJson = new JSONObject(loginResponse.body());
            String freshToken = loginJson.optString("idToken", null);

            if (freshToken == null || freshToken.isBlank())
                return false;

            JSONObject updatePayload = new JSONObject()
                    .put("idToken", freshToken)
                    .put("password", newPassword)
                    .put("returnSecureToken", true);

            URI updateUri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:update?key="
                            + API_KEY
            );

            HttpRequest updateRequest = HttpRequest.newBuilder()
                    .uri(updateUri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(updatePayload.toString()))
                    .build();

            HttpResponse<String> updateResponse = client.send(
                    updateRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            return updateResponse.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteAuthUser(String uid) {
    try {
        FirebaseAuth.getInstance().deleteUser(uid);
        System.out.println("Firebase Auth user deleted: " + uid);
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public boolean deleteAccount(
            String idToken
    ) {
        try {
            JSONObject payload =
                    new JSONObject()
                            .put("idToken", idToken);

            HttpClient client =
                    HttpClient.newHttpClient();

            URI uri =
                    URI.create(
                            "https://identitytoolkit.googleapis.com/v1/accounts:delete?key="
                                    + API_KEY
                    );

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    payload.toString()
                                            )
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            return response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    
}