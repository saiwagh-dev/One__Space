package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Popup;

import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

public class UserCalender {

    private static final String FONT="Inter,-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif";
    private static final String APP="#3A4D67",CARD="#DDE8F5",SIDE="#2E3F55";
    private static final String INPUT="#EDF3FA",BORDER="#9FB7D0";
    private static final String BLUE="#2563EB",LIGHT="#BFDBFE";
    private static final String DARK="#142338",MUTED="#405773";
    private static final String WHITE="#FFFFFF",LIGHT_TEXT="#D5E0EC";

    private int year=2026,month=8;
    private GridPane grid;
    private Button monthBtn,yearBtn;

    public Scene getCalendarPageScene(){

        Button dashboard=side("⌂","Dashboard",false);
        Button spaces=side("▦","Spaces",false);
        Button search=side("⌕","Search",false);
        Button calendar=side("▣","Calendar",true);
        Button ai=side("✦","AI Assistant",false);
        Button collab=side("♧","Collaboration",false);
        Button recent=side("◷","Recent",false);
        Button trash=side("♜","Trash",false);
        Button settings=side("⚙","Settings",false);

        dashboard.setOnAction(e->LandingPage.showUserDashboard());
        spaces.setOnAction(e->LandingPage.showUserSpace());
        search.setOnAction(e->LandingPage.showUserSearch());
        calendar.setOnAction(e->LandingPage.showCalendarPage());

        Label logoIcon=label("⬡",25,FontWeight.BOLD,LIGHT);
        Label logoText=label("OneSpace",20,FontWeight.BOLD,WHITE);
        HBox logo=new HBox(8,logoIcon,logoText);
        logo.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox=new VBox(4,logo,
                label("Your AI Workspace",12,FontWeight.NORMAL,LIGHT_TEXT));
        logoBox.setPadding(new Insets(0,0,18,8));

        ProgressBar progress=new ProgressBar(.64);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle("-fx-accent:"+BLUE+
                ";-fx-control-inner-background:#52657D;");

        VBox storage=new VBox(8,
                label("Storage Used",12,FontWeight.BOLD,WHITE),
                new HBox(10,
                        label("64.2 GB of 100 GB",12,FontWeight.NORMAL,LIGHT_TEXT),
                        label("64%",12,FontWeight.BOLD,WHITE)),
                progress,
                label("Manage Storage ›",12,FontWeight.BOLD,LIGHT));

        storage.setPadding(new Insets(14));
        storage.setStyle("-fx-background-color:"+SIDE+
                ";-fx-background-radius:12;");

        Region sideSpace=new Region();
        VBox.setVgrow(sideSpace,Priority.ALWAYS);

        VBox nav=new VBox(4,dashboard,spaces,search,calendar,
                ai,collab,recent,trash);

        VBox sidebar=new VBox(10,logoBox,nav,sideSpace,settings,storage);
        sidebar.setPadding(new Insets(20,14,20,14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setMaxWidth(230);
        sidebar.setStyle("-fx-background-color:"+APP+";");

        TextField searchField=new TextField();
        searchField.setPromptText("Search files or dates...");
        searchField.setStyle("-fx-background-color:transparent;"+
                "-fx-text-fill:"+WHITE+
                ";-fx-prompt-text-fill:"+LIGHT_TEXT+
                ";-fx-font-size:13px;");

        Label searchIcon=label("⌕",18,FontWeight.NORMAL,LIGHT_TEXT);
        Label shortcut=label("⌘ K",10,FontWeight.BOLD,MUTED);
        shortcut.setStyle("-fx-background-color:"+INPUT+
                ";-fx-padding:4 7;-fx-background-radius:5;");

        HBox searchBox=new HBox(8,searchIcon,searchField,shortcut);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0,10,0,12));
        searchBox.setMaxWidth(500);
        searchBox.setStyle("-fx-background-color:"+SIDE+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:10;-fx-background-radius:10;");
        HBox.setHgrow(searchField,Priority.ALWAYS);

        Button bell=new Button("🔔");
        bell.setPrefSize(38,38);
        bell.setStyle("-fx-background-color:transparent;"+
                "-fx-font-size:17px;-fx-cursor:hand;");
        bell.setOnAction(e->LandingPage.showNotificationPage());

        Label avatar=label("AV",12,FontWeight.BOLD,WHITE);
        avatar.setPrefSize(36,36);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:"+BLUE+
                ";-fx-background-radius:50%;");

        HBox profile=new HBox(9,bell,avatar,
                label("Aarav Verma",13,FontWeight.BOLD,WHITE),
                label("⌄",14,FontWeight.NORMAL,LIGHT_TEXT));
        profile.setAlignment(Pos.CENTER);

        Region topSpace=new Region();
        HBox.setHgrow(topSpace,Priority.ALWAYS);

        HBox topBar=new HBox(20,searchBox,topSpace,profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16,24,16,24));

