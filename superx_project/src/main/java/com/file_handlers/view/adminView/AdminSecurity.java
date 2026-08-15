package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
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
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class AdminSecurity {

    // =========================================================
    // DARK THEME PALETTE & CONSTANTS
    // =========================================================

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
    private static final String PURPLE = "#7C3AED";
    private static final String PURPLE_LIGHT = "#EDE9FE";
    private static final String GREEN = "#059669";
    private static final String RED = "#DC2626";
    private static final String ORANGE = "#D97706";


    // =========================================================
    // MAIN SCENE
    // =========================================================

    public Scene getSecurityScene() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");

        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createSecurityContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-border-color: transparent;"
        );

        VBox rightSide = new VBox(
                createTopBar(),
                scrollPane
        );
        rightSide.setFillWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.setCenter(rightSide);

        Scene scene = new Scene(root, 1200, 750);

        String cssOverride = "data:text/css," +
                ".dark-grid-card * { -fx-text-fill: #000000 !important; -fx-fill: #000000 !important; }" +
                ".dark-grid-card .text { -fx-text-fill: #000000 !important; -fx-fill: #000000 !important; }" +
                ".axis-label, .axis .tick-label { -fx-fill: #000000 !important; -fx-text-fill: #000000 !important; }" +
                ".slate-dark-combo .list-cell { -fx-text-fill: #F8FAFC !important; -fx-font-weight: bold; -fx-background-color: #1E2A3A !important; }" +
                ".slate-dark-combo .arrow { -fx-background-color: #94A3B8 !important; }";
        scene.getStylesheets().add(cssOverride);

        return scene;
    }


    // =========================================================
    // SIDEBAR
    // =========================================================

    private VBox createSidebar() {

        VBox sidebar = new VBox(10);
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
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        logoText.setTextFill(Color.web(WHITE));

        HBox logoRow = new HBox(12, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        Label tagline = new Label("Your AI Workspace");
        tagline.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        tagline.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox logoSection = new VBox(6, logoRow, tagline);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardButton = createSidebarButton("dashboard", "Dashboard", false);
        Button usersButton = createSidebarButton("users", "Users", false);
        Button filesButton = createSidebarButton("files", "Files", false);
        Button storageButton = createSidebarButton("storage", "Storage", false);
        Button aiButton = createSidebarButton("ai", "AI System", false);
        Button analyticsButton = createSidebarButton("analytics", "Analytics", false);
        Button securityButton = createSidebarButton("security", "Security", true);

        dashboardButton.setOnAction(e -> LandingPage.showAdminDashboard());
        usersButton.setOnAction(e -> LandingPage.showAdminUsers());
        filesButton.setOnAction(e -> LandingPage.showAdminFiles());
        aiButton.setOnAction(e -> LandingPage.showAdminAISystem());
        analyticsButton.setOnAction(e -> LandingPage.showAnalytics());
        securityButton.setOnAction(e -> LandingPage.showAdminSecurity());

        VBox navList = new VBox(
                4,
                dashboardButton,
                usersButton,
                filesButton,
                storageButton,
                aiButton,
                analyticsButton,
                securityButton
        );

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        Button settingsButton = createSidebarButton("settings", "Settings", false);
        settingsButton.setOnAction(e -> LandingPage.showAdminSettings());

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + SIDEBAR_BORDER + ";");

        Button logoutButton = createSidebarButton("logout", "Logout", false);
        logoutButton.setOnAction(e -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(
                logoSection,
                navList,
                sidebarSpacer,
                settingsButton,
                divider,
                logoutButton
        );

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

    private Button createSidebarButton(String type, String text, boolean selected) {

        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(selected ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(27, 27);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, selected ? FontWeight.BOLD : FontWeight.NORMAL, 16));
        label.setTextFill(Color.web(WHITE));

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


    // =========================================================
    // TOPBAR
    // =========================================================

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
        searchBox.setMaxWidth(500);
        searchBox.setPadding(new Insets(0, 10, 0, 12));
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
        notificationButton.setPrefSize(38, 38);
        notificationButton.setStyle("-fx-background-color: transparent; -fx-font-size: 19px; -fx-text-fill: #FFFFFF; -fx-cursor: hand;");

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
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
        topBar.setStyle(
                "-fx-background-color: " + SIDEBAR_BG + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 0 1 0;"
        );

        return topBar;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search anything...");
        searchField.setFont(Font.font(FONT, FontWeight.NORMAL, 15));
        searchField.setPrefHeight(38);
        searchField.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #F8FAFC;" +
                "-fx-prompt-text-fill: #94A3B8;" +
                "-fx-border-color: transparent;" +
                "-fx-padding: 0;"
        );
        return searchField;
    }


    // =========================================================
    // SECURITY CONTENT (2 GRIDS PER LINE)
    // =========================================================

    private VBox createSecurityContent() {

        VBox root = new VBox(22);
        root.setFillWidth(true);
        root.setPadding(new Insets(32, 38, 36, 38));
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");

        Label title = new Label("Security Overview");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 30));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Monitor and manage the security of your OneSpace system");
        subtitle.setFont(Font.font(FONT, FontWeight.NORMAL, 15));
        subtitle.setTextFill(Color.WHITE);

        VBox headerText = new VBox(4, title, subtitle);

        ComboBox<String> date = new ComboBox<>();
        date.getItems().addAll(
                "May 15 - Jun 15, 2025",
                "Last 7 Days",
                "Last 30 Days",
                "Last 90 Days"
        );
        date.setValue("May 15 - Jun 15, 2025");
        date.setPrefWidth(200);
        date.setPrefHeight(38);
        date.getStyleClass().add("slate-dark-combo");
        date.setStyle(
                "-fx-background-color: #1E2A3A;" +
                "-fx-border-color: #334155;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #F8FAFC;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 5, 0, 0, 2);"
        );

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox header = new HBox(headerText, headerSpacer, date);
        header.setAlignment(Pos.CENTER_LEFT);

        // Row 1: Failed Logins & Active Sessions
        HBox row1 = new HBox(18);
        addEqualChildren(row1, createFailedLoginCard(), createSessionsCard());

        // Row 2: Security Alerts & 2FA Users
        HBox row2 = new HBox(18);
        addEqualChildren(row2, createAlertsCard(), createTwoFACard());

        // Row 3: Suspicious Activity & Audit Logs
        HBox row3 = new HBox(18);
        addEqualChildren(row3, createSuspiciousCard(), createAuditLogsCard());

        root.getChildren().addAll(
                header,
                row1,
                row2,
                row3
        );

        return root;
    }

    private void addEqualChildren(HBox row, Region child1, Region child2) {
        child1.setMaxWidth(Double.MAX_VALUE); child1.setMinWidth(0);
        child2.setMaxWidth(Double.MAX_VALUE); child2.setMinWidth(0);
        HBox.setHgrow(child1, Priority.ALWAYS);
        HBox.setHgrow(child2, Priority.ALWAYS);
        row.getChildren().addAll(child1, child2);
    }


    // =========================================================
    // FAILED LOGIN CARD
    // =========================================================

    private VBox createFailedLoginCard() {

        VBox card = card();
        card.setMinHeight(330);

        HBox header = cardHeader("security", "Failed Login Attempts", "Last 30 Days");
        HBox numberRow = new HBox(6, bigNumber("128"));
        HBox change = new HBox(4, createColoredText("↑ 18.6%", GREEN), createSmallSecondaryText("from last month"));

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setTickLabelFill(Color.web(BLACK));
        yAxis.setTickLabelFill(Color.web(BLACK));
        xAxis.setTickLabelFont(Font.font(FONT, 8));
        yAxis.setTickLabelFont(Font.font(FONT, 8));

        xAxis.setTickMarkVisible(false);
        yAxis.setTickMarkVisible(false);

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setCategoryGap(4);
        chart.setBarGap(1);
        chart.setPrefHeight(115);
        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(false);
        chart.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        int[] values = { 15, 30, 47, 10, 24, 42, 8, 12, 25, 18, 45, 31, 13, 8, 55, 42, 10, 32, 28, 15, 20, 10, 30, 47, 70 };

        for (int i = 0; i < values.length; i++) {
            String label = (i == 0) ? "May 15" : (i == 7) ? "May 22" : (i == 14) ? "May 29" : (i == 18) ? "Jun 05" : (i == 24) ? "Jun 12" : "";
            series.getData().add(new XYChart.Data<>(label, values[i]));
        }

        chart.getData().add(series);

        Platform.runLater(() -> {
            chart.applyCss();
            chart.layout();
            for (XYChart.Data<String, Number> d : series.getData()) {
                if (d.getNode() != null) {
                    d.getNode().setStyle("-fx-bar-fill: " + RED + ";");
                }
            }
        });

        VBox ips = new VBox(
                3,
                ipRow("192.168.1.45", "28 attempts"),
                ipRow("203.0.113.10", "21 attempts"),
                ipRow("45.77.32.11", "17 attempts")
        );

        card.getChildren().addAll(header, numberRow, change, chart, sectionLabel("Top IP Addresses"), ips);
        return card;
    }

    private HBox ipRow(String ip, String attempts) {

        Text ipLabel = createTextNode(ip, 10, false, BLACK);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text attemptsLabel = createTextNode(attempts, 10, true, RED);

        HBox row = new HBox(6, new Circle(3, Color.web(RED)), ipLabel, spacer, attemptsLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }


    // =========================================================
    // ACTIVE SESSIONS
    // =========================================================

    private VBox createSessionsCard() {

        VBox card = card();
        card.setMinHeight(330);

        card.getChildren().add(cardHeader("storage", "Active Sessions", "View All"));

        GridPane table = new GridPane();
        table.setHgap(6);
        table.setVgap(8);
        table.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints userColumn = new ColumnConstraints(); userColumn.setPercentWidth(28);
        ColumnConstraints deviceColumn = new ColumnConstraints(); deviceColumn.setPercentWidth(20);
        ColumnConstraints locationColumn = new ColumnConstraints(); locationColumn.setPercentWidth(20);
        ColumnConstraints durationColumn = new ColumnConstraints(); durationColumn.setPercentWidth(14);
        ColumnConstraints actionColumn = new ColumnConstraints(); actionColumn.setPercentWidth(18);

        table.getColumnConstraints().addAll(userColumn, deviceColumn, locationColumn, durationColumn, actionColumn);

        String[] headers = { "User", "Device", "Location", "Duration", "Action" };
        for (int i = 0; i < headers.length; i++) {
            Text label = createTextNode(headers[i], 9, true, BLACK);
            table.add(label, i, 0);
        }

        List<Session> list = Arrays.asList(
                new Session("AV", "Aarav Verma", "Windows 11", "Pune, India", "2 hours"),
                new Session("NS", "Neha Singh", "Android", "Mumbai, India", "45 min"),
                new Session("RS", "Riya Sharma", "macOS", "Bangalore", "1 hour"),
                new Session("RM", "Rahul Mehta", "iPhone 14", "Delhi, India", "30 min")
        );

        int r = 1;
        for (Session session : list) {
            HBox userBox = new HBox(4, avatarCircle(session.initials), textNode(session.name, BLACK, true));
            userBox.setAlignment(Pos.CENTER_LEFT);

            table.add(userBox, 0, r);
            table.add(textNode(session.device, BLACK, false), 1, r);
            table.add(textNode(session.location, BLACK, false), 2, r);
            table.add(textNode(session.duration, BLACK, false), 3, r);

            Button terminate = new Button("Terminate");
            terminate.setFont(Font.font(FONT, FontWeight.BOLD, 9));
            terminate.setStyle("-fx-text-fill: " + RED + " !important; -fx-background-color: #FEE2E2; -fx-border-color: #FCA5A5; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;");
            terminate.setOnAction(e -> {
                terminate.setText("Ended");
                terminate.setDisable(true);
            });

            table.add(terminate, 4, r);
            r++;
        }

        VBox.setVgrow(table, Priority.ALWAYS);
        card.getChildren().addAll(table, separator(), link("View All Active Sessions  →"));
        return card;
    }


    // =========================================================
    // SECURITY ALERTS
    // =========================================================

    private VBox createAlertsCard() {

        VBox card = card();
        card.setMinHeight(330);

        VBox alerts = new VBox(
                0,
                alert("bell", "Multiple failed login attempts", "User: aarav.verma@example.com", "10 min ago", ORANGE),
                alert("bell", "Server connection interrupted", "Storage service disconnected", "25 min ago", ORANGE),
                alert("bell", "Backup service unavailable", "Last backup failed", "1 hour ago", ORANGE),
                alert("security", "Password changed", "User: neha.singh@example.com", "2 hours ago", BLUE),
                alert("ai", "New device logged in", "User: riya.sharma@example.com", "3 hours ago", GREEN)
        );

        card.getChildren().addAll(
                cardHeader("bell", "Security Alerts", "View All"),
                alerts,
                separator(),
                link("View All Alerts  →")
        );

        return card;
    }

    private HBox alert(String iconType, String title, String description, String time, String color) {

        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(color));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setMinWidth(20);

        Text titleLabel = createTextNode(title, 10, true, BLACK);
        Text descriptionLabel = createTextNode(description, 9, false, BLACK);

        VBox text = new VBox(2, titleLabel, descriptionLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text timeLabel = createTextNode(time, 9, false, BLACK);

        HBox row = new HBox(8, iconPane, text, spacer, timeLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 0, 6, 0));
        return row;
    }


    // =========================================================
    // TWO FACTOR AUTHENTICATION
    // =========================================================

    private VBox createTwoFACard() {

        VBox card = card();
        card.setMinHeight(330);

        card.getChildren().add(cardHeader("ai", "Users with 2FA Enabled", ""));

        HBox donutArea = new HBox(12);
        StackPane donut = donut();
        donut.setPrefSize(105, 105);

        VBox legendArea = new VBox(
                10,
                legend(GREEN, "2FA Enabled", "342 (68.4%)"),
                legend("#94A3B8", "2FA Disabled", "158 (31.6%)")
        );
        legendArea.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(legendArea, Priority.ALWAYS);

        donutArea.getChildren().addAll(donut, legendArea);
        card.getChildren().addAll(donutArea, separator(), link("Manage 2FA Settings  →"));

        return card;
    }

    private StackPane donut() {

        Arc backgroundArc = new Arc(0, 0, 48, 48, 0, 360);
        backgroundArc.setType(ArcType.OPEN);
        backgroundArc.setFill(Color.TRANSPARENT);
        backgroundArc.setStroke(Color.web("#CBD5E1"));
        backgroundArc.setStrokeWidth(14);

        Arc enabledArc = new Arc(0, 0, 48, 48, 90, -246);
        enabledArc.setType(ArcType.OPEN);
        enabledArc.setFill(Color.TRANSPARENT);
        enabledArc.setStroke(Color.web(GREEN));
        enabledArc.setStrokeWidth(14);

        Text number = createTextNode("342", 17, true, BLACK);
        Text total = createTextNode("Total Users", 9, false, BLACK);

        VBox center = new VBox(1, number, total);
        center.setAlignment(Pos.CENTER);

        StackPane pane = new StackPane(backgroundArc, enabledArc, center);
        pane.setPrefSize(105, 105);
        return pane;
    }

    private VBox legend(String color, String title, String value) {

        Text titleLabel = createTextNode(title, 10, false, BLACK);
        Text valueLabel = createTextNode(value, 10, true, BLACK);

        HBox titleRow = new HBox(5, new Circle(4, Color.web(color)), titleLabel);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        return new VBox(2, titleRow, valueLabel);
    }


    // =========================================================
    // SUSPICIOUS ACTIVITY
    // =========================================================

    private VBox createSuspiciousCard() {

        VBox card = card();
        card.setMinHeight(260);

        VBox list = new VBox(
                6,
                suspicious("Multiple failed logins", "User: rahul.mehta@example.com", "8 failed attempts in 15 min", "5 min ago"),
                suspicious("Too many upload requests", "User: unknown", "1450 requests in 10 min", "20 min ago")
        );

        card.getChildren().addAll(
                cardHeader("security", "Suspicious Activity", "View All"),
                list,
                separator(),
                link("View All Suspicious Activity  →")
        );

        return card;
    }

    private VBox suspicious(String title, String user, String description, String time) {

        SVGPath icon = createIcon("security");
        icon.setStroke(Color.web(RED));
        icon.setStrokeWidth(2);

        Text titleLabel = createTextNode(title, 10, true, BLACK);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text timeLabel = createTextNode(time, 9, false, BLACK);
        Text userLabel = createTextNode(user, 9, false, BLACK);
        Text descriptionLabel = createTextNode(description, 9, false, BLACK);

        HBox titleRow = new HBox(5, icon, titleLabel, spacer, timeLabel);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(2, titleRow, userLabel, descriptionLabel);
        box.setPadding(new Insets(6));
        box.setStyle("-fx-background-color: #FEE2E2; -fx-border-color: #FCA5A5; -fx-border-radius: 7; -fx-background-radius: 7;");
        return box;
    }


    // =========================================================
    // AUDIT LOGS
    // =========================================================

    private VBox createAuditLogsCard() {

        VBox card = card();
        card.setMinHeight(260);

        card.getChildren().add(cardHeader("files", "Audit Logs", "View All"));

        GridPane table = new GridPane();
        table.setHgap(7);
        table.setVgap(8);

        ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(26);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(20);
        ColumnConstraints c3 = new ColumnConstraints(); c3.setPercentWidth(18);
        ColumnConstraints c4 = new ColumnConstraints(); c4.setPercentWidth(36);

        table.getColumnConstraints().addAll(c1, c2, c3, c4);

        String[] headers = { "Timestamp", "Event", "User", "Details" };
        for (int i = 0; i < headers.length; i++) {
            Text label = createTextNode(headers[i], 9, true, BLACK);
            table.add(label, i, 0);
        }

        List<Audit> audits = Arrays.asList(
                new Audit("Jun 15 10:55 AM", "User suspended", "Aarav Verma", "Suspended for 7 days"),
                new Audit("Jun 15 10:40 AM", "Category updated", "Neha Singh", "'Finance' updated"),
                new Audit("Jun 15 10:35 AM", "User created", "Admin", "New user: Karan Patel")
        );

        int r = 1;
        for (Audit audit : audits) {
            table.add(tableNode(audit.time), 0, r);
            table.add(tableNode(audit.event), 1, r);
            table.add(tableNode(audit.user), 2, r);
            table.add(tableNode(audit.details), 3, r);
            r++;
        }

        card.getChildren().addAll(table, separator(), link("View All Audit Logs  →"));
        return card;
    }


    // =========================================================
    // UI HELPERS
    // =========================================================

    private VBox card() {
        VBox box = new VBox(7);
        box.setFillWidth(true);
        box.setPadding(new Insets(14));
        box.getStyleClass().add("dark-grid-card");
        box.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 7, 0, 0, 2);"
        );
        return box;
    }

    private HBox cardHeader(String iconType, String title, String right) {

        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(PURPLE));
        icon.setStrokeWidth(2);

        Text titleLabel = createTextNode(title, 13, true, BLACK);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(6, icon, titleLabel, spacer);
        header.setAlignment(Pos.CENTER_LEFT);

        if (!right.isEmpty()) {
            Text rightLabel = createTextNode(right, 11, true, PURPLE);
            header.getChildren().add(rightLabel);
        }

        return header;
    }

    private Text sectionLabel(String text) {
        return createTextNode(text, 11, true, BLACK);
    }

    private Text bigNumber(String value) {
        return createTextNode(value, 22, true, BLACK);
    }

    private Text tableNode(String text) {
        return createTextNode(text, 9, false, BLACK);
    }

    private Text textNode(String text, String color, boolean bold) {
        return createTextNode(text, 9, bold, color);
    }

    private StackPane avatarCircle(String initials) {
        Circle circle = new Circle(9, Color.web(PURPLE_LIGHT));
        Text label = createTextNode(initials, 8, true, PURPLE);

        StackPane pane = new StackPane(circle, label);
        pane.setPrefSize(18, 18);
        return pane;
    }

    private Text createSmallSecondaryText(String text) {
        return createTextNode(text, 10, false, BLACK);
    }

    private Text createColoredText(String text, String color) {
        return createTextNode(text, 10, true, color);
    }

    private Text createTextNode(String text, double fontSize, boolean isBold, String hexColor) {
        Text textNode = new Text(text);
        textNode.setFont(Font.font(FONT, isBold ? FontWeight.BOLD : FontWeight.NORMAL, fontSize));
        textNode.setFill(Color.web(hexColor));
        textNode.setStyle("-fx-fill: " + hexColor + " !important; -fx-text-fill: " + hexColor + " !important;");
        return textNode;
    }

    private Label link(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 11));
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


    // =========================================================
    // MODELS
    // =========================================================

    private static class Session {
        String initials, name, device, location, duration;
        Session(String initials, String name, String device, String location, String duration) {
            this.initials = initials; this.name = name; this.device = device;
            this.location = location; this.duration = duration;
        }
    }

    private static class Audit {
        String time, event, user, details;
        Audit(String time, String event, String user, String details) {
            this.time = time; this.event = event; this.user = user; this.details = details;
        }
    }
}