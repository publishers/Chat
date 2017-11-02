package model;

import model.status.StatusClient;

import java.io.Serializable;

/**
 * @author Serhii_Malykhin
 */
public class Client implements Serializable {
    public static final long serialVersionUID = 0l;
    private StatusClient status;
    private String userName;

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

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "Client{" +
                "status=" + status +
                ", userName='" + userName + '\'' +
                '}';
    }
}
