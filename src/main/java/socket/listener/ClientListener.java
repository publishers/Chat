package socket.listener;

import model.Client;
import model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author Serhii_Malykhin
 */
public class ClientListener extends Thread{
    private Socket socket;
    private Client client;
    private ObjectInputStream in = null;
    private MessageTransfer messageListener;

    private ClientListener(ObjectInputStream inputStream, Client client) {
        this.client = client;
        this.in = inputStream;
        this.messageListener = MessageTransfer.newInstance();
    }

    public static ClientListener newInstance(ObjectInputStream inputStream, Client client){
        return new ClientListener(inputStream, client);
    }

    @Override
    public void run() {
        try {
            Object obj;
            while ((obj = in.readObject()) != null) {
                System.out.println("listener.ClientListener read object typeName: " + obj.getClass().getTypeName());
                if(obj instanceof Message) {
                    messageListener.queueGetMessageFromServer((Message)obj);
                    System.out.println("send message to queue " + obj);
                } else {

                }
            }
            System.out.println("ClientListener is out.");
        }catch (IOException|ClassNotFoundException|InterruptedException e){
            System.err.println("ClientListener Error: " + e.getMessage());
        }
    }
}
