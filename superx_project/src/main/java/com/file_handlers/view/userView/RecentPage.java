package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RecentPage {

    private static final String FONT = "Inter";
    private static final String BG_APP = "#3A4D67";
    private static final String BG_CARD = "#DDE8F5";
    private static final String BG_CARD_INNER = "#D1E1F1";
    private static final String BG_INPUT = "#EDF3FA";
    private static final String BG_SIDEBAR_CARD = "#2E3F55";
    private static final String BORDER_COLOR = "#C9DAEE";
    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String PRIMARY_LIGHT_BLUE = "#BFDBFE";
    private static final String TEXT_DARK = "#142338";
    private static final String TEXT_MUTED_DARK = "#506580";
    private static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#9EB0C6";

    public Scene getRecentPageScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG_APP + ";");

        VBox main = createRecentContent();
        VBox center = new VBox(createTopBar(), main);
        VBox.setVgrow(main, Priority.ALWAYS);

        root.setLeft(createSidebar());
        root.setCenter(center);

        return new Scene(root, 1200, 750);
    }

    private VBox createSidebar() {
        Label logoIcon = new Label("◉");
        logoIcon.setFont(Font.font(FONT, FontWeight.BOLD, 23));
        logoIcon.setTextFill(Color.web(PRIMARY_LIGHT_BLUE));

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        logoText.setTextFill(Color.web(TEXT_LIGHT));

        HBox logoHeader = new HBox(8, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        Label tagline = new Label("Local · AI Indexed");
        tagline.setFont(Font.font(FONT, 11));
        tagline.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        VBox logoBox = new VBox(3, logoHeader, tagline);
        logoBox.setPadding(new Insets(0, 0, 18, 8));

        Button dashboard = createSidebarButton("⌂", "Dashboard", false);
        Button spaces = createSidebarButton("▦", "Spaces", false);
        Button search = createSidebarButton("⌕", "Search", false);
        Button calendar = createSidebarButton("□", "Calendar", false);
        Button ai = createSidebarButton("✧", "AI Assistant", false);
        Button collaboration = createSidebarButton("♧", "Collaboration", false);
        Button recent = createSidebarButton("◷", "Recent", true);
        Button trash = createSidebarButton("♜", "Trash", false);
        Button settings = createSidebarButton("⚙", "Settings", false);

        dashboard.setOnAction(e -> LandingPage.showUserDashboard());
        spaces.setOnAction(e -> LandingPage.showUserSpace());
        search.setOnAction(e -> showPlaceholderPage("Search"));
        calendar.setOnAction(e -> showPlaceholderPage("Calendar"));
        ai.setOnAction(e -> showPlaceholderPage("AI Assistant"));
        collaboration.setOnAction(e -> LandingPage.showCollaborationPage());
        recent.setOnAction(e -> LandingPage.showRecentPage());
        trash.setOnAction(e -> showPlaceholderPage("Trash"));
        settings.setOnAction(e -> showPlaceholderPage("Settings"));

        VBox nav = new VBox(
                5,
                dashboard,
                spaces,
                search,
                calendar,
                ai,
                collaboration,
                recent,
                trash
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox sidebar = new VBox(
                10,
                logoBox,
                nav,
                spacer,
                settings,
                createStorageCard()
        );

        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setMaxWidth(230);
        sidebar.setStyle(
                "-fx-background-color:" + BG_SIDEBAR_CARD + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-width:0 1 0 0;"
        );

        return sidebar;
    }

    private Button createSidebarButton(String icon, String label, boolean active) {
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(FONT, 15));

        Label textLabel = new Label(label);
        textLabel.setFont(Font.font(
                FONT,
                active ? FontWeight.BOLD : FontWeight.MEDIUM,
                13
        ));

        HBox content = new HBox(12, iconLabel, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("", content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(39);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        String normal =
                "-fx-background-color:transparent;" +
                "-fx-background-radius:9;" +
                "-fx-cursor:hand;";

        String activeStyle =
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-background-radius:9;" +
                "-fx-cursor:hand;";

        String hover =
                "-fx-background-color:#405572;" +
                "-fx-background-radius:9;" +
                "-fx-cursor:hand;";

        if (active) {
            button.setStyle(activeStyle);
            iconLabel.setTextFill(Color.web(TEXT_LIGHT));
            textLabel.setTextFill(Color.web(TEXT_LIGHT));
        } else {
            button.setStyle(normal);
            iconLabel.setTextFill(Color.web(TEXT_MUTED_LIGHT));
            textLabel.setTextFill(Color.web(TEXT_LIGHT));

            button.setOnMouseEntered(e -> button.setStyle(hover));
            button.setOnMouseExited(e -> button.setStyle(normal));
        }

        return button;
    }

    private VBox createStorageCard() {
        Label badge = new Label("✧  Storage indexed");
        badge.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        badge.setTextFill(Color.web(PRIMARY_LIGHT_BLUE));

        Label value = new Label("64.2 GB");
        value.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        value.setTextFill(Color.web(TEXT_LIGHT));

        Label sub = new Label("of 100 GB used");
        sub.setFont(Font.font(FONT, 11));
        sub.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        VBox storageText = new VBox(1, value, sub);

        ProgressBar progress = new ProgressBar(0.642);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(7);
        progress.setStyle(
                "-fx-accent:" + PRIMARY_BLUE + ";" +
                "-fx-control-inner-background:#435873;"
        );

        Label info = new Label(
                "Files stay in place —\n" +
                "nothing moved or renamed."
        );
        info.setFont(Font.font(FONT, 11));
        info.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        VBox card = new VBox(10, badge, storageText, progress, info);
        card.setPadding(new Insets(14));

        String normal =
                "-fx-background-color:#26374C;" +
                "-fx-border-color:#405572;" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;" +
                "-fx-cursor:hand;";

        String hover =
                "-fx-background-color:#2A3D53;" +
                "-fx-border-color:" + PRIMARY_BLUE + ";" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;" +
                "-fx-cursor:hand;";

        card.setStyle(normal);
        card.setOnMouseEntered(e -> card.setStyle(hover));
        card.setOnMouseExited(e -> card.setStyle(normal));

        return card;
    }

    private HBox createTopBar() {
        TextField searchField = new TextField();
        searchField.setPromptText("Ask OneSpace anything...");
        searchField.setPrefHeight(40);
        searchField.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-prompt-text-fill:" + TEXT_MUTED_DARK + ";" +
                "-fx-text-fill:" + TEXT_DARK + ";" +
                "-fx-font-family:'Inter';" +
                "-fx-font-size:13px;"
        );

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 17));
        searchIcon.setTextFill(Color.web(TEXT_MUTED_DARK));

        Label shortcut = new Label("Ctrl K");
        shortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        shortcut.setTextFill(Color.web(TEXT_MUTED_DARK));
        shortcut.setStyle(
                "-fx-background-color:" + BG_CARD_INNER + ";" +
                "-fx-padding:4 7;" +
                "-fx-background-radius:5;"
        );

        HBox search = new HBox(8, searchIcon, searchField, shortcut);
        search.setAlignment(Pos.CENTER_LEFT);
        search.setPadding(new Insets(0, 10, 0, 12));
        search.setPrefHeight(40);
        search.setMaxWidth(500);
        search.setStyle(
                "-fx-background-color:" + BG_INPUT + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;"
        );

        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button notification = new Button("♢");
        notification.setPrefSize(38, 38);

        String notificationNormal =
                "-fx-background-color:" + BG_CARD_INNER + ";" +
                "-fx-text-fill:" + TEXT_DARK + ";" +
                "-fx-font-size:17px;" +
                "-fx-background-radius:10;" +
                "-fx-cursor:hand;";

        String notificationHover =
                "-fx-background-color:" + PRIMARY_LIGHT_BLUE + ";" +
                "-fx-text-fill:" + PRIMARY_BLUE + ";" +
                "-fx-font-size:17px;" +
                "-fx-background-radius:10;" +
                "-fx-cursor:hand;";

        notification.setStyle(notificationNormal);
        notification.setOnMouseEntered(e -> notification.setStyle(notificationHover));
        notification.setOnMouseExited(e -> notification.setStyle(notificationNormal));

        Label avatar = new Label("AV");
        avatar.setPrefSize(36, 36);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-background-radius:50%;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-font-size:11px;"
        );

        Label userName = new Label("Aarav Verma");
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setTextFill(Color.web(TEXT_DARK));

        Label dropdown = new Label("⌄");
        dropdown.setTextFill(Color.web(TEXT_MUTED_DARK));

        HBox profile = new HBox(
                9,
                notification,
                avatar,
                userName,
                dropdown
        );
        profile.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(20, search, spacer, profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 24, 14, 24));

        return topBar;
    }

    private VBox createRecentContent() {
        Label title = new Label("Recent");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        title.setTextFill(Color.web(TEXT_LIGHT));

        Label subtitle = new Label(
                "Your recently accessed and indexed files."
        );
        subtitle.setFont(Font.font(FONT, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        VBox titleBox = new VBox(4, title, subtitle);

        Button typeButton = createPillFilterButton("All types");
        ContextMenu typeMenu = createTypeDropdown(typeButton);

        typeButton.setOnAction(
                e -> toggleDropdown(typeMenu, typeButton)
        );

        Button markRead = new Button("✓  Mark all as read");
        markRead.setPrefHeight(36);
        markRead.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        markRead.setTextFill(Color.web(PRIMARY_BLUE));
        markRead.setPadding(new Insets(0, 14, 0, 14));

        String readNormal =
                "-fx-background-color:" + PRIMARY_LIGHT_BLUE + ";" +
                "-fx-border-color:#93C5FD;" +
                "-fx-border-radius:9;" +
                "-fx-background-radius:9;" +
                "-fx-cursor:hand;";

        String readHover =
                "-fx-background-color:#A8CCF7;" +
                "-fx-border-color:#60A5FA;" +
                "-fx-border-radius:9;" +
                "-fx-background-radius:9;" +
                "-fx-cursor:hand;";

        markRead.setStyle(readNormal);
        markRead.setOnMouseEntered(e -> markRead.setStyle(readHover));
        markRead.setOnMouseExited(e -> markRead.setStyle(readNormal));

        markRead.setOnAction(e -> {
            markRead.setText("✓  All caught up");
            markRead.setTextFill(Color.web("#166534"));
            markRead.setStyle(
                    "-fx-background-color:#BBF7D0;" +
                    "-fx-border-color:#86EFAC;" +
                    "-fx-border-radius:9;" +
                    "-fx-background-radius:9;"
            );
        });

        HBox filters = new HBox(10, typeButton, markRead);
        filters.setAlignment(Pos.CENTER_RIGHT);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox header = new HBox(
                titleBox,
                headerSpacer,
                filters
        );
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane illustration = createEmptyIllustration();

        Label emptyTitle = new Label("No recent files yet");
        emptyTitle.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        emptyTitle.setTextFill(Color.web(TEXT_DARK));

        Label description = new Label(
                "Open or index files to see your recent activity here."
        );
        description.setFont(Font.font(FONT, 13));
        description.setTextFill(Color.web(TEXT_MUTED_DARK));
        description.setWrapText(true);
        description.setAlignment(Pos.CENTER);
        description.setMaxWidth(480);

        Button openSpaces = new Button("Open Spaces");
        openSpaces.setPrefHeight(36);
        openSpaces.setPadding(new Insets(0, 18, 0, 18));
        openSpaces.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        openSpaces.setTextFill(Color.web(TEXT_LIGHT));

        String openNormal =
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-background-radius:9;" +
                "-fx-cursor:hand;";

        String openHover =
                "-fx-background-color:#1D4ED8;" +
                "-fx-background-radius:9;" +
                "-fx-cursor:hand;";

        openSpaces.setStyle(openNormal);
        openSpaces.setOnMouseEntered(e -> openSpaces.setStyle(openHover));
        openSpaces.setOnMouseExited(e -> openSpaces.setStyle(openNormal));
        openSpaces.setOnAction(e -> LandingPage.showUserSpace());

        VBox emptyState = new VBox(
                15,
                illustration,
                emptyTitle,
                description,
                openSpaces
        );
        emptyState.setAlignment(Pos.CENTER);
        emptyState.setPadding(new Insets(40, 20, 40, 20));

        VBox recentCard = new VBox(emptyState);
        recentCard.setAlignment(Pos.CENTER);
        recentCard.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;"
        );

        VBox.setVgrow(recentCard, Priority.ALWAYS);

        VBox content = new VBox(16, header, recentCard);
        content.setPadding(new Insets(0, 24, 24, 24));
        VBox.setVgrow(recentCard, Priority.ALWAYS);

        return content;
    }

    private StackPane createEmptyIllustration() {
        Label folder = new Label("▱");
        folder.setFont(Font.font(FONT, 42));
        folder.setTextFill(Color.web(PRIMARY_BLUE));
        folder.setPrefSize(72, 72);
        folder.setAlignment(Pos.CENTER);
        folder.setStyle(
                "-fx-background-color:" + PRIMARY_LIGHT_BLUE + ";" +
                "-fx-background-radius:16;"
        );

        Label search = new Label("⌕");
        search.setFont(Font.font(FONT, 20));
        search.setTextFill(Color.web(PRIMARY_BLUE));
        search.setPrefSize(38, 38);
        search.setAlignment(Pos.CENTER);
        search.setStyle(
                "-fx-background-color:" + BG_INPUT + ";" +
                "-fx-background-radius:50%;" +
                "-fx-border-color:#93C5FD;" +
                "-fx-border-radius:50%;"
        );

        StackPane pane = new StackPane(folder, search);
        pane.setPrefSize(90, 82);

        StackPane.setAlignment(folder, Pos.CENTER);
        StackPane.setAlignment(search, Pos.BOTTOM_RIGHT);

        return pane;
    }

    private Button createPillFilterButton(String text) {
        Button button = new Button("☷   " + text + "   ⌄");
        button.setPrefHeight(36);
        button.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        button.setTextFill(Color.web(TEXT_DARK));
        button.setPadding(new Insets(0, 14, 0, 14));

        String normal =
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:18;" +
                "-fx-background-radius:18;" +
                "-fx-cursor:hand;";

        String hover =
                "-fx-background-color:" + PRIMARY_LIGHT_BLUE + ";" +
                "-fx-border-color:#93C5FD;" +
                "-fx-border-radius:18;" +
                "-fx-background-radius:18;" +
                "-fx-cursor:hand;";

        button.setStyle(normal);
        button.setOnMouseEntered(e -> button.setStyle(hover));
        button.setOnMouseExited(e -> button.setStyle(normal));

        return button;
    }

    private ContextMenu createTypeDropdown(Button filterButton) {
        ContextMenu menu = new ContextMenu();

        menu.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-background-radius:9;" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:9;" +
                "-fx-padding:5;"
        );

        String[] types = {
                "All types",
                "Images",
                "Videos",
                "Documents",
                "PDFs",
                "Spreadsheets",
                "Presentations"
        };

        for (String type : types) {
            MenuItem item = new MenuItem(type);

            item.setStyle(
                    "-fx-font-family:'Inter';" +
                    "-fx-font-size:12px;" +
                    "-fx-text-fill:" + TEXT_DARK + ";" +
                    "-fx-padding:7 18 7 12;"
            );

            item.setOnAction(e -> {
                filterButton.setText("☷   " + type + "   ⌄");
                menu.hide();
            });

            menu.getItems().add(item);
        }

        return menu;
    }

    private void toggleDropdown(ContextMenu menu, Button anchor) {
        if (menu.isShowing()) {
            menu.hide();
        } else {
            menu.show(anchor, Side.BOTTOM, 0, 5);
        }
    }

    private void showPlaceholderPage(String pageName) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG_APP + ";");

        Label title = new Label(pageName);
        title.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        title.setTextFill(Color.web(TEXT_LIGHT));

        Label message = new Label(
                pageName + " navigation is ready to be connected."
        );
        message.setFont(Font.font(FONT, 14));
        message.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        VBox box = new VBox(10, title, message);
        box.setAlignment(Pos.CENTER);

        root.setCenter(box);
        LandingPage.setScene(new Scene(root, 1200, 750));
    }
}