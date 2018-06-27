package com.chat.server;

import com.chat.server.listener.ObserverClientListener;
import com.chat.server.listener.StreamClientConnection;
import com.chat.server.listener.manager.ClientManager;
import com.chat.server.response.ClientListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@Slf4j
public class SocketServer extends Thread {
    @Override
    public void run() {
        try {
            ObserverClientListener clientObserver = ObserverClientListener.newInstance();
            ClientManager clientManager = new ClientManager();
            while (!clientManager.isConnectionClosed()) {
                try {
                    StreamClientConnection streamClientConnection = clientManager.awaitClient();
                    ClientListener clientListener = ClientListener.newInstance(streamClientConnection, clientObserver);
                    clientListener.start();
                } catch (Exception e) {
                    log.error("SocketServer: {}", e.getMessage());
                }
            }
            clientManager.closedConnection();
        } catch (IOException e) {
            log.error("Cannot establish connection. Server may not be up. Reason {}", e.getMessage());
        }
    }
}