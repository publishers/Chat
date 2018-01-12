package com.chat.server.response;

import com.chat.model.Client;
import com.chat.model.StatusClient;
import com.chat.server.listener.StreamClientConnection;
import com.chat.server.listener.ObserverClientListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// TODO: need delegate the responsibility for sending and remove client.
// TODO: Client object has to be received before create clientListener object
public class ClientListener extends Thread {
  private static final Logger LOG = LoggerFactory.getLogger(ClientListener.class);
  private final ObserverClientListener observerClientListener;
  private final StreamClientConnection streamClientConnection;
  private Client client;

  private ClientListener(StreamClientConnection streamClientConnection, ObserverClientListener observerClientListener) {
    this.streamClientConnection = streamClientConnection;
    this.observerClientListener = observerClientListener;
  }

  public static ClientListener newInstance(StreamClientConnection streamClientConnection, ObserverClientListener observer) {
    return new ClientListener(streamClientConnection, observer);
  }

  @Override
  public void run() {
    try {
      streamClientConnection.initStreamsReadAndWrite();
      observerClientListener.add(this);
      while (!streamClientConnection.isConnectionClosed()) {
        Object obj = streamClientConnection.readObject();
        if (obj instanceof Client) {
          client = (Client) obj;
          changeStatusClient(client);
        }
        LOG.info("new message: {}", obj);
        observerClientListener.sendMessage(obj);
      }
    } catch (Exception e) {
      LOG.info("Client was disconnected: {}", client);
      observerClientListener.removeClientListener(this);
      observerClientListener.sendMessage(client);
    }
  }

  public void sendMessage(Object message) {
    try {
      streamClientConnection.sendObject(message);
    } catch (IOException e) {
      LOG.info(
          "Cannot send message: "
              + message + System.lineSeparator()
              + "Exception message: "
              + e.getMessage()
      );
    }
  }

  public Client getClient() {
    return client;
  }

  private Client changeStatusClient(Client client) {
    client.setStatus(StatusClient.CONNECT);
    return client;
  }
}
