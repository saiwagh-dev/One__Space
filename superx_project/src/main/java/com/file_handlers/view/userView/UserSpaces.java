package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class UserSpaces {

    // Style Constants - Exact Theme Hierarchy
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
    private static final String TEXT_DARK = "#0F172A";        // Deep Navy
    private static final String TEXT_MUTED_DARK = "#334155";  // Slate
    private static final String TEXT_LIGHT = "#FFFFFF";       // White text for dark surfaces
    private static final String TEXT_MUTED_LIGHT = "#94A3B8"; // Subtext for dark surfaces

    // Accent Colors
    private static final String PRIMARY_BLUE = "#2563EB";

    public Scene getUserSpacesScene() {

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

        // Sidebar Navigation
        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("📁", "Spaces", true);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);

        dashboardBtn.setOnAction(e -> { LandingPage.showUserDashboard(); });
        spacesBtn.setOnAction(e -> { LandingPage.showLandingPage(); });
        searchBtn.setOnAction(e -> { LandingPage.showLandingPage(); });
        calendarBtn.setOnAction(e -> { LandingPage.showLandingPage(); });
        aiBtn.setOnAction(e -> { LandingPage.showLandingPage(); });
        collabBtn.setOnAction(e -> { LandingPage.showLandingPage(); });
        recentBtn.setOnAction(e -> { LandingPage.showLandingPage(); });
        trashBtn.setOnAction(e -> { LandingPage.showLandingPage(); });
        settingsBtn.setOnAction(e -> { LandingPage.showLandingPage(); });

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
        manageStorageBtn.setOnAction(e -> { LandingPage.showLandingPage(); });

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

        TextField searchField = new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-background-color: #141E2C; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 6; -fx-background-radius: 4;");

        HBox searchContainer = new HBox(8, searchIcon, searchField, keyShortcut);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0, 12, 0, 14));
        searchContainer.setPrefWidth(420);
        searchContainer.setStyle("-fx-background-color: #141E2C; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand;");

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

        HBox topBar = new HBox(20, searchContainer, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        // =========================================================
        // PAGE HEADER
        // =========================================================

        Label pageTitle = new Label("Spaces");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        pageTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label pageDescription = new Label("Virtual groupings built by AI. Files remain in their original folders.");
        pageDescription.setFont(Font.font(FONT, 13));
        pageDescription.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        VBox headerTitleBox = new VBox(4, pageTitle, pageDescription);

        Button newSpaceButton = new Button("+  New Space");
        newSpaceButton.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        newSpaceButton.setStyle(
                "-fx-background-color: " + PRIMARY_BLUE + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 8 18;"
        );
        newSpaceButton.setOnAction(e -> { LandingPage.showLandingPage(); });

        HBox pageHeader = new HBox(headerTitleBox, new Region(), newSpaceButton);
        HBox.setHgrow(pageHeader.getChildren().get(1), Priority.ALWAYS);
        pageHeader.setAlignment(Pos.CENTER_LEFT);

        // =========================================================
        // SPACES GRID (3 COLUMNS x 2 ROWS)
        // =========================================================

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);

        ColumnConstraints col1 = new ColumnConstraints(); col1.setPercentWidth(33.33);
        ColumnConstraints col2 = new ColumnConstraints(); col2.setPercentWidth(33.33);
        ColumnConstraints col3 = new ColumnConstraints(); col3.setPercentWidth(33.33);
        grid.getColumnConstraints().addAll(col1, col2, col3);

        VBox personalCard = createSpaceCard("👤", "#F3E8FF", "#7C3AED", "Personal",
                "IDs, certificates, personal photos and everyday documents.",
                "1,284 files", "8.2 GB", "Updated 2 minutes ago");

        VBox collegeCard = createSpaceCard("🎓", "#BAE6FD", "#0284C7", "College",
                "Notes, assignments, lab records, presentations and projects.",
                "946 files", "12.7 GB", "Updated 18 minutes ago");

        VBox workCard = createSpaceCard("💼", "#A7F3D0", "#059669", "Work",
                "Contracts, reports, decks and client deliverables.",
                "612 files", "6.4 GB", "Updated 1 hour ago");

        VBox financeCard = createSpaceCard("💳", "#FDE68A", "#D97706", "Finance",
                "Invoices, tax filings, statements and receipts.",
                "318 files", "1.9 GB", "Updated Yesterday");

        VBox familyCard = createSpaceCard("💖", "#FBCFE8", "#DB2777", "Friends & Family",
                "Photos, videos, memories and shared moments.",
                "587 files", "9.3 GB", "Updated 3 days ago");

        VBox travelCard = createSpaceCard("✈", "#BFDBFE", "#2563EB", "Travel",
                "Tickets, visas, itineraries and trip albums.",
                "487 files", "9.1 GB", "Updated 5 days ago");

        grid.add(personalCard, 0, 0);
        grid.add(collegeCard, 1, 0);
        grid.add(workCard, 2, 0);
        grid.add(financeCard, 0, 1);
        grid.add(familyCard, 1, 1);
        grid.add(travelCard, 2, 1);

        // Footer Summary Indicator
        Label footerText = new Label("ⓘ  Total 6 spaces  ·  64.2 GB of 100 GB used");
        footerText.setFont(Font.font(FONT, 12));
        footerText.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        // =========================================================
        // SCROLLABLE BODY
        // =========================================================

        VBox contentBody = new VBox(22, pageHeader, grid, footerText);
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
    // HELPER BUILDERS
    // =========================================================

        // OneSpace Logo
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

    private VBox createSpaceCard(String icon, String iconBgHex, String iconTextHex,
                                 String title, String description,
                                 String filesCount, String sizeText, String updatedTime) {

        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(16));
        iconLbl.setStyle("-fx-text-fill: " + iconTextHex + "; -fx-background-color: " + iconBgHex + "; -fx-background-radius: 50%;");
        iconLbl.setPrefSize(38, 38);
        iconLbl.setAlignment(Pos.CENTER);

        Label cardTitle = new Label(title);
        cardTitle.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        cardTitle.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label cardDesc = new Label(description);
        cardDesc.setFont(Font.font(FONT, 12));
        cardDesc.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
        cardDesc.setWrapText(true);
        cardDesc.setMinHeight(36);

        Label filesLbl = new Label(filesCount);
        filesLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        filesLbl.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label sizeLbl = new Label(sizeText);
        sizeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        sizeLbl.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        HBox statsTopRow = new HBox(filesLbl, new Region(), sizeLbl);
        HBox.setHgrow(statsTopRow.getChildren().get(1), Priority.ALWAYS);

        Label updatedLbl = new Label(updatedTime);
        updatedLbl.setFont(Font.font(FONT, 11));
        updatedLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox statsBox = new VBox(2, statsTopRow, updatedLbl);
        statsBox.setPadding(new Insets(10, 0, 0, 0));
        statsBox.setStyle("-fx-border-color: " + BORDER_CARD + "; -fx-border-width: 1 0 0 0;");

        VBox card = new VBox(10, iconLbl, cardTitle, cardDesc, statsBox);
        card.setPadding(new Insets(18));

        String styleIdle = "-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 14; -fx-background-radius: 14; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.14), 12, 0, 0, 4); -fx-cursor: hand;";
        String styleHover = "-fx-background-color: #EBF2FC; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 14; -fx-background-radius: 14; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.22), 16, 0, 0, 6); -fx-cursor: hand;";

        card.setStyle(styleIdle);
        card.setOnMouseEntered(e -> card.setStyle(styleHover));
        card.setOnMouseExited(e -> card.setStyle(styleIdle));
        card.setOnMouseClicked(e -> { LandingPage.showLandingPage(); });

        return card;
    }
}