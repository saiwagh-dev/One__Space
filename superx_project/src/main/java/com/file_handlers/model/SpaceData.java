package com.file_handlers.model;

import com.google.cloud.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a user-created custom Space (as opposed to the 6 fixed
 * built-in spaces defined in SpaceConstants). Stored per-user under
 * users/{uid}/spaces/{spaceId} in Firestore.
 */
public class SpaceData {

    private String spaceId;
    private String name;
    private String description;
    private List<String> tags;
    private String createdBy;
    private Timestamp createdAt;

    public SpaceData() {
    }

    public SpaceData(String spaceId, String name, String description, List<String> tags, String createdBy) {
        this.spaceId = spaceId;
        this.name = name;
        this.description = description;
        this.tags = tags == null ? new ArrayList<>() : tags;
        this.createdBy = createdBy;
    }

    public String getSpaceId() { return spaceId; }
    public void setSpaceId(String spaceId) { this.spaceId = spaceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("spaceId", spaceId);
        data.put("name", name);
        data.put("description", description);
        data.put("tags", tags);
        data.put("createdBy", createdBy);
        data.put("createdAt", createdAt);
        return data;
    }
}