package com.file_handlers.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.file_handlers.cache.FileCache;
import com.file_handlers.config.FirebaseConfig;
import com.file_handlers.model.FileData;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;

public class FileDAO{
    private static final String USERS_COLLECTION="users";
    private static final String FILES_COLLECTION="files";

    private static final String[] SUMMARY_FIELDS={
        "fileName","fileNameLower","localPath","fileSize","fileType",
        "fileHash","uploadedBy","uploadedAt","aiCategory","aiConfidence",
        "spaceId","deleted","deletedAt","lastAccessedAt",
        "extractedSnippet","description","smartTags"
    };

    private static final Set<String> STOP_WORDS=new HashSet<>(Arrays.asList(
        "what","when","where","who","which","how","why","is","are","was","were",
        "my","me","i","the","a","an","and","or","of","to","in","on","for",
        "do","does","did","can","could","tell","show","give","please","about",
        "from","have","has","with","your","you","it","this","that","number"
    ));

    private Firestore getFirestore(){return FirebaseConfig.getFirestore();}

    private CollectionReference getUserFilesCollection(String uid){
        validateUid(uid);
        return getFirestore().collection(USERS_COLLECTION).document(uid).collection(FILES_COLLECTION);
    }

    public String saveFile(String uid,FileData file)throws Exception{
        validateUid(uid);
        if(file==null)throw new IllegalArgumentException("File data cannot be null.");

        String fileId=file.getFileHash();
        if(fileId==null||fileId.isBlank())
            fileId=getUserFilesCollection(uid).document().getId();

        Map<String,Object> data=new HashMap<>();
        Map<String,Object> fileMap=file.toMap();

        if(fileMap!=null)data.putAll(fileMap);

        data.put("uploadedAt",Timestamp.now());

        if(file.getFileName()!=null)
            data.put("fileNameLower",file.getFileName().trim().toLowerCase());

        data.put("fileHash",file.getFileHash());
        data.put("deleted",false);
        data.put("deletedAt",null);

        getUserFilesCollection(uid).document(fileId).set(data).get();
        FileCache.invalidate(uid);

        return fileId;
    }

    public FileData getFile(String uid,String fileId)throws Exception{
        validateUid(uid);

        if(fileId==null||fileId.isBlank())
            throw new IllegalArgumentException("File ID is required.");

        DocumentSnapshot document=getUserFilesCollection(uid)
                .document(fileId)
                .get()
                .get();

        return document.exists()?document.toObject(FileData.class):null;
    }

    public List<FileData> getFileSummaries(String uid)throws Exception{
        validateUid(uid);

        List<FileData> cached=FileCache.get(uid);

        if(cached!=null)return cached;

        QuerySnapshot snapshot=getUserFilesCollection(uid)
                .select(SUMMARY_FIELDS)
                .get()
                .get();

        List<FileData> files=convertDocuments(snapshot);

        files.removeIf(FileData::isDeleted);

        FileCache.put(uid,files);

        return files;
    }

    public List<FileData> getFileSummariesBySpace(String uid,String spaceId)throws Exception{
        validateUid(uid);

        if(spaceId==null||spaceId.isBlank())
            return new ArrayList<>();

        List<FileData> files=getFileSummaries(uid);
        List<FileData> result=new ArrayList<>();

        for(FileData file:files)
            if(spaceId.equals(file.getSpaceId()))
                result.add(file);

        return result;
    }

    public List<FileData> searchFileSummaries(String uid,String searchText)throws Exception{
        validateUid(uid);

        if(searchText==null||searchText.isBlank())
            return getFileSummaries(uid);

        String search=searchText.trim().toLowerCase();
        String end=search+"\uf8ff";

        QuerySnapshot snapshot=getUserFilesCollection(uid)
                .whereGreaterThanOrEqualTo("fileNameLower",search)
                .whereLessThan("fileNameLower",end)
                .select(SUMMARY_FIELDS)
                .get()
                .get();

        List<FileData> files=convertDocuments(snapshot);

        files.removeIf(FileData::isDeleted);

        return files;
    }

    public List<FileData> searchFilesForAI(String uid,String query)throws Exception{
        validateUid(uid);

        if(query==null||query.isBlank())
            return new ArrayList<>();

        QuerySnapshot snapshot=getUserFilesCollection(uid)
                .get()
                .get();

        List<FileMatch> matches=new ArrayList<>();

        for(DocumentSnapshot document:snapshot.getDocuments()){
            try{
                FileData file=document.toObject(FileData.class);

                if(file==null||file.isDeleted())
                    continue;

                int score=calculateAIRelevance(file,query);

                if(score>=10)
                    matches.add(new FileMatch(file,score));

            }catch(Exception e){
                System.out.println("[WARN] AI file search skipped file: "+e.getMessage());
            }
        }

        matches.sort((a,b)->Integer.compare(b.score,a.score));

        List<FileData> result=new ArrayList<>();

        if(matches.isEmpty())
            return result;

        int limit=Math.min(3,matches.size());

        int highestScore=matches.get(0).score;

        for(int i=0;i<limit;i++){
            FileMatch match=matches.get(i);

            if(i>0&&match.score<Math.max(10,highestScore/2))
                break;

            result.add(match.file);
        }

        return result;
    }

