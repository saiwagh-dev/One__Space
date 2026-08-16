package com.file_handlers.view.adminView;

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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;

public class AdminSignUpPage {

    // =========================================================
    // SLATE BLUE THEME CONSTANTS
    // =========================================================

    private static final String FONT =
            "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    private static final String BG_APP = "#3A4D67";
    private static final String BG_CARD = "#DDE8F5";
    private static final String BG_INPUT = "#EDF3FA";

    private static final String BORDER_COLOR = "#C9DAEE";

    private static final String PRIMARY_BLUE = "#2563EB";

    private static final String TEXT_DARK = "#142338";
    private static final String TEXT_MUTED_DARK = "#506580";

    private static final String TEXT_MUTED_LIGHT = "#9EB0C6";

    public Scene getAdminSignUpScene() {

        // =========================================================
        // APP HEADER BAR
        // =========================================================

        Button backBtn = new Button("← Back to home");

        backBtn.setFont(
                Font.font(FONT, FontWeight.SEMI_BOLD, 12)
        );

        backBtn.setTextFill(
                Color.web(TEXT_MUTED_LIGHT)
        );

        backBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-cursor: hand;"
        );

        backBtn.setOnAction(e -> { 
        LandingPage.setScene(new LandingPage().getLandingPageScene()); 
        });

        HBox appHeader = new HBox(
                new Region(),
                backBtn
        );

        HBox.setHgrow(
                appHeader.getChildren().get(0),
                Priority.ALWAYS
        );

        appHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        appHeader.setPadding(
                new Insets(16, 24, 16, 24)
        );

        // =========================================================
        // BRANDING HEADER (ABOVE CARD GRID)
        // =========================================================

        Label brandingText = new Label("OneSpace");
        brandingText.setFont(
                Font.font(FONT, FontWeight.BOLD, 24)
        );
        brandingText.setTextFill(
                Color.web("#FFFFFF")
        );

        VBox topBranding = new VBox(
                0,
                createLogo(),
                brandingText
        );

        topBranding.setAlignment(
                Pos.CENTER
        );

        // =========================================================
        // SIGN UP CARD HEADER
        // =========================================================

        Label title = new Label("Create Account");

        title.setFont(
                Font.font(FONT, FontWeight.BOLD, 20)
        );

        title.setTextFill(
                Color.web(TEXT_DARK)
        );

        Label subtitle = new Label(
                "Get started with your free OneSpace account"
        );

        subtitle.setFont(
                Font.font(FONT, 13)
        );

        subtitle.setTextFill(
                Color.web(TEXT_MUTED_DARK)
        );

        VBox cardHeader = new VBox(
                4,
                title,
                subtitle
        );

        cardHeader.setAlignment(
                Pos.CENTER
        );

        // =========================================================
        // FULL NAME FIELD
        // =========================================================

        Label nameLabel = new Label("Full Name");

