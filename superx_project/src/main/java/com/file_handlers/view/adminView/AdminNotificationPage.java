package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

public class AdminNotificationPage {

    // Typography
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar: Deep Sleek Obsidian/Navy Tones
    private static final String SIDEBAR_BG = "#070C16";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";

    // 2. Center Workspace Canvas: Atmospheric Dark Radial Glow
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards & Text Colors
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";

    // 4. Vibrant Typography & Accent Highlights
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";
    private static final String CYAN = "#00D2FF";
    
    private String activeUserName = "Admin";
    private String initials = "A";



    private final List<NotificationItemData> allNotifications = new ArrayList<>();
    private VBox notificationListContainer;
    private String activeFilter = "All";

    public Scene getAdminNotificationPageScene() {
        initData();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createNotificationContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-background-insets: 0; -fx-padding: 0;");

        VBox rightSide = new VBox(createTopBar(), scrollPane);
        rightSide.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        rightSide.setFillWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.setCenter(rightSide);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMaxWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setTextFill(Color.web(WHITE));

        HBox logoRow = new HBox(10, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(4, logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("dashboard", "Dashboard", false);
        dashboardBtn.setOnAction(e -> LandingPage.showAdminDashboard());

        Button usersBtn = createSidebarButton("users", "Users", false);
        usersBtn.setOnAction(e -> LandingPage.showAdminUsers());

        Button filesBtn = createSidebarButton("files", "Files", false);
        filesBtn.setOnAction(e -> LandingPage.showAdminFiles());

        Button collabBtn = createSidebarButton("collaboration", "Collaboration", false);
        collabBtn.setOnAction(e -> LandingPage.showAdminCollaboration());

        Button aiBtn = createSidebarButton("ai", "AI System", false);
        aiBtn.setOnAction(e -> LandingPage.showAdminAISystem());

        Button analyticsBtn = createSidebarButton("analytics", "Analytics", false);
        analyticsBtn.setOnAction(e -> LandingPage.showAnalytics());

        Button securityBtn = createSidebarButton("security", "Security", false);
        securityBtn.setOnAction(e -> LandingPage.showAdminSecurity());

        VBox navigation = new VBox(4, dashboardBtn, usersBtn, filesBtn, collabBtn, aiBtn, analyticsBtn, securityBtn);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button settingsBtn = createSidebarButton("settings", "Settings", false);
        settingsBtn.setOnAction(e -> LandingPage.showAdminSettings());

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + SIDEBAR_BORDER + ";");

        Button logoutBtn = createSidebarButton("logout", "Logout", false);
        logoutBtn.setOnAction(event -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(logoSection, navigation, spacer, settingsBtn, divider, logoutBtn);
        return sidebar;
    }

    private StackPane createLogo() {
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

    private Button createSidebarButton(String type, String text, boolean active) {
        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.web(WHITE));

        HBox row = new HBox(12, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38);
        button.setMinHeight(38);
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
        return button;
    }

    private HBox createTopBar() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notification = new Button();
        notification.setGraphic(bell);
        notification.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        notification.setOnAction(e -> LandingPage.showAdminNotificationPage());

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        Label admin = new Label(activeUserName);
        admin.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        admin.setTextFill(Color.WHITE);

        HBox profile = new HBox(10, avatar, admin);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(4, 12, 4, 6));
        profile.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");

        Popup profilePopup = createProfilePopup();
        profile.setOnMouseClicked(e -> {
            if (profilePopup.isShowing()) {
                profilePopup.hide();
            } else {
                javafx.geometry.Point2D p = profile.localToScreen(0.0, profile.getHeight());
                profilePopup.show(profile, p.getX() - 30, p.getY() + 8);
            }
        });

        HBox topBar = new HBox(16, spacer, notification, profile);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPrefHeight(70);
        topBar.setMinHeight(70);
        topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private Popup createProfilePopup() {
        Popup popup = new Popup();
        popup.setAutoHide(true);

        HBox profileBtn = createProfilePopupItem("users", "Profile Page", "#F59E0B", () -> {
            popup.hide();
            LandingPage.showAdminProfilePage();
        });

        HBox settingsBtn = createProfilePopupItem("settings", "Settings", "#38BDF8", () -> {
            popup.hide();
            LandingPage.showAdminSettings();
        });

        HBox signOutBtn = createProfilePopupItem("logout", "Sign Out", "#F87171", () -> {
            popup.hide();
            LandingPage.showAdminLoginPage();
        });

        Region menuDivider = new Region();
        menuDivider.setPrefHeight(1);
        menuDivider.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08);");

        VBox menuBox = new VBox(6, profileBtn, settingsBtn, menuDivider, signOutBtn);
        menuBox.setPrefWidth(170);
        menuBox.setPadding(new Insets(10, 8, 10, 8));
        menuBox.setStyle(
            "-fx-background-color: #0B132B;" +
            "-fx-border-color: rgba(255, 255, 255, 0.12);" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 14;" +
            "-fx-background-radius: 14;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 24, 0, 0, 10);"
        );

        popup.getContent().add(menuBox);
        return popup;
    }

