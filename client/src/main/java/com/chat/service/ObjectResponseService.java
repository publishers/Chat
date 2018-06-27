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
public class ObjectResponseService implements Service<Object> {

    @Autowired
    @Qualifier("responseLinkedBlockingQueue")
    private LinkedBlockingQueue<Object> queueGetData;

    @Override
    public void send(Object entity) {
        queueGetData.add(entity);
    }

    @Override
    public Object receive() {
        try {
            return queueGetData.take();
        } catch (InterruptedException e) {
            return new RuntimeException(e);
        }
    }
}
