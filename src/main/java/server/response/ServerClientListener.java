package server.response;

import model.Client;
import model.Message;
import model.status.StatusClient;
import server.listener.ClientActionListener;

import java.io.*;
import java.net.Socket;

/**
 * @author Serhii_Malykhin
 */
public class ServerClientListener extends Thread{
    private final Socket socket;
    private final ClientActionListener actionListener;
    private ObjectOutputStream outSendMessage;

    private ServerClientListener(Socket socket, ClientActionListener actionListener) throws IOException {
        this.socket = socket;
        this.actionListener = actionListener;
        actionListener.add(this);
    }

    public Socket getSocket() {
        return socket;
    }

    public static ServerClientListener newInstance(Socket socket, ClientActionListener observer){
        try {
            return new ServerClientListener(socket, observer);
        } catch (IOException e) {
            System.err.println("Cannot create ClientListener: " + e.getMessage());
        }
        return null;
    }

    public void sendMessage(Message message){
        try {
            System.out.println("outSendMessage: " + outSendMessage);
            System.out.println("message: " + message);
                outSendMessage.writeObject(message);
            outSendMessage.flush();
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
            System.out.println("Init inputStream");
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            outSendMessage = out;
            System.out.println("end Init inputStream");
            while(!socket.isClosed()) {
                System.out.println("wait for new Object");
                Object obj = in.readObject();
                System.out.println("new Object here ");
                if (obj instanceof Client) {
                    System.out.println("Here listener new Client");
                    Client client = (Client) obj;
                    out.writeObject(changeStatusClient(client));
                    System.out.println("Status client was changed");
                } else if (obj instanceof Message) {
                    System.out.println("Server got Message");
                    actionListener.sendMessage((Message) obj);
                    System.out.println("Server send Message");
                } else {
                    System.err.println("Bad object " + obj);
                }
            }
        } catch (IOException e) {
            System.out.println("Bad Connection");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not Found");
        }
    }

    private Client changeStatusClient(Client client) {
        if(client.getStatus() == StatusClient.CONNECT) {
            client.setStatus(StatusClient.DISCONNECT);
        } else {
            client.setStatus(StatusClient.CONNECT);
        }
        return client;
    }
}
