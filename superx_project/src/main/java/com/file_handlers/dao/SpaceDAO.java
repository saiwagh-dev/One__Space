package com.file_handlers.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.file_handlers.config.FirebaseConfig;
import com.file_handlers.model.SpaceData;
import com.file_handlers.util.SpaceConstants;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class SpaceDAO {
    private static final String USERS_COLLECTION = "users";
    private static final String SPACES_COLLECTION = "spaces";

    // Names that collide with the fixed, built-in Spaces and can't be reused.
    private static final List<String> RESERVED_NAMES = List.of(
            SpaceConstants.PERSONAL,
            SpaceConstants.COLLEGE,
            SpaceConstants.OFFICE,
            SpaceConstants.FINANCE,
            SpaceConstants.ENTERTAINMENT,
            SpaceConstants.OTHER
    );

    private Firestore getFirestore() { return FirebaseConfig.getFirestore(); }

    private CollectionReference getUserSpacesCollection(String uid) {
        validateUid(uid);
        return getFirestore().collection(USERS_COLLECTION).document(uid).collection(SPACES_COLLECTION);
    }

    public List<SpaceData> getUserSpaces(String uid) throws Exception {
        validateUid(uid);

        QuerySnapshot snapshot = getUserSpacesCollection(uid).get().get();
        List<SpaceData> spaces = new ArrayList<>();

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            SpaceData space = doc.toObject(SpaceData.class);
            if (space != null) spaces.add(space);
        }

        return spaces;
    }

    public boolean nameIsTaken(String uid, String name) throws Exception {
        if (name == null || name.isBlank()) return true;

        String normalized = name.trim().toLowerCase(Locale.ROOT);

        if (RESERVED_NAMES.contains(normalized)) return true;

        for (SpaceData space : getUserSpaces(uid)) {
            if (space.getName() != null && space.getName().trim().toLowerCase(Locale.ROOT).equals(normalized)) {
                return true;
            }
        }

        return false;
    }

    public SpaceData createSpace(String uid, String name, String description, List<String> tags) throws Exception {
        validateUid(uid);

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Space name is required.");

        String trimmedName = name.trim();

        if (trimmedName.length() > 40)
            throw new IllegalArgumentException("Space name is too long (max 40 characters).");

        if (nameIsTaken(uid, trimmedName))
            throw new IllegalStateException("A Space named \"" + trimmedName + "\" already exists.");

        CollectionReference collection = getUserSpacesCollection(uid);

        // Slugify the name into an id, and disambiguate on collision.
        String baseId = slugify(trimmedName);
        String candidateId = baseId;
        int suffix = 1;

        while (collection.document(candidateId).get().get().exists()) {
            candidateId = baseId + "-" + (++suffix);
        }

        SpaceData space = new SpaceData(
                candidateId,
                trimmedName,
                description == null ? "" : description.trim(),
                tags == null ? new ArrayList<>() : tags,
                uid
        );
        space.setCreatedAt(Timestamp.now());

        collection.document(candidateId).set(space.toMap()).get();

        return space;
    }

    private String slugify(String name) {
        String slug = name.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");

        return slug.isBlank() ? "space-" + System.currentTimeMillis() : slug;
    }

    private void validateUid(String uid) {
        if (uid == null || uid.isBlank())
            throw new IllegalArgumentException("User UID is required.");
    }
}