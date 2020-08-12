package co.cellarcollective.tools.chronopostapiemu.soap.tracking;


import com.chronopost.model.GetSimpleTraceURL;
import com.chronopost.model.GetSimpleTraceURLResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class RegisterTrackingEndpoint {

    private static final String NAMESPACE_URI = "http://webservice.trace.chronopost.com";

    private TrackingRepository repository;


    @Autowired
    public RegisterTrackingEndpoint(TrackingRepository repository) {
        this.repository = repository;
    }

    // expedictionID

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSimpleTraceURL")
    @ResponsePayload
    public GetSimpleTraceURLResponse getSimpleTraceURL(@RequestPayload GetSimpleTraceURL request) {
        GetSimpleTraceURLResponse response = new GetSimpleTraceURLResponse();
        response.setReturn(repository.findTraceUrlResponse(request));

        return response;
    }

}
