package ee.ituk.rsvp.database;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InviteRepo extends CrudRepository<InviteModel, Long> {
    List<InviteModel> findByEventId(long eventId);
}
