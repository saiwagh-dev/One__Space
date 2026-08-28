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

    private static final String SERVICE_ACCOUNT_ENV = "FIREBASE_SERVICE_ACCOUNT";
    
    // You can keep your preferred credentials file name or support both
    private static final String CREDENTIALS_FILE = "java26.json"; 

    static {
        initialize();
    }

    private static synchronized void initialize() {
        try {
            if (!FirebaseApp.getApps().isEmpty()) {
                return;
            }

            GoogleCredentials credentials = loadCredentials();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("[FIREBASE] Initialized successfully!");

        } catch (Exception e) {
            throw new RuntimeException("[FIREBASE] Initialization failed.", e);
        }
    }

    private static GoogleCredentials loadCredentials() throws Exception {
        // Option 1: Environment variable check
        String credentialsPath = System.getenv(SERVICE_ACCOUNT_ENV);
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            Path credentialsFile = Path.of(credentialsPath);
            if (Files.exists(credentialsFile)) {
                try (InputStream input = new FileInputStream(credentialsFile.toFile())) {
                    return GoogleCredentials.fromStream(input);
                }
            }
        }

        // Option 2: Load safely from src/main/resources using ClassLoader
        InputStream input = FirebaseConfig.class
                .getClassLoader()
                .getResourceAsStream(CREDENTIALS_FILE);

        if (input != null) {
            try (InputStream stream = input) {
                return GoogleCredentials.fromStream(stream);
            }
        }

        throw new IllegalStateException(
                "Firebase credentials not found. Place " + CREDENTIALS_FILE + 
                " inside src/main/resources or set " + SERVICE_ACCOUNT_ENV + "."
        );
    }

    /**
     * Main method used across your views (SharedSpacePage, CollaborationPage, etc.)
     */
    public static Firestore getFireStore() {
        initialize();
        return FirestoreClient.getFirestore();
    }

    // Optional alias if any class calls getFirestore() with a lowercase 't'
    public static Firestore getFirestore() {
        return getFireStore();
    }

    public static FirebaseApp getFirebaseApp() {
        initialize();
        return FirebaseApp.getInstance();
    }

    private FirebaseConfig() {
    }
}
