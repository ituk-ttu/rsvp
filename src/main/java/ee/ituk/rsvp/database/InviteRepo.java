package ee.ituk.rsvp.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InviteRepo extends CrudRepository<InviteModel, Long> {
    List<InviteModel> findByEventId(long eventId);
}
