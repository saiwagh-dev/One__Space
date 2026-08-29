package com.file_handlers.service;

import com.file_handlers.model.AIResult;
import com.file_handlers.util.SpaceConstants;

public class SpaceResolver {
    private static final double CONFIDENCE_THRESHOLD = 0.6;

    public String resolveSpaceId(AIResult result) {
        if (result == null) {
            return SpaceConstants.OTHER;
        }

        if (result.getConfidence() < CONFIDENCE_THRESHOLD) {
            return SpaceConstants.OTHER;
        }

        if (result.getCategory() == null) {
            return SpaceConstants.OTHER;
        }

        return switch (result.getCategory().trim().toLowerCase()) {
            case "personal" -> SpaceConstants.PERSONAL;
            case "college" -> SpaceConstants.COLLEGE;
            case "office" -> SpaceConstants.OFFICE;
            case "finance" -> SpaceConstants.FINANCE;
            case "entertainment" -> SpaceConstants.ENTERTAINMENT;
            case "other", "others" -> SpaceConstants.OTHER;
            default -> SpaceConstants.OTHER;
        };
    }
}