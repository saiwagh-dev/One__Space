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
import com.google.cloud.firestore.WriteBatch;

public class SpaceDAO {
    private static final String USERS_COLLECTION = "users";
    private static final String SPACES_COLLECTION = "spaces";
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
            if (space.getName() != null && space.getName().trim().toLowerCase(Locale.ROOT).equals(normalized)) return true;
        }
        return false;
    }

    private boolean nameIsTakenByAnotherSpace(String uid, String name, String currentSpaceId) throws Exception {
        if (name == null || name.isBlank()) return true;
        String normalized = name.trim().toLowerCase(Locale.ROOT);
        if (RESERVED_NAMES.contains(normalized)) return true;
        for (SpaceData space : getUserSpaces(uid)) {
            if (space.getSpaceId() != null && space.getSpaceId().equals(currentSpaceId)) continue;
            if (space.getName() != null && space.getName().trim().toLowerCase(Locale.ROOT).equals(normalized)) return true;
        }
        return false;
    }

    public SpaceData createSpace(String uid, String name, String description, List<String> tags) throws Exception {
        validateUid(uid);
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Space name is required.");
        String trimmedName = name.trim();
        if (trimmedName.length() > 40) throw new IllegalArgumentException("Space name is too long (max 40 characters).");
        if (nameIsTaken(uid, trimmedName)) throw new IllegalStateException("A Space named \"" + trimmedName + "\" already exists.");
        CollectionReference collection = getUserSpacesCollection(uid);
        String baseId = slugify(trimmedName);
        String candidateId = baseId;
        int suffix = 1;
        while (collection.document(candidateId).get().get().exists()) candidateId = baseId + "-" + (++suffix);
        SpaceData space = new SpaceData(candidateId, trimmedName, description == null ? "" : description.trim(), tags == null ? new ArrayList<>() : tags, uid);
        space.setCreatedAt(Timestamp.now());
        collection.document(candidateId).set(space.toMap()).get();
        return space;
    }

    public SpaceData updateSpace(String uid, String spaceId, String name, String description, List<String> tags) throws Exception {
        validateUid(uid);
        if (spaceId == null || spaceId.isBlank()) throw new IllegalArgumentException("Space ID is required.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Space name is required.");
        String trimmedName = name.trim();
        if (trimmedName.length() > 40) throw new IllegalArgumentException("Space name is too long (max 40 characters).");
        if (RESERVED_NAMES.contains(trimmedName.toLowerCase(Locale.ROOT))) throw new IllegalStateException("That name is reserved for a built-in Space.");
        if (nameIsTakenByAnotherSpace(uid, trimmedName, spaceId)) throw new IllegalStateException("A Space named \"" + trimmedName + "\" already exists.");

        var document = getUserSpacesCollection(uid).document(spaceId);
        DocumentSnapshot snapshot = document.get().get();
        if (!snapshot.exists()) throw new IllegalStateException("Space not found.");

        SpaceData existing = snapshot.toObject(SpaceData.class);
        if (existing == null) throw new IllegalStateException("Unable to read the Space.");
        existing.setName(trimmedName);
        existing.setDescription(description == null ? "" : description.trim());
        existing.setTags(tags == null ? new ArrayList<>() : new ArrayList<>(tags));
        document.set(existing.toMap()).get();
        return existing;
    }

    public void deleteSpace(String uid, String spaceId) throws Exception {
        validateUid(uid);
        if (spaceId == null || spaceId.isBlank()) throw new IllegalArgumentException("Space ID is required.");
        if (RESERVED_NAMES.contains(spaceId.toLowerCase(Locale.ROOT))) throw new IllegalStateException("Built-in Spaces cannot be deleted.");
        getUserSpacesCollection(uid).document(spaceId).delete().get();
    }

    public void deleteSpaceAndMoveFiles(String uid, String spaceId, String targetSpaceId) throws Exception {
        validateUid(uid);
        if (spaceId == null || spaceId.isBlank()) throw new IllegalArgumentException("Space ID is required.");
        if (targetSpaceId == null || targetSpaceId.isBlank()) throw new IllegalArgumentException("Target Space ID is required.");
        if (RESERVED_NAMES.contains(spaceId.toLowerCase(Locale.ROOT))) throw new IllegalStateException("Built-in Spaces cannot be deleted.");

        CollectionReference files = getFirestore().collection(USERS_COLLECTION).document(uid).collection("files");
        QuerySnapshot snapshot = files.whereEqualTo("spaceId", spaceId).get().get();
        WriteBatch batch = getFirestore().batch();
        for (DocumentSnapshot document : snapshot.getDocuments()) {
            batch.update(document.getReference(), "spaceId", targetSpaceId);
        }
        batch.delete(getUserSpacesCollection(uid).document(spaceId));
        batch.commit().get();
    }

    private String slugify(String name) {
        String slug = name.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-+|-+$)", "");
        return slug.isBlank() ? "space-" + System.currentTimeMillis() : slug;
    }

    private void validateUid(String uid) {
        if (uid == null || uid.isBlank()) throw new IllegalArgumentException("User UID is required.");
    }
}
