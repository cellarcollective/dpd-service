package co.cellarcollective.tools.dpd.repository;

import co.cellarcollective.tools.dpd.domain.Scenario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScenarioRepository extends MongoRepository<Scenario, String> {

    Optional<Scenario> findByName(String name);
}
