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

    private static ClientActionListener listener;

    private ClientActionListener() {
        clientListeners = Collections.synchronizedList(clientListeners);
    }

    public synchronized static ClientActionListener newInstance(){
        if(listener == null){
            listener = new ClientActionListener();
        }
        return listener;
    }

    public synchronized void sendMessage(Message message) {
        clientListeners.forEach(client -> client.sendMessage(message));
    }

    public void add(ClientListener clientListener){
        clientListeners.add(clientListener);
    }

}
