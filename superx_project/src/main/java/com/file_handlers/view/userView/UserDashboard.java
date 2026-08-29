package com.file_handlers.view.userView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.file_handlers.model.UserSession;
import com.file_handlers.model.FileData;
import java.util.List;
import com.file_handlers.dao.FileDAO;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

public class UserDashboard {
    private final FileDAO fileDAO=new FileDAO();

    // =========================================================
    // STYLE CONSTANTS
    // =========================================================

    private static final String FONT =
            "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";

    private static final String BG_CENTER_CANVAS = "#31435B";

    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_CARD_INNER = "#CADDF2";
    private static final String BORDER_CARD = "#C3D6EC";

    private static final String TEXT_DARK = "#0F172A";
    private static final String TEXT_MUTED_DARK = "#334155";
    private static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#94A3B8";

    private static final String PRIMARY_BLUE = "#2563EB";

    private static final String[] CHART_COLORS = {
            "#2563EB",
            "#0284C7",
            "#059669",
            "#7C3AED",
            "#475569"
    };

    // =========================================================
    // MAIN DASHBOARD
    // =========================================================

    public Scene getDashboardScene() {

        // =====================================================
        // CURRENT USER
        // =====================================================

        String activeUserName = "User";
        String initials = "U";

        UserSession session = UserSession.getInstance();

        if (session != null &&
                session.getDisplayName() != null) {

            String fullName =
                    session.getDisplayName().trim();

            if (!fullName.isEmpty()) {

                String[] parts =
                        fullName.split("\\s+");

                activeUserName = parts[0];

                initials =
                        activeUserName
                                .substring(0, 1)
                                .toUpperCase();
            }
        }

        // =====================================================
        // SIDEBAR
        // =====================================================

        StackPane logoIcon =
                createOneSpaceLogo();

        Label logoText =
                new Label("OneSpace");

        logoText.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        19
                )
        );

        logoText.setStyle(
                "-fx-font-family:" + FONT + ";" +
                "-fx-font-size:19px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + TEXT_LIGHT + ";"
        );

        HBox logoHeader =
                new HBox(
                        10,
                        logoIcon,
                        logoText
                );

        logoHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox logoBox =
                new VBox(
                        4,
                        logoHeader
                );

        logoBox.setPadding(
                new Insets(
                        0,
                        0,
                        18,
                        6
                )
        );

        Button dashboardBtn =
                createSidebarButton(
                        "⌂",
                        "Dashboard",
                        true
                );

        Button spacesBtn =
                createSidebarButton(
                        "📁",
                        "Spaces",
                        false
                );

        Button searchBtn =
                createSidebarButton(
                        "⌕",
                        "Search",
                        false
                );

        Button calendarBtn =
                createSidebarButton(
                        "📅",
                        "Calendar",
                        false
                );

        Button aiBtn =
                createSidebarButton(
                        "✧",
                        "AI Assistant",
                        false
                );

        Button collabBtn =
                createSidebarButton(
                        "👥",
                        "Collaboration",
                        false
                );

        Button recentBtn =
                createSidebarButton(
                        "🕒",
                        "Recent",
                        false
                );

        Button trashBtn =
                createSidebarButton(
                        "🗑",
                        "Trash",
                        false
                );

        Button settingsBtn =
                createSidebarButton(
                        "⚙",
                        "Settings",
                        false
                );

        Button logoutBtn =
                createSidebarButton(
                        "🚪",
                        "Logout",
                        false
                );

        // =====================================================
        // NAVIGATION
        // =====================================================

        dashboardBtn.setOnAction(
                e -> LandingPage.showUserDashboard()
        );

        

        spacesBtn.setOnAction(e ->
                {
                        LandingPage.showUserSpace();
                }
        );

        // NEW:
        // Search now opens the trial Search implementation.

        searchBtn.setOnAction(e ->{
                
                LandingPage.showUserSearch();
        }
        );

        calendarBtn.setOnAction(
                e -> LandingPage.showCalendarPage()
        );

        aiBtn.setOnAction(
                e -> LandingPage.showAiAssistantPage()
        );

        collabBtn.setOnAction(
                e -> LandingPage.showCollaborationPage()
        );

        recentBtn.setOnAction(
                e -> LandingPage.showRecentPage()
        );

        trashBtn.setOnAction(
                e -> LandingPage.showTrashPage()
        );

        settingsBtn.setOnAction(
                e -> LandingPage.showSettingPage()
        );

        logoutBtn.setOnAction(e -> {

            UserSession.clearSession();

            LandingPage.showUserLoginPage();
        });

        VBox navList =
                new VBox(
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

        // =====================================================
        // STORAGE CARD
        // =====================================================

        Label storageTitle =
                new Label("Storage Used");

        storageTitle.setStyle(
                "-fx-font-family:" + FONT + ";" +
                "-fx-font-size:12px;" +
                "-fx-font-weight:600;" +
                "-fx-text-fill:" + TEXT_LIGHT + ";"
        );

        Label storageVal =
                new Label("64.2 GB of 100 GB");

        storageVal.setStyle(
                "-fx-font-family:" + FONT + ";" +
                "-fx-font-size:12px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + TEXT_LIGHT + ";"
        );

        Label storagePercent =
                new Label("64%");

        storagePercent.setStyle(
                "-fx-font-family:" + FONT + ";" +
                "-fx-font-size:11px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" + TEXT_MUTED_LIGHT + ";"
        );

        Region storageSpacer =
                new Region();

        HBox.setHgrow(
                storageSpacer,
                Priority.ALWAYS
        );

        HBox storageValGroup =
                new HBox(
                        storageVal,
                        storageSpacer,
                        storagePercent
                );

        storageValGroup.setAlignment(
                Pos.CENTER_LEFT
        );

        ProgressBar sidebarProgress =
                new ProgressBar(0.64);

        sidebarProgress.setMaxWidth(
                Double.MAX_VALUE
        );

        sidebarProgress.setPrefHeight(6);

        sidebarProgress.setStyle(
                "-fx-accent:" + PRIMARY_BLUE + ";" +
                "-fx-control-inner-background:#0E1520;"
        );

        Button manageStorageBtn =
        new Button("Storage Index ›");

        manageStorageBtn.setOnAction(
        e -> LandingPage.showStorageIndexPage()
        );

        manageStorageBtn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#60A5FA;" +
                "-fx-font-size:11px;" +
                "-fx-font-weight:600;" +
                "-fx-padding:2 0 0 0;" +
                "-fx-cursor:hand;"
        );

        

        VBox storageCard =
                new VBox(
                        8,
                        storageTitle,
                        storageValGroup,
                        sidebarProgress,
                        manageStorageBtn
                );

        storageCard.setPadding(
                new Insets(14)
        );

        storageCard.setStyle(
                "-fx-background-color:" +
                BG_SIDEBAR_CARD + ";" +
                "-fx-border-color:" +
                SIDEBAR_BORDER + ";" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;"
        );

        Region sidebarSpacer =
                new Region();

        VBox.setVgrow(
                sidebarSpacer,
                Priority.ALWAYS
        );

        VBox sidebar =
                new VBox(
                        12,
                        logoBox,
                        navList,
                        sidebarSpacer,
                        settingsBtn,
                        logoutBtn,
                        storageCard
                );

        sidebar.setPadding(
                new Insets(
                        20,
                        14,
                        20,
                        14
                )
        );

        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);

        sidebar.setStyle(
                "-fx-background-color:" +
                BG_SIDEBAR + ";" +
                "-fx-border-color:" +
                SIDEBAR_BORDER + ";" +
                "-fx-border-width:0 1 0 0;"
        );

        // =====================================================
        // TOP SEARCH
        // =====================================================

        Label searchIcon =
                new Label("⌕");

        searchIcon.setStyle(
                "-fx-font-size:16px;" +
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT + ";"
        );

        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "Search in OneSpace..."
        );

        searchField.setPrefHeight(38);

        searchField.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-prompt-text-fill:" +
                TEXT_MUTED_LIGHT + ";" +
                "-fx-font-size:13px;" +
                "-fx-text-fill:" +
                TEXT_LIGHT + ";"
        );

        Label keyShortcut =
                new Label("⌘ K");

        keyShortcut.setStyle(
                "-fx-background-color:#141E2C;" +
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT + ";" +
                "-fx-padding:3 6;" +
                "-fx-background-radius:4;" +
                "-fx-font-size:10px;"
        );

        HBox searchContainer =
                new HBox(
                        8,
                        searchIcon,
                        searchField,
                        keyShortcut
                );

        searchContainer.setAlignment(
                Pos.CENTER_LEFT
        );

        searchContainer.setPadding(
                new Insets(
                        0,
                        12,
                        0,
                        14
                )
        );

        searchContainer.setPrefWidth(420);

        searchContainer.setStyle(
                "-fx-background-color:#141E2C;" +
                "-fx-border-color:" +
                SIDEBAR_BORDER + ";" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;"
        );

        HBox.setHgrow(
                searchField,
                Priority.ALWAYS
        );

        Button bellBtn =
                new Button("🔔");

        bellBtn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-font-size:16px;" +
                "-fx-text-fill:" +
                TEXT_LIGHT + ";" +
                "-fx-cursor:hand;"
        );

        bellBtn.setOnAction(
                e -> LandingPage.showNotificationPage()
        );

        Label avatar =
                new Label(initials);

        avatar.setPrefSize(
                34,
                34
        );

        avatar.setAlignment(
                Pos.CENTER
        );

        avatar.setStyle(
                "-fx-background-color:" +
                PRIMARY_BLUE + ";" +
                "-fx-background-radius:50%;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-font-size:12px;"
        );

        Label userName =
                new Label(activeUserName);

        userName.setStyle(
                "-fx-text-fill:" +
                TEXT_LIGHT + ";" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:600;"
        );

        Label dropDown =
                new Label("⌄");

        dropDown.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT + ";"
        );

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
                "-fx-background-color:transparent;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        profileOption.setOnMouseClicked(
                e -> LandingPage.showUserProfilePage()
        );

        profileOption.setOnMouseEntered(
                e -> profileOption.setStyle(
                        "-fx-background-color:#26354A;" +
                        "-fx-background-radius:8;" +
                        "-fx-cursor:hand;"
                )
        );

        profileOption.setOnMouseExited(
                e -> profileOption.setStyle(
                        "-fx-background-color:transparent;" +
                        "-fx-background-radius:8;" +
                        "-fx-cursor:hand;"
                )
        );

        HBox profileBox =
                new HBox(
                        10,
                        bellBtn,
                        profileOption
                );

        profileBox.setAlignment(
                Pos.CENTER
        );

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
                        ResponsiveUtil.PAGE_PADDING,
                        14,
                        ResponsiveUtil.PAGE_PADDING
                )
        );

        topBar.setStyle(
                "-fx-background-color:" +
                BG_SIDEBAR + ";" +
                "-fx-border-color:" +
                SIDEBAR_BORDER + ";" +
                "-fx-border-width:0 0 1 0;"
        );

        // =====================================================
        // GREETING
        // =====================================================

        Label welcomeTitle =
                new Label(
                        "Good afternoon, " +
                        activeUserName
                );

        welcomeTitle.setStyle(
                "-fx-font-family:" + FONT + ";" +
                "-fx-font-size:24px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" +
                TEXT_LIGHT + ";"
        );

        Label welcomeSub =
                new Label(
                        "Manage your files, spaces and AI-organized content from one place."
                );

        welcomeSub.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT + ";" +
                "-fx-font-weight:500;"
        );

        VBox greetingText =
                new VBox(
                        4,
                        welcomeTitle,
                        welcomeSub
                );

        // =====================================================
        // SCAN FOLDER
        // =====================================================

        Button addFilebtn =
                new Button("⛶  Add File");

        addFilebtn.setStyle(
                "-fx-font-family:" + FONT + ";" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:700;" +
                "-fx-background-color:" +
                PRIMARY_BLUE + ";" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:10;" +
                "-fx-cursor:hand;" +
                "-fx-padding:12 20;"
        );

        // =====================================================
        // IMPORTANT CHANGE
        //
        // Scan Folder no longer opens DirectoryChooser here.
        // It navigates to the real AddFileData page.
        // =====================================================

        addFilebtn.setOnAction(e ->
                LandingPage.setScene(
                        new AddFileData(
                                () -> LandingPage.showUserDashboard()
                        ).getScene()
                )
        );

        AnchorPane greetingHeader =
                new AnchorPane(
                        greetingText,
                        addFilebtn
                );

        AnchorPane.setTopAnchor(
                greetingText,
                0.0
        );

        AnchorPane.setLeftAnchor(
                greetingText,
                0.0
        );

        AnchorPane.setTopAnchor(
                addFilebtn,
                0.0
        );

        AnchorPane.setRightAnchor(
                addFilebtn,
                0.0
        );

        // =====================================================
        // METRIC CARDS
        // =====================================================

        HBox card1 =
                createMetricCard(
                        "📁",
                        "Indexing Activity",
                        "Loading...",
                        "● Syncing",
                        "From Firestore",
                        "#2563EB",
                        "#CADDF2",
                        "#1D4ED8"
                );

        HBox card2 =
                createMetricCard(
                        "▦",
                        "Active Spaces",
                        "6 Spaces",
                        "AI organized",
                        "Personal · College · Office",
                        "#0284C7",
                        "#BAE6FD",
                        "#0369A1"
                );

        HBox card3 =
                createMetricCard(
                        "💾",
                        "Indexed Storage",
                        "Loading...",
                        "● Syncing",
                        "From Firestore",
                        "#059669",
                        "#A7F3D0",
                        "#065F46"
                );

        HBox card4 =
                createMetricCard(
                        "✦",
                        "AI Actions Live",
                        "126 Actions",
                        "⚡ Live pipeline",
                        "12 summaries · 8 links",
                        "#D97706",
                        "#FDE68A",
                        "#92400E"
                );

        HBox metricsRow =
                new HBox(
                        14,
                        card1,
                        card2,
                        card3,
                        card4
                );

        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);
        HBox.setHgrow(card4, Priority.ALWAYS);

        // =====================================================
        // SPACE OCCUPANCY
        // =====================================================

        Label cardTitle =
                new Label("Space Occupancy");

        cardTitle.setStyle(
                "-fx-font-size:17px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" +
                TEXT_DARK + ";"
        );

        Label cardSub =
                new Label(
                        "Overview of file storage across your spaces."
                );

        cardSub.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:" +
                TEXT_MUTED_DARK + ";"
        );

        VBox cardHeaderTitles =
                new VBox(
                        2,
                        cardTitle,
                        cardSub
                );

        Button viewAllBtn =
                new Button(
                        "View all spaces ›"
                );

        viewAllBtn.setStyle(
                "-fx-background-color:" +
                BG_CARD_INNER + ";" +
                "-fx-border-color:" +
                BORDER_CARD + ";" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:" +
                PRIMARY_BLUE + ";" +
                "-fx-padding:6 14;" +
                "-fx-cursor:hand;"
        );

        viewAllBtn.setOnAction(e ->{
                LandingPage.showUserSpace();
        }
        );

        HBox cardHeader =
                new HBox(
                        cardHeaderTitles,
                        new Region(),
                        viewAllBtn
                );

        HBox.setHgrow(
                cardHeader.getChildren().get(1),
                Priority.ALWAYS
        );

        cardHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        ObservableList<PieChart.Data> pieChartData=FXCollections.observableArrayList();
        PieChart chart=new PieChart(pieChartData);
        chart.setLabelsVisible(false);
        chart.setLegendVisible(false);
        chart.setPrefSize(205,205);
        chart.setMaxSize(205,205);
        Circle donutHole=new Circle(66,Color.web(BG_CARD));
        Label chartValText=new Label("Loading...");
        chartValText.setStyle("-fx-font-size:18px;-fx-font-weight:700;-fx-text-fill:"+TEXT_DARK+";");
        Label chartSubText=new Label("from Firestore");
        chartSubText.setStyle("-fx-font-size:11px;-fx-text-fill:"+TEXT_MUTED_DARK+";");
        VBox chartCenterText=new VBox(2,chartValText,chartSubText);
        chartCenterText.setAlignment(Pos.CENTER);
        StackPane donutChartPane=new StackPane(chart,donutHole,chartCenterText);
        HBox tableHeader=new HBox(createHeaderLabel("Space",200),createHeaderLabel("Storage Used",110),createHeaderLabel("Percentage",140));
        VBox spaceRows=new VBox(11,tableHeader,new Label("Loading..."));

        HBox cardContent =
                new HBox(
                        28,
                        donutChartPane,
                        spaceRows
                );

        cardContent.setAlignment(
                Pos.CENTER_LEFT
        );

        Label lastUpdated =
                new Label(
                        "🕒  Last updated just now"
                );

        lastUpdated.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:" +
                TEXT_MUTED_DARK + ";"
        );

        VBox occupancyCard =
                new VBox(
                        16,
                        cardHeader,
                        cardContent,
                        lastUpdated
                );

        occupancyCard.setPadding(
                new Insets(24)
        );

        occupancyCard.setStyle(
                "-fx-background-color:" +
                BG_CARD + ";" +
                "-fx-border-color:" +
                BORDER_CARD + ";" +
                "-fx-border-radius:16;" +
                "-fx-background-radius:16;" +
                "-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.18),16,0,0,6);"
        );

        loadDashboardData(pieChartData,chart,chartValText,chartSubText,spaceRows,lastUpdated,card1,card2,card3);

        // =====================================================
        // CONTENT
        // =====================================================

        VBox contentBody =
                new VBox(
                        22,
                        greetingHeader,
                        metricsRow,
                        occupancyCard
                );

        contentBody.setPadding(
                new Insets(
                        24,
                        ResponsiveUtil.PAGE_PADDING,
                        28,
                        ResponsiveUtil.PAGE_PADDING
                )
        );

        contentBody.setStyle(
                "-fx-background-color:" +
                BG_CENTER_CANVAS + ";"
        );

        ScrollPane scrollPane =
                new ScrollPane(
                        contentBody
                );

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color:" +
                BG_CENTER_CANVAS + ";" +
                "-fx-background:" +
                BG_CENTER_CANVAS + ";" +
                "-fx-background-insets:0;" +
                "-fx-padding:0;"
        );

        VBox mainArea =
                new VBox(
                        topBar,
                        scrollPane
                );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        mainArea.setStyle(
                "-fx-background-color:" +
                BG_CENTER_CANVAS + ";"
        );

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color:" +
                BG_SIDEBAR + ";"
        );

        root.setLeft(sidebar);
        root.setCenter(mainArea);

        Scene scene =
                new Scene(
                        root,
                        LandingPage.getCurrentWidth(),
                        LandingPage.getCurrentHeight()
                );

        Platform.runLater(
                () -> applyPieChartColors(pieChartData)
        );

        return scene;
    }

    private void loadDashboardData(ObservableList<PieChart.Data> chartData,PieChart chart,Label totalLabel,Label subLabel,VBox rows,Label updated,HBox card1,HBox card2,HBox card3){
        UserSession session=UserSession.getInstance();
        if(session==null||!UserSession.isLoggedIn()||session.getUid()==null||session.getUid().isBlank()) return;
        Thread t=new Thread(()->{
            try{
                List<FileData> files=fileDAO.getFileSummaries(session.getUid());
                String[] ids={"personal","college","office","finance","entertainment","other"};
                String[] names={"Personal","College","Office","Finance","Entertainment","Others"};
                long[] totals=new long[ids.length];
                long total=0;
                for(FileData f:files){
                    total+=f.getFileSize();
                    String id=f.getSpaceId();
                    if(id!=null) for(int i=0;i<ids.length;i++) if(ids[i].equalsIgnoreCase(id)){totals[i]+=f.getFileSize();break;}
                }
                final long totalBytes=total;
                Platform.runLater(()->{
                    chartData.clear();
                    rows.getChildren().setAll(tableHeaderNode(rows));
                    for(int i=0;i<ids.length;i++){
                        double pct=totalBytes==0?0:(totals[i]*100.0/totalBytes);
                        chartData.add(new PieChart.Data(names[i],totals[i]));
                        rows.getChildren().add(createSpaceRow("📁",CHART_COLORS[i%CHART_COLORS.length],names[i],formatSize(totals[i]),pct/100,String.format("%.0f%%",pct),CHART_COLORS[i%CHART_COLORS.length]));
                    }
                    totalLabel.setText(formatSize(totalBytes));
                    subLabel.setText("across "+files.size()+" files");
                    updated.setText("🕒 Last updated just now");
                    setMetricValue(card1,files.size()+" Files","● Indexed");
                    setMetricValue(card3,formatSize(totalBytes),"● Synced");
                    applyPieChartColors(chartData);
                });
            }catch(Exception e){
                Platform.runLater(()->updated.setText("ⓘ Unable to load dashboard data"));
                System.out.println("[Dashboard] Unable to load files: "+e.getMessage());
            }
        });
        t.setDaemon(true);t.start();
    }

        private String formatSize(long bytes){
                if(bytes<1024) return bytes+" B";
                if(bytes<1024*1024) return String.format("%.1f KB",bytes/1024.0);
                if(bytes<1024*1024*1024) return String.format("%.1f MB",bytes/(1024.0*1024));
                return String.format("%.1f GB",bytes/(1024.0*1024*1024));
        }

    private javafx.scene.Node tableHeaderNode(VBox rows){ return rows.getChildren().get(0); }

    private void setMetricValue(HBox card,String value,String badge){
        if(card.getChildren().isEmpty()||!(card.getChildren().get(0) instanceof VBox)) return;
        VBox content=(VBox)card.getChildren().get(0);
        if(content.getChildren().size()>1&&content.getChildren().get(1) instanceof Label) ((Label)content.getChildren().get(1)).setText(value);
        if(content.getChildren().size()>2&&content.getChildren().get(2) instanceof HBox){
            HBox bottom=(HBox)content.getChildren().get(2);
            if(!bottom.getChildren().isEmpty()&&bottom.getChildren().get(0) instanceof Label) ((Label)bottom.getChildren().get(0)).setText(badge);
        }
    }

    // =========================================================
    // LOGO
    // =========================================================

    private StackPane createOneSpaceLogo() {

        Image logoImage =
                new Image(
                        getClass().getResourceAsStream(
                                "/assets/logo/OneSpace_logo.png"
                        )
                );

        ImageView logoView =
                new ImageView(logoImage);

        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane pane =
                new StackPane(logoView);

        pane.setPrefSize(
                42,
                42
        );

        pane.setAlignment(
                Pos.CENTER
        );

        return pane;
    }

    // =========================================================
    // SIDEBAR BUTTON
    // =========================================================

    private Button createSidebarButton(
            String icon,
            String label,
            boolean isActive
    ) {

        Label iconLbl =
                new Label(icon);

        iconLbl.setFont(
                Font.font(
                        FONT,
                        14
                )
        );

        Label textLbl =
                new Label(label);

        textLbl.setStyle(
                "-fx-font-family:" + FONT + ";" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:" +
                (isActive ? "700" : "500") + ";" +
                "-fx-text-fill:" +
                TEXT_LIGHT + ";"
        );

        HBox content =
                new HBox(
                        12,
                        iconLbl,
                        textLbl
                );

        content.setAlignment(
                Pos.CENTER_LEFT
        );

        Button btn =
                new Button(
                        "",
                        content
                );

        btn.setMaxWidth(
                Double.MAX_VALUE
        );

        btn.setPrefHeight(38);

        btn.setAlignment(
                Pos.CENTER_LEFT
        );

        btn.setPadding(
                new Insets(
                        0,
                        12,
                        0,
                        12
                )
        );

        if (isActive) {

            btn.setStyle(
                    "-fx-background-color:" +
                    PRIMARY_BLUE + ";" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;"
            );

        } else {

            btn.setStyle(
                    "-fx-background-color:transparent;" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;"
            );

            btn.setOnMouseEntered(
                    e -> btn.setStyle(
                            "-fx-background-color:#26354A;" +
                            "-fx-background-radius:8;" +
                            "-fx-cursor:hand;"
                    )
            );

            btn.setOnMouseExited(
                    e -> btn.setStyle(
                            "-fx-background-color:transparent;" +
                            "-fx-background-radius:8;" +
                            "-fx-cursor:hand;"
                    )
            );
        }

        return btn;
    }

    // =========================================================
    // METRIC CARD
    // =========================================================

    private HBox createMetricCard(
            String icon,
            String title,
            String value,
            String badgeText,
            String subText,
            String accentColor,
            String bgAccent,
            String textBadgeColor
    ) {

        Label titleLbl =
                new Label(title);

        titleLbl.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" +
                TEXT_MUTED_DARK + ";"
        );

        Label iconLbl =
                new Label(icon);

        iconLbl.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:" +
                accentColor + ";"
        );

        Label iconBox =
                new Label("", iconLbl);

        iconBox.setPrefSize(
                32,
                32
        );

        iconBox.setAlignment(
                Pos.CENTER
        );

        iconBox.setStyle(
                "-fx-background-color:" +
                bgAccent + ";" +
                "-fx-background-radius:8;"
        );

        HBox topRow =
                new HBox(
                        titleLbl,
                        new Region(),
                        iconBox
                );

        HBox.setHgrow(
                topRow.getChildren().get(1),
                Priority.ALWAYS
        );

        topRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label valLbl =
                new Label(value);

        valLbl.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" +
                TEXT_DARK + ";"
        );

        Label subLbl =
                new Label(subText);

        subLbl.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:" +
                TEXT_MUTED_DARK + ";"
        );

        Label badgeLbl =
                new Label(badgeText);

        badgeLbl.setStyle(
                "-fx-font-size:10px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" +
                textBadgeColor + ";" +
                "-fx-background-color:" +
                bgAccent + ";" +
                "-fx-background-radius:6;" +
                "-fx-padding:3 8;"
        );

        HBox bottomRow =
                new HBox(
                        6,
                        badgeLbl,
                        subLbl
                );

        bottomRow.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox cardContent =
                new VBox(
                        8,
                        topRow,
                        valLbl,
                        bottomRow
                );

        HBox card =
                new HBox(
                        cardContent
                );

        HBox.setHgrow(
                cardContent,
                Priority.ALWAYS
        );

        card.setPadding(
                new Insets(16)
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setStyle(
                "-fx-background-color:" +
                BG_CARD + ";" +
                "-fx-border-color:" +
                BORDER_CARD + ";" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;" +
                "-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.14),12,0,0,4);"
        );

        return card;
    }

    // =========================================================
    // HEADER LABEL
    // =========================================================

    private Label createHeaderLabel(
            String text,
            double width
    ) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" +
                TEXT_MUTED_DARK + ";"
        );

        label.setPrefWidth(width);

        return label;
    }

    // =========================================================
    // SPACE ROW
    // =========================================================

    private HBox createSpaceRow(
            String icon,
            String iconHex,
            String title,
            String storage,
            double progress,
            String percent,
            String colorHex
    ) {

        Label folderIcon =
                new Label(icon);

        folderIcon.setPrefSize(
                24,
                24
        );

        folderIcon.setAlignment(
                Pos.CENTER
        );

        folderIcon.setStyle(
                "-fx-background-color:" +
                iconHex + "22;" +
                "-fx-background-radius:6;" +
                "-fx-text-fill:" +
                iconHex + ";"
        );

        Label spaceName =
                new Label(title);

        spaceName.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" +
                TEXT_DARK + ";"
        );

        HBox nameGroup =
                new HBox(
                        10,
                        folderIcon,
                        spaceName
                );

        nameGroup.setAlignment(
                Pos.CENTER_LEFT
        );

        nameGroup.setPrefWidth(200);

        Label sizeLbl =
                new Label(storage);

        sizeLbl.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" +
                TEXT_DARK + ";"
        );

        sizeLbl.setPrefWidth(110);

        ProgressBar bar =
                new ProgressBar(progress);

        bar.setPrefWidth(90);
        bar.setPrefHeight(6);

        bar.setStyle(
                "-fx-accent:" +
                colorHex + ";" +
                "-fx-control-inner-background:#B6CDE7;"
        );

        Label percentLbl =
                new Label(percent);

        percentLbl.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:700;" +
                "-fx-text-fill:" +
                TEXT_MUTED_DARK + ";"
        );

        percentLbl.setPrefWidth(40);

        HBox progressGroup =
                new HBox(
                        10,
                        bar,
                        percentLbl
                );

        progressGroup.setAlignment(
                Pos.CENTER_LEFT
        );

        progressGroup.setPrefWidth(140);

        return new HBox(
                nameGroup,
                sizeLbl,
                progressGroup
        );
    }

    // =========================================================
    // PIE COLORS
    // =========================================================

    private void applyPieChartColors(
            ObservableList<PieChart.Data> data
    ) {

        int i = 0;

        for (PieChart.Data item : data) {

            if (item.getNode() != null) {

                item.getNode().setStyle(
                        "-fx-pie-color:" +
                        CHART_COLORS[
                                i % CHART_COLORS.length
                        ] + ";"
                );
            }

            i++;
        }
    }
}