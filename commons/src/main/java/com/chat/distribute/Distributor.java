package com.chat.distribute;

import com.chat.model.Client;

public interface Distributor<T, R> {
    void init(T t);

    void distribute(R obj, Client client);
}
