package com.chat.service.distributor;

import com.chat.distribute.Distributor;
import com.chat.model.Message;
import javafx.scene.control.TextArea;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDistributor implements Distributor<TextArea> {

    private TextArea messagesDialog;

    @Override
    public void init(TextArea textArea) {
        this.messagesDialog = textArea;
    }

    @Override
    public void distribute(Object obj) {
        Message message = (Message) obj;
        messagesDialog.setText(updateTextArea(message));
    }

    private String updateTextArea(Message message) {
        return new StringBuilder()
                .append(message.getSendTime())
                .append(System.lineSeparator())
                .append(message.getMessage())
                .append(System.lineSeparator())
                .append(messagesDialog.getText().trim())
                .append(System.lineSeparator())
                .toString();
    }

}