package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;
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

public class UserProfilePage {

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

    private static final String USER_NAME = "Aarav Verma";
    private static final String USER_EMAIL = "aarav@example.com";
    private static final String USER_USERNAME = "@aarav";
    private static final String USER_BIO =
            "Cloud enthusiast, productivity lover, and OneSpace user.";
    private static final String MEMBER_SINCE = "May 12, 2024";

    private TextField nameField, emailField, usernameField;
    private TextArea bioField;

    private Label profileAvatar;
    private Label profileNameLabel;
    private Label profileEmailLabel;
    private Label profileUsernameLabel;
    private Label profileBioLabel;
    private Label saveStatus;

    public Scene getUserProfilePageScene() {

        StackPane logoIcon = createLogo();

        Label logoText = label(
                "OneSpace",
                19,
                FontWeight.BOLD,
                LIGHT
        );

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = sidebarButton("⌂", "Dashboard", false);
        Button spaces = sidebarButton("📁", "Spaces", false);
        Button search = sidebarButton("⌕", "Search", false);
        Button calendar = sidebarButton("📅", "Calendar", false);
        Button ai = sidebarButton("✧", "AI Assistant", false);
        Button collaboration = sidebarButton("👥", "Collaboration", false);
        Button recent = sidebarButton("🕒", "Recent", false);
        Button trash = sidebarButton("🗑", "Trash", false);
        Button settings = sidebarButton("⚙", "Settings", false);

        dashboard.setOnAction(e -> LandingPage.showUserDashboard());
        spaces.setOnAction(e -> LandingPage.showUserSpace());
        search.setOnAction(e -> LandingPage.showUserSearch());
        calendar.setOnAction(e -> LandingPage.showCalendarPage());
        ai.setOnAction(e -> LandingPage.showLandingPage());
        collaboration.setOnAction(e -> LandingPage.showCollaborationPage());
        recent.setOnAction(e -> LandingPage.showRecentPage());
        trash.setOnAction(e -> LandingPage.showTrashPage());
        settings.setOnAction(e -> LandingPage.showLandingPage());

        VBox nav = new VBox(
                4,
                dashboard,
                spaces,
                search,
                calendar,
                ai,
                collaboration,
                recent,
                trash
        );

        Label storageTitle = label(
                "Storage Used",
                12,
                FontWeight.SEMI_BOLD,
                LIGHT
        );

        Label storageValue = label(
                "64.2 GB of 100 GB",
                12,
                FontWeight.BOLD,
                LIGHT
        );

        Label storagePercent = label(
                "64%",
                11,
                FontWeight.BOLD,
                MUTED_LIGHT
        );

        Region storageSpacer = spacer();

        HBox.setHgrow(storageSpacer, Priority.ALWAYS);

        HBox storageValues = new HBox(
                storageValue,
                storageSpacer,
                storagePercent
        );
        storageValues.setAlignment(Pos.CENTER_LEFT);

        ProgressBar progress = new ProgressBar(.64);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle(
                "-fx-accent:" + BLUE +
                ";-fx-control-inner-background:#0E1520;"
        );

        Button manageStorage = flatButton(
                "Manage Storage ›",
                "#60A5FA"
        );
        manageStorage.setOnAction(e -> LandingPage.showLandingPage());

        VBox storageCard = new VBox(
                8,
                storageTitle,
                storageValues,
                progress,
                manageStorage
        );

        storageCard.setPadding(new Insets(14));
        storageCard.setStyle(
                "-fx-background-color:" + BG_SIDEBAR_CARD +
                ";-fx-border-color:" + SIDEBAR_BORDER +
                ";-fx-border-radius:12;-fx-background-radius:12;"
        );

        Region sidebarSpacer = spacer();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(
                12,
                logoBox,
                nav,
                sidebarSpacer,
                settings,
                storageCard
        );

        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle(
                "-fx-background-color:" + BG_SIDEBAR +
                ";-fx-border-color:" + SIDEBAR_BORDER +
                ";-fx-border-width:0 1 0 0;"
        );

        
        Label searchIcon = label("⌕", 16, FontWeight.NORMAL, MUTED_LIGHT);

        TextField globalSearch = new TextField();
        globalSearch.setPromptText("Search files, spaces or members...");
        globalSearch.setPrefHeight(38);
        globalSearch.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-prompt-text-fill:" + MUTED_LIGHT + ";" +
                "-fx-font-size:13px;" +
                "-fx-text-fill:" + LIGHT + ";"
        );

