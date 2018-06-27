package com.chat.server.listener.manager;

import com.chat.server.listener.StreamClientConnection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;

@Slf4j
public class ClientManager {
    private static final Integer PORT = 10222;

    private ServerSocket serverSocket;

    public ClientManager() {
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            log.error("Port is not available: {}, error message: {}", PORT, e.getMessage());
        }
    }

    public StreamClientConnection awaitClient() throws IOException {
        return new StreamClientConnection(serverSocket.accept());
    }

    public boolean isConnectionClosed() {
        return serverSocket.isClosed();
    }

    public void closedConnection() throws IOException {
        serverSocket.close();
    }
}
