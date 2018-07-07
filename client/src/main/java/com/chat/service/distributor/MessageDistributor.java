package com.chat.service.distributor;

import com.chat.distribute.Distributor;
import com.chat.model.Client;
import com.chat.model.Message;
import javafx.scene.control.TextArea;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDistributor implements Distributor<TextArea, Message> {

    private TextArea messagesDialog;

    @Override
    public void init(TextArea textArea) {
        this.messagesDialog = textArea;
    }

    @Override
    public void distribute(Message message, Client client) {
        messagesDialog.setText(updateTextArea(message));
    }

    private String updateTextArea(Message message) {
        return new StringBuilder()
                .append(message.getTime())
                .append(System.lineSeparator())
                .append(message.getClient().getUserName()).append(':')
                .append(message.getMessage())
                .append(System.lineSeparator())
                .append(messagesDialog.getText().trim())
                .append(System.lineSeparator())
                .toString();
    }

}