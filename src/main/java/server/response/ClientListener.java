package server.response;

import model.Client;
import model.Message;
import server.listener.ClientActionListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Serhii_Malykhin
 */
public class ClientListener extends Thread{
    private final Client client;
    private final Socket socket;
    private final ClientActionListener actionListener;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private ClientListener(Client client, Socket socket, ClientActionListener actionListener) throws IOException {
        this.client = client;
        this.socket = socket;
        this.actionListener = actionListener;
        actionListener.add(this);
        init();
    }

    private void init() throws IOException {
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    public static ClientListener newInstance(Client client, Socket socket, ClientActionListener actionListener){
        try {
            return new ClientListener(client, socket, actionListener);
        } catch (IOException e) {
            System.out.println("Cannot create ClientListener: " + e.getMessage());
        }
        return null;
    }

    private Client getClient() {
        return client;
    }

    private Socket getSocket() {
        return socket;
    }

    public void sendMessage(Message message){
        try {
            out.writeObject(message);
        } catch (IOException e) {
            System.err.println(
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

            while(socket.isConnected()){
                Object obj = in.readObject();
                if(obj instanceof Message) {
                    actionListener.sendMessage((Message) obj);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
