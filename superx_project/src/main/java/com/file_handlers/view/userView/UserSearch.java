package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Arrays;
import java.util.List;

public class UserSearch {

    // Style Constants - Exact Color Hierarchy matching UserDashboard
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar: Deep Dark Slate
    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";

    // 2. Center Workspace Canvas: Medium Slate Blue
    private static final String BG_CENTER_CANVAS = "#31435B";

    // 3. Main Cards: Soft Light Blue
    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_CARD_INNER = "#CADDF2";
    private static final String BORDER_CARD = "#C3D6EC";

    // 4. Contrast Typography
    private static final String TEXT_DARK = "#0F172A";        // Deep Navy for headings / big numbers
    private static final String TEXT_MUTED_DARK = "#334155";  // Slate for subtext / labels inside cards
    private static final String TEXT_LIGHT = "#FFFFFF";       // Main white text on dark surfaces
    private static final String TEXT_MUTED_LIGHT = "#94A3B8"; // Subtext on dark surfaces

    // Accent Colors
    private static final String PRIMARY_BLUE = "#2563EB";

    // File Data Model
    private final List<FileInfo> files = Arrays.asList(
            new FileInfo("PDF", "Aadhaar_Card_Scan.pdf", "C:/Users/you/Documents/IDs", "1.2 MB", "12 Mar 2026"),
            new FileInfo("PDF", "Java_Unit3_Notes.pdf", "C:/Users/you/College/Notes", "4.6 MB", "02 Jul 2026"),
            new FileInfo("DOCX", "DBMS_Assignment_4.docx", "C:/Users/you/College/Assignments", "780 KB", "27 Jun 2026"),
            new FileInfo("PPTX", "Computer_Graphics.pptx", "C:/Users/you/College/Presentations", "3.8 MB", "18 Jul 2026"),
            new FileInfo("XLSX", "College_Expenses.xlsx", "C:/Users/you/Finance", "620 KB", "22 Jul 2026"),
            new FileInfo("JPG", "College_Event.jpg", "C:/Users/you/Pictures/College", "2.4 MB", "20 Jul 2026"),
            new FileInfo("PNG", "OneSpace_Logo.png", "C:/Users/you/Pictures/Projects", "850 KB", "10 Aug 2026"),
            new FileInfo("MP4", "Project_Demo.mp4", "C:/Users/you/Videos/Projects", "24.5 MB", "12 Aug 2026"),
            new FileInfo("AVI", "College_Event.avi", "C:/Users/you/Videos/College", "18.2 MB", "05 Aug 2026")
    );

    private VBox listContainer;
    private GridPane gridContainer;
    private StackPane contentBox;
    private String selectedType = "All";
    private String searchQuery = "";
    private boolean isGridView = false;

    public Scene getUserSearchScene() {

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
        Button searchBtn = createSidebarButton("⌕", "Search", true);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);

        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        aiBtn.setOnAction(e -> LandingPage.showLandingPage());
        collabBtn.setOnAction(e -> LandingPage.showLandingPage());
        recentBtn.setOnAction(e -> LandingPage.showLandingPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());
        settingsBtn.setOnAction(e -> LandingPage.showLandingPage());

