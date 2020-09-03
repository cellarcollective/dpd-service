package co.cellarcollective.tools.dpd.service;

import co.cellarcollective.tools.dpd.domain.Scenario;
import co.cellarcollective.tools.dpd.repository.ScenarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final ObjectMapper jacksonObjectMapper;

    @PostConstruct
    public void init() {
        if (scenarioRepository.count() == 0) {
            List<Scenario> scenarios = readAsListFromResource(Scenario.class, "scenario/scenarios-all.json");
            scenarioRepository.saveAll(scenarios);
        }
    }

    public Scenario create(Scenario scenario) {
        Optional<Scenario> scenarioOptional = scenarioRepository.findByName(scenario.getName());
        if (!scenarioOptional.isPresent()) {
            return scenarioRepository.save(scenario);
        } else {
            throw new IllegalArgumentException("Scenario with name " + scenario.getName() + " already exists");
        }
    }

    public List<Scenario> getAll() {
        return scenarioRepository.findAll();
    }

    public Optional<Scenario> find(String name) {
        return scenarioRepository.findByName(name);
    }

    private <T> List<T> readAsListFromResource(Class<T> type, String jsonFile) {

        Objects.requireNonNull(type, "Type is required");
        Objects.requireNonNull(jsonFile, "JSON file name is required");

        log.debug("Loading " + type.getSimpleName() + " data from " + jsonFile);

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFile);
             Reader reader = new InputStreamReader(inputStream)) {

            CollectionType collectionType = jacksonObjectMapper.getTypeFactory()
                    .constructCollectionType(List.class, type);

            return jacksonObjectMapper.readValue(reader, collectionType);
        } catch (IOException e) {
            log.error("Error", e);
        }
        return Collections.emptyList();
    }

    public boolean delete(String name) {
        Optional<Scenario> scenarioOptional = scenarioRepository.findByName(name);
        if (scenarioOptional.isPresent()) {
            scenarioRepository.delete(scenarioOptional.get());
            return true;
        }
        return false;
    }
}
