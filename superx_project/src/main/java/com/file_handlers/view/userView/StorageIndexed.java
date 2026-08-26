package com.file_handlers.view.userView;

import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;

import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;

public class StorageIndexed {

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
    private static final String TEXT_DARK = "#0F172A";        // Deep Navy for headings / big numbers
    private static final String TEXT_MUTED_DARK = "#334155";  // Slate for subtext / labels inside cards
    private static final String TEXT_LIGHT = "#FFFFFF";       // Main white text on dark surfaces
    private static final String TEXT_MUTED_LIGHT = "#94A3B8"; // Subtext on dark surfaces

    // Accent Colors
    private static final String PRIMARY_BLUE = "#2563EB";

    public Scene getStorageIndexedScene() {

        // =========================================================
        // FETCH USER SESSION DATA (First Name Only & Initials)
        // =========================================================
        String activeUserName = "User";
        String initials = "U";

        if (UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null) {
                String fullName = UserSession.getInstance().getDisplayName().trim();
                if (!fullName.isEmpty()) {
                        String[] parts = fullName.split("\\s+");
                        activeUserName = parts[0];
                        initials = activeUserName.substring(0, 1).toUpperCase();
                }
        }

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

        // Navigation Buttons
        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);

        // Sidebar Navigation Actions
        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        aiBtn.setOnAction(e -> LandingPage.showAiAssistantPage());
        collabBtn.setOnAction(e -> LandingPage.showCollaborationPage());
        recentBtn.setOnAction(e -> LandingPage.showRecentPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());
        settingsBtn.setOnAction(e -> LandingPage.showSettingPage());

        VBox navList = new VBox(4,
                dashboardBtn, spacesBtn, searchBtn, calendarBtn,
                aiBtn, collabBtn, recentBtn, trashBtn
        );

        // Active Storage Indicator Card in Sidebar
        Label storageBadge = new Label("✧  Storage indexed");
        storageBadge.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        storageBadge.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 600; -fx-text-fill: #60A5FA;");

