package co.cellarcollective.tools.chronopostapiemu.soap.delivery;


import co.cellarcollective.tools.chronopostapiemu.soap.delivery.DeliveryRepository;
import com.chronopost.model.GetDeliveryRequest;
import com.chronopost.model.GetDeliveryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class RegisterExpeditionEndpoint {

    private static final String NAMESPACE_URI = "http://webservice.trace.chronopost.com";

    private DeliveryRepository deliveryRepository;

    @Autowired
    public RegisterExpeditionEndpoint(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDeliveryRequest")
    @ResponsePayload
    public GetDeliveryResponse getDeliveryRequest(@RequestPayload GetDeliveryRequest request) {
        GetDeliveryResponse getDeliveryResponse = new GetDeliveryResponse();
        getDeliveryResponse.setName(deliveryRepository.findDelivery(request.getName()));

        return getDeliveryResponse;
    }
}
