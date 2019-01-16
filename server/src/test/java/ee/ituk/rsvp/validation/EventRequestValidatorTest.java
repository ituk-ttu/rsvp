package ee.ituk.rsvp.validation;

import ee.ituk.rsvp.TestsUtils;
import ee.ituk.rsvp.database.EventModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EventRequestValidatorTest {

    @Test
    public void validEventValidatorShouldSucceed() {
        EventRequestValidator validator = new EventRequestValidator();

        EventModel event = TestsUtils.validEvent();

        BindException result = new BindException(event, "eventModel");
        validator.validate(event, result);
        assertThat(result.getErrorCount()).isEqualTo(0);
    }

    @Test
    public void invalidEventValidatorShouldFail() {
        EventRequestValidator validator = new EventRequestValidator();

        EventModel event = TestsUtils.invalidEvent();

        BindException result = new BindException(event, "eventModel");
        validator.validate(event, result);

        assertThat(result.getErrorCount()).isGreaterThan(0);
    }
}
