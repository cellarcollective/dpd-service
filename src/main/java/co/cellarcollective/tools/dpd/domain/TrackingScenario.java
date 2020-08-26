package co.cellarcollective.tools.dpd.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class TrackingScenario {

    private final long id;
    private final List<ReplayEvent> replayEvents;
    private String pSkybillNumber;
    private LocalTime startTime;
}
