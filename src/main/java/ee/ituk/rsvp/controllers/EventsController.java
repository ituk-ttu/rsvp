package ee.ituk.rsvp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ee.ituk.rsvp.util.Constants;
import ee.ituk.rsvp.database.EventModel;
import ee.ituk.rsvp.database.EventRepo;
import ee.ituk.rsvp.database.InviteModel;
import ee.ituk.rsvp.database.InviteRepo;
import ee.ituk.rsvp.validation.EventRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventsController {
    private JsonNodeFactory factory;
    private ThinkingClass thinkingClass;
    private SimpleDateFormat dateFormat;
    private ObjectMapper objectMapper;

    public EventsController() {
        factory = new JsonNodeFactory(false);
        thinkingClass = new ThinkingClass();
        dateFormat = new SimpleDateFormat("dd.MM.YYYY HH:mm");

    }

    @Autowired
    EventRequestValidator validator;
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
    @PostMapping(value = {"/", "/create"})
    public ResponseEntity<String> create(@Valid @RequestBody EventModel eventModel, Errors errors) {
        try {
            validator.validate(eventModel, errors);

            if (errors.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(thinkingClass.createValidationErrorNode(errors));

            eventRepo.save(eventModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
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
     *              String eventTime
     *              String eventPlace
     *              String info
     *              long inviteExpiry
     * @return JSON String
     */
    @PutMapping(value = {"/{id}", "/edit/{id}"})
    public ResponseEntity<String> edit(@PathVariable Long id, @Valid @RequestBody EventModel eventModel, Errors errors) {
        if (id == null) {
            String msg = factory.objectNode().put("error", "Event ID is null").toString();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        validator.validate(eventModel, errors);
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(thinkingClass.createValidationErrorNode(errors));

        if (eventRepo.existsById(id)) {
            try {
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
            String msg = factory.objectNode().put("error", "Event ID is null").toString();
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