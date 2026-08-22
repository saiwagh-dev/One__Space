package com.file_handlers.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

import com.file_handlers.model.UserSession;

public class AuthController {

    private final String API_KEY = "AIzaSyBzqJUI39goTF-9Mz14gbxsxKYs-pAHVlY";

    public String signUpAndGetToken(String email, String password) {
        try {
            JSONObject payload = new JSONObject()
                    .put("email", email)
                    .put("password", password)
                    .put("returnSecureToken", true);

            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                return jsonResponse.optString("idToken", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Regular User Sign In with Admin Block Check & Display Name Extraction
    public boolean signIn(String email, String password) {
        try {
            String trimmedEmail = email.trim();

            // BLOCK ALL ADMINS: Prevent any administrator credentials from logging into the user portal
            boolean isAdminEmail = trimmedEmail.equalsIgnoreCase("sai12@gmail.com") || 
                                   trimmedEmail.equalsIgnoreCase("xavierwagh@gmail.com");

            if (isAdminEmail) {
                System.out.println("Access Denied: Admins must log in through the Admin Portal.");
                return false; 
            }

            JSONObject payload = new JSONObject()
                    .put("email", trimmedEmail)
                    .put("password", password)
                    .put("returnSecureToken", true);

            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                String idToken = jsonResponse.optString("idToken", null);
                
                // Extract display name from Firebase response, fallback to "User" if empty
                String displayName = jsonResponse.optString("displayName", "User");
                if (displayName == null || displayName.trim().isEmpty()) {
                    displayName = "User";
                }
                
                // Save regular user session with the fetched display name
                UserSession.setInstance(idToken, trimmedEmail, displayName, false);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String sendPasswordResetEmail(String email) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Step 1: Check if the user email actually exists using the lookup endpoint
            JSONObject lookupPayload = new JSONObject().put("email", new String[]{email});
            URI lookupUri = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=" + API_KEY);

            HttpRequest lookupRequest = HttpRequest.newBuilder()
                    .uri(lookupUri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(lookupPayload.toString()))
                    .build();

            HttpResponse<String> lookupResponse = client.send(lookupRequest, HttpResponse.BodyHandlers.ofString());

            // If the lookup response does not contain users, the email is not registered
            if (lookupResponse.statusCode() != 200 || !lookupResponse.body().contains("users")) {
                return "NOT_REGISTERED";
            }

            // Step 2: If user exists, proceed to send the password reset email
            JSONObject resetPayload = new JSONObject()
                    .put("requestType", "PASSWORD_RESET")
                    .put("email", email);

            URI resetUri = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=" + API_KEY);

            HttpRequest resetRequest = HttpRequest.newBuilder()
                    .uri(resetUri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(resetPayload.toString()))
                    .build();

            HttpResponse<String> resetResponse = client.send(resetRequest, HttpResponse.BodyHandlers.ofString());

            if (resetResponse.statusCode() == 200) {
                return "SUCCESS";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public boolean updateProfile(String idToken, String displayName) {
        try {
            JSONObject payload = new JSONObject()
                    .put("idToken", idToken)
                    .put("displayName", displayName)
                    .put("returnSecureToken", true);

            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:update?key=" + API_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}