package com.file_handlers.view;

import com.file_handlers.view.userView.AddReminderPage;
import com.file_handlers.view.userView.NotificationPage;
import com.file_handlers.view.userView.UserCalender;
import com.file_handlers.view.userView.UserDashboard;
import com.file_handlers.view.userView.UserLoginPage;
import com.file_handlers.view.userView.UserSearch;
import com.file_handlers.view.userView.UserSpaces;
import com.file_handlers.view.userView.UserTrash;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class LandingPage extends Application {

    private static final String FONT="Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";
    private static final String BG="#3A4D67",CARD="#DDE8F5",BORDER="#C9DAEE";
    private static final String BLUE="#2563EB",LIGHT="#BFDBFE";
    private static final String TEXT="#142338",MUTED="#506580";
    private static final String WHITE="#FFFFFF",MUTED_LIGHT="#9EB0C6";

    private static Stage primaryStage;

    @Override
    public void start(Stage stage){
        primaryStage=stage;
        primaryStage.setTitle("OneSpace");
        primaryStage.setScene(getLandingPageScene());
        primaryStage.show();
    }

    public static void setScene(Scene scene){
        if(primaryStage!=null) primaryStage.setScene(scene);
    }

    public static void showLandingPage(){
        setScene(new LandingPage().getLandingPageScene());
    }

    public static void showUserLoginPage(){
        setScene(new UserLoginPage().getUserLoginPageScene());
    }

    public static void showUserDashboard(){
        setScene(new UserDashboard().getDashboardScene());
    }

    public static void showUserSpace(){
        setScene(new UserSpaces().getUserSpacesScene());
    }

    public static void showUserSearch(){
        setScene(new UserSearch().getUserSearchScene());
    }

    public static void showNotificationPage(){
    setScene(new NotificationPage().getNotificationsScene());
}

    public static void showCalendarPage(){
        setScene(new UserCalender().getCalendarPageScene());
    }
    
    public static void showAddReminderPage(){
        if(primaryStage!=null)
            primaryStage.setScene(
                new AddReminderPage().getAddReminderPageScene(primaryStage)
            );
    }

    //public static void showTrashPage() { primaryStage.setScene(new UserTrash().getTrashPageScene());}
    public static void showTrashPage() {
    if(primaryStage != null)
        primaryStage.setScene(new UserTrash().getTrashPageScene());
}

    public Scene getLandingPageScene(){

        Label logoIcon=label("⬡",22,FontWeight.BOLD,LIGHT);
        Label logoText=label("OneSpace",18,FontWeight.BOLD,WHITE);

        HBox header=new HBox(8,logoIcon,logoText);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18,24,18,24));

        Circle outer=new Circle(30,Color.web(LIGHT));
        Circle inner=new Circle(21,Color.web(BLUE));
        Label symbol=label("◎",18,FontWeight.BOLD,WHITE);

        StackPane icon=new StackPane(outer,inner,symbol);

        Label title=label("Welcome to OneSpace",28,FontWeight.BOLD,WHITE);
        Label sub=label("Choose how you want to continue",14,FontWeight.NORMAL,MUTED_LIGHT);

        VBox titleBox=new VBox(10,icon,title,sub);
        titleBox.setAlignment(Pos.CENTER);

        VBox user=createRoleCard(
            "👤",LIGHT,BLUE,
            "User Login",
            "Access your personal space,\nmanage your files and more.",
            "Continue as User  →",
            BLUE,
            e->showUserLoginPage()
        );

        VBox admin=createRoleCard(
            "🛡", "#BAE6FD","#0284C7",
            "Admin Login",
            "Manage users, oversee system\nactivities and configurations.",
            "Continue as Admin  →",
            "#0284C7",
            e->showUserLoginPage()
        );

        HBox cards=new HBox(28,user,admin);
        cards.setAlignment(Pos.CENTER);

        Label shield=label("🛡",14,FontWeight.NORMAL,MUTED_LIGHT);
        Label secure=label(
            "Secure. Organized. Intelligent.",
            12,FontWeight.BOLD,MUTED_LIGHT
        );

        HBox footerRow=new HBox(6,shield,secure);
        footerRow.setAlignment(Pos.CENTER);

        Label brand=label("OneSpace",12,FontWeight.BOLD,LIGHT);

        VBox footer=new VBox(4,footerRow,brand);
        footer.setAlignment(Pos.CENTER);

        Region top=new Region();
        Region bottom=new Region();
        VBox.setVgrow(top,Priority.ALWAYS);
        VBox.setVgrow(bottom,Priority.ALWAYS);

        VBox center=new VBox(
            28,top,titleBox,cards,bottom,footer
        );
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(0,24,24,24));

        BorderPane root=new BorderPane();
        root.setTop(header);
        root.setCenter(center);
        root.setStyle("-fx-background-color:"+BG+";");

        return new Scene(root,1200,750);
    }

    private VBox createRoleCard(
            String iconSymbol,String iconBg,String iconColor,
            String title,String description,
            String buttonText,String buttonColor,
            javafx.event.EventHandler<javafx.event.ActionEvent> action){

        Label icon=label(iconSymbol,20,FontWeight.BOLD,iconColor);
        icon.setPrefSize(48,48);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle(
            "-fx-background-color:"+iconBg+
            ";-fx-background-radius:50%;"
        );

        Label cardTitle=label(title,18,FontWeight.BOLD,TEXT);

        Label desc=label(
            description,13,FontWeight.NORMAL,MUTED
        );
        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setWrapText(true);

        Button button=new Button(buttonText);
        button.setFont(Font.font(FONT,FontWeight.BOLD,13));
        button.setTextFill(Color.WHITE);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.setStyle(
            "-fx-background-color:"+buttonColor+
            ";-fx-background-radius:10;-fx-cursor:hand;"
        );
        button.setOnAction(action);

        VBox card=new VBox(
            16,icon,cardTitle,desc,button
        );
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(32,28,32,28));
        card.setPrefWidth(300);
        card.setMaxWidth(300);
        card.setStyle(
            "-fx-background-color:"+CARD+
            ";-fx-border-color:"+BORDER+
            ";-fx-border-radius:18;-fx-background-radius:18;"+
            "-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,.12),16,0,0,6);"
        );

        return card;
    }

    private static Label label(
            String text,double size,
            FontWeight weight,String color){

        Label l=new Label(text);
        l.setFont(Font.font(FONT,weight,size));
        l.setTextFill(Color.web(color));
        return l;
    }
}