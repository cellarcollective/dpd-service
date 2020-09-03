package co.cellarcollective.tools.dpd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@Document(collection = "trackings")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tracking {
    @Id
    private String id;
    private String trackingNumber;
    private String trackingScenario;
    private LocalTime startTime;
    private List<ReplayEvent> replayEvents;
}
