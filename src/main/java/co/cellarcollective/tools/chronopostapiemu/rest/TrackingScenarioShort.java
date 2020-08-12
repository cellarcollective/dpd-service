package co.cellarcollective.tools.chronopostapiemu.rest;


import java.util.List;

public class TrackingScenarioShort {

    private final Long id;
    private final List<ReplayEventShort> events;

    public TrackingScenarioShort(Long id, List<ReplayEventShort> events) {
        this.id = id;
        this.events = events;
    }

    public Long getId() {
        return id;
    }

    public List<ReplayEventShort> getEvents() {
        return events;
    }
}
