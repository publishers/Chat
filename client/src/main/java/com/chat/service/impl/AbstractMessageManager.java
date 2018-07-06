package com.chat.service.impl;


import com.chat.service.MessageManager;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.Socket;

@Data
@AllArgsConstructor
public abstract class AbstractMessageManager extends Thread implements MessageManager {
    protected Socket socket;
}
