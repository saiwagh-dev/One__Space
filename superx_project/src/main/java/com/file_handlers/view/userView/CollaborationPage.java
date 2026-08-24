package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;
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
import java.util.ArrayList;
import java.util.List;

public class CollaborationPage {

    private static final String FONT =
            "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";
    private static final String BG_CENTER = "#31435B";
    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_INNER = "#CADDF2";
    private static final String BORDER = "#C3D6EC";
    private static final String TEXT_DARK = "#0F172A";
    private static final String TEXT_MUTED = "#334155";
    private static final String TEXT_WHITE = "#FFFFFF";
    private static final String TEXT_LIGHT = "#94A3B8";
    private static final String BLUE = "#2563EB";

    private final List<WorkspaceData> workspaces = new ArrayList<>();
    private Label spacesValue, membersValue, filesValue;
    private VBox workspaceListPane;
    private boolean isGridView;

    private static class WorkspaceData {
        String icon, iconColor, name, storage, role, badgeBg, badgeText;
        int members, files;

        WorkspaceData(String icon, String color, String name, int members,
                      int files, String storage, String role,
                      String badgeBg, String badgeText) {
            this.icon = icon;
            this.iconColor = color;
            this.name = name;
            this.members = members;
            this.files = files;
            this.storage = storage;
            this.role = role;
            this.badgeBg = badgeBg;
            this.badgeText = badgeText;
        }
    }

    private void initializeWorkspaces() {
        if (!workspaces.isEmpty()) return;

        workspaces.add(new WorkspaceData(
                "🎓", "#0284C7", "College Presentation",
                4, 32, "12.4 GB", "Owner", "#BAE6FD", "#0369A1"));

        workspaces.add(new WorkspaceData(
                "💼", "#059669", "Placement Prep Team",
                3, 84, "18.7 GB", "Editor", "#A7F3D0", "#047857"));

        workspaces.add(new WorkspaceData(
                "📁", BLUE, "AI Project Artifacts",
                5, 32, "6.8 GB", "Editor", "#BFDBFE", "#1D4ED8"));

        workspaces.add(new WorkspaceData(
                "🔬", "#D97706", "Research & Development",
                8, 142, "45.2 GB", "Owner", "#FEF3C7", "#B45309"));

        workspaces.add(new WorkspaceData(
                "🎨", "#7C3AED", "UI/UX Design Systems",
                6, 95, "22.1 GB", "Editor", "#DDD6FE", "#6D28D9"));
    }

    public Scene getCollaborationPageScene() {
        initializeWorkspaces();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG_SIDEBAR + ";");

        root.setLeft(createSidebar());
        root.setCenter(createMainArea(root));

