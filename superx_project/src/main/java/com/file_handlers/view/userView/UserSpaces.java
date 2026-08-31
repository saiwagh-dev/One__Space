package com.file_handlers.view.userView;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.model.FileData;
import com.file_handlers.model.UserSession;
import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.view.LandingPage;
import com.google.cloud.Timestamp;

import javafx.application.Platform;
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
import javafx.stage.Popup;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSpaces {

    // Typography
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar Tones
    private static final String SIDEBAR_BG = "#070C16";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";

    // 2. Center Workspace Canvas: Atmospheric Dark Radial Glow
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards & Text Colors
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BG_HOVER = "linear-gradient(to bottom right, rgba(23, 40, 68, 0.9), rgba(12, 22, 40, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";
    private static final String CARD_TITLE = "#FFFFFF";
    private static final String CARD_SECONDARY = "#94A3B8";

    // 4. Vibrant Typography & Accent Highlights
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";

    private final FileDAO fileDAO = new FileDAO();

    private final List<SpaceInfo> spaces = List.of(
        new SpaceInfo("Personal", "personal", "IDs, certificates, personal photos and everyday documents.", "👤", "rgba(124, 58, 237, 0.2)", "#A78BFA"),
        new SpaceInfo("College", "college", "Notes, assignments, lab records, presentations and projects.", "🎓", "rgba(2, 132, 199, 0.2)", "#38BDF8"),
        new SpaceInfo("Office", "office", "Contracts, reports, decks and client deliverables.", "💼", "rgba(5, 150, 105, 0.2)", "#34D399"),
        new SpaceInfo("Finance", "finance", "Invoices, tax filings, statements and receipts.", "💳", "rgba(217, 119, 6, 0.2)", "#FBBF24"),
        new SpaceInfo("Entertainment", "entertainment", "Photos, videos, movies, music and other entertainment files.", "💖", "rgba(219, 39, 119, 0.2)", "#F472B6"),
        new SpaceInfo("Others", "other", "Files that do not clearly belong to another space.", "📁", "rgba(37, 99, 235, 0.2)", "#60A5FA")
    );

    public Scene getUserSpacesScene() {
        UserSession session = UserSession.getInstance();
        String user = "User", initials = "U";

        if (session != null && session.getDisplayName() != null && !session.getDisplayName().trim().isEmpty()) {
            String fullName = session.getDisplayName().trim();
            String[] parts = fullName.split("\\s+");
            user = parts[0];
            initials = user.substring(0, 1).toUpperCase();
        }

        VBox sidebar = createSidebar();

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button notification = new Button();
        notification.setGraphic(bellIcon);
        notification.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        notification.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34); avatar.setMinSize(34, 34); avatar.setMaxSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        Label userLabel = label(user, 13, FontWeight.SEMI_BOLD, WHITE);
        Label dropDown = label("⌄", 12, FontWeight.NORMAL, LIGHT_SECONDARY);

        HBox profile = new HBox(8, avatar, userLabel, dropDown);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(4, 12, 4, 6));
        profile.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");

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

        profile.setOnMouseClicked(e -> {
            if (userDropdownPopup.isShowing()) {
                userDropdownPopup.hide();
            } else {
                javafx.geometry.Point2D point = profile.localToScreen(0, profile.getHeight() + 6);
                userDropdownPopup.show(profile, point.getX(), point.getY());
            }
        });

        HBox profileBox = new HBox(10, notification, profile);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(0), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        Label title = label("Spaces", 26, FontWeight.BOLD, WHITE);
        Label description = label("Virtual groupings built by AI. Files remain in their original folders.", 13, FontWeight.MEDIUM, LIGHT_SECONDARY);
        VBox titleBox = new VBox(4, title, description);

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(33.33);
            grid.getColumnConstraints().add(c);
        }

        Map<String, SpaceCardView> cards = new HashMap<>();

        for (int i = 0; i < spaces.size(); i++) {
            SpaceInfo info = spaces.get(i);
            SpaceCardView card = createSpaceCard(info);
            cards.put(info.spaceId, card);
            grid.add(card.card, i % 3, i / 3);
        }

        Label footer = label("ⓘ Loading spaces...", 12, FontWeight.NORMAL, LIGHT_SECONDARY);

        VBox content = new VBox(22, titleBox, grid, footer);
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color: transparent;");

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-background-insets: 0; -fx-padding: 0;");

        VBox center = new VBox(topBar, scroll);
        center.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(center);
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");

        loadSpaceStatistics(cards, footer);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private VBox createSidebar() {
        Image image = new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
        ImageView view = new ImageView(image);
        view.setFitWidth(42);
        view.setFitHeight(42);
        view.setPreserveRatio(true);

        StackPane logoIcon = new StackPane(view);
        logoIcon.setPrefSize(42, 42);
        logoIcon.setAlignment(Pos.CENTER);

        Label logoText = label("OneSpace", 19, FontWeight.BOLD, WHITE);
        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = side("dashboard", "Dashboard", false, e -> LandingPage.showUserDashboard());
        Button spacesBtn = side("files", "Spaces", true, e -> LandingPage.showUserSpace());
        Button search = side("search", "Search", false, e -> LandingPage.showUserSearch());
        Button calendar = side("calendar", "Calendar", false, e -> LandingPage.showCalendarPage());
        Button ai = side("ai", "AI Assistant", false, e -> LandingPage.showAiAssistantPage());
        Button collab = side("collaboration", "Collaboration", false, e -> LandingPage.showCollaborationPage());
        Button recent = side("recent", "Recent", false, e -> LandingPage.showRecentPage());
        Button trash = side("trash", "Trash", false, e -> LandingPage.showTrashPage());
        Button settings = side("settings", "Settings", false, e -> LandingPage.showSettingPage());

        VBox navList = new VBox(4, dashboard, spacesBtn, search, calendar, ai, collab, recent, trash);

        Label storageTitle = label("Storage Used", 12, FontWeight.BOLD, WHITE);
        Label storageVal = label("64.2 GB of 100 GB", 12, FontWeight.BOLD, WHITE);
        Label storagePercent = label("64%", 11, FontWeight.BOLD, LIGHT_SECONDARY);

        Region storageSpacer = new Region();
        HBox.setHgrow(storageSpacer, Priority.ALWAYS);

        HBox storageValGroup = new HBox(storageVal, storageSpacer, storagePercent);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar storageBar = new ProgressBar(0.64);
        storageBar.setMaxWidth(Double.MAX_VALUE);
        storageBar.setPrefHeight(6);
        storageBar.setStyle("-fx-accent: " + BLUE + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        Button manageStorageBtn = new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexPage());

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, storageBar, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: rgba(16, 28, 48, 0.65); -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settings, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        return sidebar;
    }

    private Button side(String iconType, String text, boolean active, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label label = label(text, 13, active ? FontWeight.BOLD : FontWeight.MEDIUM, WHITE);

        HBox row = new HBox(12, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        if (active) {
            button.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);"
            );
        } else {
            button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.WHITE);
                label.setTextFill(Color.WHITE);
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                label.setTextFill(Color.web(WHITE));
            });
        }

        button.setOnAction(action);
        return button;
    }

    private SpaceCardView createSpaceCard(SpaceInfo info) {
        Label icon = label(info.icon, 16, FontWeight.NORMAL, info.iconTextColor);
        icon.setPrefSize(38, 38);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color: " + info.iconBackground + "; -fx-background-radius: 50%; -fx-text-fill: " + info.iconTextColor + ";");

        Label title = label(info.name, 16, FontWeight.BOLD, CARD_TITLE);

        Label description = label(info.description, 12, FontWeight.NORMAL, CARD_SECONDARY);
        description.setWrapText(true);
        description.setMinHeight(36);

        Label files = label("0 files", 12, FontWeight.BOLD, CARD_TITLE);
        Label size = label("—", 12, FontWeight.BOLD, CARD_TITLE);
        Label updated = label("No files yet", 11, FontWeight.NORMAL, CARD_SECONDARY);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox statsRow = new HBox(files, spacer, size);

        VBox stats = new VBox(2, statsRow, updated);
        stats.setPadding(new Insets(10, 0, 0, 0));
        stats.setStyle("-fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-width: 1 0 0 0;");

        VBox card = new VBox(10, icon, title, description, stats);
        card.setPadding(new Insets(18));
        card.setFocusTraversable(false);
        card.setCache(true);

        String styleIdle = "-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);";
        String styleHover = "-fx-background-color: " + CARD_BG_HOVER + "; -fx-border-color: " + info.iconTextColor + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, " + info.iconTextColor + "66, 20, 0, 0, 6);";

        card.setStyle(styleIdle);
        card.setOnMouseEntered(e -> card.setStyle(styleHover));
        card.setOnMouseExited(e -> card.setStyle(styleIdle));
        card.setOnMouseClicked(e -> LandingPage.showUnifiedSpace(info.spaceId, info.name));

        return new SpaceCardView(card, files, size, updated);
    }

    private void loadSpaceStatistics(
            Map<String, SpaceCardView> cards,
            Label footer) {

        UserSession session = UserSession.getInstance();

        if (session == null || !UserSession.isLoggedIn() ||
                session.getUid() == null ||
                session.getUid().isBlank()) {

            footer.setText("ⓘ No authenticated user");
            return;
        }

        Thread thread = new Thread(() -> {
            try {
                List<FileData> files =
                        fileDAO.getFileSummaries(
                                session.getUid()
                        );

                Map<String, SpaceStats> stats =
                        new HashMap<>();

                long totalSize = 0;

                for (FileData file : files) {

                    String spaceId = file.getSpaceId();

                    if (spaceId == null || spaceId.isBlank())
                        continue;

                    SpaceStats stat =
                            stats.computeIfAbsent(
                                    spaceId,
                                    key -> new SpaceStats()
                            );

                    stat.fileCount++;
                    stat.totalSize += file.getFileSize();
                    totalSize += file.getFileSize();

                    Timestamp uploadedAt =
                            file.getUploadedAt();

                    Instant time =
                            uploadedAt == null
                                    ? null
                                    : uploadedAt
                                            .toDate()
                                            .toInstant();

                    if (time != null && (
                            stat.latestUpdate == null ||
                            time.isAfter(stat.latestUpdate))) {

                        stat.latestUpdate = time;
                    }
                }

                final long finalTotalSize = totalSize;

                Platform.runLater(() -> {

                    for (SpaceInfo info : spaces) {

                        SpaceStats stat =
                                stats.getOrDefault(
                                        info.spaceId,
                                        new SpaceStats()
                                );

                        SpaceCardView card =
                                cards.get(info.spaceId);

                        if (card != null) {

                            card.setStats(
                                    stat.fileCount,
                                    formatUpdated(
                                            stat.latestUpdate
                                    ),
                                    formatSize(
                                            stat.totalSize
                                    )
                            );
                        }
                    }

                    footer.setText(
                            "ⓘ " +
                            files.size() +
                            " files  ·  " +
                            formatSize(finalTotalSize) +
                            " used"
                    );
                });

            } catch (Exception e) {

                Platform.runLater(() -> {
                    footer.setText(
                            "ⓘ Unable to load space statistics"
                    );
                });
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private String formatUpdated(Instant time) {
        if (time == null)
            return "No files yet";

        long minutes = Math.max(
                0,
                Duration.between(
                        time,
                        Instant.now()
                ).toMinutes()
        );

        if (minutes < 1)
            return "Updated just now";

        if (minutes < 60)
            return "Updated " + minutes + " min ago";

        long hours = minutes / 60;

        if (hours < 24)
            return "Updated " + hours + " hr ago";

        long days = hours / 24;

        if (days == 1)
            return "Updated yesterday";

        return "Updated " + days + " days ago";
    }

    private String formatSize(long bytes) {
        if (bytes <= 0)
            return "—";
        if (bytes < 1024)
            return bytes + " B";
        if (bytes < 1048576)
            return String.format(
                    "%.1f KB",
                    bytes / 1024.0
            );
        if (bytes < 1073741824L)
            return String.format(
                    "%.1f MB",
                    bytes / 1048576.0
            );
        return String.format(
                "%.1f GB",
                bytes / 1073741824.0
        );
    }

    private Label label(
            String text,
            double size,
            FontWeight weight,
            String color) {

        Label label = new Label(text);
        label.setFont(
                Font.font(
                        FONT,
                        weight,
                        size
                )
        );
        label.setStyle(
                "-fx-text-fill:" + color + ";"
        );
        return label;
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

    private static class SpaceInfo {
        final String name, spaceId, description, icon, iconBackground, iconTextColor;

        SpaceInfo(
                String name,
                String spaceId,
                String description,
                String icon,
                String iconBackground,
                String iconTextColor) {

            this.name = name;
            this.spaceId = spaceId;
            this.description = description;
            this.icon = icon;
            this.iconBackground = iconBackground;
            this.iconTextColor = iconTextColor;
        }
    }

    private static class SpaceStats {
        int fileCount;
        long totalSize;
        Instant latestUpdate;
    }

    private static class SpaceCardView {
        final VBox card;
        final Label filesLabel, sizeLabel, updatedLabel;

        SpaceCardView(
                VBox card,
                Label filesLabel,
                Label sizeLabel,
                Label updatedLabel) {

            this.card = card;
            this.filesLabel = filesLabel;
            this.sizeLabel = sizeLabel;
            this.updatedLabel = updatedLabel;
        }

        void setStats(
                int count,
                String updated,
                String size) {

            filesLabel.setText(
                    count + " files"
            );

            sizeLabel.setText(size);
            updatedLabel.setText(updated);
        }
    }
}