        nameLabel.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        12
                )
        );

        nameLabel.setTextFill(
                Color.web(TEXT_DARK)
        );

        TextField nameField = new TextField();

        nameField.setPromptText(
                "Aarav Verma"
        );

        nameField.setPrefHeight(42);

        nameField.setStyle(
                getFieldStyle()
        );

        VBox nameBox = new VBox(
                6,
                nameLabel,
                nameField
        );

        // =========================================================
        // EMAIL FIELD
        // =========================================================

        Label emailLabel = new Label("Email Address");

        emailLabel.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        12
                )
        );

        emailLabel.setTextFill(
                Color.web(TEXT_DARK)
        );

        TextField emailField = new TextField();

        emailField.setPromptText(
                "name@example.com"
        );

        emailField.setPrefHeight(42);

        emailField.setStyle(
                getFieldStyle()
        );

        VBox emailBox = new VBox(
                6,
                emailLabel,
                emailField
        );

        // =========================================================
        // PASSWORD FIELD
        // =========================================================

        Label passwordLabel = new Label("Password");

        passwordLabel.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        12
                )
        );

        passwordLabel.setTextFill(
                Color.web(TEXT_DARK)
        );

        PasswordField passwordField = new PasswordField();

        passwordField.setPromptText(
                "••••••••"
        );

        passwordField.setPrefHeight(42);

        passwordField.setStyle(
                getFieldStyle()
        );

        VBox passwordBox = new VBox(
                6,
                passwordLabel,
                passwordField
        );

        // =========================================================
        // CONFIRM PASSWORD FIELD
        // =========================================================

        Label confirmPasswordLabel =
                new Label("Confirm Password");

        confirmPasswordLabel.setFont(
                Font.font(
                        FONT,
                        FontWeight.SEMI_BOLD,
                        12
                )
        );

        confirmPasswordLabel.setTextFill(
                Color.web(TEXT_DARK)
        );

        PasswordField confirmPasswordField =
                new PasswordField();

        confirmPasswordField.setPromptText(
                "••••••••"
        );

        confirmPasswordField.setPrefHeight(42);

        confirmPasswordField.setStyle(
                getFieldStyle()
        );

        VBox confirmPasswordBox = new VBox(
                6,
                confirmPasswordLabel,
                confirmPasswordField
        );

        // =========================================================
        // CREATE ACCOUNT BUTTON
        // =========================================================

        Button signupButton =
                new Button("Create Account  →");

        signupButton.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        13
                )
        );

        signupButton.setTextFill(
                Color.WHITE
        );

        signupButton.setMaxWidth(
                Double.MAX_VALUE
        );

        signupButton.setPrefHeight(42);

        signupButton.setStyle(
                "-fx-background-color: " +
                PRIMARY_BLUE +
                ";" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
        );

        signupButton.setOnAction(e -> {
            LandingPage.showAdminDashboard();
        });

        // =========================================================
        // FOOTER LOGIN LINK
        // =========================================================

        Label existingAccountText =
                new Label("Already have an account?");

        existingAccountText.setFont(
                Font.font(FONT, 12)
        );

        existingAccountText.setTextFill(
                Color.web(TEXT_MUTED_DARK)
        );

        Label loginLink =
                new Label("Sign In");

        loginLink.setFont(
                Font.font(
                        FONT,
                        FontWeight.BOLD,
                        12
                )
        );

        loginLink.setTextFill(
                Color.web(PRIMARY_BLUE)
        );

        loginLink.setStyle(
                "-fx-cursor: hand;"
        );

        loginLink.setOnMouseClicked(e -> {
            LandingPage.showAdminLoginPage();
        });

        HBox loginBox = new HBox(
                4,
                existingAccountText,
                loginLink
        );

        loginBox.setAlignment(
                Pos.CENTER
        );

        // =========================================================
        // CARD ASSEMBLY
        // =========================================================

        VBox card = new VBox(
                16,
                cardHeader,
                nameBox,
                emailBox,
                passwordBox,
                confirmPasswordBox,
                signupButton,
                loginBox
        );

        card.setPadding(
                new Insets(
                        28,
                        28,
                        28,
                        28
                )
        );

        card.setPrefWidth(380);
        card.setMaxWidth(380);

        card.setStyle(
                "-fx-background-color: " +
                BG_CARD +
                ";" +

                "-fx-border-color: " +
                BORDER_COLOR +
                ";" +

                "-fx-border-radius: 18;" +

                "-fx-background-radius: 18;" +

                "-fx-effect: dropshadow(" +
                "three-pass-box, " +
                "rgba(0,0,0,0.12), " +
                "16, 0, 0, 6" +
                ");"
        );

        // =========================================================
        // MAIN LAYOUT ASSEMBLY
        // =========================================================

        Region topSpacer = new Region();
        Region bottomSpacer = new Region();

        VBox.setVgrow(
                topSpacer,
                Priority.ALWAYS
        );

        VBox.setVgrow(
                bottomSpacer,
                Priority.ALWAYS
        );

        VBox centerBody = new VBox(
                8,
                topSpacer,
                topBranding,
                card,
                bottomSpacer
        );

        centerBody.setAlignment(
                Pos.CENTER
        );

        centerBody.setPadding(
                new Insets(
                        0,
                        24,
                        24,
                        24
                )
        );

        // =========================================================
        // ROOT LAYOUT
        // =========================================================

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " +
                BG_APP +
                ";"
        );

        root.setTop(
                appHeader
        );

        root.setCenter(
                centerBody
        );

        return new Scene(
                root,
                1200,
                750
        );
    }

    // =========================================================
    // LOGO LOADER
    // =========================================================

    private StackPane createLogo() {
        InputStream stream = getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png");
        if (stream != null) {
            Image logoImage = new Image(stream);
            ImageView imageView = new ImageView(logoImage);
            imageView.setFitWidth(76); 
            imageView.setFitHeight(76); 
            imageView.setPreserveRatio(true);
            return new StackPane(imageView);
        }
        Circle circle = new Circle(36, Color.web(PRIMARY_BLUE));
        Label fallback = new Label("O");
        fallback.setFont(Font.font(FONT, FontWeight.BOLD, 30));
        fallback.setTextFill(Color.WHITE);
        return new StackPane(circle, fallback);
    }

    // =========================================================
    // INPUT FIELD STYLE
    // =========================================================

    private String getFieldStyle() {

        return
                "-fx-background-color: " +
                BG_INPUT +
                ";" +

                "-fx-border-color: " +
                BORDER_COLOR +
                ";" +

                "-fx-border-radius: 10;" +

                "-fx-background-radius: 10;" +

                "-fx-padding: 0 14;" +

                "-fx-font-size: 13px;" +

                "-fx-prompt-text-fill: " +
                TEXT_MUTED_DARK +
                ";" +

                "-fx-text-fill: " +
                TEXT_DARK +
                ";";
    }
}