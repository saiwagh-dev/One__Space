package com.file_handlers.view.userView;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.model.FileData;
import com.file_handlers.model.UserSession;
import com.file_handlers.service.FileProcessingService;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.awt.Desktop;
import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class UnifiedSpaceView {

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

    private final String spaceId, spaceName;
    private final FileDAO fileDAO = new FileDAO();
    private final FileProcessingService fileProcessingService = new FileProcessingService();
    private List<FileData> files = new ArrayList<>();
    private FlowPane filePane;
    private Label countLabel, storageLabel, updatedLabel, previewName, previewDate, previewType, previewSize;
    private StackPane previewIconPane;
    private Button previewButton, detailsButton, removeButton;
    private FileData selectedFile;
    private String selectedType="All";

    public UnifiedSpaceView() { this("all", "All Spaces"); }

    public UnifiedSpaceView(String spaceId, String spaceName) {
        this.spaceId = spaceId == null || spaceId.isBlank() ? "all" : spaceId;
        this.spaceName = spaceName == null || spaceName.isBlank() ? "All Spaces" : spaceName;
    }

    public Scene getUnifiedSpaceScene() {
        String activeUserName = "User", initials = "U";

        UserSession session = UserSession.getInstance();
        if (session != null && session.getDisplayName() != null && !session.getDisplayName().isBlank()) {
            String fullName = session.getDisplayName().trim();
            activeUserName = fullName.split("\\s+")[0];
            initials = activeUserName.substring(0, 1).toUpperCase();
        }

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
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        SVGPath folderHeaderIcon = createIcon("files");
        folderHeaderIcon.setStroke(Color.web("#38BDF8"));
        folderHeaderIcon.setStrokeWidth(2);

        StackPane spaceIconPane = new StackPane(folderHeaderIcon);
        spaceIconPane.setPrefSize(42, 42); spaceIconPane.setMinSize(42, 42);
        spaceIconPane.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-background-radius: 10; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 10;");

        Label title = label(spaceName + " Space", 22, FontWeight.BOLD, WHITE);
        Label subtitle = label("Files automatically organized into " + spaceName + ".", 12, FontWeight.NORMAL, LIGHT_SECONDARY);
        VBox titleBox = new VBox(3, title, subtitle);
        HBox titleArea = new HBox(12, spaceIconPane, titleBox);
        titleArea.setAlignment(Pos.CENTER_LEFT);

        Button back = new Button("← Spaces");
        back.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 14; -fx-cursor: hand;");
        back.setOnMouseEntered(e -> back.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-text-fill: " + WHITE + "; -fx-border-color: rgba(255, 255, 255, 0.2); -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 14; -fx-cursor: hand;"));
        back.setOnMouseExited(e -> back.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 14; -fx-cursor: hand;"));
        back.setOnAction(e -> LandingPage.showUserSpace());

        Region headerGap = new Region();
        HBox.setHgrow(headerGap, Priority.ALWAYS);
        HBox header = new HBox(titleArea, headerGap, back);
        header.setAlignment(Pos.CENTER_LEFT);

        countLabel = statValue("Loading...");
        storageLabel = statValue("Loading...");
        updatedLabel = statValue("Loading...");

        GridPane stats = new GridPane();
        stats.setHgap(14);
        stats.add(statCard("Files", countLabel, "files"), 0, 0);
        stats.add(statCard("Storage", storageLabel, "storage"), 1, 0);
        stats.add(statCard("Last Updated", updatedLabel, "recent"), 2, 0);

        Region statsRowSpacer = new Region();
        HBox.setHgrow(statsRowSpacer, Priority.ALWAYS);

        HBox statsRow = new HBox(stats, statsRowSpacer);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        if (!"all".equals(spaceId)) {
            Button importFilesBtn = new Button("⬆   Import Files");
            importFilesBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
            String importIdle = "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 18; -fx-cursor: hand; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 10;";
            String importHover = "-fx-background-color: linear-gradient(to right, #2563EB, #3B82F6); -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 18; -fx-cursor: hand; -fx-border-color: rgba(96, 165, 250, 0.85); -fx-border-radius: 10;";
            importFilesBtn.setStyle(importIdle);
            importFilesBtn.setOnMouseEntered(e -> importFilesBtn.setStyle(importHover));
            importFilesBtn.setOnMouseExited(e -> importFilesBtn.setStyle(importIdle));
            importFilesBtn.setOnAction(e -> importFilesDirectly());
            statsRow.getChildren().add(importFilesBtn);
        }

        filePane = new FlowPane(12, 12);
        filePane.setPadding(new Insets(4));

        ScrollPane fileScroll = new ScrollPane(filePane);
        fileScroll.setFitToWidth(true);
        fileScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        fileScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        fileScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        VBox.setVgrow(fileScroll, Priority.ALWAYS);

        // Filter Dropdown Button for Filtering Files based on Type
        ComboBox<String> typeFilterCombo = new ComboBox<>();
        typeFilterCombo.getItems().addAll("All", "PDF", "Image", "Video", "Audio", "Document", "Text", "Code", "Archive");
        typeFilterCombo.setValue(selectedType);
        typeFilterCombo.setStyle(
                "-fx-background-color: " + INPUT_BG + ";" +
                "-fx-text-fill: " + WHITE + ";" +
                "-fx-border-color: rgba(255, 255, 255, 0.1);" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-font-size: 12px;" +
                "-fx-cursor: hand;"
        );
        typeFilterCombo.setOnAction(e -> {
            String val = typeFilterCombo.getValue();
            selectedType = (val == null || val.isBlank()) ? "All" : val;
            refreshFiles("");
        });

        Region filterGap = new Region();
        HBox.setHgrow(filterGap, Priority.ALWAYS);

        HBox fileHeaderBox = new HBox(10, label("Files", 17, FontWeight.BOLD, WHITE), filterGap, typeFilterCombo);
        fileHeaderBox.setAlignment(Pos.CENTER_LEFT);

        VBox fileArea = new VBox(12, fileHeaderBox, fileScroll);
        
        fileArea.setPadding(new Insets(18));
        fileArea.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");
        HBox.setHgrow(fileArea, Priority.ALWAYS);

        SVGPath defaultPreviewIcon = createIcon("files");
        defaultPreviewIcon.setStroke(Color.web("#38BDF8"));
        defaultPreviewIcon.setStrokeWidth(1.5);
        defaultPreviewIcon.setScaleX(2.5); defaultPreviewIcon.setScaleY(2.5);

        previewIconPane = new StackPane(defaultPreviewIcon);
        previewIconPane.setPrefSize(330, 160); previewIconPane.setMinSize(330, 160);
        previewIconPane.setAlignment(Pos.CENTER);
        previewIconPane.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-background-radius: 12; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 12;");

        previewName = detailValue("Select a file");
        previewDate = detailValue("—");
        previewType = detailValue("—");
        previewSize = detailValue("—");

        Label previewTitle = label("File Preview", 17, FontWeight.BOLD, WHITE);

        previewButton = new Button("Preview");
        previewButton.setDisable(true);
        previewButton.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16; -fx-cursor: hand; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 8;");
        previewButton.setOnAction(e -> { if (selectedFile != null) openFile(selectedFile); });

        detailsButton = new Button("More details");
        detailsButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #38BDF8; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 5 8; -fx-cursor: hand;");
        detailsButton.setOnAction(e -> { if (selectedFile != null) loadFullDetails(selectedFile); });

        removeButton = new Button("Remove");
        removeButton.setDisable(true);
        removeButton.setStyle("-fx-background-color: rgba(239, 68, 68, 0.15); -fx-text-fill: #F87171; -fx-border-color: rgba(239, 68, 68, 0.3); -fx-border-radius: 7; -fx-background-radius: 7; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 5 10; -fx-cursor: hand;");
        removeButton.setOnAction(e -> { if (selectedFile != null) removeFile(selectedFile); });

        Region previewGap = new Region();
        HBox.setHgrow(previewGap, Priority.ALWAYS);
        HBox previewHeader = new HBox(previewTitle, previewGap, previewButton);
        previewHeader.setAlignment(Pos.CENTER_LEFT);

        HBox previewActions = new HBox(8, detailsButton, removeButton);
        previewActions.setAlignment(Pos.CENTER_RIGHT);

        VBox preview = new VBox(12, previewHeader, previewIconPane, detailBox("File Name", previewName), detailBox("Date", previewDate), detailBox("Type", previewType), detailBox("Size", previewSize), previewActions);
        preview.setPadding(new Insets(20));
        preview.setPrefWidth(370);
        preview.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");

        HBox main = new HBox(16, fileArea, preview);
        VBox.setVgrow(main, Priority.ALWAYS);

        VBox content = new VBox(20, header, statsRow, main);
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color: transparent;");

        ScrollPane centerScroll = new ScrollPane(content);
        centerScroll.setFitToWidth(true);
        centerScroll.setFitToHeight(true);
        centerScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        centerScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        centerScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");

        VBox center = new VBox(topBar, centerScroll);
        center.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        VBox.setVgrow(centerScroll, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setCenter(center);
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");

        loadFiles();
        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private void importFilesDirectly() {
        if ("all".equals(spaceId)) {
            showAlert("Open a specific Space to import files directly into it.");
            return;
        }

        UserSession session = UserSession.getInstance();
        if (session == null || !UserSession.isLoggedIn() || session.getUid() == null || session.getUid().isBlank()) {
            showAlert("No authenticated user.");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import Files into " + spaceName);

        List<File> selected = chooser.showOpenMultipleDialog(filePane.getScene().getWindow());
        if (selected == null || selected.isEmpty()) return;

        Thread thread = new Thread(() -> {
            int imported = 0, duplicates = 0, failed = 0;

            for (File selectedFile : selected) {
                try {
                    String result = fileProcessingService.processFileToSpace(selectedFile.toPath(), spaceId, spaceName, null);
                    if (result != null && result.startsWith("DUPLICATE:")) duplicates++;
                    else imported++;
                } catch (Exception e) {
                    failed++;
                }
            }

            int finalImported = imported, finalDuplicates = duplicates, finalFailed = failed;

            Platform.runLater(() -> {
                StringBuilder summary = new StringBuilder();
                summary.append(finalImported).append(finalImported == 1 ? " file" : " files")
                        .append(" imported directly into ").append(spaceName).append(" without AI categorization.");
                if (finalDuplicates > 0) summary.append(" ").append(finalDuplicates).append(" already existed and were skipped.");
                if (finalFailed > 0) summary.append(" ").append(finalFailed).append(" failed to import.");

                showAlert(summary.toString());
                loadFiles();
            });
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void loadFiles() {
        UserSession session = UserSession.getInstance();
        if (session == null || !UserSession.isLoggedIn() || session.getUid() == null || session.getUid().isBlank()) {
            Platform.runLater(() -> showEmpty("No authenticated user."));
            return;
        }

        String uid = session.getUid();

        Thread thread = new Thread(() -> {
            try {
                List<FileData> loaded = spaceId.equals("all") ? fileDAO.getFileSummaries(uid) : fileDAO.getFileSummariesBySpace(uid, spaceId);
                loaded.sort((a, b) -> {
                    if (a.getUploadedAt() == null) return 1;
                    if (b.getUploadedAt() == null) return -1;
                    return b.getUploadedAt().compareTo(a.getUploadedAt());
                });
                files = loaded;
                Platform.runLater(() -> { updateStats(); refreshFiles(""); });
            } catch (Exception e) {
                Platform.runLater(() -> showEmpty("Unable to load files."));
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void refreshFiles(String query) {
        filePane.getChildren().clear();
        String search = query == null ? "" : query.trim().toLowerCase();
        int shown = 0;

        for (FileData file : files) {
            String name = file.getFileName() == null ? "Unnamed file" : file.getFileName();
            if (!search.isEmpty() && !name.toLowerCase().contains(search)) continue;

            if (selectedType != null && !"All".equalsIgnoreCase(selectedType)) {
                String fType = file.getFileType() == null ? "" : file.getFileType().toLowerCase();
                if (!fType.contains(selectedType.toLowerCase())) continue;
            }

            filePane.getChildren().add(createFileCard(file));
            shown++;
        }

        if (shown == 0) showEmpty(search.isEmpty() ? "No files in this Space." : "No matching files.");
    }

    private VBox createFileCard(FileData file) {
        String name = file.getFileName() == null ? "Unnamed file" : file.getFileName();

        SVGPath fileIcon = createIcon(getFileIconType(name));
        fileIcon.setStroke(Color.web("#38BDF8"));
        fileIcon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(fileIcon);
        iconBox.setPrefSize(40, 40); iconBox.setMinSize(40, 40);
        iconBox.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-background-radius: 8; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 8;");

        Label title = label(name, 12, FontWeight.BOLD, WHITE);
        Label meta = label((file.getFileType() == null ? "" : file.getFileType()) + " • " + formatSize(file.getFileSize()), 10, FontWeight.NORMAL, LIGHT_SECONDARY);

        VBox text = new VBox(3, title, meta);
        HBox card = new HBox(10, iconBox, text);
        card.setPrefWidth(420);
        card.setMinHeight(64);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER_LEFT);

        String baseStyle = "-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: #38BDF8; -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.25), 12, 0, 0, 4);";

        card.setStyle(baseStyle);
        card.setOnMouseEntered(e -> card.setStyle(hoverStyle));
        card.setOnMouseExited(e -> card.setStyle(baseStyle));
        card.setOnMouseClicked(e -> selectFile(file));

        return new VBox(card);
    }

    private void selectFile(FileData file) {
        selectedFile = file;
        previewButton.setDisable(false);
        detailsButton.setDisable(false);
        removeButton.setDisable(false);

        SVGPath previewTypeIcon = createIcon(getFileIconType(file.getFileName()));
        previewTypeIcon.setStroke(Color.web("#38BDF8"));
        previewTypeIcon.setStrokeWidth(1.5);
        previewTypeIcon.setScaleX(2.5); previewTypeIcon.setScaleY(2.5);

        previewIconPane.getChildren().setAll(previewTypeIcon);
        previewName.setText(file.getFileName() == null ? "Unnamed file" : file.getFileName());
        previewType.setText(file.getFileType() == null ? "—" : file.getFileType());
        previewSize.setText(formatSize(file.getFileSize()));
        previewDate.setText(file.getUploadedAt() == null ? "—" : formatDate(file.getUploadedAt().toDate().toInstant()));
    }

    private void removeFile(FileData file) {
        UserSession session = UserSession.getInstance();

        if (session == null || session.getUid() == null || session.getUid().isBlank()) {
            showAlert("No authenticated user.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove File");
        alert.setHeaderText("Remove \"" + file.getFileName() + "\" from OneSpace?");
        alert.setContentText("The file will disappear from this Space and appear in Trash. The actual file on your computer will not be deleted.");

        ButtonType remove = new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(cancel, remove);

        alert.showAndWait().ifPresent(result -> {
            if (result != remove) return;

            String fileId = file.getFileHash();

            if (fileId == null || fileId.isBlank()) {
                showAlert("Unable to identify this file.");
                return;
            }

            removeButton.setDisable(true);
            removeButton.setText("Removing...");

            Thread thread = new Thread(() -> {
                try {
                    fileDAO.softDeleteFile(session.getUid(), fileId);

                    Platform.runLater(() -> {
                        files.remove(file);
                        selectedFile = null;
                        previewButton.setDisable(true);
                        detailsButton.setDisable(true);
                        removeButton.setDisable(true);
                        removeButton.setText("Remove");

                        SVGPath defaultIcon = createIcon("files");
                        defaultIcon.setStroke(Color.web("#38BDF8"));
                        defaultIcon.setStrokeWidth(1.5);
                        defaultIcon.setScaleX(2.5); defaultIcon.setScaleY(2.5);
                        previewIconPane.getChildren().setAll(defaultIcon);

                        previewName.setText("Select a file");
                        previewDate.setText("—");
                        previewType.setText("—");
                        previewSize.setText("—");
                        updateStats();
                        refreshFiles("");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        removeButton.setDisable(false);
                        removeButton.setText("Remove");
                        showAlert("Unable to remove the file from OneSpace.");
                    });
                }
            });

            thread.setDaemon(true);
            thread.start();
        });
    }

    private void loadFullDetails(FileData summary) {
        UserSession session = UserSession.getInstance();

        if (session == null || session.getUid() == null || session.getUid().isBlank()) {
            showAlert("No authenticated user.");
            return;
        }

        if (summary.getFileHash() == null || summary.getFileHash().isBlank()) {
            showDetails(summary);
            return;
        }

        detailsButton.setDisable(true);
        detailsButton.setText("Loading...");

        Thread thread = new Thread(() -> {
            try {
                FileData full = fileDAO.getFile(session.getUid(), summary.getFileHash());

                Platform.runLater(() -> {
                    detailsButton.setDisable(false);
                    detailsButton.setText("More details");
                    showDetails(full == null ? summary : full);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    detailsButton.setDisable(false);
                    detailsButton.setText("More details");
                    showAlert("Unable to load file details.");
                });
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void showDetails(FileData file) {
        if (file == null) return;

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("File Details");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        String name = file.getFileName() == null ? "Unnamed file" : file.getFileName();
        String description = file.getDescription();

        if (description == null || description.isBlank()) description = "No description available.";

        VBox box = new VBox(14);
        box.setPadding(new Insets(18));
        box.setStyle("-fx-background-color: #0A121E;");

        Label heading = label(name, 18, FontWeight.BOLD, WHITE);
        Label type = detailLine("File Type", safe(file.getFileType()));
        Label size = detailLine("File Size", formatSize(file.getFileSize()));
        Label date = detailLine("Uploaded", file.getUploadedAt() == null ? "—" : formatDate(file.getUploadedAt().toDate().toInstant()));
        Label path = detailLine("Local Path", safe(file.getLocalPath()));

        Label descriptionTitle = label("Description", 11, FontWeight.BOLD, LIGHT_SECONDARY);
        Label descriptionValue = label(description, 12, FontWeight.NORMAL, WHITE);
        descriptionValue.setWrapText(true);
        descriptionValue.setMaxWidth(460);

        VBox descriptionBox = new VBox(4, descriptionTitle, descriptionValue);
        descriptionBox.setPadding(new Insets(10));
        descriptionBox.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-background-radius: 10; -fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-radius: 10;");

        Label tagsTitle = label("Smart Tags", 11, FontWeight.BOLD, LIGHT_SECONDARY);
        FlowPane tagsPane = new FlowPane(7, 7);

        List<String> tags = file.getSmartTags();

        if (tags == null || tags.isEmpty()) {
            tagsPane.getChildren().add(tag("No smart tags available."));
        } else {
            for (String tag : tags)
                if (tag != null && !tag.isBlank()) tagsPane.getChildren().add(tag(tag));
        }

        VBox tagsBox = new VBox(6, tagsTitle, tagsPane);
        tagsBox.setPadding(new Insets(10));
        tagsBox.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-background-radius: 10; -fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-radius: 10;");

        box.getChildren().addAll(heading, type, size, date, path, descriptionBox, tagsBox);

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportWidth(500);
        scroll.setPrefViewportHeight(430);
        scroll.setStyle("-fx-background-color: #0A121E; -fx-background: #0A121E; -fx-border-color: transparent;");

        dialog.getDialogPane().setContent(scroll);
        dialog.getDialogPane().setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");
        dialog.showAndWait();
    }

    private Label detailLine(String title, String value) {
        Label l = label(title + "   " + value, 11, FontWeight.NORMAL, WHITE);
        l.setWrapText(true);
        return l;
    }

    private Label tag(String text) {
        Label l = label(text, 10, FontWeight.BOLD, "#38BDF8");
        l.setPadding(new Insets(5, 9, 5, 9));
        l.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-background-radius: 12; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 12; -fx-text-fill: #38BDF8; -fx-font-weight: bold;");
        return l;
    }

    private String safe(String value) { return value == null || value.isBlank() ? "—" : value; }

    private void updateStats() {
        long total = 0;
        Instant latest = null;

        for (FileData file : files) {
            total += file.getFileSize();

            if (file.getUploadedAt() != null) {
                Instant time = file.getUploadedAt().toDate().toInstant();
                if (latest == null || time.isAfter(latest)) latest = time;
            }
        }

        countLabel.setText(files.size() + " files");
        storageLabel.setText(formatSize(total));
        updatedLabel.setText(latest == null ? "—" : relativeTime(latest));
    }

    private void openFile(FileData file) {
        if (file == null || file.getLocalPath() == null || file.getLocalPath().isBlank()) return;

        try {
            File localFile = new File(file.getLocalPath());

            if (!localFile.exists()) {
                showAlert("The file no longer exists at its stored location.");
                return;
            }

            if (!Desktop.isDesktopSupported()) {
                showAlert("Opening files is not supported on this system.");
                return;
            }

            UserSession session = UserSession.getInstance();

            if (session != null && session.getUid() != null && !session.getUid().isBlank()
                    && file.getFileHash() != null && !file.getFileHash().isBlank()) {
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

            Desktop.getDesktop().open(localFile);

        } catch (Exception e) {
            showAlert("Unable to open the selected file.");
        }
    }

    private void showEmpty(String text) {
        if (filePane == null) return;
        filePane.getChildren().clear();
        filePane.getChildren().add(label(text, 13, FontWeight.NORMAL, LIGHT_SECONDARY));
    }

    private String formatDate(Instant instant) {
        return DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm").withZone(ZoneId.systemDefault()).format(instant);
    }

    private String relativeTime(Instant time) {
        long minutes = Math.max(0, Duration.between(time, Instant.now()).toMinutes());

        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " min ago";

        long hours = minutes / 60;
        if (hours < 24) return hours + " hr ago";

        long days = hours / 24;
        return days + " day" + (days == 1 ? "" : "s") + " ago";
    }

    private String formatSize(long bytes) {
        if (bytes <= 0) return "0 B";
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1048576) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1073741824L) return String.format("%.1f MB", bytes / 1048576.0);
        return String.format("%.1f GB", bytes / 1073741824.0);
    }

    private String getFileIconType(String name) {
        if (name == null) return "files";
        String n = name.toLowerCase();
        if (n.matches(".*\\.(jpg|jpeg|png|gif|webp|mp4|avi|mkv|mov|mp3|wav|m4a)$")) return "media";
        return "files";
    }

    private VBox statCard(String title, Label value, String iconType) {
        Label heading = label(title, 11, FontWeight.BOLD, LIGHT_SECONDARY);

        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web("#38BDF8"));
        icon.setStrokeWidth(2);

        StackPane symbolPane = new StackPane(icon);
        symbolPane.setPrefSize(28, 28); symbolPane.setMinSize(28, 28);
        symbolPane.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-background-radius: 6; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 6;");

        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);

        HBox row = new HBox(heading, gap, symbolPane);
        VBox card = new VBox(8, row, value);
        card.setPadding(new Insets(14));
        card.setMinHeight(85);
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 14; -fx-background-radius: 14;");

        return card;
    }

    private Label statValue(String text) { return label(text, 19, FontWeight.BOLD, WHITE); }

    private VBox detailBox(String title, Label value) {
        return new VBox(2, label(title, 10, FontWeight.BOLD, LIGHT_SECONDARY), value);
    }

    private Label detailValue(String text) { return label(text, 12, FontWeight.BOLD, WHITE); }

    private Label label(String text, double size, FontWeight weight, String color) {
        Label l = new Label(text);
        l.setFont(Font.font(FONT, weight, size));
        l.setStyle("-fx-text-fill: " + color + ";");
        return l;
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);
        switch (type) {
            case "files": icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13"); break;
            case "storage": icon.setContent("M4 6H20 M4 12H20 M4 18H20"); break;
            case "recent": icon.setContent("M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"); break;
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

    private Button createDropdownBtn(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.5); -fx-text-fill: " + color + "; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-padding: 8 12; -fx-cursor: hand; -fx-background-radius: 6; -fx-border-radius: 6;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-text-fill: " + color + "; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-padding: 8 12; -fx-cursor: hand; -fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-width: 1;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.5); -fx-text-fill: " + color + "; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-padding: 8 12; -fx-cursor: hand; -fx-background-radius: 6; -fx-border-radius: 6;"));
        btn.setOnAction(action);
        return btn;
    }
}