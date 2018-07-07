package com.chat.service.listener.impl;

import com.chat.service.listener.ObjectTransfer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
public class MessageSenderListener extends AbstractMessageListener {

    private ObjectTransfer requestObjectTransfer;

    public MessageSenderListener(ObjectTransfer requestObjectTransfer, Socket socket) {
        super(socket);
        this.requestObjectTransfer = requestObjectTransfer;
    }

    @Override
    public void run() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
            while (!socket.isClosed()) {
                Object object = requestObjectTransfer.receive();
                log.info("responseObjectTransfer message: {}", object);
                if (!socket.isClosed()) {
                    outputStream.writeObject(object);
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            log.error("Cause message: {}", e);
        }
    }

}
