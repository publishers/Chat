package start.server;

import client.Client;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple socket server
 * @author faheem
 *
 */
public class SocketServer {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(10222);
            System.out.println("Work");
        } catch (Exception e) {
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }

        Socket socket = null;

        while(true) {
            try {
                System.out.println("Wait1 ... ");
                socket = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                if (in.readObject()instanceof Client){
                    System.out.println("client");
                }

            } catch (Exception e) {
                System.err.println("SocketServer 1");
            }
        }

    }
}