package com.file_handlers.view.userView;

import com.file_handlers.dao.ReminderDAO;
import com.file_handlers.model.Reminder;
import com.file_handlers.model.UserSession;
import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.view.LandingPage;
import com.google.cloud.Timestamp;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Point2D;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class UserCalendar {

    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String SIDEBAR_BG = "#070C16", SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BG_INNER = "linear-gradient(to bottom right, rgba(13, 22, 38, 0.9), rgba(8, 14, 26, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)", INPUT_BG = "rgba(13, 22, 38, 0.85)";
    private static final String WHITE = "#FFFFFF", LIGHT_SECONDARY = "#94A3B8", BLUE = "#2563EB", ACCENT_LIGHT_BLUE = "rgba(56, 189, 248, 0.15)", DANGER_RED = "#EF4444";
    private static final String GOLD = "#F59E0B", GOLD_BG = "rgba(245, 158, 11, 0.18)";

    private int year = 2026, month = 8;
    private GridPane grid;
    private Button monthBtn, yearBtn;
    private VBox remindersList;
    private Label infoText;

    public Scene getCalendarPageScene() {
        String activeUserName = "User", initials = "U";
        if (UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null && !UserSession.getInstance().getDisplayName().trim().isEmpty()) {
            activeUserName = UserSession.getInstance().getDisplayName().trim().split("\\s+")[0];
            initials = activeUserName.substring(0, 1).toUpperCase();
        }

        VBox sidebar = createSidebar();

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button bellBtn = new Button("", bellIcon);
        bellBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());
        applyHoverAnimation(bellBtn, 1.08, 0);

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34); avatar.setMinSize(34, 34); avatar.setMaxSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12)); avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");
        applyHoverAnimation(avatar, 1.15, 0);

        Label userName = new Label(activeUserName); userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13)); userName.setStyle("-fx-text-fill: " + WHITE + ";");
        Label dropDown = new Label("⌄"); dropDown.setFont(Font.font(FONT, FontWeight.NORMAL, 12)); dropDown.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        HBox profileOption = new HBox(8, avatar, userName, dropDown);
        profileOption.setAlignment(Pos.CENTER); profileOption.setPadding(new Insets(4, 12, 4, 6));
        profileOption.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");
        applyHoverAnimation(profileOption, 1.04, 0);

        Popup userDropdownPopup = new Popup(); userDropdownPopup.setAutoHide(true);
        Button profileDropdownBtn = createDropdownBtn("👥   Profile", "#F59E0B", e -> { userDropdownPopup.hide(); Platform.runLater(LandingPage::showUserProfilePage); });
        Button settingsDropdownBtn = createDropdownBtn("⚙   Settings", "#38BDF8", e -> { userDropdownPopup.hide(); Platform.runLater(LandingPage::showSettingPage); });
        Button logoutDropdownBtn = createDropdownBtn("↳   Logout", "#F87171", e -> { userDropdownPopup.hide(); UserSession.clearSession(); Platform.runLater(LandingPage::showUserLoginPage); });

        Separator dropdownSeparator = new Separator(); dropdownSeparator.setStyle("-fx-background-color: #1E293B; -fx-padding: 4 0;");
        VBox dropdownContainer = new VBox(4, profileDropdownBtn, settingsDropdownBtn, dropdownSeparator, logoutDropdownBtn);
        dropdownContainer.setPadding(new Insets(8)); dropdownContainer.setPrefWidth(180);
        dropdownContainer.setStyle("-fx-background-color: #0A121E; -fx-border-color: #1E2D42; -fx-border-width: 1px; -fx-border-radius: 12px; -fx-background-radius: 12px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 16, 0, 0, 8);");
        userDropdownPopup.getContent().add(dropdownContainer);

        profileOption.setOnMouseClicked(e -> {
            if (userDropdownPopup.isShowing()) userDropdownPopup.hide();
            else userDropdownPopup.show(profileOption, profileOption.localToScreen(0, profileOption.getHeight() + 6).getX(), profileOption.localToScreen(0, profileOption.getHeight() + 6).getY());
        });

        HBox topBar = new HBox(20, new Region(), new HBox(10, bellBtn, profileOption));
        HBox.setHgrow(topBar.getChildren().get(0), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        Button addReminderBtn = new Button("+   Add Reminder");
        addReminderBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        addReminderBtn.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: #FFFFFF; -fx-background-radius: 10; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 10; -fx-border-width: 1; -fx-cursor: hand; -fx-padding: 8 18; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.45), 10, 0, 0, 2);");
        addReminderBtn.setOnAction(e -> LandingPage.showAddReminderPage());
        applyHoverAnimation(addReminderBtn, 1.05, 0);

        HBox pageHeader = new HBox(new VBox(4, label("Calendar & Reminders", 26, FontWeight.BOLD, WHITE), label("Keep track of important dates, tasks, and document reminders.", 13, FontWeight.MEDIUM, LIGHT_SECONDARY)), new Region(), addReminderBtn);
        HBox.setHgrow(pageHeader.getChildren().get(1), Priority.ALWAYS);
        pageHeader.setAlignment(Pos.CENTER_LEFT);

        monthBtn = new Button(); yearBtn = new Button();
        styleCalendarHeaderPickerBtn(monthBtn); styleCalendarHeaderPickerBtn(yearBtn);
        updateCalendarHeader();
        monthBtn.setOnAction(e -> showMonthPicker()); yearBtn.setOnAction(e -> showYearPicker());

        Button prevBtn = createNavButton("‹"), nextBtn = createNavButton("›");
        prevBtn.setOnAction(e -> changeMonth(-1)); nextBtn.setOnAction(e -> changeMonth(1));

        HBox calendarHeader = new HBox(new HBox(4, monthBtn, yearBtn), new Region(), new HBox(6, prevBtn, nextBtn));
        HBox.setHgrow(calendarHeader.getChildren().get(1), Priority.ALWAYS);
        calendarHeader.setAlignment(Pos.CENTER_LEFT);

        grid = new GridPane(); grid.setHgap(8); grid.setVgap(8); grid.setMaxWidth(Double.MAX_VALUE);
        infoText = new Label("Loading reminders...");
        infoText.setFont(Font.font(FONT, FontWeight.MEDIUM, 12)); infoText.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        SVGPath infoIcon = createIcon("bell");
        infoIcon.setStroke(Color.web("#38BDF8")); infoIcon.setStrokeWidth(2);

        VBox calendarCard = new VBox(16, calendarHeader, grid, new HBox(8, infoIcon, infoText));
        calendarCard.setPadding(new Insets(24));
        calendarCard.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 16; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");

        remindersList = new VBox(10); remindersList.setPadding(new Insets(4));
        ScrollPane reminderScroll = new ScrollPane(remindersList);
        reminderScroll.setFitToWidth(true); reminderScroll.setFitToHeight(true);
        reminderScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); reminderScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        reminderScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");

        VBox remindersCard = new VBox(reminderScroll);
        remindersCard.setPadding(new Insets(16)); remindersCard.setMinHeight(410);
        remindersCard.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 16; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");

        HBox sectionsContainer = new HBox(20, new VBox(calendarCard), new VBox(12, label("Upcoming Reminders", 17, FontWeight.BOLD, WHITE), remindersCard));
        HBox.setHgrow(sectionsContainer.getChildren().get(0), Priority.ALWAYS);
        ((VBox) sectionsContainer.getChildren().get(1)).setPrefWidth(350);

        VBox contentBody = new VBox(22, pageHeader, sectionsContainer);
        contentBody.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        contentBody.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true); scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(sidebar); root.setCenter(mainArea);
        loadCalendarData();
        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private Button createDropdownBtn(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> act) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE); b.setAlignment(Pos.CENTER_LEFT);
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + color + "; -fx-font-size: 14px; -fx-font-family: " + FONT + "; -fx-padding: 8 12; -fx-cursor: hand;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #1E293B; -fx-text-fill: " + color + "; -fx-font-size: 14px; -fx-font-family: " + FONT + "; -fx-padding: 8 12; -fx-cursor: hand; -fx-background-radius: 6;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + color + "; -fx-font-size: 14px; -fx-font-family: " + FONT + "; -fx-padding: 8 12; -fx-cursor: hand;"));
        b.setOnAction(act);
        return b;
    }

    private void applyHoverAnimation(Node node, double scaleTo, double translateY) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
            st.setToX(scaleTo); st.setToY(scaleTo); st.play();
            if (translateY != 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(140), node);
                tt.setToY(translateY); tt.play();
            }
        });
        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
            st.setToX(1.0); st.setToY(1.0); st.play();
            if (translateY != 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(140), node);
                tt.setToY(0); tt.play();
            }
        });
    }

    private VBox createSidebar() {
        Image logoImage = new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(42); logoView.setFitHeight(42); logoView.setPreserveRatio(true);

        StackPane logoIcon = new StackPane(logoView);
        logoIcon.setPrefSize(42, 42); logoIcon.setAlignment(Pos.CENTER);
        applyHoverAnimation(logoIcon, 1.1, 0);

        HBox logoHeader = new HBox(10, logoIcon, label("OneSpace", 19, FontWeight.BOLD, WHITE));
        logoHeader.setAlignment(Pos.CENTER_LEFT);
        VBox logoBox = new VBox(4, logoHeader); logoBox.setPadding(new Insets(0, 0, 18, 6));

        VBox navList = new VBox(4,
                createSidebarButton("dashboard", "Dashboard", false, e -> LandingPage.showUserDashboard()),
                createSidebarButton("files", "Spaces", false, e -> LandingPage.showUserSpace()),
                createSidebarButton("search", "Search", false, e -> LandingPage.showUserSearch()),
                createSidebarButton("calendar", "Calendar", true, e -> LandingPage.showCalendarPage()),
                createSidebarButton("ai", "AI Assistant", false, e -> LandingPage.showAiAssistantPage()),
                createSidebarButton("collaboration", "Collaboration", false, e -> LandingPage.showCollaborationPage()),
                createSidebarButton("recent", "Recent", false, e -> LandingPage.showRecentPage()),
                createSidebarButton("trash", "Trash", false, e -> LandingPage.showTrashPage())
        );

        Button settingsButton = createSidebarButton("settings", "Settings", false, e -> LandingPage.showSettingPage());

        HBox storageValGroup = new HBox(label("64.2 GB of 100 GB", 12, FontWeight.BOLD, WHITE), new Region(), label("64%", 11, FontWeight.BOLD, LIGHT_SECONDARY));
        HBox.setHgrow(storageValGroup.getChildren().get(1), Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE); sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: " + BLUE + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        Button manageStorageBtn = new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showStorageIndexPage());

        VBox storageCard = new VBox(8, label("Storage Used", 12, FontWeight.BOLD, WHITE), storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: rgba(16, 28, 48, 0.65); -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");
        applyHoverAnimation(storageCard, 1.01, -1);

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsButton, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH); sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");
        return sidebar;
    }

    private Button createSidebarButton(String iconType, String text, boolean active, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        Label label = label(text, 13, active ? FontWeight.BOLD : FontWeight.MEDIUM, WHITE);
        HBox content = new HBox(12, new StackPane(icon), label);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("", content);
        button.setMaxWidth(Double.MAX_VALUE); button.setPrefHeight(38); button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12)); button.setOnAction(action);

        if (active) {
            button.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-background-radius: 12; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 12; -fx-border-width: 1; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);");
        } else {
            button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color: rgba(56, 189, 248, 0.12); -fx-background-radius: 12; -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 12; -fx-border-width: 1; -fx-cursor: hand;");
                icon.setStroke(Color.web("#38BDF8"));
                label.setTextFill(Color.web("#38BDF8"));
                TranslateTransition tt = new TranslateTransition(Duration.millis(120), button);
                tt.setToX(4); tt.play();
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                label.setTextFill(Color.web(WHITE));
                TranslateTransition tt = new TranslateTransition(Duration.millis(120), button);
                tt.setToX(0); tt.play();
            });
        }
        return button;
    }

    private void loadCalendarData() {
        createCalendarGrid(Collections.emptyList());
        if (!UserSession.isLoggedIn()) return;
        new Thread(() -> {
            try {
                YearMonth ym = YearMonth.of(year, month);
                List<Reminder> monthReminders = new ReminderDAO().getRemindersForRange(UserSession.getInstance().getUid(), toTimestamp(ym.atDay(1)), toTimestamp(ym.plusMonths(1).atDay(1)));
                List<Reminder> upcoming = new ReminderDAO().getUpcoming(UserSession.getInstance().getUid(), 8);
                Platform.runLater(() -> {
                    createCalendarGrid(monthReminders);
                    updateUpcoming(upcoming);
                    infoText.setText(monthReminders.isEmpty() ? "No reminders scheduled for this month." : monthReminders.size() + " reminder(s) scheduled this month.");
                });
            } catch (Exception ignored) {}
        }).start();
    }

    private void createCalendarGrid(List<Reminder> reminders) {
        grid.getChildren().clear();
        String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        for (int i = 0; i < 7; i++) {
            Label dayHeader = new Label(days[i]);
            dayHeader.setFont(Font.font(FONT, FontWeight.BOLD, 11)); dayHeader.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
            dayHeader.setPrefSize(82, 28); dayHeader.setAlignment(Pos.CENTER);
            grid.add(dayHeader, i, 0);
        }

        Map<LocalDate, List<Reminder>> grouped = reminders.stream().filter(r -> r.getDate() != null).collect(Collectors.groupingBy(r -> toLocalDate(r.getDate())));
        YearMonth ym = YearMonth.of(year, month);
        int daysInMonth = ym.lengthOfMonth(), col = ym.atDay(1).getDayOfWeek().getValue() % 7, row = 1;
        LocalDate today = LocalDate.now();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = ym.atDay(day);
            VBox cell = createDateCell(day, today);
            List<Reminder> dayReminders = grouped.getOrDefault(date, Collections.emptyList());
            for (int i = 0; i < Math.min(dayReminders.size(), 2); i++) addReminderBadge(cell, dayReminders.get(i));
            if (dayReminders.size() > 2) addEventBadge(cell, "+" + (dayReminders.size() - 2) + " more", GOLD, GOLD_BG);
            grid.add(cell, col++, row);
            if (col > 6) { col = 0; row++; }
        }
    }

    private VBox createDateCell(int day, LocalDate today) {
        boolean isToday = year == today.getYear() && month == today.getMonthValue() && day == today.getDayOfMonth();
        Label dayLabel = new Label(String.valueOf(day));
        dayLabel.setFont(Font.font(FONT, FontWeight.BOLD, 12)); dayLabel.setStyle("-fx-text-fill: " + (isToday ? "#38BDF8" : WHITE) + ";");
        VBox cell = new VBox(4, dayLabel); cell.setPrefSize(82, 65); cell.setPadding(new Insets(6));
        
        String defaultStyle = "-fx-background-color: " + (isToday ? ACCENT_LIGHT_BLUE : CARD_BG_INNER) + "; -fx-border-color: #FFFFFF; -fx-border-width: 1px; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: " + ACCENT_LIGHT_BLUE + "; -fx-border-color: #38BDF8; -fx-border-width: 1.5px; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56, 189, 248, 0.4), 10, 0, 0, 0);";
        
        cell.setStyle(defaultStyle);
        
        cell.setOnMouseEntered(e -> {
            cell.setStyle(hoverStyle);
            ScaleTransition st = new ScaleTransition(Duration.millis(120), cell);
            st.setToX(1.04);
            st.setToY(1.04);
            st.play();
        });
        
        cell.setOnMouseExited(e -> {
            cell.setStyle(defaultStyle);
            ScaleTransition st = new ScaleTransition(Duration.millis(120), cell);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        
        LocalDate date = LocalDate.of(year, month, day);
        cell.setOnMouseClicked(e -> showDayEventsWindow(date));
        return cell;
    }

    private void addReminderBadge(VBox cell, Reminder reminder) {
        Label badge = new Label(getTypeIcon(reminder.getType()) + " " + reminder.getTitle());
        badge.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 9)); badge.setMaxWidth(Double.MAX_VALUE); badge.setEllipsisString("...");
        badge.setStyle("-fx-text-fill: " + GOLD + "; -fx-background-color: " + GOLD_BG + "; -fx-background-radius: 4; -fx-padding: 2 4; -fx-border-color: rgba(245, 158, 11, 0.4); -fx-border-radius: 4; -fx-border-width: 0.8;");
        badge.setOnMouseClicked(e -> { e.consume(); showReminderDetails(reminder); });
        cell.getChildren().add(badge);
    }

    private void addEventBadge(VBox cell, String title, String textColor, String bgColor) {
        Label badge = new Label(title); badge.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 9)); badge.setMaxWidth(Double.MAX_VALUE);
        badge.setStyle("-fx-text-fill: " + textColor + "; -fx-background-color: " + bgColor + "; -fx-background-radius: 4; -fx-padding: 2 4; -fx-border-color: rgba(245, 158, 11, 0.4); -fx-border-radius: 4; -fx-border-width: 0.8;");
        cell.getChildren().add(badge);
    }

    private void updateUpcoming(List<Reminder> reminders) {
        remindersList.getChildren().clear();
        if (reminders == null || reminders.isEmpty()) { remindersList.getChildren().add(emptyLabel("No upcoming reminders.")); return; }
        for (Reminder r : reminders) remindersList.getChildren().add(createReminderCard(r));
    }

    private VBox createReminderCard(Reminder reminder) {
        LocalDate date = toLocalDate(reminder.getDate());
        String accent = getPriorityColor(reminder.getPriority());
        VBox content = new VBox(2, label(date == null ? "Date unavailable" : date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")), 11, FontWeight.BOLD, accent), label(getTypeIcon(reminder.getType()) + " " + reminder.getTitle(), 13, FontWeight.BOLD, WHITE), label((reminder.getTime() == null ? "Time not specified" : reminder.getTime()) + " • " + safe(reminder.getPriority(), "Medium") + " priority", 11, FontWeight.MEDIUM, LIGHT_SECONDARY));
        VBox card = new VBox(content); card.setPadding(new Insets(10, 12, 10, 12));
        card.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08) rgba(255, 255, 255, 0.08) rgba(255, 255, 255, 0.08) " + accent + "; -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-width: 1 1 1 4; -fx-cursor: hand;");
        card.setOnMouseClicked(e -> showReminderDetails(reminder));
        card.setOnMouseEntered(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), card);
            tt.setToX(4); tt.play();
        });
        card.setOnMouseExited(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), card);
            tt.setToX(0); tt.play();
        });
        return card;
    }

    private void showDayEventsWindow(LocalDate date) {
        Stage stage = new Stage(); stage.initModality(javafx.stage.Modality.APPLICATION_MODAL); stage.setTitle("Reminders");
        VBox list = new VBox(12); list.setPadding(new Insets(4));
        ScrollPane scroll = new ScrollPane(list); scroll.setFitToWidth(true); scroll.setFitToHeight(true); scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        Button close = new Button("Close"); close.setOnAction(e -> stage.close());
        close.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        close.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-text-fill: #38BDF8; -fx-border-color: #38BDF8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 6 16;");
        applyHoverAnimation(close, 1.05, 0);

        VBox layout = new VBox(16, label("Reminders for " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")), 18, FontWeight.BOLD, WHITE), scroll, close);
        layout.setPadding(new Insets(24)); layout.setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");
        stage.setScene(new Scene(layout, 500, 400)); stage.show();
    }

    private void showReminderDetails(Reminder reminder) {
        Stage stage = new Stage();
        stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        stage.setTitle("Reminder Details");

        LocalDate date = toLocalDate(reminder.getDate());
        String priorityColor = getPriorityColor(reminder.getPriority());

        Label headerTitle = label(getTypeIcon(reminder.getType()) + "  " + reminder.getTitle(), 18, FontWeight.BOLD, WHITE);
        headerTitle.setWrapText(true);

        VBox infoCard = new VBox(10);
        infoCard.setPadding(new Insets(16));
        infoCard.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");

        HBox dateRow = new HBox(8, label("📅  Date:", 12, FontWeight.BOLD, LIGHT_SECONDARY), label(date == null ? "Date unavailable" : date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")), 12, FontWeight.MEDIUM, WHITE));
        HBox timeRow = new HBox(8, label("⏰  Time:", 12, FontWeight.BOLD, LIGHT_SECONDARY), label(safe(reminder.getTime(), "Not specified"), 12, FontWeight.MEDIUM, WHITE));
        HBox priorityRow = new HBox(8, label("⚡  Priority:", 12, FontWeight.BOLD, LIGHT_SECONDARY), label(safe(reminder.getPriority(), "Medium"), 12, FontWeight.BOLD, priorityColor));

        infoCard.getChildren().addAll(dateRow, timeRow, priorityRow);

        Button closeBtn = new Button("Close");
        closeBtn.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        closeBtn.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: #FFFFFF; -fx-background-radius: 8; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 8 20; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.4), 8, 0, 0, 2);");
        closeBtn.setOnAction(e -> stage.close());
        applyHoverAnimation(closeBtn, 1.05, 0);

        HBox buttonContainer = new HBox(closeBtn);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);

        VBox layout = new VBox(16, headerTitle, infoCard, buttonContainer);
        layout.setPadding(new Insets(24));
        layout.setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.5; -fx-border-radius: 14; -fx-background-radius: 14; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.7), 20, 0, 0, 8);");

        Scene scene = new Scene(layout, 420, 260);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    private Timestamp toTimestamp(LocalDate date) { return Timestamp.of(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())); }
    private LocalDate toLocalDate(Timestamp timestamp) { return timestamp == null ? null : timestamp.toSqlTimestamp().toLocalDateTime().toLocalDate(); }
    private String getTypeIcon(String type) { return type != null && type.toLowerCase().contains("task") ? "✓" : "🔔"; }
    private String getPriorityColor(String priority) { return priority != null && priority.equalsIgnoreCase("high") ? DANGER_RED : "#38BDF8"; }
    private String getPriorityBackground(String priority) { return priority != null && priority.equalsIgnoreCase("high") ? "rgba(239, 68, 68, 0.15)" : ACCENT_LIGHT_BLUE; }
    private String safe(String val, String fallback) { return val == null || val.isBlank() ? fallback : val; }
    private Label emptyLabel(String text) { return label(text, 12, FontWeight.MEDIUM, LIGHT_SECONDARY); }

    private void changeMonth(int amount) {
        month += amount;
        if (month < 1) { month = 12; year--; }
        else if (month > 12) { month = 1; year++; }
        updateCalendarHeader(); loadCalendarData();
    }

    private void updateCalendarHeader() {
        monthBtn.setText(Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        yearBtn.setText(String.valueOf(year));
    }

    private void showMonthPicker() {
        Popup popup = new Popup(); VBox box = createPickerPopupBox();
        for (int i = 1; i <= 12; i++) {
            final int m = i; Button b = new Button(Month.of(i).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            b.setOnAction(e -> { month = m; popup.hide(); updateCalendarHeader(); loadCalendarData(); });
            box.getChildren().add(b);
        }
        popup.getContent().add(box); popup.setAutoHide(true); popup.show(monthBtn, monthBtn.localToScreen(0, monthBtn.getHeight()).getX(), monthBtn.localToScreen(0, monthBtn.getHeight()).getY());
    }

    private void showYearPicker() {
        Popup popup = new Popup(); VBox box = createPickerPopupBox();
        for (int y = year - 5; y <= year + 5; y++) {
            final int selectedYear = y; Button b = new Button(String.valueOf(y));
            b.setOnAction(e -> { year = selectedYear; popup.hide(); updateCalendarHeader(); loadCalendarData(); });
            box.getChildren().add(b);
        }
        popup.getContent().add(box); popup.setAutoHide(true); popup.show(yearBtn, yearBtn.localToScreen(0, yearBtn.getHeight()).getX(), yearBtn.localToScreen(0, yearBtn.getHeight()).getY());
    }

    private VBox createPickerPopupBox() {
        VBox box = new VBox(4); box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        return box;
    }

    private void styleCalendarHeaderPickerBtn(Button b) {
        b.setFont(Font.font(FONT, FontWeight.BOLD, 18)); b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + WHITE + "; -fx-cursor: hand; -fx-padding: 4 6;");
        applyHoverAnimation(b, 1.08, 0);
    }

    private Button createNavButton(String text) {
        Button b = new Button(text); b.setPrefSize(34, 34); b.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        b.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        b.setOnMouseEntered(e -> {
            b.setStyle("-fx-background-color: rgba(56, 189, 248, 0.2); -fx-text-fill: #38BDF8; -fx-border-color: #38BDF8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.3), 8, 0, 0, 0);");
            ScaleTransition st = new ScaleTransition(Duration.millis(120), b);
            st.setToX(1.1); st.setToY(1.1); st.play();
        });
        b.setOnMouseExited(e -> {
            b.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            ScaleTransition st = new ScaleTransition(Duration.millis(120), b);
            st.setToX(1.0); st.setToY(1.0); st.play();
        });
        return b;
    }

    private Label label(String text, double size, FontWeight weight, String color) {
        Label l = new Label(text); l.setFont(Font.font(FONT, weight, size)); l.setStyle("-fx-text-fill: " + color + ";"); return l;
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath(); icon.setFill(Color.TRANSPARENT); icon.setStrokeWidth(2);
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