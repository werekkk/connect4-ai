package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_view.fxml"));
        primaryStage.setTitle("Connect 4 - Jacek Wernikowski");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("images/icon.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
//        EffectivenessTest.testHeuristics("effectiveness.csv");
    }
}
