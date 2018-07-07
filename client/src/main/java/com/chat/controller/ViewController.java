package com.chat.controller;

import com.chat.model.Client;
import com.chat.model.Message;
import com.chat.service.ClientService;
import com.chat.service.distributor.MessageManager;
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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.chat.utils.ChatValidator.isMessageCorrect;
import static com.chat.utils.ChatValidator.isUserNickCorrect;

@Slf4j
public class ViewController implements Initializable {
    private static final int TEXT_SIZE = 12;

    @FXML
    public TextArea connectedUsers;
    @FXML
    public TextArea sendMessage;
    @FXML
    public TextArea showMessagesDialog;
    @FXML
    public TextField userName;

    private Map<Class, TextArea> areaMap;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MessageManager messageManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectedUsers.setFont(Font.font("Verdana", FontPosture.ITALIC, TEXT_SIZE));
        showMessagesDialog.setFont(Font.font("Verdana", FontPosture.ITALIC, TEXT_SIZE));
        userName.setFont(Font.font("Verdana", FontPosture.ITALIC, TEXT_SIZE));

        sendMessage.setWrapText(true);
        showMessagesDialog.setWrapText(true);

        areaMap = new LinkedHashMap<>();
        areaMap.put(Client.class, connectedUsers);
        areaMap.put(Message.class, showMessagesDialog);
    }

    @PostConstruct
    public void init() {
        messageManager.getData().forEach((key, value) -> value.init(areaMap.get(key)));
    }

    @PreDestroy
    public void destroy() {
        clientService.disconnect();
    }
    @FXML
    private void disconnect() {
        clientService.disconnect();
    }

    @FXML
    private void connect() {
        String userName = this.userName.getText();
        if (isUserNickCorrect(userName) && !clientService.isClientRegistered()) {
            Client client = new Client(userName);
            clientService.registerClient(client);
            clientService.activateConnection();
        } else {
            showMessagesDialog.setText("You have bad userName: " + userName);
        }
    }

    @FXML
    private void sendMessage() {
        String textMessage = sendMessage.getText();
        if (isMessageCorrect(textMessage) && clientService.isConnectionOpen()) {
            sendMessage.setText("");
            sendMessage.positionCaret(0);
            Message message = new Message(textMessage.trim(), new Date().toString());
            clientService.sendMessage(message);
        }
    }

    @FXML
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

}