        Label shortcut = label(
                "⌘ K",
                10,
                FontWeight.SEMI_BOLD,
                MUTED_LIGHT
        );
        shortcut.setStyle(
                "-fx-background-color:#141E2C;" +
                "-fx-text-fill:" + MUTED_LIGHT +
                ";-fx-padding:3 6;-fx-background-radius:4;"
        );

        HBox searchBox = new HBox(
                8,
                searchIcon,
                globalSearch,
                shortcut
        );

        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 12, 0, 14));
        searchBox.setPrefWidth(420);
        searchBox.setStyle(
                "-fx-background-color:#141E2C;" +
                "-fx-border-color:" + SIDEBAR_BORDER +
                ";-fx-border-radius:10;-fx-background-radius:10;"
        );

        HBox.setHgrow(globalSearch, Priority.ALWAYS);

        Button bell = new Button("🔔");
        bell.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-font-size:16px;" +
                "-fx-text-fill:" + LIGHT +
                ";-fx-cursor:hand;"
        );
        bell.setOnAction(e -> LandingPage.showNotificationPage());

        Label topAvatar = createAvatar("AV", 34);

        Label topName = label(
                USER_NAME,
                13,
                FontWeight.SEMI_BOLD,
                LIGHT
        );

        Label dropdown = label("⌄", 13, FontWeight.NORMAL, MUTED_LIGHT);

        HBox profileOption = new HBox(
                8,
                topAvatar,
                topName,
                dropdown
        );

        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(5, 8, 5, 8));
        profileOption.setStyle(
                "-fx-background-color:#26354A;" +
                "-fx-background-radius:8;-fx-cursor:hand;"
        );

        profileOption.setOnMouseEntered(e ->
                profileOption.setStyle(
                        "-fx-background-color:#344762;" +
                        "-fx-background-radius:8;-fx-cursor:hand;"
                )
        );

        profileOption.setOnMouseExited(e ->
                profileOption.setStyle(
                        "-fx-background-color:#26354A;" +
                        "-fx-background-radius:8;-fx-cursor:hand;"
                )
        );

        HBox profileBox = new HBox(10, bell, profileOption);
        profileBox.setAlignment(Pos.CENTER);

        Region topSpacer = spacer();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        HBox topBar = new HBox(
                20,
                searchBox,
                topSpacer,
                profileBox
        );

        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle(
                "-fx-background-color:" + BG_SIDEBAR +
                ";-fx-border-color:" + SIDEBAR_BORDER +
                ";-fx-border-width:0 0 1 0;"
        );

        
        Label title = label(
                "My Profile",
                24,
                FontWeight.BOLD,
                LIGHT
        );

        Label description = label(
                "Manage your OneSpace account information and profile settings.",
                13,
                FontWeight.MEDIUM,
                MUTED_LIGHT
        );

        VBox headerText = new VBox(4, title, description);

        saveStatus = label(
                "",
                11,
                FontWeight.SEMI_BOLD,
                "#86EFAC"
        );

        Region headerSpacer = spacer();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox pageHeader = new HBox(
                headerText,
                headerSpacer,
                saveStatus
        );
        pageHeader.setAlignment(Pos.CENTER_LEFT);

        
        profileAvatar = createAvatar("AV", 92);

        profileNameLabel = label(
                USER_NAME,
                22,
                FontWeight.BOLD,
                DARK
        );

        profileEmailLabel = label(
                USER_EMAIL,
                12,
                FontWeight.NORMAL,
                MUTED
        );

        profileUsernameLabel = label(
                USER_USERNAME,
                12,
                FontWeight.SEMI_BOLD,
                BLUE
        );

        Label memberSince = label(
                "Member since " + MEMBER_SINCE,
                11,
                FontWeight.NORMAL,
                MUTED
        );

        profileBioLabel = label(
                USER_BIO,
                12,
                FontWeight.NORMAL,
                MUTED
        );
        profileBioLabel.setWrapText(true);

        VBox profileInfo = new VBox(
                5,
                profileNameLabel,
                profileEmailLabel,
                profileUsernameLabel,
                memberSince,
                profileBioLabel
        );

        profileInfo.setAlignment(Pos.CENTER_LEFT);

        Button editButton = primaryButton("Edit Profile", 11);
        editButton.setOnAction(e -> showEditProfileDialog());

        Button logoutButton = new Button("🚪 Logout");
        logoutButton.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        logoutButton.setStyle(secondaryStyle());
        logoutButton.setOnAction(e -> handleLogout());

        VBox profileActions = new VBox(
                8,
                editButton,
                logoutButton
        );
        profileActions.setAlignment(Pos.CENTER_RIGHT);

        Region profileSpacer = spacer();
        HBox.setHgrow(profileSpacer, Priority.ALWAYS);

        HBox profileSummary = new HBox(
                20,
                profileAvatar,
                profileInfo,
                profileSpacer,
                profileActions
        );

        profileSummary.setAlignment(Pos.CENTER_LEFT);
        profileSummary.setPadding(new Insets(22));
        profileSummary.setStyle(cardStyle());

        Label detailsTitle = cardTitle("Profile Details");
        Label detailsDescription = cardDescription(
                "Your basic OneSpace profile information."
        );

        nameField = textField(USER_NAME);
        emailField = textField(USER_EMAIL);
        usernameField = textField(USER_USERNAME);

        bioField = new TextArea(USER_BIO);
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

        VBox detailsCard = card(
                detailsTitle,
                detailsDescription,
                grid
        );

        
        VBox accountRows = new VBox(
                0,
                infoRow("Account Type", "Personal Account"),
                infoRow("Member Since", MEMBER_SINCE),
                infoRow("Shared Workspaces", "3 Workspaces"),
                infoRow("Storage", "64.2 GB / 100 GB")
        );

        VBox accountCard = card(
                cardTitle("Account Information"),
                cardDescription(
                        "Basic information about your OneSpace account."
                ),
                accountRows
        );

        Label deleteIcon = label(
                "⚠",
                18,
                FontWeight.NORMAL,
                "#DC2626"
        );

        deleteIcon.setPrefSize(40, 40);
        deleteIcon.setAlignment(Pos.CENTER);
        deleteIcon.setStyle(
                "-fx-background-color:#FEE2E2;" +
                "-fx-background-radius:9;" +
                "-fx-text-fill:#DC2626;"
        );

        Label deleteTitle = label(
                "Delete Account",
                13,
                FontWeight.BOLD,
                DARK
        );

        Label deleteText = label(
                "Permanently remove your OneSpace account and associated data.",
                11,
                FontWeight.NORMAL,
                MUTED
        );
        deleteText.setWrapText(true);

        VBox deleteInfo = new VBox(
                3,
                deleteTitle,
                deleteText
        );

        Region deleteSpacer = spacer();
        HBox.setHgrow(deleteSpacer, Priority.ALWAYS);

        Button deleteButton = new Button("Delete Account");
        deleteButton.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        deleteButton.setStyle(
                "-fx-background-color:#FEE2E2;" +
                "-fx-text-fill:#B91C1C;" +
                "-fx-border-color:#FECACA;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;" +
                "-fx-padding:8 12;"
        );
        deleteButton.setOnAction(e -> showDeleteAccountDialog());

        HBox deleteRow = new HBox(
                12,
                deleteIcon,
                deleteInfo,
                deleteSpacer,
                deleteButton
        );

        deleteRow.setAlignment(Pos.CENTER_LEFT);
        deleteRow.setPadding(new Insets(16));
        deleteRow.setStyle(
                "-fx-background-color:" + BG_INNER +
                ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:11;-fx-background-radius:11;"
        );

        VBox actionsCard = card(
                cardTitle("Account Actions"),
                cardDescription(
                        "Manage important actions related to your account."
                ),
                deleteRow
        );

        Button saveButton = primaryButton("Save Changes", 13);
        saveButton.setPadding(new Insets(10, 22, 10, 22));
        saveButton.setOnAction(e -> saveProfile());

        Button resetButton = new Button("Reset");
        resetButton.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        resetButton.setStyle(secondaryStyle());
        resetButton.setPadding(new Insets(10, 18, 10, 18));
        resetButton.setOnAction(e -> resetProfile());

        Region actionSpacer = spacer();
        HBox.setHgrow(actionSpacer, Priority.ALWAYS);

        HBox actionButtons = new HBox(
                8,
                actionSpacer,
                resetButton,
                saveButton
        );

        actionButtons.setAlignment(Pos.CENTER_RIGHT);

        VBox content = new VBox(
                20,
                pageHeader,
                profileSummary,
                detailsCard,
                accountCard,
                actionsCard,
                actionButtons
        );

        content.setPadding(new Insets(24, 28, 28, 28));
        content.setStyle("-fx-background-color:" + BG_CENTER + ";");

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle(
                "-fx-background-color:" + BG_CENTER +
                ";-fx-background:" + BG_CENTER +
                ";-fx-background-insets:0;-fx-padding:0;"
        );

        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox mainArea = new VBox(topBar, scroll);
        mainArea.setStyle("-fx-background-color:" + BG_CENTER + ";");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(mainArea);
        root.setStyle("-fx-background-color:" + BG_SIDEBAR + ";");

        return new Scene(root, 1200, 750);
    }

    private StackPane createLogo() {

        Image image = new Image(
                getClass().getResourceAsStream(
                        "/assets/logo/OneSpace_logo.png"
                )
        );

        ImageView view = new ImageView(image);
        view.setFitWidth(42);
        view.setFitHeight(42);
        view.setPreserveRatio(true);

        StackPane pane = new StackPane(view);
        pane.setPrefSize(42, 42);
        pane.setAlignment(Pos.CENTER);

        return pane;
    }

    private Label createAvatar(String initials, double size) {

        Label avatar = new Label(initials);

        avatar.setPrefSize(size, size);
        avatar.setMinSize(size, size);
        avatar.setMaxSize(size, size);
        avatar.setAlignment(Pos.CENTER);

        avatar.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        size >= 70 ? 24 : 12
                )
        );

        avatar.setStyle(
                "-fx-background-color:" + BLUE +
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

        Label iconLabel = label(
                icon,
                14,
                FontWeight.NORMAL,
                active ? LIGHT : MUTED_LIGHT
        );

        Label textLabel = label(
                text,
                13,
                active ? FontWeight.BOLD : FontWeight.MEDIUM,
                LIGHT
        );

        HBox content = new HBox(
                12,
                iconLabel,
                textLabel
        );
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("", content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(38);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        String normal = active
                ? "-fx-background-color:" + BLUE + ";"
                : "-fx-background-color:transparent;";

        button.setStyle(
                normal +
                "-fx-background-radius:8;-fx-cursor:hand;"
        );

        if (!active) {
            button.setOnMouseEntered(e ->
                    button.setStyle(
                            "-fx-background-color:#26354A;" +
                            "-fx-background-radius:8;" +
                            "-fx-cursor:hand;"
                    )
            );

            button.setOnMouseExited(e ->
                    button.setStyle(
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

        Label label = new Label(text);
        label.setFont(Font.font(FONT, weight, size));
        label.setStyle("-fx-text-fill:" + color + ";");

        return label;
    }

    private Button primaryButton(String text, double size) {

        Button button = new Button(text);
        button.setFont(Font.font(FONT, FontWeight.BOLD, size));
        button.setStyle(primaryStyle(BLUE));
        button.setCursor(javafx.scene.Cursor.HAND);

        button.setOnMouseEntered(e ->
                button.setStyle(primaryStyle(LIGHT_BLUE))
        );

        button.setOnMouseExited(e ->
                button.setStyle(primaryStyle(BLUE))
        );

        return button;
    }

    private String primaryStyle(String color) {

        return "-fx-background-color:" + color +
                ";-fx-text-fill:white;" +
                "-fx-background-radius:9;" +
                "-fx-cursor:hand;" +
                "-fx-padding:10 22;";
    }

    private Button flatButton(String text, String color) {

        Button button = new Button(text);

        button.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        11
                )
        );

        button.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:" + color +
                ";-fx-padding:2 0 0 0;" +
                "-fx-cursor:hand;"
        );

        return button;
    }

    private String secondaryStyle() {

        return "-fx-background-color:" + BG_INNER +
                ";-fx-text-fill:" + DARK +
                ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;" +
                "-fx-padding:8 14;";
    }

    private TextField textField(String value) {

        TextField field = new TextField(value);
        field.setPrefHeight(40);
        field.setStyle(
                "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:" + BORDER +
                ";-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-padding:0 12;" +
                "-fx-text-fill:" + DARK +
                ";-fx-font-size:12px;"
        );

        return field;
    }

    private TextArea textArea(String value) {

        TextArea area = new TextArea(value);
        area.setWrapText(true);
        area.setStyle(textAreaStyle());

        return area;
    }

    private String textAreaStyle() {

        return "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:" + BORDER +
                ";-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-padding:8 12;" +
                "-fx-text-fill:" + DARK +
                ";-fx-font-size:12px;";
    }

    private VBox fieldBox(
            String title,
            Control field
    ) {

        Label label = label(
                title,
                11,
                FontWeight.SEMI_BOLD,
                DARK
        );

        return new VBox(6, label, field);
    }

    private VBox card(Node... children) {

        VBox box = new VBox(8, children);
        box.setPadding(new Insets(22));
        box.setStyle(cardStyle());

        return box;
    }

    private Label cardTitle(String text) {

        return label(
                text,
                17,
                FontWeight.BOLD,
                DARK
        );
    }

    private Label cardDescription(String text) {

        Label label = label(
                text,
                11,
                FontWeight.NORMAL,
                MUTED
        );

        label.setWrapText(true);

        return label;
    }

    private String cardStyle() {

        return "-fx-background-color:" + BG_CARD +
                ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:14;" +
                "-fx-background-radius:14;" +
                "-fx-effect:dropshadow(" +
                "three-pass-box,rgba(0,0,0,0.16),12,0,0,4);";
    }

    private HBox infoRow(
            String title,
            String value
    ) {

        Label titleLabel = label(
                title,
                11,
                FontWeight.SEMI_BOLD,
                MUTED
        );

        Label valueLabel = label(
                value,
                12,
                FontWeight.BOLD,
                DARK
        );

        Region spacer = spacer();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(
                titleLabel,
                spacer,
                valueLabel
        );

        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 4, 12, 4));
        row.setStyle(
                "-fx-border-color:transparent transparent " +
                BORDER + " transparent;" +
                "-fx-border-width:0 0 1 0;"
        );

        return row;
    }

    private Region spacer() {
        return new Region();
    }

    private void saveProfile() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String bio = bioField.getText().trim();

        if (name.isEmpty()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Invalid Name",
                    "Please enter your name."
            );
            return;
        }

        if (email.isEmpty()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Invalid Email",
                    "Please enter your email address."
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

        username = normalizeUsername(username);

        profileNameLabel.setText(name);
        profileEmailLabel.setText(email);
        profileUsernameLabel.setText(username);
        profileBioLabel.setText(
                bio.isEmpty() ? USER_BIO : bio
        );
        profileAvatar.setText(getInitials(name));
        profileAvatar.setGraphic(null);
        profileAvatar.setStyle(
                "-fx-background-color:" + BLUE +
                ";-fx-background-radius:50%;" +
                "-fx-text-fill:white;"
        );

        saveStatus.setText("✓ Changes saved");

        showAlert(
                Alert.AlertType.INFORMATION,
                "Profile Updated",
                "Your profile changes have been saved successfully."
        );
    }

    private void resetProfile() {

        nameField.setText(USER_NAME);
        emailField.setText(USER_EMAIL);
        usernameField.setText(USER_USERNAME);
        bioField.setText(USER_BIO);

        profileNameLabel.setText(USER_NAME);
        profileEmailLabel.setText(USER_EMAIL);
        profileUsernameLabel.setText(USER_USERNAME);
        profileBioLabel.setText(USER_BIO);

        profileAvatar.setText("AV");
        profileAvatar.setGraphic(null);
        profileAvatar.setStyle(
                "-fx-background-color:" + BLUE +
                ";-fx-background-radius:50%;" +
                "-fx-text-fill:white;"
        );

        saveStatus.setText("");
    }

    private void showEditProfileDialog() {

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Edit Profile");
        dialog.setHeaderText("Update your OneSpace profile");

        TextField name = textField(nameField.getText());
        TextField email = textField(emailField.getText());
        TextField username = textField(usernameField.getText());

        TextArea bio = textArea(bioField.getText());
        bio.setPrefRowCount(4);

        VBox content = new VBox(
                10,
                fieldBox("Full Name", name),
                fieldBox("Email Address", email),
                fieldBox("Username", username),
                fieldBox("Bio", bio)
        );

        content.setPadding(new Insets(10));
        content.setPrefWidth(380);

        ButtonType cancel = new ButtonType(
                "Cancel",
                ButtonBar.ButtonData.CANCEL_CLOSE
        );

        ButtonType save = new ButtonType(
                "Save",
                ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(cancel, save);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setStyle(
                "-fx-background-color:" + BG_CARD +
                ";-fx-border-color:" + BORDER + ";"
        );

        Button saveButton =
                (Button) dialog.getDialogPane().lookupButton(save);

        saveButton.setStyle(
                "-fx-background-color:" + BLUE +
                ";-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        dialog.setResultConverter(result -> {

            if (result != save)
                return result;

            String newName = name.getText().trim();
            String newEmail = email.getText().trim();
            String newUsername = username.getText().trim();

            if (newName.isEmpty()) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Name",
                        "Please enter your name."
                );
                return null;
            }

            if (newEmail.isEmpty()) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Email",
                        "Please enter your email."
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

            nameField.setText(newName);
            emailField.setText(newEmail);
            usernameField.setText(
                    normalizeUsername(newUsername)
            );
            bioField.setText(
                    bio.getText().trim()
            );

            saveProfile();

            return result;
        });

        dialog.showAndWait();
    }

    private void chooseProfilePhoto() {

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Choose Profile Photo");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.png",
                        "*.jpg",
                        "*.jpeg"
                )
        );

        File file = chooser.showOpenDialog(
                profileAvatar.getScene().getWindow()
        );

        if (file == null)
            return;

        try {

            Image image = new Image(
                    file.toURI().toString()
            );

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(92);
            imageView.setFitHeight(92);
            imageView.setPreserveRatio(false);

            profileAvatar.setGraphic(imageView);
            profileAvatar.setText("");

            profileAvatar.setStyle(
                    "-fx-background-color:" + BLUE +
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

    private void handleLogout() {

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        alert.setTitle("Logout");
        alert.setHeaderText("Logout from OneSpace?");
        alert.setContentText(
                "Are you sure you want to logout?"
        );

        ButtonType logout = new ButtonType(
                "Logout",
                ButtonBar.ButtonData.OK_DONE
        );

        ButtonType cancel = new ButtonType(
                "Cancel",
                ButtonBar.ButtonData.CANCEL_CLOSE
        );

        alert.getButtonTypes().setAll(cancel, logout);
        styleDialog(alert);

        alert.showAndWait().ifPresent(result -> {

            if (result == logout)
                LandingPage.showLandingPage();
        });
    }

    private void showDeleteAccountDialog() {

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        alert.setTitle("Delete Account");
        alert.setHeaderText(
                "Delete your OneSpace account?"
        );

        alert.setContentText(
                "This action will permanently remove your account and associated data."
        );

        ButtonType delete = new ButtonType(
                "Delete Account",
                ButtonBar.ButtonData.OK_DONE
        );

        ButtonType cancel = new ButtonType(
                "Cancel",
                ButtonBar.ButtonData.CANCEL_CLOSE
        );

        alert.getButtonTypes().setAll(cancel, delete);
        styleDialog(alert);

        alert.showAndWait().ifPresent(result -> {

            if (result == delete) {

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Account Deleted",
                        "Your account deletion request has been processed."
                );

                LandingPage.showLandingPage();
            }
        });
    }

    private void styleDialog(Alert alert) {

        alert.getDialogPane().setStyle(
                "-fx-background-color:" + BG_CARD +
                ";-fx-border-color:" + BORDER + ";"
        );
    }

    private String normalizeUsername(String username) {

        username = username.trim();

        return username.startsWith("@")
                ? username
                : "@" + username;
    }

    private String getInitials(String name) {

        if (name == null || name.trim().isEmpty())
            return "AV";

        String[] parts = name.trim().split("\\s+");

        if (parts.length >= 2) {

            return (
                    "" +
                    parts[0].charAt(0) +
                    parts[1].charAt(0)
            ).toUpperCase();
        }

        return name.substring(
                0,
                Math.min(2, name.length())
        ).toUpperCase();
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        styleDialog(alert);

        alert.showAndWait();
    }
}