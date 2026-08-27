package com.file_handlers.view.userView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.format.DateTimeFormatter;

import com.file_handlers.view.LandingPage;

public class AddReminderPage {

    // Style Constants - Synchronized with UserDashboard.java
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar Layout Colors
    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";

    // 2. Center Workspace Canvas Color
    private static final String BG_CENTER_CANVAS = "#31435B";

    // 3. Card Surface & Input Colors
    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_CARD_INNER = "#CADDF2";
    private static final String BORDER_CARD = "#C3D6EC";
    private static final String BG_INPUT = "#EDF3FA";

    // 4. Typography Color System
    private static final String TEXT_DARK = "#0F172A";
    private static final String TEXT_MUTED_DARK = "#334155";
    private static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#94A3B8";

    // Accent Colors
    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String BADGE_BLUE_BG = "#BFDBFE";

    // Class Variables
    private TextField titleField, reminderTimeField;
    private TextArea descriptionField;
    private ComboBox<String> reminderTypeCombo, repeatCombo, priorityCombo;
    private DatePicker reminderDatePicker;
    private Label selectedFileLabel, previewReminderType, previewTitle, previewDescription;
    private Label previewDate, previewTime, previewRepeat, previewPriority;

    public Scene getAddReminderPageScene() {

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

        Button dashboard = createSidebarButton("⌂", "Dashboard", false);
        Button spaces = createSidebarButton("📁", "Spaces", false);
        Button search = createSidebarButton("⌕", "Search", false);
        Button calendar = createSidebarButton("📅", "Calendar", true);
        Button ai = createSidebarButton("✧", "AI Assistant", false);
        Button collab = createSidebarButton("👥", "Collaboration", false);
        Button recent = createSidebarButton("🕒", "Recent", false);
        Button trash = createSidebarButton("🗑", "Trash", false);
        Button settings = createSidebarButton("⚙", "Settings", false);
        Button logoutBtn = createSidebarButton("🚪", "Logout", false);


        dashboard.setOnAction(e -> LandingPage.showUserDashboard());
        spaces.setOnAction(e -> LandingPage.showUserSpace());
        search.setOnAction(e -> LandingPage.showUserSearch());
        calendar.setOnAction(e -> LandingPage.showCalendarPage());
        ai.setOnAction(e -> LandingPage.showAiAssistantPage());
        collab.setOnAction(e -> LandingPage.showCollaborationPage());
        recent.setOnAction(e -> LandingPage.showRecentPage());
        trash.setOnAction(e -> LandingPage.showTrashPage());
        settings.setOnAction(e -> LandingPage.showSettingPage());
        logoutBtn.setOnAction(e -> LandingPage.showUserLoginPage());


        VBox nav = new VBox(4, dashboard, spaces, search, calendar, ai, collab, recent, trash, settings, logoutBtn);

        // Sidebar Storage Card
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

        ProgressBar progress = new ProgressBar(0.64);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle("-fx-accent: " + PRIMARY_BLUE + "; -fx-control-inner-background: #0E1520;");

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showLandingPage());

        VBox storage = new VBox(8, storageTitle, storageValGroup, progress, manageStorageBtn);
        storage.setPadding(new Insets(14));
        storage.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sideSpace = space();
        VBox.setVgrow(sideSpace, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, nav, sideSpace, settings, storage);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        // =========================================================
        // TOP SEARCH BAR & PROFILE
        // =========================================================

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        TextField searchField = new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label shortcut = new Label("⌘ K");
        shortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        shortcut.setStyle("-fx-background-color: #141E2C; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 6; -fx-background-radius: 4;");

