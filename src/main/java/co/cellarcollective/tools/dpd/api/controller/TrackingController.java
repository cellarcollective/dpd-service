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
    public ResponseEntity<Tracking> track(@RequestBody TrackingRequest request) {
        Tracking tracking = this.trackingService.track(request.getTrackingNumber(), request.getTrackingScenario(), request.getTrackingMode());
        return ResponseEntity.ok(tracking);
    }

    @PostMapping("/retrack")
    public ResponseEntity<Tracking> reTrack(@RequestBody TrackingRequest request) {
        Tracking tracking = this.trackingService.reTrack(request.getTrackingNumber(), request.getTrackingScenario(), request.getTrackingMode());
        return ResponseEntity.ok(tracking);
    }

    @PutMapping("/next/{trackingNumber}")
    public ResponseEntity<Tracking> manualNext(@PathVariable @NotNull String trackingNumber) {
        Tracking tracking = this.trackingService.manualNext(trackingNumber);
        return ResponseEntity.ok(tracking);
    }

    @DeleteMapping("/{trackingNumber}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull String trackingNumber) {
        if (this.trackingService.delete(trackingNumber)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
