package com.chat.server.response;

import com.chat.model.Client;
import com.chat.model.Message;
import com.chat.model.status.StatusClient;
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
    private final CommonClientListener actionListener;
    private ObjectOutputStream outSendMessage;

    private ClientListener(Socket socket, CommonClientListener actionListener) {
        this.socket = socket;
        this.actionListener = actionListener;
        actionListener.add(this);
    }

    public Socket getSocket() {
        return socket;
    }

    public static ClientListener newInstance(Socket socket, CommonClientListener observer) {
        return new ClientListener(socket, observer);
    }

    public void sendMessage(Message message) {
        try {
            outSendMessage.writeObject(message);
            outSendMessage.flush();
        } catch (IOException e) {
            LOG.error(
                    "Cannot send message: "
                    + message + System.lineSeparator()
                    +"Exception message: "
                    + e.getMessage()
            );
        }
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            outSendMessage = out;
            while (!socket.isClosed()) {
                Object obj = in.readObject();
                if (obj instanceof Client) {
                    Client client = (Client) obj;
                    out.writeObject(changeStatusClient(client));
                } else if (obj instanceof Message) {
                    actionListener.sendMessage((Message) obj);
                }
            }
        } catch (IOException e) {
            LOG.error("Bad Connection: {}", e.getMessage());
        } catch (ClassNotFoundException e) {
            LOG.error("Class not Found: {}", e.getMessage());
        }
    }

    private Client changeStatusClient(Client client) {
        if (client.getStatus() == StatusClient.CONNECT) {
            client.setStatus(StatusClient.DISCONNECT);
        } else {
            client.setStatus(StatusClient.CONNECT);
        }
        return client;
    }
}
