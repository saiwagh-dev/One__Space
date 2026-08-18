package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.InputStream;

public class AdminSettings {
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String SIDEBAR_BG = "#1E2A3A";
    private static final String SIDEBAR_DARK = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";
    private static final String MAIN_BG = "#31435B";
    private static final String CARD_BG = "#DDE8F8";
    private static final String CARD_BORDER = "#C3D6EC";
    private static final String CARD_TITLE = "#0B1220";
    private static final String CARD_SECONDARY = "#1E293B";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";
    private static final String BLUE_LIGHT = "#BFDBFE";
    private static final String GREEN = "#059669";
    private static final String GREEN_LIGHT = "#A7F3D0";
    private static final String PURPLE = "#7C3AED";
    private static final String PURPLE_LIGHT = "#EDE9FE";

    public AdminSettings() {}

    public Scene getAdminSettingsScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createMainContent());
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

        return new Scene(root, 1200, 750);
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

    private VBox createMainContent() {
        VBox content = new VBox(24);
        content.setFillWidth(true);
        content.setPadding(new Insets(28, 32, 40, 32));
        content.setStyle("-fx-background-color: " + MAIN_BG + ";");

        Label title = new Label("Settings");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 26));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Manage your account, preferences, indexing controls, and security across OneSpace.");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox heading = new VBox(6, title, subtitle);

        VBox accountCard = createSettingsCard();
        VBox accountInfo = new VBox(3, createCardLabel("Admin", 16, FontWeight.BOLD), createSecondaryLabel("admin@onespace.com", 13));
        HBox accountLeft = new HBox(14, createIconBox("users", BLUE, BLUE_LIGHT), accountInfo);
        accountLeft.setAlignment(Pos.CENTER_LEFT);

        Region accountSpacer = new Region();
        HBox.setHgrow(accountSpacer, Priority.ALWAYS);

        Button editProfile = createOutlineButton("Edit Profile");
        editProfile.setOnAction(e -> showInfo("Edit Profile", "Profile editing can be connected to your admin profile service."));

        HBox accountButtons = new HBox(10, editProfile);
        accountButtons.setAlignment(Pos.CENTER_RIGHT);

        HBox accountRow = new HBox(accountLeft, accountSpacer, accountButtons);
        accountRow.setAlignment(Pos.CENTER_LEFT);
        accountCard.getChildren().add(accountRow);

        VBox appearanceCard = createSettingsCard();
        VBox appearanceTitle = new VBox(3, createCardLabel("Appearance", 16, FontWeight.BOLD), createSecondaryLabel("Customize how OneSpace looks and adapts.", 13));
        HBox appearanceLeft = new HBox(14, createIconBox("appearance", PURPLE, PURPLE_LIGHT), appearanceTitle);
        appearanceLeft.setAlignment(Pos.CENTER_LEFT);

        Button lightButton = createThemeButton("☼", "Light", true);
        Button darkButton = createThemeButton("☾", "Dark", false);
        Button systemButton = createThemeButton("▣", "System", false);
        lightButton.setOnAction(e -> selectThemeButton(lightButton, darkButton, systemButton));
        darkButton.setOnAction(e -> selectThemeButton(darkButton, lightButton, systemButton));
        systemButton.setOnAction(e -> selectThemeButton(systemButton, lightButton, darkButton));

        HBox themeButtons = new HBox(12, lightButton, darkButton, systemButton);
        themeButtons.setAlignment(Pos.CENTER_LEFT);
        VBox themeBox = new VBox(7, createCardLabel("Theme", 13, FontWeight.BOLD), themeButtons);
        themeBox.setAlignment(Pos.CENTER_LEFT);

        Region appearanceSpacer = new Region();
        HBox.setHgrow(appearanceSpacer, Priority.ALWAYS);

        HBox appearanceRow = new HBox(appearanceLeft, appearanceSpacer, themeBox);
        appearanceRow.setAlignment(Pos.CENTER_LEFT);
        appearanceCard.getChildren().add(appearanceRow);

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

    private Label createCardLabel(String text, int size, FontWeight weight) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, weight, size));
        label.setTextFill(Color.web(CARD_TITLE));
        label.setStyle("-fx-text-fill: " + CARD_TITLE + " !important;");
        return label;
    }

    private Label createSecondaryLabel(String text, int size) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.MEDIUM, size));
        label.setTextFill(Color.web(CARD_SECONDARY));
        label.setStyle("-fx-text-fill: " + CARD_SECONDARY + " !important;");
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
        box.setStyle("-fx-background-color: " + background + "; -fx-background-radius: 8;");
        return box;
    }

    private Button createOutlineButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(34);
        button.setPadding(new Insets(0, 15, 0, 15));

        String baseStyle = "-fx-background-color: #FFFFFF; -fx-border-color: #94A3B8; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7; -fx-text-fill: #0F172A; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: #F1F5F9; -fx-border-color: #64748B; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7; -fx-text-fill: #020617; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-cursor: hand;";

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
            baseStyle = "-fx-background-color: #2563EB; -fx-border-color: #3B82F6; -fx-border-width: 1.5;";
            hoverStyle = "-fx-background-color: #1D4ED8; -fx-border-color: #60A5FA; -fx-border-width: 1.5;";
            iconLabel.setTextFill(Color.WHITE);
            iconLabel.setStyle("-fx-text-fill: #FFFFFF !important;");
            textLabel.setTextFill(Color.WHITE);
            textLabel.setStyle("-fx-text-fill: #FFFFFF !important; -fx-font-weight: bold;");
        } else {
            baseStyle = "-fx-background-color: #1E2A3A; -fx-border-color: #475569; -fx-border-width: 1;";
            hoverStyle = "-fx-background-color: #334155; -fx-border-color: #94A3B8; -fx-border-width: 1;";
            iconLabel.setTextFill(Color.web("#60A5FA"));
            iconLabel.setStyle("-fx-text-fill: #60A5FA !important;");
            textLabel.setTextFill(Color.WHITE);
            textLabel.setStyle("-fx-text-fill: #FFFFFF !important;");
        }

        String common = " -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;";

        button.setStyle(baseStyle + common);

        button.setOnMouseEntered(e -> {
            button.setStyle(hoverStyle + common);
            if (!selected) {
                iconLabel.setTextFill(Color.web("#93C5FD"));
                iconLabel.setStyle("-fx-text-fill: #93C5FD !important;");
                textLabel.setTextFill(Color.WHITE);
                textLabel.setStyle("-fx-text-fill: #FFFFFF !important;");
            }
        });

        button.setOnMouseExited(e -> {
            button.setStyle(baseStyle + common);
            if (!selected) {
                iconLabel.setTextFill(Color.web("#60A5FA"));
                iconLabel.setStyle("-fx-text-fill: #60A5FA !important;");
                textLabel.setTextFill(Color.WHITE);
                textLabel.setStyle("-fx-text-fill: #FFFFFF !important;");
            }
        });
    }

    private void selectThemeButton(Button selected, Button other1, Button other2) {
        styleSelectedThemeButton(selected);
        styleUnselectedThemeButton(other1);
        styleUnselectedThemeButton(other2);
    }

    private void styleSelectedThemeButton(Button button) {
        if (button.getGraphic() instanceof HBox) {
            HBox box = (HBox) button.getGraphic();
            Label iconLabel = (Label) box.getChildren().get(0);
            Label textLabel = (Label) box.getChildren().get(1);
            applyThemeButtonStyle(button, iconLabel, textLabel, true);
        }
    }

    private void styleUnselectedThemeButton(Button button) {
        if (button.getGraphic() instanceof HBox) {
            HBox box = (HBox) button.getGraphic();
            Label iconLabel = (Label) box.getChildren().get(0);
            Label textLabel = (Label) box.getChildren().get(1);
            applyThemeButtonStyle(button, iconLabel, textLabel, false);
        }
    }

    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setPrefWidth(210); label.setMinWidth(210);
        label.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        label.setTextFill(Color.web(CARD_TITLE));
        label.setStyle("-fx-text-fill: " + CARD_TITLE + " !important; -fx-font-weight: 600;");
        return label;
    }

    private HBox createFormRow(Label label, javafx.scene.Node control) {
        HBox row = new HBox(10, label, control);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setPrefWidth(345); comboBox.setPrefHeight(34);
        comboBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #94A3B8; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #0B1220;");
    }

    private String createToggleStyle(boolean enabled) {
        return "-fx-background-color: " + (enabled ? PURPLE : "#94A3B8") + "; -fx-background-radius: 20; -fx-border-radius: 20; -fx-text-fill: transparent; -fx-padding: 0; -fx-cursor: hand;";
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(message);
        alert.showAndWait();
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
            case "appearance": icon.setContent("M12 3 A9 9 0 1 0 12 21 A9 9 0 0 0 12 3 Z M12 3 V21"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }
}