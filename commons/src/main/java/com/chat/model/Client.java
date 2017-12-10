package com.chat.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Serhii_Malykhin
 */
public class Client implements Serializable {
    public static final long serialVersionUID = 0L;
    private String UUID;
    private StatusClient status;
    private String userName;

    public Client(String userNick) {
        this.userName = userNick;
        this.status = StatusClient.DISCONNECT;
        this.UUID = java.util.UUID.randomUUID().toString();
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

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(UUID, client.UUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UUID);
    }

    @Override
    public String toString() {
        return "Client{" +
                "UUID='" + UUID + '\'' +
                ", status=" + status +
                ", userName='" + userName + '\'' +
                '}';
    }
}
