package model;

import mySocketClient.Sender;
import status.StatusClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by User on 30.01.15.
 */
public class Client {
    private Socket socket;
    private StatusClient statusClient;
    private String userName;
    private String port;
    private Sender sender;
    private BufferedReader ois;

    public Client(Socket socket, Sender sender){
        this.socket = socket;
        this.sender = sender;
        statusClient = StatusClient.CONNECT;
        setName();
    }

    private void setName(){
        try {
            ois = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userName = ois.readLine();
            port = ois.readLine();
        } catch (IOException e) {
            System.err.println("Client IOException " + e);
        }
    }

    public void setStatusClient(StatusClient statusClient){
        this.statusClient = statusClient;
    }

    public String getUserName(){
        return userName;
    }

    public String getPort(){
        return port;
    }

    public synchronized StatusClient getStatusClient() {
        return statusClient;
    }

    public synchronized Socket getSocketClient(){
        return socket;
    }

    @Override
    public String toString() {
        return userName;
    }
}

