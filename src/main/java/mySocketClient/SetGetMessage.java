package mySocketClient;

/**
 * Created by Admin on 02.04.15.
 */
public final class SetGetMessage {
    private static String message = null;

    private SetGetMessage() {}

    public synchronized static String getMessage() {
        return SetGetMessage.message;
    }

    public synchronized static void setMessage(String message) {
        SetGetMessage.message = message;
    }
}
