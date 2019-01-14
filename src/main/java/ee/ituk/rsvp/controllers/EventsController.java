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
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

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
    public String showAll() {
        ObjectNode root = factory.objectNode();
        root.put(Constants.TYPE, "events");

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

            root.putPOJO(Constants.EVENTS, events);
        } catch (Exception e) {
            root = thinkingClass.getErrorNode(e);
            e.printStackTrace();
        }
        return root.toString();
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
     *              long inviteExpiri
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
     * @param eventModel Must contain 'long eventId'
     * @return JSON String
     */
    @PostMapping(value = "/delete")
    public String eventDelete(@ModelAttribute EventModel eventModel) {
        ObjectNode root = factory.objectNode();

        if (eventRepo.existsById(eventModel.getId())) {
            try {
                eventRepo.delete(eventModel);
                root.put(Constants.STATUS, Constants.STATUS_OK);
                root.put(Constants.INFO, "Deletion successful");
                root.put(Constants.ID, eventModel.getId());
            } catch (Exception e) {
                root = thinkingClass.getErrorNode(e);
                root.put(Constants.ID, eventModel.getId());
                e.printStackTrace();
            }
            return root.toString();
        } else {
            root.put(Constants.STATUS, Constants.STATUS_OK);
            root.put(Constants.INFO, "Event not found!");
            root.put(Constants.ID, eventModel.getId());
            return root.toString();
        }
    }
}