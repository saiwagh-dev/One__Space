package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;
import com.file_handlers.model.UserSession;
import com.file_handlers.util.ResponsiveUtil;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;

public class AdminProfilePage {

    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    
    // 1. Sidebar & Top Bar Tones
    private static final String SIDEBAR_BG = "#070C16";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";
    
    // 2. Center Canvas Radial Glow Background
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";
    
    // 3. Main Glassmorphic Cards & Text Colors
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";
    private static final String INPUT_BG = "rgba(10, 18, 33, 0.85)";
    
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";
    private static final String BLUE_LIGHT = "rgba(0, 210, 255, 0.15)";
    private static final String GREEN = "#10B981";
    private static final String DANGER_BORDER = "rgba(239, 68, 68, 0.4)";
    private static final String DANGER_BTN = "#DC2626";
    
    private String activeUserName = "Admin";
    private String initials = "A";

    // Form fields that can be updated from the modal
    private TextField fullNameField;
    private TextField emailField;
    private TextField usernameField;
    private TextArea bioArea;

    private Label heroNameLabel;
    private Label heroEmailLabel;
    private Label heroHandleLabel;
    private Label heroDescLabel;
    private Label bigAvatar;
    private Label topBarAdminName;
    private Label topBarAvatar;

    private void applyHoverAnimation(Node node, double scale, double translateY) {
        DropShadow blueGlow = new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(56, 189, 248, 0.6), 12, 0, 0, 2);
        ScaleTransition st = new ScaleTransition(Duration.millis(180), node);
        TranslateTransition tt = new TranslateTransition(Duration.millis(180), node);

        node.setOnMouseEntered(e -> {
            node.setEffect(blueGlow);
            st.stop();
            tt.stop();
            st.setToX(scale);
            st.setToY(scale);
            tt.setToY(translateY);
            st.play();
            tt.play();
        });

