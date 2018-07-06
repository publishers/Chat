package com.chat.service;

public interface ObjectTransfer<T> {
    void send(T entity);

    T receive();
}
