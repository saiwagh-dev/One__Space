package com.file_handlers.view.userView;

import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StorageIndexPage {

    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String SIDEBAR_BG = "#070C16", SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BG_INNER = "linear-gradient(to bottom right, rgba(13, 22, 38, 0.9), rgba(8, 14, 26, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";
    private static final String WHITE = "#FFFFFF", LIGHT_SECONDARY = "#94A3B8", BLUE = "#2563EB";

    private final File oneSpace = new File(System.getProperty("user.home"), "OneSpace");

    public Scene getStorageIndexPageScene() {
        BorderPane root = new BorderPane();
        root.setCenter(createMainArea());
        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private VBox createMainArea() {
        VBox main = new VBox(createTopBar(), createStorageContent());
        main.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        return main;
    }

    private HBox createTopBar() {
        Button backButton = new Button("←   Dashboard");
        backButton.setPrefHeight(38);
        backButton.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-text-fill: " + WHITE + "; -fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 16; -fx-cursor: hand;");
        backButton.setOnAction(e -> LandingPage.showUserDashboard());
        backButton.setOnMouseEntered(e -> {
            backButton.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-text-fill: #38BDF8; -fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #38BDF8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 16; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.3), 8, 0, 0, 0);");
            ScaleTransition st = new ScaleTransition(Duration.millis(140), backButton);
            st.setToX(1.05); st.setToY(1.05); st.play();
        });
        backButton.setOnMouseExited(e -> {
            backButton.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-text-fill: " + WHITE + "; -fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 16; -fx-cursor: hand;");
            ScaleTransition st = new ScaleTransition(Duration.millis(140), backButton);
            st.setToX(1.0); st.setToY(1.0); st.play();
        });

        String activeUserName = "User", initials = "U";
        if (UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null && !UserSession.getInstance().getDisplayName().isBlank()) {
            activeUserName = UserSession.getInstance().getDisplayName().trim().split("\\s+")[0];
            initials = activeUserName.substring(0, 1).toUpperCase();
        }

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button bellBtn = new Button("", bellIcon);
        bellBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());
        bellBtn.setOnMouseEntered(e -> {
            bellBtn.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-border-color: #38BDF8; -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.3), 8, 0, 0, 0);");
            bellIcon.setStroke(Color.web("#38BDF8"));
            ScaleTransition st = new ScaleTransition(Duration.millis(140), bellBtn);
            st.setToX(1.08); st.setToY(1.08); st.play();
        });
        bellBtn.setOnMouseExited(e -> {
            bellBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
            bellIcon.setStroke(Color.WHITE);
            ScaleTransition st = new ScaleTransition(Duration.millis(140), bellBtn);
            st.setToX(1.0); st.setToY(1.0); st.play();
        });

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34); avatar.setMinSize(34, 34); avatar.setMaxSize(34, 34); avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12)); avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");
        applyHoverAnimation(avatar, 1.15, 0);

        Label userLabel = label(activeUserName, 13, FontWeight.SEMI_BOLD, WHITE);
        Label dropDown = label("⌄", 12, FontWeight.NORMAL, LIGHT_SECONDARY);

        HBox profileOption = new HBox(8, avatar, userLabel, dropDown);
        profileOption.setAlignment(Pos.CENTER); profileOption.setPadding(new Insets(4, 12, 4, 6));
        profileOption.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");
        profileOption.setOnMouseEntered(e -> {
            profileOption.setStyle("-fx-background-color: rgba(56, 189, 248, 0.12); -fx-border-color: #38BDF8; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.25), 8, 0, 0, 0);");
            userLabel.setTextFill(Color.web("#38BDF8"));
            ScaleTransition st = new ScaleTransition(Duration.millis(140), profileOption);
            st.setToX(1.04); st.setToY(1.04); st.play();
        });
        profileOption.setOnMouseExited(e -> {
            profileOption.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");
            userLabel.setTextFill(Color.web(WHITE));
            ScaleTransition st = new ScaleTransition(Duration.millis(140), profileOption);
            st.setToX(1.0); st.setToY(1.0); st.play();
        });

        Popup userDropdownPopup = new Popup(); userDropdownPopup.setAutoHide(true);
        Button profileDropdownBtn = createDropdownBtn("👥   Profile", "#F59E0B", e -> { userDropdownPopup.hide(); Platform.runLater(LandingPage::showUserProfilePage); });
        Button settingsDropdownBtn = createDropdownBtn("⚙   Settings", "#38BDF8", e -> { userDropdownPopup.hide(); Platform.runLater(LandingPage::showSettingPage); });
        Button logoutDropdownBtn = createDropdownBtn("↳   Logout", "#F87171", e -> { userDropdownPopup.hide(); UserSession.clearSession(); Platform.runLater(LandingPage::showUserLoginPage); });

        Separator dropdownSeparator = new Separator(); dropdownSeparator.setStyle("-fx-background-color: #1E293B; -fx-padding: 4 0;");
        VBox dropdownContainer = new VBox(4, profileDropdownBtn, settingsDropdownBtn, dropdownSeparator, logoutDropdownBtn);
        dropdownContainer.setPadding(new Insets(8)); dropdownContainer.setPrefWidth(180);
        dropdownContainer.setStyle("-fx-background-color: #0A121E; -fx-border-color: #1E2D42; -fx-border-width: 1px; -fx-border-radius: 12px; -fx-background-radius: 12px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 16, 0, 0, 8);");
        userDropdownPopup.getContent().add(dropdownContainer);

        profileOption.setOnMouseClicked(e -> {
            if (userDropdownPopup.isShowing()) userDropdownPopup.hide();
            else userDropdownPopup.show(profileOption, profileOption.localToScreen(0, profileOption.getHeight() + 6).getX(), profileOption.localToScreen(0, profileOption.getHeight() + 6).getY());
        });

        HBox topBar = new HBox(20, backButton, new Region(), new HBox(10, bellBtn, profileOption));
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return topBar;
    }

    private Button createDropdownBtn(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> act) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE); b.setAlignment(Pos.CENTER_LEFT);
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + color + "; -fx-font-size: 14px; -fx-font-family: " + FONT + "; -fx-padding: 8 12; -fx-cursor: hand;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #1E293B; -fx-text-fill: " + color + "; -fx-font-size: 14px; -fx-font-family: " + FONT + "; -fx-padding: 8 12; -fx-cursor: hand; -fx-background-radius: 6;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + color + "; -fx-font-size: 14px; -fx-font-family: " + FONT + "; -fx-padding: 8 12; -fx-cursor: hand;"));
        b.setOnAction(act);
        return b;
    }

    private void applyHoverAnimation(Node node, double scaleTo, double translateY) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
            st.setToX(scaleTo); st.setToY(scaleTo); st.play();
            if (translateY != 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(140), node);
                tt.setToY(translateY); tt.play();
            }
        });
        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
            st.setToX(1.0); st.setToY(1.0); st.play();
            if (translateY != 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(140), node);
                tt.setToY(0); tt.play();
            }
        });
    }

    private ScrollPane createStorageContent() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(28, ResponsiveUtil.PAGE_PADDING, 30, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color: transparent;");

        long oneSpaceSize = folderSize(oneSpace);
        File drive = getInternalDrive();
        long totalPC = drive.getTotalSpace(), usedPC = totalPC - drive.getFreeSpace();
        double oneSpaceOfTotal = totalPC == 0 ? 0 : oneSpaceSize * 100.0 / totalPC;
        double oneSpaceOfUsed = usedPC == 0 ? 0 : oneSpaceSize * 100.0 / usedPC;

        Button refresh = blueButton("⟳   Refresh");
        refresh.setOnAction(e -> LandingPage.showStorageIndexPage());
        applyHoverAnimation(refresh, 1.05, 0);

        HBox header = new HBox(new VBox(5, label("Storage Index", 26, true, WHITE), label("Monitor the storage occupied by your OneSpace files and your PC storage.", 13, false, LIGHT_SECONDARY)), new Region(), refresh);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);

        HBox twoCards = new HBox(14, createStorageBySpace(oneSpaceSize), createPCStorage(oneSpaceSize, totalPC, usedPC, drive));
        HBox.setHgrow(twoCards.getChildren().get(0), Priority.ALWAYS); HBox.setHgrow(twoCards.getChildren().get(1), Priority.ALWAYS);

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08);");

        HBox footer = new HBox(label("Storage data is calculated from your system.", 11, false, LIGHT_SECONDARY), new Region(), label("Actual OneSpace storage • Live system data", 11, false, LIGHT_SECONDARY));
        HBox.setHgrow(footer.getChildren().get(1), Priority.ALWAYS);

        content.getChildren().addAll(header, createUsageCard(oneSpaceSize, oneSpaceOfTotal, oneSpaceOfUsed), twoCards, createFileActivity(oneSpaceSize), createSummary(oneSpace), separator, footer);
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true); scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        return scroll;
    }

    private HBox createUsageCard(long size, double totalPercent, double usedPercent) {
        SVGPath folderIcon = createIcon("files");
        folderIcon.setStroke(Color.web("#34D399")); folderIcon.setStrokeWidth(2.2);
        StackPane icon = new StackPane(folderIcon); icon.setPrefSize(76, 76);
        icon.setStyle("-fx-background-color: rgba(16, 185, 129, 0.15); -fx-border-color: rgba(16, 185, 129, 0.3); -fx-border-radius: 50%; -fx-background-radius: 50%;");

        VBox right = new VBox(3, label(String.format("%.2f%%", totalPercent), 27, true, "#34D399"), label("of total PC storage", 12, false, LIGHT_SECONDARY), label(String.format("%.2f%% of currently used space", usedPercent), 11, false, LIGHT_SECONDARY));
        right.setAlignment(Pos.CENTER_RIGHT);

        HBox card = new HBox(18, icon, new VBox(3, label("OneSpace Usage", 14, true, LIGHT_SECONDARY), label(format(size), 31, true, WHITE), label("Actual space occupied by OneSpace files", 12, false, LIGHT_SECONDARY)), new Region(), right);
        HBox.setHgrow(card.getChildren().get(2), Priority.ALWAYS);
        card.setAlignment(Pos.CENTER_LEFT); card.setPadding(new Insets(20, 24, 20, 24)); card.setStyle(cardStyle());
        
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.9), rgba(12, 22, 40, 0.95)); -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 24, 0, 0, 6);");
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.01); st.setToY(1.01); st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(-2); tt.play();
        });
        card.setOnMouseExited(e -> {
            card.setStyle(cardStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.0); st.setToY(1.0); st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(0); tt.play();
        });
        return card;
    }

    private VBox createStorageBySpace(long total) {
        VBox rows = new VBox(11);
        String[] spaces = {"Personal", "College", "Office", "Finance", "Entertainment", "Others"};
        for (String name : spaces) {
            long size = folderSize(new File(oneSpace, name));
            rows.getChildren().add(createSpaceRow(name, size, total == 0 ? 0 : size * 100.0 / total));
        }
        VBox card = new VBox(10, label("Storage by Space", 17, true, WHITE), label("Space used by each OneSpace category.", 12, false, LIGHT_SECONDARY), new Separator(), rows);
        card.setPadding(new Insets(18)); card.setStyle(cardStyle());
        
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.9), rgba(12, 22, 40, 0.95)); -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 24, 0, 0, 6);");
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.01); st.setToY(1.01); st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(-2); tt.play();
        });
        card.setOnMouseExited(e -> {
            card.setStyle(cardStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.0); st.setToY(1.0); st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(0); tt.play();
        });
        return card;
    }

    private HBox createSpaceRow(String name, long size, double percent) {
        SVGPath dotIcon = createIcon("bullet");
        dotIcon.setStroke(Color.web("#38BDF8")); dotIcon.setStrokeWidth(3);

        ProgressBar progress = new ProgressBar(Math.min(percent / 100, 1));
        progress.setPrefWidth(100); progress.setPrefHeight(7);
        progress.setStyle("-fx-accent: " + BLUE + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        HBox row = new HBox(8, dotIcon, label(name, 12, true, WHITE), label(format(size), 12, true, WHITE), new Region(), progress, label(String.format("%.1f%%", percent), 11, true, LIGHT_SECONDARY));
        HBox.setHgrow(row.getChildren().get(3), Priority.ALWAYS);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(4, 6, 4, 6));
        row.setStyle("-fx-background-color: transparent; -fx-background-radius: 6; -fx-cursor: hand;");
        row.setOnMouseEntered(e -> {
            row.setStyle("-fx-background-color: rgba(56, 189, 248, 0.1); -fx-background-radius: 6; -fx-cursor: hand;");
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), row);
            tt.setToX(3); tt.play();
        });
        row.setOnMouseExited(e -> {
            row.setStyle("-fx-background-color: transparent; -fx-background-radius: 6; -fx-cursor: hand;");
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), row);
            tt.setToX(0); tt.play();
        });
        return row;
    }

    private VBox createPCStorage(long oneSpaceSize, long total, long used, File drive) {
        VBox card = new VBox(11, label("PC Storage Info", 17, true, WHITE), label("Current internal drive information.", 12, false, LIGHT_SECONDARY), new Separator(),
                infoRow("Drive", drive.getAbsolutePath()), infoRow("Total Capacity", format(total)), infoRow("Used", format(used) + " (" + String.format("%.2f%%", total == 0 ? 0 : used * 100.0 / total) + ")"), infoRow("Available", format(drive.getFreeSpace())), infoRow("OneSpace Usage", format(oneSpaceSize) + " (" + String.format("%.2f%%", total == 0 ? 0 : oneSpaceSize * 100.0 / total) + ")"));
        card.setPadding(new Insets(18)); card.setStyle(cardStyle());
        
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.9), rgba(12, 22, 40, 0.95)); -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 24, 0, 0, 6);");
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.01); st.setToY(1.01); st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(-2); tt.play();
        });
        card.setOnMouseExited(e -> {
            card.setStyle(cardStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.0); st.setToY(1.0); st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(0); tt.play();
        });
        return card;
    }

    private HBox infoRow(String name, String value) {
        HBox row = new HBox(label(name, 12, true, LIGHT_SECONDARY), new Region(), label(value, 12, true, WHITE));
        HBox.setHgrow(row.getChildren().get(1), Priority.ALWAYS);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private VBox createFileActivity(long totalSize) {
        List<File> files = getFiles(oneSpace);
        files.sort(Comparator.comparingLong(File::length).reversed());
        VBox rows = new VBox(6);
        for (File f : files) {
            if (rows.getChildren().size() >= 8) break;
            rows.getChildren().add(createFileRow(f, totalSize == 0 ? 0 : f.length() * 100.0 / totalSize));
        }
        if (files.isEmpty()) rows.getChildren().add(label("No files are currently stored in OneSpace.", 12, false, LIGHT_SECONDARY));
        VBox card = new VBox(10, label("Files Occupying Storage", 17, true, WHITE), label("Files currently stored in OneSpace, sorted by size.", 12, false, LIGHT_SECONDARY), new Separator(), rows);
        card.setPadding(new Insets(18)); card.setStyle(cardStyle());
        
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.9), rgba(12, 22, 40, 0.95)); -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 24, 0, 0, 6);");
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.01); st.setToY(1.01); st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(-2); tt.play();
        });
        card.setOnMouseExited(e -> {
            card.setStyle(cardStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.0); st.setToY(1.0); st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(0); tt.play();
        });
        return card;
    }

    private HBox createFileRow(File file, double percent) {
        SVGPath fileIcon = createIcon("files");
        fileIcon.setStroke(Color.web("#38BDF8")); fileIcon.setStrokeWidth(2);

        ProgressBar progress = new ProgressBar(Math.min(percent / 100, 1));
        progress.setPrefWidth(150); progress.setPrefHeight(7);
        progress.setStyle("-fx-accent: " + BLUE + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        HBox row = new HBox(10, fileIcon, label(file.getName(), 12, true, WHITE), new Region(), progress, label(format(file.length()), 12, true, WHITE));
        HBox.setHgrow(row.getChildren().get(2), Priority.ALWAYS);
        row.setAlignment(Pos.CENTER_LEFT); row.setPadding(new Insets(8, 10, 8, 10));
        row.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        row.setOnMouseEntered(e -> {
            row.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(56, 189, 248, 0.45); -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.25), 8, 0, 0, 0);");
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), row);
            tt.setToX(4); tt.play();
        });
        row.setOnMouseExited(e -> {
            row.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-radius: 8; -fx-background-radius: 8;");
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), row);
            tt.setToX(0); tt.play();
        });
        return row;
    }

    private HBox createSummary(File folder) {
        List<File> files = getFiles(folder);
        long total = folderSize(folder);
        long largest = files.isEmpty() ? 0 : files.stream().mapToLong(File::length).max().orElse(0);
        long smallest = files.isEmpty() ? 0 : files.stream().mapToLong(File::length).min().orElse(0);
        long average = files.isEmpty() ? 0 : total / files.size();

        return new HBox(12, stat(String.valueOf(files.size()), "Total Files"), stat(format(total), "Total OneSpace Storage"), stat(format(largest), "Largest File"), stat(format(smallest), "Smallest File"), stat(format(average), "Average File Size"));
    }

    private VBox stat(String value, String title) {
        VBox box = new VBox(4, label(value, 19, true, "#38BDF8"), label(title, 11, false, LIGHT_SECONDARY));
        box.setPadding(new Insets(13)); box.setStyle(cardStyle()); HBox.setHgrow(box, Priority.ALWAYS);
        box.setOnMouseEntered(e -> {
            box.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.9), rgba(12, 22, 40, 0.95)); -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 20, 0, 0, 4);");
            ScaleTransition st = new ScaleTransition(Duration.millis(140), box);
            st.setToX(1.03); st.setToY(1.03); st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), box);
            tt.setToY(-2); tt.play();
        });
        box.setOnMouseExited(e -> {
            box.setStyle(cardStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(140), box);
            st.setToX(1.0); st.setToY(1.0); st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), box);
            tt.setToY(0); tt.play();
        });
        return box;
    }

    private Button blueButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(42);
        button.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: #FFFFFF; -fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 10; -fx-border-width: 1; -fx-padding: 0 18; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.45), 10, 0, 0, 2);");
        return button;
    }

    private long folderSize(File folder) {
        if (!folder.exists()) return 0;
        if (folder.isFile()) return folder.length();
        long total = 0; File[] files = folder.listFiles();
        if (files != null) for (File f : files) total += folderSize(f);
        return total;
    }

    private List<File> getFiles(File folder) {
        List<File> result = new ArrayList<>();
        if (!folder.exists()) return result;
        File[] files = folder.listFiles();
        if (files != null) for (File f : files) if (f.isFile()) result.add(f); else result.addAll(getFiles(f));
        return result;
    }

    private File getInternalDrive() {
        String systemDrive = System.getenv("SystemDrive");
        return systemDrive != null ? new File(systemDrive + "\\") : new File("/");
    }

    private String format(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1048576) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1073741824L) return String.format("%.2f MB", bytes / 1048576.0);
        return String.format("%.2f GB", bytes / 1073741824.0);
    }

    private Label label(String text, double size, boolean bold, String color) {
        return label(text, size, bold ? FontWeight.BOLD : FontWeight.NORMAL, color);
    }

    private Label label(String text, double size, FontWeight weight, String color) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, weight, size));
        label.setTextFill(Color.web(color));
        return label;
    }

    private String cardStyle() {
        return "-fx-background-color: " + CARD_BG + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24,0,0,10);";
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath(); icon.setFill(Color.TRANSPARENT); icon.setStrokeWidth(2);
        switch (type) {
            case "files": icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13"); break;
            case "bell": icon.setContent("M6 17 H18 M8 17 V10 A4 4 0 0 1 16 10 V17 M10 20 H14"); break;
            case "bullet": icon.setContent("M12 12m-3 0a3 3 0 1 0 6 0a3 3 0 1 0 -6 0"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }
}