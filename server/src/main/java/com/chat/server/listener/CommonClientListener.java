package com.chat.server.listener;

import com.chat.model.Client;
import com.chat.server.response.ClientListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void sendMessage(Object object) {
        clientListeners.forEach(client -> {
            if (object instanceof Client) {
                client.sendMessage(buildClientList());
            } else {
                client.sendMessage(object);
            }
        });
    }

    private List<Client> buildClientList() {
        return clientListeners.stream()
                .map(ClientListener::getClient)
                .collect(Collectors.toList());
    }

    public void removeClientListener(ClientListener clientListener) {
        clientListeners.remove(clientListener);
    }

    public void add(ClientListener clientListener) {
        clientListeners.add(clientListener);
    }

}