    private HBox createProfilePopupItem(String iconType, String text, String iconColor, Runnable action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(2.0);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(22, 22);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        label.setTextFill(Color.WHITE);

        HBox item = new HBox(12, iconBox, label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(8, 10, 8, 10));
        item.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        item.setOnMouseClicked(e -> action.run());
        return item;
    }

    private VBox createNotificationContent() {
        Label title = new Label("Notifications");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        title.setTextFill(Color.web(WHITE));

        Label subtitle = new Label("Findings from the last scan and updates from your collaborators.");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox titleBox = new VBox(4, title, subtitle);

        Button markAllReadBtn = new Button("Mark all read");
        markAllReadBtn.setPrefHeight(34);
        markAllReadBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        markAllReadBtn.setTextFill(Color.web(WHITE));
        markAllReadBtn.setPadding(new Insets(0, 16, 0, 16));
        markAllReadBtn.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.06);" +
            "-fx-border-color: " + CARD_BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );

        markAllReadBtn.setOnAction(e -> {
            allNotifications.forEach(n -> n.isRead = true);
            renderList();
        });

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox headerRow = new HBox(titleBox, headerSpacer, markAllReadBtn);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        HBox filterGroup = new HBox(8);
        Button pillAll = createFilterPill("All", true);
        Button pillReminders = createFilterPill("Reminders", false);
        Button pillCollab = createFilterPill("Collaboration", false);

        pillAll.setOnAction(e -> updateFilter("All", pillAll, pillReminders, pillCollab));
        pillReminders.setOnAction(e -> updateFilter("Reminders", pillReminders, pillAll, pillCollab));
        pillCollab.setOnAction(e -> updateFilter("Collaboration", pillCollab, pillAll, pillReminders));

        filterGroup.getChildren().addAll(pillAll, pillReminders, pillCollab);

        notificationListContainer = new VBox(10);
        renderList();

        SVGPath warnIcon = createIcon("alert");
        warnIcon.setStroke(Color.web("#F59E0B"));
        warnIcon.setStrokeWidth(2);

        Label warnText = new Label("OneSpace never deletes or moves files on its own. Every suggested clean-up requires your explicit confirmation.");
        warnText.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        warnText.setTextFill(Color.web(LIGHT_SECONDARY));

        HBox disclaimerBanner = new HBox(10, warnIcon, warnText);
        disclaimerBanner.setAlignment(Pos.CENTER_LEFT);
        disclaimerBanner.setPadding(new Insets(14, 18, 14, 18));
        disclaimerBanner.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-border-color: rgba(245, 158, 11, 0.3);" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;"
        );

        VBox content = new VBox(20, headerRow, filterGroup, notificationListContainer, disclaimerBanner);
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setFillWidth(true);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setStyle("-fx-background-color: transparent;");

        return content;
    }

    private void renderList() {
        notificationListContainer.getChildren().clear();

        for (NotificationItemData data : allNotifications) {
            if (activeFilter.equals("All") || data.type.equalsIgnoreCase(activeFilter)) {
                notificationListContainer.getChildren().add(createNotificationCard(data));
            }
        }
    }

    private HBox createNotificationCard(NotificationItemData data) {
        SVGPath icon = createIcon(data.iconType);
        icon.setStroke(Color.web(data.type.equalsIgnoreCase("Collaboration") ? CYAN : BLUE));
        icon.setStrokeWidth(2.0);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(36, 36);
        iconBox.setStyle("-fx-background-color: rgba(37, 99, 235, 0.15); -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 8; -fx-background-radius: 8;");

        Label titleLbl = new Label(data.title);
        titleLbl.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        titleLbl.setTextFill(Color.web(WHITE));

        Label subLbl = new Label(data.subtitle);
        subLbl.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        subLbl.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox textGroup = new VBox(3, titleLbl, subLbl);

        Label timeLbl = new Label(data.time);
        timeLbl.setFont(Font.font(FONT, FontWeight.MEDIUM, 11));
        timeLbl.setTextFill(Color.web(LIGHT_SECONDARY));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(14, iconBox, textGroup, spacer, timeLbl);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16, 18, 16, 18));

        String idleStyle = "-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 14; -fx-background-radius: 14;";
        String hoverStyle = "-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.9), rgba(12, 22, 40, 0.95)); -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 14; -fx-background-radius: 14; -fx-cursor: hand;";

        row.setStyle(idleStyle);
        row.setOnMouseEntered(e -> row.setStyle(hoverStyle));
        row.setOnMouseExited(e -> row.setStyle(idleStyle));

        return row;
    }

    private void updateFilter(String filter, Button selected, Button... unselected) {
        this.activeFilter = filter;

        selected.setStyle(
            "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
            "-fx-border-color: rgba(96, 165, 250, 0.6);" +
            "-fx-border-radius: 20;" +
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;"
        );

        for (Button btn : unselected) {
            btn.setStyle(
                "-fx-background-color: rgba(13, 22, 38, 0.85);" +
                "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: #94A3B8;" +
                "-fx-font-weight: normal;" +
                "-fx-cursor: hand;"
            );
        }

        renderList();
    }

    private Button createFilterPill(String text, boolean isSelected) {
        Button btn = new Button(text);
        btn.setPrefHeight(32);
        btn.setFont(Font.font(FONT, isSelected ? FontWeight.BOLD : FontWeight.MEDIUM, 12));
        btn.setPadding(new Insets(0, 16, 0, 16));

        if (isSelected) {
            btn.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: white;" +
                "-fx-cursor: hand;"
            );
        } else {
            btn.setStyle(
                "-fx-background-color: rgba(13, 22, 38, 0.85);" +
                "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: #94A3B8;" +
                "-fx-cursor: hand;"
            );
        }

        return btn;
    }

    private void initData() {
        allNotifications.clear();
        allNotifications.add(new NotificationItemData("files", "12 duplicate files detected", "Downloads folder · 4.2 GB recoverable", "1 h", "Reminders"));
        allNotifications.add(new NotificationItemData("security", "Sensitive files found", "Sensitive identity, PAN and passport scans detected", "3 h", "Reminders"));
        allNotifications.add(new NotificationItemData("security", "Passport expires in 12 days", "Linked to Passport_Scan.pdf", "5 h", "Reminders"));
        allNotifications.add(new NotificationItemData("collaboration", "Riya commented on a shared file", "Cloud_Computing_Seminar.pptx", "Yesterday", "Collaboration"));
        allNotifications.add(new NotificationItemData("ai", "AI created 2 new Spaces", "Healthcare and Travel from 609 files", "2 d", "Reminders"));
        allNotifications.add(new NotificationItemData("collaboration", "Priya Sharma uploaded 'SVM_Optimization.pdf'", "Shared in College Presentation Workspace", "2 d", "Collaboration"));
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);
        switch (type) {
            case "dashboard": icon.setContent("M3 3 H10 V10 H3 Z M14 3 H21 V10 H14 Z M3 14 H10 V21 H3 Z M14 14 H21 V21 H14 Z"); break;
            case "users": icon.setContent("M8 11 A3 3 0 1 0 8 5 A3 3 0 0 0 8 11 Z M16 11 A3 3 0 1 0 16 5 A3 3 0 0 0 16 11 Z M2 20 C2 16 5 14 8 14 C11 14 14 16 14 20 M12 15 C14 14 17 14 19 15 C21 16 22 18 22 20"); break;
            case "files": icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13"); break;
            case "collaboration": icon.setContent("M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2 M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M23 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75"); break;
            case "ai": icon.setContent("M12 2 L13.5 8.5 L20 7 L15.5 11.5 L21 15 L14 14.5 L12 22 L10 14.5 L3 15 L8.5 11.5 L4 7 L10.5 8.5 Z"); break;
            case "analytics": icon.setContent("M4 20 V11 M10 20 V6 M16 20 V13 M22 20 V3"); break;
            case "security": icon.setContent("M12 2 L20 5 V11 C20 16 17 20 12 22 C7 20 4 16 4 11 V5 Z M9 12 L11 14 L15 9"); break;
            case "settings": icon.setContent("M12 3 V6 M12 18 V21 M3 12 H6 M18 12 H21 M5.6 5.6 L7.7 7.7 M16.3 16.3 L18.4 18.4 M18.4 5.6 L16.3 7.7 M7.7 16.3 L5.6 18.4 M12 8 A4 4 0 1 0 12 16 A4 4 0 0 0 12 8"); break;
            case "logout": icon.setContent("M10 4 H5 V20 H10 M14 8 L19 12 L14 16 M19 12 H8"); break;
            case "bell": icon.setContent("M6 17 H18 M8 17 V10 A4 4 0 0 1 16 10 V17 M10 20 H14"); break;
            case "alert": icon.setContent("M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z M12 9v4 M12 17h.01"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }

    private static class NotificationItemData {
        String iconType;
        String title;
        String subtitle;
        String time;
        String type;
        @SuppressWarnings("unused")
        boolean isRead = false;

        NotificationItemData(String iconType, String title, String subtitle, String time, String type) {
            this.iconType = iconType;
            this.title = title;
            this.subtitle = subtitle;
            this.time = time;
            this.type = type;
        }
    }
}