        HBox searchBox = new HBox(8, searchIcon, searchField, shortcut);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 12, 0, 14));
        searchBox.setPrefWidth(420);
        searchBox.setStyle("-fx-background-color: #141E2C; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button bell = new Button("🔔");
        bell.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand;");

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
                "-fx-background-color: " + PRIMARY_BLUE + ";" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;"
        );

        Label userName = new Label("Aarav Verma");
        userName.setFont(
                Font.font(FONT, FontWeight.SEMI_BOLD, 13)
        );
        userName.setStyle(
                "-fx-text-fill: " + TEXT_LIGHT + ";"
        );

        Label dropDown = new Label("⌄");
        dropDown.setStyle(
                "-fx-text-fill: " + TEXT_MUTED_LIGHT + ";"
        );



        HBox profileOption =
                new HBox(
                        8,
                        avatar,
                        userName,
                        dropDown
                );

        profileOption.setAlignment(
                Pos.CENTER
        );

        profileOption.setPadding(
                new Insets(5, 8, 5, 8)
        );

        profileOption.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );



        profileOption.setOnMouseClicked(e -> {
            LandingPage.showUserProfilePage();
        });



        profileOption.setOnMouseEntered(e -> {
            profileOption.setStyle(
                    "-fx-background-color: #26354A;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
            );
        });

        profileOption.setOnMouseExited(e -> {
            profileOption.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
            );
        });


        
        HBox profileBox =
                new HBox(
                        10,
                        bell,
                        profileOption
                );

        profileBox.setAlignment(
                Pos.CENTER
        );



        HBox topBar =
                new HBox(
                        20,
                        searchBox,
                        new Region(),
                        profileBox
                );

        HBox.setHgrow(
                topBar.getChildren().get(1),
                Priority.ALWAYS
        );

        topBar.setAlignment(
                Pos.CENTER_LEFT
        );

        topBar.setPadding(
                new Insets(
                        16,
                        28,
                        14,
                        28
                )
        );

        topBar.setStyle(
                "-fx-background-color: " + BG_SIDEBAR + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 0 1 0;"
        );
        
        Label title = new Label("Add Reminder");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label desc = new Label("Set a reminder for your important document or task.");
        desc.setFont(Font.font(FONT, 13));
        desc.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        Button close = new Button("×");
        close.setPrefSize(38, 38);
        close.setFont(Font.font(FONT, FontWeight.BOLD, 20));
        close.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " + TEXT_DARK + ";" +
                "-fx-cursor: hand;"
        );
        close.setOnAction(e -> LandingPage.showCalendarPage());

        Region headerSpace = space();
        HBox header = new HBox(new VBox(4, title, desc), headerSpace, close);
        header.setAlignment(Pos.CENTER_LEFT);

        // =========================================================
        // FORM CONTROLS
        // =========================================================

        titleField = new TextField();
        titleField.setPromptText("E.g., Passport Expiry, Insurance Renewal");
        styleTextField(titleField);

        descriptionField = new TextArea();
        descriptionField.setPromptText("Add more details about this reminder...");
        descriptionField.setWrapText(true);
        descriptionField.setPrefRowCount(3);
        descriptionField.setStyle(
                "-fx-control-inner-background: " + BG_INPUT + ";" +
                "-fx-background-color: " + BG_INPUT + ";" +
                "-fx-text-fill: " + TEXT_DARK + ";" +
                "-fx-prompt-text-fill: " + TEXT_MUTED_DARK + ";" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 13px;" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );

        reminderTypeCombo = new ComboBox<>();
        reminderTypeCombo.getItems().addAll("Document Reminder", "Task Reminder", "Event Reminder", "Deadline Reminder");
        reminderTypeCombo.setValue("Document Reminder");
        styleCombo(reminderTypeCombo);

        Button choose = new Button("📄  Choose a file");
        choose.setMaxWidth(Double.MAX_VALUE);
        choose.setPrefHeight(42);
        choose.setAlignment(Pos.CENTER_LEFT);
        choose.setTextFill(Color.web(TEXT_DARK));
        choose.setStyle(
                "-fx-background-color: " + BG_INPUT + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-cursor: hand;"
        );
        choose.setOnAction(e -> chooseDocument());

        selectedFileLabel = new Label("No file selected");
        selectedFileLabel.setFont(Font.font(FONT, 11));
        selectedFileLabel.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        reminderDatePicker = new DatePicker();
        reminderDatePicker.setPromptText("dd/mm/yyyy");
        reminderDatePicker.setPrefHeight(42);
        reminderDatePicker.setMaxWidth(Double.MAX_VALUE);
        reminderDatePicker.setStyle("-fx-background-color: " + BG_INPUT + "; -fx-font-family: " + FONT + ";");

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

        Label notifTitle = new Label("Enable notification");
        notifTitle.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        notifTitle.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label notifSub = new Label("You will be notified on the selected date and time.");
        notifSub.setFont(Font.font(FONT, 11));
        notifSub.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        HBox notificationBox = new HBox(12, notification, new VBox(2, notifTitle, notifSub));
        notificationBox.setAlignment(Pos.CENTER_LEFT);

        VBox reminderTypeBox = new VBox(6, fieldLabel("Reminder Type"), reminderTypeCombo);
        VBox documentBox = new VBox(6, fieldLabel("Select Document (Optional)"), choose, selectedFileLabel);

        HBox typeFile = new HBox(18, reminderTypeBox, documentBox);
        HBox.setHgrow(reminderTypeBox, Priority.ALWAYS);
        HBox.setHgrow(documentBox, Priority.ALWAYS);

        HBox dateTime = row(field("Reminder Date *", reminderDatePicker), field("Reminder Time", reminderTimeField));
        HBox repeatPriority = row(field("Repeat", repeatCombo), field("Priority", priorityCombo));

        VBox details = new VBox(16,
                section("Reminder Details"),
                fieldLabel("Title *"), titleField,
                fieldLabel("Description"), descriptionField,
                typeFile, dateTime, repeatPriority, notificationBox
        );

        details.setPadding(new Insets(24));
        details.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.14), 12, 0, 0, 4);"
        );

        // =========================================================
        // PREVIEW CARD
        // =========================================================

        previewReminderType = new Label(reminderTypeCombo.getValue());
        previewReminderType.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        previewReminderType.setStyle(
                "-fx-background-color: " + BADGE_BLUE_BG + ";" +
                "-fx-text-fill: " + PRIMARY_BLUE + ";" +
                "-fx-padding: 5 8;" +
                "-fx-background-radius: 5;"
        );

        previewTitle = new Label("Reminder Title");
        previewTitle.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        previewTitle.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        previewDescription = new Label("Reminder description will appear here...");
        previewDescription.setFont(Font.font(FONT, 12));
        previewDescription.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
        previewDescription.setWrapText(true);

        previewDate = preview("▣", "Select reminder date");
        previewTime = preview("◷", "Select reminder time");
        previewRepeat = preview("⟳", "Does not repeat");
        previewPriority = preview("⚑", "Medium Priority");

        Label previewIcon = new Label("🔔");
        previewIcon.setFont(Font.font(FONT, 20));
        previewIcon.setStyle("-fx-text-fill: " + PRIMARY_BLUE + ";");

        VBox previewInner = new VBox(14,
                previewIcon,
                previewReminderType, previewTitle,
                previewDescription, new Separator(),
                previewDate, previewTime, previewRepeat, previewPriority
        );

        previewInner.setPadding(new Insets(24));
        previewInner.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;"
        );

        Label previewSub = new Label("This is how your reminder will appear.");
        previewSub.setFont(Font.font(FONT, 12));
        previewSub.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox previewCard = new VBox(8,
                section("Reminder Preview"),
                previewSub,
                previewInner
        );

        previewCard.setPadding(new Insets(24));
        previewCard.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.14), 12, 0, 0, 4);"
        );

        // Dynamically reflect form updates in Live Preview
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

        // Action Buttons
        Button cancel = new Button("Cancel");
        cancel.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        cancel.setPrefHeight(40);
        cancel.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " + TEXT_DARK + ";" +
                "-fx-padding: 8 20;" +
                "-fx-cursor: hand;"
        );
        cancel.setOnAction(e -> LandingPage.showCalendarPage());

        Button create = new Button("+  Create Reminder");
        create.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        create.setPrefHeight(40);
        create.setStyle(
                "-fx-background-color: " + PRIMARY_BLUE + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 8 20;" +
                "-fx-cursor: hand;"
        );
        create.setOnAction(e -> createReminder());

        HBox buttons = new HBox(10, cancel, create);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox contentBody = new VBox(22, header, columns, buttons);
        contentBody.setPadding(new Insets(24, 28, 28, 28));
        contentBody.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: " + BG_CENTER_CANVAS + ";" +
                "-fx-background: " + BG_CENTER_CANVAS + ";" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_SIDEBAR + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1200, 750);
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

        String date = reminderDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        String time = reminderTimeField.getText().trim();
        if (time.isEmpty()) time = "Not specified";

        String msg = "Reminder created successfully.\n\n" +
                "Title: " + title + "\nType: " + reminderTypeCombo.getValue() +
                "\nDate: " + date + "\nTime: " + time +
                "\nRepeat: " + repeatCombo.getValue() + "\nPriority: " + priorityCombo.getValue();

        alert(Alert.AlertType.INFORMATION, "Reminder Created", msg);
        LandingPage.showCalendarPage();
    }

    private void chooseDocument() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Document");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Documents", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        if (selectedFileLabel.getScene() != null) {
            File file = fc.showOpenDialog(selectedFileLabel.getScene().getWindow());
            if (file != null) selectedFileLabel.setText("Selected: " + file.getName());
        }
    }

    private StackPane createOneSpaceLogo() {
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
            btn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 8; -fx-cursor: hand;");
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

    private Label fieldLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        lbl.setStyle("-fx-text-fill: " + TEXT_DARK + ";");
        return lbl;
    }

    private Label section(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        lbl.setStyle("-fx-text-fill: " + TEXT_DARK + ";");
        return lbl;
    }

    private void styleTextField(TextField f) {
        f.setPrefHeight(42);
        f.setMaxWidth(Double.MAX_VALUE);
        f.setStyle(
                "-fx-background-color: " + BG_INPUT + ";" +
                "-fx-control-inner-background: " + BG_INPUT + ";" +
                "-fx-text-fill: " + TEXT_DARK + ";" +
                "-fx-prompt-text-fill: " + TEXT_MUTED_DARK + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 13px;"
        );
    }

    private void styleCombo(ComboBox<String> c) {
        c.setPrefHeight(42);
        c.setMaxWidth(Double.MAX_VALUE);
        c.setStyle(
                "-fx-background-color: " + BG_INPUT + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + TEXT_DARK + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );
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
        lbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
        return lbl;
    }

    private Region space() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

    private void alert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}