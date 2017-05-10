package server.response;

import model.Client;
import model.Message;
import server.listener.ClientActionListener;

import java.net.Socket;

/**
 * @author Serhii_Malykhin
 */
public class ClientListener {
    private final Client client;
    private final Socket socket;
    private final ClientActionListener actionListener;

    private ClientListener(Client client, Socket socket, ClientActionListener actionListener) {
        this.client = client;
        this.socket = socket;
        this.actionListener = actionListener;
    }

    private Client getClient() {
        return client;
    }

    private Socket getSocket() {
        return socket;
    }

    public void sendMessage(Message message){
        actionListener.sendMessage(message);
    }

}
