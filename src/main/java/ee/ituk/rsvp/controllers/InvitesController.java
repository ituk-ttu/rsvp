package ee.ituk.rsvp.controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ee.ituk.rsvp.util.Constants;
import ee.ituk.rsvp.database.EventModel;
import ee.ituk.rsvp.database.EventRepo;
import ee.ituk.rsvp.database.InviteModel;
import ee.ituk.rsvp.database.InviteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/invites")
public class InvitesController {
    private JsonNodeFactory factory;
    private ThinkingClass thinkingClass;

    public InvitesController() {
        factory = new JsonNodeFactory(false);
        thinkingClass = new ThinkingClass();
    }

    @Autowired
    InviteRepo inviteRepo;
    @Autowired
    EventRepo eventRepo;

    /**
     * Returns all invites in database
     *
     * @return JSON String
     */
    @GetMapping(value={"", "/", "/all"})
    public String showAll() {
        ObjectNode root = factory.objectNode();
        root.put(Constants.TYPE, "invites");

        try {
            ArrayNode invites = factory.arrayNode();
            for (InviteModel inviteModel : inviteRepo.findAll()) {
                invites.add(thinkingClass.getInviteNode(inviteModel));
            }

            root.put(Constants.STATUS, Constants.STATUS_OK);
            root.putPOJO(Constants.INVITES, invites);
        } catch (Exception e) {
            root = thinkingClass.getErrorNode(e);
            e.printStackTrace();
        }
        return root.toString();
    }

    /**
     * Create invite
     *
     * @param inviteModel Must contain 'int eventId', 'String name', 'String info', 'boolean coming'
     * @return JSON String
     */
    @PostMapping("/create")
    public String inviteCreate(@ModelAttribute InviteModel inviteModel) {
        ObjectNode root = factory.objectNode();

        try {
            inviteRepo.save(inviteModel);
            root.put(Constants.STATUS, Constants.STATUS_OK);
            thinkingClass.populateInviteNode(inviteModel, root);
        } catch (Exception e) {
            thinkingClass.populateErrorNode(e, root);
            root.put(Constants.ID, inviteModel.getId());
            e.printStackTrace();
        }
        return root.toString();
    }

    /**
     * Update invite by overwriting database entry
     *
     * @param inviteId InviteId
     * @param inviteModel Must contain all fields of InviteModel:
     *               eventId
     *               name
     *               info
     *               answer
     * @return JSON String
     */
    @PostMapping(value="/edit")
    public String inviteEdit(@RequestParam("inviteId") long inviteId, @ModelAttribute InviteModel inviteModel) {
        ObjectNode root = factory.objectNode();

        if (inviteRepo.existsById(inviteId)) {
            try {
                InviteModel prevInviteModel = inviteRepo.findById(inviteId).get();

                if (prevInviteModel.isComing() != inviteModel.isComing()) {
                    Optional<EventModel> oEventModel = eventRepo.findById(inviteModel.getEventId());
                    if (oEventModel.isPresent()) {
                        EventModel eventModel = oEventModel.get();

                        if (eventModel.getInviteExpire() < System.currentTimeMillis() / 1000) {
                            root.put(Constants.STATUS, Constants.STATUS_NOTOK);
                            root.put(Constants.INFO, "Expiry date has passed.");
                            root.put(Constants.ID, inviteId);
                            return root.toString();
                        }
                    } else {
                        root.put(Constants.STATUS, Constants.STATUS_NOTOK);
                        root.put(Constants.INFO, "Could not find event with id: " + inviteModel.getEventId());
                        root.put(Constants.ID, inviteId);
                        return root.toString();
                    }
                }

                inviteModel.hiddenIdSetter(inviteId);
                inviteRepo.save(inviteModel);

                root.put(Constants.STATUS, Constants.STATUS_OK);
                thinkingClass.populateInviteNode(inviteModel, root);
            } catch (Exception e) {
                thinkingClass.populateErrorNode(e, root);
                e.printStackTrace();
            }
        } else {
            root.put(Constants.STATUS, Constants.STATUS_NOTOK);
            root.put(Constants.INFO, "Invite not found");
            root.put(Constants.ID, inviteId);
        }
        return root.toString();
    }

    /**
     * Delete invite
     *
     * @param id Must contain 'long inviteId'
     * @return JSON String
     */
    @PostMapping(value="/delete")
    public String inviteDelete(@RequestParam("inviteId") long id) {
        Optional<InviteModel> oInviteModel = inviteRepo.findById(id);
        ObjectNode root = factory.objectNode();

        if (oInviteModel.isPresent()) {
            try {
                inviteRepo.delete(oInviteModel.get());
                root.put(Constants.STATUS, Constants.STATUS_OK);
                root.put(Constants.INFO, "Deletion successful");
                root.put(Constants.ID, id);
            } catch (Exception e) {
                thinkingClass.populateErrorNode(e, root);
                root.put(Constants.ID, id);
                e.printStackTrace();
            }
            return root.toString();
        } else {
            root.put(Constants.STATUS, Constants.STATUS_OK);
            root.put(Constants.INFO, "Invite not found!");
            root.put(Constants.ID, id);
            return root.toString();
        }
    }

    /**
     * Get invite info with accompanying event
     *
     * @param id Invite id
     * @return JSON String
     */
    @PostMapping(value = "/get")
    public String getInvite(@RequestParam("inviteId") long id) {
        Optional<InviteModel> oInviteModel = inviteRepo.findById(id);
        ObjectNode root = factory.objectNode();

        if (oInviteModel.isPresent()) {
            InviteModel inviteModel = oInviteModel.get();
            Optional<EventModel> oEventModel = eventRepo.findById(inviteModel.getEventId());

            if (oEventModel.isPresent()) {
                EventModel eventModel = oEventModel.get();

                root.putPOJO("invite", thinkingClass.getInviteNode(inviteModel));
                root.putPOJO("event", thinkingClass.getEventNode(eventModel));
            } else {
                root.put(Constants.STATUS, Constants.STATUS_NOTOK);
                root.put(Constants.INFO, "Event not found");
                root.put(Constants.ID, inviteModel.getEventId());
            }
        } else {
            root.put(Constants.STATUS, Constants.STATUS_NOTOK);
            root.put(Constants.INFO, "Invite not found");
            root.put(Constants.ID, id);
        }
        return root.toString();
    }
}
