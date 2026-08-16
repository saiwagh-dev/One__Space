package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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
import java.net.URL;

public class AdminAnalytics {
    private static final String FONT = "Inter, 'Segoe UI', Arial, sans-serif";
    private static final String SIDEBAR_BG = "#1E2A3A";
    private static final String SIDEBAR_DARK = "#141D29";
    private static final String SIDEBAR_BORDER = "#334155";
    private static final String MAIN_BG = "#31435B";
    private static final String CARD_BG = "#DDE8F8";
    private static final String CARD_BORDER = "#C3D6EC";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#CBD5E1";
    private static final String BLUE = "#2563EB";
    private static final String BLUE_LIGHT = "#BFDBFE";
    private static final String PURPLE = "#7C3AED";
    private static final String PURPLE_LIGHT = "#E9D5FF";
    private static final String ORANGE = "#F59E0B";
    private static final String ORANGE_LIGHT = "#FEF3C7";

    public AdminAnalytics() {}

    public Scene getAnalyticsScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createAnalyticsContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        VBox rightSide = new VBox(createTopBar(), scrollPane);
        rightSide.setFillWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.setCenter(rightSide);

        return new Scene(root, 1200, 750);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setMaxWidth(230);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        logoText.setTextFill(Color.WHITE);

        HBox logoRow = new HBox(12, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        Label tagline = new Label("Your AI Workspace");
        tagline.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        tagline.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox logoSection = new VBox(6, logoRow, tagline);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = createSidebarButton("dashboard", "Dashboard", false);
        dashboard.setOnAction(e -> LandingPage.showAdminDashboard());

        Button users = createSidebarButton("users", "Users", false);
        users.setOnAction(e -> LandingPage.showAdminUsers());

        Button files = createSidebarButton("files", "Files", false);
        files.setOnAction(e -> LandingPage.showAdminFiles());

        Button storage = createSidebarButton("storage", "Storage", false);
        Button aiSystem = createSidebarButton("ai", "AI System", false);
        aiSystem.setOnAction(e -> LandingPage.showAdminAISystem());

        
        Button analytics = createSidebarButton("analytics", "Analytics", true);
        analytics.setOnAction(e -> LandingPage.showAnalytics());

        Button security = createSidebarButton("security", "Security", false);
        security.setOnAction(e -> LandingPage.showAdminSecurity());


        VBox navigation = new VBox(4, dashboard, users, files, storage, aiSystem, analytics, security);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button settings = createSidebarButton("settings", "Settings", false);
        settings.setOnAction(e -> LandingPage.showAdminSettings());


        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + SIDEBAR_BORDER + ";");

        Button logout = createSidebarButton("logout", "Logout", false);

