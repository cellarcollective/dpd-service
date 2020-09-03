package co.cellarcollective.tools.dpd.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TraceEvent {
    private String traceEventCODE;
    private String traceEventComment;
    private String traceEventDate;
    private String traceEventDescription;
    private String traceEventURL;
}
