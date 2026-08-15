package com.file_handlers.view.userView;

import com.file_handlers.view.LandingPage;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import java.util.*;

public class UserSearch {
    private static final String F="Inter",APP="#3A4D67",NAV="#1D2B3D",CARD="#DDE8F5",
            INNER="#C8D9EA",INPUT="#172435",BORDER="#AFC5DB",BLUE="#2563EB",
            LB="#BFDBFE",DARK="#142338",MUTED="#405773",WHITE="#FFFFFF",LIGHT="#B8C7D8";

    private final List<FileInfo> files=Arrays.asList(
        new FileInfo("PDF","Aadhaar_Card_Scan.pdf","C:/Users/you/Documents/IDs","1.2 MB","12 Mar 2026"),
        new FileInfo("PDF","Java_Unit3_Notes.pdf","C:/Users/you/College/Notes","4.6 MB","02 Jul 2026"),
        new FileInfo("DOCX","DBMS_Assignment_4.docx","C:/Users/you/College/Assignments","780 KB","27 Jun 2026"),
        new FileInfo("PPTX","Computer_Graphics.pptx","C:/Users/you/College/Presentations","3.8 MB","18 Jul 2026"),
        new FileInfo("XLSX","College_Expenses.xlsx","C:/Users/you/Finance","620 KB","22 Jul 2026"),
        new FileInfo("JPG","College_Event.jpg","C:/Users/you/Pictures/College","2.4 MB","20 Jul 2026"),
        new FileInfo("PNG","OneSpace_Logo.png","C:/Users/you/Pictures/Projects","850 KB","10 Aug 2026"),
        new FileInfo("MP4","Project_Demo.mp4","C:/Users/you/Videos/Projects","24.5 MB","12 Aug 2026"),
        new FileInfo("AVI","College_Event.avi","C:/Users/you/Videos/College","18.2 MB","05 Aug 2026"));

    private VBox list;
    private GridPane grid;
    private StackPane box;
    private String type="All",query="";

