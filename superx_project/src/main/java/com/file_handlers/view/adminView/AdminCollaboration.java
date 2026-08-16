package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class AdminCollaboration {

    private static final String FONT = "Inter, 'Segoe UI', Arial, sans-serif";
    private static final String SIDEBAR_BG = "#1E2A3A";
    private static final String SIDEBAR_DARK = "#141D29";
    private static final String SIDEBAR_BORDER = "#334155";
    private static final String MAIN_BG = "#31435B";
    private static final String CARD_BG = "#DDE8F8";
    private static final String CARD_BORDER = "#C3D6EC";

    private static final String BLACK = "#000000";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#CBD5E1";

    private static final String BLUE = "#2563EB";
    private static final String PURPLE = "#7C3AED";
    private static final String PURPLE_LIGHT = "#EDE9FE";
    private static final String GREEN = "#059669";

    public AdminCollaboration() {}

    public Scene getCollaborationScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        VBox rightSide = new VBox(createTopBar(), scrollPane);
        rightSide.setFillWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.setCenter(rightSide);

        Scene scene = new Scene(root, 1200, 750);

        String cssOverride = "data:text/css," +
                ".dark-grid-card * { -fx-text-fill: #000000 !important; -fx-fill: #000000 !important; }" +
                ".dark-grid-card .text { -fx-text-fill: #000000 !important; -fx-fill: #000000 !important; }";
        scene.getStylesheets().add(cssOverride);

        return scene;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(230); sidebar.setMinWidth(230); sidebar.setMaxWidth(230);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        logoText.setTextFill(Color.web(WHITE));

        HBox logoRow = new HBox(12, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(logoRow);
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
        InputStream stream = getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png");
        if (stream != null) {
            Image logoImage = new Image(stream);
            ImageView imageView = new ImageView(logoImage);
            imageView.setFitWidth(42); 
            imageView.setFitHeight(42); 
            imageView.setPreserveRatio(true);
            return new StackPane(imageView);
        }
        Circle circle = new Circle(20, Color.web(BLUE));
        Label fallback = new Label("O");
        fallback.setFont(Font.font(FONT, FontWeight.BOLD, 20));
        fallback.setTextFill(Color.WHITE);
        return new StackPane(circle, fallback);
    }

    private Button createSidebarButton(String type, String text, boolean selected) {
        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(selected ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(27, 27);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, selected ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.web(WHITE));

        HBox row = new HBox(14, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38); button.setMinHeight(38);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        String baseStyle = "-fx-background-radius: 8; -fx-cursor: hand; -fx-border-width: 0;";
        if (selected) {
            button.setStyle("-fx-background-color: " + BLUE + ";" + baseStyle);
        } else {
            button.setStyle("-fx-background-color: transparent;" + baseStyle);
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color: " + SIDEBAR_DARK + ";" + baseStyle);
                icon.setStroke(Color.WHITE);
                label.setTextFill(Color.WHITE);
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent;" + baseStyle);
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                label.setTextFill(Color.WHITE);
            });
        }
        return button;
    }

    private HBox createTopBar() {
        SVGPath searchIcon = createIcon("search");
        searchIcon.setStroke(Color.web(LIGHT_SECONDARY));
        searchIcon.setStrokeWidth(2);

        StackPane searchIconBox = new StackPane(searchIcon);
        searchIconBox.setPrefSize(25, 25);

        TextField search = new TextField();
        search.setPromptText("Search in OneSpace...");
        search.setFont(Font.font(FONT, FontWeight.NORMAL, 15));
        search.setPrefHeight(38);
        search.setStyle("-fx-background-color: transparent; -fx-text-fill: #F8FAFC; -fx-prompt-text-fill: #94A3B8; -fx-border-color: transparent; -fx-padding: 0;");

        HBox searchBox = new HBox(8, searchIconBox, search);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefHeight(38); searchBox.setMaxWidth(Double.MAX_VALUE);
        searchBox.setPadding(new Insets(0, 10, 0, 12));
        searchBox.setStyle("-fx-background-color: " + SIDEBAR_DARK + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        HBox.setHgrow(search, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notification = new Button();
        notification.setGraphic(bell);
        notification.setPrefSize(38, 38);
        notification.setStyle("-fx-background-color: transparent; -fx-font-size: 19px; -fx-text-fill: #FFFFFF; -fx-cursor: hand;");

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: " + BLUE + "; -fx-background-radius: 50%;");

        Label admin = new Label("Admin");
        admin.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        admin.setTextFill(Color.WHITE);

        Label arrow = new Label("⌄");
        arrow.setFont(Font.font(FONT, FontWeight.NORMAL, 16));
        arrow.setTextFill(Color.web(LIGHT_SECONDARY));

        HBox profile = new HBox(8, notification, avatar, admin, arrow);
        profile.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, searchBox, spacer, profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 24, 16, 24));
        topBar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private VBox createContent() {
        VBox root = new VBox(22);
        root.setFillWidth(true);
        root.setPadding(new Insets(32, 38, 36, 38));
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");

        Label title = new Label("Collaboration Management");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 30));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Oversee shared team spaces, member roles, and internal workspace governance.");
        subtitle.setFont(Font.font(FONT, FontWeight.NORMAL, 15));
        subtitle.setTextFill(Color.WHITE);

        VBox headerText = new VBox(4, title, subtitle);

        HBox summaryRow = new HBox(18);
        addEqualChildren(summaryRow, createWorkspaceSummaryCard(), createGovernanceSummaryCard());

        HBox mainRow = new HBox(18);
        addEqualChildren(mainRow, createWorkspacesGridCard(), createActivityGridCard());

        root.getChildren().addAll(headerText, summaryRow, mainRow);
        return root;
    }

    private void addEqualChildren(HBox row, Region c1, Region c2) {
        c1.setMaxWidth(Double.MAX_VALUE); c1.setMinWidth(0);
        c2.setMaxWidth(Double.MAX_VALUE); c2.setMinWidth(0);
        HBox.setHgrow(c1, Priority.ALWAYS);
        HBox.setHgrow(c2, Priority.ALWAYS);
        row.getChildren().addAll(c1, c2);
    }

    private VBox createWorkspaceSummaryCard() {
        VBox card = card();
        card.setMinHeight(120);

        SVGPath icon = createIcon("collab");
        icon.setStroke(Color.web(BLUE));
        icon.setStrokeWidth(2);

        Text title = createTextNode("Active Shared Workspaces", 13, true, BLACK);
        HBox top = new HBox(8, icon, title);
        top.setAlignment(Pos.CENTER_LEFT);

        Text number = createTextNode("24", 24, true, BLACK);
        Text detail = createTextNode("Across 186 active internal collaborators", 11, false, BLACK);

        card.getChildren().addAll(top, number, detail);
        return card;
    }

    private VBox createGovernanceSummaryCard() {
        VBox card = card();
        card.setMinHeight(120);

        SVGPath icon = createIcon("security");
        icon.setStroke(Color.web(GREEN));
        icon.setStrokeWidth(2);

        Text title = createTextNode("Access Policy Status", 13, true, BLACK);
        HBox top = new HBox(8, icon, title);
        top.setAlignment(Pos.CENTER_LEFT);

        Text status = createTextNode("Restricted Access", 24, true, GREEN);
        Text detail = createTextNode("Domain-restricted sharing is active", 11, false, BLACK);

        card.getChildren().addAll(top, status, detail);
        return card;
    }

    private VBox createWorkspacesGridCard() {
        VBox card = card();
        card.setMinHeight(330);

        HBox header = cardHeader("collab", "Shared Workspaces", "Total: 24");

        GridPane table = new GridPane();
        table.setHgap(8); table.setVgap(10);
        table.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(35);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(25);
        ColumnConstraints c3 = new ColumnConstraints(); c3.setPercentWidth(20);
        ColumnConstraints c4 = new ColumnConstraints(); c4.setPercentWidth(20);
        table.getColumnConstraints().addAll(c1, c2, c3, c4);

        String[] headers = { "Workspace", "Owner", "Members", "Action" };
        for (int i = 0; i < headers.length; i++) {
            table.add(createTextNode(headers[i], 10, true, BLACK), i, 0);
        }

        List<Workspace> list = Arrays.asList(
                new Workspace("AI Development", "Aarav Verma", "12 Members"),
                new Workspace("Marketing Q3", "Neha Singh", "8 Members"),
                new Workspace("Finance Audit", "Rahul Mehta", "5 Members"),
                new Workspace("UI Redesign", "Riya Sharma", "15 Members")
        );

        int r = 1;
        for (Workspace w : list) {
            table.add(createTextNode(w.name, 10, true, BLACK), 0, r);
            table.add(createTextNode(w.owner, 10, false, BLACK), 1, r);
            table.add(createTextNode(w.members, 10, false, BLACK), 2, r);

            Button manage = new Button("Manage");
            manage.setFont(Font.font(FONT, FontWeight.BOLD, 9));
            manage.setStyle("-fx-text-fill: " + PURPLE + " !important; -fx-background-color: " + PURPLE_LIGHT + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;");
            table.add(manage, 3, r);
            r++;
        }

        VBox.setVgrow(table, Priority.ALWAYS);
        card.getChildren().addAll(header, table, separator(), link("View All Workspaces →"));
        return card;
    }

    private VBox createActivityGridCard() {
        VBox card = card();
        card.setMinHeight(330);

        HBox header = cardHeader("security", "Internal Collaboration Log", "Recent");

        VBox logs = new VBox(8,
                activityItem("Aarav Verma", "added Neha Singh to", "AI Development", "10 min ago"),
                activityItem("Rahul Mehta", "updated roles in", "Finance Audit", "25 min ago"),
                activityItem("Riya Sharma", "created new workspace", "UI Redesign", "1 hour ago"),
                activityItem("Neha Singh", "removed member from", "Marketing Q3", "2 hours ago")
        );

        VBox.setVgrow(logs, Priority.ALWAYS);
        card.getChildren().addAll(header, logs, separator(), link("View Full Audit Log →"));
        return card;
    }

    private HBox activityItem(String user, String action, String target, String time) {
        Text userText = createTextNode(user + " ", 10, true, BLACK);
        Text actionText = createTextNode(action + " ", 10, false, BLACK);
        Text targetText = createTextNode(target, 10, true, BLUE);

        HBox textFlow = new HBox(userText, actionText, targetText);
        textFlow.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text timeText = createTextNode(time, 9, false, BLACK);

        HBox row = new HBox(6, textFlow, spacer, timeText);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 0, 6, 0));
        return row;
    }

    private VBox card() {
        VBox box = new VBox(8);
        box.setFillWidth(true);
        box.setPadding(new Insets(14));
        box.getStyleClass().add("dark-grid-card");
        box.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1; -fx-border-radius: 14; -fx-background-radius: 14; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 7, 0, 0, 2);");
        return box;
    }

    private HBox cardHeader(String iconType, String title, String right) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(PURPLE));
        icon.setStrokeWidth(2);

        Text titleLabel = createTextNode(title, 13, true, BLACK);

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
        label.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        label.setStyle("-fx-text-fill: " + PURPLE + " !important;");
        label.setCursor(javafx.scene.Cursor.HAND);
        return label;
    }

    private Separator separator() {
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: " + CARD_BORDER + ";");
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