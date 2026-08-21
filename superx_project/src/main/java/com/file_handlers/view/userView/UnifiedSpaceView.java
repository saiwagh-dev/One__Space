package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UnifiedSpaceView {

    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
        private static final String BG_APP = "#F4F8FC", WHITE = "#FFFFFF", BORDER = "#DCE5EF";
        private static final String BG_SIDEBAR = "#FFFFFF", SIDEBAR_BORDER = BORDER;
    private static final String PURPLE = "#6366F1", PURPLE_LIGHT = "#EEF2FF";
        private static final String PRIMARY_BLUE = "#2563EB";
        private static final String TEXT = "#0F172A", MUTED = "#64748B", TEXT_LIGHT = "#FFFFFF",
                TEXT_MUTED_LIGHT = "#CBD5E1";

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

        Label logoIcon = new Label("◉");
        logoIcon.setFont(Font.font(FONT, FontWeight.BOLD, 20));
        logoIcon.setTextFill(Color.web(PURPLE));

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        logoText.setTextFill(Color.web(TEXT));

        HBox logoRow = new HBox(8, logoIcon, logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        Label tagline = new Label("Local · AI Indexed");
        tagline.setFont(Font.font(FONT, 10));
        tagline.setTextFill(Color.web(MUTED));

        VBox logoBox = new VBox(3, logoRow, tagline);
        logoBox.setPadding(new Insets(0, 0, 14, 8));

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("▦", "Spaces", true);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("□", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collaborationBtn = createSidebarButton("♧", "Collaboration", false);
        Button recentBtn = createSidebarButton("◷", "Recent", false);
        Button trashBtn = createSidebarButton("♜", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);

        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        recentBtn.setOnAction(e -> LandingPage.showRecentPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());

        VBox navigation = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn,
                collaborationBtn, recentBtn, trashBtn);

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        Label quickTipTitle = new Label("✧  Quick Tip");
        quickTipTitle.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        quickTipTitle.setTextFill(Color.web(TEXT));

        Label quickTipText = new Label(
                "Files in spaces are virtual groups. Your original files stay safe in the source folders.");
        quickTipText.setFont(Font.font(FONT, 10));
        quickTipText.setTextFill(Color.web(MUTED));
        quickTipText.setWrapText(true);

        VBox quickTip = new VBox(8, quickTipTitle, quickTipText);
        quickTip.setPadding(new Insets(12));
        quickTip.setStyle("-fx-background-color:#F8FAFC;-fx-border-color:" + BORDER +
                ";-fx-border-radius:10;-fx-background-radius:10;");

        VBox sidebar = new VBox(10, logoBox, navigation, sidebarSpacer, settingsBtn, quickTip);
        sidebar.setPadding(new Insets(20, 13, 18, 13));
        sidebar.setPrefWidth(200);
        sidebar.setMinWidth(200);
        sidebar.setMaxWidth(200);
        sidebar.setStyle("-fx-background-color:" + WHITE + ";-fx-border-color:" + BORDER +
                ";-fx-border-width:0 1 0 0;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search in " + selectedSpace + "...");
        searchField.setPrefHeight(36);
        searchField.setStyle("-fx-background-color:transparent;-fx-prompt-text-fill:#94A3B8;" +
                "-fx-text-fill:" + TEXT + ";-fx-font-size:12px;");

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 15));
        searchIcon.setTextFill(Color.web(MUTED));

        Label shortcut = new Label("⌘ K");
        shortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 9));
        shortcut.setTextFill(Color.web(MUTED));
        shortcut.setStyle("-fx-background-color:#F1F5F9;-fx-padding:3 6;-fx-background-radius:4;");

        HBox searchBox = new HBox(8, searchIcon, searchField, shortcut);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 9, 0, 11));
        searchBox.setStyle("-fx-background-color:" + WHITE + ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:9;-fx-background-radius:9;");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBox.setPrefWidth(310);
        searchBox.setMaxWidth(310);

        Button notificationButton = new Button("🔔");
        notificationButton.setStyle("-fx-background-color:transparent;-fx-font-size:14px;-fx-cursor:hand;");
        notificationButton.setOnAction(e -> LandingPage.showNotificationPage());

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


// =========================================================
// CLICKABLE PROFILE OPTION
// =========================================================

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


// =========================================================
// OPEN PROFILE PAGE WHEN CLICKED
// =========================================================

profileOption.setOnMouseClicked(e -> {
    LandingPage.showUserProfilePage();
});


// =========================================================
// HOVER EFFECT
// =========================================================

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


// =========================================================
// TOP RIGHT
// =========================================================

HBox profileBox =
        new HBox(
                10,
                notificationButton,
                profileOption
        );

profileBox.setAlignment(
        Pos.CENTER
);


// =========================================================
// TOP BAR
// =========================================================

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



        Label pageIcon = new Label("♟");
        pageIcon.setPrefSize(46, 46);
        pageIcon.setAlignment(Pos.CENTER);
        pageIcon.setStyle("-fx-background-color:" + PURPLE_LIGHT + ";-fx-background-radius:50%;" +
                "-fx-text-fill:" + PURPLE + ";-fx-font-size:20px;");

        Label pageTitle = new Label(selectedSpace.equals("All Spaces") ? "Unified Space View" : selectedSpace + " Space");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 23));
        pageTitle.setTextFill(Color.web(TEXT));

        Label pageDescription = new Label(
                "Access and filter all your virtual spaces and their files.");
        pageDescription.setFont(Font.font(FONT, 11));
        pageDescription.setTextFill(Color.web(MUTED));

        VBox titleBox = new VBox(3, pageTitle, pageDescription);
        HBox titleArea = new HBox(13, pageIcon, titleBox);
        titleArea.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("←  Back");
        backButton.setStyle("-fx-background-color:" + WHITE + ";-fx-text-fill:" + TEXT +
                ";-fx-border-color:" + BORDER + ";-fx-border-radius:8;-fx-background-radius:8;" +
                "-fx-padding:8 13;-fx-cursor:hand;");
        backButton.setOnAction(e -> LandingPage.showUserSpace());

        Button addFilesButton = new Button("+  Add Files");
        addFilesButton.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        addFilesButton.setTextFill(Color.WHITE);
        addFilesButton.setPadding(new Insets(8, 15, 8, 15));
        addFilesButton.setStyle("-fx-background-color:" + PURPLE +
                ";-fx-background-radius:8;-fx-cursor:hand;");

        HBox headerButtons = new HBox(10, backButton, addFilesButton);
        headerButtons.setAlignment(Pos.CENTER_RIGHT);

        Region headerSpacer = new Region();
        HBox header = new HBox(titleArea, headerSpacer, headerButtons);
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox totalFiles = createStatCard("▣", "Total Files", "5", "Files in this space");
        VBox folders = createStatCard("▰", "Folders", "3", "Organized groups");
        VBox size = createStatCard("◔", "Size", "18.6 MB", "Used storage");
        VBox updated = createStatCard("▣", "Last Updated", "Today", "2 hours ago");

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(10);
        statsGrid.add(totalFiles, 0, 0);
        statsGrid.add(folders, 1, 0);
        statsGrid.add(size, 2, 0);
        statsGrid.add(updated, 3, 0);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(25);
            statsGrid.getColumnConstraints().add(column);
        }

        Button allSpacesButton = createFilterButton(selectedSpace);
        Button filtersButton = createFilterButton("All Files");
        Button viewTypeButton = createFilterButton("List View");

        createMenu(allSpacesButton, "SPACE", "All Spaces", "Personal", "College", "Work",
                "Finance", "Friends & Family", "Travel");
        createMenu(filtersButton, "TYPE", "All Files", "Documents", "Photos",
                "IDs & Certificates", "PDFs", "Videos", "Other");
        createMenu(viewTypeButton, "VIEW", "Grid View", "List View", "Compact View");

        HBox fileAreaTitle = new HBox(8, allSpacesButton, filtersButton, viewTypeButton);
        fileAreaTitle.setAlignment(Pos.CENTER_RIGHT);

        Label previewTitle = new Label("File Preview");
        previewTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        previewTitle.setTextFill(Color.web(TEXT));

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
        imagePreview.setPrefSize(390, 250);
        imagePreview.setAlignment(Pos.CENTER);
        imagePreview.setFont(Font.font(FONT, 58));
        imagePreview.setTextFill(Color.web(PURPLE));
        imagePreview.setStyle("-fx-background-color:#F8FAFC;-fx-border-color:" + BORDER +
                ";-fx-border-radius:10;-fx-background-radius:10;");

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
        fileOverview.setPadding(new Insets(12));
        fileOverview.setStyle("-fx-background-color:#F8FAFC;-fx-border-color:" + BORDER +
                ";-fx-border-radius:9;-fx-background-radius:9;");

        VBox previewSection = new VBox(12, previewHeader, imagePreview, fileOverview);
        previewSection.setPadding(new Insets(18));
        previewSection.setPrefWidth(440);
        previewSection.setMinWidth(440);
        previewSection.setMaxWidth(440);
        previewSection.setStyle("-fx-background-color:" + WHITE + ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:13;-fx-background-radius:13;");

        FlowPane fileCards = new FlowPane();
        fileCards.setHgap(10);
        fileCards.setVgap(10);
        fileCards.setPadding(new Insets(2));
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
        fileScrollPane.setStyle("-fx-background-color:transparent;-fx-background:transparent;" +
                "-fx-border-color:transparent;");

        VBox fileListArea = new VBox(10, fileAreaTitle, fileScrollPane);
        fileListArea.setPadding(new Insets(12));
        fileListArea.setStyle("-fx-background-color:" + WHITE + ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:13;-fx-background-radius:13;");
        VBox.setVgrow(fileScrollPane, Priority.ALWAYS);

        HBox mainHBox = new HBox(14, fileListArea, previewSection);
        HBox.setHgrow(fileListArea, Priority.ALWAYS);
        VBox.setVgrow(mainHBox, Priority.ALWAYS);

        VBox mainContent = new VBox(12, header, statsGrid, mainHBox);
        mainContent.setPadding(new Insets(0, 14, 14, 18));
        VBox.setVgrow(mainHBox, Priority.ALWAYS);

        VBox center = new VBox(topBar, mainContent);
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        VBox.setVgrow(center, Priority.ALWAYS);

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
        root.setStyle("-fx-background-color:" + BG_APP + ";");
        root.setLeft(sidebar);
        root.setCenter(center);

        return new Scene(root, 1250, 800);
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
        icon.setFont(Font.font(FONT, 22));
        icon.setPrefSize(42, 42);
        icon.setMinSize(42, 42);
        icon.setMaxSize(42, 42);
        icon.setAlignment(Pos.CENTER);
        icon.setTextFill(Color.web(PURPLE));
        icon.setStyle("-fx-background-color:" + PURPLE_LIGHT +
                ";-fx-background-radius:8;-fx-text-fill:" + PURPLE + ";");

        Label nameLabel = new Label(file.name);
        nameLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        nameLabel.setTextFill(Color.web(TEXT));
        nameLabel.setStyle("-fx-text-fill:" + TEXT +
                ";-fx-font-size:12px;-fx-font-weight:600;");
        nameLabel.setWrapText(true);
        nameLabel.setVisible(true);
        nameLabel.setManaged(true);

        Label infoLabel = new Label(file.type + "  •  " + file.date + "  •  " + file.size);
        infoLabel.setFont(Font.font(FONT, 10));
        infoLabel.setTextFill(Color.web(MUTED));
        infoLabel.setStyle("-fx-text-fill:" + MUTED + ";-fx-font-size:10px;");
        infoLabel.setWrapText(true);

        VBox details = new VBox(4, nameLabel, infoLabel);
        details.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(12, icon, details);
        card.setAlignment(Pos.CENTER_LEFT);

        if (selectedViewType.equals("Grid View")) {
            card.setPrefSize(250, 125);
            card.setMinSize(250, 125);
            card.setMaxSize(250, 125);
            card.setAlignment(Pos.CENTER);
            card.setSpacing(7);
            card.setPadding(new Insets(12));
            details.setAlignment(Pos.CENTER);
            details.setPrefWidth(210);
            details.setMaxWidth(210);
            nameLabel.setMaxWidth(210);
            infoLabel.setMaxWidth(210);
            nameLabel.setAlignment(Pos.CENTER);
            infoLabel.setAlignment(Pos.CENTER);
        } else if (selectedViewType.equals("Compact View")) {
            card.setPrefWidth(1000);
            card.setMinWidth(1000);
            card.setPrefHeight(50);
            card.setMinHeight(50);
            card.setPadding(new Insets(5, 10, 5, 10));
            icon.setPrefSize(32, 32);
            icon.setMinSize(32, 32);
            icon.setMaxSize(32, 32);
            icon.setFont(Font.font(FONT, 16));
            details.setPrefWidth(900);
            details.setMaxWidth(900);
            nameLabel.setMaxWidth(900);
            infoLabel.setMaxWidth(900);
        } else {
            card.setPrefWidth(1000);
            card.setMinWidth(1000);
            card.setPrefHeight(72);
            card.setMinHeight(72);
            card.setPadding(new Insets(12));
            details.setPrefWidth(900);
            details.setMaxWidth(900);
            nameLabel.setMaxWidth(900);
            infoLabel.setMaxWidth(900);
        }

        String normalStyle = selectedViewType.equals("Grid View")
                ? "-fx-background-color:#F8FAFC;-fx-border-color:" + BORDER +
                  ";-fx-border-radius:10;-fx-background-radius:10;-fx-cursor:hand;"
                : selectedViewType.equals("Compact View")
                ? "-fx-background-color:#F8FAFC;-fx-border-color:" + BORDER +
                  ";-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;"
                : "-fx-background-color:#F8FAFC;-fx-border-color:" + BORDER +
                  ";-fx-border-radius:9;-fx-background-radius:9;-fx-cursor:hand;";

        card.setStyle(normalStyle);

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color:#EEF2FF;-fx-border-color:" + PURPLE +
                    ";-fx-border-radius:9;-fx-background-radius:9;-fx-cursor:hand;");
            nameLabel.setTextFill(Color.web(TEXT));
            infoLabel.setTextFill(Color.web(MUTED));
            icon.setTextFill(Color.web(PURPLE));
        });

        card.setOnMouseExited(e -> {
            card.setStyle(normalStyle);
            nameLabel.setTextFill(Color.web(TEXT));
            infoLabel.setTextFill(Color.web(MUTED));
            icon.setTextFill(Color.web(PURPLE));
        });

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
        button.setStyle("-fx-background-color:" + WHITE + ";-fx-text-fill:" + TEXT +
                ";-fx-font-size:11px;-fx-font-weight:bold;-fx-background-radius:8;" +
                "-fx-border-color:" + BORDER + ";-fx-border-radius:8;-fx-cursor:hand;");
        return button;
    }

    private ContextMenu createMenu(Button button, String menuType, String... options) {
        ContextMenu menu = new ContextMenu();
        menu.setStyle("-fx-background-color:white;-fx-background-radius:8;" +
                "-fx-border-color:" + BORDER + ";-fx-border-radius:8;");

        for (String option : options) {
            MenuItem item = new MenuItem(option);
            item.setStyle("-fx-font-size:12px;-fx-padding:8 18 8 18;");

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
        return new VBox(3, title, value);
    }

    private void styleDetailTitle(Label label) {
        label.setFont(Font.font(FONT, FontWeight.BOLD, 9));
        label.setTextFill(Color.web(MUTED));
        label.setStyle("-fx-text-fill:" + MUTED + ";");
    }

    private void styleDetailValue(Label label) {
        label.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        label.setTextFill(Color.web(TEXT));
        label.setStyle("-fx-text-fill:" + TEXT + ";");
    }

    private void styleActionButton(Button button, boolean primary) {
        button.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        button.setPadding(new Insets(8, 13, 8, 13));

        if (primary) {
            button.setTextFill(Color.WHITE);
            button.setStyle("-fx-background-color:" + PURPLE +
                    ";-fx-text-fill:white;-fx-background-radius:7;-fx-cursor:hand;");
        } else {
            button.setTextFill(Color.web(TEXT));
            button.setStyle("-fx-background-color:" + WHITE +
                    ";-fx-text-fill:" + TEXT + ";-fx-border-color:" + BORDER +
                    ";-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;");
        }
    }

    private VBox createStatCard(String icon, String title, String value, String subtitle) {
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        iconLabel.setTextFill(Color.web(PURPLE));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(FONT, 10));
        titleLabel.setTextFill(Color.web(MUTED));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        valueLabel.setTextFill(Color.web(TEXT));

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font(FONT, 10));
        subtitleLabel.setTextFill(Color.web(MUTED));

        VBox text = new VBox(2, titleLabel, valueLabel, subtitleLabel);
        HBox row = new HBox(9, iconLabel, text);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(row);
        card.setPadding(new Insets(12));
        card.setMinHeight(80);
        card.setPrefHeight(80);
        card.setStyle("-fx-background-color:" + WHITE + ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:12;-fx-background-radius:12;");

        return card;
    }

    private Button createSidebarButton(String icon, String text, boolean active) {
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(FONT, 13));

        Label textLabel = new Label(text);
        textLabel.setFont(Font.font(FONT,
                active ? FontWeight.BOLD : FontWeight.MEDIUM, 11));

        HBox content = new HBox(10, iconLabel, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("", content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(36);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 9, 0, 9));

        if (active) {
            button.setStyle("-fx-background-color:#EEF2FF;-fx-background-radius:8;-fx-cursor:hand;");
            iconLabel.setTextFill(Color.web(PURPLE));
            textLabel.setTextFill(Color.web(PURPLE));
        } else {
            button.setStyle("-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;");
            iconLabel.setTextFill(Color.web(MUTED));
            textLabel.setTextFill(Color.web(TEXT));

            button.setOnMouseEntered(e ->
                    button.setStyle("-fx-background-color:#F1F5F9;-fx-background-radius:8;-fx-cursor:hand;"));

            button.setOnMouseExited(e ->
                    button.setStyle("-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;"));
        }

        return button;
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