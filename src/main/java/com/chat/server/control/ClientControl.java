package com.chat.server.control;

import com.chat.server.response.ServerClientListener;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Serhii_Malykhin
 */
public class ClientControl {
    private List<ServerClientListener> listeners;

    private ClientControl(){
        listeners = new LinkedList<>();
    }

    public static ClientControl newInstance(){
        return new ClientControl();
    }

    public void add(ServerClientListener clientActionListener){
        listeners.add(clientActionListener);
        clientActionListener.start();
    }

    public ServerClientListener get(int i){
        return listeners.get(i);
    }

    public List<ServerClientListener> getListeners() {
        return listeners;
    }
}
