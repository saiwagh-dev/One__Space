package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class UserCalendar {

    // Style Constants
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // Sidebar & Top Bar Backgrounds
    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";

    // Center Workspace Background
    private static final String BG_CENTER_CANVAS = "#31435B";

    // Card Colors
    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_CARD_INNER = "#CADDF2";
    private static final String BORDER_CARD = "#C3D6EC";
    private static final String BG_INPUT = "#EDF3FA";

    // Typography Colors
    private static final String TEXT_DARK = "#0F172A";        
    private static final String TEXT_MUTED_DARK = "#334155";  
    private static final String TEXT_LIGHT = "#FFFFFF";       
    private static final String TEXT_MUTED_LIGHT = "#94A3B8"; 

    // Accent Colors
    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String ACCENT_LIGHT_BLUE = "#BFDBFE";
    private static final String DANGER_RED = "#DC2626";

    // Calendar Dynamic State Variables
    private int year = 2026;
    private int month = 8;
    private GridPane grid;
    private Button monthBtn;
    private Button yearBtn;

    public Scene getCalendarPageScene() {

        // =========================================================
        // SIDEBAR
        // =========================================================

        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", true);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", false);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);
        Button logoutBtn = createSidebarButton("🚪", "Logout", false);


        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        aiBtn.setOnAction(e -> LandingPage.showLandingPage());
        collabBtn.setOnAction(e -> LandingPage.showCollaborationPage());
        recentBtn.setOnAction(e -> LandingPage.showRecentPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());
        settingsBtn.setOnAction(e -> LandingPage.showLandingPage());
        logoutBtn.setOnAction(e -> LandingPage.showUserLoginPage());


        VBox navList = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn, logoutBtn);

        // Sidebar Storage Card
        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        storageTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox storageValGroup = new HBox(storageVal, new Region(), storagePercent);
        HBox.setHgrow(storageValGroup.getChildren().get(1), Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: " + PRIMARY_BLUE + "; -fx-control-inner-background: #0E1520;");

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showLandingPage());

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        // =========================================================
        // TOP SEARCH BAR & PROFILE
        // =========================================================

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        TextField searchField = new TextField();
        searchField.setPromptText("Search files or dates...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-background-color: #141E2C; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 6; -fx-background-radius: 4;");

        HBox searchContainer = new HBox(8, searchIcon, searchField, keyShortcut);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0, 12, 0, 14));
        searchContainer.setPrefWidth(420);
        searchContainer.setStyle("-fx-background-color: #141E2C; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());


        Label avatar = new Label("AV");
avatar.setPrefSize(34, 34);
avatar.setAlignment(Pos.CENTER);
avatar.setStyle(
        "-fx-background-color: " + PRIMARY_BLUE + ";" +
        "-fx-background-radius: 50%;" +
        "-fx-text-fill: " + TEXT_LIGHT + ";" +
        "-fx-font-weight: bold;" +
        "-fx-font-size: 12px;"
);

Label userName = new Label("Aarav Verma");
userName.setFont(
        Font.font(FONT, FontWeight.SEMI_BOLD, 13)
);
userName.setStyle(
        "-fx-text-fill: " + TEXT_LIGHT + ";"
);

Label dropDown = new Label("⌄");
dropDown.setStyle(
        "-fx-text-fill: " + TEXT_MUTED_LIGHT + ";"
);


// =========================================================
// CLICKABLE PROFILE OPTION
// =========================================================

HBox profileOption =
        new HBox(
                8,
                avatar,
                userName,
                dropDown
        );

profileOption.setAlignment(
        Pos.CENTER
);

profileOption.setPadding(
        new Insets(5, 8, 5, 8)
);

profileOption.setStyle(
        "-fx-background-color: transparent;" +
        "-fx-background-radius: 8;" +
        "-fx-cursor: hand;"
);


// =========================================================
// OPEN PROFILE PAGE WHEN CLICKED
// =========================================================

profileOption.setOnMouseClicked(e -> {
    LandingPage.showUserProfilePage();
});


// =========================================================
// HOVER EFFECT
// =========================================================

profileOption.setOnMouseEntered(e -> {
    profileOption.setStyle(
            "-fx-background-color: #26354A;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
    );
});

profileOption.setOnMouseExited(e -> {
    profileOption.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
    );
});


