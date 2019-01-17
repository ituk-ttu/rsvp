package ee.ituk.rsvp.controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ee.ituk.rsvp.database.EventModel;
import ee.ituk.rsvp.database.EventRepo;
import ee.ituk.rsvp.database.InviteModel;
import ee.ituk.rsvp.database.InviteRepo;
import ee.ituk.rsvp.util.Constants;
import ee.ituk.rsvp.validation.EventRequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@CrossOrigin
public class EventsController {
    private JsonNodeFactory factory;

    public EventsController() {
        factory = new JsonNodeFactory(false);
    }

    @Resource
    private ThinkerService thinkerService;
    @Resource
    private EventRequestValidator validator;
    @Resource
    private EventRepo eventRepo;
    @Resource
    private InviteRepo inviteRepo;

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
                ObjectNode event = thinkerService.getEventNode(eventModel);

                ArrayNode invites = factory.arrayNode();

                for (InviteModel inviteModel : inviteRepo.findByEventId(eventModel.getId())) {
                    invites.add(thinkerService.getInviteNode(inviteModel));
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
    @PostMapping(value = {"/", "/create"})
    public ResponseEntity<String> create(@RequestBody EventModel eventModel, Errors errors) {
        try {
            validator.validate(eventModel, errors);

            if (errors.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(thinkerService.createValidationErrorNode(errors).toString());

            EventModel savedModel = eventRepo.save(eventModel);

            String msg = factory.objectNode().put("eventId", savedModel.getId()).toString();
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Edit event by overwriting database entry
     *
     * @param eventModel Must contain all fields of EventModel:
     *              String creatorId
     *              String eventName
     *              Long eventTime
     *              String eventPlace
     *              String info
     *              long inviteExpiry
     * @return JSON String
     */
    @PutMapping(value = {"/{id}", "/edit/{id}"})
    public ResponseEntity<String> edit(@PathVariable Long id, @RequestBody EventModel eventModel, Errors errors) {
        if (id == null) {
            String msg = factory.objectNode().put("error", "Event id is null").toString();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        validator.validate(eventModel, errors);
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(thinkerService.createValidationErrorNode(errors).toString());

        if (eventRepo.existsById(id)) {
            try {
                System.out.println(eventModel.isPublic());
                eventModel.hiddenIdSetter(id);
                eventRepo.save(eventModel);

                return ResponseEntity.status(HttpStatus.OK).body(null);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            String msg = factory.objectNode().put("error", "Event not found").toString();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    /**
     * Delete event
     *
     * @param id: Id of event.
     * @return JSON String
     */
    @DeleteMapping(value = {"/{id}", "/delete/{id}"})
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (id == null) {
            String msg = factory.objectNode().put("error", "Event id is null").toString();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

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
            String msg = factory.objectNode().put("error", "Event not found").toString();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }
}