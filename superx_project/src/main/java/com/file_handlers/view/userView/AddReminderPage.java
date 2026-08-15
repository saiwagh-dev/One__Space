package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.time.format.DateTimeFormatter;

public class AddReminderPage {

    private static final String FONT="Arial",BG_APP="#3A4D67",BG_CARD="#F7FAFE",
            BG_INNER="#E8F1FB",BG_INPUT="#FFFFFF",BG_SIDE="#26384E",
            BORDER="#AFC4DA",BLUE="#2563EB",LIGHT_BLUE="#DBEAFE",
            DARK="#111827",MUTED="#374151",WHITE="#FFFFFF",LIGHT="#E5EDF6";

    private TextField titleField,reminderTimeField;
    private TextArea descriptionField;
    private ComboBox<String> reminderTypeCombo,repeatCombo,priorityCombo;
    private DatePicker reminderDatePicker;
    private Label selectedFileLabel,previewReminderType,previewTitle,previewDescription;
    private Label previewDate,previewTime,previewRepeat,previewPriority;

    public Scene getAddReminderPageScene(Stage stage){

        Button dashboard=side("⌂","Dashboard",false),
                spaces=side("▦","Spaces",false),
                search=side("⌕","Search",false),
                calendar=side("▣","Calendar",true),
                ai=side("✦","AI Assistant",false),
                collab=side("♧","Collaboration",false),
                recent=side("◷","Recent",false),
                trash=side("♜","Trash",false),
                settings=side("⚙","Settings",false);

        dashboard.setOnAction(e->LandingPage.showUserDashboard());
        spaces.setOnAction(e->LandingPage.showUserSpace());
        search.setOnAction(e->LandingPage.showUserSearch());
        calendar.setOnAction(e->LandingPage.showCalendarPage());
        ai.setOnAction(e->LandingPage.showLandingPage());
        collab.setOnAction(e->LandingPage.showLandingPage());
        recent.setOnAction(e->LandingPage.showLandingPage());
        trash.setOnAction(e->LandingPage.showLandingPage());
        settings.setOnAction(e->LandingPage.showLandingPage());

        VBox logoBox=new VBox(4,
                new HBox(8,
                        label("⬡",25,FontWeight.BOLD,LIGHT_BLUE),
                        label("OneSpace",20,FontWeight.BOLD,WHITE)),
                label("Your AI Workspace",12,FontWeight.NORMAL,LIGHT));
        logoBox.setPadding(new Insets(0,0,15,8));

        VBox nav=new VBox(4,dashboard,spaces,search,calendar,ai,collab,recent,trash);

        ProgressBar progress=new ProgressBar(0);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);

        VBox storage=new VBox(8,
                label("✧ Storage indexed",11,FontWeight.BOLD,LIGHT_BLUE),
                label("0.0 GB",16,FontWeight.BOLD,WHITE),
                label("of 100 GB used",11,FontWeight.NORMAL,LIGHT),
                progress,
                label("No files scanned yet.",11,FontWeight.NORMAL,LIGHT));
        storage.setPadding(new Insets(14));
        storage.setStyle(card(BG_SIDE));

        Region sideSpace=space();
        VBox.setVgrow(sideSpace,Priority.ALWAYS);

        VBox sidebar=new VBox(10,logoBox,nav,sideSpace,settings,storage);
        sidebar.setPadding(new Insets(20,14,20,14));
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color:"+BG_APP+";");

        TextField searchField=new TextField();
        searchField.setPromptText("Search files...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color:transparent;"+
                "-fx-text-fill:"+WHITE+";-fx-prompt-text-fill:"+LIGHT+
                ";-fx-font-family:Arial;-fx-font-size:13px;");

        Label searchIcon=label("⌕",16,FontWeight.NORMAL,LIGHT);
        Label shortcut=label("⌘ K",10,FontWeight.BOLD,DARK);
        shortcut.setStyle("-fx-background-color:"+BG_INPUT+
                ";-fx-padding:3 6;-fx-background-radius:4;"+
                "-fx-text-fill:"+DARK+";");

