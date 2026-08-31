package com.file_handlers.view.userView;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.file_handlers.model.UserSession;
import com.file_handlers.service.FileProcessingService;
import com.file_handlers.service.ProcessingStatusListener;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

public class AddFileData {
    private final VBox fileList = new VBox(14);
    private final List<File> selectedFiles = new ArrayList<>();
    private final Runnable backAction;
    private static final int MAX_WORKERS = 3;

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
    private static final String SUCCESS = "#10B981";
    private static final String WARNING = "#F59E0B";
    private static final String ERROR = "#EF4444";
    private static final String INFO = "#38BDF8";

    public AddFileData() { this(() -> LandingPage.showUserDashboard()); }
    public AddFileData(Runnable backAction) { this.backAction = backAction; }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        HBox topBar = createTopBar();

        Button back = new Button("← Dashboard");
        back.setStyle(
            "-fx-background-color: rgba(13, 22, 38, 0.85);" +
            "-fx-text-fill: " + WHITE + ";" +
            "-fx-border-color: rgba(255, 255, 255, 0.1);" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-family: " + FONT + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-padding: 6 12;" +
            "-fx-cursor: hand;"
        );
        back.setOnMouseEntered(e -> back.setStyle(
            "-fx-background-color: rgba(37, 99, 235, 0.2);" +
            "-fx-text-fill: " + WHITE + ";" +
            "-fx-border-color: rgba(56, 189, 248, 0.4);" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-family: " + FONT + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-padding: 6 12;" +
            "-fx-cursor: hand;"
        ));
        back.setOnMouseExited(e -> back.setStyle(
            "-fx-background-color: rgba(13, 22, 38, 0.85);" +
            "-fx-text-fill: " + WHITE + ";" +
            "-fx-border-color: rgba(255, 255, 255, 0.1);" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-family: " + FONT + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-padding: 6 12;" +
            "-fx-cursor: hand;"
        ));
        back.setOnAction(e -> backAction.run());

        HBox backRow = new HBox(back);
        backRow.setAlignment(Pos.CENTER_RIGHT);
        backRow.setPadding(new Insets(12, ResponsiveUtil.PAGE_PADDING, 0, ResponsiveUtil.PAGE_PADDING));

        VBox content = new VBox(22);
        content.setPadding(new Insets(14, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color: transparent;");

        Label title = new Label("Add Files");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 26));
        title.setTextFill(Color.web(WHITE));

        Label subtitle = new Label("Add files to OneSpace and let the pipeline understand, categorize and organize them.");
        subtitle.setWrapText(true);
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

        VBox titleBox = new VBox(5, title, subtitle);
        HBox actionCard = createActionCard();

        HBox listHeader = new HBox();
        listHeader.setAlignment(Pos.CENTER_LEFT);

        Label filesTitle = new Label("Files");
        filesTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        filesTitle.setTextFill(Color.web(WHITE));

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        Label pipelineLabel = new Label("3 files at a time");
        pipelineLabel.setFont(Font.font(FONT, FontWeight.MEDIUM, 11));
        pipelineLabel.setTextFill(Color.web(LIGHT_SECONDARY));
        listHeader.getChildren().addAll(filesTitle, headerSpacer, pipelineLabel);

        fileList.setPadding(new Insets(4));

        VBox fileContainer = new VBox(fileList);
        fileContainer.setPadding(new Insets(14));
        fileContainer.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");

        ScrollPane fileScroll = new ScrollPane(fileContainer);
        fileScroll.setFitToWidth(true);
        fileScroll.setFitToHeight(true);
        fileScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        fileScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        fileScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        VBox.setVgrow(fileScroll, Priority.ALWAYS);

        Button processButton = new Button("Process Selected Files");
        processButton.setStyle(primaryButtonStyle());
        processButton.setOnAction(e -> processFiles());

        HBox bottomBar = new HBox(processButton);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        showEmptyState();
        content.getChildren().addAll(titleBox, actionCard, listHeader, fileScroll, bottomBar);

        VBox mainArea = new VBox(topBar, backRow, content);
        VBox.setVgrow(content, Priority.ALWAYS);
        mainArea.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");

