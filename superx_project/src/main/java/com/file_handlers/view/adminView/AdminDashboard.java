package com.file_handlers.view.adminView;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
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
import java.io.InputStream;
import com.file_handlers.view.LandingPage;

public class AdminDashboard {
    private static final String FONT = "Inter, 'Segoe UI', Arial, sans-serif";
    private static final String SIDEBAR_BG = "#1E2A3A";
    private static final String SIDEBAR_DARK = "#141D29";
    private static final String SIDEBAR_BORDER = "#334155";
    private static final String MAIN_BG = "#31435B";
    private static final String CARD_TITLE = "#0B1220";
    private static final String CARD_VALUE = "#020617";
    private static final String CARD_SECONDARY = "#1E293B";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#CBD5E1";
    private static final String BLUE = "#2563EB";
    private static final String BLUE_LIGHT = "#BFDBFE";
    private static final String CYAN = "#0284C7";
    private static final String CYAN_LIGHT = "#BAE6FD";
    private static final String GREEN = "#059669";

    public AdminDashboard() {}

    public Scene getAdminDashboardScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createDashboardContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
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
        sidebar.setPrefWidth(230); sidebar.setMinWidth(230); sidebar.setMaxWidth(230);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        logoText.setTextFill(Color.web(WHITE));

        HBox logoRow = new HBox(12, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = createSidebarButton("dashboard", "Dashboard", true);
        Button users = createSidebarButton("users", "Users", false);
        users.setOnAction(e -> LandingPage.showAdminUsers());
        Button files = createSidebarButton("files", "Files", false);
        files.setOnAction(e -> LandingPage.showAdminFiles());
        Button collaboration = createSidebarButton("collaboration", "Collaboration", false);
        collaboration.setOnAction(e -> LandingPage.showAdminCollaboration());

        Button aiSystem = createSidebarButton("ai", "AI System", false);
        aiSystem.setOnAction(e -> LandingPage.showAdminAISystem());

        Button analytics = createSidebarButton("analytics", "Analytics", false);
        analytics.setOnAction(e -> LandingPage.showAnalytics());
        
        Button security = createSidebarButton("security", "Security", false);
        security.setOnAction(e -> LandingPage.showAdminSecurity());

        VBox navigation = new VBox(4, dashboard, users, files, collaboration, aiSystem, analytics, security);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button settings = createSidebarButton("settings", "Settings", false);
        settings.setOnAction(e -> LandingPage.showAdminSettings());

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + SIDEBAR_BORDER + ";");

        Button logout = createSidebarButton("logout", "Logout", false);
        logout.setOnAction(event -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(logoSection, navigation, spacer, settings, divider, logout);
        return sidebar;
    }

    private StackPane createLogo() {
        InputStream stream = getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png");
        if (stream != null) {
            Image logoImage = new Image(stream);
            ImageView imageView = new ImageView(logoImage);
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
        label.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.web(WHITE));

        HBox row = new HBox(14, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38); button.setMinHeight(38);
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
        searchBox.setPrefHeight(38); searchBox.setMaxWidth(Double.MAX_VALUE);
        searchBox.setPadding(new Insets(0, 10, 0, 12));
        searchBox.setStyle("-fx-background-color: " + SIDEBAR_DARK + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        HBox.setHgrow(search, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notification = new Button();
        notification.setGraphic(bell);
        notification.setPrefSize(38, 38);
        notification.setStyle("-fx-background-color: transparent; -fx-font-size: 19px; -fx-text-fill: #FFFFFF; -fx-cursor: hand;");

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34); avatar.setAlignment(Pos.CENTER);
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

    private VBox createDashboardContent() {
        Label welcome = new Label("Good Evening, Admin!");
        welcome.setStyle("-fx-font-family: '" + FONT + "'; -fx-font-size: 36px; -fx-font-weight: 700; -fx-text-fill: #FFFFFF;");

        Label subtitle = new Label("Here's what's happening in OneSpace today.");
        subtitle.setStyle("-fx-font-family: '" + FONT + "'; -fx-font-size: 16px; -fx-font-weight: 400; -fx-text-fill: #FFFFFF;");

        VBox heading = new VBox(7, welcome, subtitle);

        GridPane grid = new GridPane();
        grid.setHgap(22); grid.setVgap(22);
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints firstColumn = new ColumnConstraints();
        firstColumn.setPercentWidth(50); firstColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints secondColumn = new ColumnConstraints();
        secondColumn.setPercentWidth(50); secondColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(firstColumn, secondColumn);

        VBox totalUsers = createStatCard("users", "Total Users", "—", "—% from last month", BLUE, BLUE_LIGHT, 
                e -> Platform.runLater(LandingPage::showAdminUsers));
                
        VBox totalFiles = createStatCard("files", "Total Files", "—", "—% from last month", CYAN, CYAN_LIGHT, 
                e -> Platform.runLater(LandingPage::showAdminFiles));

        grid.add(totalUsers, 0, 0);
        grid.add(totalFiles, 1, 0);

        VBox systemHealth = createSystemHealth();

        VBox content = new VBox(25, heading, grid, systemHealth);
        content.setPadding(new Insets(30, 35, 30, 35));
        content.setFillWidth(true);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setStyle("-fx-background-color: " + MAIN_BG + ";");
        return content;
    }

    private VBox createStatCard(String iconType, String title, String value, String description, String iconColor, String iconBackground, EventHandler<MouseEvent> onClick) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-family: '" + FONT + "'; -fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: " + CARD_TITLE + ";");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-family: '" + FONT + "'; -fx-font-size: 31px; -fx-font-weight: 700; -fx-text-fill: " + CARD_VALUE + ";");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-font-family: '" + FONT + "'; -fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: " + CARD_SECONDARY + ";");

        VBox text = new VBox(8, titleLabel, valueLabel, descriptionLabel);
        text.setAlignment(Pos.CENTER_LEFT);

        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(2.2);

        StackPane iconCircle = new StackPane(icon);
        iconCircle.setPrefSize(75, 75); iconCircle.setMinSize(75, 75); iconCircle.setMaxSize(75, 75);
        iconCircle.setStyle("-fx-background-color: " + iconBackground + "; -fx-background-radius: 50%;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(15, text, spacer, iconCircle);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(row);
        card.setPrefHeight(188); card.setMinHeight(188);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(25, 28, 25, 28));
        card.setFocusTraversable(false);
        card.setCache(true);
        card.setStyle("-fx-background-color: #DDE8F8; -fx-border-color: #C3D6EC; -fx-border-width: 1; -fx-border-radius: 18; -fx-background-radius: 18; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 7, 0, 0, 2);");

        if (onClick != null) {
            card.setOnMouseClicked(e -> onClick.handle(e));
        }

        return card;
    }

