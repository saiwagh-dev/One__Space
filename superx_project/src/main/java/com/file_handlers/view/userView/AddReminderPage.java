package com.file_handlers.view.userView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.dao.ReminderDAO;
import com.file_handlers.model.FileData;
import com.file_handlers.model.Reminder;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;
import com.google.cloud.Timestamp;

public class AddReminderPage {
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

    private TextField titleField, reminderTimeField;
    private TextArea descriptionField;
    private ComboBox<String> reminderTypeCombo, repeatCombo, priorityCombo;
    private DatePicker reminderDatePicker;
    private Label selectedFileLabel, previewReminderType, previewTitle, previewDescription;
    private Label previewDate, previewTime, previewRepeat, previewPriority;
    private String selectedFileId;
    private String selectedFileName;

    public Scene getAddReminderPageScene() {
        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + WHITE + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = createSidebarButton("dashboard", "Dashboard", false);
        Button spaces = createSidebarButton("files", "Spaces", false);
        Button search = createSidebarButton("search", "Search", false);
        Button calendar = createSidebarButton("calendar", "Calendar", true);
        Button ai = createSidebarButton("ai", "AI Assistant", false);
        Button collab = createSidebarButton("collaboration", "Collaboration", false);
        Button recent = createSidebarButton("recent", "Recent", false);
        Button trash = createSidebarButton("trash", "Trash", false);
        Button settings = createSidebarButton("settings", "Settings", false);

        dashboard.setOnAction(e -> LandingPage.showUserDashboard());
        spaces.setOnAction(e -> LandingPage.showUserSpace());
        search.setOnAction(e -> LandingPage.showUserSearch());
        calendar.setOnAction(e -> LandingPage.showCalendarPage());
        ai.setOnAction(e -> LandingPage.showAiAssistantPage());
        collab.setOnAction(e -> LandingPage.showCollaborationPage());
        recent.setOnAction(e -> LandingPage.showRecentPage());
        trash.setOnAction(e -> LandingPage.showTrashPage());
        settings.setOnAction(e -> LandingPage.showSettingPage());

        VBox nav = new VBox(4, dashboard, spaces, search, calendar, ai, collab, recent, trash);

        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageTitle.setStyle("-fx-text-fill: " + WHITE + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-text-fill: " + WHITE + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        HBox storageValGroup = new HBox(storageVal, new Region(), storagePercent);
        HBox.setHgrow(storageValGroup.getChildren().get(1), Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar progress = new ProgressBar(0.64);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle("-fx-accent: " + BLUE + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        Button manageStorageBtn = new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexPage());

        VBox storage = new VBox(8, storageTitle, storageValGroup, progress, manageStorageBtn);
        storage.setPadding(new Insets(14));
        storage.setStyle("-fx-background-color: rgba(16, 28, 48, 0.65); -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sideSpace = space();
        VBox.setVgrow(sideSpace, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, nav, sideSpace, settings, storage);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        HBox topBar = createTopBar();

        Label title = new Label("Add Reminder");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label desc = new Label("Set a reminder for your important document or task.");
        desc.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        desc.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        Button close = new Button("×");
        close.setPrefSize(38, 38);
        close.setFont(Font.font(FONT, FontWeight.BOLD, 20));
        close.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: " + WHITE + "; -fx-cursor: hand;");
        close.setOnMouseEntered(e -> close.setStyle("-fx-background-color: rgba(239, 68, 68, 0.2); -fx-border-color: rgba(239, 68, 68, 0.4); -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: #F87171; -fx-cursor: hand;"));
        close.setOnMouseExited(e -> close.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: " + WHITE + "; -fx-cursor: hand;"));
        close.setOnAction(e -> LandingPage.showCalendarPage());

        Region headerSpace = space();
        HBox header = new HBox(new VBox(4, title, desc), headerSpace, close);
        header.setAlignment(Pos.CENTER_LEFT);

        titleField = new TextField();
        titleField.setPromptText("E.g., Passport Expiry, Insurance Renewal");
        styleTextField(titleField);

        descriptionField = new TextArea();
        descriptionField.setPromptText("Add more details about this reminder...");
        descriptionField.setWrapText(true);
        descriptionField.setPrefRowCount(3);
        descriptionField.setStyle("-fx-control-inner-background: " + INPUT_BG + "; -fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-prompt-text-fill: " + LIGHT_SECONDARY + "; -fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8;");

        reminderTypeCombo = new ComboBox<>();
        reminderTypeCombo.getItems().addAll("Document Reminder", "Task Reminder", "Event Reminder", "Deadline Reminder");
        reminderTypeCombo.setValue("Document Reminder");
        styleCombo(reminderTypeCombo);

        Button choose = new Button("📄  Choose a file");
        choose.setMaxWidth(Double.MAX_VALUE);
        choose.setPrefHeight(42);
        choose.setAlignment(Pos.CENTER_LEFT);
        choose.setTextFill(Color.web(WHITE));
        choose.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-family: " + FONT + "; -fx-cursor: hand;");
        choose.setOnAction(e -> chooseDocument());

        selectedFileLabel = new Label("No file selected");
        selectedFileLabel.setFont(Font.font(FONT, 11));
        selectedFileLabel.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        reminderDatePicker = new DatePicker();
        reminderDatePicker.setPromptText("dd/mm/yyyy");
        reminderDatePicker.setPrefHeight(42);
        reminderDatePicker.setMaxWidth(Double.MAX_VALUE);
        reminderDatePicker.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-control-inner-background: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-font-family: " + FONT + ";");

        reminderTimeField = new TextField();
        reminderTimeField.setPromptText("--:-- --");
        styleTextField(reminderTimeField);

        repeatCombo = new ComboBox<>();
        repeatCombo.getItems().addAll("Does not repeat", "Every day", "Every week", "Every month", "Every year");
        repeatCombo.setValue("Does not repeat");
        styleCombo(repeatCombo);

        priorityCombo = new ComboBox<>();
        priorityCombo.getItems().addAll("High", "Medium", "Low");
        priorityCombo.setValue("Medium");
        styleCombo(priorityCombo);

        CheckBox notification = new CheckBox();
        notification.setSelected(true);
        notification.setStyle("-fx-text-fill: " + WHITE + ";");

        Label notifTitle = new Label("Enable notification");
        notifTitle.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        notifTitle.setStyle("-fx-text-fill: " + WHITE + ";");

        Label notifSub = new Label("You will be notified on the selected date and time.");
        notifSub.setFont(Font.font(FONT, 11));
        notifSub.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        HBox notificationBox = new HBox(12, notification, new VBox(2, notifTitle, notifSub));
        notificationBox.setAlignment(Pos.CENTER_LEFT);

        VBox reminderTypeBox = new VBox(6, fieldLabel("Reminder Type"), reminderTypeCombo);
        VBox documentBox = new VBox(6, fieldLabel("Select Document (Optional)"), choose, selectedFileLabel);

        HBox typeFile = new HBox(18, reminderTypeBox, documentBox);
        HBox.setHgrow(reminderTypeBox, Priority.ALWAYS);
        HBox.setHgrow(documentBox, Priority.ALWAYS);

        HBox dateTime = row(field("Reminder Date *", reminderDatePicker), field("Reminder Time", reminderTimeField));
        HBox repeatPriority = row(field("Repeat", repeatCombo), field("Priority", priorityCombo));

        VBox details = new VBox(16, section("Reminder Details"), fieldLabel("Title *"), titleField, fieldLabel("Description"), descriptionField, typeFile, dateTime, repeatPriority, notificationBox);
        details.setPadding(new Insets(24));
        details.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");

        previewReminderType = new Label(reminderTypeCombo.getValue());
        previewReminderType.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        previewReminderType.setStyle("-fx-background-color: rgba(37, 99, 235, 0.2); -fx-border-color: rgba(96, 165, 250, 0.4); -fx-border-radius: 6; -fx-text-fill: #60A5FA; -fx-padding: 4 8; -fx-background-radius: 6;");

        previewTitle = new Label("Reminder Title");
        previewTitle.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        previewTitle.setStyle("-fx-text-fill: " + WHITE + ";");

        previewDescription = new Label("Reminder description will appear here...");
        previewDescription.setFont(Font.font(FONT, 12));
        previewDescription.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
        previewDescription.setWrapText(true);

        previewDate = preview("▣", "Select reminder date");
        previewTime = preview("◷", "Select reminder time");
        previewRepeat = preview("⟳", "Does not repeat");
        previewPriority = preview("⚑", "Medium Priority");

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.web("#00D2FF"));
        bellIcon.setStrokeWidth(2);

        StackPane bellPane = new StackPane(bellIcon);
        bellPane.setPrefSize(32, 32); bellPane.setMinSize(32, 32); bellPane.setMaxSize(32, 32);
        bellPane.setStyle("-fx-background-color: rgba(0, 210, 255, 0.15); -fx-border-color: rgba(0, 210, 255, 0.3); -fx-border-radius: 8; -fx-background-radius: 8;");

        Separator previewSep = new Separator();
        previewSep.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08);");

        VBox previewInner = new VBox(14, bellPane, previewReminderType, previewTitle, previewDescription, previewSep, previewDate, previewTime, previewRepeat, previewPriority);
        previewInner.setPadding(new Insets(24));
        previewInner.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 14; -fx-background-radius: 14;");

        Label previewSub = new Label("This is how your reminder will appear.");
        previewSub.setFont(Font.font(FONT, 12));
        previewSub.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox previewCard = new VBox(8, section("Reminder Preview"), previewSub, previewInner);
        previewCard.setPadding(new Insets(24));
        previewCard.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");

        titleField.textProperty().addListener((o, a, b) -> previewTitle.setText(b.trim().isEmpty() ? "Reminder Title" : b.trim()));
        descriptionField.textProperty().addListener((o, a, b) -> previewDescription.setText(b.trim().isEmpty() ? "Reminder description will appear here..." : b.trim()));
        reminderTypeCombo.valueProperty().addListener((o, a, b) -> previewReminderType.setText(b));
        reminderDatePicker.valueProperty().addListener((o, a, b) -> previewDate.setText(b == null ? "▣  Select reminder date" : "▣  " + b.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))));
        reminderTimeField.textProperty().addListener((o, a, b) -> previewTime.setText(b.trim().isEmpty() ? "◷  Select reminder time" : "◷  " + b.trim()));
        repeatCombo.valueProperty().addListener((o, a, b) -> previewRepeat.setText("⟳  " + b));
        priorityCombo.valueProperty().addListener((o, a, b) -> previewPriority.setText("⚑  " + b + " Priority"));

        HBox columns = new HBox(20, details, previewCard);
        HBox.setHgrow(details, Priority.ALWAYS);
        HBox.setHgrow(previewCard, Priority.ALWAYS);

        Button cancel = new Button("Cancel");
        cancel.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        cancel.setPrefHeight(40);
        cancel.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: " + WHITE + "; -fx-padding: 8 20; -fx-cursor: hand;");
        cancel.setOnAction(e -> LandingPage.showCalendarPage());

        Button create = new Button("+  Create Reminder");
        create.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        create.setPrefHeight(40);
        create.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 8; -fx-border-width: 1; -fx-text-fill: #FFFFFF; -fx-background-radius: 8; -fx-padding: 8 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);");
        create.setOnAction(e -> createReminder());

        HBox buttons = new HBox(10, cancel, create);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox contentBody = new VBox(22, header, columns, buttons);
        contentBody.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        contentBody.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-background-insets: 0; -fx-padding: 0;");

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private HBox createTopBar() {
        String activeUserName = "User", initials = "U";

        UserSession session = UserSession.getInstance();
        if (session != null && session.getDisplayName() != null && !session.getDisplayName().isBlank()) {
            String fullName = session.getDisplayName().trim();
            activeUserName = fullName.split("\\s+")[0];
            initials = activeUserName.substring(0, 1).toUpperCase();
        }

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button bellBtn = new Button();
        bellBtn.setGraphic(bellIcon);
        bellBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34); avatar.setMinSize(34, 34); avatar.setMaxSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        Label userName = new Label(activeUserName);
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill: " + WHITE + ";");

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
            LandingPage.showUserProfilePage();
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
            LandingPage.showSettingPage();
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
            LandingPage.showUserLoginPage();
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

        return topBar;
    }

