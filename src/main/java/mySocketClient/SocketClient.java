package mySocketClient;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 30.01.15.
 */
public class SocketClient implements Runnable {
    private BufferedReader in = null;
    private PrintWriter out = null;

    private Socket socket = null;
    private String host = "46.98.142.218";//"192.168.0.5";
    private int port = 8080;

    private TextArea getMessages;
    private TextArea connectedUsers;


    public SocketClient(String userName, TextArea getMessages, TextArea connectedUsers) {
        this.getMessages = getMessages;
        this.connectedUsers = connectedUsers;
        connect(userName);
    }

    private void connect(String userName) {
        try {
            socket = new Socket(host, port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println(userName);
            out.println("" + socket.getLocalPort());
            out.flush();

        } catch (IOException e) {
        }
    }

    public void disConnect() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("SocketClient");
        }
    }

    public void setMassage(String message) {
        out.println(message);
        out.flush();
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(message.split("/")[0].equals("")){
                    if(message.split("/")[1].equals("new")){
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
                } else {
                    SetGetMessage.setMessage(message);
                    Platform.runLater(() -> {
                                if (SetGetMessage.getMessage() != null) {
                                    getMessages.setText(getMessages.getText() + "\n"
                                            + SetGetMessage.getMessage());
                                    SetGetMessage.setMessage(null);
                                }
                            }
                    );
                }
            }
        } catch (IOException e) {
        }
    }

}

final class SetGetMessage {
    private static String message = null;

    private SetGetMessage() {}

    public synchronized static String getMessage() {
        return SetGetMessage.message;
    }

    public synchronized static void setMessage(String message) {
        SetGetMessage.message = message;
    }
}

