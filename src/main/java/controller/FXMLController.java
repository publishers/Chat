package controller;

import client.status.StatusClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import mySocketClient.SocketClient;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

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

    public static String UName;
    public static String UPASS;

    public Button smileButton;
    public TextFlow smileField;
    public TextFlow textAndSm;
    private int sizeText = 12;

    private SocketClient sClient;
    private Thread threadsClient;

    private boolean conn = false;
    private boolean connNew = false;

    /*
    * Connect to the Server
    * */
    public void connectToServer(ActionEvent actionEvent) {
//        if(!conn)         {
            if (!userName.getText().equals("your name")) {
//                if (connNew){
//                    sClient = new SocketClient(UName, UPASS);
//                    if (sClient.Exist()) {
//                        connNew = true;
//                    }
//                }
                    sClient = new SocketClient(userName.getText(), getMessages, connectedUsers);
                    if (sClient.getStatusClient() == StatusClient.CONNECT) {
                        conn = true;
                    } else {
                        conn = false;
                    }
                    threadsClient = new Thread(sClient);
                    threadsClient.start();
            } else {
                JOptionPane.showMessageDialog(null, "Please Input Login");
            }
//        } else {
//            JOptionPane.showMessageDialog(null, "You was connected!");
//        }
    }

    /*
    * Sending message if pressed Button Send
    * */
    public void sendMessage(ActionEvent actionEvent) throws UnsupportedEncodingException{
        send();
    }

    /*
    * Send the message to the server and clear the message field.
    * */
    public void send(){
        if(conn) {
            if (!sendMessage.getText().trim().equals("Type your message here ...")) {
                if (!sendMessage.getText().trim().equals("")){
                    sClient.setMassage(sendMessage.getText().trim());
                    try {
                        Robot robot = new Robot();
                        robot.keyPress(java.awt.event.KeyEvent.VK_BACK_SPACE);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                }
                sendMessage.selectPositionCaret(Integer.MIN_VALUE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You need to connect! \n Please press the Connect Button!");
        }
    }

    /*
    * Disconnect the user.
    * */
    public void disConnect(ActionEvent actionEvent) {
//        if(conn) {
//            conn = false;
//            connNew = true;
            sClient.setStatusClient(StatusClient.DISCONNECT);
            sClient.disConnect();
            threadsClient.stop();
            connectedUsers.setText("");
            getMessages.setText("");
//        }
    }

    public void smiles(ActionEvent actionEvent) {
        if (smileField.isVisible()) {
            smileField.setVisible(false);
        } else {
            smileField.setVisible(true);
        }
    }

    /*
    * Handler button Enter
    * */
    public void pressEnter(KeyEvent event) {
        if (event.isShiftDown() && event.getCode() == KeyCode.ENTER) {
            StringBuffer sb = new StringBuffer(sendMessage.getText());
            sb.insert(sendMessage.getCaretPosition(),"\n");
            sendMessage.setText(String.valueOf(sb));
            sendMessage.positionCaret(sendMessage.getText().length());
        } else if (event.getCode() == KeyCode.ENTER) {
            send();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        userName.setEditable(false);
//        if (UName != null){
//            userName.setText(UName);
//            connNew = false;
//            connectToServer(new ActionEvent());
//            conn = false;
//        }

        connectedUsers.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));
        getMessages.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));
        userName.setFont(Font.font("Verdana", FontPosture.ITALIC, sizeText));

        sendMessage.setWrapText(true);
        getMessages.setWrapText(true);
        sendMessage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (sendMessage.getText().trim().equals("Type your message here ...")) {
                    sendMessage.setText("");
                    try {
                        Robot robot = new Robot();
                        robot.keyPress(java.awt.event.KeyEvent.VK_SPACE);
                        robot.keyPress(java.awt.event.KeyEvent.VK_BACK_SPACE);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (sendMessage.getText().trim().equals("")) {
                    sendMessage.setText("Type your message here ...");
                }
            }
        });
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

        for (int i = 1; i <= 10; i++) {
            Image im = new Image("/smile/" + i + ".gif");
            ImageView imv = new ImageView(im);
            imv.setFitHeight(30);
            imv.setFitWidth(30);
            imv.addEventHandler(ActionEvent.ACTION, (event) -> {
                textAndSm.getChildren().add(imv);
            });
            smileField.getChildren().add(imv);
        }

        smileField.setVisible(false);
        smileButton.setVisible(false);
        smileButton.setGraphic(new ImageView(new Image("head_smile.png")));
        textAndSm.getChildren().clear();

        textAndSm.getChildren().add(new Text("Hello"));
        textAndSm.getChildren().add(new ImageView(new Image("head_smile.png")));
        textAndSm.getChildren().add(new Text("\nHello"));

        textAndSm.setVisible(false);
    }

}
