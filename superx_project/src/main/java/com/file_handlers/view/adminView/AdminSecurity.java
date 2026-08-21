package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.InputStream;

public class AdminSecurity {

    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    private static final String SIDEBAR_BG = "#1E2A3A";
    private static final String SIDEBAR_DARK = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";

    private static final String MAIN_BG = "#31435B";
    private static final String CARD_BG = "#DDE8F8";
    private static final String CARD_BORDER = "#C3D6EC";

    private static final String BLACK = "#000000";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";

    private static final String BLUE = "#2563EB";
    private static final String PURPLE = "#7C3AED";
    private static final String PURPLE_LIGHT = "#EDE9FE";
    private static final String GREEN = "#059669";
    private static final String RED = "#DC2626";
    private static final String ORANGE = "#D97706";

    public Scene getSecurityScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        VBox contentContainer = createSecurityContent();
        
        ScrollPane scrollPane = new ScrollPane(contentContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
                "-fx-background-color: " + MAIN_BG + ";" +
                "-fx-background: " + MAIN_BG + ";" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );

        VBox rightSide = new VBox(createTopBar(), scrollPane);
        rightSide.setStyle("-fx-background-color: " + MAIN_BG + ";");
        rightSide.setFillWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.setCenter(rightSide);

        Scene scene = new Scene(root, 1200, 750);

        String cssOverride = "data:text/css," +
                ".dark-grid-card * { -fx-text-fill: #000000 !important; -fx-fill: #000000 !important; }" +
                ".dark-grid-card .text { -fx-text-fill: #000000 !important; -fx-fill: #000000 !important; }" +
                ".axis-label, .axis .tick-label { -fx-fill: #000000 !important; -fx-text-fill: #000000 !important; }" +
                ".slate-dark-combo .list-cell { -fx-text-fill: #F8FAFC !important; -fx-font-weight: bold; -fx-background-color: #1E2A3A !important; -fx-padding: 10 14 10 14; }" +
                ".slate-dark-combo .list-cell:hover { -fx-background-color: #2563EB !important; -fx-text-fill: #FFFFFF !important; }" +
                ".slate-dark-combo .arrow { -fx-background-color: #94A3B8 !important; }" +
                ".chart-bar { -fx-background-insets: 0; }" +
                ".chart-horizontal-grid-lines { -fx-stroke: rgba(0,0,0,0.08) !important; -fx-stroke-width: 1px !important; }" +
                ".chart-vertical-grid-lines { -fx-stroke: transparent !important; -fx-stroke-width: 0px !important; }" +
                ".chart-horizontal-zero-line { -fx-stroke: transparent !important; }" +
                ".chart-vertical-zero-line { -fx-stroke: transparent !important; }" +
                ".axis { -fx-tick-mark-color: transparent; -fx-minor-tick-mark-visible: false; }" +
                ".axis:left { -fx-border-color: transparent !important; }" +
                ".axis:bottom { -fx-border-color: transparent !important; }";
        scene.getStylesheets().add(cssOverride);

        return scene;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setMaxWidth(230);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle(
                "-fx-background-color: " + SIDEBAR_BG + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 1 0 0;"
        );

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
        Button aiButton = createSidebarButton("ai", "AI System", false);
        Button analyticsButton = createSidebarButton("analytics", "Analytics", false);
        Button securityButton = createSidebarButton("security", "Security", true);

