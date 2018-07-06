package com.chat.service.impl;

import com.chat.service.ObjectTransfer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Getter
@Service
public class ResponseObjectTransfer implements ObjectTransfer<Object> {

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
