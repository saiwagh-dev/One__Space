package com.file_handlers.view.adminView;

import com.file_handlers.model.UserSession;
import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.view.LandingPage;
import com.file_handlers.dao.AdminFileStatsDAO;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.concurrent.Task;
import javafx.stage.Popup;

public class AdminFiles {
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    
    // 1. Sidebar & Top Bar Tones
    private static final String SIDEBAR_BG = "#070C16";
    public static final String SIDEBAR_DARK = "#070C16";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";
    
    // 2. Center Canvas Radial Glow Background
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";
    
    // 3. Main Glassmorphic Cards & Text Colors
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";
    private static final String CARD_TITLE = "#FFFFFF";
    private static final String CARD_VALUE = "#FFFFFF";
    private static final String CARD_SECONDARY = "#94A3B8";
    
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    
    // Chart and Accent Colors
    private static final String PDF_COLOR = "#3B82F6";
    private static final String IMAGE_COLOR = "#10B981";
    private static final String DOCUMENT_COLOR = "#F59E0B";
    private static final String VIDEO_COLOR = "#8B5CF6";
    private static final String OTHER_COLOR = "#00D2FF";
    private static final String TOTAL_FILES_BG = "rgba(139, 92, 246, 0.15)";
    private static final String TOTAL_FILES_BORDER = "rgba(139, 92, 246, 0.4)";
    
    private String activeUserName = "Admin";
    private String initials = "A";

    public AdminFiles() {
        UserSession session = UserSession.getInstance();

        if (session != null && session.getDisplayName() != null) {
            String fullName = session.getDisplayName().trim();
            if (!fullName.isEmpty()) {
                String[] parts = fullName.split("\\s+");
                this.activeUserName = parts[0];
                this.initials = this.activeUserName.substring(0, 1).toUpperCase();
            }
        }}
    private final AdminFileStatsDAO statsDAO = new AdminFileStatsDAO();

    private Label totalFilesValue;
    private final Map<String, PieChart.Data> typeSlices = new LinkedHashMap<>();
    private final Map<String, Label> typePercentLabels = new LinkedHashMap<>();
    private final Map<String, Region> typeProgressFills = new LinkedHashMap<>();
    private final Map<String, StackPane> typeProgressBackgrounds = new LinkedHashMap<>();

    private final Map<String, Label> categoryCountLabels = new LinkedHashMap<>();
    private final Map<String, Region> categoryProgressFills = new LinkedHashMap<>();
    private final Map<String, StackPane> categoryProgressBackgrounds = new LinkedHashMap<>();

   

