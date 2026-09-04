package com.file_handlers.service;

import java.util.List;
import com.file_handlers.dao.FileDAO;
import com.file_handlers.model.FileData;

public class EmbeddingMigrationService{

    private final FileDAO fileDAO=new FileDAO();
    private final EmbeddingService embeddingService=
            new EmbeddingService();

    public void generateMissingEmbeddings(
            String uid
    )throws Exception{

        List<FileData> files=
                fileDAO.getFilesForSemanticSearch(uid);

        int total=files.size();
        int processed=0;
        int skipped=0;
        int failed=0;

        System.out.println(
                "[EMBED MIGRATION] Found "+
                total+
                " files."
        );

        for(FileData file:files){

            processed++;

            if(file.getEmbedding()!=null&&
                    !file.getEmbedding().isEmpty()){

                skipped++;

                System.out.println(
                        "[EMBED MIGRATION] "+
                        processed+
                        "/"+
                        total+
                        " SKIPPED: "+
                        file.getFileName()
                );

                continue;
            }

            try{

                String searchableText=
                        embeddingService.buildFileText(
                                file.getFileName(),
                                file.getDescription(),
                                file.getSmartTags(),
                                file.getAiCategory(),
                                file.getFileType()
                        );

                List<Double> embedding=
                        embeddingService.embedDocument(
                                searchableText
                        );

                if(embedding==null||
                        embedding.isEmpty()){

                    failed++;

                    System.out.println(
                            "[EMBED MIGRATION] FAILED: "+
                            file.getFileName()
                    );

                    continue;
                }

                String fileId=
                        file.getFileHash();

                if(fileId==null||
                        fileId.isBlank()){

                    failed++;

                    System.out.println(
                            "[EMBED MIGRATION] FAILED - "
                            +"missing file hash: "+
                            file.getFileName()
                    );

                    continue;
                }

                fileDAO.updateEmbedding(
                        uid,
                        fileId,
                        embedding
                );

                System.out.println(
                        "[EMBED MIGRATION] "+
                        processed+
                        "/"+
                        total+
                        " DONE: "+
                        file.getFileName()+
                        " ("+
                        embedding.size()+
                        " dimensions)"
                );

            }catch(Exception e){

                failed++;

                System.out.println(
                        "[EMBED MIGRATION] FAILED: "+
                        file.getFileName()+
                        " -> "+
                        e.getMessage()
                );
            }
        }

        System.out.println(
                "[EMBED MIGRATION] Complete. "+
                "Processed="+processed+
                ", Skipped="+skipped+
                ", Failed="+failed
        );
    }
}