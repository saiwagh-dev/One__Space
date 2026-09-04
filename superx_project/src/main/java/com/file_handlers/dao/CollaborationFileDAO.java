package com.file_handlers.dao;
 
import com.file_handlers.config.FirebaseConfig;
import com.file_handlers.model.CollaborationFileData;
import com.google.cloud.firestore.*;
 
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
 
/**
 * Single source of truth for the "workspaces/{id}/files" subcollection.
 *
 * IMPORTANT: file.secureUrl must always be a Cloudinary secure_url returned by
 * CollaborationController.uploadFile(). Never pass a local File.toURI() here —
 * that URL only resolves on the machine that uploaded it.
 */
public class CollaborationFileDAO {
 
    private final Firestore db = FirebaseConfig.getFirestore();
 
    public void addFile(String workspaceDocId, CollaborationFileData file, String cloudinaryPublicId,
                         Consumer<String> onSuccess, Consumer<Exception> onError) {
        try {
            if (file.secureUrl == null || file.secureUrl.startsWith("file:")) {
                throw new IllegalStateException(
                    "Refusing to save a local file:// URL as secureUrl — upload to Cloudinary first.");
            }
 
            Map<String, Object> data = new HashMap<>();
            data.put("fileName", file.fileName);
            data.put("size", file.size);
            data.put("uploadedOn", file.uploadedOn);
            data.put("iconColor", file.iconColor);
            data.put("secureUrl", file.secureUrl);
            data.put("uploaderName", file.uploadedBy); // Updated to match what is written in CollaborationController
            data.put("cloudinaryPublicId", cloudinaryPublicId); // keep this so you can delete from Cloudinary later
            data.put("uploadedAt", FieldValue.serverTimestamp());
 
            DocumentReference ref = db.collection("workspaces")
                    .document(workspaceDocId)
                    .collection("files")
                    .document();
            ref.set(data).get();
 
            db.collection("workspaces").document(workspaceDocId)
              .update("fileCount", FieldValue.increment(1));
 
            if (onSuccess != null) onSuccess.accept(ref.getId());
        } catch (Exception e) {
            if (onError != null) onError.accept(e);
        }
    }
 
    public void deleteFile(String workspaceDocId, String fileDocId,
                            Runnable onSuccess, Consumer<Exception> onError) {
        try {
            db.collection("workspaces").document(workspaceDocId)
              .collection("files").document(fileDocId).delete().get();
            db.collection("workspaces").document(workspaceDocId)
              .update("fileCount", FieldValue.increment(-1));
            if (onSuccess != null) onSuccess.run();
        } catch (Exception e) {
            if (onError != null) onError.accept(e);
        }
    }
 
    /** Live updates — call once when the SharedSpacePage for a workspace opens. */
    public ListenerRegistration listenForFiles(String workspaceDocId, EventListener<QuerySnapshot> listener) {
        return db.collection("workspaces").document(workspaceDocId)
                  .collection("files")
                  .addSnapshotListener(listener);
    }
}
