package com.chat.controller;

import com.chat.model.Client;
import com.chat.model.Message;
import com.chat.service.Service;
import com.chat.socket.client.ClientConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
public class FXMLController implements Initializable {
    private static final int TEXT_SIZE = 12;

    @FXML
    public TextArea connectedUsers;
    @FXML
    public TextArea sendMessage;
    @FXML
    public TextArea showMessagesDialog;
    @FXML
    public TextField userName;
//    @FXML
//    public WebView htmlMessageView;

    @Autowired
    private ClientConnection clientConnection;

    @Autowired
    @Qualifier("objectResponseService")
    private Service<Object> messageResponseService;

    @Autowired
    @Qualifier("objectRequestService")
    private Service<Object> messageRequestService;

    private StringBuilder builderMessageDialog = new StringBuilder();

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectedUsers.setFont(Font.font("Verdana", FontPosture.ITALIC, TEXT_SIZE));
        showMessagesDialog.setFont(Font.font("Verdana", FontPosture.ITALIC, TEXT_SIZE));
        userName.setFont(Font.font("Verdana", FontPosture.ITALIC, TEXT_SIZE));

        sendMessage.setWrapText(true);
        showMessagesDialog.setWrapText(true);
    }

    private String updateTextArea(Message message) {
        return builderMessageDialog
                .append(message.getSendTime())
                .append(System.lineSeparator())
                .append(message.getMessage())
                .append(System.lineSeparator())
                .append(showMessagesDialog.getText().trim())
                .append(System.lineSeparator())
                .toString();
    }

    @FXML
    private void disconnect() throws IOException {
        if (!clientConnection.isSocketClosed()) {
            clientConnection.disconnectFromServer();
            showMessagesDialog.setText("");
        }
    }

    @FXML
    private void connect() {
        String userName = this.userName.getText();
        if (isUserNickCorrect(userName) && clientConnection.isSocketClosed()) {
            settingsReceiveMessages();
            client = new Client(userName);
            clientConnection.start();
            messageRequestService.send(client);
        } else {
            log.trace("You have bad userName '{}'", userName);
        }
    }

    private void settingsReceiveMessages() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Object obj = messageResponseService.receive();
                    if (obj instanceof Message) {
                        Message message = (Message) obj;
                        showMessagesDialog.setText(updateTextArea(message));
                        builderMessageDialog.delete(0, builderMessageDialog.length());
                    } else if (obj instanceof List) {
                        List<Client> clients = (List<Client>) obj;
                        String user = clients.stream()
                                .filter(Objects::nonNull)
                                .map(Client::getUserName)
                                .collect(Collectors.joining(System.lineSeparator()));
                        connectedUsers.setText(user);
                    }
                } catch (Exception e) {
                    log.error("Error: {}", e.getMessage());
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private boolean isUserNickCorrect(String userName) {
        return userName.trim().matches("[A-Za-z0-9 ]{2,}");
    }

    @FXML
    private void sendMessage() {
//        htmlMessageView.getEngine().loadContent(message);

        String message = sendMessage.getText();
        if (isMessageCorrect(message)) {
            sendMessage(message);
        } else {
            JOptionPane.showMessageDialog(null, "Your message is not correct!");
        }
    }

    private boolean isMessageCorrect(String text) {
        log.info("isMessageCorrect: '" + text + "'");
        return text != null && !text.trim().isEmpty();
    }

    public void pressEnter(KeyEvent event) {
        if (event.isShiftDown() && event.getCode() == KeyCode.ENTER) {
            StringBuffer sb = new StringBuffer(sendMessage.getText());
            sb.insert(sendMessage.getCaretPosition(), "\n");
            sendMessage.setText(String.valueOf(sb));
            sendMessage.positionCaret(sendMessage.getText().length());
        } else if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }

    private void sendMessage(String messageText) {
        if (!clientConnection.isSocketClosed()) {
            sendMessage.setText("");
            sendMessage.positionCaret(0);
            Message message = new Message(client.getUserName() + ": " + messageText.trim(), new Date().toString());
            messageRequestService.send(message);
        } else {
            showMessagesDialog.setText("You need to connect");
        }
    }
}
