package server.listener;

import model.Message;
import server.response.ServerClientListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Serhii_Malykhin
 */
public class ClientActionListener {
    private List<ServerClientListener> serverClientListeners;

    private static ClientActionListener listener;

    private ClientActionListener() {
        serverClientListeners = Collections.synchronizedList(new LinkedList<>());
    }

    public synchronized static ClientActionListener newInstance(){
        if(listener == null){
            listener = new ClientActionListener();
        }
        return listener;
    }

    public synchronized void sendMessage(Message message) {
        List<ServerClientListener> unConnectedClients = new LinkedList<>();
        serverClientListeners.forEach(client -> {
            if(!client.getSocket().isClosed()) {
                System.out.println("send message to: " + client);
                client.sendMessage(message);
            } else {
                System.out.println("Client unconnected, cannot send message to: " + client);
                unConnectedClients.add(client);
            }
        });
        unConnectedClients.forEach(client -> {
            System.out.println("unconnected: " + client);
            if(serverClientListeners.contains(client)){
                serverClientListeners.remove(client);
            }
        });
    }

    public void add(ServerClientListener serverClientListener){
        System.out.println("client was added: " + serverClientListener.getName());
        serverClientListeners.add(serverClientListener);
    }

}
