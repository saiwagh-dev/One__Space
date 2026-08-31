package com.file_handlers.view.userView;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.file_handlers.controller.AuthController;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

import java.util.prefs.Preferences;

public class UserSettingPage {

    // Typography
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final Preferences PREFS = Preferences.userRoot().node("onespace");
    private static final AuthController AUTH = new AuthController();

    private static String currentTheme = PREFS.get("theme", "Dark");
    private static String primaryBlue = PREFS.get("accentColor", "#2563EB");

    // 1. Sidebar & Top Bar Tones
    private static final String SIDEBAR_BG = "#070C16";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";

    // 2. Center Workspace Canvas: Atmospheric Dark Radial Glow
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards & Container Colors
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BG_INNER = "linear-gradient(to bottom right, rgba(13, 22, 38, 0.9), rgba(8, 14, 26, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";
    private static final String INPUT_BG = "rgba(13, 22, 38, 0.85)";
    private static final String INPUT_BORDER = "rgba(255, 255, 255, 0.1)";

    // 4. Vibrant Typography & Accent Highlights
    private static final String WHITE = "#FFFFFF";
    private static final String BLUE = "#2563EB";
    private static final String PURPLE = "#A855F7";
    private static final String LIGHT_SECONDARY = "#94A3B8";

    private Button activeSidebarBtn;
    private ProgressBar sidebarStorageProgress;