    public Scene getUserSearchScene(){
        Button d=nav("⌂","Dashboard",false),s=nav("▣","Spaces",false),
                se=nav("⌕","Search",true),c=nav("▦","Calendar",false),
                a=nav("✧","AI Assistant",false),co=nav("♟","Collaboration",false),
                r=nav("◷","Recent",false),t=nav("▥","Trash",false),
                st=nav("⚙","Settings",false);

        d.setOnAction(e->LandingPage.showUserDashboard());
        s.setOnAction(e->LandingPage.showUserSpace());
        se.setOnAction(e->LandingPage.showUserSearch());
        c.setOnAction(e->LandingPage.showLandingPage());
        a.setOnAction(e->LandingPage.showLandingPage());
        co.setOnAction(e->LandingPage.showLandingPage());
        r.setOnAction(e->LandingPage.showLandingPage());
        t.setOnAction(e->LandingPage.showLandingPage());
        st.setOnAction(e->LandingPage.showLandingPage());

        Region sg=gap();VBox.setVgrow(sg,Priority.ALWAYS);
        VBox side=new VBox(6,label("☁  OneSpace",18,true,WHITE),
                label("Your AI Workspace",11,false,LIGHT),
                d,s,se,c,a,co,r,t,sg,st,storage());
        side.setPadding(new Insets(25,18,18,18));
        side.setPrefWidth(287);
        side.setStyle("-fx-background-color:"+NAV+";");

        TextField top=new TextField();
        top.setPromptText("Search in OneSpace...");
        top.setPrefHeight(48);
        top.setStyle("-fx-background-color:"+INPUT+";-fx-text-fill:"+WHITE+
                ";-fx-prompt-text-fill:"+LIGHT+";-fx-border-color:#304258;"+
                "-fx-border-radius:12;-fx-background-radius:12;-fx-font-size:14px;");

        Button bell=new Button("🔔");
        bell.setPrefSize(44,44);
        bell.setFont(Font.font(F,FontWeight.BOLD,18));
        bell.setTextFill(Color.WHITE);
        bell.setStyle("-fx-background-color:"+BLUE+
                ";-fx-text-fill:white;-fx-border-radius:10;"+
                "-fx-background-radius:10;-fx-cursor:hand;");
        bell.setTooltip(new Tooltip("Notifications"));
        bell.setOnAction(e->LandingPage.showNotificationPage());

        Label av=label("AV",11,true,WHITE);
        av.setAlignment(Pos.CENTER);
        av.setPrefSize(44,44);
        av.setStyle("-fx-background-color:"+BLUE+";-fx-background-radius:50%;");

        HBox topBar=new HBox(12,top,gap(),bell,av,
                label("Aarav Verma  ⌄",13,false,WHITE));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20,28,18,35));
        topBar.setStyle("-fx-background-color:"+NAV+";");

        Label title=label("Search files",27,true,WHITE);
        Label sub=label("Search and discover files indexed by OneSpace.",13,false,LIGHT);

        TextField search=new TextField();
        search.setPromptText("Search anything about your files...");
        search.setPrefHeight(52);
        search.setStyle("-fx-background-color:"+INPUT+
                ";-fx-text-fill:"+WHITE+
                ";-fx-prompt-text-fill:"+LIGHT+
                ";-fx-border-color:#40546D;-fx-border-radius:10;"+
                "-fx-background-radius:10;-fx-font-size:14px;");
        search.textProperty().addListener((o,x,y)->{query=y.toLowerCase();showList();});

        MenuButton filter=new MenuButton("Filter");
        menuStyle(filter);

        MenuItem all=new MenuItem("All Files"),docs=new MenuItem("Documents"),
                imgs=new MenuItem("Images"),vids=new MenuItem("Videos"),
                pdf=new MenuItem("PDFs");

        filter.getItems().addAll(all,docs,imgs,vids,pdf);

        all.setOnAction(e->change(filter,"All","All Files"));
        docs.setOnAction(e->change(filter,"Documents","Documents"));
        imgs.setOnAction(e->change(filter,"Images","Images"));
        vids.setOnAction(e->change(filter,"Videos","Videos"));
        pdf.setOnAction(e->change(filter,"PDFs","PDFs"));

        list=new VBox(14);
        grid=new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);
        box=new StackPane(list);
        showList();

        MenuButton view=new MenuButton("List View");
        menuStyle(view);

        MenuItem gv=new MenuItem("Grid View"),
                lv=new MenuItem("List View");
        view.getItems().addAll(gv,lv);

        gv.setOnAction(e->{showGrid();view.setText("Grid View");});
        lv.setOnAction(e->{showList();view.setText("List View");});

        VBox ai=new VBox(14,
                new HBox(label("AI Answer",16,true,DARK),gap(),
                        label("94% confidence",11,true,"#166534")),
                label("Found 6 matches for \"Invoices from June\". "+
                        "The strongest match is Aadhaar_Card_Scan.pdf stored in your Documents folder.",
                        13,false,DARK),
                new HBox(8,action("Open best match"),
                        action("Create reminder"),action("Add to Space")));
        ai.setPadding(new Insets(18));
        ai.setStyle(style(CARD,BORDER,15));

        HBox rh=new HBox(label("Results",17,true,WHITE),gap(),view);
        rh.setAlignment(Pos.CENTER_LEFT);

        VBox content=new VBox(18,title,sub,search,
                new HBox(10,filter),ai,rh,box);
        content.setPadding(new Insets(28,35,40,35));

        ScrollPane scroll=new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color:"+APP+
                ";-fx-background:"+APP+";-fx-border-color:transparent;");

        VBox center=new VBox(topBar,scroll);
        VBox.setVgrow(scroll,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setLeft(side);
        root.setCenter(center);
        root.setStyle("-fx-background-color:"+APP+";");

        return new Scene(root,1200,750);
    }

    private VBox storage(){
        VBox v=new VBox(7,label("Storage Used",12,true,WHITE),
                label("64.2 GB of 100 GB",13,true,WHITE),
                new ProgressBar(.64),label("64%",11,true,LIGHT),
                label("Manage Storage",11,false,"#60A5FA"));
        v.setPadding(new Insets(16));
        v.setStyle(style("#172435","#304258",14));
        return v;
    }

    private void change(MenuButton b,String t,String text){
        type=t;
        b.setText(text);
        showList();
    }

    private void showList(){
        list.getChildren().clear();
        for(FileInfo f:files)
            if(match(f)&&searchMatch(f))
                list.getChildren().add(file(f));

        if(list.getChildren().isEmpty())
            list.getChildren().add(label("No files found",14,true,WHITE));

        box.getChildren().setAll(list);
    }

    private void showGrid(){
        grid.getChildren().clear();
        int c=0,r=0;

        for(FileInfo f:files)
            if(match(f)&&searchMatch(f)){
                grid.add(file(f),c,r);
                if(++c==2){c=0;r++;}
            }

        if(grid.getChildren().isEmpty())
            grid.add(label("No files found",14,true,WHITE),0,0);

        box.getChildren().setAll(grid);
    }

    private boolean searchMatch(FileInfo f){
        return query.isEmpty()||
                f.name.toLowerCase().contains(query)||
                f.type.toLowerCase().contains(query)||
                f.path.toLowerCase().contains(query);
    }

    private boolean match(FileInfo f){
        if(type.equals("All"))return true;
        if(type.equals("PDFs"))return f.type.equals("PDF");
        if(type.equals("Documents"))
            return f.type.equals("DOCX")||f.type.equals("PPTX")||f.type.equals("XLSX");
        if(type.equals("Images"))
            return f.type.equals("JPG")||f.type.equals("PNG")||f.type.equals("JPEG");
        return type.equals("Videos")&&
                (f.type.equals("MP4")||f.type.equals("AVI")||f.type.equals("MKV"));
    }

    private VBox file(FileInfo f){
        Label ty=label(f.type,11,true,BLUE);
        ty.setPadding(new Insets(5,10,5,10));
        ty.setStyle("-fx-background-color:"+LB+
                ";-fx-text-fill:"+BLUE+";-fx-background-radius:7;");

        StackPane preview=new StackPane(label("FILE",20,true,BLUE));
        preview.setPrefHeight(75);
        preview.setStyle("-fx-background-color:"+INNER+
                ";-fx-background-radius:10;");

        Label name=label(f.name,15,true,DARK);
        Label path=label(f.path,11,false,MUTED);
        Label size=label(f.size,11,true,DARK);
        Label date=label(f.date,11,false,DARK);

        name.setStyle("-fx-text-fill:"+DARK+";");
        path.setStyle("-fx-text-fill:"+MUTED+";");
        size.setStyle("-fx-text-fill:"+DARK+";");
        date.setStyle("-fx-text-fill:"+DARK+";");

        HBox top=new HBox(ty,gap(),size);
        HBox bottom=new HBox(date,gap(),label("...",18,true,DARK));

        VBox v=new VBox(11,top,preview,name,path,bottom);
        v.setPadding(new Insets(16));
        v.setMinHeight(225);
        v.setMaxWidth(Double.MAX_VALUE);
        v.setStyle("-fx-background-color:"+CARD+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:15;-fx-background-radius:15;");

        return v;
    }

    private Button nav(String i,String n,boolean active){
        Button b=new Button(i+"   "+n);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(48);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setFont(Font.font(F,
                active?FontWeight.BOLD:FontWeight.NORMAL,14));
        b.setTextFill(Color.WHITE);
        b.setStyle("-fx-background-color:"+
                (active?BLUE:"transparent")+
                ";-fx-background-radius:9;-fx-cursor:hand;");
        return b;
    }

    private Button action(String s){
        Button b=new Button(s);
        b.setFont(Font.font(F,11));
        b.setTextFill(Color.web(BLUE));
        b.setStyle("-fx-background-color:"+INNER+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:8;-fx-background-radius:8;");
        return b;
    }

    private Label label(String s,double z,boolean bold,String c){
        Label l=new Label(s);
        l.setFont(Font.font(F,
                bold?FontWeight.BOLD:FontWeight.NORMAL,z));
        l.setTextFill(Color.web(c));
        l.setStyle("-fx-text-fill:"+c+";");
        return l;
    }

    private Region gap(){
        Region r=new Region();
        HBox.setHgrow(r,Priority.ALWAYS);
        return r;
    }

    private void menuStyle(MenuButton b){
        b.setPrefHeight(40);
        b.setFont(Font.font(F,12));
        b.setTextFill(Color.web(DARK));
        b.setStyle("-fx-background-color:"+CARD+
                ";-fx-border-color:"+BORDER+
                ";-fx-border-radius:9;-fx-background-radius:9;"+
                "-fx-text-fill:"+DARK+";");
    }

    private String style(String bg,String br,int r){
        return "-fx-background-color:"+bg+
                ";-fx-border-color:"+br+
                ";-fx-border-radius:"+r+
                ";-fx-background-radius:"+r+";";
    }

    private static class FileInfo{
        String type,name,path,size,date;
        FileInfo(String t,String n,String p,String s,String d){
            type=t;name=n;path=p;size=s;date=d;
        }
    }
}