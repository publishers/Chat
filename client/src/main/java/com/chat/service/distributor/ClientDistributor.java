package com.chat.service.distributor;

import com.chat.distribute.Distributor;
import com.chat.model.Client;
import javafx.scene.control.TextArea;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDistributor implements Distributor<TextArea> {

    private TextArea connectedUsers;

    @Override
    public void init(TextArea textArea) {
        this.connectedUsers = textArea;
    }

    @Override
    public void distribute(Object obj) {
        List<Client> clients = (List<Client>) obj;
        connectedUsers.setText(clients.stream()
                .map(client -> client.getUserName())
                .collect(Collectors.joining(System.lineSeparator())));
    }
}
