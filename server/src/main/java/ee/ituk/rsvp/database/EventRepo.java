package ee.ituk.rsvp.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepo extends JpaRepository<EventModel, Long> {
    EventModel findByCreatorId(String name);
}