package com.chat.server;

import com.chat.server.listener.CommonClientListener;
import com.chat.server.response.ClientListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple socket server
 *
 * @author Serhii Malykhin
 */
public class SocketServer extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(SocketServer.class);
    private static final Integer PORT = 10222;
    private static final CommonClientListener OBSERVER = CommonClientListener.newInstance();
    public static void main(String[] args) {
        new SocketServer().start();
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(PORT);
            while (!serverSocket.isClosed()) {
                try {
                    LOG.info("Wait1 ... ");
                    Socket socket = serverSocket.accept();

                    ClientListener clientListener = ClientListener.newInstance(socket, OBSERVER);
                    clientListener.start();
                } catch (Exception e) {
                    LOG.error("SocketServer: {}", e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            LOG.error("Cannot establish connection. Server may not be up. Reason {}", e.getMessage());
        }
    }
}