        root.setCenter(mainArea);
        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private HBox createActionCard() {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");

        SVGPath addIcon = createIcon("plus");
        addIcon.setStroke(Color.web("#38BDF8"));
        addIcon.setStrokeWidth(2.5);

        StackPane iconCircle = new StackPane(addIcon);
        iconCircle.setPrefSize(44, 44); iconCircle.setMinSize(44, 44); iconCircle.setMaxSize(44, 44);
        iconCircle.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 12; -fx-background-radius: 12;");

        Label title = new Label("Add files to OneSpace");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label description = new Label("Files stay on your computer. OneSpace stores metadata and AI understanding.");
        description.setWrapText(true);
        description.setFont(Font.font(FONT, FontWeight.MEDIUM, 12));
        description.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox text = new VBox(5, title, description);
        HBox.setHgrow(text, Priority.ALWAYS);

        Button addButton = new Button("+   Add Files");
        addButton.setStyle(primaryButtonStyle());
        addButton.setOnAction(e -> selectFiles());

        card.getChildren().addAll(iconCircle, text, addButton);
        return card;
    }

    private HBox createTopBar() {
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
        userName.setTextFill(Color.WHITE);

        Label dropDown = new Label("⌄");
        dropDown.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        dropDown.setTextFill(Color.web(LIGHT_SECONDARY));

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

        return topBar;
    }

