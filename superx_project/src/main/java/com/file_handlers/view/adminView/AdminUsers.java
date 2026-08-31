package com.file_handlers.view.adminView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
import javafx.stage.Popup;
import java.util.ArrayList;
import java.util.List;

import com.file_handlers.dao.AdminUserDAO;
import com.file_handlers.model.UserData;

import com.file_handlers.controller.AdminAuthController;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

public class AdminUsers {
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

    // Accent Colors
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";

    private final ObservableList<UserData> users = FXCollections.observableArrayList();
    private AdminUserDAO adminUserDAO;
    private VBox tableBody;
    private TextField userSearchField;
    private Button statusFilterBtn;
    private String selectedStatus = "All Status";
    private HBox batchActionBar;
    private Label selectedCountLabel;

    public AdminUsers() {
    }

    public Scene getAdminUsersScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createUsersContent());
        loadUsersAsync();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-background-insets: 0; -fx-padding: 0;");

        VBox rightSide = new VBox(createTopBar(), scrollPane);
        rightSide.setStyle("-fx-background-color: " + MAIN_BG + ";");
        rightSide.setFillWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.setCenter(rightSide);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private void loadUsersAsync() {

        if (tableBody != null)
            showLoading();

        Task<List<UserData>> task = new Task<>() {
            @Override
            protected List<UserData> call() throws Exception {
                AdminUserDAO dao = new AdminUserDAO();
                List<UserData> result = dao.listUsers();
                adminUserDAO = dao;
                return result;
            }
        };

        task.setOnSucceeded(e -> {
            users.setAll(task.getValue());
            refreshUserTable();
            updateBatchActionBar();
        });

        task.setOnFailed(e -> {
            users.clear();
            showLoadingError(
                    task.getException() == null
                            ? "Unable to load users."
                            : task.getException().getMessage()
            );
        });

        Thread thread = new Thread(task, "AdminUsersLoader");
        thread.setDaemon(true);
        thread.start();
    }

    private void showLoading() {

        tableBody.getChildren().clear();

        Label loading =
                createLabel(
                        "Loading users...",
                        "-fx-font-size: 14px; -fx-text-fill: #94A3B8;"
                );

        VBox wrapper = new VBox(loading);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(40));

        tableBody.getChildren().add(wrapper);
    }

    private void showLoadingError(String message) {

        tableBody.getChildren().clear();

        Label error =
                createLabel(
                        message == null || message.isBlank()
                                ? "Unable to load users."
                                : "Unable to load users.",
                        "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #F87171;"
                );

        Label detail =
                createLabel(
                        message == null || message.isBlank()
                                ? "Please try again."
                                : message,
                        "-fx-font-size: 12px; -fx-text-fill: #94A3B8;"
                );

        VBox wrapper = new VBox(8, error, detail);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(40));

        tableBody.getChildren().add(wrapper);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH); sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH); sidebar.setMaxWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setTextFill(Color.WHITE);

        HBox logoRow = new HBox(10, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(4, logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = createSidebarButton("dashboard", "Dashboard", false);
        dashboard.setOnAction(e -> LandingPage.showAdminDashboard());
        Button usersButton = createSidebarButton("users", "Users", true);
        usersButton.setOnAction(e -> LandingPage.showAdminUsers());
        Button files = createSidebarButton("files", "Files", false);
        files.setOnAction(e -> LandingPage.showAdminFiles());
        Button collab = createSidebarButton("collaboration", "Collaboration", false);
        collab.setOnAction(e -> LandingPage.showAdminCollaboration());
        Button aiSystem = createSidebarButton("ai", "AI System", false);
        aiSystem.setOnAction(e -> LandingPage.showAdminAISystem());

        Button analytics = createSidebarButton("analytics", "Analytics", false);
        analytics.setOnAction(e -> LandingPage.showAnalytics());
        Button security = createSidebarButton("security", "Security", false);
        security.setOnAction(e -> LandingPage.showAdminSecurity());

        VBox navigation = new VBox(4, dashboard, usersButton, files, collab, aiSystem, analytics, security);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button settings = createSidebarButton("settings", "Settings", false);
        settings.setOnAction(e -> LandingPage.showAdminSettings());

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + SIDEBAR_BORDER + ";");

        Button logout = createSidebarButton("logout", "Logout", false);
        logout.setOnAction(e -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(logoSection, navigation, spacer, settings, divider, logout);
        return sidebar;
    }

    private Button createSidebarButton(String type, String text, boolean active) {
        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.WHITE);

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

    private String getLoggedInAdminName() {
        try {
            AdminAuthController controller = new AdminAuthController();
            for (java.lang.reflect.Method m : controller.getClass().getMethods()) {
                if (m.getName().equalsIgnoreCase("getCurrentAdminName") || 
                    m.getName().equalsIgnoreCase("getLoggedInUserName") ||
                    m.getName().equalsIgnoreCase("getAdminName") ||
                    m.getName().equalsIgnoreCase("getCurrentUser")) {
                    Object val = m.invoke(controller);
                    if (val != null && !val.toString().trim().isEmpty()) {
                        return val.toString().trim();
                    }
                }
            }
        } catch (Exception ignored) {}

        try {
            Class<?> sessionClass = Class.forName("com.file_handlers.session.AdminSession");
            java.lang.reflect.Method m = sessionClass.getMethod("getCurrentUser");
            Object user = m.invoke(null);
            if (user != null) {
                try {
                    Object name = user.getClass().getMethod("getName").invoke(user);
                    if (name != null && !name.toString().trim().isEmpty()) return name.toString().trim();
                } catch (Exception e) {
                    return user.toString().trim();
                }
            }
        } catch (Exception ignored) {}

        try {
            for (java.lang.reflect.Field f : LandingPage.class.getDeclaredFields()) {
                if (f.getName().toLowerCase().contains("user") || f.getName().toLowerCase().contains("admin")) {
                    f.setAccessible(true);
                    Object val = f.get(null);
                    if (val != null && !val.toString().trim().isEmpty() && !val.toString().equalsIgnoreCase("Admin")) {
                        return val.toString().trim();
                    }
                }
            }
        } catch (Exception ignored) {}

        try {
            for (java.lang.reflect.Method m : LandingPage.class.getMethods()) {
                if (m.getParameterCount() == 0 && (m.getName().startsWith("getAdmin") || m.getName().startsWith("getUser") || m.getName().startsWith("getLoggedIn"))) {
                    Object val = m.invoke(null);
                    if (val != null && !val.toString().trim().isEmpty() && !val.toString().equalsIgnoreCase("Admin")) {
                        return val.toString().trim();
                    }
                }
            }
        } catch (Exception ignored) {}

        return "Sai";
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

        String loginName = getLoggedInAdminName();
        String loginInitial = getInitials(loginName);

        Label avatar = new Label(loginInitial);
        avatar.setPrefSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: #0084FF; -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(0, 132, 255, 0.5), 10, 0, 0, 2);");

        Label admin = new Label(loginName);
        admin.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        admin.setTextFill(Color.WHITE);

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

    private VBox createUsersContent() {
        Label title = createLabel("Users", "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        Label subtitle = createLabel("Manage your organization's users easily.", "-fx-font-size: 13px; -fx-font-weight: 500;");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

        // Search Bar below "Manage your organization's users easily."
        SVGPath searchIcon = createIcon("search");
        searchIcon.setStroke(Color.web("#64748B"));
        searchIcon.setStrokeWidth(2);

        StackPane searchIconBox = new StackPane(searchIcon);
        searchIconBox.setPrefSize(24, 24);

        userSearchField = new TextField();
        userSearchField.setPromptText("Search users by name or email...");
        userSearchField.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        userSearchField.setPrefHeight(38);
        userSearchField.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #64748B; -fx-border-color: transparent; -fx-padding: 0;");
        userSearchField.textProperty().addListener((obs, oldVal, newVal) -> refreshUserTable());

        HBox searchBox = new HBox(10, searchIconBox, userSearchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefHeight(40); searchBox.setMinHeight(40); searchBox.setMaxHeight(40);
        searchBox.setPrefWidth(380); searchBox.setMinWidth(380); searchBox.setMaxWidth(380);
        searchBox.setPadding(new Insets(0, 14, 0, 14));
        searchBox.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20;");
        HBox.setHgrow(userSearchField, Priority.ALWAYS);

        VBox titleBox = new VBox(6, title, subtitle, searchBox);

        // Status Filter Button with Pop-up Menu
        HBox statusBtnContent = new HBox(8);
        statusBtnContent.setAlignment(Pos.CENTER_LEFT);
        Label statusBtnLabel = new Label(selectedStatus);
        statusBtnLabel.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        statusBtnLabel.setTextFill(Color.WHITE);

        SVGPath chevronIcon = new SVGPath();
        chevronIcon.setContent("M6 9l6 6 6-6");
        chevronIcon.setStroke(Color.web(LIGHT_SECONDARY));
        chevronIcon.setStrokeWidth(2);
        chevronIcon.setFill(Color.TRANSPARENT);

        statusBtnContent.getChildren().addAll(statusBtnLabel, chevronIcon);

        statusFilterBtn = new Button();
        statusFilterBtn.setGraphic(statusBtnContent);
        statusFilterBtn.setPrefHeight(40);
        statusFilterBtn.setPadding(new Insets(0, 16, 0, 16));
        statusFilterBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;");

        Popup statusPopup = createStatusFilterPopup(statusBtnLabel);
        statusFilterBtn.setOnAction(e -> {
            if (statusPopup.isShowing()) {
                statusPopup.hide();
            } else {
                javafx.geometry.Point2D p = statusFilterBtn.localToScreen(0.0, statusFilterBtn.getHeight());
                statusPopup.show(statusFilterBtn, p.getX(), p.getY() + 6);
            }
        });

        Region filterSpacer = new Region();
        HBox.setHgrow(filterSpacer, Priority.ALWAYS);

        HBox filterRow = new HBox(16, titleBox, filterSpacer, statusFilterBtn);
        filterRow.setAlignment(Pos.BOTTOM_LEFT);

        GridPane tableHeader = createTableGrid();
        tableHeader.setMinHeight(44); tableHeader.setPrefHeight(44);
        tableHeader.setStyle("-fx-background-color: rgba(10, 18, 33, 0.85); -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1 0 1 0; -fx-background-radius: 14 14 0 0;");

        CheckBox selectAll = new CheckBox();
        GridPane.setColumnIndex(selectAll, 0);
        GridPane.setHalignment(selectAll, HPos.CENTER);
        tableHeader.getChildren().add(selectAll);

        tableHeader.add(createHeaderLabel("User"), 1, 0);
        tableHeader.add(createHeaderLabel("Email"), 2, 0);
        tableHeader.add(createHeaderLabel("Account Status"), 3, 0);
        tableHeader.add(createHeaderLabel("Last Login"), 4, 0);
        tableHeader.add(createHeaderLabel("Actions"), 5, 0);

        tableBody = new VBox();
        tableBody.setFillWidth(true);
        tableBody.setStyle("-fx-background-color: transparent;");

        selectAll.setOnAction(e -> {
            for (javafx.scene.Node node : tableBody.getChildren()) {
                if (node instanceof GridPane) {
                    for (javafx.scene.Node child : ((GridPane) node).getChildren()) {
                        if (child instanceof CheckBox) {
                            ((CheckBox) child).setSelected(selectAll.isSelected());
                            break;
                        }
                    }
                }
            }
            updateBatchActionBar();
        });

        showLoading();

        VBox table = new VBox(tableHeader, tableBody);
        table.setFillWidth(true);
        VBox.setVgrow(tableBody, Priority.ALWAYS);
        table.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");

        selectedCountLabel = createLabel("0 selected", "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        Button bulkDeactivateBtn = new Button("Deactivate Selected");
        bulkDeactivateBtn.setStyle("-fx-background-color: #EF4444; -fx-text-fill: #FFFFFF; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        bulkDeactivateBtn.setOnAction(e -> deactivateSelectedUsers());

        Region batchSpacer = new Region();
        HBox.setHgrow(batchSpacer, Priority.ALWAYS);

        batchActionBar = new HBox(14, selectedCountLabel, batchSpacer, bulkDeactivateBtn);
        batchActionBar.setAlignment(Pos.CENTER_LEFT);
        batchActionBar.setPadding(new Insets(10, 16, 10, 16));
        batchActionBar.setStyle("-fx-background-color: #0D1626; -fx-background-radius: 10; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 10;");
        batchActionBar.setVisible(false);
        batchActionBar.setManaged(false);

        VBox content = new VBox(22, filterRow, batchActionBar, table, createPagination());
        content.setFillWidth(true);
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color: transparent;");
        return content;
    }

    private Popup createStatusFilterPopup(Label statusBtnLabel) {
        Popup popup = new Popup();
        popup.setAutoHide(true);

        // 1. All Status (Sky Blue theme)
        HBox allOption = createStatusOptionItem("All Status", "#38BDF8", () -> {
            selectedStatus = "All Status";
            statusBtnLabel.setText(selectedStatus);
            popup.hide();
            refreshUserTable();
        });

        // 2. Active (Emerald Green theme)
        HBox activeOption = createStatusOptionItem("Active", "#10B981", () -> {
            selectedStatus = "Active";
            statusBtnLabel.setText(selectedStatus);
            popup.hide();
            refreshUserTable();
        });

        // 3. Inactive (Rose Red theme)
        HBox inactiveOption = createStatusOptionItem("Inactive", "#EF4444", () -> {
            selectedStatus = "Inactive";
            statusBtnLabel.setText(selectedStatus);
            popup.hide();
            refreshUserTable();
        });

        VBox container = new VBox(4, allOption, activeOption, inactiveOption);
        container.setPrefWidth(140);
        container.setPadding(new Insets(8, 6, 8, 6));
        container.setStyle(
            "-fx-background-color: #0B132B;" +
            "-fx-border-color: rgba(255, 255, 255, 0.12);" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 24, 0, 0, 10);"
        );

        popup.getContent().add(container);
        return popup;
    }

    private HBox createStatusOptionItem(String title, String themeColor, Runnable action) {
        Circle dot = new Circle(4);
        dot.setFill(Color.web(themeColor));

        Label label = new Label(title);
        label.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        label.setTextFill(Color.WHITE);

        HBox item = new HBox(10, dot, label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(8, 12, 8, 12));
        item.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;");

        item.setOnMouseClicked(e -> action.run());
        return item;
    }

    private GridPane createTableGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(0); grid.setVgap(0); grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints checkColumn = new ColumnConstraints(); checkColumn.setPercentWidth(6); checkColumn.setHgrow(Priority.NEVER);
        ColumnConstraints userColumn = new ColumnConstraints(); userColumn.setPercentWidth(25); userColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints emailColumn = new ColumnConstraints(); emailColumn.setPercentWidth(25); emailColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints statusColumn = new ColumnConstraints(); statusColumn.setPercentWidth(16); statusColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints loginColumn = new ColumnConstraints(); loginColumn.setPercentWidth(18); loginColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints actionsColumn = new ColumnConstraints(); actionsColumn.setPercentWidth(10); actionsColumn.setHgrow(Priority.NEVER);

        grid.getColumnConstraints().addAll(checkColumn, userColumn, emailColumn, statusColumn, loginColumn, actionsColumn);
        return grid;
    }

    private void refreshUserTable() {
        if (tableBody == null) return;
        tableBody.getChildren().clear();

        String searchText = userSearchField == null ? "" : userSearchField.getText().trim().toLowerCase();

        List<UserData> filteredUsers = new ArrayList<>();
        for (UserData user : users) {
            boolean searchMatches = searchText.isEmpty() || user.getName().toLowerCase().contains(searchText) || user.getEmail().toLowerCase().contains(searchText);
            boolean statusMatches = selectedStatus.equals("All Status") || user.getStatus().equalsIgnoreCase(selectedStatus);
            if (searchMatches && statusMatches) filteredUsers.add(user);
        }

        if (filteredUsers.isEmpty()) {
            SVGPath emptyIcon = createIcon("search");
            emptyIcon.setStroke(Color.web("#64748B"));
            emptyIcon.setStrokeWidth(2);
            StackPane emptyIconBox = new StackPane(emptyIcon);
            emptyIconBox.setPrefSize(40, 40);

            VBox noUsers = new VBox(10,
                    emptyIconBox,
                    createLabel("No users found", "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;"),
                    createLabel("Try changing your search or status filter.", "-fx-font-size: 13px; -fx-text-fill: #94A3B8;")
            );
            noUsers.setAlignment(Pos.CENTER);
            noUsers.setPadding(new Insets(30));
            noUsers.setStyle("-fx-border-color: " + CARD_BORDER + "; -fx-border-style: dashed; -fx-border-radius: 8;");
            
            VBox wrapper = new VBox(noUsers);
            wrapper.setAlignment(Pos.CENTER);
            wrapper.setPadding(new Insets(20));
            tableBody.getChildren().add(wrapper);
            return;
        }

        for (UserData user : filteredUsers) {
            tableBody.getChildren().add(createUserRow(user));
        }
    }

    private GridPane createUserRow(UserData user) {
        GridPane row = createTableGrid();
        row.setUserData(user);
        row.setMinHeight(64); row.setPrefHeight(64); row.setMaxWidth(Double.MAX_VALUE);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(0, 10, 0, 10));
        row.setStyle("-fx-background-color: transparent; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-width: 0 0 1 0;");

        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateBatchActionBar();
            if (newVal) {
                row.setStyle("-fx-background-color: rgba(37, 99, 235, 0.2); -fx-border-color: #2563EB; -fx-border-width: 0 0 1 3;");
            } else {
                row.setStyle("-fx-background-color: transparent; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-width: 0 0 1 0;");
            }
        });
        GridPane.setHalignment(checkBox, HPos.CENTER);
        row.add(checkBox, 0, 0);

        Circle avatarCircle = new Circle(18, getAvatarColor(user.getName()));
        Label initials = createLabel(getInitials(user.getName()), "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");
        StackPane avatarPane = new StackPane(avatarCircle, initials);

        Label nameLabel = createLabel(user.getName(), "-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        HBox userBox = new HBox(10, avatarPane, nameLabel);
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(0, 8, 0, 8));
        userBox.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(userBox, Priority.ALWAYS);
        row.add(userBox, 1, 0);

        Label emailLabel = createLabel(user.getEmail(), "-fx-font-size: 12px; -fx-text-fill: #94A3B8;");
        emailLabel.setMaxWidth(Double.MAX_VALUE);
        emailLabel.setPadding(new Insets(0, 8, 0, 8));
        emailLabel.setTooltip(new Tooltip(user.getEmail()));
        GridPane.setHgrow(emailLabel, Priority.ALWAYS);
        row.add(emailLabel, 2, 0);

        Circle statusDot = new Circle(3.5);
        Label statusText = new Label(user.getStatus());
        statusText.setFont(Font.font(FONT, FontWeight.BOLD, 11));

        HBox statusLabel = new HBox(6, statusDot, statusText);
        statusLabel.setAlignment(Pos.CENTER_LEFT);
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setPadding(new Insets(4, 10, 4, 10));

        if (user.getStatus().equalsIgnoreCase("Active")) {
            statusDot.setFill(Color.web("#34D399"));
            statusText.setTextFill(Color.web("#34D399"));
            statusLabel.setStyle("-fx-background-color: rgba(16, 185, 129, 0.15); -fx-border-color: rgba(16, 185, 129, 0.3); -fx-border-radius: 12; -fx-background-radius: 12;");
        } else {
            statusDot.setFill(Color.web("#F87171"));
            statusText.setTextFill(Color.web("#F87171"));
            statusLabel.setStyle("-fx-background-color: rgba(239, 68, 68, 0.15); -fx-border-color: rgba(239, 68, 68, 0.3); -fx-border-radius: 12; -fx-background-radius: 12;");
        }
        GridPane.setHgrow(statusLabel, Priority.ALWAYS);
        row.add(statusLabel, 3, 0);

        Label lastLogin = createLabel(user.getLastLogin(), "-fx-font-size: 12px; -fx-text-fill: #94A3B8;");
        lastLogin.setPadding(new Insets(0, 8, 0, 8));
        lastLogin.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lastLogin, Priority.ALWAYS);
        row.add(lastLogin, 4, 0);

        Button actionButton = new Button("⋮");
        actionButton.setPrefSize(36, 32);
        actionButton.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-text-fill: #94A3B8; -fx-cursor: hand;");
        actionButton.setOnMouseEntered(e -> actionButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-background-radius: 6; -fx-font-size: 18px; -fx-text-fill: #38BDF8; -fx-cursor: hand;"));
        actionButton.setOnMouseExited(e -> actionButton.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-text-fill: #94A3B8; -fx-cursor: hand;"));

        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewItem = new MenuItem("View User");
        viewItem.setStyle("-fx-font-size: 11px; -fx-padding: 4 10 4 10; -fx-cursor: hand; -fx-text-fill: #FFFFFF;");
        contextMenu.setStyle("-fx-background-color: #0D1626; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 2;");
        contextMenu.getItems().add(viewItem);

        actionButton.setOnAction(e -> contextMenu.show(actionButton, Side.BOTTOM, 0, 0));
        viewItem.setOnAction(e -> showUserDetails(user));

        row.add(actionButton, 5, 0);
        GridPane.setHalignment(actionButton, HPos.CENTER);

        row.setOnMouseEntered(e -> {
            if (!checkBox.isSelected()) {
                row.setStyle("-fx-background-color: rgba(255, 255, 255, 0.04); -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-width: 0 0 1 3;");
            }
        });
        row.setOnMouseExited(e -> {
            if (!checkBox.isSelected()) {
                row.setStyle("-fx-background-color: transparent; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-width: 0 0 1 0;");
            }
        });
        return row;
    }

    private void deactivateSelectedUsers() {
        List<UserData> selectedUsers = new ArrayList<>();

        for (javafx.scene.Node node : tableBody.getChildren()) {
            if (node instanceof GridPane row) {
                for (javafx.scene.Node child : row.getChildren()) {
                    if (child instanceof CheckBox checkBox && checkBox.isSelected()) {
                        UserData user = (UserData) row.getUserData();
                        if (user != null)
                            selectedUsers.add(user);
                        break;
                    }
                }
            }
        }

        if (selectedUsers.isEmpty()) {
            showInfo("Deactivate Users", "Please select at least one user.");
            return;
        }

        if (adminUserDAO == null) {
            showError(
                    "Deactivate Users",
                    "Users are still loading. Please try again."
            );
            return;
        }

        int successCount = 0;

        try {
            for (UserData user : selectedUsers) {
                adminUserDAO.setUserDisabled(user.getUid(), true);
                successCount++;
            }

            showInfo(
                    "Deactivate Users",
                    successCount + " user" + (successCount == 1 ? "" : "s") +
                    " deactivated successfully."
            );

            loadUsersAsync();

        } catch (Exception e) {
            showError(
                    "Deactivate Users",
                    "Some users could not be deactivated.\n\n" + e.getMessage()
            );
        }

        updateBatchActionBar();
    }

    private void updateBatchActionBar() {
        int count = 0;
        for (javafx.scene.Node node : tableBody.getChildren()) {
            if (node instanceof GridPane) {
                for (javafx.scene.Node child : ((GridPane) node).getChildren()) {
                    if (child instanceof CheckBox && ((CheckBox) child).isSelected()) {
                        count++;
                        break;
                    }
                }
            }
        }
        if (count > 0) {
            selectedCountLabel.setText(count + " user" + (count > 1 ? "s" : "") + " selected");
            batchActionBar.setVisible(true);
            batchActionBar.setManaged(true);
        } else {
            batchActionBar.setVisible(false);
            batchActionBar.setManaged(false);
        }
    }

    private void showUserDetails(UserData user) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Details");
        alert.setHeaderText(user.getName());
        alert.setContentText("Email: " + user.getEmail() + "\n\nAccount Status: " + user.getStatus() + "\n\nLast Login: " + user.getLastLogin());
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private HBox createPagination() {
        Label dots = createLabel("...", "-fx-font-size: 13px; -fx-text-fill: #94A3B8;");
        dots.setAlignment(Pos.CENTER); dots.setPrefSize(32, 36);

        HBox pagination = new HBox(6,
                createPageButton("‹"), createPageButton("1"), createPageButton("2"), createPageButton("3"),
                createPageButton("4"), createPageButton("5"), dots, createPageButton("10"), createPageButton("›")
        );
        pagination.setAlignment(Pos.CENTER);
        pagination.setPadding(new Insets(14, 0, 0, 0));
        return pagination;
    }

    private Button createPageButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(36, 36);
        boolean active = text.equals("1");

        String normalStyle = active 
                ? "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-border-color: rgba(96, 165, 250, 0.6); -fx-text-fill: #FFFFFF; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 12px; -fx-cursor: hand;"
                : "-fx-background-color: rgba(10, 18, 33, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-text: #94A3B8; -fx-text-fill: #94A3B8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 12px; -fx-cursor: hand;";
        button.setStyle(normalStyle);

        if (!active) {
            button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-border-color: rgba(56, 189, 248, 0.4); -fx-text-fill: #FFFFFF; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 12px; -fx-cursor: hand;"));
            button.setOnMouseExited(e -> button.setStyle(normalStyle));
        }
        return button;
    }

    private Label createLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        label.setFont(Font.font(FONT));
        return label;
    }

    private Label createHeaderLabel(String text) {
        Label label = createLabel(text, "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #94A3B8;");
        label.setAlignment(Pos.CENTER_LEFT);
        label.setPadding(new Insets(0, 8, 0, 8));
        return label;
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "U";
        String trimmed = name.trim();
        return String.valueOf(trimmed.charAt(0)).toUpperCase();
    }

    private Color getAvatarColor(String name) {
        int index = Math.abs(name.hashCode()) % 5;
        switch (index) {
            case 0: return Color.web("#38BDF8");
            case 1: return Color.web("#312E81");
            case 2: return Color.web("#059669");
            case 3: return Color.web("#D97706");
            default: return Color.web("#7C3AED");
        }
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