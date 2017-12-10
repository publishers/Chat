package com.chat.server.response;

import com.chat.model.Client;
import com.chat.model.StatusClient;
import com.chat.server.listener.CommonClientListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Serhii_Malykhin
 */
public class ClientListener extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(ClientListener.class);
    private final Socket socket;
    private final CommonClientListener commonClientListener;
    private ObjectOutputStream outSendMessage;
    private Client client;

    private ClientListener(Socket socket, CommonClientListener commonClientListener) {
        this.socket = socket;
        this.commonClientListener = commonClientListener;
    }

    public Socket getSocket() {
        return socket;
    }

    public static ClientListener newInstance(Socket socket, CommonClientListener observer) {
        return new ClientListener(socket, observer);
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            outSendMessage = new ObjectOutputStream(socket.getOutputStream());
            commonClientListener.add(this);
            while (!socket.isClosed()) {
                Object obj = in.readObject();
                if (obj instanceof Client) {
                    client = (Client) obj;
                    changeStatusClient(client);
                }
                LOG.info("new message: {}", obj);
                commonClientListener.sendMessage(obj);
            }
        } catch (Exception e) {
            LOG.info("Client was disconnected: {}", client);
            commonClientListener.removeClientListener(this);
            commonClientListener.sendMessage(client);
        }
    }

    public void sendMessage(Object message) {
        try {
            outSendMessage.writeObject(message);
            outSendMessage.flush();
        } catch (IOException e) {
            LOG.info(
                    "Cannot send message: "
                            + message + System.lineSeparator()
                            + "Exception message: "
                            + e.getMessage()
            );
        }
    }

    public Client getClient() {
        return client;
    }

    private Client changeStatusClient(Client client) {
        client.setStatus(StatusClient.CONNECT);
        return client;
    }
}