        node.setOnMouseExited(e -> {
            node.setEffect(null);
            st.stop();
            tt.stop();
            st.setToX(1.0);
            st.setToY(1.0);
            tt.setToY(0);
            st.play();
            tt.play();
        });
    }

    private void updateAdminSymbol(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            if (bigAvatar != null && bigAvatar.getGraphic() == null) bigAvatar.setText("A");
            if (topBarAvatar != null) topBarAvatar.setText("A");
            return;
        }

        String[] parts = fullName.trim().split("\\s+");
        String symbol;
        if (parts.length > 1 && !parts[1].isEmpty()) {
            symbol = (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        } else {
            symbol = parts[0].substring(0, 1).toUpperCase();
        }

        if (bigAvatar != null && bigAvatar.getGraphic() == null) {
            bigAvatar.setText(symbol);
        }
        if (topBarAvatar != null) {
            topBarAvatar.setText(parts[0].substring(0, 1).toUpperCase());
        }
        if (topBarAdminName != null) {
            topBarAdminName.setText(parts[0]);
        }
    }

    public AdminProfilePage() { UserSession session = UserSession.getInstance();

        if (session != null && session.getDisplayName() != null) {
            String fullName = session.getDisplayName().trim();
            if (!fullName.isEmpty()) {
                String[] parts = fullName.split("\\s+");
                this.activeUserName = parts[0];
                this.initials = this.activeUserName.substring(0, 1).toUpperCase();
            }
        }}

    public Scene getAdminProfileScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(createSidebar());

        ScrollPane scrollPane = new ScrollPane(createProfileContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-background-insets: 0; -fx-padding: 0;");

        VBox rightSide = new VBox(createTopBar(), scrollPane);
        rightSide.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        rightSide.setFillWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.setCenter(rightSide);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH); sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH); sidebar.setMaxWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setTextFill(Color.web(WHITE));

        HBox logoRow = new HBox(10, createLogo(), logoText);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        VBox logoSection = new VBox(4, logoRow);
        logoSection.setPadding(new Insets(0, 0, 18, 6));

        Button dashboard = createSidebarButton("dashboard", "Dashboard", false);
        dashboard.setOnAction(e -> LandingPage.showAdminDashboard());
        Button users = createSidebarButton("users", "Users", false);
        users.setOnAction(e -> LandingPage.showAdminUsers());
        Button files = createSidebarButton("files", "Files", false);
        files.setOnAction(e -> LandingPage.showAdminFiles());
        Button collaboration = createSidebarButton("collaboration", "Collaboration", false);
        collaboration.setOnAction(e -> LandingPage.showAdminCollaboration());

        Button aiSystem = createSidebarButton("ai", "AI System", false);
        aiSystem.setOnAction(e -> LandingPage.showAdminAISystem());

        Button analytics = createSidebarButton("analytics", "Analytics", false);
        analytics.setOnAction(e -> LandingPage.showAnalytics());
        
        Button security = createSidebarButton("security", "Security", false);
        security.setOnAction(e -> LandingPage.showAdminSecurity());

        VBox navigation = new VBox(4, dashboard, users, files, collaboration, aiSystem, analytics, security);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button settings = createSidebarButton("settings", "Settings", false);
        settings.setOnAction(e -> LandingPage.showAdminSettings());

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + SIDEBAR_BORDER + ";");

        Button logout = createSidebarButton("logout", "Logout", false);
        logout.setOnAction(event -> LandingPage.showAdminLoginPage());

        sidebar.getChildren().addAll(logoSection, navigation, spacer, settings, divider, logout);
        return sidebar;
    }

    private StackPane createLogo() {
        Image logoImage = new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane logoPane = new StackPane(logoView);
        logoPane.setPrefSize(42, 42);
        logoPane.setAlignment(Pos.CENTER);
        applyHoverAnimation(logoPane, 1.08, 0);
        return logoPane;
    }

    private Button createSidebarButton(String type, String text, boolean active) {
        SVGPath icon = createIcon(type);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.web(WHITE));

        HBox row = new HBox(12, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setPrefHeight(38); button.setMinHeight(38);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));

        ScaleTransition st = new ScaleTransition(Duration.millis(160), button);
        TranslateTransition tt = new TranslateTransition(Duration.millis(160), button);
        DropShadow blueGlow = new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(37, 99, 235, 0.75), 14, 0, 0, 2);

        if (active) {
            button.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);"
            );
            button.setOnMouseEntered(e -> {
                st.stop(); tt.stop();
                st.setToX(1.02); st.setToY(1.02);
                tt.setToX(3);
                st.play(); tt.play();
            });
            button.setOnMouseExited(e -> {
                st.stop(); tt.stop();
                st.setToX(1.0); st.setToY(1.0);
                tt.setToX(0);
                st.play(); tt.play();
            });
        } else {
            button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color: rgba(37, 99, 235, 0.15); -fx-border-color: rgba(56, 189, 248, 0.5); -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 1;");
                button.setEffect(blueGlow);
                icon.setStroke(Color.web("#38BDF8"));
                label.setTextFill(Color.WHITE);
                st.stop(); tt.stop();
                st.setToX(1.02); st.setToY(1.02);
                tt.setToX(3);
                st.play(); tt.play();
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                button.setEffect(null);
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                label.setTextFill(Color.web(WHITE));
                st.stop(); tt.stop();
                st.setToX(1.0); st.setToY(1.0);
                tt.setToX(0);
                st.play(); tt.play();
            });
        }
        return button;
    }

    private HBox createTopBar() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        SVGPath bell = createIcon("bell");
        bell.setStroke(Color.WHITE);
        bell.setStrokeWidth(2);

        Button notification = new Button();
        notification.setGraphic(bell);
        notification.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        notification.setOnAction(e -> LandingPage.showAdminNotificationPage());

        DropShadow blueGlow = new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(56, 189, 248, 0.6), 14, 0, 0, 2);
        ScaleTransition stNotif = new ScaleTransition(Duration.millis(180), notification);
        TranslateTransition ttNotif = new TranslateTransition(Duration.millis(180), notification);
        notification.setOnMouseEntered(e -> {
            notification.setStyle("-fx-background-color: rgba(37, 99, 235, 0.2); -fx-border-color: #38BDF8; -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
            notification.setEffect(blueGlow);
            stNotif.stop(); ttNotif.stop();
            stNotif.setToX(1.05); stNotif.setToY(1.05);
            ttNotif.setToY(-2);
            stNotif.play(); ttNotif.play();
        });
        notification.setOnMouseExited(e -> {
            notification.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
            notification.setEffect(null);
            stNotif.stop(); ttNotif.stop();
            stNotif.setToX(1.0); stNotif.setToY(1.0);
            ttNotif.setToY(0);
            stNotif.play(); ttNotif.play();
        });

        topBarAvatar = new Label(initials);
        topBarAvatar.setPrefSize(34, 34); topBarAvatar.setAlignment(Pos.CENTER);
        topBarAvatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        topBarAvatar.setTextFill(Color.WHITE);
        topBarAvatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        topBarAdminName = new Label(activeUserName);
        topBarAdminName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        topBarAdminName.setTextFill(Color.WHITE);

        HBox profile = new HBox(10, topBarAvatar, topBarAdminName);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(4, 12, 4, 6));
        profile.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");

        ScaleTransition stProf = new ScaleTransition(Duration.millis(180), profile);
        TranslateTransition ttProf = new TranslateTransition(Duration.millis(180), profile);
        profile.setOnMouseEntered(e -> {
            profile.setStyle("-fx-background-color: rgba(37, 99, 235, 0.2); -fx-border-color: #38BDF8; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");
            profile.setEffect(blueGlow);
            stProf.stop(); ttProf.stop();
            stProf.setToX(1.03); stProf.setToY(1.03);
            ttProf.setToY(-1);
            stProf.play(); ttProf.play();
        });
        profile.setOnMouseExited(e -> {
            profile.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");
            profile.setEffect(null);
            stProf.stop(); ttProf.stop();
            stProf.setToX(1.0); stProf.setToY(1.0);
            ttProf.setToY(0);
            stProf.play(); ttProf.play();
        });

        ContextMenu profileMenu = createProfileMenu();
        profile.setOnMouseClicked(e -> {
            if (profileMenu.isShowing()) {
                profileMenu.hide();
            } else {
                profileMenu.show(profile, Side.BOTTOM, -50, 8);
            }
        });

        HBox topBar = new HBox(16, spacer, notification, profile);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPrefHeight(70);
        topBar.setMinHeight(70);
        topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private ContextMenu createProfileMenu() {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setStyle(
            "-fx-background-color: #0B132B;" +
            "-fx-background-insets: 0;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: rgba(255, 255, 255, 0.1);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 14;" +
            "-fx-padding: 6;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.75), 24, 0, 0, 8);"
        );

        Button profileBtn = createProfileMenuItem(
            "users", 
            "Profile Page", 
            "#F59E0B", 
            () -> {
                contextMenu.hide();
                LandingPage.showAdminProfilePage();
            }
        );

        Button settingsBtn = createProfileMenuItem(
            "settings", 
            "Settings", 
            "#38BDF8", 
            () -> {
                contextMenu.hide();
                LandingPage.showAdminSettings();
            }
        );

        Button signOutBtn = createProfileMenuItem(
            "logout", 
            "Sign Out", 
            "#F87171", 
            () -> {
                contextMenu.hide();
                LandingPage.showAdminLoginPage();
            }
        );

        Region menuDivider = new Region();
        menuDivider.setPrefHeight(1);
        menuDivider.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-margin: 4 0;");

        VBox menuBox = new VBox(4, profileBtn, settingsBtn, menuDivider, signOutBtn);
        menuBox.setPrefWidth(168);
        menuBox.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");

        CustomMenuItem customMenuItem = new CustomMenuItem(menuBox, false);
        customMenuItem.setHideOnClick(false);
        customMenuItem.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-background-insets: 0;");
        contextMenu.getItems().add(customMenuItem);

        return contextMenu;
    }

    private Button createProfileMenuItem(String iconType, String text, String iconColor, Runnable action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(1.8);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(20, 20);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        label.setTextFill(Color.WHITE);

        HBox row = new HBox(12, iconBox, label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(row);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(8, 12, 8, 12));
        button.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-border-width: 0; -fx-cursor: hand;");

        ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
        TranslateTransition tt = new TranslateTransition(Duration.millis(150), button);

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: rgba(37, 99, 235, 0.2); -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            st.stop(); tt.stop();
            st.setToX(1.03); st.setToY(1.03);
            tt.setToX(3);
            st.play(); tt.play();
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            st.stop(); tt.stop();
            st.setToX(1.0); st.setToY(1.0);
            tt.setToX(0);
            st.play(); tt.play();
        });

        button.setOnAction(e -> action.run());
        return button;
    }

    private VBox createProfileContent() {
        Label title = new Label("Admin Profile");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        title.setTextFill(Color.web(WHITE));

        Label subtitle = new Label("View and manage your administrator account information and preferences.");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

        Label savedStatus = new Label("✓ All changes saved");
        savedStatus.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        savedStatus.setTextFill(Color.web(GREEN));

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox headerRow = new HBox(title, headerSpacer, savedStatus);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        VBox heading = new VBox(4, headerRow, subtitle);

        VBox heroCard = createHeroCard();
        VBox adminDetailsCard = createAdminDetailsCard();
        VBox adminActionsCard = createAdminActionsCard();

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        Button resetBtn = new Button("Reset Changes");
        resetBtn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 16;");
        applyHoverAnimation(resetBtn, 1.04, -2);
        resetBtn.setOnAction(e -> {
            fullNameField.setText("Admin User");
            emailField.setText("admin@onespace.com");
            usernameField.setText("@admin");
            bioArea.setText("System administrator with full access to OneSpace platform and all resources.");
            heroNameLabel.setText("Admin User");
            heroEmailLabel.setText("admin@onespace.com");
            heroHandleLabel.setText("@admin");
            heroDescLabel.setText("System administrator with full access to OneSpace platform and all resources.");
            updateAdminSymbol("Admin User");

            savedStatus.setText("✓ Reset to defaults");
            savedStatus.setTextFill(Color.web(GREEN));
        });
        
        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #0284C7); -fx-background-radius: 8; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 16; -fx-effect: dropshadow(three-pass-box, rgba(2, 132, 199, 0.5), 14, 0, 0, 3);");
        applyHoverAnimation(saveBtn, 1.04, -2);
        saveBtn.setOnAction(e -> {
            String nameVal = fullNameField.getText().trim();
            String emailVal = emailField.getText().trim();
            String userVal = usernameField.getText().trim();
            if (!userVal.startsWith("@") && !userVal.isEmpty()) {
                userVal = "@" + userVal;
                usernameField.setText(userVal);
            }
            heroNameLabel.setText(nameVal.isEmpty() ? "Admin User" : nameVal);
            heroEmailLabel.setText(emailVal.isEmpty() ? "admin@onespace.com" : emailVal);
            heroHandleLabel.setText(userVal.isEmpty() ? "@admin" : userVal);
            heroDescLabel.setText(bioArea.getText());

            updateAdminSymbol(nameVal);

            UserSession session = UserSession.getInstance();
            if (session != null) {
                if (!nameVal.isEmpty()) session.setDisplayName(nameVal);
                if (!emailVal.isEmpty()) session.setEmail(emailVal);
            }
            savedStatus.setText("✓ All changes saved");
            savedStatus.setTextFill(Color.web(GREEN));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Profile updated successfully!");
            alert.showAndWait();
        });

        HBox actionFooter = new HBox(12, bottomSpacer, resetBtn, saveBtn);
        actionFooter.setAlignment(Pos.CENTER_RIGHT);
        actionFooter.setPadding(new Insets(10, 0, 0, 0));

        VBox content = new VBox(22, heading, heroCard, adminDetailsCard, adminActionsCard, actionFooter);
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setFillWidth(true);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setStyle("-fx-background-color: transparent;");
        return content;
    }

    private VBox createHeroCard() {
        bigAvatar = new Label(initials);
        bigAvatar.setPrefSize(72, 72); bigAvatar.setMinSize(72, 72); bigAvatar.setMaxSize(72, 72);
        bigAvatar.setAlignment(Pos.CENTER);
        bigAvatar.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        bigAvatar.setTextFill(Color.WHITE);
        bigAvatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        Button changePhotoBtn = new Button("Change Photo");
        SVGPath cameraIcon = createIcon("camera");
        cameraIcon.setStroke(Color.WHITE);
        cameraIcon.setStrokeWidth(2);
        changePhotoBtn.setGraphic(cameraIcon);
        changePhotoBtn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-text-fill: " + WHITE + "; -fx-font-size: 11px; -fx-cursor: hand;");
        applyHoverAnimation(changePhotoBtn, 1.04, -1);
        changePhotoBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Profile Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                try {
                    ImageView imgView = new ImageView(new Image(new FileInputStream(file)));
                    imgView.setFitWidth(64);
                    imgView.setFitHeight(64);
                    imgView.setPreserveRatio(true);
                    bigAvatar.setText("");
                    bigAvatar.setGraphic(imgView);
                } catch (Exception ignored) {}
            }
        });

        VBox avatarCol = new VBox(10, bigAvatar, changePhotoBtn);
        avatarCol.setAlignment(Pos.CENTER);

        heroNameLabel = new Label("Admin User");
        heroNameLabel.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        heroNameLabel.setStyle("-fx-text-fill: " + WHITE + ";");

        Label badge = new Label("Super Admin");
        badge.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        badge.setTextFill(Color.web("#00D2FF"));
        badge.setPadding(new Insets(2, 8, 2, 8));
        badge.setStyle("-fx-background-color: " + BLUE_LIGHT + "; -fx-border-color: rgba(0, 210, 255, 0.3); -fx-border-radius: 10; -fx-background-radius: 10;");

        HBox nameRow = new HBox(10, heroNameLabel, badge);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        heroEmailLabel = new Label("admin@onespace.com");
        heroEmailLabel.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        heroEmailLabel.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        heroHandleLabel = new Label("@admin");
        heroHandleLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        heroHandleLabel.setTextFill(Color.web("#38BDF8"));

        HBox metaRow = new HBox(15, heroEmailLabel, heroHandleLabel);
        metaRow.setAlignment(Pos.CENTER_LEFT);

        Label memberSince = new Label("📅 Member since Jan 15, 2024");
        memberSince.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        memberSince.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        heroDescLabel = new Label("System administrator with full access to OneSpace platform and all resources.");
        heroDescLabel.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        heroDescLabel.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox infoCol = new VBox(6, nameRow, metaRow, memberSince, heroDescLabel);
        infoCol.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editProfileBtn = new Button("Edit Profile");
        editProfileBtn.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #0284C7); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
        applyHoverAnimation(editProfileBtn, 1.04, -2);
        editProfileBtn.setOnAction(e -> openEditProfileModal());

        Button changePassBtn = new Button("Change Password");
        changePassBtn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: " + WHITE + "; -fx-font-weight: bold; -fx-cursor: hand;");
        applyHoverAnimation(changePassBtn, 1.04, -2);
        changePassBtn.setOnAction(e -> openChangePasswordModal());

        VBox buttonsCol = new VBox(10, editProfileBtn, changePassBtn);
        buttonsCol.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(25, avatarCol, infoCol, spacer, buttonsCol);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(row);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");
        applyHoverAnimation(card, 1.01, -2);
        return card;
    }

    private void openEditProfileModal() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Edit Profile");
        modalStage.setResizable(false);

        Label modalTitle = new Label("Edit Profile");
        modalTitle.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        modalTitle.setStyle("-fx-text-fill: " + WHITE + ";");

        Label modalSub = new Label("Update your OneSpace profile information.");
        modalSub.setFont(Font.font(FONT, FontWeight.NORMAL, 11));
        modalSub.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox headerBox = new VBox(2, modalTitle, modalSub);
        headerBox.setPadding(new Insets(16, 16, 10, 16));
        headerBox.setStyle("-fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-width: 0 0 1 0;");

        TextField modalNameField = new TextField(fullNameField.getText());
        modalNameField.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
        Label fnLbl = new Label("Full Name");
        fnLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        fnLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        VBox fnGroup = new VBox(4, fnLbl, modalNameField);

        TextField modalEmailField = new TextField(emailField.getText());
        modalEmailField.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
        Label emLbl = new Label("Email Address");
        emLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        emLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        VBox emGroup = new VBox(4, emLbl, modalEmailField);

        TextField modalUsernameField = new TextField(usernameField.getText());
        modalUsernameField.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
        Label unLbl = new Label("Username");
        unLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        unLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        VBox unGroup = new VBox(4, unLbl, modalUsernameField);

        TextArea modalBioArea = new TextArea(bioArea.getText());
        modalBioArea.setPrefRowCount(3);
        modalBioArea.setWrapText(true);
        modalBioArea.setStyle("-fx-control-inner-background: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6;");
        Label bioLbl = new Label("Bio");
        bioLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        bioLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        VBox bioGroup = new VBox(4, bioLbl, modalBioArea);

        VBox formContent = new VBox(14, fnGroup, emGroup, unGroup, bioGroup);
        formContent.setPadding(new Insets(16));

        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16;");
        applyHoverAnimation(saveBtn, 1.04, -1);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-text-fill: " + WHITE + "; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16;");
        applyHoverAnimation(cancelBtn, 1.04, -1);
        cancelBtn.setOnAction(e -> modalStage.close());

        saveBtn.setOnAction(e -> {
            String uName = modalUsernameField.getText().trim();
            if (!uName.startsWith("@") && !uName.isEmpty()) {
                uName = "@" + uName;
            }
            fullNameField.setText(modalNameField.getText().trim());
            emailField.setText(modalEmailField.getText().trim());
            usernameField.setText(uName);
            bioArea.setText(modalBioArea.getText().trim());

            heroNameLabel.setText(modalNameField.getText().trim());
            heroEmailLabel.setText(modalEmailField.getText().trim());
            heroHandleLabel.setText(uName);
            heroDescLabel.setText(modalBioArea.getText().trim());

            updateAdminSymbol(modalNameField.getText().trim());

            UserSession session = UserSession.getInstance();
            if (session != null) {
                if (!modalNameField.getText().trim().isEmpty()) session.setDisplayName(modalNameField.getText().trim());
                if (!modalEmailField.getText().trim().isEmpty()) session.setEmail(modalEmailField.getText().trim());
            }

            modalStage.close();
        });

        Region modalSpacer = new Region();
        HBox.setHgrow(modalSpacer, Priority.ALWAYS);

        HBox modalFooter = new HBox(10, modalSpacer, cancelBtn, saveBtn);
        modalFooter.setAlignment(Pos.CENTER_RIGHT);
        modalFooter.setPadding(new Insets(10, 16, 16, 16));
        modalFooter.setStyle("-fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-width: 1 0 0 0;");

        VBox modalRoot = new VBox(headerBox, formContent, modalFooter);
        modalRoot.setStyle("-fx-background-color: #0D1626; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1;");

        Scene modalScene = new Scene(modalRoot, 460, 470);
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

    private void openChangePasswordModal() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Change Password");
        modalStage.setResizable(false);

        Label modalTitle = new Label("Change Password");
        modalTitle.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        modalTitle.setStyle("-fx-text-fill: " + WHITE + ";");

        Label modalSub = new Label("Ensure your account is using a secure password.");
        modalSub.setFont(Font.font(FONT, FontWeight.NORMAL, 11));
        modalSub.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox headerBox = new VBox(2, modalTitle, modalSub);
        headerBox.setPadding(new Insets(16, 16, 10, 16));
        headerBox.setStyle("-fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-width: 0 0 1 0;");

        PasswordField currentPassField = new PasswordField();
        currentPassField.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
        Label cpLbl = new Label("Current Password");
        cpLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        cpLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        VBox cpGroup = new VBox(4, cpLbl, currentPassField);

        PasswordField newPassField = new PasswordField();
        newPassField.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
        Label npLbl = new Label("New Password");
        npLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        npLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        VBox npGroup = new VBox(4, npLbl, newPassField);

        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
        Label confLbl = new Label("Confirm New Password");
        confLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        confLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        VBox confGroup = new VBox(4, confLbl, confirmPassField);

        VBox formContent = new VBox(14, cpGroup, npGroup, confGroup);
        formContent.setPadding(new Insets(16));

        Button saveBtn = new Button("Update Password");
        saveBtn.setStyle("-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16;");
        applyHoverAnimation(saveBtn, 1.04, -1);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-text-fill: " + WHITE + "; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16;");
        applyHoverAnimation(cancelBtn, 1.04, -1);
        cancelBtn.setOnAction(e -> modalStage.close());

        saveBtn.setOnAction(e -> {
            String cp = currentPassField.getText();
            String np = newPassField.getText();
            String conf = confirmPassField.getText();

            if (cp.isEmpty() || np.isEmpty() || conf.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all password fields.");
                alert.showAndWait();
                return;
            }

            if (!np.equals(conf)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("New password and confirm password do not match.");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Password updated successfully.");
            alert.showAndWait();
            modalStage.close();
        });

        Region modalSpacer = new Region();
        HBox.setHgrow(modalSpacer, Priority.ALWAYS);

        HBox modalFooter = new HBox(10, modalSpacer, cancelBtn, saveBtn);
        modalFooter.setAlignment(Pos.CENTER_RIGHT);
        modalFooter.setPadding(new Insets(10, 16, 16, 16));
        modalFooter.setStyle("-fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-width: 1 0 0 0;");

        VBox modalRoot = new VBox(headerBox, formContent, modalFooter);
        modalRoot.setStyle("-fx-background-color: #0D1626; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1;");

        Scene modalScene = new Scene(modalRoot, 420, 390);
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

    private VBox createAdminDetailsCard() {
        Label title = new Label("Administrator Details");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label subtitle = new Label("Your administrator account information.");
        subtitle.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        subtitle.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox heading = new VBox(2, title, subtitle);

        fullNameField = new TextField("Admin User");
        fullNameField.setEditable(true);
        fullNameField.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
        fullNameField.textProperty().addListener((obs, o, n) -> {
            heroNameLabel.setText(n.isEmpty() ? "Admin User" : n);
            updateAdminSymbol(n);
        });
        Label fnLbl = new Label("Full Name");
        fnLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        fnLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        VBox fullNameGroup = new VBox(4, fnLbl, fullNameField);

        emailField = new TextField("admin@onespace.com");
        emailField.setEditable(true);
        emailField.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
        emailField.textProperty().addListener((obs, o, n) -> heroEmailLabel.setText(n.isEmpty() ? "admin@onespace.com" : n));
        Label emLbl = new Label("Email Address");
        emLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        emLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        VBox emailGroup = new VBox(4, emLbl, emailField);

        usernameField = new TextField("@admin");
        usernameField.setEditable(true);
        usernameField.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
        usernameField.textProperty().addListener((obs, o, n) -> heroHandleLabel.setText(n.isEmpty() ? "@admin" : n));
        Label unLbl = new Label("Username");
        unLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        unLbl.setStyle("-fx-text-fill: " + WHITE + ";");
        VBox usernameGroup = new VBox(4, unLbl, usernameField);

        bioArea = new TextArea("System administrator with full access to OneSpace platform and all resources.");
        bioArea.setEditable(true);
        bioArea.setVisible(false);
        bioArea.setManaged(false);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15); formGrid.setVgap(12);
        ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(50);
        formGrid.getColumnConstraints().addAll(c1, c2);

        formGrid.add(fullNameGroup, 0, 0);
        formGrid.add(emailGroup, 1, 0);
        formGrid.add(usernameGroup, 0, 1);
        
        VBox card = new VBox(18, heading, formGrid);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");
        applyHoverAnimation(card, 1.01, -2);
        return card;
    }

    private VBox createAdminActionsCard() {
        Label title = new Label("Administrator Actions");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label subtitle = new Label("Critical actions that require administrator privileges.");
        subtitle.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        subtitle.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox heading = new VBox(2, title, subtitle);

        Label warningTitle = new Label("Delete Administrator Account");
        warningTitle.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        warningTitle.setStyle("-fx-text-fill: #FFFFFF;");

        Label warningDesc = new Label("Permanently remove your administrator account. This action cannot be undone.");
        warningDesc.setFont(Font.font(FONT, FontWeight.NORMAL, 11));
        warningDesc.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
        warningDesc.setWrapText(true);

        VBox textCol = new VBox(4, warningTitle, warningDesc);
        VBox.setVgrow(warningDesc, Priority.ALWAYS);

        Button deleteBtn = new Button("Delete Account");
        deleteBtn.setStyle("-fx-background-color: " + DANGER_BTN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        applyHoverAnimation(deleteBtn, 1.04, -2);
        deleteBtn.setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Delete Administrator Account");
            confirmAlert.setContentText("Are you sure you want to permanently delete your administrator account? This action cannot be undone.");
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    LandingPage.showAdminLoginPage();
                }
            });
        });

        SVGPath alertIcon = createIcon("alert");
        alertIcon.setStroke(Color.web("#EF4444"));
        alertIcon.setStrokeWidth(2);

        Region deleteSpacer = new Region();
        HBox.setHgrow(deleteSpacer, Priority.ALWAYS);
        HBox innerAlertBox = new HBox(12, alertIcon, textCol, deleteSpacer, deleteBtn);
        innerAlertBox.setAlignment(Pos.CENTER_LEFT);
        innerAlertBox.setPadding(new Insets(16));
        innerAlertBox.setStyle("-fx-background-color: rgba(10, 18, 33, 0.85); -fx-border-color: " + DANGER_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");

        VBox card = new VBox(18, heading, innerAlertBox);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");
        applyHoverAnimation(card, 1.01, -2);
        return card;
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);
        switch (type) {
            case "dashboard": icon.setContent("M3 3 H10 V10 H3 Z M14 3 H21 V10 H14 Z M3 14 H10 V21 H3 Z M14 14 H21 V21 H14 Z"); break;
            case "users": icon.setContent("M8 11 A3 3 0 1 0 8 5 A3 3 0 0 0 8 11 Z M16 11 A3 3 0 1 0 16 5 A3 3 0 0 0 16 11 Z M2 20 C2 16 5 14 8 14 C11 14 14 16 14 20 M12 15 C14 14 17 14 19 15 C21 16 22 18 22 20"); break;
            case "files": icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13"); break;
            case "collaboration": icon.setContent("M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2 M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M23 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75"); break;
            case "ai": icon.setContent("M12 2 L13.5 8.5 L20 7 L15.5 11.5 L21 15 L14 14.5 L12 22 L10 14.5 L3 15 L8.5 11.5 L4 7 L10.5 8.5 Z"); break;
            case "analytics": icon.setContent("M4 20 V11 M10 20 V6 M16 20 V13 M22 20 V3"); break;
            case "security": icon.setContent("M12 2 L20 5 V11 C20 16 17 20 12 22 C7 20 4 16 4 11 V5 Z M9 12 L11 14 L15 9"); break;
            case "settings": icon.setContent("M12 3 V6 M12 18 V21 M3 12 H6 M18 12 H21 M5.6 5.6 L7.7 7.7 M16.3 16.3 L18.4 18.4 M18.4 5.6 L16.3 7.7 M7.7 16.3 L5.6 18.4 M12 8 A4 4 0 1 0 12 16 A4 4 0 0 0 12 8"); break;
            case "logout": icon.setContent("M10 4 H5 V20 H10 M14 8 L19 12 L14 16 M19 12 H8"); break;
            case "search": icon.setContent("M10 3 A7 7 0 1 0 10 17 A7 7 0 0 0 10 3 Z M15 15 L21 21"); break;
            case "bell": icon.setContent("M6 17 H18 M8 17 V10 A4 4 0 0 1 16 10 V17 M10 20 H14"); break;
            case "camera": icon.setContent("M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z M12 13a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"); break;
            case "alert": icon.setContent("M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z M12 9v4 M12 17h.01"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }
}