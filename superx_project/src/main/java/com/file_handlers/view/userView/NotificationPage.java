package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import java.util.*;

public class NotificationPage {
    private static final String FONT="Inter";
    private static final String APP="#3A4D67",NAV="#1D2B3D",CARD="#DDE8F5",
            INNER="#C8D8EA",INPUT="#172435",BORDER="#AFC4DA",
            BLUE="#2563EB",LIGHT_BLUE="#BFDBFE",DARK="#142338",
            MUTED="#405773",WHITE="#FFFFFF",LIGHT="#B8C7D8";

    private final List<N> data=new ArrayList<>();
    private VBox list;
    private String filter="All";

    public Scene getNotificationsScene(){
        init();

        Button dashboard=nav("⌂","Dashboard",false),
                spaces=nav("▣","Spaces",false),
                search=nav("⌕","Search",false),
                calendar=nav("▦","Calendar",false),
                ai=nav("✧","AI Assistant",false),
                collab=nav("♟","Collaboration",false),
                recent=nav("◷","Recent",false),
                trash=nav("▥","Trash",false),
                notifications=nav("🔔","Notifications",true),
                settings=nav("⚙","Settings",false);

        dashboard.setOnAction(e->LandingPage.showUserDashboard());
        spaces.setOnAction(e->LandingPage.showUserSpace());
        search.setOnAction(e->LandingPage.showUserSearch());
        calendar.setOnAction(e->LandingPage.showCalendarPage());
        ai.setOnAction(e->LandingPage.showLandingPage());
        collab.setOnAction(e->LandingPage.showLandingPage());
        recent.setOnAction(e->LandingPage.showLandingPage());
        trash.setOnAction(e->LandingPage.showLandingPage());
        notifications.setOnAction(e->LandingPage.showNotificationPage());
        settings.setOnAction(e->LandingPage.showLandingPage());

        Region sg=space();VBox.setVgrow(sg,Priority.ALWAYS);

        VBox side=new VBox(7,
                text("☁  OneSpace",18,true,WHITE),
                text("Your AI Workspace",11,false,LIGHT),
                dashboard,spaces,search,calendar,ai,collab,recent,trash,
                notifications,sg,settings);

        side.setPadding(new Insets(25,18,18,18));
        side.setPrefWidth(287);
        side.setStyle("-fx-background-color:"+NAV+";");

        TextField topSearch=new TextField();
        topSearch.setPromptText("Search in OneSpace...");
        topSearch.setPrefHeight(48);
        topSearch.setStyle("-fx-background-color:"+INPUT+
                ";-fx-text-fill:"+WHITE+
                ";-fx-prompt-text-fill:"+LIGHT+
                ";-fx-border-color:#304258"+
                ";-fx-border-radius:12"+
                ";-fx-background-radius:12;"+
                "-fx-font-size:14px;");

        Button bell=new Button("🔔");
        bell.setPrefSize(40,40);
        bell.setStyle("-fx-background-color:transparent;-fx-cursor:hand;");
        bell.setOnAction(e->LandingPage.showNotificationPage());

        Label avatar=text("AV",11,true,WHITE);
        avatar.setAlignment(Pos.CENTER);
        avatar.setPrefSize(44,44);
        avatar.setStyle("-fx-background-color:"+BLUE+
                ";-fx-background-radius:50%;");

        HBox top=new HBox(12,topSearch,space(),bell,avatar,
                text("Aarav Verma  ⌄",13,false,WHITE));
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(20,28,18,35));
        top.setStyle("-fx-background-color:"+NAV+";");

        Label title=text("Notifications",27,true,WHITE);
        Label sub=text(
                "Stay updated with OneSpace activity, reminders and collaboration.",
                13,false,LIGHT);

