package com.chat.model;

import com.chat.ConnectionStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
public class Client implements Serializable {
    public static final long serialVersionUID = 0L;
    private transient static Client client;
    private String uuid;
    private ConnectionStatus status;
    private String userName;

    public Client(String userNick) {
        this.userName = userNick;
        this.status = ConnectionStatus.DISCONNECTED;
        this.uuid = UUID.randomUUID().toString();
    }

    public static Client notConnected() {
        if (client == null) {
            client = new Client("Not Connected");
        }
        return client;
    }

    public void update(Client client) {
        this.status = client.getStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(uuid, client.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "Client{" +
                "UUID='" + uuid + '\'' +
                ", status=" + status +
                ", userName='" + userName + '\'' +
                '}';
    }
}
