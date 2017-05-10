package client;

import client.status.StatusClient;

import java.io.Serializable;

/**
 * @author Serhii_Malykhin
 */
public class Client implements Serializable {

    private StatusClient status;
    private String message;
    private final String userName;

    public Client(String userNick) {
        this.userName = userNick;
        this.status = StatusClient.DISCONNECT;
    }

    public StatusClient getStatus() {
        return status;
    }

    public void setStatus(StatusClient status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }
}
