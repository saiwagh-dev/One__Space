package com.file_handlers.view.userView;

import com.file_handlers.controller.AuthController;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class UserSettingPage {

    private static final String FONT="Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final Preferences PREFS=Preferences.userRoot().node("onespace");
    private static final AuthController AUTH=new AuthController();

    private static String currentTheme=PREFS.get("theme","Dark");
    private static String primaryBlue=PREFS.get("accentColor","#2563EB");

    private String BG_SIDEBAR,BG_SIDEBAR_CARD,SIDEBAR_BORDER,BG_CENTER_CANVAS;
    private String BG_CARD,BG_CARD_INNER,BORDER_CARD,TEXT_DARK,TEXT_MUTED_DARK,TEXT_LIGHT,TEXT_MUTED_LIGHT;

    private void loadThemeColors(){
        if("Light".equals(currentTheme)){
            BG_SIDEBAR="#F1F5F9";
            BG_SIDEBAR_CARD="#E2E8F0";
            SIDEBAR_BORDER="#CBD5E1";
            BG_CENTER_CANVAS="#F8FAFC";
            BG_CARD="#FFFFFF";
            BG_CARD_INNER="#F1F5F9";
            BORDER_CARD="#E2E8F0";
            TEXT_DARK="#0F172A";
            TEXT_MUTED_DARK="#64748B";
            TEXT_LIGHT="#0F172A";
            TEXT_MUTED_LIGHT="#64748B";
        }else{
            BG_SIDEBAR="#1E2A3A";
            BG_SIDEBAR_CARD="#141D29";
            SIDEBAR_BORDER="#2D3D52";
            BG_CENTER_CANVAS="#31435B";
            BG_CARD="#DDE8F8";
            BG_CARD_INNER="#CADDF2";
            BORDER_CARD="#C3D6EC";
            TEXT_DARK="#0F172A";
            TEXT_MUTED_DARK="#334155";
            TEXT_LIGHT="#FFFFFF";
            TEXT_MUTED_LIGHT="#94A3B8";
        }
    }

    public Scene getSettingPageScene(){
        loadThemeColors();
        UserSession session=UserSession.getInstance();

        String displayName=session!=null&&session.getDisplayName()!=null&&!session.getDisplayName().isBlank()
                ?session.getDisplayName():"User";
        String email=session!=null&&session.getEmail()!=null&&!session.getEmail().isBlank()
                ?session.getEmail():"No email";

        String initials=getInitials(displayName);

        StackPane logoIcon=createOneSpaceLogo();
        Label logoText=new Label("OneSpace");
        logoText.setFont(Font.font(FONT,FontWeight.BOLD,19));
        logoText.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        HBox logoHeader=new HBox(10,logoIcon,logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox=new VBox(logoHeader);
        logoBox.setPadding(new Insets(0,0,18,6));

        Button dashboardBtn=createSidebarButton("⌂","Dashboard",false);
        Button spacesBtn=createSidebarButton("📁","Spaces",false);
        Button searchBtn=createSidebarButton("⌕","Search",false);
        Button calendarBtn=createSidebarButton("📅","Calendar",false);
        Button aiBtn=createSidebarButton("✧","AI Assistant",false);
        Button collabBtn=createSidebarButton("👥","Collaboration",false);
        Button recentBtn=createSidebarButton("🕒","Recent",false);
        Button trashBtn=createSidebarButton("🗑","Trash",false);
        Button settingsBtn=createSidebarButton("⚙","Settings",true);

        dashboardBtn.setOnAction(e->LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e->LandingPage.showUserSpace());
        searchBtn.setOnAction(e->LandingPage.showUserSearch());
        calendarBtn.setOnAction(e->LandingPage.showCalendarPage());
        aiBtn.setOnAction(e->LandingPage.showAiAssistantPage());
        collabBtn.setOnAction(e->LandingPage.showCollaborationPage());
        recentBtn.setOnAction(e->LandingPage.showRecentPage());
        trashBtn.setOnAction(e->LandingPage.showTrashPage());
        settingsBtn.setOnAction(e->LandingPage.showSettingPage());

        VBox navList=new VBox(4,dashboardBtn,spacesBtn,searchBtn,calendarBtn,aiBtn,collabBtn,recentBtn,trashBtn);

        Label storageTitle=new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,12));
        storageTitle.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        Label storageVal=new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT,FontWeight.BOLD,12));
        storageVal.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        Label storagePercent=new Label("64%");
        storagePercent.setFont(Font.font(FONT,FontWeight.BOLD,11));
        storagePercent.setStyle("-fx-text-fill:"+TEXT_MUTED_LIGHT+";");

        Region storageSpacer=new Region();
        HBox.setHgrow(storageSpacer,Priority.ALWAYS);

        HBox storageValues=new HBox(storageVal,storageSpacer,storagePercent);
        storageValues.setAlignment(Pos.CENTER_LEFT);

        ProgressBar progress=new ProgressBar(.64);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle("-fx-accent:"+primaryBlue+";-fx-control-inner-background:#0E1520;");

        Button manageStorageBtn=new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,11));
        manageStorageBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:#60A5FA;-fx-padding:2 0 0 0;-fx-cursor:hand;");
        manageStorageBtn.setOnAction(e->LandingPage.showStorageIndexPage());

        VBox storageCard=new VBox(8,storageTitle,storageValues,progress,manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color:"+BG_SIDEBAR_CARD+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-radius:12;-fx-background-radius:12;");

        Region sidebarSpacer=new Region();
        VBox.setVgrow(sidebarSpacer,Priority.ALWAYS);

        VBox bottomButtons=new VBox(4,settingsBtn);
        VBox sidebar=new VBox(12,logoBox,navList,sidebarSpacer,bottomButtons,storageCard);
        sidebar.setPadding(new Insets(20,14,20,14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMaxWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 1 0 0;");

        Button bellBtn=new Button("🔔");
        bellBtn.setStyle("-fx-background-color:transparent;-fx-font-size:16px;-fx-text-fill:"+TEXT_LIGHT+";-fx-cursor:hand;");
        bellBtn.setOnAction(e->LandingPage.showNotificationPage());

        Label avatar=new Label(initials);
        avatar.setPrefSize(34,34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:"+primaryBlue+";-fx-background-radius:50%;-fx-text-fill:"+TEXT_LIGHT+";-fx-font-weight:bold;-fx-font-size:12px;");

        Label userName=new Label(getFirstName(displayName));
        userName.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,13));
        userName.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        Label dropDown=new Label("⌄");
        dropDown.setStyle("-fx-text-fill:"+TEXT_MUTED_LIGHT+";");

        HBox profileBox=new HBox(10,bellBtn,avatar,userName,dropDown);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar=new HBox(20,new Region(),profileBox);
        HBox.setHgrow(topBar.getChildren().get(0),Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16,ResponsiveUtil.PAGE_PADDING,14,ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 0 1 0;");

        Label pageTitle=new Label("Settings");
        pageTitle.setFont(Font.font(FONT,FontWeight.BOLD,22));
        pageTitle.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        Label pageDescription=new Label("Manage your account, preferences, indexing controls, and security across OneSpace.");
        pageDescription.setFont(Font.font(FONT,13));
        pageDescription.setStyle("-fx-text-fill:"+TEXT_MUTED_LIGHT+";-fx-font-weight:500;");

        VBox titleBox=new VBox(4,pageTitle,pageDescription);

        Label userAvatarBig=new Label(initials);
        userAvatarBig.setPrefSize(48,48);
        userAvatarBig.setAlignment(Pos.CENTER);
        userAvatarBig.setStyle("-fx-background-color:"+primaryBlue+";-fx-background-radius:50%;-fx-text-fill:"+TEXT_LIGHT+";-fx-font-weight:bold;-fx-font-size:16px;");

        Label accountName=new Label(displayName);
        accountName.setFont(Font.font(FONT,FontWeight.BOLD,15));
        accountName.setStyle("-fx-text-fill:"+TEXT_DARK+";");

        Label accountEmail=new Label(email);
        accountEmail.setFont(Font.font(FONT,12));
        accountEmail.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");

        VBox accountDetails=new VBox(2,accountName,accountEmail);

        Button editProfileBtn=createActionButton("Edit Profile");
        editProfileBtn.setOnAction(e->LandingPage.showUserProfilePage());

        Button switchAccountBtn=createActionButton("Switch Account");
        switchAccountBtn.setOnAction(e->showInfo("Switch Account","Please sign out and sign in with another account."));

        HBox profileActions=new HBox(8,editProfileBtn,switchAccountBtn);
        HBox profileCard=new HBox(16,userAvatarBig,accountDetails,new Region(),profileActions);
        HBox.setHgrow(profileCard.getChildren().get(2),Priority.ALWAYS);
        profileCard.setAlignment(Pos.CENTER_LEFT);
        profileCard.setPadding(new Insets(14,20,14,20));

        Label appearanceIcon=createSettingIcon("🎨");
        Label appearanceTitle=createSectionTitle("Appearance");
        Label appearanceDesc=createSectionDescription("Customize how OneSpace looks and adapts.");

        VBox appearanceText=new VBox(2,appearanceTitle,appearanceDesc);
        HBox appearanceLeft=new HBox(12,appearanceIcon,appearanceText);
        appearanceLeft.setAlignment(Pos.TOP_LEFT);

        Label themeTitle=createSectionTitle("Theme");

        Button lightTheme=createThemeButton("☀️","Light","Light".equals(currentTheme));
        Button darkTheme=createThemeButton("🌙","Dark","Dark".equals(currentTheme));
        Button systemTheme=createThemeButton("💻","System","System".equals(currentTheme));

        lightTheme.setOnAction(e->setTheme("Light"));
        darkTheme.setOnAction(e->setTheme("Dark"));
        systemTheme.setOnAction(e->setTheme("System"));

        HBox themeCards=new HBox(8,lightTheme,darkTheme,systemTheme);
        VBox themeBox=new VBox(8,themeTitle,themeCards);

        HBox appearanceSection=new HBox(30,appearanceLeft,new Region(),themeBox);
        HBox.setHgrow(appearanceSection.getChildren().get(1),Priority.ALWAYS);
        appearanceSection.setPadding(new Insets(14,20,14,20));
        appearanceSection.setAlignment(Pos.CENTER_LEFT);

        HBox accentRow=createSettingRow("✨","Accent color","Choose the accent color palette used across indicators.");

        HBox accentColors=new HBox(10,
                createColorCircle("#2563EB"),
                createColorCircle("#0284C7"),
                createColorCircle("#059669"),
                createColorCircle("#7C3AED"),
                createColorCircle("#D97706"),
                createColorCircle("#DC2626")
        );

        accentColors.setAlignment(Pos.CENTER_RIGHT);
        accentRow.getChildren().add(accentColors);

        HBox indexingRow=createSettingRow("⚡","Local AI Indexing","Rescan local directories or clear cached search indices.");

        Button rescanBtn=createActionButton("Rescan All");
        Button clearIndexBtn=createActionButton("Clear Cache");

        rescanBtn.setOnAction(e->showInfo("Rescan","File rescan functionality will be connected to FileProcessingService."));
        clearIndexBtn.setOnAction(e->showInfo("Clear Cache","Search cache clearing will be connected when the indexing cache is implemented."));

        indexingRow.getChildren().add(new HBox(8,rescanBtn,clearIndexBtn));

        HBox securityRow=createSettingRow("🛡","Security & Password","Update your Firebase account password.");

        Button changePasswordBtn=createActionButton("Change Password");
        changePasswordBtn.setOnAction(e->openChangePasswordWindow());

        securityRow.getChildren().add(changePasswordBtn);

        VBox settingsCard=new VBox(
                profileCard,createSeparator(),
                appearanceSection,createSeparator(),
                accentRow,createSeparator(),
                indexingRow,createSeparator(),
                securityRow
        );

        settingsCard.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:16;-fx-background-radius:16;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.18),16,0,0,6);");

        VBox mainContent=new VBox(22,titleBox,settingsCard);
        mainContent.setPadding(new Insets(24,ResponsiveUtil.PAGE_PADDING,28,ResponsiveUtil.PAGE_PADDING));
        mainContent.setStyle("-fx-background-color:"+BG_CENTER_CANVAS+";");

        VBox centerContent=new VBox(topBar,mainContent);
        centerContent.setStyle("-fx-background-color:"+BG_CENTER_CANVAS+";");
        VBox.setVgrow(mainContent,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setStyle("-fx-background-color:"+BG_SIDEBAR+";");
        root.setLeft(sidebar);
        root.setCenter(centerContent);

        return new Scene(root,LandingPage.getCurrentWidth(),LandingPage.getCurrentHeight());
    }

    private void setTheme(String theme){
        currentTheme=theme;
        PREFS.put("theme",theme);
        Stage stage=(Stage) Stage.getWindows().stream().filter(Window->Window.isShowing()).findFirst().orElse(null);
        if(stage!=null) stage.setScene(getSettingPageScene());
    }

    private Circle createColorCircle(String hexColor){
        Circle circle=new Circle(11);
        circle.setFill(Color.web(hexColor));
        boolean selected=primaryBlue.equals(hexColor);
        circle.setStroke(selected?Color.web(TEXT_DARK):Color.TRANSPARENT);
        circle.setStrokeWidth(selected?2.5:0);
        circle.setCursor(javafx.scene.Cursor.HAND);

        circle.setOnMouseClicked(e->{
            primaryBlue=hexColor;
            PREFS.put("accentColor",hexColor);
            Stage stage=(Stage)circle.getScene().getWindow();
            stage.setScene(getSettingPageScene());
        });

        return circle;
    }

    private void openChangePasswordWindow(){
        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Change Password");

        Label title=new Label("Change Password");
        title.setFont(Font.font(FONT,FontWeight.BOLD,16));
        title.setStyle("-fx-text-fill:"+TEXT_DARK+";");

        PasswordField currentPass=new PasswordField();
        PasswordField newPass=new PasswordField();
        PasswordField confirmPass=new PasswordField();

        currentPass.setPromptText("Current password");
        newPass.setPromptText("New password");
        confirmPass.setPromptText("Confirm new password");

        Label status=new Label();
        status.setWrapText(true);
        status.setStyle("-fx-text-fill:#DC2626;-fx-font-size:11px;");

        Button updateBtn=new Button("Update Password");
        updateBtn.setStyle("-fx-background-color:"+primaryBlue+";-fx-text-fill:white;-fx-font-weight:bold;-fx-cursor:hand;");

        updateBtn.setOnAction(e->{
            String current=currentPass.getText();
            String password=newPass.getText();
            String confirm=confirmPass.getText();

            if(current.isBlank()||password.isBlank()||confirm.isBlank()){
                status.setText("Please fill all password fields.");
                return;
            }

            if(password.length()<6){
                status.setText("New password must contain at least 6 characters.");
                return;
            }

            if(!password.equals(confirm)){
                status.setText("New passwords do not match.");
                return;
            }

            UserSession session=UserSession.getInstance();

            if(session==null||session.getEmail()==null){
                status.setText("No authenticated user session found.");
                return;
            }

            updateBtn.setDisable(true);
            status.setStyle("-fx-text-fill:#334155;-fx-font-size:11px;");
            status.setText("Updating password...");

            Thread thread=new Thread(()->{
                boolean success=AUTH.changePassword(session.getEmail(),current,password);

                javafx.application.Platform.runLater(()->{
                    updateBtn.setDisable(false);

                    if(success){
                        stage.close();
                        showInfo("Password Updated","Your Firebase password has been updated successfully.");
                    }else{
                        status.setStyle("-fx-text-fill:#DC2626;-fx-font-size:11px;");
                        status.setText("Current password is incorrect or the password could not be updated.");
                    }
                });
            });

            thread.setDaemon(true);
            thread.start();
        });

        VBox layout=new VBox(
                10,
                title,
                new Label("Current Password:"),currentPass,
                new Label("New Password:"),newPass,
                new Label("Confirm Password:"),confirmPass,
                status,
                updateBtn
        );

        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color:"+BG_CARD+";");

        stage.setScene(new Scene(layout,380,390));
        stage.setResizable(false);
        stage.showAndWait();
    }

    private StackPane createOneSpaceLogo(){
        Image logoImage=new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
        ImageView logoView=new ImageView(logoImage);
        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane logoPane=new StackPane(logoView);
        logoPane.setPrefSize(42,42);
        logoPane.setAlignment(Pos.CENTER);
        return logoPane;
    }

    private Button createSidebarButton(String icon,String label,boolean active){
        Label iconLbl=new Label(icon);
        iconLbl.setFont(Font.font(FONT,14));

        Label textLbl=new Label(label);
        textLbl.setFont(Font.font(FONT,active?FontWeight.BOLD:FontWeight.MEDIUM,13));

        HBox content=new HBox(12,iconLbl,textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn=new Button("",content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0,12,0,12));

        if(active){
            btn.setStyle("-fx-background-color:"+primaryBlue+";-fx-background-radius:8;-fx-cursor:hand;");
            iconLbl.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");
            textLbl.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");
        }else{
            btn.setStyle("-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;");
            iconLbl.setStyle("-fx-text-fill:"+TEXT_MUTED_LIGHT+";");
            textLbl.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

            btn.setOnMouseEntered(e->btn.setStyle("-fx-background-color:#26354A;-fx-background-radius:8;-fx-cursor:hand;"));
            btn.setOnMouseExited(e->btn.setStyle("-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;"));
        }

        return btn;
    }

    private Label createSettingIcon(String symbol){
        Label icon=new Label(symbol);
        icon.setFont(Font.font(14));
        icon.setPrefSize(34,34);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-background-radius:8;-fx-text-fill:"+primaryBlue+";");
        return icon;
    }

    private Label createSectionTitle(String text){
        Label label=new Label(text);
        label.setFont(Font.font(FONT,FontWeight.BOLD,13));
        label.setStyle("-fx-text-fill:"+TEXT_DARK+";");
        return label;
    }

    private Label createSectionDescription(String text){
        Label label=new Label(text);
        label.setFont(Font.font(FONT,12));
        label.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");
        return label;
    }

    private HBox createSettingRow(String iconText,String titleText,String descriptionText){
        Label icon=createSettingIcon(iconText);
        Label title=createSectionTitle(titleText);
        Label description=createSectionDescription(descriptionText);

        VBox textBox=new VBox(1,title,description);
        HBox row=new HBox(12,icon,textBox);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12,20,12,20));

        Region spacer=new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);
        row.getChildren().add(spacer);

        return row;
    }

    private Button createActionButton(String text){
        Button btn=new Button(text);
        btn.setFont(Font.font(FONT,FontWeight.MEDIUM,12));
        btn.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:8;-fx-background-radius:8;-fx-text-fill:"+TEXT_DARK+";-fx-pref-height:32;-fx-padding:0 12;-fx-cursor:hand;");
        return btn;
    }

    private Button createThemeButton(String iconText,String themeName,boolean selected){
        Label icon=new Label(iconText);
        icon.setFont(Font.font(14));

        Label name=new Label(themeName);
        name.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,12));

        VBox content=new VBox(2,icon,name);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button=new Button("",content);
        button.setPrefSize(90,52);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(8,10,8,10));

        if(selected){
            button.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-border-color:"+primaryBlue+";-fx-border-width:2;-fx-border-radius:8;-fx-background-radius:8;-fx-cursor:hand;");
            icon.setStyle("-fx-text-fill:"+primaryBlue+";");
            name.setStyle("-fx-text-fill:"+primaryBlue+";");
        }else{
            button.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:8;-fx-background-radius:8;-fx-cursor:hand;");
            icon.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");
            name.setStyle("-fx-text-fill:"+TEXT_DARK+";");
        }

        return button;
    }

    private Separator createSeparator(){
        Separator sep=new Separator();
        sep.setStyle("-fx-background-color:"+BORDER_CARD+";-fx-opacity:0.5;");
        return sep;
    }

    private String getFirstName(String name){
        if(name==null||name.isBlank()) return "User";
        return name.trim().split("\\s+")[0];
    }

    private String getInitials(String name){
        if(name==null||name.isBlank()) return "U";
        String[] parts=name.trim().split("\\s+");
        if(parts.length>=2) return (""+parts[0].charAt(0)+parts[1].charAt(0)).toUpperCase();
        return name.substring(0,Math.min(2,name.length())).toUpperCase();
    }

    private void styleDialog(Alert alert){
        alert.getDialogPane().setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";");
    }

    private void showInfo(String title,String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleDialog(alert);
        alert.showAndWait();
    }
}