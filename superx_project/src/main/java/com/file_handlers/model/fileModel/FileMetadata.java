package com.file_handlers.model.fileModel;

import java.nio.file.attribute.FileTime;

public class FileMetadata {

    private final String fileName;
    private final String filePath;
    private final long fileSize;

    private final String extension;
    private final String mimeType;

    private final FileTime creationTime;
    private final FileTime lastModifiedTime;
    private final FileTime lastAccessTime;

    private final boolean directory;
    private final boolean readable;
    private final boolean writable;

    public FileMetadata(
            String fileName,
            String filePath,
            long fileSize,
            String extension,
            String mimeType,
            FileTime creationTime,
            FileTime lastModifiedTime,
            FileTime lastAccessTime,
            boolean directory,
            boolean readable,
            boolean writable) {

        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.extension = extension;
        this.mimeType = mimeType;
        this.creationTime = creationTime;
        this.lastModifiedTime = lastModifiedTime;
        this.lastAccessTime = lastAccessTime;
        this.directory = directory;
        this.readable = readable;
        this.writable = writable;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public FileTime getCreationTime() {
        return creationTime;
    }

    public FileTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public FileTime getLastAccessTime() {
        return lastAccessTime;
    }

    public boolean isDirectory() {
        return directory;
    }

    public boolean isReadable() {
        return readable;
    }

    public boolean isWritable() {
        return writable;
    }
}