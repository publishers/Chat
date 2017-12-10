package com.chat.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by User on 30.01.15.
 */
public class SocketClient extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(SocketClient.class);
    private static final String host = "localhost";
    private static final int port = 10222;

    private InputStream in = null;
    private OutputStream out = null;

    private Socket socket = null;
    private BlockingQueue<Object> queueSendData;
    private BlockingQueue<Object> queueGetData;

    public SocketClient(BlockingQueue<Object> queueSendData, BlockingQueue<Object> queueGetData) {
        this.queueGetData = queueGetData;
        this.queueSendData = queueSendData;
    }

    /**
     * Connect to the Server
     */
    private void connect() throws IOException {
        socket = new Socket(host, port);
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }

    /**
     * Disconnect from the Server
     */
    public void disConnect() throws IOException {
        if (socket != null && socket.isConnected()) {
            socket.close();
            socket = null;
            Thread.currentThread().interrupt();
            System.gc();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void run() {
        try {
            connect();
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            Thread thread = new Thread(() -> {
                Object object = null;
                while (true) {
                    try {
                        object = queueSendData.take();
                        if (!socket.isClosed()) {
                            outputStream.writeObject(object);
                            outputStream.flush();
                        }
                    } catch (InterruptedException e) {
                        LOG.info("Cannot got message: {}", object);
                    } catch (IOException e) {
                        LOG.info("Cannot send message: {}", object);
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
            while (!socket.isClosed()) {
                Object obj = inputStream.readObject();
                LOG.info("Object: {}", obj.toString());
                queueGetData.add(obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Connection was closed or unavailable. \nPlease check internet connection");
            LOG.info("info: {}", e.getMessage());
        }
    }
}