    private VBox createSystemHealth() {
        Label title = new Label("System Health");
        title.setStyle("-fx-font-family: '" + FONT + "'; -fx-font-size: 21px; -fx-font-weight: 700; -fx-text-fill: " + CARD_TITLE + ";");

        Label subtitle = new Label("Current status of OneSpace services.");
        subtitle.setStyle("-fx-font-family: '" + FONT + "'; -fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: " + CARD_SECONDARY + ";");

        VBox heading = new VBox(6, title, subtitle);
        VBox services = new VBox(4,
                createHealthRow("Database"),
                createHealthRow("File Storage"),
                createHealthRow("AI Service"),
                createHealthRow("Authentication"),
                createHealthRow("Backup Service")
        );

        VBox card = new VBox(18, heading, services);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPrefHeight(420);
        card.setPadding(new Insets(28, 30, 25, 30));
        card.setStyle("-fx-background-color: #DDE8F8; -fx-border-color: #C3D6EC; -fx-border-width: 1; -fx-border-radius: 18; -fx-background-radius: 18; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 7, 0, 0, 2);");
        return card;
    }

    private HBox createHealthRow(String serviceName) {
        Label service = new Label(serviceName);
        service.setStyle("-fx-font-family: '" + FONT + "'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: " + CARD_SECONDARY + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Circle dot = new Circle(5);
        dot.setFill(Color.web(GREEN));

        Label online = new Label("Online");
        online.setStyle("-fx-font-family: '" + FONT + "'; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #047857;");

        HBox status = new HBox(8, dot, online);
        status.setAlignment(Pos.CENTER);
        status.setPadding(new Insets(9, 17, 9, 17));
        status.setStyle("-fx-background-color: #A7F3D0; -fx-background-radius: 22;");

        HBox row = new HBox(10, service, spacer, status);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefHeight(50);
        return row;
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);
        switch (type) {
            case "dashboard": icon.setContent("M3 3 H10 V10 H3 Z M14 3 H21 V10 H14 Z M3 14 H10 V21 H3 Z M14 14 H21 V21 H14 Z"); break;
            case "users": icon.setContent("M8 11 A3 3 0 1 0 8 5 A3 3 0 0 0 8 11 Z M16 11 A3 3 0 1 0 16 5 A3 3 0 0 0 16 11 Z M2 20 C2 16 5 14 8 14 C11 14 14 16 14 20 M12 15 C14 14 17 14 19 15 C21 16 22 18 22 20"); break;
            case "files": icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13"); break;
            case "collaboration": icon.setContent("M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2 M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M23 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75"); break;
            case "ai": icon.setContent("M12 2 L13.5 8.5 L20 7 L15.5 11.5 L21 15 L14 14.5 L12 22 L10 14.5 L3 15 L8.5 11.5 L4 7 L10.5 8.5 Z"); break;
            case "analytics": icon.setContent("M4 20 V11 M10 20 V6 M16 20 V13 M22 20 V3"); break;
            case "security": icon.setContent("M12 2 L20 5 V11 C20 16 17 20 12 22 C7 20 4 16 4 11 V5 Z M9 12 L11 14 L15 9"); break;
            case "settings": icon.setContent("M12 3 V6 M12 18 V21 M3 12 H6 M18 12 H21 M5.6 5.6 L7.7 7.7 M16.3 16.3 L18.4 18.4 M18.4 5.6 L16.3 7.7 M7.7 16.3 L5.6 18.4 M12 8 A4 4 0 1 0 12 16 A4 4 0 0 0 12 8"); break;
            case "logout": icon.setContent("M10 4 H5 V20 H10 M14 8 L19 12 L14 16 M19 12 H8"); break;
            case "search": icon.setContent("M10 3 A7 7 0 1 0 10 17 A7 7 0 0 0 10 3 Z M15 15 L21 21"); break;
            case "bell": icon.setContent("M6 17 H18 M8 17 V10 A4 4 0 0 1 16 10 V17 M10 20 H14"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }
}