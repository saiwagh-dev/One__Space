package com.file_handlers.view.userView;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.model.FileData;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;

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

import java.util.ArrayList;
import java.util.List;

public class UserTrash {
    private static final String FONT="Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_SIDEBAR="#1E2A3A",BG_SIDEBAR_CARD="#141D29",SIDEBAR_BORDER="#2D3D52";
    private static final String BG_CENTER_CANVAS="#31435B",BG_CARD="#DDE8F8",BG_CARD_INNER="#CADDF2",BORDER_CARD="#C3D6EC";
    private static final String TEXT_DARK="#0F172A",TEXT_MUTED_DARK="#334155",TEXT_LIGHT="#FFFFFF",TEXT_MUTED_LIGHT="#94A3B8";
    private static final String PRIMARY_BLUE="#2563EB";

    private final FileDAO fileDAO=new FileDAO();
    private final List<FileData> trashedFiles=new ArrayList<>();
    private VBox trashTableRows;
    private Label countLabel;

    public Scene getTrashPageScene(){
        UserSession session=UserSession.getInstance();

        String activeUserName="User";
        String initials="U";

        if(session!=null&&session.getDisplayName()!=null&&!session.getDisplayName().isBlank()){
            String fullName=session.getDisplayName().trim();
            activeUserName=fullName.split("\\s+")[0];
            initials=activeUserName.substring(0,1).toUpperCase();
        }

        VBox sidebar=createSidebar();

        Button bellBtn=new Button("🔔");
        bellBtn.setStyle("-fx-background-color:transparent;-fx-font-size:16px;-fx-text-fill:"+TEXT_LIGHT+";-fx-cursor:hand;");
        bellBtn.setOnAction(e->LandingPage.showNotificationPage());

        Label avatar=label(initials,12,FontWeight.BOLD,TEXT_LIGHT);
        avatar.setPrefSize(34,34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-background-radius:50%;");

        Label userName=label(activeUserName,13,FontWeight.SEMI_BOLD,TEXT_LIGHT);

        HBox profileOption=new HBox(8,avatar,userName,label("⌄",13,FontWeight.NORMAL,TEXT_MUTED_LIGHT));
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(5,8,5,8));
        profileOption.setStyle("-fx-background-color:transparent;-fx-background-radius:8;-fx-cursor:hand;");
        profileOption.setOnMouseClicked(e->LandingPage.showUserProfilePage());

        HBox profileBox=new HBox(10,bellBtn,profileOption);
        profileBox.setAlignment(Pos.CENTER);

        Region topGap=new Region();
        HBox.setHgrow(topGap,Priority.ALWAYS);

        HBox topBar=new HBox(20,topGap,profileBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16,ResponsiveUtil.PAGE_PADDING,14,ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 0 1 0;");

        Label title=label("Trash Bin",24,FontWeight.BOLD,TEXT_LIGHT);
        Label subtitle=label("Files removed from OneSpace are kept here until restored or permanently deleted.",13,FontWeight.NORMAL,TEXT_MUTED_LIGHT);

        VBox greetingText=new VBox(4,title,subtitle);

        Button emptyTrashBtn=new Button("🗑 Empty Trash");
        emptyTrashBtn.setFont(Font.font(FONT,FontWeight.BOLD,13));
        emptyTrashBtn.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-text-fill:#FFFFFF;-fx-background-radius:10;-fx-cursor:hand;-fx-padding:8 18;");
        emptyTrashBtn.setOnAction(e->emptyTrash());

        Region headerGap=new Region();
        HBox.setHgrow(headerGap,Priority.ALWAYS);

        HBox greetingHeader=new HBox(greetingText,headerGap,emptyTrashBtn);
        greetingHeader.setAlignment(Pos.CENTER_LEFT);

        countLabel=label("Loading...",22,FontWeight.BOLD,TEXT_DARK);

        VBox statusCard=new VBox(
                6,
                label("Items in Trash",12,FontWeight.BOLD,TEXT_MUTED_DARK),
                countLabel,
                label("Removed files remain stored in OneSpace until restored or permanently deleted.",11,FontWeight.NORMAL,TEXT_MUTED_DARK)
        );

        statusCard.setPadding(new Insets(16));
        statusCard.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:14;-fx-background-radius:14;");

        Label cardTitle=label("Removed Items",17,FontWeight.BOLD,TEXT_DARK);
        Label cardSub=label("Files removed from their original Spaces.",12,FontWeight.NORMAL,TEXT_MUTED_DARK);

        VBox cardHeaderTitles=new VBox(2,cardTitle,cardSub);

        Region cardGap=new Region();
        HBox.setHgrow(cardGap,Priority.ALWAYS);

        HBox cardHeader=new HBox(cardHeaderTitles,cardGap);
        cardHeader.setAlignment(Pos.CENTER_LEFT);

        trashTableRows=new VBox(8);

        VBox trashCard=new VBox(16,cardHeader,trashTableRows);
        trashCard.setPadding(new Insets(24));
        trashCard.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:16;-fx-background-radius:16;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.18),16,0,0,6);");

