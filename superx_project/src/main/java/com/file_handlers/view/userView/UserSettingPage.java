package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserSettingPage {

    // Style Constants & Theme/Accent State
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static String currentTheme = "Dark"; // Default theme state
    private static String primaryBlue = "#2563EB"; // Default accent color state

    private String BG_SIDEBAR;
    private String BG_SIDEBAR_CARD;
    private String SIDEBAR_BORDER;
    private String BG_CENTER_CANVAS;
    private String BG_CARD;
    private String BG_CARD_INNER;
    private String BORDER_CARD;
    private String TEXT_DARK;
    private String TEXT_MUTED_DARK;
    private String TEXT_LIGHT;
    private String TEXT_MUTED_LIGHT;

    private void loadThemeColors() {
        if ("Light".equals(currentTheme)) {
            BG_SIDEBAR = "#F1F5F9";
            BG_SIDEBAR_CARD = "#E2E8F0";
            SIDEBAR_BORDER = "#CBD5E1";
            BG_CENTER_CANVAS = "#F8FAFC";
            BG_CARD = "#FFFFFF";
            BG_CARD_INNER = "#F1F5F9";
            BORDER_CARD = "#E2E8F0";
            TEXT_DARK = "#0F172A";
            TEXT_MUTED_DARK = "#64748B";
            TEXT_LIGHT = "#0F172A";
            TEXT_MUTED_LIGHT = "#64748B";
        } else { // Dark / System
            BG_SIDEBAR = "#1E2A3A";
            BG_SIDEBAR_CARD = "#141D29";
            SIDEBAR_BORDER = "#2D3D52";
            BG_CENTER_CANVAS = "#31435B";
            BG_CARD = "#DDE8F8";
            BG_CARD_INNER = "#CADDF2";
            BORDER_CARD = "#C3D6EC";
            TEXT_DARK = "#0F172A";
            TEXT_MUTED_DARK = "#334155";
            TEXT_LIGHT = "#FFFFFF";
            TEXT_MUTED_LIGHT = "#94A3B8";
        }
    }

    public Scene getSettingPageScene() {
        loadThemeColors();

        // =========================================================
        // SIDEBAR
        // =========================================================
        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", true);
        Button logoutSidebarBtn = createSidebarButton("🚪", "Logout", false);

        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        collabBtn.setOnAction(e -> LandingPage.showCollaborationPage());
        aiBtn.setOnAction(e -> LandingPage.showAiAssistantPage());
        recentBtn.setOnAction(e -> LandingPage.showRecentPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());
        settingsBtn.setOnAction(e -> LandingPage.showSettingPage());
        
        // Sign out action from sidebar logout button
        logoutSidebarBtn.setOnAction(e -> performSignOut(logoutSidebarBtn));

        VBox navList = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

        // Storage Box (Bottom Sidebar)
        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        storageTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox storageValGroup = new HBox(storageVal, new Region(), storagePercent);
        HBox.setHgrow(storageValGroup.getChildren().get(1), Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: " + primaryBlue + "; -fx-control-inner-background: #0E1520;");

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexedPage());

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox settingsAndLogoutBox = new VBox(4, settingsBtn, logoutSidebarBtn);
        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsAndLogoutBox, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setMaxWidth(230);
        sidebar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        // =========================================================
        // TOP BAR
        // =========================================================
        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        TextField searchField = new TextField();
        searchField.setPromptText("Search settings...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 6; -fx-background-radius: 4;");

        HBox searchContainer = new HBox(8, searchIcon, searchField, keyShortcut);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0, 12, 0, 14));
        searchContainer.setPrefWidth(420);
        searchContainer.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color: " + primaryBlue + "; -fx-background-radius: 50%; -fx-text-fill: " + TEXT_LIGHT + "; -fx-font-weight: bold; -fx-font-size: 12px;");

        Label userName = new Label("Aarav Verma");
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label dropDown = new Label("⌄");
        dropDown.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox profileBox = new HBox(10, bellBtn, avatar, userName, dropDown);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, searchContainer, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        // =========================================================
        // PAGE HEADER
        // =========================================================
        Label pageTitle = new Label("Settings");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        pageTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label pageDescription = new Label("Manage your account, preferences, indexing controls, and security across OneSpace.");
        pageDescription.setFont(Font.font(FONT, 13));
        pageDescription.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        VBox titleBox = new VBox(4, pageTitle, pageDescription);

        // =========================================================
        // SECTION 1: USER PROFILE CARD
        // =========================================================
        Label userAvatarBig = new Label("AV");
        userAvatarBig.setPrefSize(48, 48);
        userAvatarBig.setAlignment(Pos.CENTER);
        userAvatarBig.setStyle("-fx-background-color: " + primaryBlue + "; -fx-background-radius: 50%; -fx-text-fill: " + TEXT_LIGHT + "; -fx-font-weight: bold; -fx-font-size: 16px;");

        Label accountName = new Label("Aarav Verma");
        accountName.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        accountName.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label accountEmail = new Label("aarav.verma@onespace.app");
        accountEmail.setFont(Font.font(FONT, 12));
        accountEmail.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox accountDetails = new VBox(2, accountName, accountEmail);

        Button editProfileBtn = createActionButton("Edit Profile");
        editProfileBtn.setOnAction(e -> openEditProfileWindow());

        Button switchAccountBtn = createActionButton("Switch Account");
        switchAccountBtn.setOnAction(e -> openSwitchAccountWindow());

        HBox profileCardActions = new HBox(8, editProfileBtn, switchAccountBtn);

        HBox profileCard = new HBox(16, userAvatarBig, accountDetails, new Region(), profileCardActions);
        HBox.setHgrow(profileCard.getChildren().get(2), Priority.ALWAYS);
        profileCard.setAlignment(Pos.CENTER_LEFT);
        profileCard.setPadding(new Insets(14, 20, 14, 20));

        // =========================================================
        // SECTION 2: APPEARANCE & THEME
        // =========================================================
        Label appearanceIcon = createSettingIcon("🎨");
        Label appearanceTitle = createSectionTitle("Appearance");
        Label appearanceDesc = createSectionDescription("Customize how OneSpace looks and adapts.");

        VBox appearanceText = new VBox(2, appearanceTitle, appearanceDesc);
        HBox appearanceLeft = new HBox(12, appearanceIcon, appearanceText);
        appearanceLeft.setAlignment(Pos.TOP_LEFT);

        Label themeTitle = createSectionTitle("Theme");
        Button lightTheme = createThemeButton("☀️", "Light", "Light".equals(currentTheme));
        Button darkTheme = createThemeButton("🌙", "Dark", "Dark".equals(currentTheme));
        Button systemTheme = createThemeButton("💻", "System", "System".equals(currentTheme));

        lightTheme.setOnAction(e -> {
            currentTheme = "Light";
            Stage stage = (Stage) lightTheme.getScene().getWindow();
            stage.setScene(getSettingPageScene());
        });

        darkTheme.setOnAction(e -> {
            currentTheme = "Dark";
            Stage stage = (Stage) darkTheme.getScene().getWindow();
            stage.setScene(getSettingPageScene());
        });

        systemTheme.setOnAction(e -> {
            currentTheme = "System";
            Stage stage = (Stage) systemTheme.getScene().getWindow();
            stage.setScene(getSettingPageScene());
        });

        HBox themeCards = new HBox(8, lightTheme, darkTheme, systemTheme);
        VBox themeBox = new VBox(8, themeTitle, themeCards);

        HBox appearanceSection = new HBox(30, appearanceLeft, new Region(), themeBox);
        HBox.setHgrow(appearanceSection.getChildren().get(1), Priority.ALWAYS);
        appearanceSection.setPadding(new Insets(14, 20, 14, 20));
        appearanceSection.setAlignment(Pos.CENTER_LEFT);

        // Accent Color Row with interactive palette selection
        HBox accentRow = createSettingRow("✨", "Accent color", "Choose the accent color palette used across indicators.");
        HBox accentColors = new HBox(10,
                createColorCircle("#2563EB", primaryBlue.equals("#2563EB")),
                createColorCircle("#0284C7", primaryBlue.equals("#0284C7")),
                createColorCircle("#059669", primaryBlue.equals("#059669")),
                createColorCircle("#7C3AED", primaryBlue.equals("#7C3AED")),
                createColorCircle("#D97706", primaryBlue.equals("#D97706")),
                createColorCircle("#DC2626", primaryBlue.equals("#DC2626"))
        );
        accentColors.setAlignment(Pos.CENTER_RIGHT);
        accentRow.getChildren().add(accentColors);

        // =========================================================
        // SECTION 3: STORAGE & INDEXING CONTROLS
        // =========================================================
        HBox indexingRow = createSettingRow("⚡", "Local AI Indexing", "Rescan local directories or clear cached search indices.");
        Button rescanBtn = createActionButton("Rescan All");
        Button clearIndexBtn = createActionButton("Clear Cache");
        HBox indexingActions = new HBox(8, rescanBtn, clearIndexBtn);
        indexingRow.getChildren().add(indexingActions);

        // =========================================================
        // SECTION 4: SECURITY & LOG OUT ROW
        // =========================================================
        HBox securityRow = createSettingRow("🛡", "Security & Password", "Update credentials and manage offline encryption keys.");
        Button changePasswordBtn = createActionButton("Change Password");
        changePasswordBtn.setOnAction(e -> openChangePasswordWindow());
        securityRow.getChildren().add(changePasswordBtn);

        // Logout Row
        HBox logoutRow = createSettingRow("🚪", "Account Sign Out", "Safely sign out of your local OneSpace session.");
        Button logoutBtn = new Button("Sign Out");
        logoutBtn.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        logoutBtn.setStyle("-fx-background-color: #FEF2F2; -fx-border-color: #FCA5A5; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #DC2626; -fx-padding: 0 16; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> performSignOut(logoutBtn));
        logoutRow.getChildren().add(logoutBtn);

        // =========================================================
        // MAIN CARD ASSEMBLY
        // =========================================================
        VBox settingsCard = new VBox(
                profileCard, createSeparator(),
                appearanceSection, createSeparator(),
                accentRow, createSeparator(),
                indexingRow, createSeparator(),
                securityRow, createSeparator(),
                logoutRow
        );
        settingsCard.setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 16; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 16, 0, 0, 6);");

        VBox mainContent = new VBox(22, titleBox, settingsCard);
        mainContent.setPadding(new Insets(24, 28, 28, 28));
        mainContent.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        VBox centerContent = new VBox(topBar, mainContent);
        centerContent.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");
        VBox.setVgrow(centerContent, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_SIDEBAR + ";");
        root.setLeft(sidebar);
        root.setCenter(centerContent);

        return new Scene(root, 1200, 750);
    }

    // =========================================================
    // FUNCTIONALITY WINDOWS & ACTIONS
    // =========================================================
    private void performSignOut(Button triggerButton) {
        // Sign out implementation redirecting to the main Landing/Login screen
        LandingPage.showLandingPage();
    }

    private void openEditProfileWindow() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Profile - Aarav Verma");

        Label title = new Label("Edit User Profile");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        TextField nameField = new TextField("Aarav Verma");
        TextField emailField = new TextField("aarav.verma@onespace.app");

        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle("-fx-background-color: " + primaryBlue + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        saveBtn.setOnAction(e -> stage.close());

        VBox layout = new VBox(12, title, new Label("Name:"), nameField, new Label("Email:"), emailField, saveBtn);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: " + BG_CARD + ";");

        stage.setScene(new Scene(layout, 350, 300));
        stage.showAndWait();
    }

    private void openChangePasswordWindow() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Change Password");

        Label title = new Label("Change Password");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        PasswordField currentPass = new PasswordField();
        PasswordField newPass = new PasswordField();
        PasswordField confirmPass = new PasswordField();

        Button updateBtn = new Button("Update Password");
        updateBtn.setStyle("-fx-background-color: " + primaryBlue + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        updateBtn.setOnAction(e -> stage.close());

        VBox layout = new VBox(12, title, new Label("Current Password:"), currentPass, new Label("New Password:"), newPass, new Label("Confirm Password:"), confirmPass, updateBtn);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: " + BG_CARD + ";");

        stage.setScene(new Scene(layout, 350, 350));
        stage.showAndWait();
    }

    private void openSwitchAccountWindow() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Switch Account");

        Label title = new Label("Switch Account");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        TextField accountField = new TextField();
        accountField.setPromptText("Enter email or username...");

        Button switchBtn = new Button("Sign In to Account");
        switchBtn.setStyle("-fx-background-color: " + primaryBlue + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        switchBtn.setOnAction(e -> stage.close());

        VBox layout = new VBox(12, title, new Label("Select or enter account:"), accountField, switchBtn);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: " + BG_CARD + ";");

        stage.setScene(new Scene(layout, 350, 220));
        stage.showAndWait();
    }

    // =========================================================
    // HELPER METHODS
    // =========================================================
    private StackPane createOneSpaceLogo() {
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

    private Button createSidebarButton(String icon, String label, boolean isActive) {
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(FONT, 14));

        Label textLbl = new Label(label);
        textLbl.setFont(Font.font(FONT, isActive ? FontWeight.BOLD : FontWeight.MEDIUM, 13));

        HBox content = new HBox(12, iconLbl, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 12, 0, 12));

        if (isActive) {
            btn.setStyle("-fx-background-color: " + primaryBlue + "; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");
            textLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
            textLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #26354A; -fx-background-radius: 8; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;"));
        }

        return btn;
    }

    private Label createSettingIcon(String symbol) {
        Label icon = new Label(symbol);
        icon.setFont(Font.font(14));
        icon.setPrefSize(34, 34);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color: " + BG_CARD_INNER + "; -fx-background-radius: 8; -fx-text-fill: " + primaryBlue + ";");
        return icon;
    }

    private Label createSectionTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        label.setStyle("-fx-text-fill: " + TEXT_DARK + ";");
        return label;
    }

    private Label createSectionDescription(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, 12));
        label.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
        return label;
    }

    private HBox createSettingRow(String iconText, String titleText, String descriptionText) {
        Label icon = createSettingIcon(iconText);
        Label title = createSectionTitle(titleText);
        Label description = createSectionDescription(descriptionText);

        VBox textBox = new VBox(1, title, description);
        HBox row = new HBox(12, icon, textBox);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 20, 12, 20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        row.getChildren().add(spacer);

        return row;
    }

    private Button createActionButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font(FONT, FontWeight.MEDIUM, 12));
        btn.setStyle("-fx-background-color: " + BG_CARD_INNER + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: " + TEXT_DARK + "; -fx-pref-height: 32; -fx-padding: 0 12; -fx-cursor: hand;");
        return btn;
    }

    private Button createThemeButton(String iconText, String themeName, boolean selected) {
        Label icon = new Label(iconText);
        icon.setFont(Font.font(14));

        Label name = new Label(themeName);
        name.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));

        VBox content = new VBox(2, icon, name);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("", content);
        button.setPrefSize(90, 52);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(8, 10, 8, 10));

        if (selected) {
            button.setStyle("-fx-background-color: " + BG_CARD_INNER + "; -fx-border-color: " + primaryBlue + "; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            icon.setStyle("-fx-text-fill: " + primaryBlue + ";");
            name.setStyle("-fx-text-fill: " + primaryBlue + ";");
        } else {
            button.setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            icon.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
            name.setStyle("-fx-text-fill: " + TEXT_DARK + ";");
        }

        return button;
    }

    private Circle createColorCircle(String hexColor, boolean selected) {
        Circle circle = new Circle(11);
        circle.setFill(Color.web(hexColor));
        circle.setStyle("-fx-cursor: hand;");
        circle.setStroke(selected ? Color.web(TEXT_DARK) : Color.TRANSPARENT);
        circle.setStrokeWidth(selected ? 2.5 : 0);

        // Make accent color interactive on click
        circle.setOnMouseClicked(e -> {
            primaryBlue = hexColor;
            Stage stage = (Stage) circle.getScene().getWindow();
            stage.setScene(getSettingPageScene());
        });

        return circle;
    }

    private Separator createSeparator() {
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + BORDER_CARD + "; -fx-opacity: 0.5;");
        return sep;
    }
}