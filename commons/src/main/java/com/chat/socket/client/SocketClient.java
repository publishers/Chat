package com.chat.socket.client;

import com.chat.model.Client;
import com.chat.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class SocketClient extends Thread {
    public static final Logger LOG = LoggerFactory.getLogger(SocketClient.class);
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
    public void disConnect() throws IOException {
        if (socket != null && socket.isConnected()) {
            socket.close();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void run() {
        try {
            LOG.info("Start run Socket Client");
            connect();
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(client);
            ObjectInputStream inputStream = new ObjectInputStream(in);
//            ClientListener.newInstance(inputStream, client).start();
            LOG.info("Socket Client: Client Listener start work");
            Thread thread = new Thread(() -> {
                Message message = null;
                while (true) {
                    try {
                        LOG.info("wait for new Message");
                        message = queueSendMessages.take();
                        LOG.info("new Message: " + message);
                        if (!socket.isClosed()) {
                            outputStream.writeObject(message);
                            outputStream.flush();
                        } else {
                            LOG.info("Cannot send the message connection is closed!");
                        }
                        LOG.info("Send the message to Server");
                    } catch (InterruptedException e) {
                        System.err.println("Cannot got message " + message);
                    } catch (IOException e) {
                        System.err.println("Cannot send message " + message);
                    }
                }
            });
            thread.start();
            Object obj = null;
            while (!socket.isClosed()) {
                try {
                    LOG.info("-->wait for object");
                    obj = inputStream.readObject();
                    LOG.info("-->get Object " + obj);
                    if (obj instanceof Client) {
                        client = (Client) obj;
                        LOG.info("-->" + client);
                    } else if (obj instanceof Message) {
                        Message message = (Message) obj;
                        LOG.info("-->"+message);
                        queueGetMessages.add(message);
                    }
                } catch (IOException e) {
                    System.err.println("here we got unknown object: " + obj);
                    System.err.println("Error message: " + e.getMessage());
                    inputStream.defaultReadObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Connection error");
            System.err.println("Error: " + e.getMessage());
        }
    }
}
