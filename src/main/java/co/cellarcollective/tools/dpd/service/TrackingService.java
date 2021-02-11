package co.cellarcollective.tools.dpd.service;

import co.cellarcollective.tools.dpd.domain.*;
import co.cellarcollective.tools.dpd.repository.TrackingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TrackingService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MMM/dd','HH:mm:ss", new Locale("pt", "", ""));

    private final TrackingRepository trackingRepository;
    private final ScenarioService scenarioService;

    public List<Tracking> track(String expedition, String trackingScenario, TrackingMode trackingMode) {

        if (!trackingRepository.findByExpeditionNumber(expedition).isEmpty()) {
            throw new IllegalArgumentException("Tracking number " + expedition + " already assigned to a scenario");
        }

        Scenario scenario = scenarioService.find(trackingScenario).orElseThrow(() -> new IllegalArgumentException("No Scenario was found for name " + trackingScenario));

        List<ReplayEvent> trackingEvents = createEvents(scenario);

        List<Tracking> trackings = new ArrayList<>();

        Arrays.asList(expedition.split(";"))
                .forEach(trackingNumber -> {

                    Tracking tracking = Tracking.builder()
                            .startTime(LocalTime.now())
                            .trackingNumber(trackingNumber)
                            .expeditionNumber(expedition)
                            .trackingScenario(trackingScenario)
                            .replayEvents(trackingEvents)
                            .trackingMode(trackingMode)
                            .build();
                    trackings.add(trackingRepository.save(tracking));
                });
        return trackings;
    }

    public List<Tracking> reTrack(String expedition, String trackingScenario, TrackingMode trackingMode) {

        List<Tracking> trackings = trackingRepository.findByExpeditionNumber(expedition);
        if (trackings.isEmpty()) {
            throw new IllegalArgumentException("Tracking number " + expedition + " not being tracked yet");
        }

        Scenario scenario = scenarioService.find(trackingScenario).orElseThrow(() -> new IllegalArgumentException("No Scenario was found for name " + trackingScenario));

        List<ReplayEvent> trackingEvents = createEvents(scenario);

        List<Tracking> result = new ArrayList<>();

        trackings.forEach(tracking -> {

            tracking.setStartTime(LocalTime.now());
            tracking.setTrackingScenario(trackingScenario);
            tracking.setReplayEvents(trackingEvents);
            tracking.setTrackingMode(trackingMode);

            result.add(trackingRepository.save(tracking));
        });
        return result;
    }

    public Tracking live(Tracking tracking) {

        for (ReplayEvent event : tracking.getReplayEvents()) {
            if (isAutomatic(tracking) && !event.isTriggered()) {
                event.setTriggered(hasEventHappen(event));
            }
        }
        trackingRepository.save(tracking);
        return buildLiveTracking(tracking);
    }

    public List<Tracking> manualNext(String expedition) {

        List<Tracking> trackings = trackingRepository.findByExpeditionNumber(expedition);
        if (trackings.isEmpty()) {
            throw new IllegalArgumentException("No Tracking found for expedition " + expedition);
        }

        trackings.forEach(tracking -> {
            if (isAutomatic(tracking)) {
                throw new IllegalArgumentException("Only MANUAL Trackings can be manually controlled");
            }

            if (tracking.getReplayEvents().stream().allMatch(ReplayEvent::isTriggered)) {
                throw new IllegalArgumentException("All trigger events already fired");
            }
        });

        List<Tracking> result = new ArrayList<>();
        trackings.forEach(tracking -> {

            List<ReplayEvent> sortedEvents = tracking.getReplayEvents().stream().sorted(Comparator.comparingInt(ReplayEvent::getDelay)).collect(Collectors.toList());

            for (ReplayEvent event : sortedEvents) {
                if (!event.isTriggered()) {
                    event.setTriggered(true);
                    break;
                }
            }
            trackingRepository.save(tracking);
            result.add(buildLiveTracking(tracking));
        });
        return result;
    }

    private Tracking buildLiveTracking(Tracking tracking) {

        List<ReplayEvent> liveEvents = tracking.getReplayEvents()
                .stream()
                .filter(ReplayEvent::isTriggered)
                .collect(Collectors.toList());

        return Tracking.builder()
                .id(tracking.getId())
                .replayEvents(liveEvents)
                .startTime(tracking.getStartTime())
                .expeditionNumber(tracking.getExpeditionNumber())
                .trackingNumber(tracking.getTrackingNumber())
                .trackingScenario(tracking.getTrackingScenario())
                .build();
    }

    public boolean delete(String expedition) {
        List<Tracking> trackings = trackingRepository.findByExpeditionNumber(expedition);
        trackings.forEach(trackingRepository::delete);
        return !trackings.isEmpty();
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

    private boolean isAutomatic(Tracking tracking) {
        return tracking.getTrackingMode() == null || tracking.getTrackingMode() == TrackingMode.AUTOMATIC;
    }
}
