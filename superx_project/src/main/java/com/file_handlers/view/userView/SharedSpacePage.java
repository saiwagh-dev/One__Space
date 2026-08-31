package com.file_handlers.view.userView;


import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.file_handlers.config.FirebaseConfig;
import com.file_handlers.controller.CollaborationController;
import com.file_handlers.model.CollaborationFileData;
import com.file_handlers.model.CollaborationMemberData;
import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;

public class SharedSpacePage {

    private static final String FONT = "Inter";
    private static final String BG_APP = "#3A4D67";
    private static final String BG_CARD = "#DDE8F5";
    private static final String BG_CARD_INNER = "#D1E1F1";
    private static final String BG_INPUT = "#EDF3FA";
    private static final String BG_SIDEBAR_CARD = "#2E3F55";
    private static final String BORDER_COLOR = "#C9DAEE";
    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String PRIMARY_LIGHT_BLUE = "#93C5FD";
    private static final String TEXT_DARK = "#000000"; 
    private static final String TEXT_MUTED_DARK = "#1E293B"; 
    private static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#9EB0C6";
    private static final String SUCCESS = "#15803D"; 
    private static final String SUCCESS_LIGHT = "#86EFAC"; 
    private static final String ORANGE = "#C2410C";
    private static final String ORANGE_LIGHT = "#FFEDD5";
    private static final String RED = "#DC2626";
    public static final String RED_LIGHT = "#FEE2E2";

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

    private String currentUserRole = "Owner";
    private String createdDate = "26 Aug 2026";
    private String workspaceOwnerName = "Aarav Verma";
    private String workspaceOwnerEmail = "aarav.verma@email.com";

    public SharedSpacePage() {
        this("Shared Space");
    }

    public SharedSpacePage(String spaceName) {
        this.spaceName =
                spaceName == null || spaceName.trim().isEmpty()
                        ? "Shared Space"
                        : spaceName.trim();
        
        loadDefaultData();
        
        javafx.application.Platform.runLater(() -> {
            refreshMemberList();
            updateMemberCount();
        });
        
        checkUserAccessAndLoadContent();
    }

