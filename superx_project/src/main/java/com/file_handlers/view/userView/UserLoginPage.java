package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

public class UserLoginPage {

    // Slate Blue Theme Constants
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_APP = "#3A4D67";
    private static final String BG_CARD = "#DDE8F5";
    private static final String BG_INPUT = "#EDF3FA";
    private static final String BORDER_COLOR = "#C9DAEE";
    private static final String PRIMARY_BLUE = "#2563EB";
    public static final String PRIMARY_LIGHT_BLUE = "#BFDBFE";
    private static final String TEXT_DARK = "#142338";
    private static final String TEXT_MUTED_DARK = "#506580";
    public static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#9EB0C6";

    public Scene getUserLoginPageScene() {

        // App Header Bar
        Button backBtn = new Button("← Back to home");
        backBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        backBtn.setTextFill(Color.web(TEXT_MUTED_LIGHT));
        backBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        backBtn.setOnAction(e -> { LandingPage.setScene(new LandingPage().getLandingPageScene()); });

        HBox appHeader = new HBox(new Region(), backBtn);
        HBox.setHgrow(appHeader.getChildren().get(0), Priority.ALWAYS);
        appHeader.setAlignment(Pos.CENTER_LEFT);
        appHeader.setPadding(new Insets(16, 24, 16, 24));

        // Login Card Header (Increased logo size and reduced spacing using negative/tight VBox spacing)
        StackPane centerIconPane = createOneSpaceLogo(135);

        Label title = new Label("OneSpace");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        title.setTextFill(Color.web(TEXT_DARK));

        Label subtitle = new Label("Enter your credentials to access OneSpace");
        subtitle.setFont(Font.font(FONT, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED_DARK));

        VBox cardHeader = new VBox(-2, centerIconPane, title, subtitle);
        cardHeader.setAlignment(Pos.CENTER);

        // Form Fields
        Label emailLabel = new Label("Email Address");
        emailLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        emailLabel.setTextFill(Color.web(TEXT_DARK));

        TextField emailField = new TextField();
        emailField.setPromptText("name@example.com");
        emailField.setPrefHeight(42);
        emailField.setStyle(getFieldStyle());

        VBox emailBox = new VBox(6, emailLabel, emailField);

        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        passwordLabel.setTextFill(Color.web(TEXT_DARK));

        Label forgotPassword = new Label("Forgot?");
        forgotPassword.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        forgotPassword.setTextFill(Color.web(PRIMARY_BLUE));
        forgotPassword.setStyle("-fx-cursor: hand;");

        HBox passwordHeader = new HBox(passwordLabel, new Region(), forgotPassword);
        HBox.setHgrow(passwordHeader.getChildren().get(1), Priority.ALWAYS);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");
        passwordField.setPrefHeight(42);
        passwordField.setStyle(getFieldStyle());

        VBox passwordBox = new VBox(6, passwordHeader, passwordField);

        // Action Buttons
        Button loginButton = new Button("Sign In  →");
        loginButton.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        loginButton.setTextFill(Color.WHITE);
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(42);
        loginButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 10; -fx-cursor: hand;");
        loginButton.setOnAction(e -> { LandingPage.showUserDashboard();});

        // Footer Link
        Label noAccountText = new Label("Don't have an account?");
        noAccountText.setFont(Font.font(FONT, 12));
        noAccountText.setTextFill(Color.web(TEXT_MUTED_DARK));

        Label signUpLink = new Label("Sign Up");
        signUpLink.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        signUpLink.setTextFill(Color.web(PRIMARY_BLUE));
        signUpLink.setStyle("-fx-cursor: hand;");
        signUpLink.setOnMouseClicked(e -> { LandingPage.showUserSignupPage();});

        HBox signUpBox = new HBox(4, noAccountText, signUpLink);
        signUpBox.setAlignment(Pos.CENTER);

        // Card Assembly
        VBox card = new VBox(16, cardHeader, emailBox, passwordBox, loginButton, signUpBox);
        card.setPadding(new Insets(24, 28, 24, 28));
        card.setPrefWidth(360);
        card.setMaxWidth(360);
        card.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_COLOR + ";" +
                "-fx-border-radius: 18;" +
                "-fx-background-radius: 18;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 16, 0, 0, 6);"
        );

        // Main Layout Assembly
        Region topSpacer = new Region();
        Region bottomSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        VBox centerBody = new VBox(topSpacer, card, bottomSpacer);
        centerBody.setAlignment(Pos.CENTER);
        centerBody.setPadding(new Insets(0, 24, 24, 24));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_APP + ";");
        root.setTop(appHeader);
        root.setCenter(centerBody);

        return new Scene(root, 1200, 750);
    }

    private String getFieldStyle() {
        return "-fx-background-color: " + BG_INPUT + ";" +
               "-fx-border-color: " + BORDER_COLOR + ";" +
               "-fx-border-radius: 10;" +
               "-fx-background-radius: 10;" +
               "-fx-padding: 0 14;" +
               "-fx-font-size: 13px;" +
               "-fx-prompt-text-fill: " + TEXT_MUTED_DARK + ";" +
               "-fx-text-fill: " + TEXT_DARK + ";";
    }

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
}