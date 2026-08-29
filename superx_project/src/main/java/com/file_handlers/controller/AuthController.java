package com.file_handlers.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import com.file_handlers.model.UserSession;

public class AuthController {

    private static final String API_KEY =
            "AIzaSyBzqJUI39goTF-9Mz14gbxsxKYs-pAHVlY";

    // ---------------------------------------------------------
    // Sign Up
    // ---------------------------------------------------------

    public String signUpAndGetToken(
            String email,
            String password
    ) {

        try {

            JSONObject payload =
                    new JSONObject()
                            .put("email", email.trim())
                            .put("password", password)
                            .put("returnSecureToken", true);

            HttpClient client =
                    HttpClient.newHttpClient();

            URI uri =
                    URI.create(
                            "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="
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

            if (response.statusCode() == 200) {

                JSONObject json =
                        new JSONObject(response.body());

                return json.optString(
                        "idToken",
                        null
                );
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

    // ---------------------------------------------------------
    // Password Reset
    // ---------------------------------------------------------

    public String sendPasswordResetEmail(
            String email
    ) {

        try {

            HttpClient client =
                    HttpClient.newHttpClient();

            // -------------------------------------------------
            // Check account
            // -------------------------------------------------

            JSONObject lookupPayload =
                    new JSONObject()
                            .put(
                                    "email",
                                    new String[]{
                                            email.trim()
                                    }
                            );

            URI lookupUri =
                    URI.create(
                            "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key="
                                    + API_KEY
                    );

            HttpRequest lookupRequest =
                    HttpRequest.newBuilder()
                            .uri(lookupUri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    lookupPayload.toString()
                                            )
                            )
                            .build();

            HttpResponse<String> lookupResponse =
                    client.send(
                            lookupRequest,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (lookupResponse.statusCode() != 200 ||
                    !lookupResponse.body()
                            .contains("users")) {

                return "NOT_REGISTERED";
            }

            // -------------------------------------------------
            // Send reset email
            // -------------------------------------------------

            JSONObject resetPayload =
                    new JSONObject()
                            .put(
                                    "requestType",
                                    "PASSWORD_RESET"
                            )
                            .put(
                                    "email",
                                    email.trim()
                            );

            URI resetUri =
                    URI.create(
                            "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key="
                                    + API_KEY
                    );

            HttpRequest resetRequest =
                    HttpRequest.newBuilder()
                            .uri(resetUri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    resetPayload.toString()
                                            )
                            )
                            .build();

            HttpResponse<String> resetResponse =
                    client.send(
                            resetRequest,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (resetResponse.statusCode() == 200) {
                return "SUCCESS";
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
                HttpClient client=HttpClient.newHttpClient();

                JSONObject loginPayload=new JSONObject()
                        .put("email",email.trim())
                        .put("password",currentPassword)
                        .put("returnSecureToken",true);

                URI loginUri=URI.create(
                        "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                                +API_KEY
                );

                HttpRequest loginRequest=HttpRequest.newBuilder()
                        .uri(loginUri)
                        .header("Content-Type","application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(loginPayload.toString()))
                        .build();

                HttpResponse<String> loginResponse=client.send(
                        loginRequest,
                        HttpResponse.BodyHandlers.ofString()
                );

                if(loginResponse.statusCode()!=200)
                return false;

                JSONObject loginJson=new JSONObject(loginResponse.body());
                String freshToken=loginJson.optString("idToken",null);

                if(freshToken==null||freshToken.isBlank())
                return false;

                JSONObject updatePayload=new JSONObject()
                        .put("idToken",freshToken)
                        .put("password",newPassword)
                        .put("returnSecureToken",true);

                URI updateUri=URI.create(
                        "https://identitytoolkit.googleapis.com/v1/accounts:update?key="
                                +API_KEY
                );

                HttpRequest updateRequest=HttpRequest.newBuilder()
                        .uri(updateUri)
                        .header("Content-Type","application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(updatePayload.toString()))
                        .build();

                HttpResponse<String> updateResponse=client.send(
                        updateRequest,
                        HttpResponse.BodyHandlers.ofString()
                );

                return updateResponse.statusCode()==200;

        }catch(Exception e){
                e.printStackTrace();
        }

        return false;
        }
}