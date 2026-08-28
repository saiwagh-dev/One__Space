package com.file_handlers.view.userView;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.model.FileData;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.awt.Desktop;
import java.io.File;
import java.util.List;

public class RecentPage{
    private static final String FONT="Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_SIDEBAR="#1E2A3A",BG_SIDEBAR_CARD="#141D29",SIDEBAR_BORDER="#2D3D52";
    private static final String BG_CENTER_CANVAS="#31435B",BG_CARD="#DDE8F8",BG_CARD_INNER="#CADDF2",BORDER_CARD="#C3D6EC";
    private static final String TEXT_DARK="#0F172A",TEXT_MUTED_DARK="#334155",TEXT_LIGHT="#FFFFFF",TEXT_MUTED_LIGHT="#94A3B8",PRIMARY_BLUE="#2563EB";
    private final FileDAO fileDAO=new FileDAO();
    private VBox fileRows;
    private Label emptyLabel;

    public Scene getRecentPageScene(){
        UserSession session=UserSession.getInstance();
        String activeUserName="User",initials="U";

        if(session!=null&&session.getDisplayName()!=null&&!session.getDisplayName().isBlank()){
            String[] parts=session.getDisplayName().trim().split("\\s+");
            activeUserName=parts[0];
            initials=activeUserName.substring(0,1).toUpperCase();
        }

        StackPane logoIcon=createLogo();
        Label logoText=label("OneSpace",19,FontWeight.BOLD,TEXT_LIGHT);
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
        Button recentBtn=createSidebarButton("🕒","Recent",true);
        Button trashBtn=createSidebarButton("🗑","Trash",false);
        Button settingsBtn=createSidebarButton("⚙","Settings",false);
        Button logoutBtn=createSidebarButton("🚪","Logout",false);

        dashboardBtn.setOnAction(e->LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e->LandingPage.showUserSpace());
        searchBtn.setOnAction(e->LandingPage.showUserSearch());
        calendarBtn.setOnAction(e->LandingPage.showCalendarPage());
        collabBtn.setOnAction(e->LandingPage.showCollaborationPage());
        aiBtn.setOnAction(e->LandingPage.showAiAssistantPage());
        recentBtn.setOnAction(e->LandingPage.showRecentPage());
        trashBtn.setOnAction(e->LandingPage.showTrashPage());
        settingsBtn.setOnAction(e->LandingPage.showSettingPage());
        logoutBtn.setOnAction(e->LandingPage.showUserLoginPage());

        VBox navList=new VBox(4,dashboardBtn,spacesBtn,searchBtn,calendarBtn,aiBtn,collabBtn,recentBtn,trashBtn);
        Region navGap=new Region();
        VBox.setVgrow(navGap,Priority.ALWAYS);

        Label storageTitle=label("Storage Used",12,FontWeight.SEMI_BOLD,TEXT_LIGHT);
        Label storageVal=label("64.2 GB of 100 GB",12,FontWeight.BOLD,TEXT_LIGHT);
        Label storagePercent=label("64%",11,FontWeight.BOLD,TEXT_MUTED_LIGHT);

        Region storageGap=new Region();
        HBox.setHgrow(storageGap,Priority.ALWAYS);

        HBox storageValGroup=new HBox(storageVal,storageGap,storagePercent);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar progress=new ProgressBar(.64);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setPrefHeight(6);
        progress.setStyle("-fx-accent:"+PRIMARY_BLUE+";-fx-control-inner-background:#0E1520;");

        VBox storageCard=new VBox(8,storageTitle,storageValGroup,progress);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color:"+BG_SIDEBAR_CARD+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-radius:12;-fx-background-radius:12;");

        VBox sidebar=new VBox(12,logoBox,navList,navGap,settingsBtn,logoutBtn,storageCard);
        sidebar.setPadding(new Insets(20,14,20,14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 1 0 0;");

        Label searchIcon=label("⌕",16,FontWeight.NORMAL,TEXT_MUTED_LIGHT);

        TextField searchField=new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color:transparent;-fx-prompt-text-fill:"+TEXT_MUTED_LIGHT+";-fx-font-size:13px;-fx-text-fill:"+TEXT_LIGHT+";");

        Label shortcut=label("⌘ K",10,FontWeight.SEMI_BOLD,TEXT_MUTED_LIGHT);
        shortcut.setStyle("-fx-background-color:#141E2C;-fx-text-fill:"+TEXT_MUTED_LIGHT+";-fx-padding:3 6;-fx-background-radius:4;");

        HBox searchBox=new HBox(8,searchIcon,searchField,shortcut);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0,12,0,14));
        searchBox.setPrefWidth(420);
        searchBox.setStyle("-fx-background-color:#141E2C;-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-radius:10;-fx-background-radius:10;");
        HBox.setHgrow(searchField,Priority.ALWAYS);

