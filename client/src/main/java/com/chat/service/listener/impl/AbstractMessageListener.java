package com.chat.service.listener.impl;


import com.chat.service.listener.MessageManager;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.Socket;

@Data
@AllArgsConstructor
public abstract class AbstractMessageListener extends Thread implements MessageManager {
    protected Socket socket;
}
