package controller;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import socket.client.SocketClient;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by User on 29.01.15.
 */
public class FXMLController implements Initializable {
    @FXML
    public TextArea connectedUsers;
    @FXML
    public TextArea sendMessage;
    @FXML
    public TextArea getMessages;
    @FXML
    public TextField userName;

    private Client client;
    private SocketClient socketClient;

    private int sizeText = 12;

    /**
    * Handler button Enter
    * */
    public void pressEnter(KeyEvent event) {
        if (event.isShiftDown() && event.getCode() == KeyCode.ENTER) {
            StringBuffer sb = new StringBuffer(sendMessage.getText());
            sb.insert(sendMessage.getCaretPosition(),"\n");
            sendMessage.setText(String.valueOf(sb));
            sendMessage.positionCaret(sendMessage.getText().length());
        } else if (event.getCode() == KeyCode.ENTER) {
            //TODO send Message
            client.setMessage(sendMessage.getText());
            System.out.println("Message: " + client.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        connectedUsers.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));
        getMessages.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));
        userName.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));

        sendMessage.setWrapText(true);
        getMessages.setWrapText(true);
    }

    @FXML
    private void disconnect(ActionEvent actionEvent) {
    }

    @FXML
    private void connect(ActionEvent actionEvent) {
        String userName = this.userName.getText();
        if(isUserNickCorrect(userName)){
            client = new Client(userName);
            doConnection(client);
            System.out.println("Connect is ready");
            connectedToServer();
        } else {
            JOptionPane.showMessageDialog(null, "You have bad userName '" + userName + "'");
        }
    }

    private void doConnection(Client client) {
        socketClient = new SocketClient(client);
        new Thread(socketClient).start();
    }

    private void connectedToServer() {
        socketClient.run();
    }

    private boolean isUserNickCorrect(String userName) {
        return userName.trim().matches("[A-Za-z0-9 ].*");
    }

    @FXML
    private void sendMessage(ActionEvent actionEvent) {

    }
}
