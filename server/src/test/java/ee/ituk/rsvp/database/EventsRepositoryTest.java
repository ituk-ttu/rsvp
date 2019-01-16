package ee.ituk.rsvp.database;

import ee.ituk.rsvp.TestsUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EventsRepositoryTest {

    @Resource
    private TestEntityManager entityManager;
    @Resource
    private EventRepo eventRepo;

    @Test
    public void validEventShouldBeCreatedInDatabase() {
        EventModel event = TestsUtils.validEvent();
        entityManager.persistAndFlush(event);

        Iterable<EventModel> events = eventRepo.findAll();
        assertThat(events).hasSize(1).contains(event);
    }

    @Test
    public void validEventShouldBeFoundFromDatabase() {
        EventModel event = TestsUtils.validEvent();
        entityManager.persistAndFlush(event);

        Optional<EventModel> oEventModel = eventRepo.findById(event.getId());
        assertThat(oEventModel.isPresent()).isTrue();
    }

    @Test
    public void validEventShouldBeDeletedFromDatabase() {
        EventModel event = TestsUtils.validEvent();
        entityManager.persistAndFlush(event);

        long id = event.getId();

        eventRepo.deleteById(id);

        Iterable<EventModel> events = eventRepo.findAll();
        assertThat(events).isEmpty();
    }
}
