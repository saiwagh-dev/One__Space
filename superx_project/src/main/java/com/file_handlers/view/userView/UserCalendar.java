package com.file_handlers.view.userView;

import com.file_handlers.dao.ReminderDAO;
import com.file_handlers.model.Reminder;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;
import com.google.cloud.Timestamp;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Point2D;
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
import javafx.stage.Stage;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class UserCalendar {

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
    private static final String ACCENT_LIGHT_BLUE = "rgba(56, 189, 248, 0.15)";
    private static final String DANGER_RED = "#EF4444";

    private int year = 2026, month = 8;
    private GridPane grid;
    private Button monthBtn, yearBtn;
    private VBox remindersList;
    private Label infoText;

    public Scene getCalendarPageScene() {
        String activeUserName = "User", initials = "U";

        if (UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null && !UserSession.getInstance().getDisplayName().trim().isEmpty()) {
            String name = UserSession.getInstance().getDisplayName().trim();
            activeUserName = name.split("\\s+")[0];
            initials = activeUserName.substring(0, 1).toUpperCase();
        }

        VBox sidebar = createSidebar();

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button bellBtn = new Button();
        bellBtn.setGraphic(bellIcon);
        bellBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());

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
                Point2D point = profileOption.localToScreen(0, profileOption.getHeight() + 6);
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
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        Label pageTitle = new Label("Calendar & Reminders");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 26));
        pageTitle.setStyle("-fx-text-fill: " + WHITE + ";");

        Label pageDesc = new Label("Keep track of important dates, tasks, and document reminders.");
        pageDesc.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        pageDesc.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox titleBox = new VBox(4, pageTitle, pageDesc);

        Button addReminderBtn = new Button("+   Add Reminder");
        addReminderBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        addReminderBtn.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: #FFFFFF; -fx-background-radius: 10; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 10; -fx-border-width: 1; -fx-cursor: hand; -fx-padding: 8 18; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.45), 10, 0, 0, 2);");
        addReminderBtn.setOnAction(e -> LandingPage.showAddReminderPage());

        HBox pageHeader = new HBox(titleBox, new Region(), addReminderBtn);
        HBox.setHgrow(pageHeader.getChildren().get(1), Priority.ALWAYS);
        pageHeader.setAlignment(Pos.CENTER_LEFT);

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

        VBox calendarCard = new VBox(16, calendarHeader, grid);
        calendarCard.setPadding(new Insets(24));
        calendarCard.setStyle(createCardStyle());

        VBox calendarSection = new VBox(calendarCard);
        HBox.setHgrow(calendarSection, Priority.ALWAYS);

        Label reminderTitle = new Label("Upcoming Reminders");
        reminderTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        reminderTitle.setStyle("-fx-text-fill: " + WHITE + ";");

        remindersList = new VBox(10);
        remindersList.setPadding(new Insets(4));

        ScrollPane reminderScroll = new ScrollPane(remindersList);
        reminderScroll.setFitToWidth(true);
        reminderScroll.setFitToHeight(true);
        reminderScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        reminderScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        reminderScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        VBox.setVgrow(reminderScroll, Priority.ALWAYS);

        VBox remindersCard = new VBox(reminderScroll);
        remindersCard.setPadding(new Insets(16));
        remindersCard.setMinHeight(410);
        remindersCard.setStyle(createCardStyle());

        VBox remindersSection = new VBox(12, reminderTitle, remindersCard);
        remindersSection.setPrefWidth(350);

        HBox sectionsContainer = new HBox(20, calendarSection, remindersSection);
        HBox.setHgrow(calendarSection, Priority.ALWAYS);

        infoText = new Label("Loading reminders...");
        infoText.setFont(Font.font(FONT, FontWeight.MEDIUM, 12));
        infoText.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        SVGPath infoIcon = createIcon("bell");
        infoIcon.setStroke(Color.web("#38BDF8"));
        infoIcon.setStrokeWidth(2);

        HBox infoBox = new HBox(8, infoIcon, infoText);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        calendarCard.getChildren().add(infoBox);

        VBox contentBody = new VBox(22, pageHeader, sectionsContainer);
        contentBody.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        contentBody.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        loadCalendarData();

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

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + WHITE + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("dashboard", "Dashboard", false, e -> LandingPage.showUserDashboard());
        Button spacesBtn = createSidebarButton("files", "Spaces", false, e -> LandingPage.showUserSpace());
        Button searchBtn = createSidebarButton("search", "Search", false, e -> LandingPage.showUserSearch());
        Button calendarBtn = createSidebarButton("calendar", "Calendar", true, e -> LandingPage.showCalendarPage());
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

        ProgressBar sidebarProgress = new ProgressBar(.64);
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

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        return sidebar;
    }

    private Button createSidebarButton(String iconType, String label, boolean active, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label textLbl = new Label(label);
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
            dayHeader.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
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
                addEventBadge(cell, "+" + (dayReminders.size() - 2) + " more", "#38BDF8", ACCENT_LIGHT_BLUE);

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
        dayLabel.setStyle("-fx-text-fill: " + (isToday ? "#38BDF8" : WHITE) + ";");

        VBox cell = new VBox(4, dayLabel);
        cell.setPrefSize(82, 65);
        cell.setPadding(new Insets(6));

        String normal = createDayCellStyle(isToday ? ACCENT_LIGHT_BLUE : CARD_BG_INNER, isToday ? "#38BDF8" : "rgba(255, 255, 255, 0.08)");
        String hover = createDayCellStyle(ACCENT_LIGHT_BLUE, "#38BDF8");

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
        badge.setStyle("-fx-text-fill: " + color + "; -fx-background-color: " + bg + "; -fx-background-radius: 4; -fx-padding: 2 4;");

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
        badge.setStyle("-fx-text-fill: " + textColor + "; -fx-background-color: " + bgColor + "; -fx-background-radius: 4; -fx-padding: 2 4;");
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
        dateLbl.setStyle("-fx-text-fill: " + accent + ";");

        Label titleLbl = new Label(getTypeIcon(reminder.getType()) + " " + reminder.getTitle());
        titleLbl.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        titleLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        titleLbl.setWrapText(true);

        Label subLbl = new Label(time + " • " + safe(reminder.getPriority(), "Medium") + " priority");
        subLbl.setFont(Font.font(FONT, FontWeight.MEDIUM, 11));
        subLbl.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox content = new VBox(2, dateLbl, titleLbl, subLbl);

        if (reminder.getLinkedFileName() != null && !reminder.getLinkedFileName().isBlank()) {
            Label fileLbl = new Label("📄 " + reminder.getLinkedFileName());
            fileLbl.setFont(Font.font(FONT, 11));
            fileLbl.setStyle("-fx-text-fill: #38BDF8;");
            fileLbl.setWrapText(true);
            content.getChildren().add(fileLbl);
        }

        VBox card = new VBox(content);
        card.setPadding(new Insets(10, 12, 10, 12));
        card.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08) rgba(255, 255, 255, 0.08) rgba(255, 255, 255, 0.08) " + accent + "; -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-width: 1 1 1 4; -fx-cursor: hand;");
        card.setOnMouseClicked(e -> showReminderDetails(reminder));

        return card;
    }

    private void showDayEventsWindow(LocalDate date) {
        Stage stage = new Stage();
        stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        stage.setTitle("Reminders");

        Label title = new Label("Reminders for " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
        title.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label subtitle = new Label("Reminders scheduled for this date.");
        subtitle.setFont(Font.font(FONT, 12));
        subtitle.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox list = new VBox(12);
        list.setPadding(new Insets(4));

        Label loading = new Label("Loading...");
        loading.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
        list.getChildren().add(loading);

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        Button close = new Button("Close");
        close.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        close.setPrefWidth(100);
        close.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        close.setOnAction(e -> stage.close());

        VBox layout = new VBox(16, new VBox(4, title, subtitle), scroll, new HBox(close));
        layout.setPadding(new Insets(24));
        layout.setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

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
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label meta = new Label((reminder.getTime() == null ? "" : reminder.getTime()) + " • " + safe(reminder.getPriority(), "Medium") + " priority");
        meta.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        meta.setStyle("-fx-text-fill: " + accent + ";");

        Label desc = new Label(reminder.getDescription() == null || reminder.getDescription().isBlank() ? "No description" : reminder.getDescription());
        desc.setFont(Font.font(FONT, 12));
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox card = new VBox(6, title, meta, desc);

        if (reminder.getLinkedFileName() != null && !reminder.getLinkedFileName().isBlank()) {
            Label file = new Label("📄 " + reminder.getLinkedFileName());
            file.setFont(Font.font(FONT, 11));
            file.setStyle("-fx-text-fill: #38BDF8;");
            card.getChildren().add(file);
        }

        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08) rgba(255, 255, 255, 0.08) rgba(255, 255, 255, 0.08) " + accent + "; -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-width: 1 1 1 4;");
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
        if (priority == null) return "#38BDF8";

        switch (priority.toLowerCase()) {
            case "high": return DANGER_RED;
            case "low": return "#34D399";
            default: return "#FBBF24";
        }
    }

    private String getPriorityBackground(String priority) {
        if (priority == null) return ACCENT_LIGHT_BLUE;

        switch (priority.toLowerCase()) {
            case "high": return "rgba(239, 68, 68, 0.15)";
            case "low": return "rgba(16, 185, 129, 0.15)";
            default: return "rgba(245, 158, 11, 0.15)";
        }
    }

    private String safe(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private Label emptyLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setFont(Font.font(FONT, FontWeight.MEDIUM, 12));
        label.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
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
        box.setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 16, 0, 0, 8);");
        return box;
    }

    private void applyPickerButtonStyle(Button b, boolean selected) {
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(7, 12, 7, 12));
        b.setFont(Font.font(FONT, selected ? FontWeight.BOLD : FontWeight.MEDIUM, 12));

        if (selected)
            b.setStyle("-fx-background-color: rgba(37, 99, 235, 0.2); -fx-text-fill: #38BDF8; -fx-background-radius: 6; -fx-cursor: hand;");
        else {
            b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + WHITE + "; -fx-background-radius: 6; -fx-cursor: hand;");
            b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-text-fill: " + WHITE + "; -fx-background-radius: 6; -fx-cursor: hand;"));
            b.setOnMouseExited(e -> b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + WHITE + "; -fx-background-radius: 6; -fx-cursor: hand;"));
        }
    }

    private void showPopupRelativeToControl(Popup popup, Control control) {
        Point2D pos = control.localToScreen(0, control.getHeight());
        popup.setAutoHide(true);
        popup.show(control, pos.getX(), pos.getY());
    }

    private void styleCalendarHeaderPickerBtn(Button b) {
        b.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + WHITE + "; -fx-cursor: hand; -fx-padding: 4 6;");
    }

    private Button createNavButton(String text) {
        Button b = new Button(text);
        b.setPrefSize(34, 34);
        b.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        b.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        return b;
    }

    private String createCardStyle() {
        return "-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 16; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);";
    }

    private String createDayCellStyle(String bg, String border) {
        return "-fx-background-color: " + bg + "; -fx-border-color: " + border + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;";
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
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }
}