    private void createReminder() {
        String title = titleField.getText().trim();

        if (title.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Missing Title", "Please enter a reminder title.");
            titleField.requestFocus();
            return;
        }

        if (reminderDatePicker.getValue() == null) {
            alert(Alert.AlertType.WARNING, "Missing Date", "Please select a reminder date.");
            reminderDatePicker.requestFocus();
            return;
        }

        if (!UserSession.isLoggedIn()) {
            alert(Alert.AlertType.ERROR, "Not Logged In", "Please log in before creating a reminder.");
            return;
        }

        Reminder reminder = new Reminder();
        reminder.setTitle(title);
        reminder.setDescription(descriptionField.getText().trim());
        reminder.setType(reminderTypeCombo.getValue());
        reminder.setDate(Timestamp.of(Date.from(
            reminderDatePicker.getValue()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )));
        reminder.setTime(reminderTimeField.getText().trim().isEmpty()
                ? "Not specified" : reminderTimeField.getText().trim());
        reminder.setRepeat(repeatCombo.getValue());
        reminder.setPriority(priorityCombo.getValue());
        reminder.setLinkedFileId(selectedFileId);
        reminder.setLinkedFileName(selectedFileName);

        try {
            String id = new ReminderDAO().saveReminder(
                UserSession.getInstance().getUid(),
                reminder
            );

            System.out.println("[REMINDER] Saved: " + id);

            alert(
                Alert.AlertType.INFORMATION,
                "Reminder Created",
                "Reminder saved successfully."
            );

            LandingPage.showCalendarPage();

        } catch (Exception e) {
            e.printStackTrace();

            alert(
                Alert.AlertType.ERROR,
                "Could Not Save Reminder",
                e.getMessage() == null ? "Unable to save reminder." : e.getMessage()
            );
        }
    }

