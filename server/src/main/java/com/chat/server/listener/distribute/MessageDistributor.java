package com.chat.server.listener.distribute;

import com.chat.distribute.Distributor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
public class MessageDistributor {

    @Value("#{dispatcherMap}")
    private Map<String, String> data;

    public Distributor getDistributor(Object obj) {
        return null;
    }

}
