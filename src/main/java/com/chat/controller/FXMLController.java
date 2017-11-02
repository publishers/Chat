package com.chat.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import com.chat.model.Client;
import com.chat.model.Message;
import com.chat.socket.client.SocketClient;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by User on 29.01.15.
 */
public class FXMLController implements Initializable {
    @FXML
    public TextArea connectedUsers;
    @FXML
    public TextArea sendMessage;
    @FXML
    public TextArea showMessagesDialog;
    @FXML
    public TextField userName;

    private Client client;
    private Message message;
    private SocketClient socketClient;
    private BlockingQueue<Message> queueGetMessages;
    private BlockingQueue<Message> queueSendMessages;

    private int sizeText = 12;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        queueGetMessages = new LinkedBlockingQueue<>();
        queueSendMessages = new LinkedBlockingQueue<>();

        connectedUsers.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));
        showMessagesDialog.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));
        userName.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));

        sendMessage.setWrapText(true);
        showMessagesDialog.setWrapText(true);
        init();
    }

    private void init() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Message message = queueGetMessages.take();
                    showMessagesDialog.setText(showMessagesDialog.getText() + System.lineSeparator() + message.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Handler button Enter
     */
    public void pressEnter(KeyEvent event) {
        if (event.isShiftDown() && event.getCode() == KeyCode.ENTER) {
            StringBuffer sb = new StringBuffer(sendMessage.getText());
            sb.insert(sendMessage.getCaretPosition(), "\n");
            sendMessage.setText(String.valueOf(sb));
            sendMessage.positionCaret(sendMessage.getText().length());
        } else if (event.getCode() == KeyCode.ENTER) {
            message = new Message(sendMessage.getText(), new Date().toString());
            queueSendMessages.add(message);
        }
    }

    private boolean isConnect() {
        if (socketClient == null || socketClient.getSocket().isClosed()) {
            JOptionPane.showMessageDialog(null, "You can't send message something wrong with connection!");
            return false;
        }
        return true;
    }

    @FXML
    private void disconnect(ActionEvent actionEvent) throws IOException {
        if (socketClient != null) {
            socketClient.disConnect();
        }
    }

    @FXML
    private void connect(ActionEvent actionEvent) {
        String userName = this.userName.getText();
        if (isUserNickCorrect(userName)) {
            client = new Client(userName);
            doConnection(client);
        } else {
            JOptionPane.showMessageDialog(null, "You have bad userName '" + userName + "'");
        }
    }

    private void doConnection(Client client) {
        if (!isConnect()) {
            socketClient = new SocketClient(client, queueSendMessages, queueGetMessages);
            socketClient.start();
        }
    }

    private boolean isUserNickCorrect(String userName) {
        return userName.trim().matches("[A-Za-z0-9 ].*");
    }

    @FXML
    private void sendMessage(ActionEvent actionEvent) {
        if (isMessageCorrect(sendMessage.getText())) {
//            System.out.println("Send new message:  " + sendMessage.getText());
//            queueSendMessages.add(new Message(sendMessage.getText()));
        } else {
            JOptionPane.showMessageDialog(null, "Your message is not correct!");
        }
    }

    private boolean isMessageCorrect(String text) {
        return !text.trim().isEmpty();
    }
}
