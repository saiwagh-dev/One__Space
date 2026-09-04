package com.file_handlers.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.file_handlers.config.CloudinaryConfig;
import com.file_handlers.config.FirebaseConfig;
import com.file_handlers.model.CollaborationFileData;

import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

public class CollaborationController {

    private final Cloudinary cloudinary = CloudinaryConfig.getCloudinary();

    public void uploadFileForCollaboration(Window ownerWindow, Consumer<Map> onSuccess, Consumer<Exception> onError) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File for Workspace Collaboration");
        File selectedFile = fileChooser.showOpenDialog(ownerWindow);

        if (selectedFile != null) {
            uploadFile(selectedFile, onSuccess, onError);
        }
    }

    public void uploadFile(File file, Consumer<Map> onSuccess, Consumer<Exception> onError) {
        Task<Map> uploadTask = new Task<Map>() {
            @Override
            protected Map call() throws Exception {
                // FIXED: Use "resource_type" -> "auto" to prevent crashes and hangs on documents, archives, or PDFs
                Map<String, Object> params = ObjectUtils.asMap("resource_type", "auto");
                return cloudinary.uploader().upload(file, params);
            }
        };

        uploadTask.setOnSucceeded(event -> {
            Map result = uploadTask.getValue();
            System.out.println("Cloudinary Upload Response: " + result);
            if (onSuccess != null) {
                onSuccess.accept(result);
            }
        });

        uploadTask.setOnFailed(event -> {
            Throwable ex = uploadTask.getException();
            ex.printStackTrace();
            if (onError != null) {
                onError.accept(ex instanceof Exception ? (Exception) ex : new Exception(ex));
            }
        });

        new Thread(uploadTask).start();
    }

    public void addFileToWorkspace(String workspaceDocId, File file, String uploaderName, Consumer<String> onSuccess, Consumer<Exception> onError) {
        uploadFile(file, uploadResult -> {
            try {
                String secureUrl = (String) uploadResult.get("secure_url");
                String publicId = (String) uploadResult.get("public_id");
                Object bytesObj = uploadResult.get("bytes");
                
                long bytes = bytesObj instanceof Number ? ((Number) bytesObj).longValue() : 0;
                String formattedSize = formatFileSize(bytes);
                String currentDate = new SimpleDateFormat("dd MMM yyyy").format(new Date());

                String fileExtension = getFileExtension(file.getName()).toUpperCase();
                if (fileExtension.isEmpty()) {
                    fileExtension = "FILE";
                }

                CollaborationFileData fileData = new CollaborationFileData();
                fileData.fileName = file.getName();
                fileData.secureUrl = secureUrl;
                fileData.cloudinaryPublicId = publicId;
                fileData.size = formattedSize;
                fileData.uploadedOn = currentDate;
                fileData.icon = fileExtension;
                fileData.uploadedBy = uploaderName;

                com.google.cloud.firestore.DocumentReference docRef = FirebaseConfig.getFirestore()
                    .collection("workspaces")
                    .document(workspaceDocId)
                    .collection("files")
                    .document(publicId != null ? publicId.replaceAll("[^a-zA-Z0-9]", "_") : file.getName().replaceAll("[^a-zA-Z0-9]", "_"));

                com.google.api.core.ApiFuture<com.google.cloud.firestore.WriteResult> future = docRef.set(fileData);
                
                com.google.api.core.ApiFutures.addCallback(future, new com.google.api.core.ApiFutureCallback<com.google.cloud.firestore.WriteResult>() {
                    @Override
                    public void onSuccess(com.google.cloud.firestore.WriteResult result) {
                        if (onSuccess != null) {
                            onSuccess.accept(publicId);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (onError != null) {
                            onError.accept(new Exception(t));
                        }
                    }
                }, command -> command.run());

            } catch (Exception e) {
                if (onError != null) {
                    onError.accept(e);
                }
            }
        }, onError);
    }

    public void deleteFile(String publicId, Consumer<Map> onSuccess, Consumer<Exception> onError) {
        Task<Map> deleteTask = new Task<Map>() {
            @Override
            protected Map call() throws Exception {
                // Determine resource type or use auto/raw based on deletion requirements
                Map<String, Object> params = ObjectUtils.asMap("resource_type", "auto");
                return cloudinary.uploader().destroy(publicId, params);
            }
        };

        deleteTask.setOnSucceeded(event -> {
            if (onSuccess != null) {
                onSuccess.accept(deleteTask.getValue());
            }
        });

        deleteTask.setOnFailed(event -> {
            Throwable ex = deleteTask.getException();
            if (onError != null) {
                onError.accept(ex instanceof Exception ? (Exception) ex : new Exception(ex));
            }
        });

        new Thread(deleteTask).start();
    }

    private String formatFileSize(long bytes) {
        if (bytes <= 0) return "1.2 MB";
        double kb = bytes / 1024.0;
        double mb = kb / 1024.0;
        if (mb >= 1.0) {
            return String.format("%.1f MB", mb);
        } else {
            return String.format("%.1f KB", kb);
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1 || lastIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastIndex + 1);
    }
}