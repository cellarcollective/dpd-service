package co.cellarcollective.tools.dpd.api.dto;

import co.cellarcollective.tools.dpd.domain.TrackingMode;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class TrackingRequest {
    @NotNull
    private String trackingNumber;
    @NotNull
    private String trackingScenario;
    private TrackingMode trackingMode;
}
