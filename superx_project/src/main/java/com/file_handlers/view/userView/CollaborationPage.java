package com.file_handlers.view.userView;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;
import com.file_handlers.util.ResponsiveUtil;
import com.file_handlers.controller.WorkspaceController;
import com.file_handlers.dao.WorkspaceDAO;
import java.util.HashMap;

public class CollaborationPage {

    // Typography
    private static final String FONT = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

    // 1. Sidebar & Top Bar Tones
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

    private static class WorkspaceData {
        String iconType, iconColor, name, storage, role, badgeBg, badgeText, docId;
        String ownerEmail;
        int members, files;

        WorkspaceData(String iconType, String iconColor, String name, int members, int files,
                      String storage, String role, String badgeBg, String badgeText, String ownerEmail, String docId) {
            this.iconType = iconType;
            this.iconColor = iconColor;
            this.name = name;
            this.members = members;
            this.files = files;
            this.storage = storage;
            this.role = role;
            this.badgeBg = badgeBg;
            this.badgeText = badgeText;
            this.ownerEmail = ownerEmail;
            this.docId = docId;
        }
    }

    private static class ActivityItem {
        String user, action, time;
        ActivityItem(String user, String action, String time) {
            this.user = user;
            this.action = action;
            this.time = time;
        }
    }

    private final List<WorkspaceData> workspaces = new ArrayList<>();
    private final List<ActivityItem> activitiesList = new ArrayList<>();
    private Label spacesValue, membersValue, filesValue;
    private boolean isGridView;
    private VBox workspaceListPane;
    private VBox activityListPane;
    
    private int pendingInvitesCount = 0;
    private Button pendingBtn;

