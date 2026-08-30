package com.file_handlers.view.userView;

import com.file_handlers.model.UserSession;
import com.file_handlers.view.LandingPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StorageIndexPage {

    private static final String TOPBAR = "#1E2A3A";
    private static final String BACKGROUND = "#31445D";

    private static final String CARD = "#DCE8F7";
    private static final String CARD_HOVER = "#D2E2F5";

    private static final String TEXT = "#10213A";
    private static final String MUTED = "#536985";

    private static final String BLUE = "#2563EB";

    private static final String GREEN = "#10B981";
    private static final String GREEN_LIGHT = "#A7EFD2";

    private static final String BORDER = "#C2D4E8";

    private final File oneSpace =
            new File(
                    System.getProperty("user.home"),
                    "OneSpace"
            );

    public Scene getStorageIndexPageScene() {

        BorderPane root = new BorderPane();

        root.setCenter(createMainArea());

        root.setStyle(
                "-fx-background-color:" + BACKGROUND + ";"
        );

        return new Scene(root, 1200, 750);
    }

    private VBox createMainArea() {

        VBox main = new VBox(
                createTopBar(),
                createStorageContent()
        );

        main.setStyle(
                "-fx-background-color:" + BACKGROUND + ";"
        );

        return main;
    }

    private HBox createTopBar() {

        Button backButton = new Button("←  Dashboard");

        backButton.setPrefHeight(44);

        backButton.setStyle(
                "-fx-background-color:#142030;" +
                "-fx-text-fill:#FFFFFF;" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-border-color:#30445D;" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;" +
                "-fx-padding:0 16;" +
                "-fx-cursor:hand;"
        );

        backButton.setOnMouseEntered(
                e -> backButton.setStyle(
                        "-fx-background-color:#2563EB;" +
                        "-fx-text-fill:#FFFFFF;" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-border-color:#2563EB;" +
                        "-fx-border-radius:12;" +
                        "-fx-background-radius:12;" +
                        "-fx-padding:0 16;" +
                        "-fx-cursor:hand;"
                )
        );

        backButton.setOnMouseExited(
                e -> backButton.setStyle(
                        "-fx-background-color:#142030;" +
                        "-fx-text-fill:#FFFFFF;" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-border-color:#30445D;" +
                        "-fx-border-radius:12;" +
                        "-fx-background-radius:12;" +
                        "-fx-padding:0 16;" +
                        "-fx-cursor:hand;"
                )
        );

        backButton.setOnAction(
                e -> LandingPage.showUserDashboard()
        );

        Button notification =
                new Button("♟");

        notification.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#FFFFFF;" +
                "-fx-font-size:18px;" +
                "-fx-cursor:hand;"
        );

        notification.setOnAction(
                e -> LandingPage.showNotificationPage()
        );

        String name = "Ananta";

        try {

            UserSession session =
                    UserSession.getInstance();

            if (session != null &&
                session.getDisplayName() != null &&
                !session.getDisplayName().isBlank()) {

                name =
                        session.getDisplayName()
                                .split("\\s+")[0];
            }

        } catch (Exception ignored) {
        }

        Label avatar =
                label(
                        name.substring(0, 1)
                                .toUpperCase(),
                        14,
                        true,
                        "#FFFFFF"
                );

        avatar.setPrefSize(38, 38);
        avatar.setAlignment(Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color:" +
                BLUE + ";" +
                "-fx-background-radius:50%;"
        );

        Label userName =
                label(
                        name,
                        14,
                        true,
                        "#FFFFFF"
                );

        Label arrow =
                label(
                        "⌄",
                        13,
                        false,
                        "#A7B6CA"
                );

        HBox profile =
                new HBox(
                        10,
                        avatar,
                        userName,
                        arrow
                );

        profile.setAlignment(
                Pos.CENTER
        );

        Region spacer = spacer();

        HBox bar =
                new HBox(
                        16,
                        backButton,
                        spacer,
                        notification,
                        profile
                );

        bar.setAlignment(
                Pos.CENTER_LEFT
        );

        bar.setPadding(
                new Insets(
                        20,
                        24,
                        20,
                        36
                )
        );

        bar.setStyle(
                "-fx-background-color:" +
                TOPBAR + ";" +
                "-fx-border-color:#2B3C52;" +
                "-fx-border-width:0 0 1 0;"
        );

        return bar;
    }

    private ScrollPane createStorageContent() {

        VBox content = new VBox(18);

        content.setPadding(
                new Insets(
                        28,
                        34,
                        30,
                        34
                )
        );

        content.setStyle(
                "-fx-background-color:" +
                BACKGROUND + ";"
        );

        long oneSpaceSize =
                folderSize(oneSpace);

        File drive =
                getInternalDrive();

        long totalPC =
                drive.getTotalSpace();

        long usedPC =
                totalPC -
                drive.getFreeSpace();

        double oneSpaceOfTotal =
                totalPC == 0
                        ? 0
                        : oneSpaceSize *
                          100.0 /
                          totalPC;

        double oneSpaceOfUsed =
                usedPC == 0
                        ? 0
                        : oneSpaceSize *
                          100.0 /
                          usedPC;

        HBox header =
                new HBox(
                        createPageHeading(),
                        spacer()
                );

        Button refresh =
                blueButton(
                        "⟳  Refresh"
                );

        refresh.setOnAction(
                e -> LandingPage.showStorageIndexPage()
        );

        header.getChildren().add(
                refresh
        );

        HBox usage =
                createUsageCard(
                        oneSpaceSize,
                        oneSpaceOfTotal,
                        oneSpaceOfUsed
                );

        VBox storageBySpace =
                createStorageBySpace(
                        oneSpaceSize
                );

        VBox pcStorage =
                createPCStorage(
                        oneSpaceSize,
                        totalPC,
                        usedPC,
                        drive
                );

        HBox twoCards =
                new HBox(
                        14,
                        storageBySpace,
                        pcStorage
                );

        HBox.setHgrow(
                storageBySpace,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                pcStorage,
                Priority.ALWAYS
        );

        VBox files =
                createFileActivity(
                        oneSpaceSize
                );

        HBox summary =
                createSummary(
                        oneSpace
                );

        Separator separator =
                new Separator();

        separator.setStyle(
                "-fx-background-color:#50657E;"
        );

        HBox footer =
                new HBox(
                        label(
                                "Storage data is calculated from your system.",
                                11,
                                false,
                                "#AFC0D5"
                        ),
                        spacer(),
                        label(
                                "Actual OneSpace storage • Live system data",
                                11,
                                false,
                                "#AFC0D5"
                        )
                );

        content.getChildren().addAll(
                header,
                usage,
                twoCards,
                files,
                summary,
                separator,
                footer
        );

        ScrollPane scroll =
                new ScrollPane(content);

        scroll.setFitToWidth(true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scroll.setStyle(
                "-fx-background-color:" +
                BACKGROUND + ";" +
                "-fx-background:" +
                BACKGROUND + ";"
        );

        return scroll;
    }

    private VBox createPageHeading() {

        Label title =
                label(
                        "Storage Index",
                        25,
                        true,
                        "#FFFFFF"
                );

        Label subtitle =
                label(
                        "Monitor the storage occupied by your "
                        + "OneSpace files and your PC storage.",
                        13,
                        false,
                        "#AFC0D5"
                );

        return new VBox(
                5,
                title,
                subtitle
        );
    }

    private HBox createUsageCard(
            long size,
            double totalPercent,
            double usedPercent) {

        Label folder =
                label(
                        "▰",
                        30,
                        true,
                        GREEN
                );

        VBox icon =
                new VBox(folder);

        icon.setAlignment(
                Pos.CENTER
        );

        icon.setPrefSize(
                76,
                76
        );

        icon.setStyle(
                "-fx-background-color:" +
                GREEN_LIGHT + ";" +
                "-fx-background-radius:50%;"
        );

        Label heading =
                label(
                        "OneSpace Usage",
                        14,
                        true,
                        MUTED
                );

        Label sizeLabel =
                label(
                        format(size),
                        31,
                        true,
                        TEXT
                );

        Label description =
                label(
                        "Actual space occupied by OneSpace files",
                        12,
                        false,
                        MUTED
                );

        VBox middle =
                new VBox(
                        3,
                        heading,
                        sizeLabel,
                        description
                );

        Region spacer =
                spacer();

        VBox right =
                new VBox(
                        3,
                        label(
                                String.format(
                                        "%.2f%%",
                                        totalPercent
                                ),
                                27,
                                true,
                                GREEN
                        ),
                        label(
                                "of total PC storage",
                                12,
                                false,
                                MUTED
                        ),
                        label(
                                String.format(
                                        "%.2f%% of currently used space",
                                        usedPercent
                                ),
                                11,
                                false,
                                MUTED
                        )
                );

        right.setAlignment(
                Pos.CENTER_RIGHT
        );

        HBox card =
                new HBox(
                        18,
                        icon,
                        middle,
                        spacer,
                        right
                );

        card.setAlignment(
                Pos.CENTER_LEFT
        );

        card.setPadding(
                new Insets(20, 24, 20, 24)
        );

        card.setStyle(
                cardStyle()
        );

        return card;
    }

    private VBox createStorageBySpace(
            long total) {

        VBox rows =
                new VBox(11);

        String[] spaces = {
                "Personal",
                "College",
                "Office",
                "Finance",
                "Entertainment",
                "Others"
        };

        for (String name : spaces) {

            File folder =
                    new File(
                            oneSpace,
                            name
                    );

            long size =
                    folderSize(folder);

            double percent =
                    total == 0
                            ? 0
                            : size *
                              100.0 /
                              total;

            rows.getChildren().add(
                    createSpaceRow(
                            name,
                            size,
                            percent
                    )
            );
        }

        VBox card =
                new VBox(
                        10,
                        label(
                                "Storage by Space",
                                17,
                                true,
                                TEXT
                        ),
                        label(
                                "Space used by each OneSpace category.",
                                12,
                                false,
                                MUTED
                        ),
                        separator(),
                        rows
                );

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                cardStyle()
        );

        return card;
    }

    private HBox createSpaceRow(
            String name,
            long size,
            double percent) {

        Label icon =
                label(
                        "■",
                        10,
                        true,
                        BLUE
                );

        icon.setPrefWidth(18);

        Label nameLabel =
                label(
                        name,
                        12,
                        true,
                        TEXT
                );

        Label sizeLabel =
                label(
                        format(size),
                        12,
                        true,
                        TEXT
                );

        sizeLabel.setPrefWidth(76);

        ProgressBar progress =
                new ProgressBar(
                        Math.min(
                                percent / 100,
                                1
                        )
                );

        progress.setPrefWidth(100);
        progress.setPrefHeight(7);

        progress.setStyle(
                "-fx-accent:" +
                BLUE + ";" +
                "-fx-control-inner-background:" +
                "#B8CBE1;"
        );

        Label percentage =
                label(
                        String.format(
                                "%.1f%%",
                                percent
                        ),
                        11,
                        true,
                        MUTED
                );

        Region spacer =
                spacer();

        HBox row =
                new HBox(
                        7,
                        icon,
                        nameLabel,
                        sizeLabel,
                        spacer,
                        progress,
                        percentage
                );

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        return row;
    }

    private VBox createPCStorage(
            long oneSpaceSize,
            long total,
            long used,
            File drive) {

        long available =
                drive.getFreeSpace();

        double usedPercent =
                total == 0
                        ? 0
                        : used *
                          100.0 /
                          total;

        double oneSpacePercent =
                total == 0
                        ? 0
                        : oneSpaceSize *
                          100.0 /
                          total;

        VBox card =
                new VBox(
                        11,
                        label(
                                "PC Storage Info",
                                17,
                                true,
                                TEXT
                        ),
                        label(
                                "Current internal drive information.",
                                12,
                                false,
                                MUTED
                        ),
                        separator()
                );

        card.getChildren().addAll(

                infoRow(
                        "Drive",
                        drive.getAbsolutePath()
                ),

                infoRow(
                        "Total Capacity",
                        format(total)
                ),

                infoRow(
                        "Used",
                        format(used)
                        + " ("
                        + String.format(
                                "%.2f%%",
                                usedPercent
                        )
                        + ")"
                ),

                infoRow(
                        "Available",
                        format(available)
                ),

                infoRow(
                        "OneSpace Usage",
                        format(oneSpaceSize)
                        + " ("
                        + String.format(
                                "%.2f%%",
                                oneSpacePercent
                        )
                        + ")"
                )
        );

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                cardStyle()
        );

        return card;
    }

    private HBox infoRow(
            String name,
            String value) {

        Label left =
                label(
                        name,
                        12,
                        true,
                        MUTED
                );

        Label right =
                label(
                        value,
                        12,
                        true,
                        TEXT
                );

        Region spacer =
                spacer();

        HBox row =
                new HBox(
                        left,
                        spacer,
                        right
                );

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        return row;
    }

    private VBox createFileActivity(
            long totalSize) {

        List<File> files =
                getFiles(oneSpace);

        files.sort(
                Comparator.comparingLong(
                        File::length
                ).reversed()
        );

        VBox rows =
                new VBox(6);

        for (File file : files) {

            if (rows.getChildren().size() >= 8)
                break;

            double percent =
                    totalSize == 0
                            ? 0
                            : file.length()
                              * 100.0 /
                              totalSize;

            rows.getChildren().add(
                    createFileRow(
                            file,
                            percent
                    )
            );
        }

        if (files.isEmpty()) {

            rows.getChildren().add(
                    label(
                            "No files are currently stored in OneSpace.",
                            12,
                            false,
                            MUTED
                    )
            );
        }

        VBox card =
                new VBox(
                        10,
                        label(
                                "Files Occupying Storage",
                                17,
                                true,
                                TEXT
                        ),
                        label(
                                "Files currently stored in OneSpace, "
                                + "sorted by size.",
                                12,
                                false,
                                MUTED
                        ),
                        separator(),
                        rows
                );

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                cardStyle()
        );

        return card;
    }

    private HBox createFileRow(
            File file,
            double percent) {

        Label fileIcon =
                label(
                        "▣",
                        13,
                        false,
                        BLUE
                );

        Label name =
                label(
                        file.getName(),
                        12,
                        true,
                        TEXT
                );

        Label size =
                label(
                        format(file.length()),
                        12,
                        true,
                        TEXT
                );

        ProgressBar progress =
                new ProgressBar(
                        Math.min(
                                percent / 100,
                                1
                        )
                );

        progress.setPrefWidth(150);
        progress.setPrefHeight(7);

        progress.setStyle(
                "-fx-accent:" +
                BLUE + ";" +
                "-fx-control-inner-background:" +
                "#B8CBE1;"
        );

        Region spacer =
                spacer();

        HBox row =
                new HBox(
                        10,
                        fileIcon,
                        name,
                        spacer,
                        progress,
                        size
                );

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPadding(
                new Insets(
                        8,
                        10,
                        8,
                        10
                )
        );

        row.setStyle(
                "-fx-background-color:" +
                CARD_HOVER + ";" +
                "-fx-background-radius:8;"
        );

        return row;
    }

    private HBox createSummary(
            File folder) {

        List<File> files =
                getFiles(folder);

        long total =
                folderSize(folder);

        long largest =
                files.isEmpty()
                        ? 0
                        : files.stream()
                                .mapToLong(
                                        File::length
                                )
                                .max()
                                .orElse(0);

        long smallest =
                files.isEmpty()
                        ? 0
                        : files.stream()
                                .mapToLong(
                                        File::length
                                )
                                .min()
                                .orElse(0);

        long average =
                files.isEmpty()
                        ? 0
                        : total / files.size();

        HBox summary =
                new HBox(
                        12,

                        stat(
                                String.valueOf(
                                        files.size()
                                ),
                                "Total Files"
                        ),

                        stat(
                                format(total),
                                "Total OneSpace Storage"
                        ),

                        stat(
                                format(largest),
                                "Largest File"
                        ),

                        stat(
                                format(smallest),
                                "Smallest File"
                        ),

                        stat(
                                format(average),
                                "Average File Size"
                        )
                );

        return summary;
    }

    private VBox stat(
            String value,
            String title) {

        VBox box =
                new VBox(
                        4,
                        label(
                                value,
                                19,
                                true,
                                BLUE
                        ),
                        label(
                                title,
                                11,
                                false,
                                MUTED
                        )
                );

        box.setPadding(
                new Insets(13)
        );

        box.setStyle(
                cardStyle()
        );

        HBox.setHgrow(
                box,
                Priority.ALWAYS
        );

        return box;
    }

    private Button blueButton(
            String text) {

        Button button =
                new Button(text);

        button.setPrefHeight(42);

        button.setStyle(
                "-fx-background-color:" +
                BLUE + ";" +
                "-fx-text-fill:#FFFFFF;" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:10;" +
                "-fx-padding:0 18;" +
                "-fx-cursor:hand;"
        );

        button.setOnMouseEntered(
                e -> button.setStyle(
                        "-fx-background-color:#1D4ED8;" +
                        "-fx-text-fill:#FFFFFF;" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:10;" +
                        "-fx-padding:0 18;" +
                        "-fx-cursor:hand;"
                )
        );

        button.setOnMouseExited(
                e -> button.setStyle(
                        "-fx-background-color:" +
                        BLUE + ";" +
                        "-fx-text-fill:#FFFFFF;" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:10;" +
                        "-fx-padding:0 18;" +
                        "-fx-cursor:hand;"
                )
        );

        return button;
    }

    private long folderSize(
            File folder) {

        if (!folder.exists())
            return 0;

        if (folder.isFile())
            return folder.length();

        long total = 0;

        File[] files =
                folder.listFiles();

        if (files != null) {

            for (File file : files) {
                total += folderSize(file);
            }
        }

        return total;
    }

    private List<File> getFiles(
            File folder) {

        List<File> result =
                new ArrayList<>();

        if (!folder.exists())
            return result;

        File[] files =
                folder.listFiles();

        if (files == null)
            return result;

        for (File file : files) {

            if (file.isFile()) {

                result.add(file);

            } else {

                result.addAll(
                        getFiles(file)
                );
            }
        }

        return result;
    }

    private File getInternalDrive() {

        String systemDrive =
                System.getenv(
                        "SystemDrive"
                );

        if (systemDrive != null) {

            return new File(
                    systemDrive + "\\"
            );
        }

        return new File("/");
    }

    private String format(
            long bytes) {

        if (bytes < 1024)
            return bytes + " B";

        if (bytes < 1024L * 1024)
            return String.format(
                    "%.1f KB",
                    bytes / 1024.0
            );

        if (bytes < 1024L * 1024 * 1024)
            return String.format(
                    "%.2f MB",
                    bytes /
                    (1024.0 * 1024)
            );

        return String.format(
                "%.2f GB",
                bytes /
                (1024.0 * 1024 * 1024)
        );
    }

    private Label label(
            String text,
            double size,
            boolean bold,
            String color) {

        Label label =
                new Label(text);

        label.setFont(
                Font.font(
                        "System",
                        bold
                                ? FontWeight.BOLD
                                : FontWeight.NORMAL,
                        size
                )
        );

        label.setTextFill(
                Color.web(color)
        );

        return label;
    }

    private Region spacer() {

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        return spacer;
    }

    private Separator separator() {

        Separator separator =
                new Separator();

        separator.setStyle(
                "-fx-background-color:" +
                BORDER + ";"
        );

        return separator;
    }

    private String cardStyle() {

        return
                "-fx-background-color:" +
                CARD + ";" +

                "-fx-border-color:" +
                BORDER + ";" +

                "-fx-border-radius:16;" +

                "-fx-background-radius:16;" +

                "-fx-effect:dropshadow(" +
                "three-pass-box," +
                "rgba(15,23,42,0.15)," +
                "7,0,0,3);";
    }
}