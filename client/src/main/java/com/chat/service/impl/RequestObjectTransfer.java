package com.chat.service.impl;

import com.chat.service.ObjectTransfer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Getter
@Service
public class RequestObjectTransfer implements ObjectTransfer<Object> {

    @Autowired
    @Qualifier("requestLinkedBlockingQueue")
    private LinkedBlockingQueue<Object> queueSendData;

    @Override
    public void send(Object entity) {
        queueSendData.add(entity);
    }

    @Override
    public Object receive() {
        try {
            return queueSendData.take();
        } catch (InterruptedException e) {
            return new RuntimeException(e);
        }
    }
}