    private String getLoggedInUserRole() {
        String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";
        if (myEmail == null || myEmail.isEmpty()) {
            return currentUserRole != null ? currentUserRole : "Viewer";
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
        String role = getLoggedInUserRole();
        return "Owner".equalsIgnoreCase(role);
    }

    private void listenForRealtimeFiles() {
        try {
            com.google.cloud.firestore.Firestore db = FirebaseConfig.getFirestore();
            db.collection("workspaces")
                .document(spaceName.replaceAll("\\s+", "_"))
                .collection("files")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }

                    if (snapshots != null) {
                        for (com.google.cloud.firestore.DocumentChange docChange : snapshots.getDocumentChanges()) {
                            CollaborationFileData cloudFile = docChange.getDocument().toObject(CollaborationFileData.class);

                            javafx.application.Platform.runLater(() -> {
                                if (cloudFile != null && cloudFile.fileName != null) {
                                    if (cloudFile.size == null || cloudFile.size.equalsIgnoreCase("Cloud File") || cloudFile.size.equalsIgnoreCase("Local File") || cloudFile.size.isEmpty()) {
                                        cloudFile.size = "1.2 MB";
                                    }
                                    if (cloudFile.uploadedOn == null || cloudFile.uploadedOn.equalsIgnoreCase("Just now") || cloudFile.uploadedOn.isEmpty()) {
                                        cloudFile.uploadedOn = "26 Aug 2026";
                                    }
                                }

                                if (docChange.getType() == com.google.cloud.firestore.DocumentChange.Type.ADDED ||
                                    docChange.getType() == com.google.cloud.firestore.DocumentChange.Type.MODIFIED) {
                                    
                                    if (cloudFile != null && cloudFile.fileName != null) {
                                        boolean exists = filesList.stream().anyMatch(f -> 
                                            (f.secureUrl != null && f.secureUrl.equals(cloudFile.secureUrl)) ||
                                            (f.fileName != null && f.fileName.equalsIgnoreCase(cloudFile.fileName))
                                        );

                                        if (!exists) {
                                            filesList.add(cloudFile);
                                        } else {
                                            filesList.removeIf(f -> f.fileName != null && f.fileName.equalsIgnoreCase(cloudFile.fileName));
                                            filesList.add(cloudFile);
                                        }

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

    private void listenForRealtimeMembers() {
        try {
            FirebaseConfig.getFirestore()
                .collection("workspaces")
                .document(spaceName.replaceAll("\\s+", "_"))
                .collection("members")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        error.printStackTrace();
                        return;
                    }
                    if (value != null) {
                        for (com.google.cloud.firestore.DocumentChange docChange : value.getDocumentChanges()) {
                            CollaborationMemberData cloudMember = docChange.getDocument().toObject(CollaborationMemberData.class);
                            
                            javafx.application.Platform.runLater(() -> {
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
                                            m.avatarBackground = cloudMember.avatarBackground;
                                            m.avatarColor = cloudMember.avatarColor;
                                            break;
                                        }
                                    }
                                    refreshMemberList();
                                } else if (docChange.getType() == com.google.cloud.firestore.DocumentChange.Type.REMOVED) {
                                    membersList.removeIf(m -> m.email != null && m.email.equalsIgnoreCase(cloudMember.email));
                                    refreshMemberList();
                                    updateMemberCount();
                                }

                                if (manageAccessButton != null) {
                                    updateManageAccessPermission(manageAccessButton);
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

        String currentUserName = (UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null) 
                ? UserSession.getInstance().getDisplayName() : "Workspace Owner";
        String currentUserEmail = (UserSession.getInstance() != null && UserSession.getInstance().getEmail() != null) 
                ? UserSession.getInstance().getEmail() : "owner@email.com";
        
        workspaceOwnerName = currentUserName;
        workspaceOwnerEmail = currentUserEmail;

        String initials = getInitials(workspaceOwnerName);

        membersList.add(new CollaborationMemberData(
                "AV", workspaceOwnerName, workspaceOwnerEmail,
                "Owner", PRIMARY_LIGHT_BLUE, PRIMARY_BLUE, "active"));
    }

    private boolean hasPermission(String role, String permission) {
        if (role == null || permission == null) return false;

        switch (role) {
            case "Owner":
                return true;
            case "Editor":
                return permission.equals("VIEW")
                        || permission.equals("SEARCH")
                        || permission.equals("DOWNLOAD")
                        || permission.equals("UPLOAD")
                        || permission.equals("EDIT_FILE");
            case "Moderator":
                return permission.equals("VIEW")
                        || permission.equals("SEARCH")
                        || permission.equals("DOWNLOAD")
                        || permission.equals("UPLOAD")
                        || permission.equals("EDIT_FILE")
                        || permission.equals("DELETE_FILE");
            case "Viewer":
                return permission.equals("VIEW")
                        || permission.equals("SEARCH");
            default:
                return false;
        }
    }

    private boolean currentUserCan(String permission) {
        return hasPermission(getLoggedInUserRole(), permission);
    }

    public VBox getSharedSpaceContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(28));
        content.setStyle("-fx-background-color:" + BG_APP + ";");

        Button back = createBackButton();

        Label title = new Label(spaceName);
        title.setFont(Font.font(FONT, FontWeight.BOLD, 27));
        title.setTextFill(Color.web(TEXT_LIGHT));

        Label subtitle = new Label("Shared workspace");
        subtitle.setFont(Font.font(FONT, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        VBox titleBox = new VBox(3, title, subtitle);

        HBox header = new HBox(12, back, titleBox);
        header.setAlignment(Pos.CENTER_LEFT);

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

        content.getChildren().addAll(header, summary, center);
        VBox.setVgrow(center, Priority.ALWAYS);

        return content;
    }

    private Button createBackButton() {
        Button button = new Button("←");
        button.setFont(Font.font(FONT, FontWeight.BOLD, 28));
        button.setTextFill(Color.web(TEXT_LIGHT));
        button.setPrefSize(42, 42);
        button.setMinSize(42, 42);
        button.setPadding(Insets.EMPTY);
        button.setStyle(
                "-fx-background-color:rgba(255,255,255,0.08);" +
                "-fx-border-color:transparent;" +
                "-fx-background-radius:10;" +
                "-fx-cursor:hand;");

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-border-color:transparent;" +
                "-fx-background-radius:10;" +
                "-fx-cursor:hand;"));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color:rgba(255,255,255,0.08);" +
                "-fx-border-color:transparent;" +
                "-fx-background-radius:10;" +
                "-fx-cursor:hand;"));

        button.setOnAction(e -> LandingPage.showCollaborationPage());

        return button;
    }

    private HBox createSummaryCard() {
        HBox card = new HBox(14);
        card.setPrefHeight(150);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;");

        CollaborationMemberData currentOwner = membersList.stream()
                .filter(m -> m.role != null && m.role.equalsIgnoreCase("Owner"))
                .findFirst()
                .orElse(new CollaborationMemberData("AV", workspaceOwnerName, workspaceOwnerEmail, "Owner", PRIMARY_LIGHT_BLUE, PRIMARY_BLUE, "active"));

        VBox owner = createSummaryItem(
                "♙", "Owner", workspaceOwnerName,
                PRIMARY_LIGHT_BLUE, PRIMARY_BLUE);
        ownerNameLabel = (Label) owner.getProperties().get("valueLabel");

        owner.setCursor(Cursor.HAND);
        owner.setOnMouseClicked(e -> showOwnerDetailsPopup(currentOwner));

        VBox members = createSummaryItem(
                "♧", "Members",
                membersList.size() + " Members",
                PRIMARY_LIGHT_BLUE, PRIMARY_BLUE);

        memberCountLabel = (Label) members.getProperties().get("valueLabel");

        VBox files = createSummaryItem(
                "▱", "Files",
                filesList.size() + " Files",
                SUCCESS_LIGHT, SUCCESS);

        fileCountLabel = (Label) files.getProperties().get("valueLabel");

        VBox created = createSummaryItem(
                "▣", "Created On", createdDate,
                ORANGE_LIGHT, ORANGE);
        createdDateLabel = (Label) created.getProperties().get("valueLabel");

        card.getChildren().addAll(owner, members, files, created);
        return card;
    }

    private VBox createSummaryItem(
            String icon,
            String heading,
            String value,
            String iconBackground,
            String iconColor) {

        VBox box = new VBox(7);
        box.setAlignment(Pos.CENTER);
        box.setPrefHeight(115);
        box.setMinHeight(110);
        box.setMaxHeight(125);
        box.setStyle(
                "-fx-background-color:" + BG_CARD_INNER + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;");

        HBox.setHgrow(box, Priority.ALWAYS);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(FONT, FontWeight.BOLD, 21));
        iconLabel.setTextFill(Color.web(iconColor));
        iconLabel.setAlignment(Pos.CENTER);
        iconLabel.setPrefSize(46, 42);
        iconLabel.setStyle(
                "-fx-background-color:" + iconBackground + ";" +
                "-fx-background-radius:50%;");

        Label headingLabel = new Label(heading);
        headingLabel.setFont(Font.font(FONT, 12));
        headingLabel.setTextFill(Color.web(TEXT_MUTED_DARK));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        valueLabel.setTextFill(Color.web(TEXT_DARK));

        box.getProperties().put("valueLabel", valueLabel);
        box.getChildren().addAll(iconLabel, headingLabel, valueLabel);

        return box;
    }

    private void showOwnerDetailsPopup(CollaborationMemberData ownerMember) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Owner Details");
        dialog.setHeaderText("Owner Details");
        dialog.getDialogPane().setPrefWidth(420);

        Label nameHeading = createPopupHeading("Name");
        Label nameValue = createPopupValue(ownerMember.name);

        Label emailHeading = createPopupHeading("Email");
        Label emailValue = createPopupValue(ownerMember.email);

        Label roleHeading = createPopupHeading("Role");

        Label roleValue = new Label(ownerMember.role);
        roleValue.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        roleValue.setTextFill(Color.web(PRIMARY_BLUE));
        roleValue.setPadding(new Insets(6, 10, 6, 10));
        roleValue.setStyle(
                "-fx-background-color:" + PRIMARY_LIGHT_BLUE + ";" +
                "-fx-background-radius:6;");

        VBox box = new VBox(
                12,
                nameHeading, nameValue,
                emailHeading, emailValue,
                roleHeading, roleValue);

        box.setPadding(new Insets(20));
        dialog.getDialogPane().setContent(box);
        addCloseButton(dialog);
        dialog.showAndWait();
    }

    private void checkUserAccessAndLoadContent() {
        String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";
        
        try {
            com.google.cloud.firestore.DocumentReference spaceDocRef = 
                FirebaseConfig.getFirestore()
                    .collection("workspaces")
                    .document(spaceName.replaceAll("\\s+", "_"));

            spaceDocRef.get().addListener(() -> {
                try {
                    var docSnap = spaceDocRef.get().get();
                    if (docSnap.exists()) {
                        if (docSnap.contains("createdAt")) {
                            Object cDate = docSnap.get("createdAt");
                            if (cDate != null) {
                                createdDate = cDate.toString();
                            }
                        }
                        if (docSnap.contains("ownerName")) {
                            workspaceOwnerName = docSnap.getString("ownerName");
                        }
                        if (docSnap.contains("ownerEmail")) {
                            workspaceOwnerEmail = docSnap.getString("ownerEmail");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, command -> command.run());

            com.google.api.core.ApiFuture<com.google.cloud.firestore.QuerySnapshot> future = 
                spaceDocRef.collection("members").get();

            com.google.api.core.ApiFutures.addCallback(future, new com.google.api.core.ApiFutureCallback<com.google.cloud.firestore.QuerySnapshot>() {
                @Override
                public void onSuccess(com.google.cloud.firestore.QuerySnapshot result) {
                    boolean isAuthorized = false;
                    List<CollaborationMemberData> fetchedMembers = new ArrayList<>();
                    
                    for (com.google.cloud.firestore.DocumentSnapshot doc : result.getDocuments()) {
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
                                "Owner", PRIMARY_LIGHT_BLUE, PRIMARY_BLUE, "active"));
                        isAuthorized = true;
                        currentUserRole = "Owner";
                    }

                    List<CollaborationFileData> fetchedFiles = new ArrayList<>();
                    try {
                        var fileDocs = spaceDocRef.collection("files")
                            .get().get().getDocuments();
                            
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
                    
                    javafx.application.Platform.runLater(() -> {
                        membersList.clear();
                        membersList.addAll(fetchedMembers);

                        filesList.clear();
                        filesList.addAll(fetchedFiles);

                        updateMemberCount();
                        updateFileCount();
                        
                        if (ownerNameLabel != null) {
                            ownerNameLabel.setText(workspaceOwnerName);
                        }
                        if (createdDateLabel != null) {
                            createdDateLabel.setText(createdDate);
                        }

                        if (manageAccessButton != null) {
                            updateManageAccessPermission(manageAccessButton);
                        }

                        refreshFileList();
                        refreshMemberList();

                        if (allowed || membersList.isEmpty()) {
                            listenForRealtimeFiles();
                            listenForRealtimeMembers();
                        } else {
                            showAccessDeniedPopup("Your invite is still pending. Please accept it in the Collaboration page first.");
                        }
                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            }, command -> command.run());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Label createPopupHeading(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        label.setStyle("-fx-text-fill: #000000;");
        return label;
    }

    private Label createPopupValue(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        label.setStyle("-fx-text-fill: #000000;");
        return label;
    }

    private void addCloseButton(Dialog<ButtonType> dialog) {
        dialog.getDialogPane().getButtonTypes().add(
                new ButtonType("Close", ButtonData.CANCEL_CLOSE));
    }

    private VBox createFilesCard() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;");

        Label title = new Label("Files");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        title.setStyle("-fx-text-fill: #000000;");

        Label subtitle = new Label("Files uploaded to this shared space");
        subtitle.setFont(Font.font(FONT, 11));
        subtitle.setStyle("-fx-text-fill: #000000;");

        VBox titleBox = new VBox(3, title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button upload = new Button("☁  Upload File");
        upload.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        upload.setTextFill(Color.WHITE);
        upload.setPrefHeight(38);
        upload.setPadding(new Insets(0, 16, 0, 16));
        upload.setStyle(
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;");
        
        updateUploadPermission(upload);
        
        upload.setOnAction(e -> {
            if (!currentUserCan("UPLOAD")) {
                showAccessDeniedPopup("Only Owners, Editors, and Moderators can upload files.");
                return;
            }

            CollaborationController collabController = new CollaborationController();
            final String currentUserName = (UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null) 
                    ? UserSession.getInstance().getDisplayName() : "User";

            collabController.uploadFileForCollaboration(
                upload.getScene().getWindow(),
                result -> {
                    String secureUrl = (String) result.get("secure_url");
                    String fileName = (String) result.get("original_filename");
                    if (fileName == null || fileName.isEmpty()) {
                        fileName = "Uploaded_File";
                    }

                    String actualSize = "1.2 KB";
                    Object fileObj = result.get("file_obj"); 
                    if (fileObj instanceof File) {
                        File f = (File) fileObj;
                        if (f.exists()) {
                            long bytes = f.length();
                            if (bytes < 1024) {
                                actualSize = bytes + " B";
                            } else if (bytes < 1024 * 1024) {
                                actualSize = (bytes / 1024) + " KB";
                            } else {
                                actualSize = String.format("%.1f MB", (double) bytes / (1024 * 1024));
                            }
                        }
                    }

                    String actualDate = java.time.LocalDate.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("d MMM yyyy")
                    );

                    final String finalFileName = fileName;
                    final String fileId = "file_" + System.currentTimeMillis();

                    CollaborationFileData newFile = new CollaborationFileData("FILE", finalFileName, actualSize, actualDate, PRIMARY_BLUE, secureUrl, currentUserName);

                    try {
                        java.util.Map<String, Object> fileMap = new java.util.HashMap<>();
                        fileMap.put("icon", newFile.icon);
                        fileMap.put("fileName", newFile.fileName);
                        fileMap.put("size", newFile.size);
                        fileMap.put("uploadedOn", newFile.uploadedOn);
                        fileMap.put("iconColor", newFile.iconColor);
                        fileMap.put("secureUrl", newFile.secureUrl);
                        fileMap.put("uploaderName", newFile.uploaderName);

                        FirebaseConfig.getFirestore()
                            .collection("workspaces")
                            .document(spaceName.replaceAll("\\s+", "_"))
                            .collection("files")
                            .document(fileId)
                            .set(fileMap);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    javafx.application.Platform.runLater(() -> {
                        filesList.add(newFile);
                        refreshFileList();
                        updateFileCount();
                    });
                },
                error -> {
                    error.printStackTrace();
                }
            );
        });

        HBox titleRow = new HBox(10, titleBox, spacer, upload);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        fileSearchField = createSearchField("⌕  Search files...");
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

// Use explicit fixed widths for reliable column tracking
name.setPrefWidth(260);
name.setMinWidth(260);
name.setMaxWidth(260);

size.setPrefWidth(110);
size.setMinWidth(110);
size.setMaxWidth(110);

uploaded.setPrefWidth(180);
uploaded.setMinWidth(180);
uploaded.setMaxWidth(180);

more.setPrefWidth(30);
more.setMinWidth(30);
more.setMaxWidth(30);

        
        tableHeader.getChildren().addAll(name, size, uploaded, more);

        fileListBox = new VBox(0);
        
        ScrollPane fileScroll = new ScrollPane(fileListBox);
        fileScroll.setFitToWidth(true);
        fileScroll.setPrefHeight(180);
        fileScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        fileScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        refreshFileList();

        Button viewAll = createViewAllButton("View All Files");
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
                "-fx-background-color:" + BG_INPUT + ";" +
                "-fx-text-fill:" + TEXT_DARK + ";" +
                "-fx-prompt-text-fill:" + TEXT_MUTED_DARK + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
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
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;");

        Label title = new Label("Members");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        title.setStyle("-fx-text-fill: #000000;");

        manageAccessButton = new Button("♜  Manage Access");
        manageAccessButton.setPrefHeight(36);
        manageAccessButton.setPrefWidth(140);
        manageAccessButton.setAlignment(Pos.CENTER);
        manageAccessButton.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        manageAccessButton.setTextFill(Color.WHITE);
        manageAccessButton.setStyle(
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-background-radius:7;" +
                "-fx-border-color:" + PRIMARY_BLUE + ";" +
                "-fx-border-radius:7;" +
                "-fx-cursor:hand;");

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

        memberSearchField = createSearchField("⌕  Search members...");
        memberSearchField.textProperty().addListener((obs, oldValue, newValue) -> refreshMemberList());

        memberListBox = new VBox(0);
        
        ScrollPane memberScroll = new ScrollPane(memberListBox);
        memberScroll.setFitToWidth(true);
        memberScroll.setPrefHeight(180);
        memberScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        memberScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        refreshMemberList();

        Button viewAll = createViewAllButton("View All Members");
        viewAll.setOnAction(e -> showAllMembersPopup());

        Button addMember = new Button("♙  Add Member     ▼");
        addMember.setMaxWidth(Double.MAX_VALUE);
        addMember.setPrefHeight(40);
        addMember.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        addMember.setTextFill(Color.WHITE);
        addMember.setStyle(
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;");

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

        Label label = new Label(message);
        label.setFont(Font.font(FONT, 13));
        label.setStyle("-fx-text-fill: #000000;");
        label.setWrapText(true);

        VBox box = new VBox(label);
        box.setPadding(new Insets(20));
        box.setPrefWidth(360);

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
            empty.setStyle("-fx-text-fill: #000000;");
            empty.setPadding(new Insets(15));

            fileListBox.getChildren().add(empty);
        }
        
        fileListBox.requestLayout();
    }

    private HBox createFileRow(CollaborationFileData file) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMinHeight(58);
        row.setPadding(new Insets(7, 10, 7, 10));
        row.setStyle(
                "-fx-border-color:transparent transparent " +
                BORDER_COLOR + " transparent;");

        Label icon = new Label(file.icon != null ? file.icon : "FILE");
        icon.setFont(Font.font(FONT, FontWeight.BOLD, 8));
        icon.setTextFill(Color.WHITE);
        icon.setAlignment(Pos.CENTER);
        icon.setPrefSize(30, 34);
        icon.setStyle(
                "-fx-background-color:" + (file.iconColor != null ? file.iconColor : PRIMARY_BLUE) + ";" +
                "-fx-background-radius:4;");

        Label name = new Label(file.fileName != null ? file.fileName : "Unnamed File");
        name.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        name.setStyle("-fx-text-fill: #000000;");

        HBox nameBox = new HBox(12, icon, name);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameBox.setPrefWidth(260);
        nameBox.setMinWidth(260);
        nameBox.setMaxWidth(260);
        HBox.setHgrow(nameBox, Priority.ALWAYS);

        String displaySize = (file.size == null || file.size.equalsIgnoreCase("Cloud File") || file.size.equalsIgnoreCase("Local File") || file.size.isEmpty()) ? "1.2 MB" : file.size;
        Label size = new Label(displaySize);
        size.setFont(Font.font(FONT, 12));
        size.setStyle("-fx-text-fill: #000000;");
        size.setPrefWidth(110);
        size.setMinWidth(110);
        size.setMaxWidth(110);

        String displayDate = (file.uploadedOn == null || file.uploadedOn.equalsIgnoreCase("Just now") || file.uploadedOn.isEmpty()) ? "26 Aug 2026" : file.uploadedOn;
        Label date = new Label(displayDate);
        date.setFont(Font.font(FONT, 12));
        date.setStyle("-fx-text-fill: #000000;");
        date.setPrefWidth(180);
        date.setMinWidth(180);
        date.setMaxWidth(180);;

        Button more = new Button("⋮");
        more.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        more.setStyle("-fx-text-fill: #000000; -fx-background-color: transparent; -fx-cursor: hand;");
        more.setPrefWidth(30);
        more.setMinWidth(30);
        more.setMaxWidth(30);

        ContextMenu menu = new ContextMenu();
        
        MenuItem viewFile = new MenuItem("View File");
        viewFile.setOnAction(e -> {
            if (!currentUserCan("VIEW")) {
                showAccessDeniedPopup("You do not have permission to view files.");
                return;
            }
            showFilePreviewDialog(file);
        });
        menu.getItems().add(viewFile);

        MenuItem download = new MenuItem("Download File");
        download.setOnAction(e -> {
            String activeRole = getLoggedInUserRole();
            if ("Viewer".equalsIgnoreCase(activeRole)) {
                showAccessDeniedPopup("Viewer can only view. You cannot download.");
                return;
            }
            if (file.secureUrl != null && !file.secureUrl.isEmpty()) {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(file.secureUrl));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        menu.getItems().add(download);

        MenuItem delete = new MenuItem("Delete File");
        delete.setOnAction(e -> {
            String activeRole = getLoggedInUserRole();
            boolean isModerator = "Moderator".equalsIgnoreCase(activeRole);
            boolean isOwnerUser = "Owner".equalsIgnoreCase(activeRole);

            if (isOwnerUser || isModerator) {
                try {
                    com.google.cloud.firestore.Firestore db = FirebaseConfig.getFirestore();
                    var docs = db.collection("workspaces")
                        .document(spaceName.replaceAll("\\s+", "_"))
                        .collection("files")
                        .whereEqualTo("fileName", file.fileName)
                        .get().get().getDocuments();
                        
                    for (var doc : docs) {
                        doc.getReference().delete();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                filesList.remove(file);
                refreshFileList();
                updateFileCount();
            } else if ("Editor".equalsIgnoreCase(activeRole)) {
                showAccessDeniedPopup("Editors cannot delete files.");
            } else if ("Viewer".equalsIgnoreCase(activeRole)) {
                showAccessDeniedPopup("Viewers cannot delete files.");
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
        dialog.getDialogPane().setPrefWidth(1000);
        dialog.getDialogPane().setPrefHeight(700);

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
                String htmlContent = "<html><body style='background:#f1f5f9; display:flex; justify-content:center; align-items:center; height:100vh; margin:0;'>"
                        + "<img src='" + file.secureUrl + "' style='max-width:100%; max-height:100%; object-fit:contain; box-shadow: 0 4px 12px rgba(0,0,0,0.15); border-radius:8px;'/>"
                        + "</body></html>";
                webView.getEngine().loadContent(htmlContent);
                targetUrl = null;
            }

            if (targetUrl != null) {
                webView.getEngine().load(targetUrl);
            }
        } else {
            webView.getEngine().loadContent("<h3 style='font-family:sans-serif; text-align:center; margin-top:50px;'>File preview unavailable.</h3>");
        }

        VBox container = new VBox(10, webView);
        container.setPadding(new Insets(15));
        container.setStyle("-fx-background-color: " + BG_CARD + ";");
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

        Label label = new Label(message);
        label.setFont(Font.font(FONT, 13));
        label.setStyle("-fx-text-fill: #000000;");
        label.setWrapText(true);

        VBox box = new VBox(label);
        box.setPadding(new Insets(20));
        box.setPrefWidth(380);

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
                            ? "No members added yet."
                            : "No matching members found.");

            empty.setFont(Font.font(FONT, 11));
            empty.setStyle("-fx-text-fill: #000000;");
            empty.setPadding(new Insets(12, 0, 12, 0));

            memberListBox.getChildren().add(empty);
        }

        memberListBox.requestLayout();
    }

    private HBox createMemberRow(CollaborationMemberData member) {
        HBox row = new HBox(9);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(9, 0, 9, 0));
        row.setStyle(
                "-fx-border-color:transparent transparent " +
                BORDER_COLOR + " transparent;");

        Label avatar = new Label(member.initials != null ? member.initials : "M");
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setAlignment(Pos.CENTER);
        avatar.setPrefSize(36, 36);
        avatar.setStyle(
                "-fx-background-color: #334155;" +
                "-fx-background-radius: 50%;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0, 0, 1);");

        Label name = new Label(member.name != null ? member.name : "Member");
        name.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        name.setStyle("-fx-text-fill: #000000;");

        Label email = new Label(member.email != null ? member.email : "");
        email.setFont(Font.font(FONT, 9));
        email.setStyle("-fx-text-fill: #000000;");

        VBox info = new VBox(2, name, email);
        HBox.setHgrow(info, Priority.ALWAYS);

        String roleBadgeBg = "Owner".equalsIgnoreCase(member.role) ? "#BFDBFE" : "#E2E8F0";
        String roleBadgeText = "Owner".equalsIgnoreCase(member.role) ? "#1E40AF" : "#000000";

        Label role = new Label(member.role != null ? member.role : "Viewer");
        role.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        role.setStyle(
                "-fx-text-fill: " + roleBadgeText + ";" +
                "-fx-background-color: " + roleBadgeBg + ";" +
                "-fx-background-radius: 6;" +
                "-fx-border-color: #64748B;" +
                "-fx-border-radius: 6;" +
                "-fx-border-width: 0.8;" +
                "-fx-padding: 3 8;");

        Button more = new Button("⋮");
        more.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        more.setStyle("-fx-text-fill: #000000; -fx-background-color: transparent; -fx-cursor: hand;");
        more.setPrefWidth(25);

        boolean isOwner = isCurrentLoggedInUserOwner();
        more.setVisible(isOwner);
        more.setManaged(isOwner);

        ContextMenu menu = new ContextMenu();
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
        button.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        button.setTextFill(Color.web(PRIMARY_BLUE));
        button.setStyle(
                "-fx-background-color:" + PRIMARY_LIGHT_BLUE + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;");
        return button;
    }

    private void showAllFilesPopup() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("All Files");
        dialog.setHeaderText(
                spaceName + " - All Uploaded Files");

        VBox box = new VBox(0);
        box.setPrefWidth(700);
        box.setStyle("-fx-background-color: " + BG_CARD + ";");

        for (CollaborationFileData file : filesList) {
            box.getChildren().add(createFileRow(file));
        }

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setPrefWidth(720);
        scroll.setPrefHeight(420);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:transparent;");

        dialog.getDialogPane().setContent(scroll);
        addCloseButton(dialog);
        dialog.showAndWait();
    }

    private void showAllMembersPopup() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("All Members");
        dialog.setHeaderText(
                spaceName + " - All Members");

        VBox box = new VBox(0);
        box.setPrefWidth(520);

        for (CollaborationMemberData member : membersList) {
            box.getChildren().add(createMemberRow(member));
        }

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setPrefWidth(550);
        scroll.setPrefHeight(420);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:transparent;");

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
            memberCountLabel.setText(
                    membersList.size() + " Members");
        }
    }

    private void styleTableHeader(Label label) {
        label.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        label.setStyle("-fx-text-fill: #000000;");
    }

    private void showManageAccessPopup() {
        if (!isCurrentLoggedInUserOwner()) {
            showAccessDeniedPopup("Access Denied: Only the workspace Owner can manage member access.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Manage Access");
        dialog.setHeaderText(
                spaceName + " - Manage Access");

        dialog.getDialogPane().setPrefWidth(600);
        dialog.getDialogPane().setPrefHeight(470);

        Label description = new Label(
                "Manage members and their access roles.");
        description.setFont(Font.font(FONT, 12));
        description.setStyle("-fx-text-fill: #000000;");

        Label currentRole = new Label(
                "Current User Role: " + getLoggedInUserRole());
        currentRole.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        currentRole.setTextFill(Color.web(PRIMARY_BLUE));

        VBox memberRows = new VBox(0);

        for (CollaborationMemberData member : membersList) {
            memberRows.getChildren().add(
                    createManageAccessRow(member));
        }

        ScrollPane scroll = new ScrollPane(memberRows);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(340);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";");

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
                "-fx-border-color:transparent transparent " +
                BORDER_COLOR + " transparent;");

        Label avatar = new Label(member.initials != null ? member.initials : "M");
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        avatar.setTextFill(Color.WHITE);
        avatar.setAlignment(Pos.CENTER);
        avatar.setPrefSize(38, 38);
        avatar.setStyle(
                "-fx-background-color: #334155;" +
                "-fx-background-radius: 50%;");

        Label name = new Label(member.name != null ? member.name : "Member");
        name.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        name.setStyle("-fx-text-fill: #000000;");

        Label email = new Label(member.email != null ? member.email : "");
        email.setFont(Font.font(FONT, 9));
        email.setStyle("-fx-text-fill: #000000;");

        VBox info = new VBox(2, name, email);
        HBox.setHgrow(info, Priority.ALWAYS);

        if (member.role != null && member.role.equalsIgnoreCase("Owner")) {
            Label owner = new Label("Owner");
            owner.setFont(Font.font(FONT, FontWeight.BOLD, 10));
            owner.setTextFill(Color.web(PRIMARY_BLUE));
            owner.setPadding(new Insets(6, 12, 6, 12));
            owner.setStyle(
                    "-fx-background-color:" +
                    PRIMARY_LIGHT_BLUE + ";" +
                    "-fx-background-radius:6;");

            row.getChildren().addAll(
                    avatar,
                    info,
                    owner);

            return row;
        }

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Editor", "Moderator", "Viewer");
        roleCombo.setValue(member.role != null ? member.role : "Viewer");
        roleCombo.setPrefWidth(110);
        roleCombo.setPrefHeight(34);
        roleCombo.setStyle(
                "-fx-background-color:" + BG_INPUT + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;");

        roleCombo.setOnAction(e ->
                updateMemberRole(
                        member,
                        roleCombo.getValue()));

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
            member.avatarBackground = PRIMARY_LIGHT_BLUE;
            member.avatarColor = PRIMARY_BLUE;
        } else if ("Moderator".equalsIgnoreCase(member.role)) {
            member.avatarBackground = ORANGE_LIGHT;
            member.avatarColor = ORANGE;
        } else if ("Viewer".equalsIgnoreCase(member.role)) {
            member.avatarBackground = SUCCESS_LIGHT;
            member.avatarColor = SUCCESS;
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

        dialog.getDialogPane().setPrefWidth(500);
        dialog.getDialogPane().setPrefHeight(430);

        Label nameLabel = new Label("Name");
        styleFormLabel(nameLabel);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter member name");
        nameField.setPrefHeight(42);

        Label emailLabel = new Label("Email");
        styleFormLabel(emailLabel);

        TextField emailField = new TextField();
        emailField.setPromptText("Enter member email");
        emailField.setPrefHeight(42);

        Label roleLabel = new Label("Role");
        styleFormLabel(roleLabel);

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Viewer", "Moderator", "Editor");
        roleCombo.setValue("Viewer");
        roleCombo.setMaxWidth(Double.MAX_VALUE);
        roleCombo.setPrefHeight(42);
        roleCombo.setStyle(
                "-fx-background-color:" + BG_INPUT + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;");

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

        sendNode.setPrefHeight(38);
        sendNode.setPrefWidth(120);
        sendNode.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        sendNode.setTextFill(Color.WHITE);
        sendNode.setStyle(
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;");

        Button cancelNode = (Button) dialog.getDialogPane().lookupButton(cancelButton);

        cancelNode.setPrefHeight(38);
        cancelNode.setPrefWidth(90);
        cancelNode.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        cancelNode.setStyle("-fx-text-fill: #000000;");
        cancelNode.setStyle(
                "-fx-background-color:" + BG_INPUT + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;");

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
                background = PRIMARY_LIGHT_BLUE;
                avatarColor = PRIMARY_BLUE;
            } else if (role.equalsIgnoreCase("Moderator")) {
                background = ORANGE_LIGHT;
                avatarColor = ORANGE;
            } else {
                background = SUCCESS_LIGHT;
                avatarColor = SUCCESS;
            }

            CollaborationMemberData newMember = new CollaborationMemberData(
                    initials,
                    name,
                    email,
                    role,
                    background,
                    avatarColor,
                    "pending" // <-- Set to pending so it routes to the Pending Invites queue
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
        label.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        label.setStyle("-fx-text-fill: #000000;");
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
        BorderPane root = new BorderPane();

        root.setStyle("-fx-background-color:" + BG_APP + ";");

        root.setLeft(createSidebar());

        ScrollPane scroll = new ScrollPane(getSharedSpaceContent());

        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle(
                "-fx-background-color:" + BG_APP + ";" +
                "-fx-border-color:transparent;");

        root.setCenter(scroll);

        return new Scene(root, 1280, 800);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setPadding(new Insets(22, 14, 20, 14));
        sidebar.setStyle(
                "-fx-background-color:" + BG_SIDEBAR_CARD + ";" +
                "-fx-border-color:transparent;");

        Label logoIcon = new Label("◉");
        logoIcon.setFont(Font.font(FONT, FontWeight.BOLD, 20));
        logoIcon.setTextFill(Color.web(PRIMARY_LIGHT_BLUE));

        Label logo = new Label("OneSpace");
        logo.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        logo.setTextFill(Color.web(TEXT_LIGHT));

        HBox logoRow = new HBox(9, logoIcon, logo);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        Label local = new Label("Local • AI Indexed");
        local.setFont(Font.font(FONT, 11));
        local.setTextFill(Color.web(TEXT_MUTED_LIGHT));

        VBox logoBox = new VBox(4, logoRow, local);
        logoBox.setPadding(new Insets(0, 8, 25, 8));

        Button dashboard = createSidebarButton("⌂", "Dashboard", false);

        Button spaces = createSidebarButton("▦", "Spaces", false);
        Button search = createSidebarButton("⌕", "Search", false);
        Button calendar = createSidebarButton("□", "Calendar", false);
        Button aiAssistant = createSidebarButton("✧", "AI Assistant", false);

        Button collaboration = createSidebarButton("♧", "Collaboration", true);

        Button recent = createSidebarButton("◷", "Recent", false);
        Button trash = createSidebarButton("♧", "Trash", false);
        Button logoutBtn = createSidebarButton("🚪", "Logout", false);



        dashboard.setOnAction(e -> LandingPage.showUserDashboard());
        spaces.setOnAction(e -> LandingPage.showUserSpace());
        search.setOnAction(e -> LandingPage.showUserSearch());
        calendar.setOnAction(e -> LandingPage.showCalendarPage());
        aiAssistant.setOnAction(e -> LandingPage.showAiAssistantPage());
        collaboration.setOnAction(e -> LandingPage.showCollaborationPage());
        recent.setOnAction(e -> LandingPage.showRecentPage());
        trash.setOnAction(e -> LandingPage.showTrashPage());
        logoutBtn.setOnAction(e -> LandingPage.showUserLoginPage());



        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button settings = createSidebarButton("⚙", "Settings", false);
        settings.setOnAction(e -> LandingPage.showSettingPage());

        VBox storage = createStorageCard();

        sidebar.getChildren().addAll(
                logoBox,
                dashboard,
                spaces,
                search,
                calendar,
                aiAssistant,
                collaboration,
                recent,
                trash,
                spacer,
                settings,
                storage);

        return sidebar;
    }

    private Button createSidebarButton(
            String icon,
            String text,
            boolean selected) {

        Button button = new Button(icon + "    " + text);

        button.setAlignment(Pos.CENTER_LEFT);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.setPadding(new Insets(0, 10, 0, 10));
        button.setFont(Font.font(FONT, 12));

        if (selected) {
            button.setTextFill(Color.web(TEXT_LIGHT));
            button.setStyle(
                    "-fx-background-color:" + PRIMARY_BLUE + ";" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;");
        } else {
            button.setTextFill(Color.web(TEXT_MUTED_LIGHT));
            button.setStyle(
                    "-fx-background-color:transparent;" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;");

            button.setOnMouseEntered(e ->
                    button.setStyle(
                            "-fx-background-color:rgba(191,219,254,0.12);" +
                            "-fx-background-radius:8;" +
                            "-fx-cursor:hand;"));

            button.setOnMouseExited(e ->
                    button.setStyle(
                            "-fx-background-color:transparent;" +
                            "-fx-background-radius:8;" +
                            "-fx-cursor:hand;"));
        }

        return button;
    }

    private VBox createStorageCard() {
        VBox card = new VBox(8);
        card.setMinHeight(150);
        card.setPrefHeight(150);
        card.setMaxHeight(150);
        card.setPadding(new Insets(15));
        card.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;");

        Label title = new Label("✧  Storage indexed");
        title.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        title.setTextFill(Color.web(PRIMARY_BLUE));

        Label amount = new Label("64.2 GB");
        amount.setFont(Font.font(FONT, FontWeight.BOLD, 19));
        amount.setTextFill(Color.web(TEXT_DARK));

        Label used = new Label("of 100 GB used");
        used.setFont(Font.font(FONT, 10));
        used.setTextFill(Color.web(TEXT_MUTED_DARK));

        HBox progressBox = new HBox();
        progressBox.setPrefHeight(7);
        progressBox.setMinHeight(7);
        progressBox.setMaxWidth(Double.MAX_VALUE);
        progressBox.setStyle(
                "-fx-background-color:" + BORDER_COLOR + ";" +
                "-fx-background-radius:10;");

        Region progress = new Region();
        progress.setPrefWidth(105);
        progress.setPrefHeight(7);
        progress.setStyle(
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-background-radius:10;");

        progressBox.getChildren().add(progress);

        Label bottom = new Label(
                "Files stay in place —\n" +
                "nothing was moved or renamed.");

        bottom.setFont(Font.font(FONT, 9));
        bottom.setTextFill(Color.web(TEXT_MUTED_DARK));
        bottom.setWrapText(true);

        card.getChildren().addAll(
                title,
                amount,
                used,
                progressBox,
                bottom);

        return card;
    }
}