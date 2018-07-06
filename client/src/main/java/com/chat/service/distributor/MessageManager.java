package com.chat.service.distributor;

import com.chat.distribute.Distributor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class MessageManager {

    @Getter
    @Value("#{dispatcherMap}")
    private Map<Class, Distributor> data;

    public void manageMessage(Object obj) {
        if (Objects.isNull(obj)) return;
        Distributor distributor = data.get(obj.getClass());
        if (distributor == null) {
            List list = (List) obj;
            if (!list.isEmpty()) {
                distributor = data.get(list.get(0).getClass());
            }
        }
        if (!Objects.isNull(distributor)) {
            distributor.distribute(obj);
        }
    }

}
