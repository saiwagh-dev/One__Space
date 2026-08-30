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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.file_handlers.controller.AuthController;
import com.file_handlers.dao.UserProfileDAO;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

public class UserProfilePage {

    // =========================================================
    // STYLE CONSTANTS
    // =========================================================

    private static final String FONT =
            "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";
    private static final String BG_CENTER = "#31435B";

    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_INNER = "#CADDF2";
    private static final String BORDER = "#C3D6EC";

    private static final String DARK = "#0F172A";
    private static final String MUTED = "#334155";
    private static final String LIGHT = "#FFFFFF";
    private static final String MUTED_LIGHT = "#94A3B8";

    private static final String BLUE = "#2563EB";
    private static final String LIGHT_BLUE = "#3B82F6";

    // =========================================================
    // DEFAULT PROFILE VALUES
    // =========================================================

    private static final String DEFAULT_USERNAME = "@user";

    private static final String DEFAULT_BIO =
            "OneSpace user.";

    // =========================================================
    // CONTROLS
    // =========================================================

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

    // =========================================================
    // BACKEND
    // =========================================================

    private final UserProfileDAO profileDAO =
            new UserProfileDAO();

    private final AuthController authController =
            new AuthController();

    // =========================================================
    // PROFILE DATA
    // =========================================================

    private String currentUsername = DEFAULT_USERNAME;
    private String currentBio = DEFAULT_BIO;

    // =========================================================
    // PAGE
    // =========================================================

    public Scene getUserProfilePageScene() {

        UserSession session =
                UserSession.getInstance();

        // -----------------------------------------------------
        // Make sure a user is authenticated
        // -----------------------------------------------------

        if (session == null ||
                !UserSession.isLoggedIn()) {

            return createUnauthenticatedScene();
        }

        String displayName =
                getDisplayName(session);

        String email =
                getEmail(session);

        // -----------------------------------------------------
        // SIDEBAR
        // -----------------------------------------------------

        StackPane logoIcon = createLogo();

        Label logoText = label(
                "OneSpace",
                19,
                FontWeight.BOLD,
                LIGHT
        );

        HBox logoHeader =
                new HBox(
                        10,
                        logoIcon,
                        logoText
                );

        logoHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox logoBox =
                new VBox(
                        logoHeader
                );

        logoBox.setPadding(
                new Insets(
                        0,
                        0,
                        18,
                        6
                )
        );

        Button dashboard =
                sidebarButton(
                        "⌂",
                        "Dashboard",
                        false
                );

        Button spaces =
                sidebarButton(
                        "📁",
                        "Spaces",
                        false
                );

        Button search =
                sidebarButton(
                        "⌕",
                        "Search",
                        false
                );

        Button calendar =
                sidebarButton(
                        "📅",
                        "Calendar",
                        false
                );

        Button ai =
                sidebarButton(
                        "✧",
                        "AI Assistant",
                        false
                );

        Button collaboration =
                sidebarButton(
                        "👥",
                        "Collaboration",
                        false
                );

        Button recent =
                sidebarButton(
                        "🕒",
                        "Recent",
                        false
                );

        Button trash =
                sidebarButton(
                        "🗑",
                        "Trash",
                        false
                );

        Button notifications =
                sidebarButton(
                        "🔔",
                        "Notifications",
                        false
                );

        Button settings =
                sidebarButton(
                        "⚙",
                        "Settings",
                        false
                );

        dashboard.setOnAction(
                e -> LandingPage.showUserDashboard()
        );

        spaces.setOnAction(
                e -> LandingPage.showUserSpace()
        );

        search.setOnAction(
                e -> LandingPage.showUserSearch()
        );

        calendar.setOnAction(
                e -> LandingPage.showCalendarPage()
        );

        ai.setOnAction(
                e -> LandingPage.showAiAssistantPage()
        );

        collaboration.setOnAction(
                e -> LandingPage.showCollaborationPage()
        );

        recent.setOnAction(
                e -> LandingPage.showRecentPage()
        );

        trash.setOnAction(
                e -> LandingPage.showTrashPage()
        );

        notifications.setOnAction(
                e -> LandingPage.showNotificationPage()
        );

        settings.setOnAction(
                e -> LandingPage.showSettingPage()
        );

        VBox nav =
                new VBox(
                        4,
                        dashboard,
                        spaces,
                        search,
                        calendar,
                        ai,
                        collaboration,
                        recent,
                        trash,
                        notifications
                );

        // -----------------------------------------------------
        // STORAGE CARD
        // -----------------------------------------------------

        Label storageTitle =
                label(
                        "Storage Used",
                        12,
                        FontWeight.SEMI_BOLD,
                        LIGHT
                );

        Label storageValue =
                label(
                        "64.2 GB of 100 GB",
                        12,
                        FontWeight.BOLD,
                        LIGHT
                );

        Label storagePercent =
                label(
                        "64%",
                        11,
                        FontWeight.BOLD,
                        MUTED_LIGHT
                );

        Region storageSpacer =
                spacer();

        HBox.setHgrow(
                storageSpacer,
                Priority.ALWAYS
        );

        HBox storageValues =
                new HBox(
                        storageValue,
                        storageSpacer,
                        storagePercent
                );

        storageValues.setAlignment(
                Pos.CENTER_LEFT
        );

        ProgressBar progress =
                new ProgressBar(.64);

        progress.setMaxWidth(
                Double.MAX_VALUE
        );

        progress.setPrefHeight(6);

        progress.setStyle(
                "-fx-accent:" + BLUE +
                ";-fx-control-inner-background:#0E1520;"
        );

        Button manageStorage =
                flatButton(
                        "Manage Storage ›",
                        "#60A5FA"
                );

        manageStorage.setOnAction(
                e -> LandingPage.showLandingPage()
        );

        VBox storageCard =
                new VBox(
                        8,
                        storageTitle,
                        storageValues,
                        progress,
                        manageStorage
                );

        storageCard.setPadding(
                new Insets(14)
        );

        storageCard.setStyle(
                "-fx-background-color:" +
                BG_SIDEBAR_CARD +
                ";-fx-border-color:" +
                SIDEBAR_BORDER +
                ";-fx-border-radius:12;" +
                "-fx-background-radius:12;"
        );

        Region sidebarSpacer =
                spacer();

        VBox.setVgrow(
                sidebarSpacer,
                Priority.ALWAYS
        );

        VBox sidebar =
                new VBox(
                        12,
                        logoBox,
                        nav,
                        sidebarSpacer,
                        settings,
                        storageCard
                );

        sidebar.setPadding(
                new Insets(
                        20,
                        14,
                        20,
                        14
                )
        );

        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);

