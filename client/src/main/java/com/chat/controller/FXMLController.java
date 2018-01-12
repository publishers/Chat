package com.chat.controller;

import com.chat.model.Client;
import com.chat.model.Message;
import com.chat.socket.client.ClientConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class FXMLController implements Initializable {
  private static final Logger LOG = LoggerFactory.getLogger(FXMLController.class);
  private static final int sizeText = 12;

  @FXML
  public TextArea connectedUsers;
  @FXML
  public TextArea sendMessage;
  @FXML
  public TextArea showMessagesDialog;
  @FXML
  public TextField userName;
  @FXML
  public WebView htmlMessageView;

  private Client client;
  private ClientConnection clientConnection;
  private BlockingQueue<Object> queueGetData;
  private BlockingQueue<Object> queueSendData;
  private StringBuilder builderMessageDialog;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    builderMessageDialog = new StringBuilder();

    connectedUsers.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));
    showMessagesDialog.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));
    userName.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));

    sendMessage.setWrapText(true);
    showMessagesDialog.setWrapText(true);
  }

  private void init() {
    queueGetData = new LinkedBlockingQueue<>();
    queueSendData = new LinkedBlockingQueue<>();
    Thread thread = new Thread(() -> {
      while (true) {
        try {
          Object obj = queueGetData.take();
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
        } catch (InterruptedException e) {
          LOG.error("Error: {}", e.getMessage());
        }
      }
    });
    thread.setDaemon(true);
    thread.start();
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

  private boolean isConnect() {
    if (clientConnection.getSocket().isClosed()) {
      JOptionPane.showMessageDialog(null, "You can't send message something wrong with connection!");
      return false;
    }
    return true;
  }

  @FXML
  private void disconnect() throws IOException {
    if (clientConnection != null) {
      clientConnection.disconnectFromServer();
      clientConnection = null;
      showMessagesDialog.setText("");
    }
  }

  @FXML
  private void connect() {
    init();
    String userName = this.userName.getText();
    if (isUserNickCorrect(userName)) {
      client = new Client(userName);
      doConnection();
      queueSendData.add(client);
    } else {
      JOptionPane.showMessageDialog(null, "You have bad userName '" + userName + "'");
    }
  }

  private void doConnection() {
    if (clientConnection == null || clientConnection.getSocket() == null || !isConnect()) {
      clientConnection = new ClientConnection(queueSendData, queueGetData);
      clientConnection.start();
    }
  }

  private boolean isUserNickCorrect(String userName) {
    return userName.trim().matches("[A-Za-z0-9 ]{2,}");
  }

  @FXML
  private void sendMessage() {
    String message = sendMessage.getText();
    htmlMessageView.getEngine().loadContent(message);

    if (isMessageCorrect(message)) {
      sendMessage(message);
    } else {
      JOptionPane.showMessageDialog(null, "Your message is not correct!");
    }
  }

  private boolean isMessageCorrect(String text) {
    LOG.info("user message: '" + text + "'");
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
    if (clientConnection != null && clientConnection.getSocket() != null) {
      Message message = new Message(client.getUserName() + ": " + messageText.trim(), new Date().toString());
      queueSendData.add(message);
      sendMessage.setText("");
      sendMessage.positionCaret(0);
    } else {
      showMessagesDialog.setText("You need to connect");
    }
  }
}
