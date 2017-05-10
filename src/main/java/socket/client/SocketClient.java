package socket.client;

import client.Client;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by User on 30.01.15.
 */
public class SocketClient extends Thread {
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    private Client client;
    private Socket socket = null;
    private String host = "localhost";
    private int port = 10222;

    public SocketClient(Client client) {
        this.client = client;
    }

    /**
     * Connect to the Server
     */
    private void connect() throws IOException {
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Disconnect from the Server
     */
    public void disConnect() {
        interrupt();
    }

    @Override
    public void run() {
        Client client;
        try {
            connect();
            while ((client = (Client) in.readObject()) != null) {
                System.out.println(client.getMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Connection error");
            System.err.println("Error: " + e.getMessage());
        }
    }
}
