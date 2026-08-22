package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UserTrash {

    // Style Constants - Synchronized with UserDashboard.java (Original Light/Medium Slate Theme)
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar: Deep Dark Slate
    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";

    // 2. Center Workspace Canvas: Medium Slate Blue
    private static final String BG_CENTER_CANVAS = "#31435B";

    // 3. Main Cards & Inner Surfaces: Soft Light Blue
    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_CARD_INNER = "#CADDF2";
    private static final String BORDER_CARD = "#C3D6EC";

    // 4. Contrast Typography
    private static final String TEXT_DARK = "#0F172A";        // Deep Navy for headings / big numbers
    private static final String TEXT_MUTED_DARK = "#334155";  // Slate for subtext / labels inside cards
    private static final String TEXT_LIGHT = "#FFFFFF";       // Main white text on dark surfaces
    private static final String TEXT_MUTED_LIGHT = "#94A3B8"; // Subtext on dark surfaces

    // Accent Colors
    private static final String PRIMARY_BLUE = "#2563EB";

    public Scene getTrashPageScene() {

        // =========================================================
        // SIDEBAR
        // =========================================================

        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 19px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", true);

        // Action handlers for main navigation
        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        aiBtn.setOnAction(e -> LandingPage.showLandingPage());
        collabBtn.setOnAction(e -> LandingPage.showCollaborationPage());
        recentBtn.setOnAction(e -> LandingPage.showRecentPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());

        VBox navList = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

        // Bottom Sidebar Buttons (Settings & Logout)
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);
        Button logoutBtn = createSidebarButton("🚪", "Logout", false);

        settingsBtn.setOnAction(e -> LandingPage.showSettingPage());
        logoutBtn.setOnAction(e -> LandingPage.showUserLoginPage());

        // Sidebar Storage Card
        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        storageTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox storageValGroup = new HBox(storageVal, new Region(), storagePercent);
        HBox.setHgrow(storageValGroup.getChildren().get(1), Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: " + PRIMARY_BLUE + "; -fx-control-inner-background: #0E1520;");

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showLandingPage());

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        // Sidebar container layout with Settings and Logout stacked correctly at the bottom
        VBox sidebar = new VBox(8, logoBox, navList, sidebarSpacer, settingsBtn, logoutBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        // =========================================================
        // TOP SEARCH BAR & PROFILE
        // =========================================================

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        TextField searchField = new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 600; -fx-background-color: #141E2C; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 6; -fx-background-radius: 4;");

        HBox searchContainer = new HBox(8, searchIcon, searchField, keyShortcut);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0, 12, 0, 14));
        searchContainer.setPrefWidth(420);
        searchContainer.setStyle("-fx-background-color: #141E2C; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand;");

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


// =========================================================
// CLICKABLE PROFILE OPTION
// =========================================================

HBox profileOption =
        new HBox(
                8,
                avatar,
                userName,
                dropDown
        );

profileOption.setAlignment(
        Pos.CENTER
);

profileOption.setPadding(
        new Insets(5, 8, 5, 8)
);

profileOption.setStyle(
        "-fx-background-color: transparent;" +
        "-fx-background-radius: 8;" +
        "-fx-cursor: hand;"
);


// =========================================================
// OPEN PROFILE PAGE WHEN CLICKED
// =========================================================

profileOption.setOnMouseClicked(e -> {
    LandingPage.showUserProfilePage();
});


// =========================================================
// HOVER EFFECT
// =========================================================

profileOption.setOnMouseEntered(e -> {
    profileOption.setStyle(
            "-fx-background-color: #26354A;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
    );
});

profileOption.setOnMouseExited(e -> {
    profileOption.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
    );
});


// =========================================================
// TOP RIGHT
// =========================================================

HBox profileBox =
        new HBox(
                10,
                bellBtn,
                profileOption
        );

profileBox.setAlignment(
        Pos.CENTER
);


// =========================================================
// TOP BAR
// =========================================================

HBox topBar =
        new HBox(
                20,
                searchContainer,
                new Region(),
                profileBox
        );

HBox.setHgrow(
        topBar.getChildren().get(1),
        Priority.ALWAYS
);

topBar.setAlignment(
        Pos.CENTER_LEFT
);

topBar.setPadding(
        new Insets(
                16,
                28,
                14,
                28
        )
);

