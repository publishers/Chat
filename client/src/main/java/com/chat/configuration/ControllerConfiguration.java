package com.chat.configuration;

import com.chat.controller.ViewController;
import com.chat.service.ClientService;
import com.chat.service.impl.ClientServiceImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class ControllerConfiguration {

    @Bean("requestLinkedBlockingQueue")
    @Scope(value = "singleton")
    public LinkedBlockingQueue<Object> requestLinkedBlockingQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean("responseLinkedBlockingQueue")
    @Scope(value = "singleton")
    public LinkedBlockingQueue<Object> responseLinkedBlockingQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean("clientService")
    @Scope(value = "singleton")
    public ClientService clientConnection() {
        return new ClientServiceImpl();
    }

    @Bean("mainView")
    @Scope(value = "singleton")
    public ViewHolder getMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Interface.fxml"));
        Parent parent = loader.load();
        return new ViewHolder(parent, loader.getController());
    }

    @Bean
    @Scope(value = "singleton")
    public ViewController fxmlController() throws IOException {
        return (ViewController) getMainView().getController();
    }

    @Data
    @AllArgsConstructor
    public class ViewHolder {
        private Parent view;
        private Object controller;
    }
}