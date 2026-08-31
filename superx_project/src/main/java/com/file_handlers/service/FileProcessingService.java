package com.file_handlers.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.dao.SpaceDAO;
import com.file_handlers.model.AIResult;
import com.file_handlers.model.FileData;
import com.file_handlers.model.SpaceData;
import com.file_handlers.model.UserSession;

public class FileProcessingService {
    private final FileMetadataService metadataService=new FileMetadataService();
    private final AIClassificationService aiService=new AIClassificationService();
    private final SpaceResolver spaceResolver=new SpaceResolver();
    private final FileDAO fileDAO=new FileDAO();
    private final SpaceDAO spaceDAO=new SpaceDAO();

    public String processFile(Path path,ProcessingStatusListener listener)throws Exception {
        if(path==null) throw new IllegalArgumentException("File path cannot be null.");
        if(!Files.exists(path)||!Files.isRegularFile(path)) throw new IllegalArgumentException("Invalid file: "+path);

        UserSession session=UserSession.getInstance();
        if(session==null||!UserSession.isLoggedIn()||session.getUid()==null||session.getUid().isBlank())
            throw new IllegalStateException("No authenticated user session.");

        String uid=session.getUid();
        String email=session.getEmail();

        taskStarted(listener,"Metadata");
        FileData file=metadataService.extractMetadata(path,email);
        if(file==null) throw new IllegalStateException("Metadata extraction returned no file data.");
        file.setUploadedBy(email);
        taskCompleted(listener,"Metadata");

        String fileHash=file.getFileHash();
        if(fileHash==null||fileHash.isBlank()) throw new IllegalStateException("File hash could not be generated.");

        System.out.println("[CHECK] Checking duplicate: "+file.getFileName());

        if(fileDAO.fileExistsByHash(uid,fileHash)) {
            String existingName=fileDAO.getFileNameByHash(uid,fileHash);
            if(existingName==null||existingName.isBlank()) existingName=file.getFileName();
            System.out.println("[DUPLICATE] File already exists: "+existingName);
            if(listener!=null) listener.onTaskFailed("Duplicate","File already exists: "+existingName);
            return "DUPLICATE:"+existingName;
        }

        List<SpaceData> customSpaces;
        try {
            customSpaces=spaceDAO.getUserSpaces(uid);
        } catch(Exception e) {
            System.out.println("[SPACE] Unable to load custom spaces, continuing with fixed categories only: "+e.getMessage());
            customSpaces=new ArrayList<>();
        }

        taskStarted(listener,"AI");
        AIResult result=aiService.classify(file,customSpaces);
        if(result==null) throw new IllegalStateException("AI classification returned no result.");

        file.setAiCategory(result.getCategory());
        file.setAiConfidence(result.getConfidence());
        file.setDescription(result.getDescription());
        file.setSmartTags(result.getSmartTags());
        taskCompleted(listener,"AI");

        taskStarted(listener,"Space");
        String spaceId=spaceResolver.resolveSpaceId(result);
        if(spaceId==null||spaceId.isBlank()) throw new IllegalStateException("Unable to resolve a Space for the file.");
        file.setSpaceId(spaceId);
        taskCompleted(listener,"Space");

        taskStarted(listener,"Firestore");
        String firestoreId=fileDAO.saveFile(uid,file);
        taskCompleted(listener,"Firestore");

        System.out.println("[DONE] "+file.getFileName());
        return firestoreId;
    }

    /**
     * Adds a file directly into a specific Space, bypassing AI classification
     * entirely. Used when the user is certain a file belongs in a given
     * Space and doesn't want it re-routed by the AI pipeline.
     */
    public String processFileToSpace(Path path,String forcedSpaceId,String spaceName,ProcessingStatusListener listener)throws Exception {
        if(path==null) throw new IllegalArgumentException("File path cannot be null.");
        if(!Files.exists(path)||!Files.isRegularFile(path)) throw new IllegalArgumentException("Invalid file: "+path);
        if(forcedSpaceId==null||forcedSpaceId.isBlank()) throw new IllegalArgumentException("A target Space is required.");

        UserSession session=UserSession.getInstance();
        if(session==null||!UserSession.isLoggedIn()||session.getUid()==null||session.getUid().isBlank())
            throw new IllegalStateException("No authenticated user session.");

        String uid=session.getUid();
        String email=session.getEmail();

        taskStarted(listener,"Metadata");
        FileData file=metadataService.extractMetadata(path,email);
        if(file==null) throw new IllegalStateException("Metadata extraction returned no file data.");
        file.setUploadedBy(email);
        taskCompleted(listener,"Metadata");

        String fileHash=file.getFileHash();
        if(fileHash==null||fileHash.isBlank()) throw new IllegalStateException("File hash could not be generated.");

        System.out.println("[CHECK] Checking duplicate: "+file.getFileName());

        if(fileDAO.fileExistsByHash(uid,fileHash)) {
            String existingName=fileDAO.getFileNameByHash(uid,fileHash);
            if(existingName==null||existingName.isBlank()) existingName=file.getFileName();
            System.out.println("[DUPLICATE] File already exists: "+existingName);
            if(listener!=null) listener.onTaskFailed("Duplicate","File already exists: "+existingName);
            return "DUPLICATE:"+existingName;
        }

        // No Gemini call, no SpaceResolver: the user has already decided where this file goes.
        taskStarted(listener,"Space");
        file.setAiCategory("Manual");
        file.setAiConfidence(1.0);
        file.setDescription("Manually added to "+(spaceName==null||spaceName.isBlank()?"this Space":spaceName)+" without AI categorization.");
        file.setSmartTags(new ArrayList<>());
        file.setSpaceId(forcedSpaceId);
        taskCompleted(listener,"Space");

        taskStarted(listener,"Firestore");
        String firestoreId=fileDAO.saveFile(uid,file);
        taskCompleted(listener,"Firestore");

        System.out.println("[DONE] "+file.getFileName()+" (manually imported into "+forcedSpaceId+")");
        return firestoreId;
    }

    private void taskStarted(ProcessingStatusListener listener,String task) {
        if(listener!=null) listener.onTaskStarted(task);
    }

    private void taskCompleted(ProcessingStatusListener listener,String task) {
        if(listener!=null) listener.onTaskCompleted(task);
    }
}