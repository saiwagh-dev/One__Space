package com.file_handlers.view;

import com.file_handlers.view.adminView.*;
import com.file_handlers.view.userView.AddReminderPage;
import com.file_handlers.view.userView.AiAssistantPage;
import com.file_handlers.view.userView.CollaborationPage;
import com.file_handlers.view.userView.NotificationPage;
import com.file_handlers.view.userView.RecentPage;
import com.file_handlers.view.userView.StorageIndexed;
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

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LandingPage extends Application {

    // Theme Constants
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
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

    // Application Setup
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("OneSpace");
        primaryStage.setScene(getLandingPageScene());
        primaryStage.show();
    }

    // Navigation
    public static void setScene(Scene scene) {
        if (primaryStage != null) primaryStage.setScene(scene);
    }

    public static void showLandingPage() {
        if (primaryStage != null) primaryStage.setScene(new LandingPage().getLandingPageScene());
    }

    // User Pages
    public static void showUserLoginPage() { setScene(new UserLoginPage().getUserLoginPageScene()); }
    public static void showUserSignupPage() { setScene(new UserSignupPage().getUserSignupPageScene()); }
    public static void showUserDashboard() { setScene(new UserDashboard().getDashboardScene()); }
    public static void showUserSpace() { setScene(new UserSpaces().getUserSpacesScene()); }
    public static void showUserSearch() { setScene(new UserSearch().getUserSearchScene()); }
    public static void showCalendarPage() { setScene(new UserCalendar().getCalendarPageScene()); }
    public static void showTrashPage() { setScene(new UserTrash().getTrashPageScene()); }
    public static void showAddReminderPage() { setScene(new AddReminderPage().getAddReminderPageScene()); }
    public static void showNotificationPage() { setScene(new NotificationPage().getNotificationsScene()); }
    public static void showCollaborationPage() { setScene(new CollaborationPage().getCollaborationPageScene()); }
    public static void showRecentPage() { setScene(new RecentPage().getRecentPageScene()); }
    public static void showSettingPage() { setScene(new UserSettingPage().getSettingPageScene()); }
    public static void showAiAssistantPage() { setScene(new AiAssistantPage().getAiAssistantPageScene()); }
    public static void showUserProfilePage() {setScene(new UserProfilePage().getUserProfilePageScene());}

    public static void showStorageIndexedPage() {setScene(new StorageIndexed().getStorageIndexedScene());}

    public static void showUnifiedSpaceView() {
        try {
            UnifiedSpaceView unifiedSpaceView = new UnifiedSpaceView();
            primaryStage.setScene(unifiedSpaceView.getUnifiedSpaceScene());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Admin Pages
    public static void showAdminLoginPage() { setScene(new AdminLoginPage().getAdminLoginPageScene()); }
    public static void showAdminDashboard() { setScene(new AdminDashboard().getAdminDashboardScene()); }
    public static void showAdminUsers() { setScene(new AdminUsers().getAdminUsersScene()); }
    public static void showAdminFiles() { setScene(new AdminFiles().getAdminFilesScene()); }
    public static void showAnalytics() { setScene(new AdminAnalytics().getAnalyticsScene()); }
    public static void showAdminSettings() { setScene(new AdminSettings().getAdminSettingsScene()); }
    public static void showAdminSignUp() { setScene(new AdminSignUpPage().getAdminSignUpScene()); }
    public static void showAdminAISystem() { setScene(new AdminAISystem().getAdminAIScene()); }
    public static void showAdminSecurity() { setScene(new AdminSecurity().getSecurityScene()); }
    public static void showAdminCollaboration() { setScene(new AdminCollaboration().getCollaborationScene()); }
    public static void showAdminProfilePage() { setScene(new AdminProfilePage().getAdminProfileScene());}

    // Landing Page
    public Scene getLandingPageScene() {
        StackPane centerIconPane = createOneSpaceLogo(200);

        Label title = label("Welcome to OneSpace", 28, FontWeight.BOLD, TEXT_LIGHT);
        Label sub = label("Choose how you want to continue", 14, FontWeight.NORMAL, TEXT_MUTED_LIGHT);

        VBox titleBox = new VBox(10, centerIconPane, title, sub);
        titleBox.setAlignment(Pos.CENTER);

        VBox userCard = createRoleCard(
                "👤", PRIMARY_LIGHT_BLUE, PRIMARY_BLUE,
                "User Login",
                "Access your personal space,\nmanage your files and more.",
                "Continue as User  →", PRIMARY_BLUE,
                e -> showUserLoginPage()
        );

        VBox adminCard = createRoleCard(
                "🛡", "#BAE6FD", PRIMARY_BLUE,
                "Admin Login",
                "Manage users, oversee system\nactivities and configurations.",
                "Continue as Admin  →", "#0284C7",
                e -> showAdminLoginPage()
        );

        HBox cardsContainer = new HBox(28, userCard, adminCard);
        cardsContainer.setAlignment(Pos.CENTER);

        Label footerIcon = new Label("🛡");
        footerIcon.setFont(Font.font(14));
        footerIcon.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        Label footerText = new Label("Secure. Organized. Intelligent.");
        footerText.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        footerText.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        HBox footerRow = new HBox(6, footerIcon, footerText);
        footerRow.setAlignment(Pos.CENTER);

        Label brand = label("OneSpace", 12, FontWeight.BOLD, TEXT_LIGHT);

        VBox footerBox = new VBox(4, footerRow, brand);
        footerBox.setAlignment(Pos.CENTER);

        Region topSpacer = new Region();
        Region bottomSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        VBox centerBody = new VBox(32, topSpacer, titleBox, cardsContainer, bottomSpacer, footerBox);
        centerBody.setAlignment(Pos.CENTER);
        centerBody.setPadding(new Insets(24));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_APP + ";");
        root.setCenter(centerBody);

        playLandingAnimation(centerIconPane, title, sub, cardsContainer, footerBox);

        return new Scene(root, 1200, 750);
    }

    // Landing Animation
    private void playLandingAnimation(StackPane logo, Label title, Label subtitle, HBox cards, VBox footer) {
        logo.setOpacity(0);
        logo.setScaleX(0.85);
        logo.setScaleY(0.85);

        title.setOpacity(0);
        title.setTranslateY(12);

        subtitle.setOpacity(0);
        subtitle.setTranslateY(10);

        cards.setOpacity(0);
        cards.setTranslateY(35);

        footer.setOpacity(0);

        FadeTransition logoFade = new FadeTransition(Duration.millis(450), logo);
        logoFade.setToValue(1);

        ScaleTransition logoScale = new ScaleTransition(Duration.millis(550), logo);
        logoScale.setToX(1);
        logoScale.setToY(1);

        ParallelTransition logoAnim = new ParallelTransition(logoFade, logoScale);

        FadeTransition titleFade = new FadeTransition(Duration.millis(350), title);
        titleFade.setToValue(1);

        TranslateTransition titleMove = new TranslateTransition(Duration.millis(350), title);
        titleMove.setToY(0);

        ParallelTransition titleAnim = new ParallelTransition(titleFade, titleMove);

        FadeTransition subtitleFade = new FadeTransition(Duration.millis(300), subtitle);
        subtitleFade.setToValue(1);

        TranslateTransition subtitleMove = new TranslateTransition(Duration.millis(300), subtitle);
        subtitleMove.setToY(0);

        ParallelTransition subtitleAnim = new ParallelTransition(subtitleFade, subtitleMove);

        FadeTransition cardsFade = new FadeTransition(Duration.millis(500), cards);
        cardsFade.setToValue(1);

        TranslateTransition cardsMove = new TranslateTransition(Duration.millis(500), cards);
        cardsMove.setToY(0);

        ParallelTransition cardsAnim = new ParallelTransition(cardsFade, cardsMove);

        FadeTransition footerFade = new FadeTransition(Duration.millis(300), footer);
        footerFade.setToValue(1);

        new SequentialTransition(
                logoAnim, titleAnim, subtitleAnim, cardsAnim, footerFade
        ).play();
    }

    // UI Helpers
    private StackPane createOneSpaceLogo(double size) {
        Image logoImage = new Image(
                getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png")
        );

        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(size);
        logoView.setFitHeight(size);
        logoView.setPreserveRatio(true);

        StackPane logoPane = new StackPane(logoView);
        logoPane.setPrefSize(size, size);
        logoPane.setAlignment(Pos.CENTER);

        return logoPane;
    }

    private VBox createRoleCard(
            String iconSymbol,
            String iconBg,
            String iconColor,
            String title,
            String description,
            String buttonText,
            String buttonColor,
            javafx.event.EventHandler<javafx.event.ActionEvent> onAction) {

        Label icon = new Label(iconSymbol);
        icon.setFont(Font.font(20));
        icon.setTextFill(Color.web(iconColor));
        icon.setPrefSize(48, 48);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 50%;");

        Label cardTitle = label(title, 18, FontWeight.BOLD, TEXT_DARK);

        Label desc = label(description, 13, FontWeight.NORMAL, TEXT_MUTED_DARK);
        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setWrapText(true);

        Button actionBtn = new Button(buttonText);
        actionBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        actionBtn.setTextFill(Color.WHITE);
        actionBtn.setMaxWidth(Double.MAX_VALUE);
        actionBtn.setPrefHeight(42);
        actionBtn.setStyle(
                "-fx-background-color: " + buttonColor + ";" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 8, 0, 0, 2);"
        );
        actionBtn.setOnAction(onAction);

        VBox card = new VBox(16, icon, cardTitle, desc, actionBtn);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(32, 28, 32, 28));
        card.setPrefWidth(300);
        card.setMaxWidth(300);
        card.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_COLOR + ";" +
                "-fx-border-radius: 18;" +
                "-fx-background-radius: 18;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 16, 0, 0, 6);"
        );

        return card;
    }

    private static Label label(String text, double size, FontWeight weight, String color) {
        Label l = new Label(text);
        l.setFont(Font.font(FONT, weight, size));
        l.setTextFill(Color.web(color));
        return l;
    }

    public static Object showFolderSelection() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showFolderSelection'");
    }
}