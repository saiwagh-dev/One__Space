package com.file_handlers.view.adminView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
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
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;

import com.file_handlers.controller.AdminAuthController;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

public class AdminLoginPage {

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

    public Scene getAdminLoginPageScene() {

        // App Header Bar
        Button backBtn = new Button("← Back to home");
        backBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        backBtn.setTextFill(Color.web(TEXT_MUTED_LIGHT));
        backBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        backBtn.setOnAction(e -> { 
            LandingPage.setScene(new LandingPage().getLandingPageScene()); 
        });

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
        Label subtitle = new Label("Enter your credentials to access OneSpace");
        subtitle.setFont(Font.font(FONT, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED_DARK));

        // Error Feedback Label
        Label errorLabel = new Label();
        errorLabel.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        errorLabel.setTextFill(Color.web(ERROR_COLOR));
        errorLabel.setManaged(false);
        errorLabel.setVisible(false);

        // Card Header - Direct assembly
        VBox cardHeader = new VBox(4, createLogo(), brandingText, subtitle, errorLabel);
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
        forgotPassword.setTextFill(Color.web("#38BDF8"));
        forgotPassword.setStyle("-fx-cursor: hand;");

        HBox passwordHeader = new HBox(passwordLabel, new Region(), forgotPassword);
        HBox.setHgrow(passwordHeader.getChildren().get(1), Priority.ALWAYS);

        // --- Password Field with Creative Vector Eye Toggle ---
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");
        passwordField.setPrefHeight(42);
        passwordField.setStyle(getFieldStyle() + "; -fx-padding: 0 44 0 14;");

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("••••••••");
        visiblePasswordField.setPrefHeight(42);
        visiblePasswordField.setStyle(getFieldStyle() + "; -fx-padding: 0 44 0 14;");
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setManaged(false);

        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());

        // SVG Path definitions for Eye Open vs Eye Closed
        String eyeOpenSvg = "M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z";
        String eyeClosedSvg = "M12 7c2.76 0 5 2.24 5 5 0 .65-.13 1.26-.36 1.83l2.92 2.92c1.51-1.26 2.7-2.89 3.43-4.75-1.73-4.39-6-7.5-11-7.5-1.4 0-2.74.25-3.98.7l2.16 2.16C10.74 7.13 11.35 7 12 7zM2 4.27l2.28 2.28.46.46C3.08 8.3 1.78 10.02 1 12c1.73 4.39 6 7.5 11 7.5 1.55 0 3.03-.3 4.38-.84l.42.42L19.73 22 21 20.73 3.27 3 2 4.27zM7.53 9.8l1.55 1.55c-.05.21-.08.43-.08.65 0 1.66 1.34 3 3 3 .22 0 .44-.03.65-.08l1.55 1.55c-.67.33-1.41.53-2.2.53-2.76 0-5-2.24-5-5 0-.79.2-1.53.53-2.2zm4.31-.78l3.15 3.15.02-.12c0-1.66-1.34-3-3-3l-.17.02z";

        SVGPath iconPath = new SVGPath();
        iconPath.setContent(eyeOpenSvg);
        iconPath.setFill(Color.web(TEXT_MUTED_DARK));
        iconPath.setScaleX(0.75);
        iconPath.setScaleY(0.75);

        ToggleButton showHideBtn = new ToggleButton();
        showHideBtn.setGraphic(iconPath);
        showHideBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0;");
        StackPane.setAlignment(showHideBtn, Pos.CENTER_RIGHT);
        StackPane.setMargin(showHideBtn, new Insets(0, 12, 0, 0));

        showHideBtn.setOnAction(e -> {
            if (showHideBtn.isSelected()) {
                visiblePasswordField.setText(passwordField.getText());
                visiblePasswordField.setVisible(true);
                visiblePasswordField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                iconPath.setContent(eyeClosedSvg);
            } else {
                passwordField.setText(visiblePasswordField.getText());
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                visiblePasswordField.setVisible(false);
                visiblePasswordField.setManaged(false);
                iconPath.setContent(eyeOpenSvg);
            }
        });

        StackPane passwordStack = new StackPane(passwordField, visiblePasswordField, showHideBtn);
        VBox passwordBox = new VBox(6, passwordHeader, passwordStack);
        // -------------------------------------------------------------
        
        // Controller instantiation
        AdminAuthController adminAuthController = new AdminAuthController();

        // Action Buttons
        Button loginButton = new Button("Sign In  →");
        loginButton.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        loginButton.setTextFill(Color.WHITE);
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(42);
        loginButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #1D4ED8, #0284C7);" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: rgba(96, 165, 250, 0.6);" +
            "-fx-border-radius: 12;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(2, 132, 199, 0.5), 14, 0, 0, 3);"
        );
        
        loginButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.isVisible() ? passwordField.getText() : visiblePasswordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill out all fields.");
                errorLabel.setManaged(true);
                errorLabel.setVisible(true);
                return;
            }

            boolean success = adminAuthController.adminSignInAndSetSession(email, password);
            if (success) {
                errorLabel.setManaged(false);
                errorLabel.setVisible(false);
                LandingPage.showAdminDashboard();
            } else {
                errorLabel.setText("Invalid email or password.");
                errorLabel.setManaged(true);
                errorLabel.setVisible(true);
            }
        });

        // Footer Link
       
        HBox signUpBox = new HBox(4);
        signUpBox.setAlignment(Pos.CENTER);

        // Card Assembly
        VBox card = new VBox(16, cardHeader, emailBox, passwordBox, loginButton, signUpBox);
        card.setPadding(new Insets(32, 28, 32, 28));
        card.setPrefWidth(360);
        card.setMaxWidth(360);
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
               "-fx-font-size: 13px;" +
               "-fx-prompt-text-fill: #64748B;" +
               "-fx-text-fill: " + TEXT_DARK + ";";
    }
}