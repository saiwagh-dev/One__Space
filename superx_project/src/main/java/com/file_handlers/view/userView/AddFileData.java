package com.file_handlers.view.userView;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.file_handlers.model.UserSession;
import com.file_handlers.service.FileProcessingService;
import com.file_handlers.service.ProcessingStatusListener;
import com.file_handlers.view.LandingPage;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class AddFileData {
    private final VBox fileList=new VBox(14);
    private final List<File> selectedFiles=new ArrayList<>();
    private final Runnable backAction;
    private static final int MAX_WORKERS=3;

    private static final String FONT="Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_CENTER="#31435B",BG_SIDEBAR="#1E2A3A",SIDEBAR_BORDER="#2D3D52";
    private static final String BG_CARD="#DDE8F8",BG_CARD_INNER="#CADDF2",BORDER_CARD="#C3D6EC";
    private static final String TEXT_DARK="#0F172A",TEXT_MUTED_DARK="#475569",TEXT_LIGHT="#FFFFFF",TEXT_MUTED_LIGHT="#94A3B8";
    private static final String PRIMARY_BLUE="#2563EB",SUCCESS="#16A34A",WARNING="#D97706",ERROR="#DC2626",INFO="#0284C7";

    public AddFileData(){this(()->LandingPage.showUserDashboard());}
    public AddFileData(Runnable backAction){this.backAction=backAction;}

    public Scene getScene(){
        BorderPane root=new BorderPane();
        root.setStyle("-fx-background-color:"+BG_CENTER+";");
        VBox sidebar=createSidebar();
        HBox topBar=createTopBar();

        VBox content=new VBox(22);
        content.setPadding(new Insets(26,28,28,28));
        content.setStyle("-fx-background-color:"+BG_CENTER+";");

        Label title=new Label("Add Files");
        title.setStyle("-fx-font-family:"+FONT+";-fx-font-size:26px;-fx-font-weight:700;-fx-text-fill:"+TEXT_LIGHT+";");

        Label subtitle=new Label("Add files to OneSpace and let the pipeline understand, categorize and organize them.");
        subtitle.setWrapText(true);
        subtitle.setStyle("-fx-font-family:"+FONT+";-fx-font-size:13px;-fx-font-weight:500;-fx-text-fill:"+TEXT_MUTED_LIGHT+";");

        VBox titleBox=new VBox(5,title,subtitle);
        HBox actionCard=createActionCard();

        HBox listHeader=new HBox();
        listHeader.setAlignment(Pos.CENTER_LEFT);

        Label filesTitle=new Label("Files");
        filesTitle.setStyle("-fx-font-family:"+FONT+";-fx-font-size:17px;-fx-font-weight:700;-fx-text-fill:"+TEXT_LIGHT+";");

        Region headerSpacer=new Region();
        HBox.setHgrow(headerSpacer,Priority.ALWAYS);

        Label pipelineLabel=new Label("3 files at a time");
        pipelineLabel.setStyle("-fx-font-family:"+FONT+";-fx-font-size:11px;-fx-font-weight:500;-fx-text-fill:"+TEXT_MUTED_LIGHT+";");
        listHeader.getChildren().addAll(filesTitle,headerSpacer,pipelineLabel);

        fileList.setPadding(new Insets(4));

        VBox fileContainer=new VBox(fileList);
        fileContainer.setPadding(new Insets(14));
        fileContainer.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:16;-fx-background-radius:16;");

        ScrollPane fileScroll=new ScrollPane(fileContainer);
        fileScroll.setFitToWidth(true);
        fileScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        fileScroll.setStyle("-fx-background-color:"+BG_CENTER+";-fx-background:"+BG_CENTER+";-fx-background-insets:0;-fx-padding:0;");
        VBox.setVgrow(fileScroll,Priority.ALWAYS);

        Button processButton=new Button("Process Selected Files");
        processButton.setStyle(primaryButtonStyle());
        processButton.setOnAction(e->processFiles());

        HBox bottomBar=new HBox(processButton);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        showEmptyState();
        content.getChildren().addAll(titleBox,actionCard,listHeader,fileScroll,bottomBar);

        VBox mainArea=new VBox(topBar,content);
        VBox.setVgrow(content,Priority.ALWAYS);
        mainArea.setStyle("-fx-background-color:"+BG_CENTER+";");

        root.setLeft(sidebar);
        root.setCenter(mainArea);
        return new Scene(root,1200,750);
    }

    private HBox createActionCard(){
        HBox card=new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:14;-fx-background-radius:14;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.12),10,0,0,3);");

        Label icon=new Label("+");
        icon.setMinSize(44,44);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color:#DBEAFE;-fx-background-radius:10;-fx-font-family:"+FONT+";-fx-font-size:24px;-fx-font-weight:500;-fx-text-fill:"+PRIMARY_BLUE+";");

        Label title=new Label("Add files to OneSpace");
        title.setStyle("-fx-font-family:"+FONT+";-fx-font-size:16px;-fx-font-weight:700;-fx-text-fill:"+TEXT_DARK+";");

        Label description=new Label("Files stay on your computer. OneSpace stores metadata and AI understanding.");
        description.setWrapText(true);
        description.setStyle("-fx-font-family:"+FONT+";-fx-font-size:12px;-fx-font-weight:500;-fx-text-fill:"+TEXT_MUTED_DARK+";");

        VBox text=new VBox(5,title,description);
        HBox.setHgrow(text,Priority.ALWAYS);

        Button addButton=new Button("+  Add Files");
        addButton.setStyle(primaryButtonStyle());
        addButton.setOnAction(e->selectFiles());

        card.getChildren().addAll(icon,text,addButton);
        return card;
    }

    private VBox createSidebar(){
        Label logo=new Label("☁  OneSpace");
        logo.setStyle("-fx-font-family:"+FONT+";-fx-font-size:20px;-fx-font-weight:700;-fx-text-fill:"+TEXT_LIGHT+";");
        VBox logoBox=new VBox(logo);
        logoBox.setPadding(new Insets(0,0,14,6));

        Button dashboard=navButton("⌂","Dashboard",false);
        Button spaces=navButton("📁","Spaces",false);
        Button search=navButton("⌕","Search",false);
        Button recent=navButton("◷","Recent",false);
        Button settings=navButton("⚙","Settings",false);
        Button logout=navButton("⇥","Logout",false);

        dashboard.setOnAction(e->LandingPage.showUserDashboard());
        spaces.setOnAction(e->LandingPage.showUserSpace());
        search.setOnAction(e->LandingPage.showUserSearch());
        recent.setOnAction(e->LandingPage.showRecentPage());
        settings.setOnAction(e->LandingPage.showSettingPage());
        logout.setOnAction(e->{UserSession.clearSession();LandingPage.showUserLoginPage();});

        Label storageTitle=new Label("Storage Used");
        storageTitle.setStyle("-fx-font-family:"+FONT+";-fx-font-size:12px;-fx-font-weight:600;-fx-text-fill:"+TEXT_LIGHT+";");

        Label storageValue=new Label("64.2 GB of 100 GB");
        storageValue.setStyle("-fx-font-family:"+FONT+";-fx-font-size:12px;-fx-font-weight:700;-fx-text-fill:"+TEXT_LIGHT+";");

        Label storagePercent=new Label("64%");
        storagePercent.setStyle("-fx-font-family:"+FONT+";-fx-font-size:11px;-fx-font-weight:700;-fx-text-fill:"+TEXT_MUTED_LIGHT+";");

        Region storageSpacer=new Region();
        HBox.setHgrow(storageSpacer,Priority.ALWAYS);

        HBox storageRow=new HBox(storageValue,storageSpacer,storagePercent);
        storageRow.setAlignment(Pos.CENTER_LEFT);

        Region progress=new Region();
        progress.setPrefHeight(5);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-background-radius:5;");

        Label manageStorage=new Label("Manage Storage ›");
        manageStorage.setStyle("-fx-font-family:"+FONT+";-fx-font-size:11px;-fx-font-weight:600;-fx-text-fill:#60A5FA;-fx-cursor:hand;");

        VBox storageCard=new VBox(8,storageTitle,storageRow,progress,manageStorage);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color:#141D29;-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-radius:12;-fx-background-radius:12;");

        Region spacer=new Region();
        VBox.setVgrow(spacer,Priority.ALWAYS);

        VBox sidebar=new VBox(5,logoBox,dashboard,spaces,search,recent,spacer,settings,logout,storageCard);
        sidebar.setPadding(new Insets(20,14,20,14));
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 1 0 0;");
        return sidebar;
    }

    private HBox createTopBar(){
        Button back=new Button("‹");
        back.setStyle("-fx-background-color:transparent;-fx-text-fill:"+TEXT_LIGHT+";-fx-font-family:"+FONT+";-fx-font-size:26px;-fx-cursor:hand;-fx-padding:0 8;");
        back.setOnAction(e->backAction.run());

        Label page=new Label("Add File");
        page.setStyle("-fx-font-family:"+FONT+";-fx-font-size:15px;-fx-font-weight:600;-fx-text-fill:"+TEXT_LIGHT+";");

        Region spacer=new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);

        Label user=new Label(getUserName());
        user.setStyle("-fx-font-family:"+FONT+";-fx-font-size:13px;-fx-font-weight:600;-fx-text-fill:"+TEXT_LIGHT+";");

        HBox bar=new HBox(8,back,page,spacer,user);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(12,28,12,28));
        bar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 0 1 0;");
        return bar;
    }

    private Button navButton(String icon,String text,boolean active){
        Label iconLabel=new Label(icon);
        iconLabel.setStyle("-fx-font-family:"+FONT+";-fx-font-size:14px;");

        Label textLabel=new Label(text);
        textLabel.setStyle("-fx-font-family:"+FONT+";-fx-font-size:13px;-fx-font-weight:"+(active?"700":"500")+";-fx-text-fill:"+TEXT_LIGHT+";");

        HBox content=new HBox(12,iconLabel,textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button=new Button("",content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(38);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0,12,0,12));

        if(active) button.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-background-radius:8;-fx-cursor:hand;");
        else{
            button.setStyle(navStyle(false));
            button.setOnMouseEntered(e->button.setStyle(navStyle(true)));
            button.setOnMouseExited(e->button.setStyle(navStyle(false)));
        }
        return button;
    }

    private void selectFiles(){
        FileChooser chooser=new FileChooser();
        chooser.setTitle("Select Files");
        List<File> files=chooser.showOpenMultipleDialog(null);
        if(files==null||files.isEmpty())return;
        selectedFiles.clear();
        selectedFiles.addAll(files);
        refreshFileList();
    }

    private void refreshFileList(){
        fileList.getChildren().clear();
        for(File file:selectedFiles)fileList.getChildren().add(createFileCard(file));
    }

    private void showEmptyState(){
        Label icon=new Label("+");
        icon.setStyle("-fx-font-family:"+FONT+";-fx-font-size:26px;-fx-text-fill:"+TEXT_MUTED_DARK+";");

        Label title=new Label("No files selected");
        title.setStyle("-fx-font-family:"+FONT+";-fx-font-size:14px;-fx-font-weight:700;-fx-text-fill:"+TEXT_DARK+";");

        Label description=new Label("Click Add Files above to begin.");
        description.setStyle("-fx-font-family:"+FONT+";-fx-font-size:11px;-fx-text-fill:"+TEXT_MUTED_DARK+";");

        VBox empty=new VBox(6,icon,title,description);
        empty.setAlignment(Pos.CENTER);
        empty.setPadding(new Insets(45));
        empty.setUserData("EMPTY_STATE");
        fileList.getChildren().add(empty);
    }

    private VBox createFileCard(File file){
        VBox card=new VBox(12);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-background-radius:12;-fx-border-color:"+BORDER_CARD+";-fx-border-radius:12;");

        HBox header=new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label fileIcon=new Label(getFileIcon(file));
        fileIcon.setMinSize(40,40);
        fileIcon.setAlignment(Pos.CENTER);
        fileIcon.setStyle("-fx-background-color:#E8F0FA;-fx-background-radius:9;-fx-font-size:17px;");

        Label name=new Label(file.getName());
        name.setStyle("-fx-font-family:"+FONT+";-fx-font-size:13px;-fx-font-weight:700;-fx-text-fill:"+TEXT_DARK+";");

        Label path=new Label(file.getParent());
        path.setStyle("-fx-font-family:"+FONT+";-fx-font-size:10px;-fx-text-fill:"+TEXT_MUTED_DARK+";");

        VBox fileInfo=new VBox(3,name,path);
        HBox.setHgrow(fileInfo,Priority.ALWAYS);

        Label status=new Label("Waiting");
        setStatusStyle(status,"WAITING");

        header.getChildren().addAll(fileIcon,spacer(12),fileInfo,spacer(12),status);

        HBox pipeline=new HBox(8);
        pipeline.setAlignment(Pos.CENTER_LEFT);

        StepView metadata=createStep("1","Metadata");
        StepView ai=createStep("2","AI");
        StepView space=createStep("3","Space");
        StepView saved=createStep("4","Saved");

        pipeline.getChildren().addAll(metadata.container,connector(),ai.container,connector(),space.container,connector(),saved.container);

        Label explanation=new Label("Waiting to start...");
        explanation.setWrapText(true);
        explanation.setStyle("-fx-font-family:"+FONT+";-fx-font-size:11px;-fx-text-fill:"+TEXT_MUTED_DARK+";");

        card.getChildren().addAll(header,pipeline,explanation);
        card.setUserData(new ProcessingView(status,explanation,metadata,ai,space,saved));
        return card;
    }

    private StepView createStep(String number,String name){
        Label circle=new Label(number);
        circle.setMinSize(24,24);
        circle.setAlignment(Pos.CENTER);
        circle.setStyle(circleStyle("WAITING"));

        Label title=new Label(name);
        title.setStyle("-fx-font-family:"+FONT+";-fx-font-size:10px;-fx-font-weight:700;-fx-text-fill:"+TEXT_MUTED_DARK+";");

        Label state=new Label("Waiting");
        state.setStyle("-fx-font-family:"+FONT+";-fx-font-size:9px;-fx-text-fill:"+TEXT_MUTED_DARK+";");

        HBox titleRow=new HBox(6,circle,title);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        VBox container=new VBox(4,titleRow,state);
        container.setPadding(new Insets(9,11,9,11));
        container.setMinWidth(120);
        container.setStyle(stepContainerStyle("WAITING"));

        return new StepView(container,circle,title,state,name);
    }

    private Region connector(){
        Region line=new Region();
        line.setPrefHeight(2);
        line.setPrefWidth(20);
        line.setStyle("-fx-background-color:#B8C9DC;");
        return line;
    }

    private void processFiles(){
        if(selectedFiles.isEmpty())return;

        UserSession session=UserSession.getInstance();
        if(session==null||!UserSession.isLoggedIn())return;

        ExecutorService executor=Executors.newFixedThreadPool(MAX_WORKERS);

        for(File file:selectedFiles){
            VBox card=findCard(file);
            if(card==null)continue;

            ProcessingView view=(ProcessingView)card.getUserData();
            resetProcessingView(view);

            setStatus(view,"Checking file...");
            view.explanation.setText("Preparing file and checking whether it already exists...");

            executor.submit(()->processSingleFile(file,view));
        }

        executor.shutdown();
    }

    private void processSingleFile(File file,ProcessingView view){
        try{
            FileProcessingService service=new FileProcessingService();

            ProcessingStatusListener listener=new ProcessingStatusListener(){
                @Override
                public void onTaskStarted(String task){
                    updateStep(view,task,"PROCESSING");
                }

                @Override
                public void onTaskCompleted(String task){
                    updateStep(view,task,"COMPLETED");
                }

                @Override
                public void onTaskFailed(String task,String error){
                    if("Duplicate".equalsIgnoreCase(task))updateDuplicate(view);
                    else updateFailed(view,task,error);
                }
            };

            String result=service.processFile(Path.of(file.getAbsolutePath()),listener);

            if(result!=null&&result.startsWith("DUPLICATE:")){
                updateDuplicate(view);
                return;
            }

            Platform.runLater(()->{
                setStatus(view,"Completed");
                view.explanation.setText("File successfully added to your OneSpace library.");
            });
        }catch(Exception e){
            Platform.runLater(()->{
                setStatus(view,"Failed");
                setStatusStyle(view.status,"FAILED");
                view.explanation.setText("Processing failed: "+safeMessage(e));
            });

            System.out.println("[ERROR] "+file.getName()+" - "+e.getMessage());
        }
    }

    private void updateStep(ProcessingView view,String task,String state){
        Platform.runLater(()->{
            StepView step=getStep(view,normalizeTask(task));
            if(step==null)return;

            setStepState(step,state);

            if("PROCESSING".equals(state)){
                setStatus(view,"Processing "+step.name+"...");
                view.explanation.setText(getProcessingMessage(step.name));
            }

            if("COMPLETED".equals(state))
                view.explanation.setText(getCompletedMessage(step.name));
        });
    }

    private void updateDuplicate(ProcessingView view){
        Platform.runLater(()->{
            setStepState(view.metadata,"COMPLETED");
            setStepState(view.ai,"SKIPPED");
            setStepState(view.space,"SKIPPED");
            setStepState(view.saved,"SKIPPED");
            setStatus(view,"Already Exists");
            setStatusStyle(view.status,"DUPLICATE");
            view.explanation.setText("This file is already in your OneSpace library. No duplicate was created.");
        });
    }

    private void updateFailed(ProcessingView view,String task,String error){
        Platform.runLater(()->{
            StepView step=getStep(view,normalizeTask(task));
            if(step!=null)setStepState(step,"FAILED");

            setStatus(view,"Processing Failed");
            setStatusStyle(view.status,"FAILED");
            view.explanation.setText(error==null||error.isBlank()?"The "+normalizeTask(task)+" step failed.":error);
        });
    }

    private void setStepState(StepView step,String state){
        if(step==null)return;

        String display;

        switch(state){
            case "PROCESSING":
                display="Processing";
                step.circle.setText("•");
                break;
            case "COMPLETED":
                display="Completed";
                step.circle.setText("✓");
                break;
            case "SKIPPED":
                display="Skipped";
                step.circle.setText("–");
                break;
            case "FAILED":
                display="Failed";
                step.circle.setText("!");
                break;
            default:
                display="Waiting";
                step.circle.setText(step.number);
        }

        step.state.setText(display);
        step.container.setStyle(stepContainerStyle(state));
        step.circle.setStyle(circleStyle(state));
        step.title.setStyle("-fx-font-family:"+FONT+";-fx-font-size:10px;-fx-font-weight:700;-fx-text-fill:"+stepTextColor(state)+";");
        step.state.setStyle("-fx-font-family:"+FONT+";-fx-font-size:9px;-fx-font-weight:600;-fx-text-fill:"+stepTextColor(state)+";");
    }

    private void resetProcessingView(ProcessingView view){
        setStepState(view.metadata,"WAITING");
        setStepState(view.ai,"WAITING");
        setStepState(view.space,"WAITING");
        setStepState(view.saved,"WAITING");
        setStatus(view,"Waiting");
        setStatusStyle(view.status,"WAITING");
        view.explanation.setText("Waiting to start...");
    }

    private void setStatus(ProcessingView view,String text){
        view.status.setText(text);

        if("Completed".equalsIgnoreCase(text))setStatusStyle(view.status,"SUCCESS");
        else if("Already Exists".equalsIgnoreCase(text))setStatusStyle(view.status,"DUPLICATE");
        else if("Failed".equalsIgnoreCase(text)||"Processing Failed".equalsIgnoreCase(text))setStatusStyle(view.status,"FAILED");
        else setStatusStyle(view.status,"PROCESSING");
    }

    private void setStatusStyle(Label label,String state){
        switch(state){
            case "SUCCESS":
                label.setStyle("-fx-font-family:"+FONT+";-fx-font-size:11px;-fx-font-weight:700;-fx-text-fill:"+SUCCESS+";-fx-background-color:#DCFCE7;-fx-padding:6 10;-fx-background-radius:7;");
                break;
            case "DUPLICATE":
                label.setStyle("-fx-font-family:"+FONT+";-fx-font-size:11px;-fx-font-weight:700;-fx-text-fill:"+WARNING+";-fx-background-color:#FEF3C7;-fx-padding:6 10;-fx-background-radius:7;");
                break;
            case "FAILED":
                label.setStyle("-fx-font-family:"+FONT+";-fx-font-size:11px;-fx-font-weight:700;-fx-text-fill:"+ERROR+";-fx-background-color:#FEE2E2;-fx-padding:6 10;-fx-background-radius:7;");
                break;
            case "PROCESSING":
                label.setStyle("-fx-font-family:"+FONT+";-fx-font-size:11px;-fx-font-weight:700;-fx-text-fill:"+INFO+";-fx-background-color:#E0F2FE;-fx-padding:6 10;-fx-background-radius:7;");
                break;
            default:
                label.setStyle("-fx-font-family:"+FONT+";-fx-font-size:11px;-fx-font-weight:600;-fx-text-fill:"+TEXT_MUTED_DARK+";-fx-background-color:#E8EEF5;-fx-padding:6 10;-fx-background-radius:7;");
        }
    }

    private String stepContainerStyle(String state){
        switch(state){
            case "PROCESSING":
                return "-fx-background-color:#DBEAFE;-fx-border-color:#93C5FD;-fx-border-radius:8;-fx-background-radius:8;";
            case "COMPLETED":
                return "-fx-background-color:#DCFCE7;-fx-border-color:#86EFAC;-fx-border-radius:8;-fx-background-radius:8;";
            case "SKIPPED":
                return "-fx-background-color:#F1F5F9;-fx-border-color:#CBD5E1;-fx-border-radius:8;-fx-background-radius:8;";
            case "FAILED":
                return "-fx-background-color:#FEE2E2;-fx-border-color:#FCA5A5;-fx-border-radius:8;-fx-background-radius:8;";
            default:
                return "-fx-background-color:#EEF3F9;-fx-border-color:#D5E0ED;-fx-border-radius:8;-fx-background-radius:8;";
        }
    }

    private String circleStyle(String state){
        switch(state){
            case "PROCESSING":
                return "-fx-background-color:"+PRIMARY_BLUE+";-fx-text-fill:white;-fx-background-radius:20;-fx-font-size:11px;-fx-font-weight:700;";
            case "COMPLETED":
                return "-fx-background-color:"+SUCCESS+";-fx-text-fill:white;-fx-background-radius:20;-fx-font-size:11px;-fx-font-weight:700;";
            case "FAILED":
                return "-fx-background-color:"+ERROR+";-fx-text-fill:white;-fx-background-radius:20;-fx-font-size:11px;-fx-font-weight:700;";
            default:
                return "-fx-background-color:#CBD5E1;-fx-text-fill:#475569;-fx-background-radius:20;-fx-font-size:11px;-fx-font-weight:700;";
        }
    }

    private String stepTextColor(String state){
        switch(state){
            case "PROCESSING":return PRIMARY_BLUE;
            case "COMPLETED":return SUCCESS;
            case "FAILED":return ERROR;
            default:return TEXT_MUTED_DARK;
        }
    }

    private String normalizeTask(String task){
        if(task==null)return "";
        String value=task.toLowerCase();

        if(value.contains("metadata"))return "Metadata";
        if(value.contains("ai")||value.contains("understand")||value.contains("classif"))return "AI";
        if(value.contains("space")||value.contains("categor"))return "Space";
        if(value.contains("firestore")||value.contains("save"))return "Saved";

        return task;
    }

    private StepView getStep(ProcessingView view,String name){
        return switch(name){
            case "Metadata"->view.metadata;
            case "AI"->view.ai;
            case "Space"->view.space;
            case "Saved"->view.saved;
            default->null;
        };
    }

    private String getProcessingMessage(String step){
        return switch(step){
            case "Metadata"->"Reading file information and generating its metadata.";
            case "AI"->"Understanding the file and generating AI classification.";
            case "Space"->"Resolving the best OneSpace category for this file.";
            case "Saved"->"Saving the processed file information to your account.";
            default->"Processing...";
        };
    }

    private String getCompletedMessage(String step){
        return switch(step){
            case "Metadata"->"Metadata extracted successfully. Checking the file before continuing.";
            case "AI"->"AI understanding completed. Moving to space resolution.";
            case "Space"->"Space resolved successfully. Preparing to save.";
            case "Saved"->"File information saved successfully.";
            default->"Step completed.";
        };
    }

    private VBox findCard(File file){
        for(var node:fileList.getChildren()){
            if(!(node instanceof VBox))continue;

            VBox card=(VBox)node;
            if(!(card.getUserData() instanceof ProcessingView))continue;

            HBox header=(HBox)card.getChildren().get(0);
            VBox info=(VBox)header.getChildren().get(2);
            Label name=(Label)info.getChildren().get(0);

            if(name.getText().equals(file.getName()))return card;
        }
        return null;
    }

    private String getFileIcon(File file){
        String name=file.getName().toLowerCase();

        if(name.endsWith(".pdf"))return "📄";
        if(name.endsWith(".jpg")||name.endsWith(".jpeg")||name.endsWith(".png")||name.endsWith(".gif"))return "🖼";
        if(name.endsWith(".mp4")||name.endsWith(".mov")||name.endsWith(".avi"))return "🎬";
        if(name.endsWith(".mp3")||name.endsWith(".wav")||name.endsWith(".m4a"))return "🎵";
        if(name.endsWith(".doc")||name.endsWith(".docx"))return "📝";
        if(name.endsWith(".ppt")||name.endsWith(".pptx"))return "📊";
        if(name.endsWith(".xls")||name.endsWith(".xlsx"))return "📈";

        return "📁";
    }

    private String getUserName(){
        UserSession session=UserSession.getInstance();

        if(session!=null&&session.getDisplayName()!=null&&!session.getDisplayName().isBlank())
            return session.getDisplayName();

        return "User";
    }

    private String safeMessage(Exception e){
        if(e.getMessage()==null||e.getMessage().isBlank())
            return "An unexpected error occurred.";

        return e.getMessage();
    }

    private Region spacer(double width){
        Region region=new Region();
        region.setPrefWidth(width);
        return region;
    }

    private String primaryButtonStyle(){
        return "-fx-background-color:"+PRIMARY_BLUE+";-fx-text-fill:white;-fx-font-family:"+FONT+";-fx-font-size:13px;-fx-font-weight:700;-fx-padding:12 20;-fx-background-radius:10;-fx-cursor:hand;";
    }

    private String navStyle(boolean hovered){
        return "-fx-background-color:"+(hovered?"#26354A;":"transparent;")+"-fx-text-fill:white;-fx-font-family:"+FONT+";-fx-font-size:13px;-fx-background-radius:8;-fx-cursor:hand;";
    }

    private static class ProcessingView{
        Label status,explanation;
        StepView metadata,ai,space,saved;

        ProcessingView(Label status,Label explanation,StepView metadata,StepView ai,StepView space,StepView saved){
            this.status=status;
            this.explanation=explanation;
            this.metadata=metadata;
            this.ai=ai;
            this.space=space;
            this.saved=saved;
        }
    }

    private static class StepView{
        VBox container;
        Label circle,title,state;
        String name,number;

        StepView(VBox container,Label circle,Label title,Label state,String name){
            this.container=container;
            this.circle=circle;
            this.title=title;
            this.state=state;
            this.name=name;
            this.number=getNumber(name);
        }

        private String getNumber(String name){
            return switch(name){
                case "Metadata"->"1";
                case "AI"->"2";
                case "Space"->"3";
                case "Saved"->"4";
                default->"•";
            };
        }
    }
}