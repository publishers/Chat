package com.chat.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Serhii_Malykhin
 */
public class Client implements Serializable {
  public static final long serialVersionUID = 0L;
  private String uuid;
  private StatusClient status;
  private String userName;

  public Client(String userNick) {
    this.userName = userNick;
    this.status = StatusClient.DISCONNECT;
    this.uuid = UUID.randomUUID().toString();
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

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public void setUserName(String userName) {
    this.userName = userName;
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
