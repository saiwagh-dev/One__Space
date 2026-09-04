package com.file_handlers.view;

import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.view.adminView.*;
import com.file_handlers.view.userView.AddReminderPage;
import com.file_handlers.view.userView.AiAssistantPage;
import com.file_handlers.view.userView.CollaborationPage;
import com.file_handlers.view.userView.NotificationPage;
import com.file_handlers.view.userView.RecentPage;
import com.file_handlers.view.userView.StorageIndexPage;
import com.file_handlers.view.userView.UnifiedSpaceView;
import com.file_handlers.view.userView.UserCalendar;
import com.file_handlers.view.userView.UserDashboard;
import com.file_handlers.view.userView.UserLoginPage;
import com.file_handlers.view.userView.UserProfilePage;
import com.file_handlers.view.userView.UserSearch;
import com.file_handlers.view.userView.UserSettingPage;
import com.file_handlers.view.userView.UserSignupPage;
import com.file_handlers.view.userView.UserSpaces;
import com.file_handlers.view.userView.UserTrash;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LandingPage extends Application {

    // Typography
    private static final String FONT =
            "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar Tones
    private static final String SIDEBAR_BG = "#070C16";

    // 2. Center Workspace Canvas: Atmospheric Dark Radial Glow
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards & Container Colors
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";

    // 4. Vibrant Typography & Accent Highlights
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";

    private static Stage primaryStage;

    @Override
    public void start(Stage stage){
        primaryStage=stage;
        primaryStage.setTitle("OneSpace");
        primaryStage.setMaximized(true);

        SplashScreen splash=new SplashScreen(
                primaryStage,
                LandingPage::showLandingPage
        );

        primaryStage.setScene(splash.getSplashScene());
        primaryStage.show();

        splash.play();
    }

    // Dynamic sizing helpers to prevent shrinking/flickering on scene changes
    public static double getCurrentWidth() {
        if (primaryStage != null && primaryStage.getScene() != null && primaryStage.getScene().getWidth() > 0) {
            return primaryStage.getScene().getWidth();
        }
        return ResponsiveUtil.COMPACT ? 1000 : 1200;
    }

    public static double getCurrentHeight() {
        if (primaryStage != null && primaryStage.getScene() != null && primaryStage.getScene().getHeight() > 0) {
            return primaryStage.getScene().getHeight();
        }
        return ResponsiveUtil.COMPACT ? 650 : 750;
    }

    public static void setScene(Scene scene) {
        if (primaryStage != null) {
            primaryStage.setScene(scene);
            javafx.application.Platform.runLater(() -> primaryStage.setMaximized(true));
        }
    }

    public static void showLandingPage() {
        setScene(new LandingPage().getLandingPageScene());
    }

    // ================= USER PAGES =================

    public static void showUserLoginPage() {
        setScene(new UserLoginPage().getUserLoginPageScene());
    }

    public static void showUserSignupPage() {
        setScene(new UserSignupPage().getUserSignupPageScene());
    }

    public static void showUserDashboard() {
        setScene(new UserDashboard().getDashboardScene());
    }

    public static void showUserSpace() {
        setScene(new UserSpaces().getUserSpacesScene());
    }

    public static void showUserSearch() {
        setScene(new UserSearch().getUserSearchScene());
    }

    public static void showCalendarPage() {
        setScene(new UserCalendar().getCalendarPageScene());
    }

    public static void showTrashPage() {
        setScene(new UserTrash().getTrashPageScene());
    }

    public static void showAddReminderPage() {
        setScene(new AddReminderPage().getAddReminderPageScene());
    }

    public static void showNotificationPage() {
        setScene(new NotificationPage().getNotificationsScene());
    }

    public static void showCollaborationPage() {
        setScene(new CollaborationPage().getCollaborationPageScene());
    }

    public static void showRecentPage() {
        setScene(new RecentPage().getRecentPageScene());
    }

    public static void showSettingPage() {
        setScene(new UserSettingPage().getSettingPageScene());
    }

    public static void showAiAssistantPage() {
        setScene(new AiAssistantPage().getAiAssistantPageScene());
    }

    public static void showUserProfilePage() {
        setScene(new UserProfilePage().getUserProfilePageScene());
    }

    public static void showStorageIndexPage() {
        setScene(new StorageIndexPage().getStorageIndexPageScene());
    }

    // ================= DYNAMIC SPACE =================

    public static void showUnifiedSpace(String spaceId, String spaceName) {
        System.out.println(
                "[NAVIGATION] Opening Space: "
                        + spaceName
                        + " | ID: "
                        + spaceId
        );

        UnifiedSpaceView view =
                new UnifiedSpaceView(spaceId, spaceName);

        setScene(view.getUnifiedSpaceScene());
    }
    public static void showUnifiedSpaceView() {
        showUnifiedSpace("all", "All Spaces");
    }

    // ================= ADMIN PAGES =================

    public static void showAdminLoginPage() {
        setScene(new AdminLoginPage().getAdminLoginPageScene());
    }

    public static void showAdminDashboard() {
        setScene(new AdminDashboard().getAdminDashboardScene());
    }

    public static void showAdminNotificationPage() {
        setScene(new AdminNotificationPage().getAdminNotificationPageScene());
    }

    public static void showAdminUsers() {
        setScene(new AdminUsers().getAdminUsersScene());
    }

    public static void showAdminFiles() {
        setScene(new AdminFiles().getAdminFilesScene());
    }

    public static void showAnalytics() {
        setScene(new AdminAnalytics().getAnalyticsScene());
    }

    public static void showAdminSettings() {
        setScene(new AdminSettings().getAdminSettingsScene());
    }

    public static void showAdminSignUp() {
        setScene(new AdminSignUpPage().getAdminSignUpScene());
    }

    public static void showAdminAISystem() {
        setScene(new AdminAISystem().getAdminAIScene());
    }

    public static void showAdminSecurity() {
        setScene(new AdminSecurity().getSecurityScene());
    }

    public static void showAdminCollaboration() {
        setScene(new AdminCollaboration().getCollaborationScene());
    }

    public static void showAdminProfilePage() {
        setScene(new AdminProfilePage().getAdminProfileScene());
    }

    

    // ================= LANDING PAGE =================

    public Scene getLandingPageScene() {
        StackPane logo = createOneSpaceLogo(ResponsiveUtil.COMPACT ? 150 : 200);

        Label title = label(
                "Welcome to OneSpace",
                ResponsiveUtil.COMPACT ? 24 : 28,
                FontWeight.BOLD,
                WHITE
        );

        Label sub = label(
                "Choose how you want to continue",
                14,
                FontWeight.MEDIUM,
                LIGHT_SECONDARY
        );

        VBox titleBox = new VBox(ResponsiveUtil.COMPACT ? 8 : 10, logo, title, sub);
        titleBox.setAlignment(Pos.CENTER);

        VBox userCard = createRoleCard(
                "user",
                "rgba(37, 99, 235, 0.15)",
                "#38BDF8",
                "User Login",
                "Access your personal space,\nmanage your files and more.",
                "Continue as User   →",
                BLUE,
                e -> showUserLoginPage()
        );

        VBox adminCard = createRoleCard(
                "security",
                "rgba(0, 210, 255, 0.15)",
                "#00D2FF",
                "Admin Login",
                "Manage users, oversee system\nactivities and configurations.",
                "Continue as Admin   →",
                "#0284C7",
                e -> showAdminLoginPage()
        );

        HBox cards = new HBox(ResponsiveUtil.COMPACT ? 20 : 28, userCard, adminCard);
        cards.setAlignment(Pos.CENTER);

        SVGPath footerShield = createIcon("security");
        footerShield.setStroke(Color.web(LIGHT_SECONDARY));
        footerShield.setStrokeWidth(2);

        Label footerText = label(
                "Secure. Organized. Intelligent.",
                12,
                FontWeight.SEMI_BOLD,
                LIGHT_SECONDARY
        );

        HBox footerRow = new HBox(8, footerShield, footerText);
        footerRow.setAlignment(Pos.CENTER);

        VBox footer = new VBox(
                4,
                footerRow,
                label("OneSpace", 12, FontWeight.BOLD, WHITE)
        );
        footer.setAlignment(Pos.CENTER);

        Region top = new Region();
        Region bottom = new Region();
        VBox.setVgrow(top, Priority.ALWAYS);
        VBox.setVgrow(bottom, Priority.ALWAYS);

        VBox body = new VBox(
                ResponsiveUtil.COMPACT ? 20 : 32,
                top,
                titleBox,
                cards,
                bottom,
                footer
        );
        body.setAlignment(Pos.CENTER);
        body.setPadding(new Insets(ResponsiveUtil.COMPACT ? 16 : 24));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        root.setCenter(body);

        playLandingAnimation(logo, title, sub, cards, footer);

        return new Scene(root, getCurrentWidth(), getCurrentHeight());
    }

    private void playLandingAnimation(
            StackPane logo,
            Label title,
            Label subtitle,
            HBox cards,
            VBox footer) {

        logo.setOpacity(0);
        logo.setScaleX(.85);
        logo.setScaleY(.85);
        title.setOpacity(0);
        title.setTranslateY(12);
        subtitle.setOpacity(0);
        subtitle.setTranslateY(10);
        cards.setOpacity(0);
        cards.setTranslateY(35);
        footer.setOpacity(0);

        FadeTransition lf = new FadeTransition(Duration.millis(450), logo);
        lf.setToValue(1);

        ScaleTransition ls = new ScaleTransition(Duration.millis(550), logo);
        ls.setToX(1);
        ls.setToY(1);

        ParallelTransition logoAnim = new ParallelTransition(lf, ls);

        FadeTransition tf = new FadeTransition(Duration.millis(350), title);
        tf.setToValue(1);

        TranslateTransition tm = new TranslateTransition(Duration.millis(350), title);
        tm.setToY(0);

        ParallelTransition titleAnim = new ParallelTransition(tf, tm);

        FadeTransition sf = new FadeTransition(Duration.millis(300), subtitle);
        sf.setToValue(1);

        TranslateTransition sm =
                new TranslateTransition(Duration.millis(300), subtitle);
        sm.setToY(0);

        ParallelTransition subAnim = new ParallelTransition(sf, sm);

        FadeTransition cf = new FadeTransition(Duration.millis(500), cards);
        cf.setToValue(1);

        TranslateTransition cm =
                new TranslateTransition(Duration.millis(500), cards);
        cm.setToY(0);

        ParallelTransition cardsAnim = new ParallelTransition(cf, cm);

        FadeTransition ff = new FadeTransition(Duration.millis(300), footer);
        ff.setToValue(1);

        new SequentialTransition(
                logoAnim,
                titleAnim,
                subAnim,
                cardsAnim,
                ff
        ).play();
    }

    private StackPane createOneSpaceLogo(double size) {
        Image image = new Image(
                getClass().getResourceAsStream(
                        "/assets/logo/OneSpace_logo.png"
                )
        );

        ImageView view = new ImageView(image);
        view.setFitWidth(size);
        view.setFitHeight(size);
        view.setPreserveRatio(true);

        StackPane pane = new StackPane(view);
        pane.setPrefSize(size, size);
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

    private VBox createRoleCard(
            String iconType,
            String iconBg,
            String iconColor,
            String title,
            String description,
            String buttonText,
            String buttonColor,
            javafx.event.EventHandler<javafx.event.ActionEvent> action) {

        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setPrefSize(48, 48); iconPane.setMinSize(48, 48);

        Label cardTitle =
                label(title, 18, FontWeight.BOLD, WHITE);

        Label desc =
                label(description, 13, FontWeight.NORMAL, LIGHT_SECONDARY);

        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setWrapText(true);

        Button button = new Button(buttonText);
        button.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        button.setTextFill(Color.WHITE);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.45), 10, 0, 0, 2);"
        );
        button.setOnAction(action);

        VBox card = new VBox(
                16,
                iconPane,
                cardTitle,
                desc,
                button
        );

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(ResponsiveUtil.COMPACT ? 24 : 32, 28, ResponsiveUtil.COMPACT ? 24 : 32, 28));
        card.setPrefWidth(ResponsiveUtil.COMPACT ? 280 : 300);
        card.setMaxWidth(ResponsiveUtil.COMPACT ? 280 : 300);
        card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-width: 1.2;" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);"
        );

        return card;
    }

    private static Label label(
            String text,
            double size,
            FontWeight weight,
            String color) {

        Label label = new Label(text);
        label.setFont(Font.font(FONT, weight, size));
        label.setStyle("-fx-text-fill: " + color + ";");
        return label;
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);
        switch (type) {
            case "user": icon.setContent("M8 11 A3 3 0 1 0 8 5 A3 3 0 0 0 8 11 Z M2 20 C2 16 5 14 8 14 C11 14 14 16 14 20"); break;
            case "security": icon.setContent("M12 2 L20 5 V11 C20 16 17 20 12 22 C7 20 4 16 4 11 V5 Z M9 12 L11 14 L15 9"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }
}