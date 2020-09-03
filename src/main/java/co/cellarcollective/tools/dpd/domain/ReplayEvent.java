package co.cellarcollective.tools.dpd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReplayEvent {
    private int delay;
    private boolean triggered;
    private LocalDateTime triggerTime;
    private TraceEvent event;
}
