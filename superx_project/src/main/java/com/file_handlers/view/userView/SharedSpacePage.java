package com.file_handlers.view.userView;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.cloudinary.utils.ObjectUtils;

import com.file_handlers.config.FirebaseConfig;
import com.file_handlers.controller.CollaborationController;
import com.file_handlers.model.CollaborationFileData;
import com.file_handlers.model.CollaborationMemberData;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.dao.CommentDAO;
import com.file_handlers.util.MentionUtil;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

public class SharedSpacePage {

    // Typography
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar Tones (Matching CollaborationPage)
    private static final String SIDEBAR_BG = "#070C16";
    private static final String SIDEBAR_BORDER = "rgba(255, 255, 255, 0.07)";

    // 2. Center Workspace Canvas: Atmospheric Dark Radial Glow
    private static final String MAIN_BG = "radial-gradient(center 70% 20%, radius 80%, #0D1F3D 0%, #060B14 60%, #03060A 100%)";

    // 3. Main Glassmorphic Cards & Container Colors
    private static final String CARD_BG = "linear-gradient(to bottom right, rgba(16, 28, 48, 0.85), rgba(9, 16, 30, 0.95))";
    private static final String CARD_BG_INNER = "linear-gradient(to bottom right, rgba(13, 22, 38, 0.9), rgba(8, 14, 26, 0.95))";
    private static final String CARD_BORDER = "rgba(56, 189, 248, 0.22)";
    private static final String INPUT_BG = "rgba(13, 22, 38, 0.85)";
    private static final String INPUT_BORDER = "rgba(255, 255, 255, 0.1)";

    // 4. Vibrant Typography & Accent Highlights
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_SECONDARY = "#94A3B8";
    private static final String BLUE = "#2563EB";
    private static final String SUCCESS = "#34D399";
    private static final String ORANGE = "#FBBF24";
    private static final String RED = "#F87171";

    private String spaceName;
    private final List<CollaborationMemberData> membersList = new ArrayList<>();
    private final List<CollaborationFileData> filesList = new ArrayList<>();

    private VBox memberListBox;
    private VBox fileListBox;
    private TextField memberSearchField;
    private TextField fileSearchField;
    private Label memberCountLabel;
    private Label fileCountLabel;
    private Label createdDateLabel;
    private Label ownerNameLabel;
    private Button manageAccessButton;
    private ScrollPane mainScrollPane;

    private String currentUserRole = "Owner";
    private String createdDate = "26 Aug 2026";
    private String workspaceOwnerName = UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null 
        ? UserSession.getInstance().getDisplayName() 
        : "Workspace Owner";    
    private String workspaceOwnerEmail = "";

    // Overlay Stack Container & Floating Chat Panel tracking
    private StackPane stackContainer;
    private VBox floatingChatPanel;
    private boolean isDiscussionOpen = false;

    public SharedSpacePage() {
        this("Shared Space");
    }

    public SharedSpacePage(String spaceName) {
        this(spaceName, 0, 0, "Owner");
    }

    // Cached constructor accepting initial state to eliminate flash/re-fetch from zero
    public SharedSpacePage(String spaceName, int initialMembers, int initialFiles, String initialRole) {
        this.spaceName =
                spaceName == null || spaceName.trim().isEmpty()
                        ? "Shared Space"
                        : spaceName.trim();
        
        if (initialRole != null && !initialRole.isEmpty()) {
            this.currentUserRole = initialRole;
        }
        
        loadDefaultData();
        
        Platform.runLater(() -> {
            refreshMemberList();
            updateMemberCount();
            if (memberCountLabel != null && initialMembers > 0) {
                memberCountLabel.setText(initialMembers + " Members");
            }
            if (fileCountLabel != null && initialFiles >= 0) {
                fileCountLabel.setText(initialFiles + " Files");
            }
        });
        
        checkUserAccessAndLoadContent();
    }

    private String getLoggedInUserRole() {
        String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";
        if (myEmail == null || myEmail.isEmpty()) {
            return currentUserRole != null ? currentUserRole : "Viewer";
        }

        if (workspaceOwnerEmail != null && !workspaceOwnerEmail.isEmpty() && workspaceOwnerEmail.equalsIgnoreCase(myEmail)) {
            return "Owner";
        }

        for (CollaborationMemberData m : membersList) {
            if (m.email != null && m.email.equalsIgnoreCase(myEmail)) {
                if (m.role != null && !m.role.isEmpty()) {
                    return m.role;
                }
            }
        }
        return currentUserRole != null ? currentUserRole : "Viewer";
    }

    private boolean isCurrentLoggedInUserOwner() {
        String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";
        if (myEmail == null || myEmail.isEmpty()) {
            return false;
        }
        if (workspaceOwnerEmail != null && !workspaceOwnerEmail.isEmpty() && workspaceOwnerEmail.equalsIgnoreCase(myEmail)) {
            return true;
        }
        for (CollaborationMemberData m : membersList) {
            if (m.email != null && m.email.equalsIgnoreCase(myEmail)) {
                if ("Owner".equalsIgnoreCase(m.role)) {
                    return true;
                }
            }
        }
        return "Owner".equalsIgnoreCase(currentUserRole);
    }

