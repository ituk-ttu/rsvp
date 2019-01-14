package ee.ituk.rsvp.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="invites")
public class InviteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private long id;

    @Column(name="event_id", nullable = false)
    @Getter @Setter private long eventId;

    @Column(name="name", nullable = false)
    @Getter @Setter private String name;

    @Column(name="info", nullable = false)
    @Getter @Setter private String info;

    @Column(name="answer", nullable = false)
    @Getter @Setter private boolean coming;

    protected InviteModel() {}

    public InviteModel(String name, long eventId, String info) {
        this.name = name;
        this.eventId = eventId;
        this.info = info;
        this.coming = false;
    }

    public void hiddenIdSetter(long id) {
        this.id = id;
    }
}
