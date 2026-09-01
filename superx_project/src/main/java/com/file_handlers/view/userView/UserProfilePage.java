package com.file_handlers.view.userView;

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
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.file_handlers.controller.AuthController;
import com.file_handlers.dao.UserProfileDAO;
import com.file_handlers.model.UserSession;
import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.view.LandingPage;

public class UserProfilePage {

    // Typography
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

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
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";
    private static final String DEFAULT_USERNAME = "@user";
    private static final String DEFAULT_BIO = "OneSpace user.";

    private static final String BG_INNER = CARD_BG_INNER;
    private static final String BORDER = "rgba(255, 255, 255, 0.08)";
    private static final String DARK = WHITE;
    private static final String BG_CENTER = "#0A121E";
    private static final String LIGHT = WHITE;
    private static final String MUTED = LIGHT_SECONDARY;

    private TextField nameField;
    private TextField emailField;
    private TextField usernameField;
    private TextArea bioField;

    private Label profileAvatar;
    private Label profileNameLabel;
    private Label profileEmailLabel;
    private Label profileUsernameLabel;
    private Label profileBioLabel;
    private Label saveStatus;

    private final UserProfileDAO profileDAO = new UserProfileDAO();
    private final AuthController authController = new AuthController();

    private String currentUsername = DEFAULT_USERNAME;
    private String currentBio = DEFAULT_BIO;

