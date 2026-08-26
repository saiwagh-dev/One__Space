package com.file_handlers.view.userView;

import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class UserDashboard {

    // Typography
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar: Deep Sleek Obsidian/Navy Tones
    private static final String BG_SIDEBAR = "#070C16";
    private static final String BG_SIDEBAR_CARD = "linear-gradient(to bottom right, rgba(14, 24, 43, 0.95), rgba(8, 14, 26, 0.95))";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";

    // 2. Center Workspace Canvas: Atmospheric Dark Radial Glow
    private static final String BG_CENTER_CANVAS = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards with Crisp High-Contrast Highlights
    private static final String BG_CARD = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String BG_CARD_INNER = "rgba(10, 18, 33, 0.85)";
    private static final String BORDER_CARD = "rgba(56, 189, 248, 0.22)";

    // 4. Vibrant Typography & Highlights
    private static final String TEXT_DARK = "#FFFFFF";         
    private static final String TEXT_MUTED_DARK = "#94A3B8";   
    private static final String TEXT_LIGHT = "#FFFFFF";        
    private static final String TEXT_MUTED_LIGHT = "#94A3B8";  

    // Dynamic Accent Colors & Gradients
    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String PRIMARY_BLUE_HOVER = "#38BDF8";
    private static final String ACCENT_CYAN = "#00D2FF";
    private static final String ACCENT_EMERALD = "#10B981";
    private static final String ACCENT_AMBER = "#F59E0B";
    private static final String[] CHART_COLORS = {"#3B82F6", "#00D2FF", "#10B981", "#F59E0B", "#6366F1"};

    public Scene getDashboardScene() {
        // FETCH USER SESSION DATA
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
        logoText.setStyle(
                "-fx-font-family: " + FONT + "; " +
                "-fx-font-size: 19px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: " + TEXT_LIGHT + ";"
        );

        HBox logoHeader = new HBox(12, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(6, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", true);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);

        Button logoutBtn = createSidebarButton("🚪", "Logout", false);

        dashboardBtn.setOnAction(e -> { LandingPage.showUserDashboard(); });
        spacesBtn.setOnAction(e -> { LandingPage.showUserSpace(); });
        searchBtn.setOnAction(e -> { LandingPage.showUserSearch(); });
        calendarBtn.setOnAction(e -> { LandingPage.showCalendarPage(); });
        collabBtn.setOnAction(e -> { LandingPage.showCollaborationPage();});
        aiBtn.setOnAction(e -> { LandingPage.showAiAssistantPage(); });
        recentBtn.setOnAction(e -> { LandingPage.showRecentPage(); });
        trashBtn.setOnAction(e -> { LandingPage.showTrashPage(); });
        settingsBtn.setOnAction(e -> { LandingPage.showSettingPage(); });

        logoutBtn.setOnAction(e -> {
            UserSession.clearSession();
            LandingPage.showUserLoginPage();
        });

        VBox navList = new VBox(5, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

        // Sidebar Storage Card with Gradient & Glow Effect
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
        sidebarProgress.setStyle(
                "-fx-accent: linear-gradient(to right, #0284C7, #38BDF8);" +
                "-fx-control-inner-background: #0B1526;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 0;"
        );

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: transparent; -fx-text-fill: #38BDF8; -fx-padding: 3 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> { LandingPage.showStorageIndexedPage(); });

        VBox storageCard = new VBox(9, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle(
                "-fx-background-color: " + BG_SIDEBAR_CARD + ";" +
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
        searchIcon.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-text-fill: #64748B;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search files, folders or smart spaces...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: transparent; -fx-prompt-text-fill: #64748B; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 600; -fx-background-color: rgba(255, 255, 255, 0.06); -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 7; -fx-background-radius: 5; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 5;");

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
        bellBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-size: 14px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand; -fx-padding: 6 10;");
        bellBtn.setOnAction(e -> { LandingPage.showNotificationPage(); });

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF);" +
            "-fx-background-radius: 50%;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);"
        );

        Label userName = new Label(activeUserName);
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label dropDown = new Label("⌄");
        dropDown.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox profileOption = new HBox(8, avatar, userName, dropDown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(4, 12, 4, 6));
        profileOption.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");

        profileOption.setOnMouseClicked(e -> { LandingPage.showUserProfilePage(); });
        profileOption.setOnMouseEntered(e -> profileOption.setStyle("-fx-background-color: rgba(23, 37, 64, 0.95); -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;"));
        profileOption.setOnMouseExited(e -> profileOption.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;"));

        HBox profileBox = new HBox(12, bellBtn, profileOption);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, searchContainer, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        // =========================================================
        // GREETING & SCAN ACTION HEADER
        // =========================================================

        Label welcomeTitle = new Label("Dashboard");
        welcomeTitle.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        welcomeTitle.setStyle(
                "-fx-font-family: " + FONT + "; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: " + TEXT_LIGHT + ";"
        );

        Label welcomeSub = new Label("Manage your files, spaces, and automated AI indexing pipelines seamlessly.");
        welcomeSub.setFont(Font.font(FONT, 13));
        welcomeSub.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        VBox greetingText = new VBox(4, welcomeTitle, welcomeSub);

        Button scanFolderBtn = new Button("⛶  Scan Folder");
        scanFolderBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        scanFolderBtn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #0284C7);" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 22;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(2, 132, 199, 0.6), 16, 0, 0, 3);"
        );

        scanFolderBtn.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Folder to Scan & Upload");
            Stage stage = (Stage) scanFolderBtn.getScene().getWindow();
            File selectedDirectory = directoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                System.out.println("Selected folder: " + selectedDirectory.getAbsolutePath());
            }
        });

        AnchorPane greetingHeader = new AnchorPane(greetingText, scanFolderBtn);
        AnchorPane.setTopAnchor(greetingText, 0.0);
        AnchorPane.setLeftAnchor(greetingText, 0.0);
        AnchorPane.setBottomAnchor(greetingText, 0.0);

        AnchorPane.setTopAnchor(scanFolderBtn, 0.0);
        AnchorPane.setRightAnchor(scanFolderBtn, 0.0);
        AnchorPane.setBottomAnchor(scanFolderBtn, 0.0);
        greetingHeader.setMaxWidth(Double.MAX_VALUE);

        // =========================================================
        // TOP 4 FILE OVERVIEW / TELEMETRY METRIC CARDS
        // =========================================================

        HBox card1 = createMetricCard("📁", "Indexing Activity", "7,032", "● 84 auto-tagged", "+412 files today", "#3B82F6", "rgba(59, 130, 246, 0.15)", "#93C5FD");
        HBox card2 = createMetricCard("▦", "Active Spaces", "8 Spaces", "2 AI generated", "🔥 Java Project (64%)", "#00D2FF", "rgba(0, 210, 255, 0.15)", "#38BDF8");
        HBox card3 = createMetricCard("💾", "Indexed Storage", "64.2 GB", "● Synced 2m ago", "4.2 GB recoverable", "#10B981", "rgba(16, 185, 129, 0.15)", "#34D399");
        HBox card4 = createMetricCard("✦", "AI Actions Live", "126 Actions", "⚡ Live pipeline", "12 summaries · 8 links", "#F59E0B", "rgba(245, 158, 11, 0.15)", "#FBBF24");

        HBox metricsRow = new HBox(16, card1, card2, card3, card4);
        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);
        HBox.setHgrow(card4, Priority.ALWAYS);

        // =========================================================
        // SPACE OCCUPANCY CARD
        // =========================================================

        Label cardTitle = new Label("Space Occupancy");
        cardTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        cardTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 17px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label cardSub = new Label("Overview of file storage across your spaces.");
        cardSub.setFont(Font.font(FONT, 12));
        cardSub.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox cardHeaderTitles = new VBox(2, cardTitle, cardSub);

        Button viewAllBtn = new Button("View all spaces ›");
        viewAllBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        viewAllBtn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-color: rgba(255, 255, 255, 0.05);" +
                "-fx-border-color: rgba(255, 255, 255, 0.1);" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: #38BDF8;" +
                "-fx-padding: 6 14;" +
                "-fx-cursor: hand;"
        );
        viewAllBtn.setOnAction(e -> { LandingPage.showUserSpace(); });

        HBox cardHeader = new HBox(cardHeaderTitles, new Region(), viewAllBtn);
        HBox.setHgrow(cardHeader.getChildren().get(1), Priority.ALWAYS);
        cardHeader.setAlignment(Pos.CENTER_LEFT);

        // Donut Chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Java Project", 34),
                new PieChart.Data("Placement Preparation", 29),
                new PieChart.Data("College Assignments", 20),
                new PieChart.Data("Personal Documents", 11),
                new PieChart.Data("Others", 6)
        );

        PieChart chart = new PieChart(pieChartData);
        chart.setLabelsVisible(false);
        chart.setLegendVisible(false);
        chart.setPrefSize(210, 210);
        chart.setMaxSize(210, 210);

        Circle donutHole = new Circle(68, Color.web("#0A1424"));

        Label chartValText = new Label("64.2 GB");
        chartValText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        chartValText.setStyle(
                "-fx-font-family: " + FONT + "; " +
                "-fx-font-size: 19px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: " + TEXT_DARK + "; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.5), 10, 0, 0, 0);"
        );

        Label chartSubText = new Label("of 100 GB used");
        chartSubText.setFont(Font.font(FONT, 11));
        chartSubText.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 600;");

        VBox chartCenterText = new VBox(2, chartValText, chartSubText);
        chartCenterText.setAlignment(Pos.CENTER);

        StackPane donutChartPane = new StackPane(chart, donutHole, chartCenterText);
        donutChartPane.setPadding(new Insets(8));

        // Space Breakdown Table
        HBox tableHeader = new HBox(
                createHeaderLabel("Space", 210),
                createHeaderLabel("Storage Used", 120),
                createHeaderLabel("Percentage", 150)
        );
        tableHeader.setPadding(new Insets(0, 0, 10, 0));
        tableHeader.setStyle("-fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-width: 0 0 1 0;");

        VBox spaceRows = new VBox(12,
                tableHeader,
                createSpaceRow("📁", CHART_COLORS[0], "Java Project", "22.4 GB", 0.34, "34%", CHART_COLORS[0]),
                createSpaceRow("📁", CHART_COLORS[1], "Placement Preparation", "18.7 GB", 0.29, "29%", CHART_COLORS[1]),
                createSpaceRow("📁", CHART_COLORS[2], "College Assignments", "12.6 GB", 0.20, "20%", CHART_COLORS[2]),
                createSpaceRow("📁", CHART_COLORS[3], "Personal Documents", "6.8 GB", 0.11, "11%", CHART_COLORS[3]),
                createSpaceRow("📁", CHART_COLORS[4], "Others", "3.7 GB", 0.06, "6%", CHART_COLORS[4])
        );

        HBox cardContent = new HBox(32, donutChartPane, spaceRows);
        cardContent.setAlignment(Pos.CENTER_LEFT);

        Label lastUpdated = new Label("ⓘ  Last updated just now");
        lastUpdated.setFont(Font.font(FONT, 11));
        lastUpdated.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 500;");

        VBox occupancyCard = new VBox(18, cardHeader, cardContent, lastUpdated);
        occupancyCard.setPadding(new Insets(26));
        occupancyCard.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-border-width: 1.2;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);"
        );

        // =========================================================
        // SCROLLABLE CONTAINER
        // =========================================================

        VBox contentBody = new VBox(24, greetingHeader, metricsRow, occupancyCard);
        contentBody.setPadding(new Insets(24, 28, 32, 28));
        contentBody.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background: " + BG_CENTER_CANVAS + "; -fx-background-color: " + BG_CENTER_CANVAS + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_SIDEBAR + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        Scene scene = new Scene(root, 1220, 760);

        Platform.runLater(() -> applyPieChartColors(pieChartData));

        return scene;
    }

    // =========================================================
    // HELPER BUILDERS WITH GLOW EFFECTS & HOVER TRANSITIONS
    // =========================================================

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
        textLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: " + (isActive ? "700" : "500") + "; -fx-text-fill: " + (isActive ? "#FFFFFF" : TEXT_MUTED_LIGHT) + ";");

        HBox content = new HBox(12, iconLbl, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(40);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 14, 0, 14));

        if (isActive) {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); " +
                    "-fx-background-radius: 12; " +
                    "-fx-border-color: rgba(96, 165, 250, 0.6); " +
                    "-fx-border-radius: 12; " +
                    "-fx-border-width: 1; " +
                    "-fx-cursor: hand; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);"
            );
            iconLbl.setStyle("-fx-text-fill: #FFFFFF;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

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

    private HBox createMetricCard(String icon, String title, String value, String badgeText, String subText, String accentColor, String bgAccent, String textBadgeColor) {
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        titleLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(14));
        iconLbl.setStyle("-fx-text-fill: " + accentColor + ";");

        Label iconBox = new Label("", iconLbl);
        iconBox.setPrefSize(34, 34);
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setStyle("-fx-background-color: " + bgAccent + "; -fx-border-color: " + accentColor + "55; -fx-border-radius: 9; -fx-background-radius: 9;");

        HBox topRow = new HBox(titleLbl, new Region(), iconBox);
        HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label valLbl = new Label(value);
        valLbl.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        valLbl.setStyle(
                "-fx-font-family: " + FONT + "; " +
                "-fx-font-size: 22px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: " + TEXT_DARK + ";"
        );

        Label subLbl = new Label(subText);
        subLbl.setFont(Font.font(FONT, 11));
        subLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 500;");
        subLbl.setMinWidth(Region.USE_PREF_SIZE);

        Label badgeLbl = new Label(badgeText);
        badgeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        badgeLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: " + textBadgeColor + "; -fx-background-color: " + bgAccent + "; -fx-border-color: " + accentColor + "44; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 3 9;");
        badgeLbl.setMinWidth(Region.USE_PREF_SIZE);

        HBox bottomRow = new HBox(8, badgeLbl, subLbl);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox cardContent = new VBox(12, topRow, valLbl, bottomRow);
        cardContent.setMaxWidth(Double.MAX_VALUE);

        HBox card = new HBox(cardContent);
        HBox.setHgrow(cardContent, Priority.ALWAYS);
        card.setPadding(new Insets(18));
        card.setMaxWidth(Double.MAX_VALUE);

        String styleIdle = "-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 16; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 18, 0, 0, 6);";
        String styleHover = "-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.9), rgba(12, 22, 40, 0.95)); -fx-border-color: " + accentColor + "; -fx-border-radius: 16; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, " + accentColor + "66, 20, 0, 0, 6); -fx-cursor: hand;";

        card.setPickOnBounds(true);
        card.setStyle(styleIdle);
        card.setOnMouseEntered(e -> card.setStyle(styleHover));
        card.setOnMouseExited(e -> card.setStyle(styleIdle));

        return card;
    }

    private Label createHeaderLabel(String text, double width) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        lbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_DARK + ";");
        lbl.setPrefWidth(width);
        return lbl;
    }

    private HBox createSpaceRow(String icon, String iconHex, String title, String storage, double progress, String percent, String colorHex) {
        Label folderIcon = new Label(icon);
        folderIcon.setFont(Font.font(12));
        folderIcon.setPrefSize(28, 28);
        folderIcon.setAlignment(Pos.CENTER);
        folderIcon.setStyle("-fx-background-color: " + iconHex + "25; -fx-border-color: " + iconHex + "55; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: " + iconHex + ";");

        Label spaceName = new Label(title);
        spaceName.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        spaceName.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_DARK + ";");

        HBox nameGroup = new HBox(12, folderIcon, spaceName);
        nameGroup.setAlignment(Pos.CENTER_LEFT);
        nameGroup.setPrefWidth(210);

        Label sizeLbl = new Label(storage);
        sizeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        sizeLbl.setStyle("-fx-text-fill: " + TEXT_DARK + ";");
        sizeLbl.setPrefWidth(120);

        ProgressBar bar = new ProgressBar(progress);
        bar.setPrefWidth(110);
        bar.setPrefHeight(6);
        bar.setStyle("-fx-accent: " + colorHex + "; -fx-control-inner-background: rgba(255, 255, 255, 0.08); -fx-background-radius: 6; -fx-padding: 0;");

        Label percentLbl = new Label(percent);
        percentLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        percentLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
        percentLbl.setPrefWidth(45);
        percentLbl.setAlignment(Pos.BASELINE_RIGHT);

        HBox progressGroup = new HBox(10, bar, percentLbl);
        progressGroup.setAlignment(Pos.CENTER_LEFT);
        progressGroup.setPrefWidth(150);

        HBox row = new HBox(nameGroup, sizeLbl, progressGroup);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 10, 6, 10));
        row.setStyle("-fx-background-color: transparent; -fx-background-radius: 10;");

        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255, 255, 255, 0.04); -fx-background-radius: 10;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-background-radius: 10;"));

        return row;
    }

    private void applyPieChartColors(ObservableList<PieChart.Data> data) {
        int i = 0;
        for (PieChart.Data d : data) {
            if (d.getNode() != null) {
                d.getNode().setStyle("-fx-pie-color: " + CHART_COLORS[i % CHART_COLORS.length] + ";");
            }
            i++;
        }
    }
}