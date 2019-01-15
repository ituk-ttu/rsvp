package ee.ituk.rsvp.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Entity
@Table(name="events")
public class EventModel implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id @Getter private long id;

    @Column(nullable = false)
    @Getter @Setter private String creatorId;

    @Column(nullable = false)
    @Getter @Setter private boolean isPublic;

    @Column(nullable = false)
    @Getter @Setter private String eventName;

    @Column(nullable = false)
    @Getter @Setter private long eventTime;

    @Column(nullable = false)
    @Getter @Setter private String eventPlace;

    @Column(nullable = false)
    @Getter @Setter private String info;

    @Column(nullable = false)
    @Getter @Setter private long inviteExpire;

    protected EventModel() {}

    public EventModel(String creatorId, boolean isPublic, String eventName, long eventTime, String eventPlace, String info, long inviteExpire) {
        this.creatorId = creatorId;
        this.isPublic = isPublic;
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventPlace = eventPlace;
        this.info = info;
        this.inviteExpire = inviteExpire;
    }

    public void hiddenIdSetter(long id) {
        this.id = id;
    }
}
