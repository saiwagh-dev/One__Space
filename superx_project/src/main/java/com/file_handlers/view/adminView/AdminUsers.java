package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AdminUsers {
    private static final String FONT = "Inter, 'Segoe UI', Arial, sans-serif";
    private static final String SIDEBAR_BG = "#1E2A3A";
    private static final String SIDEBAR_DARK = "#141D29";
    private static final String SIDEBAR_BORDER = "#334155";
    private static final String MAIN_BG = "#31435B";
    private static final String CARD_BG = "#DDE8F8";
    private static final String CARD_BORDER = "#C3D6EC";
    private static final String BLUE = "#2563EB";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#CBD5E1";

    private final ObservableList<UserData> users = FXCollections.observableArrayList();
    private VBox tableBody;
    private TextField userSearch;
    private ComboBox<String> statusDropdown;

    public AdminUsers() {
        loadDummyUsers();
    }

    public Scene getAdminUsersScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createUsersContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        VBox rightSide = new VBox(createTopBar(), scrollPane);
        rightSide.setFillWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.setCenter(rightSide);

        return new Scene(root, 1200, 750);
    }

    private void loadDummyUsers() {
        users.addAll(
                new UserData("Aarav Sharma", "aarav.sharma@gmail.com", "Active", "Today, 09:30 AM"),
                new UserData("Priya Patel", "priya.patel@gmail.com", "Active", "Today, 08:10 AM"),
                new UserData("Rahul Deshmukh", "rahul.deshmukh@gmail.com", "Inactive", "Yesterday"),
                new UserData("Sneha Kulkarni", "sneha.kulkarni@gmail.com", "Active", "2 days ago"),
                new UserData("Rohan Joshi", "rohan.joshi@gmail.com", "Active", "Today, 11:15 AM"),
                new UserData("Neha Shah", "neha.shah@gmail.com", "Inactive", "5 days ago"),
                new UserData("Aditya Patil", "aditya.patil@gmail.com", "Active", "Today, 10:45 AM"),
                new UserData("Kavya Mehta", "kavya.mehta@gmail.com", "Active", "Yesterday")
        );
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(230); sidebar.setMinWidth(230); sidebar.setMaxWidth(230);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        logoText.setTextFill(Color.WHITE);

        HBox logoRow = new HBox(12, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = createSidebarButton("dashboard", "Dashboard", false);
        dashboard.setOnAction(e -> LandingPage.showAdminDashboard());
        Button usersButton = createSidebarButton("users", "Users", true);
        usersButton.setOnAction(e -> LandingPage.showAdminUsers());
        Button files = createSidebarButton("files", "Files", false);
        files.setOnAction(e -> LandingPage.showAdminFiles());
        Button collab = createSidebarButton("collab", "Collaboration", false);
        collab.setOnAction(e -> LandingPage.showAdminCollaboration());
        Button aiSystem = createSidebarButton("ai", "AI System", false);
        aiSystem.setOnAction(e -> LandingPage.showAdminAISystem());

        Button analytics = createSidebarButton("analytics", "Analytics", false);
        analytics.setOnAction(e -> LandingPage.showAnalytics());
        Button security = createSidebarButton("security", "Security", false);
        security.setOnAction(e -> LandingPage.showAdminSecurity());

        VBox navigation = new VBox(4, dashboard, usersButton, files, collab, aiSystem, analytics, security);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button settings = createSidebarButton("settings", "Settings", false);
        settings.setOnAction(e -> LandingPage.showAdminSettings());

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + SIDEBAR_BORDER + ";");

        Button logout = createSidebarButton("logout", "Logout", false);
        logout.setOnAction(e -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(logoSection, navigation, spacer, settings, divider, logout);
        return sidebar;
    }

    private Button createSidebarButton(String type, String text, boolean active) {
        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(27, 27);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.WHITE);

        HBox row = new HBox(14, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38); button.setMinHeight(38);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        String baseStyle = "-fx-background-radius: 8; -fx-cursor: hand; -fx-border-width: 0;";
        if (active) {
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
        searchIconBox.setPrefSize(25, 25); searchIconBox.setMinSize(25, 25); searchIconBox.setMaxSize(25, 25);

        TextField search = new TextField();
        search.setPromptText("Search in OneSpace...");
        search.setFont(Font.font(FONT, FontWeight.NORMAL, 15));
        search.setPrefHeight(38); search.setMinHeight(38); search.setMaxHeight(38);
        search.setStyle("-fx-background-color: transparent; -fx-text-fill: #F8FAFC; -fx-prompt-text-fill: #94A3B8; -fx-border-color: transparent; -fx-padding: 0;");
        HBox.setHgrow(search, Priority.ALWAYS);

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
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, 24, 16, 24));
        topBar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private VBox createUsersContent() {
        Label title = createLabel("Users", "-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        Label subtitle = createLabel("Manage your organization's users easily.", "-fx-font-size: 16px; -fx-text-fill: #CBD5E1;");
        VBox titleBox = new VBox(7, title, subtitle);

        userSearch = new TextField();
        userSearch.setPromptText("Search users...");
        userSearch.setPrefHeight(56);
        userSearch.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 15px; -fx-text-fill: #0B1220; -fx-prompt-text-fill: #657696;");
        HBox.setHgrow(userSearch, Priority.ALWAYS);

        SVGPath searchUserIcon = createIcon("search");
        searchUserIcon.setStroke(Color.web("#657696"));
        searchUserIcon.setStrokeWidth(2);

        StackPane userSearchIcon = new StackPane(searchUserIcon);
        userSearchIcon.setPrefSize(25, 25);

        HBox userSearchBox = new HBox(10, userSearchIcon, userSearch);
        userSearchBox.setAlignment(Pos.CENTER_LEFT);
        userSearchBox.setPrefHeight(56);
        HBox.setHgrow(userSearchBox, Priority.ALWAYS);
        userSearchBox.setPadding(new Insets(0, 15, 0, 18));
        userSearchBox.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 11; -fx-background-radius: 11;");

        statusDropdown = createDropdown(182, "All Status", "All Status", "Active", "Inactive");

        HBox filterRow = new HBox(25, userSearchBox, statusDropdown);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        filterRow.setPadding(new Insets(30, 0, 28, 0));

        userSearch.textProperty().addListener((observable, oldValue, newValue) -> refreshUserTable());
        statusDropdown.valueProperty().addListener((observable, oldValue, newValue) -> refreshUserTable());

        GridPane tableHeader = createTableGrid();
        tableHeader.setMinHeight(48); tableHeader.setPrefHeight(48);
        tableHeader.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1 0 1 0;");

        CheckBox selectAll = new CheckBox();
        GridPane.setColumnIndex(selectAll, 0);
        GridPane.setHalignment(selectAll, HPos.CENTER);
        tableHeader.getChildren().add(selectAll);

        tableHeader.add(createHeaderLabel("User"), 1, 0);
        tableHeader.add(createHeaderLabel("Email"), 2, 0);
        tableHeader.add(createHeaderLabel("Account Status"), 3, 0);
        tableHeader.add(createHeaderLabel("Last Login"), 4, 0);
        tableHeader.add(createHeaderLabel("Actions"), 5, 0);

        tableBody = new VBox();
        tableBody.setFillWidth(true);
        tableBody.setStyle("-fx-background-color: " + CARD_BG + ";");

        selectAll.setOnAction(e -> {
            for (javafx.scene.Node node : tableBody.getChildren()) {
                if (node instanceof GridPane) {
                    for (javafx.scene.Node child : ((GridPane) node).getChildren()) {
                        if (child instanceof CheckBox) {
                            ((CheckBox) child).setSelected(selectAll.isSelected());
                            break;
                        }
                    }
                }
            }
        });

        refreshUserTable();

        VBox table = new VBox(tableHeader, tableBody);
        table.setFillWidth(true);
        VBox.setVgrow(tableBody, Priority.ALWAYS);
        table.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 14; -fx-background-radius: 14;");

        VBox content = new VBox(0, titleBox, filterRow, table, createPagination());
        content.setFillWidth(true);
        content.setPadding(new Insets(42, 48, 30, 48));
        content.setStyle("-fx-background-color: " + MAIN_BG + ";");
        return content;
    }

    private GridPane createTableGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(0); grid.setVgap(0); grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints checkColumn = new ColumnConstraints(); checkColumn.setPercentWidth(6); checkColumn.setHgrow(Priority.NEVER);
        ColumnConstraints userColumn = new ColumnConstraints(); userColumn.setPercentWidth(25); userColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints emailColumn = new ColumnConstraints(); emailColumn.setPercentWidth(25); emailColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints statusColumn = new ColumnConstraints(); statusColumn.setPercentWidth(16); statusColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints loginColumn = new ColumnConstraints(); loginColumn.setPercentWidth(18); loginColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints actionsColumn = new ColumnConstraints(); actionsColumn.setPercentWidth(10); actionsColumn.setHgrow(Priority.NEVER);

        grid.getColumnConstraints().addAll(checkColumn, userColumn, emailColumn, statusColumn, loginColumn, actionsColumn);
        return grid;
    }

    private void refreshUserTable() {
        if (tableBody == null) return;
        tableBody.getChildren().clear();

        String searchText = userSearch == null ? "" : userSearch.getText().trim().toLowerCase();
        String selectedStatus = statusDropdown == null ? "All Status" : statusDropdown.getValue();

        List<UserData> filteredUsers = new ArrayList<>();
        for (UserData user : users) {
            boolean searchMatches = searchText.isEmpty() || user.getName().toLowerCase().contains(searchText) || user.getEmail().toLowerCase().contains(searchText);
            boolean statusMatches = selectedStatus == null || selectedStatus.equals("All Status") || user.getStatus().equals(selectedStatus);
            if (searchMatches && statusMatches) filteredUsers.add(user);
        }

        if (filteredUsers.isEmpty()) {
            VBox noUsers = new VBox(10,
                    createLabel("No users found", "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0B1220;"),
                    createLabel("Try changing your search or status filter.", "-fx-font-size: 15px; -fx-text-fill: #334155;")
            );
            noUsers.setAlignment(Pos.CENTER);
            noUsers.setPrefHeight(300);
            tableBody.getChildren().add(noUsers);
            return;
        }

        for (UserData user : filteredUsers) {
            tableBody.getChildren().add(createUserRow(user));
        }
    }

    private GridPane createUserRow(UserData user) {
        GridPane row = createTableGrid();
        row.setMinHeight(72); row.setPrefHeight(72); row.setMaxWidth(Double.MAX_VALUE);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(0, 10, 0, 10));
        row.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 0 0 1 0;");

        CheckBox checkBox = new CheckBox();
        GridPane.setHalignment(checkBox, HPos.CENTER);
        row.add(checkBox, 0, 0);

        Circle avatarCircle = new Circle(20, getAvatarColor(user.getName()));
        Label initials = createLabel(getInitials(user.getName()), "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        StackPane avatarPane = new StackPane(avatarCircle, initials);

        Label nameLabel = createLabel(user.getName(), "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0B1220;");
        HBox userBox = new HBox(12, avatarPane, nameLabel);
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(0, 8, 0, 8));
        userBox.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(userBox, Priority.ALWAYS);
        row.add(userBox, 1, 0);

        Label emailLabel = createLabel(user.getEmail(), "-fx-font-size: 14px; -fx-text-fill: #1E293B;");
        emailLabel.setMaxWidth(Double.MAX_VALUE);
        emailLabel.setPadding(new Insets(0, 8, 0, 8));
        GridPane.setHgrow(emailLabel, Priority.ALWAYS);
        row.add(emailLabel, 2, 0);

        Label statusLabel = new Label("●  " + user.getStatus());
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setAlignment(Pos.CENTER_LEFT);
        statusLabel.setPadding(new Insets(6, 12, 6, 12));
        if (user.getStatus().equalsIgnoreCase("Active")) {
            statusLabel.setStyle("-fx-background-color: #A7F3D0; -fx-text-fill: #047857; -fx-background-radius: 15; -fx-font-size: 12px; -fx-font-weight: bold;");
        } else {
            statusLabel.setStyle("-fx-background-color: #FECACA; -fx-text-fill: #B91C1C; -fx-background-radius: 15; -fx-font-size: 12px; -fx-font-weight: bold;");
        }
        GridPane.setHgrow(statusLabel, Priority.ALWAYS);
        row.add(statusLabel, 3, 0);

        Label lastLogin = createLabel(user.getLastLogin(), "-fx-font-size: 14px; -fx-text-fill: #1E293B;");
        lastLogin.setPadding(new Insets(0, 8, 0, 8));
        lastLogin.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lastLogin, Priority.ALWAYS);
        row.add(lastLogin, 4, 0);

        Button actionButton = new Button("⋮");
        actionButton.setPrefSize(42, 38);
        actionButton.setStyle("-fx-background-color: transparent; -fx-font-size: 22px; -fx-text-fill: #1E293B; -fx-cursor: hand;");
        actionButton.setOnMouseEntered(e -> actionButton.setStyle("-fx-background-color: #BFDBFE; -fx-background-radius: 8; -fx-font-size: 22px; -fx-text-fill: #2563EB; -fx-cursor: hand;"));
        actionButton.setOnMouseExited(e -> actionButton.setStyle("-fx-background-color: transparent; -fx-font-size: 22px; -fx-text-fill: #1E293B; -fx-cursor: hand;"));

        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewItem = new MenuItem("View User");
        MenuItem editItem = new MenuItem("Edit User");
        MenuItem deleteItem = new MenuItem("Delete User");
        contextMenu.getItems().addAll(viewItem, editItem, new SeparatorMenuItem(), deleteItem);

        actionButton.setOnAction(e -> contextMenu.show(actionButton, Side.BOTTOM, 0, 0));
        viewItem.setOnAction(e -> showUserDetails(user));
        editItem.setOnAction(e -> showInfo("Edit User", "Edit functionality can be connected later."));
        deleteItem.setOnAction(e -> deleteUser(user));

        row.add(actionButton, 5, 0);
        GridPane.setHalignment(actionButton, HPos.CENTER);

        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #EAF2FC; -fx-border-color: #C3D6EC; -fx-border-width: 0 0 1 0;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 0 0 1 0;"));
        return row;
    }

    private void showUserDetails(UserData user) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Details");
        alert.setHeaderText(user.getName());
        alert.setContentText("Email: " + user.getEmail() + "\n\nAccount Status: " + user.getStatus() + "\n\nLast Login: " + user.getLastLogin());
        alert.showAndWait();
    }

    private void deleteUser(UserData user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Delete " + user.getName() + "?");
        alert.setContentText("This action will remove the user from this list.");
        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                users.remove(user);
                refreshUserTable();
            }
        });
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(message);
        alert.showAndWait();
    }

    private HBox createPagination() {
        Label dots = createLabel("...", "-fx-font-size: 14px; -fx-text-fill: #1E293B;");
        dots.setAlignment(Pos.CENTER); dots.setPrefSize(35, 42);

        HBox pagination = new HBox(6,
                createPageButton("‹"), createPageButton("1"), createPageButton("2"), createPageButton("3"),
                createPageButton("4"), createPageButton("5"), dots, createPageButton("10"), createPageButton("›")
        );
        pagination.setAlignment(Pos.CENTER);
        pagination.setPadding(new Insets(22, 0, 0, 0));
        return pagination;
    }

    private Button createPageButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(42, 42);
        boolean active = text.equals("1");

        String normalStyle = "-fx-background-color: " + (active ? BLUE : "#FFFFFF") + "; -fx-border-color: " + (active ? BLUE : "#C3D6EC") + "; -fx-text-fill: " + (active ? WHITE : "#28344D") + "; -fx-border-radius: 9; -fx-background-radius: 9; -fx-font-size: 14px; -fx-cursor: hand;";
        button.setStyle(normalStyle);

        if (!active) {
            button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #BFDBFE; -fx-border-color: #2563EB; -fx-text-fill: #2563EB; -fx-border-radius: 9; -fx-background-radius: 9; -fx-font-size: 14px; -fx-cursor: hand;"));
            button.setOnMouseExited(e -> button.setStyle(normalStyle));
        }
        return button;
    }

    private Label createLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        label.setFont(Font.font(FONT));
        return label;
    }

    private Label createHeaderLabel(String text) {
        Label label = createLabel(text, "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0B1220;");
        label.setAlignment(Pos.CENTER_LEFT);
        label.setPadding(new Insets(0, 8, 0, 8));
        return label;
    }

    private ComboBox<String> createDropdown(double width, String defaultValue, String... items) {
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll(items);
        cb.setValue(defaultValue);
        cb.setPrefSize(width, 56); cb.setMinWidth(width);
        cb.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 11; -fx-background-radius: 11; -fx-font-size: 15px; -fx-text-fill: #0F172A;");
        return cb;
    }

    private String getInitials(String name) {
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }

    private Color getAvatarColor(String name) {
        int index = Math.abs(name.hashCode()) % 5;
        switch (index) {
            case 0: return Color.web("#E9C7B8");
            case 1: return Color.web("#C8D8F5");
            case 2: return Color.web("#D8C8F2");
            case 3: return Color.web("#C9E6D2");
            default: return Color.web("#F1D7B5");
        }
    }

    private StackPane createLogo() {
        InputStream stream = getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png");
        if (stream != null) {
            Image logoImage = new Image(stream);
            ImageView imageView = new ImageView(logoImage);
            imageView.setFitWidth(42); imageView.setFitHeight(42); imageView.setPreserveRatio(true); imageView.setSmooth(true);
            return new StackPane(imageView);
        }
        Circle circle = new Circle(20, Color.web(BLUE));
        Label fallback = new Label("O");
        fallback.setFont(Font.font(FONT, FontWeight.BOLD, 20));
        fallback.setTextFill(Color.WHITE);
        return new StackPane(circle, fallback);
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);
        switch (type) {
            case "dashboard": icon.setContent("M3 3 H10 V10 H3 Z M14 3 H21 V10 H14 Z M3 14 H10 V21 H3 Z M14 14 H21 V21 H14 Z"); break;
            case "users": icon.setContent("M8 11 A3 3 0 1 0 8 5 A3 3 0 0 0 8 11 Z M16 11 A3 3 0 1 0 16 5 A3 3 0 0 0 16 11 Z M2 20 C2 16 5 14 8 14 C11 14 14 16 14 20 M12 15 C14 14 17 14 19 15 C21 16 22 18 22 20"); break;
            case "files": icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13"); break;
            case "collab": icon.setContent("M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2 M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M23 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75"); break;
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

    private static class UserData {
        private final String name, email, status, lastLogin;

        public UserData(String name, String email, String status, String lastLogin) {
            this.name = name; this.email = email; this.status = status; this.lastLogin = lastLogin;
        }

        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getStatus() { return status; }
        public String getLastLogin() { return lastLogin; }
    }
}