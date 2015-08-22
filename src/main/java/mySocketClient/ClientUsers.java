package mySocketClient;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by Admin on 02.04.15.
 */
public class ClientUsers extends Thread {
    private DataInputStream in;
    private TextArea connectedUsers;
    private Socket socket;
    public ClientUsers(Socket socket, TextArea connectedUsers) {
        this.connectedUsers = connectedUsers;
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("ClientUsers1");
        }
    }
    Object object;
    @Override
    public void run() {
        try {
            synchronized  (in) {
                while ((object = in.readUTF()) != null) {
                    Platform.runLater(() ->
                                    connectedUsers.setText(connectedUsers.getText() + "\n" + object)
                    );
                }
            }
        }catch (Exception e){
            System.err.println("ClientUsers2");
        }

    }
}

