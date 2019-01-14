package ee.ituk.rsvp.database;

import org.springframework.data.repository.CrudRepository;

public interface EventRepo extends CrudRepository<EventModel, Long> {
}