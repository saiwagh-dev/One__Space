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

    // Typography
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar Tones
    private static final String SIDEBAR_BG = "#070C16";

    // 2. Center Workspace Canvas: Atmospheric Dark Radial Glow
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards & Container Colors
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";
    private static final String INPUT_BG = "rgba(13, 22, 38, 0.85)";
    private static final String INPUT_BORDER = "rgba(255, 255, 255, 0.1)";

    // 4. Vibrant Typography & Accent Highlights
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";
    private static final String ERROR_COLOR = "#F87171";

    public Scene getUserSignupPageScene() {

        // App Header
        Button backBtn = new Button("← Back to home");
        backBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        backBtn.setTextFill(Color.web(LIGHT_SECONDARY));
        backBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e -> backBtn.setTextFill(Color.web(WHITE)));
        backBtn.setOnMouseExited(e -> backBtn.setTextFill(Color.web(LIGHT_SECONDARY)));
        backBtn.setOnAction(e -> {LandingPage.setScene(new LandingPage().getLandingPageScene());});

        HBox appHeader = new HBox(new Region(), backBtn);
        HBox.setHgrow(appHeader.getChildren().get(0), Priority.ALWAYS);
        appHeader.setAlignment(Pos.CENTER_LEFT);
        appHeader.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 16, ResponsiveUtil.PAGE_PADDING));

        // Signup Card Header
        StackPane centerIconPane = createOneSpaceLogo(135);

        Label title = new Label("One Space");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        title.setTextFill(Color.web(WHITE));

        Label subtitle = new Label("Create your personal OneSpace workspace");
        subtitle.setFont(Font.font(FONT, 13));
        subtitle.setTextFill(Color.web(LIGHT_SECONDARY));

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
        nameLabel.setTextFill(Color.web(WHITE));

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setPrefHeight(42);
        nameField.setStyle(getFieldStyle());

        VBox nameBox = new VBox(6, nameLabel, nameField);

        // Email Field
        Label emailLabel = new Label("Email Address");
        emailLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        emailLabel.setTextFill(Color.web(WHITE));

        TextField emailField = new TextField();
        emailField.setPromptText("name@example.com");
        emailField.setPrefHeight(42);
        emailField.setStyle(getFieldStyle());

        VBox emailBox = new VBox(6, emailLabel, emailField);

        // Password Fields
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        passwordLabel.setTextFill(Color.web(WHITE));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("At least 6 characters");
        passwordField.setPrefHeight(42);
        passwordField.setStyle(getFieldStyle());

        VBox passwordBox = new VBox(6, passwordLabel, passwordField);

        Label confirmLabel = new Label("Confirm Password");
        confirmLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        confirmLabel.setTextFill(Color.web(WHITE));

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Re-enter your password");
        confirmField.setPrefHeight(42);
        confirmField.setStyle(getFieldStyle());

        VBox confirmBox = new VBox(6, confirmLabel, confirmField);

        // Controller Instantiation
        AuthController authController = new AuthController();

        // Signup Button & Action Logic
        Button signupButton = new Button("Create Account   →");
        signupButton.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        signupButton.setTextFill(Color.WHITE);
        signupButton.setMaxWidth(Double.MAX_VALUE);
        signupButton.setPrefHeight(42);
        signupButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.45), 10, 0, 0, 2);"
        );

        signupButton.setOnAction(e -> {
            String fullName = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmField.getText();
            
            // Provide a default bio since there isn't a bio text field on the signup screen yet
            String bio = "OneSpace user";

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
            String idToken = authController.signUpAndGetToken(email, password, fullName, bio);

            if (idToken != null) {
                authController.updateProfile(idToken, fullName);
                
                // Fix UserSession call: pass positional arguments (uid, idToken, email, displayName, isAdmin)
                // Note: If you need the exact UID here, you can have your controller return it or fetch it, 
                // but setting instance with token/email/name updates the active session.
                UserSession.setInstance(null, idToken, email, fullName, false);

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
        accountText.setTextFill(Color.web(LIGHT_SECONDARY));

        Label loginLink = new Label("Sign In");
        loginLink.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        loginLink.setTextFill(Color.web("#38BDF8"));
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
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-border-color: " + CARD_BORDER + ";" +
                "-fx-border-width: 1.2;" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);"
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
        root.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        root.setTop(appHeader);
        root.setCenter(centerBody);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    // Input Style
    private String getFieldStyle() {
        return "-fx-background-color: " + INPUT_BG + ";" +
               "-fx-border-color: " + INPUT_BORDER + ";" +
               "-fx-border-radius: 10;" +
               "-fx-background-radius: 10;" +
               "-fx-padding: 0 14;" +
               "-fx-font-size: 13px;" +
               "-fx-prompt-text-fill: " + LIGHT_SECONDARY + ";" +
               "-fx-text-fill: " + WHITE + ";";
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