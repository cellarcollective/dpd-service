package co.cellarcollective.tools.dpd.service;

import co.cellarcollective.tools.dpd.domain.Scenario;
import co.cellarcollective.tools.dpd.repository.ScenarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final ObjectMapper jacksonObjectMapper;
    private final ResourcePatternResolver resourcePatternResolver;

    @PostConstruct
    public void init() throws IOException {
        if (scenarioRepository.count() == 0) {
            log.debug("======= Start loading scenarios");
            Resource[] resources = resourcePatternResolver.getResources("classpath:scenario/*.json");
            List<Scenario> scenarios = Arrays.stream(resources)
                    .map(resource -> readAsObjectFromResource(Scenario.class, resource))
                    .collect(Collectors.toList());

            scenarioRepository.saveAll(scenarios);
            log.debug("======= {} scenarios loaded", scenarios.size());
        } else {
            log.debug("======= Scenarios already loaded");
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

    public <T> T readAsObjectFromResource(Class<T> type, Resource resource) {
        Objects.requireNonNull(type, "Type is required");
        Objects.requireNonNull(resource, "Resource is required");

        try (InputStream inputStream = resource.getInputStream(); Reader reader = new InputStreamReader(inputStream)) {
            return jacksonObjectMapper.readValue(reader, type);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid scenario file provided", e);
        }
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