    public Scene getAdminFilesScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createFilesContent());
        loadStatsAsync();
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
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH); 
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH); 
        sidebar.setMaxWidth(ResponsiveUtil.SIDEBAR_WIDTH);
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
        Button collab = createSidebarButton("collaboration", "Collaboration", false);
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
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notification = new Button();
        notification.setGraphic(bell);
        notification.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        notification.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-text-fill: #FFFFFF; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        Label admin = new Label(activeUserName);
        admin.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        admin.setTextFill(Color.WHITE);
        admin.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #FFFFFF;");

        HBox profile = new HBox(10, avatar, admin);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(4, 12, 4, 6));
        profile.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");

        Popup profilePopup = createProfilePopup();
        profile.setOnMouseClicked(e -> {
            if (profilePopup.isShowing()) {
                profilePopup.hide();
            } else {
                javafx.geometry.Point2D p = profile.localToScreen(0.0, profile.getHeight());
                profilePopup.show(profile, p.getX() - 30, p.getY() + 8);
            }
        });

        HBox topBar = new HBox(16, spacer, notification, profile);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private Popup createProfilePopup() {
        Popup popup = new Popup();
        popup.setAutoHide(true);

        HBox profileBtn = createProfilePopupItem("users", "Profile Page", "#F59E0B", () -> {
            popup.hide();
            LandingPage.showAdminProfilePage();
        });

        HBox settingsBtn = createProfilePopupItem("settings", "Settings", "#38BDF8", () -> {
            popup.hide();
            LandingPage.showAdminSettings();
        });

        HBox signOutBtn = createProfilePopupItem("logout", "Sign Out", "#F87171", () -> {
            popup.hide();
            LandingPage.showAdminLoginPage();
        });

        Region menuDivider = new Region();
        menuDivider.setPrefHeight(1);
        menuDivider.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08);");

        VBox menuBox = new VBox(6, profileBtn, settingsBtn, menuDivider, signOutBtn);
        menuBox.setPrefWidth(170);
        menuBox.setPadding(new Insets(10, 8, 10, 8));
        menuBox.setStyle(
            "-fx-background-color: #0B132B;" +
            "-fx-border-color: rgba(255, 255, 255, 0.12);" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 14;" +
            "-fx-background-radius: 14;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 24, 0, 0, 10);"
        );

        popup.getContent().add(menuBox);
        return popup;
    }

    private HBox createProfilePopupItem(String iconType, String text, String iconColor, Runnable action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(2.0);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(22, 22);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        label.setTextFill(Color.WHITE);

        HBox item = new HBox(12, iconBox, label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(8, 10, 8, 10));
        item.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        item.setOnMouseClicked(e -> action.run());
        return item;
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

        // Vertically stacked cards centered with balanced compact dimensions
        VBox fileTypesCard = createFileTypesOverview();
        VBox categoriesCard = createMostUsedCategories();

        VBox cardsContainer = new VBox(22, fileTypesCard, categoriesCard);
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.setMaxWidth(880);
        cardsContainer.setFillWidth(true);

        HBox centeringRow = new HBox(cardsContainer);
        centeringRow.setAlignment(Pos.CENTER);
        centeringRow.setFillHeight(false);
        HBox.setHgrow(cardsContainer, Priority.ALWAYS);

        VBox content = new VBox(22, heading, centeringRow);
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 32, ResponsiveUtil.PAGE_PADDING));
        content.setFillWidth(true);
        content.setStyle("-fx-background-color: transparent;");
        return content;
    }

    private VBox createFileTypesOverview() {

        Label title = new Label("File Types Overview");
        title.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: " + CARD_TITLE + ";");

        Label totalFilesTitle = new Label("Total Files");
        totalFilesTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #A78BFA;");

        totalFilesValue = new Label("Loading...");
        totalFilesValue.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: " + CARD_VALUE + ";");

        Label totalFilesDescription = new Label("All uploaded files");
        totalFilesDescription.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 600; -fx-text-fill: " + CARD_SECONDARY + ";");

        VBox totalFilesText = new VBox(2, totalFilesTitle, totalFilesValue, totalFilesDescription);
        totalFilesText.setAlignment(Pos.CENTER_LEFT);

        VBox totalFilesCard = new VBox(totalFilesText);
        totalFilesCard.setPrefWidth(135); totalFilesCard.setMinWidth(125); totalFilesCard.setMaxWidth(145);
        totalFilesCard.setPrefHeight(76); totalFilesCard.setMinHeight(72);
        totalFilesCard.setPadding(new Insets(8, 12, 8, 12));
        totalFilesCard.setStyle("-fx-background-color: " + TOTAL_FILES_BG + "; " +
                "-fx-border-color: " + TOTAL_FILES_BORDER + "; -fx-border-width: 1; -fx-border-radius: 12; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(two-pass-box, rgba(139, 92, 246, 0.3), 8, 0, 0, 3);");

        Region titleSpacer = new Region();
        HBox.setHgrow(titleSpacer, Priority.ALWAYS);

        HBox headerRow = new HBox(14, title, titleSpacer, totalFilesCard);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        headerRow.setMaxWidth(Double.MAX_VALUE);

        PieChart pieChart = new PieChart();
        createTypeSlice(pieChart, "PDF", PDF_COLOR);
        createTypeSlice(pieChart, "Images", IMAGE_COLOR);
        createTypeSlice(pieChart, "Documents", DOCUMENT_COLOR);
        createTypeSlice(pieChart, "Videos", VIDEO_COLOR);
        createTypeSlice(pieChart, "Others", OTHER_COLOR);

        pieChart.setLegendVisible(false);
        pieChart.setLabelsVisible(false);
        pieChart.setStartAngle(90);
        pieChart.setPrefSize(155, 155); pieChart.setMinSize(155, 155); pieChart.setMaxSize(155, 155);
        pieChart.setStyle("-fx-background-color: transparent;");

        Circle donutCenter = new Circle(38);
        donutCenter.setFill(Color.web("#0A1424"));

        StackPane donut = new StackPane(pieChart, donutCenter);
        donut.setPrefSize(155, 155); donut.setMinSize(155, 155); donut.setMaxSize(155, 155);

        VBox legend = new VBox(10,
                createLegendRow("PDF", PDF_COLOR),
                createLegendRow("Images", IMAGE_COLOR),
                createLegendRow("Documents", DOCUMENT_COLOR),
                createLegendRow("Videos", VIDEO_COLOR),
                createLegendRow("Others", OTHER_COLOR)
        );
        legend.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(legend, Priority.ALWAYS);

        HBox middle = new HBox(28, donut, legend);
        middle.setAlignment(Pos.CENTER_LEFT);
        middle.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(16, headerRow, middle);
        card.setMaxWidth(820);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: " + CARD_BG + "; " +
                "-fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");
        card.setMaxWidth(880);
        card.setFillWidth(true);
        card.setPadding(new Insets(20, 24, 20, 24));
        
        card.setStyle("-fx-background-color: " + CARD_BG + "; " +
                      "-fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 18; -fx-background-radius: 18; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.55), 20, 0, 0, 8);");

        return card;
    }

    private void createTypeSlice(PieChart chart, String name, String color) {
        PieChart.Data data = new PieChart.Data(name, 0);
        typeSlices.put(name, data);
        chart.getData().add(data);

        data.nodeProperty().addListener((obs, oldNode, node) -> {
            if (node != null)
                node.setStyle("-fx-pie-color: " + color + ";");
        });
    }

    private HBox createLegendRow(String name, String hexColor) {

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + CARD_TITLE + ";");
        nameLabel.setPrefWidth(90);

        StackPane progressBackground = new StackPane();
        progressBackground.setPrefHeight(8); progressBackground.setMinHeight(8); progressBackground.setMaxHeight(8);
        progressBackground.setMaxWidth(Double.MAX_VALUE);
        progressBackground.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 6; -fx-background-radius: 6;");

        Region progressFill = new Region();
        progressFill.setPrefHeight(8); progressFill.setMaxHeight(8);
        progressFill.setStyle("-fx-background-color: " + hexColor + "; -fx-background-radius: 6;");
        StackPane.setAlignment(progressFill, Pos.CENTER_LEFT);
        progressBackground.getChildren().add(progressFill);
        HBox.setHgrow(progressBackground, Priority.ALWAYS);

        Label percentageLabel = new Label("0%");
        percentageLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: " + CARD_SECONDARY + ";");
        percentageLabel.setPrefWidth(50);
        percentageLabel.setAlignment(Pos.CENTER_RIGHT);

        typePercentLabels.put(name, percentageLabel);
        typeProgressFills.put(name, progressFill);
        typeProgressBackgrounds.put(name, progressBackground);

        progressBackground.widthProperty().addListener((obs, oldWidth, newWidth) ->
                updateTypeProgress(name, currentTypeProgress(name)));

        HBox row = new HBox(14, nameLabel, progressBackground, percentageLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);
        return row;
    }

    private double currentTypeProgress(String name) {
        PieChart.Data data = typeSlices.get(name);
        double total = typeSlices.values().stream()
                .mapToDouble(PieChart.Data::getPieValue)
                .sum();
        return total <= 0 ? 0 : data.getPieValue() / total;
    }

    private void updateTypeProgress(String name, double progress) {
        StackPane background = typeProgressBackgrounds.get(name);
        Region fill = typeProgressFills.get(name);
        if (background != null && fill != null)
            fill.setPrefWidth(background.getWidth() * progress);
    }

    private VBox createMostUsedCategories() {

        Label title = new Label("Most Used Categories");
        title.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: " + CARD_TITLE + ";");

        VBox categories = new VBox(14,
                createCategoryRow("College"),
                createCategoryRow("Personal"),
                createCategoryRow("Office"),
                createCategoryRow("Finance"),
                createCategoryRow("Entertainment"),
                createCategoryRow("Other")
        );

        categories.setFillWidth(true);
        categories.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(16, title, categories);
        card.setMaxWidth(880);
        card.setFillWidth(true);
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setStyle("-fx-background-color: " + CARD_BG + "; " +
                      "-fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 18; -fx-background-radius: 18; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.55), 20, 0, 0, 8);");

        return card;
    }

    private HBox createCategoryRow(String category) {

        Label categoryLabel = new Label(category);
        categoryLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + CARD_TITLE + ";");
        categoryLabel.setPrefWidth(90);

        StackPane progressBackground = new StackPane();
        progressBackground.setPrefHeight(8);
        progressBackground.setMinHeight(8);
        progressBackground.setMaxHeight(8);
        progressBackground.setMaxWidth(Double.MAX_VALUE);
        progressBackground.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-radius: 6; -fx-background-radius: 6;"
        );

        Region progressFill = new Region();
        progressFill.setPrefHeight(8);
        progressFill.setMaxHeight(8);
        progressFill.setStyle(
                "-fx-background-color: linear-gradient(to right, #0284C7, #38BDF8);" +
                "-fx-background-radius: 6;" +
                "-fx-effect: dropshadow(two-pass-box, rgba(56,189,248,0.35), 4, 0, 0, 1);"
        );

        StackPane.setAlignment(progressFill, Pos.CENTER_LEFT);
        progressBackground.getChildren().add(progressFill);

        HBox.setHgrow(progressBackground, Priority.ALWAYS);

        String count = null;
        String percentageText = null;
        Label countLabel = new Label(count + " (" + percentageText + ")");
        countLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + CARD_SECONDARY + ";");
        countLabel.setPrefWidth(100);
        countLabel.setAlignment(Pos.CENTER_RIGHT);

        categoryCountLabels.put(category, countLabel);
        categoryProgressFills.put(category, progressFill);
        categoryProgressBackgrounds.put(category, progressBackground);

        progressBackground.widthProperty().addListener((obs, oldWidth, newWidth) ->
                updateCategoryProgress(category, currentCategoryProgress(category))
        );

        HBox row = new HBox(
                14,
                categoryLabel,
                progressBackground,
                countLabel
        );
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);

        return row;
    }

    private double currentCategoryProgress(String category) {
        int total = categoryCountLabels.values().stream()
                .mapToInt(label -> {
                    String text = label.getText();
                    try {
                        return Integer.parseInt(text.split(" ")[0]);
                    } catch (Exception e) {
                        return 0;
                    }
                }).sum();

        Label label = categoryCountLabels.get(category);
        int count = 0;
        if (label != null) {
            try {
                count = Integer.parseInt(label.getText().split(" ")[0]);
            } catch (Exception ignored) {}
        }

        return total <= 0 ? 0 : (double) count / total;
    }

    private void updateCategoryProgress(String category, double progress) {
        StackPane background = categoryProgressBackgrounds.get(category);
        Region fill = categoryProgressFills.get(category);
        if (background != null && fill != null)
            fill.setPrefWidth(background.getWidth() * progress);
    }

    // =========================================================
    // LOAD REAL FILE STATISTICS
    // =========================================================

    private void loadStatsAsync() {

        Task<FileStats> task = new Task<>() {
            @Override
            protected FileStats call() throws Exception {
                return new FileStats(
                        statsDAO.getTotalFiles(),
                        statsDAO.getFileTypeCounts(),
                        statsDAO.getCategoryCounts()
                );
            }
        };

        task.setOnSucceeded(e -> updateStats(task.getValue()));

        task.setOnFailed(e -> {
            totalFilesValue.setText("--");
            typePercentLabels.values()
                    .forEach(label -> label.setText("--"));
            categoryCountLabels.values()
                    .forEach(label -> label.setText("--"));
            System.err.println(
                    "Unable to load admin file statistics: "
                            + task.getException()
            );
        });

        Thread thread = new Thread(task, "AdminFileStatsLoader");
        thread.setDaemon(true);
        thread.start();
    }

    private void updateStats(FileStats stats) {

        totalFilesValue.setText(
                String.valueOf(stats.totalFiles)
        );

        int typeTotal = stats.fileTypes.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        for (String type : typeSlices.keySet()) {

            int count = stats.fileTypes.getOrDefault(type, 0);

            // Audio is grouped into Others to match the existing UI.
            if (type.equals("Others"))
                count += stats.fileTypes.getOrDefault("Audio", 0);

            typeSlices.get(type).setPieValue(count);

            double percentage =
                    typeTotal <= 0
                            ? 0
                            : (count * 100.0 / typeTotal);

            typePercentLabels.get(type).setText(
                    formatPercentage(percentage)
            );

            updateTypeProgress(
                    type,
                    typeTotal <= 0 ? 0 : (double) count / typeTotal
            );
        }

        int categoryTotal = stats.categories.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        for (String category : categoryCountLabels.keySet()) {

            int count =
                    stats.categories.getOrDefault(category, 0);

            double percentage =
                    categoryTotal <= 0
                            ? 0
                            : (count * 100.0 / categoryTotal);

            categoryCountLabels.get(category).setText(
                    count + " (" + formatPercentage(percentage) + ")"
            );

            updateCategoryProgress(
                    category,
                    categoryTotal <= 0
                            ? 0
                            : (double) count / categoryTotal
            );
        }
    }

    private String formatPercentage(double value) {
        return Math.round(value) + "%";
    }

    private static class FileStats {

        private final int totalFiles;
        private final Map<String, Integer> fileTypes;
        private final Map<String, Integer> categories;

        private FileStats(
                int totalFiles,
                Map<String, Integer> fileTypes,
                Map<String, Integer> categories
        ) {
            this.totalFiles = totalFiles;
            this.fileTypes = fileTypes;
            this.categories = categories;
        }
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