// =========================================================
// TOP RIGHT
// =========================================================

HBox profileBox =
        new HBox(
                10,
                bellBtn,
                profileOption
        );

profileBox.setAlignment(
        Pos.CENTER
);


// =========================================================
// TOP BAR
// =========================================================

HBox topBar =
        new HBox(
                20,
                searchContainer,
                new Region(),
                profileBox
        );

HBox.setHgrow(
        topBar.getChildren().get(1),
        Priority.ALWAYS
);

topBar.setAlignment(
        Pos.CENTER_LEFT
);

topBar.setPadding(
        new Insets(
                16,
                28,
                14,
                28
        )
);

topBar.setStyle(
        "-fx-background-color: " + BG_SIDEBAR + ";" +
        "-fx-border-color: " + SIDEBAR_BORDER + ";" +
        "-fx-border-width: 0 0 1 0;"
);


        // =========================================================
        // CALENDAR PAGE HEADER & ACTION BUTTON
        // =========================================================

        Label pageTitle = new Label("Calendar & Reminders");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        pageTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label pageDesc = new Label("Dates and reminders will automatically appear here once your files are scanned.");
        pageDesc.setFont(Font.font(FONT, 13));
        pageDesc.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        VBox titleBox = new VBox(4, pageTitle, pageDesc);

        Button addReminderBtn = new Button("Add Reminder");
        addReminderBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        addReminderBtn.setStyle(
                "-fx-background-color: " + PRIMARY_BLUE + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 8 18;"
        );
        addReminderBtn.setOnAction(e -> LandingPage.showAddReminderPage());

        HBox pageHeader = new HBox(titleBox, new Region(), addReminderBtn);
        HBox.setHgrow(pageHeader.getChildren().get(1), Priority.ALWAYS);
        pageHeader.setAlignment(Pos.CENTER_LEFT);

        // =========================================================
        // MAIN CALENDAR CARD (Interactive Monthly Grid View)
        // =========================================================

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

        createCalendarGrid();

        Label infoIcon = new Label("ⓘ");
        infoIcon.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        infoIcon.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        Label infoText = new Label("Key deadlines, events, and document dates will populate here.");
        infoText.setFont(Font.font(FONT, FontWeight.MEDIUM, 12));
        infoText.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        HBox infoBox = new HBox(8, infoIcon, infoText);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        VBox calendarCard = new VBox(16, calendarHeader, grid, infoBox);
        calendarCard.setPadding(new Insets(24));
        calendarCard.setStyle(createCardStyle());

        VBox calendarSection = new VBox(calendarCard);
        HBox.setHgrow(calendarSection, Priority.ALWAYS);

        // =========================================================
        // UPCOMING REMINDERS SIDE PANEL (DISPLAYING DUMMY DATA)
        // =========================================================

        Label reminderTitle = new Label("Upcoming Reminders");
        reminderTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        reminderTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        // Populating dummy items matching the grid dates
        VBox reminderItem1 = createReminderCard("Aug 12, 2026", "📄 JavaFX Submission", "11:59 PM • Project Portal", DANGER_RED, "#FEE2E2");
        VBox reminderItem2 = createReminderCard("Aug 16, 2026", "👥 Team Sync 4PM", "04:00 PM • Google Meet", PRIMARY_BLUE, "#CADDF2");
        VBox reminderItem3 = createReminderCard("Aug 20, 2026", "📝 DBMS Mock Exam", "10:00 AM • Exam Hall B", "#D97706", "#FDE68A");
        VBox reminderItem4 = createReminderCard("Aug 25, 2026", "💻 Arch Review", "02:30 PM • Conference Room 1", "#059669", "#A7F3D0");

        VBox remindersList = new VBox(10, reminderItem1, reminderItem2, reminderItem3, reminderItem4);

        VBox remindersCard = new VBox(remindersList);
        remindersCard.setPadding(new Insets(16));
        remindersCard.setMinHeight(410);
        remindersCard.setStyle(createCardStyle());

        VBox remindersSection = new VBox(12, reminderTitle, remindersCard);
        remindersSection.setPrefWidth(350);

        HBox sectionsContainer = new HBox(20, calendarSection, remindersSection);
        HBox.setHgrow(calendarSection, Priority.ALWAYS);

        // =========================================================
        // SCROLLABLE CONTAINER & MAIN CANVAS
        // =========================================================

        VBox contentBody = new VBox(22, pageHeader, sectionsContainer);
        contentBody.setPadding(new Insets(24, 28, 28, 28));
        contentBody.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: " + BG_CENTER_CANVAS + ";" +
                "-fx-background: " + BG_CENTER_CANVAS + ";" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_SIDEBAR + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1200, 750);
    }

    // =========================================================
    // HELPER METHODS
    // =========================================================

    private VBox createReminderCard(String date, String title, String subtitle, String accentColor, String badgeBg) {
        Label dateLbl = new Label(date);
        dateLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        dateLbl.setStyle("-fx-text-fill: " + accentColor + ";");

        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        titleLbl.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label subLbl = new Label(subtitle);
        subLbl.setFont(Font.font(FONT, FontWeight.MEDIUM, 11));
        subLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox content = new VBox(2, dateLbl, titleLbl, subLbl);

        VBox card = new VBox(content);
        card.setPadding(new Insets(10, 12, 10, 12));
        card.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-border-width: 1 1 1 4;" +
                "-fx-border-color: " + BORDER_CARD + " " + BORDER_CARD + " " + BORDER_CARD + " " + accentColor + ";"
        );
        return card;
    }

    private StackPane createOneSpaceLogo() {
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

    private Button createSidebarButton(String icon, String label, boolean isActive) {
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(FONT, 14));

        Label textLbl = new Label(label);
        textLbl.setFont(Font.font(FONT, isActive ? FontWeight.BOLD : FontWeight.MEDIUM, 13));

        HBox content = new HBox(12, iconLbl, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 12, 0, 12));

        if (isActive) {
            btn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");
            textLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
            textLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #26354A; -fx-background-radius: 8; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;"));
        }

        return btn;
    }

    private void createCalendarGrid() {
        grid.getChildren().clear();

        String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

        for (int i = 0; i < 7; i++) {
            Label dayHeader = new Label(days[i]);
            dayHeader.setFont(Font.font(FONT, FontWeight.BOLD, 11));
            dayHeader.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
            dayHeader.setPrefSize(82, 28);
            dayHeader.setAlignment(Pos.CENTER);
            grid.add(dayHeader, i, 0);
        }

        YearMonth ym = YearMonth.of(year, month);
        int daysInMonth = ym.lengthOfMonth();
        int col = ym.atDay(1).getDayOfWeek().getValue() % 7;
        int row = 1;

        LocalDate today = LocalDate.now();

        for (int day = 1; day <= daysInMonth; day++) {
            VBox cell = createDateCell(day, today);

            // Add dummy events into specific date boxes
            if (day == 12) {
                addEventBadge(cell, "📄 JavaFX Submission", DANGER_RED, "#FEE2E2");
            } else if (day == 16) {
                addEventBadge(cell, "👥 Team Sync 4PM", PRIMARY_BLUE, "#CADDF2");
            } else if (day == 20) {
                addEventBadge(cell, "📝 DBMS Mock Exam", "#D97706", "#FDE68A");
            } else if (day == 25) {
                addEventBadge(cell, "💻 Arch Review", "#059669", "#A7F3D0");
            }

            grid.add(cell, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createDateCell(int day, LocalDate today) {
        Label dayLabel = new Label(String.valueOf(day));
        dayLabel.setFont(Font.font(FONT, FontWeight.BOLD, 12));

        boolean isToday = year == today.getYear() &&
                        month == today.getMonthValue() &&
                        day == today.getDayOfMonth();

        dayLabel.setStyle("-fx-text-fill: " + (isToday ? PRIMARY_BLUE : TEXT_DARK) + ";");

        VBox cell = new VBox(4, dayLabel);
        cell.setPrefSize(82, 65);
        cell.setPadding(new Insets(6));

        String defaultBg = isToday ? ACCENT_LIGHT_BLUE : BG_CARD_INNER;
        String defaultBorder = isToday ? PRIMARY_BLUE : BORDER_CARD;

        String normalStyle = createDayCellStyle(defaultBg, defaultBorder);
        String hoverStyle = createDayCellStyle(ACCENT_LIGHT_BLUE, PRIMARY_BLUE);

        cell.setStyle(normalStyle);
        cell.setOnMouseEntered(e -> cell.setStyle(hoverStyle));
        cell.setOnMouseExited(e -> cell.setStyle(normalStyle));

        // --- UPDATED: Open a large centered modal window on click ---
        LocalDate cellDate = LocalDate.of(year, month, day);
        cell.setOnMouseClicked(e -> showDayEventsWindow(cellDate));

        return cell;
    }

    private void showDayEventsWindow(LocalDate date) {
        javafx.stage.Stage eventStage = new javafx.stage.Stage();
        eventStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        eventStage.setTitle("Scheduled Events");

        // Header Title
        String formattedDate = date.format(java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        Label headerTitle = new Label("Events for " + formattedDate);
        headerTitle.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        headerTitle.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label headerSubtitle = new Label("Review all tasks, assignments, and meetings planned for this date.");
        headerSubtitle.setFont(Font.font(FONT, 12));
        headerSubtitle.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox headerBox = new VBox(4, headerTitle, headerSubtitle);

        // Events Container
        VBox eventsList = new VBox(12);
        eventsList.setPadding(new Insets(4));

        // Match dummy data with clicked date
        if (date.getDayOfMonth() == 12 && date.getMonthValue() == 8 && date.getYear() == 2026) {
            eventsList.getChildren().add(createModalEventCard("📄 JavaFX Submission", "11:59 PM • Project Portal", "Ensure all controller logic and FXML files are properly linked and tested before submission.", DANGER_RED));
        } else if (date.getDayOfMonth() == 16 && date.getMonthValue() == 8 && date.getYear() == 2026) {
            eventsList.getChildren().add(createModalEventCard("👥 Team Sync 4PM", "04:00 PM • Google Meet", "Discuss final module integrations and assign remaining tasks for the upcoming sprint.", PRIMARY_BLUE));
        } else if (date.getDayOfMonth() == 20 && date.getMonthValue() == 8 && date.getYear() == 2026) {
            eventsList.getChildren().add(createModalEventCard("📝 DBMS Mock Exam", "10:00 AM • Exam Hall B", "Covers normalization, SQL queries, transactional properties, and ER diagrams.", "#D97706"));
        } else if (date.getDayOfMonth() == 25 && date.getMonthValue() == 8 && date.getYear() == 2026) {
            eventsList.getChildren().add(createModalEventCard("💻 Arch Review", "02:30 PM • Conference Room 1", "Present component interaction layouts and security layers to the project supervisor.", "#059669"));
        } else {
            VBox emptyBox = new VBox();
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(40));
            Label noEventLbl = new Label("No events or reminders scheduled for this day.");
            noEventLbl.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
            noEventLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
            emptyBox.getChildren().add(noEventLbl);
            eventsList.getChildren().add(emptyBox);
        }

        ScrollPane scrollPane = new ScrollPane(eventsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Cancel / Close Button
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        cancelBtn.setPrefWidth(100);
        cancelBtn.setStyle(
                "-fx-background-color: " + BG_INPUT + ";" +
                "-fx-text-fill: " + TEXT_DARK + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 8 16;"
        );
        cancelBtn.setOnAction(e -> eventStage.close());

        HBox footerBox = new HBox(cancelBtn);
        footerBox.setAlignment(Pos.CENTER_RIGHT);

        // Main Layout Container
        VBox rootLayout = new VBox(16, headerBox, scrollPane, footerBox);
        rootLayout.setPadding(new Insets(24));
        rootLayout.setStyle("-fx-background-color: " + BG_CARD + ";");

        Scene scene = new Scene(rootLayout, 500, 380);
        eventStage.setScene(scene);
        eventStage.setResizable(false);
        eventStage.centerOnScreen();
        eventStage.show();
    }

    private VBox createModalEventCard(String title, String timeAndLocation, String description, String accentColor) {
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        titleLbl.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label metaLbl = new Label(timeAndLocation);
        metaLbl.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        metaLbl.setStyle("-fx-text-fill: " + accentColor + ";");

        Label descLbl = new Label(description);
        descLbl.setFont(Font.font(FONT, 12));
        descLbl.setWrapText(true);
        descLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox card = new VBox(6, titleLbl, metaLbl, descLbl);
        //card.setPadding(14);
        card.setStyle(
                "-fx-background-color: " + BG_CARD_INNER + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-border-width: 1 1 1 4;" +
                "-fx-border-color: " + BORDER_CARD + " " + BORDER_CARD + " " + BORDER_CARD + " " + accentColor + ";"
        );
        return card;
    }

    private void addEventBadge(VBox dateBox, String title, String textColor, String bgColor) {
        Label badge = new Label(title);
        badge.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 9));
        badge.setMaxWidth(Double.MAX_VALUE);
        badge.setStyle(
                "-fx-text-fill: " + textColor + ";" +
                "-fx-background-color: " + bgColor + ";" +
                "-fx-background-radius: 4;" +
                "-fx-padding: 2 4;"
        );
        dateBox.getChildren().add(badge);
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
        createCalendarGrid();
    }

    private void updateCalendarHeader() {
        monthBtn.setText(Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        yearBtn.setText(String.valueOf(year));
    }

    private void showMonthPicker() {
        Popup popup = new Popup();
        VBox box = createPickerPopupBox();

        for (int i = 1; i <= 12; i++) {
            final int m = i;
            Button b = new Button(Month.of(i).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            applyPickerButtonStyle(b, m == month);

            b.setOnAction(e -> {
                month = m;
                popup.hide();
                updateCalendarHeader();
                createCalendarGrid();
            });

            box.getChildren().add(b);
        }

        popup.getContent().add(box);
        showPopupRelativeToControl(popup, monthBtn);
    }

    private void showYearPicker() {
        Popup popup = new Popup();
        VBox box = createPickerPopupBox();

        for (int y = year - 5; y <= year + 5; y++) {
            final int selectedYear = y;
            Button b = new Button(String.valueOf(y));
            applyPickerButtonStyle(b, selectedYear == year);

            b.setOnAction(e -> {
                year = selectedYear;
                popup.hide();
                updateCalendarHeader();
                createCalendarGrid();
            });

            box.getChildren().add(b);
        }

        popup.getContent().add(box);
        showPopupRelativeToControl(popup, yearBtn);
    }

    private VBox createPickerPopupBox() {
        VBox box = new VBox(4);
        box.setPadding(new Insets(10));
        box.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 4);"
        );
        return box;
    }

    private void applyPickerButtonStyle(Button b, boolean isSelected) {
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(7, 12, 7, 12));
        b.setFont(Font.font(FONT, isSelected ? FontWeight.BOLD : FontWeight.MEDIUM, 12));

        if (isSelected) {
            b.setStyle("-fx-background-color: " + ACCENT_LIGHT_BLUE + "; -fx-text-fill: " + PRIMARY_BLUE + "; -fx-background-radius: 6; -fx-cursor: hand;");
        } else {
            b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_DARK + "; -fx-background-radius: 6; -fx-cursor: hand;");
            b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: " + BG_CARD_INNER + "; -fx-text-fill: " + TEXT_DARK + "; -fx-background-radius: 6; -fx-cursor: hand;"));
            b.setOnMouseExited(e -> b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_DARK + "; -fx-background-radius: 6; -fx-cursor: hand;"));
        }
    }

    private void showPopupRelativeToControl(Popup popup, Control control) {
        Point2D screenPos = control.localToScreen(0, control.getHeight());
        popup.setAutoHide(true);
        popup.show(control, screenPos.getX(), screenPos.getY());
    }

    private void styleCalendarHeaderPickerBtn(Button b) {
        b.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_DARK + "; -fx-cursor: hand; -fx-padding: 4 6;");
    }

    private Button createNavButton(String text) {
        Button b = new Button(text);
        b.setPrefSize(34, 34);
        b.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        b.setStyle(
                "-fx-background-color: " + BG_INPUT + ";" +
                "-fx-text-fill: " + TEXT_DARK + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );
        return b;
    }

    private String createCardStyle() {
        return "-fx-background-color: " + BG_CARD + ";" +
               "-fx-border-color: " + BORDER_CARD + ";" +
               "-fx-border-radius: 16;" +
               "-fx-background-radius: 16;" +
               "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 16, 0, 0, 6);";
    }

    private String createDayCellStyle(String bgHex, String borderHex) {
        return "-fx-background-color: " + bgHex + ";" +
               "-fx-border-color: " + borderHex + ";" +
               "-fx-border-radius: 8;" +
               "-fx-background-radius: 8;" +
               "-fx-cursor: hand;";
    }
}