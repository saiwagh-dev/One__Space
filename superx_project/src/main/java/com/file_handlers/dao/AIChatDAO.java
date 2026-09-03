package com.file_handlers.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.file_handlers.config.FirebaseConfig;
import com.file_handlers.model.AIChatData;
import com.file_handlers.model.AIMessageData;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;

public class AIChatDAO{

    private static final String USERS_COLLECTION="users";
    private static final String CHATS_COLLECTION="aiChats";
    private static final String MESSAGES_COLLECTION="messages";

    private Firestore getFirestore(){
        return FirebaseConfig.getFirestore();
    }

    private CollectionReference getUserChatsCollection(String uid){
        validateUid(uid);

        return getFirestore()
                .collection(USERS_COLLECTION)
                .document(uid)
                .collection(CHATS_COLLECTION);
    }

    private CollectionReference getMessagesCollection(String uid,String chatId){
        if(chatId==null||chatId.isBlank())
            throw new IllegalArgumentException("Chat ID is required.");

        return getUserChatsCollection(uid)
                .document(chatId)
                .collection(MESSAGES_COLLECTION);
    }

    public String createChat(String uid,String title)throws Exception{
        validateUid(uid);

        String chatId=getUserChatsCollection(uid)
                .document()
                .getId();

        Timestamp now=Timestamp.now();

        Map<String,Object> data=new HashMap<>();
        data.put(
                "title",
                title==null||title.isBlank()
                        ?"New Chat"
                        :title.trim()
        );
        data.put("createdAt",now);
        data.put("updatedAt",now);

        getUserChatsCollection(uid)
                .document(chatId)
                .set(data)
                .get();

        return chatId;
    }

    public void updateChatTitle(
            String uid,
            String chatId,
            String title
    )throws Exception{

        validateUid(uid);

        if(chatId==null||chatId.isBlank())
            throw new IllegalArgumentException("Chat ID is required.");

        Map<String,Object> data=new HashMap<>();

        data.put(
                "title",
                title==null||title.isBlank()
                        ?"New Chat"
                        :title.trim()
        );

        data.put("updatedAt",Timestamp.now());

        getUserChatsCollection(uid)
                .document(chatId)
                .update(data)
                .get();
    }

    public void touchChat(String uid,String chatId)throws Exception{
        validateUid(uid);

        if(chatId==null||chatId.isBlank())
            throw new IllegalArgumentException("Chat ID is required.");

        getUserChatsCollection(uid)
                .document(chatId)
                .update("updatedAt",Timestamp.now())
                .get();
    }

    public void saveMessage(
            String uid,
            String chatId,
            String role,
            String content
    )throws Exception{

        validateUid(uid);

        if(chatId==null||chatId.isBlank())
            throw new IllegalArgumentException("Chat ID is required.");

        if(role==null||role.isBlank())
            throw new IllegalArgumentException("Message role is required.");

        if(content==null)
            content="";

        String messageId=getMessagesCollection(uid,chatId)
                .document()
                .getId();

        Map<String,Object> data=new HashMap<>();

        data.put("role",role);
        data.put("content",content);
        data.put("timestamp",Timestamp.now());

        getMessagesCollection(uid,chatId)
                .document(messageId)
                .set(data)
                .get();

        touchChat(uid,chatId);
    }

    public List<AIChatData> getChats(String uid)throws Exception{
        validateUid(uid);

        QuerySnapshot snapshot=getUserChatsCollection(uid)
                .orderBy(
                        "updatedAt",
                        Query.Direction.DESCENDING
                )
                .get()
                .get();

        List<AIChatData> chats=new ArrayList<>();

        for(DocumentSnapshot document:snapshot.getDocuments()){
            if(!document.exists())
                continue;

            Timestamp created=document.getTimestamp("createdAt");
            Timestamp updated=document.getTimestamp("updatedAt");

            chats.add(
                    new AIChatData(
                            document.getId(),
                            document.getString("title"),
                            created==null
                                    ?null
                                    :created.toSqlTimestamp().toInstant(),
                            updated==null
                                    ?null
                                    :updated.toSqlTimestamp().toInstant()
                    )
            );
        }

        return chats;
    }

    public List<AIMessageData> getMessages(
            String uid,
            String chatId
    )throws Exception{

        validateUid(uid);

        if(chatId==null||chatId.isBlank())
            throw new IllegalArgumentException("Chat ID is required.");

        QuerySnapshot snapshot=getMessagesCollection(uid,chatId)
                .orderBy(
                        "timestamp",
                        Query.Direction.ASCENDING
                )
                .get()
                .get();

        List<AIMessageData> messages=new ArrayList<>();

        for(DocumentSnapshot document:snapshot.getDocuments()){
            if(!document.exists())
                continue;

            Timestamp timestamp=document.getTimestamp("timestamp");

            messages.add(
                    new AIMessageData(
                            document.getId(),
                            document.getString("role"),
                            document.getString("content"),
                            timestamp==null
                                    ?null
                                    :timestamp.toSqlTimestamp().toInstant()
                    )
            );
        }

        return messages;
    }

    public void deleteChat(
            String uid,
            String chatId
    )throws Exception{

        validateUid(uid);

        if(chatId==null||chatId.isBlank())
            throw new IllegalArgumentException("Chat ID is required.");

        List<AIMessageData> messages=getMessages(uid,chatId);

        for(AIMessageData message:messages){
            getMessagesCollection(uid,chatId)
                    .document(message.getMessageId())
                    .delete()
                    .get();
        }

        getUserChatsCollection(uid)
                .document(chatId)
                .delete()
                .get();
    }

    private void validateUid(String uid){
        if(uid==null||uid.isBlank())
            throw new IllegalArgumentException("User ID is required.");
    }
}