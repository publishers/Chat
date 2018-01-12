package com.chat.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

// TODO: need to separate connection and read/write actions
public class ClientConnection extends Thread {
  private static final Logger LOG = LoggerFactory.getLogger(ClientConnection.class);
  private static final String HOST = "localhost";
  private static final int PORT = 10222;
  private static final String MESSAGE_CONNECTION_WAS_CLOSED = "Connection was closed or unavailable. \nPlease check internet connection";

  private InputStream in = null;
  private OutputStream out = null;

  private Socket socket = null;
  private BlockingQueue<Object> queueSendData;
  private BlockingQueue<Object> queueGetData;

  public ClientConnection(BlockingQueue<Object> queueSendData, BlockingQueue<Object> queueGetData) {
    this.queueGetData = queueGetData;
    this.queueSendData = queueSendData;
  }

  private void serverConnection() throws IOException {
    socket = new Socket(HOST, PORT);
    out = socket.getOutputStream();
    in = socket.getInputStream();
  }

  public void disconnectFromServer() throws IOException {
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
      serverConnection();
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
      JOptionPane.showMessageDialog(null, MESSAGE_CONNECTION_WAS_CLOSED);
      LOG.error("info: {}", e.getMessage());
    }
  }
}