        Label storageVal = new Label("64.2 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        storageVal.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label storageSub = new Label("of 100 GB used");
        storageSub.setFont(Font.font(FONT, 11));
        storageSub.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        VBox storageTextGroup = new VBox(1, storageVal, storageSub);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: " + PRIMARY_BLUE + "; -fx-control-inner-background: #0E1520;");

        Label storageInfo = new Label("Files stay in place —\nnothing moved or renamed.");
        storageInfo.setFont(Font.font(FONT, 11));
        storageInfo.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        VBox storageCard = new VBox(10, storageBadge, storageTextGroup, sidebarProgress, storageInfo);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle(
                "-fx-background-color: " + BG_SIDEBAR_CARD + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;"
        );

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setMaxWidth(230);
        sidebar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        // =========================================================
        // TOP BAR
        // =========================================================

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        TextField searchField = new TextField();
        searchField.setPromptText("Search indexed folders & files...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 600; -fx-background-color: #141E2C; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 6; -fx-background-radius: 4;");

        HBox searchContainer = new HBox(8, searchIcon, searchField, keyShortcut);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0, 12, 0, 14));
        searchContainer.setStyle(
                "-fx-background-color: #141E2C;" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchContainer.setMaxWidth(500);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
                "-fx-background-color: " + PRIMARY_BLUE + ";" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;"
        );

        Label userName = new Label(activeUserName);
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label dropDown = new Label("⌄");
        dropDown.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox profileOption = new HBox(8, avatar, userName, dropDown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(5, 8, 5, 8));
        profileOption.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;");

        profileOption.setOnMouseClicked(e -> LandingPage.showUserProfilePage());
        profileOption.setOnMouseEntered(e -> profileOption.setStyle("-fx-background-color: #26354A; -fx-background-radius: 8; -fx-cursor: hand;"));
        profileOption.setOnMouseExited(e -> profileOption.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;"));

        HBox profileBox = new HBox(10, bellBtn, profileOption);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, searchContainer, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle(
                "-fx-background-color: " + BG_SIDEBAR + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 0 1 0;"
        );

        // =========================================================
        // PAGE HEADER & SCAN ACTION
        // =========================================================

        Label pageTitle = new Label("Storage & Indexing");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        pageTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label pageDescription = new Label("Detailed overview of your local disk indexing status, categories, and scan paths.");
        pageDescription.setFont(Font.font(FONT, 13));
        pageDescription.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        VBox titleBox = new VBox(4, pageTitle, pageDescription);

        Button addFolderBtn = new Button("+  Add Directory");
        addFolderBtn.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        addFolderBtn.setPrefHeight(36);
        addFolderBtn.setPadding(new Insets(0, 16, 0, 16));
        addFolderBtn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-color: " + PRIMARY_BLUE + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        // =========================================================
        // INDEXED DIRECTORIES TABLE
        // =========================================================

        Label tableTitle = new Label("Indexed Local Directories");
        tableTitle.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        tableTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        HBox tableHeader = new HBox(
                createHeaderLabel("Directory Path", 280),
                createHeaderLabel("Files", 100),
                createHeaderLabel("Size", 100),
                createHeaderLabel("Last Synced", 130),
                createHeaderLabel("Status", 100)
        );
        tableHeader.setPadding(new Insets(8, 12, 8, 12));
        tableHeader.setStyle("-fx-border-color: " + BORDER_CARD + "; -fx-border-width: 0 0 1 0;");

        VBox folderRows = new VBox(8,
                tableHeader,
                createFolderRow("~/Documents/College/Assignments", "1,240", "18.4 GB", "2 mins ago", "Indexed"),
                createFolderRow("~/Projects/Java/OneSpace", "980", "14.2 GB", "10 mins ago", "Indexed"),
                createFolderRow("~/Documents/Personal/Certificates", "450", "8.6 GB", "1 hour ago", "Indexed"),
                createFolderRow("~/Downloads/Invoices", "310", "4.2 GB", "Yesterday", "Indexed")
        );

        // Add Directory Button Action (DirectoryChooser Integration)
        addFolderBtn.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Local Directory to Index");
            File selectedDirectory = directoryChooser.showDialog(addFolderBtn.getScene().getWindow());
            if (selectedDirectory != null) {
                String folderPath = selectedDirectory.getAbsolutePath();
                folderRows.getChildren().add(createFolderRow(folderPath, "45", "1.2 GB", "Just now", "Indexed"));
            }
        });

        HBox headerRow = new HBox(titleBox, new Region(), addFolderBtn);
        HBox.setHgrow(headerRow.getChildren().get(1), Priority.ALWAYS);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        // =========================================================
        // METRICS ROW (4 STAT CARDS)
        // =========================================================

        HBox stat1 = createStatCard("💾", "64.2 GB", "Total Storage Used", "#2563EB", "#CADDF2");
        HBox stat2 = createStatCard("📄", "4,232", "Total Files Indexed", "#0284C7", "#BAE6FD");
        HBox stat3 = createStatCard("📁", "12 Folders", "Local Directories", "#059669", "#A7F3D0");
        HBox stat4 = createStatCard("⚡", "Active", "Sync Engine Status", "#D97706", "#FDE68A");

        HBox metricsRow = new HBox(14, stat1, stat2, stat3, stat4);
        HBox.setHgrow(stat1, Priority.ALWAYS);
        HBox.setHgrow(stat2, Priority.ALWAYS);
        HBox.setHgrow(stat3, Priority.ALWAYS);
        HBox.setHgrow(stat4, Priority.ALWAYS);

        // =========================================================
        // FILE CATEGORY BREAKDOWN
        // =========================================================

        Label categoryTitle = new Label("Category Breakdown");
        categoryTitle.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        categoryTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        VBox cat1 = createCategoryProgress("Documents & PDFs", "28.4 GB", "1,840 files", 0.44, PRIMARY_BLUE);
        VBox cat2 = createCategoryProgress("Projects & Code", "18.2 GB", "1,120 files", 0.28, "#0284C7");
        VBox cat3 = createCategoryProgress("Media & Images", "11.6 GB", "850 files", 0.18, "#059669");
        VBox cat4 = createCategoryProgress("Archives & Others", "6.0 GB", "422 files", 0.10, "#D97706");

        HBox categoryGrid = new HBox(20, cat1, cat2, cat3, cat4);
        HBox.setHgrow(cat1, Priority.ALWAYS);
        HBox.setHgrow(cat2, Priority.ALWAYS);
        HBox.setHgrow(cat3, Priority.ALWAYS);
        HBox.setHgrow(cat4, Priority.ALWAYS);

        VBox breakdownCard = new VBox(12, categoryTitle, categoryGrid);
        breakdownCard.setPadding(new Insets(20));
        breakdownCard.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.14), 12, 0, 0, 4);"
        );

        VBox directoriesCard = new VBox(12, tableTitle, folderRows);
        directoriesCard.setPadding(new Insets(20));
        directoriesCard.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.14), 12, 0, 0, 4);"
        );
        VBox.setVgrow(directoriesCard, Priority.ALWAYS);

        // =========================================================
        // PRIVACY & LOCAL INDEX GUARANTEE BANNER
        // =========================================================

        Label privacyIcon = new Label("🔒");
        privacyIcon.setFont(Font.font(FONT, FontWeight.BOLD, 14));

        Label privacyTextBold = new Label("100% Local & Private:");
        privacyTextBold.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        privacyTextBold.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label privacyTextNormal = new Label("OneSpace indexes your files locally on your computer. Files are never moved, renamed, or uploaded.");
        privacyTextNormal.setFont(Font.font(FONT, 12));
        privacyTextNormal.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        HBox textSpan = new HBox(6, privacyTextBold, privacyTextNormal);
        textSpan.setAlignment(Pos.CENTER_LEFT);

        HBox privacyBanner = new HBox(10, privacyIcon, textSpan);
        privacyBanner.setAlignment(Pos.CENTER_LEFT);
        privacyBanner.setPadding(new Insets(14, 18, 14, 18));
        privacyBanner.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;"
        );

        // =========================================================
        // MAIN CONTENT CONTAINER
        // =========================================================

        VBox contentBody = new VBox(22, headerRow, metricsRow, breakdownCard, directoriesCard, privacyBanner);
        contentBody.setPadding(new Insets(24, 28, 28, 28));
        contentBody.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(contentBody);
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

    private Label createHeaderLabel(String text, double width) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        lbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_DARK + ";");
        lbl.setPrefWidth(width);
        return lbl;
    }

    private HBox createStatCard(String icon, String value, String description, String iconColorHex, String iconBgHex) {
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(14));
        iconLbl.setStyle("-fx-text-fill: " + iconColorHex + ";");

        Label iconBox = new Label("", iconLbl);
        iconBox.setPrefSize(32, 32);
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setStyle("-fx-background-color: " + iconBgHex + "; -fx-background-radius: 8;");

        Label valLbl = new Label(value);
        valLbl.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        valLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label descLbl = new Label(description);
        descLbl.setFont(Font.font(FONT, 11));
        descLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 600;");

        VBox textGroup = new VBox(2, valLbl, descLbl);

        HBox card = new HBox(12, iconBox, textGroup);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16));
        card.setMaxWidth(Double.MAX_VALUE);

        String styleIdle = "-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 14; -fx-background-radius: 14; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.14), 12, 0, 0, 4);";
        String styleHover = "-fx-background-color: " + BG_CARD + "; -fx-border-color: " + iconColorHex + "; -fx-border-radius: 14; -fx-background-radius: 14; -fx-effect: dropshadow(three-pass-box, rgba(99,102,241,0.08), 16, 0, 0, 6); -fx-cursor: hand;";

        card.setPickOnBounds(true);
        card.setStyle(styleIdle);
        card.setOnMouseEntered(e -> card.setStyle(styleHover));
        card.setOnMouseExited(e -> card.setStyle(styleIdle));

        return card;
    }

    private VBox createCategoryProgress(String name, String size, String files, double progress, String barColorHex) {
        Label nameLbl = new Label(name);
        nameLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        nameLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label sizeLbl = new Label(size);
        sizeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        sizeLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        HBox header = new HBox(nameLbl, new Region(), sizeLbl);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);

        ProgressBar bar = new ProgressBar(progress);
        bar.setMaxWidth(Double.MAX_VALUE);
        bar.setPrefHeight(6);
        bar.setStyle("-fx-accent: " + barColorHex + "; -fx-control-inner-background: #B6CDE7;");

        Label filesLbl = new Label(files);
        filesLbl.setFont(Font.font(FONT, 11));
        filesLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 600;");

        return new VBox(6, header, bar, filesLbl);
    }

    private HBox createFolderRow(String path, String files, String size, String synced, String status) {
        Label pathLbl = new Label(path);
        pathLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        pathLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");
        pathLbl.setPrefWidth(280);

        Label filesLbl = new Label(files);
        filesLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        filesLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");
        filesLbl.setPrefWidth(100);

        Label sizeLbl = new Label(size);
        sizeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        sizeLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");
        sizeLbl.setPrefWidth(100);

        Label syncLbl = new Label(synced);
        syncLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        syncLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");
        syncLbl.setPrefWidth(130);

        Label statusBadge = new Label("● " + status);
        statusBadge.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        statusBadge.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #059669;");
        statusBadge.setPrefWidth(100);

        HBox row = new HBox(pathLbl, filesLbl, sizeLbl, syncLbl, statusBadge);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 12, 8, 12));
        row.setStyle("-fx-background-color: " + BG_CARD_INNER + "; -fx-background-radius: 8;");

        return row;
    }
}