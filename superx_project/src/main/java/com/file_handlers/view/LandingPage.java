package com.file_handlers.view;

import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.view.adminView.*;
import com.file_handlers.view.userView.AddReminderPage;
import com.file_handlers.view.userView.AiAssistantPage;
import com.file_handlers.view.userView.CollaborationPage;
import com.file_handlers.view.userView.NotificationPage;
import com.file_handlers.view.userView.RecentPage;
import com.file_handlers.view.userView.StorageIndexPage;
import com.file_handlers.view.userView.UserCalendar;
import com.file_handlers.view.userView.UserDashboard;
import com.file_handlers.view.userView.UserLoginPage;
import com.file_handlers.view.userView.UserProfilePage;
import com.file_handlers.view.userView.UserSearch;
import com.file_handlers.view.userView.UserSettingPage;
import com.file_handlers.view.userView.UserSignupPage;
import com.file_handlers.view.userView.UserSpaces;
import com.file_handlers.view.userView.UserTrash;
import com.file_handlers.view.userView.StorageIndexPage;

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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LandingPage extends Application {

    private static final String FONT =
            "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_APP = "#3A4D67";
    private static final String BG_CARD = "#DDE8F5";
    private static final String BORDER_COLOR = "#C9DAEE";
    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String PRIMARY_LIGHT_BLUE = "#BFDBFE";
    private static final String TEXT_DARK = "#142338";
    private static final String TEXT_MUTED_DARK = "#506580";
    private static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#9EB0C6";

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("OneSpace");
        primaryStage.setScene(getLandingPageScene());
        primaryStage.setMaximized(true);
        primaryStage.show();
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
    setScene( new StorageIndexPage().getStorageIndexPageScene() );
    }

    // ================= DYNAMIC SPACE =================

    public static void showUnifiedSpace(String spaceId, String spaceName) {
        try {
            UnifiedSpaceView view =
                    new UnifiedSpaceView();
            setScene(view.getUnifiedSpaceScene());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                TEXT_LIGHT
        );

        Label sub = label(
                "Choose how you want to continue",
                14,
                FontWeight.NORMAL,
                TEXT_MUTED_LIGHT
        );

        VBox titleBox = new VBox(ResponsiveUtil.COMPACT ? 8 : 10, logo, title, sub);
        titleBox.setAlignment(Pos.CENTER);

        VBox userCard = createRoleCard(
                "👤",
                PRIMARY_LIGHT_BLUE,
                PRIMARY_BLUE,
                "User Login",
                "Access your personal space,\nmanage your files and more.",
                "Continue as User  →",
                PRIMARY_BLUE,
                e -> showUserLoginPage()
        );

        VBox adminCard = createRoleCard(
                "🛡",
                "#BAE6FD",
                PRIMARY_BLUE,
                "Admin Login",
                "Manage users, oversee system\nactivities and configurations.",
                "Continue as Admin  →",
                "#0284C7",
                e -> showAdminLoginPage()
        );

        HBox cards = new HBox(ResponsiveUtil.COMPACT ? 20 : 28, userCard, adminCard);
        cards.setAlignment(Pos.CENTER);

        Label footerIcon = new Label("🛡");
        footerIcon.setFont(Font.font(14));
        footerIcon.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        Label footerText = label(
                "Secure. Organized. Intelligent.",
                12,
                FontWeight.SEMI_BOLD,
                TEXT_MUTED_LIGHT
        );

        HBox footerRow = new HBox(6, footerIcon, footerText);
        footerRow.setAlignment(Pos.CENTER);

        VBox footer = new VBox(
                4,
                footerRow,
                label("OneSpace", 12, FontWeight.BOLD, TEXT_LIGHT)
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
        root.setStyle("-fx-background-color:" + BG_APP + ";");
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
            String iconSymbol,
            String iconBg,
            String iconColor,
            String title,
            String description,
            String buttonText,
            String buttonColor,
            javafx.event.EventHandler<javafx.event.ActionEvent> action) {

        Label icon = new Label(iconSymbol);
        icon.setFont(Font.font(20));
        icon.setTextFill(Color.web(iconColor));
        icon.setPrefSize(48, 48);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle(
                "-fx-background-color:" + iconBg +
                ";-fx-background-radius:50%;"
        );

        Label cardTitle =
                label(title, 18, FontWeight.BOLD, TEXT_DARK);

        Label desc =
                label(description, 13, FontWeight.NORMAL, TEXT_MUTED_DARK);

        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setWrapText(true);

        Button button = new Button(buttonText);
        button.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        button.setTextFill(Color.WHITE);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.setStyle(
                "-fx-background-color:" + buttonColor +
                ";-fx-background-radius:10;" +
                "-fx-cursor:hand;"
        );
        button.setOnAction(action);

        VBox card = new VBox(
                16,
                icon,
                cardTitle,
                desc,
                button
        );

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(ResponsiveUtil.COMPACT ? 24 : 32, 28, ResponsiveUtil.COMPACT ? 24 : 32, 28));
        card.setPrefWidth(ResponsiveUtil.COMPACT ? 280 : 300);
        card.setMaxWidth(ResponsiveUtil.COMPACT ? 280 : 300);
        card.setStyle(
                "-fx-background-color:" + BG_CARD +
                ";-fx-border-color:" + BORDER_COLOR +
                ";-fx-border-radius:18;" +
                ";-fx-background-radius:18;"
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
        label.setTextFill(Color.web(color));
        return label;
    }

    
}