package ee.ituk.rsvp.controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ee.ituk.rsvp.Constants;
import ee.ituk.rsvp.database.EventModel;
import ee.ituk.rsvp.database.EventRepo;
import ee.ituk.rsvp.database.InviteModel;
import ee.ituk.rsvp.database.InviteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventsController {
    private JsonNodeFactory factory;
    private ThinkingClass thinkingClass;
    private SimpleDateFormat dateFormat;

    public EventsController() {
        factory = new JsonNodeFactory(false);
        thinkingClass = new ThinkingClass();
        dateFormat = new SimpleDateFormat("dd.MM.YYYY HH:mm");
    }

    @Autowired
    EventRepo eventRepo;
    @Autowired
    InviteRepo inviteRepo;

    /**
     * Return all events currently in database
     *
     * @return JSON String
     */
    @GetMapping(value = {"", "/", "/all"})
    public ResponseEntity<String> all() {

        try {
            ArrayNode events = factory.arrayNode();
            for (EventModel eventModel : eventRepo.findAll()) {
                ObjectNode event = thinkingClass.getEventNode(eventModel);
                ArrayNode invites = factory.arrayNode();

                for (InviteModel inviteModel : inviteRepo.findByEventId(eventModel.getId())) {
                    invites.add(thinkingClass.getInviteNode(inviteModel));
                }

                event.putPOJO(Constants.INVITES, invites);
                events.add(event);
            }

            return ResponseEntity.status(HttpStatus.OK).body(events.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Create an event and returns it's data
     *
     * @param eventModel
     * @return JSON string
     */
    @PostMapping("/create")
    public String eventCreate(@ModelAttribute EventModel eventModel) {
        ObjectNode root = factory.objectNode();
        try {
            eventRepo.save(eventModel);
            root.put(Constants.STATUS, Constants.STATUS_OK);
            thinkingClass.populateEventNode(eventModel, root);
        } catch (Exception e) {
            root = thinkingClass.getErrorNode(e);
            root.put(Constants.ID, eventModel.getId());
            e.printStackTrace();
        }
        return root.toString();
    }

    /**
     * Edit event by overwriting database entry
     *
     * @param eventModel Must contain all fields of EventModel:
     *              String creatorId
     *              String eventName
     *              String eventTime
     *              String eventPlace
     *              String info
     *              long inviteExpiry
     * @return JSON String
     */
    @PostMapping(value="/edit")
    public String eventEdit(@RequestParam("eventId") long eventId, @ModelAttribute EventModel eventModel) {
        ObjectNode root = factory.objectNode();

        if (eventRepo.existsById(eventId)) {
            try {
                eventModel.hiddenIdSetter(eventId);
                eventRepo.save(eventModel);

                root.put(Constants.STATUS, Constants.STATUS_OK);
                thinkingClass.populateEventNode(eventModel, root);
            } catch (Exception e) {
                root = thinkingClass.getErrorNode(e);
                root.put(Constants.ID, eventModel.getId());
                e.printStackTrace();
            }
        } else {
            root.put(Constants.STATUS, Constants.STATUS_NOTOK);
            root.put(Constants.INFO, "Event not found.");
            root.put(Constants.ID, eventId);
        }
        return root.toString();
    }

    /**
     * Delete event
     *
     * @param id: Id of event.
     * @return JSON String
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Optional<EventModel> oEventModel = eventRepo.findById(id);

        if (oEventModel.isPresent()) {
            try {
                eventRepo.delete(oEventModel.get());
                return ResponseEntity.status(HttpStatus.OK).body(null);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }
    }
}