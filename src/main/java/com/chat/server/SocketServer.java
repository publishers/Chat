package com.chat.server;

import com.chat.server.listener.ClientActionListener;
import com.chat.server.response.ServerClientListener;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple socket server
 *
 * @author faheem
 */
public class SocketServer extends Thread {
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
        ClientActionListener observer = ClientActionListener.newInstance();
        while (true) {
            try {
                System.out.println("Wait1 ... ");
                Socket socket = serverSocket.accept();

                ServerClientListener serverClientListener = ServerClientListener.newInstance(socket, observer);

                if (serverClientListener != null) {
                    serverClientListener.start();
                } else {
                    System.err.println("Something was wrong with socket");
                }
                System.out.println("Added new Client!");
            } catch (Exception e) {
                System.err.println("SocketServer " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}