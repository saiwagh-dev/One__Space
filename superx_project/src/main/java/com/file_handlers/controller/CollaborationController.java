package com.file_handlers.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.file_handlers.config.CloudinaryConfig;

import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
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
                Map result = cloudinary.uploader().upload(
                    file, 
                    ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "onespace_collaborations"
                    )
                );
                result.put("original_filename", file.getName());
                return result;
            }
        };
        

        uploadTask.setOnSucceeded(event -> {
            Map result = uploadTask.getValue();
            if (onSuccess != null) {
                onSuccess.accept(result);
            }
        });

        uploadTask.setOnFailed(event -> {
            Throwable ex = uploadTask.getException();
            if (onError != null) {
                onError.accept(ex instanceof Exception ? (Exception) ex : new Exception(ex));
            }
        });

        
    }
}