        Label title=label("Calendar & Reminders",22,FontWeight.BOLD,WHITE);
        Label desc=label(
                "Dates and reminders will automatically appear here once your files are scanned.",
                13,FontWeight.NORMAL,LIGHT_TEXT);

        VBox titleBox=new VBox(4,title,desc);

        Button add=new Button("+ Add Reminder");
        add.setPrefHeight(38);
        add.setPadding(new Insets(0,16,0,16));
        add.setFont(Font.font(FONT,FontWeight.BOLD,12));
        add.setTextFill(Color.WHITE);
        add.setStyle("-fx-background-color:"+BLUE+
                ";-fx-background-radius:8;-fx-cursor:hand;");
        add.setOnAction(e->LandingPage.showAddReminderPage());

        Region headerSpace=new Region();
        HBox.setHgrow(headerSpace,Priority.ALWAYS);

        HBox pageHeader=new HBox(20,titleBox,headerSpace,add);
        pageHeader.setAlignment(Pos.CENTER_LEFT);

        monthBtn=new Button();
        yearBtn=new Button();

        calBtn(monthBtn);
        calBtn(yearBtn);
        updateHeader();

        monthBtn.setOnAction(e->showMonthPicker());
        yearBtn.setOnAction(e->showYearPicker());

        HBox monthYear=new HBox(4,monthBtn,yearBtn);

        Button prev=navBtn("‹");
        Button next=navBtn("›");

        prev.setOnAction(e->changeMonth(-1));
        next.setOnAction(e->changeMonth(1));

        Region monthSpace=new Region();
        HBox.setHgrow(monthSpace,Priority.ALWAYS);

        HBox calendarHeader=new HBox(
                monthYear,monthSpace,new HBox(6,prev,next));
        calendarHeader.setAlignment(Pos.CENTER_LEFT);

        grid=new GridPane();
        grid.setHgap(6);
        grid.setVgap(6);
        grid.setMaxWidth(Double.MAX_VALUE);

        createCalendarGrid();

        HBox infoBox=new HBox(8,
                label("ⓘ",14,FontWeight.BOLD,MUTED),
                label("Key deadlines, events, and document dates will populate here.",
                        12,FontWeight.NORMAL,MUTED));

        VBox calendarCard=new VBox(16,calendarHeader,grid,infoBox);
        calendarCard.setPadding(new Insets(20));
        calendarCard.setStyle(card());

        VBox calendarSection=new VBox(calendarCard);
        HBox.setHgrow(calendarSection,Priority.ALWAYS);

        Label reminderTitle=
                label("Upcoming Reminders",16,FontWeight.BOLD,WHITE);

        Label noReminder=
                label("No reminders yet",16,FontWeight.BOLD,DARK);

        Label reminderDesc=label(
                "Your scheduled task reminders and deadlines will appear here once indexed.",
                12,FontWeight.NORMAL,MUTED);

