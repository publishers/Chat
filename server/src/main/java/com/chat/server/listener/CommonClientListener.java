package com.chat.server.listener;

import com.chat.model.Message;
import com.chat.server.response.ClientListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Serhii_Malykhin
 */
public class CommonClientListener {
    private static final Logger LOG = LoggerFactory.getLogger(CommonClientListener.class);

    private List<ClientListener> clientListeners;

    private static CommonClientListener listener;

    private CommonClientListener() {
        clientListeners = Collections.synchronizedList(new LinkedList<>());
    }

    public static CommonClientListener newInstance() {
        if (listener == null) {
            listener = new CommonClientListener();
        }
        return listener;
    }

    public synchronized void sendMessage(Message message) {
        List<ClientListener> unConnectedClients = new LinkedList<>();

        clientListeners.forEach(client -> {
            if (!client.getSocket().isClosed()) {
                LOG.info("send message to: " + client);
                client.sendMessage(message);
            } else {
                LOG.info("Client unconnected, cannot send message to: " + client);
                unConnectedClients.add(client);
            }
        });

        unConnectedClients.forEach(client -> {
            LOG.info("unconnected: " + client);
            if (clientListeners.contains(client)) {
                clientListeners.remove(client);
            }
        });
    }

    public void add(ClientListener clientListener) {
        LOG.info("client was added: " + clientListener.getName());
        clientListeners.add(clientListener);
    }

}
