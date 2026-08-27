package com.file_handlers.view.userView;

//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
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
//import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.file_handlers.view.LandingPage;

public class AiAssistantPage {

    // Style Constants - Exact Color Hierarchy from UserDashboard
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar: Deep Dark Slate
    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";

    // 2. Center Workspace Canvas: Medium Slate Blue
    private static final String BG_CENTER_CANVAS = "#31435B";

    // 3. Main Cards: Soft Light Blue
    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_CARD_INNER = "#CADDF2";
    private static final String BORDER_CARD = "#C3D6EC";

    // 4. Contrast Typography
    private static final String TEXT_DARK = "#0F172A";        
    private static final String TEXT_MUTED_DARK = "#334155";  
    private static final String TEXT_LIGHT = "#FFFFFF";       
    private static final String TEXT_MUTED_LIGHT = "#94A3B8"; 

    // Accent Colors
    private static final String PRIMARY_BLUE = "#2563EB";

    private final List<String> chatHistory = new ArrayList<>();

    public Scene getAiAssistantPageScene() {

        // =========================================================
        // SIDEBAR
        // =========================================================

        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

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

        VBox navList = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

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
        sidebarProgress.setStyle("-fx-accent: " + PRIMARY_BLUE + "; -fx-control-inner-background: #0E1520;");

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showLandingPage());

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, logoutBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        // =========================================================
        // TOP SEARCH BAR & PROFILE
        // =========================================================

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        TextField searchField = new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-background-color: #141E2C; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 6; -fx-background-radius: 4;");