        reminderDesc.setWrapText(true);
        reminderDesc.setTextAlignment(TextAlignment.CENTER);

        Label bellIcon=label("🔔",25,FontWeight.NORMAL,BLUE);

        VBox empty=new VBox(10,bellIcon,noReminder,reminderDesc);
        empty.setAlignment(Pos.CENTER);
        empty.setPadding(new Insets(20));

        VBox remindersCard=new VBox(empty);
        remindersCard.setAlignment(Pos.CENTER);
        remindersCard.setMinHeight(390);
        remindersCard.setStyle(card());

        VBox remindersSection=new VBox(12,reminderTitle,remindersCard);
        remindersSection.setPrefWidth(360);

        HBox sections=new HBox(20,calendarSection,remindersSection);
        HBox.setHgrow(calendarSection,Priority.ALWAYS);

        VBox content=new VBox(20,pageHeader,sections);
        content.setPadding(new Insets(0,24,24,24));
        VBox.setVgrow(sections,Priority.ALWAYS);

        VBox center=new VBox(topBar,content);
        VBox.setVgrow(content,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(center);
        root.setStyle("-fx-background-color:"+APP+";");

        return new Scene(root,1200,750);
    }

    private void createCalendarGrid(){

        grid.getChildren().clear();

        String[] days={"SAT","SUN","MON","TUE","WED","THU","FRI"};

        for(int i=0;i<7;i++){
            Label d=label(days[i],11,FontWeight.BOLD,MUTED);
            d.setPrefSize(82,28);
            d.setAlignment(Pos.CENTER);
            grid.add(d,i,0);
        }

        YearMonth ym=YearMonth.of(year,month);
        int daysInMonth=ym.lengthOfMonth();
        int col=(ym.atDay(1).getDayOfWeek().getValue()+1)%7;
        int row=1;

        LocalDate today=LocalDate.now();

        for(int day=1;day<=daysInMonth;day++){

            Label cell=label(String.valueOf(day),13,
                    FontWeight.BOLD,DARK);

            cell.setAlignment(Pos.TOP_LEFT);
            cell.setPadding(new Insets(8));
            cell.setPrefSize(82,62);

            boolean todayFlag=
                    year==today.getYear() &&
                    month==today.getMonthValue() &&
                    day==today.getDayOfMonth();

            String normal=todayFlag
                    ?cellStyle(LIGHT,BLUE,true)
                    :cellStyle("#F4F8FC",BORDER,false);

            cell.setStyle(normal);

            cell.setOnMouseEntered(e->
                    cell.setStyle(cellStyle(LIGHT,BLUE,true)));

            cell.setOnMouseExited(e->
                    cell.setStyle(normal));

            grid.add(cell,col,row);

            col++;

            if(col>6){
                col=0;
                row++;
            }
        }
    }

    private void changeMonth(int amount){

        month+=amount;

        if(month<1){
            month=12;
            year--;
        }

        if(month>12){
            month=1;
            year++;
        }

        updateHeader();
        createCalendarGrid();
    }

    private void updateHeader(){

        monthBtn.setText(Month.of(month).getDisplayName(
                TextStyle.FULL,Locale.ENGLISH));

        yearBtn.setText(String.valueOf(year));
    }

    private void showMonthPicker(){

        Popup popup=new Popup();
        VBox box=popupBox();

        for(int i=1;i<=12;i++){

            final int m=i;

            Button b=new Button(Month.of(i).getDisplayName(
                    TextStyle.FULL,Locale.ENGLISH));

            pickerButton(b,m==month);

            b.setOnAction(e->{
                month=m;
                popup.hide();
                updateHeader();
                createCalendarGrid();
            });

            box.getChildren().add(b);
        }

        popup.getContent().add(box);
        showPopup(popup,monthBtn);
    }

