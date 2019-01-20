package ee.ituk.rsvp.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InviteRepo extends JpaRepository<InviteModel, String> {
    List<InviteModel> findByEventId(long eventId);
}