    private void selectFiles() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Files");
        List<File> files = chooser.showOpenMultipleDialog(null);
        if (files == null || files.isEmpty()) return;
        selectedFiles.clear();
        selectedFiles.addAll(files);
        refreshFileList();
    }

    private void refreshFileList() {
        fileList.getChildren().clear();
        for (File file : selectedFiles) fileList.getChildren().add(createFileCard(file));
    }

    private void showEmptyState() {
        SVGPath filePlusIcon = createIcon("plus");
        filePlusIcon.setStroke(Color.web(LIGHT_SECONDARY));
        filePlusIcon.setStrokeWidth(2);

        Label title = new Label("No files selected");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label description = new Label("Click Add Files above to begin.");
        description.setFont(Font.font(FONT, FontWeight.NORMAL, 11));
        description.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox empty = new VBox(8, filePlusIcon, title, description);
        empty.setAlignment(Pos.CENTER);
        empty.setPadding(new Insets(45));
        empty.setUserData("EMPTY_STATE");
        fileList.getChildren().add(empty);
    }

    private VBox createFileCard(File file) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-background-radius: 14; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 14;");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        SVGPath fileIcon = createIcon(getFileIconType(file));
        fileIcon.setStroke(Color.web("#38BDF8"));
        fileIcon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(fileIcon);
        iconBox.setMinSize(40, 40); iconBox.setPrefSize(40, 40);
        iconBox.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-background-radius: 10; -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 10;");

        Label name = new Label(file.getName());
        name.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        name.setStyle("-fx-text-fill: " + WHITE + ";");

        Label path = new Label(file.getParent());
        path.setFont(Font.font(FONT, FontWeight.NORMAL, 10));
        path.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox fileInfo = new VBox(3, name, path);
        HBox.setHgrow(fileInfo, Priority.ALWAYS);

        Label status = new Label("Waiting");
        setStatusStyle(status, "WAITING");

        header.getChildren().addAll(iconBox, spacer(12), fileInfo, spacer(12), status);

        HBox pipeline = new HBox(8);
        pipeline.setAlignment(Pos.CENTER_LEFT);

        StepView metadata = createStep("1", "Metadata");
        StepView ai = createStep("2", "AI");
        StepView space = createStep("3", "Space");
        StepView saved = createStep("4", "Saved");

        pipeline.getChildren().addAll(metadata.container, connector(), ai.container, connector(), space.container, connector(), saved.container);

        Label explanation = new Label("Waiting to start...");
        explanation.setWrapText(true);
        explanation.setFont(Font.font(FONT, FontWeight.NORMAL, 11));
        explanation.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        card.getChildren().addAll(header, pipeline, explanation);
        card.setUserData(new ProcessingView(status, explanation, metadata, ai, space, saved));
        return card;
    }

    private StepView createStep(String number, String name) {
        Label circle = new Label(number);
        circle.setMinSize(24, 24); circle.setPrefSize(24, 24);
        circle.setAlignment(Pos.CENTER);
        circle.setStyle(circleStyle("WAITING"));

        Label title = new Label(name);
        title.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: " + LIGHT_SECONDARY + ";");

        Label state = new Label("Waiting");
        state.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 9px; -fx-text-fill: " + LIGHT_SECONDARY + ";");

        HBox titleRow = new HBox(6, circle, title);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        VBox container = new VBox(4, titleRow, state);
        container.setPadding(new Insets(9, 11, 9, 11));
        container.setMinWidth(120);
        container.setStyle(stepContainerStyle("WAITING"));

        return new StepView(container, circle, title, state, name);
    }

    private Region connector() {
        Region line = new Region();
        line.setPrefHeight(2);
        line.setPrefWidth(20);
        line.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1);");
        return line;
    }

    private void processFiles() {
        if (selectedFiles.isEmpty()) return;

        UserSession session = UserSession.getInstance();
        if (session == null || !UserSession.isLoggedIn()) return;

        ExecutorService executor = Executors.newFixedThreadPool(MAX_WORKERS);

        for (File file : selectedFiles) {
            VBox card = findCard(file);
            if (card == null) continue;

            ProcessingView view = (ProcessingView) card.getUserData();
            resetProcessingView(view);

            setStatus(view, "Checking file...");
            view.explanation.setText("Preparing file and checking whether it already exists...");

            executor.submit(() -> processSingleFile(file, view));
        }

        executor.shutdown();
    }

    private void processSingleFile(File file, ProcessingView view) {
        try {
            FileProcessingService service = new FileProcessingService();

            ProcessingStatusListener listener = new ProcessingStatusListener() {
                @Override
                public void onTaskStarted(String task) {
                    updateStep(view, task, "PROCESSING");
                }

                @Override
                public void onTaskCompleted(String task) {
                    updateStep(view, task, "COMPLETED");
                }

                @Override
                public void onTaskFailed(String task, String error) {
                    if ("Duplicate".equalsIgnoreCase(task)) updateDuplicate(view);
                    else updateFailed(view, task, error);
                }
            };

            String result = service.processFile(Path.of(file.getAbsolutePath()), listener);

            if (result != null && result.startsWith("DUPLICATE:")) {
                updateDuplicate(view);
                return;
            }

            Platform.runLater(() -> {
                setStatus(view, "Completed");
                view.explanation.setText("File successfully added to your OneSpace library.");
            });
        } catch (Exception e) {
            Platform.runLater(() -> {
                setStatus(view, "Failed");
                setStatusStyle(view.status, "FAILED");
                view.explanation.setText("Processing failed: " + safeMessage(e));
            });

            System.out.println("[ERROR] " + file.getName() + " - " + e.getMessage());
        }
    }

    private void updateStep(ProcessingView view, String task, String state) {
        Platform.runLater(() -> {
            StepView step = getStep(view, normalizeTask(task));
            if (step == null) return;

            setStepState(step, state);

            if ("PROCESSING".equals(state)) {
                setStatus(view, "Processing " + step.name + "...");
                view.explanation.setText(getProcessingMessage(step.name));
            }

            if ("COMPLETED".equals(state))
                view.explanation.setText(getCompletedMessage(step.name));
        });
    }

    private void updateDuplicate(ProcessingView view) {
        Platform.runLater(() -> {
            setStepState(view.metadata, "COMPLETED");
            setStepState(view.ai, "SKIPPED");
            setStepState(view.space, "SKIPPED");
            setStepState(view.saved, "SKIPPED");
            setStatus(view, "Already Exists");
            setStatusStyle(view.status, "DUPLICATE");
            view.explanation.setText("This file is already in your OneSpace library. No duplicate was created.");
        });
    }

    private void updateFailed(ProcessingView view, String task, String error) {
        Platform.runLater(() -> {
            StepView step = getStep(view, normalizeTask(task));
            if (step != null) setStepState(step, "FAILED");

            setStatus(view, "Processing Failed");
            setStatusStyle(view.status, "FAILED");
            view.explanation.setText(error == null || error.isBlank() ? "The " + normalizeTask(task) + " step failed." : error);
        });
    }

    private void setStepState(StepView step, String state) {
        if (step == null) return;

        String display;

        switch (state) {
            case "PROCESSING":
                display = "Processing";
                step.circle.setText("•");
                break;
            case "COMPLETED":
                display = "Completed";
                step.circle.setText("✓");
                break;
            case "SKIPPED":
                display = "Skipped";
                step.circle.setText("–");
                break;
            case "FAILED":
                display = "Failed";
                step.circle.setText("!");
                break;
            default:
                display = "Waiting";
                step.circle.setText(step.number);
        }

        step.state.setText(display);
        step.container.setStyle(stepContainerStyle(state));
        step.circle.setStyle(circleStyle(state));
        step.title.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: " + stepTextColor(state) + ";");
        step.state.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 9px; -fx-font-weight: 600; -fx-text-fill: " + stepTextColor(state) + ";");
    }

    private void resetProcessingView(ProcessingView view) {
        setStepState(view.metadata, "WAITING");
        setStepState(view.ai, "WAITING");
        setStepState(view.space, "WAITING");
        setStepState(view.saved, "WAITING");
        setStatus(view, "Waiting");
        setStatusStyle(view.status, "WAITING");
        view.explanation.setText("Waiting to start...");
    }

    private void setStatus(ProcessingView view, String text) {
        view.status.setText(text);

        if ("Completed".equalsIgnoreCase(text)) setStatusStyle(view.status, "SUCCESS");
        else if ("Already Exists".equalsIgnoreCase(text)) setStatusStyle(view.status, "DUPLICATE");
        else if ("Failed".equalsIgnoreCase(text) || "Processing Failed".equalsIgnoreCase(text)) setStatusStyle(view.status, "FAILED");
        else setStatusStyle(view.status, "PROCESSING");
    }

    private void setStatusStyle(Label label, String state) {
        switch (state) {
            case "SUCCESS":
                label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #34D399; -fx-background-color: rgba(16, 185, 129, 0.15); -fx-border-color: rgba(16, 185, 129, 0.3); -fx-padding: 4 10; -fx-background-radius: 6; -fx-border-radius: 6;");
                break;
            case "DUPLICATE":
                label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #FBBF24; -fx-background-color: rgba(245, 158, 11, 0.15); -fx-border-color: rgba(245, 158, 11, 0.3); -fx-padding: 4 10; -fx-background-radius: 6; -fx-border-radius: 6;");
                break;
            case "FAILED":
                label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #F87171; -fx-background-color: rgba(239, 68, 68, 0.15); -fx-border-color: rgba(239, 68, 68, 0.3); -fx-padding: 4 10; -fx-background-radius: 6; -fx-border-radius: 6;");
                break;
            case "PROCESSING":
                label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #38BDF8; -fx-background-color: rgba(56, 189, 248, 0.15); -fx-border-color: rgba(56, 189, 248, 0.3); -fx-padding: 4 10; -fx-background-radius: 6; -fx-border-radius: 6;");
                break;
            default:
                label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 600; -fx-text-fill: " + LIGHT_SECONDARY + "; -fx-background-color: rgba(255, 255, 255, 0.05); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-padding: 4 10; -fx-background-radius: 6; -fx-border-radius: 6;");
        }
    }

    private String stepContainerStyle(String state) {
        switch (state) {
            case "PROCESSING":
                return "-fx-background-color: rgba(56, 189, 248, 0.15); -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 8; -fx-background-radius: 8;";
            case "COMPLETED":
                return "-fx-background-color: rgba(16, 185, 129, 0.15); -fx-border-color: rgba(16, 185, 129, 0.4); -fx-border-radius: 8; -fx-background-radius: 8;";
            case "SKIPPED":
                return "-fx-background-color: rgba(255, 255, 255, 0.03); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 8; -fx-background-radius: 8;";
            case "FAILED":
                return "-fx-background-color: rgba(239, 68, 68, 0.15); -fx-border-color: rgba(239, 68, 68, 0.4); -fx-border-radius: 8; -fx-background-radius: 8;";
            default:
                return "-fx-background-color: rgba(13, 22, 38, 0.6); -fx-border-color: rgba(255, 255, 255, 0.06); -fx-border-radius: 8; -fx-background-radius: 8;";
        }
    }

    private String circleStyle(String state) {
        switch (state) {
            case "PROCESSING":
                return "-fx-background-color: " + INFO + "; -fx-text-fill: #070C16; -fx-background-radius: 20; -fx-font-size: 11px; -fx-font-weight: 700;";
            case "COMPLETED":
                return "-fx-background-color: " + SUCCESS + "; -fx-text-fill: #070C16; -fx-background-radius: 20; -fx-font-size: 11px; -fx-font-weight: 700;";
            case "FAILED":
                return "-fx-background-color: " + ERROR + "; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 11px; -fx-font-weight: 700;";
            default:
                return "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-text-fill: " + LIGHT_SECONDARY + "; -fx-background-radius: 20; -fx-font-size: 11px; -fx-font-weight: 700;";
        }
    }

    private String stepTextColor(String state) {
        switch (state) {
            case "PROCESSING": return INFO;
            case "COMPLETED": return "#34D399";
            case "FAILED": return "#F87171";
            default: return LIGHT_SECONDARY;
        }
    }

    private String normalizeTask(String task) {
        if (task == null) return "";
        String value = task.toLowerCase();

        if (value.contains("metadata")) return "Metadata";
        if (value.contains("ai") || value.contains("understand") || value.contains("classif")) return "AI";
        if (value.contains("space") || value.contains("categor")) return "Space";
        if (value.contains("firestore") || value.contains("save")) return "Saved";

        return task;
    }

    private StepView getStep(ProcessingView view, String name) {
        return switch (name) {
            case "Metadata" -> view.metadata;
            case "AI" -> view.ai;
            case "Space" -> view.space;
            case "Saved" -> view.saved;
            default -> null;
        };
    }

    private String getProcessingMessage(String step) {
        return switch (step) {
            case "Metadata" -> "Reading file information and generating its metadata.";
            case "AI" -> "Understanding the file and generating AI classification.";
            case "Space" -> "Resolving the best OneSpace category for this file.";
            case "Saved" -> "Saving the processed file information to your account.";
            default -> "Processing...";
        };
    }

    private String getCompletedMessage(String step) {
        return switch (step) {
            case "Metadata" -> "Metadata extracted successfully. Checking the file before continuing.";
            case "AI" -> "AI understanding completed. Moving to space resolution.";
            case "Space" -> "Space resolved successfully. Preparing to save.";
            case "Saved" -> "File information saved successfully.";
            default -> "Step completed.";
        };
    }

    private VBox findCard(File file) {
        for (var node : fileList.getChildren()) {
            if (!(node instanceof VBox)) continue;

            VBox card = (VBox) node;
            if (!(card.getUserData() instanceof ProcessingView)) continue;

            HBox header = (HBox) card.getChildren().get(0);
            VBox info = (VBox) header.getChildren().get(2);
            Label name = (Label) info.getChildren().get(0);

            if (name.getText().equals(file.getName())) return card;
        }
        return null;
    }

    private String getFileIconType(File file) {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".docx") || name.endsWith(".ppt") || name.endsWith(".pptx") || name.endsWith(".xls") || name.endsWith(".xlsx")) return "files";
        if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif")) return "media";
        if (name.endsWith(".mp4") || name.endsWith(".mov") || name.endsWith(".avi") || name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".m4a")) return "media";

        return "files";
    }

    private String safeMessage(Exception e) {
        if (e.getMessage() == null || e.getMessage().isBlank())
            return "An unexpected error occurred.";

        return e.getMessage();
    }

    private Region spacer(double width) {
        Region region = new Region();
        region.setPrefWidth(width);
        return region;
    }

    private String primaryButtonStyle() {
        return "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: white; -fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-padding: 10 20; -fx-background-radius: 10; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 10; -fx-border-width: 1; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.45), 10, 0, 0, 2);";
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);
        switch (type) {
            case "files": icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13"); break;
            case "media": icon.setContent("M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"); break;
            case "bell": icon.setContent("M6 17 H18 M8 17 V10 A4 4 0 0 1 16 10 V17 M10 20 H14"); break;
            case "plus": icon.setContent("M12 5v14M5 12h14"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }

    private static class ProcessingView {
        Label status, explanation;
        StepView metadata, ai, space, saved;

        ProcessingView(Label status, Label explanation, StepView metadata, StepView ai, StepView space, StepView saved) {
            this.status = status;
            this.explanation = explanation;
            this.metadata = metadata;
            this.ai = ai;
            this.space = space;
            this.saved = saved;
        }
    }

    private static class StepView {
        VBox container;
        Label circle, title, state;
        String name, number;

        StepView(VBox container, Label circle, Label title, Label state, String name) {
            this.container = container;
            this.circle = circle;
            this.title = title;
            this.state = state;
            this.name = name;
            this.number = getNumber(name);
        }

        private String getNumber(String name) {
            return switch (name) {
                case "Metadata" -> "1";
                case "AI" -> "2";
                case "Space" -> "3";
                case "Saved" -> "4";
                default -> "•";
            };
        }
    }
}