package com.chat.model;

import java.io.Serializable;

/**
 * @author Serhii_Malykhin
 */
public class Message implements Serializable {
  public static final long serialVersionUID = 5l;

  private String sendTime;
  private String message;

  public Message(String message, String sendTime) {
    this.message = message;
    this.sendTime = sendTime;
  }

  public String getSendTime() {
    return sendTime;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "Message{" +
        "message='" + message + '\'' +
        ", sendTime='" + sendTime + '\'' +
        '}';
  }
}
