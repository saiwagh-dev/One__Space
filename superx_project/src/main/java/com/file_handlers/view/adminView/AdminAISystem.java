package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;
import com.file_handlers.model.UserSession;
import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;

public class AdminAISystem {

    private static final String FONT =
            "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    private static final String SIDEBAR_BG = "#070C16";
    public static final String SIDEBAR_DARK = "#070C16";
    private static final String SIDEBAR_BORDER = "rgba(255,255,255,0.07)";

    private static final String MAIN_BG =
            "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    private static final String CARD_BG =
            "linear-gradient(to bottom right, rgba(16,28,48,0.85), rgba(9,16,30,0.95))";
    private static final String CARD_BORDER = "rgba(56,189,248,0.22)";

    private static final String WHITE = "#FFFFFF";
    private static final String SECONDARY = "#94A3B8";

    private static final String BLUE = "#2563EB";
    private static final String CYAN = "#00D2FF";
    private static final String GREEN = "#10B981";
    private static final String ORANGE = "#F59E0B";
    private static final String ORANGE_LIGHT = "rgba(245, 158, 11, 0.15)";
    
    private String activeUserName = "Admin";
    private String initials = "A";

    public AdminAISystem() {UserSession session = UserSession.getInstance();

        if (session != null && session.getDisplayName() != null) {
            String fullName = session.getDisplayName().trim();
            if (!fullName.isEmpty()) {
                String[] parts = fullName.split("\\s+");
                this.activeUserName = parts[0];
                this.initials = this.activeUserName.substring(0, 1).toUpperCase();
            }
        }}

