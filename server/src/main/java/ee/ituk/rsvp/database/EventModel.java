package ee.ituk.rsvp.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="events")
public class EventModel {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id @Getter private long id;

    @Column(nullable = false)
    @Getter @Setter private String creatorId;

    @Column(nullable = false)
    @JsonProperty
    @Getter @Setter private Boolean isPublic;

    @Column(nullable = false)
    @Getter @Setter private String eventName;

    @Column(nullable = false)
    @Getter @Setter private Long eventTime;

    @Column(nullable = false)
    @Getter @Setter private String eventPlace;

    @Column(nullable = false, length = 1000)
    @Getter @Setter private String info;

    @Column(nullable = false)
    @Getter @Setter private Long inviteExpire;

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
