package com.file_handlers.view.userView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import javafx.stage.FileChooser;

public class SharedSpacePage {

    private static final String FONT = "Inter";
    private static final String BG_APP = "#3A4D67";
    private static final String BG_CARD = "#DDE8F5";
    private static final String BG_CARD_INNER = "#D1E1F1";
    private static final String BG_INPUT = "#EDF3FA";
    private static final String BG_SIDEBAR_CARD = "#2E3F55";
    private static final String BORDER_COLOR = "#C9DAEE";
    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String PRIMARY_LIGHT_BLUE = "#BFDBFE";
    private static final String TEXT_DARK = "#142338";
    private static final String TEXT_MUTED_DARK = "#506580";
    private static final String TEXT_LIGHT = "#FFFFFF";
    private static final String TEXT_MUTED_LIGHT = "#9EB0C6";
    private static final String SUCCESS = "#16A34A";
    private static final String SUCCESS_LIGHT = "#DCFCE7";
    private static final String ORANGE = "#EA580C";
    private static final String ORANGE_LIGHT = "#FFEDD5";
    private static final String RED = "#DC2626";
    private static final String RED_LIGHT = "#FEE2E2";

    private static final int MAX_VISIBLE_FILES = 3;
    private static final int MAX_VISIBLE_MEMBERS = 3;

    private String spaceName;
    private final List<MemberData> membersList = new ArrayList<>();
    private final List<FileData> filesList = new ArrayList<>();

    private VBox memberListBox;
    private VBox fileListBox;
    private TextField memberSearchField;
    private TextField fileSearchField;
    private Label memberCountLabel;
    private Label fileCountLabel;

    private String currentUserRole = "Owner";

    private final String ownerName = "Aarav Verma";
    private final String ownerEmail = "aarav.verma@email.com";
    private final String ownerRole = "Owner";
    private final String createdDate = "13 Aug 2026";

    public SharedSpacePage() {
        this("Shared Space");
    }

    public SharedSpacePage(String spaceName) {
        this.spaceName =
                spaceName == null || spaceName.trim().isEmpty()
                        ? "Shared Space"
                        : spaceName.trim();
        loadDefaultData();
    }

