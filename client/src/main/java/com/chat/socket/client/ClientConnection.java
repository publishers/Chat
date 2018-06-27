package com.chat.socket.client;

import com.chat.service.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

// TODO: need to separate connection and read/write actions
@Component
@Slf4j
public class ClientConnection {
    @Value("${connection.host}")
    private String HOST;

    @Value("${connection.port}")
    private int PORT;

    private InputStream in;
    private OutputStream out;

    private Socket socket;

    @Autowired
    @Qualifier("objectResponseService")
    private Service<Object> messageResponseService;

    @Autowired
    @Qualifier("objectRequestService")
    private Service<Object> messageRequestService;

    private Thread currentThread;

    public void disconnectFromServer() throws IOException {
        if (socket != null && socket.isConnected() && !socket.isClosed()) {
            in.close();
            out.close();
            socket.close();
            currentThread.interrupt();
            System.gc();
        }
    }

    public boolean isSocketClosed() {
        return socket == null || (socket.isClosed() && socket.isConnected());
    }

    public void start() {
        currentThread = new Thread(() -> {
            try {
                connectToServer();
                ObjectOutputStream outputStream = new ObjectOutputStream(out);
                ObjectInputStream inputStream = new ObjectInputStream(in);
                Thread thread = new Thread(sendData(outputStream));
                thread.setDaemon(true);
                thread.start();
                while (!(socket.isInputShutdown() && socket.isOutputShutdown())) {
                    Object obj = inputStream.readObject();
                    log.info("Object: {}", obj);
                    messageResponseService.send(obj);
                }
            } catch (IOException | ClassNotFoundException e) {
                log.error("info: {}", e.getMessage());
            }
        });
        currentThread.start();
    }

    private void connectToServer() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(HOST, PORT));
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }

    private Runnable sendData(ObjectOutputStream outputStream) {
        return () -> {
            while (true) {
                Object object = messageRequestService.receive();
                log.info("receive message: {}", object);
                sendData(outputStream, object);
            }
        };
    }

    private void sendData(ObjectOutputStream outputStream, Object object) {
        if (!socket.isClosed()) {
            try {
                outputStream.writeObject(object);
                outputStream.flush();
            } catch (IOException e) {
                log.info("Cannot send message: {}", object);
                log.error("Cause message: {}", e);
            }
        }
    }
}
