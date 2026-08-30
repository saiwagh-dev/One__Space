package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class AdminCollaboration {

    // Typography
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar: Deep Sleek Obsidian/Navy Tones
    private static final String SIDEBAR_BG = "#070C16";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";

    // 2. Center Workspace Canvas: Atmospheric Dark Radial Glow
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards & Borders
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";

    // 4. Vibrant Typography & Highlights
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";

    // Dynamic Accent Colors & Gradients
    private static final String BLUE = "#2563EB";
    private static final String PRIMARY_BLUE_HOVER = "#38BDF8";
    private static final String PURPLE = "#00D2FF";
    private static final String PURPLE_LIGHT = "rgba(0, 210, 255, 0.15)";

    public AdminCollaboration() {}

    public Scene getCollaborationScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
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
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH); sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH); sidebar.setMaxWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setTextFill(Color.web(WHITE));

        HBox logoRow = new HBox(10, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(4, logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardButton = createSidebarButton("dashboard", "Dashboard", false);
        Button usersButton = createSidebarButton("users", "Users", false);
        Button filesButton = createSidebarButton("files", "Files", false);
        Button collabButton = createSidebarButton("collab", "Collaboration", true);
        Button aiButton = createSidebarButton("ai", "AI System", false);
        Button analyticsButton = createSidebarButton("analytics", "Analytics", false);
        Button securityButton = createSidebarButton("security", "Security", false);

        dashboardButton.setOnAction(e -> LandingPage.showAdminDashboard());
        usersButton.setOnAction(e -> LandingPage.showAdminUsers());
        filesButton.setOnAction(e -> LandingPage.showAdminFiles());
        aiButton.setOnAction(e -> LandingPage.showAdminAISystem());
        analyticsButton.setOnAction(e -> LandingPage.showAnalytics());
        securityButton.setOnAction(e -> LandingPage.showAdminSecurity());

        VBox navList = new VBox(4, dashboardButton, usersButton, filesButton, collabButton, aiButton, analyticsButton, securityButton);

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        Button settingsButton = createSidebarButton("settings", "Settings", false);
        settingsButton.setOnAction(e -> LandingPage.showAdminSettings());

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + SIDEBAR_BORDER + ";");

        Button logoutButton = createSidebarButton("logout", "Logout", false);
        logoutButton.setOnAction(e -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(logoSection, navList, sidebarSpacer, settingsButton, divider, logoutButton);
        return sidebar;
    }

    private StackPane createLogo() {
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

    private Button createSidebarButton(String type, String text, boolean selected) {
        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(selected ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, selected ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.web(WHITE));

        HBox row = new HBox(12, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38); button.setMinHeight(38);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        if (selected) {
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
        SVGPath searchIcon = createIcon("search");
        searchIcon.setStroke(Color.web("#64748B"));
        searchIcon.setStrokeWidth(2);

        StackPane searchIconBox = new StackPane(searchIcon);
        searchIconBox.setPrefSize(24, 24);

        TextField search = new TextField();
        search.setPromptText("Search in OneSpace...");
        search.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        search.setPrefHeight(38);
        search.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #64748B; -fx-border-color: transparent; -fx-padding: 0;");

        HBox searchBox = new HBox(8, searchIconBox, search);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefHeight(38); searchBox.setMinHeight(38); searchBox.setMaxHeight(38);
        searchBox.setPrefWidth(420); searchBox.setMinWidth(420); searchBox.setMaxWidth(420);
        searchBox.setPadding(new Insets(0, 12, 0, 14));
        searchBox.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20;");
        HBox.setHgrow(search, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notification = new Button();
        notification.setGraphic(bell);
        notification.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        Label admin = new Label("Admin");
        admin.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        admin.setTextFill(Color.WHITE);
        

        HBox profile = new HBox(10, avatar, admin);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(4, 12, 4, 6));
        profile.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");
        profile.setOnMouseClicked(e -> LandingPage.showAdminProfilePage());

        HBox topBar = new HBox(20, searchBox, spacer, notification, profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private VBox createContent() {
        VBox root = new VBox(22);
        root.setFillWidth(true);
        root.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        root.setStyle("-fx-background-color: transparent;");

        Label title = new Label("Collaboration Management");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Oversee shared team spaces, member roles, and internal workspace governance.");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox headerText = new VBox(4, title, subtitle);

        root.getChildren().addAll(headerText, createWorkspaceSummaryCard(), createWorkspacesGridCard(), createActivityGridCard());
        return root;
    }

    private VBox createWorkspaceSummaryCard() {
        VBox card = card();
        card.setPrefWidth(480);
        card.setMaxWidth(500);
        card.setMinHeight(90);

        SVGPath icon = createIcon("collab");
        icon.setStroke(Color.web(BLUE));
        icon.setStrokeWidth(2);

        Text title = createTextNode("Active Shared Workspaces Overview", 13, true, WHITE);
        HBox top = new HBox(8, icon, title);
        top.setAlignment(Pos.CENTER_LEFT);

        Text number = createTextNode("24 Active Spaces", 20, true, WHITE);
        Text detail = createTextNode("Connecting 186 active internal collaborators across organizations.", 11, false, LIGHT_SECONDARY);

        card.getChildren().addAll(top, number, detail);
        return card;
    }

    private VBox createWorkspacesGridCard() {
        VBox card = card();
        card.setMinHeight(360);

        HBox header = cardHeader("collab", "Shared Workspaces", "Total: 24");

        GridPane table = new GridPane();
        table.setHgap(8); table.setVgap(16);
        table.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(35);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(25);
        ColumnConstraints c3 = new ColumnConstraints(); c3.setPercentWidth(20);
        ColumnConstraints c4 = new ColumnConstraints(); c4.setPercentWidth(20);
        table.getColumnConstraints().addAll(c1, c2, c3, c4);

        String[] headers = { "Workspace", "Owner", "Members", "Action" };
        for (int i = 0; i < headers.length; i++) {
            table.add(createTextNode(headers[i], 12, true, LIGHT_SECONDARY), i, 0);
        }

        List<Workspace> list = Arrays.asList(
                new Workspace("AI Development", "Aarav Verma", "12 Members"),
                new Workspace("Marketing Q3", "Neha Singh", "8 Members"),
                new Workspace("Finance Audit", "Rahul Mehta", "5 Members"),
                new Workspace("UI Redesign", "Riya Sharma", "15 Members")
        );

        int r = 1;
        for (Workspace w : list) {
            table.add(createTextNode(w.name, 12, true, WHITE), 0, r);
            table.add(createTextNode(w.owner, 12, false, LIGHT_SECONDARY), 1, r);
            table.add(createTextNode(w.members, 12, false, LIGHT_SECONDARY), 2, r);

            Button manage = new Button("Manage");
            manage.setFont(Font.font(FONT, FontWeight.BOLD, 10));
            manage.setStyle("-fx-text-fill: " + PURPLE + " !important; -fx-background-color: " + PURPLE_LIGHT + "; -fx-border-color: rgba(0, 210, 255, 0.3); -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;");
            manage.setOnAction(e -> showManageWorkspaceDialog(w));
            table.add(manage, 3, r);
            r++;
        }

        Label viewAllLink = link("View All Workspaces →");
        viewAllLink.setOnMouseClicked(e -> showAllWorkspacesModal(list));

        VBox.setVgrow(table, Priority.ALWAYS);
        card.getChildren().addAll(header, table, separator(), viewAllLink);
        return card;
    }

    private VBox createActivityGridCard() {
        VBox card = card();
        card.setMinHeight(360);

        HBox header = cardHeader("security", "Internal Collaboration Log", "Recent Activity");

        VBox logs = new VBox(14,
                activityItem("Aarav Verma", "added Neha Singh to", "AI Development", "10 min ago"),
                activityItem("Rahul Mehta", "updated roles in", "Finance Audit", "25 min ago"),
                activityItem("Riya Sharma", "created new workspace", "UI Redesign", "1 hour ago"),
                activityItem("Neha Singh", "removed member from", "Marketing Q3", "2 hours ago")
        );

        Label viewAuditLink = link("View Full Audit Log →");
        viewAuditLink.setOnMouseClicked(e -> showFullAuditLogModal());

        VBox.setVgrow(logs, Priority.ALWAYS);
        card.getChildren().addAll(header, logs, separator(), viewAuditLink);
        return card;
    }

    private void showManageWorkspaceDialog(Workspace w) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Manage Workspace");
        alert.setHeaderText("Workspace Details: " + w.name);
        alert.setContentText("Owner: " + w.owner + "\nMembers: " + w.members + "\n\nUse this dialog to update roles or remove users from this space.");
        alert.showAndWait();
    }

    private void showAllWorkspacesModal(List<Workspace> workspaces) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle("All Shared Workspaces");

        VBox container = new VBox(14);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #0D1626; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1;");

        Label header = new Label("All Active Shared Workspaces");
        header.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        header.setTextFill(Color.web(WHITE));

        VBox list = new VBox(10);
        for (Workspace w : workspaces) {
            Label nameLbl = new Label(w.name); nameLbl.setTextFill(Color.web(WHITE));
            Label ownerLbl = new Label("Owner: " + w.owner); ownerLbl.setTextFill(Color.web(LIGHT_SECONDARY));
            Label memLbl = new Label(w.members); memLbl.setTextFill(Color.web(LIGHT_SECONDARY));

            HBox item = new HBox(20, nameLbl, ownerLbl, memLbl);
            item.setStyle("-fx-background-color: rgba(16, 28, 48, 0.85); -fx-padding: 10; -fx-background-radius: 6; -fx-border-color: rgba(255,255,255,0.08); -fx-border-radius: 6;");
            list.getChildren().add(item);
        }

        Button close = new Button("Close");
        close.setStyle("-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-cursor: hand;");
        close.setOnAction(e -> modal.close());

        container.getChildren().addAll(header, list, close);
        modal.setScene(new Scene(container, 500, 400));
        modal.show();
    }

    private void showFullAuditLogModal() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Full Audit Log");
        alert.setHeaderText("Complete Workspace Audit Log");
        alert.setContentText("Displaying full log history of member assignments, permission updates, and workspace creation events.");
        alert.showAndWait();
    }

    private HBox activityItem(String user, String action, String target, String time) {
        Text userText = createTextNode(user + " ", 11, true, WHITE);
        Text actionText = createTextNode(action + " ", 11, false, LIGHT_SECONDARY);
        Text targetText = createTextNode(target, 11, true, PRIMARY_BLUE_HOVER);

        HBox textFlow = new HBox(userText, actionText, targetText);
        textFlow.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text timeText = createTextNode(time, 10, false, LIGHT_SECONDARY);

        HBox row = new HBox(6, textFlow, spacer, timeText);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 0, 6, 0));
        return row;
    }

    private VBox card() {
        VBox box = new VBox(14);
        box.setFillWidth(true);
        box.setPadding(new Insets(20));
        box.getStyleClass().add("dark-grid-card");
        box.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");
        return box;
    }

    private HBox cardHeader(String iconType, String title, String right) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(PURPLE));
        icon.setStrokeWidth(2);

        Text titleLabel = createTextNode(title, 14, true, WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(6, icon, titleLabel, spacer);
        header.setAlignment(Pos.CENTER_LEFT);

        if (!right.isEmpty()) {
            Text rightLabel = createTextNode(right, 11, true, PURPLE);
            header.getChildren().add(rightLabel);
        }
        return header;
    }

    private Text createTextNode(String text, double fontSize, boolean isBold, String hexColor) {
        Text textNode = new Text(text);
        textNode.setFont(Font.font(FONT, isBold ? FontWeight.BOLD : FontWeight.NORMAL, fontSize));
        textNode.setFill(Color.web(hexColor));
        textNode.setStyle("-fx-fill: " + hexColor + " !important; -fx-text-fill: " + hexColor + " !important;");
        return textNode;
    }

    private Label link(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        label.setStyle("-fx-text-fill: " + PRIMARY_BLUE_HOVER + " !important;");
        label.setCursor(javafx.scene.Cursor.HAND);
        return label;
    }

    private Separator separator() {
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08);");
        return separator;
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);
        switch (type) {
            case "collab": icon.setContent("M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2 M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M23 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75"); break;
            case "dashboard": icon.setContent("M3 3 H10 V10 H3 Z M14 3 H21 V10 H14 Z M3 14 H10 V21 H3 Z M14 14 H21 V21 H14 Z"); break;
            case "users": icon.setContent("M8 11 A3 3 0 1 0 8 5 A3 3 0 0 0 8 11 Z M16 11 A3 3 0 1 0 16 5 A3 3 0 0 0 16 11 Z M2 20 C2 16 5 14 8 14 C11 14 14 16 14 20 M12 15 C14 14 17 14 19 15 C21 16 22 18 22 20"); break;
            case "files": icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13"); break;
            case "ai": icon.setContent("M12 2 L13.5 8.5 L20 7 L15.5 11.5 L21 15 L14 14.5 L12 22 L10 14.5 L3 15 L8.5 11.5 L4 7 L10.5 8.5 Z"); break;
            case "analytics": icon.setContent("M4 20 V11 M10 20 V6 M16 20 V13 M22 20 V3"); break;
            case "security": icon.setContent("M12 2 L20 5 V11 C20 16 17 20 12 22 C7 20 4 16 4 11 V5 Z M9 12 L11 14 L15 9"); break;
            case "settings": icon.setContent("M12 3 V6 M12 18 V21 M3 12 H6 M18 12 H21 M5.6 5.6 L7.7 7.7 M16.3 16.3 L18.4 18.4 M18.4 5.6 L16.3 7.7 M7.7 16.3 L5.6 18.4 M12 8 A4 4 0 1 0 12 16 A4 4 0 0 0 12 8"); break;
            case "logout": icon.setContent("M10 4 H5 V20 H10 M14 8 L19 12 L14 16 M19 12 H8"); break;
            case "search": icon.setContent("M10 3 A7 7 0 1 0 10 17 A7 7 0 0 0 10 3 Z M15 15 L21 21"); break;
            case "bell": icon.setContent("M6 17 H18 M8 17 V10 A4 4 0 0 1 16 10 V17 M10 20 H14"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }

    private static class Workspace {
        String name, owner, members;
        Workspace(String name, String owner, String members) {
            this.name = name; this.owner = owner; this.members = members;
        }
    }
}