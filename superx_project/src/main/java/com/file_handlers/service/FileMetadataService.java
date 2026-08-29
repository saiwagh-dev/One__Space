package com.file_handlers.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;

import com.google.cloud.Timestamp;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;

import com.file_handlers.model.FileData;

public class FileMetadataService {
    private static final int FULL_SNIPPET_LIMIT=30000;
    private static final int LARGE_FILE_SECTIONS=20;
    private static final int SECTION_SNIPPET_LENGTH=1500;
    private final Tika tika=new Tika();

    public FileData extractMetadata(Path path,String uploadedBy) {
        validatePath(path);
        try {
            BasicFileAttributes attr=Files.readAttributes(path,BasicFileAttributes.class);
            FileData file=new FileData();
            file.setFileName(path.getFileName().toString());
            file.setLocalPath(path.toAbsolutePath().normalize().toString());
            file.setFileSize(attr.size());
            file.setFileType(detectFileType(path));
            file.setUploadedBy(uploadedBy);
            file.setUploadedAt(Timestamp.now());

            ProcessingData data=readFile(path);
            file.setFileHash(data.hash);
            file.setExtractedSnippet(createSmartSnippet(data.text));
            return file;
        } catch(Exception e) {
            throw new RuntimeException("Unable to extract metadata from file: "+path,e);
        }
    }

    private ProcessingData readFile(Path path) {
        try {
            MessageDigest digest=MessageDigest.getInstance("SHA-256");
            String text;
            try(InputStream raw=Files.newInputStream(path);
                DigestInputStreamWithHash input=new DigestInputStreamWithHash(raw,digest)) {
                Metadata metadata=new Metadata();
                metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY,path.getFileName().toString());
                BodyContentHandler handler=new BodyContentHandler(-1);
                new AutoDetectParser().parse(input,handler,metadata,new ParseContext());
                text=handler.toString().trim();
            }
            return new ProcessingData(toHex(digest.digest()),text);
        } catch(Exception e) {
            throw new RuntimeException("Unable to read file: "+path,e);
        }
    }

    private String detectFileType(Path path) {
        try {
            return tika.detect(path);
        } catch(Exception e) {
            return "application/octet-stream";
        }
    }

    private String createSmartSnippet(String text) {
        if(text==null||text.isBlank()) return "";
        text=text.trim();
        if(text.length()<=FULL_SNIPPET_LIMIT) return text;
        int sectionSize=text.length()/LARGE_FILE_SECTIONS;
        StringBuilder snippet=new StringBuilder();
        for(int i=0;i<LARGE_FILE_SECTIONS;i++) {
            int start=i*sectionSize;
            int end=i==LARGE_FILE_SECTIONS-1?text.length():(i+1)*sectionSize;
            String section=text.substring(start,end).trim();
            if(section.isBlank()) continue;
            snippet.append("\n\n--- Section ").append(i+1).append(" ---\n");
            snippet.append(getRepresentativePart(section));
        }
        return snippet.toString().trim();
    }

    private String getRepresentativePart(String section) {
        if(section.length()<=SECTION_SNIPPET_LENGTH) return section;
        String first=section.substring(0,Math.min(1000,section.length()));
        String last=section.substring(Math.max(0,section.length()-500));
        return first.trim()+"\n...\n"+last.trim();
    }

    private String toHex(byte[] bytes) {
        StringBuilder hash=new StringBuilder(bytes.length*2);
        for(byte b:bytes) hash.append(String.format("%02x",b));
        return hash.toString();
    }

    private void validatePath(Path path) {
        if(path==null) throw new IllegalArgumentException("File path cannot be null.");
        if(!Files.exists(path)) throw new IllegalArgumentException("File does not exist: "+path);
        if(!Files.isRegularFile(path)) throw new IllegalArgumentException("Path is not a regular file: "+path);
    }

    private static class ProcessingData {
        final String hash;
        final String text;
        ProcessingData(String hash,String text) {
            this.hash=hash;
            this.text=text;
        }
    }

    private static class DigestInputStreamWithHash extends InputStream {
        private final InputStream input;
        private final MessageDigest digest;

        DigestInputStreamWithHash(InputStream input,MessageDigest digest) {
            this.input=input;
            this.digest=digest;
        }

        public int read() throws java.io.IOException {
            int value=input.read();
            if(value!=-1) digest.update((byte)value);
            return value;
        }

        public int read(byte[] b,int off,int len) throws java.io.IOException {
            int count=input.read(b,off,len);
            if(count>0) digest.update(b,off,count);
            return count;
        }

        public void close() throws java.io.IOException {
            input.close();
        }
    }
}