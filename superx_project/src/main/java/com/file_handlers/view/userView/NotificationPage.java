package com.file_handlers.view.userView;

import com.file_handlers.config.FirebaseConfig;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;

import java.util.ArrayList;
import java.util.List;

public class NotificationPage {

    // Typography
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar Tones
    private static final String SIDEBAR_BG = "#070C16";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";

    // 2. Center Workspace Canvas: Atmospheric Dark Radial Glow
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards & Container Colors
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BG_INNER = "linear-gradient(to bottom right, rgba(13, 22, 38, 0.9), rgba(8, 14, 26, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";
    private static final String INPUT_BG = "rgba(13, 22, 38, 0.85)";

    // 4. Vibrant Typography & Accent Highlights
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";

    private final List<N> data = new ArrayList<>();
    private VBox list;
    private String filter = "All";

    public Scene getNotificationsScene() {
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
        init();

        VBox side = createSidebar();

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button bell = new Button();
        bell.setGraphic(bellIcon);
        bell.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        bell.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34); avatar.setMinSize(34, 34); avatar.setMaxSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        Label userName = new Label(activeUserName);
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill: " + WHITE + ";");

        Label dropDown = new Label("⌄");
        dropDown.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        dropDown.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        HBox profileOption = new HBox(8, avatar, userName, dropDown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(4, 12, 4, 6));
        profileOption.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");

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

        profileOption.setOnMouseClicked(e -> {
            if (userDropdownPopup.isShowing()) {
                userDropdownPopup.hide();
            } else {
                javafx.geometry.Point2D point = profileOption.localToScreen(0, profileOption.getHeight() + 6);
                userDropdownPopup.show(profileOption, point.getX(), point.getY());
            }
        });

        HBox profileBox = new HBox(10, bell, profileOption);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(0), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        Label title = text("Notifications", 26, true, WHITE);
        Label sub = text("Stay updated with OneSpace activity, reminders and collaboration.", 13, false, LIGHT_SECONDARY);

