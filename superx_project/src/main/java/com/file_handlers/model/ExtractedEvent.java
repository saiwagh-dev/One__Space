package com.file_handlers.model;

/**
 * A single date-bearing event the AI pulled directly out of a file's text —
 * a due date, an expiry date, an appointment, an exam date, etc. Produced by
 * AIClassificationService as part of AIResult, then turned into a Reminder
 * by FileEventSyncService.
 *
 * The AI is instructed to only report dates that are explicitly written in
 * the file; it should never infer or guess a date, so every instance here is
 * expected to be traceable back to real text in the source document.
 */
public class ExtractedEvent {

    private String title;
    private String date;        // ISO format, "YYYY-MM-DD"
    private String type;        // "deadline" | "event" | "task"
    private String description;

    public ExtractedEvent() {
    }

    public ExtractedEvent(String title, String date, String type, String description) {
        this.title = title;
        this.date = date;
        this.type = type;
        this.description = description;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "ExtractedEvent{title='" + title + "', date='" + date + "', type='" + type + "'}";
    }
}