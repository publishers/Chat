package socket.client;

import model.Client;
import model.Message;
import socket.listener.ClientListener;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by User on 30.01.15.
 */
public class SocketClient {
    private InputStream in = null;
    private OutputStream out = null;

    private Client client;
    private Socket socket = null;
    private String host = "localhost";
    private int port = 10222;
    private BlockingQueue<Message> queueSendMessages;
    private BlockingQueue<Message> queueGetMessages;

    public SocketClient(Client client, BlockingQueue<Message> queueSendMessages, BlockingQueue<Message> queueGetMessages) {
        this.client = client;
        this.queueGetMessages = queueGetMessages;
        this.queueSendMessages = queueSendMessages;
    }

    /**
     * Connect to the Server
     */
    private void connect() throws IOException {
        socket = new Socket(host, port);
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }

    /**
     * Disconnect from the Server
     */
    public void disConnect() {
    }

    public Socket getSocket() {
        return socket;
    }

    public void run() {
        try {
            connect();
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(client);
            ObjectInputStream inputStream = new ObjectInputStream(in);

            Object obj = inputStream.readObject();
            if (obj instanceof Client) {
                client = (Client) obj;
            }
            ClientListener.newInstance(socket, client).start();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Connection error");
            System.err.println("Error: " + e.getMessage());
        }
    }
}
