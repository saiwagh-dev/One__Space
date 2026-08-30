package com.file_handlers.view.adminView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.concurrent.Task;

import com.file_handlers.dao.AdminFileStatsDAO;

import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

public class AdminAnalytics {
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    
    // 1. Sidebar & Top Bar Tones
    private static final String SIDEBAR_BG = "#070C16";
    private static final String SIDEBAR_DARK = "#070C16";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";

    // 2. Center Canvas Radial Glow Background
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards & Text Colors
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";

    // Accent & Typography Colors
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";
    private static final String BLUE_LIGHT = "rgba(37, 99, 235, 0.15)";
    private static final String PURPLE = "#00D2FF";
    private static final String PURPLE_LIGHT = "rgba(0, 210, 255, 0.15)";
    private static final String ORANGE = "#F59E0B";
    private static final String ORANGE_LIGHT = "rgba(245, 158, 11, 0.15)";

    private final AdminFileStatsDAO statsDAO = new AdminFileStatsDAO();
    private LineChart<String, Number> userGrowthChart;
    private ComboBox<String> periodSelector;
    private Label totalUsersAnalyticsValue;
    private Label filesUploadedAnalyticsValue;

    public AdminAnalytics() {}

    public Scene getAnalyticsScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createAnalyticsContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-background-insets: 0; -fx-padding: 0;");

