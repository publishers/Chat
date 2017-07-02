package socket.client;

import model.Client;
import model.Message;
import socket.listener.ClientListener;

import javax.swing.*;
import java.io.*;
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
        if (!socket.isConnected()) {
            socket = new Socket(host, port);
            out = socket.getOutputStream();
            in = socket.getInputStream();
        }
    }

    /**
     * Disconnect from the Server
     */
    public void disConnect() throws IOException {
        if (socket != null && socket.isConnected()) {
            socket.getChannel().close();
        }
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
            ClientListener.newInstance(socket, client).start();
            while (socket.isConnected()) {
                Object obj = inputStream.readObject();
                if (obj instanceof Client) {
                    client = (Client) obj;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Connection error");
            System.err.println("Error: " + e.getMessage());
        }
    }
}
