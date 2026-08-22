package com.file_handlers.model;

public class UserSession {
    private static UserSession instance;
    
    private String idToken;
    private String email;
    private String displayName;
    private boolean isAdmin; // Added admin flag

    // Updated constructor to accept 4 parameters
    private UserSession(String idToken, String email, String displayName, boolean isAdmin) {
        this.idToken = idToken;
        this.email = email;
        this.displayName = displayName;
        this.isAdmin = isAdmin;
    }

    // Updated setInstance method accepting 4 parameters
    public static void setInstance(String idToken, String email, String displayName, boolean isAdmin) {
        if (instance == null) {
            instance = new UserSession(idToken, email, displayName, isAdmin);
        } else {
            instance.idToken = idToken;
            instance.email = email;
            instance.displayName = displayName;
            instance.isAdmin = isAdmin;
        }
    }

    // Overloaded 3-parameter version for standard regular users (defaults isAdmin to false)
    public static void setInstance(String idToken, String email, String displayName) {
        setInstance(idToken, email, displayName, false);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public static void clearSession() {
        instance = null;
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
    
    public boolean isAdmin() { 
        return isAdmin; 
    }
    
    public static boolean isLoggedIn() {
        return instance != null && instance.idToken != null;
    }
}