        Button bell=new Button("🔔");
        bell.setStyle("-fx-background-color:transparent;-fx-font-size:16px;-fx-text-fill:"+TEXT_LIGHT+";");
        bell.setOnAction(e->LandingPage.showNotificationPage());

        Label avatar=label(initials,12,FontWeight.BOLD,TEXT_LIGHT);
        avatar.setPrefSize(34,34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-background-radius:50%;");

        Label userName=label(activeUserName,13,FontWeight.SEMI_BOLD,TEXT_LIGHT);
        HBox profile=new HBox(10,bell,avatar,userName);
        profile.setAlignment(Pos.CENTER);

        Region topGap=new Region();
        HBox.setHgrow(topGap,Priority.ALWAYS);

        HBox topBar=new HBox(20,searchBox,topGap,profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16,28,14,28));
        topBar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 0 1 0;");

        Label title=label("Recent Files",24,FontWeight.BOLD,TEXT_LIGHT);
        Label sub=label("Files you've recently opened in OneSpace.",13,FontWeight.NORMAL,TEXT_MUTED_LIGHT);
        VBox header=new VBox(4,title,sub);

        HBox listHeader=new HBox(
                createHeaderLabel("Name",370),
                createHeaderLabel("Space",190),
                createHeaderLabel("Size",110),
                createHeaderLabel("Last Accessed",150)
        );
        listHeader.setPadding(new Insets(0,12,10,12));
        listHeader.setStyle("-fx-border-color:transparent transparent "+BORDER_CARD+" transparent;-fx-border-width:0 0 1 0;");

        fileRows=new VBox(6);
        emptyLabel=label("Loading recent files...",13,FontWeight.NORMAL,TEXT_MUTED_DARK);

