package com.chat.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Serhii Malykhin
 */
@Getter
@org.springframework.stereotype.Service
public class ObjectRequestService implements Service<Object> {

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
