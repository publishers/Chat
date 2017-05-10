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
public class SocketClient implements Runnable {
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    private Client client;
    private Socket socket = null;
    private String host = "localhost";
    private int port = 10222;

    public SocketClient(Client client) {
        this.client = client;
        connect();
    }

    /**
     * Connect to the Server
     */
    private void connect() {
        try {
            socket = new Socket(host, port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Connection error");
        }
    }

    /**
     * Disconnect from the Server
     */
    public void disConnect() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("SocketClient");
        }
    }

    @Override
    public void run() {
        Client client;
        try {
            while( (client = (Client) in.readObject())!= null){
                System.out.println(client.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
