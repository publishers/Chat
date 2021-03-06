package com.chat.server.listener;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
public class StreamClientConnection {
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public StreamClientConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private ObjectInputStream readObjectStream() throws IOException {
        return new ObjectInputStream(clientSocket.getInputStream());
    }

    private ObjectOutputStream sendObjectStream() throws IOException {
        return new ObjectOutputStream(clientSocket.getOutputStream());
    }

    public void initStreamsReadAndWrite() throws IOException {
        inputStream = readObjectStream();
        outputStream = sendObjectStream();
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return inputStream.readObject();
    }

    public void sendObject(Object object) throws IOException {
        log.trace("Send object: {}", object);
        outputStream.writeObject(object);
        outputStream.flush();
    }

    public boolean isConnectionClosed() {
        return clientSocket.isClosed();
    }
}
