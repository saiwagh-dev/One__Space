package com.file_handlers.view.userView;

import com.file_handlers.dao.FileDAO;
import com.file_handlers.model.FileData;
import com.file_handlers.model.UserSession;
import com.file_handlers.service.GeminiClient;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;
import javafx.animation.TranslateTransition;
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
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AiAssistantPage{
    private static final String FONT="Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG_SIDEBAR="#1E2A3A",BG_SIDEBAR_CARD="#141D29",SIDEBAR_BORDER="#2D3D52";
    private static final String BG_CENTER_CANVAS="#31435B",BG_CARD="#DDE8F8",BG_CARD_INNER="#CADDF2",BORDER_CARD="#C3D6EC";
    private static final String TEXT_DARK="#0F172A",TEXT_MUTED_DARK="#334155",TEXT_LIGHT="#FFFFFF",TEXT_MUTED_LIGHT="#94A3B8",PRIMARY_BLUE="#2563EB";

    private final List<String> chatHistory=new ArrayList<>();
    private final List<String> recentQueries=new ArrayList<>();
    private final GeminiClient geminiClient=new GeminiClient();
    private final FileDAO fileDAO=new FileDAO();
    private VBox chatMessages,aiEmptyState,menuPanel,recentList;
    private TextField aiInput;
    private Button sendButton;
    private ScrollPane chatScroll;
    private HBox processingRow;

    public Scene getAiAssistantPageScene(){
        StackPane logoIcon=createOneSpaceLogo();
        Label logoText=label("OneSpace",19,FontWeight.BOLD,TEXT_LIGHT);
        HBox logoHeader=new HBox(10,logoIcon,logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);
        VBox logoBox=new VBox(logoHeader);
        logoBox.setPadding(new Insets(0,0,18,6));

        Button dashboardBtn=createSidebarButton("⌂","Dashboard",false);
        Button spacesBtn=createSidebarButton("📁","Spaces",false);
        Button searchBtn=createSidebarButton("⌕","Search",false);
        Button calendarBtn=createSidebarButton("📅","Calendar",false);
        Button aiBtn=createSidebarButton("✧","AI Assistant",true);
        Button collabBtn=createSidebarButton("👥","Collaboration",false);
        Button recentBtn=createSidebarButton("🕒","Recent",false);
        Button trashBtn=createSidebarButton("🗑","Trash",false);
        Button settingsBtn=createSidebarButton("⚙","Settings",false);

        dashboardBtn.setOnAction(e->LandingPage.showUserDashboard());
        spacesBtn.setOnAction(e->LandingPage.showUserSpace());
        searchBtn.setOnAction(e->LandingPage.showUserSearch());
        calendarBtn.setOnAction(e->LandingPage.showCalendarPage());
        aiBtn.setOnAction(e->LandingPage.showAiAssistantPage());
        collabBtn.setOnAction(e->LandingPage.showCollaborationPage());
        recentBtn.setOnAction(e->LandingPage.showRecentPage());
        trashBtn.setOnAction(e->LandingPage.showTrashPage());
        settingsBtn.setOnAction(e->LandingPage.showSettingPage());

        VBox navList=new VBox(4,dashboardBtn,spacesBtn,searchBtn,calendarBtn,aiBtn,collabBtn,recentBtn,trashBtn);
        Region sidebarSpacer=new Region();
        VBox.setVgrow(sidebarSpacer,Priority.ALWAYS);

        Label storageTitle=label("Storage Used",12,FontWeight.SEMI_BOLD,TEXT_LIGHT);
        Label storageVal=label("64.2 GB of 100 GB",12,FontWeight.BOLD,TEXT_LIGHT);
        Label storagePercent=label("64%",11,FontWeight.BOLD,TEXT_MUTED_LIGHT);
        Region storageGap=new Region();
        HBox.setHgrow(storageGap,Priority.ALWAYS);

        HBox storageValues=new HBox(storageVal,storageGap,storagePercent);
        storageValues.setAlignment(Pos.CENTER_LEFT);

        ProgressBar storageProgress=new ProgressBar(.64);
        storageProgress.setMaxWidth(Double.MAX_VALUE);
        storageProgress.setPrefHeight(6);
        storageProgress.setStyle("-fx-accent:"+PRIMARY_BLUE+";-fx-control-inner-background:#0E1520;");

        Button manageStorageBtn=new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,11));
        manageStorageBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:#60A5FA;-fx-padding:2 0 0 0;-fx-cursor:hand;");
        manageStorageBtn.setOnAction(e->LandingPage.showStorageIndexPage());

        VBox storageCard=new VBox(8,storageTitle,storageValues,storageProgress,manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color:"+BG_SIDEBAR_CARD+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-radius:12;-fx-background-radius:12;");

        VBox sidebar=new VBox(12,logoBox,navList,sidebarSpacer,settingsBtn,storageCard);
        sidebar.setPadding(new Insets(20,14,20,14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 1 0 0;");

        Button bellBtn=new Button("🔔");
        bellBtn.setStyle("-fx-background-color:transparent;-fx-font-size:16px;-fx-text-fill:"+TEXT_LIGHT+";-fx-cursor:hand;");
        bellBtn.setOnAction(e->LandingPage.showNotificationPage());

        Label avatar=label("AV",12,FontWeight.BOLD,TEXT_LIGHT);
        avatar.setPrefSize(34,34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-background-radius:50%;");

        Label userName=label("Aarav Verma",13,FontWeight.SEMI_BOLD,TEXT_LIGHT);
        Label arrow=label("⌄",13,FontWeight.NORMAL,TEXT_MUTED_LIGHT);

        HBox profile=new HBox(10,bellBtn,avatar,userName,arrow);
        profile.setAlignment(Pos.CENTER);

        Region topGap=new Region();
        HBox.setHgrow(topGap,Priority.ALWAYS);

        HBox topBar=new HBox(20,topGap,profile);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16,ResponsiveUtil.PAGE_PADDING,14,ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color:"+BG_SIDEBAR+";-fx-border-color:"+SIDEBAR_BORDER+";-fx-border-width:0 0 1 0;");

        Label title=label("AI Assistant",22,FontWeight.BOLD,TEXT_LIGHT);
        Label subtitle=label("Ask questions and get help from OneSpace AI",13,FontWeight.NORMAL,TEXT_MUTED_LIGHT);
        VBox heading=new VBox(2,title,subtitle);

        Button menuButton=new Button("☰  Menu");
        menuButton.setPrefHeight(38);
        menuButton.setPadding(new Insets(0,16,0,16));
        menuButton.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,13));
        menuButton.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:10;-fx-background-radius:10;-fx-text-fill:"+PRIMARY_BLUE+";-fx-cursor:hand;");

        menuPanel=createMenuPanel();

        Label chatIcon=label("💬",32,FontWeight.NORMAL,TEXT_DARK);
        Label conversationTitle=label("Start a conversation with OneSpace AI",20,FontWeight.BOLD,TEXT_DARK);
        Label conversationDescription=label("Ask questions, get explanations, brainstorm ideas, or get help with your work.",13,FontWeight.NORMAL,TEXT_MUTED_DARK);
        conversationDescription.setWrapText(true);
        conversationDescription.setMaxWidth(600);
        conversationDescription.setAlignment(Pos.CENTER);

        Button suggestion1=createSuggestionButton("✧  Explain something to me");
        Button suggestion2=createSuggestionButton("📄  Help me summarize text");
        Button suggestion3=createSuggestionButton("💡  Help me brainstorm");
        Button suggestion4=createSuggestionButton("☑  Create a task list");

        HBox suggestionRow1=new HBox(10,suggestion1,suggestion2,suggestion3);
        suggestionRow1.setAlignment(Pos.CENTER);
        HBox suggestionRow2=new HBox(suggestion4);
        suggestionRow2.setAlignment(Pos.CENTER);

        VBox suggestions=new VBox(10,suggestionRow1,suggestionRow2);
        suggestions.setAlignment(Pos.CENTER);

        aiEmptyState=new VBox(16,chatIcon,conversationTitle,conversationDescription,suggestions);
        aiEmptyState.setAlignment(Pos.CENTER);
        aiEmptyState.setPadding(new Insets(30,10,30,10));

        chatMessages=new VBox(12);
        chatMessages.setPadding(new Insets(10));
        chatMessages.setFillWidth(true);

        ProgressIndicator processingIndicator=new ProgressIndicator();
        processingIndicator.setPrefSize(18,18);

        Label processingLabel=label("Searching OneSpace and thinking...",13,FontWeight.NORMAL,TEXT_MUTED_DARK);

        processingRow=new HBox(8,processingIndicator,processingLabel);
        processingRow.setAlignment(Pos.CENTER_LEFT);
        processingRow.setPadding(new Insets(10,15,10,15));
        processingRow.setMaxWidth(320);
        processingRow.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-background-radius:16 16 16 4;");
        processingRow.setVisible(false);
        processingRow.setManaged(false);

        VBox chatContent=new VBox(8,chatMessages,processingRow);

        chatScroll=new ScrollPane(chatContent);
        chatScroll.setFitToWidth(true);
        chatScroll.setVisible(false);
        chatScroll.setManaged(false);
        chatScroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;-fx-padding:0;");
        VBox.setVgrow(chatScroll,Priority.ALWAYS);

        aiInput=new TextField();
        aiInput.setPromptText("Ask OneSpace AI...");
        aiInput.setPrefHeight(48);
        aiInput.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:24;-fx-background-radius:24;-fx-padding:0 55px 0 55px;-fx-font-size:13px;-fx-text-fill:"+TEXT_DARK+";-fx-prompt-text-fill:"+TEXT_MUTED_DARK+";");
        aiInput.setOnAction(e->sendMessage());

        Button plusButton=new Button("+");
        plusButton.setPrefSize(34,34);
        plusButton.setFont(Font.font(FONT,FontWeight.NORMAL,20));
        plusButton.setStyle("-fx-background-color:"+BG_CARD+";-fx-background-radius:50%;-fx-border-color:"+BORDER_CARD+";-fx-border-radius:50%;-fx-text-fill:"+TEXT_DARK+";-fx-cursor:hand;-fx-padding:0;");

        MenuItem uploadItem=new MenuItem("📎  Upload File");
        ContextMenu uploadMenu=new ContextMenu(uploadItem);

        uploadItem.setOnAction(e->{
            FileChooser chooser=new FileChooser();
            chooser.setTitle("Select File");
            File selected=chooser.showOpenDialog(aiInput.getScene().getWindow());
            if(selected!=null)aiInput.setText("Tell me about this file: "+selected.getName());
        });

        plusButton.setOnAction(e->{
            if(uploadMenu.isShowing())uploadMenu.hide();
            else uploadMenu.show(plusButton,javafx.geometry.Side.TOP,0,-5);
        });

        sendButton=new Button("➔");
        sendButton.setPrefSize(34,34);
        sendButton.setFont(Font.font(FONT,FontWeight.BOLD,13));
        sendButton.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-background-radius:50%;-fx-text-fill:#FFFFFF;-fx-cursor:hand;");
        sendButton.setOnAction(e->sendMessage());

        StackPane inputBox=new StackPane(aiInput,plusButton,sendButton);
        StackPane.setAlignment(plusButton,Pos.CENTER_LEFT);
        StackPane.setMargin(plusButton,new Insets(0,0,0,10));
        StackPane.setAlignment(sendButton,Pos.CENTER_RIGHT);
        StackPane.setMargin(sendButton,new Insets(0,8,0,0));

        Label disclaimer=label("AI responses may contain mistakes. Verify important information.",11,FontWeight.NORMAL,TEXT_MUTED_DARK);
        VBox promptArea=new VBox(10,inputBox,disclaimer);
        promptArea.setAlignment(Pos.CENTER);

        HBox menuRow=new HBox(menuButton);
        menuRow.setAlignment(Pos.CENTER_LEFT);

        VBox aiContent=new VBox(10,aiEmptyState,chatScroll,promptArea);
        VBox.setVgrow(aiEmptyState,Priority.ALWAYS);
        VBox.setVgrow(chatScroll,Priority.ALWAYS);

        VBox aiCard=new VBox(16,menuRow,aiContent);
        aiCard.setPadding(new Insets(24));
        aiCard.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:16;-fx-background-radius:16;-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.18),16,0,0,6);");
        VBox.setVgrow(aiContent,Priority.ALWAYS);

        StackPane cardContainer=new StackPane(aiCard,menuPanel);
        StackPane.setAlignment(menuPanel,Pos.CENTER_LEFT);
        VBox.setVgrow(cardContainer,Priority.ALWAYS);

        menuButton.setOnAction(e->openMenu());

        Button backButton=(Button)menuPanel.getProperties().get("back");
        Button newChat=(Button)menuPanel.getProperties().get("newChat");
        Button clearHistory=(Button)menuPanel.getProperties().get("clear");

        backButton.setOnAction(e->closeMenu());
        newChat.setOnAction(e->newChat());
        clearHistory.setOnAction(e->clearHistory());

        suggestion1.setOnAction(e->sendSuggestion("Explain something to me"));
        suggestion2.setOnAction(e->sendSuggestion("Help me summarize text"));
        suggestion3.setOnAction(e->sendSuggestion("Help me brainstorm"));
        suggestion4.setOnAction(e->sendSuggestion("Create a task list"));

        VBox body=new VBox(22,heading,cardContainer);
        body.setPadding(new Insets(24,ResponsiveUtil.PAGE_PADDING,28,ResponsiveUtil.PAGE_PADDING));
        body.setStyle("-fx-background-color:"+BG_CENTER_CANVAS+";");
        VBox.setVgrow(cardContainer,Priority.ALWAYS);

        ScrollPane pageScroll=new ScrollPane(body);
        pageScroll.setFitToWidth(true);
        pageScroll.setStyle("-fx-background-color:"+BG_CENTER_CANVAS+";-fx-background:"+BG_CENTER_CANVAS+";-fx-background-insets:0;-fx-padding:0;");
        VBox.setVgrow(pageScroll,Priority.ALWAYS);

        VBox main=new VBox(topBar,pageScroll);
        VBox.setVgrow(pageScroll,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setStyle("-fx-background-color:"+BG_SIDEBAR+";");
        root.setLeft(sidebar);
        root.setCenter(main);

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private void sendMessage(){
        String question=aiInput.getText().trim();
        if(question.isEmpty()||sendButton.isDisabled())return;

        String context=buildContext();
        chatHistory.add("You: "+question);
        addRecentQuery(question);
        addMessage(question,true);
        aiInput.clear();
        setChatMode(true);
        setProcessing(true);

        Thread thread=new Thread(()->{
            try{
                UserSession session=UserSession.getInstance();
                if(session==null||session.getUid()==null||session.getUid().isBlank())
                    throw new IllegalStateException("No active user session.");

                List<FileData> files=fileDAO.searchFilesForAI(session.getUid(),question);
                String fileContext=buildFileContext(files);
                String response=geminiClient.chat(question,context,fileContext);

                Platform.runLater(()->{
                    setProcessing(false);
                    chatHistory.add("AI: "+response);
                    addMessage(response,false);
                    addReferencedFiles(files);
                    aiInput.setDisable(false);
                    sendButton.setDisable(false);
                    sendButton.setText("➔");
                    aiInput.requestFocus();
                    scrollToBottom();
                });
            }catch(Exception e){
                String error=e.getMessage()==null?"Unknown error":e.getMessage();
                Platform.runLater(()->{
                    setProcessing(false);
                    addMessage("Unable to get a response.\n\n"+error,false);
                    aiInput.setDisable(false);
                    sendButton.setDisable(false);
                    sendButton.setText("➔");
                    aiInput.requestFocus();
                    scrollToBottom();
                });
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private String buildFileContext(List<FileData> files){
        if(files==null||files.isEmpty())return "";

        StringBuilder context=new StringBuilder();

        for(FileData file:files){
            context.append("File: ").append(safe(file.getFileName())).append("\n");
            context.append("Description: ").append(safe(file.getDescription())).append("\n");
            context.append("Category: ").append(safe(file.getAiCategory())).append("\n");
            context.append("Tags: ").append(file.getSmartTags()==null?"":String.join(", ",file.getSmartTags())).append("\n");
            context.append("Content: ").append(safe(file.getExtractedSnippet())).append("\n\n");
        }

        return context.toString();
    }

    private void addReferencedFiles(List<FileData> files){
        if(files==null||files.isEmpty())return;

        Label title=label("Referenced from OneSpace",11,FontWeight.BOLD,TEXT_MUTED_DARK);
        VBox fileList=new VBox(6);
        fileList.setPadding(new Insets(8,0,0,0));

        for(FileData file:files){
            Button fileButton=new Button("📄  "+safe(file.getFileName()));
            fileButton.setMaxWidth(500);
            fileButton.setAlignment(Pos.CENTER_LEFT);
            fileButton.setPrefHeight(34);
            fileButton.setPadding(new Insets(0,12,0,12));
            fileButton.setFont(Font.font(FONT,FontWeight.MEDIUM,12));
            fileButton.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:8;-fx-background-radius:8;-fx-text-fill:"+PRIMARY_BLUE+";-fx-cursor:hand;");
            fileButton.setOnAction(e->openFile(file));
            fileList.getChildren().add(fileButton);
        }

        VBox referenceBox=new VBox(3,title,fileList);
        referenceBox.setPadding(new Insets(8,12,10,12));
        referenceBox.setMaxWidth(540);
        referenceBox.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-background-radius:10;");

        HBox row=new HBox(referenceBox);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(0,0,0,10));
        chatMessages.getChildren().add(row);
    }

    private void openFile(FileData file){
        try{
            File selected=new File(file.getLocalPath());
            if(!selected.exists()){
                showInfo("File Not Found","The referenced file is no longer available at its saved location.");
                return;
            }
            Desktop.getDesktop().open(selected);
        }catch(Exception e){
            showInfo("Unable to Open File","Could not open the referenced file.");
        }
    }

    private String safe(String value){
        return value==null?"":value;
    }

    private void addRecentQuery(String query){
        recentQueries.remove(query);
        recentQueries.add(0,query);
        if(recentQueries.size()>8)recentQueries.remove(8);
        refreshRecentQueries();
    }

    private void refreshRecentQueries(){
        if(recentList==null)return;
        recentList.getChildren().clear();

        if(recentQueries.isEmpty()){
            Label empty=label("No recent queries",12,FontWeight.NORMAL,TEXT_MUTED_DARK);
            empty.setPadding(new Insets(10,8,10,8));
            recentList.getChildren().add(empty);
            return;
        }

        for(String query:recentQueries){
            Button item=new Button(query);
            item.setMaxWidth(Double.MAX_VALUE);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setWrapText(true);
            item.setPrefHeight(42);
            item.setPadding(new Insets(0,10,0,10));
            item.setFont(Font.font(FONT,FontWeight.NORMAL,12));
            item.setStyle("-fx-background-color:transparent;-fx-text-fill:"+TEXT_DARK+";-fx-background-radius:8;-fx-cursor:hand;");
            item.setOnMouseEntered(e->item.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-text-fill:"+TEXT_DARK+";-fx-background-radius:8;-fx-cursor:hand;"));
            item.setOnMouseExited(e->item.setStyle("-fx-background-color:transparent;-fx-text-fill:"+TEXT_DARK+";-fx-background-radius:8;-fx-cursor:hand;"));
            item.setOnAction(e->{
                aiInput.setText(query);
                closeMenu();
                aiInput.requestFocus();
            });
            recentList.getChildren().add(item);
        }
    }

    private void setProcessing(boolean processing){
        processingRow.setVisible(processing);
        processingRow.setManaged(processing);
        aiInput.setDisable(processing);
        sendButton.setDisable(processing);
        sendButton.setText(processing?"…":"➔");
        if(processing)scrollToBottom();
    }

    private String buildContext(){
        if(chatHistory.isEmpty())return "";

        StringBuilder context=new StringBuilder();
        int start=Math.max(0,chatHistory.size()-8);

        for(int i=start;i<chatHistory.size();i++)
            context.append(chatHistory.get(i)).append("\n");

        return context.toString();
    }

    private void sendSuggestion(String text){
        aiInput.setText(text);
        sendMessage();
    }

    private void setChatMode(boolean active){
        aiEmptyState.setVisible(!active);
        aiEmptyState.setManaged(!active);
        chatScroll.setVisible(active);
        chatScroll.setManaged(active);
    }

    private void addMessage(String text,boolean user){
        Label message=label(text,13,FontWeight.NORMAL,user?TEXT_LIGHT:TEXT_DARK);
        message.setWrapText(true);
        message.setMaxWidth(650);

        HBox bubble=new HBox(message);
        bubble.setPadding(new Insets(11,15,11,15));
        bubble.setMaxWidth(700);
        bubble.setStyle(user
                ?"-fx-background-color:"+PRIMARY_BLUE+";-fx-background-radius:16 16 4 16;"
                :"-fx-background-color:"+BG_CARD_INNER+";-fx-background-radius:16 16 16 4;");

        HBox row=new HBox(bubble);
        row.setAlignment(user?Pos.CENTER_RIGHT:Pos.CENTER_LEFT);
        chatMessages.getChildren().add(row);
        scrollToBottom();
    }

    private void scrollToBottom(){
        Platform.runLater(()->{
            chatScroll.layout();
            chatScroll.setVvalue(1.0);
        });
    }

    private void newChat(){
        chatHistory.clear();
        chatMessages.getChildren().clear();
        aiInput.clear();
        setProcessing(false);
        setChatMode(false);
        closeMenu();
        aiInput.requestFocus();
    }

    private void clearHistory(){
        recentQueries.clear();
        refreshRecentQueries();
    }

    private void openMenu(){
        menuPanel.setVisible(true);
        menuPanel.setManaged(true);
        menuPanel.setTranslateX(-260);

        TranslateTransition transition=new TranslateTransition(Duration.millis(220),menuPanel);
        transition.setFromX(-260);
        transition.setToX(0);
        transition.play();
    }

    private void closeMenu(){
        TranslateTransition transition=new TranslateTransition(Duration.millis(220),menuPanel);
        transition.setFromX(0);
        transition.setToX(-260);
        transition.setOnFinished(e->{
            menuPanel.setVisible(false);
            menuPanel.setManaged(false);
        });
        transition.play();
    }

    private VBox createMenuPanel(){
        VBox panel=new VBox(12);
        panel.setPrefWidth(255);
        panel.setMinWidth(255);
        panel.setMaxWidth(255);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color:"+BG_CARD+";-fx-border-color:"+BORDER_CARD+";-fx-border-width:0 1 0 0;-fx-background-radius:16 0 0 16;-fx-border-radius:16 0 0 16;");

        Button back=new Button("←  Back");
        back.setPrefHeight(38);
        back.setMaxWidth(Double.MAX_VALUE);
        back.setAlignment(Pos.CENTER_LEFT);
        back.setPadding(new Insets(0,10,0,6));
        back.setFont(Font.font(FONT,FontWeight.MEDIUM,13));
        back.setStyle("-fx-background-color:transparent;-fx-text-fill:"+TEXT_DARK+";-fx-background-radius:8;-fx-cursor:hand;");

        Label title=label("OneSpace AI",18,FontWeight.BOLD,TEXT_DARK);
        Label subtitle=label("Your recent conversations",11,FontWeight.NORMAL,TEXT_MUTED_DARK);

        Button newChat=new Button("＋  New Chat");
        newChat.setMaxWidth(Double.MAX_VALUE);
        newChat.setPrefHeight(42);
        newChat.setAlignment(Pos.CENTER_LEFT);
        newChat.setPadding(new Insets(0,12,0,12));
        newChat.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,13));
        newChat.setStyle("-fx-background-color:"+PRIMARY_BLUE+";-fx-text-fill:#FFFFFF;-fx-background-radius:9;-fx-cursor:hand;");

        Label recentTitle=label("Recent",12,FontWeight.BOLD,TEXT_MUTED_DARK);

        recentList=new VBox(3);
        recentList.setFillWidth(true);
        refreshRecentQueries();

        ScrollPane recentScroll=new ScrollPane(recentList);
        recentScroll.setFitToWidth(true);
        recentScroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;-fx-padding:0;");
        VBox.setVgrow(recentScroll,Priority.ALWAYS);

        Button clear=new Button("🗑  Clear Recent");
        clear.setMaxWidth(Double.MAX_VALUE);
        clear.setPrefHeight(38);
        clear.setAlignment(Pos.CENTER_LEFT);
        clear.setPadding(new Insets(0,10,0,10));
        clear.setFont(Font.font(FONT,FontWeight.MEDIUM,12));
        clear.setStyle("-fx-background-color:transparent;-fx-text-fill:"+TEXT_MUTED_DARK+";-fx-background-radius:8;-fx-cursor:hand;");
        clear.setOnMouseEntered(e->clear.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-text-fill:"+TEXT_DARK+";-fx-background-radius:8;-fx-cursor:hand;"));
        clear.setOnMouseExited(e->clear.setStyle("-fx-background-color:transparent;-fx-text-fill:"+TEXT_MUTED_DARK+";-fx-background-radius:8;-fx-cursor:hand;"));

        panel.getChildren().addAll(back,new Separator(),title,subtitle,newChat,recentTitle,recentScroll,clear);
        panel.getProperties().put("back",back);
        panel.getProperties().put("newChat",newChat);
        panel.getProperties().put("clear",clear);
        panel.setVisible(false);
        panel.setManaged(false);

        return panel;
    }

    private StackPane createOneSpaceLogo(){
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

    private Button createSuggestionButton(String text){
        Button button=new Button(text);
        button.setPrefHeight(36);
        button.setPadding(new Insets(0,14,0,14));
        button.setFont(Font.font(FONT,FontWeight.MEDIUM,12));
        button.setStyle("-fx-background-color:"+BG_CARD_INNER+";-fx-border-color:"+BORDER_CARD+";-fx-border-radius:18;-fx-background-radius:18;-fx-text-fill:"+PRIMARY_BLUE+";-fx-cursor:hand;");
        return button;
    }

    private Label label(String text,double size,FontWeight weight,String color){
        Label label=new Label(text);
        label.setFont(Font.font(FONT,weight,size));
        label.setStyle("-fx-text-fill:"+color+";");
        return label;
    }

    private void showInfo(String title,String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setMinWidth(450);
        alert.showAndWait();
    }
}