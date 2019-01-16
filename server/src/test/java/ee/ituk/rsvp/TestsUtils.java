package ee.ituk.rsvp;

import ee.ituk.rsvp.database.EventModel;
import ee.ituk.rsvp.database.InviteModel;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestsUtils {
    public static EventModel validEvent() {
        ZoneId zoneId = ZoneId.systemDefault();
        Long eventTime = LocalDateTime.now().plusDays(2).atZone(zoneId).toEpochSecond();
        Long inviteExpire = LocalDateTime.now().plusDays(1).atZone(zoneId).toEpochSecond();
        return new EventModel("Kaur", true, "Repo testing", eventTime, "The computer", "Testing the model", inviteExpire);
    }

    public static EventModel invalidEvent() {
        ZoneId zoneId = ZoneId.systemDefault();
        Long eventTime = LocalDateTime.now().minusDays(1).atZone(zoneId).toEpochSecond();
        Long inviteExpire = LocalDateTime.now().minusDays(2).atZone(zoneId).toEpochSecond();
        return new EventModel(null, true, null, eventTime, "", "", inviteExpire);
    }

    public static InviteModel validInvite() {
        return new InviteModel("Kaur", 1, "Testing the model");
    }

    public static InviteModel invalidInvite() {
        return new InviteModel(null, -1, null);
    }
}
