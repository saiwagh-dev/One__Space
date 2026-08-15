package com.file_handlers.service.fileService;

import com.file_handlers.model.fileModel.FileMetadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FileMetadataService {

    public FileMetadata getMetadata(Path filePath) throws IOException {

        // Read basic metadata provided by the operating system
        BasicFileAttributes attributes =
                Files.readAttributes(
                        filePath,
                        BasicFileAttributes.class
                );

        // Get file name
        String fileName =
                filePath.getFileName().toString();

        // Get file extension
        String extension = getExtension(fileName);

        // Try to determine MIME type
        String mimeType =
                Files.probeContentType(filePath);

        // Create and return our Model object
        return new FileMetadata(
                fileName,
                filePath.toAbsolutePath().toString(),
                attributes.size(),
                extension,
                mimeType,
                attributes.creationTime(),
                attributes.lastModifiedTime(),
                attributes.lastAccessTime(),
                attributes.isDirectory(),
                Files.isReadable(filePath),
                Files.isWritable(filePath)
        );
    }

    private String getExtension(String fileName) {

        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex > 0) {
            return fileName.substring(dotIndex);
        }

        return "";
    }
}