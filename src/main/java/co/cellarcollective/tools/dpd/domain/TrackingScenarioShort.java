package co.cellarcollective.tools.dpd.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TrackingScenarioShort {
    private final Long id;
    private final List<ReplayEventShort> events;
}
