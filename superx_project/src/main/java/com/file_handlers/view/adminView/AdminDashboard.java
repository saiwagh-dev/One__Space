package com.file_handlers.view.adminView;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import javafx.concurrent.Task;
import java.io.InputStream;

import com.file_handlers.model.UserSession;
import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.view.LandingPage;
import com.file_handlers.dao.AdminStatsDAO;

import java.time.LocalTime;

public class AdminDashboard {
    

    // Typography
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar: Deep Sleek Obsidian/Navy Tones
    private static final String SIDEBAR_BG = "#070C16";
    public static final String SIDEBAR_DARK = "#070C16";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";

    // 2. Center Workspace Canvas: Atmospheric Dark Radial Glow
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards & Text Colors
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";
    private static final String CARD_TITLE = "#FFFFFF";
    private static final String CARD_VALUE = "#FFFFFF";
    private static final String CARD_SECONDARY = "#94A3B8";

    // 4. Vibrant Typography & Accent Highlights
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";
    private static final String BLUE_LIGHT = "rgba(37, 99, 235, 0.15)";
    private static final String CYAN = "#00D2FF";
    private static final String CYAN_LIGHT = "rgba(0, 210, 255, 0.15)";
    private static final String GREEN = "#10B981";

    private final AdminStatsDAO statsDAO = new AdminStatsDAO();
    private Label totalUsersValue;
    private Label totalFilesValue;

    private String activeUserName = "Admin";
    private String initials = "A";
    public AdminDashboard() {
        UserSession session = UserSession.getInstance();

        if (session != null && session.getDisplayName() != null) {
            String fullName = session.getDisplayName().trim();
            if (!fullName.isEmpty()) {
                String[] parts = fullName.split("\\s+");
                this.activeUserName = parts[0];
                this.initials = this.activeUserName.substring(0, 1).toUpperCase();
            }
        }}
    private final AdminStatsDAO statsDAO1 = new AdminStatsDAO();
    private Label totalUsersValue1;
    private Label totalFilesValue1;

  

    public Scene getAdminDashboardScene() {
       
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createDashboardContent());
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
        logoText.setTextFill(Color.web(WHITE));

