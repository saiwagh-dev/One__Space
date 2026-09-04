package com.file_handlers.dao;
 
import com.file_handlers.config.FirebaseConfig;
import com.google.cloud.firestore.*;
 
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
 
/** Single source of truth for the "workspaces/{id}/members" subcollection. */
public class MemberDAO {
 
    private final Firestore db = FirebaseConfig.getFirestore();
 
    public void addMember(String workspaceDocId, String name, String email, String role,
                           Runnable onSuccess, Consumer<Exception> onError) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("email", email);
            data.put("role", role);
            data.put("status", "pending");
 
            db.collection("workspaces").document(workspaceDocId)
              .collection("members").document(sanitize(email))
              .set(data).get();
 
            db.collection("workspaces").document(workspaceDocId)
              .update("memberCount", FieldValue.increment(1));
 
            if (onSuccess != null) onSuccess.run();
        } catch (Exception e) {
            if (onError != null) onError.accept(e);
        }
    }
 
    public void removeMember(String workspaceDocId, String memberDocId,
                              Runnable onSuccess, Consumer<Exception> onError) {
        try {
            db.collection("workspaces").document(workspaceDocId)
              .collection("members").document(memberDocId).delete().get();
            db.collection("workspaces").document(workspaceDocId)
              .update("memberCount", FieldValue.increment(-1));
            if (onSuccess != null) onSuccess.run();
        } catch (Exception e) {
            if (onError != null) onError.accept(e);
        }
    }
 
    public void updateRole(String workspaceDocId, String memberDocId, String newRole,
                            Runnable onSuccess, Consumer<Exception> onError) {
        try {
            db.collection("workspaces").document(workspaceDocId)
              .collection("members").document(memberDocId)
              .update("role", newRole).get();
            if (onSuccess != null) onSuccess.run();
        } catch (Exception e) {
            if (onError != null) onError.accept(e);
        }
    }
 
    public ListenerRegistration listenForMembers(String workspaceDocId, EventListener<QuerySnapshot> listener) {
        return db.collection("workspaces").document(workspaceDocId)
                  .collection("members")
                  .addSnapshotListener(listener);
    }
 
    private String sanitize(String email) {
        return email.toLowerCase().replaceAll("[^a-z0-9]", "_");
    }
}
 