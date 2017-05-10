package socket.listener;

import model.Client;
import model.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Serhii_Malykhin
 */
public class ClientListener extends Thread{
    private Socket socket;
    private Client client;
    private InputStream in = null;
    private OutputStream out = null;
    private MessageTransfer messageListener;
    public ClientListener(Socket socketClient, Client client) {
        this.socket = socketClient;
        this.client = client;
        this.messageListener = MessageTransfer.getInstance();
    }


    private void init() throws IOException {
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }

    @Override
    public void run() {
        try {
            init();
            while (!socket.isConnected()) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Object obj = in.readObject();
                if(obj instanceof Message) {
                    messageListener.queueGetMessageFromServer((Message)obj);
                }
            }
        }catch (IOException|ClassNotFoundException|InterruptedException e){
            System.err.println("ClientListener Error: " + e.getLocalizedMessage());
        }
    }
}