    private int calculateAIRelevance(FileData file,String query){
        String normalizedQuery=normalize(query);
        List<String> terms=getKeywords(normalizedQuery);

        if(terms.isEmpty())
            return 0;

        String name=normalize(file.getFileName());
        String snippet=normalize(file.getExtractedSnippet());
        String description=normalize(file.getDescription());
        String category=normalize(file.getAiCategory());
        String tags=normalizeTags(file.getSmartTags());

        int score=0;

        if(!name.isBlank()&&name.contains(normalizedQuery))
            score+=60;

        if(!snippet.isBlank()&&snippet.contains(normalizedQuery))
            score+=35;

        if(!description.isBlank()&&description.contains(normalizedQuery))
            score+=30;

        for(String term:terms){
            if(term.length()<2)
                continue;

            if(name.contains(term))
                score+=25;

            if(tags.contains(term))
                score+=12;

            if(description.contains(term))
                score+=8;

            if(snippet.contains(term))
                score+=6;

            if(category.contains(term))
                score+=4;
        }

        score+=getDocumentTypeBonus(normalizedQuery,name,snippet,description);

        return score;
    }

    private int getDocumentTypeBonus(String query,String name,String snippet,String description){
        String content=name+" "+snippet+" "+description;

        if(containsAny(query,"bank","statement","account","transaction")&&
                containsAny(content,"bank","statement","transaction"))
            return 40;

        if(containsAny(query,"insurance","policy","premium","expiry","expire")&&
                containsAny(content,"insurance","policy","premium","expiry","expire"))
            return 40;

        if(containsAny(query,"aadhar","aadhaar","uidai")&&
                containsAny(content,"aadhar","aadhaar","uidai"))
            return 50;

        if(containsAny(query,"passport","passport number")&&
                content.contains("passport"))
            return 50;

        if(containsAny(query,"resume","cv","experience","qualification")&&
                containsAny(content,"resume","experience","qualification"))
            return 40;

        if(containsAny(query,"timetable","semester","class","lecture")&&
                containsAny(content,"timetable","semester","lecture"))
            return 40;

        return 0;
    }

    private boolean containsAny(String text,String...words){
        for(String word:words)
            if(text.contains(word))
                return true;

        return false;
    }

    private List<String> getKeywords(String query){
        List<String> keywords=new ArrayList<>();

        for(String word:query.split("\\s+")){
            if(word.length()>1&&!STOP_WORDS.contains(word))
                keywords.add(word);
        }

        return keywords;
    }

    private String normalizeTags(List<String> tags){
        if(tags==null||tags.isEmpty())
            return "";

        StringBuilder result=new StringBuilder();

        for(String tag:tags){
            if(tag!=null)
                result.append(" ").append(normalize(tag));
        }

        return result.toString().trim();
    }

    private String normalize(String text){
        if(text==null)
            return "";

        return text.toLowerCase()
                .replaceAll("[^a-z0-9]+"," ")
                .trim();
    }

    private static class FileMatch{
        private final FileData file;
        private final int score;

        FileMatch(FileData file,int score){
            this.file=file;
            this.score=score;
        }
    }

    public List<FileData> getAllFiles(String uid)throws Exception{
        return getFileSummaries(uid);
    }

    public List<FileData> getFilesBySpace(String uid,String spaceId)throws Exception{
        return getFileSummariesBySpace(uid,spaceId);
    }

    public List<FileData> searchFiles(String uid,String fileName)throws Exception{
        return searchFileSummaries(uid,fileName);
    }

    public boolean fileExistsByHash(String uid,String hash)throws Exception{
        validateUid(uid);

        if(hash==null||hash.isBlank())
            return false;

        QuerySnapshot snapshot=getUserFilesCollection(uid)
                .whereEqualTo("fileHash",hash)
                .select("fileHash")
                .limit(1)
                .get()
                .get();

        return !snapshot.isEmpty();
    }

    public String getFileNameByHash(String uid,String hash)throws Exception{
        validateUid(uid);

        if(hash==null||hash.isBlank())
            return null;

        QuerySnapshot snapshot=getUserFilesCollection(uid)
                .whereEqualTo("fileHash",hash)
                .select("fileName")
                .limit(1)
                .get()
                .get();

        if(snapshot.isEmpty())
            return null;

        return snapshot.getDocuments()
                .get(0)
                .getString("fileName");
    }

    public FileData findFileByHash(String uid,String hash)throws Exception{
        validateUid(uid);

        if(hash==null||hash.isBlank())
            return null;

        QuerySnapshot snapshot=getUserFilesCollection(uid)
                .whereEqualTo("fileHash",hash)
                .limit(1)
                .get()
                .get();

        if(snapshot.isEmpty())
            return null;

        return snapshot.getDocuments()
                .get(0)
                .toObject(FileData.class);
    }

