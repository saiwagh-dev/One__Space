package com.file_handlers.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

import com.file_handlers.model.UserSession;

public class AdminAuthController {

    private final String API_KEY = "AIzaSyBzqJUI39goTF-9Mz14gbxsxKYs-pAHVlY";

    // 1. Admin Sign Up - Returns idToken on success
    public String adminSignUpAndGetToken(String email, String password) {
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
            } else {
                JSONObject errorJson = new JSONObject(response.body());
                if (errorJson.has("error")) {
                    System.out.println("Admin Sign Up Error: " + errorJson.getJSONObject("error").getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 2. Admin Sign In - Authenticates and checks if email matches your admin credentials
    public boolean adminSignInAndSetSession(String email, String password) {
        try {
            String trimmedEmail = email.trim();

            // ALLOW MULTIPLE ADMINS: Check if email matches either authorized administrator
            boolean isAuthorizedAdmin = trimmedEmail.equalsIgnoreCase("sai12@gmail.com") || 
                                        trimmedEmail.equalsIgnoreCase("xavierwagh@gmail.com");

            if (!isAuthorizedAdmin) {
                System.out.println("Access Denied: Only authorized administrators can log in here.");
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

                // Save session with admin flag set to true
                UserSession.setInstance(idToken, trimmedEmail, "Administrator", true);
                return true;
            } else {
                JSONObject errorJson = new JSONObject(response.body());
                if (errorJson.has("error")) {
                    System.out.println("Admin Sign In Error: " + errorJson.getJSONObject("error").getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}