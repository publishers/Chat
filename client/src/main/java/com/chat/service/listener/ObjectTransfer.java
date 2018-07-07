package com.chat.service.listener;

public interface ObjectTransfer<T> {
    void send(T entity);

    T receive();
}
