package ee.ituk.rsvp.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="invites")
public class InviteModel {
    @GenericGenerator(name = "str_id_gen", strategy = "ee.ituk.rsvp.database.StringIdentifierGenerator")
    @GeneratedValue(generator = "str_id_gen")
    @Id @Getter private String id;

    @Column(nullable = false)
    @Getter @Setter private Long eventId;

    @Column(nullable = false)
    @Getter @Setter private String name;

    @Column(nullable = false)
    @Getter @Setter private String info;

    @Column(nullable = false)
    @JsonProperty
    @Getter @Setter private Boolean isComing;

    protected InviteModel() {}

    public InviteModel(String name, long eventId, String info) {
        this.name = name;
        this.eventId = eventId;
        this.info = info;
        this.isComing = false;
    }

    public void hiddenIdSetter(String id) {
        this.id = id;
    }

    public void hiddenIdSetter(long id) {
        this.id = Long.toString(id);
    }

    @Override
    public String toString() {
        return "I: " + this.name + " INFO: " + this.info + " COMING: " + this.isComing + " EID: " + this.eventId;
    }
}
