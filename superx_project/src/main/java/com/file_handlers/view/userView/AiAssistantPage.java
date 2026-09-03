package com.file_handlers.view.userView;

import com.file_handlers.dao.AIChatDAO;
import com.file_handlers.dao.FileDAO;
import com.file_handlers.model.AIChatData;
import com.file_handlers.model.AIMessageData;
import com.file_handlers.model.FileData;
import com.file_handlers.model.UserSession;
import com.file_handlers.service.GeminiClient;
import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.view.LandingPage;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AiAssistantPage{
    private static final String FONT="Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String SIDEBAR_BG="#070C16";
    private static final String SIDEBAR_BORDER="rgba(255, 255, 255, 0.07)";
    private static final String MAIN_BG="radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";
    private static final String CARD_BG="linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BG_INNER="linear-gradient(to bottom right, rgba(13, 22, 38, 0.9), rgba(8, 14, 26, 0.95))";
    private static final String CARD_BORDER="rgba(56, 189, 248, 0.22)";
    private static final String INPUT_BG="rgba(13, 22, 38, 0.85)";
    private static final String INPUT_BORDER="rgba(255, 255, 255, 0.1)";
    private static final String WHITE="#FFFFFF";
    private static final String LIGHT_SECONDARY="#94A3B8";
    private static final String BLUE="#2563EB";

    private final List<String> chatHistory=new ArrayList<>();
    private final List<String> recentQueries=new ArrayList<>();
    private final GeminiClient geminiClient=new GeminiClient();
    private final FileDAO fileDAO=new FileDAO();
    private final AIChatDAO aiChatDAO=new AIChatDAO();

    private VBox chatMessages,aiEmptyState,menuPanel,recentList;
    private TextField aiInput;
    private Button sendButton;
    private ScrollPane chatScroll;
    private HBox processingRow;

    private String currentChatId;
    private boolean chatTitleCreated;

    public Scene getAiAssistantPageScene(){
        String activeUserName="User",initials="U";

        if(UserSession.getInstance()!=null&&
                UserSession.getInstance().getDisplayName()!=null&&
                !UserSession.getInstance().getDisplayName().trim().isEmpty()){
            String fullName=UserSession.getInstance().getDisplayName().trim();
            activeUserName=fullName.split("\\s+")[0];
            initials=activeUserName.substring(0,1).toUpperCase();
        }

        VBox sidebar=createSidebar();

        SVGPath bellIcon=createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button bellBtn=new Button();
        bellBtn.setGraphic(bellIcon);
        bellBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        bellBtn.setOnAction(e->LandingPage.showNotificationPage());

        Label avatar=label(initials,12,FontWeight.BOLD,WHITE);
        avatar.setMinSize(34,34);
        avatar.setPrefSize(34,34);
        avatar.setMaxSize(34,34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");

        Label userName=label(activeUserName,13,FontWeight.SEMI_BOLD,WHITE);
        Label arrow=label("⌄",12,FontWeight.NORMAL,LIGHT_SECONDARY);

        HBox profileOption=new HBox(8,avatar,userName,arrow);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(4,12,4,6));
        profileOption.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");

        Popup userDropdownPopup=new Popup();
        userDropdownPopup.setAutoHide(true);

        Button profileDropdownBtn=new Button("👥   Profile");
        profileDropdownBtn.setMaxWidth(Double.MAX_VALUE);
        profileDropdownBtn.setAlignment(Pos.CENTER_LEFT);
        profileDropdownBtn.setStyle("-fx-background-color: transparent;-fx-text-fill: #F59E0B;-fx-font-size: 14px;-fx-font-family: "+FONT+";-fx-padding: 8 12;-fx-cursor: hand;");
        profileDropdownBtn.setOnMouseEntered(e->profileDropdownBtn.setStyle("-fx-background-color: #1E293B;-fx-text-fill: #F59E0B;-fx-font-size: 14px;-fx-font-family: "+FONT+";-fx-padding: 8 12;-fx-cursor: hand;-fx-background-radius: 6;"));
        profileDropdownBtn.setOnMouseExited(e->profileDropdownBtn.setStyle("-fx-background-color: transparent;-fx-text-fill: #F59E0B;-fx-font-size: 14px;-fx-font-family: "+FONT+";-fx-padding: 8 12;-fx-cursor: hand;"));
        profileDropdownBtn.setOnAction(e->{userDropdownPopup.hide();LandingPage.showUserProfilePage();});

        Button settingsDropdownBtn=new Button("⚙   Settings");
        settingsDropdownBtn.setMaxWidth(Double.MAX_VALUE);
        settingsDropdownBtn.setAlignment(Pos.CENTER_LEFT);
        settingsDropdownBtn.setStyle("-fx-background-color: transparent;-fx-text-fill: #38BDF8;-fx-font-size: 14px;-fx-font-family: "+FONT+";-fx-padding: 8 12;-fx-cursor: hand;");
        settingsDropdownBtn.setOnMouseEntered(e->settingsDropdownBtn.setStyle("-fx-background-color: #1E293B;-fx-text-fill: #38BDF8;-fx-font-size: 14px;-fx-font-family: "+FONT+";-fx-padding: 8 12;-fx-cursor: hand;-fx-background-radius: 6;"));
        settingsDropdownBtn.setOnMouseExited(e->settingsDropdownBtn.setStyle("-fx-background-color: transparent;-fx-text-fill: #38BDF8;-fx-font-size: 14px;-fx-font-family: "+FONT+";-fx-padding: 8 12;-fx-cursor: hand;"));
        settingsDropdownBtn.setOnAction(e->{userDropdownPopup.hide();LandingPage.showSettingPage();});

        Separator dropdownSeparator=new Separator();
        dropdownSeparator.setStyle("-fx-background-color: #1E293B; -fx-padding: 4 0;");

        Button logoutDropdownBtn=new Button("↳   Logout");
        logoutDropdownBtn.setMaxWidth(Double.MAX_VALUE);
        logoutDropdownBtn.setAlignment(Pos.CENTER_LEFT);
        logoutDropdownBtn.setStyle("-fx-background-color: transparent;-fx-text-fill: #F87171;-fx-font-size: 14px;-fx-font-family: "+FONT+";-fx-padding: 8 12;-fx-cursor: hand;");
        logoutDropdownBtn.setOnMouseEntered(e->logoutDropdownBtn.setStyle("-fx-background-color: #1E293B;-fx-text-fill: #F87171;-fx-font-size: 14px;-fx-font-family: "+FONT+";-fx-padding: 8 12;-fx-cursor: hand;-fx-background-radius: 6;"));
        logoutDropdownBtn.setOnMouseExited(e->logoutDropdownBtn.setStyle("-fx-background-color: transparent;-fx-text-fill: #F87171;-fx-font-size: 14px;-fx-font-family: "+FONT+";-fx-padding: 8 12;-fx-cursor: hand;"));
        logoutDropdownBtn.setOnAction(e->{userDropdownPopup.hide();UserSession.clearSession();LandingPage.showUserLoginPage();});

        VBox dropdownContainer=new VBox(4,profileDropdownBtn,settingsDropdownBtn,dropdownSeparator,logoutDropdownBtn);
        dropdownContainer.setPadding(new Insets(8));
        dropdownContainer.setPrefWidth(180);
        dropdownContainer.setStyle("-fx-background-color: #0A121E;-fx-border-color: #1E2D42;-fx-border-width: 1px;-fx-border-radius: 12px;-fx-background-radius: 12px;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 16, 0, 0, 8);");

        userDropdownPopup.getContent().add(dropdownContainer);

        profileOption.setOnMouseClicked(e->{
            if(userDropdownPopup.isShowing()) userDropdownPopup.hide();
            else{
                javafx.geometry.Point2D point=profileOption.localToScreen(0,profileOption.getHeight()+6);
                userDropdownPopup.show(profileOption,point.getX(),point.getY());
            }
        });

        HBox profileBox=new HBox(10,bellBtn,profileOption);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar=new HBox(20,new Region(),profileBox);
        HBox.setHgrow(topBar.getChildren().get(0),Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70);
        topBar.setMinHeight(70);
        topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16,ResponsiveUtil.PAGE_PADDING,14,ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: "+SIDEBAR_BORDER+"; -fx-border-width: 0 0 1 0;");

        Label title=label("AI Assistant",26,FontWeight.BOLD,WHITE);
        Label subtitle=label("Ask questions and get help from OneSpace AI",13,FontWeight.MEDIUM,LIGHT_SECONDARY);
        VBox heading=new VBox(4,title,subtitle);

        Button menuButton=new Button("☰   Menu");
        menuButton.setPrefHeight(38);
        menuButton.setPadding(new Insets(0,16,0,16));
        menuButton.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,13));
        menuButton.setStyle("-fx-background-color: "+INPUT_BG+"; -fx-border-color: "+CARD_BORDER+"; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: #38BDF8; -fx-cursor: hand;");
        menuButton.setOnMouseEntered(e->menuButton.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-border-color: #38BDF8; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: #38BDF8; -fx-cursor: hand;"));
        menuButton.setOnMouseExited(e->menuButton.setStyle("-fx-background-color: "+INPUT_BG+"; -fx-border-color: "+CARD_BORDER+"; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: #38BDF8; -fx-cursor: hand;"));

        menuPanel=createMenuPanel();

        SVGPath aiIcon=createIcon("ai");
        aiIcon.setStroke(Color.web("#38BDF8"));
        aiIcon.setStrokeWidth(1.5);
        aiIcon.setScaleX(2.5);
        aiIcon.setScaleY(2.5);

        Label conversationTitle=label("Start a conversation with OneSpace AI",22,FontWeight.BOLD,WHITE);
        Label conversationDescription=label("Ask questions, get explanations, brainstorm ideas, or get help with your work.",14,FontWeight.MEDIUM,LIGHT_SECONDARY);
        conversationDescription.setWrapText(true);
        conversationDescription.setMaxWidth(600);
        conversationDescription.setAlignment(Pos.CENTER);

        Button suggestion1=createSuggestionButton("📄   Summarize a file");
        Button suggestion2=createSuggestionButton("🔎   Find information in my files");
        Button suggestion3=createSuggestionButton("📁   Help me organize my files");
        Button suggestion4=createSuggestionButton("✦   Explain my document");

        HBox suggestionRow1=new HBox(10,suggestion1,suggestion2,suggestion3);
        suggestionRow1.setAlignment(Pos.CENTER);
        HBox suggestionRow2=new HBox(suggestion4);
        suggestionRow2.setAlignment(Pos.CENTER);

        VBox suggestions=new VBox(12,suggestionRow1,suggestionRow2);
        suggestions.setAlignment(Pos.CENTER);
        suggestions.setPadding(new Insets(10,0,0,0));

        aiEmptyState=new VBox(18,aiIcon,conversationTitle,conversationDescription,suggestions);
        aiEmptyState.setAlignment(Pos.CENTER);
        aiEmptyState.setPadding(new Insets(40,10,40,10));

        chatMessages=new VBox(16);
        chatMessages.setPadding(new Insets(10,15,10,15));
        chatMessages.setFillWidth(true);

        ProgressIndicator processingIndicator=new ProgressIndicator();
        processingIndicator.setPrefSize(18,18);
        processingIndicator.setStyle("-fx-progress-color: #38BDF8;");

        Label processingLabel=label("Searching OneSpace and thinking...",13,FontWeight.MEDIUM,WHITE);

        processingRow=new HBox(10,processingIndicator,processingLabel);
        processingRow.setAlignment(Pos.CENTER_LEFT);
        processingRow.setPadding(new Insets(12,16,12,16));
        processingRow.setMaxWidth(340);
        processingRow.setStyle("-fx-background-color: "+INPUT_BG+"; -fx-border-color: "+INPUT_BORDER+"; -fx-background-radius: 16 16 16 4; -fx-border-radius: 16 16 16 4;");
        processingRow.setVisible(false);
        processingRow.setManaged(false);

        VBox chatContent=new VBox(12,chatMessages,processingRow);

        chatScroll=new ScrollPane(chatContent);
        chatScroll.setFitToWidth(true);
        chatScroll.setVisible(false);
        chatScroll.setManaged(false);
        chatScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        VBox.setVgrow(chatScroll,Priority.ALWAYS);

        aiInput=new TextField();
        aiInput.setPromptText("Ask OneSpace AI...");
        aiInput.setPrefHeight(54);
        aiInput.setStyle("-fx-background-color: "+INPUT_BG+"; -fx-border-color: "+CARD_BORDER+"; -fx-border-radius: 27; -fx-background-radius: 27; -fx-padding: 0 60px 0 60px; -fx-font-size: 14px; -fx-text-fill: "+WHITE+"; -fx-prompt-text-fill: "+LIGHT_SECONDARY+"; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 16, 0, 0, 8);");
        aiInput.setOnAction(e->sendMessage());

        SVGPath attachIcon=createIcon("plus");
        attachIcon.setStroke(Color.web(LIGHT_SECONDARY));
        attachIcon.setStrokeWidth(2.5);

        Button plusButton=new Button();
        plusButton.setGraphic(attachIcon);
        plusButton.setPrefSize(38,38);
        plusButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 50%; -fx-cursor: hand; -fx-padding: 0;");
        plusButton.setOnMouseEntered(e->{plusButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.15); -fx-background-radius: 50%; -fx-cursor: hand; -fx-padding: 0;");attachIcon.setStroke(Color.WHITE);});
        plusButton.setOnMouseExited(e->{plusButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 50%; -fx-cursor: hand; -fx-padding: 0;");attachIcon.setStroke(Color.web(LIGHT_SECONDARY));});

        MenuItem uploadItem=new MenuItem("📎   Upload File");
        uploadItem.setStyle("-fx-text-fill: #000000;");
        ContextMenu uploadMenu=new ContextMenu(uploadItem);

        uploadItem.setOnAction(e->{
            FileChooser chooser=new FileChooser();
            chooser.setTitle("Select File");
            File selected=chooser.showOpenDialog(aiInput.getScene().getWindow());
            if(selected!=null) aiInput.setText("Tell me about this file: "+selected.getName());
        });

        plusButton.setOnAction(e->{
            if(uploadMenu.isShowing()) uploadMenu.hide();
            else uploadMenu.show(plusButton,javafx.geometry.Side.TOP,0,-5);
        });

        Button sendButtonNode=new Button("➔");
        sendButtonNode.setPrefSize(38,38);
        sendButtonNode.setFont(Font.font(FONT,FontWeight.BOLD,15));
        sendButtonNode.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-text-fill: #FFFFFF; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.6), 12, 0, 0, 4);");
        sendButtonNode.setOnAction(e->sendMessage());
        this.sendButton=sendButtonNode;

        StackPane inputBox=new StackPane(aiInput,plusButton,sendButtonNode);
        StackPane.setAlignment(plusButton,Pos.CENTER_LEFT);
        StackPane.setMargin(plusButton,new Insets(0,0,0,8));
        StackPane.setAlignment(sendButtonNode,Pos.CENTER_RIGHT);
        StackPane.setMargin(sendButtonNode,new Insets(0,8,0,0));
        inputBox.setMaxWidth(800);

        Label disclaimer=label("AI responses may contain mistakes. Verify important information.",11,FontWeight.NORMAL,LIGHT_SECONDARY);
        VBox promptArea=new VBox(10,inputBox,disclaimer);
        promptArea.setAlignment(Pos.CENTER);
        promptArea.setPadding(new Insets(10,0,0,0));

        HBox menuRow=new HBox(menuButton);
        menuRow.setAlignment(Pos.CENTER_LEFT);

        VBox aiContent=new VBox(10,aiEmptyState,chatScroll,promptArea);
        VBox.setVgrow(aiEmptyState,Priority.ALWAYS);
        VBox.setVgrow(chatScroll,Priority.ALWAYS);

        VBox aiCard=new VBox(16,menuRow,aiContent);
        aiCard.setPadding(new Insets(24));
        aiCard.setStyle("-fx-background-color: "+CARD_BG+"; -fx-border-color: "+CARD_BORDER+"; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);");
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

        suggestion1.setOnAction(e->sendSuggestion("Summarize my files"));
        suggestion2.setOnAction(e->sendSuggestion("Find important information in my files"));
        suggestion3.setOnAction(e->sendSuggestion("Help me organize my files"));
        suggestion4.setOnAction(e->sendSuggestion("Explain my document"));

        VBox body=new VBox(22,heading,cardContainer);
        body.setPadding(new Insets(24,ResponsiveUtil.PAGE_PADDING,28,ResponsiveUtil.PAGE_PADDING));
        body.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(cardContainer,Priority.ALWAYS);

        ScrollPane pageScroll=new ScrollPane(body);
        pageScroll.setFitToWidth(true);
        pageScroll.setFitToHeight(true);
        pageScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pageScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        pageScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        VBox.setVgrow(pageScroll,Priority.ALWAYS);

        VBox main=new VBox(topBar,pageScroll);
        main.setStyle("-fx-background: "+MAIN_BG+"; -fx-background-color: "+MAIN_BG+";");
        VBox.setVgrow(pageScroll,Priority.ALWAYS);

        BorderPane root=new BorderPane();
        root.setStyle("-fx-background-color: "+SIDEBAR_BG+";");
        root.setLeft(sidebar);
        root.setCenter(main);

        loadChatHistory();

        return new Scene(root,LandingPage.getCurrentWidth(),LandingPage.getCurrentHeight());
    }

    private void loadChatHistory(){
        UserSession session=UserSession.getInstance();

        if(session==null||session.getUid()==null||session.getUid().isBlank())
            return;

        Thread thread=new Thread(()->{
            try{
                List<AIChatData> chats=aiChatDAO.getChats(session.getUid());

                if(chats==null||chats.isEmpty()){
                    String chatId=aiChatDAO.createChat(session.getUid(),"New Chat");

                    Platform.runLater(()->{
                        currentChatId=chatId;
                        chatTitleCreated=false;
                    });

                    return;
                }

                AIChatData latest=chats.get(0);
                List<AIMessageData> messages=aiChatDAO.getMessages(session.getUid(),latest.getChatId());

                Platform.runLater(()->{
                    currentChatId=latest.getChatId();
                    chatTitleCreated=latest.getTitle()!=null&&!latest.getTitle().equals("New Chat");

                    chatHistory.clear();
                    chatMessages.getChildren().clear();

                    for(AIMessageData message:messages){
                        boolean user="user".equalsIgnoreCase(message.getRole());
                        String prefix=user?"You: ":"AI: ";

                        chatHistory.add(prefix+safe(message.getContent()));
                        addMessage(message.getContent(),user);

                        if(user)
                            addRecentQuery(message.getContent());
                    }

                    if(!messages.isEmpty())
                        setChatMode(true);

                    scrollToBottom();
                });

            }catch(Exception e){
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void saveMessage(String role,String content){
        UserSession session=UserSession.getInstance();

        if(session==null||session.getUid()==null||session.getUid().isBlank()||currentChatId==null||currentChatId.isBlank())
            return;

        String uid=session.getUid();
        String chatId=currentChatId;

        Thread thread=new Thread(()->{
            try{
                aiChatDAO.saveMessage(uid,chatId,role,content);
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void updateChatTitle(String question){
        UserSession session=UserSession.getInstance();

        if(session==null||session.getUid()==null||session.getUid().isBlank()||currentChatId==null||currentChatId.isBlank())
            return;

        String title=question.trim();

        if(title.length()>45)
            title=title.substring(0,45).trim()+"...";

        String uid=session.getUid();
        String chatId=currentChatId;
        String finalTitle=title;

        Thread thread=new Thread(()->{
            try{
                aiChatDAO.updateChatTitle(uid,chatId,finalTitle);
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private VBox createSidebar(){
        Image logoImage=new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
        ImageView logoView=new ImageView(logoImage);
        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane logoIcon=new StackPane(logoView);
        logoIcon.setPrefSize(42,42);
        logoIcon.setAlignment(Pos.CENTER);

        Label logoText=label("OneSpace",19,FontWeight.BOLD,WHITE);
        HBox logoHeader=new HBox(10,logoIcon,logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox=new VBox(4,logoHeader);
        logoBox.setPadding(new Insets(0,0,18,6));

        Button dashboardBtn=createSidebarButton("dashboard","Dashboard",false,e->LandingPage.showUserDashboard());
        Button spacesBtn=createSidebarButton("files","Spaces",false,e->LandingPage.showUserSpace());
        Button searchBtn=createSidebarButton("search","Search",false,e->LandingPage.showUserSearch());
        Button calendarBtn=createSidebarButton("calendar","Calendar",false,e->LandingPage.showCalendarPage());
        Button aiBtn=createSidebarButton("ai","AI Assistant",true,e->LandingPage.showAiAssistantPage());
        Button collabBtn=createSidebarButton("collaboration","Collaboration",false,e->LandingPage.showCollaborationPage());
        Button recentBtn=createSidebarButton("recent","Recent",false,e->LandingPage.showRecentPage());
        Button trashBtn=createSidebarButton("trash","Trash",false,e->LandingPage.showTrashPage());
        Button settingsBtn=createSidebarButton("settings","Settings",false,e->LandingPage.showSettingPage());

        VBox navList=new VBox(4,dashboardBtn,spacesBtn,searchBtn,calendarBtn,aiBtn,collabBtn,recentBtn,trashBtn);

        Label storageTitle=label("Storage Used",12,FontWeight.BOLD,WHITE);
        Label storageVal=label("64.2 GB of 100 GB",12,FontWeight.BOLD,WHITE);
        Label storagePercent=label("64%",11,FontWeight.BOLD,LIGHT_SECONDARY);

        Region storageSpacer=new Region();
        HBox.setHgrow(storageSpacer,Priority.ALWAYS);

        HBox storageValGroup=new HBox(storageVal,storageSpacer,storagePercent);
        storageValGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar sidebarProgress=new ProgressBar(.64);
        sidebarProgress.setMaxWidth(Double.MAX_VALUE);
        sidebarProgress.setPrefHeight(6);
        sidebarProgress.setStyle("-fx-accent: "+BLUE+"; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        Button manageStorageBtn=new Button("Storage Index ›");
        manageStorageBtn.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e->LandingPage.showStorageIndexPage());

        VBox storageCard=new VBox(8,storageTitle,storageValGroup,sidebarProgress,manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: rgba(16, 28, 48, 0.65); -fx-border-color: "+SIDEBAR_BORDER+"; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer=new Region();
        VBox.setVgrow(sidebarSpacer,Priority.ALWAYS);

        VBox sidebar=new VBox(12,logoBox,navList,sidebarSpacer,settingsBtn,storageCard);
        sidebar.setPadding(new Insets(20,14,20,14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: "+SIDEBAR_BG+"; -fx-border-color: "+SIDEBAR_BORDER+"; -fx-border-width: 0 1 0 0;");

        return sidebar;
    }

    private Button createSidebarButton(String iconType,String text,boolean active,javafx.event.EventHandler<javafx.event.ActionEvent> action){
        SVGPath icon=createIcon(iconType);
        icon.setStroke(Color.web(active?WHITE:LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox=new StackPane(icon);
        iconBox.setPrefSize(24,24);

        Label label=label(text,13,active?FontWeight.BOLD:FontWeight.MEDIUM,WHITE);

        HBox row=new HBox(12,iconBox,label);
        row.setAlignment(Pos.CENTER_LEFT);

        Button button=new Button();
        button.setGraphic(row);
        button.setPrefHeight(38);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0,12,0,12));
        button.setOnAction(action);

        if(active){
            button.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);-fx-background-radius: 12;-fx-border-color: rgba(96, 165, 250, 0.6);-fx-border-radius: 12;-fx-border-width: 1;-fx-cursor: hand;-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);");
        }else{
            button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
            button.setOnMouseEntered(e->{button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");icon.setStroke(Color.WHITE);label.setTextFill(Color.WHITE);});
            button.setOnMouseExited(e->{button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");icon.setStroke(Color.web(LIGHT_SECONDARY));label.setTextFill(Color.web(WHITE));});
        }

        return button;
    }

    private void sendMessage(){
        String question=aiInput.getText().trim();

        if(question.isEmpty()||sendButton.isDisabled())
            return;

        String context=buildContext();

        chatHistory.add("You: "+question);
        addRecentQuery(question);
        addMessage(question,true);
        saveMessage("user",question);

        if(!chatTitleCreated){
            chatTitleCreated=true;
            updateChatTitle(question);
        }

        aiInput.clear();
        setChatMode(true);
        setProcessing(true);

        Thread thread=new Thread(()->{
            try{
                UserSession session=UserSession.getInstance();

                if(session==null||session.getUid()==null||session.getUid().isBlank())
                    throw new IllegalStateException("No active user session.");

                List<FileData> files=fileDAO.searchFilesForAI(session.getUid(),question);

                String response;

                if(!isOneSpaceQuery(question,files)){
                    response="I'm OneSpace AI, so I can only help with your OneSpace files, Spaces, and application.";
                }else{
                    String fileContext=buildFileContext(files);
                    response=geminiClient.chat(question,context,fileContext);
                }

                Platform.runLater(()->{
                    setProcessing(false);
                    chatHistory.add("AI: "+response);
                    addMessage(response,false);
                    saveMessage("assistant",response);

                    if(isOneSpaceQuery(question,files))
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

                    String errorMessage="Unable to get a response.\n\n"+error;

                    addMessage(errorMessage,false);
                    saveMessage("assistant",errorMessage);

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

    private boolean isOneSpaceQuery(String question,List<FileData> files){
        if(files!=null&&!files.isEmpty())
            return true;

        String query=question.toLowerCase();

        String[] keywords={
            "onespace","one space","file","files","document","documents",
            "space","spaces","storage","upload","uploaded","search","folder",
            "local file","local files","dashboard","assistant","ai","trash",
            "recent","calendar","collaboration","settings","profile",
            "personal","college","office","finance","entertainment"
        };

        for(String keyword:keywords)
            if(query.contains(keyword))
                return true;

        return false;
    }

    private String buildFileContext(List<FileData> files){
        if(files==null||files.isEmpty())
            return "";

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
        if(files==null||files.isEmpty())
            return;

        Label title=label("Referenced from OneSpace",11,FontWeight.BOLD,LIGHT_SECONDARY);
        VBox fileList=new VBox(8);
        fileList.setPadding(new Insets(8,0,0,0));

        for(FileData file:files){
            Button fileButton=new Button("📄   "+safe(file.getFileName()));
            fileButton.setMaxWidth(500);
            fileButton.setAlignment(Pos.CENTER_LEFT);
            fileButton.setPrefHeight(38);
            fileButton.setPadding(new Insets(0,14,0,14));
            fileButton.setFont(Font.font(FONT,FontWeight.MEDIUM,13));
            fileButton.setStyle("-fx-background-color: "+INPUT_BG+"; -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #38BDF8; -fx-cursor: hand;");
            fileButton.setOnMouseEntered(e->fileButton.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-border-color: #38BDF8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #38BDF8; -fx-cursor: hand;"));
            fileButton.setOnMouseExited(e->fileButton.setStyle("-fx-background-color: "+INPUT_BG+"; -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #38BDF8; -fx-cursor: hand;"));
            fileButton.setOnAction(e->openFile(file));
            fileList.getChildren().add(fileButton);
        }

        VBox referenceBox=new VBox(6,title,fileList);
        referenceBox.setPadding(new Insets(12,16,14,16));
        referenceBox.setMaxWidth(540);
        referenceBox.setStyle("-fx-background-color: rgba(13, 22, 38, 0.5); -fx-background-radius: 12; -fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-radius: 12;");

        HBox row=new HBox(referenceBox);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(4,0,4,10));
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

        if(recentQueries.size()>8)
            recentQueries.remove(8);

        refreshRecentQueries();
    }

    private void refreshRecentQueries(){
        if(recentList==null)
            return;

        recentList.getChildren().clear();

        if(recentQueries.isEmpty()){
            Label empty=label("No recent queries",12,FontWeight.NORMAL,LIGHT_SECONDARY);
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
            item.setStyle("-fx-background-color: transparent; -fx-text-fill: "+WHITE+"; -fx-background-radius: 8; -fx-cursor: hand;");
            item.setOnMouseEntered(e->item.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-text-fill: "+WHITE+"; -fx-background-radius: 8; -fx-cursor: hand;"));
            item.setOnMouseExited(e->item.setStyle("-fx-background-color: transparent; -fx-text-fill: "+WHITE+"; -fx-background-radius: 8; -fx-cursor: hand;"));
            item.setOnAction(e->{aiInput.setText(query);closeMenu();aiInput.requestFocus();});
            recentList.getChildren().add(item);
        }
    }

    private void setProcessing(boolean processing){
        processingRow.setVisible(processing);
        processingRow.setManaged(processing);
        aiInput.setDisable(processing);
        sendButton.setDisable(processing);
        sendButton.setText(processing?"…":"➔");

        if(processing)
            scrollToBottom();
    }

    private String buildContext(){
        if(chatHistory.isEmpty())
            return "";

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
        Label message=label(text,14,FontWeight.NORMAL,WHITE);
        message.setWrapText(true);
        message.setMaxWidth(650);

        HBox bubble=new HBox(message);
        bubble.setPadding(new Insets(12,16,12,16));
        bubble.setMaxWidth(700);
        bubble.setStyle(user
                ?"-fx-background-color: linear-gradient(to bottom right, #1D4ED8, #2563EB); -fx-background-radius: 18 18 4 18; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.4), 10, 0, 0, 2);"
                :"-fx-background-color: "+INPUT_BG+"; -fx-border-color: "+INPUT_BORDER+"; -fx-background-radius: 18 18 18 4; -fx-border-radius: 18 18 18 4; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 2);");

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
        UserSession session=UserSession.getInstance();

        if(session==null||session.getUid()==null||session.getUid().isBlank())
            return;

        Thread thread=new Thread(()->{
            try{
                String chatId=aiChatDAO.createChat(session.getUid(),"New Chat");

                Platform.runLater(()->{
                    currentChatId=chatId;
                    chatTitleCreated=false;
                    chatHistory.clear();
                    recentQueries.clear();
                    chatMessages.getChildren().clear();
                    aiInput.clear();
                    setProcessing(false);
                    setChatMode(false);
                    refreshRecentQueries();
                    closeMenu();
                    aiInput.requestFocus();
                });

            }catch(Exception e){
                Platform.runLater(()->showInfo("Unable to Create Chat","Could not create a new AI conversation."));
            }
        });

        thread.setDaemon(true);
        thread.start();
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
        panel.setPrefWidth(260);
        panel.setMinWidth(260);
        panel.setMaxWidth(260);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: "+CARD_BG_INNER+"; -fx-border-color: "+INPUT_BORDER+"; -fx-border-width: 0 1 0 0; -fx-background-radius: 20 0 0 20; -fx-border-radius: 20 0 0 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 4, 0, 0);");

        Button back=new Button("←   Back");
        back.setPrefHeight(38);
        back.setMaxWidth(Double.MAX_VALUE);
        back.setAlignment(Pos.CENTER_LEFT);
        back.setPadding(new Insets(0,10,0,6));
        back.setFont(Font.font(FONT,FontWeight.MEDIUM,13));
        back.setStyle("-fx-background-color: transparent; -fx-text-fill: "+LIGHT_SECONDARY+"; -fx-background-radius: 8; -fx-cursor: hand;");
        back.setOnMouseEntered(e->back.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-text-fill: "+WHITE+"; -fx-background-radius: 8; -fx-cursor: hand;"));
        back.setOnMouseExited(e->back.setStyle("-fx-background-color: transparent; -fx-text-fill: "+LIGHT_SECONDARY+"; -fx-background-radius: 8; -fx-cursor: hand;"));

        Label title=label("OneSpace AI",18,FontWeight.BOLD,WHITE);
        Label subtitle=label("Your recent conversations",11,FontWeight.NORMAL,LIGHT_SECONDARY);

        Button newChat=new Button("＋   New Chat");
        newChat.setMaxWidth(Double.MAX_VALUE);
        newChat.setPrefHeight(42);
        newChat.setAlignment(Pos.CENTER_LEFT);
        newChat.setPadding(new Insets(0,12,0,12));
        newChat.setFont(Font.font(FONT,FontWeight.SEMI_BOLD,13));
        newChat.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: #FFFFFF; -fx-background-radius: 9; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.4), 10, 0, 0, 2);");

        Label recentTitle=label("Recent",12,FontWeight.BOLD,LIGHT_SECONDARY);

        recentList=new VBox(4);
        recentList.setFillWidth(true);
        refreshRecentQueries();

        ScrollPane recentScroll=new ScrollPane(recentList);
        recentScroll.setFitToWidth(true);
        recentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        recentScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        recentScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        VBox.setVgrow(recentScroll,Priority.ALWAYS);

        Button clear=new Button("🗑   Clear Recent");
        clear.setMaxWidth(Double.MAX_VALUE);
        clear.setPrefHeight(38);
        clear.setAlignment(Pos.CENTER_LEFT);
        clear.setPadding(new Insets(0,10,0,10));
        clear.setFont(Font.font(FONT,FontWeight.MEDIUM,12));
        clear.setStyle("-fx-background-color: transparent; -fx-text-fill: "+LIGHT_SECONDARY+"; -fx-background-radius: 8; -fx-cursor: hand;");
        clear.setOnMouseEntered(e->clear.setStyle("-fx-background-color: rgba(239, 68, 68, 0.15); -fx-text-fill: #F87171; -fx-background-radius: 8; -fx-cursor: hand;"));
        clear.setOnMouseExited(e->clear.setStyle("-fx-background-color: transparent; -fx-text-fill: "+LIGHT_SECONDARY+"; -fx-background-radius: 8; -fx-cursor: hand;"));

        Separator sep=new Separator();
        sep.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08);");

        panel.getChildren().addAll(back,sep,title,subtitle,newChat,recentTitle,recentScroll,clear);
        panel.getProperties().put("back",back);
        panel.getProperties().put("newChat",newChat);
        panel.getProperties().put("clear",clear);
        panel.setVisible(false);
        panel.setManaged(false);

        return panel;
    }

    private Button createSuggestionButton(String text){
        Button button=new Button(text);
        button.setPrefHeight(38);
        button.setPadding(new Insets(0,16,0,16));
        button.setFont(Font.font(FONT,FontWeight.MEDIUM,13));
        button.setStyle("-fx-background-color: rgba(56, 189, 248, 0.1); -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 19; -fx-background-radius: 19; -fx-text-fill: #38BDF8; -fx-cursor: hand;");
        button.setOnMouseEntered(e->button.setStyle("-fx-background-color: rgba(56, 189, 248, 0.2); -fx-border-color: #38BDF8; -fx-border-radius: 19; -fx-background-radius: 19; -fx-text-fill: #38BDF8; -fx-cursor: hand;"));
        button.setOnMouseExited(e->button.setStyle("-fx-background-color: rgba(56, 189, 248, 0.1); -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 19; -fx-background-radius: 19; -fx-text-fill: #38BDF8; -fx-cursor: hand;"));
        return button;
    }

    private Label label(String text,double size,FontWeight weight,String color){
        Label label=new Label(text);
        label.setFont(Font.font(FONT,weight,size));
        label.setStyle("-fx-text-fill: "+color+";");
        return label;
    }

    private SVGPath createIcon(String type){
        SVGPath icon=new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);

        switch(type){
            case "dashboard": icon.setContent("M3 3 H10 V10 H3 Z M14 3 H21 V10 H14 Z M3 14 H10 V21 H3 Z M14 14 H21 V21 H14 Z"); break;
            case "files": icon.setContent("M5 2 H14 L19 7 V21 H5 Z M14 2 V7 H19 M8 11 H16 M8 15 H16 M8 18 H13"); break;
            case "search": icon.setContent("M10 3 A7 7 0 1 0 10 17 A7 7 0 0 0 10 3 Z M15 15 L21 21"); break;
            case "calendar": icon.setContent("M19 4H5C3.89543 4 3 4.89543 3 6V20C3 21.1046 3.89543 22 5 22H19C20.1046 22 21 21.1046 21 20V6C21 4.89543 20.1046 4 19 4Z M16 2V6 M8 2V6 M3 10H21"); break;
            case "ai": icon.setContent("M12 2 L13.5 8.5 L20 7 L15.5 11.5 L21 15 L14 14.5 L12 22 L10 14.5 L3 15 L8.5 11.5 L4 7 L10.5 8.5 Z"); break;
            case "collaboration": icon.setContent("M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2 M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M23 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75"); break;
            case "recent": icon.setContent("M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"); break;
            case "trash": icon.setContent("M3 6h18 M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"); break;
            case "settings": icon.setContent("M12 3 V6 M12 18 V21 M3 12 H6 M18 12 H21 M5.6 5.6 L7.7 7.7 M16.3 16.3 L18.4 18.4 M18.4 5.6 L16.3 7.7 M7.7 16.3 L5.6 18.4 M12 8 A4 4 0 1 0 12 16 A4 4 0 0 0 12 8"); break;
            case "bell": icon.setContent("M6 17 H18 M8 17 V10 A4 4 0 0 1 16 10 V17 M10 20 H14"); break;
            case "plus": icon.setContent("M12 5v14M5 12h14"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }

        return icon;
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