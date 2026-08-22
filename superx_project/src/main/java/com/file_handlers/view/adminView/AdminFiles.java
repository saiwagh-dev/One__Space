package com.file_handlers.view.adminView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
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
//import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.file_handlers.view.LandingPage;

public class AdminFiles {
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String SIDEBAR_BG = "#1E2A3A";
    public static final String SIDEBAR_DARK = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";
    private static final String MAIN_BG = "#31435B";
    private static final String CARD_BG = "#DDE8F8";
    private static final String CARD_BORDER = "#C3D6EC";
    private static final String BLACK = "#000000";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";
    private static final String PDF_COLOR = "#4F46E5";
    private static final String IMAGE_COLOR = "#10B981";
    private static final String DOCUMENT_COLOR = "#F97316";
    private static final String VIDEO_COLOR = "#8B5CF6";
    private static final String OTHER_COLOR = "#A78BFA";
    private static final String TOTAL_FILES_BG = "#D8B4FE";
    private static final String TOTAL_FILES_BORDER = "#C084FC";

    public AdminFiles() {}

    public Scene getAdminFilesScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createFilesContent());
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

        return new Scene(root, 1200, 750);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.setPrefWidth(230); sidebar.setMinWidth(230); sidebar.setMaxWidth(230);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setTextFill(Color.web(WHITE));
        logoText.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 19px; -fx-font-weight: 700; -fx-text-fill: #FFFFFF;");

        HBox logoRow = new HBox(10, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(4, logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = createSidebarButton("dashboard", "Dashboard", false);
        dashboard.setOnAction(e -> LandingPage.showAdminDashboard());
        Button users = createSidebarButton("users", "Users", false);
        users.setOnAction(e -> LandingPage.showAdminUsers());
        Button files = createSidebarButton("files", "Files", true);
        files.setOnAction(e -> LandingPage.showAdminFiles());
        Button collab = createSidebarButton("collab", "Collaboration", false);
        collab.setOnAction(e -> LandingPage.showAdminCollaboration());
        Button aiSystem = createSidebarButton("ai", "AI System", false);
        aiSystem.setOnAction(e -> LandingPage.showAdminAISystem());

        Button analytics = createSidebarButton("analytics", "Analytics", false);
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
        logout.setOnAction(event -> LandingPage.showAdminLoginPage());

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
        label.setTextFill(Color.web(WHITE));
        label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: " + (active ? "700" : "500") + "; -fx-text-fill: #FFFFFF;");

        HBox row = new HBox(12, iconBox, label);
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

        TextField search = new TextField();
        search.setPromptText("Search in OneSpace...");
        search.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        search.setPrefHeight(38);
        search.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #94A3B8; -fx-border-color: transparent; -fx-padding: 0;");

        HBox searchBox = new HBox(8, searchIconBox, search);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefHeight(38); searchBox.setMinHeight(38); searchBox.setMaxHeight(38);
        searchBox.setPrefWidth(420); searchBox.setMinWidth(420); searchBox.setMaxWidth(420);
        searchBox.setPadding(new Insets(0, 12, 0, 14));
        searchBox.setStyle("-fx-background-color: #141E2C; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(search, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notification = new Button();
        notification.setGraphic(bell);
        notification.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: #FFFFFF; -fx-cursor: hand;");

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-background-color: " + BLUE + "; -fx-background-radius: 50%; -fx-text-fill: #FFFFFF;");

        Label admin = new Label("Admin");
        admin.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        admin.setTextFill(Color.WHITE);
        admin.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #FFFFFF;");

        HBox profile = new HBox(10, notification, avatar, admin);
        profile.setAlignment(Pos.CENTER);
        profile.setStyle("-fx-cursor: hand;");

        HBox topBar = new HBox(20, searchBox, spacer, profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private VBox createFilesContent() {
        Label title = new Label("Files");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 24px; -fx-text-fill: #FFFFFF; -fx-font-weight: 700;");

        Label subtitle = new Label("Manage and monitor all files stored in OneSpace.");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox heading = new VBox(4, title, subtitle);

        VBox cardsContainer = new VBox(20, createFileTypesOverview(), createMostUsedCategories());
        cardsContainer.setMaxWidth(820);
        cardsContainer.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(24, heading, cardsContainer);
        content.setPadding(new Insets(24, 36, 36, 36));
        content.setFillWidth(true);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setStyle("-fx-background-color: " + MAIN_BG + ";");
        return content;
    }

    private VBox createFileTypesOverview() {
        Label title = new Label("File Types Overview");
        title.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 17px; -fx-font-weight: 700; -fx-text-fill: #0F172A;");

        Label totalFilesTitle = new Label("Total Files");
        totalFilesTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #0F172A;");

        Label totalFilesValue = new Label("3841");
        totalFilesValue.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: #0F172A;");

        Label totalFilesDescription = new Label("All uploaded files");
        totalFilesDescription.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 600; -fx-text-fill: #334155;");

        VBox totalFilesText = new VBox(2, totalFilesTitle, totalFilesValue, totalFilesDescription);
        totalFilesText.setAlignment(Pos.CENTER_LEFT);

        VBox totalFilesCard = new VBox(totalFilesText);
        totalFilesCard.setPrefWidth(140); totalFilesCard.setMinWidth(130); totalFilesCard.setMaxWidth(150);
        totalFilesCard.setPrefHeight(90); totalFilesCard.setMinHeight(85);
        totalFilesCard.setPadding(new Insets(8, 12, 8, 12));
        
        totalFilesCard.setStyle("-fx-background-color: linear-gradient(to bottom right, #F3E8FF, " + TOTAL_FILES_BG + "); " +
                                "-fx-border-color: " + TOTAL_FILES_BORDER + "; -fx-border-width: 1; -fx-border-radius: 12; -fx-background-radius: 12; " +
                                "-fx-effect: dropshadow(two-pass-box, rgba(192, 132, 252, 0.4), 8, 0, 0, 3);");

        Region titleSpacer = new Region();
        HBox.setHgrow(titleSpacer, Priority.ALWAYS);

        HBox headerRow = new HBox(16, title, titleSpacer, totalFilesCard);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        headerRow.setMaxWidth(Double.MAX_VALUE);

        PieChart pieChart = new PieChart();
        PieChart.Data pdf = new PieChart.Data("PDF", 36);
        PieChart.Data images = new PieChart.Data("Images", 27);
        PieChart.Data documents = new PieChart.Data("Documents", 21);
        PieChart.Data videos = new PieChart.Data("Videos", 9);
        PieChart.Data others = new PieChart.Data("Others", 5);

        pieChart.getData().addAll(pdf, images, documents, videos, others);
        pieChart.setLegendVisible(false);
        pieChart.setLabelsVisible(false);
        pieChart.setStartAngle(90);
        pieChart.setPrefSize(170, 170); pieChart.setMinSize(170, 170); pieChart.setMaxSize(170, 170);
        pieChart.setStyle("-fx-background-color: transparent;");

        pdf.getNode().setStyle("-fx-pie-color: " + PDF_COLOR + ";");
        images.getNode().setStyle("-fx-pie-color: " + IMAGE_COLOR + ";");
        documents.getNode().setStyle("-fx-pie-color: " + DOCUMENT_COLOR + ";");
        videos.getNode().setStyle("-fx-pie-color: " + VIDEO_COLOR + ";");
        others.getNode().setStyle("-fx-pie-color: " + OTHER_COLOR + ";");

        Circle donutCenter = new Circle(42);
        donutCenter.setFill(Color.web(WHITE));

        StackPane donut = new StackPane(pieChart, donutCenter);
        donut.setPrefSize(170, 170); donut.setMinSize(170, 170); donut.setMaxSize(170, 170);

        VBox legend = new VBox(10,
                createLegendRow("PDF", "36%", 0.36, PDF_COLOR),
                createLegendRow("Images", "27%", 0.27, IMAGE_COLOR),
                createLegendRow("Documents", "21%", 0.21, DOCUMENT_COLOR),
                createLegendRow("Videos", "9%", 0.09, VIDEO_COLOR),
                createLegendRow("Others", "5%", 0.05, OTHER_COLOR)
        );
        legend.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(legend, Priority.ALWAYS);

        HBox middle = new HBox(28, donut, legend);
        middle.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(16, headerRow, middle);
        card.setMaxWidth(820);
        card.setPadding(new Insets(24));
        
        card.setStyle("-fx-background-color: linear-gradient(to bottom right, #FFFFFF, " + CARD_BG + "); " +
                      "-fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1; -fx-border-radius: 16; -fx-background-radius: 16; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 20, 0, 0, 8);");

        return card;
    }

    private HBox createLegendRow(String name, String percentageText, double progress, String hexColor) {
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: #0F172A;");
        nameLabel.setPrefWidth(85);

        StackPane progressBackground = new StackPane();
        progressBackground.setPrefHeight(8); progressBackground.setMinHeight(8); progressBackground.setMaxHeight(8);
        progressBackground.setPrefWidth(110); progressBackground.setMinWidth(110); progressBackground.setMaxWidth(110);
        progressBackground.setStyle("-fx-background-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6;");

        Region progressFill = new Region();
        progressFill.setPrefHeight(8); progressFill.setMaxHeight(8);
        progressFill.setStyle("-fx-background-color: " + hexColor + "; -fx-background-radius: 6;");
        // Dynamically binds width based on specific percentage progress value
        progressFill.prefWidthProperty().bind(progressBackground.widthProperty().multiply(progress));
        StackPane.setAlignment(progressFill, Pos.CENTER_LEFT);

        progressBackground.getChildren().add(progressFill);

        Label percentageLabel = new Label(percentageText);
        percentageLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #334155;");
        percentageLabel.setPrefWidth(45);
        percentageLabel.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(12, nameLabel, progressBackground, percentageLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);
        return row;
    }

    private VBox createMostUsedCategories() {
        Label title = new Label("Most Used Categories");
        title.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 17px; -fx-font-weight: 700; -fx-text-fill: #0F172A;");

        VBox categories = new VBox(16,
                createCategoryRow("College", 1245, "32%", 0.32),
                createCategoryRow("Personal", 987, "25%", 0.25),
                createCategoryRow("Office", 832, "21%", 0.21),
                createCategoryRow("Finance", 421, "11%", 0.11),
                createCategoryRow("Family", 356, "9%", 0.09)
        );

        VBox card = new VBox(22, title, categories);
        card.setMaxWidth(820);
        card.setPadding(new Insets(24));
        
        card.setStyle("-fx-background-color: linear-gradient(to bottom right, #FFFFFF, " + CARD_BG + "); " +
                      "-fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1; -fx-border-radius: 16; -fx-background-radius: 16; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 20, 0, 0, 8);");

        return card;
    }

    private HBox createCategoryRow(String category, int count, String percentageText, double progress) {
        Label categoryLabel = new Label(category);
        categoryLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #0F172A;");
        categoryLabel.setPrefWidth(90);

        StackPane progressBackground = new StackPane();
        progressBackground.setPrefHeight(8); progressBackground.setMinHeight(8); progressBackground.setMaxHeight(8);
        progressBackground.setMaxWidth(Double.MAX_VALUE);
        progressBackground.setStyle("-fx-background-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6;");

        Region progressFill = new Region();
        progressFill.setPrefHeight(8); progressFill.setMaxHeight(8);
        progressFill.setStyle("-fx-background-color: linear-gradient(to right, #60A5FA, " + BLUE + "); -fx-background-radius: 6; -fx-effect: dropshadow(two-pass-box, rgba(37,99,235,0.4), 4, 0, 0, 1);");
        progressFill.prefWidthProperty().bind(progressBackground.widthProperty().multiply(progress));
        StackPane.setAlignment(progressFill, Pos.CENTER_LEFT);

        progressBackground.getChildren().add(progressFill);
        HBox.setHgrow(progressBackground, Priority.ALWAYS);

        Label countLabel = new Label(count + " (" + percentageText + ")");
        countLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: #334155;");
        countLabel.setPrefWidth(100);
        countLabel.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(16, categoryLabel, progressBackground, countLabel);
        row.setAlignment(Pos.CENTER_LEFT);
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