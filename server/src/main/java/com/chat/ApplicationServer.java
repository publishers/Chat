package com.chat;

import com.chat.server.SocketServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ApplicationServer {

    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("configuration.xml");
        SocketServer socketServer = context.getBean(SocketServer.class);
        socketServer.start();
    }
}
