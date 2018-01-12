package com.chat.server.listener;

import com.chat.model.Client;
import com.chat.server.response.ClientListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii_Malykhin
 */
public class ObserverClientListener {

  private List<ClientListener> clientListeners;

  private static ObserverClientListener listener;

  private ObserverClientListener() {
    clientListeners = Collections.synchronizedList(new LinkedList<>());
  }

  public static ObserverClientListener newInstance() {
    if (listener == null) {
      listener = new ObserverClientListener();
    }
    return listener;
  }

  public void sendMessage(Object object) {
    clientListeners.forEach(client -> {
      if (object instanceof Client) {
        client.sendMessage(buildClientList());
      } else {
        client.sendMessage(object);
      }
    });
  }

  private List<Client> buildClientList() {
    return clientListeners.stream()
        .map(ClientListener::getClient)
        .collect(Collectors.toList());
  }

  public void removeClientListener(ClientListener clientListener) {
    clientListeners.remove(clientListener);
  }

  public void add(ClientListener clientListener) {
    clientListeners.add(clientListener);
  }

}
