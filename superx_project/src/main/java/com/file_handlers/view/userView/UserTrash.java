package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class UserTrash {

    private static final String BG="#3A4D67",NAV="#1D2B3D",CARD="#F7FAFE",
            BORDER="#B8C9DC",BLUE="#2563EB",DARK="#111827",
            MUTED="#374151",WHITE="#FFFFFF",LIGHT="#D5DFEB";

    public Scene getTrashPageScene(){

        Button dashboard=nav("⌂","Dashboard",false),spaces=nav("▦","Spaces",false),
                search=nav("⌕","Search",false),calendar=nav("□","Calendar",false),
                ai=nav("✧","AI Assistant",false),collab=nav("♧","Collaboration",false),
                recent=nav("◷","Recent",false),trash=nav("♜","Trash",true),
                settings=nav("⚙","Settings",false);

        dashboard.setOnAction(e->LandingPage.showUserDashboard());
        spaces.setOnAction(e->LandingPage.showUserSpace());
        search.setOnAction(e->LandingPage.showUserSearch());
        calendar.setOnAction(e->LandingPage.showCalendarPage());
        trash.setOnAction(e->LandingPage.showTrashPage());

        VBox logo=new VBox(3,txt("⬡  OneSpace",20,true,WHITE),
                txt("Your AI Workspace",11,false,LIGHT));
        logo.setPadding(new Insets(0,0,15,8));

        Region grow=space();
        VBox.setVgrow(grow,Priority.ALWAYS);

        VBox side=new VBox(7,logo,dashboard,spaces,search,calendar,ai,
                collab,recent,trash,grow,settings);
        side.setPadding(new Insets(20,14,20,14));
        side.setPrefWidth(230);
        side.setStyle("-fx-background-color:"+NAV+";");

        TextField topSearch=new TextField();
        topSearch.setPromptText("Search in OneSpace...");
        topSearch.setPrefHeight(42);
        topSearch.setStyle(input());

        Button notification=new Button("🔔");
        notification.setPrefSize(42,42);
        notification.setTooltip(new Tooltip("Notifications"));
        notification.setStyle("-fx-background-color:transparent;"+
                "-fx-text-fill:white;-fx-font-size:18px;-fx-cursor:hand;");
        notification.setOnAction(e->LandingPage.showNotificationPage());

        Label av=txt("AV",11,true,WHITE);
        av.setAlignment(Pos.CENTER);
        av.setPrefSize(38,38);
        av.setStyle("-fx-background-color:"+BLUE+";-fx-background-radius:50%;");

        HBox top=new HBox(12,topSearch,space(),notification,av,
                txt("Aarav Verma  ⌄",13,true,WHITE));
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(16,24,14,24));
        top.setStyle("-fx-background-color:"+NAV+";");

        VBox title=new VBox(4,txt("Trash",26,true,WHITE),
                txt("Unlinked items from your Spaces. Original files remain untouched on your disk.",
                        13,false,LIGHT));

        HBox stats=new HBox(12,
                stat("📄",BLUE,"0","Items in Trash"),
                stat("🕒","#D97706","0.0 B","Total size"),
                stat("📅","#EF4444","30 days","Auto delete period"),
                stat("⏱","#059669","30 days","Expiry timeframe"));

        TextField trashSearch=new TextField();
        trashSearch.setPromptText("Search trash...");
        trashSearch.setPrefHeight(38);
        trashSearch.setStyle(input());

        Button type=filter("All types ⌄"),sf=filter("All Spaces ⌄"),
                sort=filter("Date deleted ↓");

        Button empty=new Button("🗑  Empty Trash");
        empty.setPrefHeight(38);
        empty.setTextFill(Color.web("#991B1B"));
        empty.setStyle("-fx-background-color:#FEE2E2;"+
                "-fx-border-color:#FCA5A5;-fx-border-radius:8;"+
                "-fx-background-radius:8;");

        HBox filters=new HBox(10,trashSearch,type,sf,sort,space(),empty);
        filters.setAlignment(Pos.CENTER_LEFT);

        HBox header=new HBox(15,col("□",40),col("Name",180),
                col("Original location",180),col("Space",120),
                col("Deleted on ↓",120),col("Size",100),
                col("Deleted by",120),col("Actions",80));
        header.setPadding(new Insets(12,16,12,16));
        header.setStyle("-fx-background-color:"+CARD+
                ";-fx-border-color:"+BORDER+";-fx-border-width:0 0 1 0;");

        Label icon=txt("▱",40,true,BLUE);
        StackPane iconBox=new StackPane(icon);
        iconBox.setPrefSize(70,70);
        iconBox.setStyle("-fx-background-color:#DBEAFE;-fx-background-radius:18;");

        Label emptyTitle=txt("Trash is empty",18,true,DARK);
        Label emptyDesc=txt("No items have been moved to Trash yet.\n"+
                "Deleted items from your Spaces will appear here.",
                12,false,MUTED);
        emptyDesc.setTextAlignment(TextAlignment.CENTER);

        Button browse=new Button("📁  Browse Spaces");
        browse.setPrefHeight(38);
        browse.setTextFill(Color.WHITE);
        browse.setStyle("-fx-background-color:"+BLUE+
                ";-fx-background-radius:8;");
        browse.setOnAction(e->LandingPage.showUserSpace());

        VBox body=new VBox(12,iconBox,emptyTitle,emptyDesc,browse);
        body.setAlignment(Pos.CENTER);
        body.setPadding(new Insets(55,20,55,20));
        body.setStyle("-fx-background-color:"+CARD+";");

        VBox table=new VBox(header,body);
        table.setStyle("-fx-background-color:"+CARD+
                ";-fx-border-color:"+BORDER+";-fx-border-radius:14;"+
                "-fx-background-radius:14;");

        Label warn=txt("🛡  How Trash works: Files in Trash are unlinked from Spaces. "+
                "Original files remain safe on disk until the auto-delete period finishes.",
                12,false,DARK);

        HBox footer=new HBox(warn);
        footer.setPadding(new Insets(12,16,12,16));
        footer.setStyle("-fx-background-color:#DBEAFE;"+
                "-fx-border-color:"+BORDER+";-fx-border-radius:12;"+
                "-fx-background-radius:12;");

        VBox content=new VBox(16,title,stats,filters,table,footer);
        content.setPadding(new Insets(0,24,24,24));

        ScrollPane scroll=new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:"+BG+
                ";-fx-background-color:"+BG+
                ";-fx-border-color:transparent;");

        VBox center=new VBox(top,scroll);
        VBox.setVgrow(scroll,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setLeft(side);
        root.setCenter(center);
        root.setStyle("-fx-background-color:"+BG+";");

        return new Scene(root,1200,750);
    }

    private Button nav(String icon,String name,boolean active){
        Button b=new Button(icon+"   "+name);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(43);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setFont(Font.font("Arial",
                active?FontWeight.BOLD:FontWeight.NORMAL,14));
        b.setTextFill(Color.WHITE);
        b.setStyle("-fx-background-color:"+
                (active?BLUE:"transparent")+
                ";-fx-background-radius:9;-fx-cursor:hand;");
        return b;
    }

    private HBox stat(String icon,String color,String value,String name){
        Label i=txt(icon,15,true,color);
        i.setPrefSize(34,34);
        i.setAlignment(Pos.CENTER);
        i.setStyle("-fx-background-color:#DBEAFE;-fx-background-radius:8;");

        VBox v=new VBox(2,txt(value,18,true,DARK),txt(name,11,true,MUTED));
        HBox b=new HBox(12,i,v);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(14));
        b.setStyle("-fx-background-color:"+CARD+
                ";-fx-border-color:"+BORDER+";-fx-border-radius:12;"+
                "-fx-background-radius:12;");
        HBox.setHgrow(b,Priority.ALWAYS);
        return b;
    }

    private Button filter(String s){
        Button b=new Button(s);
        b.setPrefHeight(38);
        b.setTextFill(Color.web(DARK));
        b.setStyle("-fx-background-color:"+CARD+
                ";-fx-border-color:"+BORDER+";-fx-border-radius:8;"+
                "-fx-background-radius:8;");
        return b;
    }

    private Label col(String s,double w){
        Label l=txt(s,11,true,DARK);
        l.setPrefWidth(w);
        return l;
    }

    private Label txt(String s,double size,boolean bold,String color){
        Label l=new Label(s);
        l.setFont(Font.font("Arial",
                bold?FontWeight.BOLD:FontWeight.NORMAL,size));
        l.setTextFill(Color.web(color));
        l.setStyle("-fx-text-fill:"+color+";");
        return l;
    }

    private String input(){
        return "-fx-background-color:"+CARD+
                ";-fx-control-inner-background:"+CARD+
                ";-fx-text-fill:"+DARK+
                ";-fx-prompt-text-fill:"+MUTED+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:10;-fx-background-radius:10;";
    }

    private Region space(){
        Region r=new Region();
        HBox.setHgrow(r,Priority.ALWAYS);
        return r;
    }
}