        HBox searchContainer = new HBox(8, searchIcon, searchField, keyShortcut);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0, 12, 0, 14));
        searchContainer.setPrefWidth(420);
        searchContainer.setStyle("-fx-background-color: #141E2C; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 50%; -fx-text-fill: " + TEXT_LIGHT + "; -fx-font-weight: bold; -fx-font-size: 12px;");

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
        topBar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        // =========================================================
        // AI HEADER
        // =========================================================

        Label aiTitle = new Label("AI Assistant");
        aiTitle.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        aiTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label aiSubtitle = new Label("Grounded in your local index — nothing uploaded to cloud");
        aiSubtitle.setFont(Font.font(FONT, 13));
        aiSubtitle.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        VBox aiHeadingText = new VBox(2, aiTitle, aiSubtitle);
        HBox aiHeader = new HBox(14, aiHeadingText);
        aiHeader.setAlignment(Pos.CENTER_LEFT);

        // =========================================================
        // AI CARD & CONTENT AREA
        // =========================================================

        Button menuButton = new Button("☰  Menu");
        menuButton.setPrefHeight(38);
        menuButton.setPadding(new Insets(0, 16, 0, 16));
        menuButton.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        menuButton.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: " + PRIMARY_BLUE + ";" +
                "-fx-cursor: hand;"
        );

        // AI MENU PANEL
        VBox menuPanel = new VBox(8);
        menuPanel.setPrefWidth(245);
        menuPanel.setMinWidth(245);
        menuPanel.setMaxWidth(245);
        menuPanel.setPadding(new Insets(18));
        menuPanel.setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-width: 0 1 0 0; -fx-background-radius: 16 0 0 16; -fx-border-radius: 16 0 0 16; -fx-effect: dropshadow(three-pass-box, rgba(15,23,42,0.14), 14, 0, 3, 0);");

        Label menuTitle = new Label("AI Assistant Menu");
        menuTitle.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        menuTitle.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label menuSub = new Label("Manage your conversation");
        menuSub.setFont(Font.font(FONT, 11));
        menuSub.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox menuTitleBox = new VBox(3, menuTitle, menuSub);

        Button newChatButton = new Button("＋  New Chat");
        Button chatHistoryButton = new Button("🕒  Chat History");
        Button clearHistoryButton = new Button("🗑  Clear History");
        Button pinChatButton = new Button("📌  Pin Chat");

        Button[] aiMenuButtons = {newChatButton, chatHistoryButton, clearHistoryButton, pinChatButton};
        for (Button button : aiMenuButtons) {
            button.setMaxWidth(Double.MAX_VALUE);
            button.setPrefHeight(40);
            button.setAlignment(Pos.CENTER_LEFT);
            button.setPadding(new Insets(0, 12, 0, 12));
            button.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_DARK + "; -fx-background-radius: 8; -fx-cursor: hand;");
            button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + BG_CARD_INNER + "; -fx-text-fill: " + TEXT_DARK + "; -fx-background-radius: 8; -fx-cursor: hand;"));
            button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_DARK + "; -fx-background-radius: 8; -fx-cursor: hand;"));
        }

        menuPanel.getChildren().addAll(menuTitleBox, newChatButton, chatHistoryButton, clearHistoryButton, pinChatButton);
        menuPanel.setVisible(false);
        menuPanel.setManaged(false);

        // EMPTY STATE / SUGGESTIONS
        Label chatIcon = new Label("💬");
        chatIcon.setFont(Font.font(32));

        Label conversationTitle = new Label("Start a conversation with OneSpace AI");
        conversationTitle.setFont(Font.font(FONT, FontWeight.BOLD, 20));
        conversationTitle.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label conversationDescription = new Label("Ask questions about your files, extracted dates, people, or generate insights directly from your indexed storage.");
        conversationDescription.setFont(Font.font(FONT, 13));
        conversationDescription.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
        conversationDescription.setAlignment(Pos.CENTER);

        Button suggestion1 = createSuggestionButton("✧  When does my passport expire?");
        Button suggestion2 = createSuggestionButton("📄  Summarize recent document");
        Button suggestion3 = createSuggestionButton("🎓  Find my internship certificate");
        Button suggestion4 = createSuggestionButton("☑  Generate a checklist from project files");

        HBox suggestionRow1 = new HBox(10, suggestion1, suggestion2, suggestion3);
        suggestionRow1.setAlignment(Pos.CENTER);
        HBox suggestionRow2 = new HBox(10, suggestion4);
        suggestionRow2.setAlignment(Pos.CENTER);

        VBox suggestionsBox = new VBox(10, suggestionRow1, suggestionRow2);
        suggestionsBox.setAlignment(Pos.CENTER);

        VBox aiEmptyState = new VBox(16, chatIcon, conversationTitle, conversationDescription, suggestionsBox);
        aiEmptyState.setAlignment(Pos.CENTER);
        aiEmptyState.setPadding(new Insets(30, 10, 30, 10));

        // CHAT INPUT FIELD
        TextField aiInput = new TextField();
        aiInput.setPromptText("Ask about any file, date, or document...");
        aiInput.setPrefHeight(48);
        aiInput.setStyle("-fx-background-color: " + BG_CARD_INNER + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 24; -fx-background-radius: 24; -fx-padding: 0 55px 0 55px; -fx-font-size: 13px; -fx-text-fill: " + TEXT_DARK + "; -fx-prompt-text-fill: " + TEXT_MUTED_DARK + ";");

        suggestion1.setOnAction(e -> aiInput.setText("When does my passport expire?"));
        suggestion2.setOnAction(e -> aiInput.setText("Summarize recent document"));
        suggestion3.setOnAction(e -> aiInput.setText("Find my internship certificate"));
        suggestion4.setOnAction(e -> aiInput.setText("Generate a checklist from project files"));

        // PLUS BUTTON & UPLOAD MENU
        Button plusButton = new Button("+");
        plusButton.setPrefSize(34, 34);
        plusButton.setFont(Font.font(FONT, FontWeight.NORMAL, 20));
        plusButton.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 50%; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 50%; -fx-text-fill: " + TEXT_DARK + "; -fx-cursor: hand; -fx-padding: 0;");

        MenuItem uploadFileItem = new MenuItem("📎  Upload File");
        ContextMenu uploadMenu = new ContextMenu(uploadFileItem);
        uploadMenu.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 10; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 10; -fx-padding: 5;");

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
        sendButton.setPrefSize(34, 34);
        sendButton.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        sendButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 50%; -fx-text-fill: #FFFFFF; -fx-cursor: hand;");

        sendButton.setOnAction(e -> {
            String question = aiInput.getText().trim();
            if (!question.isEmpty()) {
                chatHistory.add(question);
                aiInput.clear();
            }
        });

        StackPane inputContainer = new StackPane(aiInput, plusButton, sendButton);
        StackPane.setAlignment(plusButton, Pos.CENTER_LEFT);
        StackPane.setMargin(plusButton, new Insets(0, 0, 0, 10));
        StackPane.setAlignment(sendButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(sendButton, new Insets(0, 8, 0, 0));

        Label disclaimer = new Label("OneSpace AI uses local neural indexing. Always verify important documents.");
        disclaimer.setFont(Font.font(FONT, 11));
        disclaimer.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 500;");
        disclaimer.setAlignment(Pos.CENTER);

        VBox promptArea = new VBox(10, inputContainer, disclaimer);
        promptArea.setAlignment(Pos.CENTER);

        // AI CARD CONTAINER
        HBox menuRow = new HBox(menuButton);
        menuRow.setAlignment(Pos.CENTER_LEFT);

        VBox aiCard = new VBox(16, menuRow, aiEmptyState, promptArea);
        aiCard.setAlignment(Pos.CENTER);
        aiCard.setPadding(new Insets(24));
        aiCard.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 16, 0, 0, 6);"
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
        contentBody.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");
        VBox.setVgrow(contentBody, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: " + BG_CENTER_CANVAS + ";" +
                "-fx-background: " + BG_CENTER_CANVAS + ";" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");
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
        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane logoPane = new StackPane(logoView);
        logoPane.setPrefSize(42, 42);
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
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 12, 0, 12));

        if (isActive) {
            btn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");
            textLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
            textLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #26354A; -fx-background-radius: 8; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;"));
        }

        return btn;
    }

    private Button createSuggestionButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(36);
        button.setPadding(new Insets(0, 14, 0, 14));
        button.setFont(Font.font(FONT, FontWeight.MEDIUM, 12));
        button.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 18;" +
                "-fx-background-radius: 18;" +
                "-fx-text-fill: " + PRIMARY_BLUE + ";" +
                "-fx-cursor: hand;"
        );
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