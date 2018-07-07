package com.chat.utils;

public final class PublicMessages {
    public static final String SERVER_NOT_AVAILABLE = "Server is NOT available! Or you should connect";
    public static final String DISCONNECTED_MESSAGE = "You were disconnected!";
    public static final String CONNECTED_MESSAGE = "You are connected!";
    public static final String BLANK = "";

    private PublicMessages() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