        VBox contentBody=new VBox(22,greetingHeader,statusCard,trashCard);
        contentBody.setPadding(new Insets(24,ResponsiveUtil.PAGE_PADDING,28,ResponsiveUtil.PAGE_PADDING));
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

        loadTrash();

        return new Scene(root,LandingPage.getCurrentWidth(),LandingPage.getCurrentHeight());
    }

    private void loadTrash(){
        UserSession session=UserSession.getInstance();

        if(session==null||!UserSession.isLoggedIn()||session.getUid()==null||session.getUid().isBlank()){
            Platform.runLater(()->showRows("No authenticated user."));
            return;
        }

        Thread thread=new Thread(()->{
            try{
                List<FileData> loaded=fileDAO.getTrashedFiles(session.getUid());

                Platform.runLater(()->{
                    trashedFiles.clear();
                    trashedFiles.addAll(loaded);
                    refreshTrash();
                });
            }catch(Exception e){
                Platform.runLater(()->showRows("Unable to load Trash."));
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void refreshTrash(){
        trashTableRows.getChildren().clear();
        countLabel.setText(trashedFiles.size()+" file"+(trashedFiles.size()==1?"":"s"));

        if(trashedFiles.isEmpty()){
            showRows("Trash is empty.");
            return;
        }

        HBox header=new HBox(
                headerLabel("Name",250),
                headerLabel("Original Space",180),
                headerLabel("Date Removed",150),
                headerLabel("Size",100),
                headerLabel("Actions",150)
        );

        trashTableRows.getChildren().add(header);

        for(FileData file:trashedFiles)
            trashTableRows.getChildren().add(createTrashRow(file));
    }

    private void showRows(String message){
        if(trashTableRows==null)return;
        trashTableRows.getChildren().clear();
        trashTableRows.getChildren().add(label(message,13,FontWeight.NORMAL,TEXT_MUTED_DARK));
        if(countLabel!=null)countLabel.setText("0 files");
    }

    private HBox createTrashRow(FileData file){
        String name=file.getFileName()==null?"Unnamed file":file.getFileName();
        String space=file.getSpaceId()==null||file.getSpaceId().isBlank()?"Unknown":file.getSpaceId();
        String date=file.getDeletedAt()==null?"—":file.getDeletedAt().toDate().toString();

        Label icon=label(fileIcon(name),12,FontWeight.NORMAL,PRIMARY_BLUE);
        icon.setPrefSize(28,28);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-background-radius:6;");

        HBox nameGroup=new HBox(10,icon,label(name,12,FontWeight.BOLD,TEXT_DARK));
        nameGroup.setAlignment(Pos.CENTER_LEFT);
        nameGroup.setPrefWidth(250);

        Label spaceLabel=label(space,12,FontWeight.NORMAL,TEXT_MUTED_DARK);
        spaceLabel.setPrefWidth(180);

        Label dateLabel=label(date,11,FontWeight.NORMAL,TEXT_MUTED_DARK);
        dateLabel.setPrefWidth(150);

        Label sizeLabel=label(formatSize(file.getFileSize()),12,FontWeight.BOLD,TEXT_DARK);
        sizeLabel.setPrefWidth(100);

        Button restore=new Button("Restore");
        restore.setStyle("-fx-background-color:#DCFCE7;-fx-text-fill:#166534;-fx-border-color:#BBF7D0;-fx-border-radius:7;-fx-background-radius:7;-fx-font-size:11px;-fx-font-weight:bold;-fx-padding:6 10;-fx-cursor:hand;");
        restore.setOnAction(e->restoreFile(file));

        Button delete=new Button("Delete");
        delete.setStyle("-fx-background-color:#FEE2E2;-fx-text-fill:#B91C1C;-fx-border-color:#FECACA;-fx-border-radius:7;-fx-background-radius:7;-fx-font-size:11px;-fx-font-weight:bold;-fx-padding:6 10;-fx-cursor:hand;");
        delete.setOnAction(e->permanentlyDelete(file));

        HBox actions=new HBox(8,restore,delete);
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.setPrefWidth(150);

        HBox row=new HBox(nameGroup,spaceLabel,dateLabel,sizeLabel,actions);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8,0,8,0));
        row.setStyle("-fx-border-color:"+BORDER_CARD+";-fx-border-width:0 0 1 0;");

        return row;
    }

    private void restoreFile(FileData file){
        UserSession session=UserSession.getInstance();
        if(session==null)return;

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Restore File");
        alert.setHeaderText("Restore \""+file.getFileName()+"\"?");
        alert.setContentText("The file will return to its original Space.");

        ButtonType yes=new ButtonType("Restore",ButtonBar.ButtonData.OK_DONE);
        ButtonType no=new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(no,yes);

        alert.showAndWait().ifPresent(result->{
            if(result!=yes)return;

            Thread thread=new Thread(()->{
                try{
                    fileDAO.restoreFile(session.getUid(),file.getFileHash());
                    Platform.runLater(()->{
                        trashedFiles.remove(file);
                        refreshTrash();
                    });
                }catch(Exception e){
                    Platform.runLater(()->showAlert("Unable to restore the file."));
                }
            });

            thread.setDaemon(true);
            thread.start();
        });
    }

    private void permanentlyDelete(FileData file){
        UserSession session=UserSession.getInstance();
        if(session==null)return;

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Permanent Delete");
        alert.setHeaderText("Permanently delete \""+file.getFileName()+"\"?");
        alert.setContentText("This removes the OneSpace record. The local file on your computer is not deleted.");

        ButtonType yes=new ButtonType("Delete Permanently",ButtonBar.ButtonData.OK_DONE);
        ButtonType no=new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(no,yes);

        alert.showAndWait().ifPresent(result->{
            if(result!=yes)return;

            Thread thread=new Thread(()->{
                try{
                    fileDAO.permanentlyDeleteFile(session.getUid(),file.getFileHash());
                    Platform.runLater(()->{
                        trashedFiles.remove(file);
                        refreshTrash();
                    });
                }catch(Exception e){
                    Platform.runLater(()->showAlert("Unable to permanently delete the file."));
                }
            });

            thread.setDaemon(true);
            thread.start();
        });
    }

    private void emptyTrash(){
        if(trashedFiles.isEmpty()){
            showAlert("Trash is already empty.");
            return;
        }

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Empty Trash");
        alert.setHeaderText("Permanently delete all trashed files?");
        alert.setContentText("The OneSpace records will be permanently removed. Local files on your computer will not be deleted.");

        ButtonType yes=new ButtonType("Empty Trash",ButtonBar.ButtonData.OK_DONE);
        ButtonType no=new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(no,yes);

        alert.showAndWait().ifPresent(result->{
            if(result!=yes)return;

            UserSession session=UserSession.getInstance();
            if(session==null)return;

            Thread thread=new Thread(()->{
                try{
                    for(FileData file:new ArrayList<>(trashedFiles))
                        fileDAO.permanentlyDeleteFile(session.getUid(),file.getFileHash());

                    Platform.runLater(()->{
                        trashedFiles.clear();
                        refreshTrash();
                    });
                }catch(Exception e){
                    Platform.runLater(()->showAlert("Unable to empty Trash completely."));
                }
            });

            thread.setDaemon(true);
            thread.start();
        });
    }

    private VBox createSidebar(){
        Image image=new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
        ImageView logoImage=new ImageView(image);
        logoImage.setFitWidth(42);
        logoImage.setFitHeight(42);
        logoImage.setPreserveRatio(true);

        HBox logo=new HBox(10,logoImage,label("OneSpace",19,FontWeight.BOLD,TEXT_LIGHT));
        logo.setAlignment(Pos.CENTER_LEFT);

        Button dashboard=side("⌂","Dashboard",false,e->LandingPage.showUserDashboard());
        Button spaces=side("📁","Spaces",false,e->LandingPage.showUserSpace());
        Button search=side("⌕","Search",false,e->LandingPage.showUserSearch());
        Button calendar=side("📅","Calendar",false,e->LandingPage.showCalendarPage());
        Button ai=side("✧","AI Assistant",false,e->LandingPage.showAiAssistantPage());
        Button collab=side("👥","Collaboration",false,e->LandingPage.showCollaborationPage());
        Button recent=side("🕒","Recent",false,e->LandingPage.showRecentPage());
        Button trash=side("🗑","Trash",true,e->LandingPage.showTrashPage());
        Button settings=side("⚙","Settings",false,e->LandingPage.showSettingPage());

        Region gap=new Region();
        VBox.setVgrow(gap,Priority.ALWAYS);

        VBox sidebar=new VBox(8,logo,dashboard,spaces,search,calendar,ai,collab,recent,trash,gap,settings);
        sidebar.setPadding(new Insets(20,14,20,14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 1 0 0;");

        return sidebar;
    }

    private Button side(String icon,String text,boolean active,javafx.event.EventHandler<javafx.event.ActionEvent> action){
        HBox h=new HBox(12,label(icon,14,FontWeight.NORMAL,active?TEXT_LIGHT:TEXT_MUTED_LIGHT),label(text,13,active?FontWeight.BOLD:FontWeight.MEDIUM,TEXT_LIGHT));
        h.setAlignment(Pos.CENTER_LEFT);

        Button b=new Button("",h);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(38);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(0,12,0,12));
        b.setStyle("-fx-background-color:"+(active?PRIMARY_BLUE:"transparent")+";-fx-background-radius:8;");
        b.setOnAction(action);

        return b;
    }

    private Label headerLabel(String text,double width){
        Label l=label(text,11,FontWeight.BOLD,TEXT_MUTED_DARK);
        l.setPrefWidth(width);
        return l;
    }

    private Label label(String text,double size,FontWeight weight,String color){
        Label l=new Label(text);
        l.setFont(Font.font(FONT,weight,size));
        l.setStyle("-fx-text-fill:"+color+";");
        return l;
    }

    private String fileIcon(String name){
        if(name==null)return "📄";
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

    private String formatSize(long bytes){
        if(bytes<=0)return "0 B";
        if(bytes<1024)return bytes+" B";
        if(bytes<1048576)return String.format("%.1f KB",bytes/1024.0);
        if(bytes<1073741824L)return String.format("%.1f MB",bytes/1048576.0);
        return String.format("%.1f GB",bytes/1073741824.0);
    }

    private void showAlert(String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("OneSpace");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}