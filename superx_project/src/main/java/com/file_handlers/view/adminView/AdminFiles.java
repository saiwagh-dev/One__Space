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
import com.file_handlers.Main;
import com.file_handlers.view.LandingPage;

public class AdminFiles {
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
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createFilesContent());
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
        sidebar.setPrefWidth(230); sidebar.setMinWidth(230); sidebar.setMaxWidth(230);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        logoText.setTextFill(Color.web(WHITE));

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
        Button files = createSidebarButton("files", "Files", true);
        files.setOnAction(e -> LandingPage.showAdminFiles());
        Button storage = createSidebarButton("storage", "Storage", false);
        Button aiSystem = createSidebarButton("ai", "AI System", false);
        aiSystem.setOnAction(e -> LandingPage.showAdminAISystem());

        Button analytics = createSidebarButton("analytics", "Analytics", false);
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
        logout.setOnAction(event -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(logoSection, navigation, spacer, settings, divider, logout);
        return sidebar;
    }

    private StackPane createLogo() {
        URL logoURL = getClass().getResource("/images/onespace-logo.png");
        if (logoURL != null) {
            ImageView imageView = new ImageView(new Image(logoURL.toExternalForm()));
            imageView.setFitWidth(42); imageView.setFitHeight(42); imageView.setPreserveRatio(true);
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
        searchBox.setPrefHeight(38); searchBox.setMaxWidth(500);
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
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 24, 16, 24));
        topBar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private VBox createFilesContent() {
        Label title = new Label("Files");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: 700;");

        Label subtitle = new Label("Manage and monitor all files stored in OneSpace.");
        subtitle.setFont(Font.font(FONT, FontWeight.NORMAL, 16));
        subtitle.setTextFill(Color.WHITE);
        subtitle.setStyle("-fx-text-fill: #FFFFFF;");

        VBox heading = new VBox(7, title, subtitle);

        GridPane grid = new GridPane();
        grid.setHgap(22); grid.setVgap(22);

        ColumnConstraints firstColumn = new ColumnConstraints();
        firstColumn.setPercentWidth(50); firstColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints secondColumn = new ColumnConstraints();
        secondColumn.setPercentWidth(50); secondColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(firstColumn, secondColumn);

        grid.add(createFileTypesOverview(), 0, 0);
        grid.add(createMostUsedCategories(), 1, 0);

        VBox content = new VBox(25, heading, grid);
        content.setPadding(new Insets(42, 48, 45, 48));
        content.setFillWidth(true);
        content.setStyle("-fx-background-color: " + MAIN_BG + ";");
        return content;
    }

    private VBox createFileTypesOverview() {
        Label title = new Label("File Types Overview");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 21));
        title.setTextFill(Color.web(BLACK));
        title.setStyle("-fx-text-fill: #000000; -fx-font-weight: 700;");

        Label totalFilesTitle = new Label("Total Files");
        totalFilesTitle.setFont(Font.font(FONT, FontWeight.NORMAL, 16));
        totalFilesTitle.setTextFill(Color.web(BLACK));
        totalFilesTitle.setStyle("-fx-text-fill: #000000;");

        Label totalFilesValue = new Label("3841");
        totalFilesValue.setFont(Font.font(FONT, FontWeight.BOLD, 36));
        totalFilesValue.setTextFill(Color.web(BLACK));
        totalFilesValue.setStyle("-fx-text-fill: #000000; -fx-font-weight: 700;");

        Label totalFilesDescription = new Label("All uploaded files");
        totalFilesDescription.setFont(Font.font(FONT, FontWeight.NORMAL, 14));
        totalFilesDescription.setTextFill(Color.web(BLACK));
        totalFilesDescription.setStyle("-fx-text-fill: #000000;");

        VBox totalFilesText = new VBox(18, totalFilesTitle, totalFilesValue, totalFilesDescription);
        totalFilesText.setAlignment(Pos.CENTER_LEFT);

        VBox totalFilesCard = new VBox(totalFilesText);
        totalFilesCard.setPrefWidth(183); totalFilesCard.setMinWidth(183); totalFilesCard.setMaxWidth(183);
        totalFilesCard.setPrefHeight(225); totalFilesCard.setMinHeight(225);
        totalFilesCard.setPadding(new Insets(28, 24, 28, 24));
        totalFilesCard.setStyle("-fx-background-color: " + TOTAL_FILES_BG + "; -fx-border-color: " + TOTAL_FILES_BORDER + "; -fx-border-width: 1; -fx-border-radius: 18; -fx-background-radius: 18;");

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
        pieChart.setPrefSize(160, 160); pieChart.setMinSize(160, 160); pieChart.setMaxSize(160, 160);
        pieChart.setStyle("-fx-background-color: transparent;");

        pdf.getNode().setStyle("-fx-pie-color: " + PDF_COLOR + ";");
        images.getNode().setStyle("-fx-pie-color: " + IMAGE_COLOR + ";");
        documents.getNode().setStyle("-fx-pie-color: " + DOCUMENT_COLOR + ";");
        videos.getNode().setStyle("-fx-pie-color: " + VIDEO_COLOR + ";");
        others.getNode().setStyle("-fx-pie-color: " + OTHER_COLOR + ";");

        Circle donutCenter = new Circle(42);
        donutCenter.setFill(Color.web(CARD_BG));

        StackPane donut = new StackPane(pieChart, donutCenter);
        donut.setPrefSize(170, 170); donut.setMinSize(170, 170); donut.setMaxSize(170, 170);

        VBox legend = new VBox(15,
                createLegendRow("PDF", "36%"),
                createLegendRow("Images", "27%"),
                createLegendRow("Documents", "21%"),
                createLegendRow("Videos", "9%"),
                createLegendRow("Others", "5%")
        );

        HBox chartArea = new HBox(18, donut, legend);
        chartArea.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(legend, Priority.ALWAYS);

        HBox middle = new HBox(28, totalFilesCard, chartArea);
        middle.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(22, title, middle);
        card.setPrefHeight(333); card.setMinHeight(333);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(30, 28, 25, 28));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1; -fx-border-radius: 18; -fx-background-radius: 18; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 7, 0, 0, 2);");
        return card;
    }

    private HBox createLegendRow(String name, String percentage) {
        Circle bullet = new Circle(4, Color.BLACK);
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font(FONT, FontWeight.NORMAL, 15));
        nameLabel.setTextFill(Color.BLACK);
        nameLabel.setStyle("-fx-text-fill: #000000;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label percentageLabel = new Label(percentage);
        percentageLabel.setFont(Font.font(FONT, FontWeight.NORMAL, 14));
        percentageLabel.setTextFill(Color.BLACK);
        percentageLabel.setStyle("-fx-text-fill: #000000;");

        HBox row = new HBox(12, bullet, nameLabel, spacer, percentageLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefWidth(210);
        return row;
    }

    private VBox createMostUsedCategories() {
        Label title = new Label("Most Used Categories");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 21));
        title.setTextFill(Color.BLACK);
        title.setStyle("-fx-text-fill: #000000; -fx-font-weight: 700;");

        VBox categories = new VBox(17,
                createCategoryRow("College", 1245, "32%", 0.32),
                createCategoryRow("Personal", 987, "25%", 0.25),
                createCategoryRow("Office", 832, "21%", 0.21),
                createCategoryRow("Finance", 421, "11%", 0.11),
                createCategoryRow("Family", 356, "9%", 0.09)
        );

        VBox card = new VBox(22, title, categories);
        card.setPrefHeight(333); card.setMinHeight(333);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(30, 28, 25, 28));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1; -fx-border-radius: 18; -fx-background-radius: 18; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 7, 0, 0, 2);");
        return card;
    }

    private HBox createCategoryRow(String category, int count, String percentage, double progress) {
        Label categoryLabel = new Label(category);
        categoryLabel.setFont(Font.font(FONT, FontWeight.NORMAL, 16));
        categoryLabel.setTextFill(Color.BLACK);
        categoryLabel.setStyle("-fx-text-fill: #000000;");
        categoryLabel.setPrefWidth(128);

        StackPane progressBackground = new StackPane();
        progressBackground.setPrefHeight(8); progressBackground.setMinHeight(8); progressBackground.setMaxHeight(8);
        progressBackground.setMaxWidth(Double.MAX_VALUE);
        progressBackground.setStyle("-fx-background-color: #CBD5E1; -fx-border-color: #000000; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6;");

        Region progressFill = new Region();
        progressFill.setPrefHeight(6); progressFill.setMaxHeight(6);
        progressFill.setStyle("-fx-background-color: #64748B; -fx-background-radius: 5;");
        progressFill.prefWidthProperty().bind(progressBackground.widthProperty().multiply(progress).subtract(2));
        StackPane.setAlignment(progressFill, Pos.CENTER_LEFT);

        progressBackground.getChildren().add(progressFill);
        HBox.setHgrow(progressBackground, Priority.ALWAYS);

        Label countLabel = new Label(count + " (" + percentage + ")");
        countLabel.setFont(Font.font(FONT, FontWeight.NORMAL, 14));
        countLabel.setTextFill(Color.BLACK);
        countLabel.setStyle("-fx-text-fill: #000000;");
        countLabel.setPrefWidth(130);
        countLabel.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(10, categoryLabel, progressBackground, countLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefHeight(28);
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
            case "storage": icon.setContent("M4 5 H20 L21 8 H3 Z M4 8 V20 H20 V8 M7 12 H17 M7 16 H17"); break;
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