        Button mark = new Button("✓   Mark all read");
        mark.setPrefHeight(36);
        mark.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        mark.setTextFill(Color.web("#38BDF8"));
        mark.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 6 14;");
        mark.setOnMouseEntered(e -> mark.setStyle("-fx-background-color: rgba(56, 189, 248, 0.25); -fx-border-color: #38BDF8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 6 14;"));
        mark.setOnMouseExited(e -> mark.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 6 14;"));
        mark.setOnAction(e -> { data.forEach(n -> n.read = true); render(); });

        HBox header = new HBox(new VBox(4, title, sub), space(), mark);
        header.setAlignment(Pos.CENTER_LEFT);

        Button all = filter("All", true);
        Button rem = filter("Reminders", false);
        Button col = filter("Collaboration", false);

        all.setOnAction(e -> setFilter("All", all, rem, col));
        rem.setOnAction(e -> setFilter("Reminders", rem, all, col));
        col.setOnAction(e -> setFilter("Collaboration", col, all, rem));

        HBox filters = new HBox(8, all, rem, col);

        list = new VBox(10);
        render();

        SVGPath warningIcon = createIcon("security");
        warningIcon.setStroke(Color.web("#F59E0B"));
        warningIcon.setStrokeWidth(2);

        Label warning = text("OneSpace never deletes or moves files automatically. Every suggested action requires your confirmation.", 12, false, LIGHT_SECONDARY);

        HBox warningBox = new HBox(10, warningIcon, warning);
        warningBox.setAlignment(Pos.CENTER_LEFT);
        warningBox.setPadding(new Insets(14, 18, 14, 18));
        warningBox.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(245, 158, 11, 0.3); -fx-border-radius: 12; -fx-background-radius: 12;");

        VBox content = new VBox(18, header, filters, list, warningBox);
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color: transparent;");

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent; -fx-padding: 0;");

        VBox center = new VBox(topBar, scroll);
        center.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setLeft(side);
        root.setCenter(center);
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private VBox createSidebar() {
        Image logoImage = new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane logoIcon = new StackPane(logoView);
        logoIcon.setPrefSize(42, 42);
        logoIcon.setAlignment(Pos.CENTER);

        Label logoText = text("OneSpace", 19, true, WHITE);

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = nav("dashboard", "Dashboard", false, e -> LandingPage.showUserDashboard());
        Button spaces = nav("files", "Spaces", false, e -> LandingPage.showUserSpace());
        Button search = nav("search", "Search", false, e -> LandingPage.showUserSearch());
        Button calendar = nav("calendar", "Calendar", false, e -> LandingPage.showCalendarPage());
        Button ai = nav("ai", "AI Assistant", false, e -> LandingPage.showAiAssistantPage());
        Button collab = nav("collaboration", "Collaboration", false, e -> LandingPage.showCollaborationPage());
        Button recent = nav("recent", "Recent", false, e -> LandingPage.showRecentPage());
        Button trash = nav("trash", "Trash", false, e -> LandingPage.showTrashPage());
        Button notifications = nav("bell", "Notifications", true, e -> LandingPage.showNotificationPage());
        Button settings = nav("settings", "Settings", false, e -> LandingPage.showSettingPage());

        VBox navList = new VBox(4, dashboard, spaces, search, calendar, ai, collab, recent, trash, notifications);

        Label storageTitle = text("Storage Used", 12, true, WHITE);
        Label storageVal = text("64.2 GB of 100 GB", 12, true, WHITE);
        Label storagePercent = text("64%", 11, true, LIGHT_SECONDARY);

        HBox storageValGroup = new HBox(storageVal, space(), storagePercent);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: " + BLUE + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        Button manageStorageBtn = new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexPage());

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: rgba(16, 28, 48, 0.65); -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = space();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox side = new VBox(12, logoBox, navList, sidebarSpacer, settings, storageCard);
        side.setPadding(new Insets(20, 14, 20, 14));
        side.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        side.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        side.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        return side;
    }

    private Button nav(String iconType, String name, boolean active, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label textLbl = new Label(name);
        textLbl.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        textLbl.setTextFill(Color.web(WHITE));

        HBox content = new HBox(12, iconBox, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 12, 0, 12));
        btn.setOnAction(action);

        if (active) {
            btn.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);"
            );
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
            btn.setOnMouseEntered(e -> {
                btn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.WHITE);
                textLbl.setTextFill(Color.WHITE);
            });
            btn.setOnMouseExited(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                textLbl.setTextFill(Color.web(WHITE));
            });
        }

        return btn;
    }

    private void render() {
        list.getChildren().clear();

        for (N n : data) {
            if (filter.equals("All") || n.type.equals(filter)) {
                list.getChildren().add(card(n));
            }
        }
    }

    private HBox card(N n) {
        SVGPath icon = createIcon(getNotificationIconType(n.icon));
        icon.setStroke(Color.web("#38BDF8"));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setPrefSize(38, 38); iconPane.setMinSize(38, 38);
        iconPane.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-background-radius: 10; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 10;");

        Label title = text(n.title, 14, true, WHITE);
        Label sub = text(n.sub, 12, false, LIGHT_SECONDARY);

        VBox info = new VBox(3, title, sub);

        Label time = text(n.time, 11, true, LIGHT_SECONDARY);

        Label dot = text(n.read ? "" : "●", 10, true, "#38BDF8");

        HBox row = new HBox(14, iconPane, info, space(), time, dot);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 18, 14, 18));

        String baseStyle = 
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-width: 1.2;" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 12, 0, 0, 4);";

        String hoverStyle = 
                "-fx-background-color: " + CARD_BG_INNER + ";" +
                "-fx-border-color: #38BDF8;" +
                "-fx-border-width: 1.2;" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.22), 16, 0, 0, 6);" +
                "-fx-cursor: hand;";

        row.setStyle(baseStyle);

        row.setOnMouseEntered(e -> row.setStyle(hoverStyle));
        row.setOnMouseExited(e -> row.setStyle(baseStyle));
        row.setOnMouseClicked(e -> { n.read = true; render(); });

        return row;
    }

    private String getNotificationIconType(String rawIcon) {
        if (rawIcon.contains("📄")) return "files";
        if (rawIcon.contains("🛡")) return "security";
        if (rawIcon.contains("📅")) return "calendar";
        if (rawIcon.contains("👥")) return "collaboration";
        if (rawIcon.contains("✦")) return "ai";
        return "bell";
    }

    private void setFilter(String f, Button selected, Button... others) {
        this.filter = f;
        selected.setStyle(pill(true));
        for (Button b : others) b.setStyle(pill(false));
        render();
    }

    private Button filter(String s, boolean active) {
        Button b = new Button(s);
        b.setPrefHeight(34);
        b.setPadding(new Insets(0, 16, 0, 16));
        b.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        b.setTextFill(Color.web(active ? WHITE : LIGHT_SECONDARY));
        b.setStyle(pill(active));
        return b;
    }

    private String pill(boolean active) {
        return "-fx-background-color: " + (active ? "linear-gradient(to right, #1D4ED8, #2563EB)" : INPUT_BG) + ";" +
               "-fx-text-fill: " + (active ? WHITE : LIGHT_SECONDARY) + ";" +
               "-fx-border-color: " + (active ? "rgba(96, 165, 250, 0.6)" : "rgba(255, 255, 255, 0.1)") + ";" +
               "-fx-border-radius: 16;" +
               "-fx-background-radius: 16;" +
               "-fx-cursor: hand;";
    }

    private Label text(String s, double size, boolean bold, String color) {
        Label l = new Label(s);
        l.setFont(Font.font(FONT, bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        l.setStyle("-fx-text-fill: " + color + ";");
        return l;
    }

    private Region space() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
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
            case "security": icon.setContent("M12 2 L20 5 V11 C20 16 17 20 12 22 C7 20 4 16 4 11 V5 Z M9 12 L11 14 L15 9"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }

    private void init() {
        data.clear();
        
        String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";
        if (myEmail == null || myEmail.trim().isEmpty()) {
            return; // Exit if no user is logged in
        }

        try {
            com.google.cloud.firestore.Firestore db = FirebaseConfig.getFirestore();
            var workspacesDocs = db.collection("workspaces").get().get().getDocuments();

            for (var wsDoc : workspacesDocs) {
                String spaceDocId = wsDoc.getId();
                String spaceName = wsDoc.getString("spaceName");
                if (spaceName == null) {
                    spaceName = spaceDocId.replaceAll("_", " ");
                }

                boolean isUserMemberOrOwner = false;
                List<N> workspaceNotifications = new ArrayList<>();

                // Check members subcollection to verify if user belongs to this workspace
                var memberDocs = db.collection("workspaces").document(spaceDocId).collection("members").get().get().getDocuments();
                
                for (var mDoc : memberDocs) {
                    String email = mDoc.getString("email");
                    String status = mDoc.getString("status");
                    String name = mDoc.getString("name");
                    String role = mDoc.getString("role");

                    if (email != null && email.equalsIgnoreCase(myEmail)) {
                        if ("active".equalsIgnoreCase(status) || "Owner".equalsIgnoreCase(role)) {
                            isUserMemberOrOwner = true;
                        }
                        if ("pending".equalsIgnoreCase(status)) {
                            // User has a pending invite to this space
                            workspaceNotifications.add(new N("👥", "Collaboration Invite", "You have been invited to join '" + spaceName + "' as " + (role != null ? role : "Viewer"), "Recent", "Collaboration"));
                            isUserMemberOrOwner = true; // Allow them to see their own invite
                        } else if ("active".equalsIgnoreCase(status)) {
                            workspaceNotifications.add(new N("👥", "Workspace Access Active", "You are an active " + role + " in '" + spaceName + "'", "Synced", "Collaboration"));
                        }
                    } else if (name != null) {
                        // Other team member activity inside a workspace this user belongs to
                        workspaceNotifications.add(new N("👥", name + " joined workspace", "Added to '" + spaceName + "'", "Recent", "Collaboration"));
                    }
                }

                // If the user has no association with this workspace, skip its notifications completely
                if (!isUserMemberOrOwner) {
                    continue;
                }

                // Check files subcollection for recent file uploads within this authorized workspace
                var fileDocs = db.collection("workspaces").document(spaceDocId).collection("files").get().get().getDocuments();
                for (var fDoc : fileDocs) {
                    String fileName = fDoc.getString("fileName");
                    if (fileName != null) {
                        workspaceNotifications.add(new N("📄", "File uploaded in " + spaceName, fileName, "Recent", "Collaboration"));
                    }
                }

                // Add collected notifications for this workspace to the main list
                data.addAll(workspaceNotifications);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.add(new N("📄", "12 duplicate files detected",
                "Downloads folder · 4.2 GB recoverable", "1 h", "Reminders"));

        data.add(new N("🛡", "Sensitive files found",
                "Personal files and scans detected", "3 h", "Reminders"));

        data.add(new N("📅", "Document expires in 12 days",
                "Linked to Document_Scan.pdf", "5 h", "Reminders"));

        data.add(new N("💬", "Riya commented on a shared file",
                "Cloud_Computing_Seminar.pptx", "Yesterday", "Collaboration"));

        data.add(new N("✦", "AI created 2 new Spaces",
                "Healthcare and Travel from 609 files", "2 d", "Reminders"));

        data.add(new N("👥", "Priya Sharma uploaded SVM_Optimization.pdf",
                "Shared in College Presentation Workspace", "2 d", "Collaboration"));
        // Fallback if no workspace notifications exist yet
        if (data.isEmpty()) {
            data.add(new N("🔔", "No new notifications", "Your workspaces are up to date", "Just now", "Reminders"));
        }
    }
    private static class N {
        String icon, title, sub, time, type;
        boolean read = false;

        N(String i, String t, String s, String tm, String ty) {
            icon = i; title = t; sub = s; time = tm; type = ty;
        }
    }
}