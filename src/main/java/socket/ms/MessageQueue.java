package socket.ms;

import client.Client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Serhii_Malykhin
 */
public class MessageQueue {
    private BlockingQueue<Client> messageQueue;

    private MessageQueue() {
        messageQueue = new LinkedBlockingQueue<>();
    }

    public void addClientMessage(Client client) {
        messageQueue.add(client);
    }

    public void sendClientMessage(Client client) {

    }
}
