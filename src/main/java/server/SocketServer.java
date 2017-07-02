package server;

import model.Client;
import model.status.StatusClient;
import server.control.ClientControl;
import server.listener.ClientActionListener;
import server.response.ClientListener;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple socket server
 *
 * @author faheem
 */
public class SocketServer extends Thread{
    private static final Integer PORT = 10222;

    public static void main(String[] args) {
        new SocketServer().start();
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Work");
        } catch (Exception e) {
            System.err.println("Cannot establish connection. Server may not be up." + e.getMessage());
        }

        ClientControl clientControl = ClientControl.newInstance();
        while (true) {
            try {
                System.out.println("Wait1 ... ");
                Socket socket = serverSocket.accept();

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                Object obj = in.readObject();
                if(obj instanceof Client) {
                    Client client = (Client) obj;
                    out.writeObject(changeStatusClient(client));
                    ClientListener.newInstance(client, socket, ClientActionListener.newInstance());
                }


            } catch (Exception e) {
                System.err.println("SocketServer 1");
            }
        }
    }

    private Client changeStatusClient(Client client){
        client.setStatus(StatusClient.CONNECT);
        return client;
    }
}