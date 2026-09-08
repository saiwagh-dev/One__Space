package com.file_handlers.view.adminView;

import com.file_handlers.view.LandingPage;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class AboutUsPage {
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.88), rgba(9, 16, 30, 0.96))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String CYAN = "#38BDF8";

    public Scene getAboutUsScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");

        VBox content = new VBox(
                70,
                createHeroSection(),
                createProblemSection(),
                createSolutionSection(),
                createAboutSection(),
                createFeaturesSection(),
                createHowItWorksSection(),
                createCollaborationSection(),
                createAiSection(),
                createMentorSection(),
                createInstructorsSection(),
                createSuperMentorsSection(),
                createMentorsSubSection(),
                createTeamLeadSection(),
                createTeamSection(),
                createVisionSection(),
                createFinalSection(),
                createFooter()
        );

        content.setPadding(new Insets(45, 70, 70, 70));
        content.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent;-fx-background-color: transparent;-fx-padding: 0;-fx-control-inner-background: transparent;-fx-control-inner-background-alt: transparent;");

        scrollPane.lookupAll(".scroll-bar").forEach(bar -> {
            bar.setStyle("-fx-background-color: transparent;-fx-pref-width: 0;-fx-pref-height: 0;-fx-opacity: 0;");
            bar.setVisible(false);
        });

        root.setTop(createTopBar());
        root.setCenter(scrollPane);
        playPageAnimation((VBox) content.getChildren().get(0));

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private HBox createTopBar() {
        ImageView logo = loadImage("/assets/logo/OneSpace_logo.jpeg");
        logo.setFitWidth(42);
        logo.setFitHeight(42);
        logo.setPreserveRatio(true);

        VBox logoText = new VBox(
                2,
                createLabel("OneSpace", 20, FontWeight.BOLD, WHITE),
                createLabel("Your AI Workspace", 11, FontWeight.NORMAL, LIGHT_SECONDARY)
        );

        HBox brand = new HBox(10, logo, logoText);
        brand.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("←  Back");
        backButton.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        backButton.setTextFill(Color.web(CYAN));
        backButton.setStyle("-fx-background-color: transparent;-fx-border-color: rgba(56, 189, 248, 0.35);-fx-border-radius: 10;-fx-background-radius: 10;-fx-padding: 10 18 10 18;-fx-cursor: hand;");
        backButton.setOnAction(e -> LandingPage.showAdminSettings());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(brand, spacer, backButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(18, 55, 18, 55));
        topBar.setStyle("-fx-background-color: rgba(7, 12, 22, 0.88);-fx-border-color: transparent transparent rgba(56, 189, 248, 0.12) transparent;-fx-border-width: 0 0 1 0;");

        return topBar;
    }

    private VBox createHeroSection() {
        Label title = createLabel("Everything important.\nOne intelligent space.", 42, FontWeight.BOLD, WHITE);
        title.setTextAlignment(TextAlignment.CENTER);

        Label description = createLabel(
                "OneSpace brings your files, workspaces, collaboration, calendar, reminders and AI tools together in one place.",
                17, FontWeight.NORMAL, LIGHT_SECONDARY
        );
        description.setWrapText(true);
        description.setMaxWidth(720);
        description.setTextAlignment(TextAlignment.CENTER);

        VBox hero = new VBox(
                18,
                createLabel("ABOUT ONESPACE", 13, FontWeight.BOLD, CYAN),
                title,
                description
        );
        hero.setAlignment(Pos.CENTER);
        return hero;
    }

    private VBox createProblemSection() {
        ImageView img = loadImage("/assets/Image/Designer.jpeg");
        img.setFitWidth(400);
        img.setPreserveRatio(true);

        VBox cards = new VBox(
                15,
                createHorizontalCard("📱", "#F43F5E", "Scattered Files", "Files are often shared across chats and different apps."),
                createHorizontalCard("🔍", "#38BDF8", "Hard to Find", "Important documents can become difficult to locate."),
                createHorizontalCard("⏰", "#F59E0B", "Missed Deadlines", "Important events and reminders can be forgotten.")
        );

        HBox row = new HBox(40, img, cards);
        row.setAlignment(Pos.CENTER);

        VBox section = new VBox(
                30,
                createSectionTitle("THE PROBLEM", "Important files are everywhere."),
                row
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createSolutionSection() {
        ImageView img = loadImage("/assets/Image/Designer (1).jpeg");
        img.setFitWidth(420);
        img.setPreserveRatio(true);

        VBox cards = new VBox(
                15,
                createHorizontalCard("📦", "#10B981", "Smart Spaces", "Auto-categorizes files into dedicated folders seamlessly."),
                createHorizontalCard("🤖", "#8B5CF6", "AI Workspace Companion", "Instant search and intelligent assistance inside your fingertips."),
                createHorizontalCard("📅", "#EC4899", "Synchronized Flow", "Deadlines, calendars, and tasks mapped cleanly together.")
        );

        HBox row = new HBox(40, img, cards);
        row.setAlignment(Pos.CENTER);

        VBox section = new VBox(
                30,
                createSectionTitle("THE SOLUTION", "One unified, organized workspace."),
                row
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createAboutSection() {
        Label desc = createLabel(
                "OneSpace is designed to make digital organization simpler. Your files are intelligently organized into different spaces, making them easier to manage and access. With search, collaboration, calendar tools, reminders and an AI assistant, OneSpace brings your digital workspace together.",
                16, FontWeight.NORMAL, LIGHT_SECONDARY
        );
        desc.setWrapText(true);
        desc.setMaxWidth(800);
        desc.setTextAlignment(TextAlignment.CENTER);

        VBox card = new VBox(
                20,
                createSectionTitle("WHAT IS ONESPACE?", "One intelligent workspace for everything."),
                desc
        );
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(45));
        card.setMaxWidth(900);
        card.setStyle(cardStyle());
        return card;
    }

    private VBox createFeaturesSection() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        addFeature(grid, 0, 0, "📦", "#10B981", "Smart Spaces", "Files are organized into meaningful spaces automatically.");
        addFeature(grid, 1, 0, "🔍", "#38BDF8", "Smart Search", "Find important files and information quickly.");
        addFeature(grid, 2, 0, "👥", "#6366F1", "Collaboration", "Create shared workspaces and work together.");
        addFeature(grid, 0, 1, "📅", "#EC4899", "Calendar & Reminders", "Keep track of events, tasks and important deadlines.");
        addFeature(grid, 1, 1, "🤖", "#8B5CF6", "AI Assistant", "Get intelligent assistance inside your workspace.");
        addFeature(grid, 2, 1, "📁", "#F59E0B", "One Workspace", "Keep everything connected in OneSpace.");

        VBox section = new VBox(
                35,
                createSectionTitle("BUILT FOR MORE THAN STORAGE", "Everything you need to manage your digital workspace."),
                grid
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private void addFeature(GridPane grid, int column, int row, String icon, String color, String title, String desc) {
        grid.add(createColorfulFeatureCard(icon, color, title, desc), column, row);
    }

    private VBox createHowItWorksSection() {
        HBox steps = new HBox(
                20,
                createStepCard("01", "#38BDF8", "Upload", "Add your important files."),
                createLabel("→", 28, FontWeight.BOLD, CYAN),
                createStepCard("02", "#10B981", "Organize", "Files are placed into the right spaces."),
                createLabel("→", 28, FontWeight.BOLD, CYAN),
                createStepCard("03", "#8B5CF6", "Access", "Find what you need whenever you need it.")
        );
        steps.setAlignment(Pos.CENTER);

        VBox section = new VBox(
                35,
                createSectionTitle("HOW ONESPACE WORKS", "A simple flow for a smarter workspace."),
                steps
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createCollaborationSection() {
        StackPane iconBadge = createDashboardIconBadge("👥", "#6366F1");

        VBox visual = new VBox(
                15,
                iconBadge,
                createLabel("Shared Workspace", 24, FontWeight.BOLD, WHITE),
                createLabel("Share files • Collaborate • Stay connected", 14, FontWeight.NORMAL, LIGHT_SECONDARY)
        );
        visual.setAlignment(Pos.CENTER);
        visual.setPadding(new Insets(45));
        visual.setPrefWidth(750);
        visual.setStyle(cardStyle());

        VBox section = new VBox(
                30,
                createSectionTitle("WORK BETTER TOGETHER", "OneSpace keeps your shared work connected."),
                visual
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createAiSection() {
        HBox promptBox = new HBox(
                10,
                createLabel("✨", 18, FontWeight.NORMAL, "#F59E0B"),
                createLabel("Find my recent project files", 14, FontWeight.NORMAL, LIGHT_SECONDARY)
        );
        promptBox.setAlignment(Pos.CENTER_LEFT);
        promptBox.setPadding(new Insets(14, 18, 14, 18));
        promptBox.setMaxWidth(450);
        promptBox.setStyle("-fx-background-color: rgba(3, 10, 22, 0.65);-fx-border-color: rgba(56, 189, 248, 0.25);-fx-border-radius: 12;-fx-background-radius: 12;");

        VBox aiCard = new VBox(
                18,
                createLabel("How can I help you today?", 18, FontWeight.SEMI_BOLD, WHITE),
                promptBox,
                createLabel("Searching your workspace...", 12, FontWeight.NORMAL, CYAN)
        );
        aiCard.setAlignment(Pos.CENTER);
        aiCard.setPadding(new Insets(45));
        aiCard.setPrefWidth(650);
        aiCard.setStyle(cardStyle() + "-fx-border-color: rgba(56, 189, 248, 0.35);");

        VBox section = new VBox(
                30,
                createSectionTitle("MEET ONESPACE AI", "Your intelligent workspace companion."),
                aiCard
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createMentorSection() {
        ImageView core2webLogo = loadImage("/assets/logo/core2web.jpeg");
        core2webLogo.setFitWidth(110);
        core2webLogo.setPreserveRatio(true);

        ImageView sirPhoto = loadImage("/assets/Image/ShashiSirImage.jpeg");
        sirPhoto.setFitWidth(100);
        sirPhoto.setFitHeight(100);
        sirPhoto.setPreserveRatio(false);
        sirPhoto.setClip(new Circle(50, 50, 50));

        Label nameLabel = createLabel("Shashi Bagal (Shashi Sir)", 20, FontWeight.BOLD, WHITE);

        Label reviewText = createLabel(
                "We sincerely thank Shashi Sir from Core2Web for building our strong foundation in Core Java and Object-Oriented Programming. His approach of explaining the internal flow of code helped us understand not just how to write code, but why it works.\n\nHis guidance strengthened our programming logic, problem-solving skills, and understanding of OOP concepts. The knowledge we gained from his sessions has been an important part of our technical journey and has supported us throughout the development of our project.\n\nThank you, Sir, for sharing your knowledge and inspiring us to understand technology at a deeper level.",
                14, FontWeight.NORMAL, LIGHT_SECONDARY
        );
        reviewText.setWrapText(true);
        reviewText.setTextAlignment(TextAlignment.CENTER);
        reviewText.setMaxWidth(620);

        VBox mentorCard = new VBox(20, core2webLogo, sirPhoto, nameLabel, reviewText);
        mentorCard.setAlignment(Pos.CENTER);
        mentorCard.setPadding(new Insets(45));
        mentorCard.setMaxWidth(750);
        mentorCard.setStyle(cardStyle());

        VBox section = new VBox(
                30,
                createSectionTitle("MENTOR", "Guiding us to the core of programming."),
                mentorCard
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createInstructorsSection() {
        HBox instructorsGrid = new HBox(
                20,
                createInstructorCard("/assets/Image/SachinSirImage.jpeg", "Sachin Sir", "A heartfelt thanks to Sachin Sir for teaching us UI Development through practical sessions and helping us understand UI concepts from the fundamentals to implementation. His knowledge and guidance were extremely valuable throughout the development of our project."),
                createInstructorCard("/assets/Image/AkshaySirImage.jpeg", "Akshay Sir", "We sincerely thank Akshay Sir for his valuable guidance and constant encouragement throughout our journey. His knowledge, support, and confidence in us motivated us to overcome challenges and played an important role in the successful development of our project."),
                createInstructorCard(null, "Pramod Sir", "We sincerely thank Pramod Sir for teaching us Backend Development and guiding us through the complete backend implementation. He also introduced us to Git and GitHub, helping us understand version control and collaborative development.")
        );
        instructorsGrid.setAlignment(Pos.CENTER);

        VBox section = new VBox(
                30,
                createSectionTitle("INSTRUCTORS", "The minds behind our learning."),
                instructorsGrid
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createSuperMentorsSection() {
        HBox grid = new HBox(
                20,
                createInstructorCard("/assets/Image/ShivSirImage.jpeg", "Shiv Sir", "We sincerely thank Shiv Sir for always being there to clear our doubts and guide us whenever we faced challenges. His continuous support, valuable guidance, and willingness to help played an important role throughout the development of our project."),
                createInstructorCard("/assets/Image/SubodhSirImage.jpeg", "Subodh Sir", "We sincerely thank Subodh Sir for his continuous support and guidance throughout our project journey. Whenever we faced challenges or difficulties, he was always there to help us find the right solutions.")
        );
        grid.setAlignment(Pos.CENTER);

        VBox section = new VBox(
                30,
                createSectionTitle("SUPER MENTORS", "Paving the path to success."),
                grid
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createMentorsSubSection() {
        HBox grid = new HBox(
                20,
                createInstructorCard(null, "Sumit Dada", "Thank you Sumit Dada for your continuous support, motivation, and guidance throughout our development journey."),
                createInstructorCard(null, "Manasi Didi", "We sincerely thank Manasi Didi for her constant guidance and valuable suggestions. Her support helped us improve our project and overcome challenges throughout the development journey."),
                createInstructorCard(null, "Dhanashree Didi", "Thank you Dhanashree Didi for your constant encouragement and guidance that helped us successfully build and refine our project.")
        );
        grid.setAlignment(Pos.CENTER);

        VBox section = new VBox(
                30,
                createSectionTitle("MENTORS", "Guiding and supporting our journey."),
                grid
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createTeamLeadSection() {
        HBox grid = new HBox(
                20,
                createInstructorCard(null, "Sudarshana Didi", "Leading the team with dedication, clarity, and continuous support to ensure the successful execution and development of OneSpace.")
        );
        grid.setAlignment(Pos.CENTER);

        VBox section = new VBox(
                30,
                createSectionTitle("TEAM LEAD", "Steering the vision forward."),
                grid
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createTeamSection() {
        HBox grid = new HBox(
                15,
                createTeamMemberCard("Sai Wagh"),
                createTeamMemberCard("Vaishnavi Kasar"),
                createTeamMemberCard("Pratiksha Kanherkar"),
                createTeamMemberCard("Ananta Pachpol"),
                createTeamMemberCard("Shravani Mohite")
        );
        grid.setAlignment(Pos.CENTER);

        VBox section = new VBox(
                30,
                createSectionTitle("MEET THE TEAM BEHIND ONESPACE", "Built with passion and dedication."),
                grid
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createTeamMemberCard(String name) {
        Region placeholder = new Region();
        placeholder.setPrefSize(70, 70);
        placeholder.setStyle("-fx-background-color: rgba(56, 189, 248, 0.1);-fx-background-radius: 35;");

        Label nameLbl = createLabel(name, 15, FontWeight.BOLD, WHITE);
        nameLbl.setTextAlignment(TextAlignment.CENTER);

        VBox cardContent = new VBox(12, placeholder, nameLbl);
        cardContent.setAlignment(Pos.CENTER);

        VBox card = new VBox(cardContent);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefSize(180, 200);
        card.setStyle(cardStyle());
        addHoverEffect(card);
        return card;
    }

    private VBox createInstructorCard(String imagePath, String name, String description) {
        VBox cardContent = new VBox(15);
        cardContent.setAlignment(Pos.CENTER);

        if (imagePath != null) {
            ImageView img = loadImage(imagePath);
            img.setFitWidth(90);
            img.setFitHeight(90);
            img.setPreserveRatio(false);
            img.setClip(new Circle(45, 45, 45));
            cardContent.getChildren().add(img);
        } else {
            Region placeholder = new Region();
            placeholder.setPrefSize(90, 90);
            placeholder.setStyle("-fx-background-color: rgba(56, 189, 248, 0.1);-fx-background-radius: 45;");
            cardContent.getChildren().add(placeholder);
        }

        Label nameLbl = createLabel(name, 18, FontWeight.BOLD, WHITE);
        Label descLbl = createLabel(description, 12, FontWeight.NORMAL, LIGHT_SECONDARY);
        descLbl.setWrapText(true);
        descLbl.setTextAlignment(TextAlignment.CENTER);
        descLbl.setMaxWidth(240);

        cardContent.getChildren().addAll(nameLbl, descLbl);

        VBox card = new VBox(cardContent);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setPrefSize(280, 360);
        card.setStyle(cardStyle());
        addHoverEffect(card);
        return card;
    }

    private StackPane createDashboardIconBadge(String icon, String colorHex) {
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(FONT, FontWeight.NORMAL, 19));
        iconLbl.setStyle("-fx-text-fill: " + colorHex + ";");

        StackPane badge = new StackPane(iconLbl);
        badge.setPrefSize(44, 44);
        badge.setMinSize(44, 44);
        badge.setMaxSize(44, 44);
        badge.setStyle("-fx-background-color: " + colorHex + "26;-fx-background-radius: 10;-fx-border-color: " + colorHex + "66;-fx-border-radius: 10;");
        return badge;
    }

    private VBox createHorizontalCard(String icon, String colorHex, String title, String desc) {
        StackPane badge = createDashboardIconBadge(icon, colorHex);

        VBox textCol = new VBox(
                4,
                createLabel(title, 15, FontWeight.BOLD, WHITE),
                wrapDesc(desc)
        );
        textCol.setAlignment(Pos.CENTER_LEFT);

        HBox row = new HBox(16, badge, textCol);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(row);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setPrefSize(360, 100);
        card.setStyle(cardStyle());
        addHoverEffect(card);
        return card;
    }

    private VBox createColorfulFeatureCard(String icon, String colorHex, String title, String desc) {
        StackPane badge = createDashboardIconBadge(icon, colorHex);

        VBox card = new VBox(
                14,
                badge,
                createLabel(title, 17, FontWeight.BOLD, WHITE),
                wrapCenterDesc(desc)
        );
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setPrefSize(250, 215);
        card.setStyle(cardStyle());
        addHoverEffect(card);
        return card;
    }

    private VBox createStepCard(String num, String colorHex, String title, String desc) {
        StackPane badge = createDashboardIconBadge(num, colorHex);

        VBox card = new VBox(
                14,
                badge,
                createLabel(title, 20, FontWeight.BOLD, WHITE),
                wrapCenterDesc(desc)
        );
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(230, 190);
        card.setPadding(new Insets(20));
        card.setStyle(cardStyle());
        return card;
    }

    private VBox createVisionSection() {
        Label description = createLabel(
                "Our vision is to make digital organization simple, intelligent and connected, so users can spend less time managing files and more time getting things done.",
                16, FontWeight.NORMAL, LIGHT_SECONDARY
        );
        description.setWrapText(true);
        description.setMaxWidth(800);
        description.setTextAlignment(TextAlignment.CENTER);

        VBox card = new VBox(
                20,
                createSectionTitle("OUR VISION", "A smarter way to work with your digital world."),
                description
        );
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(45));
        card.setMaxWidth(900);
        card.setStyle(cardStyle());
        return card;
    }

    private VBox createFinalSection() {
        VBox section = new VBox(
                12,
                createLabel("Everything in OneSpace.", 30, FontWeight.BOLD, WHITE),
                createLabel("Organize. Discover. Collaborate. Simplify.", 15, FontWeight.NORMAL, LIGHT_SECONDARY)
        );
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private VBox createFooter() {
        VBox footer = new VBox(
                6,
                createLabel("OneSpace", 14, FontWeight.BOLD, WHITE),
                createLabel("Secure. Organized. Intelligent.", 11, FontWeight.NORMAL, LIGHT_SECONDARY)
        );
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(30, 0, 20, 0));
        return footer;
    }

    private Label createSectionTitle(String small, String main) {
        Label label = new Label(small + "\n\n" + main);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 25));
        label.setTextFill(Color.web(WHITE));
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
    }

    private Label createLabel(String text, double size, FontWeight weight, String color) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, weight, size));
        label.setTextFill(Color.web(color));
        return label;
    }

    private Label wrapDesc(String desc) {
        Label label = createLabel(desc, 12, FontWeight.NORMAL, LIGHT_SECONDARY);
        label.setWrapText(true);
        return label;
    }

    private Label wrapCenterDesc(String desc) {
        Label label = wrapDesc(desc);
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
    }

    private ImageView loadImage(String path) {
        ImageView imageView = new ImageView();
        try {
            var stream = getClass().getResourceAsStream(path);
            if (stream != null) {
                imageView.setImage(new Image(stream));
            }
        } catch (Exception ignored) {
        }
        return imageView;
    }

    private String cardStyle() {
        return "-fx-background-color: " + CARD_BG + ";-fx-border-color: " + CARD_BORDER + ";-fx-border-radius: 16;-fx-background-radius: 16;";
    }

    private void addHoverEffect(VBox card) {
        card.setOnMouseEntered(e -> {
            card.setTranslateY(-5);
            card.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(18, 38, 65, 0.95), rgba(9, 16, 30, 0.98));-fx-border-color: rgba(56, 189, 248, 0.45);-fx-border-radius: 16;-fx-background-radius: 16;-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.30), 18, 0, 0, 5);");
        });

        card.setOnMouseExited(e -> {
            card.setTranslateY(0);
            card.setStyle(cardStyle());
        });
    }

    private void playPageAnimation(VBox hero) {
        hero.setOpacity(0);
        hero.setTranslateY(30);

        FadeTransition fade = new FadeTransition(Duration.millis(600), hero);
        fade.setToValue(1);

        TranslateTransition move = new TranslateTransition(Duration.millis(600), hero);
        move.setToY(0);

        fade.play();
        move.play();
    }
}