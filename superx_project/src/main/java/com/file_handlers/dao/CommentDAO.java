package com.file_handlers.dao;

import com.file_handlers.config.FirebaseConfig;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CommentDAO {

    public void addComment(String workspaceDocId, String fileDocId, String text, String authorName, String authorEmail, Runnable onSuccess, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                Firestore db = FirebaseConfig.getFirestore();
                Map<String, Object> data = new HashMap<>();
                data.put("text", text);
                data.put("authorName", authorName);
                data.put("authorEmail", authorEmail);
                data.put("timestamp", System.currentTimeMillis());

                if (fileDocId == null || fileDocId.isEmpty()) {
                    db.collection("workspaces").document(workspaceDocId).collection("comments").add(data).get();
                } else {
                    db.collection("workspaces").document(workspaceDocId)
                      .collection("files").document(fileDocId)
                      .collection("comments").add(data).get();
                }

                if (onSuccess != null) onSuccess.run();
            } catch (Exception e) {
                if (onError != null) onError.accept(e);
            }
        }).start();
    }

    public void getComments(String workspaceDocId, String fileDocId, Consumer<List<Map<String, Object>>> onLoaded, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                Firestore db = FirebaseConfig.getFirestore();
                Query query;
                if (fileDocId == null || fileDocId.isEmpty()) {
                    query = db.collection("workspaces").document(workspaceDocId).collection("comments").orderBy("timestamp", Query.Direction.ASCENDING);
                } else {
                    query = db.collection("workspaces").document(workspaceDocId)
                              .collection("files").document(fileDocId)
                              .collection("comments").orderBy("timestamp", Query.Direction.ASCENDING);
                }

                List<QueryDocumentSnapshot> docs = query.get().get().getDocuments();
                List<Map<String, Object>> comments = new ArrayList<>();
                for (var doc : docs) {
                    comments.add(doc.getData());
                }

                javafx.application.Platform.runLater(() -> onLoaded.accept(comments));
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    if (onError != null) onError.accept(e);
                });
            }
        }).start();
    }
}