package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UserSettingPage {

        // =========================================================
        // ONESPACE DASHBOARD THEME
        // =========================================================

        private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

        // Main backgrounds
        private static final String BG_APP = "#3B4B61";
        private static final String BG_CARD = "#D9E2EF";

        // Sidebar / Topbar
        private static final String SIDEBAR_BG = "#263447";
        private static final String TOPBAR_BG = "#263447";

        private static final String SIDEBAR_HOVER = "#314158";
        private static final String SIDEBAR_ACTIVE = "#3567C7";

        // Borders
        private static final String BORDER_COLOR = "#B8C7D9";

        // Primary colors
        private static final String PRIMARY_BLUE = "#3567C7";
        private static final String PRIMARY_LIGHT = "#C9DAF1";
        private static final String PRIMARY_DARK = "#274F9E";

        // Accent colors
        private static final String SKY_BLUE = "#2D8CC4";
        private static final String EMERALD = "#208C75";
        private static final String AMBER = "#D99419";

        // Text
        private static final String TEXT_MAIN = "#243247";
        private static final String TEXT_MUTED = "#617189";

        // Sidebar text
        private static final String SIDEBAR_TEXT = "#E8EDF5";
        private static final String SIDEBAR_MUTED = "#A9B8CB";

        public Scene getSettingPageScene() {

                // =========================================================
                // LEFT SIDEBAR
                // =========================================================

                Label logoIcon = new Label("◉");

                logoIcon.setFont(
                                Font.font(FONT, FontWeight.BOLD, 22));

                logoIcon.setTextFill(
                                Color.web("#58B7E8"));

                Label logoText = new Label("OneSpace");

                logoText.setFont(
                                Font.font(FONT, FontWeight.BOLD, 18));

                logoText.setTextFill(
                                Color.WHITE);

                HBox logoHeader = new HBox(8, logoIcon, logoText);

                logoHeader.setAlignment(
                                Pos.CENTER_LEFT);

                Label tagline = new Label("Your AI Workspace");

                tagline.setFont(
                                Font.font(FONT, 11));

                tagline.setTextFill(
                                Color.web(SIDEBAR_MUTED));

                VBox logoBox = new VBox(2, logoHeader, tagline);

                logoBox.setPadding(
                                new Insets(0, 0, 15, 8));

                // =========================================================
                // SIDEBAR NAVIGATION
                // =========================================================

                Button dashboardBtn = createSidebarButton(
                                "⌂",
                                "Dashboard",
                                false);

                Button spacesBtn = createSidebarButton(
                                "▦",
                                "Spaces",
                                false);

                Button searchBtn = createSidebarButton(
                                "⌕",
                                "Search",
                                false);

                Button calendarBtn = createSidebarButton(
                                "□",
                                "Calendar",
                                false);

                Button aiBtn = createSidebarButton(
                                "✧",
                                "AI Assistant",
                                false);

                Button collabBtn = createSidebarButton(
                                "♧",
                                "Collaboration",
                                false);

                Button recentBtn = createSidebarButton(
                                "◷",
                                "Recent",
                                false);

                Button trashBtn = createSidebarButton(
                                "♜",
                                "Trash",
                                false);

                Button settingsBtn = createSidebarButton(
                                "⚙",
                                "Settings",
                                true);

                // =========================================================
                // NAVIGATION ACTIONS
                // =========================================================

                dashboardBtn.setOnAction(
                                e -> LandingPage.showUserDashboard());

                spacesBtn.setOnAction(
                                e -> LandingPage.showUserSpace());

                // searchBtn.setOnAction(...);

                // calendarBtn.setOnAction(...);

                aiBtn.setOnAction(
                                e -> LandingPage.showUserAiAssistant());

                // collabBtn.setOnAction(...);

                // recentBtn.setOnAction(...);

                // trashBtn.setOnAction(...);

                VBox navList = new VBox(
                                4,
                                dashboardBtn,
                                spacesBtn,
                                searchBtn,
                                calendarBtn,
                                aiBtn,
                                collabBtn,
                                recentBtn,
                                trashBtn);

                // =========================================================
                // STORAGE CARD
                // =========================================================

                Label storageBadge = new Label("✧  Storage indexed");

                storageBadge.setFont(
                                Font.font(
                                                FONT,
                                                FontWeight.SEMI_BOLD,
                                                11));

                storageBadge.setTextFill(
                                Color.web("#8FC5FF"));

                Label storageVal = new Label("64.2 GB");

                storageVal.setFont(
                                Font.font(
                                                FONT,
                                                FontWeight.BOLD,
                                                16));

                storageVal.setTextFill(
                                Color.WHITE);

                Label storageSub = new Label("of 100 GB used");

                storageSub.setFont(
                                Font.font(FONT, 11));

                storageSub.setTextFill(
                                Color.web(SIDEBAR_MUTED));

                VBox storageTextGroup = new VBox(
                                1,
                                storageVal,
                                storageSub);

                ProgressBar sidebarProgress = new ProgressBar(0.64);

                sidebarProgress.setMaxWidth(
                                Double.MAX_VALUE);

                sidebarProgress.setPrefHeight(6);

                sidebarProgress.setStyle(
                                "-fx-accent: " + PRIMARY_BLUE + ";" +
                                                "-fx-control-inner-background: #3A4B61;");

                Label storageInfo = new Label(
                                "Files stay in place —\n" +
                                                "nothing moved or renamed.");

                storageInfo.setFont(
                                Font.font(FONT, 11));

                storageInfo.setTextFill(
                                Color.web(SIDEBAR_MUTED));

                VBox storageCard = new VBox(
                                10,
                                storageBadge,
                                storageTextGroup,
                                sidebarProgress,
                                storageInfo);

                storageCard.setPadding(
                                new Insets(14));

                storageCard.setStyle(
                                "-fx-background-color: #202D3E;" +
                                                "-fx-border-color: #3A4B61;" +
                                                "-fx-border-radius: 12;" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-cursor: hand;");

                Region sidebarSpacer = new Region();

                VBox.setVgrow(
                                sidebarSpacer,
                                Priority.ALWAYS);

                VBox sidebar = new VBox(
                                10,
                                logoBox,
                                navList,
                                sidebarSpacer,
                                settingsBtn,
                                storageCard);

                sidebar.setPadding(
                                new Insets(20, 14, 20, 14));

                sidebar.setPrefWidth(230);
                sidebar.setMinWidth(230);
                sidebar.setMaxWidth(230);

                sidebar.setStyle(
                                "-fx-background-color: " +
                                                SIDEBAR_BG + ";" +
                                                "-fx-border-color: #34445A;" +
                                                "-fx-border-width: 0 1 0 0;");

                // =========================================================
                // TOP BAR
                // =========================================================

                TextField searchField = new TextField();

                searchField.setPromptText(
                                "Search settings...");

                searchField.setPrefHeight(38);

                searchField.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-prompt-text-fill: #A9B8CB;" +
                                                "-fx-text-fill: #E8EDF5;" +
                                                "-fx-font-size: 13px;");

                Label searchIcon = new Label("⌕");

                searchIcon.setFont(
                                Font.font(FONT, 16));

                searchIcon.setTextFill(
                                Color.web("#A9B8CB"));

                Label keyShortcut = new Label("⌘ K");

                keyShortcut.setFont(
                                Font.font(
                                                FONT,
                                                FontWeight.SEMI_BOLD,
                                                10));

                keyShortcut.setTextFill(
                                Color.web("#A9B8CB"));

                keyShortcut.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-padding: 3 6;" +
                                                "-fx-background-radius: 5;");

                HBox searchContainer = new HBox(
                                8,
                                searchIcon,
                                searchField,
                                keyShortcut);

                searchContainer.setAlignment(
                                Pos.CENTER_LEFT);

                searchContainer.setPadding(
                                new Insets(0, 10, 0, 12));

                searchContainer.setPrefWidth(540);

                searchContainer.setStyle(
                                "-fx-background-color: #1F2B3B;" +
                                                "-fx-border-color: #3A4B61;" +
                                                "-fx-border-radius: 12;" +
                                                "-fx-background-radius: 12;");

                HBox.setHgrow(
                                searchField,
                                Priority.ALWAYS);

                // =========================================================
                // PROFILE SECTION
                // =========================================================

                Button bellBtn = new Button("🔔");

                bellBtn.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: transparent;" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-cursor: hand;");

                bellBtn.setOnAction(
                                e -> LandingPage.showUserNotificationPage());

                Label avatar = new Label("AV");

                avatar.setPrefSize(
                                42,
                                42);

                avatar.setAlignment(
                                Pos.CENTER);

                avatar.setStyle(
                                "-fx-background-color: " +
                                                PRIMARY_BLUE + ";" +
                                                "-fx-background-radius: 50%;" +
                                                "-fx-text-fill: #FFFFFF;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-font-size: 13px;");

                Label userName = new Label("Aarav Verma");

                userName.setFont(
                                Font.font(
                                                FONT,
                                                FontWeight.SEMI_BOLD,
                                                13));

                userName.setTextFill(
                                Color.web("#E8EDF5"));

                Label dropDown = new Label("⌄");

                dropDown.setTextFill(
                                Color.web("#A9B8CB"));

                HBox profileBox = new HBox(
                                8,
                                bellBtn,
                                avatar,
                                userName,
                                dropDown);

                profileBox.setAlignment(
                                Pos.CENTER);

                Region topSpacer = new Region();

                HBox.setHgrow(
                                topSpacer,
                                Priority.ALWAYS);

                HBox topBar = new HBox(
                                20,
                                searchContainer,
                                topSpacer,
                                profileBox);

                topBar.setAlignment(
                                Pos.CENTER_LEFT);

                topBar.setPadding(
                                new Insets(
                                                12,
                                                24,
                                                12,
                                                24));

                topBar.setStyle(
                                "-fx-background-color: " +
                                                TOPBAR_BG + ";");

                // =========================================================
                // PAGE HEADER
                // =========================================================

                Label pageTitle = new Label("Settings");

                pageTitle.setFont(
                                Font.font(
                                                FONT,
                                                FontWeight.BOLD,
                                                24));

                pageTitle.setTextFill(
                                Color.web("#F2F5FA"));

                Label pageDescription = new Label(
                                "Manage your account, preferences, indexing controls, " +
                                                "and security across OneSpace.");

                pageDescription.setFont(
                                Font.font(FONT, 13));

                pageDescription.setTextFill(
                                Color.web("#C0CBDA"));

                VBox titleBox = new VBox(
                                5,
                                pageTitle,
                                pageDescription);

                // =========================================================
                // PROFILE CARD
                // =========================================================

                Label userAvatarBig = new Label("AV");

                userAvatarBig.setPrefSize(
                                48,
                                48);

                userAvatarBig.setAlignment(
                                Pos.CENTER);

                userAvatarBig.setStyle(
                                "-fx-background-color: " +
                                                PRIMARY_BLUE + ";" +
                                                "-fx-background-radius: 50%;" +
                                                "-fx-text-fill: #FFFFFF;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-font-size: 16px;");

                Label accountName = new Label("Aarav Verma");

                accountName.setFont(
                                Font.font(
                                                FONT,
                                                FontWeight.BOLD,
                                                15));

                accountName.setTextFill(
                                Color.web(TEXT_MAIN));

                Label accountEmail = new Label(
                                "aarav.verma@onespace.app");

                accountEmail.setFont(
                                Font.font(FONT, 12));

                accountEmail.setTextFill(
                                Color.web(TEXT_MUTED));

                VBox accountDetails = new VBox(
                                2,
                                accountName,
                                accountEmail);

                Button editProfileBtn = createActionButton(
                                "Edit Profile");

                Button switchAccountBtn = createActionButton(
                                "Switch Account");

                HBox profileCardActions = new HBox(
                                8,
                                editProfileBtn,
                                switchAccountBtn);

                Region profileSpacer = new Region();

                HBox.setHgrow(
                                profileSpacer,
                                Priority.ALWAYS);

                HBox profileCard = new HBox(
                                16,
                                userAvatarBig,
                                accountDetails,
                                profileSpacer,
                                profileCardActions);

                profileCard.setAlignment(
                                Pos.CENTER_LEFT);

                profileCard.setPadding(
                                new Insets(
                                                16,
                                                20,
                                                16,
                                                20));

                // =========================================================
                // APPEARANCE SECTION
                // =========================================================

                Label appearanceIcon = createSettingIcon("🎨");

                Label appearanceTitle = createSectionTitle(
                                "Appearance");

                Label appearanceDesc = createSectionDescription(
                                "Customize how OneSpace looks and adapts.");

                VBox appearanceText = new VBox(
                                2,
                                appearanceTitle,
                                appearanceDesc);

                HBox appearanceLeft = new HBox(
                                12,
                                appearanceIcon,
                                appearanceText);

                appearanceLeft.setAlignment(
                                Pos.TOP_LEFT);

                Label themeTitle = createSectionTitle(
                                "Theme");

                Button lightTheme = createThemeButton(
                                "☀️",
                                "Light",
                                true);

                Button darkTheme = createThemeButton(
                                "🌙",
                                "Dark",
                                false);

                Button systemTheme = createThemeButton(
                                "💻",
                                "System",
                                false);

                HBox themeCards = new HBox(
                                8,
                                lightTheme,
                                darkTheme,
                                systemTheme);

                VBox themeBox = new VBox(
                                8,
                                themeTitle,
                                themeCards);

                Region appearanceSpacer = new Region();

                HBox.setHgrow(
                                appearanceSpacer,
                                Priority.ALWAYS);

                HBox appearanceSection = new HBox(
                                30,
                                appearanceLeft,
                                appearanceSpacer,
                                themeBox);

                appearanceSection.setPadding(
                                new Insets(
                                                16,
                                                20,
                                                16,
                                                20));

                appearanceSection.setAlignment(
                                Pos.CENTER_LEFT);

                // =========================================================
                // ACCENT COLOR
                // =========================================================

                HBox accentRow = createSettingRow(
                                "✨",
                                "Accent color",
                                "Choose the accent color palette used across indicators.");

                HBox accentColors = new HBox(
                                10,

                                createColorCircle(
                                                PRIMARY_BLUE,
                                                true),

                                createColorCircle(
                                                SKY_BLUE,
                                                false),

                                createColorCircle(
                                                "#5B7FC7",
                                                false),

                                createColorCircle(
                                                EMERALD,
                                                false),

                                createColorCircle(
                                                AMBER,
                                                false),

                                createColorCircle(
                                                "#D96C6C",
                                                false));

                accentColors.setAlignment(
                                Pos.CENTER_RIGHT);

                accentRow.getChildren().add(
                                accentColors);

                // =========================================================
                // LOCAL AI INDEXING
                // =========================================================

                HBox indexingRow = createSettingRow(
                                "⚡",
                                "Local AI Indexing",
                                "Rescan local directories or clear cached search indices.");

                Button rescanBtn = createActionButton(
                                "Rescan All");

                Button clearIndexBtn = createActionButton(
                                "Clear Cache");

                HBox indexingActions = new HBox(
                                8,
                                rescanBtn,
                                clearIndexBtn);

                indexingRow.getChildren().add(
                                indexingActions);

                // =========================================================
                // SECURITY
                // =========================================================

                HBox securityRow = createSettingRow(
                                "🛡",
                                "Security & Password",
                                "Update credentials and manage offline encryption keys.");

                Button changePasswordBtn = createActionButton(
                                "Change Password");

                securityRow.getChildren().add(
                                changePasswordBtn);

                // =========================================================
                // LOGOUT
                // =========================================================

                HBox logoutRow = createSettingRow(
                                "🚪",
                                "Account Sign Out",
                                "Safely sign out of your local OneSpace session.");

                Button logoutBtn = new Button("Sign Out");

                logoutBtn.setFont(
                                Font.font(
                                                FONT,
                                                FontWeight.BOLD,
                                                12));

                logoutBtn.setTextFill(
                                Color.web("#C94646"));

                logoutBtn.setPrefHeight(34);

                logoutBtn.setPadding(
                                new Insets(
                                                0,
                                                16,
                                                0,
                                                16));

                String logoutIdle = "-fx-background-color: #F4DADA;" +
                                "-fx-border-color: #E4A8A8;" +
                                "-fx-border-radius: 8;" +
                                "-fx-background-radius: 8;" +
                                "-fx-cursor: hand;";

                String logoutHover = "-fx-background-color: #EFC8C8;" +
                                "-fx-border-color: #D96C6C;" +
                                "-fx-border-radius: 8;" +
                                "-fx-background-radius: 8;" +
                                "-fx-cursor: hand;";

                logoutBtn.setStyle(logoutIdle);

                logoutBtn.setOnMouseEntered(
                                e -> logoutBtn.setStyle(logoutHover));

                logoutBtn.setOnMouseExited(
                                e -> logoutBtn.setStyle(logoutIdle));

                logoutBtn.setOnAction(
                                e -> LandingPage.showLandingPage());

                logoutRow.getChildren().add(
                                logoutBtn);

                // =========================================================
                // MAIN SETTINGS CARD
                // =========================================================

                VBox settingsCard = new VBox(
                                profileCard,

                                createSeparator(),

                                appearanceSection,

                                createSeparator(),

                                accentRow,

                                createSeparator(),

                                indexingRow,

                                createSeparator(),

                                securityRow,

                                createSeparator(),

                                logoutRow);

                settingsCard.setStyle(
                                "-fx-background-color: " +
                                                BG_CARD + ";" +
                                                "-fx-border-color: " +
                                                BORDER_COLOR + ";" +
                                                "-fx-border-radius: 16;" +
                                                "-fx-background-radius: 16;" +
                                                "-fx-effect: dropshadow(" +
                                                "three-pass-box, rgba(15,30,50,0.18), " +
                                                "14, 0, 0, 4);");

                // =========================================================
                // MAIN CONTENT
                // =========================================================

                VBox mainContent = new VBox(
                                18,
                                titleBox,
                                settingsCard);

                mainContent.setPadding(
                                new Insets(
                                                24,
                                                24,
                                                24,
                                                24));

                VBox.setVgrow(
                                mainContent,
                                Priority.ALWAYS);

                VBox centerContent = new VBox(
                                topBar,
                                mainContent);

                VBox.setVgrow(
                                mainContent,
                                Priority.ALWAYS);

                // =========================================================
                // ROOT
                // =========================================================

                BorderPane root = new BorderPane();

                root.setStyle(
                                "-fx-background-color: " +
                                                BG_APP + ";");

                root.setLeft(sidebar);
                root.setCenter(centerContent);

                return new Scene(
                                root,
                                1200,
                                750);
        }

        // =========================================================
        // SIDEBAR BUTTON
        // =========================================================

        private Button createSidebarButton(
                        String icon,
                        String label,
                        boolean isActive) {

                Label iconLbl = new Label(icon);

                iconLbl.setFont(
                                Font.font(FONT, 14));

                Label textLbl = new Label(label);

                textLbl.setFont(
                                Font.font(
                                                FONT,
                                                isActive
                                                                ? FontWeight.BOLD
                                                                : FontWeight.MEDIUM,
                                                13));

                HBox content = new HBox(
                                12,
                                iconLbl,
                                textLbl);

                content.setAlignment(
                                Pos.CENTER_LEFT);

                Button btn = new Button(
                                "",
                                content);

                btn.setMaxWidth(
                                Double.MAX_VALUE);

                btn.setPrefHeight(40);

                btn.setAlignment(
                                Pos.CENTER_LEFT);

                btn.setPadding(
                                new Insets(
                                                0,
                                                12,
                                                0,
                                                12));

                if (isActive) {

                        btn.setStyle(
                                        "-fx-background-color: " +
                                                        SIDEBAR_ACTIVE + ";" +
                                                        "-fx-background-radius: 9;" +
                                                        "-fx-border-color: rgba(82,137,235,0.35);" +
                                                        "-fx-border-radius: 9;" +
                                                        "-fx-cursor: hand;");

                        iconLbl.setTextFill(
                                        Color.web("#BFD4FF"));

                        textLbl.setTextFill(
                                        Color.WHITE);

                } else {

                        String idleStyle = "-fx-background-color: transparent;" +
                                        "-fx-background-radius: 9;" +
                                        "-fx-cursor: hand;";

                        String hoverStyle = "-fx-background-color: " +
                                        SIDEBAR_HOVER + ";" +
                                        "-fx-background-radius: 9;" +
                                        "-fx-cursor: hand;";

                        btn.setStyle(idleStyle);

                        iconLbl.setTextFill(
                                        Color.web(SIDEBAR_MUTED));

                        textLbl.setTextFill(
                                        Color.web(SIDEBAR_TEXT));

                        btn.setOnMouseEntered(
                                        e -> btn.setStyle(
                                                        hoverStyle));

                        btn.setOnMouseExited(
                                        e -> btn.setStyle(
                                                        idleStyle));
                }

                return btn;
        }

        // =========================================================
        // SETTING ICON
        // =========================================================

        private Label createSettingIcon(
                        String symbol) {

                Label icon = new Label(symbol);

                icon.setFont(
                                Font.font(14));

                icon.setPrefSize(
                                36,
                                36);

                icon.setAlignment(
                                Pos.CENTER);

                icon.setStyle(
                                "-fx-background-color: #C7D8EA;" +
                                                "-fx-background-radius: 10;");

                return icon;
        }

        // =========================================================
        // SECTION TITLE
        // =========================================================

        private Label createSectionTitle(
                        String text) {

                Label label = new Label(text);

                label.setFont(
                                Font.font(
                                                FONT,
                                                FontWeight.BOLD,
                                                13));

                label.setTextFill(
                                Color.web(TEXT_MAIN));

                return label;
        }

        // =========================================================
        // SECTION DESCRIPTION
        // =========================================================

        private Label createSectionDescription(
                        String text) {

                Label label = new Label(text);

                label.setFont(
                                Font.font(FONT, 12));

                label.setTextFill(
                                Color.web(TEXT_MUTED));

                return label;
        }

        // =========================================================
        // SETTING ROW
        // =========================================================

        private HBox createSettingRow(
                        String iconText,
                        String titleText,
                        String descriptionText) {

                Label icon = createSettingIcon(
                                iconText);

                Label title = createSectionTitle(
                                titleText);

                Label description = createSectionDescription(
                                descriptionText);

                VBox textBox = new VBox(
                                2,
                                title,
                                description);

                HBox row = new HBox(
                                12,
                                icon,
                                textBox);

                row.setAlignment(
                                Pos.CENTER_LEFT);

                row.setPadding(
                                new Insets(
                                                14,
                                                20,
                                                14,
                                                20));

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                row.getChildren().add(
                                spacer);

                return row;
        }

        // =========================================================
        // ACTION BUTTON
        // =========================================================

        private Button createActionButton(
                        String text) {

                Button btn = new Button(text);

                btn.setFont(
                                Font.font(
                                                FONT,
                                                FontWeight.MEDIUM,
                                                12));

                btn.setTextFill(
                                Color.web(TEXT_MAIN));

                btn.setPrefHeight(34);

                btn.setPadding(
                                new Insets(
                                                0,
                                                14,
                                                0,
                                                14));

                String idleStyle = "-fx-background-color: #E4EBF4;" +
                                "-fx-border-color: " +
                                BORDER_COLOR + ";" +
                                "-fx-border-radius: 8;" +
                                "-fx-background-radius: 8;" +
                                "-fx-cursor: hand;";

                String hoverStyle = "-fx-background-color: " +
                                PRIMARY_LIGHT + ";" +
                                "-fx-border-color: " +
                                PRIMARY_BLUE + ";" +
                                "-fx-border-radius: 8;" +
                                "-fx-background-radius: 8;" +
                                "-fx-text-fill: " +
                                PRIMARY_DARK + ";" +
                                "-fx-cursor: hand;";

                btn.setStyle(
                                idleStyle);

                btn.setOnMouseEntered(
                                e -> btn.setStyle(
                                                hoverStyle));

                btn.setOnMouseExited(
                                e -> btn.setStyle(
                                                idleStyle));

                return btn;
        }

        // =========================================================
        // THEME BUTTON
        // =========================================================

        private Button createThemeButton(
                        String iconText,
                        String themeName,
                        boolean selected) {

                Label icon = new Label(iconText);

                icon.setFont(
                                Font.font(14));

                Label name = new Label(themeName);

                name.setFont(
                                Font.font(
                                                FONT,
                                                FontWeight.SEMI_BOLD,
                                                12));

                VBox content = new VBox(
                                3,
                                icon,
                                name);

                content.setAlignment(
                                Pos.CENTER_LEFT);

                Button button = new Button(
                                "",
                                content);

                button.setPrefSize(
                                90,
                                54);

                button.setAlignment(
                                Pos.CENTER_LEFT);

                button.setPadding(
                                new Insets(
                                                8,
                                                10,
                                                8,
                                                10));

                if (selected) {

                        button.setStyle(
                                        "-fx-background-color: " +
                                                        PRIMARY_LIGHT + ";" +
                                                        "-fx-border-color: " +
                                                        PRIMARY_BLUE + ";" +
                                                        "-fx-border-width: 2;" +
                                                        "-fx-border-radius: 9;" +
                                                        "-fx-background-radius: 9;" +
                                                        "-fx-cursor: hand;");

                        icon.setTextFill(
                                        Color.web(PRIMARY_BLUE));

                        name.setTextFill(
                                        Color.web(PRIMARY_DARK));

                } else {

                        button.setStyle(
                                        "-fx-background-color: #E7EDF5;" +
                                                        "-fx-border-color: " +
                                                        BORDER_COLOR + ";" +
                                                        "-fx-border-radius: 9;" +
                                                        "-fx-background-radius: 9;" +
                                                        "-fx-cursor: hand;");

                        icon.setTextFill(
                                        Color.web(TEXT_MUTED));

                        name.setTextFill(
                                        Color.web(TEXT_MAIN));
                }

                return button;
        }

        // =========================================================
        // ACCENT COLOR CIRCLE
        // =========================================================

        private Circle createColorCircle(
                        String hexColor,
                        boolean selected) {

                Circle circle = new Circle(11);

                circle.setFill(
                                Color.web(hexColor));

                circle.setStyle(
                                "-fx-cursor: hand;");

                if (selected) {

                        circle.setStroke(
                                        Color.web(PRIMARY_BLUE));

                        circle.setStrokeWidth(3);

                } else {

                        circle.setStroke(
                                        Color.TRANSPARENT);
                }

                return circle;
        }

        // =========================================================
        // SEPARATOR
        // =========================================================

        private Separator createSeparator() {

                Separator sep = new Separator();

                sep.setStyle(
                                "-fx-background-color: " +
                                                BORDER_COLOR + ";" +
                                                "-fx-opacity: 0.75;");

                return sep;
        }
}