package co.cellarcollective.tools.chronopostapiemu.soap.tracking;

import com.chronopost.model.GetSimpleTraceURL;
import com.chronopost.model.TraceEventURLListType;
import com.chronopost.model.TraceEventURLType;
import org.springframework.stereotype.Repository;

@Repository
public class TrackingRepository {
    public TraceEventURLListType findTraceUrlResponse(GetSimpleTraceURL request) {
        String pSkybillNumber = request.getPSkybillNumber();


        TraceEventURLType traceEventURLType = new TraceEventURLType();
        traceEventURLType.setTraceEventCODE("800");
        traceEventURLType.setTraceEventComment("my comment");
        traceEventURLType.setTraceEventDate("my date");
        traceEventURLType.setTraceEventDescription("my description");
        traceEventURLType.setTraceEventURL("my url");


        TraceEventURLListType eventURLListType = new TraceEventURLListType();
        eventURLListType.getTraceEventsURLArr().add(traceEventURLType);
        return eventURLListType;
    }
}