    public Scene getUserProfilePageScene() {
        UserSession session = UserSession.getInstance();
        if (session == null || !UserSession.isLoggedIn()) {
            return createUnauthenticatedScene();
        }
        resetFieldsToDefaults();
        String displayName = getDisplayName(session);
        String email = getEmail(session);

        VBox sidebar = createSidebar();

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button bell = new Button();
        bell.setGraphic(bellIcon);
        bell.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        bell.setOnAction(e -> LandingPage.showNotificationPage());

        String initials = getInitials(displayName);
        Label topAvatar = createAvatar(initials, 34);
        Label topName = label(getFirstName(displayName), 13, FontWeight.SEMI_BOLD, WHITE);
        Label dropdown = label("⌄", 13, FontWeight.NORMAL, LIGHT_SECONDARY);

        HBox profileOption = new HBox(8, topAvatar, topName, dropdown);
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

        HBox profileBox = new HBox(10, bell, profileOption);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(0), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        Button back = new Button("← Dashboard");
        back.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-padding: 6 12; -fx-cursor: hand;");
        back.setOnMouseEntered(e -> back.setStyle("-fx-background-color: rgba(37, 99, 235, 0.2); -fx-text-fill: " + WHITE + "; -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-padding: 6 12; -fx-cursor: hand;"));
        back.setOnMouseExited(e -> back.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-padding: 6 12; -fx-cursor: hand;"));
        back.setOnAction(e -> LandingPage.showUserDashboard());

        HBox backRow = new HBox(back);
        backRow.setAlignment(Pos.CENTER_RIGHT);
        backRow.setPadding(new Insets(12, ResponsiveUtil.PAGE_PADDING, 0, ResponsiveUtil.PAGE_PADDING));

        Label title = label("My Profile", 26, FontWeight.BOLD, WHITE);
        Label description = label("Manage your OneSpace account information and profile settings.", 13, FontWeight.MEDIUM, LIGHT_SECONDARY);
        VBox headerText = new VBox(4, title, description);

        saveStatus = label("", 11, FontWeight.SEMI_BOLD, "#34D399");

        HBox pageHeader = new HBox(headerText, new Region(), saveStatus);
        HBox.setHgrow(pageHeader.getChildren().get(1), Priority.ALWAYS);
        pageHeader.setAlignment(Pos.CENTER_LEFT);

        profileAvatar = createAvatar(initials, 92);
        profileNameLabel = label(displayName, 22, FontWeight.BOLD, WHITE);

        Button addPhotoButton = new Button("📷 Add Photo");
        addPhotoButton.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        addPhotoButton.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 6 14; -fx-cursor: hand;");
        addPhotoButton.setOnAction(e -> chooseProfilePhoto());

        VBox avatarBox = new VBox(10, profileAvatar, addPhotoButton);
        avatarBox.setAlignment(Pos.CENTER);

        profileEmailLabel = label(email, 12, FontWeight.NORMAL, LIGHT_SECONDARY);
        profileUsernameLabel = label(currentUsername, 12, FontWeight.SEMI_BOLD, "#38BDF8");
        Label memberSince = label("OneSpace Account", 11, FontWeight.NORMAL, LIGHT_SECONDARY);
        profileBioLabel = label(currentBio, 12, FontWeight.NORMAL, LIGHT_SECONDARY);
        profileBioLabel.setWrapText(true);

        VBox profileInfo = new VBox(5, profileNameLabel, profileEmailLabel, profileUsernameLabel, memberSince, profileBioLabel);
        profileInfo.setAlignment(Pos.CENTER_LEFT);

        Button editButton = primaryButton("Edit Profile", 11);
        editButton.setOnAction(e -> showEditProfileDialog());

        Button logoutButton = new Button("Logout");
        logoutButton.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        logoutButton.setStyle(secondaryStyle());
        logoutButton.setOnAction(e -> handleLogout());

        VBox profileActions = new VBox(8, editButton, logoutButton);
        profileActions.setAlignment(Pos.CENTER_RIGHT);

        HBox profileSummary = new HBox(20, avatarBox, profileInfo, new Region(), profileActions);
        HBox.setHgrow(profileSummary.getChildren().get(2), Priority.ALWAYS);
        profileSummary.setAlignment(Pos.CENTER_LEFT);
        profileSummary.setPadding(new Insets(22));
        profileSummary.setStyle(cardStyle());

        Label detailsTitle = cardTitle("Profile Details");
        Label detailsDescription = cardDescription("Your basic OneSpace profile information.");

        nameField = textField(displayName);
        emailField = textField(email);
        emailField.setEditable(false);
        emailField.setOpacity(0.75);
        usernameField = textField(currentUsername);

        bioField = new TextArea(currentBio);
        bioField.setPrefRowCount(3);
        bioField.setWrapText(true);
        bioField.setStyle(textAreaStyle());

        VBox nameBox = fieldBox("Full Name", nameField);
        VBox emailBox = fieldBox("Email Address", emailField);
        VBox usernameBox = fieldBox("Username", usernameField);
        VBox bioBox = fieldBox("Bio", bioField);

        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(16);
        grid.add(nameBox, 0, 0);
        grid.add(emailBox, 1, 0);
        grid.add(usernameBox, 0, 1);
        grid.add(bioBox, 1, 1);

        ColumnConstraints c1 = new ColumnConstraints();
        ColumnConstraints c2 = new ColumnConstraints();
        c1.setPercentWidth(50);
        c2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(c1, c2);

        VBox detailsCard = card(detailsTitle, detailsDescription, grid);

        VBox accountRows = new VBox(0,
                infoRow("Account Type", "Personal Account"),
                infoRow("User ID", shortenUid(session.getUid())),
                infoRow("Storage", "64.2 GB / 100 GB")
        );

        VBox accountCard = card(
                cardTitle("Account Information"),
                cardDescription("Basic information about your OneSpace account."),
                accountRows
        );

        SVGPath passIcon1 = createIcon("key");
        passIcon1.setStroke(Color.web("#38BDF8"));
        passIcon1.setStrokeWidth(2);

        StackPane passIconPane = new StackPane(passIcon1);
        passIconPane.setPrefSize(40, 40); passIconPane.setMinSize(40, 40);
        passIconPane.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-background-radius: 9; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 9;");

        Label passTitle1 = label("Change Password", 13, FontWeight.BOLD, WHITE);
        Label passText1 = label("Update your password to keep your account secure.", 11, FontWeight.NORMAL, LIGHT_SECONDARY);
        passText1.setWrapText(true);
        VBox passInfo1 = new VBox(3, passTitle1, passText1);

        Button passButton1 = new Button("Change Password");
        passButton1.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        passButton1.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand; -fx-padding: 8 12;");
        passButton1.setOnAction(e -> showChangePasswordDialog1());

        HBox passRow1 = new HBox(12, passIconPane, passInfo1, new Region(), passButton1);
        HBox.setHgrow(passRow1.getChildren().get(2), Priority.ALWAYS);
        passRow1.setAlignment(Pos.CENTER_LEFT);
        passRow1.setPadding(new Insets(16));
        passRow1.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 11; -fx-background-radius: 11;");

        SVGPath deleteIcon1 = createIcon("trash");
        deleteIcon1.setStroke(Color.web("#F87171"));
        deleteIcon1.setStrokeWidth(2);

        StackPane deleteIconPane = new StackPane(deleteIcon1);
        deleteIconPane.setPrefSize(40, 40); deleteIconPane.setMinSize(40, 40);
        deleteIconPane.setStyle("-fx-background-color: rgba(239, 68, 68, 0.15); -fx-background-radius: 9; -fx-border-color: rgba(239, 68, 68, 0.3); -fx-border-radius: 9;");

        Label deleteTitle1 = label("Delete Account", 13, FontWeight.BOLD, WHITE);
        Label deleteText1 = label("Permanently remove your OneSpace account and associated data.", 11, FontWeight.NORMAL, LIGHT_SECONDARY);
        deleteText1.setWrapText(true);
        VBox deleteInfo = new VBox(3, deleteTitle1, deleteText1);

        Region deleteSpacer = spacer();
        HBox.setHgrow(deleteSpacer, Priority.ALWAYS);

        Button deleteButton = new Button("Delete Account");
        deleteButton.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        deleteButton.setStyle("-fx-background-color: rgba(239, 68, 68, 0.15); -fx-text-fill: #F87171; -fx-border-color: rgba(239, 68, 68, 0.3); -fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand; -fx-padding: 8 12;");
        deleteButton.setOnAction(e -> showDeleteAccountDialog());

        HBox deleteRow = new HBox(12, deleteIconPane, deleteInfo, deleteSpacer, deleteButton);
        deleteRow.setAlignment(Pos.CENTER_LEFT);
        deleteRow.setPadding(new Insets(16));
        deleteRow.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 11; -fx-background-radius: 11;");

        VBox actionsCard1 = card(
                cardTitle("Account Actions"),
                cardDescription("Manage important actions related to your account."),
                new VBox(10, passRow1, deleteRow)
        );

        Button saveButton = primaryButton("Save Changes", 13);
        saveButton.setPadding(new Insets(10, 22, 10, 22));
        saveButton.setOnAction(e -> saveProfile());

        Button resetButton = new Button("Reset");
        resetButton.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        resetButton.setStyle(secondaryStyle());
        resetButton.setPadding(new Insets(10, 18, 10, 18));
        resetButton.setOnAction(e -> resetProfile());

        HBox actionButtons = new HBox(8, new Region(), resetButton, saveButton);
        HBox.setHgrow(actionButtons.getChildren().get(0), Priority.ALWAYS);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);

        VBox content = new VBox(20, pageHeader, profileSummary, detailsCard, accountCard, actionsCard1, actionButtons);
        content.setPadding(new Insets(14, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color: transparent;");

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox mainArea = new VBox(topBar, backRow, scroll);
        VBox.setVgrow(content, Priority.ALWAYS);
        mainArea.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(mainArea);
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");

        loadProfileFromFirestore1();

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private Region spacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
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

        Label logoText = label("OneSpace", 19, FontWeight.BOLD, WHITE);
        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = sidebarButton("dashboard", "Dashboard", false, e -> LandingPage.showUserDashboard());
        Button spaces = sidebarButton("files", "Spaces", false, e -> LandingPage.showUserSpace());
        Button search = sidebarButton("search", "Search", false, e -> LandingPage.showUserSearch());
        Button calendar = sidebarButton("calendar", "Calendar", false, e -> LandingPage.showCalendarPage());
        Button ai = sidebarButton("ai", "AI Assistant", false, e -> LandingPage.showAiAssistantPage());
        Button collaboration = sidebarButton("collaboration", "Collaboration", false, e -> LandingPage.showCollaborationPage());
        Button recent = sidebarButton("recent", "Recent", false, e -> LandingPage.showRecentPage());
        Button trash = sidebarButton("trash", "Trash", false, e -> LandingPage.showTrashPage());
        Button settings = sidebarButton("settings", "Settings", false, e -> LandingPage.showSettingPage());

        VBox nav = new VBox(4, dashboard, spaces, search, calendar, ai, collaboration, recent, trash);

        Label storageTitle = label("Storage Used", 12, FontWeight.BOLD, WHITE);
        Label storageValue = label("64.2 GB of 100 GB", 12, FontWeight.BOLD, WHITE);
        Label storagePercent = label("64%", 11, FontWeight.BOLD, LIGHT_SECONDARY);

        Region storageSpacer = spacer();

        HBox storageValues = new HBox(storageValue, storageSpacer, storagePercent);
        storageValues.setAlignment(Pos.CENTER_LEFT);

        ProgressBar progress = new ProgressBar(.64);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle("-fx-accent: " + BLUE + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        Button manageStorage = flatButton("Storage Index ›", "#60A5FA");
        manageStorage.setOnAction(e -> LandingPage.showStorageIndexPage());

        VBox storageCard = new VBox(8, storageTitle, storageValues, progress, manageStorage);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: rgba(16, 28, 48, 0.65); -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, nav, sidebarSpacer, settings, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        return sidebar;
    }

    private Button sidebarButton(String iconType, String text, boolean active, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label textLabel = label(text, 13, active ? FontWeight.BOLD : FontWeight.MEDIUM, WHITE);

        HBox content = new HBox(12, iconBox, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("", content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(38);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));
        button.setOnAction(action);

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
                textLabel.setTextFill(Color.WHITE);
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                textLabel.setTextFill(Color.web(WHITE));
            });
        }
        return button;
    }

