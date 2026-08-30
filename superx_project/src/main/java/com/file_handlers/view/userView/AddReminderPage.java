package com.file_handlers.view.userView;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.dao.ReminderDAO;
import com.file_handlers.model.FileData;
import com.file_handlers.model.Reminder;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;
import com.google.cloud.Timestamp;

public class AddReminderPage{
    private static final String FONT="Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_SIDEBAR="#1E2A3A";
    private static final String BG_SIDEBAR_CARD="#141D29";
    private static final String SIDEBAR_BORDER="#2D3D52";
    private static final String BG_CENTER_CANVAS="#31435B";
    private static final String BG_CARD="#DDE8F8";
    private static final String BG_CARD_INNER="#CADDF2";
    private static final String BORDER_CARD="#C3D6EC";
    private static final String BG_INPUT="#EDF3FA";
    private static final String TEXT_DARK="#0F172A";
    private static final String TEXT_MUTED_DARK="#334155";
    private static final String TEXT_LIGHT="#FFFFFF";
    private static final String TEXT_MUTED_LIGHT="#94A3B8";
    private static final String PRIMARY_BLUE="#2563EB";
    private static final String BADGE_BLUE_BG="#BFDBFE";

    private TextField titleField,reminderTimeField;
    private TextArea descriptionField;
    private ComboBox<String> reminderTypeCombo,repeatCombo,priorityCombo;
    private DatePicker reminderDatePicker;
    private Label selectedFileLabel,previewReminderType,previewTitle,previewDescription;
    private Label previewDate,previewTime,previewRepeat,previewPriority;
    private String selectedFileId;
    private String selectedFileName;

