package mySocketClient;

import model.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by User on 22.01.15.
 */
public class ClientListener extends Thread {
    private Client client;
    private Sender sender;
    private BufferedReader in;

    public ClientListener(Client client, Sender sender) {
        this.client = client;
        this.sender = sender;
        try {
            in = new BufferedReader(new InputStreamReader(client.getSocketClient().getInputStream()));
        } catch (IOException e) {
            System.err.println("ClientListener 1");
        }
    }

    public String getMessage() {
        String message;
        try {
            while ((message = (in.readLine())) != null) {
                if (!message.equals("") || !message.equals("null")) {
                    sender.saveLastMessage(client.getUserName() , message);
                    sender.sendMessToAllClient(client.getUserName() + ": " + message);
                }
                else break;
            }
            return message;
        } catch (IOException e) {
            System.err.println(client.getUserName() + " was disconnect");
            sender.disconnectUser(client);
            return "";
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (getMessage().equals("")) {
                    stop();
                    break;
                }
            }
        }catch (Exception e){
            System.err.println(client.getUserName() + " was disconnect!");
            sender.disconnectUser(client);
        }

    }
}
