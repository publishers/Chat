package com.chat.utils;

import com.chat.model.Client;
import com.chat.model.Message;
import com.chat.service.distributor.MessageManager;

import java.time.LocalDateTime;

public final class ErrorSender {

    public static final void sendError(MessageManager messageManager) {
        messageManager.getData()
                .get(Message.class)
                .distribute(new Message(PublicMessages.SERVER_NOT_AVAILABLE, LocalDateTime.now()), Client.notConnected());
    }
}
