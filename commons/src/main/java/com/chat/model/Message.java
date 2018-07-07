package com.chat.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@ToString
public class Message implements Serializable {
    public static final long serialVersionUID = 5l;

    private String message;

    private LocalDateTime time;

    private Client client;

    public Message(String message, LocalDateTime time) {
        this.message = message;
        this.time = time;
    }

    public String getTime() {
        return this.time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }
}
