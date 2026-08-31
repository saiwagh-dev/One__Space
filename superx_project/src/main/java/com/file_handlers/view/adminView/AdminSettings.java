package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;
import com.file_handlers.model.UserSession;
import com.file_handlers.util.ResponsiveUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;

public class AdminSettings {
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // Dynamic Theme Tokens (Mutable for Light / Dark toggling)
    private String sidebarBg = "#070C16";
    private String sidebarBorder = "rgba(255, 255, 255, 0.07)";
    private String mainBg = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";
    private String cardBg = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private String cardBorder = "rgba(56, 189, 248, 0.22)";
    private String textPrimary = "#FFFFFF";
    private String textSecondary = "#94A3B8";
    private String topbarWidgetBg = "rgba(13, 22, 38, 0.85)";

    // Accent Colors
    private static final String BLUE = "#2563EB";
    private static final String BLUE_LIGHT = "rgba(37, 99, 235, 0.15)";
    private static final String GREEN = "#10B981";
    private static final String GREEN_LIGHT = "rgba(16, 185, 129, 0.15)";
    private static final String PURPLE = "#00D2FF";
    private static final String PURPLE_LIGHT = "rgba(0, 210, 255, 0.15)";

    // Root Containers for Theme Updates
    private BorderPane rootLayout;
    private VBox sidebarNode;
    private HBox topBarNode;
    private VBox rightSideNode;
    private ScrollPane scrollPaneNode;

    private boolean isLightMode = false;

    public AdminSettings() {}

    public Scene getAdminSettingsScene() {
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + sidebarBg + ";");

        sidebarNode = createSidebar();
        rootLayout.setLeft(sidebarNode);

        scrollPaneNode = new ScrollPane(createMainContent());
        scrollPaneNode.setFitToWidth(true);
        scrollPaneNode.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneNode.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneNode.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");

        topBarNode = createTopBar();

        rightSideNode = new VBox(topBarNode, scrollPaneNode);
        rightSideNode.setStyle("-fx-background: " + mainBg + "; -fx-background-color: " + (isLightMode ? "#F8FAFC" : "#060B14") + ";");
        rightSideNode.setFillWidth(true);
        VBox.setVgrow(scrollPaneNode, Priority.ALWAYS);
        rootLayout.setCenter(rightSideNode);