    private void listenForRealtimeFiles(String docId) {
        try {
            com.google.cloud.firestore.Firestore db = FirebaseConfig.getFirestore();
            db.collection("workspaces")
                .document(docId)
                .collection("files")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }
                    if (snapshots != null) {
                        for (com.google.cloud.firestore.DocumentChange docChange : snapshots.getDocumentChanges()) {
                            CollaborationFileData cloudFile = docChange.getDocument().toObject(CollaborationFileData.class);
                            Platform.runLater(() -> {
                                if (cloudFile != null && cloudFile.fileName != null) {
                                    if (cloudFile.size == null || cloudFile.size.isEmpty()) cloudFile.size = "1.2 MB";
                                    if (cloudFile.uploadedOn == null || cloudFile.uploadedOn.isEmpty()) cloudFile.uploadedOn = "26 Aug 2026";
                                }
                                if (docChange.getType() == com.google.cloud.firestore.DocumentChange.Type.ADDED ||
                                    docChange.getType() == com.google.cloud.firestore.DocumentChange.Type.MODIFIED) {
                                    if (cloudFile != null && cloudFile.fileName != null) {
                                        filesList.removeIf(f -> f.fileName != null && f.fileName.equalsIgnoreCase(cloudFile.fileName));
                                        filesList.add(cloudFile);
                                        refreshFileList();
                                        updateFileCount();
                                    }
                                } else if (docChange.getType() == com.google.cloud.firestore.DocumentChange.Type.REMOVED) {
                                    if (cloudFile != null && cloudFile.fileName != null) {
                                        filesList.removeIf(f -> f.fileName != null && f.fileName.equalsIgnoreCase(cloudFile.fileName));
                                        refreshFileList();
                                        updateFileCount();
                                    }
                                }
                            });
                        }
                    }
                });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void listenForRealtimeMembers(String docId) {
        try {
            FirebaseConfig.getFirestore()
                .collection("workspaces")
                .document(docId)
                .collection("members")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        error.printStackTrace();
                        return;
                    }
                    if (value != null) {
                        for (com.google.cloud.firestore.DocumentChange docChange : value.getDocumentChanges()) {
                            CollaborationMemberData cloudMember = docChange.getDocument().toObject(CollaborationMemberData.class);
                            Platform.runLater(() -> {
                                if (docChange.getType() == com.google.cloud.firestore.DocumentChange.Type.ADDED) {
                                    boolean exists = membersList.stream().anyMatch(m -> m.email != null && m.email.equalsIgnoreCase(cloudMember.email));
                                    if (!exists) {
                                        membersList.add(cloudMember);
                                        refreshMemberList();
                                        updateMemberCount();
                                    }
                                } else if (docChange.getType() == com.google.cloud.firestore.DocumentChange.Type.MODIFIED) {
                                    for (CollaborationMemberData m : membersList) {
                                        if (m.email != null && m.email.equalsIgnoreCase(cloudMember.email)) {
                                            m.role = cloudMember.role;
                                            m.status = cloudMember.status;
                                            break;
                                        }
                                    }
                                    refreshMemberList();
                                } else if (docChange.getType() == com.google.cloud.firestore.DocumentChange.Type.REMOVED) {
                                    membersList.removeIf(m -> m.email != null && m.email.equalsIgnoreCase(cloudMember.email));
                                    refreshMemberList();
                                    updateMemberCount();
                                }
                            });
                        }
                    }
                });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadDefaultData() {
        membersList.clear();
        filesList.clear();

        membersList.add(new CollaborationMemberData(
                "AV", workspaceOwnerName, workspaceOwnerEmail,
                "Owner", "#38BDF822", "#38BDF8", "active"));
    }

    private boolean hasPermission(String role, String permission) {
        if (role == null || permission == null) return false;

        switch (role) {
            case "Owner":
                return permission.equals("VIEW")
                        || permission.equals("SEARCH")
                        || permission.equals("DOWNLOAD")
                        || permission.equals("UPLOAD")
                        || permission.equals("EDIT_FILE")
                        || permission.equals("DELETE_FILE")
                        || permission.equals("ADD_MEMBER");
            case "Editor":
            case "Moderator":
                return permission.equals("VIEW")
                        || permission.equals("SEARCH")
                        || permission.equals("DOWNLOAD")
                        || permission.equals("UPLOAD")
                        || permission.equals("EDIT_FILE")
                        || permission.equals("DELETE_FILE");
            case "Viewer":
                return permission.equals("VIEW")
                        || permission.equals("SEARCH")
                        || permission.equals("UPLOAD");
            default:
                return false;
        }
    }

    private boolean currentUserCan(String permission) {
        return hasPermission(getLoggedInUserRole(), permission);
    }

    public VBox getSharedSpaceContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        content.setStyle("-fx-background-color: transparent;");

        Button back = createBackButton();

        Label title = new Label(spaceName);
        title.setFont(Font.font(FONT, FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill:" + WHITE + ";");

        Label subtitle = new Label("Shared workspace");
        subtitle.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        subtitle.setStyle("-fx-text-fill:" + LIGHT_SECONDARY + ";");

        VBox titleBox = new VBox(4, title, subtitle);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        // Discussion floating toggle button
        Button discussionBtn = new Button("💬 Discussion");
        discussionBtn.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        discussionBtn.setStyle(
            "-fx-background-color: rgba(37, 99, 235, 0.2);" +
            "-fx-text-fill: #60A5FA;" +
            "-fx-border-color: rgba(37, 99, 235, 0.4);" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 6 14;"
        );
        discussionBtn.setOnAction(e -> toggleDiscussionPanel(discussionBtn));

        String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";
        boolean isOwner = isCurrentLoggedInUserOwner();

        HBox topHeaderBox;

        if (!isOwner) {
            Button leaveSpaceBtn = new Button("Leave Workspace");
            leaveSpaceBtn.setFont(Font.font(FONT, FontWeight.BOLD, 12));
            leaveSpaceBtn.setStyle(
                "-fx-background-color: rgba(239, 68, 68, 0.15);" +
                "-fx-text-fill: #F87171;" +
                "-fx-border-color: rgba(239, 68, 68, 0.4);" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 6 14;"
            );

            leaveSpaceBtn.setOnAction(e -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Leave Workspace");
                confirm.setHeaderText("Leave '" + spaceName + "'?");
                confirm.setContentText("You will lose access to the files and chat in this shared space.");

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        new Thread(() -> {
                            try {
                                var db = FirebaseConfig.getFirestore();
                                var workspaces = db.collection("workspaces").get().get().getDocuments();
                                String targetDocId = null;
                                
                                for (var wsDoc : workspaces) {
                                    String name = wsDoc.getString("spaceName");
                                    if (name == null) name = wsDoc.getString("name");
                                    if (spaceName.equals(name) || wsDoc.getId().equals(spaceName.replaceAll("\\s+", "_"))) {
                                        targetDocId = wsDoc.getId();
                                        break;
                                    }
                                }

                                if (targetDocId != null) {
                                    var membersDocs = db.collection("workspaces").document(targetDocId)
                                                        .collection("members").get().get().getDocuments();
                                    for (var mDoc : membersDocs) {
                                        String email = mDoc.getString("email");
                                        if (email != null && email.equalsIgnoreCase(myEmail)) {
                                            mDoc.getReference().delete();
                                            break;
                                        }
                                    }
                                }

                                Platform.runLater(() -> LandingPage.showCollaborationPage());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }).start();
                    }
                });
            });

            topHeaderBox = new HBox(12, back, titleBox, headerSpacer, discussionBtn, leaveSpaceBtn);
        } else {
            topHeaderBox = new HBox(12, back, titleBox, headerSpacer, discussionBtn);
        }
        topHeaderBox.setAlignment(Pos.CENTER_LEFT);

        HBox summary = createSummaryCard();
        VBox files = createFilesCard();
        VBox members = createMembersCard();

        HBox center = new HBox(20, files, members);
        HBox.setHgrow(files, Priority.ALWAYS);

        members.setPrefWidth(315);
        members.setMinWidth(290);

        refreshFileList();
        updateFileCount();
        refreshMemberList();
        updateMemberCount();

        content.getChildren().addAll(topHeaderBox, summary, center);
        VBox.setVgrow(center, Priority.ALWAYS);

        return content;
    }

    private Button createBackButton() {
        Button button = new Button("←");
        button.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        button.setTextFill(Color.web(WHITE));
        button.setPrefSize(38, 38);
        button.setMinSize(38, 38);
        button.setPadding(Insets.EMPTY);
        button.setStyle(
                "-fx-background-color: " + INPUT_BG + ";" +
                "-fx-border-color: " + INPUT_BORDER + ";" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                "-fx-border-color: rgba(255, 255, 255, 0.2);" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + INPUT_BG + ";" +
                "-fx-border-color: " + INPUT_BORDER + ";" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"));

        button.setOnAction(e -> LandingPage.showCollaborationPage());

        return button;
    }

    private HBox createSummaryCard() {
        HBox card = new HBox(14);
        card.setPrefHeight(130);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16));
        card.setStyle(cardContainerStyle());

        CollaborationMemberData currentOwner = membersList.stream()
                .filter(m -> m.role != null && m.role.equalsIgnoreCase("Owner"))
                .findFirst()
                .orElse(new CollaborationMemberData("AV", workspaceOwnerName, workspaceOwnerEmail, "Owner", "#38BDF822", "#38BDF8", "active"));

        VBox owner = createSummaryItem(
                "security", "Owner", currentOwner.name != null ? currentOwner.name : workspaceOwnerName,
                "#38BDF8", "rgba(56, 189, 248, 0.15)");
        ownerNameLabel = (Label) owner.getProperties().get("valueLabel");

        owner.setCursor(Cursor.HAND);
        owner.setOnMouseClicked(e -> {
            CollaborationMemberData realOwner = membersList.stream()
                    .filter(m -> m.role != null && m.role.equalsIgnoreCase("Owner"))
                    .findFirst()
                    .orElse(currentOwner);
            showOwnerDetailsPopup(realOwner);
        });

        VBox members = createSummaryItem(
                "users", "Members",
                membersList.size() + " Members",
                "#A78BFA", "rgba(167, 139, 250, 0.15)");

        memberCountLabel = (Label) members.getProperties().get("valueLabel");

        VBox files = createSummaryItem(
                "files", "Files",
                filesList.size() + " Files",
                "#34D399", "rgba(52, 211, 153, 0.15)");

        fileCountLabel = (Label) files.getProperties().get("valueLabel");

        VBox created = createSummaryItem(
                "calendar", "Created On", createdDate,
                "#FBBF24", "rgba(245, 158, 11, 0.15)");
        createdDateLabel = (Label) created.getProperties().get("valueLabel");

        card.getChildren().addAll(owner, members, files, created);
        return card;
    }

    private VBox createCommentsSection(String workspaceDocId, String fileDocId) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(16));
        container.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 14; -fx-background-radius: 14;");

        Label header = new Label(fileDocId == null ? "Workspace Live Chat" : "File Chat Thread");
        header.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        header.setStyle("-fx-text-fill: " + WHITE + ";");

        Button clearChatBtn = new Button("Clear Chat");
        clearChatBtn.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        clearChatBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.15); -fx-text-fill: #F87171; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 4 8;");
        
        VBox commentList = new VBox(10);
        commentList.setPadding(new Insets(8));
        ScrollPane scroll = new ScrollPane(commentList);
        scroll.setFitToWidth(true);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        scroll.lookupAll(".scroll-bar").forEach(node -> {
            node.setStyle("-fx-pref-width: 0; -fx-opacity: 0; -fx-background-color: transparent;");
        });

        clearChatBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Clear Chat");
            confirm.setHeaderText("Clear your chat history?");
            confirm.setContentText("This will clear the chat view for you only.");
            
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Platform.runLater(() -> {
                        commentList.getChildren().clear();
                        Label empty = new Label("No messages yet. Start the conversation!");
                        empty.setFont(Font.font(FONT, 12));
                        empty.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
                        commentList.getChildren().add(empty);
                    });

                    new Thread(() -> {
                        try {
                            String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "user";
                            com.google.cloud.firestore.Firestore db = FirebaseConfig.getFirestore();
                            String userStateId = myEmail.toLowerCase().replaceAll("[^a-z0-9]", "_");
                            
                            db.collection("workspaces")
                                .document(workspaceDocId)
                                .collection(fileDocId == null ? "user_chat_states" : "files_" + fileDocId + "_user_chat_states")
                                .document(userStateId)
                                .set(java.util.Map.of("clearedAt", com.google.cloud.firestore.FieldValue.serverTimestamp()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                }
            });
        });

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox headerRow = new HBox(10, header, headerSpacer, clearChatBtn);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        TextField inputField = new TextField();
        inputField.setPromptText("Type a message ...");
        inputField.setMinHeight(40);
        inputField.setPrefHeight(40);
        inputField.setMaxHeight(40);
        inputField.setStyle(
                "-fx-background-color:" + INPUT_BG + ";" +
                "-fx-text-fill:" + WHITE + ";" +
                "-fx-prompt-text-fill:" + LIGHT_SECONDARY + ";" +
                "-fx-border-color:" + INPUT_BORDER + ";" +
                "-fx-border-radius:20;" +
                "-fx-background-radius:20;" +
                "-fx-padding:0 14 0 14;"
        );

        Button sendBtn = new Button("Send");
        sendBtn.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        sendBtn.setPrefHeight(40);
        sendBtn.setPadding(new Insets(0, 18, 0, 18));
        sendBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-background-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-text-fill: white;"
        );

        java.util.function.Consumer<List<Map<String, Object>>> renderComments = (comments) -> {
            Platform.runLater(() -> {
                commentList.getChildren().clear();
                if (comments.isEmpty()) {
                    Label empty = new Label("No messages yet. Start the conversation!");
                    empty.setFont(Font.font(FONT, 12));
                    empty.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
                    commentList.getChildren().add(empty);
                    return;
                }
                
                String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";

                for (var c : comments) {
                    String author = (String) c.get("authorName");
                    String text = (String) c.get("text");
                    String email = (String) c.get("authorEmail");
                    
                    boolean isMe = email != null && !email.isEmpty() && email.equalsIgnoreCase(myEmail);

                    VBox bubble = new VBox(3);
                    bubble.setPadding(new Insets(10, 12, 10, 12));
                    bubble.setMaxWidth(260);
                    
                    Label authorLbl = new Label(isMe ? "You" : (author != null ? author : "User"));
                    authorLbl.setFont(Font.font(FONT, FontWeight.BOLD, 11));
                    authorLbl.setTextFill(Color.web("#FBBF24"));

                    Label textLbl = new Label(text);
                    textLbl.setFont(Font.font(FONT, 12));
                    textLbl.setTextFill(Color.web(WHITE));
                    textLbl.setWrapText(true);
                    bubble.getChildren().addAll(authorLbl, textLbl);

                    HBox wrapper = new HBox(bubble);
                    if (isMe) {
                        bubble.setStyle("-fx-background-color: rgba(37, 99, 235, 0.3); -fx-background-radius: 12 12 2 12; -fx-border-color: rgba(37, 99, 235, 0.5); -fx-border-radius: 12 12 2 12;");
                        wrapper.setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        bubble.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 12 12 12 2; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 12 12 12 2;");
                        wrapper.setAlignment(Pos.CENTER_LEFT);
                    }
                    commentList.getChildren().add(wrapper);
                }
                
                Platform.runLater(() -> {
                    scroll.layout();
                    scroll.setVvalue(1.0);
                });
            });
        };

        try {
            String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "user";
            String userStateId = myEmail.toLowerCase().replaceAll("[^a-z0-9]", "_");
            com.google.cloud.firestore.Firestore db = FirebaseConfig.getFirestore();
            
            com.google.cloud.firestore.CollectionReference commentsRef = db
                    .collection("workspaces")
                    .document(workspaceDocId)
                    .collection(fileDocId == null ? "comments" : "files_" + fileDocId + "_comments");

            com.google.cloud.firestore.DocumentReference userStateRef = db
                    .collection("workspaces")
                    .document(workspaceDocId)
                    .collection(fileDocId == null ? "user_chat_states" : "files_" + fileDocId + "_user_chat_states")
                    .document(userStateId);

            final com.google.cloud.Timestamp[] clearedAtHolder = new com.google.cloud.Timestamp[1];

            userStateRef.addSnapshotListener((userDoc, userErr) -> {
                if (userDoc != null && userDoc.exists() && userDoc.contains("clearedAt")) {
                    clearedAtHolder[0] = userDoc.getTimestamp("clearedAt");
                }
            });

            commentsRef.orderBy("timestamp", com.google.cloud.firestore.Query.Direction.ASCENDING)
                    .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                if (snapshots != null) {
                    List<Map<String, Object>> liveComments = new ArrayList<>();
                    com.google.cloud.Timestamp clearedAt = clearedAtHolder[0];

                    var sortedDocs = snapshots.getDocuments().stream().sorted((doc1, doc2) -> {
                        Object t1 = doc1.get("timestamp");
                        Object t2 = doc2.get("timestamp");
                        
                        Long time1 = 0L;
                        Long time2 = 0L;
                        
                        if (t1 instanceof com.google.cloud.Timestamp) {
                            time1 = ((com.google.cloud.Timestamp) t1).toDate().getTime();
                        } else if (t1 instanceof Long) {
                            time1 = (Long) t1;
                        }
                        
                        if (t2 instanceof com.google.cloud.Timestamp) {
                            time2 = ((com.google.cloud.Timestamp) t2).toDate().getTime();
                        } else if (t2 instanceof Long) {
                            time2 = (Long) t2;
                        }
                        
                        return Long.compare(time1, time2);
                    }).toList();

                    for (var doc : sortedDocs) {
                        Map<String, Object> data = doc.getData();
                        
                        com.google.cloud.Timestamp msgTime = null;
                        try {
                            msgTime = doc.getTimestamp("timestamp");
                        } catch (Exception ex) {
                            Object rawTime = doc.get("timestamp");
                            if (rawTime instanceof Long) {
                                msgTime = com.google.cloud.Timestamp.of(new java.util.Date((Long) rawTime));
                            }
                        }
                        
                        if (clearedAt != null && msgTime != null && msgTime.compareTo(clearedAt) <= 0) {
                            continue;
                        }
                        liveComments.add(data);
                    }
                    renderComments.accept(liveComments);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        CommentDAO commentDao = new CommentDAO();

        Runnable sendMessage = () -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                String myName = UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null 
                        ? UserSession.getInstance().getDisplayName() : "User";
                String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";

                commentDao.addComment(workspaceDocId, fileDocId, text, myName, myEmail, () -> {
                    Platform.runLater(() -> {
                        inputField.clear();
                        scroll.setVvalue(1.0);
                    });
                }, ex -> ex.printStackTrace());
            }
        };

        sendBtn.setOnAction(e -> sendMessage.run());
        inputField.setOnAction(e -> sendMessage.run());

        HBox inputBox = new HBox(8, inputField, sendBtn);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputBox.setAlignment(Pos.CENTER);

        container.getChildren().addAll(headerRow, scroll, inputBox);
        return container;
    }
    
    private VBox createSummaryItem(
            String iconType,
            String heading,
            String value,
            String iconColor,
            String iconBackground) {

        VBox box = new VBox(6);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefHeight(100);
        box.setPadding(new Insets(14));
        box.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 12; -fx-background-radius: 12;");

        HBox.setHgrow(box, Priority.ALWAYS);

        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setPrefSize(32, 32); iconPane.setMinSize(32, 32);
        iconPane.setStyle("-fx-background-color: " + iconBackground + "; -fx-background-radius: 8; -fx-border-color: " + iconColor + "44; -fx-border-radius: 8;");

        Label headingLabel = new Label(heading);
        headingLabel.setFont(Font.font(FONT, 11));
        headingLabel.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + "; -fx-font-weight: 600;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        valueLabel.setStyle("-fx-text-fill: " + WHITE + ";");

        box.getProperties().put("valueLabel", valueLabel);
        box.getChildren().addAll(iconPane, headingLabel, valueLabel);

        return box;
    }

    private void showOwnerDetailsPopup(CollaborationMemberData ownerMember) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Owner Details");
        dialog.setHeaderText("Owner Details");
        styleDialog(dialog, 420, 260);

        Label nameHeading = createPopupHeading("Name");
        Label nameValue = createPopupValue(ownerMember.name);

        Label emailHeading = createPopupHeading("Email");
        Label emailValue = createPopupValue(ownerMember.email);

        Label roleHeading = createPopupHeading("Role");

        Label roleValue = new Label(ownerMember.role);
        roleValue.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        roleValue.setTextFill(Color.web("#60A5FA"));
        roleValue.setPadding(new Insets(6, 12, 6, 12));
        roleValue.setStyle("-fx-background-color: rgba(37, 99, 235, 0.2); -fx-background-radius: 6;");

        VBox box = new VBox(
                10,
                nameHeading, nameValue,
                emailHeading, emailValue,
                roleHeading, roleValue);

        box.setPadding(new Insets(20));
        dialog.getDialogPane().setContent(box);
        addCloseButton(dialog);
        dialog.showAndWait();
    }

    private Label createPopupHeading(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        label.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
        return label;
    }

    private Label createPopupValue(String text) {
        Label label = new Label(text != null ? text : "");
        label.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        label.setStyle("-fx-text-fill: " + WHITE + ";");
        return label;
    }

    private void checkUserAccessAndLoadContent() {
        String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";
        
        try {
            com.google.cloud.firestore.Firestore db = FirebaseConfig.getFirestore();
            
            // Background async loader thread
            new Thread(() -> {
                try {
                    // Directly target the standardized document ID first for instant lookup
                    String standardizedDocId = spaceName.replaceAll("\\s+", "_");
                    com.google.cloud.firestore.DocumentReference spaceDocRef = db.collection("workspaces").document(standardizedDocId);
                    com.google.cloud.firestore.DocumentSnapshot docSnap = spaceDocRef.get().get();

                    // Fallback to iterating documents if the direct ID isn't found
                    if (!docSnap.exists()) {
                        var workspacesQuery = db.collection("workspaces").get().get().getDocuments();
                        for (var doc : workspacesQuery) {
                            String name = doc.getString("spaceName");
                            if (name == null) name = doc.getString("name");
                            
                            if (name != null && name.equalsIgnoreCase(spaceName)) {
                                spaceDocRef = doc.getReference();
                                docSnap = spaceDocRef.get().get();
                                break;
                            }
                        }
                    }

                    com.google.api.core.ApiFuture<com.google.cloud.firestore.QuerySnapshot> membersFuture = spaceDocRef.collection("members").get();
                    com.google.api.core.ApiFuture<com.google.cloud.firestore.QuerySnapshot> filesFuture = spaceDocRef.collection("files").get();

                    final String actualWorkspaceDocId = spaceDocRef.getId();

                    if (docSnap.exists() && docSnap.contains("createdAt")) {
                        Object cDate = docSnap.get("createdAt");
                        if (cDate instanceof com.google.cloud.Timestamp) {
                            java.util.Date date = ((com.google.cloud.Timestamp) cDate).toDate();
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("d MMM yyyy", java.util.Locale.ENGLISH);
                            createdDate = sdf.format(date);
                        } else if (cDate != null) {
                            String dateStr = cDate.toString();
                            createdDate = dateStr.length() > 11 ? dateStr.substring(0, 11) : dateStr;
                        }
                    }

                    boolean isAuthorized = false;
                    List<CollaborationMemberData> fetchedMembers = new ArrayList<>();
                    var memberResult = membersFuture.get();
                    
                    for (com.google.cloud.firestore.DocumentSnapshot doc : memberResult.getDocuments()) {
                        CollaborationMemberData member = doc.toObject(CollaborationMemberData.class);
                        if (member != null) {
                            fetchedMembers.add(member);
                            String email = member.email;
                            String status = member.status;
                            
                            if (email != null && email.equalsIgnoreCase(myEmail)) {
                                if ("active".equalsIgnoreCase(status) || "Owner".equalsIgnoreCase(member.role)) {
                                    isAuthorized = true;
                                }
                                if (member.role != null) {
                                    currentUserRole = member.role;
                                }
                            }
                            if ("Owner".equalsIgnoreCase(member.role)) {
                                if (member.name != null) workspaceOwnerName = member.name;
                                if (member.email != null) workspaceOwnerEmail = member.email;
                            }
                        }
                    }

                    if (fetchedMembers.isEmpty()) {
                        fetchedMembers.add(new CollaborationMemberData(
                                "AV", workspaceOwnerName, myEmail.isEmpty() ? workspaceOwnerEmail : myEmail,
                                "Owner", "#38BDF822", "#38BDF8", "active"));
                        isAuthorized = true;
                        currentUserRole = "Owner";
                    }

                    List<CollaborationFileData> fetchedFiles = new ArrayList<>();
                    try {
                        var fileDocs = filesFuture.get().getDocuments();
                        for (var fDoc : fileDocs) {
                            CollaborationFileData file = fDoc.toObject(CollaborationFileData.class);
                            if (file != null) {
                                if (file.size == null || file.size.equalsIgnoreCase("Cloud File") || file.size.equalsIgnoreCase("Local File") || file.size.isEmpty()) {
                                    file.size = "1.2 MB";
                                }
                                if (file.uploadedOn == null || file.uploadedOn.equalsIgnoreCase("Just now") || file.uploadedOn.isEmpty()) {
                                    file.uploadedOn = "26 Aug 2026";
                                }
                                fetchedFiles.add(file);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    final boolean allowed = isAuthorized;

                    Platform.runLater(() -> {
                        membersList.clear();
                        membersList.addAll(fetchedMembers);

                        filesList.clear();
                        filesList.addAll(fetchedFiles);

                        updateMemberCount();
                        updateFileCount();
                        
                        if (ownerNameLabel != null) ownerNameLabel.setText(workspaceOwnerName);
                        if (createdDateLabel != null) createdDateLabel.setText(createdDate);
                        if (manageAccessButton != null) updateManageAccessPermission(manageAccessButton);

                        refreshFileList();
                        refreshMemberList();

                        if (allowed || membersList.isEmpty()) {
                            listenForRealtimeFiles(actualWorkspaceDocId);
                            listenForRealtimeMembers(actualWorkspaceDocId);
                        } else {
                            showAccessDeniedPopup("Your invite is still pending. Please accept it in the Collaboration page first.");
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "shared-space-loader").start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addCloseButton(Dialog<ButtonType> dialog) {
        dialog.getDialogPane().getButtonTypes().add(
                new ButtonType("Close", ButtonData.CANCEL_CLOSE));
        Button closeBtn = (Button) dialog.getDialogPane().lookupButton(dialog.getDialogPane().getButtonTypes().get(dialog.getDialogPane().getButtonTypes().size() - 1));
        closeBtn.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand;");
    }

    private VBox createFilesCard() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(20));
        card.setStyle(cardContainerStyle());

        Label title = new Label("Files");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label subtitle = new Label("Files uploaded to this shared space");
        subtitle.setFont(Font.font(FONT, 11));
        subtitle.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox titleBox = new VBox(3, title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button upload = new Button("+ Upload File");
        upload.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        upload.setTextFill(Color.WHITE);
        upload.setPrefHeight(36);
        upload.setPadding(new Insets(0, 16, 0, 16));
        upload.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;");
        
        updateUploadPermission(upload);
        
        upload.setOnAction(e -> {
    if (!currentUserCan("UPLOAD")) {
        showAccessDeniedPopup("Only Owners, Editors, and Moderators can upload files.");
        return;
    }

    FileChooser chooser = new FileChooser();
    chooser.setTitle("Upload Files to Shared Space");
    
    // 1. Use showOpenMultipleDialog to select multiple files
    List<File> selectedFiles = chooser.showOpenMultipleDialog(upload.getScene().getWindow());

    if (selectedFiles != null && !selectedFiles.isEmpty()) {
        final String currentUserName = (UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null) 
                ? UserSession.getInstance().getDisplayName() : "User";

        upload.setDisable(true);
        upload.setText("Uploading...");

        String workspaceDocId = spaceName.replaceAll("\\s+", "_");

        // 2. Loop through each file and trigger the upload controller
        new Thread(() -> {
            try {
                CollaborationController controller = new CollaborationController();
                for (File selectedFile : selectedFiles) {
                    if (selectedFile != null && selectedFile.exists()) {
                        // Upload each file sequentially
                        controller.addFileToWorkspace(workspaceDocId, selectedFile, currentUserName,
                            successPublicId -> {
                                // Individual file success callback (optional logging)
                            },
                            ex -> {
                                ex.printStackTrace();
                            }
                        );
                    }
                }
                
                Platform.runLater(() -> {
                    upload.setDisable(false);
                    upload.setText("+ Upload File");
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    ex.printStackTrace();
                    upload.setDisable(false);
                    upload.setText("+ Upload File");
                    showAccessDeniedPopup("Upload failed: " + ex.getMessage());
                });
            }
        }, "multi-file-upload-thread").start();
    }
});
        HBox titleRow = new HBox(10, titleBox, spacer, upload);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        fileSearchField = createSearchField("Search files...");
        fileSearchField.textProperty().addListener((obs, oldValue, newValue) -> refreshFileList());
                
        HBox tableHeader = new HBox();
        tableHeader.setPadding(new Insets(7, 10, 7, 10));

        Label name = new Label("Name");
        Label size = new Label("Size");
        Label uploaded = new Label("Uploaded On");
        Label more = new Label("");

        styleTableHeader(name);
        styleTableHeader(size);
        styleTableHeader(uploaded);

        name.setPrefWidth(260);
        size.setPrefWidth(110);
        uploaded.setPrefWidth(180);
        more.setPrefWidth(30);

        HBox.setHgrow(name, Priority.ALWAYS);
        tableHeader.getChildren().addAll(name, size, uploaded, more);

        fileListBox = new VBox(0);
        
       ScrollPane fileScroll = new ScrollPane(fileListBox);
fileScroll.setFitToWidth(true);
fileScroll.setPrefHeight(180);
fileScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
fileScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable vertical scrollbar policy
fileScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

// Hide the track and thumb nodes completely
fileScroll.lookupAll(".scroll-bar").forEach(node -> {
    node.setStyle("-fx-pref-width: 0; -fx-pref-height: 0; -fx-opacity: 0; -fx-background-color: transparent;");
});

        refreshFileList();

        Button viewAll = createViewAllButton("View all files ›");
        viewAll.setOnAction(e -> showAllFilesPopup());

        card.getChildren().addAll(
                titleRow,
                fileSearchField,
                tableHeader,
                fileScroll,
                viewAll);

        VBox.setVgrow(fileScroll, Priority.ALWAYS);

        return card;
    }

    private TextField createSearchField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefHeight(38);
        field.setMaxWidth(Double.MAX_VALUE);
        field.setStyle(
                "-fx-background-color:" + INPUT_BG + ";" +
                "-fx-text-fill:" + WHITE + ";" +
                "-fx-prompt-text-fill:" + LIGHT_SECONDARY + ";" +
                "-fx-border-color:" + INPUT_BORDER + ";" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-padding:0 12 0 12;");
        return field;
    }

    private void updateUploadPermission(Button button) {
        boolean allowed = currentUserCan("UPLOAD");
        button.setDisable(!allowed);
        button.setOpacity(allowed ? 1.0 : 0.55);
    }

    private VBox createMembersCard() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(20));
        card.setStyle(cardContainerStyle());

        Label title = new Label("Members");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        manageAccessButton = new Button("Manage Access");
        manageAccessButton.setPrefHeight(34);
        manageAccessButton.setPrefWidth(130);
        manageAccessButton.setAlignment(Pos.CENTER);
        manageAccessButton.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        manageAccessButton.setTextFill(Color.WHITE);
        manageAccessButton.setStyle(
        "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
        "-fx-background-radius: 7;" +
        "-fx-cursor: hand;");

        updateManageAccessPermission(manageAccessButton);

        manageAccessButton.setOnAction(e -> {
            if (!isCurrentLoggedInUserOwner()) {
                showAccessDeniedPopup("Only the workspace Owner can manage access.");
                return;
            }
            showManageAccessPopup();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox titleRow = new HBox(10, title, spacer, manageAccessButton);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        memberSearchField = createSearchField("Search members...");
        memberSearchField.textProperty().addListener((obs, oldValue, newValue) -> refreshMemberList());

        memberListBox = new VBox(0);
        
        ScrollPane memberScroll = new ScrollPane(memberListBox);
        memberScroll.setFitToWidth(true);
        memberScroll.setPrefHeight(180);
        memberScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        memberScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        refreshMemberList();

        Button viewAll = createViewAllButton("View all members ›");
        viewAll.setOnAction(e -> showAllMembersPopup());

        Button addMember = new Button("+ Add Member");
        addMember.setMaxWidth(Double.MAX_VALUE);
        addMember.setPrefHeight(38);
        addMember.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        addMember.setTextFill(Color.WHITE);
        addMember.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;");

        updateAddMemberPermission(addMember);

        addMember.setOnAction(e -> {
            if (!currentUserCan("ADD_MEMBER")) {
                showAccessDeniedPopup("Only the Owner can add members.");
                return;
            }
            showAddMemberPopup();
        });

        card.getChildren().addAll(
                titleRow,
                memberSearchField,
                memberScroll,
                viewAll,
                addMember);

        VBox.setVgrow(memberScroll, Priority.ALWAYS);

        return card;
    }

    private void updateManageAccessPermission(Button manageBtn) {
        boolean isOwner = isCurrentLoggedInUserOwner();
        manageBtn.setVisible(isOwner);
        manageBtn.setManaged(isOwner);
    }

    private void updateAddMemberPermission(Button button) {
        boolean allowed = currentUserCan("ADD_MEMBER");
        button.setDisable(!allowed);
        button.setOpacity(allowed ? 1.0 : 0.55);
    }

    private void showAccessDeniedPopup(String message) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Access Denied");
        dialog.setHeaderText("Permission Required");
        styleDialog(dialog, 400, 200);

        Label label = new Label(message);
        label.setFont(Font.font(FONT, 13));
        label.setStyle("-fx-text-fill: " + WHITE + ";");
        label.setWrapText(true);

        VBox box = new VBox(label);
        box.setPadding(new Insets(20));

        dialog.getDialogPane().setContent(box);
        addCloseButton(dialog);
        dialog.showAndWait();
    }

    private void refreshFileList() {
        if (fileListBox == null) return;

        fileListBox.getChildren().clear();

        String searchText = fileSearchField == null
                ? ""
                : fileSearchField.getText().trim().toLowerCase();

        int count = 0;

        for (CollaborationFileData file : filesList) {
            boolean matches = searchText.isEmpty()
                    || (file.fileName != null && file.fileName.toLowerCase().contains(searchText));

            if (matches) {
                fileListBox.getChildren().add(createFileRow(file));
                count++;
            }
        }

        if (count == 0) {
            Label empty = new Label(
                    searchText.isEmpty()
                            ? "No files uploaded yet."
                            : "No matching files found.");

            empty.setFont(Font.font(FONT, 12));
            empty.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
            empty.setPadding(new Insets(15));

            fileListBox.getChildren().add(empty);
        }
        
        fileListBox.requestLayout();
    }

    private void toggleDiscussionPanel(Button discussionBtn) {
        if (stackContainer == null && discussionBtn != null && discussionBtn.getScene() != null) {
            javafx.scene.Parent root = discussionBtn.getScene().getRoot();
            if (root instanceof BorderPane) {
                BorderPane bp = (BorderPane) root;
                if (bp.getCenter() instanceof StackPane) {
                    stackContainer = (StackPane) bp.getCenter();
                } else if (bp.getCenter() != null) {
                    javafx.scene.Node oldCenter = bp.getCenter();
                    stackContainer = new StackPane(oldCenter);
                    stackContainer.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
                    bp.setCenter(stackContainer);
                }
            }
        }

        if (stackContainer == null) return;

        if (isDiscussionOpen) {
            if (floatingChatPanel != null) {
                stackContainer.getChildren().remove(floatingChatPanel);
            }
            isDiscussionOpen = false;
        } else {
            if (floatingChatPanel == null) {
                String workspaceDocId = spaceName.replaceAll("\\s+", "_");
                VBox board = createCommentsSection(workspaceDocId, null);
                
                Button closeBtn = new Button("✕");
                closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + LIGHT_SECONDARY + "; -fx-cursor: hand; -fx-font-size: 14px;");
                
                Region r = new Region();
                HBox.setHgrow(r, Priority.ALWAYS);
                
                HBox topBar = new HBox(r, closeBtn);
                topBar.setAlignment(Pos.CENTER_RIGHT);
                topBar.setPadding(new Insets(4, 4, 0, 0));

                closeBtn.setOnAction(e -> toggleDiscussionPanel(discussionBtn));

                floatingChatPanel = new VBox(0, topBar, board);
                floatingChatPanel.setMaxWidth(360);
                floatingChatPanel.setPrefWidth(360);
                floatingChatPanel.setMaxHeight(Double.MAX_VALUE);
                floatingChatPanel.setStyle(
                    "-fx-background-color: #0B132B;" +
                    "-fx-border-color: " + CARD_BORDER + ";" +
                    "-fx-border-radius: 16;" +
                    "-fx-background-radius: 16;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 24, 0, 0, 10);"
                );
                StackPane.setAlignment(floatingChatPanel, Pos.TOP_RIGHT);
                StackPane.setMargin(floatingChatPanel, new Insets(70, 24, 24, 24));
                VBox.setVgrow(board, Priority.ALWAYS);
            }
            
            if (!stackContainer.getChildren().contains(floatingChatPanel)) {
                stackContainer.getChildren().add(floatingChatPanel);
            }
            isDiscussionOpen = true;
        }
        stackContainer.requestLayout();
    }

    private HBox createFileRow(CollaborationFileData file) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMinHeight(52);
        row.setPadding(new Insets(7, 10, 7, 10));
        row.setStyle(
                "-fx-border-color: transparent transparent rgba(255, 255, 255, 0.05) transparent;");

        Label icon = new Label(file.icon != null ? file.icon : "FILE");
        icon.setFont(Font.font(FONT, FontWeight.BOLD, 8));
        icon.setTextFill(Color.WHITE);
        icon.setAlignment(Pos.CENTER);
        icon.setPrefSize(30, 30);
        icon.setStyle(
                "-fx-background-color: rgba(56, 189, 248, 0.2);" +
                "-fx-background-radius: 6;");

        Label name = new Label(file.fileName != null ? file.fileName : "Unnamed File");
        name.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        name.setStyle("-fx-text-fill: " + WHITE + ";");

        HBox nameBox = new HBox(12, icon, name);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameBox.setPrefWidth(260);
        HBox.setHgrow(nameBox, Priority.ALWAYS);

        String displaySize = (file.size == null || file.size.equalsIgnoreCase("Cloud File") || file.size.equalsIgnoreCase("Local File") || file.size.isEmpty()) ? "1.2 MB" : file.size;
        Label size = new Label(displaySize);
        size.setFont(Font.font(FONT, 12));
        size.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
        size.setPrefWidth(110);

        String displayDate = (file.uploadedOn == null || file.uploadedOn.equalsIgnoreCase("Just now") || file.uploadedOn.isEmpty()) ? "26 Aug 2026" : file.uploadedOn;
        Label date = new Label(displayDate);
        date.setFont(Font.font(FONT, 12));
        date.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
        date.setPrefWidth(180);

        Button more = new Button("⋮");
        more.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        more.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + "; -fx-background-color: transparent; -fx-cursor: hand;");
        more.setPrefWidth(30);

        ContextMenu menu = new ContextMenu();
        menu.setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        MenuItem viewFile = new MenuItem("View File");
        viewFile.setStyle("-fx-text-fill: white; -fx-font-family: " + FONT + ";");
        viewFile.setOnAction(e -> {
            if (!currentUserCan("VIEW")) {
                showAccessDeniedPopup("You do not have permission to view files.");
                return;
            }
            showFilePreviewDialog(file);
        });
        menu.getItems().add(viewFile);

        MenuItem download = new MenuItem("Download File");
        download.setStyle("-fx-text-fill: white; -fx-font-family: " + FONT + ";");
        download.setOnAction(e -> {
            String activeRole = getLoggedInUserRole();
            if ("Viewer".equalsIgnoreCase(activeRole)) {
                showAccessDeniedPopup("Viewer can only view. You cannot download.");
                return;
            }
            if (file.secureUrl != null && !file.secureUrl.isEmpty()) {
                try {
                    String downloadUrl = file.secureUrl;
                    if (downloadUrl.contains("/upload/") && !downloadUrl.contains("fl_attachment")) {
                        downloadUrl = downloadUrl.replace("/upload/", "/upload/fl_attachment/");
                    }
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(downloadUrl));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        menu.getItems().add(download);

        MenuItem delete = new MenuItem("Delete File");
        delete.setStyle("-fx-text-fill: white; -fx-font-family: " + FONT + ";");
        delete.setOnAction(e -> {
            String activeRole = getLoggedInUserRole();
            boolean isModerator = "Moderator".equalsIgnoreCase(activeRole);
            boolean isOwnerUser = "Owner".equalsIgnoreCase(activeRole);

            if (isOwnerUser || isModerator) {
                filesList.remove(file);
                refreshFileList();
                updateFileCount();

                Thread deleteThread = new Thread(() -> {
                    try {
                        com.google.cloud.firestore.Firestore db = FirebaseConfig.getFirestore();
                        var docs = db.collection("workspaces")
                            .document(spaceName.replaceAll("\\s+", "_"))
                            .collection("files")
                            .whereEqualTo("fileName", file.fileName)
                            .get().get().getDocuments();

                        for (var doc : docs) {
                            Object publicId = doc.get("cloudinaryPublicId");
                            if (publicId != null) {
                                try {
                                    new CollaborationController()
                                        .deleteFile(publicId.toString(), r -> {}, ex -> ex.printStackTrace());
                                } catch (Exception ignored) {}
                            }
                            doc.getReference().delete();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }, "delete-file-" + file.fileName);
                deleteThread.setDaemon(true);
                deleteThread.start();
            } else {
                showAccessDeniedPopup("You do not have permission to delete files.");
            }
        });
        menu.getItems().add(delete);

        more.setOnAction(e -> menu.show(more, javafx.geometry.Side.BOTTOM, 0, 0));

        row.getChildren().addAll(nameBox, size, date, more);
        return row;
    }

    private void showFilePreviewDialog(CollaborationFileData file) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Viewing File: " + (file.fileName != null ? file.fileName : "Document"));
        dialog.setHeaderText("In-App Media & Document Viewer: " + (file.fileName != null ? file.fileName : ""));
        styleDialog(dialog, 1000, 700);

        WebView webView = new WebView();
        webView.setPrefSize(980, 620);

        if (file.secureUrl != null && !file.secureUrl.isEmpty()) {
            String targetUrl = file.secureUrl;
            String lowerName = file.fileName != null ? file.fileName.toLowerCase() : "";

            if (lowerName.endsWith(".docx") || lowerName.endsWith(".doc") || lowerName.endsWith(".xlsx")) {
                targetUrl = "https://view.officeapps.live.com/op/embed.aspx?src=" + URLEncoder.encode(file.secureUrl, StandardCharsets.UTF_8);
            } else if (lowerName.endsWith(".pdf")) {
                targetUrl = file.secureUrl;
            } else if (lowerName.endsWith(".png") || lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") || lowerName.endsWith(".gif") || lowerName.endsWith(".webp")) {
                String htmlContent = "<html><body style='background:#060B14; display:flex; justify-content:center; align-items:center; height:100vh; margin:0;'>"
                        + "<img src='" + file.secureUrl + "' style='max-width:100%; max-height:100%; object-fit:contain; box-shadow: 0 4px 12px rgba(0,0,0,0.5); border-radius:8px;'/>"
                        + "</body></html>";
                webView.getEngine().loadContent(htmlContent);
                targetUrl = null;
            }

            if (targetUrl != null) {
                webView.getEngine().load(targetUrl);
            }
        } else {
            webView.getEngine().loadContent("<h3 style='font-family:sans-serif; color:white; text-align:center; margin-top:50px;'>File preview unavailable.</h3>");
        }

        VBox container = new VBox(10, webView);
        container.setPadding(new Insets(15));
        container.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(webView, Priority.ALWAYS);

        dialog.getDialogPane().setContent(container);
        addCloseButton(dialog);
        dialog.showAndWait();
    }

    private void showInfoPopup(
            String title,
            String heading,
            String message) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(heading);
        styleDialog(dialog, 400, 220);

        Label label = new Label(message);
        label.setFont(Font.font(FONT, 13));
        label.setStyle("-fx-text-fill: " + WHITE + ";");
        label.setWrapText(true);

        VBox box = new VBox(label);
        box.setPadding(new Insets(20));

        dialog.getDialogPane().setContent(box);
        addCloseButton(dialog);
        dialog.showAndWait();
    }

    private void refreshMemberList() {
        if (memberListBox == null) return;

        memberListBox.getChildren().clear();

        String searchText = memberSearchField == null
                ? ""
                : memberSearchField.getText().trim().toLowerCase();

        int count = 0;

        for (CollaborationMemberData member : membersList) {
            String status = member.status != null ? member.status : "active";
            boolean isActiveOrOwner = "active".equalsIgnoreCase(status) || "Owner".equalsIgnoreCase(member.role);
            
            if (!isActiveOrOwner) {
                continue;
            }

            boolean matches = searchText.isEmpty()
                    || (member.name != null && member.name.toLowerCase().contains(searchText))
                    || (member.email != null && member.email.toLowerCase().contains(searchText));

            if (matches) {
                memberListBox.getChildren().add(createMemberRow(member));
                count++;
            }
        }

        if (count == 0) {
            Label empty = new Label(
                    searchText.isEmpty()
                            ? "No active members yet."
                            : "No matching members found.");

            empty.setFont(Font.font(FONT, 11));
            empty.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
            empty.setPadding(new Insets(12, 0, 12, 0));

            memberListBox.getChildren().add(empty);
        }

        memberListBox.requestLayout();
    }

    private HBox createMemberRow(CollaborationMemberData member) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 4, 8, 4));
        row.setStyle(
                "-fx-border-color: transparent transparent rgba(255, 255, 255, 0.05) transparent;");

        Label avatar = new Label(member.initials != null ? member.initials : "M");
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        avatar.setTextFill(Color.web("#60A5FA"));
        avatar.setAlignment(Pos.CENTER);
        avatar.setPrefSize(34, 34);
        avatar.setStyle(
                "-fx-background-color: rgba(37, 99, 235, 0.2);" +
                "-fx-background-radius: 50%;");

        Label name = new Label(member.name != null ? member.name : "Member");
        name.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        name.setStyle("-fx-text-fill: " + WHITE + ";");

        Label email = new Label(member.email != null ? member.email : "");
        email.setFont(Font.font(FONT, 10));
        email.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox info = new VBox(2, name, email);
        HBox.setHgrow(info, Priority.ALWAYS);

        String roleBg = "rgba(255, 255, 255, 0.08)";
        String roleText = LIGHT_SECONDARY;

        if ("Owner".equalsIgnoreCase(member.role)) {
            roleBg = "rgba(37, 99, 235, 0.2)";
            roleText = "#60A5FA";
        } else if ("Moderator".equalsIgnoreCase(member.role)) {
            roleBg = "rgba(245, 158, 11, 0.2)";
            roleText = "#FBBF24";
        } else if ("Editor".equalsIgnoreCase(member.role)) {
            roleBg = "rgba(56, 189, 248, 0.2)";
            roleText = "#38BDF8";
        } else if ("Viewer".equalsIgnoreCase(member.role)) {
            roleBg = "rgba(16, 185, 129, 0.2)";
            roleText = "#34D399";
        }

        Label role = new Label(member.role != null ? member.role : "Viewer");
        role.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        role.setStyle(
                "-fx-text-fill: " + roleText + ";" +
                "-fx-background-color: " + roleBg + ";" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 3 8;");

        Button more = new Button("⋮");
        more.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        more.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + "; -fx-background-color: transparent; -fx-cursor: hand;");
        more.setPrefWidth(25);

        boolean isOwner = isCurrentLoggedInUserOwner();
        more.setVisible(isOwner);
        more.setManaged(isOwner);

        ContextMenu menu = new ContextMenu();
        menu.setStyle("-fx-background-color: #0A121E; -fx-border-color: " + CARD_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        if (isOwner) {
            MenuItem remove = new MenuItem("Remove Member");

            remove.setOnAction(e -> {
                if ("Owner".equalsIgnoreCase(member.role)) {
                    showAccessDeniedPopup("The Owner cannot be removed.");
                    return;
                }

                try {
                    com.google.api.core.ApiFuture<com.google.cloud.firestore.QuerySnapshot> future = 
                        FirebaseConfig.getFirestore()
                            .collection("workspaces")
                            .document(spaceName.replaceAll("\\s+", "_"))
                            .collection("members")
                            .get();

                    com.google.api.core.ApiFutures.addCallback(future, new com.google.api.core.ApiFutureCallback<com.google.cloud.firestore.QuerySnapshot>() {
                        @Override
                        public void onSuccess(com.google.cloud.firestore.QuerySnapshot result) {
                            for (com.google.cloud.firestore.DocumentSnapshot doc : result.getDocuments()) {
                                CollaborationMemberData cloudMember = doc.toObject(CollaborationMemberData.class);
                                if (cloudMember != null && cloudMember.email != null && cloudMember.email.equalsIgnoreCase(member.email)) {
                                    doc.getReference().delete();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            t.printStackTrace();
                        }
                    }, command -> command.run());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                membersList.remove(member);
                refreshMemberList();
                updateMemberCount();
            });

            menu.getItems().add(remove);
            more.setOnAction(e -> menu.show(more, javafx.geometry.Side.BOTTOM, 0, 0));
        }

        row.getChildren().addAll(
                avatar,
                info,
                role,
                more);

        return row;
    }

    private Button createViewAllButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(34);
        button.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-cursor: hand; -fx-padding: 0;");
        return button;
    }

    private void showAllFilesPopup() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("All Files");
        dialog.setHeaderText(spaceName + " - All Uploaded Files");
        styleDialog(dialog, 700, 500);

        VBox box = new VBox(0);
        box.setPrefWidth(660);

        for (CollaborationFileData file : filesList) {
            box.getChildren().add(createFileRow(file));
        }

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(400);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        dialog.getDialogPane().setContent(scroll);
        addCloseButton(dialog);
        dialog.showAndWait();
    }

    private void showAllMembersPopup() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("All Members");
        dialog.setHeaderText(spaceName + " - All Members");
        styleDialog(dialog, 540, 500);

        VBox box = new VBox(0);
        box.setPrefWidth(500);

        for (CollaborationMemberData member : membersList) {
            box.getChildren().add(createMemberRow(member));
        }

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(400);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        dialog.getDialogPane().setContent(scroll);
        addCloseButton(dialog);
        dialog.showAndWait();
    }

    private void updateFileCount() {
        if (fileCountLabel != null) {
            fileCountLabel.setText(
                    filesList.size() + " Files");
        }
    }

    private void updateMemberCount() {
        if (memberCountLabel != null) {
            long activeCount = membersList.stream()
                .filter(m -> "active".equalsIgnoreCase(m.status) || "Owner".equalsIgnoreCase(m.role))
                .count();
            memberCountLabel.setText(activeCount + " Members");
        }
    }

    private void styleTableHeader(Label label) {
        label.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        label.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");
    }

    private void showManageAccessPopup() {
        if (!isCurrentLoggedInUserOwner()) {
            showAccessDeniedPopup("Access Denied: Only the workspace Owner can manage member access.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Manage Access & Invites");
        dialog.setHeaderText(spaceName + " - Member Access & Invites");
        styleDialog(dialog, 650, 520);

        Label description = new Label("View active roles, pending invitations, and declined requests.");
        description.setFont(Font.font(FONT, 12));
        description.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        Label currentRole = new Label("Current User Role: " + getLoggedInUserRole());
        currentRole.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        currentRole.setTextFill(Color.web("#60A5FA"));

        VBox memberRows = new VBox(8);
        memberRows.setPadding(new Insets(5));

        for (CollaborationMemberData member : membersList) {
            memberRows.getChildren().add(createManageAccessRow(member));
        }

        ScrollPane scroll = new ScrollPane(memberRows);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(360);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        scroll.lookupAll(".scroll-bar").forEach(node -> {
            node.setStyle("-fx-pref-width: 0; -fx-opacity: 0; -fx-background-color: transparent;");
        });

        VBox root = new VBox(
                12,
                description,
                currentRole,
                scroll);

        root.setPadding(new Insets(15));

        dialog.getDialogPane().setContent(root);
        addCloseButton(dialog);
        dialog.showAndWait();
    }

    private HBox createManageAccessRow(CollaborationMemberData member) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 8, 10, 8));
        row.setStyle(
                "-fx-border-color: transparent transparent rgba(255, 255, 255, 0.05) transparent;");

        Label avatar = new Label(member.initials != null ? member.initials : "M");
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        avatar.setTextFill(Color.web("#60A5FA"));
        avatar.setAlignment(Pos.CENTER);
        avatar.setPrefSize(38, 38);
        avatar.setStyle(
                "-fx-background-color: rgba(37, 99, 235, 0.2);" +
                "-fx-background-radius: 50%;");

        Label name = new Label(member.name != null ? member.name : "Member");
        name.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        name.setStyle("-fx-text-fill: " + WHITE + ";");

        Label email = new Label(member.email != null ? member.email : "");
        email.setFont(Font.font(FONT, 10));
        email.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox info = new VBox(2, name, email);
        HBox.setHgrow(info, Priority.ALWAYS);

        String status = member.status != null ? member.status : "active";

        if (member.role != null && member.role.equalsIgnoreCase("Owner")) {
            Label owner = new Label("Owner");
            owner.setFont(Font.font(FONT, FontWeight.BOLD, 11));
            owner.setTextFill(Color.web("#60A5FA"));
            owner.setPadding(new Insets(6, 12, 6, 12));
            owner.setStyle(
                    "-fx-background-color: rgba(37, 99, 235, 0.2);" +
                    "-fx-background-radius: 6;");

            row.getChildren().addAll(avatar, info, owner);
            return row;
        }

        if ("pending".equalsIgnoreCase(status)) {
            Label pendingBadge = new Label("⏳ Pending Acceptance");
            pendingBadge.setFont(Font.font(FONT, FontWeight.BOLD, 11));
            pendingBadge.setTextFill(Color.web("#FBBF24"));
            pendingBadge.setPadding(new Insets(6, 12, 6, 12));
            pendingBadge.setStyle("-fx-background-color: rgba(245, 158, 11, 0.2); -fx-background-radius: 6;"); 
            
            row.getChildren().addAll(avatar, info, pendingBadge);
            return row;
        } 
        else if ("declined".equalsIgnoreCase(status)) {
            Label declinedBadge = new Label("✕ Declined Request");
            declinedBadge.setFont(Font.font(FONT, FontWeight.BOLD, 11));
            declinedBadge.setTextFill(Color.web("#F87171"));
            declinedBadge.setPadding(new Insets(6, 12, 6, 12));
            declinedBadge.setStyle("-fx-background-color: rgba(239, 68, 68, 0.2); -fx-background-radius: 6;"); 

            row.getChildren().addAll(avatar, info, declinedBadge);
            return row;
        }

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Editor", "Moderator", "Viewer");
        roleCombo.setValue(member.role != null ? member.role : "Viewer");
        roleCombo.setPrefWidth(110);
        roleCombo.setPrefHeight(34);
        roleCombo.setStyle(
                "-fx-background-color:" + INPUT_BG + ";" +
                "-fx-text-fill: white;" +
                "-fx-border-color:" + INPUT_BORDER + ";" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;");

        roleCombo.setOnAction(e ->
                updateMemberRole(
                        member,
                        roleCombo.getValue()));

        roleCombo.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: #0A121E;");
                } else {
                    setText(item);
                    setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: #0A121E;");
                }
            }
        });
        roleCombo.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
                    setTextFill(Color.WHITE);
                }
            }
        });

        row.getChildren().addAll(
                avatar,
                info,
                roleCombo);

        return row;
    }

    private void updateMemberRole(CollaborationMemberData member, String newRole) {
        if (!isCurrentLoggedInUserOwner()) {
            showAccessDeniedPopup("Only the Owner is allowed to change member roles.");
            refreshMemberList();
            return;
        }

        member.role = newRole;
        updateMemberAppearance(member);

        try {
            com.google.api.core.ApiFuture<com.google.cloud.firestore.QuerySnapshot> future = 
                FirebaseConfig.getFirestore()
                    .collection("workspaces")
                    .document(spaceName.replaceAll("\\s+", "_"))
                    .collection("members")
                    .get();

            com.google.api.core.ApiFutures.addCallback(future, new com.google.api.core.ApiFutureCallback<com.google.cloud.firestore.QuerySnapshot>() {
                @Override
                public void onSuccess(com.google.cloud.firestore.QuerySnapshot result) {
                    for (com.google.cloud.firestore.DocumentSnapshot doc : result.getDocuments()) {
                        CollaborationMemberData cloudMember = doc.toObject(CollaborationMemberData.class);
                        if (cloudMember != null && cloudMember.email != null && cloudMember.email.equalsIgnoreCase(member.email)) {
                            doc.getReference().update("role", newRole, "avatarBackground", member.avatarBackground, "avatarColor", member.avatarColor);
                            break;
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            }, command -> command.run());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        refreshMemberList();
        updateMemberCount();
    }

    private void updateMemberAppearance(CollaborationMemberData member) {
        if ("Editor".equalsIgnoreCase(member.role)) {
            member.avatarBackground = "rgba(56, 189, 248, 0.2)";
            member.avatarColor = "#38BDF8";
        } else if ("Moderator".equalsIgnoreCase(member.role)) {
            member.avatarBackground = "rgba(245, 158, 11, 0.2)";
            member.avatarColor = "#FBBF24";
        } else if ("Viewer".equalsIgnoreCase(member.role)) {
            member.avatarBackground = "rgba(16, 185, 129, 0.2)";
            member.avatarColor = "#34D399";
        }
    }

    private void showAddMemberPopup() {
        if (!currentUserCan("ADD_MEMBER")) {
            showAccessDeniedPopup("Only the Owner can add members.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Member");
        dialog.setHeaderText("Send invitation to a new member");
        styleDialog(dialog, 460, 400);

        Label nameLabel = new Label("Name");
        styleFormLabel(nameLabel);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter member name");
        nameField.setPrefHeight(38);
        nameField.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: white; -fx-prompt-text-fill: " + LIGHT_SECONDARY + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label emailLabel = new Label("Email");
        styleFormLabel(emailLabel);

        TextField emailField = new TextField();
        emailField.setPromptText("Enter member email");
        emailField.setPrefHeight(38);
        emailField.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: white; -fx-prompt-text-fill: " + LIGHT_SECONDARY + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label roleLabel = new Label("Role");
        styleFormLabel(roleLabel);

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Viewer", "Moderator", "Editor");
        roleCombo.setValue("Viewer");
        roleCombo.setMaxWidth(Double.MAX_VALUE);
        roleCombo.setPrefHeight(38);
        roleCombo.setStyle(
                "-fx-background-color:" + INPUT_BG + ";" +
                "-fx-text-fill: white;" +
                "-fx-border-color:" + INPUT_BORDER + ";" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;");

        roleCombo.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: #0A121E;");
                } else {
                    setText(item);
                    setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: #0A121E;");
                }
            }
        });

        roleCombo.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
                    setTextFill(Color.WHITE);
                }
            }
        });

        VBox box = new VBox(
                10,
                nameLabel,
                nameField,
                emailLabel,
                emailField,
                roleLabel,
                roleCombo);

        box.setPadding(new Insets(20));

        ButtonType sendButton = new ButtonType(
                "Send Invite",
                ButtonData.OK_DONE);

        ButtonType cancelButton = new ButtonType(
                "Cancel",
                ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(sendButton, cancelButton);

        dialog.getDialogPane().setContent(box);

        Button sendNode = (Button) dialog.getDialogPane().lookupButton(sendButton);
        sendNode.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7; -fx-cursor: hand;");

        Button cancelNode = (Button) dialog.getDialogPane().lookupButton(cancelButton);
        cancelNode.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand;");

        dialog.showAndWait().ifPresent(result -> {
            if (result != sendButton) return;

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleCombo.getValue();

            if (name.isEmpty() || email.isEmpty() || role == null) {
                return;
            }

            String initials = getInitials(name);
            String background;
            String avatarColor;

            if (role.equalsIgnoreCase("Editor")) {
                background = "rgba(56, 189, 248, 0.2)";
                avatarColor = "#38BDF8";
            } else if (role.equalsIgnoreCase("Moderator")) {
                background = "rgba(245, 158, 11, 0.2)";
                avatarColor = "#FBBF24";
            } else {
                background = "rgba(16, 185, 129, 0.2)";
                avatarColor = "#34D399";
            }

            CollaborationMemberData newMember = new CollaborationMemberData(
                    initials,
                    name,
                    email,
                    role,
                    background,
                    avatarColor,
                    "pending"
            );

            try {
                String memberId = email.toLowerCase().replaceAll("[^a-z0-9]", "_");
                FirebaseConfig.getFirestore()
                    .collection("workspaces")
                    .document(spaceName.replaceAll("\\s+", "_"))
                    .collection("members")
                    .document(memberId)
                    .set(newMember);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            membersList.add(newMember);
            refreshMemberList();
            updateMemberCount();

            showInfoPopup(
                    "Invitation Sent",
                    "Member Invitation",
                    "Invitation sent to " + email +
                    "\n\nRole: " + role);
        });
    }

    private void styleFormLabel(Label label) {
        label.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        label.setStyle("-fx-text-fill: " + WHITE + ";");
    }

    private String getInitials(String name) {
        String[] parts = name.trim().split("\\s+");

        if (parts.length == 1) {
            return parts[0]
                    .substring(0, Math.min(2, parts[0].length()))
                    .toUpperCase();
        }

        return (
                parts[0].substring(0, 1) +
                parts[parts.length - 1].substring(0, 1)
        ).toUpperCase();
    }

    public Scene getSharedSpacePageScene() {
        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        rootLayout.setLeft(createSidebar());

        VBox mainContent = getSharedSpaceContent();
        
        mainScrollPane = new ScrollPane(mainContent);
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setFitToHeight(true);
        mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mainScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        stackContainer = new StackPane(mainScrollPane);
        stackContainer.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");

        rootLayout.setCenter(stackContainer);

        return new Scene(rootLayout, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private VBox createSidebar() {
        Image logoImage = new Image(getClass().getResourceAsStream("/assets/logo/OneSpace_logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(42);
        logoView.setFitHeight(42);
        logoView.setPreserveRatio(true);

        StackPane logoIcon = new StackPane(logoView);
        logoIcon.setPrefSize(42, 42);
        logoIcon.setAlignment(Pos.CENTER);

        Label logoText = new Label("OneSpace");
        logoText.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        logoText.setStyle("-fx-text-fill:" + WHITE + ";");

        HBox logoHeader = new HBox(10, logoIcon, logoText);
        logoHeader.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(4, logoHeader);
        logoBox.setPadding(new Insets(0, 0, 18, 6));

        Button dashboardBtn = createSidebarButton("dashboard", "Dashboard", false, e -> LandingPage.showUserDashboard());
        Button spacesBtn = createSidebarButton("files", "Spaces", false, e -> LandingPage.showUserSpace());
        Button searchBtn = createSidebarButton("search", "Search", false, e -> LandingPage.showUserSearch());
        Button calendarBtn = createSidebarButton("calendar", "Calendar", false, e -> LandingPage.showCalendarPage());
        Button aiBtn = createSidebarButton("ai", "AI Assistant", false, e -> LandingPage.showAiAssistantPage());
        Button collabBtn = createSidebarButton("collaboration", "Collaboration", true, e -> LandingPage.showCollaborationPage());
        Button recentBtn = createSidebarButton("recent", "Recent", false, e -> LandingPage.showRecentPage());
        Button trashBtn = createSidebarButton("trash", "Trash", false, e -> LandingPage.showTrashPage());
        Button settingsBtn = createSidebarButton("settings", "Settings", false, e -> LandingPage.showSettingPage());

        VBox navList = new VBox(4, dashboardBtn, spacesBtn, searchBtn, calendarBtn, aiBtn, collabBtn, recentBtn, trashBtn);

        Label storageTitle = new Label("Storage Used");
        storageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageTitle.setStyle("-fx-text-fill:" + WHITE + ";");

        Label storageVal = new Label("64.2 GB of 100 GB");
        storageVal.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        storageVal.setStyle("-fx-text-fill:" + WHITE + ";");

        Label storagePercent = new Label("64%");
        storagePercent.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        storagePercent.setStyle("-fx-text-fill:" + LIGHT_SECONDARY + ";");

        Region storageSpacer = new Region();
        HBox.setHgrow(storageSpacer, Priority.ALWAYS);

        HBox storageValueGroup = new HBox(storageVal, storageSpacer, storagePercent);
        storageValueGroup.setAlignment(Pos.CENTER_LEFT);

        ProgressBar storageProgress = new ProgressBar(.64);
        storageProgress.setMaxWidth(Double.MAX_VALUE);
        storageProgress.setPrefHeight(6);
        storageProgress.setStyle("-fx-accent: " + BLUE + "; -fx-control-inner-background: rgba(13, 22, 38, 0.85);");

        Button manageStorageBtn = new Button("Manage Storage ›");
        manageStorageBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        manageStorageBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-padding: 2 0 0 0; -fx-cursor: hand;");
        manageStorageBtn.setOnAction(e -> LandingPage.showLandingPage());

        VBox storageCard = new VBox(8, storageTitle, storageValueGroup, storageProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: rgba(16, 28, 48, 0.65); -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox sidebar = new VBox(12, logoBox, navList, sidebarSpacer, settingsBtn, storageCard);
        sidebar.setPadding(new Insets(20, 14, 20, 14));
        sidebar.setPrefWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setMinWidth(ResponsiveUtil.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 1 0 0;");

        return sidebar;
    }

    private Button createSidebarButton(String iconType, String text, boolean active, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(active ? WHITE : LIGHT_SECONDARY));
        icon.setStrokeWidth(2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(24, 24);

        Label label = new Label(text);
        label.setFont(Font.font(FONT, active ? FontWeight.BOLD : FontWeight.MEDIUM, 13));
        label.setTextFill(Color.web(WHITE));

        HBox content = new HBox(12, iconBox, label);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("", content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(38);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 12));
        button.setOnAction(action);

        if (active) {
            button.setStyle(
                "-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB);" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: rgba(96, 165, 250, 0.6);" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.55), 14, 0, 0, 2);"
            );
        } else {
            button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.WHITE);
                label.setTextFill(Color.WHITE);
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                label.setTextFill(Color.web(WHITE));
            });
        }

        return button;
    }

    private void styleDialog(Dialog<?> dialog, double width, double height) {
        dialog.getDialogPane().setPrefWidth(width);
        dialog.getDialogPane().setPrefHeight(height);
        dialog.getDialogPane().setStyle(
                "-fx-background-color: #0A121E;" +
                "-fx-border-color: " + CARD_BORDER +
                ";-fx-border-radius:12;-fx-background-radius:12;");
    }

    private String cardContainerStyle() {
        return "-fx-background-color: " + CARD_BG +
                ";-fx-border-color: " + CARD_BORDER +
                ";-fx-border-width: 1.2;-fx-border-radius:20;-fx-background-radius:20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 24, 0, 0, 10);";
    }

    private SVGPath createIcon(String type) {
        SVGPath icon = new SVGPath();
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2);
        switch (type) {
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
            case "users": icon.setContent("M8 11 A3 3 0 1 0 8 5 A3 3 0 0 0 8 11 Z M16 11 A3 3 0 1 0 16 5 A3 3 0 0 0 16 11 Z M2 20 C2 16 5 14 8 14 C11 14 14 16 14 20 M12 15 C14 14 17 14 19 15 C21 16 22 18 22 20"); break;
            case "security": icon.setContent("M12 2 L20 5 V11 C20 16 17 20 12 22 C7 20 4 16 4 11 V5 Z M9 12 L11 14 L15 9"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }
}