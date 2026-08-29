package com.file_handlers.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.file_handlers.model.Workspace;

public class WorkspaceController {

    private final List<Workspace> workspaces = new ArrayList<>();

    public Workspace createWorkspace(String name, String ownerId) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Workspace name cannot be empty");
        }

        String workspaceId = UUID.randomUUID().toString();

        Workspace workspace = new Workspace(
                workspaceId,
                name.trim(),
                ownerId
        );

        workspaces.add(workspace);

        return workspace;
    }

    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

    public Workspace getWorkspaceById(String workspaceId) {

        for (Workspace workspace : workspaces) {

            if (workspace.getWorkspaceId().equals(workspaceId)) {
                return workspace;
            }
        }

        return null;
    }
}
