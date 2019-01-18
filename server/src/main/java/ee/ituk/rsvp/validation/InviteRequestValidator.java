package ee.ituk.rsvp.validation;

import ee.ituk.rsvp.database.EventRepo;
import ee.ituk.rsvp.database.InviteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component("beforeCreateInviteValidator")
public class InviteRequestValidator implements Validator {
    @Autowired
    private EventRepo eventRepo;

    @Override
    public boolean supports(Class<?> clazz) {
        return InviteModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InviteModel inviteModel = (InviteModel) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "invite.eventId.empty", "Name is null or empty");

        if (inviteModel.getInfo() == null)
            errors.reject("invite.info.null", "Info is null");

        if (inviteModel.getEventId() == null)
            errors.reject("invite.eventid.empty", "EventId is empty");

        if (inviteModel.getIsComing() == null)
            errors.reject("invite.isComing.empty", "Coming is empty");

    }

    public void checkForDataValidity(Object target, Errors errors) {
        InviteModel inviteModel = (InviteModel) target;

        long eventid = inviteModel.getEventId();

        if (!eventRepo.existsById(eventid)) {
            errors.reject("invite.eventid.nonexistant", "Event with id doesn't exist");
        } else {
            Long expiryMillis = eventRepo.findById(eventid).get().getInviteExpire();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryLDT = LocalDateTime.ofInstant(Instant.ofEpochMilli(expiryMillis), ZoneId.systemDefault());

            if (expiryLDT.isBefore(now))
                errors.reject("invite.expired", "Event's deadline has passed");
        }
    }
}