    private Timestamp toTimestamp(java.time.LocalDate date) {
        return Timestamp.of(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    private void chooseDocument() {
        if (!UserSession.isLoggedIn()) {
            alert(Alert.AlertType.WARNING, "Not Logged In", "Please log in first.");
            return;
        }

        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Document");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Documents", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        if (selectedFileLabel.getScene() == null) return;

        File file = fc.showOpenDialog(selectedFileLabel.getScene().getWindow());

        if (file == null) return;

        try {
            List<FileData> files = new FileDAO().getAllFiles(UserSession.getInstance().getUid());

            for (FileData data : files) {
                if (file.getAbsolutePath().equals(data.getLocalPath())) {
                    selectedFileId = data.getFileHash();
                    selectedFileName = data.getFileName();
                    selectedFileLabel.setText("Selected: " + data.getFileName());
                    return;
                }
            }

            selectedFileId = null;
            selectedFileName = null;
            selectedFileLabel.setText("Not indexed in OneSpace");

        } catch (Exception e) {
            alert(Alert.AlertType.ERROR, "File Error", "Could not check the selected file.");
        }
    }

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

    private Button createSidebarButton(String iconType, String label, boolean isActive) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(isActive ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label textLbl = new Label(label);
        textLbl.setFont(Font.font(FONT, isActive ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        textLbl.setTextFill(Color.web(WHITE));

        HBox content = new HBox(12, iconBox, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 12, 0, 12));

        if (isActive) {
            btn.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
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
                btn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.WHITE);
                textLbl.setTextFill(Color.WHITE);
            });
            btn.setOnMouseExited(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                textLbl.setTextFill(Color.web(WHITE));
            });
        }

