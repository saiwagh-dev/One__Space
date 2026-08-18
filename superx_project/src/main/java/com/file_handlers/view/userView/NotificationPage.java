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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class NotificationPage {

    // Style Constants
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // Design System Color Palette
    private static final String APP = "#31435B";
    private static final String NAV = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String CARD = "#DDE8F8";
    private static final String INNER = "#CADDF2";
    private static final String INPUT = "#141E2C";
    private static final String BORDER = "#C3D6EC";
    private static final String SIDEBAR_BORDER = "#2D3D52";
    private static final String BLUE = "#2563EB";
    private static final String LIGHT_BLUE = "#DBEAFE";
    private static final String DARK = "#0F172A";
    private static final String MUTED = "#334155";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT = "#94A3B8";

    private final List<N> data = new ArrayList<>();
    private VBox list;
    private String filter = "All";

    public Scene getNotificationsScene() {
        init();

        // =========================================================
        // SIDEBAR
        // =========================================================

        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = text("OneSpace", 19, true, WHITE);
        logoText.setStyle("-fx-text-fill: " + WHITE + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = nav("⌂", "Dashboard", false);
        Button spaces = nav("📁", "Spaces", false);
        Button search = nav("⌕", "Search", false);
        Button calendar = nav("📅", "Calendar", false);
        Button ai = nav("✧", "AI Assistant", false);
        Button collab = nav("👥", "Collaboration", false);
        Button recent = nav("🕒", "Recent", false);
        Button trash = nav("🗑", "Trash", false);
        Button notifications = nav("🔔", "Notifications", true);
        Button settings = nav("⚙", "Settings", false);
        Button logoutBtn = nav("🚪", "Logout", false);


        dashboard.setOnAction(e -> LandingPage.showUserDashboard());
        spaces.setOnAction(e -> LandingPage.showUserSpace());
        search.setOnAction(e -> LandingPage.showUserSearch());
        calendar.setOnAction(e -> LandingPage.showCalendarPage());
        ai.setOnAction(e -> LandingPage.showLandingPage());
        collab.setOnAction(e -> LandingPage.showCollaborationPage());
        recent.setOnAction(e -> LandingPage.showRecentPage());
        trash.setOnAction(e -> LandingPage.showTrashPage());
        notifications.setOnAction(e -> LandingPage.showNotificationPage());
        settings.setOnAction(e -> LandingPage.showLandingPage());
        logoutBtn.setOnAction(e -> LandingPage.showUserLoginPage());

        VBox navList = new VBox(4, dashboard, spaces, search, calendar, ai, collab, recent,  trash, settings, logoutBtn);

        // Sidebar Storage Card
        Label storageTitle = text("Storage Used", 12, true, WHITE);
        storageTitle.setStyle("-fx-text-fill: " + WHITE + ";");

        Label storageVal = text("64.2 GB of 100 GB", 12, true, WHITE);
        storageVal.setStyle("-fx-text-fill: " + WHITE + ";");

        Label storagePercent = text("64%", 11, true, LIGHT);
        storagePercent.setStyle("-fx-text-fill: " + LIGHT + ";");

        HBox storageValGroup = new HBox(storageVal, space(), storagePercent);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: " + BLUE + "; -fx-control-inner-background: #0E1520;");

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showLandingPage());

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = space();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox side = new VBox(12, logoBox, navList, notifications, sidebarSpacer, settings, storageCard);
        side.setPadding(new Insets(20, 14, 20, 14));
        side.setPrefWidth(230);
        side.setMinWidth(230);
        side.setStyle("-fx-background-color: " + NAV + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        // =========================================================
        // TOP SEARCH BAR & PROFILE
        // =========================================================

        Label searchIcon = text("⌕", 16, false, LIGHT);
        searchIcon.setStyle("-fx-text-fill: " + LIGHT + ";");

        TextField topSearch = new TextField();
        topSearch.setPromptText("Search in OneSpace...");
        topSearch.setPrefHeight(38);
        topSearch.setStyle("-fx-background-color: transparent; -fx-prompt-text-fill: " + LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + WHITE + ";");

        Label keyShortcut = text("⌘ K", 10, true, LIGHT);
        keyShortcut.setStyle("-fx-background-color: #141E2C; -fx-text-fill: " + LIGHT + "; -fx-padding: 3 6; -fx-background-radius: 4;");

        HBox topSearchContainer = new HBox(8, searchIcon, topSearch, keyShortcut);
        topSearchContainer.setAlignment(Pos.CENTER_LEFT);
        topSearchContainer.setPadding(new Insets(0, 12, 0, 14));
        topSearchContainer.setPrefWidth(420);
        topSearchContainer.setStyle("-fx-background-color: " + INPUT + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(topSearch, Priority.ALWAYS);

        Button bell = new Button("🔔");
        bell.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: " + WHITE + "; -fx-cursor: hand;");
        bell.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = text("AV", 12, true, WHITE);
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color: " + BLUE + "; -fx-text-fill: " + WHITE + "; -fx-background-radius: 50%;");

        Label userName = text("Aarav Verma", 13, true, WHITE);
        userName.setStyle("-fx-text-fill: " + WHITE + ";");

        Label dropDown = text("⌄", 12, false, LIGHT);
        dropDown.setStyle("-fx-text-fill: " + LIGHT + ";");

        HBox profileBox = new HBox(10, bell, avatar, userName, dropDown);
        profileBox.setAlignment(Pos.CENTER);

        HBox top = new HBox(20, topSearchContainer, space(), profileBox);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(16, 28, 14, 28));
        top.setStyle("-fx-background-color: " + NAV + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        // =========================================================
        // NOTIFICATIONS HEADER & FILTERS
        // =========================================================

        Label title = text("Notifications", 24, true, WHITE);
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label sub = text("Stay updated with OneSpace activity, reminders and collaboration.", 13, false, LIGHT);
        sub.setStyle("-fx-text-fill: " + LIGHT + ";");

        Button mark = new Button("✓  Mark all read");
        mark.setPrefHeight(36);
        mark.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        mark.setTextFill(Color.web(BLUE));
        mark.setStyle("-fx-background-color: " + CARD + "; -fx-border-color: " + BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
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

        Label warning = text("⚠  OneSpace never deletes or moves files automatically. Every suggested action requires your confirmation.", 12, false, LIGHT);
        warning.setStyle("-fx-text-fill: " + LIGHT + ";");

        HBox warningBox = new HBox(warning);
        warningBox.setPadding(new Insets(12, 16, 12, 16));
        warningBox.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");

        VBox content = new VBox(18, header, filters, list, warningBox);
        content.setPadding(new Insets(24, 28, 28, 28));
        content.setStyle("-fx-background-color: " + APP + ";");

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color: " + APP + "; -fx-background: " + APP + "; -fx-border-color: transparent; -fx-padding: 0;");

        VBox center = new VBox(top, scroll);
        center.setStyle("-fx-background-color: " + APP + ";");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setLeft(side);
        root.setCenter(center);
        root.setStyle("-fx-background-color: " + NAV + ";");

        return new Scene(root, 1200, 750);
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
        Label icon = text(n.icon, 18, true, BLUE);
        icon.setAlignment(Pos.CENTER);
        icon.setPrefSize(38, 38);
        icon.setStyle("-fx-background-color: " + LIGHT_BLUE + "; -fx-text-fill: " + BLUE + "; -fx-background-radius: 8;");

        Label title = text(n.title, 14, true, DARK);
        title.setStyle("-fx-text-fill: " + DARK + ";");

        Label sub = text(n.sub, 11, false, MUTED);
        sub.setStyle("-fx-text-fill: " + MUTED + ";");

        VBox info = new VBox(2, title, sub);

        Label time = text(n.time, 11, true, MUTED);
        time.setStyle("-fx-text-fill: " + MUTED + ";");

        Label dot = text(n.read ? "" : "●", 10, true, BLUE);
        dot.setStyle("-fx-text-fill: " + BLUE + ";");

        HBox row = new HBox(12, icon, info, space(), time, dot);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 14, 10, 14));

        String baseStyle = 
                "-fx-background-color: " + CARD + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 6, 0, 0, 2);";

        String hoverStyle = 
                "-fx-background-color: " + INNER + ";" +
                "-fx-border-color: " + BLUE + ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 6, 0, 0, 2);" +
                "-fx-cursor: hand;";

        row.setStyle(baseStyle);

        row.setOnMouseEntered(e -> row.setStyle(hoverStyle));
        row.setOnMouseExited(e -> row.setStyle(baseStyle));
        row.setOnMouseClicked(e -> { n.read = true; render(); });

        return row;
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
        b.setPadding(new Insets(0, 14, 0, 14));
        b.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        b.setTextFill(Color.web(active ? WHITE : DARK));
        b.setStyle(pill(active));
        return b;
    }

    private String pill(boolean active) {
        return "-fx-background-color: " + (active ? BLUE : CARD) + ";" +
               "-fx-text-fill: " + (active ? WHITE : DARK) + ";" +
               "-fx-border-color: " + BORDER + ";" +
               "-fx-border-radius: 16;" +
               "-fx-background-radius: 16;" +
               "-fx-cursor: hand;";
    }

    private Button nav(String icon, String name, boolean active) {
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(FONT, 14));

        Label textLbl = new Label(name);
        textLbl.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));

        HBox content = new HBox(12, iconLbl, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 12, 0, 12));

        if (active) {
            btn.setStyle("-fx-background-color: " + BLUE + "; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + WHITE + ";");
            textLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + LIGHT + ";");
            textLbl.setStyle("-fx-text-fill: " + WHITE + ";");

            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #26354A; -fx-background-radius: 8; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;"));
        }

        return btn;
    }

    private Label text(String s, double size, boolean bold, String color) {
        Label l = new Label(s);
        l.setFont(Font.font(FONT, bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        l.setTextFill(Color.web(color));
        return l;
    }

    private Region space() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
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

    private void init() {
        data.clear();

        data.add(new N("📄", "12 duplicate files detected",
                "Downloads folder · 4.2 GB recoverable", "1 h", "Reminders"));

        data.add(new N("🛡", "Sensitive files found",
                "Identity, PAN and passport scans detected", "3 h", "Reminders"));

        data.add(new N("📅", "Passport expires in 12 days",
                "Linked to Passport_Scan.pdf", "5 h", "Reminders"));

        data.add(new N("💬", "Riya commented on a shared file",
                "Cloud_Computing_Seminar.pptx", "Yesterday", "Collaboration"));

        data.add(new N("✦", "AI created 2 new Spaces",
                "Healthcare and Travel from 609 files", "2 d", "Reminders"));

        data.add(new N("👥", "Priya Sharma uploaded SVM_Optimization.pdf",
                "Shared in College Presentation Workspace", "2 d", "Collaboration"));
    }

    private static class N {
        String icon, title, sub, time, type;
        boolean read = false;

        N(String i, String t, String s, String tm, String ty) {
            icon = i; title = t; sub = s; time = tm; type = ty;
        }
    }
}