package com.file_handlers.controller;

import com.file_handlers.dao.CollaborationFileDAO;
import com.file_handlers.dao.MemberDAO;
import com.file_handlers.dao.WorkspaceDAO;
import com.file_handlers.model.CollaborationFileData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Orchestrates workspace creation: Firestore workspace+members doc, then uploads
 * each selected file to Cloudinary via CollaborationController, THEN writes each
 * file's real Cloudinary secure_url to Firestore via CollaborationFileDAO.
 */
public class WorkspaceController {

    private final WorkspaceDAO workspaceDao = new WorkspaceDAO();
    private final CollaborationFileDAO fileDao = new CollaborationFileDAO();
    private final MemberDAO memberDao = new MemberDAO();
    private final CollaborationController collaborationController = new CollaborationController();

    public void createWorkspaceWithMembersAndFiles(
            String workspaceName, String ownerId, String ownerName, String ownerEmail,
            List<String> memberEmails, List<File> files,
            java.util.function.BiConsumer<Integer, Integer> onProgress,
            Consumer<String> onAllDone, Consumer<Exception> onError) {

        workspaceDao.createWorkspace(workspaceName, ownerId, ownerName, ownerEmail, memberEmails,
            workspaceDocId -> {
                if (files == null || files.isEmpty()) {
                    onAllDone.accept(workspaceDocId);
                    return;
                }
                uploadAllFiles(workspaceDocId, files, ownerName, onProgress, onAllDone, onError);
            },
            onError
        );
    }

    private void uploadAllFiles(String workspaceDocId, List<File> files, String uploaderName,
                                java.util.function.BiConsumer<Integer, Integer> onProgress,
                                Consumer<String> onAllDone, Consumer<Exception> onError) {
        int total = files.size();
        AtomicInteger completed = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        for (File file : files) {
            collaborationController.uploadFile(file,
                cloudinaryResult -> {
                    CollaborationFileData fileData = toFileData(cloudinaryResult, uploaderName, file);
                    String publicId = String.valueOf(cloudinaryResult.get("public_id"));

                    fileDao.addFile(workspaceDocId, fileData, publicId,
                        savedId -> {
                            int done = completed.incrementAndGet();
                            if (onProgress != null) onProgress.accept(done, total);
                            if (done + failed.get() == total) onAllDone.accept(workspaceDocId);
                        },
                        e -> {
                            failed.incrementAndGet();
                            onError.accept(e);
                        });
                },
                e -> {
                    failed.incrementAndGet();
                    onError.accept(e);
                    if (completed.get() + failed.get() == total) onAllDone.accept(workspaceDocId);
                });
        }
    }

    public void addFileToWorkspace(String workspaceDocId, File file, String uploaderName,
                                   Consumer<String> onSuccess, Consumer<Exception> onError) {
        collaborationController.uploadFile(file,
            cloudinaryResult -> {
                CollaborationFileData fileData = toFileData(cloudinaryResult, uploaderName, file);
                String publicId = String.valueOf(cloudinaryResult.get("public_id"));
                fileDao.addFile(workspaceDocId, fileData, publicId, onSuccess, onError);
            },
            onError);
    }

    public void renameWorkspace(String workspaceDocId, String newName,
                               Runnable onSuccess, Consumer<Exception> onError) {
        workspaceDao.renameWorkspace(workspaceDocId, newName, onSuccess, onError);
    }

    public void addMember(String workspaceDocId, String name, String email, String role,
                          Runnable onSuccess, Consumer<Exception> onError) {
        memberDao.addMember(workspaceDocId, name, email, role, onSuccess, onError);
    }

    private CollaborationFileData toFileData(Map cloudinaryResult, String uploaderName, File sourceFile) {
        String fileName = sourceFile != null ? sourceFile.getName() : null;
        if (fileName == null || fileName.equals("null")) {
            fileName = String.valueOf(cloudinaryResult.get("original_filename"));
            String format = String.valueOf(cloudinaryResult.get("format"));
            if (format != null && !format.equals("null") && !fileName.endsWith("." + format)) {
                fileName += "." + format;
            }
        }

        String secureUrl = String.valueOf(cloudinaryResult.get("secure_url"));
        String publicId = String.valueOf(cloudinaryResult.get("public_id"));
        Object bytesObj = cloudinaryResult.get("bytes");
        String size = bytesObj != null ? formatSize(Long.parseLong(bytesObj.toString())) : "-";
        String uploadedOn = new SimpleDateFormat("dd MMM yyyy").format(new java.util.Date());

        String extension = getFileExtension(fileName).toUpperCase();
        if (extension.isEmpty()) extension = "FILE";

        return new CollaborationFileData(extension, fileName, size, uploadedOn, "#38BDF8", secureUrl, uploaderName, publicId);
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1 || lastIndex == fileName.length() - 1) return "";
        return fileName.substring(lastIndex + 1);
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024));
    }
}