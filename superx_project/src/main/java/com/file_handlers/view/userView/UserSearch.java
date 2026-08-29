package com.file_handlers.view.userView;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.model.FileData;
import com.file_handlers.util.ResponsiveUtil;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserSearch {
    private final FileDAO fileDAO=new FileDAO();
    private int searchVersion=0;

    // ---------------------------------------------------------
    // Style
    // ---------------------------------------------------------

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

    // ---------------------------------------------------------
    // File data
    // ---------------------------------------------------------

    private final List<FileInfo> files =
            new ArrayList<>();

    private VBox listContainer;
    private GridPane gridContainer;
    private StackPane contentBox;

    private String selectedType = "All";
    private String searchQuery = "";
    private boolean isGridView = false;

    // ---------------------------------------------------------
    // Main scene
    // ---------------------------------------------------------

    public Scene getUserSearchScene() {

        String activeUserName = "User";
        String initials = "U";

        if (UserSession.getInstance() != null &&
                UserSession.getInstance().getDisplayName() != null) {

            String fullName =
                    UserSession.getInstance()
                            .getDisplayName()
                            .trim();

            if (!fullName.isEmpty()) {
                activeUserName =
                        fullName.split("\\s+")[0];

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
                "-fx-text-fill:" +
                TEXT_LIGHT +
                ";"
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
                new VBox(logoHeader);

        logoBox.setPadding(
                new Insets(0, 0, 18, 6)
        );

        Button dashboardBtn =
                createSidebarButton(
                        "⌂",
                        "Dashboard",
                        false
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
                        true
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

        dashboardBtn.setOnAction(
                e -> LandingPage.showUserDashboard()
        );

        spacesBtn.setOnAction(
                e -> LandingPage.showUserSpace()
        );

        searchBtn.setOnAction(
                e -> LandingPage.showUserSearch()
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

        logoutBtn.setOnAction(
                e -> LandingPage.showUserLoginPage()
        );

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

        // -----------------------------------------------------
        // Storage
        // -----------------------------------------------------

        Label storageTitle =
                new Label("Storage Used");

        storageTitle.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        12
                )
        );

        storageTitle.setStyle(
                "-fx-text-fill:" +
                TEXT_LIGHT +
                ";"
        );

        Label storageVal =
                new Label("64.2 GB of 100 GB");

        storageVal.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        12
                )
        );

        storageVal.setStyle(
                "-fx-text-fill:" +
                TEXT_LIGHT +
                ";"
        );

        Label storagePercent =
                new Label("64%");

        storagePercent.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        11
                )
        );

        storagePercent.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT +
                ";"
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
                "-fx-accent:" +
                PRIMARY_BLUE +
                ";" +
                "-fx-control-inner-background:#0E1520;"
        );

        Button manageStorageBtn =
                new Button("Manage Storage ›");

        manageStorageBtn.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        11
                )
        );

        manageStorageBtn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#60A5FA;" +
                "-fx-padding:2 0 0 0;" +
                "-fx-cursor:hand;"
        );

        manageStorageBtn.setOnAction(
                e -> LandingPage.showLandingPage()
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
                BG_SIDEBAR_CARD +
                ";" +
                "-fx-border-color:" +
                SIDEBAR_BORDER +
                ";" +
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
                        8,
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
                BG_SIDEBAR +
                ";" +
                "-fx-border-color:" +
                SIDEBAR_BORDER +
                ";" +
                "-fx-border-width:0 1 0 0;"
        );

        // =====================================================
        // TOP SEARCH BAR
        // =====================================================

        Label searchIcon =
                new Label("⌕");

        searchIcon.setFont(
                Font.font(FONT, 16)
        );

        searchIcon.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT +
                ";"
        );

        TextField topSearchField =
                new TextField();

        topSearchField.setPromptText(
                "Search in OneSpace..."
        );

        topSearchField.setPrefHeight(38);

        topSearchField.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-prompt-text-fill:" +
                TEXT_MUTED_LIGHT +
                ";" +
                "-fx-font-size:13px;" +
                "-fx-text-fill:" +
                TEXT_LIGHT +
                ";"
        );

        Label keyShortcut =
                new Label("⌘ K");

        keyShortcut.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        10
                )
        );

        keyShortcut.setStyle(
                "-fx-background-color:#141E2C;" +
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT +
                ";" +
                "-fx-padding:3 6;" +
                "-fx-background-radius:4;"
        );

        HBox topSearchContainer =
                new HBox(
                        8,
                        searchIcon,
                        topSearchField,
                        keyShortcut
                );

        topSearchContainer.setAlignment(
                Pos.CENTER_LEFT
        );

        topSearchContainer.setPadding(
                new Insets(0, 12, 0, 14)
        );

        topSearchContainer.setPrefWidth(420);

        topSearchContainer.setStyle(
                "-fx-background-color:#141E2C;" +
                "-fx-border-color:" +
                SIDEBAR_BORDER +
                ";" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;"
        );

        HBox.setHgrow(
                topSearchField,
                Priority.ALWAYS
        );

        Button bellBtn =
                new Button("🔔");

        bellBtn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-font-size:16px;" +
                "-fx-text-fill:" +
                TEXT_LIGHT +
                ";" +
                "-fx-cursor:hand;"
        );

        bellBtn.setOnAction(
                e -> LandingPage.showNotificationPage()
        );

        Label avatar =
                new Label(initials);

        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color:" +
                PRIMARY_BLUE +
                ";" +
                "-fx-background-radius:50%;" +
                "-fx-text-fill:" +
                TEXT_LIGHT +
                ";" +
                "-fx-font-weight:bold;" +
                "-fx-font-size:12px;"
        );

        Label userName =
                new Label(activeUserName);

        userName.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        13
                )
        );

        userName.setStyle(
                "-fx-text-fill:" +
                TEXT_LIGHT +
                ";"
        );

        Label dropDown =
                new Label("⌄");

        dropDown.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT +
                ";"
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

        Region topSpacer =
                new Region();

        HBox.setHgrow(
                topSpacer,
                Priority.ALWAYS
        );

        HBox topBar =
                new HBox(
                        20,
                        topSearchContainer,
                        topSpacer,
                        profileBox
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
                BG_SIDEBAR +
                ";" +
                "-fx-border-color:" +
                SIDEBAR_BORDER +
                ";" +
                "-fx-border-width:0 0 1 0;"
        );

        // =====================================================
        // SEARCH HEADER
        // =====================================================

        Label titleLabel =
                new Label("Search files");

        titleLabel.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        24
                )
        );

        titleLabel.setStyle(
                "-fx-text-fill:" +
                TEXT_LIGHT +
                ";"
        );

        Label subLabel =
                new Label(
                        "Search and discover files indexed by OneSpace."
                );

        subLabel.setFont(
                Font.font(FONT, 13)
        );

        subLabel.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT +
                ";"
        );

        VBox titleBox =
                new VBox(
                        4,
                        titleLabel,
                        subLabel
                );

        // =====================================================
        // MAIN SEARCH FIELD
        // =====================================================

        Label mainSearchIcon =
                new Label("⌕");

        mainSearchIcon.setFont(
                Font.font(FONT, 18)
        );

        mainSearchIcon.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT +
                ";"
        );

        TextField mainSearchField =
                new TextField();

        mainSearchField.setPromptText(
                "Search anything about your files..."
        );

        mainSearchField.setPrefHeight(44);

        mainSearchField.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:" +
                TEXT_LIGHT +
                ";" +
                "-fx-prompt-text-fill:" +
                TEXT_MUTED_LIGHT +
                ";" +
                "-fx-font-size:14px;"
        );

        PauseTransition searchDelay=new PauseTransition(Duration.millis(300));
        mainSearchField.textProperty().addListener((o,oldValue,newValue)->{
            searchQuery=newValue==null?"":newValue.trim().toLowerCase();
            searchDelay.stop();
            searchDelay.setOnFinished(e->loadSearchResults(searchQuery));
            searchDelay.playFromStart();
        });

        HBox searchBarBox =
                new HBox(
                        10,
                        mainSearchIcon,
                        mainSearchField
                );

        searchBarBox.setAlignment(
                Pos.CENTER_LEFT
        );

        searchBarBox.setPadding(
                new Insets(0, 16, 0, 16)
        );

        searchBarBox.setStyle(
                "-fx-background-color:" +
                BG_SIDEBAR_CARD +
                ";" +
                "-fx-border-color:" +
                SIDEBAR_BORDER +
                ";" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;"
        );

        HBox.setHgrow(
                mainSearchField,
                Priority.ALWAYS
        );

        // =====================================================
        // FILTER
        // =====================================================

        MenuButton filterBtn =
                new MenuButton("Filter: All");

        styleDropdownMenu(filterBtn);

        MenuItem filterAll =
                new MenuItem("All Files");

        MenuItem filterDocs =
                new MenuItem("Documents");

        MenuItem filterImgs =
                new MenuItem("Images");

        MenuItem filterVids =
                new MenuItem("Videos");

        MenuItem filterPdfs =
                new MenuItem("PDFs");

        filterBtn.getItems().addAll(
                filterAll,
                filterDocs,
                filterImgs,
                filterVids,
                filterPdfs
        );

        filterAll.setOnAction(
                e -> applyFilter(
                        filterBtn,
                        "All",
                        "Filter: All"
                )
        );

        filterDocs.setOnAction(
                e -> applyFilter(
                        filterBtn,
                        "Documents",
                        "Filter: Documents"
                )
        );

        filterImgs.setOnAction(
                e -> applyFilter(
                        filterBtn,
                        "Images",
                        "Filter: Images"
                )
        );

        filterVids.setOnAction(
                e -> applyFilter(
                        filterBtn,
                        "Videos",
                        "Filter: Videos"
                )
        );

        filterPdfs.setOnAction(
                e -> applyFilter(
                        filterBtn,
                        "PDFs",
                        "Filter: PDFs"
                )
        );

        // =====================================================
        // AI ANSWER CARD
        // =====================================================

        Label aiTitle =
                new Label("✦ AI Answer");

        aiTitle.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        15
                )
        );

        aiTitle.setStyle(
                "-fx-text-fill:" +
                TEXT_DARK +
                ";"
        );

        Label confidenceBadge =
                new Label("Search ready");

        confidenceBadge.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        10
                )
        );

        confidenceBadge.setStyle(
                "-fx-text-fill:#15803D;" +
                "-fx-background-color:#DCFCE7;" +
                "-fx-background-radius:6;" +
                "-fx-padding:3 8;"
        );

        Region aiSpacer =
                new Region();

        HBox.setHgrow(
                aiSpacer,
                Priority.ALWAYS
        );

        HBox aiHeader =
                new HBox(
                        aiTitle,
                        aiSpacer,
                        confidenceBadge
                );

        aiHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        Label aiText =
                new Label(
                        "Search results are loaded from your personal Firestore file collection."
                );

        aiText.setFont(
                Font.font(FONT, 13)
        );

        aiText.setWrapText(true);

        aiText.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_DARK +
                ";"
        );

        Button actionBtn1 =
                createCardActionButton(
                        "Open best match"
                );

        Button actionBtn2 =
                createCardActionButton(
                        "Create reminder"
                );

        Button actionBtn3 =
                createCardActionButton(
                        "Add to Space"
                );

        HBox actionRow =
                new HBox(
                        8,
                        actionBtn1,
                        actionBtn2,
                        actionBtn3
                );

        VBox aiCard =
                new VBox(
                        12,
                        aiHeader,
                        aiText,
                        actionRow
                );

        aiCard.setPadding(
                new Insets(18)
        );

        aiCard.setStyle(
                "-fx-background-color:" +
                BG_CARD +
                ";" +
                "-fx-border-color:" +
                BORDER_CARD +
                ";" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;" +
                "-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.14),12,0,0,4);"
        );

        // =====================================================
        // RESULTS
        // =====================================================

        Label resultsHeader =
                new Label("Results");

        resultsHeader.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        18
                )
        );

        resultsHeader.setStyle(
                "-fx-text-fill:" +
                TEXT_LIGHT +
                ";"
        );

        MenuButton viewBtn =
                new MenuButton("List View");

        styleDropdownMenu(viewBtn);

        MenuItem listViewOption =
                new MenuItem("List View");

        MenuItem gridViewOption =
                new MenuItem("Grid View");

        viewBtn.getItems().addAll(
                listViewOption,
                gridViewOption
        );

        listViewOption.setOnAction(e -> {
            isGridView = false;
            viewBtn.setText("List View");
            updateResultsView();
        });

        gridViewOption.setOnAction(e -> {
            isGridView = true;
            viewBtn.setText("Grid View");
            updateResultsView();
        });

        HBox rightControls =
                new HBox(
                        10,
                        filterBtn,
                        viewBtn
                );

        rightControls.setAlignment(
                Pos.CENTER_RIGHT
        );

        Region resultsSpacer =
                new Region();

        HBox.setHgrow(
                resultsSpacer,
                Priority.ALWAYS
        );

        HBox resultsBar =
                new HBox(
                        resultsHeader,
                        resultsSpacer,
                        rightControls
                );

        resultsBar.setAlignment(
                Pos.CENTER_LEFT
        );

        listContainer =
                new VBox(12);

        gridContainer =
                new GridPane();

        gridContainer.setHgap(14);
        gridContainer.setVgap(14);

        contentBox =
                new StackPane();

        Label loadingLabel =
                new Label("Loading your files...");

        loadingLabel.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT +
                ";"
        );

        contentBox.getChildren().add(
                loadingLabel
        );

        // =====================================================
        // CONTENT
        // =====================================================

        VBox contentBody =
                new VBox(
                        20,
                        titleBox,
                        searchBarBox,
                        aiCard,
                        resultsBar,
                        contentBox
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
                BG_CENTER_CANVAS +
                ";"
        );

        ScrollPane scrollPane =
                new ScrollPane(contentBody);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color:" +
                BG_CENTER_CANVAS +
                ";" +
                "-fx-background:" +
                BG_CENTER_CANVAS +
                ";" +
                "-fx-background-insets:0;" +
                "-fx-padding:0;"
        );

        VBox mainArea =
                new VBox(
                        topBar,
                        scrollPane
                );

        mainArea.setStyle(
                "-fx-background-color:" +
                BG_CENTER_CANVAS +
                ";"
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color:" +
                BG_SIDEBAR +
                ";"
        );

        root.setLeft(sidebar);
        root.setCenter(mainArea);

        // =====================================================
        // LOAD REAL USER FILES
        // =====================================================

        loadFilesFromFirestore();

        return new Scene(
                root,
                LandingPage.getCurrentWidth(),
                LandingPage.getCurrentHeight()
        );
    }

    // =========================================================
    // FIRESTORE
    // =========================================================

    private void loadFilesFromFirestore(){
        UserSession session=UserSession.getInstance();
        if(session==null||!UserSession.isLoggedIn()){
            files.clear(); updateResultsView(); return;
        }
        loadSearchResults("");
    }

    private void loadSearchResults(String query){
        UserSession session=UserSession.getInstance();
        if(session==null||!UserSession.isLoggedIn()) return;
        int version=++searchVersion;
        Thread thread=new Thread(()->{
            try{
                List<FileData> data=query==null||query.isBlank()
                        ? fileDAO.getFileSummaries(session.getUid())
                        : fileDAO.searchFileSummaries(session.getUid(),query);
                List<FileInfo> loaded=new ArrayList<>();
                for(FileData file:data){
                    String name=file.getFileName();
                    loaded.add(new FileInfo(getExtension(file.getFileType(),name),name,file.getLocalPath(),formatSize(file.getFileSize()),formatDate(file.getUploadedAt())));
                }
                Platform.runLater(()->{
                    if(version!=searchVersion) return;
                    files.clear(); files.addAll(loaded); updateResultsView();
                });
            }catch(Exception e){
                Platform.runLater(()->{
                    if(version!=searchVersion) return;
                    files.clear(); updateResultsView();
                });
                System.out.println("[Search] Unable to load files: "+e.getMessage());
            }
        });
        thread.setDaemon(true); thread.start();
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private boolean matchesSearchQuery(FileInfo file){
        if (searchQuery == null || searchQuery.isBlank()) {
            return true;
        }
        String q = searchQuery.toLowerCase();
        return (file.name != null && file.name.toLowerCase().contains(q)) ||
               (file.path != null && file.path.toLowerCase().contains(q)) ||
               (file.type != null && file.type.toLowerCase().contains(q));
    }

    private boolean matchesType(
            FileInfo file
    ) {

        if (selectedType.equals("All")) {
            return true;
        }

        if (selectedType.equals("PDFs")) {
            return file.type.equals("PDF");
        }

        if (selectedType.equals("Documents")) {
            return file.type.equals("DOC")
                    || file.type.equals("DOCX")
                    || file.type.equals("PPT")
                    || file.type.equals("PPTX")
                    || file.type.equals("XLS")
                    || file.type.equals("XLSX")
                    || file.type.equals("TXT");
        }

        if (selectedType.equals("Images")) {
            return file.type.equals("JPG")
                    || file.type.equals("JPEG")
                    || file.type.equals("PNG")
                    || file.type.equals("GIF")
                    || file.type.equals("WEBP");
        }

        if (selectedType.equals("Videos")) {
            return file.type.equals("MP4")
                    || file.type.equals("AVI")
                    || file.type.equals("MKV")
                    || file.type.equals("MOV");
        }

        return true;
    }

    // =========================================================
    // RESULTS
    // =========================================================

    private void updateResultsView() {

        if (isGridView) {
            renderGrid();
        } else {
            renderList();
        }
    }

    private void renderList() {

        listContainer
                .getChildren()
                .clear();

        int count = 0;

        for (FileInfo file : files) {

            if (matchesType(file) &&
                    matchesSearchQuery(file)) {

                listContainer
                        .getChildren()
                        .add(
                                createFileCard(
                                        file,
                                        false
                                )
                        );

                count++;
            }
        }

        if (count == 0) {
            listContainer
                    .getChildren()
                    .add(
                            createEmptyLabel()
                    );
        }

        contentBox
                .getChildren()
                .setAll(
                        listContainer
                );
    }

    private void renderGrid() {

        gridContainer
                .getChildren()
                .clear();

        int col = 0;
        int row = 0;
        int count = 0;

        for (FileInfo file : files) {

            if (matchesType(file) &&
                    matchesSearchQuery(file)) {

                gridContainer.add(
                        createFileCard(
                                file,
                                true
                        ),
                        col,
                        row
                );

                col++;

                if (col == 2) {
                    col = 0;
                    row++;
                }

                count++;
            }
        }

        if (count == 0) {
            gridContainer.add(
                    createEmptyLabel(),
                    0,
                    0
            );
        }

        contentBox
                .getChildren()
                .setAll(
                        gridContainer
                );
    }

    private Label createEmptyLabel() {

        Label label =
                new Label(
                        searchQuery.isBlank()
                                ? "No files found."
                                : "No files found matching your search."
                );

        label.setFont(
                Font.font(FONT, 13)
        );

        label.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_LIGHT +
                ";"
        );

        return label;
    }

    // =========================================================
    // FILE CARD
    // =========================================================

    private VBox createFileCard(
            FileInfo file,
            boolean isGrid
    ) {

        Label typeBadge =
                new Label(file.type);

        typeBadge.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        10
                )
        );

        typeBadge.setStyle(
                "-fx-background-color:#DBEAFE;" +
                "-fx-text-fill:" +
                PRIMARY_BLUE +
                ";" +
                "-fx-background-radius:5;" +
                "-fx-padding:2 6;"
        );

        Label sizeLbl =
                new Label(file.size);

        sizeLbl.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        11
                )
        );

        sizeLbl.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_DARK +
                ";"
        );

        Region topSpacer =
                new Region();

        HBox.setHgrow(
                topSpacer,
                Priority.ALWAYS
        );

        HBox topRow =
                new HBox(
                        typeBadge,
                        topSpacer,
                        sizeLbl
                );

        topRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label previewText =
                new Label("FILE PREVIEW");

        previewText.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        10
                )
        );

        previewText.setStyle(
                "-fx-text-fill:" +
                PRIMARY_BLUE +
                ";"
        );

        StackPane previewPane =
                new StackPane(
                        previewText
                );

        previewPane.setPrefHeight(
                isGrid ? 42 : 32
        );

        previewPane.setStyle(
                "-fx-background-color:" +
                BG_CARD_INNER +
                ";" +
                "-fx-background-radius:6;"
        );

        Label nameLbl =
                new Label(file.name);

        nameLbl.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        13
                )
        );

        nameLbl.setStyle(
                "-fx-text-fill:" +
                TEXT_DARK +
                ";"
        );

        nameLbl.setWrapText(true);

        Label pathLbl =
                new Label(file.path);

        pathLbl.setFont(
                Font.font(FONT, 10)
        );

        pathLbl.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_DARK +
                ";"
        );

        pathLbl.setWrapText(true);

        Label dateLbl =
                new Label(file.date);

        dateLbl.setFont(
                Font.font(FONT, 10)
        );

        dateLbl.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_DARK +
                ";"
        );

        Label optionsBtn =
                new Label("⋮");

        optionsBtn.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        13
                )
        );

        optionsBtn.setStyle(
                "-fx-text-fill:" +
                TEXT_MUTED_DARK +
                ";" +
                "-fx-cursor:hand;"
        );

        Region bottomSpacer =
                new Region();

        HBox.setHgrow(
                bottomSpacer,
                Priority.ALWAYS
        );

        HBox bottomRow =
                new HBox(
                        dateLbl,
                        bottomSpacer,
                        optionsBtn
                );

        bottomRow.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox card =
                new VBox(
                        6,
                        topRow,
                        previewPane,
                        nameLbl,
                        pathLbl,
                        bottomRow
                );

        card.setPadding(
                new Insets(10)
        );

        card.setStyle(
                "-fx-background-color:" +
                BG_CARD +
                ";" +
                "-fx-border-color:" +
                BORDER_CARD +
                ";" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;" +
                "-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.08),6,0,0,2);"
        );

        if (isGrid) {
            card.setPrefWidth(300);
        } else {
            card.setMaxWidth(
                    Double.MAX_VALUE
            );
        }

        return card;
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private void applyFilter(
            MenuButton button,
            String type,
            String displayLabel
    ) {

        selectedType = type;
        button.setText(displayLabel);

        updateResultsView();
    }

    private void styleDropdownMenu(
            MenuButton button
    ) {

        button.setPrefHeight(36);

        button.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        12
                )
        );

        button.setStyle(
                "-fx-background-color:" +
                BG_CARD +
                ";" +
                "-fx-border-color:" +
                BORDER_CARD +
                ";" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:" +
                TEXT_DARK +
                ";" +
                "-fx-cursor:hand;"
        );
    }

    private Button createCardActionButton(
            String text
    ) {

        Button button =
                new Button(text);

        button.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        12
                )
        );

        button.setStyle(
                "-fx-background-color:" +
                BG_CARD_INNER +
                ";" +
                "-fx-border-color:" +
                BORDER_CARD +
                ";" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:" +
                PRIMARY_BLUE +
                ";" +
                "-fx-padding:6 12;" +
                "-fx-cursor:hand;"
        );

        return button;
    }

    private Button createSidebarButton(
            String icon,
            String label,
            boolean active
    ) {

        Label iconLabel =
                new Label(icon);

        iconLabel.setFont(
                Font.font(FONT, 14)
        );

        Label textLabel =
                new Label(label);

        textLabel.setFont(
                Font.font(
                        FONT,
                        active
                                ? FontWeight.BOLD
                                : FontWeight.MEDIUM,
                        13
                )
        );

        textLabel.setStyle(
                "-fx-text-fill:" +
                TEXT_LIGHT +
                ";"
        );

        HBox content =
                new HBox(
                        12,
                        iconLabel,
                        textLabel
                );

        content.setAlignment(
                Pos.CENTER_LEFT
        );

        Button button =
                new Button(
                        "",
                        content
                );

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.setPrefHeight(38);

        button.setAlignment(
                Pos.CENTER_LEFT
        );

        button.setPadding(
                new Insets(0, 12, 0, 12)
        );

        if (active) {

            button.setStyle(
                    "-fx-background-color:" +
                    PRIMARY_BLUE +
                    ";" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;"
            );

        } else {

            button.setStyle(
                    "-fx-background-color:transparent;" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;"
            );

            iconLabel.setStyle(
                    "-fx-text-fill:" +
                    TEXT_MUTED_LIGHT +
                    ";"
            );

            button.setOnMouseEntered(
                    e -> button.setStyle(
                            "-fx-background-color:#26354A;" +
                            "-fx-background-radius:8;" +
                            "-fx-cursor:hand;"
                    )
            );

            button.setOnMouseExited(
                    e -> button.setStyle(
                            "-fx-background-color:transparent;" +
                            "-fx-background-radius:8;" +
                            "-fx-cursor:hand;"
                    )
            );
        }

        return button;
    }

    private StackPane createOneSpaceLogo() {

        Image logoImage =
                new Image(
                        getClass()
                                .getResourceAsStream(
                                        "/assets/logo/OneSpace_logo.png"
                                )
                );

        ImageView logoView =
                new ImageView(
                        logoImage
                );

        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane logoPane =
                new StackPane(
                        logoView
                );

        logoPane.setPrefSize(
                42,
                42
        );

        logoPane.setAlignment(
                Pos.CENTER
        );

        return logoPane;
    }

    private String getExtension(String type,String name){
        if(type!=null&&!type.isBlank()){
            String value=type.toUpperCase();
            if(value.contains("/")) value=value.substring(value.lastIndexOf('/')+1);
            if(!value.equals("OCTET-STREAM")) return value;
        }
        if(name!=null&&name.contains(".")) return name.substring(name.lastIndexOf('.')+1).toUpperCase();
        return "FILE";
    }

    private String formatSize(long bytes){
        if(bytes<=0) return "—";
        if(bytes<1024) return bytes+" B";
        double value=bytes/1024.0;
        if(value<1024) return String.format("%.1f KB",value);
        value/=1024;
        if(value<1024) return String.format("%.1f MB",value);
        return String.format("%.1f GB",value/1024);
    }

    private String formatDate(com.google.cloud.Timestamp value){
        return value==null?"—":new SimpleDateFormat("dd MMM yyyy").format(value.toDate());
    }

    // =========================================================
    // FILE MODEL
    // =========================================================

    private static class FileInfo {

        String type;
        String name;
        String path;
        String size;
        String date;

        FileInfo(String type,String name,String path,String size,String date) {

            this.type = type == null
                    ? ""
                    : type;

            this.name = name == null
                    ? ""
                    : name;

            this.path = path == null
                    ? ""
                    : path;

            this.size = size == null
                    ? "—"
                    : size;

            this.date = date == null ? "—" : date;
        }
    }
}