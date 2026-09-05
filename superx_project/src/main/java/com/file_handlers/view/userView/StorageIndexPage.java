package com.file_handlers.view.userView;

import com.file_handlers.model.UserSession;
import com.file_handlers.model.FileData;
import com.file_handlers.dao.FileDAO;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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
        VBox content = new VBox(20);
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 30, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color: transparent;");

        Label loading = label("Loading storage information...", 13, false, LIGHT_SECONDARY);
        content.getChildren().add(loading);

        Button refresh = blueButton("⟳   Refresh");
        refresh.setOnAction(e -> LandingPage.showStorageIndexPage());

        HBox header = new HBox(
                new VBox(
                        5,
                        label("Storage Index", 26, true, WHITE),
                        label("Monitor the storage occupied by your OneSpace files and your PC storage.", 13, false, LIGHT_SECONDARY)
                ),
                new Region(),
                refresh
        );
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);

        content.getChildren().add(0, header);

        Thread loader = new Thread(() -> {
            try {
                UserSession session = UserSession.getInstance();

                if (session == null || session.getUid() == null || session.getUid().isBlank()) {
                    throw new IllegalStateException("No authenticated user session.");
                }

                FileDAO fileDAO = new FileDAO();
                List<FileData> files = fileDAO.getFileSummaries(session.getUid());

                StorageData storageData = buildStorageData(files);

                Platform.runLater(() -> {
                    content.getChildren().clear();

                    content.getChildren().addAll(
                            header,
                            createUsageCard(
                                    storageData.totalSize,
                                    storageData.oneSpaceOfTotal,
                                    storageData.oneSpaceOfUsed
                            ),
                            createStorageBySpace(
                                    storageData.spaceSizes,
                                    storageData.totalSize
                            ),
                            createPCStorage(
                                    storageData.totalSize,
                                    storageData.totalPC,
                                    storageData.usedPC,
                                    storageData.drive
                            ),
                            createFileActivity(
                                    storageData.files,
                                    storageData.totalSize
                            ),
                            createSummary(
                                    storageData.files,
                                    storageData.totalSize
                            ),
                            createSeparatorFooter()
                    );
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    content.getChildren().clear();
                    content.getChildren().addAll(
                            header,
                            label("Unable to load storage information: " + e.getMessage(), 13, false, "#F87171")
                    );
                });
            }
        });

        loader.setDaemon(true);
        loader.start();

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        return scroll;
    }

    private StorageData buildStorageData(List<FileData> files) {
        File drive = getInternalDrive();
        long totalPC = drive.getTotalSpace();
        long usedPC = totalPC - drive.getFreeSpace();

        List<StorageFile> storageFiles = new ArrayList<>();
        long totalSize = 0;

        for (FileData fileData : files) {
            if (fileData == null) {
                continue;
            }

            long size = fileData.getFileSize();

            String localPath = fileData.getLocalPath();
            if (localPath != null && !localPath.isBlank()) {
                File localFile = new File(localPath);
                if (localFile.exists() && localFile.isFile()) {
                    size = localFile.length();
                }
            }

            if (size < 0) {
                size = 0;
            }

            totalSize += size;
            storageFiles.add(new StorageFile(fileData, size));
        }

        storageFiles.sort(
                Comparator.comparingLong(
                        StorageFile::getSize
                ).reversed()
        );

        java.util.Map<String, Long> spaceSizes = new java.util.LinkedHashMap<>();
        spaceSizes.put("Personal", 0L);
        spaceSizes.put("College", 0L);
        spaceSizes.put("Office", 0L);
        spaceSizes.put("Finance", 0L);
        spaceSizes.put("Entertainment", 0L);
        spaceSizes.put("Others", 0L);

        for (StorageFile storageFile : storageFiles) {
            String spaceId = storageFile.getFile().getSpaceId();
            String spaceName = switch (spaceId == null ? "" : spaceId.toLowerCase()) {
                case "personal" -> "Personal";
                case "college" -> "College";
                case "office" -> "Office";
                case "finance" -> "Finance";
                case "entertainment" -> "Entertainment";
                default -> "Others";
            };

            spaceSizes.put(
                    spaceName,
                    spaceSizes.get(spaceName) + storageFile.getSize()
            );
        }

        double oneSpaceOfTotal =
                totalPC == 0 ? 0 : totalSize * 100.0 / totalPC;

        double oneSpaceOfUsed =
                usedPC == 0 ? 0 : totalSize * 100.0 / usedPC;

        return new StorageData(
                storageFiles,
                totalSize,
                totalPC,
                usedPC,
                drive,
                oneSpaceOfTotal,
                oneSpaceOfUsed,
                spaceSizes
        );
    }

    private VBox createSeparatorFooter() {
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08);");

        HBox footer = new HBox(
                label("Storage data is calculated from your OneSpace files and system.", 11, false, LIGHT_SECONDARY),
                new Region(),
                label("Actual OneSpace storage • Live system data", 11, false, LIGHT_SECONDARY)
        );
        HBox.setHgrow(footer.getChildren().get(1), Priority.ALWAYS);

        VBox wrapper = new VBox(8, separator, footer);
        return wrapper;
    }

    private HBox createUsageCard(long size, double totalPercent, double usedPercent) {
        SVGPath folderIcon = createIcon("files");
        folderIcon.setStroke(Color.web("#34D399")); folderIcon.setStrokeWidth(2.2);
        StackPane icon = new StackPane(folderIcon); icon.setPrefSize(72, 72);
        icon.setStyle("-fx-background-color: rgba(16, 185, 129, 0.15); -fx-border-color: rgba(16, 185, 129, 0.35); -fx-border-radius: 50%; -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(16, 185, 129, 0.3), 12, 0, 0, 0);");

        VBox right = new VBox(4, label(String.format("%.2f%%", totalPercent), 28, true, "#34D399"), label("of total PC storage", 12, false, LIGHT_SECONDARY), label(String.format("%.2f%% of currently used space", usedPercent), 11, false, LIGHT_SECONDARY));
        right.setAlignment(Pos.CENTER_RIGHT);

        ProgressBar dynamicBar = new ProgressBar(Math.min(totalPercent / 100.0, 1.0));
        dynamicBar.setPrefWidth(220); dynamicBar.setPrefHeight(8);
        dynamicBar.setStyle("-fx-accent: #10B981; -fx-control-inner-background: rgba(10, 18, 33, 0.85); -fx-background-radius: 10; -fx-border-radius: 10;");

        VBox centerBox = new VBox(6, label("OneSpace Usage", 14, true, LIGHT_SECONDARY), label(format(size), 32, true, WHITE), dynamicBar, label("Actual space occupied by OneSpace files", 12, false, LIGHT_SECONDARY));
        centerBox.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(22, icon, centerBox, new Region(), right);
        HBox.setHgrow(card.getChildren().get(2), Priority.ALWAYS);
        card.setAlignment(Pos.CENTER_LEFT); card.setPadding(new Insets(22, 26, 22, 26)); card.setStyle(cardStyle());
        
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.95), rgba(12, 22, 40, 0.98)); -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 24, 0, 0, 6);");
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

    private VBox createStorageBySpace(java.util.Map<String, Long> spaceSizes, long total) {
        VBox rows = new VBox(10);

        String[] spaces = {
                "Personal",
                "College",
                "Office",
                "Finance",
                "Entertainment",
                "Others"
        };

        for (String name : spaces) {
            long size = spaceSizes.getOrDefault(name, 0L);
            double percent = total == 0 ? 0 : size * 100.0 / total;

            HBox row = createSpaceRow(
                    name,
                    size,
                    percent
            );

            rows.getChildren().add(row);
        }

        VBox card = new VBox(
                12,
                label("Storage by Space", 17, true, WHITE),
                label("Space used by each OneSpace category.", 12, false, LIGHT_SECONDARY),
                new Separator(),
                rows
        );

        card.setPadding(new Insets(20));
        card.setStyle(cardStyle());

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.95), rgba(12, 22, 40, 0.98)); -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 24, 0, 0, 6);");
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.01);
            st.setToY(1.01);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(-2);
            tt.play();
        });

        card.setOnMouseExited(e -> {
            card.setStyle(cardStyle());

            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(0);
            tt.play();
        });

        return card;
    }

    private HBox createSpaceRow(String name, long size, double percent) {
        String themeColor;
        switch (name) {
            case "Personal": themeColor = "#38BDF8"; break;
            case "College": themeColor = "#A855F7"; break;
            case "Office": themeColor = "#3B82F6"; break;
            case "Finance": themeColor = "#10B981"; break;
            case "Entertainment": themeColor = "#F59E0B"; break;
            default: themeColor = "#EC4899"; break;
        }

        Circle dot = new Circle(4, Color.web(themeColor));
        ProgressBar progress = new ProgressBar(Math.min(percent / 100.0, 1.0));
        progress.setPrefWidth(120); progress.setPrefHeight(8);
        progress.setStyle("-fx-accent: " + themeColor + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85); -fx-background-radius: 8; -fx-border-radius: 8;");

        HBox nameAndSize = new HBox(8, dot, label(name, 12, true, WHITE), label(format(size), 12, FontWeight.MEDIUM, LIGHT_SECONDARY));
        nameAndSize.setAlignment(Pos.CENTER_LEFT);

        HBox row = new HBox(12, nameAndSize, new Region(), progress, label(String.format("%.1f%%", percent), 11, true, WHITE));
        HBox.setHgrow(row.getChildren().get(1), Priority.ALWAYS);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 10, 6, 10));
        row.setStyle("-fx-background-color: rgba(10, 18, 33, 0.6); -fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        
        row.setOnMouseEntered(e -> {
            row.setStyle("-fx-background-color: rgba(56, 189, 248, 0.12); -fx-border-color: " + themeColor + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, " + themeColor + "44, 8, 0, 0, 2);");
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), row);
            tt.setToX(4); tt.play();
        });
        row.setOnMouseExited(e -> {
            row.setStyle("-fx-background-color: rgba(10, 18, 33, 0.6); -fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-radius: 8; -fx-background-radius: 8;");
            TranslateTransition tt = new TranslateTransition(Duration.millis(120), row);
            tt.setToX(0); tt.play();
        });
        return row;
    }

    private VBox createPCStorage(long oneSpaceSize, long total, long used, File drive) {
        double usedPercentage = total == 0 ? 0 : used * 100.0 / total;
        ProgressBar driveProgress = new ProgressBar(Math.min(usedPercentage / 100.0, 1.0));
        driveProgress.setPrefWidth(220); driveProgress.setPrefHeight(8);
        driveProgress.setStyle("-fx-accent: " + (usedPercentage > 85 ? "#EF4444" : "#38BDF8") + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85); -fx-background-radius: 8; -fx-border-radius: 8;");

        HBox driveProgressRow = new HBox(12, label("Disk Capacity Usage", 12, true, LIGHT_SECONDARY), new Region(), driveProgress, label(String.format("%.1f%%", usedPercentage), 11, true, WHITE));
        HBox.setHgrow(driveProgressRow.getChildren().get(1), Priority.ALWAYS);
        driveProgressRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(11, 
                label("PC Storage Info", 17, true, WHITE), 
                label("Current internal drive information.", 12, false, LIGHT_SECONDARY), 
                new Separator(),
                infoRow("Drive", drive.getAbsolutePath()), 
                infoRow("Total Capacity", format(total)), 
                infoRow("Used", format(used) + " (" + String.format("%.2f%%", usedPercentage) + ")"), 
                infoRow("Available", format(drive.getFreeSpace())), 
                infoRow("OneSpace Usage", format(oneSpaceSize) + " (" + String.format("%.2f%%", total == 0 ? 0 : oneSpaceSize * 100.0 / total) + ")"),
                new Separator(),
                driveProgressRow
        );
        card.setPadding(new Insets(20)); card.setStyle(cardStyle());
        
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.95), rgba(12, 22, 40, 0.98)); -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 24, 0, 0, 6);");
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

    private VBox createFileActivity(List<StorageFile> files, long totalSize) {
        VBox rows = new VBox(8);

        for (StorageFile storageFile : files) {
            if (rows.getChildren().size() >= 8) {
                break;
            }

            double percent =
                    totalSize == 0
                            ? 0
                            : storageFile.getSize() * 100.0 / totalSize;

            rows.getChildren().add(
                    createFileRow(
                            storageFile.getFile(),
                            storageFile.getSize(),
                            percent
                    )
            );
        }

        if (files.isEmpty()) {
            rows.getChildren().add(
                    label(
                            "No files are currently stored in OneSpace.",
                            12,
                            false,
                            LIGHT_SECONDARY
                    )
            );
        }

        VBox card = new VBox(
                12,
                label("Files Occupying Storage", 17, true, WHITE),
                label("Files currently stored in OneSpace, sorted by size.", 12, false, LIGHT_SECONDARY),
                new Separator(),
                rows
        );

        card.setPadding(new Insets(20));
        card.setStyle(cardStyle());

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.95), rgba(12, 22, 40, 0.98)); -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 24, 0, 0, 6);");

            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.01);
            st.setToY(1.01);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(-2);
            tt.play();
        });

        card.setOnMouseExited(e -> {
            card.setStyle(cardStyle());

            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(0);
            tt.play();
        });

        return card;
    }

    private HBox createFileRow(
            FileData fileData,
            long size,
            double percent
    ) {
        SVGPath fileIcon = createIcon("files");
        fileIcon.setStroke(Color.web("#38BDF8"));
        fileIcon.setStrokeWidth(2);

        ProgressBar progress =
                new ProgressBar(
                        Math.min(percent / 100.0, 1.0)
                );

        progress.setPrefWidth(160);
        progress.setPrefHeight(8);
        progress.setStyle(
                "-fx-accent: " + BLUE +
                "; -fx-control-inner-background: rgba(13, 22, 38, 0.85); -fx-background-radius: 8; -fx-border-radius: 8;"
        );

        Label fileName =
                label(
                        fileData.getFileName() == null
                                ? "Unknown file"
                                : fileData.getFileName(),
                        12,
                        true,
                        WHITE
                );

        HBox row =
                new HBox(
                        12,
                        fileIcon,
                        fileName,
                        new Region(),
                        progress,
                        label(format(size), 12, true, WHITE)
                );

        HBox.setHgrow(
                row.getChildren().get(2),
                Priority.ALWAYS
        );

        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(
                new Insets(10, 12, 10, 12)
        );

        row.setStyle(
                "-fx-background-color: " + CARD_BG_INNER +
                "; -fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand;"
        );

        row.setOnMouseClicked(e -> {
            String path = fileData.getLocalPath();

            if (path == null || path.isBlank()) {
                return;
            }

            try {
                File localFile = new File(path);

                if (Desktop.isDesktopSupported() &&
                        localFile.exists()) {
                    Desktop.getDesktop().open(localFile);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        row.setOnMouseEntered(e -> {
            row.setStyle(
                    "-fx-background-color: " + CARD_BG_INNER +
                    "; -fx-border-color: rgba(56, 189, 248, 0.5); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.25), 8, 0, 0, 2);"
            );

            TranslateTransition tt =
                    new TranslateTransition(
                            Duration.millis(120),
                            row
                    );

            tt.setToX(4);
            tt.play();
        });

        row.setOnMouseExited(e -> {
            row.setStyle(
                    "-fx-background-color: " + CARD_BG_INNER +
                    "; -fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-radius: 10; -fx-background-radius: 10;"
            );

            TranslateTransition tt =
                    new TranslateTransition(
                            Duration.millis(120),
                            row
                    );

            tt.setToX(0);
            tt.play();
        });

        return row;
    }

    private HBox createSummary(
            List<StorageFile> files,
            long total
    ) {
        long largest =
                files.isEmpty()
                        ? 0
                        : files.stream()
                                .mapToLong(
                                        StorageFile::getSize
                                )
                                .max()
                                .orElse(0);

        long smallest =
                files.isEmpty()
                        ? 0
                        : files.stream()
                                .mapToLong(
                                        StorageFile::getSize
                                )
                                .min()
                                .orElse(0);

        long average =
                files.isEmpty()
                        ? 0
                        : total / files.size();

        return new HBox(
                12,
                stat(
                        String.valueOf(files.size()),
                        "Total Files"
                ),
                stat(
                        format(total),
                        "Total OneSpace Storage"
                ),
                stat(
                        format(largest),
                        "Largest File"
                ),
                stat(
                        format(smallest),
                        "Smallest File"
                ),
                stat(
                        format(average),
                        "Average File Size"
                )
        );
    }

    private static class StorageFile {
        private final FileData file;
        private final long size;

        StorageFile(FileData file, long size) {
            this.file = file;
            this.size = size;
        }

        FileData getFile() {
            return file;
        }

        long getSize() {
            return size;
        }
    }

    private static class StorageData {
        private final List<StorageFile> files;
        private final long totalSize;
        private final long totalPC;
        private final long usedPC;
        private final File drive;
        private final double oneSpaceOfTotal;
        private final double oneSpaceOfUsed;
        private final java.util.Map<String, Long> spaceSizes;

        StorageData(
                List<StorageFile> files,
                long totalSize,
                long totalPC,
                long usedPC,
                File drive,
                double oneSpaceOfTotal,
                double oneSpaceOfUsed,
                java.util.Map<String, Long> spaceSizes
        ) {
            this.files = files;
            this.totalSize = totalSize;
            this.totalPC = totalPC;
            this.usedPC = usedPC;
            this.drive = drive;
            this.oneSpaceOfTotal = oneSpaceOfTotal;
            this.oneSpaceOfUsed = oneSpaceOfUsed;
            this.spaceSizes = spaceSizes;
        }
    }

    private VBox stat(String value, String title) {
        VBox box = new VBox(4, label(value, 19, true, "#38BDF8"), label(title, 11, false, LIGHT_SECONDARY));
        box.setPadding(new Insets(14)); box.setStyle(cardStyle()); HBox.setHgrow(box, Priority.ALWAYS);
        box.setOnMouseEntered(e -> {
            box.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(23, 40, 68, 0.95), rgba(12, 22, 40, 0.98)); -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 20, 0, 0, 4);");
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
        button.setPrefHeight(40);
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