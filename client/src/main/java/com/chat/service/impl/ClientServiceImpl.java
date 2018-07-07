package com.chat.service.impl;

import com.chat.ConnectionStatus;
import com.chat.model.Client;
import com.chat.model.Message;
import com.chat.service.ClientService;
import com.chat.service.ConnectorService;
import com.chat.service.distributor.MessageManager;
import com.chat.service.listener.ObjectTransfer;
import com.chat.service.listener.impl.MessageReceiverListener;
import com.chat.service.listener.impl.MessageSenderListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.Socket;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    @Value("${connection.port}")
    private int port;

    @Value("${connection.host}")
    private String host;

    @Autowired
    @Qualifier("requestObjectTransfer")
    private ObjectTransfer requestObjectTransfer;

    @Autowired
    private MessageManager messageManager;

    private MessageSenderListener senderManager;
    private MessageReceiverListener receiverManager;

    private ConnectorService connectorService;

    private Client client;

    @Override
    public void disconnect() {
        if (connectorService != null && connectorService.isConnected()) {
            connectorService.disconnect();
            connectorService = null;
            senderManager.interrupt();
            senderManager = null;
            receiverManager.interrupt();
            receiverManager = null;
        }
    }

    @Override
    public void sendMessage(Message message) {
        log.info("Message: {}", message);
        requestObjectTransfer.send(message);
    }

    @Override
    public boolean isClientRegistered() {
        return client != null && client.getStatus() == ConnectionStatus.CONNECTED;
    }

    @Override
    public void registerClient(Client client) {
        this.client = client;
    }

    @Override
    public void activateConnection() {
        if (connectorService == null || !connectorService.isConnected()) {
            connectorService = new ConnectorServiceImpl(host, port);
            connectorService.connect();
            Socket socket = connectorService.getSocket();
            senderManager = new MessageSenderListener(requestObjectTransfer, socket);
            senderManager.start();
            receiverManager = new MessageReceiverListener(messageManager, socket);
            receiverManager.start();
            requestObjectTransfer.send(client);
        }
    }

    @Override
    public boolean isConnectionOpen() {
        return connectorService != null && connectorService.isConnected();
    }
}