        Button mark=new Button("✓  Mark all read");
        mark.setPrefHeight(40);
        mark.setFont(Font.font(FONT,FontWeight.BOLD,12));
        mark.setTextFill(Color.web(BLUE));
        mark.setStyle("-fx-background-color:"+CARD+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:9;-fx-background-radius:9;");
        mark.setOnAction(e->{data.forEach(n->n.read=true);render();});

        HBox header=new HBox(new VBox(4,title,sub),space(),mark);
        header.setAlignment(Pos.CENTER_LEFT);

        Button all=filter("All",true),
                rem=filter("Reminders",false),
                col=filter("Collaboration",false);

        all.setOnAction(e->setFilter("All",all,rem,col));
        rem.setOnAction(e->setFilter("Reminders",rem,all,col));
        col.setOnAction(e->setFilter("Collaboration",col,all,rem));

        HBox filters=new HBox(8,all,rem,col);

        list=new VBox(12);
        render();

        Label warning=text(
                "⚠  OneSpace never deletes or moves files automatically. "+
                "Every suggested action requires your confirmation.",
                12,false,LIGHT);

        HBox warningBox=new HBox(warning);
        warningBox.setPadding(new Insets(14,16,14,16));
        warningBox.setStyle("-fx-background-color:"+INPUT+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:10;-fx-background-radius:10;");

        VBox content=new VBox(18,header,filters,list,warningBox);
        content.setPadding(new Insets(28,35,40,35));

        ScrollPane scroll=new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color:"+APP+
                ";-fx-background:"+APP+
                ";-fx-border-color:transparent;");

        VBox center=new VBox(top,scroll);
        VBox.setVgrow(scroll,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setLeft(side);
        root.setCenter(center);
        root.setStyle("-fx-background-color:"+APP+";");

        return new Scene(root,1200,750);
    }

    private void render(){
        list.getChildren().clear();

        for(N n:data)
            if(filter.equals("All")||n.type.equals(filter))
                list.getChildren().add(card(n));
    }

    private HBox card(N n){
        Label icon=text(n.icon,20,true,BLUE);
        icon.setAlignment(Pos.CENTER);
        icon.setPrefSize(46,46);
        icon.setStyle("-fx-background-color:"+LIGHT_BLUE+
                ";-fx-background-radius:10;");

        /* IMPORTANT: notification text colors are explicitly set */
        Label title=text(n.title,15,true,DARK);
        title.setStyle("-fx-text-fill:"+DARK+";");

        Label sub=text(n.sub,12,false,MUTED);
        sub.setStyle("-fx-text-fill:"+MUTED+";");

        VBox info=new VBox(4,title,sub);

        Label time=text(n.time,11,true,MUTED);
        Label dot=text(n.read?"":"●",10,true,BLUE);

        HBox row=new HBox(14,icon,info,space(),time,dot);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16,18,16,18));
        row.setMinHeight(78);

        row.setStyle("-fx-background-color:"+CARD+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-width:1;"+
                "-fx-border-radius:13;"+
                "-fx-background-radius:13;");

        row.setOnMouseEntered(e->row.setStyle(
                "-fx-background-color:"+INNER+
                ";-fx-border-color:"+BLUE+
                ";-fx-border-width:1;"+
                "-fx-border-radius:13;"+
                "-fx-background-radius:13;"));

        row.setOnMouseExited(e->row.setStyle(
                "-fx-background-color:"+CARD+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-width:1;"+
                "-fx-border-radius:13;"+
                "-fx-background-radius:13;"));

        row.setOnMouseClicked(e->{n.read=true;render();});

        return row;
    }

    private void setFilter(String f,Button selected,Button... others){
        this.filter=f;
        selected.setStyle(pill(true));
        for(Button b:others)b.setStyle(pill(false));
        render();
    }

    private Button filter(String s,boolean active){
        Button b=new Button(s);
        b.setPrefHeight(38);
        b.setPadding(new Insets(0,17,0,17));
        b.setFont(Font.font(FONT,FontWeight.BOLD,12));
        b.setTextFill(Color.web(active?WHITE:DARK));
        b.setStyle(pill(active));
        return b;
    }

    private String pill(boolean active){
        return "-fx-background-color:"+(active?BLUE:CARD)+
                ";-fx-text-fill:"+(active?WHITE:DARK)+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:18;"+
                "-fx-background-radius:18;";
    }

    private Button nav(String icon,String name,boolean active){
        Button b=new Button(icon+"   "+name);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(45);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setFont(Font.font(FONT,
                active?FontWeight.BOLD:FontWeight.NORMAL,14));
        b.setTextFill(Color.web(WHITE));
        b.setStyle("-fx-background-color:"+
                (active?BLUE:"transparent")+
                ";-fx-background-radius:9;");
        return b;
    }

    private Label text(String s,double size,boolean bold,String color){
        Label l=new Label(s);
        l.setFont(Font.font(FONT,
                bold?FontWeight.BOLD:FontWeight.NORMAL,size));
        l.setTextFill(Color.web(color));
        return l;
    }

    private Region space(){
        Region r=new Region();
        HBox.setHgrow(r,Priority.ALWAYS);
        return r;
    }

    private void init(){
        data.clear();

        data.add(new N("📄","12 duplicate files detected",
                "Downloads folder · 4.2 GB recoverable","1 h","Reminders"));

        data.add(new N("🛡","Sensitive files found",
                "Aadhaar, PAN and passport scans detected","3 h","Reminders"));

        data.add(new N("📅","Passport expires in 12 days",
                "Linked to Passport_Scan.pdf","5 h","Reminders"));

        data.add(new N("💬","Riya commented on a shared file",
                "Cloud_Computing_Seminar.pptx","Yesterday","Collaboration"));

        data.add(new N("✦","AI created 2 new Spaces",
                "Healthcare and Travel from 609 files","2 d","Reminders"));

        data.add(new N("👥","Priya Sharma uploaded SVM_Optimization.pdf",
                "Shared in College Presentation Workspace","2 d","Collaboration"));
    }

    private static class N{
        String icon,title,sub,time,type;
        boolean read=false;

        N(String i,String t,String s,String tm,String ty){
            icon=i;title=t;sub=s;time=tm;type=ty;
        }
    }
}