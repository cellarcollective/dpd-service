package co.cellarcollective.tools.dpd.service;

import co.cellarcollective.tools.dpd.domain.ReplayEvent;
import co.cellarcollective.tools.dpd.domain.Scenario;
import co.cellarcollective.tools.dpd.domain.Tracking;
import co.cellarcollective.tools.dpd.domain.TrackingEvent;
import co.cellarcollective.tools.dpd.repository.TrackingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TrackingService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MMM/dd','HH:mm:ss");

    private final TrackingRepository trackingRepository;
    private final ScenarioService scenarioService;

    public Tracking track(String trackingNumber, String trackingScenario) {

        if (trackingRepository.findByTrackingNumber(trackingNumber).isPresent()) {
            throw new IllegalArgumentException("Tracking number " + trackingNumber + " already assigned to a scenario");
        }

        Scenario scenario = scenarioService.find(trackingScenario).orElseThrow(() -> new IllegalArgumentException("No Scenario was found for name " + trackingScenario));

        List<ReplayEvent> trackingEvents = createEvents(scenario);

        Tracking tracking = Tracking.builder()
                .startTime(LocalTime.now())
                .trackingNumber(trackingNumber)
                .trackingScenario(trackingScenario)
                .replayEvents(trackingEvents)
                .build();

        return trackingRepository.save(tracking);
    }

    public Tracking live(Tracking tracking) {

        for (ReplayEvent event : tracking.getReplayEvents()) {
            if (!event.isTriggered()) {
                event.setTriggered(hasEventHappen(event));
            }
        }
        trackingRepository.save(tracking);

        List<ReplayEvent> liveEvents = tracking.getReplayEvents()
                .stream()
                .filter(ReplayEvent::isTriggered)
                .collect(Collectors.toList());

        return Tracking.builder()
                .id(tracking.getId())
                .replayEvents(liveEvents)
                .startTime(tracking.getStartTime())
                .trackingNumber(tracking.getTrackingNumber())
                .trackingScenario(tracking.getTrackingScenario())
                .build();
    }

    public boolean delete(String trackingNumber) {
        Optional<Tracking> tracking = trackingRepository.findByTrackingNumber(trackingNumber);
        if (tracking.isPresent()) {
            trackingRepository.delete(tracking.get());
            return true;
        } else {
            return false;
        }
    }

    private List<ReplayEvent> createEvents(Scenario scenario) {
        LocalDateTime now = LocalDateTime.now();

        return scenario.getReplayEvents()
                .stream()
                .peek(replayEvent -> {

                    replayEvent.setTriggerTime(now.plusMinutes(replayEvent.getDelay()));
                    replayEvent.getEvent().setTraceEventDate(DATE_TIME_FORMATTER.format(replayEvent.getTriggerTime()));

                    if (replayEvent.getEvent().getTraceEventDescription() == null) {
                        TrackingEvent trackingEvent = TrackingEvent.fromCode(replayEvent.getEvent().getTraceEventCODE());
                        replayEvent.getEvent().setTraceEventDescription(trackingEvent.getDescription());
                    }
                }).collect(Collectors.toList());
    }

    public Optional<Tracking> findByTrackingNumber(String trackingNumber) {
        return trackingRepository.findByTrackingNumber(trackingNumber);
    }

    public Optional<Tracking> findByTrackingNumberLive(String trackingNumber) {
        return trackingRepository.findByTrackingNumber(trackingNumber)
                .map(this::live);
    }

    public List<Tracking> getAll() {
        return trackingRepository.findAll();
    }

    public List<Tracking> getAllLive() {
        return trackingRepository.findAll()
                .stream()
                .map(this::live)
                .collect(Collectors.toList());
    }

    private boolean hasEventHappen(ReplayEvent event) {
        return LocalDateTime.now().isAfter(event.getTriggerTime());
    }
}