package com.file_handlers.view.userView;

import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;

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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.Desktop;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class UserSearch {

    // Style Constants - Exact Match to Reference UI
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar: Deepest Obsidian Navy
    private static final String BG_SIDEBAR = "#03060E";
    private static final String BG_SIDEBAR_CARD = "#070E1C";
    private static final String SIDEBAR_BORDER = "#101D33";

    // 2. Center Workspace Canvas: Atmospheric Dark Canvas
    private static final String BG_CENTER_CANVAS = "radial-gradient(center 60% 15%, radius 85%, #0B1933 0%, #050B17 55%, #03060E 100%)";

    // 3. Main Dashboard Cards & Inner Layers
    private static final String BG_CARD = "#08101E";
    private static final String BG_CARD_INNER = "#050B16";
    private static final String BORDER_CARD = "#142542";
    private static final String BORDER_CARD_HOVER = "#2563EB";

    // 4. Contrast Typography & Accents
    private static final String TEXT_DARK = "#FFFFFF";        
    private static final String TEXT_MUTED_DARK = "#8FA0BC";  
    private static final String TEXT_LIGHT = "#FFFFFF";       
    private static final String TEXT_MUTED_LIGHT = "#6B7C99"; 
    private static final String TEXT_GOLDEN = "#FBBF24";

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
        String activeUserName = "User";
        String initials = "U";

        if (UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null) {
            String fullName = UserSession.getInstance().getDisplayName().trim();
            if (!fullName.isEmpty()) {
                String[] parts = fullName.split("\\s+");
                activeUserName = parts[0];
                initials = activeUserName.substring(0, 1).toUpperCase();
            }
        }

        // =========================================================
        // SIDEBAR
        // =========================================================

        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 19px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + "; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.4), 8, 0, 0, 0);");

        HBox logoHeader = new HBox(12, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(4, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", true);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);
        Button logoutBtn = createSidebarButton("🚪", "Logout", false);

        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        aiBtn.setOnAction(e -> LandingPage.showAiAssistantPage());
        collabBtn.setOnAction(e -> LandingPage.showCollaborationPage());
        recentBtn.setOnAction(e -> LandingPage.showRecentPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());
        settingsBtn.setOnAction(e -> LandingPage.showSettingPage());
        logoutBtn.setOnAction(e -> LandingPage.showUserLoginPage());

        VBox navList = new VBox(5, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

        // Sidebar Storage Card
        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        storageTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox storageValGroup = new HBox(storageVal, new Region(), storagePercent);
        HBox.setHgrow(storageValGroup.getChildren().get(1), Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle(
                "-fx-accent: linear-gradient(to right, #1D4ED8, #38BDF8);" +
                "-fx-control-inner-background: #03060E;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 0;"
        );

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: transparent; -fx-text-fill: #38BDF8; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexedPage());

        VBox storageCard = new VBox(9, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle(
                "-fx-background-color: " + BG_SIDEBAR_CARD + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 4);"
        );

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(10, logoBox, navList, sidebarSpacer, settingsBtn, logoutBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(235);
        sidebar.setMinWidth(235);
        sidebar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        // =========================================================
        // TOP SEARCH BAR & PROFILE
        // =========================================================

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        TextField topSearchField = new TextField();
        topSearchField.setPromptText("Search files, folders or smart spaces...");
        topSearchField.setPrefHeight(38);
        topSearchField.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 600; -fx-background-color: #03060E; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 7; -fx-background-radius: 5; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 5;");

        HBox topSearchContainer = new HBox(10, searchIcon, topSearchField, keyShortcut);
        topSearchContainer.setAlignment(Pos.CENTER_LEFT);
        topSearchContainer.setPadding(new Insets(0, 12, 0, 14));
        topSearchContainer.setPrefWidth(520);
        topSearchContainer.setStyle(
                "-fx-background-color: #060D19;" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;"
        );
        HBox.setHgrow(topSearchField, Priority.ALWAYS);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: #060D19; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-size: 14px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand; -fx-padding: 6 10;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF);" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 8, 0, 0, 2);"
        );

        Label userName = new Label(activeUserName);
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label dropDown = new Label("⌄");
        dropDown.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox profileOption = new HBox(8, avatar, userName, dropDown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(4, 12, 4, 6));
        profileOption.setStyle(
                "-fx-background-color: #060D19;" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-cursor: hand;"
        );

        profileOption.setOnMouseClicked(e -> LandingPage.showUserProfilePage());
        profileOption.setOnMouseEntered(e -> profileOption.setStyle("-fx-background-color: #0C1A30; -fx-border-color: #1D4ED8; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;"));
        profileOption.setOnMouseExited(e -> profileOption.setStyle("-fx-background-color: #060D19; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;"));

        HBox profileBox = new HBox(12, bellBtn, profileOption);
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
        titleLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 24px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label subLabel = new Label("Search and discover files indexed by OneSpace.");
        subLabel.setFont(Font.font(FONT, 13));
        subLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        VBox titleBox = new VBox(4, titleLabel, subLabel);

        Label mainSearchIcon = new Label("⌕");
        mainSearchIcon.setFont(Font.font(FONT, 18));
        mainSearchIcon.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 18px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        TextField mainSearchField = new TextField();
        mainSearchField.setPromptText("Search anything about your files...");
        mainSearchField.setPrefHeight(48);
        mainSearchField.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: transparent; -fx-text-fill: " + TEXT_LIGHT + "; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 14px;");
        mainSearchField.textProperty().addListener((o, x, y) -> {
            searchQuery = y.toLowerCase();
            updateResultsView();
        });

        HBox searchBarBox = new HBox(12, mainSearchIcon, mainSearchField);
        searchBarBox.setAlignment(Pos.CENTER_LEFT);
        searchBarBox.setPadding(new Insets(0, 18, 0, 18));
        searchBarBox.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-border-width: 1;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 2);"
        );
        HBox.setHgrow(mainSearchField, Priority.ALWAYS);

        // Filter Controls (Styled in Vibrant Purple Theme)
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
        aiTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 15px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label confidenceBadge = new Label("94% confidence");
        confidenceBadge.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        confidenceBadge.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: #34D399; -fx-background-color: rgba(16, 185, 129, 0.15); -fx-border-color: rgba(16, 185, 129, 0.35); -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 3 9;");

        HBox aiHeader = new HBox(aiTitle, new Region(), confidenceBadge);
        HBox.setHgrow(aiHeader.getChildren().get(1), Priority.ALWAYS);
        aiHeader.setAlignment(Pos.CENTER_LEFT);

        Label aiText = new Label("Found matches for your query. The strongest match is Aadhaar_Card_Scan.pdf stored in your Documents folder.");
        aiText.setFont(Font.font(FONT, 13));
        aiText.setWrapText(true);
        aiText.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 500;");

        Button actionBtn1 = createCardActionButton("Open best match");
        Button actionBtn2 = createCardActionButton("Create reminder");
        Button actionBtn3 = createCardActionButton("Add to Space");

        HBox actionRow = new HBox(10, actionBtn1, actionBtn2, actionBtn3);

        VBox aiCard = new VBox(14, aiHeader, aiText, actionRow);
        aiCard.setPadding(new Insets(20));
        aiCard.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-border-width: 1;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 16, 0, 0, 6);"
        );

        // =========================================================
        // RESULTS CONTAINER & VIEW SWITCHER
        // =========================================================

        Label resultsHeader = new Label("Results");
        resultsHeader.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        resultsHeader.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 18px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

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

        HBox rightControls = new HBox(10, filterBtn, viewBtn);
        rightControls.setAlignment(Pos.CENTER_RIGHT);

        HBox resultsBar = new HBox(resultsHeader, new Region(), rightControls);
        HBox.setHgrow(resultsBar.getChildren().get(1), Priority.ALWAYS);
        resultsBar.setAlignment(Pos.CENTER_LEFT);

        listContainer = new VBox(12);
        gridContainer = new GridPane();
        gridContainer.setHgap(16);
        gridContainer.setVgap(16);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33.33);
        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33.33);
        col2.setHgrow(Priority.ALWAYS);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33.33);
        col3.setHgrow(Priority.ALWAYS);
        gridContainer.getColumnConstraints().addAll(col1, col2, col3);

        contentBox = new StackPane();
        updateResultsView();

        // =========================================================
        // SCROLLABLE CONTAINER
        // =========================================================

        VBox contentBody = new VBox(22, titleBox, searchBarBox, aiCard, resultsBar, contentBox);
        contentBody.setPadding(new Insets(24, 28, 28, 28));
        contentBody.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background: " + BG_CENTER_CANVAS + "; -fx-background-color: " + BG_CENTER_CANVAS + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_SIDEBAR + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        Scene scene = new Scene(root, 1200, 750);

        // Dark Theme Stylesheet for MenuButton / ContextMenu Popups
        String cssPopupStyle = 
                ".context-menu {" +
                "    -fx-background-color: #070E1C;" +
                "    -fx-border-color: rgba(168, 85, 247, 0.45);" +
                "    -fx-border-width: 1.2px;" +
                "    -fx-border-radius: 10px;" +
                "    -fx-background-radius: 10px;" +
                "    -fx-padding: 6px;" +
                "    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.7), 16, 0, 0, 6);" +
                "}" +
                ".menu-item {" +
                "    -fx-background-color: transparent;" +
                "    -fx-padding: 8px 16px;" +
                "    -fx-background-radius: 6px;" +
                "}" +
                ".menu-item .label {" +
                "    -fx-text-fill: #E2E8F0;" +
                "    -fx-font-family: 'Inter', sans-serif;" +
                "    -fx-font-size: 12px;" +
                "    -fx-font-weight: 600;" +
                "}" +
                ".menu-item:focused, .menu-item:hover {" +
                "    -fx-background-color: linear-gradient(to right, #7C3AED, #A855F7);" +
                "}" +
                ".menu-item:focused .label, .menu-item:hover .label {" +
                "    -fx-text-fill: #FFFFFF;" +
                "}";

        scene.getStylesheets().add("data:text/css," + cssPopupStyle.replace(" ", "%20").replace("\n", ""));

        return scene;
    }

    // =========================================================
    // HELPER BUILDERS & ACTIONS
    // =========================================================

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

    private Button createSidebarButton(String icon, String label, boolean isActive) {
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(FONT, 14));

        Label textLbl = new Label(label);
        textLbl.setFont(Font.font(FONT, isActive ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        textLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: " + (isActive ? "700" : "500") + "; -fx-text-fill: " + (isActive ? "#FFFFFF" : TEXT_MUTED_LIGHT) + ";");

        HBox content = new HBox(12, iconLbl, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(40);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 14, 0, 14));

        if (isActive) {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #1D4ED8, #3B82F6); " +
                    "-fx-background-radius: 10; " +
                    "-fx-border-color: #60A5FA; " +
                    "-fx-border-radius: 10; " +
                    "-fx-border-width: 1; " +
                    "-fx-cursor: hand; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 12, 0, 0, 2);"
            );
            iconLbl.setStyle("-fx-text-fill: #FFFFFF;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 10; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

            btn.setOnMouseEntered(e -> {
                btn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.04); -fx-background-radius: 10; -fx-cursor: hand;");
                iconLbl.setStyle("-fx-text-fill: #FFFFFF;");
                textLbl.setStyle("-fx-text-fill: #FFFFFF;");
            });
            btn.setOnMouseExited(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 10; -fx-cursor: hand;");
                iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
                textLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
            });
        }

        return btn;
    }

    private Button createCardActionButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        btn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: #60A5FA;" +
                "-fx-padding: 7 14;" +
                "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-color: #0F203C;" +
                "-fx-border-color: #2563EB;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-padding: 7 14;" +
                "-fx-cursor: hand;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: #60A5FA;" +
                "-fx-padding: 7 14;" +
                "-fx-cursor: hand;"
        ));

        return btn;
    }

    private void styleDropdownMenu(MenuButton btn) {
        btn.setPrefHeight(38);
        btn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        
        String idleStyle = 
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-color: rgba(168, 85, 247, 0.15);" +
                "-fx-border-color: rgba(168, 85, 247, 0.45);" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #C084FC;" +
                "-fx-padding: 2 14;" +
                "-fx-cursor: hand;" +
                "-fx-mark-color: #C084FC;";

        String hoverStyle = 
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-color: linear-gradient(to right, #7C3AED, #A855F7);" +
                "-fx-border-color: #D8B4FE;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-padding: 2 14;" +
                "-fx-cursor: hand;" +
                "-fx-mark-color: #FFFFFF;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(168, 85, 247, 0.45), 10, 0, 0, 2);";

        btn.setStyle(idleStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(idleStyle));
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
            emptyLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
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
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }
        }

        if (gridContainer.getChildren().isEmpty()) {
            Label emptyLbl = new Label("No files found matching your query.");
            emptyLbl.setFont(Font.font(FONT, 13));
            emptyLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
            gridContainer.add(emptyLbl, 0, 0);
        }

        contentBox.getChildren().setAll(gridContainer);
    }

    private VBox createFileCard(FileInfo file, boolean isGrid) {
        Label typeBadge = new Label(file.type);
        typeBadge.setFont(Font.font(FONT, FontWeight.BOLD, 10));

        // Color coding: Golden for images (JPG, PNG, JPEG), Vibrant Red for video files (MP4, AVI, MKV), and distinct tones for others
        String badgeColorStyle;
        if (file.type.equalsIgnoreCase("JPG") || file.type.equalsIgnoreCase("PNG") || file.type.equalsIgnoreCase("JPEG")) {
            badgeColorStyle = 
                    "-fx-background-color: rgba(245, 158, 11, 0.18);" +
                    "-fx-text-fill: " + TEXT_GOLDEN + ";" +
                    "-fx-border-color: rgba(245, 158, 11, 0.45);" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(245, 158, 11, 0.25), 6, 0, 0, 0);";
        } else if (file.type.equalsIgnoreCase("MP4") || file.type.equalsIgnoreCase("AVI") || file.type.equalsIgnoreCase("MKV")) {
            badgeColorStyle = 
                    "-fx-background-color: rgba(239, 68, 68, 0.2);" +
                    "-fx-text-fill: #EF4444;" +
                    "-fx-border-color: rgba(239, 68, 68, 0.5);" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(239, 68, 68, 0.3), 6, 0, 0, 0);";
        } else if (file.type.equalsIgnoreCase("PDF")) {
            badgeColorStyle = 
                    "-fx-background-color: rgba(239, 68, 68, 0.15);" +
                    "-fx-text-fill: #F87171;" +
                    "-fx-border-color: rgba(239, 68, 68, 0.35);";
        } else if (file.type.equalsIgnoreCase("DOCX") || file.type.equalsIgnoreCase("PPTX") || file.type.equalsIgnoreCase("XLSX")) {
            badgeColorStyle = 
                    "-fx-background-color: rgba(37, 99, 235, 0.18);" +
                    "-fx-text-fill: #60A5FA;" +
                    "-fx-border-color: rgba(37, 99, 235, 0.4);";
        } else {
            badgeColorStyle = 
                    "-fx-background-color: rgba(168, 85, 247, 0.18);" +
                    "-fx-text-fill: #C084FC;" +
                    "-fx-border-color: rgba(168, 85, 247, 0.4);";
        }

        typeBadge.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: 700;" +
                badgeColorStyle +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 2 7;"
        );

        Label sizeLbl = new Label(file.size);
        sizeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        sizeLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox topRow = new HBox(typeBadge, new Region(), sizeLbl);
        HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label previewText = new Label("FILE PREVIEW");
        previewText.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        previewText.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_GOLDEN + ";");

        StackPane previewPane = new StackPane(previewText);
        previewPane.setPrefHeight(isGrid ? 46 : 34); 
        previewPane.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        previewPane.setOnMouseClicked(e -> {
            try {
                File fileObj = new File(file.path + "/" + file.name);
                if (fileObj.exists() && Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(fileObj);
                } else {
                    System.out.println("Opening preview for file: " + file.name);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button openInSpacesBtn = new Button("Open in Spaces");
        openInSpacesBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        openInSpacesBtn.setMaxWidth(Double.MAX_VALUE);
        openInSpacesBtn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " + TEXT_GOLDEN + ";" +
                "-fx-padding: 6 0;" +
                "-fx-cursor: hand;"
        );

        openInSpacesBtn.setOnMouseEntered(e -> openInSpacesBtn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-color: #0F203C;" +
                "-fx-border-color: #2563EB;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-padding: 6 0;" +
                "-fx-cursor: hand;"
        ));

        openInSpacesBtn.setOnMouseExited(e -> openInSpacesBtn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " + TEXT_GOLDEN + ";" +
                "-fx-padding: 6 0;" +
                "-fx-cursor: hand;"
        ));

        openInSpacesBtn.setOnAction(e -> LandingPage.showUserSpace());

        Label nameLbl = new Label(file.name);
        nameLbl.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        nameLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label pathLbl = new Label(file.path);
        pathLbl.setFont(Font.font(FONT, 10));
        pathLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        Label dateLbl = new Label(file.date);
        dateLbl.setFont(Font.font(FONT, 10));
        dateLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        Label optionsBtn = new Label("⋮");
        optionsBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        optionsBtn.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-cursor: hand;");

        HBox bottomRow = new HBox(dateLbl, new Region(), optionsBtn);
        HBox.setHgrow(bottomRow.getChildren().get(1), Priority.ALWAYS);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(8, topRow, previewPane, openInSpacesBtn, nameLbl, pathLbl, bottomRow);
        card.setPadding(new Insets(14));

        String styleIdle = "-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 14; -fx-background-radius: 14; -fx-border-width: 1; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 12, 0, 0, 3);";
        String styleHover = "-fx-background-color: #0D192F; -fx-border-color: " + BORDER_CARD_HOVER + "; -fx-border-radius: 14; -fx-background-radius: 14; -fx-border-width: 1; -fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.4), 16, 0, 0, 4); -fx-cursor: hand;";

        card.setStyle(styleIdle);
        card.setOnMouseEntered(e -> card.setStyle(styleHover));
        card.setOnMouseExited(e -> card.setStyle(styleIdle));

        card.setMaxWidth(Double.MAX_VALUE);

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