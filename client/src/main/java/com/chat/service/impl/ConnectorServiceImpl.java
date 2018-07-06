package com.chat.service.impl;

import com.chat.service.ConnectorService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
public class ConnectorServiceImpl implements ConnectorService {

    private String host;
    private int port;

    @Getter
    private Socket socket;

    public ConnectorServiceImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Socket was closed with the error: {}", e.getMessage());
        }
    }

    public boolean isConnected() {
        return socket != null && (!socket.isClosed() && socket.isConnected());
    }

    public void connect() {
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            log.error("Connection was wrong: {}", e.getMessage());
            socket = null;
        }
    }

}