        sidebar.setStyle(
                "-fx-background-color:" +
                BG_SIDEBAR +
                ";-fx-border-color:" +
                SIDEBAR_BORDER +
                ";-fx-border-width:0 1 0 0;"
        );

        // -----------------------------------------------------
        // TOP SEARCH
        // -----------------------------------------------------

        Label searchIcon =
                label(
                        "⌕",
                        16,
                        FontWeight.NORMAL,
                        MUTED_LIGHT
                );

        TextField globalSearch =
                new TextField();

        globalSearch.setPromptText(
                "Search files, spaces or members..."
        );

        globalSearch.setPrefHeight(38);

        globalSearch.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-prompt-text-fill:" +
                MUTED_LIGHT +
                ";-fx-font-size:13px;" +
                "-fx-text-fill:" +
                LIGHT +
                ";"
        );

        Label shortcut =
                label(
                        "⌘ K",
                        10,
                        FontWeight.SEMI_BOLD,
                        MUTED_LIGHT
                );

        shortcut.setStyle(
                "-fx-background-color:#141E2C;" +
                "-fx-text-fill:" +
                MUTED_LIGHT +
                ";-fx-padding:3 6;" +
                "-fx-background-radius:4;"
        );

        HBox searchBox =
                new HBox(
                        8,
                        searchIcon,
                        globalSearch,
                        shortcut
                );

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        searchBox.setPadding(
                new Insets(
                        0,
                        12,
                        0,
                        14
                )
        );

        searchBox.setPrefWidth(420);

        searchBox.setStyle(
                "-fx-background-color:#141E2C;" +
                "-fx-border-color:" +
                SIDEBAR_BORDER +
                ";-fx-border-radius:10;" +
                "-fx-background-radius:10;"
        );

        HBox.setHgrow(
                globalSearch,
                Priority.ALWAYS
        );

        Button bell =
                new Button("🔔");

        bell.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-font-size:16px;" +
                "-fx-text-fill:" +
                LIGHT +
                ";-fx-cursor:hand;"
        );

        bell.setOnAction(
                e -> LandingPage.showNotificationPage()
        );

        // -----------------------------------------------------
        // TOP PROFILE
        // -----------------------------------------------------

        String initials =
                getInitials(displayName);

        Label topAvatar =
                createAvatar(
                        initials,
                        34
                );

        Label topName =
                label(
                        getFirstName(displayName),
                        13,
                        FontWeight.SEMI_BOLD,
                        LIGHT
                );

        Label dropdown =
                label(
                        "⌄",
                        13,
                        FontWeight.NORMAL,
                        MUTED_LIGHT
                );

        HBox profileOption =
                new HBox(
                        8,
                        topAvatar,
                        topName,
                        dropdown
                );

        profileOption.setAlignment(
                Pos.CENTER
        );

        profileOption.setPadding(
                new Insets(
                        5,
                        8,
                        5,
                        8
                )
        );

        profileOption.setStyle(
                "-fx-background-color:#26354A;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        profileOption.setOnMouseClicked(
                e -> LandingPage.showUserProfilePage()
        );

        profileOption.setOnMouseEntered(
                e -> profileOption.setStyle(
                        "-fx-background-color:#344762;" +
                        "-fx-background-radius:8;" +
                        "-fx-cursor:hand;"
                )
        );

        profileOption.setOnMouseExited(
                e -> profileOption.setStyle(
                        "-fx-background-color:#26354A;" +
                        "-fx-background-radius:8;" +
                        "-fx-cursor:hand;"
                )
        );

        HBox profileBox =
                new HBox(
                        10,
                        bell,
                        profileOption
                );

        profileBox.setAlignment(
                Pos.CENTER
        );

        Region topSpacer =
                spacer();

        HBox.setHgrow(
                topSpacer,
                Priority.ALWAYS
        );

        HBox topBar =
                new HBox(
                        20,
                        searchBox,
                        topSpacer,
                        profileBox
                );

        topBar.setAlignment(
                Pos.CENTER_LEFT
        );

        topBar.setPadding(
                new Insets(
                        16,
                        ResponsiveUtil.PAGE_PADDING,
                        14,
                        ResponsiveUtil.PAGE_PADDING
                )
        );

        topBar.setStyle(
                "-fx-background-color:" +
                BG_SIDEBAR +
                ";-fx-border-color:" +
                SIDEBAR_BORDER +
                ";-fx-border-width:0 0 1 0;"
        );

        // -----------------------------------------------------
        // PAGE HEADER
        // -----------------------------------------------------

        Label title =
                label(
                        "My Profile",
                        24,
                        FontWeight.BOLD,
                        LIGHT
                );

        Label description =
                label(
                        "Manage your OneSpace account information and profile settings.",
                        13,
                        FontWeight.MEDIUM,
                        MUTED_LIGHT
                );

        VBox headerText =
                new VBox(
                        4,
                        title,
                        description
                );

        saveStatus =
                label(
                        "",
                        11,
                        FontWeight.SEMI_BOLD,
                        "#86EFAC"
                );

        Region headerSpacer =
                spacer();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        HBox pageHeader =
                new HBox(
                        headerText,
                        headerSpacer,
                        saveStatus
                );

        pageHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        // -----------------------------------------------------
        // PROFILE SUMMARY
        // -----------------------------------------------------

        profileAvatar =
                createAvatar(
                        initials,
                        92
                );

        profileNameLabel =
                label(
                        displayName,
                        22,
                        FontWeight.BOLD,
                        DARK
                );

        profileEmailLabel =
                label(
                        email,
                        12,
                        FontWeight.NORMAL,
                        MUTED
                );

        profileUsernameLabel =
                label(
                        currentUsername,
                        12,
                        FontWeight.SEMI_BOLD,
                        BLUE
                );

        Label memberSince =
                label(
                        "OneSpace Account",
                        11,
                        FontWeight.NORMAL,
                        MUTED
                );

        profileBioLabel =
                label(
                        currentBio,
                        12,
                        FontWeight.NORMAL,
                        MUTED
                );

        profileBioLabel.setWrapText(true);

        VBox profileInfo =
                new VBox(
                        5,
                        profileNameLabel,
                        profileEmailLabel,
                        profileUsernameLabel,
                        memberSince,
                        profileBioLabel
                );

        profileInfo.setAlignment(
                Pos.CENTER_LEFT
        );

        Button editButton =
                primaryButton(
                        "Edit Profile",
                        11
                );

        editButton.setOnAction(
                e -> showEditProfileDialog()
        );

        Button logoutButton =
                new Button(
                        "🚪 Logout"
                );

        logoutButton.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        11
                )
        );

        logoutButton.setStyle(
                secondaryStyle()
        );

        logoutButton.setOnAction(
                e -> handleLogout()
        );

        VBox profileActions =
                new VBox(
                        8,
                        editButton,
                        logoutButton
                );

        profileActions.setAlignment(
                Pos.CENTER_RIGHT
        );

        Region profileSpacer =
                spacer();

        HBox.setHgrow(
                profileSpacer,
                Priority.ALWAYS
        );

        HBox profileSummary =
                new HBox(
                        20,
                        profileAvatar,
                        profileInfo,
                        profileSpacer,
                        profileActions
                );

        profileSummary.setAlignment(
                Pos.CENTER_LEFT
        );

        profileSummary.setPadding(
                new Insets(22)
        );

        profileSummary.setStyle(
                cardStyle()
        );

        // -----------------------------------------------------
        // PROFILE DETAILS
        // -----------------------------------------------------

        Label detailsTitle =
                cardTitle(
                        "Profile Details"
                );

        Label detailsDescription =
                cardDescription(
                        "Your basic OneSpace profile information."
                );

        nameField =
                textField(
                        displayName
                );

        emailField =
                textField(
                        email
                );

        // Email is read-only for now.
        emailField.setEditable(false);

        emailField.setOpacity(0.75);

        usernameField =
                textField(
                        currentUsername
                );

        bioField =
                new TextArea(
                        currentBio
                );

        bioField.setPrefRowCount(3);
        bioField.setWrapText(true);
        bioField.setStyle(
                textAreaStyle()
        );

        VBox nameBox =
                fieldBox(
                        "Full Name",
                        nameField
                );

        VBox emailBox =
                fieldBox(
                        "Email Address",
                        emailField
                );

        VBox usernameBox =
                fieldBox(
                        "Username",
                        usernameField
                );

        VBox bioBox =
                fieldBox(
                        "Bio",
                        bioField
                );

        GridPane grid =
                new GridPane();

        grid.setHgap(18);
        grid.setVgap(16);

        grid.add(
                nameBox,
                0,
                0
        );

        grid.add(
                emailBox,
                1,
                0
        );

        grid.add(
                usernameBox,
                0,
                1
        );

        grid.add(
                bioBox,
                1,
                1
        );

        ColumnConstraints c1 =
                new ColumnConstraints();

        ColumnConstraints c2 =
                new ColumnConstraints();

        c1.setPercentWidth(50);
        c2.setPercentWidth(50);

        grid.getColumnConstraints()
                .addAll(
                        c1,
                        c2
                );

        VBox detailsCard =
                card(
                        detailsTitle,
                        detailsDescription,
                        grid
                );

        // -----------------------------------------------------
        // ACCOUNT INFORMATION
        // -----------------------------------------------------

        VBox accountRows =
                new VBox(
                        0,
                        infoRow(
                                "Account Type",
                                "Personal Account"
                        ),
                        infoRow(
                                "User ID",
                                shortenUid(
                                        session.getUid()
                                )
                        ),
                        infoRow(
                                "Storage",
                                "64.2 GB / 100 GB"
                        )
                );

        VBox accountCard =
                card(
                        cardTitle(
                                "Account Information"
                        ),
                        cardDescription(
                                "Basic information about your OneSpace account."
                        ),
                        accountRows
                );

        // -----------------------------------------------------
        // ACCOUNT ACTIONS
        // -----------------------------------------------------

        Label deleteIcon =
                label(
                        "⚠",
                        18,
                        FontWeight.NORMAL,
                        "#DC2626"
                );

        deleteIcon.setPrefSize(
                40,
                40
        );

        deleteIcon.setAlignment(
                Pos.CENTER
        );

        deleteIcon.setStyle(
                "-fx-background-color:#FEE2E2;" +
                "-fx-background-radius:9;" +
                "-fx-text-fill:#DC2626;"
        );

        Label deleteTitle =
                label(
                        "Delete Account",
                        13,
                        FontWeight.BOLD,
                        DARK
                );

        Label deleteText =
                label(
                        "Permanently remove your OneSpace account and associated data.",
                        11,
                        FontWeight.NORMAL,
                        MUTED
                );

        deleteText.setWrapText(true);

        VBox deleteInfo =
                new VBox(
                        3,
                        deleteTitle,
                        deleteText
                );

        Region deleteSpacer =
                spacer();

        HBox.setHgrow(
                deleteSpacer,
                Priority.ALWAYS
        );

        Button deleteButton =
                new Button(
                        "Delete Account"
                );

        deleteButton.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        11
                )
        );

        deleteButton.setStyle(
                "-fx-background-color:#FEE2E2;" +
                "-fx-text-fill:#B91C1C;" +
                "-fx-border-color:#FECACA;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;" +
                "-fx-padding:8 12;"
        );

        deleteButton.setOnAction(
                e -> showDeleteAccountDialog()
        );

        HBox deleteRow =
                new HBox(
                        12,
                        deleteIcon,
                        deleteInfo,
                        deleteSpacer,
                        deleteButton
                );

        deleteRow.setAlignment(
                Pos.CENTER_LEFT
        );

        deleteRow.setPadding(
                new Insets(16)
        );

        deleteRow.setStyle(
                "-fx-background-color:" +
                BG_INNER +
                ";-fx-border-color:" +
                BORDER +
                ";-fx-border-radius:11;" +
                "-fx-background-radius:11;"
        );

        VBox actionsCard =
                card(
                        cardTitle(
                                "Account Actions"
                        ),
                        cardDescription(
                                "Manage important actions related to your account."
                        ),
                        deleteRow
                );

        // -----------------------------------------------------
        // SAVE / RESET
        // -----------------------------------------------------

        Button saveButton =
                primaryButton(
                        "Save Changes",
                        13
                );

        saveButton.setPadding(
                new Insets(
                        10,
                        22,
                        10,
                        22
                )
        );

        saveButton.setOnAction(
                e -> saveProfile()
        );

        Button resetButton =
                new Button(
                        "Reset"
                );

        resetButton.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        12
                )
        );

        resetButton.setStyle(
                secondaryStyle()
        );

        resetButton.setPadding(
                new Insets(
                        10,
                        18,
                        10,
                        18
                )
        );

        resetButton.setOnAction(
                e -> resetProfile()
        );

        Region actionSpacer =
                spacer();

        HBox.setHgrow(
                actionSpacer,
                Priority.ALWAYS
        );

        HBox actionButtons =
                new HBox(
                        8,
                        actionSpacer,
                        resetButton,
                        saveButton
                );

        actionButtons.setAlignment(
                Pos.CENTER_RIGHT
        );

        // -----------------------------------------------------
        // MAIN CONTENT
        // -----------------------------------------------------

        VBox content =
                new VBox(
                        20,
                        pageHeader,
                        profileSummary,
                        detailsCard,
                        accountCard,
                        actionsCard,
                        actionButtons
                );

        content.setPadding(
                new Insets(
                        24,
                        ResponsiveUtil.PAGE_PADDING,
                        28,
                        ResponsiveUtil.PAGE_PADDING
                )
        );

        content.setStyle(
                "-fx-background-color:" +
                BG_CENTER +
                ";"
        );

        ScrollPane scroll =
                new ScrollPane(
                        content
                );

        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setStyle(
                "-fx-background-color:" +
                BG_CENTER +
                ";-fx-background:" +
                BG_CENTER +
                ";-fx-background-insets:0;" +
                "-fx-padding:0;"
        );

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS
        );

        VBox mainArea =
                new VBox(
                        topBar,
                        scroll
                );

        mainArea.setStyle(
                "-fx-background-color:" +
                BG_CENTER +
                ";"
        );

        BorderPane root =
                new BorderPane();

        root.setLeft(sidebar);
        root.setCenter(mainArea);

        root.setStyle(
                "-fx-background-color:" +
                BG_SIDEBAR +
                ";"
        );

        // -----------------------------------------------------
        // LOAD FIRESTORE PROFILE
        // -----------------------------------------------------

        loadProfileFromFirestore();

        return new Scene(
                root,
                LandingPage.getCurrentWidth(),
                LandingPage.getCurrentHeight()
        );
    }

    // =========================================================
    // LOAD PROFILE
    // =========================================================

    private void loadProfileFromFirestore() {

        UserSession session =
                UserSession.getInstance();

        if (session == null ||
                !UserSession.isLoggedIn()) {

            return;
        }

        String uid =
                session.getUid();

        Thread thread =
                new Thread(
                        () -> {

                            try {

                                Map<String, Object> profile =
                                        profileDAO.getProfile(
                                                uid
                                        );

                                String username =
                                        readString(
                                                profile,
                                                "username"
                                        );

                                String bio =
                                        readString(
                                                profile,
                                                "bio"
                                        );

                                if (username == null ||
                                        username.isBlank()) {

                                    username =
                                            createDefaultUsername(
                                                    session.getDisplayName()
                                            );
                                }

                                if (bio == null ||
                                        bio.isBlank()) {

                                    bio =
                                            DEFAULT_BIO;
                                }

                                final String finalUsername =
                                        normalizeUsername(
                                                username
                                        );

                                final String finalBio =
                                        bio;

                                Platform.runLater(
                                        () -> {

                                            currentUsername =
                                                    finalUsername;

                                            currentBio =
                                                    finalBio;

                                            if (usernameField != null) {

                                                usernameField.setText(
                                                        finalUsername
                                                );
                                            }

                                            if (bioField != null) {

                                                bioField.setText(
                                                        finalBio
                                                );
                                            }

                                            if (profileUsernameLabel != null) {

                                                profileUsernameLabel.setText(
                                                        finalUsername
                                                );
                                            }

                                            if (profileBioLabel != null) {

                                                profileBioLabel.setText(
                                                        finalBio
                                                );
                                            }
                                        }
                                );

                            } catch (Exception e) {

                                System.out.println(
                                        "Unable to load user profile:"
                                );

                                e.printStackTrace();
                            }
                        }
                );

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // SAVE PROFILE
    // =========================================================

    private void saveProfile() {

        UserSession session =
                UserSession.getInstance();

        if (session == null ||
                !UserSession.isLoggedIn()) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Session Error",
                    "No authenticated user session was found."
            );

            return;
        }

        String name =
                nameField.getText()
                        .trim();

        String username =
                usernameField.getText()
                        .trim();

        String bio =
                bioField.getText()
                        .trim();

        // -----------------------------------------------------
        // Validation
        // -----------------------------------------------------

        if (name.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Invalid Name",
                    "Please enter your name."
            );

            return;
        }

        if (username.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Invalid Username",
                    "Please enter your username."
            );

            return;
        }

        username =
                normalizeUsername(
                        username
                );

        // -----------------------------------------------------
        // Disable save button while saving
        // -----------------------------------------------------

        saveStatus.setText(
                "Saving..."
        );

        // -----------------------------------------------------
        // Run network operations outside JavaFX thread
        // -----------------------------------------------------

        final String finalName =
                name;

        final String finalUsername =
                username;

        final String finalBio =
                bio.isEmpty()
                        ? DEFAULT_BIO
                        : bio;

        Thread thread =
                new Thread(
                        () -> {

                            try {

                                // -------------------------------------------------
                                // 1. Update Firebase Authentication display name
                                // -------------------------------------------------

                                boolean authUpdated =
                                        authController.updateProfile(
                                                session.getIdToken(),
                                                finalName
                                        );

                                if (!authUpdated) {

                                    Platform.runLater(
                                            () -> {

                                                saveStatus.setText(
                                                        ""
                                                );

                                                showAlert(
                                                        Alert.AlertType.ERROR,
                                                        "Profile Update Failed",
                                                        "Unable to update your display name."
                                                );
                                            }
                                    );

                                    return;
                                }

                                // -------------------------------------------------
                                // 2. Save username and bio to Firestore
                                // -------------------------------------------------

                                Map<String, Object> profileData =
                                        new HashMap<>();

                                profileData.put(
                                        "username",
                                        finalUsername
                                );

                                profileData.put(
                                        "bio",
                                        finalBio
                                );

                                profileDAO.saveProfile(
                                        session.getUid(),
                                        profileData
                                );

                                // -------------------------------------------------
                                // 3. Update local session
                                // -------------------------------------------------

                                session.setDisplayName(
                                        finalName
                                );

                                // -------------------------------------------------
                                // 4. Update UI
                                // -------------------------------------------------

                                Platform.runLater(
                                        () -> {

                                            currentUsername =
                                                    finalUsername;

                                            currentBio =
                                                    finalBio;

                                            profileNameLabel.setText(
                                                    finalName
                                            );

                                            profileUsernameLabel.setText(
                                                    finalUsername
                                            );

                                            profileBioLabel.setText(
                                                    finalBio
                                            );

                                            profileAvatar.setText(
                                                    getInitials(
                                                            finalName
                                                    )
                                            );

                                            profileAvatar.setGraphic(
                                                    null
                                            );

                                            profileAvatar.setStyle(
                                                    "-fx-background-color:" +
                                                    BLUE +
                                                    ";-fx-background-radius:50%;" +
                                                    "-fx-text-fill:white;"
                                            );

                                            saveStatus.setText(
                                                    "✓ Changes saved"
                                            );

                                            showAlert(
                                                    Alert.AlertType.INFORMATION,
                                                    "Profile Updated",
                                                    "Your profile changes have been saved successfully."
                                            );
                                        }
                                );

                            } catch (Exception e) {

                                e.printStackTrace();

                                Platform.runLater(
                                        () -> {

                                            saveStatus.setText(
                                                    ""
                                            );

                                            showAlert(
                                                    Alert.AlertType.ERROR,
                                                    "Profile Update Failed",
                                                    "Unable to save your profile. Please try again."
                                            );
                                        }
                                );
                            }
                        }
                );

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // RESET PROFILE
    // =========================================================

    private void resetProfile() {

        UserSession session =
                UserSession.getInstance();

        if (session == null) {
            return;
        }

        String displayName =
                getDisplayName(
                        session
                );

        String email =
                getEmail(
                        session
                );

        nameField.setText(
                displayName
        );

        emailField.setText(
                email
        );

        usernameField.setText(
                currentUsername
        );

        bioField.setText(
                currentBio
        );

        profileNameLabel.setText(
                displayName
        );

        profileEmailLabel.setText(
                email
        );

        profileUsernameLabel.setText(
                currentUsername
        );

        profileBioLabel.setText(
                currentBio
        );

        profileAvatar.setText(
                getInitials(
                        displayName
                )
        );

        profileAvatar.setGraphic(
                null
        );

        profileAvatar.setStyle(
                "-fx-background-color:" +
                BLUE +
                ";-fx-background-radius:50%;" +
                "-fx-text-fill:white;"
        );

        saveStatus.setText(
                ""
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void handleLogout() {

        UserSession session =
                UserSession.getInstance();

        if (session != null) {
            // Clear or invalidate any active session in the app if needed.
            // This page intentionally avoids direct UserSession mutation
            // to keep the logout flow consistent with the app's login page.
        }

        LandingPage.showUserLoginPage();
    }

    // =========================================================
    // EDIT PROFILE DIALOG
    // =========================================================

    private void showEditProfileDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Edit Profile"
        );

        dialog.setHeaderText(
                "Update your OneSpace profile"
        );

        TextField name =
                textField(
                        nameField.getText()
                );

        TextField email =
                textField(
                        emailField.getText()
                );

        // Email cannot be changed through this dialog yet.
        email.setEditable(false);
        email.setOpacity(0.75);

        TextField username =
                textField(
                        usernameField.getText()
                );

        TextArea bio =
                textArea(
                        bioField.getText()
                );

        bio.setPrefRowCount(4);

        VBox content =
                new VBox(
                        10,
                        fieldBox(
                                "Full Name",
                                name
                        ),
                        fieldBox(
                                "Email Address",
                                email
                        ),
                        fieldBox(
                                "Username",
                                username
                        ),
                        fieldBox(
                                "Bio",
                                bio
                        )
                );

        content.setPadding(
                new Insets(10)
        );

        content.setPrefWidth(
                380
        );

        ButtonType cancel =
                new ButtonType(
                        "Cancel",
                        ButtonBar.ButtonData
                                .CANCEL_CLOSE
                );

        ButtonType save =
                new ButtonType(
                        "Save",
                        ButtonBar.ButtonData
                                .OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        cancel,
                        save
                );

        dialog.getDialogPane()
                .setContent(
                        content
                );

        dialog.getDialogPane()
                .setStyle(
                        "-fx-background-color:" +
                        BG_CARD +
                        ";-fx-border-color:" +
                        BORDER +
                        ";"
                );

        Button saveButton =
                (Button)
                dialog.getDialogPane()
                        .lookupButton(
                                save
                        );

        saveButton.setStyle(
                "-fx-background-color:" +
                BLUE +
                ";-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        dialog.setResultConverter(
                result -> {

                    if (result != save) {
                        return result;
                    }

                    String newName =
                            name.getText()
                                    .trim();

                    String newUsername =
                            username.getText()
                                    .trim();

                    if (newName.isEmpty()) {

                        showAlert(
                                Alert.AlertType.WARNING,
                                "Invalid Name",
                                "Please enter your name."
                        );

                        return null;
                    }

                    if (newUsername.isEmpty()) {

                        showAlert(
                                Alert.AlertType.WARNING,
                                "Invalid Username",
                                "Please enter your username."
                        );

                        return null;
                    }

                    nameField.setText(
                            newName
                    );

                    // Email remains unchanged.
                    emailField.setText(
                            emailField.getText()
                    );

                    usernameField.setText(
                            normalizeUsername(
                                    newUsername
                            )
                    );

                    bioField.setText(
                            bio.getText()
                                    .trim()
                    );

                    saveProfile();

                    return result;
                }
        );

        dialog.showAndWait();
    }

    // =========================================================
    // DELETE ACCOUNT DIALOG
    // =========================================================

    private void showDeleteAccountDialog() {

        UserSession session =
                UserSession.getInstance();

        if (session == null ||
                !UserSession.isLoggedIn()) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Session Error",
                    "No authenticated user session was found."
            );

            return;
        }

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Delete Account"
        );

        dialog.setHeaderText(
                "This action is permanent."
        );

        Label warning =
                label(
                        "This will permanently remove your OneSpace account and associated profile data. This action cannot be undone.",
                        12,
                        FontWeight.NORMAL,
                        MUTED
                );

        warning.setWrapText(true);
        warning.setMaxWidth(360);

        VBox content =
                new VBox(
                        10,
                        warning
                );

        content.setPadding(
                new Insets(10)
        );

        ButtonType cancel =
                new ButtonType(
                        "Cancel",
                        ButtonBar.ButtonData
                                .CANCEL_CLOSE
                );

        ButtonType delete =
                new ButtonType(
                        "Delete Account",
                        ButtonBar.ButtonData
                                .OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        cancel,
                        delete
                );

        dialog.getDialogPane()
                .setContent(
                        content
                );

        dialog.getDialogPane()
                .setStyle(
                        "-fx-background-color:" +
                        BG_CARD +
                        ";-fx-border-color:" +
                        BORDER +
                        ";"
                );

        Button deleteButton =
                (Button)
                dialog.getDialogPane()
                        .lookupButton(
                                delete
                        );

        deleteButton.setStyle(
                "-fx-background-color:#DC2626;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        dialog.setResultConverter(
                result -> {

                    if (result == delete) {

                        showAlert(
                                Alert.AlertType.INFORMATION,
                                "Deletion Requested",
                                "Your account deletion request has been received. Please contact support if you need assistance."
                        );
                    }

                    return result;
                }
        );

        dialog.showAndWait();
    }

    // =========================================================
    // PROFILE PHOTO
    // =========================================================

    private void chooseProfilePhoto() {

        FileChooser chooser =
                new FileChooser();

        chooser.setTitle(
                "Choose Profile Photo"
        );

        chooser.getExtensionFilters()
                .add(
                        new FileChooser
                                .ExtensionFilter(
                                        "Image Files",
                                        "*.png",
                                        "*.jpg",
                                        "*.jpeg"
                                )
                );

        File file =
                chooser.showOpenDialog(
                        profileAvatar
                                .getScene()
                                .getWindow()
                );

        if (file == null) {
            return;
        }

        try {

            Image image =
                    new Image(
                            file.toURI()
                                    .toString()
                    );

            ImageView imageView =
                    new ImageView(
                            image
                    );

            imageView.setFitWidth(92);
            imageView.setFitHeight(92);
            imageView.setPreserveRatio(false);

            profileAvatar.setGraphic(
                    imageView
            );

            profileAvatar.setText(
                    ""
            );

            profileAvatar.setStyle(
                    "-fx-background-color:" +
                    BLUE +
                    ";-fx-background-radius:50%;"
            );

        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Photo Error",
                    "Unable to load the selected image."
            );
        }
    }

    // =========================================================
    // UNAUTHENTICATED PAGE
    // =========================================================

    private Scene createUnauthenticatedScene() {

        VBox box =
                new VBox(
                        12
                );

        box.setAlignment(
                Pos.CENTER
        );

        box.setStyle(
                "-fx-background-color:" +
                BG_CENTER +
                ";"
        );

        Label title =
                label(
                        "No Active Session",
                        22,
                        FontWeight.BOLD,
                        LIGHT
                );

        Label message =
                label(
                        "Please sign in to view your profile.",
                        13,
                        FontWeight.NORMAL,
                        MUTED_LIGHT
                );

        Button login =
                primaryButton(
                        "Go to Login",
                        13
                );

        login.setOnAction(
                e -> LandingPage.showUserLoginPage()
        );

        box.getChildren()
                .addAll(
                        title,
                        message,
                        login
                );

        return new Scene(
                box,
                LandingPage.getCurrentWidth(),
                LandingPage.getCurrentHeight()
        );
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private String getDisplayName(
            UserSession session
    ) {

        String name =
                session.getDisplayName();

        if (name == null ||
                name.isBlank()) {

            return "User";
        }

        return name.trim();
    }

    private String getEmail(
            UserSession session
    ) {

        String email =
                session.getEmail();

        if (email == null ||
                email.isBlank()) {

            return "No email available";
        }

        return email.trim();
    }

    private String getFirstName(
            String name
    ) {

        if (name == null ||
                name.isBlank()) {

            return "User";
        }

        String[] parts =
                name.trim()
                        .split("\\s+");

        return parts[0];
    }

    private String createDefaultUsername(
            String displayName
    ) {

        if (displayName == null ||
                displayName.isBlank()) {

            return DEFAULT_USERNAME;
        }

        String firstName =
                getFirstName(
                        displayName
                )
                        .toLowerCase();

        return normalizeUsername(
                firstName
        );
    }

    private String normalizeUsername(
            String username
    ) {

        username =
                username.trim();

        if (username.isEmpty()) {
            return DEFAULT_USERNAME;
        }

        return username.startsWith("@")
                ? username
                : "@" + username;
    }

    private String readString(
            Map<String, Object> data,
            String key
    ) {

        if (data == null) {
            return null;
        }

        Object value =
                data.get(key);

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    private String shortenUid(
            String uid
    ) {

        if (uid == null ||
                uid.isBlank()) {

            return "Unavailable";
        }

        if (uid.length() <= 12) {
            return uid;
        }

        return uid.substring(
                0,
                12
        ) + "...";
    }

    private StackPane createLogo() {

        Image image =
                new Image(
                        getClass()
                                .getResourceAsStream(
                                        "/assets/logo/OneSpace_logo.png"
                                )
                );

        ImageView view =
                new ImageView(
                        image
                );

        view.setFitWidth(42);
        view.setFitHeight(42);
        view.setPreserveRatio(true);

        StackPane pane =
                new StackPane(
                        view
                );

        pane.setPrefSize(
                42,
                42
        );

        pane.setAlignment(
                Pos.CENTER
        );

        return pane;
    }

    private Label createAvatar(
            String initials,
            double size
    ) {

        Label avatar =
                new Label(
                        initials
                );

        avatar.setPrefSize(
                size,
                size
        );

        avatar.setMinSize(
                size,
                size
        );

        avatar.setMaxSize(
                size,
                size
        );

        avatar.setAlignment(
                Pos.CENTER
        );

        avatar.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        size >= 70
                                ? 24
                                : 12
                )
        );

        avatar.setStyle(
                "-fx-background-color:" +
                BLUE +
                ";-fx-background-radius:50%;" +
                "-fx-text-fill:white;"
        );

        return avatar;
    }

    private Button sidebarButton(
            String icon,
            String text,
            boolean active
    ) {

        Label iconLabel =
                label(
                        icon,
                        14,
                        FontWeight.NORMAL,
                        active
                                ? LIGHT
                                : MUTED_LIGHT
                );

        Label textLabel =
                label(
                        text,
                        13,
                        active
                                ? FontWeight.BOLD
                                : FontWeight.MEDIUM,
                        LIGHT
                );

        HBox content =
                new HBox(
                        12,
                        iconLabel,
                        textLabel
                );

        content.setAlignment(
                Pos.CENTER_LEFT
        );

        Button button =
                new Button(
                        "",
                        content
                );

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.setPrefHeight(38);

        button.setAlignment(
                Pos.CENTER_LEFT
        );

        button.setPadding(
                new Insets(
                        0,
                        12,
                        0,
                        12
                )
        );

        String normal =
                active
                        ? "-fx-background-color:" +
                          BLUE +
                          ";"
                        : "-fx-background-color:transparent;";

        button.setStyle(
                normal +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        if (!active) {

            button.setOnMouseEntered(
                    e -> button.setStyle(
                            "-fx-background-color:#26354A;" +
                            "-fx-background-radius:8;" +
                            "-fx-cursor:hand;"
                    )
            );

            button.setOnMouseExited(
                    e -> button.setStyle(
                            "-fx-background-color:transparent;" +
                            "-fx-background-radius:8;" +
                            "-fx-cursor:hand;"
                    )
            );
        }

        return button;
    }

    private Label label(
            String text,
            double size,
            FontWeight weight,
            String color
    ) {

        Label label =
                new Label(
                        text
                );

        label.setFont(
                Font.font(
                        FONT,
                        weight,
                        size
                )
        );

        label.setStyle(
                "-fx-text-fill:" +
                color +
                ";"
        );

        return label;
    }

    private Button primaryButton(
            String text,
            double size
    ) {

        Button button =
                new Button(
                        text
                );

        button.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        size
                )
        );

        button.setStyle(
                primaryStyle(
                        BLUE
                )
        );

        button.setCursor(
                javafx.scene.Cursor.HAND
        );

        button.setOnMouseEntered(
                e -> button.setStyle(
                        primaryStyle(
                                LIGHT_BLUE
                        )
                )
        );

        button.setOnMouseExited(
                e -> button.setStyle(
                        primaryStyle(
                                BLUE
                        )
                )
        );

        return button;
    }

    private String primaryStyle(
            String color
    ) {

        return "-fx-background-color:" +
                color +
                ";-fx-text-fill:white;" +
                "-fx-background-radius:9;" +
                "-fx-cursor:hand;" +
                "-fx-padding:10 22;";
    }

    private Button flatButton(
            String text,
            String color
    ) {

        Button button =
                new Button(
                        text
                );

        button.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        11
                )
        );

        button.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:" +
                color +
                ";-fx-padding:2 0 0 0;" +
                "-fx-cursor:hand;"
        );

        return button;
    }

    private String secondaryStyle() {

        return "-fx-background-color:" +
                BG_INNER +
                ";-fx-text-fill:" +
                DARK +
                ";-fx-border-color:" +
                BORDER +
                ";-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;" +
                "-fx-padding:8 14;";
    }

    private TextField textField(
            String value
    ) {

        TextField field =
                new TextField(
                        value
                );

        field.setPrefHeight(40);

        field.setStyle(
                "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:" +
                BORDER +
                ";-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-padding:0 12;" +
                "-fx-text-fill:" +
                DARK +
                ";-fx-font-size:12px;"
        );

        return field;
    }

    private TextArea textArea(
            String value
    ) {

        TextArea area =
                new TextArea(
                        value
                );

        area.setWrapText(
                true
        );

        area.setStyle(
                textAreaStyle()
        );

        return area;
    }

    private String textAreaStyle() {

        return "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:" +
                BORDER +
                ";-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-padding:8 12;" +
                "-fx-text-fill:" +
                DARK +
                ";-fx-font-size:12px;";
    }

    private VBox fieldBox(
            String title,
            Control field
    ) {

        Label label =
                label(
                        title,
                        11,
                        FontWeight.SEMI_BOLD,
                        DARK
                );

        return new VBox(
                6,
                label,
                field
        );
    }

    private VBox card(
            Node... children
    ) {

        VBox box =
                new VBox(
                        8,
                        children
                );

        box.setPadding(
                new Insets(22)
        );

        box.setStyle(
                cardStyle()
        );

        return box;
    }

    private Label cardTitle(
            String text
    ) {

        return label(
                text,
                17,
                FontWeight.BOLD,
                DARK
        );
    }

    private Label cardDescription(
            String text
    ) {

        Label label =
                label(
                        text,
                        11,
                        FontWeight.NORMAL,
                        MUTED
                );

        label.setWrapText(
                true
        );

        return label;
    }

    private String cardStyle() {

        return "-fx-background-color:" +
                BG_CARD +
                ";-fx-border-color:" +
                BORDER +
                ";-fx-border-radius:14;" +
                "-fx-background-radius:14;" +
                "-fx-effect:dropshadow(" +
                "three-pass-box,rgba(0,0,0,0.16),12,0,0,4);";
    }

    private HBox infoRow(
            String title,
            String value
    ) {

        Label titleLabel =
                label(
                        title,
                        11,
                        FontWeight.SEMI_BOLD,
                        MUTED
                );

        Label valueLabel =
                label(
                        value,
                        12,
                        FontWeight.BOLD,
                        DARK
                );

        Region spacer =
                spacer();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        HBox row =
                new HBox(
                        titleLabel,
                        spacer,
                        valueLabel
                );

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPadding(
                new Insets(
                        12,
                        4,
                        12,
                        4
                )
        );

        row.setStyle(
                "-fx-border-color:transparent transparent " +
                BORDER +
                " transparent;" +
                "-fx-border-width:0 0 1 0;"
        );

        return row;
    }

    private Region spacer() {

        return new Region();
    }

    private String getInitials(
            String name
    ) {

        if (name == null ||
                name.trim().isEmpty()) {

            return "U";
        }

        String[] parts =
                name.trim()
                        .split("\\s+");

        if (parts.length >= 2) {

            return (
                    ""
                    + parts[0].charAt(0)
                    + parts[1].charAt(0)
            ).toUpperCase();
        }

        return name.substring(
                0,
                Math.min(
                        2,
                        name.length()
                )
        ).toUpperCase();
    }

    private void styleDialog(
            Alert alert
    ) {

        alert.getDialogPane()
                .setStyle(
                        "-fx-background-color:" +
                        BG_CARD +
                        ";-fx-border-color:" +
                        BORDER +
                        ";"
                );
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        type
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        styleDialog(
                alert
        );

        alert.showAndWait();
    }
}