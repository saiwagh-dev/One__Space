package com.file_handlers.view.userView;

import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class UserSpaces {

    // Style Constants - Exact Theme Hierarchy
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar: Deep Sleek Obsidian/Navy Tones
    private static final String BG_SIDEBAR = "#070C16";
    private static final String BG_SIDEBAR_CARD = "linear-gradient(to bottom right, rgba(14, 24, 43, 0.95), rgba(8, 14, 26, 0.95))";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";

    // 2. Center Workspace Canvas: Atmospheric Dark Radial Glow
    private static final String BG_CENTER_CANVAS = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards with Crisp High-Contrast Highlights
    private static final String BG_CARD = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String BORDER_CARD = "rgba(56, 189, 248, 0.22)";

    // 4. Contrast Typography
    private static final String TEXT_DARK = "#FFFFFF";        // Pure White text for dark surfaces
    private static final String TEXT_MUTED_DARK = "#94A3B8";  // Slate subtext
    private static final String TEXT_LIGHT = "#FFFFFF";       // White text for dark surfaces
    private static final String TEXT_MUTED_LIGHT = "#94A3B8"; // Subtext for dark surfaces

    // Accent Colors
    private static final String PRIMARY_BLUE = "#2563EB";

    public Scene getUserSpacesScene() {
        String activeUserName = "User";
        String initials = "U";

        if (UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null) {
            String fullName = UserSession.getInstance().getDisplayName().trim();
            if (!fullName.isEmpty()) {
                // Extract only the first name (everything before the first space)
                String[] parts = fullName.split("\\s+");
                activeUserName = parts[0];

                // Grab the initial from the first name
                initials = activeUserName.substring(0, 1).toUpperCase();
            }
        }

        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + TEXT_LIGHT + "; -fx-font-smoothing-type: lcd;");

        HBox logoHeader = new HBox(12, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(6, 0, 18, 6));

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
        Button logoutBtn = createSidebarButton("🚪", "Logout", false);

        dashboardBtn.setOnAction(e -> { LandingPage.showUserDashboard(); });
        spacesBtn.setOnAction(e -> { LandingPage.showUserSpace(); });
        searchBtn.setOnAction(e -> { LandingPage.showUserSearch(); });
        calendarBtn.setOnAction(e -> { LandingPage.showCalendarPage(); });
        aiBtn.setOnAction(e -> { LandingPage.showAiAssistantPage(); });
        collabBtn.setOnAction(e -> { LandingPage.showCollaborationPage(); });
        recentBtn.setOnAction(e -> { LandingPage.showRecentPage(); });
        trashBtn.setOnAction(e -> { LandingPage.showTrashPage(); });
        settingsBtn.setOnAction(e -> { LandingPage.showSettingPage(); });
        logoutBtn.setOnAction(e -> { LandingPage.showUserLoginPage(); });

        VBox navList = new VBox(5, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

        // Sidebar Storage Card
        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        storageTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_LIGHT + "; -fx-font-smoothing-type: lcd;");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + "; -fx-font-smoothing-type: lcd;");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-smoothing-type: lcd;");

        HBox storageValGroup = new HBox(storageVal, new Region(), storagePercent);
        HBox.setHgrow(storageValGroup.getChildren().get(1), Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle(
                "-fx-accent: linear-gradient(to right, #0284C7, #38BDF8);" +
                "-fx-control-inner-background: #0B1526;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 0;"
        );

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: transparent; -fx-text-fill: #38BDF8; -fx-padding: 3 0 0 0; -fx-cursor: hand; -fx-font-smoothing-type: lcd;");
        manageStorageBtn.setOnAction(e -> { LandingPage.showStorageIndexedPage(); });

        VBox storageCard = new VBox(9, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle(
                "-fx-background-color: " + BG_SIDEBAR_CARD + ";" +
                "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 4);"
        );

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        // Sidebar layout placing logout directly below settings
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
        searchIcon.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-text-fill: #64748B; -fx-font-smoothing-type: lcd;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: transparent; -fx-prompt-text-fill: #64748B; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-font-smoothing-type: lcd;");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 600; -fx-background-color: rgba(255, 255, 255, 0.06); -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 7; -fx-background-radius: 5; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 5; -fx-font-smoothing-type: lcd;");

        HBox searchContainer = new HBox(10, searchIcon, searchField, keyShortcut);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0, 12, 0, 14));
        searchContainer.setPrefWidth(520);
        searchContainer.setStyle(
                "-fx-background-color: rgba(13, 22, 38, 0.85);" +
                "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;"
        );
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-size: 14px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand; -fx-padding: 6 10;");
        bellBtn.setOnAction(event -> { LandingPage.showNotificationPage(); });

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF);" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);" +
                "-fx-font-smoothing-type: lcd;"
        );

        Label userName = new Label(activeUserName);
        userName.setFont(
                Font.font(FONT, FontWeight.SEMI_BOLD, 13)
        );
        userName.setStyle(
                "-fx-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-font-smoothing-type: lcd;"
        );

        Label dropDown = new Label("⌄");
        dropDown.setStyle(
                "-fx-text-fill: " + TEXT_MUTED_LIGHT + ";" +
                "-fx-font-smoothing-type: lcd;"
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
                new Insets(4, 12, 4, 6)
        );

        profileOption.setStyle(
                "-fx-background-color: rgba(13, 22, 38, 0.85);" +
                "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
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
                    "-fx-background-color: rgba(23, 37, 64, 0.95);" +
                    "-fx-border-color: rgba(56, 189, 248, 0.4);" +
                    "-fx-border-radius: 20;" +
                    "-fx-background-radius: 20;" +
                    "-fx-cursor: hand;"
            );
        });

        profileOption.setOnMouseExited(e -> {
            profileOption.setStyle(
                    "-fx-background-color: rgba(13, 22, 38, 0.85);" +
                    "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                    "-fx-border-radius: 20;" +
                    "-fx-background-radius: 20;" +
                    "-fx-cursor: hand;"
            );
        });

        // =========================================================
        // TOP RIGHT
        // =========================================================

        HBox profileBox =
                new HBox(
                        12,
                        bellBtn,
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
                        searchContainer,
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
                "-fx-background-color: transparent;" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 0 1 0;"
        );

        // =========================================================
        // PAGE HEADER
        // =========================================================

        Label pageTitle = new Label("Spaces");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        pageTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + "; -fx-font-smoothing-type: lcd;");

        Label pageDescription = new Label("Virtual groupings built by AI. Files remain in their original folders.");
        pageDescription.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        pageDescription.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-smoothing-type: lcd;");

        VBox headerTitleBox = new VBox(4, pageTitle, pageDescription);

        Button newSpaceButton = new Button("+  New Space");
        newSpaceButton.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        newSpaceButton.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #0284C7);" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 22;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(2, 132, 199, 0.6), 16, 0, 0, 3);" +
                "-fx-font-smoothing-type: lcd;"
        );
        newSpaceButton.setOnAction(e -> openNewSpaceWindow());

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

        VBox personalCard = createSpaceCard("👤", "rgba(124, 58, 237, 0.2)", "#C084FC", "Personal",
                "IDs, certificates, personal photos and everyday documents.",
                "1,284 files", "8.2 GB", "Updated 2 minutes ago");

        VBox collegeCard = createSpaceCard("🎓", "rgba(2, 132, 199, 0.2)", "#38BDF8", "College",
                "Notes, assignments, lab records, presentations and projects.",
                "946 files", "12.7 GB", "Updated 18 minutes ago");

        VBox workCard = createSpaceCard("💼", "rgba(5, 150, 105, 0.2)", "#34D399", "Work",
                "Contracts, reports, decks and client deliverables.",
                "612 files", "6.4 GB", "Updated 1 hour ago");

        VBox financeCard = createSpaceCard("💳", "rgba(217, 119, 6, 0.2)", "#FBBF24", "Finance",
                "Invoices, tax filings, statements and receipts.",
                "318 files", "1.9 GB", "Updated Yesterday");

        VBox familyCard = createSpaceCard("💖", "rgba(219, 39, 119, 0.2)", "#F472B6", "Friends & Family",
                "Photos, videos, memories and shared moments.",
                "587 files", "9.3 GB", "Updated 3 days ago");

        VBox travelCard = createSpaceCard("✈", "rgba(37, 99, 235, 0.2)", "#60A5FA", "Travel",
                "Tickets, visas, itineraries and trip albums.",
                "487 files", "9.1 GB", "Updated 5 days ago");

        grid.add(personalCard, 0, 0);
        grid.add(collegeCard, 1, 0);
        grid.add(workCard, 2, 0);
        grid.add(financeCard, 0, 1);
        grid.add(familyCard, 1, 1);
        grid.add(travelCard, 2, 1);

        Label footerText = new Label("ⓘ  Total 6 spaces  ·  64.2 GB of 100 GB used");
        footerText.setFont(Font.font(FONT, FontWeight.MEDIUM, 12));
        footerText.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-smoothing-type: lcd;");

        // =========================================================
        // SCROLLABLE BODY (RIGID FOCUS PROTECTION)
        // =========================================================

        VBox contentBody = new VBox(22, pageHeader, grid, footerText);
        contentBody.setPadding(new Insets(24, 28, 28, 28));
        contentBody.setFocusTraversable(false);
        contentBody.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-font-smoothing-type: lcd;"
        );

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setFocusTraversable(false);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;" +
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;"
        );

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setFocusTraversable(false);
        mainArea.setStyle(
                "-fx-background: " + BG_CENTER_CANVAS + "; " +
                "-fx-background-color: " + BG_CENTER_CANVAS + ";" +
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;"
        );
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
        iconLbl.setStyle("-fx-font-smoothing-type: lcd;");

        Label textLbl = new Label(label);
        textLbl.setFont(Font.font(FONT, isActive ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        textLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: " + (isActive ? "700" : "500") + "; -fx-text-fill: " + (isActive ? "#FFFFFF" : TEXT_MUTED_LIGHT) + "; -fx-font-smoothing-type: lcd;");

        HBox content = new HBox(12, iconLbl, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(40);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 14, 0, 14));

        if (isActive) {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); " +
                    "-fx-background-radius: 12; " +
                    "-fx-border-color: rgba(96, 165, 250, 0.6); " +
                    "-fx-border-radius: 12; " +
                    "-fx-border-width: 1; " +
                    "-fx-cursor: hand; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);"
            );
            iconLbl.setStyle("-fx-text-fill: #FFFFFF; -fx-font-smoothing-type: lcd;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-smoothing-type: lcd;");

            btn.setOnMouseEntered(e -> {
                btn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 12; -fx-cursor: hand;");
                iconLbl.setStyle("-fx-text-fill: #FFFFFF; -fx-font-smoothing-type: lcd;");
                textLbl.setStyle("-fx-text-fill: #FFFFFF; -fx-font-smoothing-type: lcd;");
            });
            btn.setOnMouseExited(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand;");
                iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-smoothing-type: lcd;");
                textLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-smoothing-type: lcd;");
            });
        }

        return btn;
    }

    private VBox createSpaceCard(String icon, String iconBgHex, String iconTextHex,
                                 String title, String description,
                                 String filesCount, String sizeText, String updatedTime) {

        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(16));
        iconLbl.setStyle(
                "-fx-text-fill: " + iconTextHex + ";" +
                "-fx-background-color: " + iconBgHex + ";" +
                "-fx-background-radius: 50%;" +
                "-fx-border-color: " + iconTextHex + "55;" +
                "-fx-border-radius: 50%;" +
                "-fx-font-smoothing-type: lcd;"
        );
        iconLbl.setPrefSize(38, 38);
        iconLbl.setAlignment(Pos.CENTER);

        Label cardTitle = new Label(title);
        cardTitle.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        cardTitle.setStyle("-fx-text-fill: " + TEXT_DARK + "; -fx-font-smoothing-type: lcd;");

        Label cardDesc = new Label(description);
        cardDesc.setFont(Font.font(FONT, 12));
        cardDesc.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-smoothing-type: lcd;");
        cardDesc.setWrapText(true);
        cardDesc.setMinHeight(36);

        Label filesLbl = new Label(filesCount);
        filesLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        filesLbl.setStyle("-fx-text-fill: " + TEXT_DARK + "; -fx-font-smoothing-type: lcd;");

        Label sizeLbl = new Label(sizeText);
        sizeLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        sizeLbl.setStyle("-fx-text-fill: " + iconTextHex + "; -fx-font-smoothing-type: lcd;");

        HBox statsTopRow = new HBox(filesLbl, new Region(), sizeLbl);
        HBox.setHgrow(statsTopRow.getChildren().get(1), Priority.ALWAYS);

        Label updatedLbl = new Label(updatedTime);
        updatedLbl.setFont(Font.font(FONT, 11));
        updatedLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-smoothing-type: lcd;");

        VBox statsBox = new VBox(4, statsTopRow, updatedLbl);
        statsBox.setPadding(new Insets(10, 0, 0, 0));
        statsBox.setStyle("-fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-width: 1 0 0 0;");

        VBox card = new VBox(12, iconLbl, cardTitle, cardDesc, statsBox);
        card.setPadding(new Insets(20));

        String styleIdle = "-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 18; -fx-background-radius: 18; -fx-border-width: 1.2; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 18, 0, 0, 6); -fx-cursor: hand;";
        String styleHover = "-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.9), rgba(12, 22, 40, 0.95)); -fx-border-color: " + iconTextHex + "; -fx-border-radius: 18; -fx-background-radius: 18; -fx-border-width: 1.2; -fx-effect: dropshadow(three-pass-box, " + iconTextHex + "66, 22, 0, 0, 8); -fx-cursor: hand;";

        card.setStyle(styleIdle);
        card.setOnMouseEntered(e -> card.setStyle(styleHover));
        card.setOnMouseExited(e -> card.setStyle(styleIdle));

        card.setOnMouseClicked(e -> { 
            LandingPage.showUnifiedSpaceView(); 
        });

        return card;
    }

    // =========================================================
    // PREMIUM DESIGNED, LARGE NEW SPACE CREATION WINDOW
    // =========================================================
    private void openNewSpaceWindow() {
        Stage newSpaceStage = new Stage();
        newSpaceStage.initModality(Modality.APPLICATION_MODAL);
        newSpaceStage.setTitle("Create New Space");

        Label windowTitle = new Label("Create New Space");
        windowTitle.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        windowTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + "; -fx-font-smoothing-type: lcd;");

        Label windowSub = new Label("Group your important documents securely into an intelligent virtual folder.");
        windowSub.setFont(Font.font(FONT, 13));
        windowSub.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-smoothing-type: lcd;");

        VBox titleContainer = new VBox(4, windowTitle, windowSub);

        Label nameLabel = new Label("SPACE NAME");
        nameLabel.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        nameLabel.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-letter-spacing: 1px; -fx-font-smoothing-type: lcd;");

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Tax Records 2026, Thesis Project...");
        nameField.setPrefHeight(42);
        nameField.setStyle(
                "-fx-background-color: rgba(13, 22, 38, 0.9); " +
                "-fx-text-fill: " + TEXT_LIGHT + "; " +
                "-fx-prompt-text-fill: #64748B; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: rgba(255, 255, 255, 0.08); " +
                "-fx-border-radius: 10; " +
                "-fx-padding: 0 14; " +
                "-fx-font-smoothing-type: lcd;"
        );

        Label descLabel = new Label("DESCRIPTION");
        descLabel.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        descLabel.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-letter-spacing: 1px; -fx-font-smoothing-type: lcd;");

        TextField descField = new TextField();
        descField.setPromptText("What kind of files will live in this space?");
        descField.setPrefHeight(42);
        descField.setStyle(
                "-fx-background-color: rgba(13, 22, 38, 0.9); " +
                "-fx-text-fill: " + TEXT_LIGHT + "; " +
                "-fx-prompt-text-fill: #64748B; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: rgba(255, 255, 255, 0.08); " +
                "-fx-border-radius: 10; " +
                "-fx-padding: 0 14; " +
                "-fx-font-smoothing-type: lcd;"
        );

        Label filesLabel = new Label("ATTACHED FILES");
        filesLabel.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        filesLabel.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-letter-spacing: 1px; -fx-font-smoothing-type: lcd;");

        ObservableList<File> uploadedFiles = FXCollections.observableArrayList();
        ListView<File> fileListView = new ListView<>(uploadedFiles);
        fileListView.setPrefHeight(160);
        fileListView.setStyle(
                "-fx-background-color: rgba(13, 22, 38, 0.9); " +
                "-fx-control-inner-background: rgba(13, 22, 38, 0.9); " +
                "-fx-border-color: rgba(255, 255, 255, 0.08); " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10;"
        );

        fileListView.setCellFactory(param -> new ListCell<File>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText("📄  " + item.getName() + "  (" + (item.length() / 1024) + " KB)");
                    setStyle("-fx-background-color: transparent; -fx-text-fill: #E2E8F0; -fx-font-size: 13px; -fx-padding: 6 10; -fx-font-smoothing-type: lcd;");
                }
            }
        });

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add Files to Space");

        Button uploadBtn = new Button("＋ Add Files");
        uploadBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        uploadBtn.setStyle(
                "-fx-background-color: rgba(56, 189, 248, 0.15); " +
                "-fx-border-color: rgba(56, 189, 248, 0.3); " +
                "-fx-text-fill: #38BDF8; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 16; " +
                "-fx-font-smoothing-type: lcd;"
        );
        uploadBtn.setOnMouseEntered(e -> uploadBtn.setStyle("-fx-background-color: rgba(56, 189, 248, 0.25); -fx-border-color: #38BDF8; -fx-text-fill: #FFFFFF; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16; -fx-font-smoothing-type: lcd;"));
        uploadBtn.setOnMouseExited(e -> uploadBtn.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-border-color: rgba(56, 189, 248, 0.3); -fx-text-fill: #38BDF8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16; -fx-font-smoothing-type: lcd;"));

        uploadBtn.setOnAction(e -> {
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(newSpaceStage);
            if (selectedFiles != null) {
                uploadedFiles.addAll(selectedFiles);
            }
        });

        Button removeBtn = new Button("✕ Remove File");
        removeBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        removeBtn.setStyle(
                "-fx-background-color: rgba(239, 68, 68, 0.15); " +
                "-fx-border-color: rgba(239, 68, 68, 0.3); " +
                "-fx-text-fill: #F87171; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 16; " +
                "-fx-font-smoothing-type: lcd;"
        );
        removeBtn.setOnMouseEntered(e -> removeBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.25); -fx-border-color: #F87171; -fx-text-fill: #FCA5A5; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16; -fx-font-smoothing-type: lcd;"));
        removeBtn.setOnMouseExited(e -> removeBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.15); -fx-border-color: rgba(239, 68, 68, 0.3); -fx-text-fill: #F87171; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16; -fx-font-smoothing-type: lcd;"));

        removeBtn.setOnAction(e -> {
            File selectedFile = fileListView.getSelectionModel().getSelectedItem();
            if (selectedFile != null) {
                uploadedFiles.remove(selectedFile);
            }
        });

        HBox fileActionBox = new HBox(10, uploadBtn, removeBtn);
        fileActionBox.setAlignment(Pos.CENTER_LEFT);

        VBox fileSection = new VBox(8, filesLabel, fileListView, fileActionBox);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        cancelBtn.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: " + TEXT_MUTED_LIGHT + "; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 10 18; " +
                "-fx-font-smoothing-type: lcd;"
        );
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-text-fill: #FFFFFF; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 18; -fx-font-smoothing-type: lcd;"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-cursor: hand; -fx-padding: 10 18; -fx-font-smoothing-type: lcd;"));
        cancelBtn.setOnAction(event -> newSpaceStage.close());

        Button createBtn = new Button("Create Space");
        createBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        createBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #0284C7); " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: rgba(96, 165, 250, 0.6); " +
                "-fx-border-radius: 10; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 10 24; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(2, 132, 199, 0.6), 14, 0, 0, 3); " +
                "-fx-font-smoothing-type: lcd;"
        );
        createBtn.setOnMouseEntered(e -> createBtn.setStyle("-fx-background-color: linear-gradient(to right, #2563EB, #38BDF8); -fx-text-fill: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #38BDF8; -fx-border-radius: 10; -fx-cursor: hand; -fx-padding: 10 24; -fx-effect: dropshadow(three-pass-box, rgba(56, 189, 248, 0.7), 16, 0, 0, 4); -fx-font-smoothing-type: lcd;"));
        createBtn.setOnMouseExited(e -> createBtn.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #0284C7); -fx-text-fill: #FFFFFF; -fx-background-radius: 10; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 10; -fx-cursor: hand; -fx-padding: 10 24; -fx-effect: dropshadow(three-pass-box, rgba(2, 132, 199, 0.6), 14, 0, 0, 3); -fx-font-smoothing-type: lcd;"));

        createBtn.setOnAction(event -> {
            System.out.println("New Space Created: " + nameField.getText() + " with " + uploadedFiles.size() + " files.");
            newSpaceStage.close();
        });

        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);

        HBox buttonBox = new HBox(12, cancelBtn, footerSpacer, createBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        VBox layout = new VBox(16, titleContainer, nameLabel, nameField, descLabel, descField, fileSection, buttonBox);
        layout.setPadding(new Insets(30));
        layout.setStyle(
                "-fx-background-color: #0B1322;" +
                "-fx-border-color: rgba(56, 189, 248, 0.2);" +
                "-fx-border-width: 1.5;"
        );

        Scene scene = new Scene(layout, 560, 640);
        newSpaceStage.setScene(scene);
        newSpaceStage.showAndWait();
    }
}