package co.cellarcollective.tools.dpd.api.controller;

import co.cellarcollective.tools.dpd.api.dto.TrackingRequest;
import co.cellarcollective.tools.dpd.domain.Tracking;
import co.cellarcollective.tools.dpd.service.TrackingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author manusant
 */
@Validated
@RequestMapping("/tracking")
@RestController
@AllArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @GetMapping("/")
    public ResponseEntity<List<Tracking>> getAll(@RequestParam(value = "live", required = false) Boolean live) {
        if (live != null && live) {
            return ResponseEntity.ok(this.trackingService.getAll());
        } else {
            return ResponseEntity.ok(this.trackingService.getAllLive());
        }
    }

    @GetMapping("/{trackingNumber}")
    public ResponseEntity<Tracking> getByTrackingNumber(@RequestParam(value = "live", required = false) Boolean live, @PathVariable @NotNull String trackingNumber) {
        if (live != null && live) {
            return this.trackingService.findByTrackingNumberLive(trackingNumber)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.ok().build());
        } else {
            return this.trackingService.findByTrackingNumber(trackingNumber)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.ok().build());
        }
    }

    @PostMapping("/")
    public ResponseEntity<List<Tracking>> track(@RequestBody TrackingRequest request) {
        List<Tracking> trackings = this.trackingService.track(request.getTrackingNumber(), request.getTrackingScenario(), request.getTrackingMode());
        return ResponseEntity.ok(trackings);
    }

    @PostMapping("/retrack")
    public ResponseEntity<List<Tracking>> reTrack(@RequestBody TrackingRequest request) {
        List<Tracking> trackings = this.trackingService.reTrack(request.getTrackingNumber(), request.getTrackingScenario(), request.getTrackingMode());
        return ResponseEntity.ok(trackings);
    }

    @PutMapping("/next/{expeditionCode}")
    public ResponseEntity<List<Tracking>> manualNext(@PathVariable @NotNull String expeditionCode) {
        List<Tracking> trackings = this.trackingService.manualNext(expeditionCode);
        return ResponseEntity.ok(trackings);
    }

    @DeleteMapping("/{expeditionCode}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull String expeditionCode) {
        if (this.trackingService.delete(expeditionCode)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
