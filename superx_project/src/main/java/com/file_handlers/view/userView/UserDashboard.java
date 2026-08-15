package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UserDashboard {

    // Style Constants - Exact Color Hierarchy
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
    private static final String[] CHART_COLORS = {"#2563EB", "#0284C7", "#059669", "#7C3AED", "#475569"};

    public Scene getDashboardScene() {

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

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", true);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);

        dashboardBtn.setOnAction(e -> { LandingPage.showUserDashboard(); });
        spacesBtn.setOnAction(e -> { LandingPage.showUserSpace(); });
        searchBtn.setOnAction(e -> { LandingPage.showUserSearch(); });
        calendarBtn.setOnAction(e -> { LandingPage.showCalendarPage(); });
        aiBtn.setOnAction(e -> { LandingPage.showLandingPage(); });
        collabBtn.setOnAction(e -> { LandingPage.showLandingPage(); });
        recentBtn.setOnAction(e -> { LandingPage.showLandingPage(); });
        trashBtn.setOnAction(e -> { LandingPage.showTrashPage(); });
        settingsBtn.setOnAction(e -> { LandingPage.showLandingPage(); });

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
        manageStorageBtn.setOnAction(e -> { LandingPage.showLandingPage(); });

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, storageCard);
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
        // GREETING & SCAN ACTION HEADER
        // =========================================================

        Label welcomeTitle = new Label("Good afternoon, Aarav");
        welcomeTitle.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        welcomeTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label welcomeSub = new Label("OneSpace indexed 412 new files since yesterday — nothing was moved or renamed.");
        welcomeSub.setFont(Font.font(FONT, 13));
        welcomeSub.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        VBox greetingText = new VBox(4, welcomeTitle, welcomeSub);

        Button scanFolderBtn = new Button("⛶  Scan folder");
        scanFolderBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        scanFolderBtn.setStyle(
                "-fx-background-color: " + PRIMARY_BLUE + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 8 18;"
        );
        scanFolderBtn.setOnAction(e -> { LandingPage.showLandingPage(); });

        HBox greetingHeader = new HBox(greetingText, new Region(), scanFolderBtn);
        HBox.setHgrow(greetingHeader.getChildren().get(1), Priority.ALWAYS);
        greetingHeader.setAlignment(Pos.CENTER_LEFT);

        // =========================================================
        // TOP 4 FILE OVERVIEW / TELEMETRY METRIC CARDS
        // =========================================================

        HBox card1 = createMetricCard("📁", "Indexing Activity", "7,032", "● 84 auto-tagged", "+412 files today", "#2563EB", "#CADDF2", "#1D4ED8");
        HBox card2 = createMetricCard("▦", "Active Spaces", "8 Spaces", "2 AI generated", "🔥 Java Project (64%)", "#0284C7", "#BAE6FD", "#0369A1");
        HBox card3 = createMetricCard("💾", "Indexed Storage", "64.2 GB", "● Synced 2m ago", "4.2 GB recoverable", "#059669", "#A7F3D0", "#065F46");
        HBox card4 = createMetricCard("✦", "AI Actions Live", "126 Actions", "⚡ Live pipeline", "12 summaries · 8 links", "#D97706", "#FDE68A", "#92400E");

        HBox metricsRow = new HBox(14, card1, card2, card3, card4);
        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);
        HBox.setHgrow(card4, Priority.ALWAYS);

        // =========================================================
        // SPACE OCCUPANCY CARD
        // =========================================================

        Label cardTitle = new Label("Space Occupancy");
        cardTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        cardTitle.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label cardSub = new Label("Overview of file storage across your spaces.");
        cardSub.setFont(Font.font(FONT, 12));
        cardSub.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox cardHeaderTitles = new VBox(2, cardTitle, cardSub);

        Button viewAllBtn = new Button("View all spaces ›");
        viewAllBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        viewAllBtn.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " + PRIMARY_BLUE + ";" +
                "-fx-padding: 6 14;" +
                "-fx-cursor: hand;"
        );
        viewAllBtn.setOnAction(e -> { LandingPage.showLandingPage(); });

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
        chart.setPrefSize(205, 205);
        chart.setMaxSize(205, 205);

        Circle donutHole = new Circle(66, Color.web(BG_CARD));

        Label chartValText = new Label("64.2 GB");
        chartValText.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        chartValText.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label chartSubText = new Label("of 100 GB used");
        chartSubText.setFont(Font.font(FONT, 11));
        chartSubText.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 600;");

        VBox chartCenterText = new VBox(2, chartValText, chartSubText);
        chartCenterText.setAlignment(Pos.CENTER);

        StackPane donutChartPane = new StackPane(chart, donutHole, chartCenterText);
        donutChartPane.setPadding(new Insets(8));

        // Space Breakdown Table
        HBox tableHeader = new HBox(
                createHeaderLabel("Space", 220),
                createHeaderLabel("Storage Used", 130),
                createHeaderLabel("Percentage", 160)
        );
        tableHeader.setPadding(new Insets(0, 0, 8, 0));

        VBox spaceRows = new VBox(11,
                tableHeader,
                createSpaceRow("📁", CHART_COLORS[0], "Java Project", "22.4 GB", 0.34, "34%", CHART_COLORS[0]),
                createSpaceRow("📁", CHART_COLORS[1], "Placement Preparation", "18.7 GB", 0.29, "29%", CHART_COLORS[1]),
                createSpaceRow("📁", CHART_COLORS[2], "College Assignments", "12.6 GB", 0.20, "20%", CHART_COLORS[2]),
                createSpaceRow("📁", CHART_COLORS[3], "Personal Documents", "6.8 GB", 0.11, "11%", CHART_COLORS[3]),
                createSpaceRow("📁", CHART_COLORS[4], "Others", "3.7 GB", 0.06, "6%", CHART_COLORS[4])
        );

        HBox cardContent = new HBox(36, donutChartPane, spaceRows);
        cardContent.setAlignment(Pos.CENTER_LEFT);

        Label lastUpdated = new Label("🕒  Last updated just now");
        lastUpdated.setFont(Font.font(FONT, 11));
        lastUpdated.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 500;");

        VBox occupancyCard = new VBox(16, cardHeader, cardContent, lastUpdated);
        occupancyCard.setPadding(new Insets(24));
        occupancyCard.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 16, 0, 0, 6);"
        );

        // =========================================================
        // SCROLLABLE CONTAINER
        // =========================================================

        VBox contentBody = new VBox(22, greetingHeader, metricsRow, occupancyCard);
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

        Scene scene = new Scene(root, 1200, 750);
        scene.setOnMouseEntered(e -> applyPieChartColors(pieChartData));

        return scene;
    }

    // =========================================================
    // HELPER BUILDERS
    // =========================================================

    private StackPane createOneSpaceLogo() {
        SVGPath cloudPath = new SVGPath();
        cloudPath.setContent("M 6 15 A 6 6 0 0 1 18 10 A 5 5 0 0 1 26 13 A 4 4 0 0 1 25 21 L 6 21 A 3 3 0 0 1 6 15 Z");
        cloudPath.setFill(Color.TRANSPARENT);
        cloudPath.setStroke(Color.web("#38BDF8"));
        cloudPath.setStrokeWidth(2.2);

        Label docSymbol = new Label("📄");
        docSymbol.setFont(Font.font(13));
        docSymbol.setStyle("-fx-text-fill: #818CF8;");

        StackPane logoPane = new StackPane(cloudPath, docSymbol);
        logoPane.setPrefSize(32, 32);
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

    private HBox createMetricCard(String icon, String title, String value, String badgeText, String subText, String accentColor, String bgAccent, String textBadgeColor) {
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        titleLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(14));
        iconLbl.setStyle("-fx-text-fill: " + accentColor + ";");

        Label iconBox = new Label("", iconLbl);
        iconBox.setPrefSize(32, 32);
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setStyle("-fx-background-color: " + bgAccent + "; -fx-background-radius: 8;");

        HBox topRow = new HBox(titleLbl, new Region(), iconBox);
        HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label valLbl = new Label(value);
        valLbl.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        valLbl.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label badgeLbl = new Label(badgeText);
        badgeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        badgeLbl.setStyle("-fx-text-fill: " + textBadgeColor + "; -fx-background-color: " + bgAccent + "; -fx-background-radius: 6; -fx-padding: 3 8;");

        Label subLbl = new Label(subText);
        subLbl.setFont(Font.font(FONT, 11));
        subLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 600;");

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
        lbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
        lbl.setPrefWidth(width);
        return lbl;
    }

    private HBox createSpaceRow(String icon, String iconHex, String title, String storage, double progress, String percent, String colorHex) {
        Label folderIcon = new Label(icon);
        folderIcon.setFont(Font.font(12));
        folderIcon.setPrefSize(24, 24);
        folderIcon.setAlignment(Pos.CENTER);
        folderIcon.setStyle("-fx-background-color: " + iconHex + "22; -fx-background-radius: 6; -fx-text-fill: " + iconHex + ";");

        Label spaceName = new Label(title);
        spaceName.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        spaceName.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        HBox nameGroup = new HBox(10, folderIcon, spaceName);
        nameGroup.setAlignment(Pos.CENTER_LEFT);
        nameGroup.setPrefWidth(220);

        Label sizeLbl = new Label(storage);
        sizeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        sizeLbl.setStyle("-fx-text-fill: " + TEXT_DARK + ";");
        sizeLbl.setPrefWidth(130);

        ProgressBar bar = new ProgressBar(progress);
        bar.setPrefWidth(100);
        bar.setPrefHeight(6);
        bar.setStyle("-fx-accent: " + colorHex + "; -fx-control-inner-background: #B6CDE7;");

        Label percentLbl = new Label(percent);
        percentLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        percentLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
        percentLbl.setPrefWidth(45);
        percentLbl.setAlignment(Pos.BASELINE_RIGHT);

        HBox progressGroup = new HBox(10, bar, percentLbl);
        progressGroup.setAlignment(Pos.CENTER_LEFT);
        progressGroup.setPrefWidth(160);

        HBox row = new HBox(nameGroup, sizeLbl, progressGroup);
        row.setAlignment(Pos.CENTER_LEFT);
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