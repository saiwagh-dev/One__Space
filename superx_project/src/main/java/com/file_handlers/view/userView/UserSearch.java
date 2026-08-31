package com.file_handlers.view.userView;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.model.UserSession;
import com.file_handlers.model.FileData;
import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.view.LandingPage;
import javafx.animation.PauseTransition;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserSearch {
    private final FileDAO fileDAO = new FileDAO();
    private int searchVersion = 0;

    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String SIDEBAR_BG = "#070C16", SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BG_INNER = "linear-gradient(to bottom right, rgba(13, 22, 38, 0.9), rgba(8, 14, 26, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)", WHITE = "#FFFFFF", LIGHT_SECONDARY = "#94A3B8", BLUE = "#2563EB";

    private final List<FileInfo> files = new ArrayList<>();
    private VBox listContainer;
    private GridPane gridContainer;
    private StackPane contentBox;
    private String selectedType = "All", searchQuery = "";
    private boolean isGridView = false;

    public Scene getUserSearchScene() {
        String activeUserName = "User", initials = "U";
        if (UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null && !UserSession.getInstance().getDisplayName().isBlank()) {
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

        Label avatar = label(initials, 12, FontWeight.BOLD, WHITE);
        avatar.setMinSize(34, 34); avatar.setPrefSize(34, 34); avatar.setMaxSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");
        applyHoverAnimation(avatar, 1.15, 0);

        Label userName = label(activeUserName, 13, FontWeight.SEMI_BOLD, WHITE);
        Label dropDown = label("⌄", 12, FontWeight.NORMAL, LIGHT_SECONDARY);

        HBox profileOption = new HBox(8, avatar, userName, dropDown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(4, 12, 4, 6));
        profileOption.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");
        applyHoverAnimation(profileOption, 1.04, 0);

        Popup userDropdownPopup = new Popup();
        userDropdownPopup.setAutoHide(true);

        Button profileDropdownBtn = createDropdownBtn("👥   Profile", "#F59E0B", e -> { userDropdownPopup.hide(); Platform.runLater(LandingPage::showUserProfilePage); });
        Button settingsDropdownBtn = createDropdownBtn("⚙   Settings", "#38BDF8", e -> { userDropdownPopup.hide(); Platform.runLater(LandingPage::showSettingPage); });
        Button logoutDropdownBtn = createDropdownBtn("↳   Logout", "#F87171", e -> { userDropdownPopup.hide(); UserSession.clearSession(); Platform.runLater(LandingPage::showUserLoginPage); });

        Separator dropdownSeparator = new Separator();
        dropdownSeparator.setStyle("-fx-background-color: #1E293B; -fx-padding: 4 0;");

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

        VBox titleBox = new VBox(4, label("Search files", 26, FontWeight.BOLD, WHITE), label("Search and discover files indexed by OneSpace.", 13, FontWeight.MEDIUM, LIGHT_SECONDARY));

        SVGPath searchIcon = createIcon("search");
        searchIcon.setStroke(Color.web(LIGHT_SECONDARY));
        searchIcon.setStrokeWidth(2);

        TextField mainSearchField = new TextField();
        mainSearchField.setPromptText("Search anything about your files...");
        mainSearchField.setPrefHeight(44);
        mainSearchField.setStyle("-fx-background-color: transparent; -fx-text-fill: " + WHITE + "; -fx-prompt-text-fill: " + LIGHT_SECONDARY + "; -fx-font-size: 14px; -fx-border-color: transparent; -fx-padding: 0;");

        PauseTransition searchDelay = new PauseTransition(Duration.millis(300));
        mainSearchField.textProperty().addListener((o, ov, nv) -> {
            searchQuery = nv == null ? "" : nv.trim().toLowerCase();
            searchDelay.stop();
            searchDelay.setOnFinished(e -> loadSearchResults(searchQuery));
            searchDelay.playFromStart();
        });

        HBox searchBarBox = new HBox(10, new StackPane(searchIcon), mainSearchField);
        searchBarBox.setAlignment(Pos.CENTER_LEFT);
        searchBarBox.setPadding(new Insets(0, 16, 0, 16));
        searchBarBox.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");
        HBox.setHgrow(mainSearchField, Priority.ALWAYS);

        MenuButton filterBtn = new MenuButton("Filter: All");
        styleDropdownMenu(filterBtn);
        MenuItem filterAll = new MenuItem("All Files"), filterDocs = new MenuItem("Documents"), filterImgs = new MenuItem("Images"), filterVids = new MenuItem("Videos"), filterPdfs = new MenuItem("PDFs");
        filterBtn.getItems().addAll(filterAll, filterDocs, filterImgs, filterVids, filterPdfs);
        filterAll.setOnAction(e -> applyFilter(filterBtn, "All", "Filter: All"));
        filterDocs.setOnAction(e -> applyFilter(filterBtn, "Documents", "Filter: Documents"));
        filterImgs.setOnAction(e -> applyFilter(filterBtn, "Images", "Filter: Images"));
        filterVids.setOnAction(e -> applyFilter(filterBtn, "Videos", "Filter: Videos"));
        filterPdfs.setOnAction(e -> applyFilter(filterBtn, "PDFs", "Filter: PDFs"));

        Label confidenceBadge = label("Search ready", 10, FontWeight.BOLD, "#34D399");
        confidenceBadge.setStyle("-fx-text-fill: #34D399; -fx-background-color: rgba(16, 185, 129, 0.15); -fx-border-color: rgba(16, 185, 129, 0.3); -fx-background-radius: 6; -fx-border-radius: 6; -fx-padding: 3 8;");
        HBox aiHeader = new HBox(label("✦ AI Answer", 15, FontWeight.BOLD, WHITE), new Region(), confidenceBadge);
        HBox.setHgrow(aiHeader.getChildren().get(1), Priority.ALWAYS);
        aiHeader.setAlignment(Pos.CENTER_LEFT);

        Label aiText = label("Search results are loaded from your personal Firestore file collection.", 13, FontWeight.NORMAL, LIGHT_SECONDARY);
        aiText.setWrapText(true);

        VBox aiCard = new VBox(12, aiHeader, aiText, new HBox(8, createCardActionButton("Open best match"), createCardActionButton("Create reminder"), createCardActionButton("Add to Space")));
        aiCard.setPadding(new Insets(18));
        aiCard.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 16; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");

        MenuButton viewBtn = new MenuButton("List View");
        styleDropdownMenu(viewBtn);
        MenuItem listViewOption = new MenuItem("List View"), gridViewOption = new MenuItem("Grid View");
        viewBtn.getItems().addAll(listViewOption, gridViewOption);
        listViewOption.setOnAction(e -> { isGridView = false; viewBtn.setText("List View"); updateResultsView(); });
        gridViewOption.setOnAction(e -> { isGridView = true; viewBtn.setText("Grid View"); updateResultsView(); });

        HBox resultsBar = new HBox(label("Results", 18, FontWeight.BOLD, WHITE), new Region(), new HBox(10, filterBtn, viewBtn));
        HBox.setHgrow(resultsBar.getChildren().get(1), Priority.ALWAYS);
        resultsBar.setAlignment(Pos.CENTER_LEFT);

        listContainer = new VBox(12);
        gridContainer = new GridPane(); gridContainer.setHgap(14); gridContainer.setVgap(14);
        contentBox = new StackPane(label("Loading your files...", 13, FontWeight.NORMAL, LIGHT_SECONDARY));

        VBox contentBody = new VBox(20, titleBox, searchBarBox, aiCard, resultsBar, contentBox);
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
        loadFilesFromFirestore();
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
                createSidebarButton("search", "Search", true, e -> LandingPage.showUserSearch()),
                createSidebarButton("calendar", "Calendar", false, e -> LandingPage.showCalendarPage()),
                createSidebarButton("ai", "AI Assistant", false, e -> LandingPage.showAiAssistantPage()),
                createSidebarButton("collaboration", "Collaboration", false, e -> LandingPage.showCollaborationPage()),
                createSidebarButton("recent", "Recent", false, e -> LandingPage.showRecentPage()),
                createSidebarButton("trash", "Trash", false, e -> LandingPage.showTrashPage()),
                createSidebarButton("settings", "Settings", false, e -> LandingPage.showSettingPage())
        );

        HBox storageValGroup = new HBox(label("64.2 GB of 100 GB", 12, FontWeight.BOLD, WHITE), new Region(), label("64%", 11, FontWeight.BOLD, LIGHT_SECONDARY));
        HBox.setHgrow(storageValGroup.getChildren().get(1), Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
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

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH); sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");
        return sidebar;
    }

    private Button createSidebarButton(String iconType, String labelText, boolean active, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        Label textLabel = label(labelText, 13, active ? FontWeight.BOLD : FontWeight.MEDIUM, WHITE);
        HBox content = new HBox(12, new StackPane(icon), textLabel);
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
                textLabel.setTextFill(Color.web("#38BDF8"));
                TranslateTransition tt = new TranslateTransition(Duration.millis(120), button);
                tt.setToX(4); tt.play();
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                textLabel.setTextFill(Color.web(WHITE));
                TranslateTransition tt = new TranslateTransition(Duration.millis(120), button);
                tt.setToX(0); tt.play();
            });
        }
        return button;
    }

    private void loadFilesFromFirestore() {
        if (UserSession.getInstance() == null || !UserSession.isLoggedIn()) {
            files.clear(); updateResultsView(); return;
        }
        loadSearchResults("");
    }

    private void loadSearchResults(String query) {
        if (UserSession.getInstance() == null || !UserSession.isLoggedIn()) return;
        int version = ++searchVersion;
        new Thread(() -> {
            try {
                List<FileData> data = query == null || query.isBlank()
                        ? fileDAO.getFileSummaries(UserSession.getInstance().getUid())
                        : fileDAO.searchFileSummaries(UserSession.getInstance().getUid(), query);
                List<FileInfo> loaded = new ArrayList<>();
                for (FileData f : data) {
                    loaded.add(new FileInfo(getExtension(f.getFileType(), f.getFileName()), f.getFileName(), f.getLocalPath(), formatSize(f.getFileSize()), formatDate(f.getUploadedAt())));
                }
                Platform.runLater(() -> {
                    if (version != searchVersion) return;
                    files.clear(); files.addAll(loaded); updateResultsView();
                });
            } catch (Exception e) {
                Platform.runLater(() -> { if (version == searchVersion) { files.clear(); updateResultsView(); } });
            }
        }).start();
    }

    private boolean matchesSearchQuery(FileInfo file) {
        if (searchQuery == null || searchQuery.isBlank()) return true;
        String q = searchQuery.toLowerCase();
        return (file.name != null && file.name.toLowerCase().contains(q)) || (file.path != null && file.path.toLowerCase().contains(q)) || (file.type != null && file.type.toLowerCase().contains(q));
    }

    private boolean matchesType(FileInfo file) {
        if (selectedType.equals("All")) return true;
        if (selectedType.equals("PDFs")) return file.type.equals("PDF");
        if (selectedType.equals("Documents")) return file.type.matches("DOC|DOCX|PPT|PPTX|XLS|XLSX|TXT");
        if (selectedType.equals("Images")) return file.type.matches("JPG|JPEG|PNG|GIF|WEBP");
        if (selectedType.equals("Videos")) return file.type.matches("MP4|AVI|MKV|MOV");
        return true;
    }

    private void updateResultsView() {
        if (isGridView) renderGrid(); else renderList();
    }

    private void renderList() {
        listContainer.getChildren().clear();
        int count = 0;
        for (FileInfo f : files) {
            if (matchesType(f) && matchesSearchQuery(f)) {
                listContainer.getChildren().add(createFileCard(f, false));
                count++;
            }
        }
        if (count == 0) listContainer.getChildren().add(label(searchQuery.isBlank() ? "No files found." : "No matching files.", 13, FontWeight.NORMAL, LIGHT_SECONDARY));
        contentBox.getChildren().setAll(listContainer);
    }

    private void renderGrid() {
        gridContainer.getChildren().clear();
        int col = 0, row = 0, count = 0;
        for (FileInfo f : files) {
            if (matchesType(f) && matchesSearchQuery(f)) {
                gridContainer.add(createFileCard(f, true), col++, row);
                if (col == 2) { col = 0; row++; }
                count++;
            }
        }
        if (count == 0) gridContainer.add(label(searchQuery.isBlank() ? "No files found." : "No matching files.", 13, FontWeight.NORMAL, LIGHT_SECONDARY), 0, 0);
        contentBox.getChildren().setAll(gridContainer);
    }

    private VBox createFileCard(FileInfo file, boolean isGrid) {
        Label typeBadge = label(file.type, 10, FontWeight.BOLD, "#38BDF8");
        typeBadge.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-text-fill: #38BDF8; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 2 6;");

        HBox topRow = new HBox(typeBadge, new Region(), label(file.size, 11, FontWeight.BOLD, LIGHT_SECONDARY));
        HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);

        StackPane previewPane = new StackPane(label("FILE PREVIEW", 10, FontWeight.BOLD, "#38BDF8"));
        previewPane.setPrefHeight(isGrid ? 42 : 32);
        previewPane.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-background-radius: 6; -fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-radius: 6;");

        Label nameLbl = label(file.name, 13, FontWeight.BOLD, WHITE); nameLbl.setWrapText(true);
        Label pathLbl = label(file.path, 10, FontWeight.NORMAL, LIGHT_SECONDARY); pathLbl.setWrapText(true);

        HBox bottomRow = new HBox(label(file.date, 10, FontWeight.NORMAL, LIGHT_SECONDARY), new Region(), label("⋮", 13, FontWeight.BOLD, LIGHT_SECONDARY));
        HBox.setHgrow(bottomRow.getChildren().get(1), Priority.ALWAYS);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(6, topRow, previewPane, nameLbl, pathLbl, bottomRow);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 16, 0, 0, 6);");

        if (isGrid) card.setPrefWidth(300); else card.setMaxWidth(Double.MAX_VALUE);

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 16, 0, 0, 4);");
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), card);
            tt.setToY(-3); tt.play();
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 16, 0, 0, 6);");
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), card);
            tt.setToY(0); tt.play();
        });
        return card;
    }

    private void applyFilter(MenuButton button, String type, String displayLabel) {
        selectedType = type; button.setText(displayLabel); updateResultsView();
    }

    private void styleDropdownMenu(MenuButton button) {
        button.setPrefHeight(36); button.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        button.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: " + WHITE + "; -fx-cursor: hand;");
    }

    private Button createCardActionButton(String text) {
        Button b = new Button(text);
        b.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        b.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #38BDF8; -fx-padding: 6 12; -fx-cursor: hand;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-border-color: #38BDF8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #38BDF8; -fx-padding: 6 12; -fx-cursor: hand;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #38BDF8; -fx-padding: 6 12; -fx-cursor: hand;"));
        return b;
    }

    private Label label(String text, double size, FontWeight weight, String color) {
        Label l = new Label(text); l.setFont(Font.font(FONT, weight, size)); l.setStyle("-fx-text-fill: " + color + ";"); return l;
    }

    private String getExtension(String type, String name) {
        if (type != null && !type.isBlank() && !type.equalsIgnoreCase("OCTET-STREAM")) return type.substring(type.lastIndexOf('/') + 1).toUpperCase();
        if (name != null && name.contains(".")) return name.substring(name.lastIndexOf('.') + 1).toUpperCase();
        return "FILE";
    }

    private String formatSize(long bytes) {
        if (bytes <= 0) return "—";
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1048576) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1073741824L) return String.format("%.1f MB", bytes / 1048576.0);
        return String.format("%.1f GB", bytes / 1073741824.0);
    }

    private String formatDate(com.google.cloud.Timestamp value) {
        return value == null ? "—" : new SimpleDateFormat("dd MMM yyyy").format(value.toDate());
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT); icon.setStrokeWidth(2);
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

    private static class FileInfo {
        String type, name, path, size, date;
        FileInfo(String type, String name, String path, String size, String date) {
            this.type = type == null ? "" : type; this.name = name == null ? "" : name;
            this.path = path == null ? "" : path; this.size = size == null ? "—" : size; this.date = date == null ? "—" : date;
        }
    }
}