        sidebar.getChildren().addAll(logoSection, navigation, spacer, settings, divider, logout);
        return sidebar;
    }

    private StackPane createLogo() {
        URL logoURL = getClass().getResource("/images/onespace-logo.png");
        if (logoURL != null) {
            ImageView imageView = new ImageView(new Image(logoURL.toExternalForm()));
            imageView.setFitWidth(42);
            imageView.setFitHeight(42);
            imageView.setPreserveRatio(true);
            return new StackPane(imageView);
        }
        Circle circle = new Circle(20, Color.web(BLUE));
        Label fallback = new Label("O");
        fallback.setFont(Font.font(FONT, FontWeight.BOLD, 20));
        fallback.setTextFill(Color.WHITE);
        return new StackPane(circle, fallback);
    }

    private Button createSidebarButton(String type, String text, boolean active) {
        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(27, 27);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.NORMAL, 16));
        label.setTextFill(Color.WHITE);

        HBox row = new HBox(14, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38);
        button.setMinHeight(38);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        String baseStyle = "-fx-background-radius: 8; -fx-cursor: hand; -fx-border-width: 0;";
        if (active) {
            button.setStyle("-fx-background-color: " + BLUE + ";" + baseStyle);
        } else {
            button.setStyle("-fx-background-color: transparent;" + baseStyle);
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color: " + SIDEBAR_DARK + ";" + baseStyle);
                icon.setStroke(Color.WHITE);
                label.setTextFill(Color.WHITE);
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent;" + baseStyle);
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                label.setTextFill(Color.WHITE);
            });
        }
        return button;
    }

    private HBox createTopBar() {
        SVGPath searchIcon = createIcon("search");
        searchIcon.setStroke(Color.web(LIGHT_SECONDARY));
        searchIcon.setStrokeWidth(2);

        StackPane searchIconBox = new StackPane(searchIcon);
        searchIconBox.setPrefSize(25, 25);

        TextField search = new TextField();
        search.setPromptText("Search in OneSpace...");
        search.setFont(Font.font(FONT, FontWeight.NORMAL, 15));
        search.setPrefHeight(38);
        search.setStyle("-fx-background-color: transparent; -fx-text-fill: #F8FAFC; -fx-prompt-text-fill: #94A3B8; -fx-border-color: transparent; -fx-padding: 0;");

        HBox searchBox = new HBox(8, searchIconBox, search);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefHeight(38);
        searchBox.setMaxWidth(500);
        searchBox.setPadding(new Insets(0, 10, 0, 12));
        searchBox.setStyle("-fx-background-color: " + SIDEBAR_DARK + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(search, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notification = new Button();
        notification.setGraphic(bell);
        notification.setPrefSize(38, 38);
        notification.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-cursor: hand;");

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: " + BLUE + "; -fx-background-radius: 50%;");

        Label admin = new Label("Admin");
        admin.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        admin.setTextFill(Color.WHITE);

        Label arrow = new Label("⌄");
        arrow.setFont(Font.font(FONT, FontWeight.NORMAL, 16));
        arrow.setTextFill(Color.web(LIGHT_SECONDARY));

        HBox profile = new HBox(8, notification, avatar, admin, arrow);
        profile.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, searchBox, spacer, profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 24, 16, 24));
        topBar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private VBox createAnalyticsContent() {
        VBox totalUsers = createAnalyticsStatCard("users", "Total Users", "—", "vs previous period  ↑", PURPLE, PURPLE_LIGHT);
        VBox filesUploaded = createAnalyticsStatCard("files", "Files Uploaded", "—", "vs previous period  ↑", BLUE, BLUE_LIGHT);
        VBox activeSessions = createAnalyticsStatCard("sessions", "Active Sessions", "—", "vs previous period  ↑", ORANGE, ORANGE_LIGHT);

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(8);
        statsGrid.setVgap(0);
        statsGrid.setStyle("-fx-text-fill: #000000;");

        ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(33.333); c1.setHgrow(Priority.ALWAYS);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(33.333); c2.setHgrow(Priority.ALWAYS);
        ColumnConstraints c3 = new ColumnConstraints(); c3.setPercentWidth(33.334); c3.setHgrow(Priority.ALWAYS);
        statsGrid.getColumnConstraints().addAll(c1, c2, c3);

        statsGrid.add(totalUsers, 0, 0);
        statsGrid.add(filesUploaded, 1, 0);
        statsGrid.add(activeSessions, 2, 0);

        VBox content = new VBox(25, statsGrid, createUserGrowthCard());
        content.setPadding(new Insets(28, 48, 45, 48));
        content.setFillWidth(true);
        content.setStyle("-fx-background-color: " + MAIN_BG + ";");
        return content;
    }

    private VBox createAnalyticsStatCard(String iconType, String title, String value, String description, String iconColor, String iconBackground) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setStyle("-fx-text-fill: #000000;");

        Label info = new Label("ⓘ");
        info.setFont(Font.font(FONT, FontWeight.NORMAL, 16));
        info.setTextFill(Color.BLACK);
        info.setStyle("-fx-text-fill: #000000;");

        HBox titleRow = new HBox(8, titleLabel, info);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setStyle("-fx-text-fill: #000000;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font(FONT, FontWeight.BOLD, 31));
        valueLabel.setTextFill(Color.BLACK);
        valueLabel.setStyle("-fx-text-fill: #000000;");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setFont(Font.font(FONT, FontWeight.NORMAL, 14));
        descriptionLabel.setTextFill(Color.BLACK);
        descriptionLabel.setStyle("-fx-text-fill: #000000;");

        VBox text = new VBox(8, titleRow, valueLabel, descriptionLabel);
        text.setAlignment(Pos.TOP_LEFT);
        text.setStyle("-fx-text-fill: #000000;");

        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(65, 80);
        iconBox.setMinSize(65, 80);
        iconBox.setMaxSize(65, 80);
        iconBox.setStyle("-fx-background-color: " + iconBackground + "; -fx-background-radius: 16;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox top = new HBox(10, text, spacer, iconBox);
        top.setAlignment(Pos.TOP_LEFT);
        top.setStyle("-fx-text-fill: #000000;");

        VBox card = new VBox(top);
        card.setPrefHeight(188);
        card.setMinHeight(188);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1; -fx-border-radius: 18; -fx-background-radius: 18; -fx-text-fill: #000000; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 7, 0, 0, 2);");
        return card;
    }

    private VBox createUserGrowthCard() {
        Label title = new Label("User Growth");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 21));
        title.setTextFill(Color.BLACK);
        title.setStyle("-fx-text-fill: #000000;");

        ComboBox<String> period = new ComboBox<>();
        period.getItems().addAll("This Month", "Last Month", "Last 3 Months", "This Year");
        period.setValue("This Month");
        period.setPrefWidth(145);
        period.setPrefHeight(38);
        period.setStyle("-fx-background-color: #EAF2FB; -fx-border-color: #C3D6EC; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-family: '" + FONT + "'; -fx-font-size: 14px; -fx-text-fill: #000000;");

        Region headingSpacer = new Region();
        HBox.setHgrow(headingSpacer, Priority.ALWAYS);

        HBox heading = new HBox(title, headingSpacer, period);
        heading.setAlignment(Pos.CENTER_LEFT);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setTickLabelFill(Color.BLACK);
        xAxis.setTickLabelFont(Font.font(FONT, FontWeight.NORMAL, 12));
        xAxis.setStyle("-fx-tick-label-fill: #000000; -fx-text-fill: #000000;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        yAxis.setTickLabelFill(Color.BLACK);
        yAxis.setTickLabelFont(Font.font(FONT, FontWeight.NORMAL, 12));
        yAxis.setStyle("-fx-tick-label-fill: #000000; -fx-text-fill: #000000;");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(false);
        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setMinHeight(270);
        chart.setPrefHeight(290);
        chart.setMaxHeight(290);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Week 1", 18));
        series.getData().add(new XYChart.Data<>("Week 2", 32));
        series.getData().add(new XYChart.Data<>("Week 3", 47));
        series.getData().add(new XYChart.Data<>("Week 4", 65));
        chart.getData().add(series);

        chart.setStyle("-fx-background-color: transparent; -fx-font-family: '" + FONT + "'; -fx-text-fill: #000000;");

        xAxis.lookupAll(".axis-tick-mark").forEach(node -> node.setStyle("-fx-stroke: #000000;"));
        yAxis.lookupAll(".axis-tick-mark").forEach(node -> node.setStyle("-fx-stroke: #000000;"));

        VBox card = new VBox(10, heading, chart);
        card.setPrefHeight(375);
        card.setMinHeight(375);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1; -fx-border-radius: 18; -fx-background-radius: 18; -fx-text-fill: #000000; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 7, 0, 0, 2);");
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
            case "storage":
                icon.setContent("M4 5 H20 L21 8 H3 Z M4 8 V20 H20 V8 M7 12 H17 M7 16 H17");
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