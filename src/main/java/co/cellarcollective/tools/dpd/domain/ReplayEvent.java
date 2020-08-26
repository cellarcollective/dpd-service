package co.cellarcollective.tools.dpd.domain;

import com.chronopost.model.TraceEventURLType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplayEvent {
    private final Integer delay;
    private final TraceEventURLType event;
}