        return new Scene(rootLayout, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private void applyTheme(boolean lightMode) {
        this.isLightMode = lightMode;

        if (lightMode) {
            sidebarBg = "#FFFFFF";
            sidebarBorder = "#E2E8F0";
            mainBg = "#F8FAFC";
            cardBg = "#FFFFFF";
            cardBorder = "#E2E8F0";
            textPrimary = "#0F172A";
            textSecondary = "#64748B";
            topbarWidgetBg = "#FFFFFF";
        } else {
            sidebarBg = "#070C16";
            sidebarBorder = "rgba(255, 255, 255, 0.07)";
            mainBg = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";
            cardBg = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
            cardBorder = "rgba(56, 189, 248, 0.22)";
            textPrimary = "#FFFFFF";
            textSecondary = "#94A3B8";
            topbarWidgetBg = "rgba(13, 22, 38, 0.85)";
        }

        rootLayout.setStyle("-fx-background-color: " + sidebarBg + ";");
        
        sidebarNode = createSidebar();
        rootLayout.setLeft(sidebarNode);

        topBarNode = createTopBar();
        scrollPaneNode.setContent(createMainContent());

        rightSideNode.getChildren().setAll(topBarNode, scrollPaneNode);
        rightSideNode.setStyle("-fx-background: " + mainBg + "; -fx-background-color: " + (isLightMode ? "#F8FAFC" : "#060B14") + ";");
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMaxWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + sidebarBg + "; -fx-border-color: " + sidebarBorder + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setTextFill(Color.web(textPrimary));

        HBox logoRow = new HBox(10, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(4, logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardButton = createSidebarButton("dashboard", "Dashboard", false);
        Button usersButton = createSidebarButton("users", "Users", false);
        Button filesButton = createSidebarButton("files", "Files", false);
        Button collabButton = createSidebarButton("collaboration", "Collaboration", false);
        Button aiButton = createSidebarButton("ai", "AI System", false);
        Button analyticsButton = createSidebarButton("analytics", "Analytics", false);
        Button securityButton = createSidebarButton("security", "Security", false);

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

        Button settingsButton = createSidebarButton("settings", "Settings", true);
        settingsButton.setOnAction(e -> LandingPage.showAdminSettings());

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + sidebarBorder + ";");

        Button logoutButton = createSidebarButton("logout", "Logout", false);
        logoutButton.setOnAction(e -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(logoSection, navList, sidebarSpacer, settingsButton, divider, logoutButton);
        return sidebar;
    }

    private StackPane createLogo() {
        Image logoImage = new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
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
        icon.setStroke(Color.web(selected ? "#FFFFFF" : textSecondary));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, selected ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.web(selected ? "#FFFFFF" : (isLightMode ? textPrimary : "#FFFFFF")));

        HBox row = new HBox(12, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38);
        button.setMinHeight(38);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        if (selected) {
            button.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.4), 14, 0, 0, 2);"
            );
        } else {
            String hoverBg = isLightMode ? "#F1F5F9" : "rgba(255, 255, 255, 0.05)";
            button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color: " + hoverBg + "; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(BLUE));
                label.setTextFill(Color.web(isLightMode ? BLUE : "#FFFFFF"));
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(textSecondary));
                label.setTextFill(Color.web(isLightMode ? textPrimary : "#FFFFFF"));
            });
        }
        return button;
    }

    private HBox createTopBar() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.web(isLightMode ? textPrimary : "#FFFFFF"));
        bell.setStrokeWidth(2);

        Button notificationButton = new Button();
        notificationButton.setGraphic(bell);
        notificationButton.setStyle("-fx-background-color: " + topbarWidgetBg + "; -fx-border-color: " + sidebarBorder + "; -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        notificationButton.setOnAction(e -> LandingPage.showAdminNotificationPage());

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        Label adminName = new Label(activeUserName);
        adminName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        adminName.setTextFill(Color.web(textPrimary));

        HBox profile = new HBox(10, avatar, adminName);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(4, 12, 4, 6));
        profile.setStyle("-fx-background-color: " + topbarWidgetBg + "; -fx-border-color: " + sidebarBorder + "; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");

        Popup profilePopup = createProfilePopup();
        profile.setOnMouseClicked(e -> {
            if (profilePopup.isShowing()) {
                profilePopup.hide();
            } else {
                javafx.geometry.Point2D p = profile.localToScreen(0.0, profile.getHeight());
                profilePopup.show(profile, p.getX() - 30, p.getY() + 8);
            }
        });

        HBox topBar = new HBox(16, spacer, notificationButton, profile);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPrefHeight(70);
        topBar.setMinHeight(70);
        topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + sidebarBorder + "; -fx-border-width: 0 0 1 0;");
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
        menuDivider.setStyle("-fx-background-color: " + sidebarBorder + ";");

        VBox menuBox = new VBox(6, profileBtn, settingsBtn, menuDivider, signOutBtn);
        menuBox.setPrefWidth(170);
        menuBox.setPadding(new Insets(10, 8, 10, 8));
        menuBox.setStyle(
            "-fx-background-color: " + (isLightMode ? "#FFFFFF" : "#0B132B") + ";" +
            "-fx-border-color: " + (isLightMode ? "#E2E8F0" : "rgba(255, 255, 255, 0.12)") + ";" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 14;" +
            "-fx-background-radius: 14;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, " + (isLightMode ? "0.15" : "0.8") + "), 24, 0, 0, 10);"
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
        label.setTextFill(Color.web(textPrimary));

        HBox item = new HBox(12, iconBox, label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(8, 10, 8, 10));
        item.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        item.setOnMouseClicked(e -> action.run());
        return item;
    }

    private VBox createMainContent() {
        VBox content = new VBox(24);
        content.setFillWidth(true);
        content.setPadding(new Insets(28, ResponsiveUtil.PAGE_PADDING, 40, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color: transparent;");

        Label title = new Label("Settings");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 26));
        title.setTextFill(Color.web(textPrimary));

        Label subtitle = new Label("Manage your account, preferences, indexing controls, and security across OneSpace.");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(textSecondary));

        VBox heading = new VBox(6, title, subtitle);

        // 1. Account Profile Card
        VBox accountCard = createSettingsCard();
        VBox accountInfo = new VBox(3, createCardLabel("Admin", 16, FontWeight.BOLD), createSecondaryLabel("admin@onespace.com", 13));
        HBox accountLeft = new HBox(14, createIconBox("users", BLUE, BLUE_LIGHT), accountInfo);
        accountLeft.setAlignment(Pos.CENTER_LEFT);

        Region accountSpacer = new Region();
        HBox.setHgrow(accountSpacer, Priority.ALWAYS);

        Button editProfile = createOutlineButton("Edit Profile");
        editProfile.setOnAction(e -> LandingPage.showAdminProfilePage());

        HBox accountButtons = new HBox(10, editProfile);
        accountButtons.setAlignment(Pos.CENTER_RIGHT);

        HBox accountRow = new HBox(accountLeft, accountSpacer, accountButtons);
        accountRow.setAlignment(Pos.CENTER_LEFT);
        accountCard.getChildren().add(accountRow);

        // 2. Appearance Card with Live Theme Switching
        VBox appearanceCard = createSettingsCard();
        VBox appearanceTitle = new VBox(3, createCardLabel("Appearance", 16, FontWeight.BOLD), createSecondaryLabel("Customize how OneSpace looks and adapts.", 13));
        HBox appearanceLeft = new HBox(14, createIconBox("appearance", PURPLE, PURPLE_LIGHT), appearanceTitle);
        appearanceLeft.setAlignment(Pos.CENTER_LEFT);

        Button lightButton = createThemeButton("☼", "Light", isLightMode);
        Button darkButton = createThemeButton("☾", "Dark", !isLightMode);
        Button systemButton = createThemeButton("▣", "System", false);

        lightButton.setOnAction(e -> {
            applyTheme(true);
        });

        darkButton.setOnAction(e -> {
            applyTheme(false);
        });

        systemButton.setOnAction(e -> {
            applyTheme(false);
        });

        HBox themeButtons = new HBox(12, lightButton, darkButton, systemButton);
        themeButtons.setAlignment(Pos.CENTER_LEFT);
        VBox themeBox = new VBox(7, createCardLabel("Theme", 13, FontWeight.BOLD), themeButtons);
        themeBox.setAlignment(Pos.CENTER_LEFT);

        Region appearanceSpacer = new Region();
        HBox.setHgrow(appearanceSpacer, Priority.ALWAYS);

        HBox appearanceRow = new HBox(appearanceLeft, appearanceSpacer, themeBox);
        appearanceRow.setAlignment(Pos.CENTER_LEFT);
        appearanceCard.getChildren().add(appearanceRow);

        // 3. Security Settings Card
        VBox securityCard = createSettingsCard();
        VBox securityTitle = new VBox(3, createCardLabel("Security Settings", 16, FontWeight.BOLD), createSecondaryLabel("Manage security and access control.", 13));
        HBox securityHeader = new HBox(14, createIconBox("security", GREEN, GREEN_LIGHT), securityTitle);
        securityHeader.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> passwordCombo = new ComboBox<>();
        passwordCombo.getItems().addAll("Strong (Min 8 characters)", "Medium (Min 6 characters)", "Custom Policy");
        passwordCombo.setValue("Strong (Min 8 characters)");
        styleComboBox(passwordCombo);

        ToggleButton twoFactorToggle = new ToggleButton();
        twoFactorToggle.setSelected(true);
        twoFactorToggle.setPrefSize(45, 24); twoFactorToggle.setMinSize(45, 24); twoFactorToggle.setMaxSize(45, 24);

        Label enabledLabel = createSecondaryLabel("Enabled", 13);
        HBox twoFactorBox = new HBox(10, twoFactorToggle, enabledLabel);
        twoFactorBox.setAlignment(Pos.CENTER_LEFT);
        twoFactorToggle.setStyle(createToggleStyle(true));
        twoFactorToggle.setOnAction(e -> {
            boolean enabled = twoFactorToggle.isSelected();
            enabledLabel.setText(enabled ? "Enabled" : "Disabled");
            twoFactorToggle.setStyle(createToggleStyle(enabled));
        });

        ComboBox<String> sessionCombo = new ComboBox<>();
        sessionCombo.getItems().addAll("15 minutes", "30 minutes", "1 hour", "2 hours", "Never");
        sessionCombo.setValue("30 minutes");
        styleComboBox(sessionCombo);

        ComboBox<String> attemptsCombo = new ComboBox<>();
        attemptsCombo.getItems().addAll("3 attempts allowed", "5 attempts allowed", "10 attempts allowed", "Unlimited");
        attemptsCombo.setValue("5 attempts allowed");
        styleComboBox(attemptsCombo);

        VBox securityForm = new VBox(12,
                createFormRow(createFormLabel("Password Policy"), passwordCombo),
                createFormRow(createFormLabel("Two-Factor Authentication"), twoFactorBox),
                createFormRow(createFormLabel("Session Timeout"), sessionCombo),
                createFormRow(createFormLabel("Login Attempts"), attemptsCombo)
        );
        securityForm.setPadding(new Insets(14, 0, 0, 0));
        securityCard.getChildren().addAll(securityHeader, securityForm);

        content.getChildren().addAll(heading, accountCard, appearanceCard, securityCard);
        return content;
    }

    private VBox createSettingsCard() {
        VBox box = new VBox(14);
        box.setFillWidth(true);
        box.setPadding(new Insets(20));
        box.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-width: 1.2;" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0," + (isLightMode ? "0.06" : "0.6") + "), 24, 0, 0, 10);"
        );
        return box;
    }

    private Label createCardLabel(String text, int size, FontWeight weight) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, weight, size));
        label.setTextFill(Color.web(textPrimary));
        return label;
    }

    private Label createSecondaryLabel(String text, int size) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.MEDIUM, size));
        label.setTextFill(Color.web(textSecondary));
        return label;
    }

    private StackPane createIconBox(String type, String color, String background) {
        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(color));
        icon.setStrokeWidth(2);

        StackPane box = new StackPane(icon);
        box.setMinSize(32, 32);
        box.setPrefSize(32, 32);
        box.setMaxSize(32, 32);
        box.setStyle("-fx-background-color: " + background + "; -fx-border-color: " + color + "55; -fx-border-radius: 8; -fx-background-radius: 8;");
        return box;
    }

    private Button createOutlineButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(34);
        button.setPadding(new Insets(0, 15, 0, 15));

        String baseStyle = "-fx-background-color: " + (isLightMode ? "#F8FAFC" : "rgba(255, 255, 255, 0.05)") + "; -fx-border-color: " + cardBorder + "; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: " + textPrimary + "; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: " + (isLightMode ? "#EEF2FF" : "rgba(255, 255, 255, 0.1)") + "; -fx-border-color: " + BLUE + "; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: " + (isLightMode ? BLUE : "#FFFFFF") + "; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-cursor: hand;";

        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        return button;
    }

    private Button createThemeButton(String icon, String text, boolean selected) {
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(FONT, FontWeight.BOLD, 14));

        Label textLabel = new Label(text);
        textLabel.setFont(Font.font(FONT, selected ? FontWeight.BOLD : FontWeight.SEMI_BOLD, 12));

        HBox content = new HBox(8, iconLabel, textLabel);
        content.setAlignment(Pos.CENTER);

        Button button = new Button();
        button.setGraphic(content);
        button.setPrefHeight(34); 
        button.setMinHeight(34);
        button.setPadding(new Insets(0, 14, 0, 14));

        applyThemeButtonStyle(button, iconLabel, textLabel, selected);
        return button;
    }

    private void applyThemeButtonStyle(Button button, Label iconLabel, Label textLabel, boolean selected) {
        String baseStyle;
        String hoverStyle;

        if (selected) {
            baseStyle = "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-width: 1;";
            hoverStyle = "-fx-background-color: linear-gradient(to right, #1D4ED8, #0284C7); -fx-border-color: rgba(96, 165, 250, 0.8); -fx-border-width: 1;";
            iconLabel.setTextFill(Color.WHITE);
            textLabel.setTextFill(Color.WHITE);
        } else {
            baseStyle = "-fx-background-color: " + (isLightMode ? "#F1F5F9" : "rgba(10, 18, 33, 0.85)") + "; -fx-border-color: " + (isLightMode ? "#CBD5E1" : "rgba(255, 255, 255, 0.08)") + "; -fx-border-width: 1;";
            hoverStyle = "-fx-background-color: " + (isLightMode ? "#E2E8F0" : "rgba(23, 37, 64, 0.95)") + "; -fx-border-color: " + BLUE + "; -fx-border-width: 1;";
            iconLabel.setTextFill(Color.web(BLUE));
            textLabel.setTextFill(Color.web(textSecondary));
        }

        String common = " -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;";
        button.setStyle(baseStyle + common);

        button.setOnMouseEntered(e -> {
            button.setStyle(hoverStyle + common);
            if (!selected) {
                iconLabel.setTextFill(Color.web(BLUE));
                textLabel.setTextFill(Color.web(textPrimary));
            }
        });

        button.setOnMouseExited(e -> {
            button.setStyle(baseStyle + common);
            if (!selected) {
                iconLabel.setTextFill(Color.web(BLUE));
                textLabel.setTextFill(Color.web(textSecondary));
            }
        });
    }

    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setPrefWidth(210); label.setMinWidth(210);
        label.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        label.setTextFill(Color.web(textPrimary));
        return label;
    }

    private HBox createFormRow(Label label, javafx.scene.Node control) {
        HBox row = new HBox(10, label, control);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setPrefWidth(345); comboBox.setPrefHeight(34);
        comboBox.setStyle("-fx-background-color: " + (isLightMode ? "#F8FAFC" : "rgba(10, 18, 33, 0.85)") + "; -fx-border-color: " + cardBorder + "; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: " + textPrimary + ";");
    }

    private String createToggleStyle(boolean enabled) {
        return "-fx-background-color: " + (enabled ? BLUE : (isLightMode ? "#E2E8F0" : "rgba(255, 255, 255, 0.08)")) + "; -fx-background-radius: 20; -fx-border-radius: 20; -fx-text-fill: transparent; -fx-padding: 0; -fx-cursor: hand;";
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
            case "appearance": icon.setContent("M12 3 A9 9 0 1 0 12 21 A9 9 0 0 0 12 3 Z M12 3 V21"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }
}