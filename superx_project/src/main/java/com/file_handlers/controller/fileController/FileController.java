package com.file_handlers.controller.fileController;

import com.file_handlers.model.fileModel.FileMetadata;
import com.file_handlers.service.fileService.FileMetadataService;

import java.io.IOException;
import java.nio.file.Path;

public class FileController {

    private final FileMetadataService metadataService;

    public FileController() {
        metadataService = new FileMetadataService();
    }

    public FileMetadata getFileMetadata(Path filePath) {

        try {

            return metadataService.getMetadata(filePath);

        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }
}