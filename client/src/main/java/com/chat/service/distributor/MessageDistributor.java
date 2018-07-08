package com.chat.service.distributor;

import com.chat.distribute.Distributor;
import com.chat.model.Client;
import com.chat.model.Message;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDistributor implements Distributor<WebView, Message> {

    private WebView messagesDialog;

    @Override
    public void init(WebView textArea) {
        this.messagesDialog = textArea;
    }

    @Override
    public void distribute(Message message, Client client) {
        Platform.runLater(() -> {
            boolean side = client.equals(message.getClient());
            updateTextArea(message, side);
        });
    }

    private void updateTextArea(Message message, boolean side) {
        messagesDialog.getEngine().executeScript("appendText('" + message.getMessage() + "'," + side + ")");
    }

}