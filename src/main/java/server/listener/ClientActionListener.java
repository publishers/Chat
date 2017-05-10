package server.listener;

import model.Message;
import server.response.ClientListener;

import java.util.Collections;
import java.util.List;

/**
 * @author Serhii_Malykhin
 */
public class ClientActionListener {
    private List<ClientListener> clientListeners;

    public ClientActionListener() {
        clientListeners = Collections.synchronizedList(clientListeners);
    }

    public void sendMessage(Message message) {
        clientListeners.forEach(client -> client.sendMessage(message));
    }
}
