package com.chat.service;

import com.chat.model.Client;
import com.chat.model.Message;

public interface ClientService {

    void disconnect();

    void sendMessage(Message message);

    boolean isClientRegistered();

    void registerClient(Client client);

    void activateConnection();

}