    private void showChangePasswordDialog1() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Update your account password");

        PasswordField oldPass = new PasswordField();
        oldPass.setPrefHeight(40);
        oldPass.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 12; -fx-text-fill: " + WHITE + "; -fx-font-size: 12px;");

        PasswordField newPass = new PasswordField();
        newPass.setPrefHeight(40);
        newPass.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 12; -fx-text-fill: " + WHITE + "; -fx-font-size: 12px;");

        PasswordField confirmPass = new PasswordField();
        confirmPass.setPrefHeight(40);
        confirmPass.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 12; -fx-text-fill: " + WHITE + "; -fx-font-size: 12px;");

        VBox content = new VBox(10,
                fieldBox("Current Password", oldPass),
                fieldBox("New Password", newPass),
                fieldBox("Confirm New Password", confirmPass)
        );
        content.setPadding(new Insets(10));
        content.setPrefWidth(350);

        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType update = new ButtonType("Update Password", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(cancel, update);
        dialog.getDialogPane().setContent(content);
        styleDialog(dialog);

        Button updateBtn = (Button) dialog.getDialogPane().lookupButton(update);
        updateBtn.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7; -fx-cursor: hand;");

        dialog.setResultConverter(result -> {
            if (result != update) return result;

            String oldP = oldPass.getText();
            String newP = newPass.getText();
            String confirmP = confirmPass.getText();

            if (oldP.isEmpty() || newP.isEmpty() || confirmP.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please fill in all password fields.");
                return null;
            }
            if (!newP.equals(confirmP)) {
                showAlert(Alert.AlertType.WARNING, "Password Mismatch", "New password and confirm password do not match.");
                return null;
            }
            if (newP.length() < 6) {
                showAlert(Alert.AlertType.WARNING, "Weak Password", "New password must be at least 6 characters long.");
                return null;
            }

            UserSession session = UserSession.getInstance();
            if (session != null && UserSession.isLoggedIn()) {
                Thread thread = new Thread(() -> {
                    try {
                        boolean success = authController.changePassword(session.getIdToken(), oldP, newP);
                        Platform.runLater(() -> {
                            if (success) {
                                showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully.");
                            } else {
                                showAlert(Alert.AlertType.ERROR, "Failed", "Failed to update password. Please check your credentials.");
                            }
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while changing password."));
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
            return result;
        });

        dialog.showAndWait();
    }

    private void loadProfileFromFirestore1() {
        UserSession session = UserSession.getInstance();
        if (session == null || !UserSession.isLoggedIn()) return;

        String uid = session.getUid();
        Thread thread = new Thread(() -> {
            try {
                Map<String, Object> profile = profileDAO.getProfile(uid);
                String username = readString(profile, "username");
                String bio = readString(profile, "bio");

                if (username == null || username.isBlank()) {
                    username = createDefaultUsername(session.getDisplayName());
                }
                if (bio == null || bio.isBlank()) {
                    bio = DEFAULT_BIO;
                }

                final String finalUsername = normalizeUsername(username);
                final String finalBio = bio;

                Platform.runLater(() -> {
                    currentUsername = finalUsername;
                    currentBio = finalBio;
                    if (usernameField != null) usernameField.setText(finalUsername);
                    if (bioField != null) bioField.setText(finalBio);
                    if (profileUsernameLabel != null) profileUsernameLabel.setText(finalUsername);
                    if (profileBioLabel != null) profileBioLabel.setText(finalBio);
                });
            } catch (Exception e) {
                System.out.println("Unable to load user profile:");
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void saveProfile() {
        UserSession session = UserSession.getInstance();
        if (session == null || !UserSession.isLoggedIn()) {
            showAlert(Alert.AlertType.ERROR, "Session Error", "No authenticated user session was found.");
            return;
        }

        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String bio = bioField.getText().trim();

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Name", "Please enter your name.");
            return;
        }
        if (username.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Username", "Please enter your username.");
            return;
        }

        username = normalizeUsername(username);
        saveStatus.setText("Saving...");

        final String finalName = name;
        final String finalUsername = username;
        final String finalBio = bio.isEmpty() ? DEFAULT_BIO : bio;

        Thread thread = new Thread(() -> {
            try {
                boolean authUpdated = authController.updateProfile(session.getIdToken(), finalName);
                if (!authUpdated) {
                    Platform.runLater(() -> {
                        saveStatus.setText("");
                        showAlert(Alert.AlertType.ERROR, "Profile Update Failed", "Unable to update your display name.");
                    });
                    return;
                }

                Map<String, Object> profileData = new HashMap<>();
                profileData.put("username", finalUsername);
                profileData.put("bio", finalBio);

                profileDAO.saveProfile(session.getUid(), profileData);
                session.setDisplayName(finalName);

                Platform.runLater(() -> {
                    currentUsername = finalUsername;
                    currentBio = finalBio;
                    profileNameLabel.setText(finalName);
                    profileUsernameLabel.setText(finalUsername);
                    profileBioLabel.setText(finalBio);
                    profileAvatar.setText(getInitials(finalName));
                    profileAvatar.setGraphic(null);
                    profileAvatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-text-fill: white;");
                    saveStatus.setText("✓ Changes saved");
                    showAlert(Alert.AlertType.INFORMATION, "Profile Updated", "Your profile changes have been saved successfully.");
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    saveStatus.setText("");
                    showAlert(Alert.AlertType.ERROR, "Profile Update Failed", "Unable to save your profile. Please try again.");
                });
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void resetProfile() {
        UserSession session = UserSession.getInstance();
        if (session == null) return;

        String displayName = getDisplayName(session);
        String email = getEmail(session);

        nameField.setText(displayName);
        emailField.setText(email);
        usernameField.setText(currentUsername);
        bioField.setText(currentBio);
        profileNameLabel.setText(displayName);
        profileEmailLabel.setText(email);
        profileUsernameLabel.setText(currentUsername);
        profileBioLabel.setText(currentBio);
        profileAvatar.setText(getInitials(displayName));
        profileAvatar.setGraphic(null);
        profileAvatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-text-fill: white;");
        saveStatus.setText("");
    }

    private void handleLogout() {
        LandingPage.showUserLoginPage();
    }

    private void showEditProfileDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.setHeaderText("Update your OneSpace profile");

        TextField name = textField(nameField.getText());
        TextField email = textField(emailField.getText());
        email.setEditable(false);
        email.setOpacity(0.75);
        TextField username = textField(usernameField.getText());
        TextArea bio = textArea(bioField.getText());
        bio.setPrefRowCount(4);

        VBox content = new VBox(10,
                fieldBox("Full Name", name),
                fieldBox("Email Address", email),
                fieldBox("Username", username),
                fieldBox("Bio", bio)
        );
        content.setPadding(new Insets(10));
        content.setPrefWidth(380);

        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(cancel, save);
        dialog.getDialogPane().setContent(content);
        styleDialog(dialog);

        Button saveButton = (Button) dialog.getDialogPane().lookupButton(save);
        saveButton.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7; -fx-cursor: hand;");

        dialog.setResultConverter(result -> {
            if (result != save) return result;

            String newName = name.getText().trim();
            String newUsername = username.getText().trim();

            if (newName.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Name", "Please enter your name.");
                return null;
            }
            if (newUsername.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Username", "Please enter your username.");
                return null;
            }

            nameField.setText(newName);
            emailField.setText(emailField.getText());
            usernameField.setText(normalizeUsername(newUsername));
            bioField.setText(bio.getText().trim());

            saveProfile();
            return result;
        });

        dialog.showAndWait();
    }

    private void chooseProfilePhoto() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Profile Photo");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = chooser.showOpenDialog(profileAvatar.getScene().getWindow());
        if (file == null) return;

        try {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(92);
            imageView.setFitHeight(92);
            imageView.setPreserveRatio(false);

            profileAvatar.setGraphic(imageView);
            profileAvatar.setText("");
            profileAvatar.setStyle("-fx-background-color: transparent; -fx-background-radius: 50%;");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Photo Error", "Unable to load the selected image.");
        }
    }

    private void showDeleteAccountDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Delete your OneSpace account?");
        alert.setContentText("This action will permanently remove your account and associated data.");

        ButtonType delete = new ButtonType("Delete Account", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(cancel, delete);
        styleDialog(alert);

        alert.showAndWait().ifPresent(result -> {
            if (result == delete) {
                UserSession session = UserSession.getInstance();

                if (session == null || !UserSession.isLoggedIn()) {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Session Error",
                            "No authenticated user session was found."
                    );
                    return;
                }

                String uid = session.getUid();
                String idToken = session.getIdToken();

                Thread thread = new Thread(() -> {
                    try {
                        profileDAO.deleteProfile(uid);
                        boolean authDeleted = authController.deleteAccount(idToken);

                        if (!authDeleted) {
                            System.out.println("Warning: Firebase Auth deletion failed or requires recent login.");
                        }

                        Platform.runLater(() -> {
                            UserSession.clearSession();
                            showAlert(
                                    Alert.AlertType.INFORMATION,
                                    "Account Deleted",
                                    "Your account has been permanently deleted."
                            );
                            LandingPage.showLandingPage();
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> showAlert(
                                Alert.AlertType.ERROR,
                                "Deletion Failed",
                                "Unable to delete your account. Please try again."
                        ));
                    }
                });

                thread.setDaemon(true);
                thread.start();
            }
        });
    }

    private Scene createUnauthenticatedScene() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color:" + BG_CENTER + ";");

        Label title = label("No Active Session", 22, FontWeight.BOLD, LIGHT);
        Label message = label("Please sign in to view your profile.", 13, FontWeight.NORMAL, LIGHT_SECONDARY);

        Button login = primaryButton("Go to Login", 13);
        login.setOnAction(e -> LandingPage.showUserLoginPage());

        box.getChildren().addAll(title, message, login);
        return new Scene(box, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private String getDisplayName(UserSession session) {
        String name = session.getDisplayName();
        if (name == null || name.isBlank()) return "User";
        return name.trim();
    }

    private String getEmail(UserSession session) {
        String email = session.getEmail();
        if (email == null || email.isBlank()) return "No email available";
        return email.trim();
    }

    private String getFirstName(String name) {
        if (name == null || name.isBlank()) return "User";
        String[] parts = name.trim().split("\\s+");
        return parts[0];
    }

    private String createDefaultUsername(String displayName) {
        if (displayName == null || displayName.isBlank()) return DEFAULT_USERNAME;
        String firstName = getFirstName(displayName).toLowerCase();
        return normalizeUsername(firstName);
    }

    private String normalizeUsername(String username) {
        username = username.trim();
        if (username.isEmpty()) return DEFAULT_USERNAME;
        return username.startsWith("@") ? username : "@" + username;
    }

    private String readString(Map<String, Object> data, String key) {
        if (data == null) return null;
        Object value = data.get(key);
        if (value == null) return null;
        return value.toString();
    }

    private String shortenUid(String uid) {
        if (uid == null || uid.isBlank()) return "Unavailable";
        if (uid.length() <= 12) return uid;
        return uid.substring(0, 12) + "...";
    }

    private Label createAvatar(String initials, double size) {
        Label avatar = new Label(initials);
        avatar.setPrefSize(size, size);
        avatar.setMinSize(size, size);
        avatar.setMaxSize(size, size);
        avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, size >= 70 ? 24 : 12));
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");
        return avatar;
    }

    private Label label(String text, double size, FontWeight weight, String color) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, weight, size));
        label.setStyle("-fx-text-fill: " + color + ";");
        return label;
    }

    private Button primaryButton(String text, double size) {
        Button button = new Button(text);
        button.setFont(Font.font(FONT, FontWeight.BOLD, size));
        button.setStyle(primaryStyle());
        button.setCursor(javafx.scene.Cursor.HAND);
        return button;
    }

    private String primaryStyle() {
        return "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: white; -fx-background-radius: 9; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 9; -fx-border-width: 1; -fx-cursor: hand; -fx-padding: 10 22; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.45), 10, 0, 0, 2);";
    }

    private Button flatButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + color + "; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        return button;
    }

    private String secondaryStyle() {
        return "-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand; -fx-padding: 8 14;";
    }

    private TextField textField(String value) {
        TextField field = new TextField(value);
        field.setPrefHeight(40);
        field.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 12; -fx-text-fill: " + WHITE + "; -fx-font-size: 12px;");
        return field;
    }

    private TextArea textArea(String value) {
        TextArea area = new TextArea(value);
        area.setWrapText(true);
        area.setStyle(textAreaStyle());
        return area;
    }

    private String textAreaStyle() {
        return "-fx-control-inner-background: " + INPUT_BG + "; -fx-background-color: " + INPUT_BG + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 12; -fx-text-fill: " + WHITE + "; -fx-font-size: 12px;";
    }

    private VBox fieldBox(String title, Control field) {
        Label label = label(title, 11, FontWeight.SEMI_BOLD, WHITE);
        return new VBox(6, label, field);
    }

    private VBox card(Node... children) {
        VBox box = new VBox(8, children);
        box.setPadding(new Insets(22));
        box.setStyle(cardStyle());
        return box;
    }

    private Label cardTitle(String text) {
        return label(text, 17, FontWeight.BOLD, WHITE);
    }

    private Label cardDescription(String text) {
        Label label = label(text, 11, FontWeight.NORMAL, LIGHT_SECONDARY);
        label.setWrapText(true);
        return label;
    }

    private String cardStyle() {
        return "-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 16; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);";
    }

    private HBox infoRow(String title, String value) {
        Label titleLabel = label(title, 11, FontWeight.SEMI_BOLD, LIGHT_SECONDARY);
        Label valueLabel = label(value, 12, FontWeight.BOLD, WHITE);

        HBox row = new HBox(titleLabel, spacer(), valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 4, 12, 4));
        row.setStyle("-fx-border-color: transparent transparent rgba(255, 255, 255, 0.08) transparent; -fx-border-width: 0 0 1 0;");
        return row;
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "U";
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    private void styleDialog(Dialog<?> dialog) {
        dialog.getDialogPane().setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");
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
            case "key": icon.setContent("M21 2l-2 2m-2-2l2 2m2 4l-4 4M9 11a5 5 0 110-10 5 5 0 010 10zm0 0l-8 8v3h3l2.5-2.5"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleDialog(alert);
        alert.showAndWait();
    }

    private void resetFieldsToDefaults() {
        currentUsername = DEFAULT_USERNAME;
        currentBio = DEFAULT_BIO;

        if (nameField != null) nameField.setText("");
        if (emailField != null) emailField.setText("");
        if (usernameField != null) usernameField.setText(DEFAULT_USERNAME);
        if (bioField != null) bioField.setText(DEFAULT_BIO);
        if (saveStatus != null) saveStatus.setText("");
        
        if (profileAvatar != null) {
            profileAvatar.setGraphic(null);
            profileAvatar.setText("U");
        }
    }
}