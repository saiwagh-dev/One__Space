package com.file_handlers.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public final class FirebaseConfig {

    // =========================================================
    // CONSTANTS
    // =========================================================

    private static final String SERVICE_ACCOUNT_ENV = "FIREBASE_SERVICE_ACCOUNT";
    private static final String CREDENTIALS_FILE = "onespace_firebase_credentials.json";

    // =========================================================
    // FIREBASE INITIALIZATION
    // =========================================================

    static {
        try {
            initialize();
        } catch (Exception e) {
            System.err.println("[FIREBASE ERROR] Static initialization failed: " + e.getMessage());
            // Rethrow as ExceptionInInitializerError to surface the root cause clearly
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Initializes the Firebase DEFAULT application.
     * If Firebase has already been initialized, nothing happens.
     */
    private static synchronized void initialize() {
        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }

        try {
            GoogleCredentials credentials = loadCredentials();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("[FIREBASE] Initialized successfully.");

        } catch (Exception e) {
            throw new RuntimeException("[FIREBASE] Initialization failed: " + e.getMessage(), e);
        }
    }

    // =========================================================
    // LOAD CREDENTIALS
    // =========================================================

    private static GoogleCredentials loadCredentials() throws Exception {

        // -----------------------------------------------------
        // OPTION 1: Check Environment Variable
        // -----------------------------------------------------
        String credentialsPath = System.getenv(SERVICE_ACCOUNT_ENV);
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            Path credentialsFile = Path.of(credentialsPath);
            if (!Files.exists(credentialsFile)) {
                throw new IllegalStateException(
                        "Firebase credentials file not found at environment path: " + credentialsFile);
            }

            try (InputStream input = new FileInputStream(credentialsFile.toFile())) {
                return GoogleCredentials.fromStream(input);
            }
        }

        // -----------------------------------------------------
        // OPTION 2: Check src/main/resources (Standard Classloader)
        // -----------------------------------------------------
        InputStream input = FirebaseConfig.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE);

        // Fallback check with leading slash if standard lookup fails
        if (input == null) {
            input = FirebaseConfig.class.getResourceAsStream("/" + CREDENTIALS_FILE);
        }

        if (input != null) {
            try (InputStream stream = input) {
                return GoogleCredentials.fromStream(stream);
            }
        }

        // -----------------------------------------------------
        // ERROR: Credentials Missing Everywhere
        // -----------------------------------------------------
        throw new IllegalStateException(
                "Firebase credentials missing!\n" +
                        "-> Place '" + CREDENTIALS_FILE + "' inside your 'src/main/resources' folder, OR\n" +
                        "-> Set the '" + SERVICE_ACCOUNT_ENV
                        + "' environment variable to point to your JSON file path.");
    }

    // =========================================================
    // FIRESTORE & APP GETTERS
    // =========================================================

    public static Firestore getFirestore() {
        initialize();
        return FirestoreClient.getFirestore();
    }

    public static FirebaseApp getFirebaseApp() {
        initialize();
        return FirebaseApp.getInstance();
    }

    // =========================================================
    // PRIVATE CONSTRUCTOR
    // =========================================================

    private FirebaseConfig() {
        // Prevent instantiation
    }
}
