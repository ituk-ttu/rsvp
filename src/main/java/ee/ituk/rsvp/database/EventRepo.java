package ee.ituk.rsvp.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepo extends CrudRepository<EventModel, Long> {
}