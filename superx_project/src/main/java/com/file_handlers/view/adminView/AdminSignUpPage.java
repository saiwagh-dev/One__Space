package com.file_handlers.view.adminView;

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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;

import com.file_handlers.controller.AdminAuthController;
//import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

public class AdminSignUpPage {

    // Dark Glassmorphic Theme Constants
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_APP = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";
    private static final String BG_CARD = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String BG_INPUT = "rgba(10, 18, 33, 0.85)";
    private static final String BORDER_COLOR = "rgba(56, 189, 248, 0.22)";
    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String TEXT_DARK = "#FFFFFF";
    private static final String TEXT_MUTED_DARK = "#94A3B8";
    private static final String TEXT_MUTED_LIGHT = "#94A3B8";
    private static final String ERROR_COLOR = "#EF4444";

    public Scene getAdminSignUpScene() {

        // App Header Bar
        Button backBtn = new Button("← Back to home");
        backBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        backBtn.setTextFill(Color.web(TEXT_MUTED_LIGHT));
        backBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        backBtn.setOnAction(e -> LandingPage.setScene(new LandingPage().getLandingPageScene()));

        HBox appHeader = new HBox(new Region(), backBtn);
        HBox.setHgrow(appHeader.getChildren().get(0), Priority.ALWAYS);
        appHeader.setAlignment(Pos.CENTER_LEFT);
        appHeader.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 16, ResponsiveUtil.PAGE_PADDING));

        // Branding Text: Negative margin cancels PNG transparency padding
        Label brandingText = new Label("OneSpace");
        brandingText.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        brandingText.setTextFill(Color.web(TEXT_DARK));
        VBox.setMargin(brandingText, new Insets(-26, 0, 4, 0));

        // Subtitle
        Label subtitle = new Label("Register for administrative workspace access");
        subtitle.setFont(Font.font(FONT, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED_DARK));

        // Error Feedback Label
        Label errorLabel = new Label();
        errorLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        errorLabel.setTextFill(Color.web(ERROR_COLOR));
        errorLabel.setManaged(false);
        errorLabel.setVisible(false);

        // Card Header
        VBox cardHeader = new VBox(4, createLogo(), brandingText, subtitle, errorLabel);
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
        Label emailLabel = new Label("Admin Email Address");
        emailLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        emailLabel.setTextFill(Color.web(TEXT_DARK));

        TextField emailField = new TextField();
        emailField.setPromptText("admin@example.com");
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
        AdminAuthController adminController = new AdminAuthController();

        // Signup Button & Action Logic
        Button signupButton = new Button("Create Admin Account  →");
        signupButton.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        signupButton.setTextFill(Color.WHITE);
        signupButton.setMaxWidth(Double.MAX_VALUE);
        signupButton.setPrefHeight(42);
        signupButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #0284C7);" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(2, 132, 199, 0.5), 14, 0, 0, 3);"
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

            
        });

        // Login Link
        Label accountText = new Label("Already have an admin account?");
        accountText.setFont(Font.font(FONT, 12));
        accountText.setTextFill(Color.web(TEXT_MUTED_DARK));

        Label loginLink = new Label("Sign In");
        loginLink.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        loginLink.setTextFill(Color.web("#38BDF8"));
        loginLink.setStyle("-fx-cursor: hand;");
        loginLink.setOnMouseClicked(e -> LandingPage.showAdminLoginPage());

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

        card.setPadding(new Insets(28));
        card.setPrefWidth(380);
        card.setMaxWidth(380);
        card.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_COLOR + ";" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-border-width: 1.2;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);"
        );

        // Main Layout Assembly
        Region topSpacer = new Region();
        Region bottomSpacer = new Region();

        VBox.setVgrow(topSpacer, Priority.ALWAYS);
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        VBox centerBody = new VBox(topSpacer, card, bottomSpacer);
        centerBody.setAlignment(Pos.CENTER);
        centerBody.setPadding(new Insets(0, ResponsiveUtil.PAGE_PADDING, 24, ResponsiveUtil.PAGE_PADDING));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: transparent;");
        root.setTop(appHeader);
        root.setCenter(centerBody);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private StackPane createLogo() {
        InputStream stream = getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png");
        if (stream != null) {
            Image logoImage = new Image(stream);
            ImageView imageView = new ImageView(logoImage);
            imageView.setFitWidth(90); 
            imageView.setFitHeight(90); 
            imageView.setPreserveRatio(true);
            
            StackPane logoHolder = new StackPane(imageView);
            logoHolder.setAlignment(Pos.CENTER);
            return logoHolder;
        }
        Circle circle = new Circle(42, Color.web(PRIMARY_BLUE));
        Label fallback = new Label("O");
        fallback.setFont(Font.font(FONT, FontWeight.BOLD, 36));
        fallback.setTextFill(Color.WHITE);
        return new StackPane(circle, fallback);
    }

    private String getFieldStyle() {
        return "-fx-background-color: " + BG_INPUT + ";" +
               "-fx-border-color: " + BORDER_COLOR + ";" +
               "-fx-border-radius: 10;" +
               "-fx-background-radius: 10;" +
               "-fx-padding: 0 14;" +
               "-fx-font-size: 13px;" +
               "-fx-prompt-text-fill: #64748B;" +
               "-fx-text-fill: " + TEXT_DARK + ";";
    }
}