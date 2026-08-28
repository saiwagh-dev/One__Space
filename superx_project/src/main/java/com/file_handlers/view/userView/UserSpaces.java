package com.file_handlers.view.userView;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.model.FileData;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.google.cloud.Timestamp;

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

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSpaces {
    private static final String FONT="Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_SIDEBAR="#1E2A3A",BG_SIDEBAR_CARD="#141D29",SIDEBAR_BORDER="#2D3D52",BG_CENTER="#31435B";
    private static final String BG_CARD="#DDE8F8",BG_CARD_HOVER="#EBF2FC",BORDER="#C3D6EC",TEXT="#0F172A",MUTED="#334155",LIGHT="#FFFFFF",MUTED_LIGHT="#94A3B8",BLUE="#2563EB";
    private final FileDAO fileDAO=new FileDAO();

    private final List<SpaceInfo> spaces=List.of(
        new SpaceInfo("Personal","personal","IDs, certificates, personal photos and everyday documents.","👤","#F3E8FF","#7C3AED"),
        new SpaceInfo("College","college","Notes, assignments, lab records, presentations and projects.","🎓","#BAE6FD","#0284C7"),
        new SpaceInfo("Office","office","Contracts, reports, decks and client deliverables.","💼","#A7F3D0","#059669"),
        new SpaceInfo("Finance","finance","Invoices, tax filings, statements and receipts.","💳","#FDE68A","#D97706"),
        new SpaceInfo("Entertainment","entertainment","Photos, videos, movies, music and other entertainment files.","💖","#FBCFE8","#DB2777"),
        new SpaceInfo("Others","other","Files that do not clearly belong to another space.","📁","#BFDBFE","#2563EB")
    );

    public Scene getUserSpacesScene(){
        UserSession session=UserSession.getInstance();
        String user="User",initials="U";

        if(session!=null&&session.getDisplayName()!=null&&!session.getDisplayName().isBlank()){
            user=session.getDisplayName().trim().split("\\s+")[0];
            initials=user.substring(0,1).toUpperCase();
        }

        VBox sidebar=createSidebar();

        TextField searchField=new TextField();
        searchField.setPromptText("Search in OneSpace...");
        searchField.setPrefWidth(540);
        searchField.setStyle("-fx-background-color:#141E2C;-fx-text-fill:white;-fx-prompt-text-fill:#94A3B8;-fx-background-radius:10;-fx-border-color:"+SIDEBAR_BORDER+";-fx-padding:0 14;");

        Button notification=new Button("🔔");
        notification.setStyle("-fx-background-color:transparent;-fx-text-fill:white;-fx-font-size:16px;");
        notification.setOnAction(e->LandingPage.showNotificationPage());

        Label avatar=new Label(initials);
        avatar.setPrefSize(34,34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:"+BLUE+";-fx-background-radius:50%;-fx-text-fill:white;-fx-font-weight:bold;");

        Label userLabel=label(user,13,FontWeight.SEMI_BOLD,LIGHT);
        HBox profile=new HBox(8,avatar,userLabel,new Label("⌄"));
        profile.setAlignment(Pos.CENTER);
        profile.setOnMouseClicked(e->LandingPage.showUserProfilePage());

        Region topGap=new Region();
        HBox.setHgrow(topGap,Priority.ALWAYS);

        HBox topBar=new HBox(20,searchField,topGap,notification,profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16,28,14,28));
        topBar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 0 1 0;");

        Label title=label("Spaces",24,FontWeight.BOLD,LIGHT);
        Label description=label("Virtual groupings built by AI. Files remain in their original folders.",13,FontWeight.NORMAL,MUTED_LIGHT);
        VBox titleBox=new VBox(4,title,description);

        GridPane grid=new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);

        for(int i=0;i<3;i++){
            ColumnConstraints c=new ColumnConstraints();
            c.setPercentWidth(33.33);
            grid.getColumnConstraints().add(c);
        }

        Map<String,SpaceCardView> cards=new HashMap<>();

        for(int i=0;i<spaces.size();i++){
            SpaceInfo info=spaces.get(i);
            SpaceCardView card=createSpaceCard(info);
            cards.put(info.spaceId,card);
            grid.add(card.card,i%3,i/3);
        }

        Label footer=label("ⓘ Loading spaces...",12,FontWeight.NORMAL,MUTED_LIGHT);

        VBox content=new VBox(22,titleBox,grid,footer);
        content.setPadding(new Insets(24,28,28,28));
        content.setStyle("-fx-background-color:"+BG_CENTER+";");

        ScrollPane scroll=new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:"+BG_CENTER+";-fx-background:"+BG_CENTER+";-fx-padding:0;");

        VBox center=new VBox(topBar,scroll);
        VBox.setVgrow(scroll,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(center);
        root.setStyle("-fx-background-color:"+BG_CENTER+";");

        loadSpaceStatistics(cards,footer);

        return new Scene(root,1200,750);
    }

    private VBox createSidebar(){
        Image image=new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
        ImageView view=new ImageView(image);
        view.setFitWidth(42);
        view.setFitHeight(42);
        view.setPreserveRatio(true);

        HBox logoRow=new HBox(10,view,label("OneSpace",19,FontWeight.BOLD,LIGHT));
        logoRow.setAlignment(Pos.CENTER_LEFT);

        Button dashboard=side("⌂","Dashboard",false,e->LandingPage.showUserDashboard());
        Button spacesBtn=side("📁","Spaces",true,e->LandingPage.showUserSpace());
        Button search=side("⌕","Search",false,e->LandingPage.showUserSearch());
        Button calendar=side("📅","Calendar",false,e->LandingPage.showCalendarPage());
        Button ai=side("✧","AI Assistant",false,e->LandingPage.showAiAssistantPage());
        Button collab=side("👥","Collaboration",false,e->LandingPage.showCollaborationPage());
        Button recent=side("🕒","Recent",false,e->LandingPage.showRecentPage());
        Button trash=side("🗑","Trash",false,e->LandingPage.showTrashPage());
        Button settings=side("⚙","Settings",false,e->LandingPage.showSettingPage());
        Button logout=side("🚪","Logout",false,e->LandingPage.showUserLoginPage());

        Region gap=new Region();
        VBox.setVgrow(gap,Priority.ALWAYS);

        Label storageTitle=label("Storage Used",12,FontWeight.SEMI_BOLD,LIGHT);
        Label storageValue=label("Loading...",12,FontWeight.BOLD,LIGHT);

        ProgressBar storageBar=new ProgressBar(0);
        storageBar.setMaxWidth(Double.MAX_VALUE);
        storageBar.setPrefHeight(6);
        storageBar.setStyle("-fx-accent:"+BLUE+";-fx-control-inner-background:#0E1520;");

        VBox storageCard=new VBox(8,storageTitle,storageValue,storageBar);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color:"+BG_SIDEBAR_CARD+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-radius:12;-fx-background-radius:12;");

        VBox sidebar=new VBox(
                8,logoRow,dashboard,spacesBtn,search,calendar,ai,collab,recent,trash,
                gap,settings,logout,storageCard
        );

        sidebar.setPadding(new Insets(20,14,20,14));
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 1 0 0;");

        return sidebar;
    }

    private Button side(String icon,String text,boolean active,javafx.event.EventHandler<javafx.event.ActionEvent> action){
        HBox h=new HBox(
                12,
                label(icon,14,FontWeight.NORMAL,active?LIGHT:MUTED_LIGHT),
                label(text,13,active?FontWeight.BOLD:FontWeight.MEDIUM,LIGHT)
        );
        h.setAlignment(Pos.CENTER_LEFT);

        Button b=new Button("",h);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(38);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(0,12,0,12));
        b.setStyle("-fx-background-color:"+(active?BLUE:"transparent")+";-fx-background-radius:8;-fx-cursor:hand;");
        b.setOnAction(action);

        if(!active){
            b.setOnMouseEntered(e->b.setStyle("-fx-background-color:#26354A;-fx-background-radius:8;"));
            b.setOnMouseExited(e->b.setStyle("-fx-background-color:transparent;-fx-background-radius:8;"));
        }

        return b;
    }

    private SpaceCardView createSpaceCard(SpaceInfo info){
        Label icon=label(info.icon,16,FontWeight.NORMAL,info.iconTextColor);
        icon.setPrefSize(38,38);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color:"+info.iconBackground+";-fx-background-radius:50%;-fx-text-fill:"+info.iconTextColor+";");

        Label title=label(info.name,16,FontWeight.BOLD,TEXT);

        Label description=label(info.description,12,FontWeight.NORMAL,MUTED);
        description.setWrapText(true);
        description.setMinHeight(36);

        Label files=label("0 files",12,FontWeight.BOLD,TEXT);
        Label size=label("—",12,FontWeight.BOLD,TEXT);
        Label updated=label("No files yet",11,FontWeight.NORMAL,MUTED);

        Region spacer=new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);

        HBox statsRow=new HBox(files,spacer,size);

        VBox stats=new VBox(2,statsRow,updated);
        stats.setPadding(new Insets(10,0,0,0));
        stats.setStyle("-fx-border-color:"+BORDER+";-fx-border-width:1 0 0 0;");

        VBox card=new VBox(10,icon,title,description,stats);
        card.setPadding(new Insets(18));

        String normal="-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER+";-fx-border-radius:14;-fx-background-radius:14;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.14),12,0,0,4);-fx-cursor:hand;";
        String hover="-fx-background-color:"+BG_CARD_HOVER+";-fx-border-color:"+BLUE+";-fx-border-radius:14;-fx-background-radius:14;-fx-effect:dropshadow(three-pass-box,rgba(37,99,235,0.22),16,0,0,6);-fx-cursor:hand;";

        card.setStyle(normal);
        card.setOnMouseEntered(e->card.setStyle(hover));
        card.setOnMouseExited(e->card.setStyle(normal));
        card.setOnMouseClicked(e->LandingPage.showUnifiedSpace(info.spaceId,info.name));

        return new SpaceCardView(card,files,size,updated);
    }

    private void loadSpaceStatistics(
            Map<String,SpaceCardView> cards,
            Label footer){

        UserSession session=UserSession.getInstance();

        if(session==null||!UserSession.isLoggedIn()||
                session.getUid()==null||
                session.getUid().isBlank()){

            footer.setText("ⓘ No authenticated user");
            return;
        }

        Thread thread=new Thread(()->{
            try{
                List<FileData> files=
                        fileDAO.getFileSummaries(
                                session.getUid()
                        );

                Map<String,SpaceStats> stats=
                        new HashMap<>();

                long totalSize=0;

                for(FileData file:files){

                    String spaceId=file.getSpaceId();

                    if(spaceId==null||spaceId.isBlank())
                        continue;

                    SpaceStats stat=
                            stats.computeIfAbsent(
                                    spaceId,
                                    key->new SpaceStats()
                            );

                    stat.fileCount++;
                    stat.totalSize+=file.getFileSize();
                    totalSize+=file.getFileSize();

                    Timestamp uploadedAt=
                            file.getUploadedAt();

                    Instant time=
                            uploadedAt==null
                                    ? null
                                    : uploadedAt
                                            .toDate()
                                            .toInstant();

                    if(time!=null&&(
                            stat.latestUpdate==null||
                            time.isAfter(stat.latestUpdate))){

                        stat.latestUpdate=time;
                    }
                }

                final long finalTotalSize=totalSize;

                Platform.runLater(()->{

                    for(SpaceInfo info:spaces){

                        SpaceStats stat=
                                stats.getOrDefault(
                                        info.spaceId,
                                        new SpaceStats()
                                );

                        SpaceCardView card=
                                cards.get(info.spaceId);

                        if(card!=null){

                            card.setStats(
                                    stat.fileCount,
                                    formatUpdated(
                                            stat.latestUpdate
                                    ),
                                    formatSize(
                                            stat.totalSize
                                    )
                            );
                        }
                    }

                    footer.setText(
                            "ⓘ "+
                            files.size()+
                            " files  ·  "+
                            formatSize(finalTotalSize)+
                            " used"
                    );
                });

            }catch(Exception e){

                Platform.runLater(()->{
                    footer.setText(
                            "ⓘ Unable to load space statistics"
                    );
                });
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private String formatUpdated(Instant time){
        if(time==null)
            return "No files yet";

        long minutes=Math.max(
                0,
                Duration.between(
                        time,
                        Instant.now()
                ).toMinutes()
        );

        if(minutes<1)
            return "Updated just now";

        if(minutes<60)
            return "Updated "+minutes+" min ago";

        long hours=minutes/60;

        if(hours<24)
            return "Updated "+hours+" hr ago";

        long days=hours/24;

        if(days==1)
            return "Updated yesterday";

        return "Updated "+days+" days ago";
    }

    private String formatSize(long bytes){
        if(bytes<=0)
            return "—";
        if(bytes<1024)
            return bytes+" B";
        if(bytes<1048576)
            return String.format(
                    "%.1f KB",
                    bytes/1024.0
            );
        if(bytes<1073741824L)
            return String.format(
                    "%.1f MB",
                    bytes/1048576.0
            );
        return String.format(
                "%.1f GB",
                bytes/1073741824.0
        );
    }

    private Label label(
            String text,
            double size,
            FontWeight weight,
            String color){

        Label label=new Label(text);
        label.setFont(
                Font.font(
                        FONT,
                        weight,
                        size
                )
        );
        label.setStyle(
                "-fx-text-fill:"+color+";"
        );
        return label;
    }

    private static class SpaceInfo{
        final String name,spaceId,description,icon,iconBackground,iconTextColor;

        SpaceInfo(
                String name,
                String spaceId,
                String description,
                String icon,
                String iconBackground,
                String iconTextColor){

            this.name=name;
            this.spaceId=spaceId;
            this.description=description;
            this.icon=icon;
            this.iconBackground=iconBackground;
            this.iconTextColor=iconTextColor;
        }
    }

    private static class SpaceStats{
        int fileCount;
        long totalSize;
        Instant latestUpdate;
    }

    private static class SpaceCardView{
        final VBox card;
        final Label filesLabel,sizeLabel,updatedLabel;

        SpaceCardView(
                VBox card,
                Label filesLabel,
                Label sizeLabel,
                Label updatedLabel){

            this.card=card;
            this.filesLabel=filesLabel;
            this.sizeLabel=sizeLabel;
            this.updatedLabel=updatedLabel;
        }

        void setStats(
                int count,
                String updated,
                String size){

            filesLabel.setText(
                    count+" files"
            );

            sizeLabel.setText(size);
            updatedLabel.setText(updated);
        }
    }
}