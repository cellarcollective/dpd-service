package co.cellarcollective.tools.chronopostapiemu.soap.delivery;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DeliveryRepository {

    public static final Map<String, String> deliveries= new HashMap();

    @PostConstruct
    public void initData() {
        deliveries.put("one", "delivery one");
        deliveries.put("two", "delivery two");
        deliveries.put("three", "delivery three");
    }

    public String findDelivery(String id) {
        return deliveries.get(id);
    }
}
