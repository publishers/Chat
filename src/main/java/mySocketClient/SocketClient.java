package mySocketClient;

import client.status.StatusClient;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 30.01.15.
 */
public class SocketClient implements Runnable {
    private BufferedReader in = null;
    private PrintStream out = null;
    private StatusClient statusClient = StatusClient.DISCONNECT;
    private Socket socket = null;
    private String host = "localhost";
    private int port = 10222;

    private String PASS;

    private TextArea getMessages;
    private TextArea connectedUsers;

    public SocketClient(String userName, TextArea getMessages, TextArea connectedUsers) {
        this.getMessages = getMessages;
        this.connectedUsers = connectedUsers;
        statusClient = StatusClient.CONNECT;
        connect(userName);
    }

    public SocketClient(String userName, String pass) {
        PASS = pass;
        connect(userName);
    }

    public StatusClient getStatusClient(){
        return statusClient;
    }

    public void setStatusClient(StatusClient statusClient){
        this.statusClient = statusClient;
    }

    /*
    * Connect to the Server
    * */
    private void connect(String userName) {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintStream(socket.getOutputStream(), true, "UTF-8");
            if(PASS != null){
                out.println(userName);
                out.println(PASS);
                out.flush();
            } else {
                out.println(userName);
                out.println("" + socket.getLocalPort());
                out.flush();
            }
        } catch (IOException e) {
            statusClient = StatusClient.DISCONNECT;
            JOptionPane.showMessageDialog(null, "Connection error");
        }
    }

    public boolean Exist(){
        try {
                if (in.readLine().equals("true")) {
                    return true;
                }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    * Disconnect from the Server
    * */
    public void disConnect() {
        try {
            statusClient = StatusClient.DISCONNECT;
            socket.close();
        } catch (IOException e) {
            System.err.println("SocketClient");
        }
    }

    /*
    * Send Sender message
    * */
    public void setMassage(String message) {
        out.println(message);
        out.flush();
    }

    @Override
    public void run() {
        try {
            String message;
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized  (in) {
                while ((message = in.readLine()) != null) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (message.split("/").length > 1) {
                        if (message.split("/")[0].equals("")) {
                            if (message.split("/")[1].equals("new")) {
                                Platform.runLater(() -> connectedUsers.setText(""));
                                continue;
                            }
                            SetGetMessage.setMessage(message.split("/")[1]);
                            Platform.runLater(() -> {
                                        if (SetGetMessage.getMessage() != null) {
                                            connectedUsers.setText(connectedUsers.getText() + "\n"
                                                    + SetGetMessage.getMessage());
                                            SetGetMessage.setMessage(null);
                                        }
                                    }
                            );
                        }
                    } else {
                        SetGetMessage.setMessage(message);
                        Platform.runLater(() -> {
                                    if (SetGetMessage.getMessage() != null) {
                                        getMessages.setText(getMessages.getText() + "\n" + SetGetMessage.getMessage());
                                        SetGetMessage.setMessage(null);
                                    }

                                }
                        );
                    }
                    Platform.runLater(() -> getMessages.setScrollTop(Double.MAX_VALUE));
                }

            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "You was disconnected!");
            statusClient = StatusClient.DISCONNECT;
            Platform.runLater(() -> {
                        connectedUsers.setText("");
                        getMessages.setText("");
                    }
            );
            disConnect();
        } catch (NullPointerException e ){
            statusClient = StatusClient.DISCONNECT;
        }
    }

}
