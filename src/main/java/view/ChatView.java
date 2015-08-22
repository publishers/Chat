package view;
/**
 * Created by User on 29.01.15.
 */

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.plugin.dom.html.HTMLDivElement;
import sun.plugin.dom.html.HTMLTextAreaElement;

public class ChatView extends Application {

    public static void main(String ... args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("Interface.fxml"));
        Scene scene = new Scene(parent);
        stage.setTitle("Welcome to chat");
        stage.setScene(scene);
        stage.show();
    }
}
