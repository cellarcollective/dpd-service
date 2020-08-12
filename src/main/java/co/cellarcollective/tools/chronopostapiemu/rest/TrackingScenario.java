package co.cellarcollective.tools.chronopostapiemu.rest;

import java.time.LocalTime;
import java.util.List;

public class TrackingScenario {

    private final long id;
    private final List<ReplayEvent> replayEvents;
    private String pSkybillNumber;
    private LocalTime startTime;

    public TrackingScenario(long id, List<ReplayEvent> replayEvents) {
        this.id = id;
        this.replayEvents = replayEvents;
    }

    public long getId() {
        return id;
    }

    public List<ReplayEvent> getReplayEvents() {
        return replayEvents;
    }

    public String getpSkybillNumber() {
        return pSkybillNumber;
    }

    public void setpSkybillNumber(String pSkybillNumber) {
        this.pSkybillNumber = pSkybillNumber;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
}
