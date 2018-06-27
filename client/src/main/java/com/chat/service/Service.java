package com.chat.service;

public interface Service<T> {
    void send(T entity);

    T receive();
}
