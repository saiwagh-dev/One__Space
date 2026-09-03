package com.file_handlers.view.userView;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.util.Duration;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.dao.SpaceDAO;
import com.file_handlers.model.FileData;
import com.file_handlers.model.SpaceData;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

import java.time.LocalTime;
import java.util.*;

public class UserDashboard {
    private final FileDAO fileDAO = new FileDAO();
    private final SpaceDAO spaceDAO = new SpaceDAO();

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

    private static final String[] CHART_COLORS = {
            "#2563EB",
            "#38BDF8",
            "#34D399",
            "#A78BFA",
            "#FBBF24",
            "#64748B"
    };

    public Scene getDashboardScene() {

        String activeUserName = "User";
        String initials = "U";

        UserSession session = UserSession.getInstance();

        if (session != null && session.getDisplayName() != null) {
            String fullName = session.getDisplayName().trim();
            if (!fullName.isEmpty()) {
                String[] parts = fullName.split("\\s+");
                activeUserName = parts[0];
                initials = activeUserName.substring(0, 1).toUpperCase();
            }
        }

        VBox sidebar = createSidebar();

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button bellBtn = new Button();
        bellBtn.setGraphic(bellIcon);
        bellBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());
        applyHoverAnimation(bellBtn, 1.08, 0);

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34); avatar.setMinSize(34, 34); avatar.setMaxSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");
        applyHoverAnimation(avatar, 1.15, 0);

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

        HBox profileBox = new HBox(10, bellBtn, profileOption);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(0), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 0 1 0;"
        );

        Label welcomeTitle = new Label(getTimeBasedGreeting() + ", " + activeUserName);
        welcomeTitle.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: " + WHITE + ";"
        );

        Label welcomeSub = new Label("Manage your files, spaces and AI-organized content from one place.");
        welcomeSub.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + LIGHT_SECONDARY + ";" +
                "-fx-font-weight: 500;"
        );

        VBox greetingText = new VBox(4, welcomeTitle, welcomeSub);

        Button addFilebtn = new Button("+   Add File");
        addFilebtn.setStyle(
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.45), 10, 0, 0, 2);"
        );

        addFilebtn.setOnAction(e ->
                LandingPage.setScene(
                        new AddFileData(
                                () -> LandingPage.showUserDashboard()
                        ).getScene()
                )
        );
        applyHoverAnimation(addFilebtn, 1.05, 0);

        AnchorPane greetingHeader = new AnchorPane(greetingText, addFilebtn);
        AnchorPane.setTopAnchor(greetingText, 0.0);
        AnchorPane.setLeftAnchor(greetingText, 0.0);
        AnchorPane.setTopAnchor(addFilebtn, 0.0);
        AnchorPane.setRightAnchor(addFilebtn, 0.0);

        HBox card1 = createMetricCard(
                "files", "Indexing Activity", "Loading...",
                "● Syncing", "From Firestore",
                "#38BDF8", "rgba(56, 189, 248, 0.15)", "#38BDF8"
        );

        HBox card2 = createMetricCard(
                "dashboard", "Active Spaces", "Loading...",
                "● Syncing", "From Firestore",
                "#38BDF8", "rgba(56, 189, 248, 0.15)", "#38BDF8"
        );

        HBox card3 = createMetricCard(
                "storage", "Indexed Storage", "Loading...",
                "● Syncing", "From Firestore",
                "#34D399", "rgba(52, 211, 153, 0.15)", "#34D399"
        );

        HBox card4 = createMetricCard(
                "ai", "AI Actions Live", "Loading...",
                "⚡ Syncing", "From indexed files",
                "#FBBF24", "rgba(245, 158, 11, 0.15)", "#FBBF24"
        );

        HBox metricsRow = new HBox(14, card1, card2, card3, card4);
        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);
        HBox.setHgrow(card4, Priority.ALWAYS);

        Label cardTitle = new Label("Space Occupancy");
        cardTitle.setStyle(
                "-fx-font-size: 17px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: " + WHITE + ";"
        );

        Label cardSub = new Label("Overview of file storage across your spaces.");
        cardSub.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: " + LIGHT_SECONDARY + ";"
        );

        VBox cardHeaderTitles = new VBox(2, cardTitle, cardSub);

        Button viewAllBtn = new Button("View all spaces ›");
        viewAllBtn.setStyle(
                "-fx-background-color: " + CARD_BG_INNER + ";" +
                "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: #60A5FA;" +
                "-fx-padding: 6 14;" +
                "-fx-cursor: hand;"
        );
        viewAllBtn.setOnAction(e -> LandingPage.showUserSpace());
        viewAllBtn.setOnMouseEntered(e -> {
            viewAllBtn.setStyle(
                    "-fx-background-color: rgba(56, 189, 248, 0.15);" +
                    "-fx-border-color: #38BDF8;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-text-fill: #38BDF8;" +
                    "-fx-padding: 6 14;" +
                    "-fx-cursor: hand;"
            );
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), viewAllBtn);
            tt.setToX(3);
            tt.play();
        });
        viewAllBtn.setOnMouseExited(e -> {
            viewAllBtn.setStyle(
                    "-fx-background-color: " + CARD_BG_INNER + ";" +
                    "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-text-fill: #60A5FA;" +
                    "-fx-padding: 6 14;" +
                    "-fx-cursor: hand;"
            );
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), viewAllBtn);
            tt.setToX(0);
            tt.play();
        });

        HBox cardHeader = new HBox(cardHeaderTitles, new Region(), viewAllBtn);
        HBox.setHgrow(cardHeader.getChildren().get(1), Priority.ALWAYS);
        cardHeader.setAlignment(Pos.CENTER_LEFT);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        PieChart chart = new PieChart(pieChartData);
        chart.setLabelsVisible(false);
        chart.setLegendVisible(false);
        chart.setPrefSize(205, 205);
        chart.setMaxSize(205, 205);

        Circle donutHole = new Circle(66, Color.web("#0A121E"));
        Label chartValText = new Label("Loading...");
        chartValText.setStyle("-fx-font-size: 18px; -fx-font-weight: 700; -fx-text-fill: " + WHITE + ";");
        Label chartSubText = new Label("from Firestore");
        chartSubText.setStyle("-fx-font-size: 11px; -fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox chartCenterText = new VBox(2, chartValText, chartSubText);
        chartCenterText.setAlignment(Pos.CENTER);

        StackPane donutChartPane = new StackPane(chart, donutHole, chartCenterText);
        applyHoverAnimation(donutChartPane, 1.03, 0);

        //
        Label spaceHeader = createHeaderLabel("Space", 0);
        Label storageHeader = createHeaderLabel("Storage Used", 0);
        Label percentageHeader = createHeaderLabel("Percentage", 0);

        HBox tableHeader = new HBox(spaceHeader, storageHeader, percentageHeader);
        tableHeader.setPadding(new Insets(0, 8, 8, 8));

        VBox spaceRows = new VBox(8, tableHeader, new Label("Loading..."));
        spaceRows.setFillWidth(true);

        ScrollPane spaceRowsScrollPane = new ScrollPane(spaceRows);
        spaceRowsScrollPane.setFitToWidth(true);
        spaceRowsScrollPane.setFitToHeight(true);
        spaceRowsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spaceRowsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spaceRowsScrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-padding: 0;"
        );

        // Bind header column widths after spaceRowsScrollPane is instantiated
        spaceHeader.prefWidthProperty().bind(spaceRowsScrollPane.widthProperty().multiply(0.35));
        storageHeader.prefWidthProperty().bind(spaceRowsScrollPane.widthProperty().multiply(0.25));
        percentageHeader.prefWidthProperty().bind(spaceRowsScrollPane.widthProperty().multiply(0.40));

        HBox cardContent = new HBox(24, donutChartPane, spaceRowsScrollPane);
        cardContent.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(spaceRowsScrollPane, Priority.ALWAYS);
        VBox.setVgrow(cardContent, Priority.ALWAYS);

        Label lastUpdated = new Label("Last updated just now");
        lastUpdated.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: " + LIGHT_SECONDARY + ";"
        );

        VBox occupancyCard = new VBox(16, cardHeader, cardContent, lastUpdated);
        VBox.setVgrow(occupancyCard, Priority.ALWAYS);
        occupancyCard.setPadding(new Insets(24));
        occupancyCard.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-width: 1.2;" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);"
        );

        loadDashboardData(
                pieChartData,
                chart,
                chartValText,
                chartSubText,
                spaceRows,
                lastUpdated,
                card1,
                card2,
                card3,
                card4
        );

        VBox contentBody = new VBox(
                22,
                greetingHeader,
                metricsRow,
                occupancyCard
        );
        VBox.setVgrow(occupancyCard, Priority.ALWAYS);

        contentBody.setPadding(new Insets(
                24,
                ResponsiveUtil.PAGE_PADDING,
                28,
                ResponsiveUtil.PAGE_PADDING
        ));

        contentBody.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-padding: 0;"
        );

        VBox mainArea = new VBox(topBar, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        mainArea.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        Scene scene = new Scene(
                root,
                LandingPage.getCurrentWidth(),
                LandingPage.getCurrentHeight()
        );

        Platform.runLater(() -> applyPieChartColors(pieChartData));

        return scene;
    }

    private String getTimeBasedGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return "Good morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good afternoon";
        } else if (hour >= 17 && hour < 22) {
            return "Good evening";
        } else {
            return "Good Night";
        }
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

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + WHITE + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("dashboard", "Dashboard", true, e -> LandingPage.showUserDashboard());
        Button spacesBtn = createSidebarButton("files", "Spaces", false, e -> LandingPage.showUserSpace());
        Button searchBtn = createSidebarButton("search", "Search", false, e -> LandingPage.showUserSearch());
        Button calendarBtn = createSidebarButton("calendar", "Calendar", false, e -> LandingPage.showCalendarPage());
        Button aiBtn = createSidebarButton("ai", "AI Assistant", false, e -> LandingPage.showAiAssistantPage());
        Button collabBtn = createSidebarButton("collaboration", "Collaboration", false, e -> LandingPage.showCollaborationPage());
        Button recentBtn = createSidebarButton("recent", "Recent", false, e -> LandingPage.showRecentPage());
        Button trashBtn = createSidebarButton("trash", "Trash", false, e -> LandingPage.showTrashPage());
        Button settingsBtn = createSidebarButton("settings", "Settings", false, e -> LandingPage.showSettingPage());

        VBox navList = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageTitle.setStyle("-fx-text-fill: " + WHITE + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-text-fill: " + WHITE + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        Region storageSpacer = new Region();
        HBox.setHgrow(storageSpacer, Priority.ALWAYS);

        HBox storageValGroup = new HBox(storageVal, storageSpacer, storagePercent);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: " + BLUE + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        Button manageStorageBtn = new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexPage());
        manageStorageBtn.setOnMouseEntered(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), manageStorageBtn);
            tt.setToX(3);
            tt.play();
        });
        manageStorageBtn.setOnMouseExited(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), manageStorageBtn);
            tt.setToX(0);
            tt.play();
        });

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: rgba(16, 28, 48, 0.65); -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");
        applyHoverAnimation(storageCard, 1.01, -1);

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, storageCard);
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

        Label label = new Label(text);
        label.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.web(WHITE));

        HBox content = new HBox(12, iconBox, label);
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
                label.setTextFill(Color.web("#38BDF8"));
                TranslateTransition tt = new TranslateTransition(Duration.millis(120), button);
                tt.setToX(4);
                tt.play();
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                label.setTextFill(Color.web(WHITE));
                TranslateTransition tt = new TranslateTransition(Duration.millis(120), button);
                tt.setToX(0);
                tt.play();
            });
        }

        return button;
    }

    private void loadDashboardData(
            ObservableList<PieChart.Data> chartData,
            PieChart chart,
            Label totalLabel,
            Label subLabel,
            VBox rows,
            Label updated,
            HBox card1,
            HBox card2,
            HBox card3,
            HBox card4
    ) {
        UserSession session = UserSession.getInstance();

        if (session == null ||
                !UserSession.isLoggedIn() ||
                session.getUid() == null ||
                session.getUid().isBlank()) {
            return;
        }

        String uid = session.getUid();

        Thread t = new Thread(() -> {
            try {
                // =====================================================
                // 1. LOAD CURRENT FILES
                // =====================================================

                List<FileData> files =
                        fileDAO.getFileSummaries(uid);

                // =====================================================
                // 2. LOAD CURRENT CUSTOM SPACES
                // =====================================================

                List<SpaceData> customSpaces =
                        spaceDAO.getUserSpaces(uid);

                // =====================================================
                // 3. BUILD ALL SPACES
                //    Six built-in Spaces + user's custom Spaces
                // =====================================================

                List<String> spaceIds = new ArrayList<>();
                List<String> spaceNames = new ArrayList<>();

                spaceIds.addAll(Arrays.asList(
                        "personal",
                        "college",
                        "office",
                        "finance",
                        "entertainment",
                        "other"
                ));

                spaceNames.addAll(Arrays.asList(
                        "Personal",
                        "College",
                        "Office",
                        "Finance",
                        "Entertainment",
                        "Others"
                ));

                for (SpaceData customSpace : customSpaces) {
                    if (customSpace == null ||
                            customSpace.getSpaceId() == null ||
                            customSpace.getSpaceId().isBlank()) {
                        continue;
                    }

                    spaceIds.add(customSpace.getSpaceId());
                    spaceNames.add(
                            customSpace.getName() == null ||
                                    customSpace.getName().isBlank()
                                    ? customSpace.getSpaceId()
                                    : customSpace.getName()
                    );
                }

                long[] totals =
                        new long[spaceIds.size()];

                long totalBytes = 0;
                int aiActionCount = 0;

                // =====================================================
                // 4. CALCULATE FILE / STORAGE / AI STATISTICS
                // =====================================================

                for (FileData file : files) {

                    if (file == null) {
                        continue;
                    }

                    totalBytes += Math.max(0, file.getFileSize());

                    String spaceId =
                            file.getSpaceId();

                    if (spaceId != null &&
                            !spaceId.isBlank()) {

                        for (int i = 0; i < spaceIds.size(); i++) {

                            if (spaceIds.get(i)
                                    .equalsIgnoreCase(spaceId)) {

                                totals[i] +=
                                        Math.max(0, file.getFileSize());

                                break;
                            }
                        }
                    }

                    // An AI action means this file has an AI category.
                    String aiCategory =
                            file.getAiCategory();

                    if (aiCategory != null &&
                            !aiCategory.isBlank()) {
                        aiActionCount++;
                    }
                }

                final long finalTotalBytes =
                        totalBytes;

                final int finalAiActionCount =
                        aiActionCount;

                final int totalSpaceCount =
                        spaceIds.size();

                final int customSpaceCount =
                        totalSpaceCount - 6;

                // =====================================================
                // 5. UPDATE JAVAFX UI
                // =====================================================

                Platform.runLater(() -> {

                    chartData.clear();

                    rows.getChildren().setAll(
                            tableHeaderNode(rows)
                    );

                    for (int i = 0;
                            i < spaceIds.size();
                            i++) {

                        double percentage =
                                finalTotalBytes == 0
                                        ? 0
                                        : totals[i] * 100.0 /
                                                finalTotalBytes;

                        String color =
                                CHART_COLORS[
                                        i % CHART_COLORS.length
                                ];

                        chartData.add(
                                new PieChart.Data(
                                        spaceNames.get(i),
                                        totals[i]
                                )
                        );

                        rows.getChildren().add(
                                createSpaceRow(
                                        "files",
                                        color,
                                        spaceNames.get(i),
                                        formatSize(totals[i]),
                                        percentage / 100.0,
                                        String.format(
                                                "%.0f%%",
                                                percentage
                                        ),
                                        color
                                )
                        );
                    }

                    // -------------------------------------------------
                    // Main occupancy summary
                    // -------------------------------------------------

                    totalLabel.setText(
                            formatSize(finalTotalBytes)
                    );

                    subLabel.setText(
                            "across " +
                                    files.size() +
                                    " files"
                    );

                    updated.setText(
                            "Last updated just now"
                    );

                    // -------------------------------------------------
                    // Indexing Activity
                    // -------------------------------------------------

                    setMetricValue(
                            card1,
                            files.size() + " Files",
                            "● Indexed",
                            "Current Firestore index"
                    );

                    // -------------------------------------------------
                    // Active Spaces
                    // -------------------------------------------------

                    setMetricValue(
                            card2,
                            totalSpaceCount + " Spaces",
                            "● Synced",
                            "6 built-in · " +
                                    customSpaceCount +
                                    " custom"
                    );

                    // -------------------------------------------------
                    // Indexed Storage
                    // -------------------------------------------------

                    setMetricValue(
                            card3,
                            formatSize(finalTotalBytes),
                            "● Synced",
                            "Current Firestore index"
                    );

                    // -------------------------------------------------
                    // AI Actions Live
                    // -------------------------------------------------

                    setMetricValue(
                            card4,
                            finalAiActionCount + " Actions",
                            "⚡ Live",
                            finalAiActionCount +
                                    " AI-classified files"
                    );

                    applyPieChartColors(chartData);
                });

            } catch (Exception e) {

                Platform.runLater(() -> {
                    updated.setText(
                            "ⓘ Unable to load dashboard data"
                    );

                    setMetricValue(
                            card1,
                            "Unavailable",
                            "ⓘ Error",
                            "Firestore unavailable"
                    );

                    setMetricValue(
                            card2,
                            "Unavailable",
                            "ⓘ Error",
                            "Firestore unavailable"
                    );

                    setMetricValue(
                            card3,
                            "Unavailable",
                            "ⓘ Error",
                            "Firestore unavailable"
                    );

                    setMetricValue(
                            card4,
                            "Unavailable",
                            "ⓘ Error",
                            "Firestore unavailable"
                    );
                });

                System.out.println(
                        "[Dashboard] Unable to load dashboard data: "
                                + e.getMessage()
                );
            }
        });

        t.setDaemon(true);
        t.setName("OneSpace-Dashboard-Loader");
        t.start();
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    private javafx.scene.Node tableHeaderNode(VBox rows) { return rows.getChildren().get(0); }

    private void setMetricValue(
            HBox card,
            String value,
            String badge
    ) {
        setMetricValue(
                card,
                value,
                badge,
                null
        );
    }

    private void setMetricValue(
            HBox card,
            String value,
            String badge,
            String subText
    ) {
        if (card == null ||
                card.getChildren().isEmpty() ||
                !(card.getChildren().get(0) instanceof VBox)) {
            return;
        }

        VBox content =
                (VBox) card.getChildren().get(0);

        if (content.getChildren().size() > 1 &&
                content.getChildren().get(1) instanceof Label) {

            ((Label) content.getChildren().get(1))
                    .setText(value);
        }

        if (content.getChildren().size() > 2 &&
                content.getChildren().get(2) instanceof HBox) {

            HBox bottom =
                    (HBox) content.getChildren().get(2);

            if (!bottom.getChildren().isEmpty() &&
                    bottom.getChildren().get(0) instanceof Label) {

                ((Label) bottom.getChildren().get(0))
                        .setText(badge);
            }

            if (subText != null &&
                    bottom.getChildren().size() > 1 &&
                    bottom.getChildren().get(1) instanceof Label) {

                ((Label) bottom.getChildren().get(1))
                        .setText(subText);
            }
        }
    }

    private HBox createMetricCard(String iconType, String title, String value, String badgeText, String subText, String accentColor, String bgAccent, String textBadgeColor) {
        Label titleLbl = new Label(title);
        titleLbl.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: " + LIGHT_SECONDARY + ";"
        );

        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(accentColor));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setPrefSize(32, 32); iconPane.setMinSize(32, 32);
        iconPane.setStyle(
                "-fx-background-color: " + bgAccent + ";" +
                "-fx-border-color: " + accentColor + "55;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );

        HBox topRow = new HBox(titleLbl, new Region(), iconPane);
        HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label valLbl = new Label(value);
        valLbl.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: " + WHITE + ";"
        );

        Label subLbl = new Label(subText);
        subLbl.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: " + LIGHT_SECONDARY + ";"
        );

        Label badgeLbl = new Label(badgeText);
        badgeLbl.setStyle(
                "-fx-font-size: 10px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: " + textBadgeColor + ";" +
                "-fx-background-color: " + bgAccent + ";" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 3 8;"
        );

        HBox bottomRow = new HBox(6, badgeLbl, subLbl);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox cardContent = new VBox(8, topRow, valLbl, bottomRow);

        HBox card = new HBox(cardContent);
        HBox.setHgrow(cardContent, Priority.ALWAYS);
        card.setPadding(new Insets(16));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-width: 1.2;" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);"
        );

        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: " + CARD_BG + ";" +
                    "-fx-border-color: #38BDF8;" +
                    "-fx-border-width: 1.2;" +
                    "-fx-border-radius: 20;" +
                    "-fx-background-radius: 20;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 24, 0, 0, 8);"
            );
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(-4);
            tt.play();
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: " + CARD_BG + ";" +
                    "-fx-border-color: " + CARD_BORDER + ";" +
                    "-fx-border-width: 1.2;" +
                    "-fx-border-radius: 20;" +
                    "-fx-background-radius: 20;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);"
            );
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(0);
            tt.play();
        });

        return card;
    }

    private Label createHeaderLabel(String text, double width) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: " + LIGHT_SECONDARY + ";"
        );
        label.setPrefWidth(width);
        return label;
    }

        
    private HBox createSpaceRow(String iconType, String iconHex, String title, String storage, double progress, String percent, String colorHex) {
            SVGPath folderIcon = createIcon(iconType);
            folderIcon.setStroke(Color.web(iconHex));
            folderIcon.setStrokeWidth(2);

            StackPane iconPane = new StackPane(folderIcon);
            iconPane.setPrefSize(28, 28); iconPane.setMinSize(28, 28);
            iconPane.setStyle(
                    "-fx-background-color: " + iconHex + "22;" +
                    "-fx-background-radius: 6;" +
                    "-fx-border-color: " + iconHex + "44;" +
                    "-fx-border-radius: 6;"
            );

            Label spaceName = new Label(title);
            spaceName.setStyle(
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: 700;" +
                    "-fx-text-fill: " + WHITE + ";"
            );

            HBox nameGroup = new HBox(10, iconPane, spaceName);
            nameGroup.setAlignment(Pos.CENTER_LEFT);

            Label sizeLbl = new Label(storage);
            sizeLbl.setStyle(
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: 700;" +
                    "-fx-text-fill: " + WHITE + ";"
            );
            sizeLbl.setAlignment(Pos.CENTER_LEFT);

            ProgressBar bar = new ProgressBar(progress);
            bar.setMaxWidth(Double.MAX_VALUE);
            bar.setPrefHeight(6);
            bar.setStyle(
                    "-fx-accent: " + colorHex + ";" +
                    "-fx-control-inner-background: rgba(13, 22, 38, 0.85);"
            );
            HBox.setHgrow(bar, Priority.ALWAYS);

            Label percentLbl = new Label(percent);
            percentLbl.setStyle(
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: 700;" +
                    "-fx-text-fill: " + LIGHT_SECONDARY + ";"
            );
            percentLbl.setMinWidth(35);
            percentLbl.setAlignment(Pos.CENTER_RIGHT);

            HBox progressGroup = new HBox(10, bar, percentLbl);
            progressGroup.setAlignment(Pos.CENTER_LEFT);

            HBox row = new HBox(nameGroup, sizeLbl, progressGroup);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(6, 8, 6, 8));
            row.setStyle("-fx-background-color: transparent; -fx-background-radius: 8;");

            // Bind column widths to match header ratio (35% / 25% / 40%)
            nameGroup.prefWidthProperty().bind(row.widthProperty().multiply(0.35));
            sizeLbl.prefWidthProperty().bind(row.widthProperty().multiply(0.25));
            progressGroup.prefWidthProperty().bind(row.widthProperty().multiply(0.40));

            row.setOnMouseEntered(e -> {
                row.setStyle("-fx-background-color: rgba(56, 189, 248, 0.08); -fx-border-color: rgba(56, 189, 248, 0.35); -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");
                TranslateTransition tt = new TranslateTransition(Duration.millis(120), row);
                tt.setToX(4);
                tt.play();
            });

            row.setOnMouseExited(e -> {
                row.setStyle("-fx-background-color: transparent; -fx-background-radius: 8;");
                TranslateTransition tt = new TranslateTransition(Duration.millis(120), row);
                tt.setToX(0);
                tt.play();
            });

            return row;
        }

    private void applyPieChartColors(ObservableList<PieChart.Data> data) {
        int i = 0;
        for (PieChart.Data item : data) {
            if (item.getNode() != null) {
                item.getNode().setStyle(
                        "-fx-pie-color: " + CHART_COLORS[i % CHART_COLORS.length] + ";"
                );
            }
            i++;
        } 
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
            case "storage": icon.setContent("M4 6H20 M4 12H20 M4 18H20"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }
}