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
public class ClientDistributor implements Distributor<TextArea, List<Client>> {

    private TextArea connectedUsers;

    @Override
    public void init(TextArea textArea) {
        this.connectedUsers = textArea;
    }

    @Override
    public void distribute(List<Client> clients, Client client) {
        client.update(clients.stream()
                .filter(clientF -> clientF.equals(client))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(client.toString())));
        connectedUsers.setText(clients.stream()
                .map(Client::getUserName)
                .collect(Collectors.joining(System.lineSeparator())));
    }
}
