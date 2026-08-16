package com.file_handlers.view.userView;

import com.file_handlers.controller.fileController.FileController;
import com.file_handlers.model.fileModel.FileMetadata;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class DemoSearchPage {

    public Scene getDemoSearchPageScene() {

        Button accessFileButton = new Button("Access Local File");

        accessFileButton.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();

            Stage stage = (Stage) accessFileButton.getScene().getWindow();

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {

                FileController controller = new FileController();
                FileMetadata metadata =
                        controller.getFileMetadata(file.toPath());

                if (metadata != null) {
                    System.out.println("Name: " + metadata.getFileName());
                    System.out.println("Path: " + metadata.getFilePath());
                    System.out.println("Size: " + metadata.getFileSize());
                    System.out.println("Extension: " + metadata.getExtension());
                    System.out.println("MIME Type: " + metadata.getMimeType());
                    System.out.println("Created: " + metadata.getCreationTime());
                    System.out.println("Modified: " + metadata.getLastModifiedTime());
                    System.out.println("Accessed: " + metadata.getLastAccessTime());
                    System.out.println("Directory: " + metadata.isDirectory());
                    System.out.println("Readable: " + metadata.isReadable());
                    System.out.println("Writable: " + metadata.isWritable());
                }
            }
        });

        StackPane root = new StackPane(accessFileButton);
        root.setAlignment(Pos.CENTER);

        return new Scene(root, 1200, 750);
    }
}