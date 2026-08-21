package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CollaborationPage {

    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
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
    public static final String PRIMARY_LIGHT_BLUE = "#3B82F6";

    private static class WorkspaceData {
        String icon, iconColor, name, storage, role, badgeBg, badgeText;
        int members, files;

        WorkspaceData(String icon, String iconColor, String name, int members, int files, String storage, String role, String badgeBg, String badgeText) {
            this.icon = icon;
            this.iconColor = iconColor;
            this.name = name;
            this.members = members;
            this.files = files;
            this.storage = storage;
            this.role = role;
            this.badgeBg = badgeBg;
            this.badgeText = badgeText;
        }
    }

    private final List<WorkspaceData> workspaces = new ArrayList<>();
    private Label spacesValue, membersValue, filesValue;
    private boolean isGridView = false;
    private VBox workspaceListPane;

    private void initializeWorkspaces() {
        if (!workspaces.isEmpty()) return;

        workspaces.add(new WorkspaceData("🎓", "#0284C7", "College Presentation", 4, 32, "12.4 GB", "Owner", "#BAE6FD", "#0369A1"));
        workspaces.add(new WorkspaceData("💼", "#059669", "Placement Prep Team", 3, 84, "18.7 GB", "Editor", "#A7F3D0", "#047857"));
        workspaces.add(new WorkspaceData("📁", PRIMARY_BLUE, "AI Project Artifacts", 5, 32, "6.8 GB", "Editor", "#BFDBFE", "#1D4ED8"));
    }

    public Scene getCollaborationPageScene() {
        initializeWorkspaces();

        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 19px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", true);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);
        Button logoutBtn = createSidebarButton("🚪", "Logout", false);

        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        aiBtn.setOnAction(e -> LandingPage.showLandingPage());
        collabBtn.setOnAction(e -> LandingPage.showCollaborationPage());
        recentBtn.setOnAction(e -> LandingPage.showRecentPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());
        settingsBtn.setOnAction(e -> LandingPage.showLandingPage());
        logoutBtn.setOnAction(e -> LandingPage.showUserLoginPage());

        VBox navList = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        storageTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox storageValueGroup = new HBox(storageVal, new Region(), storagePercent);
        HBox.setHgrow(storageValueGroup.getChildren().get(1), Priority.ALWAYS);
        storageValueGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar storageProgress = new ProgressBar(0.64);
        storageProgress.setMaxWidth(Double.MAX_VALUE);
        storageProgress.setPrefHeight(6);
        storageProgress.setStyle("-fx-accent: " + PRIMARY_BLUE + "; -fx-control-inner-background: #0E1520;");

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showLandingPage());

        VBox storageCard = new VBox(8, storageTitle, storageValueGroup, storageProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, logoutBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        TextField searchField = new TextField();
        searchField.setPromptText("Search shared workspaces or members...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 600; -fx-background-color: #141E2C; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 6; -fx-background-radius: 4;");

        HBox searchContainer = new HBox(8, searchIcon, searchField, keyShortcut);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0, 12, 0, 14));
        searchContainer.setPrefWidth(420);
        searchContainer.setStyle("-fx-background-color: #141E2C; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 50%; -fx-text-fill: " + TEXT_LIGHT + "; -fx-font-weight: bold; -fx-font-size: 12px;");

        Label userName = new Label("Aarav Verma");
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label dropDown = new Label("⌄");
        dropDown.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox profileBox = new HBox(10, bellBtn, avatar, userName, dropDown);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, searchContainer, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        Label pageTitle = new Label("Collaboration");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        pageTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 24px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label pageDescription = new Label("Invite team members to shared file workspaces with live access controls.");
        pageDescription.setFont(Font.font(FONT, 13));
        pageDescription.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        VBox headerTitleBox = new VBox(4, pageTitle, pageDescription);

        Button pendingBtn = new Button("♧  Pending Invites (3)");
        pendingBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        pendingBtn.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-background-color: " + BG_CARD_INNER + "; -fx-text-fill: " + TEXT_DARK + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 12;");
        pendingBtn.setOnAction(e -> showPendingRequestsPopup());

        Button newSpaceButton = new Button("+  New Shared Space");
        newSpaceButton.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        newSpaceButton.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: #FFFFFF; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 8 18;");

        BorderPane root = new BorderPane();
        newSpaceButton.setOnAction(e -> showCreateSharedSpacePopup(root));

        HBox headerActions = new HBox(8, pendingBtn, newSpaceButton);
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox pageHeader = new HBox(headerTitleBox, headerSpacer, headerActions);
        pageHeader.setAlignment(Pos.CENTER_LEFT);

        spacesValue = createValueLabel();
        membersValue = createValueLabel();
        filesValue = createValueLabel();

        HBox stat1 = createStatCard("♧", spacesValue, "Total Shared Workspaces", PRIMARY_BLUE, "#BFDBFE");
        HBox stat2 = createStatCard("👥", membersValue, "Members Across All Workspaces", "#0284C7", "#BAE6FD");
        HBox stat3 = createStatCard("📄", filesValue, "Files Across All Workspaces", "#059669", "#A7F3D0");

        HBox metrics = new HBox(14, stat1, stat2, stat3);
        HBox.setHgrow(stat1, Priority.ALWAYS);
        HBox.setHgrow(stat2, Priority.ALWAYS);
        HBox.setHgrow(stat3, Priority.ALWAYS);

        updateMetrics();

        Label workspaceTitle = new Label("Shared Workspaces");
        workspaceTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        workspaceTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 17px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Button toggleViewBtn = new Button("Switch to Grid View");
        toggleViewBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        toggleViewBtn.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: " + BG_CARD_INNER + "; -fx-text-fill: " + TEXT_DARK + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 10;");
        toggleViewBtn.setOnAction(e -> {
            isGridView = !isGridView;
            toggleViewBtn.setText(isGridView ? "Switch to List View" : "Switch to Grid View");
            rebuildWorkspaceCards(root);
        });

        Region wsHeaderSpacer = new Region();
        HBox.setHgrow(wsHeaderSpacer, Priority.ALWAYS);

        HBox workspaceHeaderBox = new HBox(workspaceTitle, wsHeaderSpacer, toggleViewBtn);
        workspaceHeaderBox.setAlignment(Pos.CENTER_LEFT);

        workspaceListPane = new VBox(10);
        workspaceListPane.setFillWidth(true);
        rebuildWorkspaceCards(root);

        Button viewAllWorkspaces = new Button("View all workspaces ›");
        viewAllWorkspaces.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        viewAllWorkspaces.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-background-color: transparent; -fx-text-fill: " + PRIMARY_BLUE + "; -fx-cursor: hand; -fx-padding: 8 0 4 0;");
        viewAllWorkspaces.setOnAction(e -> showAllWorkspacesPopup(root));

        VBox workspacesBox = new VBox(14, workspaceHeaderBox, workspaceListPane, viewAllWorkspaces);
        workspacesBox.setPadding(new Insets(24));
        workspacesBox.setStyle(cardContainerStyle());

        Label activityTitle = new Label("Recent Activity");
        activityTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        activityTitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 17px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        VBox activityList = new VBox(10,
            activity("Priya Sharma", "uploaded 'SVM_Optimization.pdf'", "10 mins ago"),
            activity("Rohan Patel", "viewed 'College_Assignments'", "1 hour ago"),
            activity("Aarav Verma", "updated access permissions for Sneha", "3 hours ago"),
            activity("System Sync", "indexed 12 new files in Placement Prep", "Yesterday")
        );

        Button viewAllActivities = new Button("View all activities ›");
        viewAllActivities.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        viewAllActivities.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-background-color: transparent; -fx-text-fill: " + PRIMARY_BLUE + "; -fx-cursor: hand;");
        viewAllActivities.setOnAction(e -> showAllActivitiesPopup());

        VBox activityCard = new VBox(14, activityTitle, activityList, viewAllActivities);
        activityCard.setPadding(new Insets(24));
        activityCard.setMaxWidth(Double.MAX_VALUE);
        activityCard.setStyle(cardContainerStyle());

        Label shield = new Label("🛡");
        shield.setFont(Font.font(FONT, 15));
        shield.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 15px; -fx-text-fill: " + PRIMARY_BLUE + ";");

        Label securityBold = new Label("End-to-End Encrypted Sharing:");
        securityBold.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        securityBold.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label securityText = new Label("Files in shared spaces are synced peer-to-peer. Original files remain safely stored on your local drive.");
        securityText.setFont(Font.font(FONT, 12));
        securityText.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        HBox securityTextBox = new HBox(6, securityBold, securityText);
        securityTextBox.setAlignment(Pos.CENTER_LEFT);

        HBox security = new HBox(10, shield, securityTextBox);
        security.setAlignment(Pos.CENTER_LEFT);
        security.setPadding(new Insets(16, 20, 16, 20));
        security.setStyle("-fx-background-color: " + BG_CARD_INNER + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 14; -fx-background-radius: 14;");

        VBox mainContent = new VBox(22, pageHeader, metrics, workspacesBox, activityCard, security);
        mainContent.setPadding(new Insets(24, 28, 28, 28));
        mainContent.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + "; -fx-background: " + BG_CENTER_CANVAS + "; -fx-background-insets: 0; -fx-padding: 0;");

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.setStyle("-fx-background-color: " + BG_SIDEBAR + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1200, 750);
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

    private Label createValueLabel() {
        Label label = new Label();
        label.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");
        return label;
    }

    private void updateMetrics() {
        int totalMembers = 0, totalFiles = 0;
        for (WorkspaceData workspace : workspaces) {
            totalMembers += workspace.members;
            totalFiles += workspace.files;
        }
        if (spacesValue != null) spacesValue.setText(workspaces.size() + " Spaces");
        if (membersValue != null) membersValue.setText(totalMembers + " Members");
        if (filesValue != null) filesValue.setText(totalFiles + " Files");
    }

    private void rebuildWorkspaceCards(BorderPane root) {
        workspaceListPane.getChildren().clear();

        if (!isGridView) {
            VBox listViewBox = new VBox(10);
            listViewBox.setFillWidth(true);
            for (WorkspaceData workspace : workspaces) {
                HBox card = createWorkspaceCard(workspace);
                card.setOnMouseClicked(e -> root.setCenter(new SharedSpacePage(workspace.name).getSharedSpaceContent()));
                listViewBox.getChildren().add(card);
            }
            workspaceListPane.getChildren().add(listViewBox);
        } else {
            GridPane gridPane = new GridPane();
            gridPane.setHgap(12);
            gridPane.setVgap(12);
            
            int col = 0;
            int row = 0;
            for (WorkspaceData workspace : workspaces) {
                VBox gridCard = createWorkspaceGridCard(workspace);
                gridCard.setOnMouseClicked(e -> root.setCenter(new SharedSpacePage(workspace.name).getSharedSpaceContent()));
                gridPane.add(gridCard, col, row);
                col++;
                if (col > 1) { 
                    col = 0;
                    row++;
                }
            }
            workspaceListPane.getChildren().add(gridPane);
        }
    }

    private HBox createWorkspaceCard(WorkspaceData workspace) {
        Label icon = new Label(workspace.icon);
        icon.setFont(Font.font(FONT, 16));
        icon.setPrefSize(40, 40);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-background-color: " + workspace.badgeBg + "; -fx-background-radius: 50%; -fx-text-fill: " + workspace.iconColor + ";");

        Label title = new Label(workspace.name);
        title.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        title.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label subtitle = new Label(workspace.members + " Members  ·  " + workspace.files + " Files  ·  " + workspace.storage);
        subtitle.setFont(Font.font(FONT, 11));
        subtitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox text = new VBox(3, title, subtitle);

        Label role = new Label(workspace.role);
        role.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        role.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 700; -fx-background-color: " + workspace.badgeBg + "; -fx-text-fill: " + workspace.badgeText + "; -fx-padding: 4 9; -fx-background-radius: 6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox card = new HBox(12, icon, text, spacer, role);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14));

        String normalStyle = "-fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;";

        card.setStyle(normalStyle);
        card.setOnMouseEntered(e -> card.setStyle(hoverStyle));
        card.setOnMouseExited(e -> card.setStyle(normalStyle));

        return card;
    }

    private VBox createWorkspaceGridCard(WorkspaceData workspace) {
        Label icon = new Label(workspace.icon);
        icon.setFont(Font.font(FONT, 18));
        icon.setPrefSize(42, 42);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 18px; -fx-background-color: " + workspace.badgeBg + "; -fx-background-radius: 50%; -fx-text-fill: " + workspace.iconColor + ";");

        Label role = new Label(workspace.role);
        role.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        role.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 700; -fx-background-color: " + workspace.badgeBg + "; -fx-text-fill: " + workspace.badgeText + "; -fx-padding: 3 8; -fx-background-radius: 6;");

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        HBox topRow = new HBox(icon, topSpacer, role);
        topRow.setAlignment(Pos.CENTER);

        Label title = new Label(workspace.name);
        title.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        title.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label subtitle = new Label(workspace.members + " Members  ·  " + workspace.files + " Files\nStorage: " + workspace.storage);
        subtitle.setFont(Font.font(FONT, 11));
        subtitle.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox card = new VBox(10, topRow, title, subtitle);
        card.setPadding(new Insets(16));
        card.setPrefWidth(280);
        card.setMaxWidth(Double.MAX_VALUE);

        String normalStyle = "-fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;";

        card.setStyle(normalStyle);
        card.setOnMouseEntered(e -> card.setStyle(hoverStyle));
        card.setOnMouseExited(e -> card.setStyle(normalStyle));

        return card;
    }

    private void showAllWorkspacesPopup(BorderPane root) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("All Shared Workspaces");
        dialog.setHeaderText("Shared Workspaces (" + workspaces.size() + ")");

        VBox list = new VBox(10);
        list.setPadding(new Insets(10));

        for (WorkspaceData workspace : workspaces) {
            HBox card = createWorkspaceCard(workspace);
            card.setMaxWidth(Double.MAX_VALUE);
            card.setOnMouseClicked(e -> {
                dialog.close();
                root.setCenter(new SharedSpacePage(workspace.name).getSharedSpaceContent());
            });
            list.getChildren().add(card);
        }

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(430);
        scroll.setPrefWidth(620);
        scroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(close);

        VBox content = new VBox(10, scroll);
        content.setPadding(new Insets(5));

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(660);
        dialog.getDialogPane().setPrefHeight(520);
        dialog.getDialogPane().setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        dialog.showAndWait();
    }

    private void showPendingRequestsPopup() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Pending Invites");
        dialog.setHeaderText("Collaboration Invites");

        VBox requestList = new VBox(12,
            pendingRequest("Priya Sharma", "priya.sharma@gmail.com", "College Presentation", "Invited 10 mins ago"),
            pendingRequest("Rohan Patel", "rohan.patel@gmail.com", "Placement Prep Team", "Invited 1 hour ago"),
            pendingRequest("Sneha Kulkarni", "sneha.kulkarni@gmail.com", "AI Project Artifacts", "Invited Yesterday")
        );
        requestList.setPadding(new Insets(10));

        ScrollPane scroll = new ScrollPane(requestList);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(400);
        scroll.setPrefWidth(520);
        scroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(close);

        VBox content = new VBox(10, scroll);
        content.setPadding(new Insets(5));

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(560);
        dialog.getDialogPane().setPrefHeight(500);
        dialog.getDialogPane().setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        dialog.showAndWait();
    }

    private HBox pendingRequest(String name, String email, String space, String requestedTime) {
        Label avatar = new Label(getInitials(name));
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        avatar.setPrefSize(38, 38);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-background-color: " + BG_CARD_INNER + "; -fx-background-radius: 50%; -fx-text-fill: " + PRIMARY_BLUE + ";");

        Label nameLbl = new Label(name);
        nameLbl.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        nameLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label emailLbl = new Label(email);
        emailLbl.setFont(Font.font(FONT, 10));
        emailLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        Label spaceLbl = new Label("Invited to: " + space);
        spaceLbl.setFont(Font.font(FONT, 11));
        spaceLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        Label timeLbl = new Label(requestedTime);
        timeLbl.setFont(Font.font(FONT, 10));
        timeLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox info = new VBox(2, nameLbl, emailLbl, spaceLbl, timeLbl);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button accept = new Button("Accept");
        accept.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: #059669; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");

        Button decline = new Button("Decline");
        decline.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: " + BG_CARD + "; -fx-text-fill: #DC2626; -fx-border-color: #FCA5A5; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold; -fx-cursor: hand;");

        HBox buttons = new HBox(6, accept, decline);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(10, avatar, info, spacer, buttons);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 10; -fx-background-radius: 10;");

        accept.setOnAction(e -> {
            nameLbl.setText(name + " ✓ Accepted");
            nameLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #059669;");
            accept.setDisable(true);
            decline.setDisable(true);
            spaceLbl.setText("Invite accepted");
            spaceLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: #059669;");
        });

        decline.setOnAction(e -> {
            nameLbl.setText(name + " ✕ Declined");
            nameLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #DC2626;");
            accept.setDisable(true);
            decline.setDisable(true);
            spaceLbl.setText("Invite declined");
            spaceLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: #DC2626;");
        });

        return row;
    }

    private String getInitials(String name) {
        String[] parts = name.trim().split(" ");
        if (parts.length >= 2) return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    private Button createSidebarButton(String icon, String text, boolean isActive) {
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(FONT, 14));

        Label textLbl = new Label(text);
        textLbl.setFont(Font.font(FONT, isActive ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        textLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: " + (isActive ? "700" : "500") + "; -fx-text-fill: " + TEXT_LIGHT + ";");

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
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #26354A; -fx-background-radius: 8; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;"));
        }

        return btn;
    }

    private HBox createStatCard(String icon, Label valueLabel, String description, String iconColor, String iconBackground) {
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(FONT, 14));
        iconLbl.setPrefSize(34, 34);
        iconLbl.setAlignment(Pos.CENTER);
        iconLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 14px; -fx-text-fill: " + iconColor + "; -fx-background-color: " + iconBackground + "; -fx-background-radius: 8;");

        Label descriptionLbl = new Label(description);
        descriptionLbl.setFont(Font.font(FONT, 11));
        descriptionLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + "; -fx-font-weight: 600;");

        VBox text = new VBox(2, valueLabel, descriptionLbl);

        HBox card = new HBox(12, iconLbl, text);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16));
        card.setStyle(cardContainerStyle());
        HBox.setHgrow(card, Priority.ALWAYS);

        return card;
    }

    private HBox activity(String user, String action, String time) {
        Label dot = new Label("•");
        dot.setFont(Font.font(FONT, 16));
        dot.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-text-fill: " + PRIMARY_BLUE + ";");

        Label userLbl = new Label(user + " ");
        userLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        userLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";"); 

        Label actionLbl = new Label(action);
        actionLbl.setFont(Font.font(FONT, 12));
        actionLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_DARK + ";"); 

        HBox text = new HBox(userLbl, actionLbl);

        Label timeLbl = new Label(time);
        timeLbl.setFont(Font.font(FONT, 10));
        timeLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox content = new VBox(2, text, timeLbl);

        return new HBox(8, dot, content);
    }

    private void showAllActivitiesPopup() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("All Activities");
        dialog.setHeaderText("Recent Activity");

        String[][] data = {
            {"Priya Sharma", "uploaded 'SVM_Optimization.pdf'", "10 mins ago"},
            {"Rohan Patel", "viewed 'College_Assignments'", "1 hour ago"},
            {"Aarav Verma", "updated access permissions for Sneha", "3 hours ago"},
            {"System Sync", "indexed 12 new files in Placement Prep", "Yesterday"},
            {"Sneha Kulkarni", "joined 'College Presentation'", "Yesterday"},
            {"Rahul Joshi", "uploaded 'Project_Report.docx'", "Yesterday"},
            {"Priya Sharma", "edited 'SVM_Optimization.pdf'", "2 days ago"},
            {"Rohan Patel", "downloaded 'College_Assignments'", "2 days ago"},
            {"Aarav Verma", "created 'AI Project Artifacts'", "3 days ago"},
            {"Sneha Kulkarni", "updated workspace description", "3 days ago"},
            {"System Sync", "indexed 8 new files in College Presentation", "4 days ago"},
            {"Rahul Joshi", "joined 'Placement Prep Team'", "5 days ago"},
            {"Priya Sharma", "shared 'Placement_Notes.pdf'", "5 days ago"},
            {"Aarav Verma", "changed Rahul's role to Editor", "6 days ago"},
            {"Rohan Patel", "viewed 'Placement_Notes.pdf'", "1 week ago"}
        };

        VBox list = new VBox(12);
        list.setPadding(new Insets(10));

        for (String[] item : data) list.getChildren().add(activity(item[0], item[1], item[2]));

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(430);
        scroll.setPrefWidth(500);
        scroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(close);

        VBox content = new VBox(10, scroll);
        content.setPadding(new Insets(5));

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(540);
        dialog.getDialogPane().setPrefHeight(520);
        dialog.getDialogPane().setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        dialog.showAndWait();
    }

    private void showCreateSharedSpacePopup(BorderPane root) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create New Shared Space");
        dialog.setHeaderText(null);

        Label nameLabel = new Label("1. Space name");
        nameLabel.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        nameLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Final Year Project");
        nameField.setPrefHeight(42);
        nameField.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px;");

        Label membersLabel = new Label("2. Add members");
        membersLabel.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        membersLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        TextField membersField = new TextField();
        membersField.setPromptText("Search members by name or email...");
        membersField.setPrefHeight(42);
        membersField.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px;");

        Label uploadLabel = new Label("3. Upload file");
        uploadLabel.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        uploadLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label fileName = new Label("Choose file or drag and drop");
        fileName.setFont(Font.font(FONT, 12));
        fileName.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        Button browse = new Button("Browse");
        browse.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: " + BG_CARD_INNER + "; -fx-text-fill: " + TEXT_DARK + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand;");

        Region uploadSpacer = new Region();
        HBox.setHgrow(uploadSpacer, Priority.ALWAYS);

        HBox uploadBox = new HBox(10, fileName, uploadSpacer, browse);
        uploadBox.setAlignment(Pos.CENTER_LEFT);
        uploadBox.setPadding(new Insets(0, 10, 0, 10));
        uploadBox.setPrefHeight(42);
        uploadBox.setStyle("-fx-background-color: " + BG_CARD_INNER + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 7; -fx-background-radius: 7;");

        final File[] selectedFile = new File[1];

        browse.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose File");
            File file = chooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (file != null) {
                selectedFile[0] = file;
                fileName.setText(file.getName());
            }
        });

        Label info = new Label("Upload a file to initialize your shared space.");
        info.setFont(Font.font(FONT, 11));
        info.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED_DARK + ";");

        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType create = new ButtonType("＋ Create Space", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(cancel, create);

        VBox content = new VBox(10, nameLabel, nameField, membersLabel, membersField, uploadLabel, uploadBox, info);
        content.setPadding(new Insets(10));
        content.setPrefWidth(365);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(450);
        dialog.getDialogPane().setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Button createBtn = (Button) dialog.getDialogPane().lookupButton(create);
        createBtn.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7; -fx-cursor: hand;");

        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(cancel);
        cancelBtn.setStyle("-fx-font-family: " + FONT + "; -fx-background-color: " + BG_CARD + "; -fx-text-fill: " + TEXT_DARK + "; -fx-border-color: " + BORDER_CARD + "; -fx-border-radius: 7; -fx-background-radius: 7; -fx-font-weight: bold; -fx-cursor: hand;");

        dialog.setResultConverter(button -> {
            if (button == create) {
                String spaceName = nameField.getText().trim();

                if (!spaceName.isEmpty()) {
                    String membersText = membersField.getText().trim();
                    int memberCount = 1;

                    if (!membersText.isEmpty()) {
                        for (String member : membersText.split(",")) {
                            if (!member.trim().isEmpty()) memberCount++;
                        }
                    }

                    int fileCount = selectedFile[0] != null ? 1 : 0;

                    workspaces.add(new WorkspaceData(
                        "📁", PRIMARY_BLUE, spaceName, memberCount, fileCount,
                        fileCount > 0 ? "Local" : "No files", "Owner", "#BFDBFE", "#1D4ED8"
                    ));

                    rebuildWorkspaceCards(root);
                    updateMetrics();
                }
            }
            return button;
        });

        dialog.showAndWait();
    }

    private String cardContainerStyle() {
        return "-fx-background-color: " + BG_CARD + "; " +
               "-fx-border-color: " + BORDER_CARD + "; " +
               "-fx-border-radius: 16; " +
               "-fx-background-radius: 16; " +
               "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 16, 0, 0, 6);";
    }
}