package com.file_handlers.view.userView;

import com.file_handlers.dao.ReminderDAO;
import com.file_handlers.model.Reminder;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.google.cloud.Timestamp;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class UserCalendar{
    private static final String FONT="Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_SIDEBAR="#1E2A3A",BG_SIDEBAR_CARD="#141D29",SIDEBAR_BORDER="#2D3D52";
    private static final String BG_CENTER_CANVAS="#31435B",BG_CARD="#DDE8F8",BG_CARD_INNER="#CADDF2",BORDER_CARD="#C3D6EC",BG_INPUT="#EDF3FA";
    private static final String TEXT_DARK="#0F172A",TEXT_MUTED_DARK="#334155",TEXT_LIGHT="#FFFFFF",TEXT_MUTED_LIGHT="#94A3B8";
    private static final String PRIMARY_BLUE="#2563EB",ACCENT_LIGHT_BLUE="#BFDBFE",DANGER_RED="#DC2626";

    private int year=2026,month=8;
    private GridPane grid;
    private Button monthBtn,yearBtn;
    private VBox remindersList;
    private Label infoText;

    public Scene getCalendarPageScene(){
        String activeUserName="User",initials="U";

        if(UserSession.getInstance()!=null&&UserSession.getInstance().getDisplayName()!=null){
            String name=UserSession.getInstance().getDisplayName().trim();
            if(!name.isEmpty()){
                activeUserName=name.split("\\s+")[0];
                initials=activeUserName.substring(0,1).toUpperCase();
            }
        }

        StackPane logoIcon=createOneSpaceLogo();

        Label logoText=new Label("OneSpace");
        logoText.setFont(Font.font(FONT,FontWeight.BOLD,19));
        logoText.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        HBox logoHeader=new HBox(10,logoIcon,logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox=new VBox(4,logoHeader);
        logoBox.setPadding(new Insets(0,0,18,6));

        Button dashboardBtn=createSidebarButton("⌂","Dashboard",false);
        Button spacesBtn=createSidebarButton("📁","Spaces",false);
        Button searchBtn=createSidebarButton("⌕","Search",false);
        Button calendarBtn=createSidebarButton("📅","Calendar",true);
        Button aiBtn=createSidebarButton("✧","AI Assistant",false);
        Button collabBtn=createSidebarButton("👥","Collaboration",false);
        Button recentBtn=createSidebarButton("🕒","Recent",false);
        Button trashBtn=createSidebarButton("🗑","Trash",false);
        Button settingsBtn=createSidebarButton("⚙","Settings",false);
        Button logoutBtn=createSidebarButton("🚪","Logout",false);

        dashboardBtn.setOnAction(e->LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e->LandingPage.showUserSpace());
        searchBtn.setOnAction(e->LandingPage.showUserSearch());
        calendarBtn.setOnAction(e->LandingPage.showCalendarPage());
        aiBtn.setOnAction(e->LandingPage.showAiAssistantPage());
        collabBtn.setOnAction(e->LandingPage.showCollaborationPage());
        recentBtn.setOnAction(e->LandingPage.showRecentPage());
        trashBtn.setOnAction(e->LandingPage.showTrashPage());
        settingsBtn.setOnAction(e->LandingPage.showSettingPage());
        logoutBtn.setOnAction(e->{UserSession.clearSession();LandingPage.showUserLoginPage();});

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

        HBox storageValGroup=new HBox(storageVal,new Region(),storagePercent);
        HBox.setHgrow(storageValGroup.getChildren().get(1),Priority.ALWAYS);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress=new ProgressBar(.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent:"+PRIMARY_BLUE+";-fx-control-inner-background:#0E1520;");

        Button manageStorageBtn=new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,11));
        manageStorageBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:#60A5FA;-fx-padding:2 0 0 0;-fx-cursor:hand;");

        VBox storageCard=new VBox(8,storageTitle,storageValGroup,sidebarProgress,manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color:"+BG_SIDEBAR_CARD+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-radius:12;-fx-background-radius:12;");

        Region sidebarSpacer=new Region();
        VBox.setVgrow(sidebarSpacer,Priority.ALWAYS);

        VBox sidebar=new VBox(12,logoBox,navList,sidebarSpacer,settingsBtn,logoutBtn,storageCard);
        sidebar.setPadding(new Insets(20,14,20,14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 1 0 0;");

        Label searchIcon=new Label("⌕");
        searchIcon.setFont(Font.font(FONT,16));
        searchIcon.setStyle("-fx-text-fill:"+TEXT_MUTED_LIGHT+";");

        TextField searchField=new TextField();
        searchField.setPromptText("Search files or dates...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color:transparent;-fx-prompt-text-fill:"+TEXT_MUTED_LIGHT+";-fx-font-size:13px;-fx-text-fill:"+TEXT_LIGHT+";");

        Label keyShortcut=new Label("⌘ K");
        keyShortcut.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,10));
        keyShortcut.setStyle("-fx-background-color:#141E2C;-fx-text-fill:"+TEXT_MUTED_LIGHT+";-fx-padding:3 6;-fx-background-radius:4;");

        HBox searchContainer=new HBox(8,searchIcon,searchField,keyShortcut);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0,12,0,14));
        searchContainer.setPrefWidth(420);
        searchContainer.setStyle("-fx-background-color:#141E2C;-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-radius:10;-fx-background-radius:10;");
        HBox.setHgrow(searchField,Priority.ALWAYS);

        Button bellBtn=new Button("🔔");
        bellBtn.setStyle("-fx-background-color:transparent;-fx-font-size:16px;-fx-text-fill:"+TEXT_LIGHT+";-fx-cursor:hand;");
        bellBtn.setOnAction(e->LandingPage.showNotificationPage());

        Label avatar=new Label(initials);
        avatar.setPrefSize(34,34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-background-radius:50%;-fx-text-fill:"+TEXT_LIGHT+";-fx-font-weight:bold;-fx-font-size:12px;");

        Label userName=new Label(activeUserName);
        userName.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,13));
        userName.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        Label dropDown=new Label("⌄");
        dropDown.setStyle("-fx-text-fill:"+TEXT_MUTED_LIGHT+";");

        HBox profileOption=new HBox(8,avatar,userName,dropDown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(5,8,5,8));
        profileOption.setOnMouseClicked(e->LandingPage.showUserProfilePage());

        HBox profileBox=new HBox(10,bellBtn,profileOption);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar=new HBox(20,searchContainer,new Region(),profileBox);
        HBox.setHgrow(topBar.getChildren().get(1),Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16,28,14,28));
        topBar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 0 1 0;");

        Label pageTitle=new Label("Calendar & Reminders");
        pageTitle.setFont(Font.font(FONT,FontWeight.BOLD,24));
        pageTitle.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        Label pageDesc=new Label("Keep track of important dates, tasks, and document reminders.");
        pageDesc.setFont(Font.font(FONT,13));
        pageDesc.setStyle("-fx-text-fill:"+TEXT_MUTED_LIGHT+";-fx-font-weight:500;");

        VBox titleBox=new VBox(4,pageTitle,pageDesc);

        Button addReminderBtn=new Button("Add Reminder");
        addReminderBtn.setFont(Font.font(FONT,FontWeight.BOLD,13));
        addReminderBtn.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-text-fill:#FFFFFF;-fx-background-radius:10;-fx-cursor:hand;-fx-padding:8 18;");
        addReminderBtn.setOnAction(e->LandingPage.showAddReminderPage());

        HBox pageHeader=new HBox(titleBox,new Region(),addReminderBtn);
        HBox.setHgrow(pageHeader.getChildren().get(1),Priority.ALWAYS);
        pageHeader.setAlignment(Pos.CENTER_LEFT);

        monthBtn=new Button();
        yearBtn=new Button();
        styleCalendarHeaderPickerBtn(monthBtn);
        styleCalendarHeaderPickerBtn(yearBtn);
        updateCalendarHeader();

        monthBtn.setOnAction(e->showMonthPicker());
        yearBtn.setOnAction(e->showYearPicker());

        HBox monthYearBox=new HBox(4,monthBtn,yearBtn);

        Button prevBtn=createNavButton("‹");
        Button nextBtn=createNavButton("›");

        prevBtn.setOnAction(e->changeMonth(-1));
        nextBtn.setOnAction(e->changeMonth(1));

        HBox calendarHeader=new HBox(monthYearBox,new Region(),new HBox(6,prevBtn,nextBtn));
        HBox.setHgrow(calendarHeader.getChildren().get(1),Priority.ALWAYS);
        calendarHeader.setAlignment(Pos.CENTER_LEFT);

        grid=new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setMaxWidth(Double.MAX_VALUE);

        VBox calendarCard=new VBox(16,calendarHeader,grid);
        calendarCard.setPadding(new Insets(24));
        calendarCard.setStyle(createCardStyle());

        VBox calendarSection=new VBox(calendarCard);
        HBox.setHgrow(calendarSection,Priority.ALWAYS);

        Label reminderTitle=new Label("Upcoming Reminders");
        reminderTitle.setFont(Font.font(FONT,FontWeight.BOLD,17));
        reminderTitle.setStyle("-fx-text-fill:"+TEXT_LIGHT+";");

        remindersList=new VBox(10);
        remindersList.setPadding(new Insets(4));

        ScrollPane reminderScroll=new ScrollPane(remindersList);
        reminderScroll.setFitToWidth(true);
        reminderScroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        VBox.setVgrow(reminderScroll,Priority.ALWAYS);

        VBox remindersCard=new VBox(reminderScroll);
        remindersCard.setPadding(new Insets(16));
        remindersCard.setMinHeight(410);
        remindersCard.setStyle(createCardStyle());

        VBox remindersSection=new VBox(12,reminderTitle,remindersCard);
        remindersSection.setPrefWidth(350);

        HBox sectionsContainer=new HBox(20,calendarSection,remindersSection);
        HBox.setHgrow(calendarSection,Priority.ALWAYS);

        infoText=new Label("Loading reminders...");
        infoText.setFont(Font.font(FONT,FontWeight.MEDIUM,12));
        infoText.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");

        HBox infoBox=new HBox(8,new Label("ⓘ"),infoText);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        calendarCard.getChildren().add(infoBox);

        VBox contentBody=new VBox(22,pageHeader,sectionsContainer);
        contentBody.setPadding(new Insets(24,28,28,28));
        contentBody.setStyle("-fx-background-color:"+BG_CENTER_CANVAS+";");

        ScrollPane scrollPane=new ScrollPane(contentBody);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:"+BG_CENTER_CANVAS+";-fx-background:"+BG_CENTER_CANVAS+";-fx-background-insets:0;-fx-padding:0;");

        VBox mainArea=new VBox(topBar,scrollPane);
        VBox.setVgrow(scrollPane,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setStyle("-fx-background-color:"+BG_SIDEBAR+";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        loadCalendarData();

        return new Scene(root,1200,750);
    }

    private void loadCalendarData(){
        createCalendarGrid();
        remindersList.getChildren().setAll(new Label("Loading reminders..."));
        infoText.setText("Loading reminders...");

        if(!UserSession.isLoggedIn()){
            remindersList.getChildren().setAll(emptyLabel("Please log in to view reminders."));
            infoText.setText("No active user session.");
            return;
        }

        String uid=UserSession.getInstance().getUid();

        new Thread(()->{
            try{
                YearMonth ym=YearMonth.of(year,month);
                LocalDate startDate=ym.atDay(1);
                LocalDate endDate=ym.plusMonths(1).atDay(1);

                List<Reminder> monthReminders=new ReminderDAO().getRemindersForRange(uid,toTimestamp(startDate),toTimestamp(endDate));
                List<Reminder> upcoming=new ReminderDAO().getUpcoming(uid,8);

                Platform.runLater(()->{
                    createCalendarGrid(monthReminders);
                    updateUpcoming(upcoming);
                    infoText.setText(monthReminders.isEmpty()
                            ?"No reminders scheduled for this month."
                            :monthReminders.size()+" reminder"+(monthReminders.size()==1?"":"s")+" scheduled this month.");
                });
            }catch(Exception e){
                e.printStackTrace();

                Platform.runLater(()->{
                    createCalendarGrid();
                    remindersList.getChildren().setAll(emptyLabel("Unable to load reminders."));
                    infoText.setText("Could not load reminders.");
                });
            }
        }).start();
    }

    private void createCalendarGrid(){
        createCalendarGrid(Collections.emptyList());
    }

    private void createCalendarGrid(List<Reminder> reminders){
        grid.getChildren().clear();

        String[] days={"SUN","MON","TUE","WED","THU","FRI","SAT"};

        for(int i=0;i<7;i++){
            Label dayHeader=new Label(days[i]);
            dayHeader.setFont(Font.font(FONT,FontWeight.BOLD,11));
            dayHeader.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");
            dayHeader.setPrefSize(82,28);
            dayHeader.setAlignment(Pos.CENTER);
            grid.add(dayHeader,i,0);
        }

        Map<LocalDate,List<Reminder>> grouped=reminders.stream()
                .filter(r->r.getDate()!=null)
                .collect(Collectors.groupingBy(r->toLocalDate(r.getDate())));

        YearMonth ym=YearMonth.of(year,month);
        int daysInMonth=ym.lengthOfMonth();
        int col=ym.atDay(1).getDayOfWeek().getValue()%7;
        int row=1;
        LocalDate today=LocalDate.now();

        for(int day=1;day<=daysInMonth;day++){
            LocalDate date=ym.atDay(day);
            VBox cell=createDateCell(day,today);
            List<Reminder> dayReminders=grouped.getOrDefault(date,Collections.emptyList());

            for(int i=0;i<Math.min(dayReminders.size(),2);i++)
                addReminderBadge(cell,dayReminders.get(i));

            if(dayReminders.size()>2)
                addEventBadge(cell,"+"+(dayReminders.size()-2)+" more",PRIMARY_BLUE,ACCENT_LIGHT_BLUE);

            grid.add(cell,col,row);

            col++;

            if(col>6){
                col=0;
                row++;
            }
        }
    }

    private VBox createDateCell(int day,LocalDate today){
        boolean isToday=year==today.getYear()&&month==today.getMonthValue()&&day==today.getDayOfMonth();

        Label dayLabel=new Label(String.valueOf(day));
        dayLabel.setFont(Font.font(FONT,FontWeight.BOLD,12));
        dayLabel.setStyle("-fx-text-fill:"+(isToday?PRIMARY_BLUE:TEXT_DARK)+";");

        VBox cell=new VBox(4,dayLabel);
        cell.setPrefSize(82,65);
        cell.setPadding(new Insets(6));

        String normal=createDayCellStyle(isToday?ACCENT_LIGHT_BLUE:BG_CARD_INNER,isToday?PRIMARY_BLUE:BORDER_CARD);
        String hover=createDayCellStyle(ACCENT_LIGHT_BLUE,PRIMARY_BLUE);

        cell.setStyle(normal);
        cell.setOnMouseEntered(e->cell.setStyle(hover));
        cell.setOnMouseExited(e->cell.setStyle(normal));

        LocalDate date=LocalDate.of(year,month,day);
        cell.setOnMouseClicked(e->showDayEventsWindow(date));

        return cell;
    }

    private void addReminderBadge(VBox cell,Reminder reminder){
        String color=getPriorityColor(reminder.getPriority());
        String bg=getPriorityBackground(reminder.getPriority());

        Label badge=new Label(getTypeIcon(reminder.getType())+" "+reminder.getTitle());
        badge.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,9));
        badge.setMaxWidth(Double.MAX_VALUE);
        badge.setEllipsisString("...");
        badge.setStyle("-fx-text-fill:"+color+";-fx-background-color:"+bg+";-fx-background-radius:4;-fx-padding:2 4;");

        badge.setOnMouseClicked(e->{
            e.consume();
            showReminderDetails(reminder);
        });

        cell.getChildren().add(badge);
    }

    private void addEventBadge(VBox cell,String title,String textColor,String bgColor){
        Label badge=new Label(title);
        badge.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,9));
        badge.setMaxWidth(Double.MAX_VALUE);
        badge.setStyle("-fx-text-fill:"+textColor+";-fx-background-color:"+bgColor+";-fx-background-radius:4;-fx-padding:2 4;");
        cell.getChildren().add(badge);
    }

    private void updateUpcoming(List<Reminder> reminders){
        remindersList.getChildren().clear();

        if(reminders==null||reminders.isEmpty()){
            remindersList.getChildren().add(emptyLabel("No upcoming reminders."));
            return;
        }

        for(Reminder reminder:reminders)
            remindersList.getChildren().add(createReminderCard(reminder));
    }

    private VBox createReminderCard(Reminder reminder){
        LocalDate date=toLocalDate(reminder.getDate());
        String dateText=date==null?"Date unavailable":date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

        String time=reminder.getTime();

        if(time==null||time.isBlank())
            time="Time not specified";

        String accent=getPriorityColor(reminder.getPriority());

        Label dateLbl=new Label(dateText);
        dateLbl.setFont(Font.font(FONT,FontWeight.BOLD,11));
        dateLbl.setStyle("-fx-text-fill:"+accent+";");

        Label titleLbl=new Label(getTypeIcon(reminder.getType())+" "+reminder.getTitle());
        titleLbl.setFont(Font.font(FONT,FontWeight.BOLD,13));
        titleLbl.setStyle("-fx-text-fill:"+TEXT_DARK+";");
        titleLbl.setWrapText(true);

        Label subLbl=new Label(time+" • "+safe(reminder.getPriority(),"Medium")+" priority");
        subLbl.setFont(Font.font(FONT,FontWeight.MEDIUM,11));
        subLbl.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");

        VBox content=new VBox(2,dateLbl,titleLbl,subLbl);

        if(reminder.getLinkedFileName()!=null&&!reminder.getLinkedFileName().isBlank()){
            Label fileLbl=new Label("📄 "+reminder.getLinkedFileName());
            fileLbl.setFont(Font.font(FONT,11));
            fileLbl.setStyle("-fx-text-fill:"+PRIMARY_BLUE+";");
            fileLbl.setWrapText(true);
            content.getChildren().add(fileLbl);
        }

        VBox card=new VBox(content);
        card.setPadding(new Insets(10,12,10,12));
        card.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-border-color:"+BORDER_CARD+" "+BORDER_CARD+" "+BORDER_CARD+" "+accent+";-fx-border-radius:10;-fx-background-radius:10;-fx-border-width:1 1 1 4;");
        card.setOnMouseClicked(e->showReminderDetails(reminder));

        return card;
    }

    private void showDayEventsWindow(LocalDate date){
        Stage stage=new Stage();
        stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        stage.setTitle("Reminders");

        Label title=new Label("Reminders for "+date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
        title.setFont(Font.font(FONT,FontWeight.BOLD,18));
        title.setStyle("-fx-text-fill:"+TEXT_DARK+";");

        Label subtitle=new Label("Reminders scheduled for this date.");
        subtitle.setFont(Font.font(FONT,12));
        subtitle.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");

        VBox list=new VBox(12);
        list.setPadding(new Insets(4));

        Label loading=new Label("Loading...");
        loading.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");
        list.getChildren().add(loading);

        ScrollPane scroll=new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        VBox.setVgrow(scroll,Priority.ALWAYS);

        Button close=new Button("Close");
        close.setFont(Font.font(FONT,FontWeight.BOLD,13));
        close.setPrefWidth(100);
        close.setStyle("-fx-background-color:"+BG_INPUT+";-fx-text-fill:"+TEXT_DARK+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:8;-fx-background-radius:8;-fx-cursor:hand;");
        close.setOnAction(e->stage.close());

        VBox layout=new VBox(16,new VBox(4,title,subtitle),scroll,new HBox(close));
        layout.setPadding(new Insets(24));
        layout.setStyle("-fx-background-color:"+BG_CARD+";");

        stage.setScene(new Scene(layout,500,400));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

        if(!UserSession.isLoggedIn()){
            list.getChildren().setAll(emptyLabel("Please log in."));
            return;
        }

        new Thread(()->{
            try{
                LocalDate end=date.plusDays(1);

                List<Reminder> reminders=new ReminderDAO().getRemindersForRange(
                        UserSession.getInstance().getUid(),
                        toTimestamp(date),
                        toTimestamp(end)
                );

                Platform.runLater(()->{
                    list.getChildren().clear();

                    if(reminders.isEmpty()){
                        list.getChildren().add(emptyLabel("No reminders scheduled for this day."));
                        return;
                    }

                    for(Reminder reminder:reminders)
                        list.getChildren().add(createModalReminderCard(reminder));
                });
            }catch(Exception e){
                Platform.runLater(()->list.getChildren().setAll(emptyLabel("Unable to load reminders.")));
            }
        }).start();
    }

    private VBox createModalReminderCard(Reminder reminder){
        String accent=getPriorityColor(reminder.getPriority());

        Label title=new Label(getTypeIcon(reminder.getType())+" "+reminder.getTitle());
        title.setFont(Font.font(FONT,FontWeight.BOLD,14));
        title.setStyle("-fx-text-fill:"+TEXT_DARK+";");

        Label meta=new Label((reminder.getTime()==null?"":reminder.getTime())+" • "+safe(reminder.getPriority(),"Medium")+" priority");
        meta.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,11));
        meta.setStyle("-fx-text-fill:"+accent+";");

        Label desc=new Label(reminder.getDescription()==null||reminder.getDescription().isBlank()?"No description":reminder.getDescription());
        desc.setFont(Font.font(FONT,12));
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");

        VBox card=new VBox(6,title,meta,desc);

        if(reminder.getLinkedFileName()!=null&&!reminder.getLinkedFileName().isBlank()){
            Label file=new Label("📄 "+reminder.getLinkedFileName());
            file.setFont(Font.font(FONT,11));
            file.setStyle("-fx-text-fill:"+PRIMARY_BLUE+";");
            card.getChildren().add(file);
        }

        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-border-color:"+BORDER_CARD+" "+BORDER_CARD+" "+BORDER_CARD+" "+accent+";-fx-border-radius:10;-fx-background-radius:10;-fx-border-width:1 1 1 4;");
        return card;
    }

    private void showReminderDetails(Reminder reminder){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reminder");
        alert.setHeaderText(reminder.getTitle());

        StringBuilder text=new StringBuilder();
        text.append("Type: ").append(safe(reminder.getType(),"Reminder")).append("\n");
        text.append("Date: ").append(toLocalDate(reminder.getDate())).append("\n");
        text.append("Time: ").append(safe(reminder.getTime(),"Not specified")).append("\n");
        text.append("Repeat: ").append(safe(reminder.getRepeat(),"Does not repeat")).append("\n");
        text.append("Priority: ").append(safe(reminder.getPriority(),"Medium")).append("\n");

        if(reminder.getDescription()!=null&&!reminder.getDescription().isBlank())
            text.append("\n").append(reminder.getDescription());

        if(reminder.getLinkedFileName()!=null&&!reminder.getLinkedFileName().isBlank())
            text.append("\n\nFile: ").append(reminder.getLinkedFileName());

        alert.setContentText(text.toString());

        ButtonType deleteButton=new ButtonType("Delete",ButtonBar.ButtonData.LEFT);
        ButtonType closeButton=new ButtonType("Close",ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(deleteButton,closeButton);

        Optional<ButtonType> result=alert.showAndWait();

        if(result.isPresent()&&result.get()==deleteButton)
            deleteReminder(reminder);
    }

    private void deleteReminder(Reminder reminder){
        if(!UserSession.isLoggedIn()||reminder.getId()==null||reminder.getId().isBlank()){
            alert(Alert.AlertType.ERROR,"Delete Failed","Unable to identify this reminder.");
            return;
        }

        Alert confirm=new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Reminder");
        confirm.setHeaderText("Delete \""+reminder.getTitle()+"\"?");
        confirm.setContentText("This reminder will be permanently removed.");

        ButtonType delete=new ButtonType("Delete",ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel=new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);

        confirm.getButtonTypes().setAll(delete,cancel);

        Optional<ButtonType> result=confirm.showAndWait();

        if(result.isEmpty()||result.get()!=delete)
            return;

        new Thread(()->{
            try{
                new ReminderDAO().deleteReminder(
                        UserSession.getInstance().getUid(),
                        reminder.getId()
                );

                Platform.runLater(()->{
                    loadCalendarData();
                    alert(Alert.AlertType.INFORMATION,"Reminder Deleted","The reminder was deleted successfully.");
                });
            }catch(Exception e){
                Platform.runLater(()->alert(
                        Alert.AlertType.ERROR,
                        "Delete Failed",
                        e.getMessage()==null?"Unable to delete reminder.":e.getMessage()
                ));
            }
        }).start();
    }

    private void alert(Alert.AlertType type,String title,String message){
        Alert alert=new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Timestamp toTimestamp(LocalDate date){
        return Timestamp.of(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    private LocalDate toLocalDate(Timestamp timestamp){
        if(timestamp==null)return null;
        return timestamp.toSqlTimestamp().toLocalDateTime().toLocalDate();
    }

    private String getTypeIcon(String type){
        if(type==null)return "🔔";

        String t=type.toLowerCase();

        if(t.contains("document"))return "📄";
        if(t.contains("task"))return "✓";
        if(t.contains("event"))return "📅";
        if(t.contains("deadline"))return "⏰";

        return "🔔";
    }

    private String getPriorityColor(String priority){
        if(priority==null)return PRIMARY_BLUE;

        switch(priority.toLowerCase()){
            case "high":return DANGER_RED;
            case "low":return "#059669";
            default:return "#D97706";
        }
    }

    private String getPriorityBackground(String priority){
        if(priority==null)return ACCENT_LIGHT_BLUE;

        switch(priority.toLowerCase()){
            case "high":return "#FEE2E2";
            case "low":return "#A7F3D0";
            default:return "#FDE68A";
        }
    }

    private String safe(String value,String fallback){
        return value==null||value.isBlank()?fallback:value;
    }

    private Label emptyLabel(String text){
        Label label=new Label(text);
        label.setWrapText(true);
        label.setFont(Font.font(FONT,FontWeight.MEDIUM,12));
        label.setStyle("-fx-text-fill:"+TEXT_MUTED_DARK+";");
        return label;
    }

    private void changeMonth(int amount){
        month+=amount;

        if(month<1){
            month=12;
            year--;
        }else if(month>12){
            month=1;
            year++;
        }

        updateCalendarHeader();
        loadCalendarData();
    }

    private void updateCalendarHeader(){
        monthBtn.setText(Month.of(month).getDisplayName(TextStyle.FULL,Locale.ENGLISH));
        yearBtn.setText(String.valueOf(year));
    }

    private void showMonthPicker(){
        Popup popup=new Popup();
        VBox box=createPickerPopupBox();

        for(int i=1;i<=12;i++){
            final int selectedMonth=i;
            Button button=new Button(Month.of(i).getDisplayName(TextStyle.FULL,Locale.ENGLISH));

            applyPickerButtonStyle(button,i==month);

            button.setOnAction(e->{
                month=selectedMonth;
                popup.hide();
                updateCalendarHeader();
                loadCalendarData();
            });

            box.getChildren().add(button);
        }

        popup.getContent().add(box);
        showPopupRelativeToControl(popup,monthBtn);
    }

    private void showYearPicker(){
        Popup popup=new Popup();
        VBox box=createPickerPopupBox();

        for(int y=year-5;y<=year+5;y++){
            final int selectedYear=y;
            Button button=new Button(String.valueOf(y));

            applyPickerButtonStyle(button,y==year);

            button.setOnAction(e->{
                year=selectedYear;
                popup.hide();
                updateCalendarHeader();
                loadCalendarData();
            });

            box.getChildren().add(button);
        }

        popup.getContent().add(box);
        showPopupRelativeToControl(popup,yearBtn);
    }

    private VBox createPickerPopupBox(){
        VBox box=new VBox(4);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:10;-fx-background-radius:10;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.2),10,0,0,4);");
        return box;
    }

    private void applyPickerButtonStyle(Button b,boolean selected){
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(7,12,7,12));
        b.setFont(Font.font(FONT,selected?FontWeight.BOLD:FontWeight.MEDIUM,12));

        if(selected)
            b.setStyle("-fx-background-color:"+ACCENT_LIGHT_BLUE+";-fx-text-fill:"+PRIMARY_BLUE+";-fx-background-radius:6;-fx-cursor:hand;");
        else{
            b.setStyle("-fx-background-color:transparent;-fx-text-fill:"+TEXT_DARK+";-fx-background-radius:6;-fx-cursor:hand;");
            b.setOnMouseEntered(e->b.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-text-fill:"+TEXT_DARK+";-fx-background-radius:6;-fx-cursor:hand;"));
            b.setOnMouseExited(e->b.setStyle("-fx-background-color:transparent;-fx-text-fill:"+TEXT_DARK+";-fx-background-radius:6;-fx-cursor:hand;"));
        }
    }

    private void showPopupRelativeToControl(Popup popup,Control control){
        Point2D pos=control.localToScreen(0,control.getHeight());
        popup.setAutoHide(true);
        popup.show(control,pos.getX(),pos.getY());
    }

    private void styleCalendarHeaderPickerBtn(Button b){
        b.setFont(Font.font(FONT,FontWeight.BOLD,18));
        b.setStyle("-fx-background-color:transparent;-fx-text-fill:"+TEXT_DARK+";-fx-cursor:hand;-fx-padding:4 6;");
    }

    private Button createNavButton(String text){
        Button b=new Button(text);
        b.setPrefSize(34,34);
        b.setFont(Font.font(FONT,FontWeight.BOLD,16));
        b.setStyle("-fx-background-color:"+BG_INPUT+";-fx-text-fill:"+TEXT_DARK+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:8;-fx-background-radius:8;-fx-cursor:hand;");
        return b;
    }

    private StackPane createOneSpaceLogo(){
        Image logoImage=new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));

        ImageView logoView=new ImageView(logoImage);
        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane pane=new StackPane(logoView);
        pane.setPrefSize(42,42);
        pane.setAlignment(Pos.CENTER);

        return pane;
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

    private String createCardStyle(){
        return "-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:16;-fx-background-radius:16;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.18),16,0,0,6);";
    }

    private String createDayCellStyle(String bg,String border){
        return "-fx-background-color:"+bg+";-fx-border-color:"+border+";-fx-border-radius:8;-fx-background-radius:8;-fx-cursor:hand;";
    }
}