package com.file_handlers.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.cloud.Timestamp;

import com.file_handlers.dao.ReminderDAO;
import com.file_handlers.model.ExtractedEvent;
import com.file_handlers.model.FileData;
import com.file_handlers.model.Reminder;

public class FileEventSyncService{

    private static final int MAX_EVENTS_PER_FILE=5;

    private final ReminderDAO reminderDAO=new ReminderDAO();

    public void syncEvents(
            String uid,
            FileData file,
            List<ExtractedEvent> extractedEvents
    ){

        if(file==null){
            System.out.println(
                    "[EVENT-SYNC] Skipped: file is null."
            );
            return;
        }

        if(extractedEvents==null||
                extractedEvents.isEmpty()){

            return;
        }

        String fileHash=file.getFileHash();

        if(fileHash==null||fileHash.isBlank()){
            System.out.println(
                    "[EVENT-SYNC] Skipped: file has no hash to key reminders on."
            );
            return;
        }

        int count=Math.min(
                extractedEvents.size(),
                MAX_EVENTS_PER_FILE
        );

        Set<String> savedReminderIds=new HashSet<>();

        for(int i=0;i<count;i++){

            ExtractedEvent event=extractedEvents.get(i);

            if(event==null)
                continue;

            try{

                LocalDate date=LocalDate.parse(
                        event.getDate()
                );

                String reminderId=
                        deterministicId(fileHash,i);

                Reminder reminder=new Reminder();

                reminder.setId(reminderId);

                reminder.setTitle(
                        safeTitle(event.getTitle())
                );

                reminder.setDescription(
                        safeDescription(
                                event.getDescription(),
                                file.getFileName()
                        )
                );

                reminder.setType(
                        toReminderTypeLabel(
                                event.getType()
                        )
                );

                reminder.setDate(
                        toTimestamp(date)
                );

                reminder.setTime(
                        "Not specified"
                );

                reminder.setRepeat(
                        "Does not repeat"
                );

                reminder.setPriority(
                        priorityForType(
                                event.getType()
                        )
                );

                reminder.setLinkedFileId(
                        fileHash
                );

                reminder.setLinkedFileName(
                        file.getFileName()
                );

                reminder.setSource(
                        "ai_extracted"
                );

                reminder.setCreatedAt(
                        Timestamp.now()
                );

                reminderDAO.saveAutoReminder(
                        uid,
                        reminder
                );

                savedReminderIds.add(
                        reminderId
                );

                System.out.println(
                        "[EVENT-SYNC] Saved: "
                                +reminder.getTitle()
                                +" -> "
                                +event.getDate()
                );

            }catch(Exception e){

                System.out.println(
                        "[EVENT-SYNC] Skipped event "
                                +i
                                +" for "
                                +file.getFileName()
                                +": "
                                +e.getMessage()
                );
            }
        }

        removeStaleEvents(
                uid,
                fileHash,
                savedReminderIds
        );
    }

    private void removeStaleEvents(
            String uid,
            String fileHash,
            Set<String> savedReminderIds
    ){

        try{

            List<Reminder> existing=
                    reminderDAO.getAutoRemindersForFile(
                            uid,
                            fileHash
                    );

            for(Reminder reminder:existing){

                if(!savedReminderIds.contains(
                        reminder.getId()
                )){

                    reminderDAO.deleteReminder(
                            uid,
                            reminder.getId()
                    );
                }
            }

        }catch(Exception e){

            System.out.println(
                    "[EVENT-SYNC] Could not clean stale events: "
                            +e.getMessage()
            );
        }
    }

    private String deterministicId(
            String fileHash,
            int index
    ){
        return fileHash+"-evt-"+index;
    }

    private String toReminderTypeLabel(
            String type
    ){

        if(type==null)
            return "Document Reminder";

        return switch(
                type.trim().toLowerCase()
        ){
            case "deadline" ->
                    "Deadline Reminder";

            case "task" ->
                    "Task Reminder";

            case "event" ->
                    "Event Reminder";

            default ->
                    "Document Reminder";
        };
    }

    private String safeTitle(String title){
        return title==null||title.isBlank()
                ?"Untitled event"
                :title.trim();
    }

    private String safeDescription(
            String description,
            String fileName
    ){

        if(description==null||
                description.isBlank()){

            return "Detected in "
                    +fileName
                    +".";
        }

        return description.trim();
    }

    private String priorityForType(
            String type
    ){

        if(type==null)
            return "Medium";

        return "deadline".equalsIgnoreCase(type)
                ?"High"
                :"Medium";
    }

    private Timestamp toTimestamp(
            LocalDate date
    ){

        return Timestamp.of(
                Date.from(
                        date.atStartOfDay(
                                ZoneId.systemDefault()
                        ).toInstant()
                )
        );
    }
}