        HBox logoRow = new HBox(10, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(4, logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = createSidebarButton("dashboard", "Dashboard", true);
        dashboard.setOnAction(e -> LandingPage.showAdminDashboard());
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
        notification.setOnAction(e -> LandingPage.showAdminNotificationPage());

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        Label admin = new Label(activeUserName);
        admin.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        admin.setTextFill(Color.WHITE);

        HBox profile = new HBox(10, avatar, admin);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(4, 12, 4, 6));
        profile.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");

        ContextMenu profileMenu = createProfileMenu();
        profile.setOnMouseClicked(e -> {
            if (profileMenu.isShowing()) {
                profileMenu.hide();
            } else {
                profileMenu.show(profile, Side.BOTTOM, -50, 8);
            }
        });

        HBox topBar = new HBox(16, spacer, notification, profile);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPrefHeight(70);
        topBar.setMinHeight(70);
        topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private ContextMenu createProfileMenu() {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setStyle(
            "-fx-background-color: #0B132B;" +
            "-fx-background-insets: 0;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: rgba(255, 255, 255, 0.1);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 14;" +
            "-fx-padding: 6;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.75), 24, 0, 0, 8);"
        );

        Button profileBtn = createProfileMenuItem(
            "users", 
            "Profile Page", 
            "#F59E0B", 
            "linear-gradient(to right, #D97706, #F59E0B)", 
            "rgba(245, 158, 11, 0.8)", 
            () -> {
                contextMenu.hide();
                LandingPage.showAdminProfilePage();
            }
        );

        Button settingsBtn = createProfileMenuItem(
            "settings", 
            "Settings", 
            "#38BDF8", 
            "linear-gradient(to right, #0284C7, #00D2FF)", 
            "rgba(56, 189, 248, 0.8)", 
            () -> {
                contextMenu.hide();
                LandingPage.showAdminSettings();
            }
        );

        Button signOutBtn = createProfileMenuItem(
            "logout", 
            "Sign Out", 
            "#F87171", 
            "linear-gradient(to right, #DC2626, #EF4444)", 
            "rgba(248, 113, 113, 0.8)", 
            () -> {
                contextMenu.hide();
                LandingPage.showAdminLoginPage();
            }
        );

        Region menuDivider = new Region();
        menuDivider.setPrefHeight(1);
        menuDivider.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-margin: 4 0;");

        VBox menuBox = new VBox(4, profileBtn, settingsBtn, menuDivider, signOutBtn);
        menuBox.setPrefWidth(168);
        menuBox.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");

        CustomMenuItem customMenuItem = new CustomMenuItem(menuBox, false);
        customMenuItem.setHideOnClick(false);
        customMenuItem.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-background-insets: 0;");
        contextMenu.getItems().add(customMenuItem);

        return contextMenu;
    }

    private Button createProfileMenuItem(String iconType, String text, String iconColor, String activeGradient, String activeBorder, Runnable action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(1.8);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(20, 20);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        label.setTextFill(Color.web("#E2E8F0"));

        HBox row = new HBox(12, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(8, 12, 8, 12));

        String idleStyle = "-fx-background-color: transparent; -fx-background-radius: 8; -fx-border-width: 0; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: rgba(255, 255, 255, 0.06); -fx-background-radius: 8; -fx-border-width: 0; -fx-cursor: hand;";
        String clickStyle = "-fx-background-color: " + activeGradient + "; -fx-border-color: " + activeBorder + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 1; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, " + iconColor + "66, 12, 0, 0, 2);";

        button.setStyle(idleStyle);

        button.setOnMouseEntered(e -> {
            button.setStyle(hoverStyle);
            label.setTextFill(Color.WHITE);
        });

        button.setOnMouseExited(e -> {
            button.setStyle(idleStyle);
            icon.setStroke(Color.web(iconColor));
            label.setTextFill(Color.web("#E2E8F0"));
        });

        button.setOnMousePressed(e -> {
            button.setStyle(clickStyle);
            icon.setStroke(Color.WHITE);
            label.setTextFill(Color.WHITE);
        });

        button.setOnMouseReleased(e -> {
            button.setStyle(hoverStyle);
            icon.setStroke(Color.web(iconColor));
            label.setTextFill(Color.WHITE);
        });

        button.setOnAction(e -> action.run());
        return button;
    }

