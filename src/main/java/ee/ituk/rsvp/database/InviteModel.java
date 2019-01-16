package ee.ituk.rsvp.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="invites")
public class InviteModel {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id @Getter private long id;

    @Column(nullable = false)
    @Getter @Setter private Long eventId;

    @Column(nullable = false)
    @Getter @Setter private String name;

    @Column(nullable = false)
    @Getter @Setter private String info;

    @Column(nullable = false)
    @Getter @Setter private Boolean coming;

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
