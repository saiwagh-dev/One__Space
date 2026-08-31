package com.file_handlers.view.userView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.dao.ReminderDAO;
import com.file_handlers.model.FileData;
import com.file_handlers.model.Reminder;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;
import com.google.cloud.Timestamp;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class UserDashboard {
    private final FileDAO fileDAO = new FileDAO();

    private static final String FONT =
            "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";

    private static final String BG_CENTER_CANVAS = "#31435B";

    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_CARD_INNER = "#CADDF2";
    private static final String BORDER_CARD = "#C3D6EC";
    private static final String BG_INPUT = "#EDF3FA";

    private static final String TEXT_DARK = "#0F172A";
    private static final String TEXT_MUTED_DARK = "#334155";
    private static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#94A3B8";

    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String ACCENT_LIGHT_BLUE = "#BFDBFE";
    private static final String DANGER_RED = "#DC2626";

    private static final String[] CHART_COLORS = {
            "#2563EB",
            "#0284C7",
            "#059669",
            "#7C3AED",
            "#475569"
    };

    // Calendar state fields
    private int year = 2026, month = 8;
    private GridPane grid;
    private Button monthBtn, yearBtn;
    private VBox remindersList;
    private Label infoText;

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

        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill:" + TEXT_LIGHT + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", true);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);

        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        aiBtn.setOnAction(e -> LandingPage.showAiAssistantPage());
        collabBtn.setOnAction(e -> LandingPage.showCollaborationPage());
        recentBtn.setOnAction(e -> LandingPage.showRecentPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());
        settingsBtn.setOnAction(e -> LandingPage.showSettingPage());

        VBox navList = new VBox(
                4,
                dashboardBtn,
                spacesBtn,
                searchBtn,
                calendarBtn,
                aiBtn,
                collabBtn,
                recentBtn,
                trashBtn
        );

        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        storageTitle.setStyle("-fx-text-fill:" + TEXT_LIGHT + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-text-fill:" + TEXT_LIGHT + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-text-fill:" + TEXT_MUTED_LIGHT + ";");

        Region storageSpacer = new Region();
        HBox.setHgrow(storageSpacer, Priority.ALWAYS);

        HBox storageValGroup = new HBox(storageVal, storageSpacer, storagePercent);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle(
                "-fx-accent:" + PRIMARY_BLUE + ";" +
                "-fx-control-inner-background:#0E1520;"
        );

        Button manageStorageBtn = new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:#60A5FA;-fx-padding:2 0 0 0;-fx-cursor:hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexPage());

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle(
                "-fx-background-color:" + BG_SIDEBAR_CARD + ";" +
                "-fx-border-color:" + SIDEBAR_BORDER + ";" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;"
        );

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle(
                "-fx-background-color:" + BG_SIDEBAR + ";" +
                "-fx-border-color:" + SIDEBAR_BORDER + ";" +
                "-fx-border-width:0 1 0 0;"
        );

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-font-size:16px;" +
                "-fx-text-fill:" + TEXT_LIGHT + ";" +
                "-fx-cursor:hand;"
        );
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label(initials);
        avatar.setMinSize(34, 34);
        avatar.setPrefSize(34, 34);
        avatar.setMaxSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-background-radius:999px;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-font-size:12px;"
        );

        Label userName = new Label(activeUserName);
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill:" + TEXT_LIGHT + ";");

        Label dropDown = new Label("⌄");
        dropDown.setStyle("-fx-text-fill:" + TEXT_MUTED_LIGHT + ";");

        HBox profileOption = new HBox(8, avatar, userName, dropDown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(5, 8, 5, 8));
        profileOption.setOnMouseClicked(e -> LandingPage.showUserProfilePage());

        HBox profileBox = new HBox(10, bellBtn, profileOption);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(0), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle(
                "-fx-background-color:" + BG_SIDEBAR + ";" +
                "-fx-border-color:" + SIDEBAR_BORDER + ";" +
                "-fx-border-width:0 0 1 0;"
        );

        Label welcomeTitle = new Label("Good afternoon, " + activeUserName);
        welcomeTitle.setStyle(
                "-fx-font-family:" + FONT + ";" +
                "-fx-font-size:24px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + TEXT_LIGHT + ";"
        );

        Label welcomeSub = new Label("Manage your files, spaces and AI-organized content from one place.");
        welcomeSub.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:" + TEXT_MUTED_LIGHT + ";" +
                "-fx-font-weight:500;"
        );

        VBox greetingText = new VBox(4, welcomeTitle, welcomeSub);

        Button addFilebtn = new Button("⛶  Add File");
        addFilebtn.setStyle(
                "-fx-font-family:" + FONT + ";" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:700;" +
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:10;" +
                "-fx-cursor:hand;" +
                "-fx-padding:12 20;"
        );

        addFilebtn.setOnAction(e ->
                LandingPage.setScene(
                        new AddFileData(
                                () -> LandingPage.showUserDashboard()
                        ).getScene()
                )
        );

        AnchorPane greetingHeader = new AnchorPane(greetingText, addFilebtn);
        AnchorPane.setTopAnchor(greetingText, 0.0);
        AnchorPane.setLeftAnchor(greetingText, 0.0);
        AnchorPane.setTopAnchor(addFilebtn, 0.0);
        AnchorPane.setRightAnchor(addFilebtn, 0.0);

        HBox card1 = createMetricCard(
                "📁", "Indexing Activity", "Loading...",
                "● Syncing", "From Firestore",
                "#2563EB", "#CADDF2", "#1D4ED8"
        );

        HBox card2 = createMetricCard(
                "▦", "Active Spaces", "6 Spaces",
                "AI organized", "Personal · College · Office",
                "#0284C7", "#BAE6FD", "#0369A1"
        );

        HBox card3 = createMetricCard(
                "💾", "Indexed Storage", "Loading...",
                "● Syncing", "From Firestore",
                "#059669", "#A7F3D0", "#065F46"
        );

        HBox card4 = createMetricCard(
                "✦", "AI Actions Live", "126 Actions",
                "⚡ Live pipeline", "12 summaries · 8 links",
                "#D97706", "#FDE68A", "#92400E"
        );

        HBox metricsRow = new HBox(14, card1, card2, card3, card4);
        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);
        HBox.setHgrow(card4, Priority.ALWAYS);

        Label cardTitle = new Label("Space Occupancy");
        cardTitle.setStyle(
                "-fx-font-size:17px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + TEXT_DARK + ";"
        );

        Label cardSub = new Label("Overview of file storage across your spaces.");
        cardSub.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:" + TEXT_MUTED_DARK + ";"
        );

        VBox cardHeaderTitles = new VBox(2, cardTitle, cardSub);

        Button viewAllBtn = new Button("View all spaces ›");
        viewAllBtn.setStyle(
                "-fx-background-color:" + BG_CARD_INNER + ";" +
                "-fx-border-color:" + BORDER_CARD + ";" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:" + PRIMARY_BLUE + ";" +
                "-fx-padding:6 14;" +
                "-fx-cursor:hand;"
        );
        viewAllBtn.setOnAction(e -> LandingPage.showUserSpace());

        HBox cardHeader = new HBox(cardHeaderTitles, new Region(), viewAllBtn);
        HBox.setHgrow(cardHeader.getChildren().get(1), Priority.ALWAYS);
        cardHeader.setAlignment(Pos.CENTER_LEFT);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        PieChart chart = new PieChart(pieChartData);
        chart.setLabelsVisible(false);
        chart.setLegendVisible(false);
        chart.setPrefSize(205, 205);
        chart.setMaxSize(205, 205);

        Circle donutHole = new Circle(66, Color.web(BG_CARD));
        Label chartValText = new Label("Loading...");
        chartValText.setStyle("-fx-font-size:18px;-fx-font-weight:700;-fx-text-fill:" + TEXT_DARK + ";");
        Label chartSubText = new Label("from Firestore");
        chartSubText.setStyle("-fx-font-size:11px;-fx-text-fill:" + TEXT_MUTED_DARK + ";");

        VBox chartCenterText = new VBox(2, chartValText, chartSubText);
        chartCenterText.setAlignment(Pos.CENTER);

        StackPane donutChartPane = new StackPane(chart, donutHole, chartCenterText);
        HBox tableHeader = new HBox(
                createHeaderLabel("Space", 200),
                createHeaderLabel("Storage Used", 110),
                createHeaderLabel("Percentage", 140)
        );
        VBox spaceRows = new VBox(11, tableHeader, new Label("Loading..."));

        HBox cardContent = new HBox(28, donutChartPane, spaceRows);
        cardContent.setAlignment(Pos.CENTER_LEFT);

        Label lastUpdated = new Label("🕒  Last updated just now");
        lastUpdated.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:" + TEXT_MUTED_DARK + ";"
        );

        VBox occupancyCard = new VBox(16, cardHeader, cardContent, lastUpdated);
        occupancyCard.setPadding(new Insets(24));
        occupancyCard.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:" + BORDER_CARD + ";" +
                "-fx-border-radius:16;" +
                "-fx-background-radius:16;" +
                "-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.18),16,0,0,6);"
        );

        // --- CALENDAR & REMINDERS COMPONENT ---
        Label calendarTitle = new Label("Calendar & Reminders");
        calendarTitle.setFont(Font.font(FONT, FontWeight.BOLD, 20));
        calendarTitle.setStyle("-fx-text-fill:" + TEXT_LIGHT + ";");

        Label calendarSub = new Label("Keep track of important dates, tasks, and document reminders.");
        calendarSub.setFont(Font.font(FONT, 13));
        calendarSub.setStyle("-fx-text-fill:" + TEXT_MUTED_LIGHT + ";-fx-font-weight:500;");

        VBox calTitleBox = new VBox(4, calendarTitle, calendarSub);

        Button addReminderBtn = new Button("Add Reminder");
        addReminderBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        addReminderBtn.setStyle("-fx-background-color:" + PRIMARY_BLUE + ";-fx-text-fill:#FFFFFF;-fx-background-radius:10;-fx-cursor:hand;-fx-padding:8 18;");
        addReminderBtn.setOnAction(e -> LandingPage.showAddReminderPage());

        HBox calPageHeader = new HBox(calTitleBox, new Region(), addReminderBtn);
        HBox.setHgrow(calPageHeader.getChildren().get(1), Priority.ALWAYS);
        calPageHeader.setAlignment(Pos.CENTER_LEFT);

        monthBtn = new Button();
        yearBtn = new Button();
        styleCalendarHeaderPickerBtn(monthBtn);
        styleCalendarHeaderPickerBtn(yearBtn);
        updateCalendarHeader();

        monthBtn.setOnAction(e -> showMonthPicker());
        yearBtn.setOnAction(e -> showYearPicker());

        HBox monthYearBox = new HBox(4, monthBtn, yearBtn);

        Button prevBtn = createNavButton("‹");
        Button nextBtn = createNavButton("›");

        prevBtn.setOnAction(e -> changeMonth(-1));
        nextBtn.setOnAction(e -> changeMonth(1));

        HBox calendarHeader = new HBox(monthYearBox, new Region(), new HBox(6, prevBtn, nextBtn));
        HBox.setHgrow(calendarHeader.getChildren().get(1), Priority.ALWAYS);
        calendarHeader.setAlignment(Pos.CENTER_LEFT);

        grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setMaxWidth(Double.MAX_VALUE);

        infoText = new Label("Loading reminders...");
        infoText.setFont(Font.font(FONT, FontWeight.MEDIUM, 12));
        infoText.setStyle("-fx-text-fill:" + TEXT_MUTED_DARK + ";");

        HBox infoBox = new HBox(8, new Label("ⓘ"), infoText);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        VBox calendarCard = new VBox(16, calendarHeader, grid, infoBox);
        calendarCard.setPadding(new Insets(24));
        calendarCard.setStyle(createCardStyle());

        VBox calendarSection = new VBox(calendarCard);
        HBox.setHgrow(calendarSection, Priority.ALWAYS);

        Label reminderTitle = new Label("Upcoming Reminders");
        reminderTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        reminderTitle.setStyle("-fx-text-fill:" + TEXT_LIGHT + ";");

        remindersList = new VBox(10);
        remindersList.setPadding(new Insets(4));

        ScrollPane reminderScroll = new ScrollPane(remindersList);
        reminderScroll.setFitToWidth(true);
        reminderScroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        VBox.setVgrow(reminderScroll, Priority.ALWAYS);

        VBox remindersCard = new VBox(reminderScroll);
        remindersCard.setPadding(new Insets(16));
        remindersCard.setMinHeight(410);
        remindersCard.setStyle(createCardStyle());

        VBox remindersSection = new VBox(12, reminderTitle, remindersCard);
        remindersSection.setPrefWidth(350);

        HBox calendarSectionsContainer = new HBox(20, calendarSection, remindersSection);
        HBox.setHgrow(calendarSection, Priority.ALWAYS);

        VBox calendarContainer = new VBox(16, calPageHeader, calendarSectionsContainer);

        loadCalendarData();
        loadDashboardData(pieChartData, chart, chartValText, chartSubText, spaceRows, lastUpdated, card1, card2, card3);

        VBox contentBody = new VBox(
                22,
                greetingHeader,
                metricsRow,
                occupancyCard,
                calendarContainer
        );

        contentBody.setPadding(new Insets(
                24,
                ResponsiveUtil.PAGE_PADDING,
                28,
                ResponsiveUtil.PAGE_PADDING
        ));

        contentBody.setStyle("-fx-background-color:" + BG_CENTER_CANVAS + ";");

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color:" + BG_CENTER_CANVAS + ";" +
                "-fx-background:" + BG_CENTER_CANVAS + ";" +
                "-fx-background-insets:0;" +
                "-fx-padding:0;"
        );

        VBox mainArea = new VBox(topBar, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        mainArea.setStyle("-fx-background-color:" + BG_CENTER_CANVAS + ";");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG_SIDEBAR + ";");
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

    private void loadDashboardData(ObservableList<PieChart.Data> chartData, PieChart chart, Label totalLabel, Label subLabel, VBox rows, Label updated, HBox card1, HBox card2, HBox card3) {
        UserSession session = UserSession.getInstance();
        if (session == null || !UserSession.isLoggedIn() || session.getUid() == null || session.getUid().isBlank()) return;
        Thread t = new Thread(() -> {
            try {
                List<FileData> files = fileDAO.getFileSummaries(session.getUid());
                String[] ids = {"personal", "college", "office", "finance", "entertainment", "other"};
                String[] names = {"Personal", "College", "Office", "Finance", "Entertainment", "Others"};
                long[] totals = new long[ids.length];
                long total = 0;
                for (FileData f : files) {
                    total += f.getFileSize();
                    String id = f.getSpaceId();
                    if (id != null) {
                        for (int i = 0; i < ids.length; i++) {
                            if (ids[i].equalsIgnoreCase(id)) {
                                totals[i] += f.getFileSize();
                                break;
                            }
                        }
                    }
                }
                final long totalBytes = total;
                Platform.runLater(() -> {
                    chartData.clear();
                    rows.getChildren().setAll(tableHeaderNode(rows));
                    for (int i = 0; i < ids.length; i++) {
                        double pct = totalBytes == 0 ? 0 : (totals[i] * 100.0 / totalBytes);
                        chartData.add(new PieChart.Data(names[i], totals[i]));
                        rows.getChildren().add(createSpaceRow("📁", CHART_COLORS[i % CHART_COLORS.length], names[i], formatSize(totals[i]), pct / 100, String.format("%.0f%%", pct), CHART_COLORS[i % CHART_COLORS.length]));
                    }
                    totalLabel.setText(formatSize(totalBytes));
                    subLabel.setText("across " + files.size() + " files");
                    updated.setText("🕒 Last updated just now");
                    setMetricValue(card1, files.size() + " Files", "● Indexed");
                    setMetricValue(card3, formatSize(totalBytes), "● Synced");
                    applyPieChartColors(chartData);
                });
            } catch (Exception e) {
                Platform.runLater(() -> updated.setText("ⓘ Unable to load dashboard data"));
                System.out.println("[Dashboard] Unable to load files: " + e.getMessage());
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    private javafx.scene.Node tableHeaderNode(VBox rows) { return rows.getChildren().get(0); }

    private void setMetricValue(HBox card, String value, String badge) {
        if (card.getChildren().isEmpty() || !(card.getChildren().get(0) instanceof VBox)) return;
        VBox content = (VBox) card.getChildren().get(0);
        if (content.getChildren().size() > 1 && content.getChildren().get(1) instanceof Label) ((Label) content.getChildren().get(1)).setText(value);
        if (content.getChildren().size() > 2 && content.getChildren().get(2) instanceof HBox) {
            HBox bottom = (HBox) content.getChildren().get(2);
            if (!bottom.getChildren().isEmpty() && bottom.getChildren().get(0) instanceof Label) ((Label) bottom.getChildren().get(0)).setText(badge);
        }
    }

    private StackPane createOneSpaceLogo() {
        Image logoImage = new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));

        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane pane = new StackPane(logoView);
        pane.setPrefSize(42, 42);
        pane.setAlignment(Pos.CENTER);

        return pane;
    }

    private Button createSidebarButton(String icon, String label, boolean active) {
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(FONT, 14));

        Label textLbl = new Label(label);
        textLbl.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));

        HBox content = new HBox(12, iconLbl, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 12, 0, 12));

        if (active) {
            btn.setStyle("-fx-background-color:" + PRIMARY_BLUE + ";-fx-background-radius:8;-fx-cursor:hand;");
            iconLbl.setStyle("-fx-text-fill:" + TEXT_LIGHT + ";");
            textLbl.setStyle("-fx-text-fill:" + TEXT_LIGHT + ";");
        } else {
            btn.setStyle("-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;");
            iconLbl.setStyle("-fx-text-fill:" + TEXT_MUTED_LIGHT + ";");
            textLbl.setStyle("-fx-text-fill:" + TEXT_LIGHT + ";");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color:#26354A;-fx-background-radius:8;-fx-cursor:hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;"));
        }

        return btn;
    }

    private HBox createMetricCard(String icon, String title, String value, String badgeText, String subText, String accentColor, String bgAccent, String textBadgeColor) {
        Label titleLbl = new Label(title);
        titleLbl.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + TEXT_MUTED_DARK + ";"
        );

        Label iconLbl = new Label(icon);
        iconLbl.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:" + accentColor + ";"
        );

        Label iconBox = new Label("", iconLbl);
        iconBox.setPrefSize(32, 32);
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setStyle(
                "-fx-background-color:" + bgAccent + ";" +
                "-fx-background-radius:8;"
        );

        HBox topRow = new HBox(titleLbl, new Region(), iconBox);
        HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label valLbl = new Label(value);
        valLbl.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + TEXT_DARK + ";"
        );

        Label subLbl = new Label(subText);
        subLbl.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:" + TEXT_MUTED_DARK + ";"
        );

        Label badgeLbl = new Label(badgeText);
        badgeLbl.setStyle(
                "-fx-font-size:10px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + textBadgeColor + ";" +
                "-fx-background-color:" + bgAccent + ";" +
                "-fx-background-radius:6;" +
                "-fx-padding:3 8;"
        );

        HBox bottomRow = new HBox(6, badgeLbl, subLbl);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox cardContent = new VBox(8, topRow, valLbl, bottomRow);

        HBox card = new HBox(cardContent);
        HBox.setHgrow(cardContent, Priority.ALWAYS);
        card.setPadding(new Insets(16));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:" + BORDER_CARD + ";" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;" +
                "-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.14),12,0,0,4);"
        );

        return card;
    }

    private Label createHeaderLabel(String text, double width) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + TEXT_MUTED_DARK + ";"
        );
        label.setPrefWidth(width);
        return label;
    }

    private HBox createSpaceRow(String icon, String iconHex, String title, String storage, double progress, String percent, String colorHex) {
        Label folderIcon = new Label(icon);
        folderIcon.setPrefSize(24, 24);
        folderIcon.setAlignment(Pos.CENTER);
        folderIcon.setStyle(
                "-fx-background-color:" + iconHex + "22;" +
                "-fx-background-radius:6;" +
                "-fx-text-fill:" + iconHex + ";"
        );

        Label spaceName = new Label(title);
        spaceName.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + TEXT_DARK + ";"
        );

        HBox nameGroup = new HBox(10, folderIcon, spaceName);
        nameGroup.setAlignment(Pos.CENTER_LEFT);
        nameGroup.setPrefWidth(200);

        Label sizeLbl = new Label(storage);
        sizeLbl.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + TEXT_DARK + ";"
        );
        sizeLbl.setPrefWidth(110);

        ProgressBar bar = new ProgressBar(progress);
        bar.setPrefWidth(90);
        bar.setPrefHeight(6);
        bar.setStyle(
                "-fx-accent:" + colorHex + ";" +
                "-fx-control-inner-background:#B6CDE7;"
        );

        Label percentLbl = new Label(percent);
        percentLbl.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + TEXT_MUTED_DARK + ";"
        );
        percentLbl.setPrefWidth(40);

        HBox progressGroup = new HBox(10, bar, percentLbl);
        progressGroup.setAlignment(Pos.CENTER_LEFT);
        progressGroup.setPrefWidth(140);

        return new HBox(nameGroup, sizeLbl, progressGroup);
    }

    private void applyPieChartColors(ObservableList<PieChart.Data> data) {
        int i = 0;
        for (PieChart.Data item : data) {
            if (item.getNode() != null) {
                item.getNode().setStyle(
                        "-fx-pie-color:" + CHART_COLORS[i % CHART_COLORS.length] + ";"
                );
            }
            i++;
        }
    }

    // --- CALENDAR IMPLEMENTATION METHODS ---

    private void loadCalendarData() {
        createCalendarGrid();
        remindersList.getChildren().setAll(new Label("Loading reminders..."));
        infoText.setText("Loading reminders...");

        if (!UserSession.isLoggedIn()) {
            remindersList.getChildren().setAll(emptyLabel("Please log in to view reminders."));
            infoText.setText("No active user session.");
            return;
        }

        String uid = UserSession.getInstance().getUid();

        new Thread(() -> {
            try {
                YearMonth ym = YearMonth.of(year, month);
                LocalDate startDate = ym.atDay(1);
                LocalDate endDate = ym.plusMonths(1).atDay(1);

                List<Reminder> monthReminders = new ReminderDAO().getRemindersForRange(uid, toTimestamp(startDate), toTimestamp(endDate));
                List<Reminder> upcoming = new ReminderDAO().getUpcoming(uid, 8);

                Platform.runLater(() -> {
                    createCalendarGrid(monthReminders);
                    updateUpcoming(upcoming);
                    infoText.setText(monthReminders.isEmpty()
                            ? "No reminders scheduled for this month."
                            : monthReminders.size() + " reminder" + (monthReminders.size() == 1 ? "" : "s") + " scheduled this month.");
                });
            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    createCalendarGrid();
                    remindersList.getChildren().setAll(emptyLabel("Unable to load reminders."));
                    infoText.setText("Could not load reminders.");
                });
            }
        }).start();
    }

    private void createCalendarGrid() {
        createCalendarGrid(Collections.emptyList());
    }

    private void createCalendarGrid(List<Reminder> reminders) {
        grid.getChildren().clear();

        String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

        for (int i = 0; i < 7; i++) {
            Label dayHeader = new Label(days[i]);
            dayHeader.setFont(Font.font(FONT, FontWeight.BOLD, 11));
            dayHeader.setStyle("-fx-text-fill:" + TEXT_MUTED_DARK + ";");
            dayHeader.setPrefSize(82, 28);
            dayHeader.setAlignment(Pos.CENTER);
            grid.add(dayHeader, i, 0);
        }

        Map<LocalDate, List<Reminder>> grouped = reminders.stream()
                .filter(r -> r.getDate() != null)
                .collect(Collectors.groupingBy(r -> toLocalDate(r.getDate())));

        YearMonth ym = YearMonth.of(year, month);
        int daysInMonth = ym.lengthOfMonth();
        int col = ym.atDay(1).getDayOfWeek().getValue() % 7;
        int row = 1;
        LocalDate today = LocalDate.now();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = ym.atDay(day);
            VBox cell = createDateCell(day, today);
            List<Reminder> dayReminders = grouped.getOrDefault(date, Collections.emptyList());

            for (int i = 0; i < Math.min(dayReminders.size(), 2); i++)
                addReminderBadge(cell, dayReminders.get(i));

            if (dayReminders.size() > 2)
                addEventBadge(cell, "+" + (dayReminders.size() - 2) + " more", PRIMARY_BLUE, ACCENT_LIGHT_BLUE);

            grid.add(cell, col, row);

            col++;

            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createDateCell(int day, LocalDate today) {
        boolean isToday = year == today.getYear() && month == today.getMonthValue() && day == today.getDayOfMonth();

        Label dayLabel = new Label(String.valueOf(day));
        dayLabel.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        dayLabel.setStyle("-fx-text-fill:" + (isToday ? PRIMARY_BLUE : TEXT_DARK) + ";");

        VBox cell = new VBox(4, dayLabel);
        cell.setPrefSize(82, 65);
        cell.setPadding(new Insets(6));

        String normal = createDayCellStyle(isToday ? ACCENT_LIGHT_BLUE : BG_CARD_INNER, isToday ? PRIMARY_BLUE : BORDER_CARD);
        String hover = createDayCellStyle(ACCENT_LIGHT_BLUE, PRIMARY_BLUE);

        cell.setStyle(normal);
        cell.setOnMouseEntered(e -> cell.setStyle(hover));
        cell.setOnMouseExited(e -> cell.setStyle(normal));

        LocalDate date = LocalDate.of(year, month, day);
        cell.setOnMouseClicked(e -> showDayEventsWindow(date));

        return cell;
    }

    private void addReminderBadge(VBox cell, Reminder reminder) {
        String color = getPriorityColor(reminder.getPriority());
        String bg = getPriorityBackground(reminder.getPriority());

        Label badge = new Label(getTypeIcon(reminder.getType()) + " " + reminder.getTitle());
        badge.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 9));
        badge.setMaxWidth(Double.MAX_VALUE);
        badge.setEllipsisString("...");
        badge.setStyle("-fx-text-fill:" + color + ";-fx-background-color:" + bg + ";-fx-background-radius:4;-fx-padding:2 4;");

        badge.setOnMouseClicked(e -> {
            e.consume();
            showReminderDetails(reminder);
        });

        cell.getChildren().add(badge);
    }

    private void addEventBadge(VBox cell, String title, String textColor, String bgColor) {
        Label badge = new Label(title);
        badge.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 9));
        badge.setMaxWidth(Double.MAX_VALUE);
        badge.setStyle("-fx-text-fill:" + textColor + ";-fx-background-color:" + bgColor + ";-fx-background-radius:4;-fx-padding:2 4;");
        cell.getChildren().add(badge);
    }

    private void updateUpcoming(List<Reminder> reminders) {
        remindersList.getChildren().clear();

        if (reminders == null || reminders.isEmpty()) {
            remindersList.getChildren().add(emptyLabel("No upcoming reminders."));
            return;
        }

        for (Reminder reminder : reminders)
            remindersList.getChildren().add(createReminderCard(reminder));
    }

    private VBox createReminderCard(Reminder reminder) {
        LocalDate date = toLocalDate(reminder.getDate());
        String dateText = date == null ? "Date unavailable" : date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

        String time = reminder.getTime();

        if (time == null || time.isBlank())
            time = "Time not specified";

        String accent = getPriorityColor(reminder.getPriority());

        Label dateLbl = new Label(dateText);
        dateLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        dateLbl.setStyle("-fx-text-fill:" + accent + ";");

        Label titleLbl = new Label(getTypeIcon(reminder.getType()) + " " + reminder.getTitle());
        titleLbl.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        titleLbl.setStyle("-fx-text-fill:" + TEXT_DARK + ";");
        titleLbl.setWrapText(true);

        Label subLbl = new Label(time + " • " + safe(reminder.getPriority(), "Medium") + " priority");
        subLbl.setFont(Font.font(FONT, FontWeight.MEDIUM, 11));
        subLbl.setStyle("-fx-text-fill:" + TEXT_MUTED_DARK + ";");

        VBox content = new VBox(2, dateLbl, titleLbl, subLbl);

        if (reminder.getLinkedFileName() != null && !reminder.getLinkedFileName().isBlank()) {
            Label fileLbl = new Label("📄 " + reminder.getLinkedFileName());
            fileLbl.setFont(Font.font(FONT, 11));
            fileLbl.setStyle("-fx-text-fill:" + PRIMARY_BLUE + ";");
            fileLbl.setWrapText(true);
            content.getChildren().add(fileLbl);
        }

        VBox card = new VBox(content);
        card.setPadding(new Insets(10, 12, 10, 12));
        card.setStyle("-fx-background-color:" + BG_CARD_INNER + ";-fx-border-color:" + BORDER_CARD + " " + BORDER_CARD + " " + BORDER_CARD + " " + accent + ";-fx-border-radius:10;-fx-background-radius:10;-fx-border-width:1 1 1 4;");
        card.setOnMouseClicked(e -> showReminderDetails(reminder));

        return card;
    }

    private void showDayEventsWindow(LocalDate date) {
        Stage stage = new Stage();
        stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        stage.setTitle("Reminders");

        Label title = new Label("Reminders for " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
        title.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill:" + TEXT_DARK + ";");

        Label subtitle = new Label("Reminders scheduled for this date.");
        subtitle.setFont(Font.font(FONT, 12));
        subtitle.setStyle("-fx-text-fill:" + TEXT_MUTED_DARK + ";");

        VBox list = new VBox(12);
        list.setPadding(new Insets(4));

        Label loading = new Label("Loading...");
        loading.setStyle("-fx-text-fill:" + TEXT_MUTED_DARK + ";");
        list.getChildren().add(loading);

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        Button close = new Button("Close");
        close.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        close.setPrefWidth(100);
        close.setStyle("-fx-background-color:" + BG_INPUT + ";-fx-text-fill:" + TEXT_DARK + ";-fx-border-color:" + BORDER_CARD + ";-fx-border-radius:8;-fx-background-radius:8;-fx-cursor:hand;");
        close.setOnAction(e -> stage.close());

        VBox layout = new VBox(16, new VBox(4, title, subtitle), scroll, new HBox(close));
        layout.setPadding(new Insets(24));
        layout.setStyle("-fx-background-color:" + BG_CARD + ";");

        stage.setScene(new Scene(layout, 500, 400));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

        if (!UserSession.isLoggedIn()) {
            list.getChildren().setAll(emptyLabel("Please log in."));
            return;
        }

        new Thread(() -> {
            try {
                LocalDate end = date.plusDays(1);

                List<Reminder> reminders = new ReminderDAO().getRemindersForRange(
                        UserSession.getInstance().getUid(),
                        toTimestamp(date),
                        toTimestamp(end)
                );

                Platform.runLater(() -> {
                    list.getChildren().clear();

                    if (reminders.isEmpty()) {
                        list.getChildren().add(emptyLabel("No reminders scheduled for this day."));
                        return;
                    }

                    for (Reminder reminder : reminders)
                        list.getChildren().add(createModalReminderCard(reminder));
                });
            } catch (Exception e) {
                Platform.runLater(() -> list.getChildren().setAll(emptyLabel("Unable to load reminders.")));
            }
        }).start();
    }

    private VBox createModalReminderCard(Reminder reminder) {
        String accent = getPriorityColor(reminder.getPriority());

        Label title = new Label(getTypeIcon(reminder.getType()) + " " + reminder.getTitle());
        title.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        title.setStyle("-fx-text-fill:" + TEXT_DARK + ";");

        Label meta = new Label((reminder.getTime() == null ? "" : reminder.getTime()) + " • " + safe(reminder.getPriority(), "Medium") + " priority");
        meta.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        meta.setStyle("-fx-text-fill:" + accent + ";");

        Label desc = new Label(reminder.getDescription() == null || reminder.getDescription().isBlank() ? "No description" : reminder.getDescription());
        desc.setFont(Font.font(FONT, 12));
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill:" + TEXT_MUTED_DARK + ";");

        VBox card = new VBox(6, title, meta, desc);

        if (reminder.getLinkedFileName() != null && !reminder.getLinkedFileName().isBlank()) {
            Label file = new Label("📄 " + reminder.getLinkedFileName());
            file.setFont(Font.font(FONT, 11));
            file.setStyle("-fx-text-fill:" + PRIMARY_BLUE + ";");
            card.getChildren().add(file);
        }

        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color:" + BG_CARD_INNER + ";-fx-border-color:" + BORDER_CARD + " " + BORDER_CARD + " " + BORDER_CARD + " " + accent + ";-fx-border-radius:10;-fx-background-radius:10;-fx-border-width:1 1 1 4;");
        return card;
    }

    private void showReminderDetails(Reminder reminder) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reminder");
        alert.setHeaderText(reminder.getTitle());

        StringBuilder text = new StringBuilder();
        text.append("Type: ").append(safe(reminder.getType(), "Reminder")).append("\n");
        text.append("Date: ").append(toLocalDate(reminder.getDate())).append("\n");
        text.append("Time: ").append(safe(reminder.getTime(), "Not specified")).append("\n");
        text.append("Repeat: ").append(safe(reminder.getRepeat(), "Does not repeat")).append("\n");
        text.append("Priority: ").append(safe(reminder.getPriority(), "Medium")).append("\n");

        if (reminder.getDescription() != null && !reminder.getDescription().isBlank())
            text.append("\n").append(reminder.getDescription());

        if (reminder.getLinkedFileName() != null && !reminder.getLinkedFileName().isBlank())
            text.append("\n\nFile: ").append(reminder.getLinkedFileName());

        alert.setContentText(text.toString());

        ButtonType deleteButton = new ButtonType("Delete", ButtonBar.ButtonData.LEFT);
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(deleteButton, closeButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == deleteButton)
            deleteReminder(reminder);
    }

    private void deleteReminder(Reminder reminder) {
        if (!UserSession.isLoggedIn() || reminder.getId() == null || reminder.getId().isBlank()) {
            alert(Alert.AlertType.ERROR, "Delete Failed", "Unable to identify this reminder.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Reminder");
        confirm.setHeaderText("Delete \"" + reminder.getTitle() + "\"?");
        confirm.setContentText("This reminder will be permanently removed.");

        ButtonType delete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        confirm.getButtonTypes().setAll(delete, cancel);

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isEmpty() || result.get() != delete)
            return;

        new Thread(() -> {
            try {
                new ReminderDAO().deleteReminder(
                        UserSession.getInstance().getUid(),
                        reminder.getId()
                );

                Platform.runLater(() -> {
                    loadCalendarData();
                    alert(Alert.AlertType.INFORMATION, "Reminder Deleted", "The reminder was deleted successfully.");
                });
            } catch (Exception e) {
                Platform.runLater(() -> alert(
                        Alert.AlertType.ERROR,
                        "Delete Failed",
                        e.getMessage() == null ? "Unable to delete reminder." : e.getMessage()
                ));
            }
        }).start();
    }

    private void alert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Timestamp toTimestamp(LocalDate date) {
        return Timestamp.of(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    private LocalDate toLocalDate(Timestamp timestamp) {
        if (timestamp == null) return null;
        return timestamp.toSqlTimestamp().toLocalDateTime().toLocalDate();
    }

    private String getTypeIcon(String type) {
        if (type == null) return "🔔";

        String t = type.toLowerCase();

        if (t.contains("document")) return "📄";
        if (t.contains("task")) return "✓";
        if (t.contains("event")) return "📅";
        if (t.contains("deadline")) return "⏰";

        return "🔔";
    }

    private String getPriorityColor(String priority) {
        if (priority == null) return PRIMARY_BLUE;

        switch (priority.toLowerCase()) {
            case "high":
                return DANGER_RED;
            case "low":
                return "#059669";
            default:
                return "#D97706";
        }
    }

    private String getPriorityBackground(String priority) {
        if (priority == null) return ACCENT_LIGHT_BLUE;

        switch (priority.toLowerCase()) {
            case "high":
                return "#FEE2E2";
            case "low":
                return "#A7F3D0";
            default:
                return "#FDE68A";
        }
    }

    private String safe(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private Label emptyLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setFont(Font.font(FONT, FontWeight.MEDIUM, 12));
        label.setStyle("-fx-text-fill:" + TEXT_MUTED_DARK + ";");
        return label;
    }

    private void changeMonth(int amount) {
        month += amount;

        if (month < 1) {
            month = 12;
            year--;
        } else if (month > 12) {
            month = 1;
            year++;
        }

        updateCalendarHeader();
        loadCalendarData();
    }

    private void updateCalendarHeader() {
        monthBtn.setText(Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        yearBtn.setText(String.valueOf(year));
    }

    private void showMonthPicker() {
        Popup popup = new Popup();
        VBox box = createPickerPopupBox();

        for (int i = 1; i <= 12; i++) {
            final int selectedMonth = i;
            Button button = new Button(Month.of(i).getDisplayName(TextStyle.FULL, Locale.ENGLISH));

            applyPickerButtonStyle(button, i == month);

            button.setOnAction(e -> {
                month = selectedMonth;
                popup.hide();
                updateCalendarHeader();
                loadCalendarData();
            });

            box.getChildren().add(button);
        }

        popup.getContent().add(box);
        showPopupRelativeToControl(popup, monthBtn);
    }

    private void showYearPicker() {
        Popup popup = new Popup();
        VBox box = createPickerPopupBox();

        for (int y = year - 5; y <= year + 5; y++) {
            final int selectedYear = y;
            Button button = new Button(String.valueOf(y));

            applyPickerButtonStyle(button, y == year);

            button.setOnAction(e -> {
                year = selectedYear;
                popup.hide();
                updateCalendarHeader();
                loadCalendarData();
            });

            box.getChildren().add(button);
        }

        popup.getContent().add(box);
        showPopupRelativeToControl(popup, yearBtn);
    }

    private VBox createPickerPopupBox() {
        VBox box = new VBox(4);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color:" + BG_CARD + ";-fx-border-color:" + BORDER_CARD + ";-fx-border-radius:10;-fx-background-radius:10;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.2),10,0,0,4);");
        return box;
    }

    private void applyPickerButtonStyle(Button b, boolean selected) {
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(7, 12, 7, 12));
        b.setFont(Font.font(FONT, selected ? FontWeight.BOLD : FontWeight.MEDIUM, 12));

        if (selected)
            b.setStyle("-fx-background-color:" + ACCENT_LIGHT_BLUE + ";-fx-text-fill:" + PRIMARY_BLUE + ";-fx-background-radius:6;-fx-cursor:hand;");
        else {
            b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + TEXT_DARK + ";-fx-background-radius:6;-fx-cursor:hand;");
            b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:" + BG_CARD_INNER + ";-fx-text-fill:" + TEXT_DARK + ";-fx-background-radius:6;-fx-cursor:hand;"));
            b.setOnMouseExited(e -> b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + TEXT_DARK + ";-fx-background-radius:6;-fx-cursor:hand;"));
        }
    }

    private void showPopupRelativeToControl(Popup popup, Control control) {
        Point2D pos = control.localToScreen(0, control.getHeight());
        popup.setAutoHide(true);
        popup.show(control, pos.getX(), pos.getY());
    }

    private void styleCalendarHeaderPickerBtn(Button b) {
        b.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + TEXT_DARK + ";-fx-cursor:hand;-fx-padding:4 6;");
    }

    private Button createNavButton(String text) {
        Button b = new Button(text);
        b.setPrefSize(34, 34);
        b.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        b.setStyle("-fx-background-color:" + BG_INPUT + ";-fx-text-fill:" + TEXT_DARK + ";-fx-border-color:" + BORDER_CARD + ";-fx-border-radius:8;-fx-background-radius:8;-fx-cursor:hand;");
        return b;
    }

    private String createCardStyle() {
        return "-fx-background-color:" + BG_CARD + ";-fx-border-color:" + BORDER_CARD + ";-fx-border-radius:16;-fx-background-radius:16;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.18),16,0,0,6);";
    }

    private String createDayCellStyle(String bg, String border) {
        return "-fx-background-color:" + bg + ";-fx-border-color:" + border + ";-fx-border-radius:8;-fx-background-radius:8;-fx-cursor:hand;";
    }
}