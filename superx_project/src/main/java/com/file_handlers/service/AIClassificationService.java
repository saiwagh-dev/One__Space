package com.file_handlers.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.file_handlers.model.AIResult;
import com.file_handlers.model.ExtractedEvent;
import com.file_handlers.model.FileData;
import com.file_handlers.model.SpaceData;

public class AIClassificationService {

    private static final int MAX_EXTRACTED_EVENTS = 5;

    private final GeminiClient geminiClient;

    public AIClassificationService() {
        geminiClient = new GeminiClient();
    }

    /**
     * @param file          the file being classified
     * @param customSpaces  the current user's custom Spaces, so the AI can be
     *                      offered them as extra classification targets. Pass
     *                      an empty list (never null) if the user has none.
     */
    public AIResult classify(FileData file, List<SpaceData> customSpaces) {

        try {
            String response =
                    geminiClient.classify(
                            file.getFileName(),
                            file.getExtractedSnippet(),
                            customSpaces
                    );

            JSONObject json =
                    new JSONObject(response);

            String rawCategory =
                    json.optString(
                            "category",
                            "Other"
                    );

            SpaceData matchedCustomSpace =
                    findCustomSpace(rawCategory, customSpaces);

            String category =
                    matchedCustomSpace != null
                            ? matchedCustomSpace.getName()
                            : normalizeCategory(rawCategory);

            double confidence =
                    json.optDouble(
                            "confidence",
                            0.0
                    );

            String description =
                    json.optString(
                            "description",
                            ""
                    ).trim();

            JSONArray tags =
                    json.optJSONArray(
                            "smartTags"
                    );

            String[] smartTags =
                    extractTags(tags);

            if (description.isBlank()) {
                description =
                        "No AI description was generated.";
            }

            AIResult result = new AIResult(
                    category,
                    confidence,
                    description,
                    smartTags
            );

            if (matchedCustomSpace != null) {
                result.setCustomSpaceId(matchedCustomSpace.getSpaceId());
            }

            // NEW: pull out any explicit dates/deadlines the AI found in the file.
            result.setExtractedEvents(
                    extractEvents(json.optJSONArray("extractedEvents"))
            );

            return result;

        } catch (Exception e) {

            System.out.println(
                    "[AI] Failed: " +
                    e.getMessage()
            );

            return new AIResult(
                    "Other",
                    0.0,
                    "AI understanding failed.",
                    new String[0]
            );
        }
    }

    /**
     * Parses the "extractedEvents" JSON array returned by Gemini into
     * ExtractedEvent objects. Any entry with a missing title, a date that
     * isn't valid ISO (YYYY-MM-DD), or an unparseable shape is silently
     * skipped rather than failing the whole classification — a bad date
     * from the AI should never block the file from being saved.
     */
    private List<ExtractedEvent> extractEvents(JSONArray eventsJson) {
        List<ExtractedEvent> events = new ArrayList<>();

        if (eventsJson == null) {
            return events;
        }

        int count = Math.min(eventsJson.length(), MAX_EXTRACTED_EVENTS);

        for (int i = 0; i < count; i++) {
            try {
                JSONObject eventJson = eventsJson.getJSONObject(i);

                String title = eventJson.optString("title", "").trim();
                String date = eventJson.optString("date", "").trim();
                String type = eventJson.optString("type", "event").trim();
                String description = eventJson.optString("description", "").trim();

                if (title.isBlank() || date.isBlank()) {
                    continue;
                }

                // Validate strictly — a malformed or hallucinated date must
                // never reach the calendar.
                LocalDate.parse(date);

                events.add(new ExtractedEvent(title, date, normalizeEventType(type), description));

            } catch (DateTimeParseException e) {
                System.out.println("[AI] Skipped extracted event with invalid date: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[AI] Skipped malformed extracted event: " + e.getMessage());
            }
        }

        return events;
    }

    private String normalizeEventType(String type) {
        if (type == null) return "event";

        return switch (type.trim().toLowerCase(Locale.ROOT)) {
            case "deadline" -> "deadline";
            case "task" -> "task";
            default -> "event";
        };
    }

    /**
     * Checks whether the category text the AI returned is an exact
     * (case-insensitive) match for one of the user's custom Space names.
     */
    private SpaceData findCustomSpace(String category, List<SpaceData> customSpaces) {
        if (category == null || category.isBlank() || customSpaces == null) {
            return null;
        }

        String normalized = category.trim().toLowerCase(Locale.ROOT);

        for (SpaceData space : customSpaces) {
            if (space.getName() != null
                    && space.getName().trim().toLowerCase(Locale.ROOT).equals(normalized)) {
                return space;
            }
        }

        return null;
    }

    private String normalizeCategory(String category) {

        if (category == null) {
            return "Other";
        }

        return switch (
                category.trim().toLowerCase()
        ) {
            case "personal" ->
                    "Personal";
            case "college" ->
                    "College";
            case "office" ->
                    "Office";
            case "finance" ->
                    "Finance";
            case "entertainment" ->
                    "Entertainment";
            case "other", "others" ->
                    "Other";
            default ->
                    "Other";
        };
    }

    private String[] extractTags(JSONArray tags) {

        if (tags == null) {
            return new String[0];
        }

        int count =
                Math.min(
                        tags.length(),
                        6
                );

        String[] result =
                new String[count];

        for (int i = 0; i < count; i++) {
            result[i] =
                    tags.optString(i, "").trim();
        }

        return result;
    }
}