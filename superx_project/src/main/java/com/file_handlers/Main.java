package com.file_handlers;

import com.file_handlers.view.LandingPage;

import javafx.application.Application;


public class Main {
    public static void main(String[] args) {
        System.setProperty("jdk.internal.httpclient.disableHTTP2", "true");
        System.out.println("Hello world!");

        Application.launch(LandingPage.class, args);
    }
}