        VBox navList = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

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

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: " + PRIMARY_BLUE + "; -fx-control-inner-background: #0E1520;");

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showLandingPage());

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, storageCard);
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

        TextField topSearchField = new TextField();
        topSearchField.setPromptText("Search in OneSpace...");
        topSearchField.setPrefHeight(38);
        topSearchField.setStyle("-fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-background-color: #141E2C; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 6; -fx-background-radius: 4;");

        HBox topSearchContainer = new HBox(8, searchIcon, topSearchField, keyShortcut);
        topSearchContainer.setAlignment(Pos.CENTER_LEFT);
        topSearchContainer.setPadding(new Insets(0, 12, 0, 14));
        topSearchContainer.setPrefWidth(420);
        topSearchContainer.setStyle("-fx-background-color: #141E2C; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(topSearchField, Priority.ALWAYS);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 50%; -fx-text-fill: " + TEXT_LIGHT + "; -fx-font-weight: bold; -fx-font-size: 12px;");

        Label userName = new Label("Aarav Verma");
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label dropDown = new Label("⌄");
        dropDown.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox profileBox = new HBox(10, bellBtn, avatar, userName, dropDown);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, topSearchContainer, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        // =========================================================
        // SEARCH HEADER & CONTROLS
        // =========================================================

        Label titleLabel = new Label("Search files");
        titleLabel.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label subLabel = new Label("Search and discover files indexed by OneSpace.");
        subLabel.setFont(Font.font(FONT, 13));
        subLabel.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        VBox titleBox = new VBox(4, titleLabel, subLabel);

        Label mainSearchIcon = new Label("⌕");
        mainSearchIcon.setFont(Font.font(FONT, 18));
        mainSearchIcon.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        TextField mainSearchField = new TextField();
        mainSearchField.setPromptText("Search anything about your files...");
        mainSearchField.setPrefHeight(44);
        mainSearchField.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_LIGHT + "; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 14px;");
        mainSearchField.textProperty().addListener((o, x, y) -> {
            searchQuery = y.toLowerCase();
            updateResultsView();
        });

        HBox searchBarBox = new HBox(10, mainSearchIcon, mainSearchField);
        searchBarBox.setAlignment(Pos.CENTER_LEFT);
        searchBarBox.setPadding(new Insets(0, 16, 0, 16));
        searchBarBox.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");
        HBox.setHgrow(mainSearchField, Priority.ALWAYS);

        // Filter Controls
        MenuButton filterBtn = new MenuButton("Filter: All");
        styleDropdownMenu(filterBtn);

        MenuItem filterAll = new MenuItem("All Files");
        MenuItem filterDocs = new MenuItem("Documents");
        MenuItem filterImgs = new MenuItem("Images");
        MenuItem filterVids = new MenuItem("Videos");
        MenuItem filterPdfs = new MenuItem("PDFs");

        filterBtn.getItems().addAll(filterAll, filterDocs, filterImgs, filterVids, filterPdfs);

        filterAll.setOnAction(e -> applyFilter(filterBtn, "All", "Filter: All"));
        filterDocs.setOnAction(e -> applyFilter(filterBtn, "Documents", "Filter: Documents"));
        filterImgs.setOnAction(e -> applyFilter(filterBtn, "Images", "Filter: Images"));
        filterVids.setOnAction(e -> applyFilter(filterBtn, "Videos", "Filter: Videos"));
        filterPdfs.setOnAction(e -> applyFilter(filterBtn, "PDFs", "Filter: PDFs"));

        // =========================================================
        // AI ASSISTANT ANSWER CARD
        // =========================================================

        Label aiTitle = new Label("✦ AI Answer");
        aiTitle.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        aiTitle.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label confidenceBadge = new Label("94% confidence");
        confidenceBadge.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        confidenceBadge.setStyle("-fx-text-fill: #15803D; -fx-background-color: #DCFCE7; -fx-background-radius: 6; -fx-padding: 3 8;");

        HBox aiHeader = new HBox(aiTitle, new Region(), confidenceBadge);
        HBox.setHgrow(aiHeader.getChildren().get(1), Priority.ALWAYS);
        aiHeader.setAlignment(Pos.CENTER_LEFT);

        Label aiText = new Label("Found matches for your query. The strongest match is Aadhaar_Card_Scan.pdf stored in your Documents folder.");
        aiText.setFont(Font.font(FONT, 13));
        aiText.setWrapText(true);
        aiText.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 500;");

        Button actionBtn1 = createCardActionButton("Open best match");
        Button actionBtn2 = createCardActionButton("Create reminder");
        Button actionBtn3 = createCardActionButton("Add to Space");

        HBox actionRow = new HBox(8, actionBtn1, actionBtn2, actionBtn3);

        VBox aiCard = new VBox(12, aiHeader, aiText, actionRow);
        aiCard.setPadding(new Insets(18));
        aiCard.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.14), 12, 0, 0, 4);"
        );

        // =========================================================
        // RESULTS CONTAINER & VIEW SWITCHER
        // =========================================================

        Label resultsHeader = new Label("Results");
        resultsHeader.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        resultsHeader.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        MenuButton viewBtn = new MenuButton("List View");
        styleDropdownMenu(viewBtn);

        MenuItem listViewOption = new MenuItem("List View");
        MenuItem gridViewOption = new MenuItem("Grid View");
        viewBtn.getItems().addAll(listViewOption, gridViewOption);

        listViewOption.setOnAction(e -> {
                isGridView = false;
                viewBtn.setText("List View");
                updateResultsView();
        });

        gridViewOption.setOnAction(e -> {
                isGridView = true;
                viewBtn.setText("Grid View");
                updateResultsView();
        });

        // Group Filter button and View switcher together on the right side
        HBox rightControls = new HBox(10, filterBtn, viewBtn);
        rightControls.setAlignment(Pos.CENTER_RIGHT);

        HBox resultsBar = new HBox(resultsHeader, new Region(), rightControls);
        HBox.setHgrow(resultsBar.getChildren().get(1), Priority.ALWAYS);
        resultsBar.setAlignment(Pos.CENTER_LEFT);

        listContainer = new VBox(12);
        gridContainer = new GridPane();
        gridContainer.setHgap(14);
        gridContainer.setVgap(14);

        contentBox = new StackPane();
        updateResultsView();

        // =========================================================
        // SCROLLABLE CONTAINER
        // =========================================================

        // Removed filterBtn from the vertical stack since it's now inside resultsBar
        VBox contentBody = new VBox(20, titleBox, searchBarBox, aiCard, resultsBar, contentBox);
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

    // =========================================================
    // HELPER BUILDERS & ACTIONS
    // =========================================================

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

    private Button createCardActionButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        btn.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " + PRIMARY_BLUE + ";" +
                "-fx-padding: 6 12;" +
                "-fx-cursor: hand;"
        );
        return btn;
    }

    private void styleDropdownMenu(MenuButton btn) {
        btn.setPrefHeight(36);
        btn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        btn.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " + TEXT_DARK + ";" +
                "-fx-cursor: hand;"
        );
    }

    private void applyFilter(MenuButton button, String type, String displayLabel) {
        this.selectedType = type;
        button.setText(displayLabel);
        updateResultsView();
    }

    private void updateResultsView() {
        if (isGridView) {
            renderGrid();
        } else {
            renderList();
        }
    }

    private void renderList() {
        listContainer.getChildren().clear();

        for (FileInfo f : files) {
            if (matchesType(f) && matchesSearchQuery(f)) {
                listContainer.getChildren().add(createFileCard(f, false));
            }
        }

        if (listContainer.getChildren().isEmpty()) {
            Label emptyLbl = new Label("No files found matching your query.");
            emptyLbl.setFont(Font.font(FONT, 13));
            emptyLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
            listContainer.getChildren().add(emptyLbl);
        }

        contentBox.getChildren().setAll(listContainer);
    }

    private void renderGrid() {
        gridContainer.getChildren().clear();
        int col = 0;
        int row = 0;

        for (FileInfo f : files) {
            if (matchesType(f) && matchesSearchQuery(f)) {
                VBox card = createFileCard(f, true);
                gridContainer.add(card, col, row);

                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            }
        }

        if (gridContainer.getChildren().isEmpty()) {
            Label emptyLbl = new Label("No files found matching your query.");
            emptyLbl.setFont(Font.font(FONT, 13));
            emptyLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
            gridContainer.add(emptyLbl, 0, 0);
        }

        contentBox.getChildren().setAll(gridContainer);
    }

    private VBox createFileCard(FileInfo file, boolean isGrid) {
    Label typeBadge = new Label(file.type);
    typeBadge.setFont(Font.font(FONT, FontWeight.BOLD, 10));
    typeBadge.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: " + PRIMARY_BLUE + "; -fx-background-radius: 5; -fx-padding: 2 6;");

    Label sizeLbl = new Label(file.size);
    sizeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
    sizeLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

    HBox topRow = new HBox(typeBadge, new Region(), sizeLbl);
    HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
    topRow.setAlignment(Pos.CENTER_LEFT);

    // Reduced height preview pane
    Label previewText = new Label("FILE PREVIEW");
    previewText.setFont(Font.font(FONT, FontWeight.BOLD, 10));
    previewText.setStyle("-fx-text-fill: " + PRIMARY_BLUE + ";");

    StackPane previewPane = new StackPane(previewText);
    previewPane.setPrefHeight(isGrid ? 42 : 32); 
    previewPane.setStyle("-fx-background-color: " + BG_CARD_INNER + "; -fx-background-radius: 6;");

    Label nameLbl = new Label(file.name);
    nameLbl.setFont(Font.font(FONT, FontWeight.BOLD, 13));
    nameLbl.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

    Label pathLbl = new Label(file.path);
    pathLbl.setFont(Font.font(FONT, 10));
    pathLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

    Label dateLbl = new Label(file.date);
    dateLbl.setFont(Font.font(FONT, 10));
    dateLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

    Label optionsBtn = new Label("⋮");
    optionsBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
    optionsBtn.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-cursor: hand;");

    HBox bottomRow = new HBox(dateLbl, new Region(), optionsBtn);
    HBox.setHgrow(bottomRow.getChildren().get(1), Priority.ALWAYS);
    bottomRow.setAlignment(Pos.CENTER_LEFT);

    // Reduced gap (6px) and padding (10px)
    VBox card = new VBox(6, topRow, previewPane, nameLbl, pathLbl, bottomRow);
    card.setPadding(new Insets(10));
    card.setStyle(
            "-fx-background-color: " + BG_CARD + ";" +
            "-fx-border-color: " + BORDER_CARD + ";" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 6, 0, 0, 2);"
    );

    if (isGrid) {
        card.setPrefWidth(300); // Reduced from 420px to 300px
    } else {
        card.setMaxWidth(Double.MAX_VALUE);
    }

    return card;
}

    private boolean matchesSearchQuery(FileInfo f) {
        if (searchQuery.isEmpty()) return true;
        return f.name.toLowerCase().contains(searchQuery) ||
               f.type.toLowerCase().contains(searchQuery) ||
               f.path.toLowerCase().contains(searchQuery);
    }

    private boolean matchesType(FileInfo f) {
        if (selectedType.equals("All")) return true;
        if (selectedType.equals("PDFs")) return f.type.equals("PDF");
        if (selectedType.equals("Documents"))
            return f.type.equals("DOCX") || f.type.equals("PPTX") || f.type.equals("XLSX");
        if (selectedType.equals("Images"))
            return f.type.equals("JPG") || f.type.equals("PNG") || f.type.equals("JPEG");
        return selectedType.equals("Videos") &&
               (f.type.equals("MP4") || f.type.equals("AVI") || f.type.equals("MKV"));
    }

    private static class FileInfo {
        String type, name, path, size, date;
        FileInfo(String t, String n, String p, String s, String d) {
            this.type = t;
            this.name = n;
            this.path = p;
            this.size = s;
            this.date = d;
        }
    }
}