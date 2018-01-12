package com.chat.server.listener.manager;

import com.chat.server.listener.StreamClientConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class ClientManager {
  private static final Logger LOG = LoggerFactory.getLogger(ClientManager.class);
  private static final Integer PORT = 10222;

  private ServerSocket serverSocket;

  public ClientManager() {
    try {
      this.serverSocket = new ServerSocket(PORT);
    } catch (IOException e) {
      LOG.error("Port is not available: {}, error message: {}", PORT, e.getMessage());
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
