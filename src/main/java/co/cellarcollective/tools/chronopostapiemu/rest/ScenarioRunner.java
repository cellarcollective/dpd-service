package co.cellarcollective.tools.chronopostapiemu.rest;

import com.chronopost.model.TraceEventURLType;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScenarioRunner {

    private final HashMap<String, TrackingScenario> runningScenarios;

    public ScenarioRunner() {
        this.runningScenarios = new HashMap<>();
    }

    private  boolean hasEventHappen(ReplayEvent rp, LocalTime startTime) {
        return LocalTime.now().compareTo(startTime.plusMinutes(rp.getDelay())) > 0;
    }

    public void start(TrackingScenario trackingScenario) {
        runningScenarios.put(trackingScenario.getpSkybillNumber(), trackingScenario);
        System.out.println("started");
    }

    public void stop(String pSkybillNumber) {
        runningScenarios.remove(pSkybillNumber);
    }

    public List<TrackingScenario> getAllFor(String id) {
        return runningScenarios.values()
                .stream()
                .filter(scenario -> scenario.getId() == Long.parseLong(id))
                .collect(Collectors.toList());
    }

    public List<TraceEventURLType> getTimedEvents(String id, String pSkybillNumber) {
        TrackingScenario trackingScenario = runningScenarios.get(pSkybillNumber);

        return trackingScenario
                .getReplayEvents()
                .stream()
                .filter(rp -> this.hasEventHappen(rp, trackingScenario.getStartTime()))
                .peek(ev -> ev.getEvent().setTraceEventDate(trackingScenario.getStartTime().plusMinutes(ev.getDelay()).toString()))
                .map(ReplayEvent::getEvent)
                .collect(Collectors.toList());

    }

    public Collection<TrackingScenario> getAll() {
        return runningScenarios.values();
    }

    public boolean isActive(String pSkybillNumber) {
        return this.runningScenarios.containsKey(pSkybillNumber);
    }

    public TrackingScenario getByPSkybillNumber(String pSkybillNumber) {
        return this.runningScenarios.get(pSkybillNumber);
    }
}
