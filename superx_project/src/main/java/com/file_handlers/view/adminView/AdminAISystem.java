package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;
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
import java.io.InputStream;

public class AdminAISystem {

    private static final String FONT = "Inter, 'Segoe UI', Arial, sans-serif";
    private static final String SIDEBAR_BG = "#1E2A3A";
    private static final String SIDEBAR_DARK = "#141D29";
    private static final String SIDEBAR_BORDER = "#334155";
    private static final String MAIN_BG = "#31435B";
    private static final String CARD_BG = "#DDE8F8";
    private static final String CARD_BORDER = "#C3D6EC";

    private static final String BLACK = "#000000";

    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#CBD5E1";
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
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createMainContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        VBox rightSide = new VBox(createTopBar(), scrollPane);
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

    private Button createSidebarButton(String type, String text, boolean selected) {
        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(selected ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(27, 27);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, selected ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
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
        if (selected) {
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

        TextField searchField = createSearchField();

        HBox searchBox = new HBox(8, searchIconBox, searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefHeight(38);
        searchBox.setMaxWidth(Double.MAX_VALUE);
        searchBox.setPadding(new Insets(0, 10, 0, 12));
        searchBox.setStyle("-fx-background-color: " + SIDEBAR_DARK + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notificationButton = new Button();
        notificationButton.setGraphic(bell);
        notificationButton.setPrefSize(38, 38);
        notificationButton.setStyle("-fx-background-color: transparent; -fx-font-size: 19px; -fx-text-fill: #FFFFFF; -fx-cursor: hand;");

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: " + BLUE + "; -fx-background-radius: 50%;");

        Label adminName = new Label("Admin");
        adminName.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        adminName.setTextFill(Color.WHITE);

        Label dropdown = new Label("⌄");
        dropdown.setFont(Font.font(FONT, FontWeight.NORMAL, 16));
        dropdown.setTextFill(Color.web(LIGHT_SECONDARY));

        HBox profile = new HBox(8, notificationButton, avatar, adminName, dropdown);
        profile.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, searchBox, spacer, profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 24, 16, 24));
        topBar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setFont(Font.font(FONT, FontWeight.NORMAL, 15));
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color: transparent; -fx-text-fill: #F8FAFC; -fx-prompt-text-fill: #94A3B8; -fx-border-color: transparent; -fx-padding: 0;");
        return searchField;
    }

    private VBox createMainContent() {
        Label welcome = new Label("AI System");
        welcome.setFont(Font.font(FONT, FontWeight.BOLD, 36));
        welcome.setTextFill(Color.WHITE);

        Label subtitle = new Label("Monitor AI operations and performance.");
        subtitle.setFont(Font.font(FONT, FontWeight.NORMAL, 16));
        subtitle.setTextFill(Color.WHITE);

        VBox titleBox = new VBox(7, welcome, subtitle);

        GridPane bottomGrid = new GridPane();
        bottomGrid.setHgap(22); bottomGrid.setVgap(22);

        ColumnConstraints leftColumn = new ColumnConstraints();
        leftColumn.setPercentWidth(50); leftColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints rightColumn = new ColumnConstraints();
        rightColumn.setPercentWidth(50); rightColumn.setHgrow(Priority.ALWAYS);
        bottomGrid.getColumnConstraints().addAll(leftColumn, rightColumn);

        bottomGrid.add(createAccuracyCard(), 0, 0);

        VBox content = new VBox(25, titleBox, createAIStatusCard(), createStatisticsGrid(), bottomGrid);
        content.setPadding(new Insets(42, 48, 45, 48));
        content.setFillWidth(true);
        content.setStyle("-fx-background-color: " + MAIN_BG + ";");
        return content;
    }

    private VBox createAIStatusCard() {
        SVGPath aiIcon = createIcon("ai");
        aiIcon.setStroke(Color.web(PURPLE));
        aiIcon.setStrokeWidth(2.2);

        StackPane iconPane = new StackPane(aiIcon);
        iconPane.setPrefSize(55, 55);
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
        grid.setHgap(22); grid.setVgap(22);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25); col.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(col);
        }

        grid.add(createMetricCard("Documents Analyzed", "files", PURPLE_LIGHT, PURPLE), 0, 0);
        grid.add(createMetricCard("Categories Generated", "dashboard", BLUE_LIGHT, BLUE), 1, 0);
        grid.add(createMetricCard("Tags Generated", "security", GREEN_LIGHT, GREEN), 2, 0);
        grid.add(createMetricCard("AI Searches", "search", ORANGE_LIGHT, ORANGE), 3, 0);

        return grid;
    }

    private VBox createMetricCard(String title, String iconType, String iconBackground, String iconColor) {
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
                createLabel("—", "-fx-font-family: " + FONT + "; -fx-font-size: 26px; -fx-font-weight: bold;"),
                spacer,
                createLabel("vs last 7 days  ↑", "-fx-font-family: " + FONT + "; -fx-font-size: 12px;")
        );

        card.setPadding(new Insets(16));
        card.setPrefHeight(160);
        card.setStyle(cardStyle());
        return card;
    }

    private VBox createAccuracyCard() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(
                createLabel("AI Accuracy", "-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-font-weight: bold;"),
                spacer,
                createLabel("ⓘ", "-fx-font-family: " + FONT + "; -fx-font-size: 14px;")
        );

        StackPane ring = createAccuracyRing();
        VBox.setVgrow(ring, Priority.ALWAYS);

        HBox comparisonBox = new HBox(createLabel("vs last 7 days  ↑", "-fx-font-family: " + FONT + "; -fx-font-size: 12px;"));
        comparisonBox.setAlignment(Pos.CENTER);

        VBox card = new VBox(12, header, ring, comparisonBox);
        card.setPadding(new Insets(20));
        card.setPrefHeight(300);
        card.setStyle(cardStyle());
        return card;
    }

    private StackPane createAccuracyRing() {
        Circle background = new Circle(78, Color.TRANSPARENT);
        background.setStroke(Color.web("#CBD5E1"));
        background.setStrokeWidth(14);

        Arc progress = new Arc(0, 0, 78, 78, 90, -270);
        progress.setType(ArcType.OPEN);
        progress.setFill(Color.TRANSPARENT);
        progress.setStroke(Color.web(PURPLE));
        progress.setStrokeWidth(14);
        progress.setStrokeLineCap(StrokeLineCap.ROUND);

        VBox centerText = new VBox(3,
                createLabel("—%", "-fx-font-family: " + FONT + "; -fx-font-size: 25px; -fx-font-weight: bold;"),
                createLabel("Accuracy  ⓘ", "-fx-font-family: " + FONT + "; -fx-font-size: 11px;")
        );
        centerText.setAlignment(Pos.CENTER);

        StackPane container = new StackPane(background, progress, centerText);
        container.setPrefHeight(190);
        return container;
    }

    private String cardStyle() {
        return "-fx-background-color: " + CARD_BG + ";" +
               "-fx-border-color: " + CARD_BORDER + ";" +
               "-fx-border-width: 1;" +
               "-fx-border-radius: 14;" +
               "-fx-background-radius: 14;" +
               "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 7, 0, 0, 2);";
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