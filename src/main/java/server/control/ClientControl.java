package server.control;

import server.response.ClientListener;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Serhii_Malykhin
 */
public class ClientControl {
    private List<ClientListener> listeners;

    private ClientControl(){
        listeners = new LinkedList<>();
    }

    public static ClientControl newInstance(){
        return new ClientControl();
    }

    public void add(ClientListener clientActionListener){
        listeners.add(clientActionListener);
        clientActionListener.start();
    }

    public ClientListener get(int i){
        return listeners.get(i);
    }

    public List<ClientListener> getListeners() {
        return listeners;
    }
}