    private void loadDefaultData() {
        membersList.clear();
        filesList.clear();

        membersList.add(new MemberData(
                "AV", "Aarav Verma", "aarav.verma@email.com",
                "Owner", PRIMARY_LIGHT_BLUE, PRIMARY_BLUE));

        membersList.add(new MemberData(
                "PS", "Priya Sharma", "priya.sharma@email.com",
                "Editor", PRIMARY_LIGHT_BLUE, PRIMARY_BLUE));

        membersList.add(new MemberData(
                "RP", "Rohan Patel", "rohan.patel@email.com",
                "Viewer", SUCCESS_LIGHT, SUCCESS));

        membersList.add(new MemberData(
                "NK", "Neha Kulkarni", "neha.kulkarni@email.com",
                "Viewer", ORANGE_LIGHT, ORANGE));

        membersList.add(new MemberData(
                "SK", "Sahil Kumar", "sahil.kumar@email.com",
                "Editor", PRIMARY_LIGHT_BLUE, PRIMARY_BLUE));

        filesList.add(new FileData(
                "PDF", "Java_Project.pdf", "2.4 MB",
                "13 Aug 2026 10:30 AM", RED));

        filesList.add(new FileData(
                "W", "Notes.docx", "1.1 MB",
                "13 Aug 2026 09:45 AM", PRIMARY_BLUE));

        filesList.add(new FileData(
                "P", "Presentation.pptx", "3.2 MB",
                "12 Aug 2026 04:20 PM", ORANGE));

        filesList.add(new FileData(
                "X", "Data.xlsx", "850 KB",
                "12 Aug 2026 11:15 AM", SUCCESS));

        filesList.add(new FileData(
                "TXT", "Readme.txt", "420 B",
                "11 Aug 2026 05:10 PM", "#6366F1"));

        filesList.add(new FileData(
                "PDF", "Project_Report.pdf", "4.8 MB",
                "10 Aug 2026 03:40 PM", RED));
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
                        || permission.equals("EDIT_FILE")
                        || permission.equals("DELETE_FILE");
            case "Viewer":
                return permission.equals("VIEW")
                        || permission.equals("SEARCH")
                        || permission.equals("DOWNLOAD");
            default:
                return false;
        }
    }

    private boolean currentUserCan(String permission) {
        return hasPermission(currentUserRole, permission);
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

        VBox owner = createSummaryItem(
                "♙", "Owner", ownerName,
                PRIMARY_LIGHT_BLUE, PRIMARY_BLUE);

        owner.setCursor(Cursor.HAND);
        owner.setOnMouseClicked(e -> showOwnerDetailsPopup());

        VBox members = createSummaryItem(
                "♧", "Members",
                membersList.size() + " Members",
                PRIMARY_LIGHT_BLUE, PRIMARY_BLUE);

        memberCountLabel =
                (Label) members.getProperties().get("valueLabel");

        VBox files = createSummaryItem(
                "▱", "Files",
                filesList.size() + " Files",
                SUCCESS_LIGHT, SUCCESS);

        fileCountLabel =
                (Label) files.getProperties().get("valueLabel");

        VBox created = createSummaryItem(
                "▣", "Created On", createdDate,
                ORANGE_LIGHT, ORANGE);

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

    private void showOwnerDetailsPopup() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Owner Details");
        dialog.setHeaderText("Owner Details");
        dialog.getDialogPane().setPrefWidth(420);

        Label nameHeading = createPopupHeading("Name");
        Label nameValue = createPopupValue(ownerName);

        Label emailHeading = createPopupHeading("Email");
        Label emailValue = createPopupValue(ownerEmail);

        Label roleHeading = createPopupHeading("Role");

        Label roleValue = new Label(ownerRole);
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

    private Label createPopupHeading(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        label.setTextFill(Color.web(TEXT_MUTED_DARK));
        return label;
    }

    private Label createPopupValue(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        label.setTextFill(Color.web(TEXT_DARK));
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
        title.setTextFill(Color.web(TEXT_DARK));

        Label subtitle = new Label(
                "Files uploaded to this shared space");
        subtitle.setFont(Font.font(FONT, 11));
        subtitle.setTextFill(Color.web(TEXT_MUTED_DARK));

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
                showAccessDeniedPopup(
                        "You do not have permission to upload files.");
                return;
            }

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Upload File");

            File file = chooser.showOpenDialog(
                    upload.getScene().getWindow());

            if (file != null) addUploadedFile(file);
        });

        HBox titleRow = new HBox(
                10, titleBox, spacer, upload);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        fileSearchField = createSearchField("⌕  Search files...");
        fileSearchField.textProperty().addListener(
                (obs, oldValue, newValue) -> refreshFileList());

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

        tableHeader.getChildren().addAll(
                name, size, uploaded, more);

        fileListBox = new VBox(0);
        refreshFileList();

        Button viewAll = createViewAllButton("View All Files");
        viewAll.setOnAction(e -> showAllFilesPopup());

        card.getChildren().addAll(
                titleRow,
                fileSearchField,
                tableHeader,
                fileListBox,
                viewAll);

        VBox.setVgrow(fileListBox, Priority.ALWAYS);

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
        title.setTextFill(Color.web(TEXT_DARK));

        Button manage = new Button("♜  Manage Access");
        manage.setPrefHeight(36);
        manage.setPrefWidth(140);
        manage.setAlignment(Pos.CENTER);
        manage.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        manage.setTextFill(Color.WHITE);
        manage.setStyle(
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-background-radius:7;" +
                "-fx-border-color:" + PRIMARY_BLUE + ";" +
                "-fx-border-radius:7;" +
                "-fx-cursor:hand;");

        updateManageAccessPermission(manage);

        manage.setOnAction(e -> {
            if (!currentUserCan("MANAGE_ACCESS")) {
                showAccessDeniedPopup(
                        "Only the Owner can manage workspace access.");
                return;
            }
            showManageAccessPopup();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox titleRow = new HBox(
                10, title, spacer, manage);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        memberSearchField = createSearchField(
                "⌕  Search members...");

        memberSearchField.textProperty().addListener(
                (obs, oldValue, newValue) -> refreshMemberList());

        memberListBox = new VBox(0);
        refreshMemberList();

        Button viewAll = createViewAllButton(
                "View All Members");
        viewAll.setOnAction(e -> showAllMembersPopup());

        Button addMember = new Button(
                "♙  Add Member       ▼");

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
                showAccessDeniedPopup(
                        "Only the Owner can add members.");
                return;
            }
            showAddMemberPopup();
        });

        card.getChildren().addAll(
                titleRow,
                memberSearchField,
                memberListBox,
                viewAll,
                addMember);

        VBox.setVgrow(memberListBox, Priority.ALWAYS);

        return card;
    }

    private void updateManageAccessPermission(Button button) {
        boolean allowed = currentUserCan("MANAGE_ACCESS");
        button.setDisable(!allowed);
        button.setOpacity(allowed ? 1.0 : 0.55);
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
        label.setTextFill(Color.web(TEXT_DARK));
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

        for (FileData file : filesList) {
            boolean matches = searchText.isEmpty()
                    || file.fileName.toLowerCase().contains(searchText);

            if (matches && count < MAX_VISIBLE_FILES) {
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
            empty.setTextFill(Color.web(TEXT_MUTED_DARK));
            empty.setPadding(new Insets(15));

            fileListBox.getChildren().add(empty);
        }
    }

    private HBox createFileRow(FileData file) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMinHeight(58);
        row.setPadding(new Insets(7, 10, 7, 10));
        row.setStyle(
                "-fx-border-color:transparent transparent " +
                BORDER_COLOR + " transparent;");

        Label icon = new Label(file.icon);
        icon.setFont(Font.font(FONT, FontWeight.BOLD, 8));
        icon.setTextFill(Color.WHITE);
        icon.setAlignment(Pos.CENTER);
        icon.setPrefSize(30, 34);
        icon.setStyle(
                "-fx-background-color:" + file.iconColor + ";" +
                "-fx-background-radius:4;");

        Label name = new Label(file.fileName);
        name.setFont(Font.font(FONT, 13));
        name.setTextFill(Color.web(TEXT_DARK));

        HBox nameBox = new HBox(12, icon, name);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameBox.setPrefWidth(260);
        HBox.setHgrow(nameBox, Priority.ALWAYS);

        Label size = new Label(file.size);
        size.setFont(Font.font(FONT, 12));
        size.setTextFill(Color.web(TEXT_MUTED_DARK));
        size.setPrefWidth(110);

        Label date = new Label(file.uploadedOn);
        date.setFont(Font.font(FONT, 12));
        date.setTextFill(Color.web(TEXT_MUTED_DARK));
        date.setPrefWidth(180);

        Button more = new Button("⋮");
        more.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        more.setTextFill(Color.web(TEXT_MUTED_DARK));
        more.setPrefWidth(30);
        more.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;" +
                "-fx-cursor:hand;");

        ContextMenu menu = new ContextMenu();

        MenuItem view = new MenuItem("View File");
        view.setOnAction(e -> {
            if (!currentUserCan("VIEW")) {
                showAccessDeniedPopup(
                        "You do not have permission to view this file.");
                return;
            }

            showInfoPopup(
                    "View File",
                    "Selected File",
                    file.fileName);
        });

        MenuItem download = new MenuItem("Download File");
        download.setOnAction(e -> {
            if (!currentUserCan("DOWNLOAD")) {
                showAccessDeniedPopup(
                        "You do not have permission to download files.");
                return;
            }

            showInfoPopup(
                    "Download File",
                    "File",
                    file.fileName +
                    "\n\nDownload functionality can be connected later.");
        });

        MenuItem edit = new MenuItem("Edit / Update File");
        edit.setOnAction(e -> {
            if (!currentUserCan("EDIT_FILE")) {
                showAccessDeniedPopup(
                        currentUserRole +
                        " cannot edit or update files.");
                return;
            }

            showInfoPopup(
                    "Edit File",
                    "Editing",
                    file.fileName +
                    "\n\nEdit functionality can be connected later.");
        });

        MenuItem delete = new MenuItem("Delete File");
        delete.setOnAction(e -> {
            if (!currentUserCan("DELETE_FILE")) {
                showAccessDeniedPopup(
                        currentUserRole +
                        " cannot delete files.");
                return;
            }

            filesList.remove(file);
            refreshFileList();
            updateFileCount();
        });

        menu.getItems().addAll(view, download, edit, delete);

        more.setOnAction(e ->
                menu.show(
                        more,
                        javafx.geometry.Side.BOTTOM,
                        0,
                        0));

        row.getChildren().addAll(
                nameBox,
                size,
                date,
                more);

        return row;
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
        label.setTextFill(Color.web(TEXT_DARK));
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

        for (MemberData member : membersList) {
            boolean matches = searchText.isEmpty()
                    || member.name.toLowerCase().contains(searchText)
                    || member.email.toLowerCase().contains(searchText);

            if (matches && count < MAX_VISIBLE_MEMBERS) {
                memberListBox.getChildren().add(
                        createMemberRow(member));
                count++;
            }
        }

        if (count == 0) {
            Label empty = new Label(
                    searchText.isEmpty()
                            ? "No members added yet."
                            : "No matching members found.");

            empty.setFont(Font.font(FONT, 11));
            empty.setTextFill(Color.web(TEXT_MUTED_DARK));
            empty.setPadding(new Insets(12, 0, 12, 0));

            memberListBox.getChildren().add(empty);
        }
    }

    private HBox createMemberRow(MemberData member) {
        HBox row = new HBox(9);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(9, 0, 9, 0));
        row.setStyle(
                "-fx-border-color:transparent transparent " +
                BORDER_COLOR + " transparent;");

        Label avatar = new Label(member.initials);
        avatar.setFont(Font.font(FONT, FontWeight.BOLD, 11));
        avatar.setTextFill(Color.web(member.avatarColor));
        avatar.setAlignment(Pos.CENTER);
        avatar.setPrefSize(36, 36);
        avatar.setStyle(
                "-fx-background-color:" +
                member.avatarBackground + ";" +
                "-fx-background-radius:50%;");

        Label name = new Label(member.name);
        name.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        name.setTextFill(Color.web(TEXT_DARK));

        Label email = new Label(member.email);
        email.setFont(Font.font(FONT, 9));
        email.setTextFill(Color.web(TEXT_MUTED_DARK));

        VBox info = new VBox(2, name, email);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label role = new Label(member.role);
        role.setFont(Font.font(FONT, FontWeight.BOLD, 9));
        role.setTextFill(Color.web(member.avatarColor));
        role.setPadding(new Insets(5, 7, 5, 7));
        role.setStyle(
                "-fx-background-color:" +
                member.avatarBackground + ";" +
                "-fx-background-radius:4;");

        Button more = new Button("⋮");
        more.setFont(Font.font(FONT, FontWeight.BOLD, 17));
        more.setTextFill(Color.web(TEXT_MUTED_DARK));
        more.setPrefWidth(25);
        more.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;" +
                "-fx-cursor:hand;");

        ContextMenu menu = new ContextMenu();
        MenuItem remove = new MenuItem("Remove Member");

        remove.setOnAction(e -> {
            if (!currentUserCan("REMOVE_MEMBER")) {
                showAccessDeniedPopup(
                        "Only the Owner can remove members.");
                return;
            }

            if (member.role.equals("Owner")) {
                showAccessDeniedPopup(
                        "The Owner cannot be removed.");
                return;
            }

            membersList.remove(member);
            refreshMemberList();
            updateMemberCount();
        });

        menu.getItems().add(remove);

        more.setOnAction(e ->
                menu.show(
                        more,
                        javafx.geometry.Side.BOTTOM,
                        0,
                        0));

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

        for (FileData file : filesList) {
            box.getChildren().add(createFileRow(file));
        }

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setPrefWidth(720);
        scroll.setPrefHeight(420);
        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);
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

        for (MemberData member : membersList) {
            box.getChildren().add(createMemberRow(member));
        }

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setPrefWidth(550);
        scroll.setPrefHeight(420);
        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle(
                "-fx-background-color:" + BG_CARD + ";" +
                "-fx-border-color:transparent;");

        dialog.getDialogPane().setContent(scroll);
        addCloseButton(dialog);
        dialog.showAndWait();
    }

    private void addUploadedFile(File file) {
        if (!currentUserCan("UPLOAD")) {
            showAccessDeniedPopup(
                    currentUserRole +
                    " cannot upload files.");
            return;
        }

        String fileName = file.getName();
        String extension = "";

        int dot = fileName.lastIndexOf(".");
        if (dot >= 0) {
            extension = fileName
                    .substring(dot + 1)
                    .toUpperCase();
        }

        String icon = extension.isEmpty() ? "FILE" : extension;
        String iconColor = "#64748B";

        if (extension.equals("PDF")) {
            icon = "PDF";
            iconColor = RED;
        } else if (
                extension.equals("DOC") ||
                extension.equals("DOCX")) {
            icon = "W";
            iconColor = PRIMARY_BLUE;
        } else if (
                extension.equals("PPT") ||
                extension.equals("PPTX")) {
            icon = "P";
            iconColor = ORANGE;
        } else if (
                extension.equals("XLS") ||
                extension.equals("XLSX")) {
            icon = "X";
            iconColor = SUCCESS;
        }

        filesList.add(new FileData(
                icon,
                fileName,
                formatFileSize(file.length()),
                "14 Aug 2026 01:00 AM",
                iconColor));

        refreshFileList();
        updateFileCount();
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";

        if (bytes < 1024 * 1024) {
            return String.format(
                    "%.1f KB",
                    bytes / 1024.0);
        }

        return String.format(
                "%.1f MB",
                bytes / (1024.0 * 1024.0));
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
        label.setFont(Font.font(FONT, 11));
        label.setTextFill(Color.web(TEXT_MUTED_DARK));
    }

    private void showManageAccessPopup() {
        if (!currentUserCan("MANAGE_ACCESS")) {
            showAccessDeniedPopup(
                    "Only the Owner can manage access.");
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
        description.setTextFill(
                Color.web(TEXT_MUTED_DARK));

        Label currentRole = new Label(
                "Current User Role: " + currentUserRole);
        currentRole.setFont(
                Font.font(FONT, FontWeight.BOLD, 13));
        currentRole.setTextFill(
                Color.web(PRIMARY_BLUE));

        VBox memberRows = new VBox(0);

        for (MemberData member : membersList) {
            memberRows.getChildren().add(
                    createManageAccessRow(member));
        }

        ScrollPane scroll = new ScrollPane(memberRows);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(340);
        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);
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

    private HBox createManageAccessRow(MemberData member) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 8, 10, 8));
        row.setStyle(
                "-fx-border-color:transparent transparent " +
                BORDER_COLOR + " transparent;");

        Label avatar = new Label(member.initials);
        avatar.setFont(
                Font.font(FONT, FontWeight.BOLD, 11));
        avatar.setTextFill(
                Color.web(member.avatarColor));
        avatar.setAlignment(Pos.CENTER);
        avatar.setPrefSize(38, 38);
        avatar.setStyle(
                "-fx-background-color:" +
                member.avatarBackground + ";" +
                "-fx-background-radius:50%;");

        Label name = new Label(member.name);
        name.setFont(
                Font.font(FONT, FontWeight.BOLD, 12));
        name.setTextFill(Color.web(TEXT_DARK));

        Label email = new Label(member.email);
        email.setFont(Font.font(FONT, 9));
        email.setTextFill(Color.web(TEXT_MUTED_DARK));

        VBox info = new VBox(2, name, email);
        HBox.setHgrow(info, Priority.ALWAYS);

        if (member.role.equals("Owner")) {
            Label owner = new Label("Owner");
            owner.setFont(
                    Font.font(FONT, FontWeight.BOLD, 10));
            owner.setTextFill(
                    Color.web(PRIMARY_BLUE));
            owner.setPadding(
                    new Insets(6, 12, 6, 12));
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
        roleCombo.getItems().addAll("Editor", "Viewer");
        roleCombo.setValue(member.role);
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

    private void updateMemberRole(
            MemberData member,
            String newRole) {

        if (!currentUserCan("MANAGE_ACCESS")) {
            showAccessDeniedPopup(
                    "Only the Owner can change member roles.");
            return;
        }

        if (member.role.equals("Owner") ||
                newRole == null ||
                (!newRole.equals("Editor") &&
                 !newRole.equals("Viewer"))) {
            return;
        }

        member.role = newRole;
        updateMemberAppearance(member);
        refreshMemberList();
        updateMemberCount();
    }

    private void updateMemberAppearance(MemberData member) {
        if (member.role.equals("Editor")) {
            member.avatarBackground = PRIMARY_LIGHT_BLUE;
            member.avatarColor = PRIMARY_BLUE;
        } else if (member.role.equals("Viewer")) {
            member.avatarBackground = SUCCESS_LIGHT;
            member.avatarColor = SUCCESS;
        }
    }

    private void showAddMemberPopup() {
        if (!currentUserCan("ADD_MEMBER")) {
            showAccessDeniedPopup(
                    "Only the Owner can add members.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Member");
        dialog.setHeaderText(
                "Send invitation to a new member");

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
        roleCombo.getItems().addAll("Viewer", "Editor");
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

        Button sendNode = (Button) dialog.getDialogPane()
                .lookupButton(sendButton);

        sendNode.setPrefHeight(38);
        sendNode.setPrefWidth(120);
        sendNode.setFont(
                Font.font(FONT, FontWeight.BOLD, 12));
        sendNode.setTextFill(Color.WHITE);
        sendNode.setStyle(
                "-fx-background-color:" + PRIMARY_BLUE + ";" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;");

        Button cancelNode = (Button) dialog.getDialogPane()
                .lookupButton(cancelButton);

        cancelNode.setPrefHeight(38);
        cancelNode.setPrefWidth(90);
        cancelNode.setFont(
                Font.font(FONT, FontWeight.BOLD, 12));
        cancelNode.setTextFill(Color.web(TEXT_DARK));
        cancelNode.setStyle(
                "-fx-background-color:" + BG_INPUT + ";" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;");

        sendNode.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleCombo.getValue();

            if (name.isEmpty()) {
                showAccessDeniedPopup(
                        "Please enter member name.");
                e.consume();
                return;
            }

            if (email.isEmpty()) {
                showAccessDeniedPopup(
                        "Please enter member email.");
                e.consume();
                return;
            }

            if (role == null) {
                showAccessDeniedPopup(
                        "Please select a role.");
                e.consume();
                return;
            }

            for (MemberData existing : membersList) {
                if (existing.email.equalsIgnoreCase(email)) {
                    showAccessDeniedPopup(
                            "A member with this email already exists.");
                    e.consume();
                    return;
                }
            }
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result != sendButton) return;

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleCombo.getValue();

            if (name.isEmpty() ||
                    email.isEmpty() ||
                    role == null) {
                return;
            }

            String initials = getInitials(name);
            String background;
            String avatarColor;

            if (role.equals("Editor")) {
                background = PRIMARY_LIGHT_BLUE;
                avatarColor = PRIMARY_BLUE;
            } else {
                background = SUCCESS_LIGHT;
                avatarColor = SUCCESS;
            }

            membersList.add(new MemberData(
                    initials,
                    name,
                    email,
                    role,
                    background,
                    avatarColor));

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
        label.setFont(
                Font.font(FONT, FontWeight.BOLD, 13));
        label.setTextFill(Color.web(TEXT_DARK));
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

        root.setStyle(
                "-fx-background-color:" + BG_APP + ";");

        root.setLeft(createSidebar());

        ScrollPane scroll = new ScrollPane(
                getSharedSpaceContent());

        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);
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
        sidebar.setPadding(
                new Insets(22, 14, 20, 14));
        sidebar.setStyle(
                "-fx-background-color:" +
                BG_SIDEBAR_CARD + ";" +
                "-fx-border-color:transparent;");

        Label logoIcon = new Label("◉");
        logoIcon.setFont(
                Font.font(FONT, FontWeight.BOLD, 20));
        logoIcon.setTextFill(
                Color.web(PRIMARY_LIGHT_BLUE));

        Label logo = new Label("OneSpace");
        logo.setFont(
                Font.font(FONT, FontWeight.BOLD, 18));
        logo.setTextFill(Color.web(TEXT_LIGHT));

        HBox logoRow = new HBox(9, logoIcon, logo);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        Label local = new Label("Local • AI Indexed");
        local.setFont(Font.font(FONT, 11));
        local.setTextFill(
                Color.web(TEXT_MUTED_LIGHT));

        VBox logoBox = new VBox(
                4,
                logoRow,
                local);

        logoBox.setPadding(
                new Insets(0, 8, 25, 8));

        Button dashboard = createSidebarButton(
                "⌂", "Dashboard", false);
        dashboard.setOnAction(e ->
                LandingPage.showUserDashboard());

        Button spaces = createSidebarButton(
                "▦", "Spaces", false);
        spaces.setOnAction(e ->
                LandingPage.showUserSpace());

        Button search = createSidebarButton(
                "⌕", "Search", false);

        Button calendar = createSidebarButton(
                "□", "Calendar", false);

        Button aiAssistant = createSidebarButton(
                "✧", "AI Assistant", false);

        Button collaboration = createSidebarButton(
                "♧", "Collaboration", true);
        collaboration.setOnAction(e ->
                LandingPage.showCollaborationPage());

        Button recent = createSidebarButton(
                "◷", "Recent", false);

        Button trash = createSidebarButton(
                "♧", "Trash", false);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button settings = createSidebarButton(
                "⚙", "Settings", false);

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

        Button button = new Button(
                icon + "    " + text);

        button.setAlignment(Pos.CENTER_LEFT);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.setPadding(
                new Insets(0, 10, 0, 10));
        button.setFont(Font.font(FONT, 12));

        if (selected) {
            button.setTextFill(
                    Color.web(TEXT_LIGHT));
            button.setStyle(
                    "-fx-background-color:" +
                    PRIMARY_BLUE + ";" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;");
        } else {
            button.setTextFill(
                    Color.web(TEXT_MUTED_LIGHT));
            button.setStyle(
                    "-fx-background-color:transparent;" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;");

            button.setOnMouseEntered(e ->
                    button.setStyle(
                            "-fx-background-color:" +
                            "rgba(191,219,254,0.12);" +
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

        Label title = new Label(
                "✧  Storage indexed");
        title.setFont(
                Font.font(FONT, FontWeight.BOLD, 11));
        title.setTextFill(
                Color.web(PRIMARY_BLUE));

        Label amount = new Label("64.2 GB");
        amount.setFont(
                Font.font(FONT, FontWeight.BOLD, 19));
        amount.setTextFill(Color.web(TEXT_DARK));

        Label used = new Label("of 100 GB used");
        used.setFont(Font.font(FONT, 10));
        used.setTextFill(
                Color.web(TEXT_MUTED_DARK));

        HBox progressBox = new HBox();
        progressBox.setPrefHeight(7);
        progressBox.setMinHeight(7);
        progressBox.setMaxWidth(Double.MAX_VALUE);
        progressBox.setStyle(
                "-fx-background-color:" +
                BORDER_COLOR + ";" +
                "-fx-background-radius:10;");

        Region progress = new Region();
        progress.setPrefWidth(105);
        progress.setPrefHeight(7);
        progress.setStyle(
                "-fx-background-color:" +
                PRIMARY_BLUE + ";" +
                "-fx-background-radius:10;");

        progressBox.getChildren().add(progress);

        Label bottom = new Label(
                "Files stay in place —\n" +
                "nothing was moved or renamed.");

        bottom.setFont(Font.font(FONT, 9));
        bottom.setTextFill(
                Color.web(TEXT_MUTED_DARK));
        bottom.setWrapText(true);

        card.getChildren().addAll(
                title,
                amount,
                used,
                progressBox,
                bottom);

        return card;
    }

    private static class MemberData {
        String initials;
        String name;
        String email;
        String role;
        String avatarBackground;
        String avatarColor;

        MemberData(
                String initials,
                String name,
                String email,
                String role,
                String avatarBackground,
                String avatarColor) {

            this.initials = initials;
            this.name = name;
            this.email = email;
            this.role = role;
            this.avatarBackground = avatarBackground;
            this.avatarColor = avatarColor;
        }
    }

    private static class FileData {
        String icon;
        String fileName;
        String size;
        String uploadedOn;
        String iconColor;

        FileData(
                String icon,
                String fileName,
                String size,
                String uploadedOn,
                String iconColor) {

            this.icon = icon;
            this.fileName = fileName;
            this.size = size;
            this.uploadedOn = uploadedOn;
            this.iconColor = iconColor;
        }
    }
}