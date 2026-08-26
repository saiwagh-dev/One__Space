package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AiAssistantPage {

    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // Palette & Glass Styling matching Reference UI
    private static final String BG_ROOT = "radial-gradient(center 70% 20%, radius 80%, #0d1f3d 0%, #060b14 60%, #03060a 100%)";
    private static final String BG_SIDEBAR = "#070c16";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";

    private static final String CARD_GLASS_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BORDER = "linear-gradient(to bottom right, rgba(56, 189, 248, 0.35), rgba(37, 99, 235, 0.15))";

    private static final String INNER_CONTAINER_BG = "rgba(10, 18, 33, 0.8)";
    private static final String INNER_BORDER = "rgba(56, 189, 248, 0.2)";

    private static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#94A3B8";
    private static final String TEXT_SECONDARY = "#64748B";

    private final List<String> chatHistory = new ArrayList<>();

    public Scene getAiAssistantPageScene() {

        // =========================================================
        // SIDEBAR
        // =========================================================

        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label logoSub = new Label("Local • AI indexed");
        logoSub.setFont(Font.font(FONT, FontWeight.MEDIUM, 10));
        logoSub.setStyle("-fx-text-fill: " + TEXT_SECONDARY + ";");

        VBox logoTextBox = new VBox(1, logoText, logoSub);

        HBox logoHeader = new HBox(12, logoIcon, logoTextBox);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(6, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", true);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);
        Button logoutBtn = createSidebarButton("🚪", "Logout", false);

        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        aiBtn.setOnAction(e -> LandingPage.showAiAssistantPage());
        collabBtn.setOnAction(e -> LandingPage.showCollaborationPage());
        recentBtn.setOnAction(e -> LandingPage.showRecentPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());
        settingsBtn.setOnAction(e -> LandingPage.showSettingPage());
        logoutBtn.setOnAction(e -> LandingPage.showUserLoginPage());

        VBox navList = new VBox(5, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

        // Sidebar Storage Card
        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        storageTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox storageValGroup = new HBox(storageVal, new Region(), storagePercent);
        HBox.setHgrow(storageValGroup.getChildren().get(1), Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle(
                "-fx-accent: linear-gradient(to right, #0284c7, #38bdf8);" +
                "-fx-control-inner-background: #0b1526;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 0;"
        );

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #38bdf8; -fx-padding: 3 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexedPage());

        VBox storageCard = new VBox(9, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle(
                "-fx-background-color: rgba(14, 24, 43, 0.9);" +
                "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 4);"
        );

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(10, logoBox, navList, sidebarSpacer, settingsBtn, logoutBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(235);
        sidebar.setMinWidth(235);
        sidebar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        // =========================================================
        // TOP SEARCH BAR & PROFILE
        // =========================================================

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle("-fx-text-fill: " + TEXT_SECONDARY + ";");

        TextField searchField = new TextField();
        searchField.setPromptText("Ask OneSpace anything — \"invoices from June\", \"Java notes\"...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-background-color: rgba(255, 255, 255, 0.06); -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 7; -fx-background-radius: 5; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 5;");

        HBox searchContainer = new HBox(10, searchIcon, searchField, keyShortcut);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0, 12, 0, 14));
        searchContainer.setPrefWidth(520);
        searchContainer.setStyle(
                "-fx-background-color: rgba(13, 22, 38, 0.85);" +
                "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;"
        );
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 15px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF);" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.5), 10, 0, 0, 2);"
        );

        Label userName = new Label("Aarav Verma");
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label dropDown = new Label("⌄");
        dropDown.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox profileBox = new HBox(10, bellBtn, avatar, userName, dropDown);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, searchContainer, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        // =========================================================
        // AI HEADER
        // =========================================================

        Label aiTitle = new Label("AI Assistant");
        aiTitle.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        aiTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label aiSubtitle = new Label("Grounded in your local index — nothing uploaded to cloud");
        aiSubtitle.setFont(Font.font(FONT, 13));
        aiSubtitle.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        VBox aiHeadingText = new VBox(3, aiTitle, aiSubtitle);
        HBox aiHeader = new HBox(14, aiHeadingText);
        aiHeader.setAlignment(Pos.CENTER_LEFT);

        // =========================================================
        // AI CARD & CONTENT AREA
        // =========================================================

        Button menuButton = new Button("☰  Menu");
        menuButton.setPrefHeight(36);
        menuButton.setPadding(new Insets(0, 16, 0, 16));
        menuButton.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        menuButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.05);" +
                "-fx-border-color: rgba(255, 255, 255, 0.12);" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-cursor: hand;"
        );

        // AI MENU PANEL
        VBox menuPanel = new VBox(8);
        menuPanel.setPrefWidth(245);
        menuPanel.setMinWidth(245);
        menuPanel.setMaxWidth(245);
        menuPanel.setPadding(new Insets(18));
        menuPanel.setStyle(
                "-fx-background-color: #0c1626;" +
                "-fx-border-color: rgba(56, 189, 248, 0.2);" +
                "-fx-border-width: 0 1 0 0;" +
                "-fx-background-radius: 20 0 0 20;" +
                "-fx-border-radius: 20 0 0 20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 20, 0, 6, 0);"
        );

        Label menuTitle = new Label("AI Assistant Menu");
        menuTitle.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        menuTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label menuSub = new Label("Manage your conversation");
        menuSub.setFont(Font.font(FONT, 11));
        menuSub.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        VBox menuTitleBox = new VBox(3, menuTitle, menuSub);

        Button newChatButton = new Button("＋  New Chat");
        Button chatHistoryButton = new Button("🕒  Chat History");
        Button clearHistoryButton = new Button("🗑  Clear History");
        Button pinChatButton = new Button("📌  Pin Chat");

        Button[] aiMenuButtons = {newChatButton, chatHistoryButton, clearHistoryButton, pinChatButton};
        for (Button button : aiMenuButtons) {
            button.setMaxWidth(Double.MAX_VALUE);
            button.setPrefHeight(38);
            button.setAlignment(Pos.CENTER_LEFT);
            button.setPadding(new Insets(0, 12, 0, 12));
            button.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_LIGHT + "; -fx-background-radius: 8; -fx-cursor: hand;");
            button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-text-fill: " + TEXT_LIGHT + "; -fx-background-radius: 8; -fx-cursor: hand;"));
            button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_LIGHT + "; -fx-background-radius: 8; -fx-cursor: hand;"));
        }

        menuPanel.getChildren().addAll(menuTitleBox, newChatButton, chatHistoryButton, clearHistoryButton, pinChatButton);
        menuPanel.setVisible(false);
        menuPanel.setManaged(false);

        // EMPTY STATE / SUGGESTIONS
        Label chatIcon = new Label("✦");
        chatIcon.setFont(Font.font(40));
        chatIcon.setStyle(
                "-fx-text-fill: #38bdf8;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(56, 189, 248, 0.7), 20, 0, 0, 0);"
        );

        Label conversationTitle = new Label("Start a conversation with OneSpace AI");
        conversationTitle.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        conversationTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + "; -fx-padding: 4 0 0 0;");

        Label conversationDescription = new Label("Ask questions about your files, extracted dates, people, or generate insights directly from your indexed storage.");
        conversationDescription.setFont(Font.font(FONT, 13));
        conversationDescription.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
        conversationDescription.setAlignment(Pos.CENTER);

        Button suggestion1 = createSuggestionButton("✧  When does my passport expire?");
        Button suggestion2 = createSuggestionButton("📄  Summarize recent document");
        Button suggestion3 = createSuggestionButton("🎓  Find my internship certificate");
        Button suggestion4 = createSuggestionButton("☑  Generate a checklist from project files");

        HBox suggestionRow1 = new HBox(12, suggestion1, suggestion2, suggestion3);
        suggestionRow1.setAlignment(Pos.CENTER);
        HBox suggestionRow2 = new HBox(12, suggestion4);
        suggestionRow2.setAlignment(Pos.CENTER);

        VBox suggestionsBox = new VBox(12, suggestionRow1, suggestionRow2);
        suggestionsBox.setAlignment(Pos.CENTER);
        suggestionsBox.setPadding(new Insets(10, 0, 0, 0));

        VBox aiEmptyState = new VBox(14, chatIcon, conversationTitle, conversationDescription, suggestionsBox);
        aiEmptyState.setAlignment(Pos.CENTER);
        aiEmptyState.setPadding(new Insets(40, 10, 40, 10));

        // CHAT INPUT FIELD
        TextField aiInput = new TextField();
        aiInput.setPromptText("Ask about any file, date, or document...");
        aiInput.setPrefHeight(52);
        aiInput.setStyle(
                "-fx-background-color: " + INNER_CONTAINER_BG + ";" +
                "-fx-border-color: " + INNER_BORDER + ";" +
                "-fx-border-radius: 26;" +
                "-fx-background-radius: 26;" +
                "-fx-padding: 0 58px 0 58px;" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-prompt-text-fill: " + TEXT_SECONDARY + ";" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.4), 12, 0, 0, 4);"
        );

        suggestion1.setOnAction(e -> aiInput.setText("When does my passport expire?"));
        suggestion2.setOnAction(e -> aiInput.setText("Summarize recent document"));
        suggestion3.setOnAction(e -> aiInput.setText("Find my internship certificate"));
        suggestion4.setOnAction(e -> aiInput.setText("Generate a checklist from project files"));

        // PLUS BUTTON & UPLOAD MENU
        Button plusButton = new Button("+");
        plusButton.setPrefSize(36, 36);
        plusButton.setFont(Font.font(FONT, FontWeight.NORMAL, 20));
        plusButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.08);" +
                "-fx-background-radius: 50%;" +
                "-fx-border-color: rgba(255, 255, 255, 0.12);" +
                "-fx-border-radius: 50%;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0;"
        );

        MenuItem uploadFileItem = new MenuItem("📎  Upload File");
        ContextMenu uploadMenu = new ContextMenu(uploadFileItem);
        uploadMenu.setStyle(
                "-fx-background-color: #0d1a2d;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(56, 189, 248, 0.2);" +
                "-fx-border-radius: 10;" +
                "-fx-padding: 4;"
        );

        uploadFileItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File from Computer");
            File selectedFile = fileChooser.showOpenDialog(aiInput.getScene().getWindow());
            if (selectedFile != null) {
                System.out.println("Selected File: " + selectedFile.getAbsolutePath());
            }
        });

        plusButton.setOnAction(e -> {
            if (uploadMenu.isShowing()) {
                uploadMenu.hide();
            } else {
                uploadMenu.show(plusButton, javafx.geometry.Side.TOP, 0, -5);
            }
        });

        // SEND BUTTON
        Button sendButton = new Button("➔");
        sendButton.setPrefSize(36, 36);
        sendButton.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        sendButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #1d4ed8, #0284c7);" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(2, 132, 199, 0.6), 14, 0, 0, 2);"
        );

        sendButton.setOnAction(e -> {
            String question = aiInput.getText().trim();
            if (!question.isEmpty()) {
                chatHistory.add(question);
                aiInput.clear();
            }
        });

        StackPane inputContainer = new StackPane(aiInput, plusButton, sendButton);
        StackPane.setAlignment(plusButton, Pos.CENTER_LEFT);
        StackPane.setMargin(plusButton, new Insets(0, 0, 0, 8));
        StackPane.setAlignment(sendButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(sendButton, new Insets(0, 8, 0, 0));

        Label disclaimer = new Label("OneSpace AI uses local neural indexing. Always verify important documents.");
        disclaimer.setFont(Font.font(FONT, 11));
        disclaimer.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-weight: 500;");
        disclaimer.setAlignment(Pos.CENTER);

        VBox promptArea = new VBox(12, inputContainer, disclaimer);
        promptArea.setAlignment(Pos.CENTER);

        // AI CARD CONTAINER
        HBox menuRow = new HBox(menuButton);
        menuRow.setAlignment(Pos.CENTER_LEFT);

        VBox aiCard = new VBox(16, menuRow, aiEmptyState, promptArea);
        aiCard.setAlignment(Pos.CENTER);
        aiCard.setPadding(new Insets(26));
        aiCard.setStyle(
                "-fx-background-color: " + CARD_GLASS_BG + ";" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-border-width: 1.2;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 24, 0, 0, 10);"
        );
        VBox.setVgrow(aiEmptyState, Priority.ALWAYS);
        VBox.setVgrow(aiCard, Priority.ALWAYS);

        StackPane chatCardContainer = new StackPane(aiCard, menuPanel);
        StackPane.setAlignment(menuPanel, Pos.CENTER_LEFT);
        VBox.setVgrow(chatCardContainer, Priority.ALWAYS);

        // MENU ACTIONS
        menuButton.setOnAction(e -> {
            boolean open = menuPanel.isVisible();
            menuPanel.setVisible(!open);
            menuPanel.setManaged(!open);
        });

        newChatButton.setOnAction(e -> {
            aiInput.clear();
            chatHistory.clear();
            menuPanel.setVisible(false);
            menuPanel.setManaged(false);
        });

        chatHistoryButton.setOnAction(e -> {
            if (chatHistory.isEmpty()) {
                showInfo("Chat History", "No chat history yet.");
            } else {
                StringBuilder historyText = new StringBuilder("Previous questions:\n\n");
                for (int i = 0; i < chatHistory.size(); i++) {
                    historyText.append(i + 1).append(". ").append(chatHistory.get(i)).append("\n");
                }
                showInfo("Chat History", historyText.toString());
            }
        });

        clearHistoryButton.setOnAction(e -> {
            chatHistory.clear();
            showInfo("Clear History", "Chat history has been cleared.");
        });

        pinChatButton.setOnAction(e -> {
            if (pinChatButton.getText().contains("Pin Chat")) {
                pinChatButton.setText("📌  Unpin Chat");
            } else {
                pinChatButton.setText("📌  Pin Chat");
            }
        });

        // =========================================================
        // MAIN LAYOUT SCROLLPANING & ROOT BORDERPANE
        // =========================================================

        VBox contentBody = new VBox(22, aiHeader, chatCardContainer);
        contentBody.setPadding(new Insets(24, 28, 28, 28));
        contentBody.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(contentBody, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background: " + BG_ROOT + "; -fx-background-color: " + BG_ROOT + ";");
        VBox.setVgrow(mainArea, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_SIDEBAR + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1200, 750);
    }

    private StackPane createOneSpaceLogo() {
        Image logoImage = new Image(
                getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png")
        );

        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(38);
        logoView.setFitHeight(38);
        logoView.setPreserveRatio(true);

        StackPane logoPane = new StackPane(logoView);
        logoPane.setPrefSize(38, 38);
        logoPane.setAlignment(Pos.CENTER);

        return logoPane;
    }

    private Button createSidebarButton(String icon, String label, boolean isActive) {
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(FONT, 14));

        Label textLbl = new Label(label);
        textLbl.setFont(Font.font(FONT, isActive ? FontWeight.BOLD : FontWeight.MEDIUM, 13));

        HBox content = new HBox(12, iconLbl, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(40);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 14, 0, 14));

        if (isActive) {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #1d4ed8, #2563eb);" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                    "-fx-border-radius: 12;" +
                    "-fx-border-width: 1;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.55), 14, 0, 0, 2);"
            );
            iconLbl.setStyle("-fx-text-fill: #FFFFFF;");
            textLbl.setStyle("-fx-text-fill: #FFFFFF;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
            textLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

            btn.setOnMouseEntered(e -> {
                btn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 12; -fx-cursor: hand;");
                iconLbl.setStyle("-fx-text-fill: #FFFFFF;");
                textLbl.setStyle("-fx-text-fill: #FFFFFF;");
            });
            btn.setOnMouseExited(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand;");
                iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
                textLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
            });
        }

        return btn;
    }

    private Button createSuggestionButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(38);
        button.setPadding(new Insets(0, 16, 0, 16));
        button.setFont(Font.font(FONT, FontWeight.MEDIUM, 12));
        button.setStyle(
                "-fx-background-color: rgba(14, 26, 46, 0.9);" +
                "-fx-border-color: rgba(56, 189, 248, 0.25);" +
                "-fx-border-radius: 19;" +
                "-fx-background-radius: 19;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 8, 0, 0, 2);"
        );

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: rgba(23, 42, 77, 0.95);" +
                "-fx-border-color: #38bdf8;" +
                "-fx-border-radius: 19;" +
                "-fx-background-radius: 19;" +
                "-fx-text-fill: #38bdf8;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(56, 189, 248, 0.35), 10, 0, 0, 2);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: rgba(14, 26, 46, 0.9);" +
                "-fx-border-color: rgba(56, 189, 248, 0.25);" +
                "-fx-border-radius: 19;" +
                "-fx-background-radius: 19;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 8, 0, 0, 2);"
        ));

        return button;
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}