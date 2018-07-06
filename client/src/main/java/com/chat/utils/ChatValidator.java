package com.chat.utils;

public class ChatValidator {
    private static final String REGEX_USER_NAME = "[A-Za-z0-9 ]{2,}";

    public static boolean isUserNickCorrect(String userName) {
        return userName != null && userName.trim().matches(REGEX_USER_NAME);
    }

    public static boolean isMessageCorrect(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
