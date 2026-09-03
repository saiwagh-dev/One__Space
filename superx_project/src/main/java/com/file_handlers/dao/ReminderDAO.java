package com.file_handlers.dao;

import com.file_handlers.config.FirebaseConfig;
import com.file_handlers.model.Reminder;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReminderDAO{
    private static final String USERS="users";
    private static final String REMINDERS="reminders";

    private Firestore getFirestore(){
        return FirebaseConfig.getFirestore();
    }

    private CollectionReference getCollection(String uid){
        validateUid(uid);

        return getFirestore()
                .collection(USERS)
                .document(uid)
                .collection(REMINDERS);
    }

    public String saveReminder(String uid,Reminder reminder)throws Exception{
        validateUid(uid);

        if(reminder==null)
            throw new IllegalArgumentException("Reminder cannot be null.");

        CollectionReference collection=getCollection(uid);

        String id=reminder.getId();

        if(id==null||id.isBlank())
            id=collection.document().getId();

        Map<String,Object> data=reminder.toMap();
        data.put("createdAt",Timestamp.now());

        collection.document(id)
                .set(data)
                .get();

        return id;
    }

    public void saveAutoReminder(
            String uid,
            Reminder reminder
    )throws Exception{

        validateUid(uid);

        if(reminder==null||
                reminder.getId()==null||
                reminder.getId().isBlank()){

            throw new IllegalArgumentException(
                    "Reminder and its deterministic ID are required."
            );
        }

        if(reminder.getCreatedAt()==null)
            reminder.setCreatedAt(Timestamp.now());

        getCollection(uid)
                .document(reminder.getId())
                .set(reminder.toMap())
                .get();
    }

    public List<Reminder> getRemindersForRange(
            String uid,
            Timestamp start,
            Timestamp end
    )throws Exception{

        validateUid(uid);

        if(start==null||end==null)
            return new ArrayList<>();

        QuerySnapshot snapshot=getCollection(uid)
                .whereGreaterThanOrEqualTo("date",start)
                .whereLessThan("date",end)
                .orderBy("date",Query.Direction.ASCENDING)
                .get()
                .get();

        return convert(snapshot);
    }

    public List<Reminder> getUpcoming(
            String uid,
            int limit
    )throws Exception{

        validateUid(uid);

        if(limit<=0)
            limit=10;

        QuerySnapshot snapshot=getCollection(uid)
                .whereGreaterThanOrEqualTo("date",Timestamp.now())
                .orderBy("date",Query.Direction.ASCENDING)
                .limit(limit)
                .get()
                .get();

        return convert(snapshot);
    }

    public Reminder getReminder(
            String uid,
            String reminderId
    )throws Exception{

        validateUid(uid);

        if(reminderId==null||reminderId.isBlank())
            return null;

        DocumentSnapshot document=getCollection(uid)
                .document(reminderId)
                .get()
                .get();

        if(!document.exists())
            return null;

        Reminder reminder=document.toObject(Reminder.class);

        if(reminder!=null)
            reminder.setId(document.getId());

        return reminder;
    }

    public void deleteReminder(
            String uid,
            String reminderId
    )throws Exception{

        validateUid(uid);

        if(reminderId==null||reminderId.isBlank())
            throw new IllegalArgumentException(
                    "Reminder ID is required."
            );

        getCollection(uid)
                .document(reminderId)
                .delete()
                .get();
    }

    public List<Reminder> getAutoRemindersForFile(
            String uid,
            String fileHash
    )throws Exception{

        validateUid(uid);

        if(fileHash==null||fileHash.isBlank())
            return new ArrayList<>();

        QuerySnapshot snapshot=getCollection(uid)
                .whereEqualTo("linkedFileId",fileHash)
                .whereEqualTo("source","ai_extracted")
                .get()
                .get();

        return convert(snapshot);
    }

    private List<Reminder> convert(QuerySnapshot snapshot){
        List<Reminder> reminders=new ArrayList<>();

        for(DocumentSnapshot document:snapshot.getDocuments()){
            try{
                Reminder reminder=document.toObject(Reminder.class);

                if(reminder!=null){
                    reminder.setId(document.getId());
                    reminders.add(reminder);
                }

            }catch(Exception e){
                System.out.println(
                        "[WARN] Could not read reminder: "
                        +e.getMessage()
                );
            }
        }

        return reminders;
    }

    private void validateUid(String uid){
        if(uid==null||uid.isBlank())
            throw new IllegalArgumentException(
                    "User UID is required."
            );
    }
}