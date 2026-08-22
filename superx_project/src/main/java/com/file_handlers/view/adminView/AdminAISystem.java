package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class AdminAISystem {

    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String SIDEBAR_BG = "#1E2A3A";
    public static final String SIDEBAR_DARK = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";
    private static final String MAIN_BG = "#31435B";
    private static final String CARD_BG = "#DDE8F8";
    private static final String CARD_BORDER = "#C3D6EC";

    public static final String BLACK = "#000000";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";
    private static final String BLUE_LIGHT = "#BFDBFE";
    private static final String PURPLE = "#7C3AED";
    private static final String PURPLE_LIGHT = "#EDE9FE";
    private static final String GREEN = "#059669";
    private static final String GREEN_LIGHT = "#A7F3D0";
    private static final String ORANGE = "#D97706";
    private static final String ORANGE_LIGHT = "#FEF3C7";

    public AdminAISystem() {}

    public Scene getAdminAIScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createMainContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: " + MAIN_BG + "; -fx-background: " + MAIN_BG + "; -fx-background-insets: 0; -fx-padding: 0;");

        VBox rightSide = new VBox(createTopBar(), scrollPane);
        rightSide.setStyle("-fx-background-color: " + MAIN_BG + ";");
        rightSide.setFillWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.setCenter(rightSide);

        Scene scene = new Scene(root, 1200, 750);

        String cssOverride = "data:text/css," +
                ".dark-grid-label { -fx-text-fill: #000000 !important; -fx-fill: #000000 !important; }" +
                ".dark-grid-label .text { -fx-text-fill: #000000 !important; -fx-fill: #000000 !important; }";
        scene.getStylesheets().add(cssOverride);

        return scene;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.setPrefWidth(230); sidebar.setMinWidth(230); sidebar.setMaxWidth(230);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setTextFill(Color.web(WHITE));

        HBox logoRow = new HBox(10, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(4, logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardButton = createSidebarButton("dashboard", "Dashboard", false);
        Button usersButton = createSidebarButton("users", "Users", false);
        Button filesButton = createSidebarButton("files", "Files", false);
        Button collabButton = createSidebarButton("collab", "Collaboration", false);
        Button aiButton = createSidebarButton("ai", "AI System", true);
        Button analyticsButton = createSidebarButton("analytics", "Analytics", false);
        Button securityButton = createSidebarButton("security", "Security", false);

        dashboardButton.setOnAction(e -> LandingPage.showAdminDashboard());
        usersButton.setOnAction(e -> LandingPage.showAdminUsers());
        filesButton.setOnAction(e -> LandingPage.showAdminFiles());
        collabButton.setOnAction(e -> LandingPage.showAdminCollaboration());
        analyticsButton.setOnAction(e -> LandingPage.showAnalytics());
        securityButton.setOnAction(e -> LandingPage.showAdminSecurity());

        VBox navList = new VBox(4, dashboardButton, usersButton, filesButton, collabButton, aiButton, analyticsButton, securityButton);

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        Button settingsButton = createSidebarButton("settings", "Settings", false);
        settingsButton.setOnAction(e -> LandingPage.showAdminSettings());

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + SIDEBAR_BORDER + ";");

        Button logoutButton = createSidebarButton("logout", "Logout", false);
        logoutButton.setOnAction(e -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(logoSection, navList, sidebarSpacer, settingsButton, divider, logoutButton);
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
    private Button createSidebarButton(String type, String text, boolean selected) {
        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(selected ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, selected ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.web(WHITE));

        HBox row = new HBox(12, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38); button.setMinHeight(38);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        String baseStyle = "-fx-background-radius: 8; -fx-cursor: hand; -fx-border-width: 0;";
        if (selected) {
            button.setStyle("-fx-background-color: " + BLUE + ";" + baseStyle);
        } else {
            button.setStyle("-fx-background-color: transparent;" + baseStyle);
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color: #26354A;" + baseStyle);
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
        searchIconBox.setPrefSize(24, 24);

        TextField searchField = createSearchField();

        HBox searchBox = new HBox(8, searchIconBox, searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefHeight(38); searchBox.setMinHeight(38); searchBox.setMaxHeight(38);
        searchBox.setPrefWidth(420); searchBox.setMinWidth(420); searchBox.setMaxWidth(420);
        searchBox.setPadding(new Insets(0, 12, 0, 14));
        searchBox.setStyle("-fx-background-color: #141E2C; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notificationButton = new Button();
        notificationButton.setGraphic(bell);
        notificationButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: #FFFFFF; -fx-cursor: hand;");

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: " + BLUE + "; -fx-background-radius: 50%;");

        Label adminName = new Label("Admin");
        adminName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        adminName.setTextFill(Color.WHITE);

        HBox profile = new HBox(10, notificationButton, avatar, adminName);
        profile.setAlignment(Pos.CENTER);
        profile.setStyle("-fx-cursor: hand;");

        HBox topBar = new HBox(20, searchBox, spacer, profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #94A3B8; -fx-border-color: transparent; -fx-padding: 0;");
        return searchField;
    }

    private VBox createMainContent() {
        Label welcome = new Label("AI System");
        welcome.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        welcome.setTextFill(Color.WHITE);

        Label subtitle = new Label("Monitor AI operations and performance.");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox titleBox = new VBox(4, welcome, subtitle);

        VBox content = new VBox(22, titleBox, createAIStatusCard(), createStatisticsGrid(), createAccuracyCard());
        content.setPadding(new Insets(24, 28, 28, 28));
        content.setFillWidth(true);
        content.setStyle("-fx-background-color: " + MAIN_BG + ";");
        return content;
    }

    private VBox createAIStatusCard() {
        SVGPath aiIcon = createIcon("ai");
        aiIcon.setStroke(Color.web(PURPLE));
        aiIcon.setStrokeWidth(2.2);

        StackPane iconPane = new StackPane(aiIcon);
        iconPane.setPrefSize(48, 48); iconPane.setMinSize(48, 48); iconPane.setMaxSize(48, 48);
        iconPane.setStyle("-fx-background-color: " + PURPLE_LIGHT + "; -fx-background-radius: 12;");

        Circle dot = new Circle(6, Color.web(GREEN));
        Label onlineLabel = createLabel("Online", "-fx-font-family: " + FONT + "; -fx-font-size: 15px; -fx-font-weight: bold;");
        HBox onlineRow = new HBox(10, dot, onlineLabel);
        onlineRow.setAlignment(Pos.CENTER_LEFT);

        VBox statusText = new VBox(3, onlineRow, createLabel("All AI systems operational", "-fx-font-family: " + FONT + "; -fx-font-size: 13px;"));

        HBox card = new HBox(18, iconPane, statusText);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(18));

        VBox wrapper = new VBox(card);
        wrapper.setStyle(cardStyle());
        return wrapper;
    }

    private GridPane createStatisticsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(18); grid.setVgap(18);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25); col.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(col);
        }

        grid.add(createMetricCard("Documents Analyzed", "3,841", "+14.2%", "files", PURPLE_LIGHT, PURPLE), 0, 0);
        grid.add(createMetricCard("Categories Generated", "124", "+8.5%", "dashboard", BLUE_LIGHT, BLUE), 1, 0);
        grid.add(createMetricCard("Tags Generated", "1,512", "+22.0%", "security", GREEN_LIGHT, GREEN), 2, 0);
        grid.add(createMetricCard("AI Searches", "892", "+5.4%", "search", ORANGE_LIGHT, ORANGE), 3, 0);

        return grid;
    }

    private VBox createMetricCard(String title, String value, String growthText, String iconType, String iconBackground, String iconColor) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setPrefSize(38, 38); iconPane.setMaxSize(38, 38);
        iconPane.setStyle("-fx-background-color: " + iconBackground + "; -fx-background-radius: 10;");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox card = new VBox(10,
                iconPane,
                createLabel(title, "-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: bold;"),
                createLabel(value, "-fx-font-family: " + FONT + "; -fx-font-size: 26px; -fx-font-weight: bold;"),
                spacer,
                createLabel("vs last 7 days  ↑ " + growthText, "-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #059669;")
        );

        card.setPadding(new Insets(16));
        card.setPrefHeight(160);
        card.setStyle(cardStyle());
        return card;
    }

    // =========================================================
    // HIGH-VISUAL-IMPACT AI ACCURACY CARD
    // =========================================================
    private VBox createAccuracyCard() {
        Label titleLabel = createLabel("AI Accuracy & Performance Metrics", "-fx-font-family: " + FONT + "; -fx-font-size: 17px; -fx-font-weight: bold;");

        ComboBox<String> timeFilter = new ComboBox<>();
        timeFilter.getItems().addAll("Last 24 Hours", "Last 7 Days", "Last 30 Days");
        timeFilter.setValue("Last 7 Days");
        timeFilter.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #0F172A; -fx-cursor: hand;");
        timeFilter.setPrefHeight(30);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox header = new HBox(10, titleLabel, headerSpacer, timeFilter);
        header.setAlignment(Pos.CENTER_LEFT);

        // Left Visual Gauge Side
        StackPane ring = createAccuracyRing();
        
        Label statusPill = new Label("Optimal Rate");
        statusPill.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        statusPill.setTextFill(Color.web("#047857"));
        statusPill.setStyle("-fx-background-color: " + GREEN_LIGHT + "; -fx-padding: 3 8 3 8; -fx-background-radius: 10;");

        Label confidenceLabel = createLabel("Confidence Score: 94.2%", "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        VBox ringBox = new VBox(10, ring, statusPill, confidenceLabel);
        ringBox.setAlignment(Pos.CENTER);
        ringBox.setPadding(new Insets(12, 20, 12, 20));
        ringBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        // Right Task Progress Breakdown
        VBox taskBreakdown = new VBox(14,
                createAccuracyRow("File Auto-Categorization", "96.4%", 0.964, BLUE),
                createAccuracyRow("OCR & Text Extraction", "94.1%", 0.941, PURPLE),
                createAccuracyRow("Auto-Tagging & Metadata", "91.8%", 0.918, GREEN)
        );
        taskBreakdown.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(taskBreakdown, Priority.ALWAYS);

        // Sub Card Grid Layout Container
        HBox innerContainer = new HBox(24, ringBox, taskBreakdown);
        innerContainer.setAlignment(Pos.CENTER_LEFT);
        innerContainer.setPadding(new Insets(16));
        innerContainer.setStyle("-fx-background-color: #E3EEFD; -fx-border-color: #C3D6EC; -fx-border-radius: 12; -fx-background-radius: 12;");

        // Footer Meta Badges
        Label modelBadge = createLabel("Model Version: v2.4 (Trained Aug 10)", "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #1E293B;");
        modelBadge.setStyle("-fx-background-color: #CBD5E1; -fx-padding: 4 10 4 10; -fx-background-radius: 6;");

        Label overrideBadge = createLabel("User Correction Rate: 2.3%", "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #065F46;");
        overrideBadge.setStyle("-fx-background-color: " + GREEN_LIGHT + "; -fx-padding: 4 10 4 10; -fx-background-radius: 6;");

        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);

        HBox footer = new HBox(12, modelBadge, footerSpacer, overrideBadge);
        footer.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(16, header, innerContainer, footer);
        card.setPadding(new Insets(20));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(cardStyle());
        return card;
    }

    private HBox createAccuracyRow(String title, String percentText, double progress, String colorHex) {
        Label name = createLabel(title, "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");
        Label val = createLabel(percentText, "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + colorHex + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topRow = new HBox(name, spacer, val);

        // Custom High-Visual Progress Bar
        StackPane track = new StackPane();
        track.setPrefHeight(8); track.setMinHeight(8); track.setMaxHeight(8);
        track.setMaxWidth(Double.MAX_VALUE);
        track.setStyle("-fx-background-color: #CBD5E1; -fx-background-radius: 4;");

        Region fill = new Region();
        fill.setPrefHeight(8); fill.setMaxHeight(8);
        fill.setStyle("-fx-background-color: " + colorHex + "; -fx-background-radius: 4;");
        fill.prefWidthProperty().bind(track.widthProperty().multiply(progress));
        StackPane.setAlignment(fill, Pos.CENTER_LEFT);

        track.getChildren().add(fill);

        VBox box = new VBox(5, topRow, track);
        HBox.setHgrow(box, Priority.ALWAYS);

        HBox row = new HBox(box);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private StackPane createAccuracyRing() {
        Circle background = new Circle(52, Color.TRANSPARENT);
        background.setStroke(Color.web("#CBD5E1"));
        background.setStrokeWidth(10);

        Arc progress = new Arc(0, 0, 52, 52, 90, -338);
        progress.setType(ArcType.OPEN);
        progress.setFill(Color.TRANSPARENT);
        progress.setStroke(Color.web(PURPLE));
        progress.setStrokeWidth(10);
        progress.setStrokeLineCap(StrokeLineCap.ROUND);
        progress.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(124, 58, 237, 0.35), 8, 0, 0, 0);");

        VBox centerText = new VBox(0,
                createLabel("94.1%", "-fx-font-family: " + FONT + "; -fx-font-size: 21px; -fx-font-weight: bold; -fx-text-fill: " + PURPLE + ";"),
                createLabel("Accuracy", "-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #64748B;")
        );
        centerText.setAlignment(Pos.CENTER);

        StackPane container = new StackPane(background, progress, centerText);
        container.setPrefSize(120, 120);
        return container;
    }

    private String cardStyle() {
        return "-fx-background-color: " + CARD_BG + ";" +
               "-fx-border-color: " + CARD_BORDER + ";" +
               "-fx-border-width: 1;" +
               "-fx-border-radius: 14;" +
               "-fx-background-radius: 14;" +
               "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 16, 0, 0, 6);";
    }

    private Label createLabel(String text, String extraStyle) {
        Label label = new Label(text);
        label.getStyleClass().add("dark-grid-label");
        label.setTextFill(Color.BLACK);
        label.setStyle(extraStyle + " -fx-text-fill: #000000 !important; -fx-fill: #000000 !important;");

        Platform.runLater(() -> {
            Text textNode = (Text) label.lookup(".text");
            if (textNode != null) {
                textNode.setFill(Color.BLACK);
                textNode.setStyle("-fx-fill: #000000 !important;");
            }
        });

        return label;
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);
        switch (type) {
            case "dashboard": icon.setContent("M3 3 H10 V10 H3 Z M14 3 H21 V10 H14 Z M3 14 H10 V21 H3 Z M14 14 H21 V21 H14 Z"); break;
            case "users": icon.setContent("M8 11 A3 3 0 1 0 8 5 A3 3 0 0 0 8 11 Z M16 11 A3 3 0 1 0 16 5 A3 3 0 0 0 16 11 Z M2 20 C2 16 5 14 8 14 C11 14 14 16 14 20 M12 15 C14 14 17 14 19 15 C21 16 22 18 22 20"); break;
            case "files": icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13"); break;
            case "collab": icon.setContent("M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2 M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M23 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75"); break;
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