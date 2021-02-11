package co.cellarcollective.tools.dpd.repository;

import co.cellarcollective.tools.dpd.domain.Tracking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingRepository extends MongoRepository<Tracking, String> {

    Optional<Tracking> findByTrackingNumber(String trackingNumber);

    List<Tracking> findByExpeditionNumber(String expeditionNumber);
}
