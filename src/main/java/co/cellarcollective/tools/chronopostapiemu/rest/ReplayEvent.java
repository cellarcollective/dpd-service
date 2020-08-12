package co.cellarcollective.tools.chronopostapiemu.rest;

import com.chronopost.model.TraceEventURLType;

public class ReplayEvent {

    private final Integer delay;
    private final TraceEventURLType event;

    public ReplayEvent(Integer delay, TraceEventURLType event) {
        this.delay = delay;
        this.event = event;
    }

    public Integer getDelay() {
        return delay;
    }

    public TraceEventURLType getEvent() {
        return event;
    }
}