    public Scene getSettingPageScene() {
        UserSession session = UserSession.getInstance();

        String displayName = session != null && session.getDisplayName() != null && !session.getDisplayName().isBlank()
                ? session.getDisplayName() : "User";
        String email = session != null && session.getEmail() != null && !session.getEmail().isBlank()
                ? session.getEmail() : "No email";

        String initials = getInitials(displayName);

        VBox sidebar = createSidebar();

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button bellBtn = new Button();
        bellBtn.setGraphic(bellIcon);
        bellBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());
        applyHoverAnimation(bellBtn, 1.08, 0);

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34); avatar.setMinSize(34, 34); avatar.setMaxSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B5CF6, #A855F7); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(168,85,247,0.5), 10, 0, 0, 2); -fx-cursor: hand;");
        applyHoverAnimation(avatar, 1.15, 0);

        Label userName = new Label(getFirstName(displayName));
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill: " + PURPLE + ";");

        Label dropDown = new Label("⌄");
        dropDown.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        dropDown.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        HBox profileOption = new HBox(8, avatar, userName, dropDown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(4, 12, 4, 6));
        profileOption.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");

        // Custom Dropdown Menu
        Popup userDropdownPopup = new Popup();
        userDropdownPopup.setAutoHide(true);

        Button profileDropdownBtn = new Button("👥   Profile");
        profileDropdownBtn.setMaxWidth(Double.MAX_VALUE);
        profileDropdownBtn.setAlignment(Pos.CENTER_LEFT);
        profileDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #F59E0B;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        );
        profileDropdownBtn.setOnMouseEntered(e -> profileDropdownBtn.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: #F59E0B;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 6;"
        ));
        profileDropdownBtn.setOnMouseExited(e -> profileDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #F59E0B;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        ));
        profileDropdownBtn.setOnAction(e -> {
            userDropdownPopup.hide();
            Platform.runLater(LandingPage::showUserProfilePage);
        });

        Button settingsDropdownBtn = new Button("⚙   Settings");
        settingsDropdownBtn.setMaxWidth(Double.MAX_VALUE);
        settingsDropdownBtn.setAlignment(Pos.CENTER_LEFT);
        settingsDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #38BDF8;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        );
        settingsDropdownBtn.setOnMouseEntered(e -> settingsDropdownBtn.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: #38BDF8;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 6;"
        ));
        settingsDropdownBtn.setOnMouseExited(e -> settingsDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #38BDF8;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        ));
        settingsDropdownBtn.setOnAction(e -> {
            userDropdownPopup.hide();
            Platform.runLater(LandingPage::showSettingPage);
        });

        Separator dropdownSeparator = new Separator();
        dropdownSeparator.setStyle("-fx-background-color: #1E293B; -fx-padding: 4 0;");

        Button logoutDropdownBtn = new Button("↳   Logout");
        logoutDropdownBtn.setMaxWidth(Double.MAX_VALUE);
        logoutDropdownBtn.setAlignment(Pos.CENTER_LEFT);
        logoutDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #F87171;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        );
        logoutDropdownBtn.setOnMouseEntered(e -> logoutDropdownBtn.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: #F87171;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 6;"
        ));
        logoutDropdownBtn.setOnMouseExited(e -> logoutDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #F87171;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        ));
        logoutDropdownBtn.setOnAction(e -> {
            userDropdownPopup.hide();
            UserSession.clearSession();
            Platform.runLater(LandingPage::showUserLoginPage);
        });

        VBox dropdownContainer = new VBox(4, profileDropdownBtn, settingsDropdownBtn, dropdownSeparator, logoutDropdownBtn);
        dropdownContainer.setPadding(new Insets(8));
        dropdownContainer.setPrefWidth(180);
        dropdownContainer.setStyle(
                "-fx-background-color: #0A121E;" +
                "-fx-border-color: #1E2D42;" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 12px;" +
                "-fx-background-radius: 12px;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 16, 0, 0, 8);"
        );

        userDropdownPopup.getContent().add(dropdownContainer);

        profileOption.setOnMouseClicked(e -> {
            if (userDropdownPopup.isShowing()) {
                userDropdownPopup.hide();
            } else {
                javafx.geometry.Point2D point = profileOption.localToScreen(0, profileOption.getHeight() + 6);
                userDropdownPopup.show(profileOption, point.getX(), point.getY());
            }
        });

        HBox profileBox = new HBox(10, bellBtn, profileOption);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(0), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        Label pageTitle = new Label("Settings");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 26));
        pageTitle.setStyle("-fx-text-fill: " + WHITE + ";");

        Label pageDescription = new Label("Manage your account, preferences, indexing controls, and security across OneSpace.");
        pageDescription.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        pageDescription.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox titleBox = new VBox(4, pageTitle, pageDescription);

        Label userAvatarBig = new Label(initials);
        userAvatarBig.setPrefSize(48, 48);
        userAvatarBig.setAlignment(Pos.CENTER);
        userAvatarBig.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        userAvatarBig.setTextFill(Color.WHITE);
        userAvatarBig.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B5CF6, #A855F7); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(168,85,247,0.5), 10, 0, 0, 2); -fx-cursor: hand;");
        applyHoverAnimation(userAvatarBig, 1.15, 0);

        Label accountName = new Label(displayName);
        accountName.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        accountName.setStyle("-fx-text-fill: " + PURPLE + ";");

        Label accountEmail = new Label(email);
        accountEmail.setFont(Font.font(FONT, 12));
        accountEmail.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox accountDetails = new VBox(2, accountName, accountEmail);

        Button editProfileBtn = createActionButton("Edit Profile");
        editProfileBtn.setOnAction(e -> LandingPage.showUserProfilePage());

        Button switchAccountBtn = createActionButton("Switch Account");
        switchAccountBtn.setOnAction(e -> showAnimatedSwitchAccountDialog());

        HBox profileActions = new HBox(8, editProfileBtn, switchAccountBtn);
        HBox profileCard = new HBox(16, userAvatarBig, accountDetails, new Region(), profileActions);
        HBox.setHgrow(profileCard.getChildren().get(2), Priority.ALWAYS);
        profileCard.setAlignment(Pos.CENTER_LEFT);
        profileCard.setPadding(new Insets(14, 20, 14, 20));
        applyRowHover(profileCard);

        HBox appearanceLeft = createSettingRowLeft("theme", "Appearance", "Customize how OneSpace looks and adapts.");

        Label themeTitle = createSectionTitle("Theme");

        Button lightTheme = createThemeButton("sun", "Light", "Light".equals(currentTheme));
        Button darkTheme = createThemeButton("moon", "Dark", "Dark".equals(currentTheme));
        Button systemTheme = createThemeButton("monitor", "System", "System".equals(currentTheme));

        lightTheme.setOnAction(e -> setTheme("Light"));
        darkTheme.setOnAction(e -> setTheme("Dark"));
        systemTheme.setOnAction(e -> setTheme("System"));

        HBox themeCards = new HBox(8, lightTheme, darkTheme, systemTheme);
        VBox themeBox = new VBox(8, themeTitle, themeCards);

        HBox appearanceSection = new HBox(30, appearanceLeft, new Region(), themeBox);
        HBox.setHgrow(appearanceSection.getChildren().get(1), Priority.ALWAYS);
        appearanceSection.setPadding(new Insets(14, 20, 14, 20));
        appearanceSection.setAlignment(Pos.CENTER_LEFT);
        applyRowHover(appearanceSection);

        HBox accentRow = createSettingRow("sparkles", "Accent color", "Choose the accent color palette used across indicators.");

        HBox accentColors = new HBox(10,
                createColorCircle("#2563EB"),
                createColorCircle("#0284C7"),
                createColorCircle("#059669"),
                createColorCircle("#7C3AED"),
                createColorCircle("#D97706"),
                createColorCircle("#DC2626")
        );

        accentColors.setAlignment(Pos.CENTER_RIGHT);
        accentRow.getChildren().add(accentColors);
        applyRowHover(accentRow);

        HBox indexingRow = createSettingRow("ai", "Local AI Indexing", "Rescan local directories or clear cached search indices.");

        Button rescanBtn = createActionButton("Rescan All");
        Button clearIndexBtn = createActionButton("Clear Cache");

        rescanBtn.setOnAction(e -> showInfo("Rescan", "File rescan functionality will be connected to FileProcessingService."));
        clearIndexBtn.setOnAction(e -> showInfo("Clear Cache", "Search cache clearing will be connected when the indexing cache is implemented."));

        indexingRow.getChildren().add(new HBox(8, rescanBtn, clearIndexBtn));
        applyRowHover(indexingRow);

        HBox securityRow = createSettingRow("security", "Security & Password", "Update your Firebase account password.");

        Button changePasswordBtn = createActionButton("Change Password");
        changePasswordBtn.setOnAction(e -> openChangePasswordWindow());

        securityRow.getChildren().add(changePasswordBtn);
        applyRowHover(securityRow);

        VBox settingsCard = new VBox(
                profileCard, createSeparator(),
                appearanceSection, createSeparator(),
                accentRow, createSeparator(),
                indexingRow, createSeparator(),
                securityRow
        );

        settingsCard.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");

        VBox mainContent = new VBox(22, titleBox, settingsCard);
        mainContent.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        mainContent.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox centerContent = new VBox(topBar, scrollPane);
        centerContent.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(sidebar);
        root.setCenter(centerContent);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private void showAnimatedSwitchAccountDialog() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Switch Account");

        Label title = new Label("Switch Account");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label desc = new Label("Sign out from the current active profile to authenticate with a different OneSpace account.");
        desc.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        desc.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
        desc.setWrapText(true);

        VBox infoCard = new VBox(10);
        infoCard.setPadding(new Insets(16));
        infoCard.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10;");

        String[] points = {
                "Your locally indexed spaces and files will remain safely stored.",
                "Current authentication token will be cleared securely.",
                "You will be redirected to the sign-in page immediately."
        };

        for (String point : points) {
            Label dot = new Label("•");
            dot.setFont(Font.font(FONT, FontWeight.BOLD, 14));
            dot.setStyle("-fx-text-fill: #38BDF8;");

            Label pointText = new Label(point);
            pointText.setFont(Font.font(FONT, 12));
            pointText.setStyle("-fx-text-fill: " + WHITE + ";");
            pointText.setWrapText(true);

            HBox row = new HBox(8, dot, pointText);
            row.setAlignment(Pos.TOP_LEFT);
            infoCard.getChildren().add(row);
        }

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        cancelBtn.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 16; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> stage.close());
        applyHoverAnimation(cancelBtn, 1.05, 0);

        Button switchBtn = new Button("Sign Out & Switch");
        switchBtn.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        switchBtn.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: white; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 18; -fx-cursor: hand;");
        switchBtn.setOnAction(e -> {
            stage.close();
            UserSession.clearSession();
            Platform.runLater(LandingPage::showUserLoginPage);
        });
        applyHoverAnimation(switchBtn, 1.05, 0);

        HBox btnRow = new HBox(10, cancelBtn, switchBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox layout = new VBox(16, title, desc, infoCard, btnRow);
        layout.setPadding(new Insets(24));
        layout.setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 14; -fx-background-radius: 14;");

        applyHoverAnimation(infoCard, 1.01, -2);

        Scene modalScene = new Scene(layout, 500, 360);
        stage.setScene(modalScene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.showAndWait();
    }

    private void applyHoverAnimation(Node node, double scaleTo, double translateY) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
            st.setToX(scaleTo);
            st.setToY(scaleTo);
            st.play();

            if (translateY != 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(140), node);
                tt.setToY(translateY);
                tt.play();
            }
        });

        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            if (translateY != 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(140), node);
                tt.setToY(0);
                tt.play();
            }
        });
    }

    private void applyRowHover(HBox row) {
        String normalStyle = "-fx-background-color: transparent; -fx-border-color: transparent; -fx-background-radius: 12; -fx-border-radius: 12;";
        String hoverStyle = "-fx-background-color: rgba(56, 189, 248, 0.07); -fx-border-color: rgba(56, 189, 248, 0.35); -fx-border-width: 1; -fx-background-radius: 12; -fx-border-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.25), 12, 0, 0, 2);";

        row.setStyle(normalStyle);
        row.setOnMouseEntered(e -> {
            row.setStyle(hoverStyle);
            TranslateTransition tt = new TranslateTransition(Duration.millis(130), row);
            tt.setToX(4);
            tt.play();
        });
        row.setOnMouseExited(e -> {
            row.setStyle(normalStyle);
            TranslateTransition tt = new TranslateTransition(Duration.millis(130), row);
            tt.setToX(0);
            tt.play();
        });
    }

    private VBox createSidebar() {
        Image logoImage = new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane logoIcon = new StackPane(logoView);
        logoIcon.setPrefSize(42, 42);
        logoIcon.setAlignment(Pos.CENTER);
        applyHoverAnimation(logoIcon, 1.1, 0);

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + WHITE + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("dashboard", "Dashboard", false, e -> LandingPage.showUserDashboard());
        Button spacesBtn = createSidebarButton("files", "Spaces", false, e -> LandingPage.showUserSpace());
        Button searchBtn = createSidebarButton("search", "Search", false, e -> LandingPage.showUserSearch());
        Button calendarBtn = createSidebarButton("calendar", "Calendar", false, e -> LandingPage.showCalendarPage());
        Button aiBtn = createSidebarButton("ai", "AI Assistant", false, e -> LandingPage.showAiAssistantPage());
        Button collabBtn = createSidebarButton("collaboration", "Collaboration", false, e -> LandingPage.showCollaborationPage());
        Button recentBtn = createSidebarButton("recent", "Recent", false, e -> LandingPage.showRecentPage());
        Button trashBtn = createSidebarButton("trash", "Trash", false, e -> LandingPage.showTrashPage());
        Button settingsBtn = createSidebarButton("settings", "Settings", true, e -> LandingPage.showSettingPage());
        activeSidebarBtn = settingsBtn;

        VBox navList = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageTitle.setStyle("-fx-text-fill: " + WHITE + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-text-fill: " + WHITE + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        Region storageSpacer = new Region();
        HBox.setHgrow(storageSpacer, Priority.ALWAYS);

        HBox storageValues = new HBox(storageVal, storageSpacer, storagePercent);
        storageValues.setAlignment(Pos.CENTER_LEFT);

        ProgressBar progress = new ProgressBar(.64);
        sidebarStorageProgress = progress;
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle("-fx-accent: " + primaryBlue + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        Button manageStorageBtn = new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexPage());
        manageStorageBtn.setOnMouseEntered(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), manageStorageBtn);
            tt.setToX(3);
            tt.play();
        });
        manageStorageBtn.setOnMouseExited(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), manageStorageBtn);
            tt.setToX(0);
            tt.play();
        });

        VBox storageCard = new VBox(8, storageTitle, storageValues, progress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: rgba(16, 28, 48, 0.65); -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");
        applyHoverAnimation(storageCard, 1.01, -1);

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        return sidebar;
    }

    private Button createSidebarButton(String iconType, String label, boolean active, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label textLbl = new Label(label);
        textLbl.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        textLbl.setTextFill(Color.web(WHITE));

        HBox content = new HBox(12, iconBox, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 12, 0, 12));
        btn.setOnAction(action);

        if (active) {
            btn.setStyle(
                "-fx-background-color: linear-gradient(to right, " + primaryBlue + ", #2563EB);" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);"
            );
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
            btn.setOnMouseEntered(e -> {
                btn.setStyle("-fx-background-color: rgba(56, 189, 248, 0.12); -fx-background-radius: 12; -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 12; -fx-border-width: 1; -fx-cursor: hand;");
                icon.setStroke(Color.web("#38BDF8"));
                textLbl.setTextFill(Color.web("#38BDF8"));
                TranslateTransition tt = new TranslateTransition(Duration.millis(120), btn);
                tt.setToX(4);
                tt.play();
            });
            btn.setOnMouseExited(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                textLbl.setTextFill(Color.web(WHITE));
                TranslateTransition tt = new TranslateTransition(Duration.millis(120), btn);
                tt.setToX(0);
                tt.play();
            });
        }

        return btn;
    }

    private void updateSidebarAccentColor(String hexColor) {
        if (activeSidebarBtn != null) {
            activeSidebarBtn.setStyle(
                    "-fx-background-color: linear-gradient(to right, " + hexColor + ", #2563EB);" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                    "-fx-border-radius: 12;" +
                    "-fx-border-width: 1;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);"
            );
        }
        if (sidebarStorageProgress != null) {
            sidebarStorageProgress.setStyle("-fx-accent: " + hexColor + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");
        }
    }

    private void setTheme(String theme) {
        currentTheme = theme;
        PREFS.put("theme", theme);
        Stage stage = (Stage) Stage.getWindows().stream().filter(javafx.stage.Window::isShowing).findFirst().orElse(null);
        if (stage != null) stage.setScene(getSettingPageScene());
    }

    private Circle createColorCircle(String hexColor) {
        Circle circle = new Circle(11);
        circle.setFill(Color.web(hexColor));
        boolean selected = primaryBlue.equals(hexColor);
        circle.setStroke(selected ? Color.web(WHITE) : Color.TRANSPARENT);
        circle.setStrokeWidth(selected ? 2.5 : 0);
        circle.setCursor(javafx.scene.Cursor.HAND);

        circle.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), circle);
            st.setToX(1.3);
            st.setToY(1.3);
            st.play();
            updateSidebarAccentColor(hexColor);
        });

        circle.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), circle);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            updateSidebarAccentColor(primaryBlue);
        });

        circle.setOnMouseClicked(e -> {
            primaryBlue = hexColor;
            PREFS.put("accentColor", hexColor);
            Stage stage = (Stage) circle.getScene().getWindow();
            stage.setScene(getSettingPageScene());
        });

        return circle;
    }

    private void openChangePasswordWindow() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Change Password");

        Label title = new Label("Change Password");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        PasswordField currentPass = new PasswordField();
        PasswordField newPass = new PasswordField();
        PasswordField confirmPass = new PasswordField();

        stylePasswordField(currentPass, "Current password");
        stylePasswordField(newPass, "New password");
        stylePasswordField(confirmPass, "Confirm new password");

        Label status = new Label();
        status.setWrapText(true);
        status.setStyle("-fx-text-fill: #F87171; -fx-font-size: 11px;");

        Button updateBtn = new Button("Update Password");
        updateBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        updateBtn.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 8; -fx-cursor: hand;");
        applyHoverAnimation(updateBtn, 1.04, 0);

        updateBtn.setOnAction(e -> {
            String current = currentPass.getText();
            String password = newPass.getText();
            String confirm = confirmPass.getText();

            if (current.isBlank() || password.isBlank() || confirm.isBlank()) {
                status.setText("Please fill all password fields.");
                return;
            }

            if (password.length() < 6) {
                status.setText("New password must contain at least 6 characters.");
                return;
            }

            if (!password.equals(confirm)) {
                status.setText("New passwords do not match.");
                return;
            }

            UserSession session = UserSession.getInstance();

            if (session == null || session.getEmail() == null) {
                status.setText("No authenticated user session found.");
                return;
            }

            updateBtn.setDisable(true);
            status.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + "; -fx-font-size: 11px;");
            status.setText("Updating password...");

            Thread thread = new Thread(() -> {
                boolean success = AUTH.changePassword(session.getEmail(), current, password);

                Platform.runLater(() -> {
                    updateBtn.setDisable(false);

                    if (success) {
                        stage.close();
                        showInfo("Password Updated", "Your Firebase password has been updated successfully.");
                    } else {
                        status.setStyle("-fx-text-fill: #F87171; -fx-font-size: 11px;");
                        status.setText("Current password is incorrect or the password could not be updated.");
                    }
                });
            });

            thread.setDaemon(true);
            thread.start();
        });

        VBox layout = new VBox(
                10,
                title,
                label("Current Password:", 11, FontWeight.SEMI_BOLD, WHITE), currentPass,
                label("New Password:", 11, FontWeight.SEMI_BOLD, WHITE), newPass,
                label("Confirm Password:", 11, FontWeight.SEMI_BOLD, WHITE), confirmPass,
                status,
                updateBtn
        );

        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        stage.setScene(new Scene(layout, 380, 420));
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void stylePasswordField(PasswordField f, String prompt) {
        f.setPromptText(prompt);
        f.setPrefHeight(40);
        f.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 12; -fx-text-fill: " + WHITE + "; -fx-prompt-text-fill: " + LIGHT_SECONDARY + "; -fx-font-size: 12px;");
    }

    private StackPane createSettingIcon(String iconType) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web("#38BDF8"));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setPrefSize(34, 34); iconPane.setMinSize(34, 34);
        iconPane.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-background-radius: 8; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 8;");
        applyHoverAnimation(iconPane, 1.1, 0);
        return iconPane;
    }

    private Label label(String text, int size, FontWeight weight, String textColor) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, weight, size));
        label.setStyle("-fx-text-fill: " + textColor + ";");
        return label;
    }

    private Label createSectionTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        label.setStyle("-fx-text-fill: " + WHITE + ";");
        return label;
    }

    private Label createSectionDescription(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, 12));
        label.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
        return label;
    }

    private HBox createSettingRowLeft(String iconType, String titleText, String descriptionText) {
        StackPane icon = createSettingIcon(iconType);
        Label title = createSectionTitle(titleText);
        Label description = createSectionDescription(descriptionText);

        VBox textBox = new VBox(2, title, description);
        HBox row = new HBox(12, icon, textBox);
        row.setAlignment(Pos.TOP_LEFT);
        return row;
    }

    private HBox createSettingRow(String iconType, String titleText, String descriptionText) {
        StackPane icon = createSettingIcon(iconType);
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
        btn.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: " + WHITE + "; -fx-pref-height: 32; -fx-padding: 0 12; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: rgba(56, 189, 248, 0.18); -fx-border-color: #38BDF8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #38BDF8; -fx-pref-height: 32; -fx-padding: 0 12; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 8, 0, 0, 2);");
            ScaleTransition st = new ScaleTransition(Duration.millis(120), btn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: " + WHITE + "; -fx-pref-height: 32; -fx-padding: 0 12; -fx-cursor: hand;");
            ScaleTransition st = new ScaleTransition(Duration.millis(120), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        return btn;
    }

    private Button createThemeButton(String iconType, String themeName, boolean selected) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(selected ? "#38BDF8" : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        Label name = new Label(themeName);
        name.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        name.setTextFill(Color.web(selected ? "#38BDF8" : WHITE));

        VBox content = new VBox(4, icon, name);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("", content);
        button.setPrefSize(90, 52);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(8, 10, 8, 10));

        if (selected) {
            button.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: #38BDF8; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        } else {
            button.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color: rgba(56, 189, 248, 0.12); -fx-border-color: #38BDF8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
                icon.setStroke(Color.web("#38BDF8"));
                name.setTextFill(Color.web("#38BDF8"));
                ScaleTransition st = new ScaleTransition(Duration.millis(120), button);
                st.setToX(1.04);
                st.setToY(1.04);
                st.play();
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                name.setTextFill(Color.web(WHITE));
                ScaleTransition st = new ScaleTransition(Duration.millis(120), button);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
            });
        }

        return button;
    }

    private Separator createSeparator() {
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08);");
        return sep;
    }

    private String getFirstName(String name) {
        if (name == null || name.isBlank()) return "User";
        return name.trim().split("\\s+")[0];
    }

    private String getInitials(String name) {
        if (name == null || name.isBlank()) return "U";
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    private void styleDialog(Alert alert) {
        alert.getDialogPane().setPrefWidth(480);
        alert.getDialogPane().setPrefHeight(240);
        alert.getDialogPane().setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleDialog(alert);
        alert.showAndWait();
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);
        switch (type) {
            case "dashboard": icon.setContent("M3 3 H10 V10 H3 Z M14 3 H21 V10 H14 Z M3 14 H10 V21 H3 Z M14 14 H21 V21 H14 Z"); break;
            case "files": icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13"); break;
            case "search": icon.setContent("M10 3 A7 7 0 1 0 10 17 A7 7 0 0 0 10 3 Z M15 15 L21 21"); break;
            case "calendar": icon.setContent("M19 4H5C3.89543 4 3 4.89543 3 6V20C3 21.1046 3.89543 22 5 22H19C20.1046 22 21 21.1046 21 20V6C21 4.89543 20.1046 4 19 4Z M16 2V6 M8 2V6 M3 10H21"); break;
            case "ai": icon.setContent("M12 2 L13.5 8.5 L20 7 L15.5 11.5 L21 15 L14 14.5 L12 22 L10 14.5 L3 15 L8.5 11.5 L4 7 L10.5 8.5 Z"); break;
            case "collaboration": icon.setContent("M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2 M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M23 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75"); break;
            case "recent": icon.setContent("M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"); break;
            case "trash": icon.setContent("M3 6h18 M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"); break;
            case "settings": icon.setContent("M12 3 V6 M12 18 V21 M3 12 H6 M18 12 H21 M5.6 5.6 L7.7 7.7 M16.3 16.3 L18.4 18.4 M18.4 5.6 L16.3 7.7 M7.7 16.3 L5.6 18.4 M12 8 A4 4 0 1 0 12 16 A4 4 0 0 0 12 8"); break;
            case "bell": icon.setContent("M6 17 H18 M8 17 V10 A4 4 0 0 1 16 10 V17 M10 20 H14"); break;
            case "theme": icon.setContent("M12 2a10 10 0 1 0 10 10A10 10 0 0 0 12 2zm0 18a8 8 0 1 1 8-8 8 8 0 0 1-8 8z"); break;
            case "sun": icon.setContent("M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"); break;
            case "moon": icon.setContent("M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"); break;
            case "monitor": icon.setContent("M20 3H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V5a2 2 0 0 0-2-2zM8 21h8M12 17v4"); break;
            case "sparkles": icon.setContent("M12 2L13.5 8.5L20 7L15.5 11.5L21 15L14 14.5L12 22L10 14.5L3 15L8.5 11.5L4 7L10.5 8.5Z"); break;
            case "security": icon.setContent("M12 2 L20 5 V11 C20 16 17 20 12 22 C7 20 4 16 4 11 V5 Z M9 12 L11 14 L15 9"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }
}