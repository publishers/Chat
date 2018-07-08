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
import com.chat.utils.ErrorSender;
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
        message.setClient(client);
        requestObjectTransfer.send(message);
    }

    @Override
    public boolean isClientRegistered() {
        return client != null && connectorService != null && connectorService.isConnected() && client.getStatus() == ConnectionStatus.CONNECTED;
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
            if (connectorService.isConnected()) {
                initListeners(socket);
                requestObjectTransfer.send(client);
            } else {
                ErrorSender.sendError(messageManager);
            }
        }
    }

    private void initListeners(Socket socket) {
        senderManager = new MessageSenderListener(requestObjectTransfer, socket);
        senderManager.start();
        receiverManager = new MessageReceiverListener(messageManager, client, socket);
        receiverManager.start();
    }

    @Override
    public boolean isConnectionOpen() {
        return connectorService != null && connectorService.isConnected();
    }
}
