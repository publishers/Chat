package com.chat.service;

import java.net.Socket;

public interface ConnectorService {
    void connect();

    void disconnect();

    boolean isConnected();

    Socket getSocket();
}
