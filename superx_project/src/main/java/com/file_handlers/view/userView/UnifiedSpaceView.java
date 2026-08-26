package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UnifiedSpaceView {

    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // Style Constants - Exact Color Hierarchy synced with UserDashboard
    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";
    private static final String BG_CENTER_CANVAS = "#31435B";

    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_CARD_INNER = "#CADDF2";
    private static final String BORDER_CARD = "#C3D6EC";

    private static final String TEXT_DARK = "#0F172A";
    private static final String TEXT_MUTED_DARK = "#334155";
    private static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#94A3B8";

    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String ACCENT_GREEN = "#059669";
    private static final String ACCENT_YELLOW = "#D97706";

    private final List<FileData> fileData = new ArrayList<>();

    private String selectedSpace = "All Spaces";
    private String selectedFileType = "All Files";
    private String selectedViewType = "List View";

    private FlowPane currentFileCards;
    private Label currentImagePreview, currentFileNameValue, currentDateValue;
    private Label currentTypeValue, currentSizeDetailValue;

    public UnifiedSpaceView() {
        this("All Spaces");
    }

    public UnifiedSpaceView(String initialSpace) {
        if (initialSpace != null && !initialSpace.isEmpty()) {
            this.selectedSpace = initialSpace;
        }
    }

    public Scene getUnifiedSpaceScene() {

        // =========================================================
        // SIDEBAR
        // =========================================================
        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        HBox logoRow = new HBox(10, logoIcon, logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoRow);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("📁", "Spaces", true);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collaborationBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);

        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        aiBtn.setOnAction(e -> LandingPage.showAiAssistantPage());
        collaborationBtn.setOnAction(e -> LandingPage.showCollaborationPage());
        recentBtn.setOnAction(e -> LandingPage.showRecentPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());
        settingsBtn.setOnAction(e -> LandingPage.showSettingPage());

        VBox navigation = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn,
                collaborationBtn, recentBtn, trashBtn);

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        // Sidebar Bottom Storage Card
        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        storageTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storageValue = new Label("64.2 GB of 100 GB");
        storageValue.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageValue.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storagePct = new Label("64%");
        storagePct.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePct.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox storageTopRow = new HBox(storageValue, new Region(), storagePct);
        HBox.setHgrow(storageTopRow.getChildren().get(1), Priority.ALWAYS);
        storageTopRow.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: " + PRIMARY_BLUE + "; -fx-control-inner-background: #0E1520;");

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:#60A5FA;" +
                "-fx-padding:2 0 0 0;-fx-cursor:hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexedPage());

        VBox storageCard = new VBox(8, storageTitle, storageTopRow, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color:" + BG_SIDEBAR_CARD + ";-fx-border-color:" + SIDEBAR_BORDER +
                ";-fx-border-radius:12;-fx-background-radius:12;");

        VBox sidebar = new VBox(12, logoBox, navigation, sidebarSpacer, settingsBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle("-fx-background-color:" + BG_SIDEBAR + ";-fx-border-color:" + SIDEBAR_BORDER +
                ";-fx-border-width:0 1 0 0;");

        // =========================================================
        // TOP SEARCH BAR & PROFILE
        // =========================================================
        TextField searchField = new TextField();
        searchField.setPromptText("Search in " + selectedSpace + "...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color:transparent;-fx-prompt-text-fill:" + TEXT_MUTED_LIGHT + ";" +
                "-fx-text-fill:" + TEXT_LIGHT + ";-fx-font-size:13px;");

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        Label shortcut = new Label("⌘ K");
        shortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        shortcut.setStyle("-fx-background-color:#141E2C;-fx-text-fill:" + TEXT_MUTED_LIGHT + ";-fx-padding:3 6;-fx-background-radius:4;");

        HBox searchBox = new HBox(8, searchIcon, searchField, shortcut);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 12, 0, 14));
        searchBox.setStyle("-fx-background-color:#141E2C;-fx-border-color:" + SIDEBAR_BORDER +
                ";-fx-border-radius:10;-fx-background-radius:10;");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBox.setPrefWidth(420);

        Button notificationButton = new Button("🔔");
        notificationButton.setStyle("-fx-background-color:transparent;-fx-font-size:16px;-fx-cursor:hand;-fx-text-fill:" + TEXT_LIGHT + ";");
        notificationButton.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:" + PRIMARY_BLUE + ";-fx-background-radius:50%;" +
                "-fx-text-fill:" + TEXT_LIGHT + ";-fx-font-weight:bold;-fx-font-size:12px;");

        Label userName = new Label("Aarav Verma");
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label arrow = new Label("⌄");
        arrow.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox profile = new HBox(10, notificationButton, avatar, userName, arrow);
        profile.setAlignment(Pos.CENTER);

        Region topSpacer = new Region();
        HBox topBar = new HBox(20, searchBox, topSpacer, profile);
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle("-fx-background-color:" + BG_SIDEBAR + ";-fx-border-color:" + SIDEBAR_BORDER + ";-fx-border-width:0 0 1 0;");

        // =========================================================
        // HEADER TITLE AREA
        // =========================================================
        Label pageIcon = new Label("📁");
        pageIcon.setPrefSize(42, 42);
        pageIcon.setAlignment(Pos.CENTER);
        pageIcon.setStyle("-fx-background-color:" + BG_CARD_INNER + ";-fx-background-radius:10;" +
                "-fx-text-fill:" + PRIMARY_BLUE + ";-fx-font-size:18px;");

        Label pageTitle = new Label(selectedSpace.equals("All Spaces") ? "Unified Space View" : selectedSpace + " Space");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        pageTitle.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label pageDescription = new Label("Access and filter all your virtual spaces and their files.");
        pageDescription.setFont(Font.font(FONT, 12));
        pageDescription.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox titleBox = new VBox(2, pageTitle, pageDescription);
        HBox titleArea = new HBox(12, pageIcon, titleBox);
        titleArea.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-background-color:" + BG_CARD_INNER + ";-fx-text-fill:" + TEXT_DARK +
                ";-fx-border-color:" + BORDER_CARD + ";-fx-border-radius:8;-fx-background-radius:8;" +
                "-fx-padding:8 14;-fx-cursor:hand;-fx-font-weight:bold;");
        backButton.setOnAction(e -> LandingPage.showUserSpace());

        Button addFilesButton = new Button("+ Add Files");
        addFilesButton.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        addFilesButton.setStyle("-fx-background-color:" + PRIMARY_BLUE +
                ";-fx-text-fill:#FFFFFF;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:8 16;");

        HBox headerButtons = new HBox(10, backButton, addFilesButton);
        headerButtons.setAlignment(Pos.CENTER_RIGHT);

        Region headerSpacer = new Region();
        HBox header = new HBox(titleArea, headerSpacer, headerButtons);
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);

        // =========================================================
        // STATS GRID (Metric Cards matching UserDashboard style)
        // =========================================================
        VBox totalFiles = createDashboardCard("Indexing Activity", "7,032", "📁", PRIMARY_BLUE, "• 84 auto-tagged", "+412 files today");
        VBox folders = createDashboardCard("Active Spaces", "8 Spaces", "▦", PRIMARY_BLUE, "2 AI generated", "Java Project (64%)");
        VBox size = createDashboardCard("Indexed Storage", "64.2 GB", "💾", ACCENT_GREEN, "• Synced 2m ago", "4.2 GB recoverable");
        VBox updated = createDashboardCard("AI Actions Live", "126 Actions", "✦", ACCENT_YELLOW, "⚡ Live pipeline", "12 summaries · 8 links");

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(14);
        statsGrid.add(totalFiles, 0, 0);
        statsGrid.add(folders, 1, 0);
        statsGrid.add(size, 2, 0);
        statsGrid.add(updated, 3, 0);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(25);
            statsGrid.getColumnConstraints().add(column);
        }

        // =========================================================
        // FILTER BUTTONS & CONTROLS
        // =========================================================
        Button allSpacesButton = createFilterButton(selectedSpace);
        Button filtersButton = createFilterButton("All Files");
        Button viewTypeButton = createFilterButton("List View");

        createMenu(allSpacesButton, "SPACE", "All Spaces", "Personal", "College", "Work",
                "Finance", "Friends & Family", "Travel");
        createMenu(filtersButton, "TYPE", "All Files", "Documents", "Photos",
                "IDs & Certificates", "PDFs", "Videos", "Other");
        createMenu(viewTypeButton, "VIEW", "Grid View", "List View", "Compact View");

        HBox fileAreaTitle = new HBox(10, allSpacesButton, filtersButton, viewTypeButton);
        fileAreaTitle.setAlignment(Pos.CENTER_RIGHT);

        // =========================================================
        // PREVIEW SECTION (Right Panel - Card Surface)
        // =========================================================
        Label previewTitle = new Label("File Preview");
        previewTitle.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        previewTitle.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Button previewButton = new Button("Preview");
        Button shareButton = new Button("Share");
        styleActionButton(previewButton, true);
        styleActionButton(shareButton, false);

        HBox previewButtons = new HBox(8, previewButton, shareButton);
        previewButtons.setAlignment(Pos.CENTER_RIGHT);

        Region previewSpacer = new Region();
        HBox previewHeader = new HBox(previewTitle, previewSpacer, previewButtons);
        HBox.setHgrow(previewSpacer, Priority.ALWAYS);
        previewHeader.setAlignment(Pos.CENTER_LEFT);

        Label imagePreview = new Label("🖼");
        imagePreview.setPrefSize(380, 210);
        imagePreview.setAlignment(Pos.CENTER);
        imagePreview.setFont(Font.font(FONT, 56));
        imagePreview.setStyle("-fx-background-color:" + BG_CARD_INNER + ";-fx-border-color:" + BORDER_CARD +
                ";-fx-border-radius:10;-fx-background-radius:10;-fx-text-fill:" + PRIMARY_BLUE + ";");

        Label fileNameTitle = new Label("File Name");
        Label fileNameValue = new Label("Personal_Photo.jpg");
        Label dateTitle = new Label("Date");
        Label dateValue = new Label("13 Aug 2026");
        Label typeTitle = new Label("File Type");
        Label typeValue = new Label("JPG");
        Label sizeDetailTitle = new Label("Size");
        Label sizeDetailValue = new Label("3.2 MB");

        styleDetailTitle(fileNameTitle);
        styleDetailTitle(dateTitle);
        styleDetailTitle(typeTitle);
        styleDetailTitle(sizeDetailTitle);
        styleDetailValue(fileNameValue);
        styleDetailValue(dateValue);
        styleDetailValue(typeValue);
        styleDetailValue(sizeDetailValue);

        GridPane fileOverview = new GridPane();
        fileOverview.setHgap(20);
        fileOverview.setVgap(12);
        fileOverview.add(createFileDetail(fileNameTitle, fileNameValue), 0, 0);
        fileOverview.add(createFileDetail(dateTitle, dateValue), 1, 0);
        fileOverview.add(createFileDetail(typeTitle, typeValue), 0, 1);
        fileOverview.add(createFileDetail(sizeDetailTitle, sizeDetailValue), 1, 1);
        fileOverview.setPadding(new Insets(14));
        fileOverview.setStyle("-fx-background-color:" + BG_CARD_INNER + ";-fx-border-color:" + BORDER_CARD +
                ";-fx-border-radius:10;-fx-background-radius:10;");

        VBox previewSection = new VBox(14, previewHeader, imagePreview, fileOverview);
        previewSection.setPadding(new Insets(20));
        previewSection.setPrefWidth(430);
        previewSection.setMinWidth(430);
        previewSection.setMaxWidth(430);
        previewSection.setStyle("-fx-background-color:" + BG_CARD + ";-fx-border-color:" + BORDER_CARD +
                ";-fx-border-radius:16;-fx-background-radius:16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 16, 0, 0, 6);");

        // =========================================================
        // FILE LIST / CARDS CONTAINER
        // =========================================================
        FlowPane fileCards = new FlowPane();
        fileCards.setHgap(12);
        fileCards.setVgap(12);
        fileCards.setPadding(new Insets(4));
        fileCards.setPrefWrapLength(600);

        currentFileCards = fileCards;
        currentImagePreview = imagePreview;
        currentFileNameValue = fileNameValue;
        currentDateValue = dateValue;
        currentTypeValue = typeValue;
        currentSizeDetailValue = sizeDetailValue;

        addInitialData();
        refreshFileCards(fileCards, imagePreview, fileNameValue, dateValue, typeValue, sizeDetailValue);

        ScrollPane fileScrollPane = new ScrollPane(fileCards);
        fileScrollPane.setFitToWidth(true);
        fileScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        fileScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        fileScrollPane.setPannable(true);
        fileScrollPane.setStyle("-fx-background-color:transparent;-fx-background:transparent;-fx-border-color:transparent;");

        VBox fileListArea = new VBox(12, fileAreaTitle, fileScrollPane);
        fileListArea.setPadding(new Insets(18));
        fileListArea.setStyle("-fx-background-color:" + BG_CARD + ";-fx-border-color:" + BORDER_CARD +
                ";-fx-border-radius:16;-fx-background-radius:16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 16, 0, 0, 6);");
        VBox.setVgrow(fileScrollPane, Priority.ALWAYS);

        HBox mainHBox = new HBox(16, fileListArea, previewSection);
        HBox.setHgrow(fileListArea, Priority.ALWAYS);
        VBox.setVgrow(mainHBox, Priority.ALWAYS);

        VBox mainContent = new VBox(20, header, statsGrid, mainHBox);
        mainContent.setPadding(new Insets(24, 28, 28, 28));
        mainContent.setStyle("-fx-background-color:" + BG_CENTER_CANVAS + ";");
        VBox.setVgrow(mainHBox, Priority.ALWAYS);

        ScrollPane centerScroll = new ScrollPane(mainContent);
        centerScroll.setFitToWidth(true);
        centerScroll.setStyle("-fx-background-color:" + BG_CENTER_CANVAS + ";-fx-background:" + BG_CENTER_CANVAS + ";");

        VBox center = new VBox(topBar, centerScroll);
        VBox.setVgrow(centerScroll, Priority.ALWAYS);
        VBox.setVgrow(center, Priority.ALWAYS);
        center.setStyle("-fx-background-color:" + BG_CENTER_CANVAS + ";");

        addFilesButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Add Files");

            File selectedFile = fileChooser.showOpenDialog(
                    addFilesButton.getScene().getWindow());

            if (selectedFile != null) {
                String fileName = selectedFile.getName();
                int dot = fileName.lastIndexOf(".");
                String fileType = dot > 0
                        ? fileName.substring(dot + 1).toUpperCase()
                        : "FILE";

                String fileSize = formatFileSize(selectedFile.length());
                String category = determineCategory(fileType);
                String icon = getFileIcon(fileType);

                String targetSpace = selectedSpace.equals("All Spaces") ? "Personal" : selectedSpace;

                fileData.add(new FileData(
                        fileName, "18 Aug 2026", fileType, fileSize,
                        targetSpace, category, icon));

                refreshFileCards(fileCards, imagePreview, fileNameValue,
                        dateValue, typeValue, sizeDetailValue);
            }
        });

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG_SIDEBAR + ";");
        root.setLeft(sidebar);
        root.setCenter(center);

        return new Scene(root, 1200, 750);
    }

    private void addInitialData() {
        fileData.clear();
        fileData.add(new FileData("Personal_Photo.jpg", "13 Aug 2026", "JPG",
                "3.2 MB", "Personal", "Photos", "🖼"));
        fileData.add(new FileData("Aadhaar_Card.pdf", "12 Aug 2026", "PDF",
                "1.8 MB", "Personal", "IDs & Certificates", "📄"));
        fileData.add(new FileData("Resume_2026.pdf", "10 Aug 2026", "PDF",
                "2.5 MB", "Personal", "Documents", "📄"));
        fileData.add(new FileData("Family_Photo.jpg", "08 Aug 2026", "JPG",
                "6.7 MB", "Friends & Family", "Photos", "🖼"));
        fileData.add(new FileData("Personal_Notes.docx", "05 Aug 2026", "DOCX",
                "4.4 MB", "Personal", "Documents", "📝"));
    }

    private void refreshFileCards(FlowPane fileCards, Label imagePreview,
                                    Label fileNameValue, Label dateValue,
                                    Label typeValue, Label sizeDetailValue) {

        fileCards.getChildren().clear();

        for (FileData file : fileData) {
            boolean spaceMatches = selectedSpace.equals("All Spaces")
                    || file.space.equalsIgnoreCase(selectedSpace);

            boolean typeMatches = selectedFileType.equals("All Files")
                    || file.category.equals(selectedFileType)
                    || (selectedFileType.equals("PDFs")
                    && file.type.equalsIgnoreCase("PDF"));

            if (spaceMatches && typeMatches) {
                addFileCard(fileCards, file, imagePreview, fileNameValue,
                        dateValue, typeValue, sizeDetailValue);
            }
        }
    }

    private void addFileCard(FlowPane fileCards, FileData file, Label imagePreview,
                           Label fileNameValue, Label dateValue,
                           Label typeValue, Label sizeDetailValue) {

        Label icon = new Label(file.icon);
        icon.setFont(Font.font(FONT, 20));
        icon.setPrefSize(38, 38);
        icon.setMinSize(38, 38);
        icon.setMaxSize(38, 38);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color:" + BG_CARD_INNER +
                ";-fx-background-radius:8;-fx-text-fill:" + PRIMARY_BLUE + ";");

        Label nameLabel = new Label(file.name);
        nameLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        nameLabel.setStyle("-fx-text-fill:" + TEXT_DARK + ";-fx-font-size:12px;-fx-font-weight:600;");
        nameLabel.setWrapText(true);

        Label infoLabel = new Label(file.type + " • " + file.date + " • " + file.size);
        infoLabel.setFont(Font.font(FONT, 10));
        infoLabel.setStyle("-fx-text-fill:" + TEXT_MUTED_DARK + ";-fx-font-size:10px;-fx-font-weight:600;");
        infoLabel.setWrapText(true);

        VBox details = new VBox(3, nameLabel, infoLabel);
        details.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(12, icon, details);
        card.setAlignment(Pos.CENTER_LEFT);

        if (selectedViewType.equals("Grid View")) {
            card.setPrefSize(240, 120);
            card.setMinSize(240, 120);
            card.setMaxSize(240, 120);
            card.setAlignment(Pos.CENTER);
            card.setSpacing(7);
            card.setPadding(new Insets(12));
            details.setAlignment(Pos.CENTER);
            details.setPrefWidth(200);
            details.setMaxWidth(200);
            nameLabel.setMaxWidth(200);
            infoLabel.setMaxWidth(200);
            nameLabel.setAlignment(Pos.CENTER);
            infoLabel.setAlignment(Pos.CENTER);
        } else if (selectedViewType.equals("Compact View")) {
            card.setPrefWidth(980);
            card.setMinWidth(980);
            card.setPrefHeight(46);
            card.setMinHeight(46);
            card.setPadding(new Insets(5, 10, 5, 10));
            icon.setPrefSize(30, 30);
            icon.setMinSize(30, 30);
            icon.setMaxSize(30, 30);
            icon.setFont(Font.font(FONT, 14));
            details.setPrefWidth(900);
            details.setMaxWidth(900);
        } else {
            card.setPrefWidth(980);
            card.setMinWidth(980);
            card.setPrefHeight(68);
            card.setMinHeight(68);
            card.setPadding(new Insets(12));
            details.setPrefWidth(900);
            details.setMaxWidth(900);
        }

        String normalStyle = "-fx-background-color:" + BG_CARD_INNER + ";-fx-border-color:" + BORDER_CARD +
                ";-fx-border-radius:10;-fx-background-radius:10;-fx-cursor:hand;";

        card.setStyle(normalStyle);

        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color:" + BG_CARD_INNER + ";-fx-border-color:" + PRIMARY_BLUE +
                ";-fx-border-radius:10;-fx-background-radius:10;-fx-cursor:hand;"));

        card.setOnMouseExited(e -> card.setStyle(normalStyle));

        card.setOnMouseClicked(e -> {
            imagePreview.setText(file.icon);
            fileNameValue.setText(file.name);
            dateValue.setText(file.date);
            typeValue.setText(file.type);
            sizeDetailValue.setText(file.size);
        });

        fileCards.getChildren().add(card);
    }

    private String determineCategory(String fileType) {
        if (fileType.equals("DOC") || fileType.equals("DOCX") ||
                fileType.equals("TXT") || fileType.equals("ODT")) return "Documents";

        if (fileType.equals("JPG") || fileType.equals("JPEG") ||
                fileType.equals("PNG") || fileType.equals("GIF") ||
                fileType.equals("WEBP")) return "Photos";

        if (fileType.equals("MP4") || fileType.equals("AVI") ||
                fileType.equals("MKV") || fileType.equals("MOV")) return "Videos";

        if (fileType.equals("PDF")) return "PDFs";
        return "Other";
    }

    private String getFileIcon(String fileType) {
        if (fileType.equals("JPG") || fileType.equals("JPEG") ||
                fileType.equals("PNG") || fileType.equals("GIF") ||
                fileType.equals("WEBP")) return "🖼";

        if (fileType.equals("PDF")) return "📄";

        if (fileType.equals("MP4") || fileType.equals("AVI") ||
                fileType.equals("MKV") || fileType.equals("MOV")) return "🎬";

        if (fileType.equals("DOC") || fileType.equals("DOCX")) return "📝";
        return "📁";
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024)
            return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }

    private Button createFilterButton(String text) {
        Button button = new Button(text + " ▼");
        button.setPrefHeight(34);
        button.setPadding(new Insets(0, 14, 0, 14));
        button.setStyle("-fx-background-color:" + BG_CARD_INNER + ";-fx-text-fill:" + TEXT_DARK +
                ";-fx-font-size:11px;-fx-font-weight:bold;-fx-background-radius:8;" +
                "-fx-border-color:" + BORDER_CARD + ";-fx-border-radius:8;-fx-cursor:hand;");
        return button;
    }

    private ContextMenu createMenu(Button button, String menuType, String... options) {
        ContextMenu menu = new ContextMenu();
        menu.setStyle("-fx-background-color:" + BG_CARD + ";-fx-background-radius:8;" +
                "-fx-border-color:" + BORDER_CARD + ";-fx-border-radius:8;");

        for (String option : options) {
            MenuItem item = new MenuItem(option);
            item.setStyle("-fx-font-size:12px;-fx-padding:8 18 8 18;-fx-text-fill:" + TEXT_DARK + ";");

            item.setOnAction(e -> {
                button.setText(option + " ▼");

                if (menuType.equals("SPACE")) selectedSpace = option;
                if (menuType.equals("TYPE")) selectedFileType = option;
                if (menuType.equals("VIEW")) selectedViewType = option;

                refreshFileCards(currentFileCards, currentImagePreview,
                        currentFileNameValue, currentDateValue,
                        currentTypeValue, currentSizeDetailValue);

                menu.hide();
            });

            menu.getItems().add(item);
        }

        button.setOnAction(e -> {
            if (menu.isShowing()) menu.hide();
            else menu.show(button, javafx.geometry.Side.BOTTOM, 0, 3);
        });

        return menu;
    }

    private VBox createFileDetail(Label title, Label value) {
        return new VBox(2, title, value);
    }

    private void styleDetailTitle(Label label) {
        label.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        label.setStyle("-fx-text-fill:" + TEXT_MUTED_DARK + ";-fx-font-weight:600;");
    }

    private void styleDetailValue(Label label) {
        label.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        label.setStyle("-fx-text-fill:" + TEXT_DARK + ";");
    }

    private void styleActionButton(Button button, boolean primary) {
        button.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        button.setPadding(new Insets(7, 14, 7, 14));

        if (primary) {
            button.setStyle("-fx-background-color:" + PRIMARY_BLUE +
                    ";-fx-text-fill:#FFFFFF;-fx-background-radius:8;-fx-cursor:hand;");
        } else {
            button.setStyle("-fx-background-color:" + BG_CARD_INNER +
                    ";-fx-text-fill:" + TEXT_DARK + ";-fx-border-color:" + BORDER_CARD +
                    ";-fx-border-radius:8;-fx-background-radius:8;-fx-cursor:hand;-fx-font-weight:bold;");
        }
    }

    private VBox createDashboardCard(String title, String mainValue, String symbol, String symbolColor, String tag1Text, String tag2Text) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        titleLabel.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        Label symbolIcon = new Label(symbol);
        symbolIcon.setFont(Font.font(FONT, 12));
        symbolIcon.setStyle("-fx-text-fill: " + symbolColor + ";");

        StackPane iconBadge = new StackPane(symbolIcon);
        iconBadge.setPrefSize(26, 26);
        iconBadge.setStyle("-fx-background-color:" + BG_CARD_INNER + ";-fx-background-radius:6;");

        HBox topRow = new HBox(titleLabel, new Region(), iconBadge);
        HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label valueLabel = new Label(mainValue);
        valueLabel.setFont(Font.font(FONT, FontWeight.BOLD, 20));
        valueLabel.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label tag1 = new Label(tag1Text);
        tag1.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        tag1.setPadding(new Insets(3, 8, 3, 8));
        tag1.setStyle("-fx-background-color:" + BG_CARD_INNER + ";-fx-text-fill:" + PRIMARY_BLUE + ";-fx-background-radius:6;");

        Label tag2 = new Label(tag2Text);
        tag2.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        tag2.setPadding(new Insets(3, 8, 3, 8));
        tag2.setStyle("-fx-background-color:" + BG_CARD_INNER + ";-fx-text-fill:" + TEXT_MUTED_DARK + ";-fx-background-radius:6;");

        HBox tagsRow = new HBox(6, tag1, tag2);
        tagsRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(8, topRow, valueLabel, tagsRow);
        card.setPadding(new Insets(14));
        card.setMinHeight(105);
        card.setPrefHeight(105);
        card.setStyle("-fx-background-color:" + BG_CARD + ";-fx-border-color:" + BORDER_CARD +
                ";-fx-border-radius:14;-fx-background-radius:14;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.14), 12, 0, 0, 4);");

        return card;
    }

    private Button createSidebarButton(String icon, String text, boolean active) {
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(FONT, 14));

        Label textLabel = new Label(text);
        textLabel.setFont(Font.font(FONT,
                active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));

        HBox content = new HBox(12, iconLabel, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("", content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(38);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        if (active) {
            button.setStyle("-fx-background-color:" + PRIMARY_BLUE + ";-fx-background-radius:8;-fx-cursor:hand;");
            iconLabel.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");
            textLabel.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");
        } else {
            button.setStyle("-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;");
            iconLabel.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
            textLabel.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

            button.setOnMouseEntered(e ->
                    button.setStyle("-fx-background-color:#26354A;-fx-background-radius:8;-fx-cursor:hand;"));

            button.setOnMouseExited(e ->
                    button.setStyle("-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;"));
        }

        return button;
    }

    private StackPane createOneSpaceLogo() {
        Image logoImage = new Image(
                getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png")
        );

        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(38);
        logoView.setFitHeight(38);
        logoView.setPreserveRatio(true);

        StackPane logoPane = new StackPane(logoView);
        logoPane.setPrefSize(38, 38);
        logoPane.setAlignment(Pos.CENTER);

        return logoPane;
    }

    private static class FileData {
        String name, date, type, size, space, category, icon;

        FileData(String name, String date, String type, String size,
                   String space, String category, String icon) {
            this.name = name;
            this.date = date;
            this.type = type;
            this.size = size;
            this.space = space;
            this.category = category;
            this.icon = icon;
        }
    }
}