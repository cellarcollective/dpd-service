package co.cellarcollective.tools.dpd.api.endpoint;


import co.cellarcollective.tools.dpd.repository.DeliveryRepository;
import com.chronopost.model.GetDeliveryRequest;
import com.chronopost.model.GetDeliveryResponse;
import lombok.AllArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@AllArgsConstructor
public class RegisterExpeditionEndpoint {

    private static final String NAMESPACE_URI = "http://webservice.trace.chronopost.com";

    private final DeliveryRepository deliveryRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDeliveryRequest")
    @ResponsePayload
    public GetDeliveryResponse getDeliveryRequest(@RequestPayload GetDeliveryRequest request) {
        GetDeliveryResponse getDeliveryResponse = new GetDeliveryResponse();
        getDeliveryResponse.setName(deliveryRepository.findDelivery(request.getName()));

        return getDeliveryResponse;
    }
}