        VBox recentCard=new VBox(12,listHeader,fileRows);
        recentCard.setPadding(new Insets(18));
        recentCard.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:16;-fx-background-radius:16;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.18),16,0,0,6;");

        VBox content=new VBox(22,header,recentCard);
        content.setPadding(new Insets(24,28,28,28));
        content.setStyle("-fx-background-color:"+BG_CENTER_CANVAS+";");

        ScrollPane scroll=new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:"+BG_CENTER_CANVAS+";-fx-background:"+BG_CENTER_CANVAS+";-fx-background-insets:0;-fx-padding:0;");

        VBox main=new VBox(topBar,scroll);
        VBox.setVgrow(scroll,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setStyle("-fx-background-color:"+BG_SIDEBAR+";");
        root.setLeft(sidebar);
        root.setCenter(main);

        loadRecentFiles();
        return new Scene(root,1200,750);
    }

    private void loadRecentFiles(){
        UserSession session=UserSession.getInstance();

        if(session==null||!UserSession.isLoggedIn()||session.getUid()==null||session.getUid().isBlank()){
            Platform.runLater(()->showEmpty("No authenticated user."));
            return;
        }

        Thread thread=new Thread(()->{
            try{
                List<FileData> files=fileDAO.getRecentFiles(session.getUid(),20);
                Platform.runLater(()->displayFiles(files));
            }catch(Exception e){
                e.printStackTrace();
                Platform.runLater(()->showEmpty("Unable to load recent files."));
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void displayFiles(List<FileData> files){
        fileRows.getChildren().clear();

        if(files==null||files.isEmpty()){
            showEmpty("No recently accessed files.");
            return;
        }

        for(FileData file:files)
            fileRows.getChildren().add(createFileRow(file));
    }

    private void showEmpty(String text){
        fileRows.getChildren().clear();
        fileRows.getChildren().add(emptyLabel);
        emptyLabel.setText(text);
    }

    private HBox createFileRow(FileData file){
        String name=file.getFileName()==null?"Unnamed file":file.getFileName();
        String space=file.getSpaceId()==null||file.getSpaceId().isBlank()?"Other":file.getSpaceId();

        Label icon=label(fileIcon(name),18,FontWeight.NORMAL,PRIMARY_BLUE);
        icon.setPrefSize(40,40);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-background-radius:10;");

        Label nameLabel=label(name,13,FontWeight.BOLD,TEXT_DARK);
        nameLabel.setMaxWidth(330);
        nameLabel.setEllipsisString("...");

        HBox nameGroup=new HBox(12,icon,nameLabel);
        nameGroup.setAlignment(Pos.CENTER_LEFT);
        nameGroup.setPrefWidth(370);

        Label spaceLabel=label(space,11,FontWeight.BOLD,PRIMARY_BLUE);
        spaceLabel.setPadding(new Insets(5,10,5,10));
        spaceLabel.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-text-fill:"+PRIMARY_BLUE+";-fx-background-radius:12;");

        HBox spaceGroup=new HBox(spaceLabel);
        spaceGroup.setAlignment(Pos.CENTER_LEFT);
        spaceGroup.setPrefWidth(190);

        Label size=label(formatSize(file.getFileSize()),12,FontWeight.MEDIUM,TEXT_MUTED_DARK);
        size.setPrefWidth(110);

        Label accessed=label(formatTimestamp(file),12,FontWeight.MEDIUM,TEXT_MUTED_DARK);
        accessed.setPrefWidth(150);

        HBox row=new HBox(nameGroup,spaceGroup,size,accessed);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8,12,8,12));
        row.setMinHeight(56);
        row.setStyle("-fx-background-color:transparent;-fx-background-radius:10;-fx-cursor:hand;");

        row.setOnMouseEntered(e->row.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-background-radius:10;-fx-cursor:hand;"));
        row.setOnMouseExited(e->row.setStyle("-fx-background-color:transparent;-fx-background-radius:10;-fx-cursor:hand;"));
        row.setOnMouseClicked(e->openFile(file));

        return row;
    }

    private void openFile(FileData file){
        if(file==null||file.getLocalPath()==null||file.getLocalPath().isBlank())return;

        UserSession session=UserSession.getInstance();

        try{
            File target=new File(file.getLocalPath());

            if(!target.exists()){
                showAlert("The file no longer exists at its stored location.");
                return;
            }

            if(!Desktop.isDesktopSupported()){
                showAlert("Opening files is not supported on this system.");
                return;
            }

            Desktop.getDesktop().open(target);

            if(session!=null&&session.getUid()!=null&&!session.getUid().isBlank()){
                Thread thread=new Thread(()->{
                    try{
                        fileDAO.touchFile(session.getUid(),file.getFileHash());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        }catch(Exception e){
            showAlert("Unable to open the file.");
        }
    }

    private String formatTimestamp(FileData file){
        if(file.getLastAccessedAt()==null)return "—";

        long minutes=(System.currentTimeMillis()-file.getLastAccessedAt().toDate().getTime())/60000;

        if(minutes<1)return "Just now";
        if(minutes<60)return minutes+" min ago";

        long hours=minutes/60;
        if(hours<24)return hours+" hr ago";

        long days=hours/24;
        if(days==1)return "Yesterday";
        if(days<7)return days+" days ago";

        return file.getLastAccessedAt().toDate().toString().substring(0,10);
    }

    private String formatSize(long bytes){
        if(bytes<=0)return "0 B";
        if(bytes<1024)return bytes+" B";
        if(bytes<1048576)return String.format("%.1f KB",bytes/1024.0);
        if(bytes<1073741824L)return String.format("%.1f MB",bytes/1048576.0);
        return String.format("%.1f GB",bytes/1073741824.0);
    }

    private String fileIcon(String name){
        String n=name.toLowerCase();
        if(n.matches(".*\\.(jpg|jpeg|png|gif|webp)$"))return "🖼";
        if(n.endsWith(".pdf"))return "📄";
        if(n.matches(".*\\.(doc|docx)$"))return "📝";
        if(n.matches(".*\\.(xls|xlsx)$"))return "📊";
        if(n.matches(".*\\.(ppt|pptx)$"))return "📽";
        if(n.matches(".*\\.(mp4|avi|mkv|mov)$"))return "🎬";
        if(n.matches(".*\\.(mp3|wav|m4a)$"))return "🎵";
        return "📁";
    }

    private StackPane createLogo(){
        Image image=new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
        ImageView view=new ImageView(image);
        view.setFitWidth(42);
        view.setFitHeight(42);
        view.setPreserveRatio(true);
        return new StackPane(view);
    }

    private Button createSidebarButton(String icon,String text,boolean active){
        Label iconLabel=label(icon,14,FontWeight.NORMAL,active?TEXT_LIGHT:TEXT_MUTED_LIGHT);
        Label textLabel=label(text,13,active?FontWeight.BOLD:FontWeight.MEDIUM,TEXT_LIGHT);

        HBox content=new HBox(12,iconLabel,textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button=new Button("",content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(38);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0,12,0,12));
        button.setStyle("-fx-background-color:"+(active?PRIMARY_BLUE:"transparent")+";-fx-background-radius:8;-fx-cursor:hand;");

        if(!active){
            button.setOnMouseEntered(e->button.setStyle("-fx-background-color:#26354A;-fx-background-radius:8;-fx-cursor:hand;"));
            button.setOnMouseExited(e->button.setStyle("-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;"));
        }

        return button;
    }

    private Label createHeaderLabel(String text,double width){
        Label label=label(text,12,FontWeight.BOLD,TEXT_MUTED_DARK);
        label.setPrefWidth(width);
        return label;
    }

    private Label label(String text,double size,FontWeight weight,String color){
        Label label=new Label(text);
        label.setFont(Font.font(FONT,weight,size));
        label.setStyle("-fx-text-fill:"+color+";");
        return label;
    }

    private void showAlert(String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("OneSpace");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}