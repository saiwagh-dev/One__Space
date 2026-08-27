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

    private static final String SERVICE_ACCOUNT_ENV =
            "FIREBASE_SERVICE_ACCOUNT";

    private static final String CREDENTIALS_FILE =
            "onespace_firebase_credentials.json";

    // =========================================================
    // FIREBASE INITIALIZATION
    // =========================================================

    /*
     * Firebase is initialized automatically when this class
     * is first loaded.
     */
    static {
        initialize();
    }

    /**
     * Initializes the Firebase DEFAULT application.
     *
     * If Firebase has already been initialized, nothing happens.
     */
    private static synchronized void initialize() {

        try {

            // -------------------------------------------------
            // Prevent duplicate Firebase initialization
            // -------------------------------------------------

            if (!FirebaseApp.getApps().isEmpty()) {
                return;
            }

            // -------------------------------------------------
            // Load Firebase credentials
            // -------------------------------------------------

            GoogleCredentials credentials =
                    loadCredentials();

            // -------------------------------------------------
            // Build Firebase configuration
            // -------------------------------------------------

            FirebaseOptions options =
                    FirebaseOptions.builder()
                            .setCredentials(credentials)
                            .build();

            // -------------------------------------------------
            // Initialize Firebase DEFAULT app
            // -------------------------------------------------

            FirebaseApp.initializeApp(options);

            System.out.println(
                    "[FIREBASE] Initialized"
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "[FIREBASE] Initialization failed.",
                    e
            );
        }
    }

    // =========================================================
    // LOAD CREDENTIALS
    // =========================================================

    private static GoogleCredentials loadCredentials()
            throws Exception {

        // -----------------------------------------------------
        // OPTION 1
        // Environment variable
        // -----------------------------------------------------

        String credentialsPath =
                System.getenv(
                        SERVICE_ACCOUNT_ENV
                );

        if (credentialsPath != null &&
                !credentialsPath.isBlank()) {

            Path credentialsFile =
                    Path.of(
                            credentialsPath
                    );

            if (!Files.exists(credentialsFile)) {

                throw new IllegalStateException(
                        "Firebase credentials file not found: "
                                + credentialsFile
                );
            }

            try (InputStream input =
                         new FileInputStream(
                                 credentialsFile.toFile()
                         )) {

                return GoogleCredentials.fromStream(
                        input
                );
            }
        }

        // -----------------------------------------------------
        // OPTION 2
        // src/main/resources
        // -----------------------------------------------------

        InputStream input =
                FirebaseConfig.class
                        .getClassLoader()
                        .getResourceAsStream(
                                CREDENTIALS_FILE
                        );

        if (input != null) {

            try (InputStream stream = input) {

                return GoogleCredentials.fromStream(
                        stream
                );
            }
        }

        // -----------------------------------------------------
        // No credentials found
        // -----------------------------------------------------

        throw new IllegalStateException(
                "Firebase credentials not found.\n"
                        + "Place "
                        + CREDENTIALS_FILE
                        + " inside src/main/resources "
                        + "or set "
                        + SERVICE_ACCOUNT_ENV
                        + "."
        );
    }

    // =========================================================
    // FIRESTORE
    // =========================================================

    /**
     * Returns the Firestore instance associated with the
     * Firebase DEFAULT application.
     *
     * FileDAO and other database classes should use this
     * method instead of calling FirestoreClient directly.
     */
    public static Firestore getFirestore() {

        /*
         * Accessing FirebaseConfig guarantees that the static
         * initialization block has already executed.
         */
        initialize();

        return FirestoreClient.getFirestore();
    }

    // =========================================================
    // FIREBASE APP
    // =========================================================

    /**
     * Returns the Firebase DEFAULT application.
     *
     * Useful when another Firebase service needs the
     * initialized application.
     */
    public static FirebaseApp getFirebaseApp() {

        initialize();

        return FirebaseApp.getInstance();
    }

    // =========================================================
    // PRIVATE CONSTRUCTOR
    // =========================================================

    /*
     * Prevent creating FirebaseConfig objects.
     *
     * This class only provides static Firebase configuration
     * methods.
     */
    private FirebaseConfig() {
    }
}