        HBox searchBox=new HBox(8,searchIcon,searchField,shortcut);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0,10,0,12));
        searchBox.setMaxWidth(500);
        searchBox.setStyle("-fx-background-color:"+BG_SIDE+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:10;-fx-background-radius:10;");
        HBox.setHgrow(searchField,Priority.ALWAYS);

        Button bell=new Button("🔔");
        bell.setStyle("-fx-background-color:transparent;"+
                "-fx-text-fill:"+WHITE+";-fx-font-size:16px;");
        bell.setOnAction(e->LandingPage.showNotificationPage());

        Label avatar=label("AV",12,FontWeight.BOLD,WHITE);
        avatar.setPrefSize(34,34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:"+BLUE+
                ";-fx-background-radius:50%;");

        HBox profile=new HBox(8,bell,avatar,
                label("Aarav Verma",13,FontWeight.BOLD,WHITE),
                label("⌄",14,FontWeight.NORMAL,LIGHT));
        profile.setAlignment(Pos.CENTER);

        Region topSpace=space();

        HBox topBar=new HBox(20,searchBox,topSpace,profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16,24,8,24));

        Label title=label("Add Reminder",22,FontWeight.BOLD,WHITE);
        Label desc=label("Set a reminder for your important document or task.",
                13,FontWeight.NORMAL,LIGHT);

        Button close=new Button("×");
        close.setPrefSize(40,40);
        close.setFont(Font.font(FONT,25));
        close.setTextFill(Color.web(DARK));
        close.setStyle("-fx-background-color:"+BG_INPUT+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:8;-fx-background-radius:8;");
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
        descriptionField.setStyle("-fx-control-inner-background:"+BG_INPUT+
                ";-fx-background-color:"+BG_INPUT+
                ";-fx-text-fill:"+DARK+
                ";-fx-prompt-text-fill:"+MUTED+
                ";-fx-font-family:Arial;-fx-font-size:13px;");

        reminderTypeCombo=new ComboBox<>();
        reminderTypeCombo.getItems().addAll(
                "Document Reminder","Task Reminder",
                "Event Reminder","Deadline Reminder");
        reminderTypeCombo.setValue("Document Reminder");
        styleCombo(reminderTypeCombo);

        Button choose=new Button("📄  Choose a file");
        choose.setMaxWidth(Double.MAX_VALUE);
        choose.setPrefHeight(42);
        choose.setAlignment(Pos.CENTER_LEFT);
        choose.setTextFill(Color.web(DARK));
        choose.setStyle("-fx-background-color:"+BG_INPUT+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:8;-fx-background-radius:8;"+
                "-fx-font-family:Arial;");
        choose.setOnAction(e->chooseDocument(stage));

        selectedFileLabel=label("No file selected",11,
                FontWeight.NORMAL,MUTED);

        reminderDatePicker=new DatePicker();
        reminderDatePicker.setPromptText("dd/mm/yyyy");
        reminderDatePicker.setPrefHeight(42);
        reminderDatePicker.setMaxWidth(Double.MAX_VALUE);
        reminderDatePicker.setStyle("-fx-background-color:"+BG_INPUT+
                ";-fx-font-family:Arial;");

        reminderTimeField=new TextField();
        reminderTimeField.setPromptText("--:-- --");
        styleTextField(reminderTimeField);

        repeatCombo=new ComboBox<>();
        repeatCombo.getItems().addAll(
                "Does not repeat","Every day","Every week",
                "Every month","Every year");
        repeatCombo.setValue("Does not repeat");
        styleCombo(repeatCombo);

        priorityCombo=new ComboBox<>();
        priorityCombo.getItems().addAll(
                "High","Medium","Low");
        priorityCombo.setValue("Medium");
        styleCombo(priorityCombo);

        CheckBox notification=new CheckBox();
        notification.setSelected(true);

        HBox notificationBox=new HBox(12,notification,
                new VBox(2,
                        label("Enable notification",13,FontWeight.BOLD,DARK),
                        label("You will be notified on the selected date and time.",
                                11,FontWeight.NORMAL,MUTED)));
        notificationBox.setAlignment(Pos.CENTER_LEFT);

        VBox reminderTypeBox=new VBox(6,
                fieldLabel("Reminder Type"),reminderTypeCombo);

        VBox documentBox=new VBox(6,
                fieldLabel("Select Document (Optional)"),
                choose,selectedFileLabel);

        HBox typeFile=new HBox(18,reminderTypeBox,documentBox);
        HBox.setHgrow(reminderTypeBox,Priority.ALWAYS);
        HBox.setHgrow(documentBox,Priority.ALWAYS);

        HBox dateTime=row(
                field("Reminder Date *",reminderDatePicker),
                field("Reminder Time",reminderTimeField));

        HBox repeatPriority=row(
                field("Repeat",repeatCombo),
                field("Priority",priorityCombo));

        VBox details=new VBox(16,
                section("Reminder Details"),
                fieldLabel("Title *"),titleField,
                fieldLabel("Description"),descriptionField,
                typeFile,dateTime,repeatPriority,notificationBox);

        details.setPadding(new Insets(24));
        details.setStyle(card(BG_CARD));

        previewReminderType=label(
                reminderTypeCombo.getValue(),12,FontWeight.BOLD,BLUE);
        previewReminderType.setStyle("-fx-background-color:"+LIGHT_BLUE+
                ";-fx-text-fill:"+BLUE+
                ";-fx-padding:5 8;-fx-background-radius:5;");

        previewTitle=label("Reminder Title",18,FontWeight.BOLD,DARK);
        previewDescription=label(
                "Reminder description will appear here...",
                12,FontWeight.NORMAL,MUTED);
        previewDescription.setWrapText(true);

        previewDate=preview("▣","Select reminder date");
        previewTime=preview("◷","Select reminder time");
        previewRepeat=preview("⟳","Does not repeat");
        previewPriority=preview("⚑","Medium Priority");

        VBox previewInner=new VBox(14,
                label("🔔",20,FontWeight.NORMAL,BLUE),
                previewReminderType,previewTitle,
                previewDescription,new Separator(),
                previewDate,previewTime,previewRepeat,previewPriority);

        previewInner.setPadding(new Insets(24));
        previewInner.setStyle(card(BG_INNER));

        VBox previewCard=new VBox(
                section("Reminder Preview"),
                label("This is how your reminder will appear.",
                        12,FontWeight.NORMAL,MUTED),
                previewInner);

        previewCard.setPadding(new Insets(24));
        previewCard.setSpacing(8);
        previewCard.setStyle(card(BG_CARD));

        titleField.textProperty().addListener((o,a,b)->
                previewTitle.setText(
                        b.trim().isEmpty()?"Reminder Title":b.trim()));

        descriptionField.textProperty().addListener((o,a,b)->
                previewDescription.setText(
                        b.trim().isEmpty()?
                                "Reminder description will appear here...":
                                b.trim()));

        reminderTypeCombo.valueProperty().addListener((o,a,b)->
                previewReminderType.setText(b));

        reminderDatePicker.valueProperty().addListener((o,a,b)->
                previewDate.setText(
                        b==null?"▣  Select reminder date":
                        "▣  "+b.format(
                                DateTimeFormatter.ofPattern("dd MMM yyyy"))));

        reminderTimeField.textProperty().addListener((o,a,b)->
                previewTime.setText(
                        b.trim().isEmpty()?
                                "◷  Select reminder time":
                                "◷  "+b.trim()));

        repeatCombo.valueProperty().addListener((o,a,b)->
                previewRepeat.setText("⟳  "+b));

        priorityCombo.valueProperty().addListener((o,a,b)->
                previewPriority.setText("⚑  "+b+" Priority"));

        HBox columns=new HBox(20,details,previewCard);
        HBox.setHgrow(details,Priority.ALWAYS);
        HBox.setHgrow(previewCard,Priority.ALWAYS);

        Button cancel=new Button("Cancel");
        cancel.setPrefHeight(40);
        cancel.setTextFill(Color.web(DARK));
        cancel.setStyle("-fx-background-color:"+BG_INPUT+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:8;-fx-background-radius:8;");
        cancel.setOnAction(e->LandingPage.showCalendarPage());

        Button create=new Button("+  Create Reminder");
        create.setPrefHeight(40);
        create.setTextFill(Color.WHITE);
        create.setStyle("-fx-background-color:"+BLUE+
                ";-fx-background-radius:8;"+
                "-fx-font-weight:bold;");
        create.setOnAction(e->createReminder());

        HBox buttons=new HBox(10,cancel,create);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox content=new VBox(18,header,columns,buttons);
        content.setPadding(new Insets(8,24,20,24));

        ScrollPane scroll=new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:"+BG_APP+
                ";-fx-background:"+BG_APP+
                ";-fx-border-color:transparent;");

        VBox center=new VBox(topBar,scroll);
        VBox.setVgrow(scroll,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(center);
        root.setStyle("-fx-background-color:"+BG_APP+";");

        return new Scene(root,1200,750);
    }

    private void createReminder(){

        String title=titleField.getText().trim();

        if(title.isEmpty()){
            alert(Alert.AlertType.WARNING,
                    "Missing Title",
                    "Please enter a reminder title()");
            titleField.requestFocus();
            return;
        }

        if(reminderDatePicker.getValue()==null){
            alert(Alert.AlertType.WARNING,
                    "Missing Date",
                    "Please select a reminder date.");
            reminderDatePicker.requestFocus();
            return;
        }

        String date=reminderDatePicker.getValue().format(
                DateTimeFormatter.ofPattern("dd MMM yyyy"));

        String time=reminderTimeField.getText().trim();
        if(time.isEmpty()) time="Not specified";

        String msg="Reminder created successfully.\n\n"+
                "Title: "+title+
                "\nType: "+reminderTypeCombo.getValue()+
                "\nDate: "+date+
                "\nTime: "+time+
                "\nRepeat: "+repeatCombo.getValue()+
                "\nPriority: "+priorityCombo.getValue();

        alert(Alert.AlertType.INFORMATION,
                "Reminder Created",msg);

        LandingPage.showCalendarPage();
    }

    private void chooseDocument(Stage stage){

        FileChooser fc=new FileChooser();
        fc.setTitle("Choose Document");

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files","*.*"),
                new FileChooser.ExtensionFilter("PDF Files","*.pdf"),
                new FileChooser.ExtensionFilter(
                        "Documents","*.doc","*.docx"),
                new FileChooser.ExtensionFilter(
                        "Images","*.png","*.jpg","*.jpeg"));

        File file=fc.showOpenDialog(stage);

        if(file!=null)
            selectedFileLabel.setText("Selected: "+file.getName());
    }

    private Label fieldLabel(String text){
        return label(text,12,FontWeight.BOLD,DARK);
    }

    private Label section(String text){
        return label(text,16,FontWeight.BOLD,DARK);
    }

    private void styleTextField(TextField f){
        f.setPrefHeight(42);
        f.setMaxWidth(Double.MAX_VALUE);
        f.setStyle("-fx-background-color:"+BG_INPUT+
                ";-fx-control-inner-background:"+BG_INPUT+
                ";-fx-text-fill:"+DARK+
                ";-fx-prompt-text-fill:"+MUTED+
                ";-fx-font-family:Arial;-fx-font-size:13px;");
    }

    private void styleCombo(ComboBox<String> c){
        c.setPrefHeight(42);
        c.setMaxWidth(Double.MAX_VALUE);
        c.setStyle("-fx-background-color:"+BG_INPUT+
                ";-fx-border-color:"+BORDER+
                ";-fx-font-family:Arial;-fx-font-size:13px;"+
                "-fx-text-fill:"+DARK+";");
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
        return label(icon+"  "+text,13,FontWeight.NORMAL,MUTED);
    }

    private String card(String color){
        return "-fx-background-color:"+color+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:16;"+
                "-fx-background-radius:16;";
    }

    private Button side(String icon,String text,boolean active){

        Label i=label(icon,14,FontWeight.NORMAL,WHITE);
        Label t=label(text,13,
                active?FontWeight.BOLD:FontWeight.NORMAL,WHITE);

        HBox box=new HBox(12,i,t);
        box.setAlignment(Pos.CENTER_LEFT);

        Button b=new Button("",box);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(38);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(0,12,0,12));

        String normal=active?
                "-fx-background-color:"+BLUE+
                ";-fx-background-radius:8;":
                "-fx-background-color:transparent;"+
                "-fx-background-radius:8;";

        b.setStyle(normal);
        return b;
    }

    private Label label(String text,double size,
                        FontWeight weight,String color){

        Label l=new Label(text);
        l.setFont(Font.font(FONT,weight,size));
        l.setTextFill(Color.web(color));
        l.setStyle("-fx-text-fill:"+color+";");
        return l;
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