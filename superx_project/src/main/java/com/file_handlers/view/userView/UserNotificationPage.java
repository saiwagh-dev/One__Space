package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UserNotificationPage {

    // =========================================================
    // STYLE CONSTANTS - SAME THEME AS DASHBOARD & SPACES
    // =========================================================

    private static final String FONT =
            "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // Sidebar & Top Bar
    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";

    // Center Workspace
    private static final String BG_CENTER_CANVAS = "#31435B";

    // Cards
    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_CARD_INNER = "#CADDF2";
    private static final String BORDER_CARD = "#C3D6EC";

    // Typography
    private static final String TEXT_DARK = "#0F172A";
    private static final String TEXT_MUTED_DARK = "#334155";
    private static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#94A3B8";

    // Accent
    private static final String PRIMARY_BLUE = "#2563EB";


    public Scene getUserNotificationPageScene() {

        // =========================================================
        // SIDEBAR
        // =========================================================

        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        Label tagline = new Label("Your AI Workspace");
        tagline.setFont(Font.font(FONT, 11));
        tagline.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        VBox logoBox = new VBox(4, logoHeader, tagline);
        logoBox.setPadding(new Insets(0, 0, 18, 6));


        // =========================================================
        // SIDEBAR NAVIGATION
        // =========================================================

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);

        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());

        searchBtn.setOnAction(e -> LandingPage.showLandingPage());
        calendarBtn.setOnAction(e -> LandingPage.showLandingPage());
        aiBtn.setOnAction(e -> LandingPage.showLandingPage());
        collabBtn.setOnAction(e -> LandingPage.showLandingPage());
        recentBtn.setOnAction(e -> LandingPage.showLandingPage());
        trashBtn.setOnAction(e -> LandingPage.showLandingPage());
        settingsBtn.setOnAction(e -> LandingPage.showUserSetting());

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
        // SIDEBAR STORAGE CARD
        // =========================================================

        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        storageTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        Region storageRegion = new Region();

        HBox storageValGroup =
                new HBox(storageVal, storageRegion, storagePercent);

        HBox.setHgrow(storageRegion, Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);

        sidebarProgress.setStyle(
                "-fx-accent: " + PRIMARY_BLUE + ";" +
                "-fx-control-inner-background: #0E1520;"
        );

        Button manageStorageBtn =
                new Button("Manage Storage ›");

        manageStorageBtn.setFont(
                Font.font(FONT, FontWeight.SEMI_BOLD, 11)
        );

        manageStorageBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #60A5FA;" +
                "-fx-padding: 2 0 0 0;" +
                "-fx-cursor: hand;"
        );

        manageStorageBtn.setOnAction(
                e -> LandingPage.showLandingPage()
        );

        VBox storageCard = new VBox(
                8,
                storageTitle,
                storageValGroup,
                sidebarProgress,
                manageStorageBtn
        );

        storageCard.setPadding(new Insets(14));

        storageCard.setStyle(
                "-fx-background-color: " + BG_SIDEBAR_CARD + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;"
        );


        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(
                12,
                logoBox,
                navList,
                sidebarSpacer,
                settingsBtn,
                storageCard
        );

        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);

        sidebar.setStyle(
                "-fx-background-color: " + BG_SIDEBAR + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 1 0 0;"
        );


        // =========================================================
        // TOP SEARCH BAR
        // =========================================================

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle(
                "-fx-text-fill: " + TEXT_MUTED_LIGHT + ";"
        );

        TextField searchField = new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setPrefHeight(38);

        searchField.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + ";" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";"
        );

        Label keyShortcut = new Label("⌘ K");

        keyShortcut.setFont(
                Font.font(FONT, FontWeight.SEMI_BOLD, 10)
        );

        keyShortcut.setStyle(
                "-fx-background-color: #141E2C;" +
                "-fx-text-fill: " + TEXT_MUTED_LIGHT + ";" +
                "-fx-padding: 3 6;" +
                "-fx-background-radius: 4;"
        );

        HBox searchContainer = new HBox(
                8,
                searchIcon,
                searchField,
                keyShortcut
        );

        searchContainer.setAlignment(Pos.CENTER_LEFT);

        searchContainer.setPadding(
                new Insets(0, 12, 0, 14)
        );

        searchContainer.setPrefWidth(420);

        searchContainer.setStyle(
                "-fx-background-color: #141E2C;" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );

        HBox.setHgrow(searchField, Priority.ALWAYS);


        // =========================================================
        // PROFILE SECTION
        // =========================================================

        Button bellBtn = new Button("🔔");

        bellBtn.setStyle(
                "-fx-background-color: " + BG_SIDEBAR_CARD + ";" +
                "-fx-font-size: 16px;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        bellBtn.setOnAction(
                e -> LandingPage.showUserNotificationPage()
        );


        Label avatar = new Label("AV");

        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color: " + PRIMARY_BLUE + ";" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;"
        );

        Label userName = new Label("Aarav Verma");

        userName.setFont(
                Font.font(FONT, FontWeight.SEMI_BOLD, 13)
        );

        userName.setStyle(
                "-fx-text-fill: " + TEXT_LIGHT + ";"
        );

        Label dropDown = new Label("⌄");

        dropDown.setStyle(
                "-fx-text-fill: " + TEXT_MUTED_LIGHT + ";"
        );

        HBox profileBox = new HBox(
                10,
                bellBtn,
                avatar,
                userName,
                dropDown
        );

        profileBox.setAlignment(Pos.CENTER);


        Region topSpacer = new Region();

        HBox topBar = new HBox(
                20,
                searchContainer,
                topSpacer,
                profileBox
        );

        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        topBar.setAlignment(Pos.CENTER_LEFT);

        topBar.setPadding(
                new Insets(16, 28, 14, 28)
        );

        topBar.setStyle(
                "-fx-background-color: " + BG_SIDEBAR + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 0 1 0;"
        );


        // =========================================================
        // NOTIFICATION PAGE HEADER
        // =========================================================

        Label pageTitle = new Label("Notifications");

        pageTitle.setFont(
                Font.font(FONT, FontWeight.BOLD, 24)
        );

        pageTitle.setStyle(
                "-fx-text-fill: " + TEXT_LIGHT + ";"
        );

        Label pageDescription = new Label(
                "Findings from the last scan and updates from your collaborators."
        );

        pageDescription.setFont(
                Font.font(FONT, 13)
        );

        pageDescription.setStyle(
                "-fx-text-fill: " + TEXT_MUTED_LIGHT + ";" +
                "-fx-font-weight: 500;"
        );

        VBox titleBox = new VBox(
                4,
                pageTitle,
                pageDescription
        );


        Button markAllReadBtn =
                new Button("🔔  Mark all read");

        markAllReadBtn.setFont(
                Font.font(FONT, FontWeight.SEMI_BOLD, 12)
        );

        markAllReadBtn.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 9;" +
                "-fx-background-radius: 9;" +
                "-fx-text-fill: " + PRIMARY_BLUE + ";" +
                "-fx-padding: 9 16;" +
                "-fx-cursor: hand;"
        );


        Region headerSpacer = new Region();

        HBox pageHeader = new HBox(
                titleBox,
                headerSpacer,
                markAllReadBtn
        );

        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        pageHeader.setAlignment(Pos.CENTER_LEFT);


        // =========================================================
        // FILTER BUTTONS
        // =========================================================

        Button allBtn = createFilterButton("All", true);
        Button remindersBtn = createFilterButton("Reminders", false);
        Button collaborationBtn = createFilterButton("Collaboration", false);

        HBox filterBox = new HBox(
                10,
                allBtn,
                remindersBtn,
                collaborationBtn
        );

        filterBox.setAlignment(Pos.CENTER_LEFT);


        // =========================================================
        // NOTIFICATION CARDS
        // =========================================================

        VBox notification1 = createNotificationCard(
                "📄",
                "#2563EB",
                "#BFDBFE",
                "12 duplicate files detected",
                "Downloads folder · 4.2 GB recoverable",
                "1 h"
        );

        VBox notification2 = createNotificationCard(
                "🛡",
                "#7C3AED",
                "#E9D5FF",
                "Sensitive files found",
                "Aadhaar, PAN and passport scans detected",
                "3 h"
        );

        VBox notification3 = createNotificationCard(
                "▦",
                "#D97706",
                "#FDE68A",
                "Passport expires in 12 days",
                "Linked to Passport_Scan.pdf",
                "5 h"
        );

        VBox notification4 = createNotificationCard(
                "●",
                "#2563EB",
                "#BFDBFE",
                "Riya commented on a shared file",
                "Cloud_Computing_Seminar.pptx",
                "Yesterday"
        );

        VBox notification5 = createNotificationCard(
                "✦",
                "#7C3AED",
                "#E9D5FF",
                "AI created 2 new Spaces",
                "Healthcare and Travel from 609 files",
                "2 d"
        );

        VBox notification6 = createNotificationCard(
                "👥",
                "#059669",
                "#A7F3D0",
                "Priya Sharma uploaded 'SVM_Optimization.pdf'",
                "Shared in College Presentation Workspace",
                "2 d"
        );


        // =========================================================
        // NOTIFICATION LIST
        // =========================================================

        VBox notificationList = new VBox(12);

        // Initially show all notifications
        notificationList.getChildren().addAll(
                notification1,
                notification2,
                notification3,
                notification4,
                notification5,
                notification6
        );


        // =========================================================
        // FILTER ACTIONS
        // =========================================================

        allBtn.setOnAction(e -> {

            notificationList.getChildren().setAll(
                    notification1,
                    notification2,
                    notification3,
                    notification4,
                    notification5,
                    notification6
            );

            setActiveFilter(
                    allBtn,
                    remindersBtn,
                    collaborationBtn
            );
        });


        remindersBtn.setOnAction(e -> {

            notificationList.getChildren().setAll(
                    notification1,
                    notification2,
                    notification3,
                    notification5
            );

            setActiveFilter(
                    remindersBtn,
                    allBtn,
                    collaborationBtn
            );
        });


        collaborationBtn.setOnAction(e -> {

            notificationList.getChildren().setAll(
                    notification4,
                    notification6
            );

            setActiveFilter(
                    collaborationBtn,
                    allBtn,
                    remindersBtn
            );
        });


        // =========================================================
        // MAIN CONTENT
        // =========================================================

        VBox contentBody = new VBox(
                22,
                pageHeader,
                filterBox,
                notificationList
        );

        contentBody.setPadding(
                new Insets(24, 28, 28, 28)
        );

        contentBody.setStyle(
                "-fx-background-color: " + BG_CENTER_CANVAS + ";"
        );


        ScrollPane scrollPane = new ScrollPane(contentBody);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color: " + BG_CENTER_CANVAS + ";" +
                "-fx-background: " + BG_CENTER_CANVAS + ";" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );


        VBox mainArea = new VBox(
                topBar,
                scrollPane
        );

        mainArea.setStyle(
                "-fx-background-color: " + BG_CENTER_CANVAS + ";"
        );

        VBox.setVgrow(scrollPane, Priority.ALWAYS);


        // =========================================================
        // ROOT
        // =========================================================

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + BG_SIDEBAR + ";"
        );

        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1200, 750);
    }


    // =========================================================
    // HELPER METHODS
    // =========================================================


    private StackPane createOneSpaceLogo() {

        SVGPath cloudPath = new SVGPath();

        cloudPath.setContent(
                "M 6 15 A 6 6 0 0 1 18 10 " +
                "A 5 5 0 0 1 26 13 " +
                "A 4 4 0 0 1 25 21 " +
                "L 6 21 A 3 3 0 0 1 6 15 Z"
        );

        cloudPath.setFill(Color.TRANSPARENT);
        cloudPath.setStroke(Color.web("#38BDF8"));
        cloudPath.setStrokeWidth(2.2);


        Label docSymbol = new Label("📄");

        docSymbol.setFont(Font.font(13));

        docSymbol.setStyle(
                "-fx-text-fill: #818CF8;"
        );


        StackPane logoPane =
                new StackPane(cloudPath, docSymbol);

        logoPane.setPrefSize(32, 32);
        logoPane.setAlignment(Pos.CENTER);

        return logoPane;
    }


    private Button createSidebarButton(
            String icon,
            String label,
            boolean isActive
    ) {

        Label iconLbl = new Label(icon);

        iconLbl.setFont(
                Font.font(FONT, 14)
        );


        Label textLbl = new Label(label);

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
                new HBox(12, iconLbl, textLbl);

        content.setAlignment(Pos.CENTER_LEFT);


        Button btn =
                new Button("", content);

        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);

        btn.setAlignment(Pos.CENTER_LEFT);

        btn.setPadding(
                new Insets(0, 12, 0, 12)
        );


        if (isActive) {

            btn.setStyle(
                    "-fx-background-color: " + PRIMARY_BLUE + ";" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
            );

            iconLbl.setStyle(
                    "-fx-text-fill: " + TEXT_LIGHT + ";"
            );

            textLbl.setStyle(
                    "-fx-text-fill: " + TEXT_LIGHT + ";"
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

            iconLbl.setStyle(
                    "-fx-text-fill: " + TEXT_MUTED_LIGHT + ";"
            );

            textLbl.setStyle(
                    "-fx-text-fill: " + TEXT_LIGHT + ";"
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


    private Button createFilterButton(
            String text,
            boolean active
    ) {

        Button btn = new Button(text);

        btn.setFont(
                Font.font(
                        FONT,
                        active
                                ? FontWeight.BOLD
                                : FontWeight.MEDIUM,
                        12
                )
        );

        if (active) {

            btn.setStyle(getActiveFilterStyle());

        } else {

            btn.setStyle(getInactiveFilterStyle());
        }

        return btn;
    }


    // =========================================================
    // FILTER STYLE HELPERS
    // =========================================================

    private String getActiveFilterStyle() {

        return
                "-fx-background-color: " + PRIMARY_BLUE + ";" +
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-background-radius: 18;" +
                "-fx-padding: 8 22;" +
                "-fx-cursor: hand;";
    }


    private String getInactiveFilterStyle() {

        return
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 18;" +
                "-fx-background-radius: 18;" +
                "-fx-text-fill: " + TEXT_DARK + ";" +
                "-fx-padding: 8 22;" +
                "-fx-cursor: hand;";
    }


    // =========================================================
    // SET ACTIVE FILTER
    // =========================================================

    private void setActiveFilter(
            Button activeButton,
            Button inactiveButton1,
            Button inactiveButton2
    ) {

        activeButton.setStyle(
                getActiveFilterStyle()
        );

        inactiveButton1.setStyle(
                getInactiveFilterStyle()
        );

        inactiveButton2.setStyle(
                getInactiveFilterStyle()
        );
    }


    // =========================================================
    // CREATE NOTIFICATION CARD
    // =========================================================

    private VBox createNotificationCard(
            String icon,
            String iconColor,
            String iconBg,
            String title,
            String description,
            String time
    ) {

        Label iconLabel = new Label(icon);

        iconLabel.setFont(
                Font.font(FONT, FontWeight.BOLD, 15)
        );

        iconLabel.setPrefSize(48, 48);

        iconLabel.setAlignment(Pos.CENTER);

        iconLabel.setStyle(
                "-fx-background-color: " + iconBg + ";" +
                "-fx-background-radius: 12;" +
                "-fx-text-fill: " + iconColor + ";"
        );


        Label titleLabel = new Label(title);

        titleLabel.setFont(
                Font.font(FONT, FontWeight.SEMI_BOLD, 14)
        );

        titleLabel.setStyle(
                "-fx-text-fill: " + TEXT_DARK + ";"
        );


        Label descriptionLabel =
                new Label(description);

        descriptionLabel.setFont(
                Font.font(FONT, 12)
        );

        descriptionLabel.setStyle(
                "-fx-text-fill: " + TEXT_MUTED_DARK + ";"
        );


        VBox textBox = new VBox(
                5,
                titleLabel,
                descriptionLabel
        );

        textBox.setAlignment(Pos.CENTER_LEFT);


        Label timeLabel = new Label(time);

        timeLabel.setFont(
                Font.font(FONT, 11)
        );

        timeLabel.setStyle(
                "-fx-text-fill: " + TEXT_MUTED_DARK + ";"
        );


        Region spacer = new Region();

        HBox row = new HBox(
                16,
                iconLabel,
                textBox,
                spacer,
                timeLabel
        );

        HBox.setHgrow(textBox, Priority.ALWAYS);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.setAlignment(Pos.CENTER_LEFT);


        VBox card = new VBox(row);

        card.setPadding(
                new Insets(18, 20, 18, 20)
        );

        String idleStyle =
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 15;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(" +
                "three-pass-box, rgba(0,0,0,0.12), " +
                "10, 0, 0, 3);" +
                "-fx-cursor: hand;";

        String hoverStyle =
                "-fx-background-color: #EBF2FC;" +
                "-fx-border-color: " + PRIMARY_BLUE + ";" +
                "-fx-border-radius: 15;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(" +
                "three-pass-box, rgba(37,99,235,0.20), " +
                "14, 0, 0, 4);" +
                "-fx-cursor: hand;";

        card.setStyle(idleStyle);

        card.setOnMouseEntered(
                e -> card.setStyle(hoverStyle)
        );

        card.setOnMouseExited(
                e -> card.setStyle(idleStyle)
        );

        return card;
    }
}