    public Scene getAdminAIScene() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createMainContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );

        VBox rightSide = new VBox(createTopBar(), scrollPane);
        rightSide.setStyle(
                "-fx-background: " + MAIN_BG + ";" +
                "-fx-background-color: " + MAIN_BG + ";"
        );
        rightSide.setFillWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.setCenter(rightSide);

        return new Scene(
                root,
                LandingPage.getCurrentWidth(),
                LandingPage.getCurrentHeight()
        );
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private VBox createSidebar() {

        VBox sidebar = new VBox(12);
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMaxWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle(
                "-fx-background-color: " + SIDEBAR_BG + ";" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 1 0 0;"
        );

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setTextFill(Color.WHITE);

        HBox logoRow = new HBox(10, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = createSidebarButton("dashboard", "Dashboard", false);
        Button users = createSidebarButton("users", "Users", false);
        Button files = createSidebarButton("files", "Files", false);
        Button collaboration = createSidebarButton("collaboration", "Collaboration", false);
        Button aiSystem = createSidebarButton("ai", "AI System", true);
        Button analytics = createSidebarButton("analytics", "Analytics", false);
        Button security = createSidebarButton("security", "Security", false);

        dashboard.setOnAction(e -> LandingPage.showAdminDashboard());
        users.setOnAction(e -> LandingPage.showAdminUsers());
        files.setOnAction(e -> LandingPage.showAdminFiles());
        collaboration.setOnAction(e -> LandingPage.showAdminCollaboration());
        analytics.setOnAction(e -> LandingPage.showAnalytics());
        security.setOnAction(e -> LandingPage.showAdminSecurity());

        VBox navigation = new VBox(
                4,
                dashboard,
                users,
                files,
                collaboration,
                aiSystem,
                analytics,
                security
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button settings = createSidebarButton("settings", "Settings", false);
        settings.setOnAction(e -> LandingPage.showAdminSettings());

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + SIDEBAR_BORDER + ";");

        Button logout = createSidebarButton("logout", "Logout", false);
        logout.setOnAction(e -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(
                logoSection,
                navigation,
                spacer,
                settings,
                divider,
                logout
        );

        return sidebar;
    }

    private StackPane createLogo() {

        Image logoImage = new Image(
                getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png")
        );

        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane pane = new StackPane(logoView);
        pane.setPrefSize(42, 42);
        pane.setAlignment(Pos.CENTER);

        return pane;
    }

    private Button createSidebarButton(
            String type,
            String text,
            boolean active
    ) {

        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(active ? WHITE : SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label label = new Label(text);
        label.setFont(
                Font.font(
                        FONT,
                        active ? FontWeight.BOLD : FontWeight.MEDIUM,
                        13
                )
        );
        label.setTextFill(Color.WHITE);

        HBox row = new HBox(12, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38);
        button.setMinHeight(38);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        if (active) {
            button.setStyle(
                    "-fx-background-color: linear-gradient(to right,#1D4ED8,#2563EB);" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: rgba(96,165,250,0.6);" +
                    "-fx-border-radius: 12;" +
                    "-fx-border-width: 1;" +
                    "-fx-cursor: hand;"
            );
        } else {
            button.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 12;" +
                    "-fx-cursor: hand;" +
                    "-fx-border-width: 0;"
            );

            button.setOnMouseEntered(e -> {
                button.setStyle(
                        "-fx-background-color: rgba(255,255,255,0.05);" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-width: 0;"
                );
                icon.setStroke(Color.WHITE);
                label.setTextFill(Color.WHITE);
            });

            button.setOnMouseExited(e -> {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-width: 0;"
                );
                icon.setStroke(Color.web(SECONDARY));
                label.setTextFill(Color.WHITE);
            });
        }

        return button;
    }

    // =========================================================
    // TOP BAR
    // =========================================================

    private HBox createTopBar() {

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notification = new Button();
        notification.setGraphic(bell);
        notification.setStyle(
                "-fx-background-color: rgba(13,22,38,0.85);" +
                "-fx-border-color: rgba(255,255,255,0.08);" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 6 10;"
        );
        notification.setOnAction(
                e -> LandingPage.showAdminNotificationPage()
        );

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle(
                "-fx-background-color: linear-gradient(to bottom right,#2563EB,#00D2FF);" +
                "-fx-background-radius: 50%;"
        );

        Label adminName = new Label(activeUserName);
        adminName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        adminName.setTextFill(Color.WHITE);

        HBox profile = new HBox(10, avatar, adminName);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(4, 12, 4, 6));
        profile.setStyle(
                "-fx-background-color: rgba(13,22,38,0.85);" +
                "-fx-border-color: rgba(255,255,255,0.08);" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-cursor: hand;"
        );

        Popup profilePopup = createProfilePopup();

        profile.setOnMouseClicked(e -> {
            if (profilePopup.isShowing()) {
                profilePopup.hide();
            } else {
                javafx.geometry.Point2D point =
                        profile.localToScreen(0, profile.getHeight());
                profilePopup.show(
                        profile,
                        point.getX() - 30,
                        point.getY() + 8
                );
            }
        });

        HBox topBar = new HBox(
                16,
                spacer,
                notification,
                profile
        );

        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPrefHeight(70);
        topBar.setMinHeight(70);
        topBar.setMaxHeight(70);
        topBar.setPadding(
                new Insets(
                        16,
                        ResponsiveUtil.PAGE_PADDING,
                        14,
                        ResponsiveUtil.PAGE_PADDING
                )
        );
        topBar.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: " + SIDEBAR_BORDER + ";" +
                "-fx-border-width: 0 0 1 0;"
        );

        return topBar;
    }

    private Popup createProfilePopup() {

        Popup popup = new Popup();
        popup.setAutoHide(true);

        HBox profileBtn = createProfilePopupItem(
                "users",
                "Profile Page",
                "#F59E0B",
                () -> {
                    popup.hide();
                    LandingPage.showAdminProfilePage();
                }
        );

        HBox settingsBtn = createProfilePopupItem(
                "settings",
                "Settings",
                "#38BDF8",
                () -> {
                    popup.hide();
                    LandingPage.showAdminSettings();
                }
        );

        HBox signOutBtn = createProfilePopupItem(
                "logout",
                "Sign Out",
                "#F87171",
                () -> {
                    popup.hide();
                    LandingPage.showAdminLoginPage();
                }
        );

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle(
                "-fx-background-color: rgba(255,255,255,0.08);"
        );

        VBox menu = new VBox(
                6,
                profileBtn,
                settingsBtn,
                divider,
                signOutBtn
        );

        menu.setPrefWidth(170);
        menu.setPadding(new Insets(10, 8, 10, 8));
        menu.setStyle(
                "-fx-background-color: #0B132B;" +
                "-fx-border-color: rgba(255,255,255,0.12);" +
                "-fx-border-width: 1.2;" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;"
        );

        popup.getContent().add(menu);
        return popup;
    }

    // =========================================================
    // AI STATUS
    // =========================================================

    private HBox createProfilePopupItem(
            String iconType,
            String text,
            String iconColor,
            Runnable action
    ) {

        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(22, 22);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        label.setTextFill(Color.WHITE);

        HBox item = new HBox(12, iconBox, label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(8, 10, 8, 10));
        item.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-cursor: hand;"
        );
        item.setOnMouseClicked(e -> action.run());

        return item;
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private VBox createMainContent() {

        Label title = createLabel(
                "AI System",
                "-fx-font-size: 24px; -fx-font-weight: bold;"
        );

        Label subtitle = createLabel(
                "Monitor the AI capabilities used by OneSpace.",
                "-fx-font-size: 13px; -fx-font-weight: 500;" +
                " -fx-text-fill: " + SECONDARY + ";"
        );

        VBox header = new VBox(4, title, subtitle);

        VBox content = new VBox(
                22,
                header,
                createAIStatusCard(),
                createAccuracyCard(),
                createConfigurationCard(),
                createCapabilitiesCard(),
                createProcessingCard()
        );

        content.setPadding(
                new Insets(
                        24,
                        ResponsiveUtil.PAGE_PADDING,
                        32,
                        ResponsiveUtil.PAGE_PADDING
                )
        );
        content.setFillWidth(true);
        content.setStyle("-fx-background-color: transparent;");

        return content;
    }

    // =========================================================
    // AI STATUS
    // =========================================================

    private VBox createAIStatusCard() {

        SVGPath aiIcon = createIcon("ai");
        aiIcon.setStroke(Color.web(CYAN));
        aiIcon.setStrokeWidth(2.2);

        StackPane iconPane = new StackPane(aiIcon);
        iconPane.setPrefSize(48, 48);
        iconPane.setMinSize(48, 48);
        iconPane.setMaxSize(48, 48);
        iconPane.setStyle(
                "-fx-background-color: rgba(0,210,255,0.15);" +
                "-fx-border-color: rgba(0,210,255,0.3);" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;"
        );

        Circle dot = new Circle(6, Color.web(GREEN));

        Label status = createLabel(
                "Online",
                "-fx-font-size: 15px; -fx-font-weight: bold;"
        );

        Label description = createLabel(
                "AI services are available for OneSpace processing.",
                "-fx-font-size: 12px; -fx-text-fill: " + SECONDARY + ";"
        );

        VBox text = new VBox(3, status, description);

        HBox row = new HBox(
                14,
                iconPane,
                dot,
                text
        );
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(row);
        card.setPadding(new Insets(20));
        card.setStyle(cardStyle());

        return card;
    }

    // =========================================================
    // ACCURACY / PERFORMANCE
    // =========================================================

    private VBox createAccuracyCard() {

        Label title = createLabel(
                "AI Accuracy & Performance Metrics",
                "-fx-font-size: 17px; -fx-font-weight: bold;"
        );

        ComboBox<String> filter = new ComboBox<>();
        filter.getItems().addAll(
                "Last 24 Hours",
                "Last 7 Days",
                "Last 30 Days"
        );
        filter.setValue("Last 7 Days");
        filter.setPrefHeight(30);
        filter.setStyle(
                "-fx-background-color: rgba(13,22,38,0.85);" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #FFFFFF;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(10, title, spacer, filter);
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane ring = createAccuracyRing();

        Label rate = new Label("Optimal Rate");
        rate.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        rate.setTextFill(Color.web("#34D399"));
        rate.setStyle(
                "-fx-background-color: rgba(16,185,129,0.15);" +
                "-fx-border-color: rgba(16,185,129,0.3);" +
                "-fx-padding: 3 8;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );

        Label confidence = createLabel(
                "Confidence Score: 94.2%",
                "-fx-font-size: 11px; -fx-font-weight: bold;" +
                " -fx-text-fill: " + SECONDARY + ";"
        );

        VBox ringBox = new VBox(
                10,
                ring,
                rate,
                confidence
        );
        ringBox.setAlignment(Pos.CENTER);
        ringBox.setPadding(new Insets(12, 20, 12, 20));
        ringBox.setStyle(
                "-fx-background-color: rgba(10,18,33,0.85);" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;"
        );

        VBox metrics = new VBox(
                14,
                createAccuracyRow(
                        "File Auto-Categorization",
                        "96.4%",
                        0.964,
                        BLUE
                ),
                createAccuracyRow(
                        "OCR & Text Extraction",
                        "94.1%",
                        0.941,
                        CYAN
                ),
                createAccuracyRow(
                        "Auto-Tagging & Metadata",
                        "91.8%",
                        0.918,
                        GREEN
                )
        );

        HBox body = new HBox(24, ringBox, metrics);
        body.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(metrics, Priority.ALWAYS);

        Label model = createLabel(
                "AI Model: Gemini",
                "-fx-font-size: 11px; -fx-font-weight: bold;"
        );
        model.setStyle(
                "-fx-background-color: rgba(255,255,255,0.08);" +
                "-fx-border-color: rgba(255,255,255,0.1);" +
                "-fx-padding: 4 10;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;"
        );

        Label correction = createLabel(
                "User Correction Rate: 2.3%",
                "-fx-font-size: 11px; -fx-font-weight: bold;" +
                " -fx-text-fill: #34D399;"
        );
        correction.setStyle(
                "-fx-background-color: rgba(16,185,129,0.15);" +
                "-fx-border-color: rgba(16,185,129,0.3);" +
                "-fx-padding: 4 10;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;"
        );

        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);

        HBox footer = new HBox(
                12,
                model,
                footerSpacer,
                correction
        );
        footer.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(
                16,
                header,
                body,
                footer
        );
        card.setPadding(new Insets(20));
        card.setStyle(cardStyle());

        return card;
    }

    private StackPane createAccuracyRing() {

        double value = 0.941;

        Circle background = new Circle(58);
        background.setFill(Color.TRANSPARENT);
        background.setStroke(Color.web("#26354A"));
        background.setStrokeWidth(10);

        Arc progress = new Arc(
                0,
                0,
                58,
                58,
                90,
                -360 * value
        );
        progress.setFill(Color.TRANSPARENT);
        progress.setStroke(Color.web(CYAN));
        progress.setStrokeWidth(10);
        progress.setStrokeLineCap(StrokeLineCap.ROUND);
        progress.setType(ArcType.OPEN);

        Label valueLabel = createLabel(
                "94.1%",
                "-fx-font-size: 19px; -fx-font-weight: bold;"
        );

        Label smallLabel = createLabel(
                "Accuracy",
                "-fx-font-size: 10px; -fx-font-weight: bold;"
        );

        VBox center = new VBox(1, valueLabel, smallLabel);
        center.setAlignment(Pos.CENTER);

        StackPane ring = new StackPane(
                background,
                progress,
                center
        );
        ring.setPrefSize(130, 130);
        ring.setMinSize(130, 130);
        ring.setMaxSize(130, 130);

        return ring;
    }

    private VBox createAccuracyRow(
            String title,
            String percent,
            double progress,
            String color
    ) {

        Label name = createLabel(
                title,
                "-fx-font-size: 12px; -fx-font-weight: bold;"
        );

        Label value = createLabel(
                percent,
                "-fx-font-size: 12px; -fx-font-weight: bold;" +
                " -fx-text-fill: " + color + ";"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox top = new HBox(name, spacer, value);

        StackPane track = new StackPane();
        track.setPrefHeight(8);
        track.setMinHeight(8);
        track.setMaxHeight(8);
        track.setMaxWidth(Double.MAX_VALUE);
        track.setStyle(
                "-fx-background-color: rgba(255,255,255,0.08);" +
                "-fx-background-radius: 4;"
        );

        Region fill = new Region();
        fill.setPrefHeight(8);
        fill.setMaxHeight(8);
        fill.prefWidthProperty().bind(
                track.widthProperty().multiply(progress)
        );
        fill.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-background-radius: 4;"
        );
        StackPane.setAlignment(fill, Pos.CENTER_LEFT);

        track.getChildren().add(fill);

        VBox row = new VBox(5, top, track);
        row.setPadding(new Insets(6, 0, 6, 0));
        HBox.setHgrow(row, Priority.ALWAYS);

        return row;
    }

    // =========================================================
    // CONFIGURATION
    // =========================================================

    private VBox createConfigurationCard() {

        Label title = createLabel(
                "AI Configuration",
                "-fx-font-size: 17px; -fx-font-weight: bold;"
        );

        VBox model = createInfoRow(
                "Model",
                "Gemini"
        );

        VBox integration = createInfoRow(
                "Integration",
                "AIClassificationService"
        );

        VBox output = createInfoRow(
                "AI Output",
                "Category • Confidence • Description • Smart Tags"
        );

        VBox details = new VBox(
                12,
                model,
                integration,
                output
        );

        VBox card = new VBox(
                16,
                title,
                details
        );

        card.setPadding(new Insets(20));
        card.setStyle(cardStyle());

        return card;
    }

    private VBox createInfoRow(
            String labelText,
            String valueText
    ) {

        Label label = createLabel(
                labelText,
                "-fx-font-size: 12px; -fx-font-weight: bold;" +
                " -fx-text-fill: " + SECONDARY + ";"
        );

        Label value = createLabel(
                valueText,
                "-fx-font-size: 13px; -fx-font-weight: bold;"
        );

        VBox row = new VBox(3, label, value);
        row.setPadding(new Insets(10, 12, 10, 12));
        row.setStyle(
                "-fx-background-color: rgba(10,18,33,0.85);" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );

        return row;
    }

    // =========================================================
    // CAPABILITIES
    // =========================================================

    private VBox createCapabilitiesCard() {

        Label title = createLabel(
                "AI Capabilities",
                "-fx-font-size: 17px; -fx-font-weight: bold;"
        );

        VBox capabilities = new VBox(
                10,
                createCapability(
                        "File Understanding",
                        "Processes extracted file content for AI analysis."
                ),
                createCapability(
                        "Auto-Categorization",
                        "Generates a OneSpace category for a file."
                ),
                createCapability(
                        "Confidence Score",
                        "Returns an AI confidence value for classification."
                ),
                createCapability(
                        "Description Generation",
                        "Generates a short description from analyzed content."
                ),
                createCapability(
                        "Smart Tags",
                        "Generates relevant tags for the file."
                ),
                createCapability(
                        "AI Search",
                        "Supports AI-assisted searching across file information."
                )
        );

        VBox card = new VBox(
                16,
                title,
                capabilities
        );

        card.setPadding(new Insets(20));
        card.setStyle(cardStyle());

        return card;
    }

    private HBox createCapability(
            String titleText,
            String descriptionText
    ) {

        Circle check = new Circle(5, Color.web(GREEN));

        Label title = createLabel(
                titleText,
                "-fx-font-size: 13px; -fx-font-weight: bold;"
        );

        Label description = createLabel(
                descriptionText,
                "-fx-font-size: 11px; -fx-text-fill: " + SECONDARY + ";"
        );

        VBox text = new VBox(2, title, description);

        HBox row = new HBox(10, check, text);
        row.setAlignment(Pos.TOP_LEFT);

        return row;
    }

    // =========================================================
    // PROCESSING FLOW
    // =========================================================

    private VBox createProcessingCard() {

        Label title = createLabel(
                "AI Processing Flow",
                "-fx-font-size: 17px; -fx-font-weight: bold;"
        );

        HBox flow = new HBox(
                10,
                createFlowStep("1", "File Content"),
                createArrow(),
                createFlowStep("2", "AI Analysis"),
                createArrow(),
                createFlowStep("3", "Category"),
                createArrow(),
                createFlowStep("4", "Tags & Description")
        );

        flow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(
                16,
                title,
                flow
        );

        card.setPadding(new Insets(20));
        card.setStyle(cardStyle());

        return card;
    }

    private VBox createFlowStep(
            String number,
            String text
    ) {

        Label numberLabel = createLabel(
                number,
                "-fx-font-size: 12px; -fx-font-weight: bold;"
        );

        StackPane numberBox = new StackPane(numberLabel);
        numberBox.setPrefSize(28, 28);
        numberBox.setMinSize(28, 28);
        numberBox.setStyle(
                "-fx-background-color: rgba(37,99,235,0.15);" +
                "-fx-border-color: rgba(37,99,235,0.4);" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );

        Label textLabel = createLabel(
                text,
                "-fx-font-size: 11px; -fx-font-weight: bold;"
        );

        VBox box = new VBox(
                7,
                numberBox,
                textLabel
        );
        box.setAlignment(Pos.CENTER);
        box.setMinWidth(120);

        return box;
    }

    private Label createArrow() {

        return createLabel(
                "→",
                "-fx-font-size: 18px; -fx-font-weight: bold;" +
                " -fx-text-fill: #64748B;"
        );
    }

    private String cardStyle() {

        return "-fx-background-color: " + CARD_BG + ";" +
               "-fx-border-color: " + CARD_BORDER + ";" +
               "-fx-border-width: 1.2;" +
               "-fx-border-radius: 20;" +
               "-fx-background-radius: 20;" +
               "-fx-effect: dropshadow(three-pass-box,rgba(0,0,0,0.6),24,0,0,10);";
    }

    private Label createLabel(
            String text,
            String style
    ) {

        Label label = new Label(text);
        label.setFont(Font.font(FONT));
        label.setTextFill(Color.WHITE);
        label.setStyle(
                "-fx-font-family: " + FONT + ";" +
                style +
                " -fx-text-fill: #FFFFFF;"
        );

        return label;
    }

    // =========================================================
    // ICONS
    // =========================================================

    private SVGPath createIcon(String type) {

        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);

        switch (type) {

            case "dashboard":
                icon.setContent(
                        "M3 3 H10 V10 H3 Z " +
                        "M14 3 H21 V10 H14 Z " +
                        "M3 14 H10 V21 H3 Z " +
                        "M14 14 H21 V21 H14 Z"
                );
                break;

            case "users":
                icon.setContent(
                        "M8 11 A3 3 0 1 0 8 5 " +
                        "A3 3 0 0 0 8 11 Z " +
                        "M16 11 A3 3 0 1 0 16 5 " +
                        "A3 3 0 0 0 16 11 Z " +
                        "M2 20 C2 16 5 14 8 14 " +
                        "C11 14 14 16 14 20 " +
                        "M12 15 C14 14 17 14 19 15 " +
                        "C21 16 22 18 22 20"
                );
                break;

            case "files":
                icon.setContent(
                        "M5 2 H14 L19 7 V21 H5 Z " +
                        "M14 2 V7 H19 " +
                        "M8 11 H16 M8 15 H16 M8 18 H13"
                );
                break;

            case "collaboration":
                icon.setContent(
                        "M17 21v-2a4 4 0 0 0-4-4H5" +
                        "a4 4 0 0 0-4 4v2 " +
                        "M9 11a4 4 0 1 0 0-8" +
                        "a4 4 0 0 0 0 8 " +
                        "M23 21v-2a4 4 0 0 0-3-3.87 " +
                        "M16 3.13a4 4 0 0 1 0 7.75"
                );
                break;

            case "ai":
                icon.setContent(
                        "M12 2 L13.5 8.5 L20 7 " +
                        "L15.5 11.5 L21 15 L14 14.5 " +
                        "L12 22 L10 14.5 L3 15 " +
                        "L8.5 11.5 L4 7 L10.5 8.5 Z"
                );
                break;

            case "analytics":
                icon.setContent(
                        "M4 20 V11 M10 20 V6 " +
                        "M16 20 V13 M22 20 V3"
                );
                break;

            case "security":
                icon.setContent(
                        "M12 2 L20 5 V11 C20 16 17 20 12 22 " +
                        "C7 20 4 16 4 11 V5 Z " +
                        "M9 12 L11 14 L15 9"
                );
                break;

            case "settings":
                icon.setContent(
                        "M12 3 V6 M12 18 V21 " +
                        "M3 12 H6 M18 12 H21 " +
                        "M5.6 5.6 L7.7 7.7 " +
                        "M16.3 16.3 L18.4 18.4 " +
                        "M18.4 5.6 L16.3 7.7 " +
                        "M7.7 16.3 L5.6 18.4 " +
                        "M12 8 A4 4 0 1 0 12 16 " +
                        "A4 4 0 0 0 12 8"
                );
                break;

            case "logout":
                icon.setContent(
                        "M10 4 H5 V20 H10 " +
                        "M14 8 L19 12 L14 16 " +
                        "M19 12 H8"
                );
                break;

            case "bell":
                icon.setContent(
                        "M6 17 H18 M8 17 V10 " +
                        "A4 4 0 0 1 16 10 V17 " +
                        "M10 20 H14"
                );
                break;

            default:
                icon.setContent("M4 4 H20 V20 H4 Z");
                break;
        }

        return icon;
    }
}
