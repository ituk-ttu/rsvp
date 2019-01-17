package ee.ituk.rsvp.validation;

import ee.ituk.rsvp.TestsUtils;
import ee.ituk.rsvp.database.InviteModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InviteRequestValidatorTest {
    @Test
    public void validInviteValidatorShouldSucceed() {
        InviteRequestValidator validator = new InviteRequestValidator();

        InviteModel event = TestsUtils.validInvite();

        BindException result = new BindException(event, "inviteModel");
        validator.validate(event, result);

        assertThat(result.getErrorCount()).isEqualTo(0);
    }

    @Test
    public void invalidInviteValidatorShouldFail() {
        InviteRequestValidator validator = new InviteRequestValidator();

        InviteModel event = TestsUtils.invalidInvite();

        BindException result = new BindException(event, "inviteModel");
        validator.validate(event, result);
        assertThat(result.getErrorCount()).isGreaterThan(0);
    }
}