        return btn;
    }

    private Label fieldLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        lbl.setStyle("-fx-text-fill: " + WHITE + ";");
        return lbl;
    }

    private Label section(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        lbl.setStyle("-fx-text-fill: " + WHITE + ";");
        return lbl;
    }

    private void styleTextField(TextField f) {
        f.setPrefHeight(42);
        f.setMaxWidth(Double.MAX_VALUE);
        f.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-control-inner-background: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-prompt-text-fill: " + LIGHT_SECONDARY + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-family: " + FONT + "; -fx-font-size: 13px;");
    }

    private void styleCombo(ComboBox<String> c) {
        c.setPrefHeight(42);
        c.setMaxWidth(Double.MAX_VALUE);
        c.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-border-color: " + INPUT_BORDER + "; -fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: " + WHITE + "; -fx-border-radius: 8; -fx-background-radius: 8;");
    }

    private VBox field(String name, Control control) {
        return new VBox(6, fieldLabel(name), control);
    }

    private HBox row(Pane a, Pane b) {
        HBox h = new HBox(18, a, b);
        HBox.setHgrow(a, Priority.ALWAYS);
        HBox.setHgrow(b, Priority.ALWAYS);
        return h;
    }

    private Label preview(String icon, String text) {
        Label lbl = new Label(icon + "  " + text);
        lbl.setFont(Font.font(FONT, 13));
        lbl.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
        return lbl;
    }

    private Region space() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
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
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }

    private void alert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}