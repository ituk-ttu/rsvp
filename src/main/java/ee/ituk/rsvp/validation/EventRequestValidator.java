package ee.ituk.rsvp.validation;

import ee.ituk.rsvp.database.EventModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component("beforeCreateEventValidator")
public class EventRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return EventModel.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        EventModel eventModel = (EventModel) obj;
        ValidationUtils.rejectIfEmpty(errors, "creatorId", "creatorId.empty", "CreatorID is null or empty");
        ValidationUtils.rejectIfEmpty(errors, "eventName", "eventName.empty", "EventName is null or empty");
        ValidationUtils.rejectIfEmpty(errors, "eventPlace", "eventPlace.empty", "EventPlae is null or empty");
        ValidationUtils.rejectIfEmpty(errors, "info", "eventName.info", "Info is null or empty");

        // Time validation
        Long eventTime = eventModel.getEventTime();
        if (isTimeInvalid(eventTime)) {
            errors.rejectValue("eventTime", "eventTime.invalid", "EventTime is invalid");
        } else {
            if (isTimeInPast(eventTime))
                errors.rejectValue("eventTime", "eventTime.inPast", "EventTime is in past");
        }

        Long expireTime = eventModel.getInviteExpire();
        if (isTimeInvalid(expireTime)) {
            errors.rejectValue("inviteExpire", "inviteExpire.invalid", "InviteExpire is invalid");
        } else {
            if (isTimeInPast(expireTime))
                errors.rejectValue("inviteExpire", "inviteExpire.inPast", "InviteExpire is in past");
        }
    }

    private boolean isInvalidInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }

    private boolean isTimeInvalid(Long longTime) {
        return (longTime == null || longTime < 0);
    }

    private boolean isTimeInPast(Long longTime) {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(longTime), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();

        return time.isBefore(now);


    }
}