    private void initializeWorkspacesAndActivities() {
        workspaces.clear();
        activitiesList.clear();
        
        String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";
        if (myEmail == null || myEmail.trim().isEmpty()) {
            return; 
        }
        
        try {
            com.google.cloud.firestore.Firestore db = com.file_handlers.config.FirebaseConfig.getFirestore();
            List<com.google.cloud.firestore.QueryDocumentSnapshot> workspacesDocs = db.collection("workspaces").get().get().getDocuments();
            
            for (com.google.cloud.firestore.DocumentSnapshot doc : workspacesDocs) {
                String docId = doc.getId();
                String spaceName = doc.getString("spaceName");
                if (spaceName == null) {
                    spaceName = doc.getString("name");
                }
                if (spaceName == null) {
                    spaceName = docId.replaceAll("_", " ");
                }
                
                int memberCount = 1;
                int fileCount = 0;
                String fetchedOwnerEmail = "";
                String userAssignedRole = "Viewer";
                boolean isUserMemberOrOwner = false;
                
                List<ActivityItem> tempWorkspaceActivities = new ArrayList<>();
                
                try {
                    // Optimized: Asynchronous Fetching without blocking execution threads synchronously using .get()
                    CompletableFuture<List<com.google.cloud.firestore.QueryDocumentSnapshot>> membersFuture = CompletableFuture.supplyAsync(() -> {
                        try {
                            return db.collection("workspaces").document(docId).collection("members").get().get().getDocuments();
                        } catch (Exception e) {
                            return new ArrayList<com.google.cloud.firestore.QueryDocumentSnapshot>();
                        }
                    });

                    CompletableFuture<List<com.google.cloud.firestore.QueryDocumentSnapshot>> filesFuture = CompletableFuture.supplyAsync(() -> {
                        try {
                            return db.collection("workspaces").document(docId).collection("files").get().get().getDocuments();
                        } catch (Exception e) {
                            return new ArrayList<com.google.cloud.firestore.QueryDocumentSnapshot>();
                        }
                    });

                    // Combined asynchronous join to prevent thread blocking stalls
                    CompletableFuture.allOf(membersFuture, filesFuture).join();

                    var membersDocs = membersFuture.get();
                    var filesDocs = filesFuture.get();

                    if (!membersDocs.isEmpty()) {
                        memberCount = membersDocs.size();
                    }
                    for (var mDoc : membersDocs) {
                        String mName = mDoc.getString("name");
                        String mRole = mDoc.getString("role");
                        String mEmail = mDoc.getString("email");
                        String mStatus = mDoc.getString("status");
                        
                        if ("Owner".equalsIgnoreCase(mRole)) {
                            fetchedOwnerEmail = mEmail != null ? mEmail : "";
                        }
                        
                        if (mEmail != null && mEmail.equalsIgnoreCase(myEmail)) {
                            if ("active".equalsIgnoreCase(mStatus) || "Owner".equalsIgnoreCase(mRole)) {
                                isUserMemberOrOwner = true;
                            }
                            if (mRole != null && !mRole.isEmpty()) {
                                userAssignedRole = mRole;
                            }
                        }

                        if (mName != null) {
                            tempWorkspaceActivities.add(new ActivityItem(mName, "joined '" + spaceName + "'", "Recently"));
                        }
                    }

                    fileCount = filesDocs.size();
                    for (var fDoc : filesDocs) {
                        String fName = fDoc.getString("fileName");
                        if (fName != null) {
                            tempWorkspaceActivities.add(new ActivityItem("Team Member", "uploaded '" + fName + "' to " + spaceName, "Just now"));
                        }
                    }
                } catch (Exception ignored) {}

                if (!isUserMemberOrOwner) {
                    continue;
                }
                
                activitiesList.addAll(tempWorkspaceActivities);
                
                String badgeBg = "rgba(255, 255, 255, 0.08)";
                String badgeText = LIGHT_SECONDARY;
                
                if ("Owner".equalsIgnoreCase(userAssignedRole)) {
                    badgeBg = "rgba(37, 99, 235, 0.2)";
                    badgeText = "#60A5FA";
                } else if ("Moderator".equalsIgnoreCase(userAssignedRole)) {
                    badgeBg = "rgba(245, 158, 11, 0.2)";
                    badgeText = "#FBBF24";
                } else if ("Editor".equalsIgnoreCase(userAssignedRole)) {
                    badgeBg = "rgba(56, 189, 248, 0.2)";
                    badgeText = "#38BDF8";
                } else if ("Viewer".equalsIgnoreCase(userAssignedRole)) {
                    badgeBg = "rgba(16, 185, 129, 0.2)";
                    badgeText = "#34D399";
                }
                
                workspaces.add(new WorkspaceData(
                    "files", 
                    "#38BDF8", 
                    spaceName, 
                    memberCount, 
                    fileCount, 
                    fileCount > 0 ? "Synced" : "No files", 
                    userAssignedRole, 
                    badgeBg, 
                    badgeText,
                    fetchedOwnerEmail,
                    docId
                ));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (workspaces.isEmpty()) {
            workspaces.add(new WorkspaceData("files", "#38BDF8", "College Presentation", 4, 32,
                    "12.4 GB", "Owner", "rgba(37, 99, 235, 0.2)", "#60A5FA", "", ""));
        }

        if (activitiesList.isEmpty()) {
            activitiesList.add(new ActivityItem("System", "No recent activity in your workspaces", "Just now"));
        }
    }

    private void updatePendingInvitesButtonText() {
        if (pendingBtn != null) {
            if (pendingInvitesCount > 0) {
                pendingBtn.setText("Pending Invites (" + pendingInvitesCount + ")");
                pendingBtn.setStyle("-fx-background-color: rgba(245, 158, 11, 0.2); -fx-text-fill: #FBBF24; -fx-border-color: #FBBF24; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 14;");
            } else {
                pendingBtn.setText("Pending Invites");
                pendingBtn.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 14;");
            }
        }
    }

    public Scene getCollaborationPageScene() {
        String activeUserName = "User", initials = "U";

        if (UserSession.getInstance() != null && UserSession.getInstance().getDisplayName() != null && !UserSession.getInstance().getDisplayName().trim().isEmpty()) {
            String fullName = UserSession.getInstance().getDisplayName().trim();
            activeUserName = fullName.split("\\s+")[0];
            initials = activeUserName.substring(0, 1).toUpperCase();
        }

        VBox sidebar = createSidebar();

        SVGPath bellIcon = createIcon("bell");
        bellIcon.setStroke(Color.WHITE);
        bellIcon.setStrokeWidth(2);

        Button bellBtn = new Button();
        bellBtn.setGraphic(bellIcon);
        bellBtn.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 6 10;");
        bellBtn.setOnAction(e -> LandingPage.showNotificationPage());
        applyHoverAnimation(bellBtn, 1.08, 0);

        Label avatar = new Label(initials);
        avatar.setPrefSize(34, 34); avatar.setMinSize(34, 34); avatar.setMaxSize(34, 34);
        avatar.setAlignment(Pos.CENTER);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        avatar.setTextFill(Color.WHITE);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563EB, #00D2FF); -fx-background-radius: 50%; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.5), 10, 0, 0, 2);");
        applyHoverAnimation(avatar, 1.15, 0);

        Label userName = new Label(activeUserName);
        userName.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 13));
        userName.setStyle("-fx-text-fill:" + WHITE + ";");

        Label dropDown = new Label("⌄");
        dropDown.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
        dropDown.setStyle("-fx-text-fill:" + LIGHT_SECONDARY + ";");

        HBox profileOption = new HBox(8, avatar, userName, dropDown);
        profileOption.setAlignment(Pos.CENTER);
        profileOption.setPadding(new Insets(4, 12, 4, 6));
        profileOption.setStyle("-fx-background-color: rgba(13, 22, 38, 0.85); -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;");
        applyHoverAnimation(profileOption, 1.04, 0);

        Popup userDropdownPopup = new Popup();
        userDropdownPopup.setAutoHide(true);

        Button profileDropdownBtn = new Button("👥    Profile");
        profileDropdownBtn.setMaxWidth(Double.MAX_VALUE);
        profileDropdownBtn.setAlignment(Pos.CENTER_LEFT);
        profileDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #F59E0B;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        );
        profileDropdownBtn.setOnMouseEntered(e -> profileDropdownBtn.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: #F59E0B;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 6;"
        ));
        profileDropdownBtn.setOnMouseExited(e -> profileDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #F59E0B;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        ));
        profileDropdownBtn.setOnAction(e -> {
            userDropdownPopup.hide();
            Platform.runLater(LandingPage::showUserProfilePage);
        });
        profileDropdownBtn.setOnMouseClicked(e -> {
            userDropdownPopup.hide();
            Platform.runLater(LandingPage::showUserProfilePage);
        });

        Button settingsDropdownBtn = new Button("⚙    Settings");
        settingsDropdownBtn.setMaxWidth(Double.MAX_VALUE);
        settingsDropdownBtn.setAlignment(Pos.CENTER_LEFT);
        settingsDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #38BDF8;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        );
        settingsDropdownBtn.setOnMouseEntered(e -> settingsDropdownBtn.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: #38BDF8;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 6;"
        ));
        settingsDropdownBtn.setOnMouseExited(e -> settingsDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #38BDF8;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        ));
        settingsDropdownBtn.setOnAction(e -> {
            userDropdownPopup.hide();
            Platform.runLater(LandingPage::showSettingPage);
        });
        settingsDropdownBtn.setOnMouseClicked(e -> {
            userDropdownPopup.hide();
            Platform.runLater(LandingPage::showSettingPage);
        });

        Separator dropdownSeparator = new Separator();
        dropdownSeparator.setStyle("-fx-background-color: #1E293B; -fx-padding: 4 0;");

        Button logoutDropdownBtn = new Button("↳    Logout");
        logoutDropdownBtn.setMaxWidth(Double.MAX_VALUE);
        logoutDropdownBtn.setAlignment(Pos.CENTER_LEFT);
        logoutDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #F87171;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        );
        logoutDropdownBtn.setOnMouseEntered(e -> logoutDropdownBtn.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: #F87171;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 6;"
        ));
        logoutDropdownBtn.setOnMouseExited(e -> logoutDropdownBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #F87171;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: " + FONT + ";" +
                "-fx-padding: 8 12;" +
                "-fx-cursor: hand;"
        ));
        logoutDropdownBtn.setOnAction(e -> {
            userDropdownPopup.hide();
            UserSession.clearSession();
            Platform.runLater(LandingPage::showUserLoginPage);
        });
        logoutDropdownBtn.setOnMouseClicked(e -> {
            userDropdownPopup.hide();
            UserSession.clearSession();
            Platform.runLater(LandingPage::showUserLoginPage);
        });

        VBox dropdownContainer = new VBox(4, profileDropdownBtn, settingsDropdownBtn, dropdownSeparator, logoutDropdownBtn);
        dropdownContainer.setPadding(new Insets(8));
        dropdownContainer.setPrefWidth(180);
        dropdownContainer.setStyle(
                "-fx-background-color: #0A121E;" +
                "-fx-border-color: #1E2D42;" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 12px;" +
                "-fx-background-radius: 12px;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 16, 0, 0, 8);"
        );

        userDropdownPopup.getContent().add(dropdownContainer);

        profileOption.setOnMouseClicked(e -> {
            if (userDropdownPopup.isShowing()) {
                userDropdownPopup.hide();
            } else {
                javafx.geometry.Point2D point = profileOption.localToScreen(0, profileOption.getHeight() + 6);
                userDropdownPopup.show(profileOption, point.getX(), point.getY());
            }
        });

        HBox profileBox = new HBox(10, bellBtn, profileOption);
        profileBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(20, new Region(), profileBox);
        HBox.setHgrow(topBar.getChildren().get(0), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(70); topBar.setMinHeight(70); topBar.setMaxHeight(70);
        topBar.setPadding(new Insets(16, ResponsiveUtil.PAGE_PADDING, 14, ResponsiveUtil.PAGE_PADDING));
        topBar.setStyle("-fx-background-color: transparent; -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-width: 0 0 1 0;");

        Label pageTitle = new Label("Collaboration");
        pageTitle.setFont(Font.font(FONT, FontWeight.BOLD, 26));
        pageTitle.setStyle("-fx-text-fill:" + WHITE + ";");

        Label pageDescription = new Label(
                "Invite team members to shared file workspaces with live access controls.");
        pageDescription.setFont(Font.font(FONT, FontWeight.MEDIUM, 13));
        pageDescription.setStyle("-fx-text-fill:" + LIGHT_SECONDARY + ";");

        VBox headerTitleBox = new VBox(4, pageTitle, pageDescription);

        pendingBtn = new Button("Pending Invites");
        pendingBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        updatePendingInvitesButtonText();

        pendingBtn.setOnMouseEntered(e -> {
            pendingBtn.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-text-fill: #38BDF8; -fx-border-color: #38BDF8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 14;");
            animateScale(pendingBtn, 1.04);
        });
        pendingBtn.setOnMouseExited(e -> {
            updatePendingInvitesButtonText();
            animateScale(pendingBtn, 1.0);
        });
        pendingBtn.setOnAction(e -> showPendingRequestsPopup());

        Button newSpaceButton = new Button("+    New Shared Space");
        newSpaceButton.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        newSpaceButton.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: #FFFFFF; -fx-background-radius: 10; -fx-border-color: rgba(96, 165, 250, 0.6); -fx-border-radius: 10; -fx-border-width: 1; -fx-cursor: hand; -fx-padding: 8 18; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.45), 10, 0, 0, 2);");
        applyHoverAnimation(newSpaceButton, 1.05, 0);

        BorderPane root = new BorderPane();
        newSpaceButton.setOnAction(e -> showCreateSharedSpacePopup(root));

        HBox headerActions = new HBox(10, pendingBtn, newSpaceButton);
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox pageHeader = new HBox(headerTitleBox, headerSpacer, headerActions);
        pageHeader.setAlignment(Pos.CENTER_LEFT);

        spacesValue = createValueLabel();
        membersValue = createValueLabel();
        filesValue = createValueLabel();

        HBox stat1 = createStatCard("collaboration", spacesValue, "Total Shared Workspaces", "#38BDF8", "rgba(56, 189, 248, 0.15)");
        HBox stat2 = createStatCard("users", membersValue, "Members Across All Workspaces", "#A78BFA", "rgba(167, 139, 250, 0.15)");
        HBox stat3 = createStatCard("files", filesValue, "Files Across All Workspaces", "#34D399", "rgba(52, 211, 153, 0.15)");

        HBox metrics = new HBox(14, stat1, stat2, stat3);
        HBox.setHgrow(stat1, Priority.ALWAYS);
        HBox.setHgrow(stat2, Priority.ALWAYS);
        HBox.setHgrow(stat3, Priority.ALWAYS);
        updateMetrics();

        Label workspaceTitle = new Label("Shared Workspaces");
        workspaceTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        workspaceTitle.setStyle("-fx-text-fill:" + WHITE + ";");

        Button toggleViewBtn = new Button("Switch to Grid View");
        toggleViewBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        toggleViewBtn.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE +
                "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6;" +
                "-fx-cursor: hand; -fx-padding: 5 10;");

        toggleViewBtn.setOnMouseEntered(e -> {
            toggleViewBtn.setStyle("-fx-background-color: rgba(56, 189, 248, 0.15); -fx-text-fill: #38BDF8; -fx-border-color: #38BDF8; -fx-border-radius: 6; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 10;");
            animateScale(toggleViewBtn, 1.04);
        });
        toggleViewBtn.setOnMouseExited(e -> {
            toggleViewBtn.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 10;");
            animateScale(toggleViewBtn, 1.0);
        });

        toggleViewBtn.setOnAction(e -> {
            isGridView = !isGridView;
            toggleViewBtn.setText(isGridView ? "Switch to List View" : "Switch to Grid View");
            rebuildWorkspaceCards(root);
        });

        Region wsHeaderSpacer = new Region();
        HBox.setHgrow(wsHeaderSpacer, Priority.ALWAYS);

        HBox workspaceHeaderBox = new HBox(workspaceTitle, wsHeaderSpacer, toggleViewBtn);
        workspaceHeaderBox.setAlignment(Pos.CENTER_LEFT);

        workspaceListPane = new VBox(10);
        workspaceListPane.setFillWidth(true);
        rebuildWorkspaceCards(root);

        Button viewAllWorkspaces = new Button("View all workspaces ›");
        viewAllWorkspaces.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        viewAllWorkspaces.setStyle("-fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-cursor: hand; -fx-padding: 8 0 4 0;");
        viewAllWorkspaces.setOnAction(e -> showAllWorkspacesPopup(root));
        viewAllWorkspaces.setOnMouseEntered(e -> animateTranslate(viewAllWorkspaces, 4, 0));
        viewAllWorkspaces.setOnMouseExited(e -> animateTranslate(viewAllWorkspaces, 0, 0));

        VBox workspacesBox = new VBox(14, workspaceHeaderBox, workspaceListPane, viewAllWorkspaces);
        workspacesBox.setPadding(new Insets(24));
        workspacesBox.setStyle(cardContainerStyle());

        Label activityTitle = new Label("Recent Activity");
        activityTitle.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        activityTitle.setStyle("-fx-text-fill:" + WHITE + ";");

        activityListPane = new VBox(10);
        rebuildActivityList();

        Button viewAllActivities = new Button("View all activities ›");
        viewAllActivities.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 12));
        viewAllActivities.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-background-color: transparent; -fx-text-fill: #60A5FA; -fx-cursor: hand;");
        viewAllActivities.setOnAction(e -> showAllActivitiesPopup());
        viewAllActivities.setOnMouseEntered(e -> animateTranslate(viewAllActivities, 4, 0));
        viewAllActivities.setOnMouseExited(e -> animateTranslate(viewAllActivities, 0, 0));

        VBox activityCard = new VBox(14, activityTitle, activityListPane, viewAllActivities);
        activityCard.setPadding(new Insets(24));
        activityCard.setMaxWidth(Double.MAX_VALUE);
        activityCard.setStyle(cardContainerStyle());

        SVGPath shieldIcon = createIcon("security");
        shieldIcon.setStroke(Color.web("#38BDF8"));
        shieldIcon.setStrokeWidth(2);

        Label securityBold = new Label("End-to-End Encrypted Sharing:");
        securityBold.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        securityBold.setStyle("-fx-text-fill: " + WHITE + ";");

        Label securityText = new Label(
                "Files in shared spaces are synced peer-to-peer. Original files remain safely stored on your local drive.");
        securityText.setFont(Font.font(FONT, 12));
        securityText.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        HBox securityTextBox = new HBox(6, securityBold, securityText);
        securityTextBox.setAlignment(Pos.CENTER_LEFT);

        HBox security = new HBox(12, shieldIcon, securityTextBox);
        security.setAlignment(Pos.CENTER_LEFT);
        security.setPadding(new Insets(16, 20, 16, 20));
        security.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-radius: 14; -fx-background-radius: 14;");
        applyHoverAnimation(security, 1.01, -1);

        VBox mainContent = new VBox(22, pageHeader, metrics, workspacesBox, activityCard, security);
        mainContent.setPadding(new Insets(24, ResponsiveUtil.PAGE_PADDING, 28, ResponsiveUtil.PAGE_PADDING));
        mainContent.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-background-insets: 0; -fx-padding: 0;");

        VBox mainArea = new VBox(topBar, scrollPane);
        mainArea.setStyle("-fx-background: " + MAIN_BG + "; -fx-background-color: " + MAIN_BG + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        Thread loadThread = new Thread(() -> {
            try {
                initializeWorkspacesAndActivities();
                
                String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";
                int count = 0;
                if (!myEmail.isEmpty()) {
                    com.google.cloud.firestore.Firestore db = com.file_handlers.config.FirebaseConfig.getFirestore();
                    var workspacesDocs = db.collection("workspaces").get().get().getDocuments();
                    for (var wsDoc : workspacesDocs) {
                        var membersDocs = db.collection("workspaces").document(wsDoc.getId()).collection("members").get().get().getDocuments();
                        for (var mDoc : membersDocs) {
                            String email = mDoc.getString("email");
                            String status = mDoc.getString("status");
                            if (email != null && email.equalsIgnoreCase(myEmail) && "pending".equalsIgnoreCase(status)) {
                                count++;
                            }
                        }
                    }
                }
                pendingInvitesCount = count;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Platform.runLater(() -> {
                rebuildWorkspaceCards(root);
                rebuildActivityList();
                updateMetrics();
                updatePendingInvitesButtonText();
            });
        }, "collaboration-page-loader");
        loadThread.setDaemon(true);
        loadThread.start();

        return new Scene(root, LandingPage.getCurrentWidth(), LandingPage.getCurrentHeight());
    }

    private void applyHoverAnimation(Node node, double scaleTo, double translateY) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
            st.setToX(scaleTo);
            st.setToY(scaleTo);
            st.play();

            if (translateY != 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(140), node);
                tt.setToY(translateY);
                tt.play();
            }
        });

        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            if (translateY != 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(140), node);
                tt.setToY(0);
                tt.play();
            }
        });
    }

    private void animateScale(Node node, double scaleTo) {
        ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
        st.setToX(scaleTo);
        st.setToY(scaleTo);
        st.play();
    }

    private void animateTranslate(Node node, double xTo, double yTo) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(130), node);
        tt.setToX(xTo);
        tt.setToY(yTo);
        tt.play();
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
        applyHoverAnimation(logoIcon, 1.1, 0);

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
        manageStorageBtn.setOnMouseEntered(e -> animateTranslate(manageStorageBtn, 4, 0));
        manageStorageBtn.setOnMouseExited(e -> animateTranslate(manageStorageBtn, 0, 0));

        VBox storageCard = new VBox(8, storageTitle, storageValueGroup, storageProgress, manageStorageBtn);
        storageCard.setPadding(new Insets(14));
        storageCard.setStyle("-fx-background-color: rgba(16, 28, 48, 0.65); -fx-border-color: " + SIDEBAR_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");
        applyHoverAnimation(storageCard, 1.01, -1);

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
                button.setStyle("-fx-background-color: rgba(56, 189, 248, 0.12); -fx-background-radius: 12; -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 12; -fx-border-width: 1; -fx-cursor: hand;");
                icon.setStroke(Color.web("#38BDF8"));
                label.setTextFill(Color.web("#38BDF8"));
                animateTranslate(button, 4, 0);
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color: transparent; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-width: 0;");
                icon.setStroke(Color.web(LIGHT_SECONDARY));
                label.setTextFill(Color.web(WHITE));
                animateTranslate(button, 0, 0);
            });
        }

        return button;
    }

    private void rebuildActivityList() {
        activityListPane.getChildren().clear();
        int limit = Math.min(4, activitiesList.size());
        for (int i = 0; i < limit; i++) {
            ActivityItem act = activitiesList.get(i);
            activityListPane.getChildren().add(activity(act.user, act.action, act.time));
        }
    }

    private Label createValueLabel() {
        Label label = new Label();
        label.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        label.setStyle("-fx-text-fill: " + WHITE + ";");
        return label;
    }

    private void updateMetrics() {
        int totalMembers = 0, totalFiles = 0;
        for (WorkspaceData w : workspaces) {
            totalMembers += w.members;
            totalFiles += w.files;
        }

        if (spacesValue != null) spacesValue.setText(workspaces.size() + " Spaces");
        if (membersValue != null) membersValue.setText(totalMembers + " Members");
        if (filesValue != null) filesValue.setText(totalFiles + " Files");
    }

   private void rebuildWorkspaceCards(BorderPane root) {
        workspaceListPane.getChildren().clear();

        if (!isGridView) {
            VBox list = new VBox(10);
            list.setFillWidth(true);

            for (WorkspaceData workspace : workspaces) {
                HBox card = createWorkspaceCard(workspace, root, workspace.docId);
                card.setOnMouseClicked(e -> root.setCenter(
                        new SharedSpacePage(workspace.name).getSharedSpaceContent()));
                list.getChildren().add(card);
            }

            workspaceListPane.getChildren().add(list);
        } else {
            GridPane grid = new GridPane();
            grid.setHgap(12);
            grid.setVgap(12);

            int col = 0, row = 0;

            for (WorkspaceData workspace : workspaces) {
                VBox card = createWorkspaceGridCard(workspace, root, workspace.docId);
                card.setOnMouseClicked(e -> root.setCenter(
                        new SharedSpacePage(workspace.name).getSharedSpaceContent()));

                grid.add(card, col++, row);

                if (col > 1) {
                    col = 0;
                    row++;
                }
            }

            workspaceListPane.getChildren().add(grid);
        }
    }

    private HBox createWorkspaceCard(WorkspaceData w) {
        return createWorkspaceCard(w, null, w.docId);
    }

    private HBox createWorkspaceCard(WorkspaceData w, BorderPane root, String docId) {
        SVGPath icon = createIcon(w.iconType);
        icon.setStroke(Color.web(w.iconColor));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setPrefSize(40, 40); iconPane.setMinSize(40, 40);
        iconPane.setStyle("-fx-background-color: " + w.badgeBg + "; -fx-background-radius: 50%; -fx-border-color: " + w.iconColor + "55; -fx-border-radius: 50%;");

        Label title = new Label(w.name);
        title.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label subtitle = new Label(w.members + " Members    ·    " + w.files + " Files    ·    " + w.storage);
        subtitle.setFont(Font.font(FONT, 11));
        subtitle.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox text = new VBox(3, title, subtitle);

        Label role = new Label(w.role);
        role.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        role.setStyle("-fx-background-color: " + w.badgeBg + "; -fx-text-fill: " + w.badgeText + "; -fx-padding: 4 9; -fx-background-radius: 6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox rightBox = new HBox(8, role);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        if ("Owner".equalsIgnoreCase(w.role) && root != null && docId != null && !docId.isEmpty()) {
            Button renameBtn = new Button();
            SVGPath editIcon = createIcon("edit");
            editIcon.setStroke(Color.web("#38BDF8"));
            editIcon.setStrokeWidth(1.8);
            renameBtn.setGraphic(editIcon);
            renameBtn.setStyle("-fx-background-color: rgba(56, 189, 248, 0.12); -fx-background-radius: 6; -fx-padding: 4 6; -fx-cursor: hand;");

            renameBtn.setOnAction(e -> {
                e.consume();
                promptRenameWorkspace(docId, w.name, root);
            });
            renameBtn.setOnMouseClicked(javafx.event.Event::consume);
            rightBox.getChildren().add(renameBtn);
        }

        if ("Owner".equalsIgnoreCase(w.role) && root != null && docId != null && !docId.isEmpty()) {
            Button deleteBtn = new Button();
            SVGPath trashIcon = createIcon("trash");
            trashIcon.setStroke(Color.web("#F87171"));
            trashIcon.setStrokeWidth(1.8);
            deleteBtn.setGraphic(trashIcon);
            deleteBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.12); -fx-background-radius: 6; -fx-padding: 4 6; -fx-cursor: hand;");

            deleteBtn.setOnAction(e -> {
                e.consume();
                deleteWorkspace(docId, root);
            });
            deleteBtn.setOnMouseClicked(javafx.event.Event::consume);
            rightBox.getChildren().add(deleteBtn);
        } else if (!"Owner".equalsIgnoreCase(w.role) && root != null && docId != null && !docId.isEmpty()) {
            Button leaveBtn = new Button("Leave");
            leaveBtn.setFont(Font.font(FONT, FontWeight.BOLD, 10));
            leaveBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.12); -fx-text-fill: #F87171; -fx-background-radius: 6; -fx-padding: 5 10; -fx-cursor: hand;");

            leaveBtn.setOnAction(e -> {
                e.consume();
                leaveWorkspace(docId, root);
            });
            leaveBtn.setOnMouseClicked(javafx.event.Event::consume);
            rightBox.getChildren().add(leaveBtn);
        }

        HBox card = new HBox(12, iconPane, text, spacer, rightBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14));
        applyHover(card);

        return card;
    }
    

    private VBox createWorkspaceGridCard(WorkspaceData w, BorderPane root, String docId) {
        SVGPath icon = createIcon(w.iconType);
        icon.setStroke(Color.web(w.iconColor));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setPrefSize(42, 42); iconPane.setMinSize(42, 42);
        iconPane.setStyle("-fx-background-color: " + w.badgeBg + "; -fx-background-radius: 50%; -fx-border-color: " + w.iconColor + "55; -fx-border-radius: 50%;");

        Label roleTag = new Label(w.role);
        roleTag.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        roleTag.setStyle("-fx-text-fill: " + w.badgeText + "; -fx-background-color: " + w.badgeBg + "; -fx-background-radius: 6; -fx-padding: 3 10;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topRight = new HBox(6, roleTag);
        topRight.setAlignment(Pos.CENTER_RIGHT);

        if ("Owner".equalsIgnoreCase(w.role) && root != null && docId != null && !docId.isEmpty()) {
            Button renameBtn = new Button();
            SVGPath editIcon = createIcon("edit");            
            editIcon.setStroke(Color.web("#38BDF8"));
            editIcon.setStrokeWidth(1.8);
            renameBtn.setGraphic(editIcon);
            renameBtn.setStyle("-fx-background-color: rgba(56, 189, 248, 0.12); -fx-background-radius: 6; -fx-padding: 4 6; -fx-cursor: hand;");
            renameBtn.setOnAction(e -> {
                e.consume();
                promptRenameWorkspace(docId, w.name, root);
            });
            renameBtn.setOnMouseClicked(javafx.event.Event::consume);
            topRight.getChildren().add(renameBtn);

            Button deleteBtn = new Button();
            SVGPath trashIcon = createIcon("trash");
            trashIcon.setStroke(Color.web("#F87171"));
            trashIcon.setStrokeWidth(1.8);
            deleteBtn.setGraphic(trashIcon);
            deleteBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.12); -fx-background-radius: 6; -fx-padding: 4 6; -fx-cursor: hand;");
            deleteBtn.setOnAction(e -> {
                e.consume();
                deleteWorkspace(docId, root);
            });
            deleteBtn.setOnMouseClicked(javafx.event.Event::consume);
            topRight.getChildren().add(deleteBtn);
        } else if (!"Owner".equalsIgnoreCase(w.role) && root != null && docId != null && !docId.isEmpty()) {
            Button leaveBtn = new Button("Leave");
            leaveBtn.setFont(Font.font(FONT, FontWeight.BOLD, 10));
            leaveBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.12); -fx-text-fill: #F87171; -fx-background-radius: 6; -fx-padding: 4 8; -fx-cursor: hand;");
            leaveBtn.setOnAction(e -> {
                e.consume();
                leaveWorkspace(docId, root);
            });
            leaveBtn.setOnMouseClicked(javafx.event.Event::consume);
            topRight.getChildren().add(leaveBtn);
        }

        HBox top = new HBox(iconPane, spacer, topRight);
        top.setAlignment(Pos.CENTER);

        Label title = new Label(w.name);
        title.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        title.setStyle("-fx-text-fill: " + WHITE + ";");

        Label subtitle = new Label(w.members + " Members    ·    " + w.files + " Files\nStorage: " + w.storage);
        subtitle.setFont(Font.font(FONT, 11));
        subtitle.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox card = new VBox(10, top, title, subtitle);
        card.setPadding(new Insets(16));
        card.setPrefWidth(280);
        card.setMaxWidth(Double.MAX_VALUE);
        applyHover(card);

        return card;
    }

    private void deleteWorkspace(String docId, BorderPane root) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Workspace");
        confirmDialog.setHeaderText("Are you sure you want to delete this shared space?");
        confirmDialog.setContentText("This action cannot be undone and will remove access for all members.");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                workspaces.removeIf(w -> w.docId != null && w.docId.equals(docId));
                updateMetrics();
                rebuildActivityList();
                if (root != null) {
                    rebuildWorkspaceCards(root);
                }

                new Thread(() -> {
                    try {
                        new WorkspaceDAO().deleteWorkspace(docId, () -> {}, ex -> ex.printStackTrace());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        });
    }

    private void promptRenameWorkspace(String docId, String currentName, BorderPane root) {
    TextInputDialog dialog = new TextInputDialog(currentName);
    dialog.setTitle("Rename Workspace");
    dialog.setHeaderText("Enter a new name for this shared space:");
    dialog.setContentText("Workspace Name:");

    dialog.showAndWait().ifPresent(newName -> {
        String trimmed = newName.trim();
        if (!trimmed.isEmpty() && !trimmed.equals(currentName)) {
            // Directly perform the Firestore update asynchronously
            new Thread(() -> {
                try {
                    com.google.cloud.firestore.Firestore db = com.file_handlers.config.FirebaseConfig.getFirestore();
                    java.util.Map<String, Object> updates = new java.util.HashMap<>();
                    updates.put("spaceName", trimmed);
                    updates.put("name", trimmed);

                    // Update the workspace document in Firestore
                    db.collection("workspaces").document(docId).update(updates).get();

                    Platform.runLater(() -> {
                        for (WorkspaceData w : workspaces) {
                            if (w.docId.equals(docId)) {
                                w.name = trimmed;
                                break;
                            }
                        }
                        rebuildWorkspaceCards(root);
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    });
}

    private void leaveWorkspace(String docId, BorderPane root) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Leave Workspace");
        confirmDialog.setHeaderText("Are you sure you want to leave this shared space?");
        confirmDialog.setContentText("You will lose access to its shared files and collaboration history.");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";
                new Thread(() -> {
                    try {
                        var db = com.file_handlers.config.FirebaseConfig.getFirestore();
                        var membersDocs = db.collection("workspaces").document(docId).collection("members").get().get().getDocuments();
                        for (var mDoc : membersDocs) {
                            String email = mDoc.getString("email");
                            if (email != null && email.equalsIgnoreCase(myEmail)) {
                                mDoc.getReference().delete();
                                break;
                            }
                        }
                        Platform.runLater(() -> {
                            workspaces.removeIf(w -> w.docId != null && w.docId.equals(docId));
                            updateMetrics();
                            rebuildActivityList();
                            rebuildWorkspaceCards(root);
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        });
    }

    private void applyHover(Region card) {
        String normal = "-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;";
        String hover = "-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: #38BDF8;" +
                "-fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 14, 0, 0, 4);";

        card.setStyle(normal);
        card.setOnMouseEntered(e -> {
            card.setStyle(hover);
            TranslateTransition tt = new TranslateTransition(Duration.millis(130), card);
            tt.setToY(-3);
            tt.play();
        });
        card.setOnMouseExited(e -> {
            card.setStyle(normal);
            TranslateTransition tt = new TranslateTransition(Duration.millis(130), card);
            tt.setToY(0);
            tt.play();
        });
    }

    private void showAllWorkspacesPopup(BorderPane root) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("All Shared Workspaces");
        dialog.setHeaderText("Shared Workspaces (" + workspaces.size() + ")");

        VBox list = new VBox(10);
        list.setPadding(new Insets(10));

        for (WorkspaceData w : workspaces) {
            HBox card = createWorkspaceCard(w, root, w.docId);
            card.setMaxWidth(Double.MAX_VALUE);
            card.setOnMouseClicked(e -> {
                dialog.close();
                root.setCenter(new SharedSpacePage(w.name).getSharedSpaceContent());
            });
            list.getChildren().add(card);
        }

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(430);
        scroll.setPrefWidth(620);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(close);
        dialog.getDialogPane().setContent(padded(scroll, 5));
        styleDialog(dialog, 660, 520);
        dialog.showAndWait();
    }

    private void showPendingRequestsPopup() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Pending Invites");
        dialog.setHeaderText("Collaboration Invites");

        VBox list = new VBox(12);
        list.setPadding(new Insets(10));

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(400);
        scroll.setPrefWidth(520);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        String myEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "";
        try {
            com.google.cloud.firestore.Firestore db = com.file_handlers.config.FirebaseConfig.getFirestore();
            List<com.google.cloud.firestore.QueryDocumentSnapshot> workspacesDocs = db.collection("workspaces").get().get().getDocuments();
            
            boolean foundAny = false;
            for (com.google.cloud.firestore.DocumentSnapshot wsDoc : workspacesDocs) {
                String spaceDocId = wsDoc.getId();
                String spaceName = wsDoc.getString("spaceName");
                if (spaceName == null) {
                    spaceName = wsDoc.getString("name");
                }
                if (spaceName == null) {
                    spaceName = spaceDocId.replaceAll("_", " ");
                }
                
                var membersDocs = db.collection("workspaces").document(spaceDocId).collection("members").get().get().getDocuments();
                
                String ownerName = "Workspace Owner";
                for (var mDoc : membersDocs) {
                    if ("Owner".equalsIgnoreCase(mDoc.getString("role"))) {
                        String oName = mDoc.getString("name");
                        if (oName != null) {
                            ownerName = oName;
                        }
                    }
                }

                for (var mDoc : membersDocs) {
                    String email = mDoc.getString("email");
                    String status = mDoc.getString("status");

                if (email != null && email.equalsIgnoreCase(myEmail) && ("pending".equalsIgnoreCase(status) || "declined".equalsIgnoreCase(status))) {
                foundAny = true;

                        String name = mDoc.getString("name");
                        if (name == null) name = "Unknown";

                        final String finalName = name;
                        final String finalEmail = email;
                        final String finalOwner = ownerName;
                        final String finalSpaceName = spaceName;
                        
                        javafx.application.Platform.runLater(() -> {
                            list.getChildren().add(pendingRequest(finalName, finalEmail, finalSpaceName, finalOwner, spaceDocId));
                        });
                    }
                }
            }

            if (!foundAny) {
                javafx.application.Platform.runLater(() -> {
                    Label noInvites = new Label("No pending collaboration invites found.");
                    noInvites.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + "; -fx-font-size: 12px;");
                    list.getChildren().add(noInvites);
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(close);
        dialog.getDialogPane().setContent(padded(scroll, 5));
        styleDialog(dialog, 560, 500);
        dialog.showAndWait();
    }

    private HBox pendingRequest(String name, String email, String space, String invitedBy, String spaceDocId) {
        Label avatar = new Label(getInitials(name));
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        avatar.setPrefSize(38, 38);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-background-color: rgba(37, 99, 235, 0.2); -fx-background-radius: 50%; -fx-text-fill: #60A5FA;");

        Label nameLbl = new Label(name);
        nameLbl.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        nameLbl.setStyle("-fx-text-fill: " + WHITE + ";");

        Label emailLbl = new Label(email);
        emailLbl.setFont(Font.font(FONT, 10));
        emailLbl.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        Label spaceLbl = new Label("Invited to: " + space + "  ·  Invited by: " + invitedBy);
        spaceLbl.setFont(Font.font(FONT, 11));
        spaceLbl.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox info = new VBox(2, nameLbl, emailLbl, spaceLbl);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button accept = new Button("Accept");
        accept.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        applyHoverAnimation(accept, 1.06, 0);

        Button decline = new Button("Decline");
        decline.setStyle("-fx-background-color: rgba(239, 68, 68, 0.15); -fx-text-fill: #F87171; -fx-border-color: rgba(239, 68, 68, 0.4);" +
                "-fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold; -fx-cursor: hand;");
        applyHoverAnimation(decline, 1.06, 0);

        HBox buttons = new HBox(6, accept, decline);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(10, avatar, info, spacer, buttons);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setStyle("-fx-background-color: " + CARD_BG_INNER + "; -fx-border-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-radius: 10; -fx-background-radius: 10;");

         accept.setOnAction(e -> {
            try {
                var db = com.file_handlers.config.FirebaseConfig.getFirestore();
                var docs = db.collection("workspaces").document(spaceDocId).collection("members").get().get().getDocuments();
                for (var doc : docs) {
                    if (email.equalsIgnoreCase(doc.getString("email"))) {
                        doc.getReference().update("status", "active");
                        break;
                    }
                }
            } catch (Exception ex) { ex.printStackTrace(); }

            nameLbl.setText(name + " ✓ Accepted");
            accept.setDisable(true);
            decline.setDisable(true);

            pendingInvitesCount = Math.max(0, pendingInvitesCount - 1);
            updatePendingInvitesButtonText();

            javafx.application.Platform.runLater(() -> LandingPage.showCollaborationPage());
        });

        decline.setOnAction(e -> {
            try {
                var db = com.file_handlers.config.FirebaseConfig.getFirestore();
                var docs = db.collection("workspaces").document(spaceDocId).collection("members").get().get().getDocuments();
                for (var doc : docs) {
                    if (email.equalsIgnoreCase(doc.getString("email"))) {
                        doc.getReference().update("status", "declined");
                        break;
                    }
                }
            } catch (Exception ex) { 
                ex.printStackTrace(); 
            }

            nameLbl.setText(name + " (Declined)");
            nameLbl.setStyle("-fx-text-fill: #991B1B; -fx-font-weight: bold;");
            
            accept.setDisable(false);
            decline.setDisable(true);

            pendingInvitesCount = Math.max(0, pendingInvitesCount - 1);
            updatePendingInvitesButtonText();
        });

        return row;
    }

    private String getInitials(String name) {
        String[] parts = name.trim().split(" ");
        if (parts.length >= 2)
            return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    private HBox createStatCard(String iconType, Label value, String description,
                                String iconColor, String iconBackground) {
        SVGPath icon = createIcon(iconType);
        icon.setStroke(Color.web(iconColor));
        icon.setStrokeWidth(2);

        StackPane iconPane = new StackPane(icon);
        iconPane.setPrefSize(34, 34); iconPane.setMinSize(34, 34);
        iconPane.setStyle("-fx-background-color: " + iconBackground + "; -fx-background-radius: 8; -fx-border-color: " + iconColor + "44; -fx-border-radius: 8;");

        Label desc = new Label(description);
        desc.setFont(Font.font(FONT, 11));
        desc.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + "; -fx-font-weight: 600;");

        VBox text = new VBox(2, value, desc);
        HBox card = new HBox(12, iconPane, text);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16));
        card.setStyle(cardContainerStyle());
        HBox.setHgrow(card, Priority.ALWAYS);

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-border-color: #38BDF8; -fx-border-width: 1.2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(56,189,248,0.35), 20, 0, 0, 6);");
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(-4);
            tt.play();
        });
        card.setOnMouseExited(e -> {
            card.setStyle(cardContainerStyle());
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), card);
            tt.setToY(0);
            tt.play();
        });

        return card;
    }

    private HBox activity(String user, String action, String time) {
        Label dot = new Label("•");
        dot.setFont(Font.font(FONT, 16));
        dot.setStyle("-fx-text-fill: #38BDF8;");

        Label userLbl = new Label(user + " ");
        userLbl.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        userLbl.setStyle("-fx-text-fill: " + WHITE + ";");

        Label actionLbl = new Label(action);
        actionLbl.setFont(Font.font(FONT, 12));
        actionLbl.setStyle("-fx-text-fill: " + WHITE + ";");

        HBox text = new HBox(userLbl, actionLbl);

        Label timeLbl = new Label(time);
        timeLbl.setFont(Font.font(FONT, 10));
        timeLbl.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        VBox content = new VBox(2, text, timeLbl);
        HBox row = new HBox(8, dot, content);
        row.setPadding(new Insets(4, 6, 4, 6));
        row.setStyle("-fx-background-color: transparent; -fx-background-radius: 6;");

        row.setOnMouseEntered(e -> {
            row.setStyle("-fx-background-color: rgba(56, 189, 248, 0.08); -fx-background-radius: 6;");
            animateTranslate(row, 4, 0);
        });
        row.setOnMouseExited(e -> {
            row.setStyle("-fx-background-color: transparent; -fx-background-radius: 6;");
            animateTranslate(row, 0, 0);
        });

        return row;
    }

    private void showAllActivitiesPopup() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("All Activities");
        dialog.setHeaderText("Recent Activity");

        VBox list = new VBox(12);
        list.setPadding(new Insets(10));

        for (ActivityItem act : activitiesList) {
            list.getChildren().add(activity(act.user, act.action, act.time));
        }

        ScrollPane scroll = new ScrollPane(list);
        
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(430);
        scroll.setPrefWidth(500);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(close);
        dialog.getDialogPane().setContent(padded(scroll, 5));
        styleDialog(dialog, 540, 520);
        dialog.showAndWait();
    }

    private void showCreateSharedSpacePopup(BorderPane root) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create New Shared Space");
        dialog.setHeaderText(null);

        Label nameLabel = formLabel("1. Space name");
        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Final Year Project");
        nameField.setPrefHeight(42);
        nameField.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-prompt-text-fill: " + LIGHT_SECONDARY + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label membersLabel = formLabel("2. Add members");
        
        VBox membersContainer = new VBox(8);
        List<TextField> memberEmailFields = new ArrayList<>();

        Runnable addMemberRow = () -> {
            HBox rowBox = new HBox(8);
            rowBox.setAlignment(Pos.CENTER_LEFT);
            
            TextField emailField = new TextField();
            emailField.setPromptText("member@example.com");
            emailField.setPrefHeight(38);
            HBox.setHgrow(emailField, Priority.ALWAYS);
            emailField.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE + "; -fx-prompt-text-fill: " + LIGHT_SECONDARY + "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 7; -fx-background-radius: 7;");
            memberEmailFields.add(emailField);

            Button removeRowBtn = new Button("×");
            removeRowBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.15); -fx-text-fill: #F87171; -fx-font-weight: bold; -fx-background-radius: 7; -fx-cursor: hand; -fx-padding: 6 12;");
            removeRowBtn.setOnAction(ev -> {
                membersContainer.getChildren().remove(rowBox);
                memberEmailFields.remove(emailField);
            });

            rowBox.getChildren().addAll(emailField, removeRowBtn);
            membersContainer.getChildren().add(rowBox);
        };

        Button addMemberRowBtn = new Button("+ Add member");
        addMemberRowBtn.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 11));
        addMemberRowBtn.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: #38BDF8; -fx-border-color: #38BDF8; -fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand; -fx-padding: 6 12;");
        addMemberRowBtn.setOnAction(ev -> addMemberRow.run());

        addMemberRow.run();

        VBox membersSection = new VBox(6, membersContainer, addMemberRowBtn);

        Label uploadLabel = formLabel("3. Upload files");

        Label fileName = new Label("Choose files or drag and drop");
        fileName.setFont(Font.font(FONT, 12));
        fileName.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        Button browse = new Button("Browse");
        browse.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE +
                "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand;");
        applyHoverAnimation(browse, 1.05, 0);

        Region uploadSpacer = new Region();
        HBox.setHgrow(uploadSpacer, Priority.ALWAYS);

        HBox uploadBox = new HBox(10, fileName, uploadSpacer, browse);
        uploadBox.setAlignment(Pos.CENTER_LEFT);
        uploadBox.setPadding(new Insets(0, 10, 0, 10));
        uploadBox.setPrefHeight(42);
        uploadBox.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-border-color: " +
                INPUT_BORDER + "; -fx-border-radius: 7; -fx-background-radius: 7;");

        final List<File> selectedFiles = new ArrayList<>();

        browse.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Files for Collaboration");
            List<File> files = chooser.showOpenMultipleDialog(dialog.getDialogPane().getScene().getWindow());

            if (files != null && !files.isEmpty()) {
                selectedFiles.clear();
                selectedFiles.addAll(files);
                if (files.size() == 1) {
                    fileName.setText(files.get(0).getName());
                } else {
                    String names = files.stream().map(File::getName).collect(java.util.stream.Collectors.joining(", "));
                    fileName.setText(files.size() + " files selected: " + names);
                }
            }
        });

        Label progressLabel = new Label("");
        progressLabel.setFont(Font.font(FONT, 11));
        progressLabel.setStyle("-fx-text-fill: #38BDF8;");

        Label info = new Label("Upload files to initialize your shared space.");
        info.setFont(Font.font(FONT, 11));
        info.setStyle("-fx-text-fill: " + LIGHT_SECONDARY + ";");

        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType create = new ButtonType("＋ Create Space", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(cancel, create);

        VBox content = new VBox(10, nameLabel, nameField, membersLabel, membersSection,
                uploadLabel, uploadBox, progressLabel, info);
        content.setPadding(new Insets(10));
        content.setPrefWidth(400);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(480);
        dialog.getDialogPane().setStyle("-fx-background-color: #0A121E; -fx-border-color: " +
                CARD_BORDER + "; -fx-border-radius: 12; -fx-background-radius: 12;");

        Button createBtn = (Button) dialog.getDialogPane().lookupButton(create);
        createBtn.setStyle("-fx-background-color: linear-gradient(to right, #1D4ED8, #2563EB); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7; -fx-cursor: hand;");
        applyHoverAnimation(createBtn, 1.04, 0);

        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(cancel);
        cancelBtn.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + WHITE +
                "; -fx-border-color: " + INPUT_BORDER + "; -fx-border-radius: 7; -fx-background-radius: 7;" +
                "-fx-font-weight: bold; -fx-cursor: hand;");
        applyHoverAnimation(cancelBtn, 1.04, 0);

        createBtn.setOnAction(ev -> {
            String spaceName = nameField.getText().trim();
            if (spaceName.isEmpty()) {
                return;
            }

            List<String> memberEmails = new ArrayList<>();
            for (TextField tf : memberEmailFields) {
                String val = tf.getText().trim();
                if (!val.isEmpty()) {
                    memberEmails.add(val);
                }
            }

            String ownerEmail = UserSession.getInstance() != null ? UserSession.getInstance().getEmail() : "owner@app.com";
            String ownerName = ownerEmail.split("@")[0];
            String ownerId = ownerEmail.toLowerCase().replaceAll("[^a-z0-9]", "_");

            createBtn.setDisable(true);
            progressLabel.setText("Initializing workspace...");

            new WorkspaceController().createWorkspaceWithMembersAndFiles(
                spaceName, ownerId, ownerName, ownerEmail,
                memberEmails, selectedFiles,
                (done, total) -> Platform.runLater(() -> progressLabel.setText("Uploading files: " + done + "/" + total)),
                workspaceDocId -> Platform.runLater(() -> {
                    dialog.close();

                    String newDocId = workspaceDocId != null && !workspaceDocId.isEmpty() ? workspaceDocId : spaceName.replaceAll("\\s+", "_");
                    workspaces.add(0, new WorkspaceData(
                        "files",
                        "#38BDF8",
                        spaceName,
                        memberEmails.size() + 1,
                        selectedFiles.size(),
                        selectedFiles.size() > 0 ? "Synced" : "No files",
                        "Owner",
                        "rgba(37, 99, 235, 0.2)",
                        "#60A5FA",
                        ownerEmail,
                        newDocId
                    ));

                    rebuildWorkspaceCards(root);
                    updateMetrics();
                    rebuildActivityList();
                }),
                ex -> Platform.runLater(() -> {
                    ex.printStackTrace();
                    createBtn.setDisable(false);
                    progressLabel.setText("Error: " + ex.getMessage());
                })
            );
        });

        dialog.showAndWait();
    }

    private Label formLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        label.setStyle("-fx-text-fill: " + WHITE + ";");
        return label;
    }

    private VBox padded(Node node, double padding) {
        VBox box = new VBox(10, node);
        box.setPadding(new Insets(padding));
        return box;
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
            case "edit": icon.setContent("M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7 M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"); break;
            default: icon.setContent("M4 4 H20 V20 H4 Z"); break;
        }
        return icon;
    }
}