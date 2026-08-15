package com.file_handlers.view;

import com.file_handlers.view.userView.CollaborationPage;
import com.file_handlers.view.userView.RecentPage;
import com.file_handlers.view.userView.UserDashboard;
import com.file_handlers.view.userView.UserLoginPage;
import com.file_handlers.view.userView.UserSpaces;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class LandingPage extends Application {

    // Slate Blue Theme Constants
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

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("OneSpace");
        primaryStage.setScene(getLandingPageScene());
        primaryStage.show();
    }

    // Static scene switcher and navigation methods
    public static void setScene(Scene scene) {
        if (primaryStage != null) {
            primaryStage.setScene(scene);
        }
    }

    public static void showLandingPage() { setScene(new LandingPage().getLandingPageScene()); }

    public static void showUserLoginPage() { setScene(new UserLoginPage().getUserLoginPageScene()); }

    public static void showUserDashboard() { setScene(new UserDashboard().getDashboardScene()); }

    public static void showUserSpace() { setScene(new UserSpaces().getUserSpacesScene()); }

    public static void showCollaborationPage() {setScene(new CollaborationPage().getCollaborationPageScene());}

    public static void showRecentPage() {setScene(new RecentPage().getRecentPageScene());}

    // LandingPage scene builder
    public Scene getLandingPageScene() {
        // App header bar
        Label logoIcon = new Label("⬡");
        logoIcon.setFont(Font.font(FONT, FontWeight.BOLD, 20));
        logoIcon.setTextFill(Color.web("#60A5FA"));

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        logoText.setTextFill(Color.web(TEXT_LIGHT));

        HBox appHeader = new HBox(8, logoIcon, logoText);
        appHeader.setAlignment(Pos.CENTER_LEFT);
        appHeader.setPadding(new Insets(16, 24, 16, 24));

        // Center branding & title section
        Circle outerRing = new Circle(28, Color.web(PRIMARY_LIGHT_BLUE));
        Circle innerCircle = new Circle(20, Color.web(PRIMARY_BLUE));
        Label ringSymbol = new Label("◎");
        ringSymbol.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        ringSymbol.setTextFill(Color.WHITE);
        StackPane centerIconPane = new StackPane(outerRing, innerCircle, ringSymbol);

        Label welcomeTitle = new Label("Welcome to OneSpace");
        welcomeTitle.setFont(Font.font(FONT, FontWeight.BOLD, 28));
        welcomeTitle.setTextFill(Color.web(TEXT_LIGHT));

        Label welcomeSubtitle = new Label("Choose how you want to continue");
        welcomeSubtitle.setFont(Font.font(FONT, 14));
        welcomeSubtitle.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        VBox titleBox = new VBox(8, centerIconPane, welcomeTitle, welcomeSubtitle);
        titleBox.setAlignment(Pos.CENTER);

        // Role selection cards container
        VBox userCard = createRoleCard("👤", PRIMARY_LIGHT_BLUE, PRIMARY_BLUE, "User Login",
                "Access your personal space,\nmanage your files and more.",
                "Continue as User  →", PRIMARY_BLUE, e -> { LandingPage.showUserLoginPage(); });

        VBox adminCard = createRoleCard("🛡", "#BAE6FD", "#0284C7", "Admin Login",
                "Manage users, oversee system\nactivities and configurations.",
                "Continue as Admin  →", "#0284C7", e -> { LandingPage.showUserLoginPage(); });

        HBox cardsContainer = new HBox(28, userCard, adminCard);
        cardsContainer.setAlignment(Pos.CENTER);

        // Security footer
        Label footerIcon = new Label("🛡");
        footerIcon.setFont(Font.font(14));
        footerIcon.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        Label footerText = new Label("Secure. Organized. Intelligent.");
        footerText.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        footerText.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        HBox footerRow1 = new HBox(6, footerIcon, footerText);
        footerRow1.setAlignment(Pos.CENTER);

        Label brandText = new Label("OneSpace");
        brandText.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        brandText.setTextFill(Color.web("#60A5FA"));

        VBox footerBox = new VBox(4, footerRow1, brandText);
        footerBox.setAlignment(Pos.CENTER);

        // Main layout assembly with vertical spacers
        Region topSpacer = new Region();
        Region bottomSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        VBox centerBody = new VBox(32, topSpacer, titleBox, cardsContainer, bottomSpacer, footerBox);
        centerBody.setAlignment(Pos.CENTER);
        centerBody.setPadding(new Insets(0, 24, 24, 24));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_APP + ";");
        root.setTop(appHeader);
        root.setCenter(centerBody);

        return new Scene(root, 1200, 750);
    }

    // Role card component builder
    private VBox createRoleCard(String iconSymbol, String iconBg, String iconColor,
                                String title, String description,
                                String buttonText, String buttonColor,
                                javafx.event.EventHandler<javafx.event.ActionEvent> onAction) {
        Label icon = new Label(iconSymbol);
        icon.setFont(Font.font(20));
        icon.setTextFill(Color.web(iconColor));
        icon.setPrefSize(48, 48);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 50%;");

        Label cardTitle = new Label(title);
        cardTitle.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        cardTitle.setTextFill(Color.web(TEXT_DARK));

        Label cardDesc = new Label(description);
        cardDesc.setFont(Font.font(FONT, 13));
        cardDesc.setTextFill(Color.web(TEXT_MUTED_DARK));
        cardDesc.setTextAlignment(TextAlignment.CENTER);
        cardDesc.setWrapText(true);

        Button actionBtn = new Button(buttonText);
        actionBtn.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        actionBtn.setTextFill(Color.WHITE);
        actionBtn.setMaxWidth(Double.MAX_VALUE);
        actionBtn.setPrefHeight(42);
        actionBtn.setStyle("-fx-background-color: " + buttonColor + "; -fx-background-radius: 10; -fx-cursor: hand;"
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 8, 0, 0, 2);");
        actionBtn.setOnAction(onAction);

        VBox card = new VBox(16, icon, cardTitle, cardDesc, actionBtn);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(32, 28, 32, 28));
        card.setPrefWidth(300);
        card.setMaxWidth(300);
        card.setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_COLOR
                + "; -fx-border-radius: 18; -fx-background-radius: 18;"
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 16, 0, 0, 6);");

        return card;
    }
}