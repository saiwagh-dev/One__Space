package com.file_handlers.view;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class SplashScreen{
    private final Stage stage;
    private final Runnable onFinished;
    private MediaPlayer mediaPlayer;

    public SplashScreen(Stage stage,Runnable onFinished){
        this.stage=stage;
        this.onFinished=onFinished;
    }

    public Scene getSplashScene(){
        String videoUrl=getClass()
                .getResource("/assets/splash/SplashScreenVideo.mp4")
                .toExternalForm();

        Media media=new Media(videoUrl);
        mediaPlayer=new MediaPlayer(media);
        mediaPlayer.setCycleCount(1);

        MediaView mediaView=new MediaView(mediaPlayer);
        mediaView.setPreserveRatio(true);
        mediaView.setSmooth(true);

        StackPane root=new StackPane(mediaView);
        root.setStyle("-fx-background-color:#03060A;");

        mediaView.fitWidthProperty().bind(root.widthProperty());
        mediaView.fitHeightProperty().bind(root.heightProperty());

        mediaPlayer.setOnEndOfMedia(()->{
            mediaPlayer.dispose();
            onFinished.run();
        });

        return new Scene(root,1280,720);
    }

    public void play(){
        if(mediaPlayer!=null)
            mediaPlayer.play();
    }
}