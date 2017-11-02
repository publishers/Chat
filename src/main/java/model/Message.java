package model;

import java.io.Serializable;

/**
 * @author Serhii_Malykhin
 */
public class Message implements Serializable {
    public static final long serialVersionUID = 5l;

    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
    }
}
