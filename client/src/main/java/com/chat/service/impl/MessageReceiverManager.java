package com.chat.service.impl;

import com.chat.service.distributor.MessageManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

@Slf4j
public class MessageReceiverManager extends AbstractMessageManager {

    private MessageManager messageManager;

    public MessageReceiverManager(MessageManager messageManager, Socket socket) {
        super(socket);
        this.messageManager = messageManager;
    }

    @Override
    public void run() {
        try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            while (!socket.isClosed()) {
                Object obj = null;
                try {
                    obj = inputStream.readObject();
                } catch (ClassNotFoundException e) {
                    log.error("Class not found: {}", e);
                }
                log.info("Object: {}", obj);
                messageManager.manageMessage(obj);
            }
        } catch (IOException e) {
            log.error("Cause message: {}", e);
        }
    }

}
