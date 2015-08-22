package view;

import controller.EnterHEAD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Admin on 12.05.15.
 */
public class Enter extends Application {
    public static void main(String ... args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        EnterHEAD.stage = stage;
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("EnterHead.fxml"));
        Scene scene = new Scene(parent);
        stage.setTitle("Welcome to chat");
        stage.setScene(scene);
        stage.show();

    }
}
