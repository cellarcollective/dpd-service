package co.cellarcollective.tools.dpd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Document(collection = "scenarios")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scenario {
    @Id
    private String id;
    @NotNull
    private String name;
    private List<ReplayEvent> replayEvents;
}
