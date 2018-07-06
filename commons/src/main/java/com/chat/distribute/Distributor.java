package com.chat.distribute;

public interface Distributor<T> {
    void init(T t);
    void distribute(Object obj);
}
