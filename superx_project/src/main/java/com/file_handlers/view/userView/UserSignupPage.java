package com.file_handlers.view.userView;

import com.file_handlers.controller.AuthController;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

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

public class UserSignupPage {

    // Theme Constants
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
    private static final String ERROR_COLOR = "#DC2626";

    public Scene getUserSignupPageScene() {

        // App Header
        Button backBtn = new Button("← Back to home");
        backBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        backBtn.setTextFill(Color.web(TEXT_MUTED_LIGHT));
        backBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        backBtn.setOnAction(e -> {LandingPage.setScene(new LandingPage().getLandingPageScene());});

        HBox appHeader = new HBox(new Region(), backBtn);
        HBox.setHgrow(appHeader.getChildren().get(0), Priority.ALWAYS);
        appHeader.setAlignment(Pos.CENTER_LEFT);
        appHeader.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 16, ResponsiveUtil.PAGE_PADDING));

        // Signup Card Header
        StackPane centerIconPane = createOneSpaceLogo(135);

        Label title = new Label("One Space");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        title.setTextFill(Color.web(TEXT_DARK));

        Label subtitle = new Label("Create your personal OneSpace workspace");
        subtitle.setFont(Font.font(FONT, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED_DARK));

        // Error Feedback Label
        Label errorLabel = new Label();
        errorLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        errorLabel.setTextFill(Color.web(ERROR_COLOR));
        errorLabel.setManaged(false);
        errorLabel.setVisible(false);

        VBox cardHeader = new VBox(-3, centerIconPane, title, subtitle, errorLabel);
        cardHeader.setAlignment(Pos.CENTER);

        // Name Field
        Label nameLabel = new Label("Full Name");
        nameLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        nameLabel.setTextFill(Color.web(TEXT_DARK));

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setPrefHeight(42);
        nameField.setStyle(getFieldStyle());

        VBox nameBox = new VBox(6, nameLabel, nameField);

        // Email Field
        Label emailLabel = new Label("Email Address");
        emailLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        emailLabel.setTextFill(Color.web(TEXT_DARK));

        TextField emailField = new TextField();
        emailField.setPromptText("name@example.com");
        emailField.setPrefHeight(42);
        emailField.setStyle(getFieldStyle());

        VBox emailBox = new VBox(6, emailLabel, emailField);

        // Password Fields
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        passwordLabel.setTextFill(Color.web(TEXT_DARK));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("At least 6 characters");
        passwordField.setPrefHeight(42);
        passwordField.setStyle(getFieldStyle());

        VBox passwordBox = new VBox(6, passwordLabel, passwordField);

        Label confirmLabel = new Label("Confirm Password");
        confirmLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        confirmLabel.setTextFill(Color.web(TEXT_DARK));

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Re-enter your password");
        confirmField.setPrefHeight(42);
        confirmField.setStyle(getFieldStyle());

        VBox confirmBox = new VBox(6, confirmLabel, confirmField);

        // Controller Instantiation
        AuthController authController = new AuthController();

        // Signup Button & Action Logic
        Button signupButton = new Button("Create Account  →");
        signupButton.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        signupButton.setTextFill(Color.WHITE);
        signupButton.setMaxWidth(Double.MAX_VALUE);
        signupButton.setPrefHeight(42);
        signupButton.setStyle(
                "-fx-background-color: " + PRIMARY_BLUE + ";" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
        );

        signupButton.setOnAction(e -> {
            String fullName = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmField.getText();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                errorLabel.setText("Please fill out all fields.");
                errorLabel.setManaged(true);
                errorLabel.setVisible(true);
                return;
            }

            if (password.length() < 6) {
                errorLabel.setText("Password must be at least 6 characters.");
                errorLabel.setManaged(true);
                errorLabel.setVisible(true);
                return;
            }

            if (!password.equals(confirmPassword)) {
                errorLabel.setText("Passwords do not match.");
                errorLabel.setManaged(true);
                errorLabel.setVisible(true);
                return;
            }

            String idToken = authController.signUpAndGetToken(email, password);
            if (idToken != null) {
                authController.updateProfile(idToken, fullName);
                UserSession.setInstance(idToken, email, fullName, false);

                errorLabel.setManaged(false);
                errorLabel.setVisible(false);
                LandingPage.showUserDashboard();
            } else {
                errorLabel.setText("Registration failed. Email may already be in use.");
                errorLabel.setManaged(true);
                errorLabel.setVisible(true);
            }
        });

        // Login Link
        Label accountText = new Label("Already have an account?");
        accountText.setFont(Font.font(FONT, 12));
        accountText.setTextFill(Color.web(TEXT_MUTED_DARK));

        Label loginLink = new Label("Sign In");
        loginLink.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        loginLink.setTextFill(Color.web(PRIMARY_BLUE));
        loginLink.setStyle("-fx-cursor: hand;");
        loginLink.setOnMouseClicked(e -> LandingPage.showUserLoginPage());

        HBox loginBox = new HBox(4, accountText, loginLink);
        loginBox.setAlignment(Pos.CENTER);

        // Card Assembly
        VBox card = new VBox(
                16,
                cardHeader,
                nameBox,
                emailBox,
                passwordBox,
                confirmBox,
                signupButton,
                loginBox
        );

        card.setPadding(new Insets(24, 28, 24, 28));
        card.setPrefWidth(ResponsiveUtil.AUTH_CARD_WIDTH);
        card.setMaxWidth(ResponsiveUtil.AUTH_CARD_WIDTH);
        card.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_COLOR + ";" +
                "-fx-border-radius: 18;" +
                "-fx-background-radius: 18;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 16, 0, 0, 6);"
        );

        // Main Layout
        Region topSpacer = new Region();
        Region bottomSpacer = new Region();

        VBox.setVgrow(topSpacer, Priority.ALWAYS);
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        VBox centerBody = new VBox(topSpacer, card, bottomSpacer);
        centerBody.setAlignment(Pos.CENTER);
        centerBody.setPadding(new Insets(0, ResponsiveUtil.PAGE_PADDING, ResponsiveUtil.PAGE_PADDING, ResponsiveUtil.PAGE_PADDING));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_APP + ";");
        root.setTop(appHeader);
        root.setCenter(centerBody);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    // Input Style
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