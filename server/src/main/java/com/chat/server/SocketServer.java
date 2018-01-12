package com.chat.server;

import com.chat.server.listener.StreamClientConnection;
import com.chat.server.listener.ObserverClientListener;
import com.chat.server.listener.manager.ClientManager;
import com.chat.server.response.ClientListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A simple socket server
 *
 * @author Serhii Malykhin
 */
public class SocketServer extends Thread {
  private static final Logger LOG = LoggerFactory.getLogger(SocketServer.class);

  public static void main(String[] args) {
    new SocketServer().start();
  }

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
          LOG.error("SocketServer: {}", e.getMessage());
        }
      }
      clientManager.closedConnection();
    } catch (IOException e) {
      LOG.error("Cannot establish connection. Server may not be up. Reason {}", e.getMessage());
    }
  }
}