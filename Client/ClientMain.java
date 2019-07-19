package Client;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage = new StartView();
        primaryStage.setTitle("Machi Koro Online");
        primaryStage.getIcons().add(new Image("/resources/Logo.jpeg"));
        primaryStage.show();
    }
}
