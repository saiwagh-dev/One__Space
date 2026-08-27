package com.file_handlers.service;

public interface ProcessingStatusListener {

    void onTaskStarted(String task);

    void onTaskCompleted(String task);

    default void onTaskFailed(
            String task,
            String message
    ) {
    }
}