    public void touchFile(String uid,String fileId)throws Exception{
        validateUid(uid);

        if(fileId==null||fileId.isBlank())
            throw new IllegalArgumentException("File ID is required.");

        getUserFilesCollection(uid)
                .document(fileId)
                .update("lastAccessedAt",Timestamp.now())
                .get();

        FileCache.invalidate(uid);
    }

    public List<FileData> getRecentFiles(
        String uid,
        int limit
    )throws Exception{

        validateUid(uid);

        if(limit<=0)
            limit=10;

        QuerySnapshot snapshot=
                getUserFilesCollection(uid)
                        .whereEqualTo("deleted",false)
                        .get()
                        .get();

        List<FileData> files=new ArrayList<>();

        for(DocumentSnapshot document:snapshot.getDocuments()){

            FileData file=
                    document.toObject(FileData.class);

            if(file!=null &&
                    !file.isDeleted() &&
                    file.getLastAccessedAt()!=null){

                files.add(file);
            }
        }

    // Newest accessed files first.
    files.sort((a,b)->
            b.getLastAccessedAt()
                    .compareTo(
                            a.getLastAccessedAt()
                    )
    );

    // Apply limit AFTER removing deleted files.
    if(files.size()>limit)
        return new ArrayList<>(
                files.subList(0,limit)
        );

    return files;
}

    public void softDeleteFile(String uid,String fileId)throws Exception{
        validateUid(uid);

        if(fileId==null||fileId.isBlank())
            throw new IllegalArgumentException("File ID is required.");

        getUserFilesCollection(uid)
                .document(fileId)
                .update("deleted",true,"deletedAt",Timestamp.now())
                .get();

        FileCache.invalidate(uid);
    }

    public void restoreFile(String uid,String fileId)throws Exception{
        validateUid(uid);

        if(fileId==null||fileId.isBlank())
            throw new IllegalArgumentException("File ID is required.");

        getUserFilesCollection(uid)
                .document(fileId)
                .update("deleted",false,"deletedAt",null)
                .get();

        FileCache.invalidate(uid);
    }

    public List<FileData> getTrashedFiles(String uid)throws Exception{
        validateUid(uid);

        QuerySnapshot snapshot=getUserFilesCollection(uid)
                .whereEqualTo("deleted",true)
                .get()
                .get();

        List<FileData> files=new ArrayList<>();

        for(DocumentSnapshot document:snapshot.getDocuments()){
            FileData file=document.toObject(FileData.class);

            if(file!=null)
                files.add(file);
        }

        return files;
    }

    public void permanentlyDeleteFile(String uid,String fileId)throws Exception{
        deleteFile(uid,fileId);
    }

    public void deleteFile(String uid,String fileId)throws Exception{
        validateUid(uid);

        if(fileId==null||fileId.isBlank())
            throw new IllegalArgumentException("File ID is required.");

        getUserFilesCollection(uid)
                .document(fileId)
                .delete()
                .get();

        FileCache.invalidate(uid);
    }

    public static void invalidateCache(String uid){
        FileCache.invalidate(uid);
    }

    public static void clearCache(){
        FileCache.clear();
    }

    private List<FileData> convertDocuments(QuerySnapshot snapshot){
        List<FileData> files=new ArrayList<>();

        for(DocumentSnapshot doc:snapshot.getDocuments()){
            try{
                FileData file=new FileData();

                file.setFileName(doc.getString("fileName"));
                file.setLocalPath(doc.getString("localPath"));
                file.setFileType(doc.getString("fileType"));

                Long size=doc.getLong("fileSize");
                if(size!=null)
                    file.setFileSize(size);

                file.setFileHash(doc.getString("fileHash"));
                file.setUploadedBy(doc.getString("uploadedBy"));
                file.setAiCategory(doc.getString("aiCategory"));
                file.setSpaceId(doc.getString("spaceId"));

                Double confidence=doc.getDouble("aiConfidence");
                if(confidence!=null)
                    file.setAiConfidence(confidence);

                Timestamp uploadedAt=doc.getTimestamp("uploadedAt");
                if(uploadedAt!=null)
                    file.setUploadedAt(uploadedAt);

                file.setLastAccessedAt(doc.getTimestamp("lastAccessedAt"));
                file.setExtractedSnippet(doc.getString("extractedSnippet"));
                file.setDescription(doc.getString("description"));

                Object tags=doc.get("smartTags");

                if(tags instanceof List<?>){
                    List<String> smartTags=new ArrayList<>();

                    for(Object tag:(List<?>)tags)
                        if(tag!=null)
                            smartTags.add(String.valueOf(tag));

                    file.setSmartTags(smartTags);
                }

                Boolean deleted=doc.getBoolean("deleted");
                file.setDeleted(deleted!=null&&deleted);

                file.setDeletedAt(doc.getTimestamp("deletedAt"));

                files.add(file);

            }catch(Exception e){
                System.out.println("[WARN] Could not read file summary: "+e.getMessage());
            }
        }

        return files;
    }

    private void validateUid(String uid){
        if(uid==null||uid.isBlank())
            throw new IllegalArgumentException("User UID is required.");
    }
}