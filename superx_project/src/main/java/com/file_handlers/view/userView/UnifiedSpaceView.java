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
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.awt.Desktop;
import java.io.File;
import java.time.Instant;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class UnifiedSpaceView {
    private static final String FONT="Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_SIDEBAR="#1E2A3A",BG_CENTER="#31435B",BG_CARD="#DDE8F8",BG_INNER="#CADDF2",BORDER="#C3D6EC",TEXT="#0F172A",MUTED="#334155",LIGHT="#FFFFFF",MUTED_LIGHT="#94A3B8",BLUE="#2563EB";
    private final String spaceId,spaceName;
    private final FileDAO fileDAO=new FileDAO();
    private List<FileData> files=new ArrayList<>();
    private FlowPane filePane;
    private Label countLabel,storageLabel,updatedLabel,previewIcon,previewName,previewDate,previewType,previewSize;
    private Button previewButton,detailsButton,removeButton;
    private FileData selectedFile;

    public UnifiedSpaceView(){this("all","All Spaces");}

    public UnifiedSpaceView(String spaceId,String spaceName){
        this.spaceId=spaceId==null||spaceId.isBlank()?"all":spaceId;
        this.spaceName=spaceName==null||spaceName.isBlank()?"All Spaces":spaceName;
    }

    public Scene getUnifiedSpaceScene(){
        UserSession session=UserSession.getInstance();
        String user=session!=null&&session.getDisplayName()!=null?session.getDisplayName().trim():"User";

        Button notification=new Button("🔔");
        notification.setStyle("-fx-background-color:transparent;-fx-text-fill:"+LIGHT+";-fx-font-size:16px;");
        notification.setOnAction(e->LandingPage.showNotificationPage());

        Label avatar=new Label(user.isBlank()?"U":user.substring(0,1).toUpperCase());
        avatar.setPrefSize(34,34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:"+BLUE+";-fx-background-radius:50%;-fx-text-fill:white;-fx-font-weight:bold;");

        Label userName=new Label(user.isBlank()?"User":user);
        userName.setStyle("-fx-text-fill:"+LIGHT+";-fx-font-weight:bold;");

        HBox profile=new HBox(8,avatar,userName);
        profile.setAlignment(Pos.CENTER);
        profile.setOnMouseClicked(e->LandingPage.showUserProfilePage());

        Region topGap=new Region();
        HBox.setHgrow(topGap,Priority.ALWAYS);

        HBox topBar=new HBox(20,topGap,notification,profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16,ResponsiveUtil.PAGE_PADDING,14,ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:#2D3D52;-fx-border-width:0 0 1 0;");

        Label spaceIcon=new Label("📁");
        spaceIcon.setPrefSize(42,42);
        spaceIcon.setAlignment(Pos.CENTER);
        spaceIcon.setStyle("-fx-background-color:"+BG_INNER+";-fx-background-radius:10;-fx-font-size:18px;");

        Label title=label(spaceName+" Space",22,FontWeight.BOLD,TEXT);
        Label subtitle=label("Files automatically organized into "+spaceName+".",12,FontWeight.NORMAL,MUTED);
        VBox titleBox=new VBox(3,title,subtitle);
        HBox titleArea=new HBox(12,spaceIcon,titleBox);
        titleArea.setAlignment(Pos.CENTER_LEFT);

        Button back=new Button("← Spaces");
        back.setStyle("-fx-background-color:"+BG_INNER+";-fx-text-fill:"+TEXT+";-fx-border-color:"+BORDER+";-fx-border-radius:8;-fx-background-radius:8;-fx-padding:8 14;");
        back.setOnAction(e->LandingPage.showUserSpace());

        Region headerGap=new Region();
        HBox.setHgrow(headerGap,Priority.ALWAYS);
        HBox header=new HBox(titleArea,headerGap,back);
        header.setAlignment(Pos.CENTER_LEFT);

        countLabel=statValue("Loading...");
        storageLabel=statValue("Loading...");
        updatedLabel=statValue("Loading...");

        GridPane stats=new GridPane();
        stats.setHgap(14);
        stats.add(statCard("Files",countLabel,"📁"),0,0);
        stats.add(statCard("Storage",storageLabel,"💾"),1,0);
        stats.add(statCard("Last Updated",updatedLabel,"🕒"),2,0);

        filePane=new FlowPane(12,12);
        filePane.setPadding(new Insets(4));

        ScrollPane fileScroll=new ScrollPane(filePane);
        fileScroll.setFitToWidth(true);
        fileScroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;-fx-border-color:transparent;");
        VBox.setVgrow(fileScroll,Priority.ALWAYS);

        VBox fileArea=new VBox(12,label("Files",17,FontWeight.BOLD,LIGHT),fileScroll);
        fileArea.setPadding(new Insets(18));
        fileArea.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER+";-fx-border-radius:16;-fx-background-radius:16;");
        HBox.setHgrow(fileArea,Priority.ALWAYS);

        previewIcon=label("📄",48,FontWeight.NORMAL,BLUE);
        previewIcon.setPrefSize(330,160);
        previewIcon.setAlignment(Pos.CENTER);
        previewIcon.setStyle("-fx-background-color:"+BG_INNER+";-fx-background-radius:10;");

        previewName=detailValue("Select a file");
        previewDate=detailValue("—");
        previewType=detailValue("—");
        previewSize=detailValue("—");

        Label previewTitle=label("File Preview",17,FontWeight.BOLD,TEXT);

        previewButton=new Button("Preview");
        previewButton.setDisable(true);
        previewButton.setStyle("-fx-background-color:"+BLUE+";-fx-text-fill:white;-fx-font-weight:bold;-fx-background-radius:8;-fx-padding:8 16;-fx-cursor:hand;");
        previewButton.setOnAction(e->{if(selectedFile!=null)openFile(selectedFile);});

        detailsButton=new Button("More details");
        detailsButton.setStyle("-fx-background-color:transparent;-fx-text-fill:"+BLUE+";-fx-font-size:11px;-fx-font-weight:bold;-fx-padding:5 8;-fx-cursor:hand;");
        detailsButton.setOnAction(e->{if(selectedFile!=null)loadFullDetails(selectedFile);});

        removeButton=new Button("Remove");
        removeButton.setDisable(true);
        removeButton.setStyle("-fx-background-color:#FEE2E2;-fx-text-fill:#B91C1C;-fx-border-color:#FECACA;-fx-border-radius:7;-fx-background-radius:7;-fx-font-size:11px;-fx-font-weight:bold;-fx-padding:5 10;-fx-cursor:hand;");
        removeButton.setOnAction(e->{if(selectedFile!=null)removeFile(selectedFile);});

        Region previewGap=new Region();
        HBox.setHgrow(previewGap,Priority.ALWAYS);
        HBox previewHeader=new HBox(previewTitle,previewGap,previewButton);
        previewHeader.setAlignment(Pos.CENTER_LEFT);

        HBox previewActions=new HBox(8,detailsButton,removeButton);
        previewActions.setAlignment(Pos.CENTER_RIGHT);

        VBox preview=new VBox(12,previewHeader,previewIcon,detailBox("File Name",previewName),detailBox("Date",previewDate),detailBox("Type",previewType),detailBox("Size",previewSize),previewActions);
        preview.setPadding(new Insets(20));
        preview.setPrefWidth(370);
        preview.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER+";-fx-border-radius:16;-fx-background-radius:16;");

        HBox main=new HBox(16,fileArea,preview);
        VBox.setVgrow(main,Priority.ALWAYS);

        VBox content=new VBox(20,header,stats,main);
        content.setPadding(new Insets(24,ResponsiveUtil.PAGE_PADDING,28,ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color:"+BG_CENTER+";");

        ScrollPane centerScroll=new ScrollPane(content);
        centerScroll.setFitToWidth(true);
        centerScroll.setStyle("-fx-background-color:"+BG_CENTER+";-fx-background:"+BG_CENTER+";");

        VBox center=new VBox(topBar,centerScroll);
        VBox.setVgrow(centerScroll,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setCenter(center);
        root.setStyle("-fx-background-color:"+BG_SIDEBAR+";");

        loadFiles();
        return new Scene(root,LandingPage.getCurrentWidth(),LandingPage.getCurrentHeight());
    }

    private void loadFiles(){
        UserSession session=UserSession.getInstance();
        if(session==null||!UserSession.isLoggedIn()||session.getUid()==null||session.getUid().isBlank()){
            Platform.runLater(()->showEmpty("No authenticated user."));
            return;
        }

        String uid=session.getUid();

        Thread thread=new Thread(()->{
            try{
                List<FileData> loaded=spaceId.equals("all")?fileDAO.getFileSummaries(uid):fileDAO.getFileSummariesBySpace(uid,spaceId);
                loaded.sort((a,b)->{
                    if(a.getUploadedAt()==null)return 1;
                    if(b.getUploadedAt()==null)return -1;
                    return b.getUploadedAt().compareTo(a.getUploadedAt());
                });
                files=loaded;
                Platform.runLater(()->{updateStats();refreshFiles("");});
            }catch(Exception e){
                Platform.runLater(()->showEmpty("Unable to load files."));
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void refreshFiles(String query){
        filePane.getChildren().clear();
        String search=query==null?"":query.trim().toLowerCase();
        int shown=0;

        for(FileData file:files){
            String name=file.getFileName()==null?"Unnamed file":file.getFileName();
            if(!search.isEmpty()&&!name.toLowerCase().contains(search))continue;
            filePane.getChildren().add(createFileCard(file));
            shown++;
        }

        if(shown==0)showEmpty(search.isEmpty()?"No files in this Space.":"No matching files.");
    }

    private VBox createFileCard(FileData file){
        String name=file.getFileName()==null?"Unnamed file":file.getFileName();

        Label icon=label(fileIcon(name),20,FontWeight.NORMAL,BLUE);
        icon.setPrefSize(40,40);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color:"+BG_INNER+";-fx-background-radius:8;");

        Label title=label(name,12,FontWeight.BOLD,TEXT);
        Label meta=label((file.getFileType()==null?"":file.getFileType())+" • "+formatSize(file.getFileSize()),10,FontWeight.NORMAL,MUTED);

        VBox text=new VBox(3,title,meta);
        HBox card=new HBox(10,icon,text);
        card.setPrefWidth(420);
        card.setMinHeight(64);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color:"+BG_INNER+";-fx-border-color:"+BORDER+";-fx-border-radius:10;-fx-background-radius:10;-fx-cursor:hand;");
        card.setOnMouseEntered(e->card.setStyle("-fx-background-color:#D7E7F8;-fx-border-color:"+BLUE+";-fx-border-radius:10;-fx-background-radius:10;-fx-cursor:hand;"));
        card.setOnMouseExited(e->card.setStyle("-fx-background-color:"+BG_INNER+";-fx-border-color:"+BORDER+";-fx-border-radius:10;-fx-background-radius:10;-fx-cursor:hand;"));
        card.setOnMouseClicked(e->selectFile(file));

        return new VBox(card);
    }

    private void selectFile(FileData file){
        selectedFile=file;
        previewButton.setDisable(false);
        detailsButton.setDisable(false);
        removeButton.setDisable(false);
        previewIcon.setText(fileIcon(file.getFileName()));
        previewName.setText(file.getFileName()==null?"Unnamed file":file.getFileName());
        previewType.setText(file.getFileType()==null?"—":file.getFileType());
        previewSize.setText(formatSize(file.getFileSize()));
        previewDate.setText(file.getUploadedAt()==null?"—":formatDate(file.getUploadedAt().toDate().toInstant()));
    }

    private void removeFile(FileData file){
        UserSession session=UserSession.getInstance();

        if(session==null||session.getUid()==null||session.getUid().isBlank()){
            showAlert("No authenticated user.");
            return;
        }

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove File");
        alert.setHeaderText("Remove \""+file.getFileName()+"\" from OneSpace?");
        alert.setContentText("The file will disappear from this Space and appear in Trash. The actual file on your computer will not be deleted.");

        ButtonType remove=new ButtonType("Remove",ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel=new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(cancel,remove);

        alert.showAndWait().ifPresent(result->{
            if(result!=remove)return;

            String fileId=file.getFileHash();

            if(fileId==null||fileId.isBlank()){
                showAlert("Unable to identify this file.");
                return;
            }

            removeButton.setDisable(true);
            removeButton.setText("Removing...");

            Thread thread=new Thread(()->{
                try{
                    fileDAO.softDeleteFile(session.getUid(),fileId);

                    Platform.runLater(()->{
                        files.remove(file);
                        selectedFile=null;
                        previewButton.setDisable(true);
                        detailsButton.setDisable(true);
                        removeButton.setDisable(true);
                        removeButton.setText("Remove");
                        previewIcon.setText("📄");
                        previewName.setText("Select a file");
                        previewDate.setText("—");
                        previewType.setText("—");
                        previewSize.setText("—");
                        updateStats();
                        refreshFiles("");
                    });
                }catch(Exception e){
                    Platform.runLater(()->{
                        removeButton.setDisable(false);
                        removeButton.setText("Remove");
                        showAlert("Unable to remove the file from OneSpace.");
                    });
                }
            });

            thread.setDaemon(true);
            thread.start();
        });
    }

    private void loadFullDetails(FileData summary){
        UserSession session=UserSession.getInstance();

        if(session==null||session.getUid()==null||session.getUid().isBlank()){
            showAlert("No authenticated user.");
            return;
        }

        if(summary.getFileHash()==null||summary.getFileHash().isBlank()){
            showDetails(summary);
            return;
        }

        detailsButton.setDisable(true);
        detailsButton.setText("Loading...");

        Thread thread=new Thread(()->{
            try{
                FileData full=fileDAO.getFile(session.getUid(),summary.getFileHash());

                Platform.runLater(()->{
                    detailsButton.setDisable(false);
                    detailsButton.setText("More details");
                    showDetails(full==null?summary:full);
                });
            }catch(Exception e){
                Platform.runLater(()->{
                    detailsButton.setDisable(false);
                    detailsButton.setText("More details");
                    showAlert("Unable to load file details.");
                });
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void showDetails(FileData file){
        if(file==null)return;

        Dialog<Void> dialog=new Dialog<>();
        dialog.setTitle("File Details");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        String name=file.getFileName()==null?"Unnamed file":file.getFileName();
        String description=file.getDescription();

        if(description==null||description.isBlank())description="No description available.";

        VBox box=new VBox(14);
        box.setPadding(new Insets(18));
        box.setStyle("-fx-background-color:"+BG_CARD+";");

        Label heading=label(name,18,FontWeight.BOLD,TEXT);
        Label type=detailLine("File Type",safe(file.getFileType()));
        Label size=detailLine("File Size",formatSize(file.getFileSize()));
        Label date=detailLine("Uploaded",file.getUploadedAt()==null?"—":formatDate(file.getUploadedAt().toDate().toInstant()));
        Label path=detailLine("Local Path",safe(file.getLocalPath()));

        Label descriptionTitle=label("Description",11,FontWeight.BOLD,MUTED);
        Label descriptionValue=label(description,12,FontWeight.NORMAL,TEXT);
        descriptionValue.setWrapText(true);
        descriptionValue.setMaxWidth(460);

        VBox descriptionBox=new VBox(4,descriptionTitle,descriptionValue);
        descriptionBox.setPadding(new Insets(10));
        descriptionBox.setStyle("-fx-background-color:"+BG_INNER+";-fx-background-radius:10;");

        Label tagsTitle=label("Smart Tags",11,FontWeight.BOLD,MUTED);
        FlowPane tagsPane=new FlowPane(7,7);

        List<String> tags=file.getSmartTags();

        if(tags==null||tags.isEmpty()){
            tagsPane.getChildren().add(tag("No smart tags available."));
        }else{
            for(String tag:tags)
                if(tag!=null&&!tag.isBlank())tagsPane.getChildren().add(tag(tag));
        }

        VBox tagsBox=new VBox(6,tagsTitle,tagsPane);
        tagsBox.setPadding(new Insets(10));
        tagsBox.setStyle("-fx-background-color:"+BG_INNER+";-fx-background-radius:10;");

        box.getChildren().addAll(heading,type,size,date,path,descriptionBox,tagsBox);

        ScrollPane scroll=new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportWidth(500);
        scroll.setPrefViewportHeight(430);
        scroll.setStyle("-fx-background-color:"+BG_CARD+";-fx-background:"+BG_CARD+";-fx-border-color:transparent;");

        dialog.getDialogPane().setContent(scroll);
        dialog.getDialogPane().setStyle("-fx-background-color:"+BG_CARD+";");
        dialog.showAndWait();
    }

    private Label detailLine(String title,String value){
        Label l=label(title+"   "+value,11,FontWeight.NORMAL,TEXT);
        l.setWrapText(true);
        return l;
    }

    private Label tag(String text){
        Label l=label(text,10,FontWeight.BOLD,BLUE);
        l.setPadding(new Insets(5,9,5,9));
        l.setStyle("-fx-background-color:#D6E6FA;-fx-background-radius:12;-fx-text-fill:"+BLUE+";-fx-font-weight:bold;");
        return l;
    }

    private String safe(String value){return value==null||value.isBlank()?"—":value;}

    private void updateStats(){
        long total=0;
        Instant latest=null;

        for(FileData file:files){
            total+=file.getFileSize();

            if(file.getUploadedAt()!=null){
                Instant time=file.getUploadedAt().toDate().toInstant();
                if(latest==null||time.isAfter(latest))latest=time;
            }
        }

        countLabel.setText(files.size()+" files");
        storageLabel.setText(formatSize(total));
        updatedLabel.setText(latest==null?"—":relativeTime(latest));
    }

    private void openFile(FileData file){
        if(file==null||file.getLocalPath()==null||file.getLocalPath().isBlank())return;

        try{
            File localFile=new File(file.getLocalPath());

            if(!localFile.exists()){
                showAlert("The file no longer exists at its stored location.");
                return;
            }

            if(!Desktop.isDesktopSupported()){
                showAlert("Opening files is not supported on this system.");
                return;
            }

            UserSession session=UserSession.getInstance();

            if(session!=null&&session.getUid()!=null&&!session.getUid().isBlank()
                    &&file.getFileHash()!=null&&!file.getFileHash().isBlank()){
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

            Desktop.getDesktop().open(localFile);

        }catch(Exception e){
            showAlert("Unable to open the selected file.");
        }
    }

    private void showEmpty(String text){
        if(filePane==null)return;
        filePane.getChildren().clear();
        filePane.getChildren().add(label(text,13,FontWeight.NORMAL,MUTED));
    }

    private String formatDate(Instant instant){
        return DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm").withZone(ZoneId.systemDefault()).format(instant);
    }

    private String relativeTime(Instant time){
        long minutes=Math.max(0,Duration.between(time,Instant.now()).toMinutes());

        if(minutes<1)return "Just now";
        if(minutes<60)return minutes+" min ago";

        long hours=minutes/60;
        if(hours<24)return hours+" hr ago";

        long days=hours/24;
        return days+" day"+(days==1?"":"s")+" ago";
    }

    private String formatSize(long bytes){
        if(bytes<=0)return "0 B";
        if(bytes<1024)return bytes+" B";
        if(bytes<1048576)return String.format("%.1f KB",bytes/1024.0);
        if(bytes<1073741824L)return String.format("%.1f MB",bytes/1048576.0);
        return String.format("%.1f GB",bytes/1073741824.0);
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

    private VBox statCard(String title,Label value,String icon){
        Label heading=label(title,11,FontWeight.BOLD,MUTED);
        Label symbol=label(icon,14,FontWeight.NORMAL,BLUE);

        Region gap=new Region();
        HBox.setHgrow(gap,Priority.ALWAYS);

        HBox row=new HBox(heading,gap,symbol);
        VBox card=new VBox(8,row,value);
        card.setPadding(new Insets(14));
        card.setMinHeight(85);
        card.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER+";-fx-border-radius:14;-fx-background-radius:14;");

        return card;
    }

    private Label statValue(String text){return label(text,19,FontWeight.BOLD,TEXT);}

    private VBox detailBox(String title,Label value){
        return new VBox(2,label(title,10,FontWeight.BOLD,MUTED),value);
    }

    private Label detailValue(String text){return label(text,12,FontWeight.BOLD,TEXT);}

    private Label label(String text,double size,FontWeight weight,String color){
        Label l=new Label(text);
        l.setFont(Font.font(FONT,weight,size));
        l.setStyle("-fx-text-fill:"+color+";");
        return l;
    }

    private void showAlert(String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("OneSpace");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}