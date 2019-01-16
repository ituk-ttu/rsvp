package ee.ituk.rsvp.database;

import ee.ituk.rsvp.TestsUtils;
import ee.ituk.rsvp.validation.MockInviteRequestValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InvitesRepositoryTest {

    @Resource
    private TestEntityManager entityManager;
    @Resource
    private InviteRepo inviteRepo;
    @Resource
    private EventRepo eventRepo;

    @Test
    public void validInviteValidationShouldSucceed() {
        EventModel event = TestsUtils.validEvent();
        EventModel savedEvent = entityManager.persistAndFlush(event);

        InviteModel inviteModel = TestsUtils.validInvite();
        inviteModel.setEventId(savedEvent.getId());

        MockInviteRequestValidator validator = new MockInviteRequestValidator();
        BindException result = new BindException(inviteModel, "inviteModel");

        validator.validate(inviteModel, result);
        validator.checkForDataValidity(inviteModel, result, eventRepo);
        System.out.println(result.getAllErrors());

        assertThat(result.getErrorCount()).isEqualTo(0);
    }

    @Test
    public void invalidInviteValidationShouldFail() {
        EventModel event = TestsUtils.validEvent();
        EventModel savedEvent = entityManager.persistAndFlush(event);

        InviteModel inviteModel = TestsUtils.invalidInvite();
        inviteModel.setEventId(savedEvent.getId());

        MockInviteRequestValidator validator = new MockInviteRequestValidator();
        BindException result = new BindException(inviteModel, "inviteModel");

        validator.validate(inviteModel, result);
        validator.checkForDataValidity(inviteModel, result, eventRepo);

        assertThat(result.getErrorCount()).isGreaterThan(0);
    }

    @Test
    public void validInviteCreationShouldSucceed() {
        EventModel event = TestsUtils.validEvent();
        EventModel savedEvent = entityManager.persistAndFlush(event);

        InviteModel inviteModel = TestsUtils.validInvite();
        inviteModel.setEventId(savedEvent.getId());

        entityManager.persistAndFlush(inviteModel);

        Iterable<InviteModel> invites = inviteRepo.findAll();
        assertThat(invites).hasSize(1).contains(inviteModel);
    }

    @Test
    public void validInviteShouldBeDeletedFromDatabase() {
        EventModel event = TestsUtils.validEvent();
        EventModel savedEvent = entityManager.persistAndFlush(event);

        InviteModel inviteModel = TestsUtils.validInvite();
        inviteModel.setEventId(savedEvent.getId());

        InviteModel savedInvite = entityManager.persistAndFlush(inviteModel);

        long id = savedInvite.getId();
        inviteRepo.deleteById(id);

        Iterable<InviteModel> invites = inviteRepo.findAll();
        assertThat(invites).isEmpty();
    }
}
