package socket.listener;

import model.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Objects.isNull;

/**
 * @author Serhii_Malykhin
 */
public class MessageTransfer {
    private BlockingQueue<Message> queueMessagesSendToServer;
    private BlockingQueue<Message> queueMessagesGetFromServer;

    private static MessageTransfer messageTransfer;

    public static MessageTransfer getInstance() {
        if(isNull(messageTransfer)){
            messageTransfer = new MessageTransfer();
        }
        return messageTransfer;
    }


    private MessageTransfer() {
        queueMessagesGetFromServer = new LinkedBlockingQueue<>();
        queueMessagesSendToServer = new LinkedBlockingQueue<>();
    }

    public void queueSendMessageToServer(Message message) throws InterruptedException {
        queueMessagesSendToServer.put(message);
    }

    public Message queueGetMessageFromServer() throws InterruptedException {
        return queueMessagesGetFromServer.take();
    }

    public void queueGetMessageFromServer(Message message) throws InterruptedException {
        queueMessagesGetFromServer.put(message);
    }
}