        dashboardButton.setOnAction(e -> LandingPage.showAdminDashboard());
        usersButton.setOnAction(e -> LandingPage.showAdminUsers());
        filesButton.setOnAction(e -> LandingPage.showAdminFiles());
        collabButton.setOnAction(e -> LandingPage.showAdminCollaboration());
        aiButton.setOnAction(e -> LandingPage.showAdminAISystem());
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
            StackPane logoPane = new StackPane(imageView);
            logoPane.setPrefSize(42, 42);
            logoPane.setAlignment(Pos.CENTER);
            return logoPane;
        }
        Circle circle = new Circle(21, Color.web(BLUE));
        Label fallback = new Label("O");
        fallback.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        fallback.setTextFill(Color.WHITE);
        return new StackPane(circle, fallback);
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
        button.setPrefHeight(38);
        button.setMinHeight(38);
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
        searchBox.setPrefHeight(38);
        searchBox.setMinHeight(38);
        searchBox.setMaxHeight(38);
        searchBox.setPrefWidth(420);
        searchBox.setMinWidth(420);
        searchBox.setMaxWidth(420);
        searchBox.setPadding(new Insets(0, 12, 0, 14));
        searchBox.setStyle(
                "-fx-background-color: " + SIDEBAR_DARK + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );
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
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: " + BLUE + "; -fx-background-radius: 50%;");

        Label adminName = new Label("Admin");
        adminName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        adminName.setTextFill(Color.WHITE);

        HBox profile = new HBox(10, notificationButton, avatar, adminName);
        profile.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, searchBox, spacer, profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70);
        topBar.setMinHeight(70);
        topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle(
                "-fx-background-color: " + SIDEBAR_BG + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 0 1 0;"
        );
        return topBar;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        searchField.setPrefHeight(38);
        searchField.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-prompt-text-fill: #94A3B8;" +
                "-fx-border-color: transparent;" +
                "-fx-padding: 0;"
        );
        return searchField;
    }

    private VBox createSecurityContent() {
        VBox root = new VBox(24);
        root.setFillWidth(true);
        root.setPadding(new Insets(28, 32, 40, 32));
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");

        Label title = new Label("Security Overview");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 26));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Monitor and manage the security of your OneSpace system with real-time diagnostics.");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox headerText = new VBox(6, title, subtitle);

        ComboBox<String> date = new ComboBox<>();
        date.getItems().addAll(
                "Last 7 Days",
                "Last 30 Days",
                "Last 90 Days",
                "This Year"
        );
        date.setValue("Last 30 Days");
        date.setPrefWidth(160);
        date.setPrefHeight(36);
        date.getStyleClass().add("slate-dark-combo");
        date.setStyle(
                "-fx-background-color: #1E2A3A;" +
                "-fx-border-color: #334155;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #F8FAFC;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 5, 0, 0, 2);"
        );

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox header = new HBox(headerText, headerSpacer, date);
        header.setAlignment(Pos.CENTER_LEFT);

        Region fullWidthTwoFA = createTwoFACard();
        fullWidthTwoFA.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(fullWidthTwoFA, Priority.ALWAYS);

        Region fullWidthFailedLogin = createFailedLoginCard();
        fullWidthFailedLogin.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(fullWidthFailedLogin, Priority.ALWAYS);

        Region fullWidthAlerts = createAlertsCard();
        fullWidthAlerts.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(fullWidthAlerts, Priority.ALWAYS);

        root.getChildren().addAll(header, fullWidthTwoFA, fullWidthFailedLogin, fullWidthAlerts);
        return root;
    }

    private VBox createFailedLoginCard() {
        VBox card = card();
        card.setPrefHeight(380);
        card.setMaxHeight(380);

        HBox header = cardHeader("security", "Failed Login Attempts", "Last 30 Days");
        HBox numberRow = new HBox(8, bigNumber("128"));
        HBox change = new HBox(8, badge("↑ 18.6%", "#D1FAE5", GREEN), createSmallSecondaryText("vs previous period"));

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setTickLabelFill(Color.web(BLACK));
        yAxis.setTickLabelFill(Color.web(BLACK));
        xAxis.setTickLabelFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        yAxis.setTickLabelFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));

        xAxis.setTickMarkVisible(false);
        yAxis.setTickMarkVisible(false);
        yAxis.setMinorTickVisible(false);

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(false);
        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setPrefHeight(125);
        chart.setMaxHeight(125);
        chart.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        chart.lookupAll(".chart-plot-background").forEach(n -> n.setStyle("-fx-background-color: transparent;"));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Week 1", 18));
        series.getData().add(new XYChart.Data<>("Week 2", 32));
        series.getData().add(new XYChart.Data<>("Week 3", 47));
        series.getData().add(new XYChart.Data<>("Week 4", 65));
        chart.getData().add(series);

        Platform.runLater(() -> {
            chart.applyCss();
            chart.layout();
            for (XYChart.Data<String, Number> d : series.getData()) {
                if (d.getNode() != null) {
                    d.getNode().setStyle("-fx-background-color: " + ORANGE + ", white; -fx-background-radius: 6px; -fx-padding: 4px;");
                }
            }
        });

        VBox ips = new VBox(
                6,
                ipRow("192.168.1.45", "28 attempts"),
                ipRow("203.0.113.10", "21 attempts"),
                ipRow("45.77.32.11", "17 attempts")
        );

        VBox ipsSection = new VBox(6, sectionLabel("Top IP Addresses"), ips);

        HBox chartAndIps = new HBox(16, chart, ipsSection);
        chartAndIps.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(chart, Priority.ALWAYS);
        HBox.setHgrow(ipsSection, Priority.SOMETIMES);

        card.getChildren().addAll(header, numberRow, change, chartAndIps);
        return card;
    }

    private HBox ipRow(String ip, String attempts) {
        Label ipLabel = createWrappedLabel(ip, 11, false, BLACK);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label attemptsLabel = createWrappedLabel(attempts, 11, true, ORANGE);

        HBox row = new HBox(10, new Circle(3.5, Color.web(ORANGE)), ipLabel, spacer, attemptsLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(4, 10, 4, 10));
        row.setStyle("-fx-background-color: rgba(255,255,255,0.55); -fx-background-radius: 8;");
        return row;
    }

    private VBox createAlertsCard() {
        VBox card = card();
        card.setPrefHeight(Region.USE_COMPUTED_SIZE);

        VBox alerts = new VBox(
                8,
                alert("bell", "Multiple failed login attempts", "User: aarav.verma@example.com", "10 min ago", ORANGE),
                alert("bell", "Server connection interrupted", "Storage service disconnected", "25 min ago", ORANGE),
                alert("bell", "Backup service unavailable", "Last backup failed", "1 hour ago", ORANGE),
                alert("ai", "New device logged in", "User: riya.sharma@example.com", "3 hours ago", GREEN)
        );

        Label viewAllAlertsLink = link("View All Alerts  →");
        viewAllAlertsLink.setOnMouseClicked(e -> openCreativeModalWindow("All Security Alerts", "Here is the complete detailed log of all system security alerts.", "alerts"));

        card.getChildren().addAll(
                cardHeader("bell", "Security Alerts", "View All"),
                alerts,
                separator(),
                viewAllAlertsLink
        );

        return card;
    }

    private HBox alert(String iconType, String title, String description, String time, String color) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(color));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setMinSize(28, 28);
        iconPane.setPrefSize(28, 28);
        iconPane.setMaxSize(28, 28);
        iconPane.setStyle("-fx-background-color: rgba(255,255,255,0.75); -fx-background-radius: 8;");

        Label titleLabel = createWrappedLabel(title, 12, true, BLACK);
        Label descriptionLabel = createWrappedLabel(description, 11, false, "#334155");

        VBox text = new VBox(2, titleLabel, descriptionLabel);
        HBox.setHgrow(text, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label timeLabel = createWrappedLabel(time, 11, false, "#64748B");

        HBox row = new HBox(12, iconPane, text, spacer, timeLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 12, 8, 12));
        row.setStyle("-fx-background-color: rgba(255,255,255,0.5); -fx-background-radius: 10;");
        return row;
    }

    private VBox createTwoFACard() {
        VBox card = card();
        card.setPrefHeight(250);
        card.setMaxHeight(250);

        card.getChildren().add(cardHeader("ai", "Users with 2FA Enabled", ""));

        HBox donutArea = new HBox(24);
        donutArea.setAlignment(Pos.CENTER_LEFT);
        StackPane donut = donut();
        donut.setPrefSize(120, 120);

        VBox legendArea = new VBox(
                12,
                legend(GREEN, "2FA Enabled", "342 (68.4%)"),
                legend("#94A3B8", "2FA Disabled", "158 (31.6%)")
        );
        legendArea.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(legendArea, Priority.ALWAYS);

        donutArea.getChildren().addAll(donut, legendArea);

        VBox container = new VBox(12, donutArea);
        container.setAlignment(Pos.CENTER);
        VBox.setVgrow(container, Priority.ALWAYS);

        Label manage2FALink = link("Manage 2FA Settings  →");
        manage2FALink.setOnMouseClicked(e -> openCreativeModalWindow("Manage 2FA Settings", "Configure organizational Two-Factor Authentication policies and requirements here.", "2fa"));

        card.getChildren().addAll(container, separator(), manage2FALink);
        return card;
    }

    // --- CREATIVE MODAL IMPLEMENTATION ---
    private void openCreativeModalWindow(String windowTitle, String message, String type) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.TRANSPARENT);

        // Header Titles
        Label titleLabel = new Label(windowTitle);
        titleLabel.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web(WHITE));

        Label descLabel = new Label(message);
        descLabel.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        descLabel.setTextFill(Color.web(LIGHT_SECONDARY));
        descLabel.setWrapText(true);

        VBox headerText = new VBox(4, titleLabel, descLabel);
        HBox.setHgrow(headerText, Priority.ALWAYS);

        // Custom Close Button
        SVGPath closeIcon = new SVGPath();
        closeIcon.setContent("M18 6 L6 18 M6 6 L18 18");
        closeIcon.setStroke(Color.web(LIGHT_SECONDARY));
        closeIcon.setStrokeWidth(2);
        
        StackPane closeBtnPane = new StackPane(closeIcon);
        closeBtnPane.setPrefSize(34, 34);
        closeBtnPane.setStyle("-fx-background-radius: 8; -fx-cursor: hand; -fx-background-color: transparent;");
        closeBtnPane.setOnMouseEntered(e -> {
            closeBtnPane.setStyle("-fx-background-color: #334155; -fx-background-radius: 8; -fx-cursor: hand;");
            closeIcon.setStroke(Color.WHITE);
        });
        closeBtnPane.setOnMouseExited(e -> {
            closeBtnPane.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;");
            closeIcon.setStroke(Color.web(LIGHT_SECONDARY));
        });
        
        // Header Row
        HBox headerRow = new HBox(headerText, closeBtnPane);
        headerRow.setAlignment(Pos.TOP_LEFT);

        // Main Content Area based on Type
        Node contentNode;
        if ("alerts".equals(type)) {
            contentNode = createAlertsTable();
        } else {
            contentNode = create2FASettingsLayout();
        }

        // Action Button Row
        Button actionBtn = new Button("Done & Close");
        actionBtn.setStyle("-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 24; -fx-font-size: 13px; -fx-cursor: hand;");
        actionBtn.setOnMouseEntered(e -> actionBtn.setStyle("-fx-background-color: #1D4ED8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 24; -fx-font-size: 13px; -fx-cursor: hand;"));
        actionBtn.setOnMouseExited(e -> actionBtn.setStyle("-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 24; -fx-font-size: 13px; -fx-cursor: hand;"));
        
        // Handle Close Events
        closeBtnPane.setOnMouseClicked(e -> closeModalWithAnimation(modalStage, headerRow.getParent()));
        actionBtn.setOnAction(e -> closeModalWithAnimation(modalStage, headerRow.getParent()));

        HBox bottomRow = new HBox(actionBtn);
        bottomRow.setAlignment(Pos.CENTER_RIGHT);
        bottomRow.setPadding(new Insets(10, 0, 0, 0));

        // Assemble Root Box
        VBox rootBox = new VBox(24, headerRow, contentNode, bottomRow);
        rootBox.setPadding(new Insets(32));
        rootBox.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-background-radius: 16; -fx-border-radius: 16; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 1;");
        
        // Draggable Logic
        final double[] xOffset = {0};
        final double[] yOffset = {0};
        rootBox.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });
        rootBox.setOnMouseDragged(event -> {
            modalStage.setX(event.getScreenX() - xOffset[0]);
            modalStage.setY(event.getScreenY() - yOffset[0]);
        });

        // Drop Shadow Effect
        rootBox.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0,0,0,0.5), 25, 0, 0, 10));

        // Wrapper to give space for drop shadow
        StackPane wrapper = new StackPane(rootBox);
        wrapper.setPadding(new Insets(30));
        wrapper.setStyle("-fx-background-color: transparent;");

        Scene modalScene = new Scene(wrapper, 650, 520);
        modalScene.setFill(Color.TRANSPARENT);
        modalScene.getStylesheets().add(createModalCss());

        modalStage.setScene(modalScene);
        
        // Entry Animations
        rootBox.setOpacity(0);
        rootBox.setTranslateY(30);
        modalStage.show();

        FadeTransition ft = new FadeTransition(Duration.millis(300), rootBox);
        ft.setToValue(1.0);
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), rootBox);
        tt.setToY(0);
        
        ParallelTransition pt = new ParallelTransition(ft, tt);
        pt.play();
    }

    private void closeModalWithAnimation(Stage stage, Node rootBox) {
        FadeTransition ft = new FadeTransition(Duration.millis(200), rootBox);
        ft.setToValue(0.0);
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), rootBox);
        tt.setToY(20);
        
        ParallelTransition pt = new ParallelTransition(ft, tt);
        pt.setOnFinished(e -> stage.close());
        pt.play();
    }

    private TableView<AlertItem> createAlertsTable() {
        TableView<AlertItem> table = new TableView<>();
        table.setPrefHeight(260);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AlertItem, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTime()));
        timeCol.setMaxWidth(100);
        timeCol.setMinWidth(100);

        TableColumn<AlertItem, String> typeCol = new TableColumn<>("Alert Type");
        typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        
        TableColumn<AlertItem, String> descCol = new TableColumn<>("Details");
        descCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));

        table.getColumns().addAll(timeCol, typeCol, descCol);
        table.getItems().addAll(
                new AlertItem("10 min ago", "Multiple failed login attempts", "User: aarav.verma@example.com (IP: 192.168.1.45)"),
                new AlertItem("25 min ago", "Server connection interrupted", "Storage service disconnected unexpectedly"),
                new AlertItem("1 hour ago", "Backup service unavailable", "Scheduled night backup failed to execute"),
                new AlertItem("3 hours ago", "New device logged in", "User: riya.sharma@example.com from Chrome/Windows"),
                new AlertItem("5 hours ago", "High CPU Load", "System utilization peaked above 90% threshold"),
                new AlertItem("1 day ago", "Unauthorized API Request", "Blocked request with invalid token signature")
        );
        return table;
    }

    private VBox create2FASettingsLayout() {
        VBox container = new VBox(14);
        container.getChildren().addAll(
            createToggleSetting("Enforce 2FA for all Administrators", "Require two-factor authentication for accounts with admin privileges.", true),
            createToggleSetting("Enforce 2FA for all Users", "Mandate 2FA for every regular user logging into OneSpace.", false),
            createToggleSetting("Allow SMS Recovery", "Permit users to recover their accounts via SMS codes if authenticator is lost.", true),
            createToggleSetting("Remember Devices", "Allow users to skip 2FA for 30 days on trusted network devices.", true)
        );
        return container;
    }

    private HBox createToggleSetting(String title, String desc, boolean defaultState) {
        Label tLabel = new Label(title);
        tLabel.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        tLabel.setTextFill(Color.web(WHITE));
        
        Label dLabel = new Label(desc);
        dLabel.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        dLabel.setTextFill(Color.web(LIGHT_SECONDARY));
        dLabel.setWrapText(true);
        
        VBox text = new VBox(4, tLabel, dLabel);
        HBox.setHgrow(text, Priority.ALWAYS);
        
        ToggleButton toggle = new ToggleButton(defaultState ? "ON" : "OFF");
        toggle.setSelected(defaultState);
        toggle.setPrefWidth(60);
        toggle.setStyle(defaultState ? 
            "-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 6 12; -fx-font-weight: bold; -fx-cursor: hand;" :
            "-fx-background-color: #334155; -fx-text-fill: #94A3B8; -fx-background-radius: 12; -fx-padding: 6 12; -fx-font-weight: bold; -fx-cursor: hand;"
        );
        
        toggle.setOnAction(e -> {
            if (toggle.isSelected()) {
                toggle.setText("ON");
                toggle.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 6 12; -fx-font-weight: bold; -fx-cursor: hand;");
            } else {
                toggle.setText("OFF");
                toggle.setStyle("-fx-background-color: #334155; -fx-text-fill: #94A3B8; -fx-background-radius: 12; -fx-padding: 6 12; -fx-font-weight: bold; -fx-cursor: hand;");
            }
        });
        
        HBox row = new HBox(16, text, toggle);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16));
        row.setStyle("-fx-background-color: #141D29; -fx-background-radius: 12; -fx-border-color: #2D3D52; -fx-border-radius: 12;");
        return row;
    }

    private String createModalCss() {
        return "data:text/css," +
               ".table-view { -fx-background-color: transparent; -fx-padding: 0; }" +
               ".table-view .column-header-background { -fx-background-color: #141D29; -fx-background-radius: 8 8 0 0; }" +
               ".table-view .column-header { -fx-background-color: transparent; -fx-size: 40; }" +
               ".table-view .column-header .label { -fx-text-fill: #94A3B8; -fx-font-weight: bold; -fx-font-size: 13px; }" +
               ".table-view .table-row-cell { -fx-background-color: #1E2A3A; -fx-border-color: #2D3D52; -fx-border-width: 0 0 1 0; }" +
               ".table-view .table-row-cell:odd { -fx-background-color: #1A2433; }" +
               ".table-view .table-row-cell:hover { -fx-background-color: #2563EB; }" +
               ".table-view .table-cell { -fx-text-fill: #F8FAFC; -fx-padding: 10 12; -fx-font-size: 13px; -fx-border-width: 0; }" +
               ".table-view .virtual-flow .scroll-bar:vertical, " +
               ".table-view .virtual-flow .scroll-bar:horizontal { -fx-opacity: 0; -fx-padding: 0; -fx-pref-width: 0; -fx-pref-height: 0; }";
    }

    public static class AlertItem {
        private final String time;
        private final String title;
        private final String description;

        public AlertItem(String time, String title, String description) {
            this.time = time;
            this.title = title;
            this.description = description;
        }

        public String getTime() { return time; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
    }
    // --- END CREATIVE MODAL ---

    private StackPane donut() {
        Arc backgroundArc = new Arc(0, 0, 52, 52, 0, 360);
        backgroundArc.setType(ArcType.OPEN);
        backgroundArc.setFill(Color.TRANSPARENT);
        backgroundArc.setStroke(Color.web("#CBD5E1"));
        backgroundArc.setStrokeWidth(12);

        Arc enabledArc = new Arc(0, 0, 52, 52, 90, -246);
        enabledArc.setType(ArcType.OPEN);
        enabledArc.setFill(Color.TRANSPARENT);
        enabledArc.setStroke(Color.web(GREEN));
        enabledArc.setStrokeWidth(12);

        Label number = createWrappedLabel("342", 20, true, BLACK);
        Label total = createWrappedLabel("Total Users", 10, false, "#475569");

        VBox center = new VBox(2, number, total);
        center.setAlignment(Pos.CENTER);

        StackPane pane = new StackPane(backgroundArc, enabledArc, center);
        pane.setPrefSize(120, 120);
        return pane;
    }

    private VBox legend(String color, String title, String value) {
        Label titleLabel = createWrappedLabel(title, 11, false, "#334155");
        Label valueLabel = createWrappedLabel(value, 13, true, BLACK);

        HBox titleRow = new HBox(8, new Circle(4, Color.web(color)), titleLabel);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(4, titleRow, valueLabel);
        box.setPadding(new Insets(8, 12, 8, 12));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.55); -fx-background-radius: 8;");
        return box;
    }

    private VBox card() {
        VBox box = new VBox(14);
        box.setFillWidth(true);
        box.setPadding(new Insets(20));
        box.getStyleClass().add("dark-grid-card");
        box.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 18;" +
                "-fx-background-radius: 18;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 14, 0, 0, 5);"
        );
        return box;
    }

    private HBox cardHeader(String iconType, String title, String right) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(PURPLE));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setMinSize(32, 32);
        iconBox.setPrefSize(32, 32);
        iconBox.setMaxSize(32, 32);
        iconBox.setStyle("-fx-background-color: " + PURPLE_LIGHT + "; -fx-background-radius: 8;");

        Label titleLabel = createWrappedLabel(title, 14, true, BLACK);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(12, iconBox, titleLabel, spacer);
        header.setAlignment(Pos.CENTER_LEFT);

        if (!right.isEmpty()) {
            Label rightLabel = createWrappedLabel(right, 12, true, PURPLE);
            header.getChildren().add(rightLabel);
        }

        return header;
    }

    private Label sectionLabel(String text) {
        return createWrappedLabel(text, 12, true, BLACK);
    }

    private Label bigNumber(String value) {
        return createWrappedLabel(value, 28, true, BLACK);
    }

    private Label createSmallSecondaryText(String text) {
        return createWrappedLabel(text, 11, false, "#475569");
    }

    private Label badge(String text, String bgColor, String textColor) {
        Label badge = new Label(text);
        badge.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        badge.setStyle("-fx-text-fill: " + textColor + " !important; -fx-background-color: " + bgColor + "; -fx-background-radius: 6; -fx-padding: 3 8 3 8;");
        return badge;
    }

    private Label createWrappedLabel(String text, double fontSize, boolean isBold, String hexColor) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, isBold ? FontWeight.BOLD : FontWeight.NORMAL, fontSize));
        label.setTextFill(Color.web(hexColor));
        label.setWrapText(true);
        label.setStyle("-fx-text-fill: " + hexColor + " !important;");
        return label;
    }

    private Label link(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        label.setStyle("-fx-text-fill: " + PURPLE + " !important;");
        label.setCursor(javafx.scene.Cursor.HAND);

        label.setOnMouseEntered(e -> {
            label.setUnderline(true);
            label.setStyle("-fx-text-fill: #6D28D9 !important;");
        });
        label.setOnMouseExited(e -> {
            label.setUnderline(false);
            label.setStyle("-fx-text-fill: " + PURPLE + " !important;");
        });

        return label;
    }

    private Separator separator() {
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: " + CARD_BORDER + ";");
        return separator;
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