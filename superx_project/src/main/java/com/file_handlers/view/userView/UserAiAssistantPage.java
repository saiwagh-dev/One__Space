package com.file_handlers.view.userView;

////import com.file_handler.Main;

import com.file_handlers.view.LandingPage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

public class UserAiAssistantPage {

    // =========================================================
    // THEME - SAME AS NOTIFICATION PAGE
    // =========================================================

    private static final String FONT =
            "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";

    private static final String BG_CENTER_CANVAS = "#31435B";

    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_CARD_INNER = "#CADDF2";
    private static final String BORDER_CARD = "#C3D6EC";

    private static final String TEXT_DARK = "#0F172A";
    private static final String TEXT_MUTED_DARK = "#334155";

    private static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#94A3B8";

    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String PRIMARY_PURPLE = "#6366F1";

    private final List<String> chatHistory = new ArrayList<>();

    public Scene getAiAssistantPageScene() {

        // =========================================================
        // LEFT SIDEBAR
        // =========================================================

        Label logoIcon = new Label("◉");

        logoIcon.setFont(
                Font.font(FONT, FontWeight.BOLD, 22)
        );

        logoIcon.setTextFill(
                Color.web("#60A5FA")
        );

        Label logoText = new Label("OneSpace");

        logoText.setFont(
                Font.font(FONT, FontWeight.BOLD, 18)
        );

        logoText.setTextFill(
                Color.web(TEXT_LIGHT)
        );

        HBox logoHeader = new HBox(
                8,
                logoIcon,
                logoText
        );

        logoHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        Label tagline = new Label(
                "Local · AI Indexed"
        );

        tagline.setFont(
                Font.font(FONT, 11)
        );

        tagline.setTextFill(
                Color.web(TEXT_MUTED_LIGHT)
        );

        VBox logoBox = new VBox(
                2,
                logoHeader,
                tagline
        );

        logoBox.setPadding(
                new Insets(0, 0, 15, 8)
        );

        // =========================================================
        // SIDEBAR BUTTONS
        // =========================================================

        Button dashboardBtn =
                createSidebarButton("⌂", "Dashboard", false);

        Button spacesBtn =
                createSidebarButton("▦", "Spaces", false);

        Button searchBtn =
                createSidebarButton("⌕", "Search", false);

        Button calendarBtn =
                createSidebarButton("□", "Calendar", false);

        Button aiBtn =
                createSidebarButton("✧", "AI Assistant", true);

        Button collabBtn =
                createSidebarButton("♧", "Collaboration", false);

        Button recentBtn =
                createSidebarButton("◷", "Recent", false);

        Button trashBtn =
                createSidebarButton("♜", "Trash", false);

        Button settingsBtn =
                createSidebarButton("⚙", "Settings", false);

        dashboardBtn.setOnAction(
                e -> LandingPage.showUserDashboard()
        );

        spacesBtn.setOnAction(
                e -> LandingPage.showUserSpace()
        );

        //searchBtn.setOnAction(  e -> LandingPage.showSearchPage() );

        //calendarBtn.setOnAction(  e -> Main.showCalendarPage() );

      //  aiBtn.setOnAction(   e -> Main.showAiAssistantPage()  );

     //   collabBtn.setOnAction(  e -> Main.showCollaborationPage() );

      //  recentBtn.setOnAction(  e -> Main.showRecentPage()  );

      //  trashBtn.setOnAction(  e -> Main.showTrashPage()  );

       settingsBtn.setOnAction(   e -> LandingPage.showUserSetting()  );

        VBox navList = new VBox(
                4,
                dashboardBtn,
                spacesBtn,
                searchBtn,
                calendarBtn,
                aiBtn,
                collabBtn,
                recentBtn,
                trashBtn
        );

        // =========================================================
        // STORAGE CARD
        // =========================================================

        Label storageBadge =
                new Label("✧  Storage indexed");

        storageBadge.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        11
                )
        );

        storageBadge.setTextFill(
                Color.web("#60A5FA")
        );

        Label storageVal =
                new Label("64.2 GB");

        storageVal.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        16
                )
        );

        storageVal.setTextFill(
                Color.web(TEXT_LIGHT)
        );

        Label storageSub =
                new Label("of 100 GB used");

        storageSub.setFont(
                Font.font(FONT, 11)
        );

        storageSub.setTextFill(
                Color.web(TEXT_MUTED_LIGHT)
        );

        VBox storageTextGroup =
                new VBox(
                        1,
                        storageVal,
                        storageSub
                );

        ProgressBar sidebarProgress =
                new ProgressBar(0.64);

        sidebarProgress.setMaxWidth(
                Double.MAX_VALUE
        );

        sidebarProgress.setPrefHeight(6);

        sidebarProgress.setStyle(
                "-fx-accent: " + PRIMARY_BLUE + ";" +
                "-fx-control-inner-background: #0E1520;"
        );

        Label storageInfo = new Label(
                "Files stay in place —\n" +
                "nothing moved or renamed."
        );

        storageInfo.setFont(
                Font.font(FONT, 11)
        );

        storageInfo.setTextFill(
                Color.web(TEXT_MUTED_LIGHT)
        );

        VBox storageCard = new VBox(
                10,
                storageBadge,
                storageTextGroup,
                sidebarProgress,
                storageInfo
        );

        storageCard.setPadding(
                new Insets(14)
        );

     ////   storageCard.setOnMouseClicked(e -> LandingPage.showStorageIndexed()  );

        storageCard.setStyle(
                "-fx-background-color: " +
                        BG_SIDEBAR_CARD + ";" +
                "-fx-border-color: " +
                        SIDEBAR_BORDER + ";" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;"
        );

        Region sidebarSpacer = new Region();

        VBox.setVgrow(
                sidebarSpacer,
                Priority.ALWAYS
        );

        VBox sidebar = new VBox(
                10,
                logoBox,
                navList,
                sidebarSpacer,
                settingsBtn,
                storageCard
        );

        sidebar.setPadding(
                new Insets(20, 14, 20, 14)
        );

        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setMaxWidth(230);

        sidebar.setStyle(
                "-fx-background-color: " +
                        BG_SIDEBAR + ";" +
                "-fx-border-color: " +
                        SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 1 0 0;"
        );

        // =========================================================
        // TOP BAR
        // =========================================================

        Button bellBtn = new Button("🔔");

        bellBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-font-size: 16px;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-cursor: hand;"
        );

        bellBtn.setOnAction(
                e -> LandingPage.showUserNotificationPage()
        );

        Label avatar = new Label("AV");

        avatar.setPrefSize(34, 34);

        avatar.setAlignment(
                Pos.CENTER
        );

        avatar.setStyle(
                "-fx-background-color: #263B58;" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: #93C5FD;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;"
        );

        Label userName =
                new Label("Aarav Verma");

        userName.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        13
                )
        );

        userName.setTextFill(
                Color.web(TEXT_LIGHT)
        );

        Label dropDown =
                new Label("⌄");

        dropDown.setTextFill(
                Color.web(TEXT_MUTED_LIGHT)
        );

        HBox profileBox = new HBox(
                8,
                bellBtn,
                avatar,
                userName,
                dropDown
        );

        profileBox.setAlignment(
                Pos.CENTER
        );

        Region topSpacer = new Region();

        HBox topBar = new HBox(
                topSpacer,
                profileBox
        );

        HBox.setHgrow(
                topSpacer,
                Priority.ALWAYS
        );

        topBar.setAlignment(
                Pos.CENTER_RIGHT
        );

        topBar.setPadding(
                new Insets(16, 24, 12, 24)
        );

        topBar.setStyle(
                "-fx-background-color: " +
                        BG_SIDEBAR + ";" +
                "-fx-border-color: " +
                        SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 0 1 0;"
        );

        // =========================================================
        // AI HEADER
        // =========================================================

        Label aiBadgeIcon = new Label("✧");

        aiBadgeIcon.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        20
                )
        );

        aiBadgeIcon.setTextFill(
                Color.WHITE
        );

        aiBadgeIcon.setPrefSize(
                42,
                42
        );

        aiBadgeIcon.setAlignment(
                Pos.CENTER
        );

        aiBadgeIcon.setStyle(
                "-fx-background-color: linear-gradient(" +
                "to bottom right, #3B82F6, #2563EB);" +
                "-fx-background-radius: 50%;" +
                "-fx-effect: dropshadow(" +
                "three-pass-box, rgba(37,99,235,0.35), " +
                "8, 0, 0, 3);"
        );

        Label aiTitle =
                new Label("AI Assistant");

        aiTitle.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        20
                )
        );

        aiTitle.setTextFill(
                Color.web(TEXT_LIGHT)
        );

        Label aiSubtitle = new Label(
                "Grounded in your local index  ·  nothing uploaded to cloud"
        );

        aiSubtitle.setFont(
                Font.font(FONT, 12)
        );

        aiSubtitle.setTextFill(
                Color.web(TEXT_MUTED_LIGHT)
        );

        VBox aiHeadingText = new VBox(
                2,
                aiTitle,
                aiSubtitle
        );

        HBox aiHeader = new HBox(
                14,
                aiBadgeIcon,
                aiHeadingText
        );

        aiHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        // =========================================================
        // MENU BUTTON
        // =========================================================

        Button menuButton =
                new Button("☰  Menu");

        menuButton.setPrefHeight(38);

        menuButton.setPadding(
                new Insets(0, 16, 0, 16)
        );

        menuButton.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        13
                )
        );

        menuButton.setTextFill(
                Color.web(PRIMARY_BLUE)
        );

        String menuIdleStyle =
                "-fx-background-color: #EAF2FC;" +
                "-fx-border-color: #93C5FD;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;";

        String menuHoverStyle =
                "-fx-background-color: #DBEAFE;" +
                "-fx-border-color: " +
                        PRIMARY_BLUE + ";" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;";

        menuButton.setStyle(
                menuIdleStyle
        );

        menuButton.setOnMouseEntered(
                e -> menuButton.setStyle(menuHoverStyle)
        );

        menuButton.setOnMouseExited(
                e -> menuButton.setStyle(menuIdleStyle)
        );

        // =========================================================
        // AI MENU PANEL
        // =========================================================

        VBox menuPanel = new VBox(8);

        menuPanel.setPrefWidth(245);
        menuPanel.setMinWidth(245);
        menuPanel.setMaxWidth(245);

        menuPanel.setPadding(
                new Insets(18)
        );

        menuPanel.setStyle(
                "-fx-background-color: #DDE8F8;" +
                "-fx-border-color: #C3D6EC;" +
                "-fx-border-width: 0 1 0 0;" +
                "-fx-background-radius: 16 0 0 16;" +
                "-fx-border-radius: 16 0 0 16;" +
                "-fx-effect: dropshadow(" +
                "three-pass-box, rgba(0,0,0,0.20), " +
                "14, 0, 3, 0);"
        );

        Label menuTitle =
                new Label("AI Assistant Menu");

        menuTitle.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        16
                )
        );

        menuTitle.setTextFill(
                Color.web(TEXT_DARK)
        );

        Label menuSubtitle =
                new Label("Manage your AI conversation");

        menuSubtitle.setFont(
                Font.font(FONT, 11)
        );

        menuSubtitle.setTextFill(
                Color.web(TEXT_MUTED_DARK)
        );

        VBox menuTitleBox =
                new VBox(
                        3,
                        menuTitle,
                        menuSubtitle
                );

        Button newChatButton =
                new Button("＋  New Chat");

        Button chatHistoryButton =
                new Button("◷  Chat History");

        Button clearHistoryButton =
                new Button("⌫  Clear History");

        Button pinChatButton =
                new Button("📌  Pin Chat");

        Button[] aiMenuButtons = {
                newChatButton,
                chatHistoryButton,
                clearHistoryButton,
                pinChatButton
        };

        for (Button button : aiMenuButtons) {

            button.setMaxWidth(
                    Double.MAX_VALUE
            );

            button.setPrefHeight(42);

            button.setAlignment(
                    Pos.CENTER_LEFT
            );

            button.setPadding(
                    new Insets(0, 12, 0, 12)
            );

            button.setFont(
                    Font.font(
                            FONT,
                            FontWeight.MEDIUM,
                            13
                    )
            );

            button.setTextFill(
                    Color.web(TEXT_DARK)
            );

            String idle =
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;";

            String hover =
                    "-fx-background-color: #CADDF2;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;";

            button.setStyle(idle);

            button.setOnMouseEntered(
                    e -> button.setStyle(hover)
            );

            button.setOnMouseExited(
                    e -> button.setStyle(idle)
            );
        }

        HBox menuHeader =
                new HBox(menuTitleBox);

        menuHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        menuPanel.getChildren().addAll(
                menuHeader,
                newChatButton,
                chatHistoryButton,
                clearHistoryButton,
                pinChatButton
        );

        menuPanel.setVisible(false);
        menuPanel.setManaged(false);

        // =========================================================
        // EMPTY STATE
        // =========================================================

        Label chatIcon =
                new Label("💬");

        chatIcon.setFont(
                Font.font(32)
        );

        Label conversationTitle =
                new Label(
                        "Start a conversation with OneSpace AI"
                );

        conversationTitle.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        20
                )
        );

        conversationTitle.setTextFill(
                Color.web(TEXT_DARK)
        );

        Label conversationDescription =
                new Label(
                        "Ask questions about your files, extracted dates, people, " +
                        "or generate insights directly from your indexed storage."
                );

        conversationDescription.setFont(
                Font.font(FONT, 13)
        );

        conversationDescription.setTextFill(
                Color.web(TEXT_MUTED_DARK)
        );

        conversationDescription.setAlignment(
                Pos.CENTER
        );

        conversationDescription.setWrapText(true);

        Button suggestion1 =
                createSuggestionButton(
                        "✧  When does my passport expire?"
                );

        Button suggestion2 =
                createSuggestionButton(
                        "📄  Summarize recent document"
                );

        Button suggestion3 =
                createSuggestionButton(
                        "🎓  Find my internship certificate"
                );

        Button suggestion4 =
                createSuggestionButton(
                        "☑  Generate a checklist from project files"
                );

        HBox suggestionRow1 =
                new HBox(
                        10,
                        suggestion1,
                        suggestion2,
                        suggestion3
                );

        suggestionRow1.setAlignment(
                Pos.CENTER
        );

        HBox suggestionRow2 =
                new HBox(
                        10,
                        suggestion4
                );

        suggestionRow2.setAlignment(
                Pos.CENTER
        );

        VBox suggestionsBox =
                new VBox(
                        10,
                        suggestionRow1,
                        suggestionRow2
                );

        suggestionsBox.setAlignment(
                Pos.CENTER
        );

        VBox aiEmptyState =
                new VBox(
                        16,
                        chatIcon,
                        conversationTitle,
                        conversationDescription,
                        suggestionsBox
                );

        aiEmptyState.setAlignment(
                Pos.CENTER
        );

        aiEmptyState.setPadding(
                new Insets(20, 10, 20, 10)
        );

        // =========================================================
        // CHAT INPUT
        // =========================================================

        TextField aiInput =
                new TextField();

        aiInput.setPromptText(
                "Ask about any file, date, or document..."
        );

        aiInput.setPrefHeight(50);

        aiInput.setStyle(
                "-fx-background-color: #F3F7FC;" +
                "-fx-border-color: " +
                        BORDER_CARD + ";" +
                "-fx-border-radius: 25;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 0 55px 0 58px;" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " +
                        TEXT_DARK + ";" +
                "-fx-prompt-text-fill: #64748B;"
        );

        suggestion1.setOnAction(
                e -> aiInput.setText(
                        "When does my passport expire?"
                )
        );

        suggestion2.setOnAction(
                e -> aiInput.setText(
                        "Summarize recent document"
                )
        );

        suggestion3.setOnAction(
                e -> aiInput.setText(
                        "Find my internship certificate"
                )
        );

        suggestion4.setOnAction(
                e -> aiInput.setText(
                        "Generate a checklist from project files"
                )
        );

        // =========================================================
        // PLUS BUTTON
        // =========================================================

        Button plusButton =
                new Button("+");

        plusButton.setPrefSize(
                36,
                36
        );

        plusButton.setFont(
                Font.font(
                        FONT,
                        FontWeight.NORMAL,
                        24
                )
        );

        plusButton.setTextFill(
                Color.web(TEXT_DARK)
        );

        String plusIdleStyle =
                "-fx-background-color: white;" +
                "-fx-background-radius: 50%;" +
                "-fx-border-color: #AFC4DC;" +
                "-fx-border-width: 1.2;" +
                "-fx-border-radius: 50%;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0;";

        String plusHoverStyle =
                "-fx-background-color: #EAF2FC;" +
                "-fx-background-radius: 50%;" +
                "-fx-border-color: " +
                        PRIMARY_BLUE + ";" +
                "-fx-border-width: 1.2;" +
                "-fx-border-radius: 50%;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0;";

        plusButton.setStyle(
                plusIdleStyle
        );

        plusButton.setOnMouseEntered(
                e -> plusButton.setStyle(
                        plusHoverStyle
                )
        );

        plusButton.setOnMouseExited(
                e -> plusButton.setStyle(
                        plusIdleStyle
                )
        );

        // =========================================================
        // UPLOAD MENU
        // =========================================================

        MenuItem uploadFileItem =
                new MenuItem("📎  Upload File");

        ContextMenu uploadMenu =
                new ContextMenu(
                        uploadFileItem
                );

        uploadMenu.setStyle(
                "-fx-background-color: #DDE8F8;" +
                "-fx-border-color: #C3D6EC;" +
                "-fx-border-radius: 10;" +
                "-fx-padding: 5;"
        );

        uploadFileItem.setOnAction(e -> {

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Select File from Computer"
            );

            fileChooser.getExtensionFilters().addAll(

                    new FileChooser.ExtensionFilter(
                            "Documents",
                            "*.pdf",
                            "*.doc",
                            "*.docx",
                            "*.txt",
                            "*.xls",
                            "*.xlsx",
                            "*.ppt",
                            "*.pptx"
                    ),

                    new FileChooser.ExtensionFilter(
                            "Images",
                            "*.png",
                            "*.jpg",
                            "*.jpeg",
                            "*.gif"
                    ),

                    new FileChooser.ExtensionFilter(
                            "All Files",
                            "*.*"
                    )
            );

            File selectedFile =
                    fileChooser.showOpenDialog(
                            aiInput.getScene()
                                    .getWindow()
                    );

            if (selectedFile != null) {

                System.out.println(
                        "Selected File: " +
                        selectedFile.getAbsolutePath()
                );

              ////  LandingPage.showStorageIndexed();
              }
        });

        plusButton.setOnAction(e -> {

            if (uploadMenu.isShowing()) {

                uploadMenu.hide();

            } else {

                uploadMenu.show(
                        plusButton,
                        javafx.geometry.Side.TOP,
                        0,
                        -5
                );
            }
        });

        // =========================================================
        // SEND BUTTON
        // =========================================================

        Button sendButton =
                new Button("➔");

        sendButton.setPrefSize(
                36,
                36
        );

        sendButton.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        14
                )
        );

        sendButton.setTextFill(
                Color.WHITE
        );

        sendButton.setStyle(
                "-fx-background-color: " +
                        PRIMARY_BLUE + ";" +
                "-fx-background-radius: 50%;" +
                "-fx-cursor: hand;"
        );

        sendButton.setOnAction(e -> {

            String question =
                    aiInput.getText().trim();

            if (!question.isEmpty()) {

                chatHistory.add(question);

                System.out.println(
                        "AI Question: " +
                        question
                );

                aiInput.clear();
            }
        });

        // =========================================================
        // INPUT CONTAINER
        // =========================================================

        StackPane inputContainer =
                new StackPane(
                        aiInput,
                        plusButton,
                        sendButton
                );

        StackPane.setAlignment(
                plusButton,
                Pos.CENTER_LEFT
        );

        StackPane.setMargin(
                plusButton,
                new Insets(0, 0, 0, 10)
        );

        StackPane.setAlignment(
                sendButton,
                Pos.CENTER_RIGHT
        );

        StackPane.setMargin(
                sendButton,
                new Insets(0, 7, 0, 0)
        );

        Label disclaimer =
                new Label(
                        "OneSpace AI uses local neural indexing. " +
                        "Always verify important financial and legal documents."
                );

        disclaimer.setFont(
                Font.font(FONT, 11)
        );

        disclaimer.setTextFill(
                Color.web(TEXT_MUTED_DARK)
        );

        disclaimer.setAlignment(
                Pos.CENTER
        );

        VBox promptArea =
                new VBox(
                        10,
                        inputContainer,
                        disclaimer
                );

        promptArea.setAlignment(
                Pos.CENTER
        );

        // =========================================================
        // AI CARD
        // =========================================================

        HBox menuRow =
                new HBox(menuButton);

        menuRow.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox aiCard =
                new VBox(
                        12,
                        menuRow,
                        aiEmptyState,
                        promptArea
                );

        aiCard.setAlignment(
                Pos.CENTER
        );

        aiCard.setPadding(
                new Insets(
                        16,
                        32,
                        20,
                        32
                )
        );

        aiCard.setStyle(
                "-fx-background-color: " +
                        BG_CARD + ";" +
                "-fx-border-color: " +
                        BORDER_CARD + ";" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(" +
                "three-pass-box, rgba(0,0,0,0.12), " +
                "10, 0, 0, 3);"
        );

        VBox.setVgrow(
                aiEmptyState,
                Priority.ALWAYS
        );

        VBox.setVgrow(
                aiCard,
                Priority.ALWAYS
        );

        // =========================================================
        // MENU CONTAINER
        // =========================================================

        StackPane chatCardContainer =
                new StackPane(
                        aiCard,
                        menuPanel
                );

        StackPane.setAlignment(
                menuPanel,
                Pos.CENTER_LEFT
        );

        VBox.setVgrow(
                chatCardContainer,
                Priority.ALWAYS
        );

        // =========================================================
        // AUTO CLOSE MENU
        // =========================================================

        menuPanel.setOnMouseExited(e -> {

            menuPanel.setVisible(false);
            menuPanel.setManaged(false);

        });

        // =========================================================
        // MENU ACTIONS
        // =========================================================

        newChatButton.setOnAction(e -> {

            aiInput.clear();

            aiInput.setPromptText(
                    "Ask about any file, date, or document..."
            );

            chatHistory.clear();

            menuPanel.setVisible(false);
            menuPanel.setManaged(false);

            System.out.println(
                    "New chat started."
            );
        });

        chatHistoryButton.setOnAction(e -> {

            if (chatHistory.isEmpty()) {

                showInfo(
                        "Chat History",
                        "No chat history yet."
                );

            } else {

                StringBuilder historyText =
                        new StringBuilder(
                                "Previous questions:\n\n"
                        );

                for (
                        int i = 0;
                        i < chatHistory.size();
                        i++
                ) {

                    historyText
                            .append(i + 1)
                            .append(". ")
                            .append(chatHistory.get(i))
                            .append("\n");
                }

                showInfo(
                        "Chat History",
                        historyText.toString()
                );
            }
        });

        clearHistoryButton.setOnAction(e -> {

            chatHistory.clear();

            showInfo(
                    "Clear History",
                    "Chat history has been cleared."
            );
        });

        pinChatButton.setOnAction(e -> {

            if (
                    pinChatButton
                            .getText()
                            .contains("Pin Chat")
            ) {

                pinChatButton.setText(
                        "📌  Unpin Chat"
                );

                System.out.println(
                        "Chat pinned."
                );

            } else {

                pinChatButton.setText(
                        "📌  Pin Chat"
                );

                System.out.println(
                        "Chat unpinned."
                );
            }
        });

        // =========================================================
        // MENU OPEN / CLOSE
        // =========================================================

        menuButton.setOnAction(e -> {

            boolean open =
                    menuPanel.isVisible();

            menuPanel.setVisible(!open);
            menuPanel.setManaged(!open);
        });

        // =========================================================
        // MAIN CONTENT
        // =========================================================

        VBox mainContent =
                new VBox(
                        18,
                        aiHeader,
                        chatCardContainer
                );

        mainContent.setPadding(
                new Insets(
                        0,
                        24,
                        24,
                        24
                )
        );

        mainContent.setStyle(
                "-fx-background-color: " +
                        BG_CENTER_CANVAS + ";"
        );

        VBox.setVgrow(
                mainContent,
                Priority.ALWAYS
        );

        VBox centerContent =
                new VBox(
                        topBar,
                        mainContent
                );

        centerContent.setStyle(
                "-fx-background-color: " +
                        BG_CENTER_CANVAS + ";"
        );

        VBox.setVgrow(
                mainContent,
                Priority.ALWAYS
        );

        // =========================================================
        // ROOT
        // =========================================================

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: " +
                        BG_SIDEBAR + ";"
        );

        root.setLeft(sidebar);
        root.setCenter(centerContent);

        return new Scene(
                root,
                1200,
                750
        );
    }

    // =============================================================
    // SHOW INFORMATION
    // =============================================================

    private void showInfo(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // =============================================================
    // SIDEBAR BUTTON
    // =============================================================

    private Button createSidebarButton(
            String icon,
            String label,
            boolean isActive
    ) {

        Label iconLbl =
                new Label(icon);

        iconLbl.setFont(
                Font.font(FONT, 14)
        );

        Label textLbl =
                new Label(label);

        textLbl.setFont(
                Font.font(
                        FONT,
                        isActive
                                ? FontWeight.BOLD
                                : FontWeight.MEDIUM,
                        13
                )
        );

        HBox content =
                new HBox(
                        12,
                        iconLbl,
                        textLbl
                );

        content.setAlignment(
                Pos.CENTER_LEFT
        );

        Button btn =
                new Button(
                        "",
                        content
                );

        btn.setMaxWidth(
                Double.MAX_VALUE
        );

        btn.setPrefHeight(38);

        btn.setAlignment(
                Pos.CENTER_LEFT
        );

        btn.setPadding(
                new Insets(
                        0,
                        12,
                        0,
                        12
                )
        );

        if (isActive) {

            btn.setStyle(
                    "-fx-background-color: #263B58;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
            );

            iconLbl.setTextFill(
                    Color.web("#60A5FA")
            );

            textLbl.setTextFill(
                    Color.web(TEXT_LIGHT)
            );

        } else {

            String idleStyle =
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;";

            String hoverStyle =
                    "-fx-background-color: #26354A;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;";

            btn.setStyle(idleStyle);

            iconLbl.setTextFill(
                    Color.web(TEXT_MUTED_LIGHT)
            );

            textLbl.setTextFill(
                    Color.web(TEXT_LIGHT)
            );

            btn.setOnMouseEntered(
                    e -> btn.setStyle(hoverStyle)
            );

            btn.setOnMouseExited(
                    e -> btn.setStyle(idleStyle)
            );
        }

        return btn;
    }

    // =============================================================
    // SUGGESTION BUTTON
    // =============================================================

    private Button createSuggestionButton(
            String text
    ) {

        Button button =
                new Button(text);

        button.setPrefHeight(34);

        button.setPadding(
                new Insets(
                        0,
                        14,
                        0,
                        14
                )
        );

        button.setFont(
                Font.font(
                        FONT,
                        FontWeight.MEDIUM,
                        12
                )
        );

        button.setTextFill(
                Color.web(PRIMARY_BLUE)
        );

        String idleStyle =
                "-fx-background-color: #EAF2FC;" +
                "-fx-border-color: #BFDBFE;" +
                "-fx-border-radius: 18;" +
                "-fx-background-radius: 18;" +
                "-fx-cursor: hand;";

        String hoverStyle =
                "-fx-background-color: #DBEAFE;" +
                "-fx-border-color: " +
                        PRIMARY_BLUE + ";" +
                "-fx-border-radius: 18;" +
                "-fx-background-radius: 18;" +
                "-fx-cursor: hand;";

        button.setStyle(idleStyle);

        button.setOnMouseEntered(
                e -> button.setStyle(hoverStyle)
        );

        button.setOnMouseExited(
                e -> button.setStyle(idleStyle)
        );

        return button;
    }
}