package com.file_handlers.dao;
 
import com.file_handlers.config.FirebaseConfig;
import com.file_handlers.model.Workspace;
import com.google.cloud.firestore.*;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
 
/**
 * Single source of truth for all "workspaces" collection reads/writes.
 * Views and Controllers should never call FirebaseConfig.getFirestore() directly for
 * workspace data — go through this class so there is exactly one place to fix bugs
 * or change the schema.
 */
public class WorkspaceDAO {
 
    private static final String COLLECTION = "workspaces";
    private final Firestore db = FirebaseConfig.getFirestore();
 
    /** Firestore doc id derived from the workspace name (matches what CollaborationPage/SharedSpacePage already use). */
    public String docIdFor(String workspaceName) {
        return workspaceName.trim().replaceAll("\\s+", "_");
    }
 
    /**
     * Creates a workspace doc + owner as first member + any additional invited members.
     * Does NOT touch files — call FileDao separately per uploaded file (upload to Cloudinary first!).
     */
    public void createWorkspace(String name, String ownerId, String ownerName, String ownerEmail,
                                 List<String> memberEmails,
                                 Consumer<String> onSuccess, Consumer<Exception> onError) {
        try {
            String docId = docIdFor(name);
 
            Map<String, Object> data = new HashMap<>();
            data.put("name", name.trim());
            data.put("ownerId", ownerId);
            data.put("memberCount", memberEmails.size() + 1); // + owner
            data.put("fileCount", 0);
            data.put("createdAt", FieldValue.serverTimestamp());
 
            DocumentReference wsRef = db.collection(COLLECTION).document(docId);
            wsRef.set(data).get();
 
            Map<String, Object> ownerData = new HashMap<>();
            ownerData.put("name", ownerName);
            ownerData.put("email", ownerEmail);
            ownerData.put("role", "Owner");
            ownerData.put("status", "active");
            wsRef.collection("members").document(sanitize(ownerEmail)).set(ownerData).get();
 
            for (String email : memberEmails) {
                if (email == null || email.isBlank()) continue;
                String trimmed = email.trim();
                Map<String, Object> memberData = new HashMap<>();
                memberData.put("name", trimmed.split("@")[0]);
                memberData.put("email", trimmed);
                memberData.put("role", "Viewer");
                memberData.put("status", "pending"); // flip to "active" if you add an accept flow
                wsRef.collection("members").document(sanitize(trimmed)).set(memberData).get();
            }
 
            if (onSuccess != null) onSuccess.accept(docId);
        } catch (Exception e) {
            if (onError != null) onError.accept(e);
        }
    }
 
   public void renameWorkspace(String workspaceDocId, String newName, Runnable onSuccess, Consumer<Exception> onError) {
    new Thread(() -> {
        try {
            com.google.cloud.firestore.Firestore db = com.file_handlers.config.FirebaseConfig.getFirestore();
            
            java.util.Map<String, Object> updates = new java.util.HashMap<>();
            updates.put("name", newName);
            updates.put("spaceName", newName);

            db.collection("workspaces").document(workspaceDocId).update(updates).get();
            
            if (onSuccess != null) {
                onSuccess.run();
            }
        } catch (Exception e) {
            if (onError != null) {
                onError.accept(e);
            }
        }
    }).start();
}
 
    public void deleteWorkspace(String workspaceDocId, Runnable onSuccess, Consumer<Exception> onError) {
        try {
            DocumentReference wsRef = db.collection(COLLECTION).document(workspaceDocId);
            for (QueryDocumentSnapshot doc : wsRef.collection("members").get().get().getDocuments()) {
                doc.getReference().delete();
            }
            for (QueryDocumentSnapshot doc : wsRef.collection("files").get().get().getDocuments()) {
                doc.getReference().delete();
            }
            wsRef.delete().get();
            if (onSuccess != null) onSuccess.run();
        } catch (Exception e) {
            if (onError != null) onError.accept(e);
        }
    }
 
    public List<QueryDocumentSnapshot> getAllWorkspaces() throws ExecutionException, InterruptedException {
        return db.collection(COLLECTION).get().get().getDocuments();
    }
 
    public DocumentSnapshot getWorkspace(String workspaceDocId) throws ExecutionException, InterruptedException {
        return db.collection(COLLECTION).document(workspaceDocId).get().get();
    }
 
    private String sanitize(String email) {
        return email.toLowerCase().replaceAll("[^a-z0-9]", "_");
    }
}
 