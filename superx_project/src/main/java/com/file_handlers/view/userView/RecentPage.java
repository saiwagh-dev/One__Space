package com.file_handlers.view.userView;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.model.FileData;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.util.Duration;
import java.awt.Desktop;
import java.io.File;
import java.util.List;

public class RecentPage {

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

    // 4. Vibrant Typography & Accent Highlights
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";

    private final FileDAO fileDAO = new FileDAO();
    private VBox fileRows;
    private Label emptyLabel;

    public Scene getRecentPageScene() {
        UserSession session = UserSession.getInstance();
        String activeUserName = "User", initials = "U";

        if (session != null && session.getDisplayName() != null && !session.getDisplayName().isBlank()) {
            String[] parts = session.getDisplayName().trim().split("\\s+");
            activeUserName = parts[0];
            initials = activeUserName.substring(0, 1).toUpperCase();
        }

        VBox sidebar = createSidebar();

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button bell = new Button();
        bell.setGraphic(bellIcon);
        bell.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        bell.setOnAction(e -> LandingPage.showNotificationPage());
        applyHoverAnimation(bell, 1.08, 0);

        Label avatar = label(initials, 12, FontWeight.BOLD, WHITE);
        avatar.setMinSize(34, 34); avatar.setPrefSize(34, 34); avatar.setMaxSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");
        applyHoverAnimation(avatar, 1.15, 0);

        Label userName = label(activeUserName, 13, FontWeight.SEMI_BOLD, WHITE);
        Label dropDown = label("⌄", 12, FontWeight.NORMAL, LIGHT_SECONDARY);

        HBox profileOption = new HBox(8, avatar, userName, dropDown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(4, 12, 4, 6));
        profileOption.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");
        applyHoverAnimation(profileOption, 1.04, 0);

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
            Platform.runLater(LandingPage::showUserProfilePage);
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
            Platform.runLater(LandingPage::showSettingPage);
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
            Platform.runLater(LandingPage::showUserLoginPage);
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

        Label title = label("Recent Files", 26, FontWeight.BOLD, WHITE);
        Label sub = label("Files you've recently opened in OneSpace.", 13, FontWeight.MEDIUM, LIGHT_SECONDARY);
        VBox header = new VBox(4, title, sub);

        HBox listHeader = new HBox(
                createHeaderLabel("Name", 370),
                createHeaderLabel("Space", 190),
                createHeaderLabel("Size", 110),
                createHeaderLabel("Last Accessed", 150)
        );
        listHeader.setPadding(new Insets(0, 12, 12, 12));
        listHeader.setStyle("-fx-border-color: transparent transparent rgba(255, 255, 255, 0.08) transparent; -fx-border-width: 0 0 1 0;");

        fileRows = new VBox(6);
        emptyLabel = label("Loading recent files...", 13, FontWeight.NORMAL, LIGHT_SECONDARY);

        VBox recentCard = new VBox(12, listHeader, fileRows);
        recentCard.setPadding(new Insets(20));
        recentCard.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");

        VBox content = new VBox(22, header, recentCard);
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color: transparent;");

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-background-insets: 0; -fx-padding: 0;");

        VBox main = new VBox(topBar, scroll);
        main.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(sidebar);
        root.setCenter(main);

        loadRecentFiles();
        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private void applyHoverAnimation(Node node, double scaleTo, double translateY) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
            st.setToX(scaleTo);
            st.setToY(scaleTo);
            st.play();

            if (translateY != 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(140), node);
                tt.setToY(translateY);
                tt.play();
            }
        });

        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            if (translateY != 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(140), node);
                tt.setToY(0);
                tt.play();
            }
        });
    }

    private void animateTranslate(Node node, double xTo, double yTo) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(130), node);
        tt.setToX(xTo);
        tt.setToY(yTo);
        tt.play();
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
        applyHoverAnimation(logoIcon, 1.1, 0);

        Label logoText = label("OneSpace", 19, FontWeight.BOLD, WHITE);
        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("dashboard", "Dashboard", false, e -> LandingPage.showUserDashboard());
        Button spacesBtn = createSidebarButton("files", "Spaces", false, e -> LandingPage.showUserSpace());
        Button searchBtn = createSidebarButton("search", "Search", false, e -> LandingPage.showUserSearch());
        Button calendarBtn = createSidebarButton("calendar", "Calendar", false, e -> LandingPage.showCalendarPage());
        Button aiBtn = createSidebarButton("ai", "AI Assistant", false, e -> LandingPage.showAiAssistantPage());
        Button collabBtn = createSidebarButton("collaboration", "Collaboration", false, e -> LandingPage.showCollaborationPage());
        Button recentBtn = createSidebarButton("recent", "Recent", true, e -> LandingPage.showRecentPage());
        Button trashBtn = createSidebarButton("trash", "Trash", false, e -> LandingPage.showTrashPage());
        Button settingsBtn = createSidebarButton("settings", "Settings", false, e -> LandingPage.showSettingPage());

        VBox navList = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

        Label storageTitle = label("Storage Used", 12, FontWeight.BOLD, WHITE);
        Label storageVal = label("64.2 GB of 100 GB", 12, FontWeight.BOLD, WHITE);
        Label storagePercent = label("64%", 11, FontWeight.BOLD, LIGHT_SECONDARY);

        Region storageGap = new Region();
        HBox.setHgrow(storageGap, Priority.ALWAYS);

        HBox storageValGroup = new HBox(storageVal, storageGap, storagePercent);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar progress = new ProgressBar(.64);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle("-fx-accent: " + BLUE + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        Button manageStorageBtn = new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexPage());
        manageStorageBtn.setOnMouseEntered(e -> animateTranslate(manageStorageBtn, 4, 0));
        manageStorageBtn.setOnMouseExited(e -> animateTranslate(manageStorageBtn, 0, 0));

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, progress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: rgba(16, 28, 48, 0.65); -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");
        applyHoverAnimation(storageCard, 1.01, -1);

        Region navGap = new Region();
        VBox.setVgrow(navGap, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, navGap, settingsBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        return sidebar;
    }

    private Button createSidebarButton(String iconType, String text, boolean active, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label textLabel = label(text, 13, active ? FontWeight.BOLD : FontWeight.MEDIUM, WHITE);

        HBox content = new HBox(12, iconBox, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(38);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));
        button.setOnAction(action);

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
                button.setStyle("-fx-background-color: rgba(56, 189, 248, 0.12); -fx-background-radius: 12; -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 12; -fx-border-width: 1; -fx-cursor: hand;");
                icon.setStroke(Color.web("#38BDF8"));
                textLabel.setTextFill(Color.web("#38BDF8"));
                animateTranslate(button, 4, 0);
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                textLabel.setTextFill(Color.web(WHITE));
                animateTranslate(button, 0, 0);
            });
        }

        return button;
    }

    private void loadRecentFiles() {
        UserSession session = UserSession.getInstance();

        if (session == null || !UserSession.isLoggedIn() || session.getUid() == null || session.getUid().isBlank()) {
            Platform.runLater(() -> showEmpty("No authenticated user."));
            return;
        }

        Thread thread = new Thread(() -> {
            try {
                List<FileData> files = fileDAO.getRecentFiles(session.getUid(), 20);
                Platform.runLater(() -> displayFiles(files));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showEmpty("Unable to load recent files."));
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void displayFiles(List<FileData> files) {
        fileRows.getChildren().clear();

        if (files == null || files.isEmpty()) {
            showEmpty("No recently accessed files.");
            return;
        }

        for (FileData file : files)
            fileRows.getChildren().add(createFileRow(file));
    }

    private void showEmpty(String text) {
        fileRows.getChildren().clear();
        fileRows.getChildren().add(emptyLabel);
        emptyLabel.setText(text);
    }

    private HBox createFileRow(FileData file) {
        String name = file.getFileName() == null ? "Unnamed file" : file.getFileName();
        String space = file.getSpaceId() == null || file.getSpaceId().isBlank() ? "Other" : file.getSpaceId();

        SVGPath icon = createIcon(getFileIconType(name));
        icon.setStroke(Color.web("#38BDF8"));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setPrefSize(40, 40); iconPane.setMinSize(40, 40);
        iconPane.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-background-radius: 10; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 10;");

        Label nameLabel = label(name, 13, FontWeight.BOLD, WHITE);
        nameLabel.setMaxWidth(330);
        nameLabel.setEllipsisString("...");

        HBox nameGroup = new HBox(12, iconPane, nameLabel);
        nameGroup.setAlignment(Pos.CENTER_LEFT);
        nameGroup.setPrefWidth(370);

        Label spaceLabel = label(space, 11, FontWeight.BOLD, "#38BDF8");
        spaceLabel.setPadding(new Insets(5, 10, 5, 10));
        spaceLabel.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 12; -fx-background-radius: 12;");

        HBox spaceGroup = new HBox(spaceLabel);
        spaceGroup.setAlignment(Pos.CENTER_LEFT);
        spaceGroup.setPrefWidth(190);

        Label size = label(formatSize(file.getFileSize()), 12, FontWeight.MEDIUM, LIGHT_SECONDARY);
        size.setPrefWidth(110);

        Label accessed = label(formatTimestamp(file), 12, FontWeight.MEDIUM, LIGHT_SECONDARY);
        accessed.setPrefWidth(150);

        HBox row = new HBox(nameGroup, spaceGroup, size, accessed);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 12, 8, 12));
        row.setMinHeight(56);

        String baseStyle = "-fx-background-color: transparent; -fx-background-radius: 10; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(56, 189, 248, 0.35); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.25), 8, 0, 0, 2);";

        row.setStyle(baseStyle);
        row.setOnMouseEntered(e -> {
            row.setStyle(hoverStyle);
            animateTranslate(row, 4, 0);
        });
        row.setOnMouseExited(e -> {
            row.setStyle(baseStyle);
            animateTranslate(row, 0, 0);
        });
        row.setOnMouseClicked(e -> openFile(file));

        return row;
    }

    private void openFile(FileData file) {
        if (file == null || file.getLocalPath() == null || file.getLocalPath().isBlank()) return;

        UserSession session = UserSession.getInstance();

        try {
            File target = new File(file.getLocalPath());

            if (!target.exists()) {
                showAlert("The file no longer exists at its stored location.");
                return;
            }

            if (!Desktop.isDesktopSupported()) {
                showAlert("Opening files is not supported on this system.");
                return;
            }

            Desktop.getDesktop().open(target);

            if (session != null && session.getUid() != null && !session.getUid().isBlank()) {
                Thread thread = new Thread(() -> {
                    try {
                        fileDAO.touchFile(session.getUid(), file.getFileHash());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        } catch (Exception e) {
            showAlert("Unable to open the file.");
        }
    }

    private String formatTimestamp(FileData file) {
        if (file.getLastAccessedAt() == null) return "—";

        long minutes = (System.currentTimeMillis() - file.getLastAccessedAt().toDate().getTime()) / 60000;

        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " min ago";

        long hours = minutes / 60;
        if (hours < 24) return hours + " hr ago";

        long days = hours / 24;
        if (days == 1) return "Yesterday";
        if (days < 7) return days + " days ago";

        return file.getLastAccessedAt().toDate().toString().substring(0, 10);
    }

    private String formatSize(long bytes) {
        if (bytes <= 0) return "0 B";
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1048576) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1073741824L) return String.format("%.1f MB", bytes / 1048576.0);
        return String.format("%.1f GB", bytes / 1073741824.0);
    }

    private String getFileIconType(String name) {
        String n = name.toLowerCase();
        if (n.matches(".*\\.(jpg|jpeg|png|gif|webp|mp4|avi|mkv|mov|mp3|wav|m4a)$")) return "media";
        return "files";
    }

    private Label createHeaderLabel(String text, double width) {
        Label label = label(text, 12, FontWeight.BOLD, LIGHT_SECONDARY);
        label.setPrefWidth(width);
        return label;
    }

    private Label label(String text, double size, FontWeight weight, String color) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, weight, size));
        label.setStyle("-fx-text-fill: " + color + ";");
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
            case "media": icon.setContent("M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("OneSpace");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}