    private String getTimeBasedGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return "Good Morning, Admin!";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon, Admin!";
        } else if (hour >= 17 && hour < 22) {
            return "Good Evening, Admin!";
        } else {
            return "Good Night, Admin!";
        }
    }

    private VBox createDashboardContent() {
        Label welcome = new Label(getTimeBasedGreeting());
        welcome.setFont(Font.font(FONT, FontWeight.BOLD, 26));
        welcome.setTextFill(Color.web(WHITE));

        Label subtitle = new Label("Here's what's happening in OneSpace today.");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox heading = new VBox(4, welcome, subtitle);

        GridPane grid = new GridPane();
        grid.setHgap(14); grid.setVgap(14);
        grid.setAlignment(Pos.TOP_LEFT);

        ColumnConstraints firstColumn = new ColumnConstraints();
        firstColumn.setPercentWidth(23);
        ColumnConstraints secondColumn = new ColumnConstraints();
        secondColumn.setPercentWidth(23);
        grid.getColumnConstraints().addAll(firstColumn, secondColumn);

        totalUsersValue = new Label("Loading...");
        totalFilesValue = new Label("Loading...");

        VBox totalUsers = createStatCard(
                "users", "Total Users", totalUsersValue,
                "Current registered users", BLUE, BLUE_LIGHT,
                e -> Platform.runLater(LandingPage::showAdminUsers)
        );

        VBox totalFiles = createStatCard(
                "files", "Total Files", totalFilesValue,
                "Current uploaded files", CYAN, CYAN_LIGHT,
                e -> Platform.runLater(LandingPage::showAdminFiles)
        );

        grid.add(totalUsers, 0, 0);
        grid.add(totalFiles, 1, 0);

        loadStatsAsync();

        VBox systemHealth = createSystemHealth();

        VBox content = new VBox(22, heading, grid, systemHealth);
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setFillWidth(true);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setStyle("-fx-background-color: transparent;");
        return content;
    }

    // =========================================================
    // LOAD REAL ADMIN STATS
    // =========================================================

    private void loadStatsAsync() {

        Task<int[]> task = new Task<>() {
            @Override
            protected int[] call() throws Exception {

                int totalUsers = statsDAO.getTotalUsers();
                int totalFiles = statsDAO.getTotalFiles();

                return new int[]{totalUsers, totalFiles};
            }
        };

        task.setOnSucceeded(e -> {

            int[] stats = task.getValue();

            totalUsersValue.setText(
                    String.valueOf(stats[0])
            );

            totalFilesValue.setText(
                    String.valueOf(stats[1])
            );
        });

        task.setOnFailed(e -> {

            totalUsersValue.setText("--");
            totalFilesValue.setText("--");

            System.err.println(
                    "Unable to load admin statistics: "
                            + task.getException()
            );
        });

        Thread thread =
                new Thread(task, "AdminStatsLoader");

        thread.setDaemon(true);
        thread.start();
    }

    private VBox createStatCard(String iconType, String title, Label valueLabel, String description, String iconColor, String iconBackground, EventHandler<MouseEvent> onClick) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        titleLabel.setStyle("-fx-text-fill: " + CARD_TITLE + ";");

        valueLabel.setFont(Font.font(FONT, FontWeight.BOLD, 28));
        valueLabel.setStyle("-fx-text-fill: " + CARD_VALUE + ";");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        descriptionLabel.setStyle("-fx-text-fill: " + CARD_SECONDARY + ";");

        VBox text = new VBox(6, titleLabel, valueLabel, descriptionLabel);
        text.setAlignment(Pos.CENTER_LEFT);

        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(2.2);

        StackPane iconCircle = new StackPane(icon);
        iconCircle.setPrefSize(32, 32); iconCircle.setMinSize(32, 32); iconCircle.setMaxSize(32, 32);
        iconCircle.setStyle("-fx-background-color: " + iconBackground + "; -fx-border-color: " + iconColor + "55; -fx-border-radius: 8; -fx-background-radius: 8;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(15, text, spacer, iconCircle);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(row);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(20));
        card.setFocusTraversable(false);
        card.setCache(true);
        
        String styleIdle = "-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);";
        String styleHover = "-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.9), rgba(12, 22, 40, 0.95)); -fx-border-color: " + iconColor + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, " + iconColor + "66, 20, 0, 0, 6);";

        card.setStyle(styleIdle);
        card.setOnMouseEntered(e -> card.setStyle(styleHover));
        card.setOnMouseExited(e -> card.setStyle(styleIdle));

        if (onClick != null) {
            card.setOnMouseClicked(e -> onClick.handle(e));
        }

        return card;
    }

    private VBox createSystemHealth() {
        Label title = new Label("System Health");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: " + CARD_TITLE + ";");

        Label subtitle = new Label("Current status of OneSpace services.");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setStyle("-fx-text-fill: " + CARD_SECONDARY + ";");

        VBox heading = new VBox(5, title, subtitle);
        
        VBox services = new VBox(12,
                createHealthRow("Database"),
                createHealthRow("Authentication"),
                createHealthRow("Local File Access"),
                createHealthRow("AI Processing Service")
        );

        VBox card = new VBox(18, heading, services);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(28, 24, 28, 24));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");
        return card;
    }

    private HBox createHealthRow(String serviceName) {
        Label service = new Label(serviceName);
        service.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        service.setStyle("-fx-text-fill: " + CARD_TITLE + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Circle dot = new Circle(3.5);
        dot.setFill(Color.web(GREEN));

        Label online = new Label("Online");
        online.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        online.setStyle("-fx-text-fill: #34D399;");

        HBox status = new HBox(6, dot, online);
        status.setAlignment(Pos.CENTER);
        status.setPadding(new Insets(4, 10, 4, 10));
        status.setStyle("-fx-background-color: rgba(16, 185, 129, 0.15); -fx-border-color: rgba(16, 185, 129, 0.3); -fx-border-radius: 6; -fx-background-radius: 6;");

        HBox row = new HBox(10, service, spacer, status);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 12, 8, 12));
        row.setStyle("-fx-background-color: rgba(10, 18, 33, 0.85); -fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-radius: 8; -fx-background-radius: 8;");
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