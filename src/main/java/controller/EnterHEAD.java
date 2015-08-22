package controller;

import client.status.StatusClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mySocketClient.SocketClient;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Admin on 12.05.15.
 */
public class EnterHEAD implements Initializable{
    public TextField userName;
    public TextField userPASS;
    public static Stage stage;

    private SocketClient sClient;

    public void connect(ActionEvent actionEvent) {
            if (!userName.getText().trim().equals("your name") && !userPASS.getText().trim().equals("pass")) {
                sClient = new SocketClient(userName.getText(), userPASS.getText());
                if(sClient.Exist()) {
                    try {
                        FXMLController.UName = userName.getText();
                        FXMLController.UPASS = userPASS.getText();

                        Stage stage = new Stage();
                        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("Interface.fxml"));
                        Scene scene = new Scene(parent);
                        stage.setTitle("Welcome to chat");
                        stage.setScene(scene);
                        stage.show();
                        this.stage.hide();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please Input Login and Password again!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please Input Login and Password");
            }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (userName.getText().trim().equals("your name")) {
                    userName.setText("");
                }
            } else {
                if (userName.getText().trim().equals("")) {
                    userName.setText("your name");
                }
            }
        });
        userPASS.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (userPASS.getText().trim().equals("pass")) {
                    userPASS.setText("");
                }
            } else {
                if (userPASS.getText().trim().equals("")) {
                    userPASS.setText("pass");
                }
            }
        });
    }
}