    public Scene getAddReminderPageScene(){
        StackPane logoIcon=createOneSpaceLogo();

        Label logoText=new Label("OneSpace");
        logoText.setFont(Font.font(FONT,FontWeight.BOLD,19));
        logoText.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        HBox logoHeader=new HBox(10,logoIcon,logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox=new VBox(4,logoHeader);
        logoBox.setPadding(new Insets(0,0,18,6));

        Button dashboard=createSidebarButton("⌂","Dashboard",false);
        Button spaces=createSidebarButton("📁","Spaces",false);
        Button search=createSidebarButton("⌕","Search",false);
        Button calendar=createSidebarButton("📅","Calendar",true);
        Button ai=createSidebarButton("✧","AI Assistant",false);
        Button collab=createSidebarButton("👥","Collaboration",false);
        Button recent=createSidebarButton("🕒","Recent",false);
        Button trash=createSidebarButton("🗑","Trash",false);
        Button settings=createSidebarButton("⚙","Settings",false);
        Button logoutBtn=createSidebarButton("🚪","Logout",false);

        dashboard.setOnAction(e->LandingPage.showUserDashboard());
        spaces.setOnAction(e->LandingPage.showUserSpace());
        search.setOnAction(e->LandingPage.showUserSearch());
        calendar.setOnAction(e->LandingPage.showCalendarPage());
        ai.setOnAction(e->LandingPage.showAiAssistantPage());
        collab.setOnAction(e->LandingPage.showCollaborationPage());
        recent.setOnAction(e->LandingPage.showRecentPage());
        trash.setOnAction(e->LandingPage.showTrashPage());
        settings.setOnAction(e->LandingPage.showSettingPage());
        logoutBtn.setOnAction(e->LandingPage.showUserLoginPage());

        VBox nav=new VBox(4,dashboard,spaces,search,calendar,ai,collab,recent,trash);

        Label storageTitle=new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,12));
        storageTitle.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        Label storageVal=new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT,FontWeight.BOLD,12));
        storageVal.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        Label storagePercent=new Label("64%");
        storagePercent.setFont(Font.font(FONT,FontWeight.BOLD,11));
        storagePercent.setStyle("-fx-text-fill:"+TEXT_MUTED_LIGHT+";");

        HBox storageValGroup=new HBox(storageVal,new Region(),storagePercent);
        HBox.setHgrow(storageValGroup.getChildren().get(1),Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar progress=new ProgressBar(0.64);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle("-fx-accent:"+PRIMARY_BLUE+";-fx-control-inner-background:#0E1520;");

        Button manageStorageBtn=new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,11));
        manageStorageBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:#60A5FA;-fx-padding:2 0 0 0;-fx-cursor:hand;");
        manageStorageBtn.setOnAction(e->LandingPage.showStorageIndexPage());

        VBox storage=new VBox(8,storageTitle,storageValGroup,progress,manageStorageBtn);
        storage.setPadding(new Insets(14));
        storage.setStyle("-fx-background-color:"+BG_SIDEBAR_CARD+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-radius:12;-fx-background-radius:12;");

        Region sideSpace=space();
        VBox.setVgrow(sideSpace,Priority.ALWAYS);

        VBox sidebar=new VBox(12,logoBox,nav,sideSpace,settings,storage);
        sidebar.setPadding(new Insets(20,14,20,14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 1 0 0;");

        Button bell=new Button("🔔");
        bell.setStyle("-fx-background-color:transparent;-fx-font-size:16px;-fx-text-fill:"+TEXT_LIGHT+";-fx-cursor:hand;");

        Label avatar=new Label("AV");
        avatar.setPrefSize(34,34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-background-radius:50%;-fx-text-fill:"+TEXT_LIGHT+";-fx-font-weight:bold;-fx-font-size:12px;");

        Label userName=new Label(UserSession.isLoggedIn()&&UserSession.getInstance().getDisplayName()!=null?UserSession.getInstance().getDisplayName():"User");
        userName.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,13));
        userName.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        Label dropDown=new Label("⌄");
        dropDown.setStyle("-fx-text-fill:"+TEXT_MUTED_LIGHT+";");

        HBox profileOption=new HBox(8,avatar,userName,dropDown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(5,8,5,8));
        profileOption.setOnMouseClicked(e->LandingPage.showUserProfilePage());

        HBox profileBox=new HBox(10,bell,profileOption);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar=new HBox(20,new Region(),profileBox);
        HBox.setHgrow(topBar.getChildren().get(0),Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16,ResponsiveUtil.PAGE_PADDING,14,ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 0 1 0;");

        Label title=new Label("Add Reminder");
        title.setFont(Font.font(FONT,FontWeight.BOLD,24));
        title.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        Label desc=new Label("Set a reminder for your important document or task.");
        desc.setFont(Font.font(FONT,13));
        desc.setStyle("-fx-text-fill:"+TEXT_MUTED_LIGHT+";-fx-font-weight:500;");

        Button close=new Button("×");
        close.setPrefSize(38,38);
        close.setFont(Font.font(FONT,FontWeight.BOLD,20));
        close.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:8;-fx-background-radius:8;-fx-text-fill:"+TEXT_DARK+";-fx-cursor:hand;");
        close.setOnAction(e->LandingPage.showCalendarPage());

        Region headerSpace=space();
        HBox header=new HBox(new VBox(4,title,desc),headerSpace,close);
        header.setAlignment(Pos.CENTER_LEFT);

        titleField=new TextField();
        titleField.setPromptText("E.g., Passport Expiry, Insurance Renewal");
        styleTextField(titleField);

        descriptionField=new TextArea();
        descriptionField.setPromptText("Add more details about this reminder...");
        descriptionField.setWrapText(true);
        descriptionField.setPrefRowCount(3);
        descriptionField.setStyle("-fx-control-inner-background:"+BG_INPUT+";-fx-background-color:"+BG_INPUT+";-fx-text-fill:"+TEXT_DARK+";-fx-prompt-text-fill:"+TEXT_MUTED_DARK+";-fx-font-family:"+FONT+";-fx-font-size:13px;-fx-border-color:"+BORDER_CARD+";-fx-border-radius:8;-fx-background-radius:8;");

        reminderTypeCombo=new ComboBox<>();
        reminderTypeCombo.getItems().addAll("Document Reminder","Task Reminder","Event Reminder","Deadline Reminder");
        reminderTypeCombo.setValue("Document Reminder");
        styleCombo(reminderTypeCombo);

        Button choose=new Button("📄  Choose a file");
        choose.setMaxWidth(Double.MAX_VALUE);
        choose.setPrefHeight(42);
        choose.setAlignment(Pos.CENTER_LEFT);
        choose.setTextFill(Color.web(TEXT_DARK));
        choose.setStyle("-fx-background-color:"+BG_INPUT+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:8;-fx-background-radius:8;-fx-font-family:"+FONT+";-fx-cursor:hand;");
        choose.setOnAction(e->chooseDocument());

        selectedFileLabel=new Label("No file selected");
        selectedFileLabel.setFont(Font.font(FONT,11));
        selectedFileLabel.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");

        reminderDatePicker=new DatePicker();
        reminderDatePicker.setPromptText("dd/mm/yyyy");
        reminderDatePicker.setPrefHeight(42);
        reminderDatePicker.setMaxWidth(Double.MAX_VALUE);
        reminderDatePicker.setStyle("-fx-background-color:"+BG_INPUT+";-fx-font-family:"+FONT+";");

        reminderTimeField=new TextField();
        reminderTimeField.setPromptText("--:-- --");
        styleTextField(reminderTimeField);

        repeatCombo=new ComboBox<>();
        repeatCombo.getItems().addAll("Does not repeat","Every day","Every week","Every month","Every year");
        repeatCombo.setValue("Does not repeat");
        styleCombo(repeatCombo);

        priorityCombo=new ComboBox<>();
        priorityCombo.getItems().addAll("High","Medium","Low");
        priorityCombo.setValue("Medium");
        styleCombo(priorityCombo);

        CheckBox notification=new CheckBox();
        notification.setSelected(true);

        Label notifTitle=new Label("Enable notification");
        notifTitle.setFont(Font.font(FONT,FontWeight.BOLD,13));
        notifTitle.setStyle("-fx-text-fill:"+TEXT_DARK+";");

        Label notifSub=new Label("You will be notified on the selected date and time.");
        notifSub.setFont(Font.font(FONT,11));
        notifSub.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");

        HBox notificationBox=new HBox(12,notification,new VBox(2,notifTitle,notifSub));
        notificationBox.setAlignment(Pos.CENTER_LEFT);

        VBox reminderTypeBox=new VBox(6,fieldLabel("Reminder Type"),reminderTypeCombo);
        VBox documentBox=new VBox(6,fieldLabel("Select Document (Optional)"),choose,selectedFileLabel);

        HBox typeFile=new HBox(18,reminderTypeBox,documentBox);
        HBox.setHgrow(reminderTypeBox,Priority.ALWAYS);
        HBox.setHgrow(documentBox,Priority.ALWAYS);

        HBox dateTime=row(field("Reminder Date *",reminderDatePicker),field("Reminder Time",reminderTimeField));
        HBox repeatPriority=row(field("Repeat",repeatCombo),field("Priority",priorityCombo));

        VBox details=new VBox(16,section("Reminder Details"),fieldLabel("Title *"),titleField,fieldLabel("Description"),descriptionField,typeFile,dateTime,repeatPriority,notificationBox);
        details.setPadding(new Insets(24));
        details.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:16;-fx-background-radius:16;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.14),12,0,0,4);");

        previewReminderType=new Label(reminderTypeCombo.getValue());
        previewReminderType.setFont(Font.font(FONT,FontWeight.BOLD,12));
        previewReminderType.setStyle("-fx-background-color:"+BADGE_BLUE_BG+";-fx-text-fill:"+PRIMARY_BLUE+";-fx-padding:5 8;-fx-background-radius:5;");

        previewTitle=new Label("Reminder Title");
        previewTitle.setFont(Font.font(FONT,FontWeight.BOLD,18));
        previewTitle.setStyle("-fx-text-fill:"+TEXT_DARK+";");

        previewDescription=new Label("Reminder description will appear here...");
        previewDescription.setFont(Font.font(FONT,12));
        previewDescription.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");
        previewDescription.setWrapText(true);

        previewDate=preview("▣","Select reminder date");
        previewTime=preview("◷","Select reminder time");
        previewRepeat=preview("⟳","Does not repeat");
        previewPriority=preview("⚑","Medium Priority");

        Label previewIcon=new Label("🔔");
        previewIcon.setFont(Font.font(FONT,20));

        VBox previewInner=new VBox(14,previewIcon,previewReminderType,previewTitle,previewDescription,new Separator(),previewDate,previewTime,previewRepeat,previewPriority);
        previewInner.setPadding(new Insets(24));
        previewInner.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:12;-fx-background-radius:12;");

        Label previewSub=new Label("This is how your reminder will appear.");
        previewSub.setFont(Font.font(FONT,12));
        previewSub.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");

        VBox previewCard=new VBox(8,section("Reminder Preview"),previewSub,previewInner);
        previewCard.setPadding(new Insets(24));
        previewCard.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:16;-fx-background-radius:16;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.14),12,0,0,4);");

        titleField.textProperty().addListener((o,a,b)->previewTitle.setText(b.trim().isEmpty()?"Reminder Title":b.trim()));
        descriptionField.textProperty().addListener((o,a,b)->previewDescription.setText(b.trim().isEmpty()?"Reminder description will appear here...":b.trim()));
        reminderTypeCombo.valueProperty().addListener((o,a,b)->previewReminderType.setText(b));
        reminderDatePicker.valueProperty().addListener((o,a,b)->previewDate.setText(b==null?"▣  Select reminder date":"▣  "+b.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))));
        reminderTimeField.textProperty().addListener((o,a,b)->previewTime.setText(b.trim().isEmpty()?"◷  Select reminder time":"◷  "+b.trim()));
        repeatCombo.valueProperty().addListener((o,a,b)->previewRepeat.setText("⟳  "+b));
        priorityCombo.valueProperty().addListener((o,a,b)->previewPriority.setText("⚑  "+b+" Priority"));

        HBox columns=new HBox(20,details,previewCard);
        HBox.setHgrow(details,Priority.ALWAYS);
        HBox.setHgrow(previewCard,Priority.ALWAYS);

        Button cancel=new Button("Cancel");
        cancel.setFont(Font.font(FONT,FontWeight.BOLD,13));
        cancel.setPrefHeight(40);
        cancel.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:8;-fx-background-radius:8;-fx-text-fill:"+TEXT_DARK+";-fx-padding:8 20;-fx-cursor:hand;");
        cancel.setOnAction(e->LandingPage.showCalendarPage());

        Button create=new Button("+  Create Reminder");
        create.setFont(Font.font(FONT,FontWeight.BOLD,13));
        create.setPrefHeight(40);
        create.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-text-fill:#FFFFFF;-fx-background-radius:8;-fx-padding:8 20;-fx-cursor:hand;");
        create.setOnAction(e->createReminder());

        HBox buttons=new HBox(10,cancel,create);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox contentBody=new VBox(22,header,columns,buttons);
        contentBody.setPadding(new Insets(24,ResponsiveUtil.PAGE_PADDING,28,ResponsiveUtil.PAGE_PADDING));
        contentBody.setStyle("-fx-background-color:"+BG_CENTER_CANVAS+";");

        ScrollPane scrollPane=new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:"+BG_CENTER_CANVAS+";-fx-background:"+BG_CENTER_CANVAS+";-fx-background-insets:0;-fx-padding:0;");

        VBox mainArea=new VBox(topBar,scrollPane);
        mainArea.setStyle("-fx-background-color:"+BG_CENTER_CANVAS+";");
        VBox.setVgrow(scrollPane,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setStyle("-fx-background-color:"+BG_SIDEBAR+";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private void createReminder(){
        String title=titleField.getText().trim();

        if(title.isEmpty()){
            alert(Alert.AlertType.WARNING,"Missing Title","Please enter a reminder title.");
            titleField.requestFocus();
            return;
        }

        if(reminderDatePicker.getValue()==null){
            alert(Alert.AlertType.WARNING,"Missing Date","Please select a reminder date.");
            reminderDatePicker.requestFocus();
            return;
        }

        if(!UserSession.isLoggedIn()){
            alert(Alert.AlertType.ERROR,"Not Logged In","Please log in before creating a reminder.");
            return;
        }

        Reminder reminder=new Reminder();
        reminder.setTitle(title);
        reminder.setDescription(descriptionField.getText().trim());
        reminder.setType(reminderTypeCombo.getValue());
        reminder.setDate(Timestamp.of(Date.from(
            reminderDatePicker.getValue()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )));
        reminder.setTime(reminderTimeField.getText().trim().isEmpty()
                ?"Not specified":reminderTimeField.getText().trim());
        reminder.setRepeat(repeatCombo.getValue());
        reminder.setPriority(priorityCombo.getValue());
        reminder.setLinkedFileId(selectedFileId);
        reminder.setLinkedFileName(selectedFileName);

        try{
            String id=new ReminderDAO().saveReminder(
                UserSession.getInstance().getUid(),
                reminder
            );

            System.out.println("[REMINDER] Saved: "+id);

            alert(
                Alert.AlertType.INFORMATION,
                "Reminder Created",
                "Reminder saved successfully."
            );

            LandingPage.showCalendarPage();

        }catch(Exception e){
            e.printStackTrace();

            alert(
                Alert.AlertType.ERROR,
                "Could Not Save Reminder",
                e.getMessage()==null?"Unable to save reminder.":e.getMessage()
            );
        }
    }

    private Timestamp toTimestamp(java.time.LocalDate date){
        return Timestamp.of(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    private void chooseDocument(){
        if(!UserSession.isLoggedIn()){
            alert(Alert.AlertType.WARNING,"Not Logged In","Please log in first.");
            return;
        }

        FileChooser fc=new FileChooser();
        fc.setTitle("Choose Document");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files","*.*"),
                new FileChooser.ExtensionFilter("PDF Files","*.pdf"),
                new FileChooser.ExtensionFilter("Documents","*.doc","*.docx"),
                new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg"));

        if(selectedFileLabel.getScene()==null)return;

        File file=fc.showOpenDialog(selectedFileLabel.getScene().getWindow());

        if(file==null)return;

        try{
            List<FileData> files=new FileDAO().getAllFiles(UserSession.getInstance().getUid());

            for(FileData data:files){
                if(file.getAbsolutePath().equals(data.getLocalPath())){
                    selectedFileId=data.getFileHash();
                    selectedFileName=data.getFileName();
                    selectedFileLabel.setText("Selected: "+data.getFileName());
                    return;
                }
            }

            selectedFileId=null;
            selectedFileName=null;
            selectedFileLabel.setText("Not indexed in OneSpace");

        }catch(Exception e){
            alert(Alert.AlertType.ERROR,"File Error","Could not check the selected file.");
        }
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

    private Button createSidebarButton(String icon,String label,boolean isActive){
        Label iconLbl=new Label(icon);
        iconLbl.setFont(Font.font(FONT,14));

        Label textLbl=new Label(label);
        textLbl.setFont(Font.font(FONT,isActive?FontWeight.BOLD:FontWeight.MEDIUM,13));

        HBox content=new HBox(12,iconLbl,textLbl);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn=new Button("",content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0,12,0,12));

        if(isActive){
            btn.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-background-radius:8;-fx-cursor:hand;");
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

    private Label fieldLabel(String text){
        Label lbl=new Label(text);
        lbl.setFont(Font.font(FONT,FontWeight.BOLD,12));
        lbl.setStyle("-fx-text-fill:"+TEXT_DARK+";");
        return lbl;
    }

    private Label section(String text){
        Label lbl=new Label(text);
        lbl.setFont(Font.font(FONT,FontWeight.BOLD,16));
        lbl.setStyle("-fx-text-fill:"+TEXT_DARK+";");
        return lbl;
    }

    private void styleTextField(TextField f){
        f.setPrefHeight(42);
        f.setMaxWidth(Double.MAX_VALUE);
        f.setStyle("-fx-background-color:"+BG_INPUT+";-fx-control-inner-background:"+BG_INPUT+";-fx-text-fill:"+TEXT_DARK+";-fx-prompt-text-fill:"+TEXT_MUTED_DARK+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:8;-fx-background-radius:8;-fx-font-family:"+FONT+";-fx-font-size:13px;");
    }

    private void styleCombo(ComboBox<String> c){
        c.setPrefHeight(42);
        c.setMaxWidth(Double.MAX_VALUE);
        c.setStyle("-fx-background-color:"+BG_INPUT+";-fx-border-color:"+BORDER_CARD+";-fx-font-family:"+FONT+";-fx-font-size:13px;-fx-text-fill:"+TEXT_DARK+";-fx-border-radius:8;-fx-background-radius:8;");
    }

    private VBox field(String name,Control control){
        return new VBox(6,fieldLabel(name),control);
    }

    private HBox row(Pane a,Pane b){
        HBox h=new HBox(18,a,b);
        HBox.setHgrow(a,Priority.ALWAYS);
        HBox.setHgrow(b,Priority.ALWAYS);
        return h;
    }

    private Label preview(String icon,String text){
        Label lbl=new Label(icon+"  "+text);
        lbl.setFont(Font.font(FONT,13));
        lbl.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");
        return lbl;
    }

    private Region space(){
        Region r=new Region();
        HBox.setHgrow(r,Priority.ALWAYS);
        return r;
    }

    private void alert(Alert.AlertType type,String title,String msg){
        Alert a=new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}