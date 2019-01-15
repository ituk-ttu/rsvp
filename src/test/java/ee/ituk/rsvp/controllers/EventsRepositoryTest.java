package ee.ituk.rsvp.controllers;

import ee.ituk.rsvp.database.EventModel;
import ee.ituk.rsvp.database.EventRepo;
import ee.ituk.rsvp.validation.EventRequestValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EventsRepositoryTest {

    @Autowired
    private EventRepo eventRepo;

    @Test
    public void eventValidatorShouldSucceed() {
        EventRequestValidator validator = new EventRequestValidator();

        EventModel event = validEvent();

        BindException result = new BindException(event, "eventModel");
        validator.validate(event, result);

        System.out.println(result.getAllErrors());
        Assert.assertEquals(0, result.getErrorCount());
    }

    @Test
    public void eventValidatorShouldFail() {
        EventRequestValidator validator = new EventRequestValidator();

        EventModel event = inValidEvent();

        BindException result = new BindException(event, "eventModel");
        validator.validate(event, result);

        Assert.assertTrue(result.getErrorCount() > 0);
    }

    @Test
    public void validEventShouldBeCreatedInDatabase() {
        EventModel event = validEvent();
        EventModel savedEvent = eventRepo.save(event);
        EventModel foundEvent = eventRepo.findAll().iterator().next();

        Assert.assertEquals(savedEvent.getId(), foundEvent.getId());
    }

    private EventModel validEvent() {
        ZoneId zoneId = ZoneId.systemDefault();
        Long eventTime = LocalDateTime.now().plusDays(1).atZone(zoneId).toEpochSecond();
        Long inviteExpire = LocalDateTime.now().plusDays(2).atZone(zoneId).toEpochSecond();
        return new EventModel("Kaur", true, "Repo testing", eventTime, "The computer", "Testing the model", inviteExpire);
    }

    private EventModel inValidEvent() {
        ZoneId zoneId = ZoneId.systemDefault();
        Long eventTime = LocalDateTime.now().minusDays(1).atZone(zoneId).toEpochSecond();
        Long inviteExpire = LocalDateTime.now().minusDays(2).atZone(zoneId).toEpochSecond();
        return new EventModel(null, true, null, eventTime, "", "", inviteExpire);
    }
}
