package co.cellarcollective.tools.chronopostapiemu.rest;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ScenarioRepository {

    public static final Map<Long, TrackingScenario> data = new HashMap<>();

    public void add(Long id, TrackingScenario scenario) {
        data.put(id, scenario);
    }

    public TrackingScenario get(String id) {
        return data.get(Long.parseLong(id));
    }

    public Collection<TrackingScenario> getAll() {
        return data.values();
    }
}