        return new Scene(root, 1200, 750);
    }

    private VBox createSidebar() {
        StackPane logoIcon = createLogo();

        Label logoText = label("OneSpace", 19, FontWeight.BOLD, TEXT_WHITE);
        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = sidebarButton("⌂", "Dashboard", false);
        Button spaces = sidebarButton("📁", "Spaces", false);
        Button search = sidebarButton("⌕", "Search", false);
        Button calendar = sidebarButton("📅", "Calendar", false);
        Button ai = sidebarButton("✧", "AI Assistant", false);
        Button collaboration = sidebarButton("👥", "Collaboration", true);
        Button recent = sidebarButton("🕒", "Recent", false);
        Button trash = sidebarButton("🗑", "Trash", false);
        Button settings = sidebarButton("⚙", "Settings", false);

        dashboard.setOnAction(e -> LandingPage.showUserDashboard());
        spaces.setOnAction(e -> LandingPage.showUserSpace());
        search.setOnAction(e -> LandingPage.showUserSearch());
        calendar.setOnAction(e -> LandingPage.showCalendarPage());
        ai.setOnAction(e -> LandingPage.showLandingPage());
        collaboration.setOnAction(e -> LandingPage.showCollaborationPage());
        recent.setOnAction(e -> LandingPage.showRecentPage());
        trash.setOnAction(e -> LandingPage.showTrashPage());
        settings.setOnAction(e -> LandingPage.showLandingPage());

        VBox nav = new VBox(4,
                dashboard, spaces, search, calendar,
                ai, collaboration, recent, trash);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox sidebar = new VBox(
                12, logoBox, nav, spacer, settings, createStorageCard());

        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle(
                "-fx-background-color:" + BG_SIDEBAR +
                ";-fx-border-color:" + SIDEBAR_BORDER +
                ";-fx-border-width:0 1 0 0;");

        return sidebar;
    }

    private StackPane createLogo() {
        Image image = new Image(
                getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));

        ImageView view = new ImageView(image);
        view.setFitWidth(42);
        view.setFitHeight(42);
        view.setPreserveRatio(true);

        StackPane pane = new StackPane(view);
        pane.setPrefSize(42, 42);
        pane.setAlignment(Pos.CENTER);

        return pane;
    }

    private VBox createStorageCard() {
        Label title = label("Storage Used", 12, FontWeight.SEMI_BOLD, TEXT_WHITE);
        Label value = label("64.2 GB of 100 GB", 12, FontWeight.BOLD, TEXT_WHITE);
        Label percent = label("64%", 11, FontWeight.BOLD, TEXT_LIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox values = new HBox(value, spacer, percent);
        values.setAlignment(Pos.CENTER_LEFT);

        ProgressBar progress = new ProgressBar(.64);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle(
                "-fx-accent:" + BLUE +
                ";-fx-control-inner-background:#0E1520;");

        Button manage = new Button("Manage Storage ›");
        manage.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manage.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#60A5FA;" +
                "-fx-padding:2 0 0 0;" +
                "-fx-cursor:hand;");
        manage.setOnAction(e -> LandingPage.showLandingPage());

        VBox card = new VBox(8, title, values, progress, manage);
        card.setPadding(new Insets(14));
        card.setStyle(
                "-fx-background-color:" + BG_SIDEBAR_CARD +
                ";-fx-border-color:" + SIDEBAR_BORDER +
                ";-fx-border-radius:12;-fx-background-radius:12;");

        return card;
    }

    private Button sidebarButton(String icon, String text, boolean active) {
        Label iconLabel = label(icon, 14, null,
                active ? TEXT_WHITE : TEXT_LIGHT);
        Label textLabel = label(
                text, 13,
                active ? FontWeight.BOLD : FontWeight.MEDIUM,
                TEXT_WHITE);

        HBox content = new HBox(12, iconLabel, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("", content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(38);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        String normal = active
                ? "-fx-background-color:" + BLUE + ";-fx-background-radius:8;-fx-cursor:hand;"
                : "-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;";

        String hover =
                "-fx-background-color:#26354A;" +
                "-fx-background-radius:8;-fx-cursor:hand;";

        button.setStyle(normal);

        if (!active) {
            button.setOnMouseEntered(e -> button.setStyle(hover));
            button.setOnMouseExited(e -> button.setStyle(normal));
        }

        return button;
    }

     private VBox createMainArea(BorderPane root) {
        VBox area = new VBox(createTopBar(), createContent(root));
        area.setStyle("-fx-background-color:" + BG_CENTER + ";");
        VBox.setVgrow(area.getChildren().get(1), Priority.ALWAYS);
        return area;
    }

    private HBox createTopBar() {
        Label searchIcon = label("⌕", 16, null, TEXT_LIGHT);

        TextField search = new TextField();
        search.setPromptText("Search shared workspaces or members...");
        search.setPrefHeight(38);
        search.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-prompt-text-fill:" + TEXT_LIGHT +
                ";-fx-font-size:13px;" +
                "-fx-text-fill:" + TEXT_WHITE + ";");

        Label shortcut = label("⌘ K", 10, FontWeight.SEMI_BOLD, TEXT_LIGHT);
        shortcut.setStyle(
                "-fx-background-color:#141E2C;" +
                "-fx-text-fill:" + TEXT_LIGHT +
                ";-fx-padding:3 6;-fx-background-radius:4;");

        HBox searchBox = new HBox(8, searchIcon, search, shortcut);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 12, 0, 14));
        searchBox.setPrefWidth(420);
        searchBox.setStyle(
                "-fx-background-color:#141E2C;" +
                "-fx-border-color:" + SIDEBAR_BORDER +
                ";-fx-border-radius:10;-fx-background-radius:10;");

        HBox.setHgrow(search, Priority.ALWAYS);

        Button bell = new Button("🔔");
        bell.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-font-size:16px;" +
                "-fx-text-fill:" + TEXT_WHITE +
                ";-fx-cursor:hand;");
        bell.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = label("AV", 12, FontWeight.BOLD, TEXT_WHITE);
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
                "-fx-background-color:" + BLUE +
                ";-fx-background-radius:50%;" +
                "-fx-text-fill:" + TEXT_WHITE + ";");

        Label name = label("Aarav Verma", 13, FontWeight.SEMI_BOLD, TEXT_WHITE);
        Label arrow = label("⌄", 12, null, TEXT_LIGHT);

        HBox profile = new HBox(8, avatar, name, arrow);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(5, 8, 5, 8));

        String profileNormal =
                "-fx-background-color:transparent;" +
                "-fx-background-radius:8;-fx-cursor:hand;";
        String profileHover =
                "-fx-background-color:#26354A;" +
                "-fx-background-radius:8;-fx-cursor:hand;";

        profile.setStyle(profileNormal);
        profile.setOnMouseClicked(e ->
                LandingPage.showUserProfilePage());
        profile.setOnMouseEntered(e -> profile.setStyle(profileHover));
        profile.setOnMouseExited(e -> profile.setStyle(profileNormal));

        HBox right = new HBox(10, bell, profile);
        right.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox top = new HBox(20, searchBox, spacer, right);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(16, 28, 14, 28));
        top.setStyle(
                "-fx-background-color:" + BG_SIDEBAR +
                ";-fx-border-color:" + SIDEBAR_BORDER +
                ";-fx-border-width:0 0 1 0;");

        return top;
    }

    private ScrollPane createContent(BorderPane root) {
        Label title = label(
                "Collaboration", 24, FontWeight.BOLD, TEXT_WHITE);

        Label description = label(
                "Invite team members to shared file workspaces with live access controls.",
                13, FontWeight.MEDIUM, TEXT_LIGHT);

        VBox titleBox = new VBox(4, title, description);

        Button pending = new Button("♧  Pending Invites (3)");
        pending.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        pending.setStyle(buttonStyle(BG_INNER, TEXT_DARK, BORDER, 8));
        pending.setOnAction(e -> showPendingRequestsPopup());

        Button create = new Button("+  New Shared Space");
        create.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        create.setStyle(
                "-fx-background-color:" + BLUE +
                ";-fx-text-fill:#FFFFFF;" +
                "-fx-background-radius:10;" +
                "-fx-cursor:hand;-fx-padding:8 18;");

        HBox actions = new HBox(8, pending, create);
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox header = new HBox(
                titleBox, headerSpacer, actions);
        header.setAlignment(Pos.CENTER_LEFT);

        HBox metrics = createMetrics();

        Label workTitle = label(
                "Shared Workspaces", 17, FontWeight.BOLD, TEXT_DARK);

        Button toggle = new Button("Switch to Grid View");
        toggle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        toggle.setStyle(buttonStyle(BG_INNER, TEXT_DARK, BORDER, 6));

        Region workSpacer = new Region();
        HBox.setHgrow(workSpacer, Priority.ALWAYS);

        HBox workHeader = new HBox(
                workTitle, workSpacer, toggle);
        workHeader.setAlignment(Pos.CENTER_LEFT);

        workspaceListPane = new VBox(10);
        workspaceListPane.setFillWidth(true);

        create.setOnAction(e -> showCreateSharedSpacePopup(root));

        rebuildWorkspaceCards(root);

        Button viewAll = new Button("View all workspaces ›");
        viewAll.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        viewAll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:" + BLUE +
                ";-fx-cursor:hand;-fx-padding:8 0 4 0;");
        viewAll.setOnAction(e -> showAllWorkspacesPopup(root));

        VBox workspaceBox = new VBox(
                14, workHeader, workspaceListPane, viewAll);
        workspaceBox.setPadding(new Insets(24));
        workspaceBox.setStyle(cardStyle());

        toggle.setOnAction(e -> {
            isGridView = !isGridView;
            toggle.setText(isGridView
                    ? "Switch to List View"
                    : "Switch to Grid View");
            rebuildWorkspaceCards(root);
        });

        VBox activityCard = createActivityCard();
        HBox security = createSecurityBox();

        VBox content = new VBox(
                22, header, metrics, workspaceBox, activityCard, security);

        content.setPadding(new Insets(24, 28, 28, 28));
        content.setStyle("-fx-background-color:" + BG_CENTER + ";");

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle(
                "-fx-background-color:" + BG_CENTER +
                ";-fx-background:" + BG_CENTER +
                ";-fx-background-insets:0;-fx-padding:0;");

        return scroll;
    }

    private HBox createMetrics() {
        spacesValue = valueLabel();
        membersValue = valueLabel();
        filesValue = valueLabel();

        HBox s1 = statCard(
                "♧", spacesValue, "Total Shared Workspaces",
                BLUE, "#BFDBFE");

        HBox s2 = statCard(
                "👥", membersValue, "Members Across All Workspaces",
                "#0284C7", "#BAE6FD");

        HBox s3 = statCard(
                "📄", filesValue, "Files Across All Workspaces",
                "#059669", "#A7F3D0");

        HBox.setHgrow(s1, Priority.ALWAYS);
        HBox.setHgrow(s2, Priority.ALWAYS);
        HBox.setHgrow(s3, Priority.ALWAYS);

        updateMetrics();

        return new HBox(14, s1, s2, s3);
    }

    private void updateMetrics() {
        int members = 0, files = 0;

        for (WorkspaceData w : workspaces) {
            members += w.members;
            files += w.files;
        }

        if (spacesValue != null)
            spacesValue.setText(workspaces.size() + " Spaces");

        if (membersValue != null)
            membersValue.setText(members + " Members");

        if (filesValue != null)
            filesValue.setText(files + " Files");
    }

    private HBox statCard(
            String icon, Label value, String description,
            String iconColor, String iconBg) {

        Label iconLabel = label(icon, 14, null, iconColor);
        iconLabel.setPrefSize(34, 34);
        iconLabel.setAlignment(Pos.CENTER);
        iconLabel.setStyle(
                "-fx-text-fill:" + iconColor +
                ";-fx-background-color:" + iconBg +
                ";-fx-background-radius:8;");

        Label desc = label(
                description, 11, FontWeight.SEMI_BOLD, TEXT_MUTED);

        VBox text = new VBox(2, value, desc);

        HBox card = new HBox(12, iconLabel, text);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16));
        card.setStyle(cardStyle());

        return card;
    }

    private Label valueLabel() {
        return label("", 22, FontWeight.BOLD, TEXT_DARK);
    }

    // ========================= WORKSPACES =========================

    private void rebuildWorkspaceCards(BorderPane root) {
        workspaceListPane.getChildren().clear();

        if (!isGridView) {
            VBox list = new VBox(10);

            for (WorkspaceData w : workspaces) {
                HBox card = createWorkspaceCard(w, root);
                card.setOnMouseClicked(e -> openWorkspace(e, w, root));
                list.getChildren().add(card);
            }

            workspaceListPane.getChildren().add(list);
            return;
        }

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);

        grid.getColumnConstraints().addAll(c1, c2);

        int col = 0, row = 0;

        for (WorkspaceData w : workspaces) {
            VBox card = createWorkspaceGridCard(w, root);
            card.setOnMouseClicked(e -> openWorkspace(e, w, root));

            grid.add(card, col, row);

            if (++col > 1) {
                col = 0;
                row++;
            }
        }

        workspaceListPane.getChildren().add(grid);
    }

    private void openWorkspace(
            javafx.event.Event e,
            WorkspaceData workspace,
            BorderPane root) {

        if (e.getTarget() instanceof Button) return;

        root.setCenter(
                new SharedSpacePage(workspace.name)
                        .getSharedSpaceContent());
    }

    private HBox createWorkspaceCard(
            WorkspaceData w, BorderPane root) {

        Label icon = workspaceIcon(w, 16, 40);

        Label title = label(
                w.name, 14, FontWeight.BOLD, TEXT_DARK);

        Label subtitle = label(
                w.members + " Members  ·  " +
                w.files + " Files  ·  " + w.storage,
                11, null, TEXT_MUTED);

        VBox text = new VBox(3, title, subtitle);

        Label role = roleLabel(w);

        Button options = workspaceOptions(w, root);

        HBox right = new HBox(8, role, options);
        right.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox card = new HBox(
                12, icon, text, spacer, right);

        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14));

        applyHover(card);

        return card;
    }

    private VBox createWorkspaceGridCard(
            WorkspaceData w, BorderPane root) {

        Label icon = workspaceIcon(w, 18, 42);
        Label role = roleLabel(w);
        Button options = workspaceOptions(w, root);

        HBox right = new HBox(6, role, options);
        right.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox top = new HBox(icon, spacer, right);
        top.setAlignment(Pos.CENTER);

        Label title = label(
                w.name, 14, FontWeight.BOLD, TEXT_DARK);

        Label subtitle = label(
                w.members + " Members  ·  " +
                w.files + " Files\nStorage: " + w.storage,
                11, null, TEXT_MUTED);

        VBox card = new VBox(10, top, title, subtitle);
        card.setPadding(new Insets(16));
        card.setMaxWidth(Double.MAX_VALUE);

        applyHover(card);

        return card;
    }

    private Label workspaceIcon(
            WorkspaceData w, double size, double boxSize) {

        Label icon = label(
                w.icon, size, null, w.iconColor);

        icon.setPrefSize(boxSize, boxSize);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle(
                "-fx-background-color:" + w.badgeBg +
                ";-fx-background-radius:50%;" +
                "-fx-text-fill:" + w.iconColor + ";");

        return icon;
    }

    private Label roleLabel(WorkspaceData w) {
        Label role = label(
                w.role, 10, FontWeight.BOLD, w.badgeText);

        role.setStyle(
                "-fx-background-color:" + w.badgeBg +
                ";-fx-text-fill:" + w.badgeText +
                ";-fx-padding:4 9;-fx-background-radius:6;");

        return role;
    }

    private Button workspaceOptions(
            WorkspaceData workspace, BorderPane root) {

        Button options = new Button("⋮");
        options.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        options.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:" + TEXT_MUTED +
                ";-fx-cursor:hand;-fx-padding:2 6;");

        ContextMenu menu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete Workspace");

        delete.setStyle(
                "-fx-text-fill:#DC2626;-fx-font-weight:bold;");

        delete.setOnAction(e -> {
            workspaces.remove(workspace);
            rebuildWorkspaceCards(root);
            updateMetrics();
        });

        menu.getItems().add(delete);

        options.setOnAction(e -> {
            e.consume();
            menu.show(
                    options,
                    javafx.geometry.Side.BOTTOM,
                    0, 0);
        });

        return options;
    }

    private void applyHover(Region card) {
        String normal = cardStyle() +
                "-fx-cursor:hand;";

        String hover =
                "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:" + BLUE +
                ";-fx-border-radius:12;" +
                "-fx-background-radius:12;" +
                "-fx-cursor:hand;";

        card.setStyle(normal);
        card.setOnMouseEntered(e -> card.setStyle(hover));
        card.setOnMouseExited(e -> card.setStyle(normal));
    }

    // ========================= ACTIVITY =========================

    private VBox createActivityCard() {
        Label title = label(
                "Recent Activity", 17, FontWeight.BOLD, TEXT_DARK);

        VBox list = new VBox(
                10,
                activity(
                        "Priya Sharma",
                        "uploaded 'SVM_Optimization.pdf'",
                        "10 mins ago"),
                activity(
                        "Rohan Patel",
                        "viewed 'College_Assignments'",
                        "1 hour ago"),
                activity(
                        "Aarav Verma",
                        "updated access permissions for Sneha",
                        "3 hours ago"),
                activity(
                        "System Sync",
                        "indexed 12 new files in Placement Prep",
                        "Yesterday"));

        Button viewAll = new Button("View all activities ›");
        viewAll.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        viewAll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:" + BLUE +
                ";-fx-cursor:hand;");

        viewAll.setOnAction(e -> showAllActivitiesPopup());

        VBox card = new VBox(
                14, title, list, viewAll);

        card.setPadding(new Insets(24));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(cardStyle());

        return card;
    }

    private HBox activity(
            String user, String action, String time) {

        Label dot = label(
                "•", 16, null, BLUE);

        Label userLabel = label(
                user + " ", 12, FontWeight.BOLD, TEXT_DARK);

        Label actionLabel = label(
                action, 12, null, TEXT_DARK);

        HBox text = new HBox(
                userLabel, actionLabel);

        Label timeLabel = label(
                time, 10, null, TEXT_MUTED);

        VBox content = new VBox(
                2, text, timeLabel);

        return new HBox(8, dot, content);
    }

    private HBox createSecurityBox() {
        Label shield = label(
                "🛡", 15, null, BLUE);

        Label bold = label(
                "End-to-End Encrypted Sharing:",
                12, FontWeight.BOLD, TEXT_DARK);

        Label text = label(
                "Files in shared spaces are synced peer-to-peer. " +
                "Original files remain safely stored on your local drive.",
                12, null, TEXT_MUTED);

        HBox textBox = new HBox(6, bold, text);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox security = new HBox(
                10, shield, textBox);

        security.setAlignment(Pos.CENTER_LEFT);
        security.setPadding(new Insets(16, 20, 16, 20));
        security.setStyle(
                "-fx-background-color:" + BG_INNER +
                ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:14;" +
                "-fx-background-radius:14;");

        return security;
    }

   private void showAllWorkspacesPopup(BorderPane root) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("All Shared Workspaces");
        dialog.setHeaderText(
                "Shared Workspaces (" + workspaces.size() + ")");

        VBox list = new VBox(10);
        list.setPadding(new Insets(10));

        for (WorkspaceData w : workspaces) {
            HBox card = createWorkspaceCard(w, root);
            card.setMaxWidth(Double.MAX_VALUE);

            card.setOnMouseClicked(e -> {
                if (e.getTarget() instanceof Button) return;

                dialog.close();

                root.setCenter(
                        new SharedSpacePage(w.name)
                                .getSharedSpaceContent());
            });

            list.getChildren().add(card);
        }

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(430);
        scroll.setPrefWidth(620);
        scroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPannable(true);

        scroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;" +
                "-fx-border-color:transparent;" +
                "-fx-padding:0;");

        addCloseButton(dialog);
        dialog.getDialogPane().setContent(padded(scroll, 5));

        styleDialog(dialog, 660, 520);
        dialog.showAndWait();
    }

    private void showPendingRequestsPopup() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Pending Invites");
        dialog.setHeaderText("Collaboration Invites");

        VBox list = new VBox(
                12,
                pendingRequest(
                        "Priya Sharma",
                        "priya.sharma@gmail.com",
                        "College Presentation",
                        "Invited 10 mins ago"),
                pendingRequest(
                        "Rohan Patel",
                        "rohan.patel@gmail.com",
                        "Placement Prep Team",
                        "Invited 1 hour ago"),
                pendingRequest(
                        "Sneha Kulkarni",
                        "sneha.kulkarni@gmail.com",
                        "AI Project Artifacts",
                        "Invited Yesterday"));

        list.setPadding(new Insets(10));

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(400);
        scroll.setPrefWidth(520);
        scroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;");

        addCloseButton(dialog);
        dialog.getDialogPane().setContent(padded(scroll, 5));

        styleDialog(dialog, 560, 500);
        dialog.showAndWait();
    }

    private HBox pendingRequest(
            String name,
            String email,
            String space,
            String requestedTime) {

        Label avatar = label(
                getInitials(name),
                11, FontWeight.BOLD, BLUE);

        avatar.setPrefSize(38, 38);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
                "-fx-background-color:" + BG_INNER +
                ";-fx-background-radius:50%;" +
                "-fx-text-fill:" + BLUE + ";");

        Label nameLabel = label(
                name, 13, FontWeight.BOLD, TEXT_DARK);

        Label emailLabel = label(
                email, 10, null, TEXT_MUTED);

        Label spaceLabel = label(
                "Invited to: " + space,
                11, null, TEXT_MUTED);

        Label timeLabel = label(
                requestedTime, 10, null, TEXT_MUTED);

        VBox info = new VBox(
                2, nameLabel, emailLabel, spaceLabel, timeLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button accept = new Button("Accept");
        accept.setStyle(
                "-fx-background-color:#059669;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:6;" +
                "-fx-cursor:hand;");

        Button decline = new Button("Decline");
        decline.setStyle(
                "-fx-background-color:" + BG_CARD +
                ";-fx-text-fill:#DC2626;" +
                "-fx-border-color:#FCA5A5;" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;");

        HBox buttons = new HBox(
                6, accept, decline);

        buttons.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(
                10, avatar, info, spacer, buttons);

        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setStyle(
                "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:" + BORDER +
                ";-fx-border-radius:10;" +
                "-fx-background-radius:10;");

        accept.setOnAction(e -> {
            nameLabel.setText(name + " ✓ Accepted");
            nameLabel.setStyle("-fx-text-fill:#059669;");
            accept.setDisable(true);
            decline.setDisable(true);
            spaceLabel.setText("Invite accepted");
            spaceLabel.setStyle("-fx-text-fill:#059669;");
        });

        decline.setOnAction(e -> {
            nameLabel.setText(name + " ✕ Declined");
            nameLabel.setStyle("-fx-text-fill:#DC2626;");
            accept.setDisable(true);
            decline.setDisable(true);
            spaceLabel.setText("Invite declined");
            spaceLabel.setStyle("-fx-text-fill:#DC2626;");
        });

        return row;
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

        for (String[] item : data)
            list.getChildren().add(
                    activity(item[0], item[1], item[2]));

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(430);
        scroll.setPrefWidth(500);
        scroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;");

        addCloseButton(dialog);
        dialog.getDialogPane().setContent(padded(scroll, 5));

        styleDialog(dialog, 540, 520);
        dialog.showAndWait();
    }

    private void showCreateSharedSpacePopup(BorderPane root) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Create New Shared Space");
        dialog.setHeaderText(null);

        Label nameLabel = formLabel("1. Space name");

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Final Year Project");
        nameField.setPrefHeight(42);

        Label membersLabel = formLabel("2. Add members");

        TextField membersField = new TextField();
        membersField.setPromptText(
                "Search members by name or email...");
        membersField.setPrefHeight(42);

        Label uploadLabel = formLabel("3. Upload file");

        Label fileName = label(
                "Choose file or drag and drop",
                12, null, TEXT_MUTED);

        Button browse = new Button("Browse");
        browse.setStyle(
                buttonStyle(BG_INNER, TEXT_DARK, BORDER, 7));

        Region uploadSpacer = new Region();
        HBox.setHgrow(uploadSpacer, Priority.ALWAYS);

        HBox uploadBox = new HBox(
                10, fileName, uploadSpacer, browse);

        uploadBox.setAlignment(Pos.CENTER_LEFT);
        uploadBox.setPadding(new Insets(0, 10, 0, 10));
        uploadBox.setPrefHeight(42);
        uploadBox.setStyle(
                "-fx-background-color:" + BG_INNER +
                ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:7;" +
                "-fx-background-radius:7;");

        final File[] selectedFile = new File[1];

        browse.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose File");

            File file = chooser.showOpenDialog(
                    dialog.getDialogPane()
                            .getScene()
                            .getWindow());

            if (file != null) {
                selectedFile[0] = file;
                fileName.setText(file.getName());
            }
        });

        Label info = label(
                "Upload a file to initialize your shared space.",
                11, null, TEXT_MUTED);

        ButtonType cancel = new ButtonType(
                "Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        ButtonType create = new ButtonType(
                "＋ Create Space", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(cancel, create);

        VBox content = new VBox(
                10,
                nameLabel, nameField,
                membersLabel, membersField,
                uploadLabel, uploadBox, info);

        content.setPadding(new Insets(10));
        content.setPrefWidth(365);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(450);
        dialog.getDialogPane().setStyle(
                "-fx-background-color:" + BG_CARD +
                ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:12;" +
                "-fx-background-radius:12;");

        Button createButton = (Button)
                dialog.getDialogPane().lookupButton(create);

        createButton.setStyle(
                "-fx-background-color:" + BLUE +
                ";-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;");

        Button cancelButton = (Button)
                dialog.getDialogPane().lookupButton(cancel);

        cancelButton.setStyle(
                "-fx-background-color:" + BG_CARD +
                ";-fx-text-fill:" + TEXT_DARK +
                ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;");

        dialog.setResultConverter(button -> {
            if (button == create) {

                String spaceName =
                        nameField.getText().trim();

                if (!spaceName.isEmpty()) {

                    String membersText =
                            membersField.getText().trim();

                    int memberCount = 1;

                    if (!membersText.isEmpty()) {
                        for (String member :
                                membersText.split(",")) {

                            if (!member.trim().isEmpty())
                                memberCount++;
                        }
                    }

                    int fileCount =
                            selectedFile[0] != null ? 1 : 0;

                    workspaces.add(
                            new WorkspaceData(
                                    "📁",
                                    BLUE,
                                    spaceName,
                                    memberCount,
                                    fileCount,
                                    fileCount > 0
                                            ? "Local"
                                            : "No files",
                                    "Owner",
                                    "#BFDBFE",
                                    "#1D4ED8"));

                    rebuildWorkspaceCards(root);
                    updateMetrics();
                }
            }

            return button;
        });

        dialog.showAndWait();
    }

    private Label label(
            String text,
            double size,
            FontWeight weight,
            String color) {

        Label label = new Label(text);
        label.setFont(
                Font.font(FONT, weight, size));
        label.setStyle(
                "-fx-text-fill:" + color + ";");

        return label;
    }

    private Label formLabel(String text) {
        return label(
                text, 13, FontWeight.BOLD, TEXT_DARK);
    }

    private String getInitials(String name) {
        String[] parts = name.trim().split(" ");

        if (parts.length >= 2) {
            return ("" +
                    parts[0].charAt(0) +
                    parts[1].charAt(0)).toUpperCase();
        }

        return name.substring(
                0, Math.min(2, name.length()))
                .toUpperCase();
    }

    private String buttonStyle(
            String background,
            String text,
            String border,
            int radius) {

        return "-fx-background-color:" + background +
                ";-fx-text-fill:" + text +
                ";-fx-border-color:" + border +
                ";-fx-border-radius:" + radius +
                ";-fx-background-radius:" + radius +
                ";-fx-cursor:hand;";
    }

    private String cardStyle() {
        return "-fx-background-color:" + BG_CARD +
                ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:16;" +
                "-fx-background-radius:16;" +
                "-fx-effect:dropshadow(three-pass-box," +
                "rgba(0,0,0,0.18),16,0,0,6);";
    }

    private VBox padded(Node node, double padding) {
        VBox box = new VBox(10, node);
        box.setPadding(new Insets(padding));
        return box;
    }

    private void addCloseButton(Dialog<?> dialog) {
        dialog.getDialogPane()
                .getButtonTypes()
                .add(new ButtonType(
                        "Close",
                        ButtonBar.ButtonData.CANCEL_CLOSE));
    }

    private void styleDialog(
            Dialog<?> dialog,
            double width,
            double height) {

        dialog.getDialogPane().setPrefWidth(width);
        dialog.getDialogPane().setPrefHeight(height);

        dialog.getDialogPane().setStyle(
                "-fx-background-color:" + BG_CARD +
                ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:12;" +
                "-fx-background-radius:12;");
    }
}