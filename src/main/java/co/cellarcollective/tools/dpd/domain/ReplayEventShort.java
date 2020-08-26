package co.cellarcollective.tools.dpd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplayEventShort {
    private final Integer delay;
    private final String code;
}