topBar.setStyle(
        "-fx-background-color: " + BG_SIDEBAR + ";" +
        "-fx-border-color: " + SIDEBAR_BORDER + ";" +
        "-fx-border-width: 0 0 1 0;"
);



        // =========================================================
        // HEADER & ACTION BAR
        // =========================================================

        Label welcomeTitle = new Label("Trash Bin");
        welcomeTitle.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        welcomeTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 24px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label welcomeSub = new Label("Items in trash are permanently deleted after 30 days. Recover your system space.");
        welcomeSub.setFont(Font.font(FONT, 13));
        welcomeSub.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        VBox greetingText = new VBox(4, welcomeTitle, welcomeSub);

        Button emptyTrashBtn = new Button("🗑  Empty Trash");
        emptyTrashBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        emptyTrashBtn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-color: " + PRIMARY_BLUE + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 8 18;"
        );
        emptyTrashBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to permanently delete all items in trash?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
        });

        HBox greetingHeader = new HBox(greetingText, new Region(), emptyTrashBtn);
        HBox.setHgrow(greetingHeader.getChildren().get(1), Priority.ALWAYS);
        greetingHeader.setAlignment(Pos.CENTER_LEFT);

        // =========================================================
        // SINGLE STREAMLINED METRIC CARD
        // =========================================================

        HBox statusCard = createMetricCard("🗑", "Items in Trash", "14 Files", "● Auto-clean active", "Will auto-delete after 30 days", "#059669", "#A7F3D0", "#065F46");
        HBox.setHgrow(statusCard, Priority.ALWAYS);

        // =========================================================
        // REMOVED ITEMS TABLE CARD
        // =========================================================

        Label cardTitle = new Label("Removed Items");
        cardTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        cardTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 17px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label cardSub = new Label("Manage or restore your recently removed files and folders.");
        cardSub.setFont(Font.font(FONT, 12));
        cardSub.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox cardHeaderTitles = new VBox(2, cardTitle, cardSub);

        Button restoreAllBtn = new Button("Restore All");
        restoreAllBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        restoreAllBtn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " + PRIMARY_BLUE + ";" +
                "-fx-padding: 6 14;" +
                "-fx-cursor: hand;"
        );
        restoreAllBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "All items restored to original locations.");
            alert.showAndWait();
        });

        HBox cardHeader = new HBox(cardHeaderTitles, new Region(), restoreAllBtn);
        HBox.setHgrow(cardHeader.getChildren().get(1), Priority.ALWAYS);
        cardHeader.setAlignment(Pos.CENTER_LEFT);

        // Table Headers
        HBox tableHeader = new HBox(
                createHeaderLabel("Name", 240),
                createHeaderLabel("Original Location", 180),
                createHeaderLabel("Date Deleted", 140),
                createHeaderLabel("Size", 100),
                createHeaderLabel("Days Left", 100),
                createHeaderLabel("Actions", 120)
        );
        tableHeader.setPadding(new Insets(0, 0, 8, 0));

        VBox trashTableRows = new VBox(10,
                tableHeader,
                createTrashRow("📄", "Project_Report_v1.pdf", "Java Project", "12 Oct 2024", "18.4 MB", "2 Days", true),
                createTrashRow("📁", "Old_Assignments_Backup", "College Assignments", "10 Oct 2024", "1.2 GB", "4 Days", false),
                createTrashRow("📄", "Resume_Draft_Old.docx", "Placement Preparation", "08 Oct 2024", "2.1 MB", "6 Days", false),
                createTrashRow("🎬", "Demo_Presentation.mp4", "Java Project", "05 Oct 2024", "450 MB", "9 Days", false),
                createTrashRow("📄", "Scanned_ID_Copy.png", "Personal Documents", "01 Oct 2024", "4.5 MB", "13 Days", false)
        );

        Label lastUpdated = new Label("🕒  Auto-delete schedule running every 24 hours");
        lastUpdated.setFont(Font.font(FONT, 11));
        lastUpdated.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 500;");

        VBox trashCard = new VBox(16, cardHeader, trashTableRows, lastUpdated);
        trashCard.setPadding(new Insets(24));
        trashCard.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 16, 0, 0, 6);"
        );

        // =========================================================
        // SCROLLABLE CONTAINER
        // =========================================================

        VBox contentBody = new VBox(22, greetingHeader, statusCard, trashCard);
        contentBody.setPadding(new Insets(24, 28, 28, 28));
        contentBody.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: " + BG_CENTER_CANVAS + ";" +
                "-fx-background: " + BG_CENTER_CANVAS + ";" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_SIDEBAR + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1200, 750);
    }

    // =========================================================
    // HELPER BUILDERS
    // =========================================================

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
        textLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: " + (isActive ? "700" : "500") + "; -fx-text-fill: " + TEXT_LIGHT + ";");

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
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #26354A; -fx-background-radius: 8; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;"));
        }

        return btn;
    }

    private HBox createMetricCard(String icon, String title, String value, String badgeText, String subText, String accentColor, String bgAccent, String textBadgeColor) {
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        titleLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(14));
        iconLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 14px; -fx-text-fill: " + accentColor + ";");

        Label iconBox = new Label("", iconLbl);
        iconBox.setPrefSize(32, 32);
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setStyle("-fx-background-color: " + bgAccent + "; -fx-background-radius: 8;");

        HBox topRow = new HBox(titleLbl, new Region(), iconBox);
        HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label valLbl = new Label(value);
        valLbl.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        valLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label badgeLbl = new Label(badgeText);
        badgeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        badgeLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: " + textBadgeColor + "; -fx-background-color: " + bgAccent + "; -fx-background-radius: 6; -fx-padding: 3 8;");

        Label subLbl = new Label(subText);
        subLbl.setFont(Font.font(FONT, 11));
        subLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 600;");

        HBox bottomRow = new HBox(6, badgeLbl, subLbl);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox cardContent = new VBox(8, topRow, valLbl, bottomRow);

        HBox card = new HBox(cardContent);
        HBox.setHgrow(cardContent, Priority.ALWAYS);
        card.setPadding(new Insets(16));
        card.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.14), 12, 0, 0, 4);"
        );

        return card;
    }

    private Label createHeaderLabel(String text, double width) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        lbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_DARK + ";");
        lbl.setPrefWidth(width);
        return lbl;
    }

    private HBox createTrashRow(String icon, String fileName, String originSpace, String dateDeleted, String fileSize, String daysLeft, boolean isUrgent) {
        Label fileIconLbl = new Label(icon);
        fileIconLbl.setFont(Font.font(12));
        fileIconLbl.setPrefSize(24, 24);
        fileIconLbl.setAlignment(Pos.CENTER);
        fileIconLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-background-color: #CADDF2; -fx-background-radius: 6; -fx-text-fill: " + PRIMARY_BLUE + ";");

        Label nameLbl = new Label(fileName);
        nameLbl.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        nameLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        HBox nameGroup = new HBox(10, fileIconLbl, nameLbl);
        nameGroup.setAlignment(Pos.CENTER_LEFT);
        nameGroup.setPrefWidth(240);

        Label spaceLbl = new Label(originSpace);
        spaceLbl.setFont(Font.font(FONT, 12));
        spaceLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 500;");
        spaceLbl.setPrefWidth(180);

        Label dateLbl = new Label(dateDeleted);
        dateLbl.setFont(Font.font(FONT, 12));
        dateLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 500;");
        dateLbl.setPrefWidth(140);

        Label sizeLbl = new Label(fileSize);
        sizeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        sizeLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");
        sizeLbl.setPrefWidth(100);

        Label daysLbl = new Label(daysLeft);
        daysLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        daysLbl.setStyle(isUrgent ?
                "-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #D97706; -fx-background-color: #FDE68A; -fx-padding: 2 6; -fx-background-radius: 4;" :
                "-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_DARK + ";"
        );
        daysLbl.setPrefWidth(100);

        Button restoreBtn = new Button("↺");
        restoreBtn.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: transparent; -fx-text-fill: " + PRIMARY_BLUE + "; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 14px;");
        restoreBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Restored " + fileName);
            alert.showAndWait();
        });

        Button deleteBtn = new Button("✕");
        deleteBtn.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 13px;");
        deleteBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Permanently delete " + fileName + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
        });

        HBox actionsGroup = new HBox(8, restoreBtn, deleteBtn);
        actionsGroup.setAlignment(Pos.CENTER_LEFT);
        actionsGroup.setPrefWidth(120);

        HBox row = new HBox(nameGroup, spaceLbl, dateLbl, sizeLbl, daysLbl, actionsGroup);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 0, 6, 0));
        row.setStyle("-fx-border-color: " + BORDER_CARD + "; -fx-border-width: 0 0 1 0;");

        return row;
    }
}