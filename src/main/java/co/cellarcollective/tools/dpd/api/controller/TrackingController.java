package co.cellarcollective.tools.dpd.api.controller;


import co.cellarcollective.tools.dpd.domain.ErrorModel;
import co.cellarcollective.tools.dpd.domain.ReplayEvent;
import co.cellarcollective.tools.dpd.domain.TrackingScenario;
import co.cellarcollective.tools.dpd.domain.TrackingScenarioShort;
import co.cellarcollective.tools.dpd.repository.ScenarioRepository;
import co.cellarcollective.tools.dpd.service.ScenarioRunner;
import com.chronopost.model.TraceEventURLType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class TrackingController {

    private final ScenarioRepository repository;
    private final ScenarioRunner runner;

    @GetMapping("/scenarios")
    Collection<TrackingScenario> getAllScenarios() {
        return this.repository.getAll();
    }

    @PutMapping("/scenarios/custom")
    List<TrackingScenario> newScenario(@RequestBody List<TrackingScenario> scenarioList) {
        scenarioList.forEach(sc -> repository.add(sc.getId(), sc));
        return scenarioList;
    }

    @PutMapping("/scenarios/short")
    List<TrackingScenarioShort> newShortScenario(@RequestBody List<TrackingScenarioShort> scenarioList) {
        scenarioList
                .stream()
                .map(shortSc -> TrackingScenario.builder().id(shortSc.getId())
                        .replayEvents(
                                shortSc.getEvents()
                                        .stream()
                                        .map(ev -> {
                                            ErrorModel type = ErrorModel.fromIntCode(Integer.parseInt(ev.getCode()));
                                            TraceEventURLType traceEventURLType = new TraceEventURLType();
                                            traceEventURLType.setTraceEventCODE(ev.getCode());
                                            traceEventURLType.setTraceEventDescription(type.getDescription());
                                            traceEventURLType.setTraceEventComment("CellarCollective");
                                            traceEventURLType.setTraceEventURL("CellarCollective");
                                            return new ReplayEvent(ev.getDelay(), traceEventURLType);
                                        }).collect(Collectors.toList()
                                )
                        ).build())
                .forEach(sc -> this.repository.add(sc.getId(), sc));
        return scenarioList;
    }

    /**
     * @param id
     * @param pSkybillNumber - query param
     * @return
     */
    @GetMapping("/scenarios/{id}/start")
    TrackingScenario startScenario(@PathVariable("id") String id, @RequestParam String pSkybillNumber) {
        TrackingScenario trackingScenario = repository.get(id);
        trackingScenario.setPSkybillNumber(pSkybillNumber);
        trackingScenario.setStartTime(LocalTime.now());

        if (runner.isActive(pSkybillNumber))
            throw new AlreadyRunning("Tracking number \"" + pSkybillNumber + "\" already in a running scenario.");
        runner.start(trackingScenario);
        return runner.getByPSkybillNumber(pSkybillNumber);
    }

    @GetMapping("/scenarios/{id}/stop")
    String stopScenario(@PathVariable("id") String id, @RequestParam String pSkybillNumber) {
        runner.stop(pSkybillNumber);
        return id;
    }


    @GetMapping("/scenarios/{id}/isRunning")
    List<TrackingScenario> isRunningById(@PathVariable("id") String id) {
        return runner.getAllFor(id);
    }


    @GetMapping("/scenarios/running")
    Collection<TrackingScenario> getRunningScenarios(@RequestParam(required = false) String pSkybillNumber) {
        if (pSkybillNumber != null)
            return Collections.singletonList(runner.getByPSkybillNumber(pSkybillNumber));
        return runner.getAll();
    }

    @GetMapping("/scenarios/{id}/events")
    List<TraceEventURLType> timedEvents(@PathVariable("id") String id, @RequestParam String pSkybillNumber) {
        return runner.getTimedEvents(id, pSkybillNumber);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class AlreadyRunning extends RuntimeException {
        public AlreadyRunning(String message) {
            super(message);
        }
    }
}
