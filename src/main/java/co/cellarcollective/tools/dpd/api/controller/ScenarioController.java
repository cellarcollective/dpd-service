package co.cellarcollective.tools.dpd.api.controller;


import co.cellarcollective.tools.dpd.domain.Scenario;
import co.cellarcollective.tools.dpd.service.ScenarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/scenario")
public class ScenarioController {

    private final ScenarioService scenarioService;

    @GetMapping("/")
    public ResponseEntity<List<Scenario>> getAll() {
        List<Scenario> scenarios = this.scenarioService.getAll();
        return ResponseEntity.ok(scenarios);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Scenario> getByName(@PathVariable @NotNull String name) {
        return this.scenarioService.find(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<Scenario> create(@RequestBody Scenario scenario) {
        Scenario result = this.scenarioService.create(scenario);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull String name) {
        if (this.scenarioService.delete(name)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