        VBox rightSide = new VBox(createTopBar(), scrollPane);
        rightSide.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        rightSide.setFillWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.setCenter(rightSide);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH); sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH); sidebar.setMaxWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setTextFill(Color.WHITE);

        HBox logoRow = new HBox(10, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(4, logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = createSidebarButton("dashboard", "Dashboard", false);
        dashboard.setOnAction(e -> LandingPage.showAdminDashboard());

        Button users = createSidebarButton("users", "Users", false);
        users.setOnAction(e -> LandingPage.showAdminUsers());

        Button files = createSidebarButton("files", "Files", false);
        files.setOnAction(e -> LandingPage.showAdminFiles());

        Button collab = createSidebarButton("collab", "Collaboration", false);
        collab.setOnAction(e -> LandingPage.showAdminCollaboration());

        Button aiSystem = createSidebarButton("ai", "AI System", false);
        aiSystem.setOnAction(e -> LandingPage.showAdminAISystem());

        Button analytics = createSidebarButton("analytics", "Analytics", true);
        analytics.setOnAction(e -> LandingPage.showAnalytics());

        Button security = createSidebarButton("security", "Security", false);
        security.setOnAction(e -> LandingPage.showAdminSecurity());

        VBox navigation = new VBox(4, dashboard, users, files, collab, aiSystem, analytics, security);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button settings = createSidebarButton("settings", "Settings", false);
        settings.setOnAction(e -> LandingPage.showAdminSettings());

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + SIDEBAR_BORDER + ";");

        Button logout = createSidebarButton("logout", "Logout", false);
        logout.setOnAction(e -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(logoSection, navigation, spacer, settings, divider, logout);
        return sidebar;
    }

    private StackPane createLogo() {
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

    private Button createSidebarButton(String type, String text, boolean active) {
        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.WHITE);

        HBox row = new HBox(12, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38); button.setMinHeight(38);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        if (active) {
            button.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);"
            );
        } else {
            button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.WHITE);
                label.setTextFill(Color.WHITE);
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                label.setTextFill(Color.web(WHITE));
            });
        }
        return button;
    }

    private HBox createTopBar() {
        SVGPath searchIcon = createIcon("search");
        searchIcon.setStroke(Color.web("#64748B"));
        searchIcon.setStrokeWidth(2);

        StackPane searchIconBox = new StackPane(searchIcon);
        searchIconBox.setPrefSize(24, 24);

        TextField search = new TextField();
        search.setPromptText("Search in OneSpace...");
        search.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        search.setPrefHeight(38);
        search.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #64748B; -fx-border-color: transparent; -fx-padding: 0;");

        HBox searchBox = new HBox(8, searchIconBox, search);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefHeight(38); searchBox.setMinHeight(38); searchBox.setMaxHeight(38);
        searchBox.setPrefWidth(420); searchBox.setMinWidth(420); searchBox.setMaxWidth(420);
        searchBox.setPadding(new Insets(0, 12, 0, 14));
        searchBox.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20;");
        HBox.setHgrow(search, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notification = new Button();
        notification.setGraphic(bell);
        notification.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        Label admin = new Label("Admin");
        admin.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        admin.setTextFill(Color.WHITE);

        HBox profile = new HBox(10, notification, avatar, admin);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(4, 12, 4, 6));
        profile.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");
        profile.setOnMouseClicked(e -> {
            LandingPage.showAdminProfilePage();
        });

        HBox topBar = new HBox(20, searchBox, spacer, profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private VBox createAnalyticsContent() {
        Label pageTitle = new Label("Analytics");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        pageTitle.setTextFill(Color.WHITE);

        Label subtitle = new Label("Track system performance, user engagement, and file activity.");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox headerBox = new VBox(4, pageTitle, subtitle);

        HBox totalUsers = createAnalyticsStatCard("users", "Total Users", "Loading...", "Current registered users", PURPLE, PURPLE_LIGHT);
        HBox filesUploaded = createAnalyticsStatCard("files", "Files Uploaded", "Loading...", "Current uploaded files", BLUE, BLUE_LIGHT);
        HBox activeSessions = createAnalyticsStatCard("sessions", "Active Sessions", "N/A", "Not tracked", ORANGE, ORANGE_LIGHT);

        totalUsers.setPrefWidth(260); totalUsers.setMaxWidth(260);
        filesUploaded.setPrefWidth(260); filesUploaded.setMaxWidth(260);
        activeSessions.setPrefWidth(260); activeSessions.setMaxWidth(260);

        HBox statsRow = new HBox(16, totalUsers, filesUploaded, activeSessions);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(22, headerBox, statsRow, createUserGrowthCard());
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setFillWidth(true);
        content.setStyle("-fx-background-color: transparent;");
        return content;
    }

    private HBox createAnalyticsStatCard(String iconType, String title, String value, String description, String iconColor, String iconBackground) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-text-fill: #FFFFFF;");

        Label info = new Label("ⓘ");
        info.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        info.setTextFill(Color.web(LIGHT_SECONDARY));
        info.setStyle("-fx-text-fill: #94A3B8;");

        HBox titleRow = new HBox(6, titleLabel, info);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font(FONT, FontWeight.BOLD, 26));

        if ("Total Users".equals(title)) {
            totalUsersAnalyticsValue = valueLabel;
        } else if ("Files Uploaded".equals(title)) {
            filesUploadedAnalyticsValue = valueLabel;
        }
        valueLabel.setTextFill(Color.WHITE);
        valueLabel.setStyle("-fx-text-fill: #FFFFFF;");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        descriptionLabel.setTextFill(Color.web("#38BDF8"));
        descriptionLabel.setStyle("-fx-text-fill: #38BDF8;");

        VBox text = new VBox(6, titleRow, valueLabel, descriptionLabel);
        text.setAlignment(Pos.TOP_LEFT);

        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(48, 48);
        iconBox.setMinSize(48, 48);
        iconBox.setMaxSize(48, 48);
        iconBox.setStyle("-fx-background-color: " + iconBackground + "; -fx-border-color: " + iconColor + "55; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox top = new HBox(10, text, spacer, iconBox);
        top.setAlignment(Pos.TOP_LEFT);

        HBox card = new HBox(top);
        card.setPrefHeight(160);
        card.setMinHeight(160);
        card.setMaxHeight(160);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");
        return card;
    }

    // =========================================================
    // LOAD REAL ANALYTICS DATA
    // =========================================================

    private void loadAnalyticsData() {

        Task<AnalyticsResult> task = new Task<>() {
            @Override
            protected AnalyticsResult call() throws Exception {

                Map<String, Integer> weeklyUploads =
                        statsDAO.getWeeklyUploadCounts(
                                periodSelector == null
                                        ? "This Month"
                                        : periodSelector.getValue()
                        );

                int totalFiles =
                        statsDAO.getTotalFiles();

                int totalUsers =
                        statsDAO.getTotalUsers();

                return new AnalyticsResult(
                        totalUsers,
                        totalFiles,
                        weeklyUploads
                );
            }
        };

        task.setOnSucceeded(e -> {

            AnalyticsResult result =
                    task.getValue();

            if (totalUsersAnalyticsValue != null)
                totalUsersAnalyticsValue.setText(
                        String.valueOf(result.totalUsers)
                );

            if (filesUploadedAnalyticsValue != null)
                filesUploadedAnalyticsValue.setText(
                        String.valueOf(result.totalFiles)
                );

            updateChart(result.weeklyUploads);
        });

        task.setOnFailed(e -> {

            if (totalUsersAnalyticsValue != null)
                totalUsersAnalyticsValue.setText("--");

            if (filesUploadedAnalyticsValue != null)
                filesUploadedAnalyticsValue.setText("--");

            if (userGrowthChart != null)
                userGrowthChart.getData().clear();

            System.err.println(
                    "Unable to load analytics: "
                            + task.getException()
            );
        });

        Thread thread =
                new Thread(task, "AdminAnalyticsLoader");

        thread.setDaemon(true);
        thread.start();
    }

    private void updateChart(
            Map<String, Integer> weeklyUploads
    ) {

        userGrowthChart.getData().clear();

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        for (Map.Entry<String, Integer> entry :
                weeklyUploads.entrySet()) {

            series.getData().add(
                    new XYChart.Data<>(
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }

        userGrowthChart.getData().add(series);
    }

    private static class AnalyticsResult {

        private final int totalUsers;
        private final int totalFiles;
        private final Map<String, Integer> weeklyUploads;

        private AnalyticsResult(
                int totalUsers,
                int totalFiles,
                Map<String, Integer> weeklyUploads
        ) {
            this.totalUsers = totalUsers;
            this.totalFiles = totalFiles;
            this.weeklyUploads = weeklyUploads;
        }
    }

    private VBox createUserGrowthCard() {
        Label title = new Label("User Growth");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-text-fill: #FFFFFF;");

        ComboBox<String> period = new ComboBox<>();
        period.getItems().addAll("This Month", "Last Month", "Last 3 Months", "This Year");
        period.setValue("This Month");
        period.setPrefWidth(130);
        period.setPrefHeight(30);
        period.getStyleClass().add("slate-dark-combo");
        period.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-family: '" + FONT + "'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-cursor: hand;");
        periodSelector = period;

        Region headingSpacer = new Region();
        HBox.setHgrow(headingSpacer, Priority.ALWAYS);

        HBox heading = new HBox(title, headingSpacer, period);
        heading.setAlignment(Pos.CENTER_LEFT);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setTickLabelFill(Color.WHITE);
        xAxis.setTickLabelFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        xAxis.setStyle("-fx-tick-label-fill: #FFFFFF; -fx-text-fill: #FFFFFF;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        yAxis.setTickLabelFill(Color.WHITE);
        yAxis.setTickLabelFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        yAxis.setStyle("-fx-tick-label-fill: #FFFFFF; -fx-text-fill: #FFFFFF;");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        userGrowthChart = chart;
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(false);
        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setMinHeight(290);
        chart.setPrefHeight(310);
        chart.setMaxHeight(310);

        period.valueProperty().addListener((obs, oldValue, newValue) -> {
            loadAnalyticsData();
        });

        loadAnalyticsData();

        // Completely clear plot background and chart container background fills to remove the grey area under the line
        chart.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        chart.lookupAll(".chart-plot-background").forEach(n -> n.setStyle("-fx-background-color: transparent;"));

        xAxis.lookupAll(".axis-tick-mark").forEach(node -> node.setStyle("-fx-stroke: rgba(255, 255, 255, 0.2);"));
        yAxis.lookupAll(".axis-tick-mark").forEach(node -> node.setStyle("-fx-stroke: rgba(255, 255, 255, 0.2);"));

        VBox card = new VBox(14, heading, chart);
        card.setPrefWidth(815);
        card.setMaxWidth(815);
        card.setPrefHeight(390);
        card.setMinHeight(390);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-text-fill: #FFFFFF; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");
        return card;
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);

        switch (type) {
            case "dashboard":
                icon.setContent("M3 3 H10 V10 H3 Z M14 3 H21 V10 H14 Z M3 14 H10 V21 H3 Z M14 14 H21 V21 H14 Z");
                break;
            case "users":
                icon.setContent("M8 11 A3 3 0 1 0 8 5 A3 3 0 0 0 8 11 Z M16 11 A3 3 0 1 0 16 5 A3 3 0 0 0 16 11 Z M2 20 C2 16 5 14 8 14 C11 14 14 16 14 20 M12 15 C14 14 17 14 19 15 C21 16 22 18 22 20");
                break;
            case "files":
                icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13");
                break;
            case "collab":
                icon.setContent("M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2 M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M23 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75");
                break;
            case "ai":
                icon.setContent("M12 2 L13.5 8.5 L20 7 L15.5 11.5 L21 15 L14 14.5 L12 22 L10 14.5 L3 15 L8.5 11.5 L4 7 L10.5 8.5 Z");
                break;
            case "analytics":
                icon.setContent("M4 20 V11 M10 20 V6 M16 20 V13 M22 20 V3");
                break;
            case "security":
                icon.setContent("M12 2 L20 5 V11 C20 16 17 20 12 22 C7 20 4 16 4 11 V5 Z M9 12 L11 14 L15 9");
                break;
            case "settings":
                icon.setContent("M12 3 V6 M12 18 V21 M3 12 H6 M18 12 H21 M5.6 5.6 L7.7 7.7 M16.3 16.3 L18.4 18.4 M18.4 5.6 L16.3 7.7 M7.7 16.3 L5.6 18.4 M12 8 A4 4 0 1 0 12 16 A4 4 0 0 0 12 8");
                break;
            case "logout":
                icon.setContent("M10 4 H5 V20 H10 M14 8 L19 12 L14 16 M19 12 H8");
                break;
            case "search":
                icon.setContent("M10 3 A7 7 0 1 0 10 17 A7 7 0 0 0 10 3 Z M15 15 L21 21");
                break;
            case "bell":
                icon.setContent("M6 17 H18 M8 17 V10 A4 4 0 0 1 16 10 V17 M10 20 H14");
                break;
            case "sessions":
                icon.setContent("M12 3 A9 9 0 1 0 21 12 M12 3 V12 H21");
                break;
            default:
                icon.setContent("M4 4 H20 V20 H4 Z");
                break;
        }
        return icon;
    }
}