    private void showYearPicker(){

        Popup popup=new Popup();
        VBox box=popupBox();

        for(int y=year-5;y<=year+5;y++){

            final int selected=y;

            Button b=new Button(String.valueOf(y));
            pickerButton(b,y==year);

            b.setOnAction(e->{
                year=selected;
                popup.hide();
                updateHeader();
                createCalendarGrid();
            });

            box.getChildren().add(b);
        }

        popup.getContent().add(box);
        showPopup(popup,yearBtn);
    }

    private VBox popupBox(){

        VBox box=new VBox(4);
        box.setPadding(new Insets(10));

        box.setStyle("-fx-background-color:"+CARD+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:10;-fx-background-radius:10;");

        return box;
    }

    private void pickerButton(Button b,boolean selected){

        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(7,12,7,12));

        b.setTextFill(Color.web(selected?BLUE:DARK));

        b.setStyle(selected
                ?"-fx-background-color:"+LIGHT+
                 ";-fx-background-radius:6;-fx-font-weight:bold;"
                :"-fx-background-color:transparent;"+
                 "-fx-background-radius:6;");
    }

    private void showPopup(Popup popup,Control c){

        Point2D p=c.localToScreen(0,c.getHeight());

        popup.setAutoHide(true);
        popup.show(c,p.getX(),p.getY());
    }

    private void calBtn(Button b){
        b.setFont(Font.font(FONT,FontWeight.BOLD,17));
        b.setTextFill(Color.WHITE);
        b.setPadding(new Insets(4,8,4,8));
        b.setStyle(
            "-fx-background-color:transparent;"+
            "-fx-text-fill:white;"+
            "-fx-font-weight:bold;"+
            "-fx-cursor:hand;"
    );
    }

    private Button side(String icon,String text,boolean active){

        Label i=label(icon,15,FontWeight.NORMAL,
                active?WHITE:LIGHT_TEXT);

        Label t=label(text,13,
                active?FontWeight.BOLD:FontWeight.MEDIUM,
                WHITE);

        HBox box=new HBox(12,i,t);
        box.setAlignment(Pos.CENTER_LEFT);

        Button b=new Button("",box);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(40);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(0,12,0,12));

        String normal=active
                ?"-fx-background-color:"+BLUE+
                 ";-fx-background-radius:9;-fx-cursor:hand;"
                :"-fx-background-color:transparent"+
                 ";-fx-background-radius:9;-fx-cursor:hand;";

        b.setStyle(normal);

        if(!active){

            b.setOnMouseEntered(e->b.setStyle(
                    "-fx-background-color:"+SIDE+
                    ";-fx-background-radius:9;"));

            b.setOnMouseExited(e->b.setStyle(normal));
        }

        return b;
    }

    private Button navBtn(String text){

        Button b=new Button(text);

        b.setPrefSize(34,34);
        b.setFont(Font.font(FONT,FontWeight.BOLD,18));
        b.setTextFill(Color.web(DARK));

        b.setStyle("-fx-background-color:"+INPUT+
                ";-fx-text-fill:"+DARK+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:8;"+
                "-fx-background-radius:8;"+
                "-fx-cursor:hand;");

        return b;
    }

    private Label label(String text,double size,
                        FontWeight weight,String color){

        Label l=new Label(text);

        l.setFont(Font.font(FONT,weight,size));
        l.setTextFill(Color.web(color));

        return l;
    }

    private String card(){

        return "-fx-background-color:"+CARD+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:16;"+
                "-fx-background-radius:16;";
    }

    private String cellStyle(String bg,String border,boolean today){

        return "-fx-background-color:"+bg+
                ";-fx-border-color:"+border+
                ";-fx-border-radius:8;"+
                "-fx-background-radius:8;"+
                "-fx-text-fill:"+DARK+";"+
                "-fx-cursor:hand;"+
                (today
                        ?"-fx-font-weight:bold;-fx-text-fill:"+BLUE+";"
                        :"");
    }
}