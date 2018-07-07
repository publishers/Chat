package com.chat.controller;

import com.chat.distribute.Distributor;
import com.chat.model.Client;
import com.chat.model.Message;
import com.chat.service.ClientService;
import com.chat.service.distributor.MessageManager;
import com.chat.utils.PublicMessages;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.web.WebView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.ResourceBundle;

import static com.chat.utils.ChatValidator.isMessageCorrect;
import static com.chat.utils.ChatValidator.isUserNickCorrect;

@Slf4j
public class ViewController implements Initializable {
    private static final int TEXT_SIZE = 12;

    public WebView htmlMessageView;
    @FXML
    public TextArea userView;
    @FXML
    public TextArea sendMessage;
    @FXML
    public TextArea messagesView;
    @FXML
    public TextField userName;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MessageManager messageManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font font = Font.font("Verdana", FontPosture.ITALIC, TEXT_SIZE);
        userView.setFont(font);
        messagesView.setFont(font);
        userName.setFont(font);

        sendMessage.setWrapText(true);
        messagesView.setWrapText(true);
    }

    @PostConstruct
    public void init() {
        Map<Class, Distributor> distributorMap = messageManager.getData();
        distributorMap.get(Client.class).init(userView);
        distributorMap.get(Message.class).init(messagesView);
    }

    @PreDestroy
    public void destroy() {
        clientService.disconnect();
    }

    @FXML
    private void disconnect() {
        clientService.disconnect();
        messagesView.setText(PublicMessages.DISCONNECTED_MESSAGE);
        userView.setText(PublicMessages.BLANK);
    }

    @FXML
    private void connect() {
        String userName = this.userName.getText();
        if (isUserNickCorrect(userName) && !clientService.isClientRegistered()) {
            messagesView.setText(PublicMessages.BLANK);
            Client client = new Client(userName);
            clientService.registerClient(client);
            clientService.activateConnection();
        } else {
            messagesView.setText("You have bad userName: " + userName);
        }
    }

    @FXML
    private void sendMessage() {
        String textMessage = sendMessage.getText();
        if (isMessageCorrect(textMessage) && clientService.isConnectionOpen()) {
            sendMessage.setText("");
            sendMessage.positionCaret(0);
            Message message = new Message(textMessage.trim(), LocalDateTime.now());
            clientService.sendMessage(message);
        } else if (!clientService.isConnectionOpen()) {
            messagesView.setText(PublicMessages.SERVER_NOT_AVAILABLE);
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
