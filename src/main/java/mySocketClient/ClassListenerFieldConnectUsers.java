package mySocketClient;


import javafx.scene.control.TextArea;
import model.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by Admin on 26.02.15.
 */
public final class ClassListenerFieldConnectUsers extends Thread{
    private Socket socket;
    private TextArea textArea;
    private ObjectInputStream ois = null;
    private Vector<Client> clients = null;

    public ClassListenerFieldConnectUsers(Socket socket, TextArea textArea){
        this.socket = socket;
        this.textArea = textArea;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        try {
            Object o;
            while ((o =  ois.readObject()) != null){
                if(o instanceof Vector) {
                    clients = (Vector<Client>) o;
                }
            }
        } catch (IOException  e){
            System.err.println("ClassListenerFieldConnectUsers " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
