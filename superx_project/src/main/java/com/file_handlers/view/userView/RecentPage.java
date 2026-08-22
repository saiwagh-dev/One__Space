package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class RecentPage {

    // Style Constants - Exact Color Hierarchy Matched with UserDashboard
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar: Deep Dark Slate
    private static final String BG_SIDEBAR = "#1E2A3A";
    private static final String BG_SIDEBAR_CARD = "#141D29";
    private static final String SIDEBAR_BORDER = "#2D3D52";

    // 2. Center Workspace Canvas: Medium Slate Blue
    private static final String BG_CENTER_CANVAS = "#31435B";

    // 3. Main Cards: Soft Light Blue
    private static final String BG_CARD = "#DDE8F8";
    private static final String BG_CARD_INNER = "#CADDF2";
    private static final String BORDER_CARD = "#C3D6EC";

    // 4. Contrast Typography
    private static final String TEXT_DARK = "#0F172A";       // Deep Navy for headings
    private static final String TEXT_MUTED_DARK = "#334155";  // Slate for subtext inside cards
    private static final String TEXT_LIGHT = "#FFFFFF";       // Main white text on dark surfaces
    private static final String TEXT_MUTED_LIGHT = "#94A3B8"; // Subtext on dark surfaces

    // Accent Colors
    private static final String PRIMARY_BLUE = "#2563EB";

    public Scene getRecentPageScene() {

        // =========================================================
        // SIDEBAR
        // =========================================================

        StackPane logoIcon = createOneSpaceLogo();

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("⌂", "Dashboard", false);
        Button spacesBtn = createSidebarButton("📁", "Spaces", false);
        Button searchBtn = createSidebarButton("⌕", "Search", false);
        Button calendarBtn = createSidebarButton("📅", "Calendar", false);
        Button aiBtn = createSidebarButton("✧", "AI Assistant", false);
        Button collabBtn = createSidebarButton("👥", "Collaboration", false);
        Button recentBtn = createSidebarButton("🕒", "Recent", true);
        Button trashBtn = createSidebarButton("🗑", "Trash", false);
        Button settingsBtn = createSidebarButton("⚙", "Settings", false);
        Button logoutBtn = createSidebarButton("🚪", "Logout", false);

        // Sidebar Navigation Actions
        dashboardBtn.setOnAction(e -> LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e -> LandingPage.showUserSpace());
        searchBtn.setOnAction(e -> LandingPage.showUserSearch());
        calendarBtn.setOnAction(e -> LandingPage.showCalendarPage());
        collabBtn.setOnAction(e -> LandingPage.showCollaborationPage());
        aiBtn.setOnAction(e -> LandingPage.showAiAssistantPage());
        recentBtn.setOnAction(e -> LandingPage.showRecentPage());
        trashBtn.setOnAction(e -> LandingPage.showTrashPage());
        settingsBtn.setOnAction(e -> LandingPage.showLandingPage());
        logoutBtn.setOnAction(e -> LandingPage.showUserLoginPage());

        VBox navList = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn, settingsBtn, logoutBtn);

        // Sidebar Storage Card
        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        storageTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox storageValGroup = new HBox(storageVal, new Region(), storagePercent);
        HBox.setHgrow(storageValGroup.getChildren().get(1), Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress = new ProgressBar(0.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: " + PRIMARY_BLUE + "; -fx-control-inner-background: #0E1520;");

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showLandingPage());

        VBox storageCard = new VBox(8, storageTitle, storageValGroup, sidebarProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: " + BG_SIDEBAR_CARD + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        // Sidebar Assembly (Logout positioned directly below Settings)
        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, logoutBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        // =========================================================
        // TOP SEARCH BAR & PROFILE
        // =========================================================

        Label searchIcon = new Label("⌕");
        searchIcon.setFont(Font.font(FONT, 16));
        searchIcon.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        TextField searchField = new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color: transparent; -fx-prompt-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_LIGHT + ";");

        Label keyShortcut = new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 10));
        keyShortcut.setStyle("-fx-background-color: #141E2C; -fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-padding: 3 6; -fx-background-radius: 4;");

        HBox searchContainer = new HBox(8, searchIcon, searchField, keyShortcut);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0, 12, 0, 14));
        searchContainer.setPrefWidth(420);
        searchContainer.setStyle("-fx-background-color: #141E2C; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button bellBtn = new Button("🔔");
        bellBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: " + TEXT_LIGHT + "; -fx-cursor: hand;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());

        Label avatar = new Label("AV");
        avatar.setPrefSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 50%; -fx-text-fill: " + TEXT_LIGHT + "; -fx-font-weight: bold; -fx-font-size: 12px;");

        Label userName = new Label("Aarav Verma");
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label dropDown = new Label("⌄");
        dropDown.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");

        HBox profileBox = new HBox(10, bellBtn, avatar, userName, dropDown);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, searchContainer, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 28, 14, 28));
        topBar.setStyle("-fx-background-color: " + BG_SIDEBAR + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        // =========================================================
        // RECENT PAGE CONTENT AREA
        // =========================================================

        Label pageTitle = new Label("Recent Files");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 24));
        pageTitle.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

        Label pageSub = new Label("Files you've opened, edited, or indexed recently across your spaces.");
        pageSub.setFont(Font.font(FONT, 13));
        pageSub.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + "; -fx-font-weight: 500;");

        VBox headerBox = new VBox(4, pageTitle, pageSub);

        HBox listHeader = new HBox(
                createHeaderLabel("Name", 350),
                createHeaderLabel("Space", 200),
                createHeaderLabel("Size", 100),
                createHeaderLabel("Last Modified", 150)
        );
        listHeader.setPadding(new Insets(0, 0, 10, 0));
        listHeader.setStyle("-fx-border-color: transparent transparent " + BORDER_CARD + " transparent; -fx-border-width: 0 0 1 0;");

        // Using user home directory or system temporary directory so the files are guaranteed to open/exist when clicked
        File tempDir = new File(System.getProperty("user.home"), "OneSpaceDemoFiles");
        if (!tempDir.exists()) tempDir.mkdirs();

        VBox fileRows = new VBox(8,
                listHeader,
                createFileRow("☕", "#2563EB", "UserService.java", "Backend logic for user authentications", "Java Project", "14.2 KB", "10 mins ago", new File(tempDir, "UserService.java")),
                createFileRow("📊", "#059669", "System_Architecture_v2.pdf", "High level component workflow diagrams", "College Assignments", "4.8 MB", "2 hours ago", new File(tempDir, "System_Architecture_v2.pdf")),
                createFileRow("📝", "#0284C7", "Resume_Aarav_Verma_2026.docx", "Updated technical skill sets & projects", "Placement Preparation", "1.2 MB", "Yesterday", new File(tempDir, "Resume_Aarav_Verma_2026.docx")),
                createFileRow("☕", "#2563EB", "DatabaseConnection.java", "JDBC configuration handler script", "Java Project", "8.5 KB", "Yesterday", new File(tempDir, "DatabaseConnection.java")),
                createFileRow("📈", "#7C3AED", "DSA_Arrays_CheatSheet.md", "Important sorting and searching algorithms", "Placement Preparation", "24.0 KB", "3 days ago", new File(tempDir, "DSA_Arrays_CheatSheet.md")),
                createFileRow("🖼", "#D97706", "Project_Demo_Screenshot.png", "UI layout preview for dashboard screen", "Java Project", "2.1 MB", "4 days ago", new File(tempDir, "Project_Demo_Screenshot.png"))
        );

        VBox recentCard = new VBox(14, fileRows);
        recentCard.setPadding(new Insets(24));
        recentCard.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + BORDER_CARD + ";" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.18), 16, 0, 0, 6);"
        );

        VBox contentBody = new VBox(22, headerBox, recentCard);
        contentBody.setPadding(new Insets(24, 28, 28, 28));
        contentBody.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");

        ScrollPane scrollPane = new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: " + BG_CENTER_CANVAS + ";" +
                "-fx-background: " + BG_CENTER_CANVAS + ";" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background-color: " + BG_CENTER_CANVAS + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_SIDEBAR + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1200, 750);
    }

    // =========================================================
    // HELPER BUILDERS & ROBUST CLICK-TO-OPEN HANDLER
    // =========================================================

    private StackPane createOneSpaceLogo() {
        Image logoImage = new Image(
                getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png")
        );

        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane logoPane = new StackPane(logoView);
        logoPane.setPrefSize(42, 42);
        logoPane.setAlignment(Pos.CENTER);

        return logoPane;
    }

    private Button createSidebarButton(String icon, String label, boolean isActive) {
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(FONT, 14));

        Label textLbl = new Label(label);
        textLbl.setFont(Font.font(FONT, isActive ? FontWeight.BOLD : FontWeight.MEDIUM, 13));

        HBox content = new HBox(12, iconLbl, textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button("", content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 12, 0, 12));

        if (isActive) {
            btn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");
            textLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;");
            iconLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_LIGHT + ";");
            textLbl.setStyle("-fx-text-fill: " + TEXT_LIGHT + ";");

            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #26354A; -fx-background-radius: 8; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;"));
        }

        return btn;
    }

    private Label createHeaderLabel(String text, double width) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        lbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
        lbl.setPrefWidth(width);
        return lbl;
    }

    private HBox createFileRow(String icon, String iconHex, String fileName, String fileSub, String spaceName, String fileSize, String modifiedTime, File targetFile) {
        Label fileIcon = new Label(icon);
        fileIcon.setFont(Font.font(13));
        fileIcon.setPrefSize(30, 30);
        fileIcon.setAlignment(Pos.CENTER);
        fileIcon.setStyle("-fx-background-color: " + iconHex + "22; -fx-background-radius: 8; -fx-text-fill: " + iconHex + ";");

        Label nameLbl = new Label(fileName);
        nameLbl.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        nameLbl.setStyle("-fx-text-fill: " + TEXT_DARK + ";");

        Label subLbl = new Label(fileSub);
        subLbl.setFont(Font.font(FONT, 11));
        subLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");

        VBox nameStack = new VBox(2, nameLbl, subLbl);
        nameStack.setAlignment(Pos.CENTER_LEFT);

        HBox nameGroup = new HBox(10, fileIcon, nameStack);
        nameGroup.setAlignment(Pos.CENTER_LEFT);
        nameGroup.setPrefWidth(350);

        Label spaceLbl = new Label(spaceName);
        spaceLbl.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        spaceLbl.setStyle("-fx-text-fill: " + PRIMARY_BLUE + "; -fx-background-color: " + BG_CARD_INNER + "; -fx-padding: 3 8; -fx-background-radius: 6;");
        
        HBox spaceGroup = new HBox(spaceLbl);
        spaceGroup.setAlignment(Pos.CENTER_LEFT);
        spaceGroup.setPrefWidth(200);

        Label sizeLbl = new Label(fileSize);
        sizeLbl.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        sizeLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
        sizeLbl.setPrefWidth(100);

        Label timeLbl = new Label(modifiedTime);
        timeLbl.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        timeLbl.setStyle("-fx-text-fill: " + TEXT_MUTED_DARK + ";");
        timeLbl.setPrefWidth(150);

        HBox row = new HBox(nameGroup, spaceGroup, sizeLbl, timeLbl);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 0, 8, 0));
        row.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        // Row hover highlights
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: " + BG_CARD_INNER + "; -fx-background-radius: 8; -fx-cursor: hand;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-cursor: hand;"));

        // Robust Click event: Automatically creates a placeholder file if it doesn't exist yet so Desktop.open() fires successfully
        row.setOnMouseClicked(e -> {
            try {
                if (!targetFile.exists()) {
                    targetFile.createNewFile();
                }
                
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(targetFile);
                } else {
                    System.out.println("Desktop API is not supported on this platform.");
                }
            } catch (IOException ex) {
                System.err.println("Could not open file: " + targetFile.getName());
                ex.printStackTrace();
            }
        });

        return row;
    }
}