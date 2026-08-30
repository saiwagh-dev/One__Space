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

    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
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
    private static final String DEFAULT_USERNAME = "@user";
    private static final String DEFAULT_BIO = "OneSpace user.";

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

        String displayName = getDisplayName(session);
        String email = getEmail(session);

        StackPane logoIcon = createLogo();
        Label logoText = label("OneSpace", 19, FontWeight.BOLD, LIGHT);
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
        Button notifications = sidebarButton("🔔", "Notifications", false);
        Button settings = sidebarButton("⚙", "Settings", false);

        dashboard.setOnAction(e -> LandingPage.showUserDashboard());
        spaces.setOnAction(e -> LandingPage.showUserSpace());
        search.setOnAction(e -> LandingPage.showUserSearch());
        calendar.setOnAction(e -> LandingPage.showCalendarPage());
        ai.setOnAction(e -> LandingPage.showAiAssistantPage());
        collaboration.setOnAction(e -> LandingPage.showCollaborationPage());
        recent.setOnAction(e -> LandingPage.showRecentPage());
        trash.setOnAction(e -> LandingPage.showTrashPage());
        notifications.setOnAction(e -> LandingPage.showNotificationPage());
        settings.setOnAction(e -> LandingPage.showSettingPage());

        VBox nav = new VBox(4, dashboard, spaces, search, calendar, ai, collaboration, recent, trash, notifications);

        Label storageTitle = label("Storage Used", 12, FontWeight.SEMI_BOLD, LIGHT);
        Label storageValue = label("64.2 GB of 100 GB", 12, FontWeight.BOLD, LIGHT);
        Label storagePercent = label("64%", 11, FontWeight.BOLD, MUTED_LIGHT);
        Region storageSpacer = spacer();
        HBox.setHgrow(storageSpacer, Priority.ALWAYS);
        HBox storageValues = new HBox(storageValue, storageSpacer, storagePercent);
        storageValues.setAlignment(Pos.CENTER_LEFT);

        ProgressBar progress = new ProgressBar(.64);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle("-fx-accent:" + BLUE + ";-fx-control-inner-background:#0E1520;");

        Button manageStorage = flatButton("Storage Index ›", "#60A5FA");
        manageStorage.setOnAction(e -> LandingPage.showStorageIndexPage());

        VBox storageCard = new VBox(8, storageTitle, storageValues, progress, manageStorage);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color:" + BG_SIDEBAR_CARD + ";-fx-border-color:" + SIDEBAR_BORDER + ";-fx-border-radius:12;-fx-background-radius:12;");

        Region sidebarSpacer = spacer();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, nav, sidebarSpacer, settings, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color:" + BG_SIDEBAR + ";-fx-border-color:" + SIDEBAR_BORDER + ";-fx-border-width:0 1 0 0;");

        Button bell = new Button("🔔");
        bell.setStyle("-fx-background-color:transparent;-fx-font-size:16px;-fx-text-fill:" + LIGHT + ";-fx-cursor:hand;");
        bell.setOnAction(e -> LandingPage.showNotificationPage());

        String initials = getInitials(displayName);
        Label topAvatar = createAvatar(initials, 34);
        Label topName = label(getFirstName(displayName), 13, FontWeight.SEMI_BOLD, LIGHT);
        Label dropdown = label("⌄", 13, FontWeight.NORMAL, MUTED_LIGHT);

        HBox profileOption = new HBox(8, topAvatar, topName, dropdown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(5, 8, 5, 8));
        profileOption.setStyle("-fx-background-color:#26354A;-fx-background-radius:8;-fx-cursor:hand;");
        profileOption.setOnMouseClicked(e -> LandingPage.showUserProfilePage());
        profileOption.setOnMouseEntered(e -> profileOption.setStyle("-fx-background-color:#344762;-fx-background-radius:8;-fx-cursor:hand;"));
        profileOption.setOnMouseExited(e -> profileOption.setStyle("-fx-background-color:#26354A;-fx-background-radius:8;-fx-cursor:hand;"));

        HBox profileBox = new HBox(10, bell, profileOption);
        profileBox.setAlignment(Pos.CENTER);

        Region topSpacer = spacer();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        HBox topBar = new HBox(20, topSpacer, profileBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color:" + BG_SIDEBAR + ";-fx-border-color:" + SIDEBAR_BORDER + ";-fx-border-width:0 0 1 0;");

        Button back = new Button("← Dashboard");
        back.setStyle("-fx-background-color:" + BG_INNER + ";-fx-text-fill:" + DARK + ";-fx-border-color:" + BORDER + ";-fx-border-radius:8;-fx-background-radius:8;-fx-font-family:" + FONT + ";-fx-font-size:12px;-fx-font-weight:600;-fx-padding:5 10;-fx-cursor:hand;");
        back.setOnAction(e -> LandingPage.showUserDashboard());

        HBox backRow = new HBox(back);
        backRow.setAlignment(Pos.CENTER_RIGHT);
        backRow.setPadding(new Insets(12, ResponsiveUtil.PAGE_PADDING, 0, ResponsiveUtil.PAGE_PADDING));

        Label title = label("My Profile", 24, FontWeight.BOLD, LIGHT);
        Label description = label("Manage your OneSpace account information and profile settings.", 13, FontWeight.MEDIUM, MUTED_LIGHT);
        VBox headerText = new VBox(4, title, description);

        saveStatus = label("", 11, FontWeight.SEMI_BOLD, "#86EFAC");
        Region headerSpacer = spacer();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox pageHeader = new HBox(headerText, headerSpacer, saveStatus);
        pageHeader.setAlignment(Pos.CENTER_LEFT);

        profileAvatar = createAvatar(initials, 92);
        profileNameLabel = label(displayName, 22, FontWeight.BOLD, DARK);

        Button addPhotoButton = new Button("📷 Add Photo");
        addPhotoButton.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        addPhotoButton.setStyle("-fx-background-color:" + BG_INNER + ";-fx-text-fill:" + DARK + ";-fx-border-color:" + BORDER + ";-fx-border-radius:8;-fx-background-radius:8;-fx-padding:6 14;-fx-cursor:hand;");
        addPhotoButton.setOnAction(e -> chooseProfilePhoto());

        VBox avatarBox = new VBox(10, profileAvatar, addPhotoButton);
        avatarBox.setAlignment(Pos.CENTER);

        profileEmailLabel = label(email, 12, FontWeight.NORMAL, MUTED);
        profileUsernameLabel = label(currentUsername, 12, FontWeight.SEMI_BOLD, BLUE);
        Label memberSince = label("OneSpace Account", 11, FontWeight.NORMAL, MUTED);
        profileBioLabel = label(currentBio, 12, FontWeight.NORMAL, MUTED);
        profileBioLabel.setWrapText(true);

        VBox profileInfo = new VBox(5, profileNameLabel, profileEmailLabel, profileUsernameLabel, memberSince, profileBioLabel);
        profileInfo.setAlignment(Pos.CENTER_LEFT);

        Button editButton = primaryButton("Edit Profile", 11);
        editButton.setOnAction(e -> showEditProfileDialog());

        Button logoutButton = new Button("🚪 Logout");
        logoutButton.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        logoutButton.setStyle(secondaryStyle());
        logoutButton.setOnAction(e -> handleLogout());

        VBox profileActions = new VBox(8, editButton, logoutButton);
        profileActions.setAlignment(Pos.CENTER_RIGHT);

        Region profileSpacer = spacer();
        HBox.setHgrow(profileSpacer, Priority.ALWAYS);

        HBox profileSummary = new HBox(20, avatarBox, profileInfo, profileSpacer, profileActions);
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

        Label passIcon = label("🔑", 18, FontWeight.NORMAL, BLUE);
        passIcon.setPrefSize(40, 40);
        passIcon.setAlignment(Pos.CENTER);
        passIcon.setStyle("-fx-background-color:#DBEAFE;-fx-background-radius:9;-fx-text-fill:" + BLUE + ";");

        Label passTitle = label("Change Password", 13, FontWeight.BOLD, DARK);
        Label passText = label("Update your password to keep your account secure.", 11, FontWeight.NORMAL, MUTED);
        passText.setWrapText(true);
        VBox passInfo = new VBox(3, passTitle, passText);

        Region passSpacer = spacer();
        HBox.setHgrow(passSpacer, Priority.ALWAYS);

        Button passButton = new Button("Change Password");
        passButton.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        passButton.setStyle("-fx-background-color:" + BG_INNER + ";-fx-text-fill:" + DARK + ";-fx-border-color:" + BORDER + ";-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;-fx-padding:8 12;");
        passButton.setOnAction(e -> showChangePasswordDialog());

        HBox passRow = new HBox(12, passIcon, passInfo, passSpacer, passButton);
        passRow.setAlignment(Pos.CENTER_LEFT);
        passRow.setPadding(new Insets(16));
        passRow.setStyle("-fx-background-color:" + BG_INNER + ";-fx-border-color:" + BORDER + ";-fx-border-radius:11;-fx-background-radius:11;");

        Label deleteIcon = label("⚠", 18, FontWeight.NORMAL, "#DC2626");
        deleteIcon.setPrefSize(40, 40);
        deleteIcon.setAlignment(Pos.CENTER);
        deleteIcon.setStyle("-fx-background-color:#FEE2E2;-fx-background-radius:9;-fx-text-fill:#DC2626;");

        Label deleteTitle = label("Delete Account", 13, FontWeight.BOLD, DARK);
        Label deleteText = label("Permanently remove your OneSpace account and associated data.", 11, FontWeight.NORMAL, MUTED);
        deleteText.setWrapText(true);
        VBox deleteInfo = new VBox(3, deleteTitle, deleteText);

        Region deleteSpacer = spacer();
        HBox.setHgrow(deleteSpacer, Priority.ALWAYS);

        Button deleteButton = new Button("Delete Account");
        deleteButton.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        deleteButton.setStyle("-fx-background-color:#FEE2E2;-fx-text-fill:#B91C1C;-fx-border-color:#FECACA;-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;-fx-padding:8 12;");
        deleteButton.setOnAction(e -> showDeleteAccountDialog());

        HBox deleteRow = new HBox(12, deleteIcon, deleteInfo, deleteSpacer, deleteButton);
        deleteRow.setAlignment(Pos.CENTER_LEFT);
        deleteRow.setPadding(new Insets(16));
        deleteRow.setStyle("-fx-background-color:" + BG_INNER + ";-fx-border-color:" + BORDER + ";-fx-border-radius:11;-fx-background-radius:11;");

        VBox actionsCard = card(
                cardTitle("Account Actions"),
                cardDescription("Manage important actions related to your account."),
                new VBox(10, passRow, deleteRow)
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
        HBox actionButtons = new HBox(8, actionSpacer, resetButton, saveButton);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);

        VBox content = new VBox(20, pageHeader, profileSummary, detailsCard, accountCard, actionsCard, actionButtons);
        content.setPadding(new Insets(14, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color:" + BG_CENTER + ";");

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color:" + BG_CENTER + ";-fx-background:" + BG_CENTER + ";-fx-background-insets:0;-fx-padding:0;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox mainArea = new VBox(topBar, backRow, scroll);
        VBox.setVgrow(content, Priority.ALWAYS);
        mainArea.setStyle("-fx-background-color:" + BG_CENTER + ";");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(mainArea);
        root.setStyle("-fx-background-color:" + BG_SIDEBAR + ";");

        loadProfileFromFirestore();

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private void showChangePasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Update your account password");

        PasswordField oldPass = new PasswordField();
        oldPass.setPrefHeight(40);
        oldPass.setStyle("-fx-background-color:#FFFFFF;-fx-border-color:" + BORDER + ";-fx-border-radius:8;-fx-background-radius:8;-fx-padding:0 12;-fx-text-fill:" + DARK + ";-fx-font-size:12px;");

        PasswordField newPass = new PasswordField();
        newPass.setPrefHeight(40);
        newPass.setStyle("-fx-background-color:#FFFFFF;-fx-border-color:" + BORDER + ";-fx-border-radius:8;-fx-background-radius:8;-fx-padding:0 12;-fx-text-fill:" + DARK + ";-fx-font-size:12px;");

        PasswordField confirmPass = new PasswordField();
        confirmPass.setPrefHeight(40);
        confirmPass.setStyle("-fx-background-color:#FFFFFF;-fx-border-color:" + BORDER + ";-fx-border-radius:8;-fx-background-radius:8;-fx-padding:0 12;-fx-text-fill:" + DARK + ";-fx-font-size:12px;");

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
        updateBtn.setStyle("-fx-background-color:" + BLUE + ";-fx-text-fill:white;-fx-font-weight:bold;-fx-background-radius:7;-fx-cursor:hand;");

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

    private void loadProfileFromFirestore() {
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
                    profileAvatar.setStyle("-fx-background-color:" + BLUE + ";-fx-background-radius:50%;-fx-text-fill:white;");
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
        profileAvatar.setStyle("-fx-background-color:" + BLUE + ";-fx-background-radius:50%;-fx-text-fill:white;");
        saveStatus.setText("");
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
        saveButton.setStyle("-fx-background-color:" + BLUE + ";-fx-text-fill:white;-fx-font-weight:bold;-fx-background-radius:7;-fx-cursor:hand;");

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
            profileAvatar.setStyle("-fx-background-color:" + BLUE + ";-fx-background-radius:50%;");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Photo Error", "Unable to load the selected image.");
        }
    }

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Logout from OneSpace?");
        alert.setContentText("Are you sure you want to logout?");

        ButtonType logout = new ButtonType("Logout", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(cancel, logout);
        styleDialog(alert);

        alert.showAndWait().ifPresent(result -> {
            if (result == logout) {
                UserSession.clearSession();
                LandingPage.showLandingPage();
            }
        });
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
                showAlert(Alert.AlertType.INFORMATION, "Account Deleted", "Account deletion is not implemented yet.");
            }
        });
    }

    private Scene createUnauthenticatedScene() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color:" + BG_CENTER + ";");

        Label title = label("No Active Session", 22, FontWeight.BOLD, LIGHT);
        Label message = label("Please sign in to view your profile.", 13, FontWeight.NORMAL, MUTED_LIGHT);

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

    private StackPane createLogo() {
        Image image = new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
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
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, size >= 70 ? 24 : 12));
        avatar.setStyle("-fx-background-color:" + BLUE + ";-fx-background-radius:50%;-fx-text-fill:white;");
        return avatar;
    }

    private Button sidebarButton(String icon, String text, boolean active) {
        Label iconLabel = label(icon, 14, FontWeight.NORMAL, active ? LIGHT : MUTED_LIGHT);
        Label textLabel = label(text, 13, active ? FontWeight.BOLD : FontWeight.MEDIUM, LIGHT);

        HBox content = new HBox(12, iconLabel, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("", content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(38);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        String normal = active ? "-fx-background-color:" + BLUE + ";" : "-fx-background-color:transparent;";
        button.setStyle(normal + "-fx-background-radius:8;-fx-cursor:hand;");

        if (!active) {
            button.setOnMouseEntered(e -> button.setStyle("-fx-background-color:#26354A;-fx-background-radius:8;-fx-cursor:hand;"));
            button.setOnMouseExited(e -> button.setStyle("-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;"));
        }
        return button;
    }

    private Label label(String text, double size, FontWeight weight, String color) {
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
        button.setOnMouseEntered(e -> button.setStyle(primaryStyle(LIGHT_BLUE)));
        button.setOnMouseExited(e -> button.setStyle(primaryStyle(BLUE)));
        return button;
    }

    private String primaryStyle(String color) {
        return "-fx-background-color:" + color + ";-fx-text-fill:white;-fx-background-radius:9;-fx-cursor:hand;-fx-padding:10 22;";
    }

    private Button flatButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        button.setStyle("-fx-background-color:transparent;-fx-text-fill:" + color + ";-fx-padding:2 0 0 0;-fx-cursor:hand;");
        return button;
    }

    private String secondaryStyle() {
        return "-fx-background-color:" + BG_INNER + ";-fx-text-fill:" + DARK + ";-fx-border-color:" + BORDER + ";-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;-fx-padding:8 14;";
    }

    private TextField textField(String value) {
        TextField field = new TextField(value);
        field.setPrefHeight(40);
        field.setStyle("-fx-background-color:#FFFFFF;-fx-border-color:" + BORDER + ";-fx-border-radius:8;-fx-background-radius:8;-fx-padding:0 12;-fx-text-fill:" + DARK + ";-fx-font-size:12px;");
        return field;
    }

    private TextArea textArea(String value) {
        TextArea area = new TextArea(value);
        area.setWrapText(true);
        area.setStyle(textAreaStyle());
        return area;
    }

    private String textAreaStyle() {
        return "-fx-background-color:#FFFFFF;-fx-border-color:" + BORDER + ";-fx-border-radius:8;-fx-background-radius:8;-fx-padding:8 12;-fx-text-fill:" + DARK + ";-fx-font-size:12px;";
    }

    private VBox fieldBox(String title, Control field) {
        Label label = label(title, 11, FontWeight.SEMI_BOLD, DARK);
        return new VBox(6, label, field);
    }

    private VBox card(Node... children) {
        VBox box = new VBox(8, children);
        box.setPadding(new Insets(22));
        box.setStyle(cardStyle());
        return box;
    }

    private Label cardTitle(String text) {
        return label(text, 17, FontWeight.BOLD, DARK);
    }

    private Label cardDescription(String text) {
        Label label = label(text, 11, FontWeight.NORMAL, MUTED);
        label.setWrapText(true);
        return label;
    }

    private String cardStyle() {
        return "-fx-background-color:" + BG_CARD + ";-fx-border-color:" + BORDER + ";-fx-border-radius:14;-fx-background-radius:14;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.16),12,0,0,4);";
    }

    private HBox infoRow(String title, String value) {
        Label titleLabel = label(title, 11, FontWeight.SEMI_BOLD, MUTED);
        Label valueLabel = label(value, 12, FontWeight.BOLD, DARK);
        Region spacer = spacer();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(titleLabel, spacer, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 4, 12, 4));
        row.setStyle("-fx-border-color:transparent transparent " + BORDER + " transparent;-fx-border-width:0 0 1 0;");
        return row;
    }

    private Region spacer() {
        return new Region();
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
        dialog.getDialogPane().setStyle("-fx-background-color:" + BG_CARD + ";-fx-border-color:" + BORDER + ";");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleDialog(alert);
        alert.showAndWait();
    }
}