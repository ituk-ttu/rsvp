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
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

@Service
public class ThinkerService {
    private JsonNodeFactory factory;

    public ThinkerService() {
        factory = new JsonNodeFactory(false);
    }

    @Autowired
    EventRepo eventRepo;
    @Autowired
    InviteRepo inviteRepo;

    public ObjectNode getEventNode(EventModel eventModel) {
        ObjectNode root = factory.objectNode();

        root.put(Constants.ID, eventModel.getId());
        root.put(Constants.E_ISPUBLIC, eventModel.isPublic());
        root.put(Constants.E_CREATORID, eventModel.getCreatorId());
        root.put(Constants.E_NAME, eventModel.getEventName());
        root.put(Constants.E_TIME, eventModel.getEventTime());
        root.put(Constants.E_PLACE, eventModel.getEventPlace());
        root.put(Constants.INFO, eventModel.getInfo());
        root.put(Constants.E_EXPIRE, eventModel.getInviteExpire());

        return root;
    }

    public String createValidationErrorNode(Errors errors) {
        ObjectNode root = factory.objectNode();
        ArrayNode errorArray = factory.arrayNode();
        for (ObjectError objectError : errors.getAllErrors()) {
            errorArray.add(objectError.getDefaultMessage());
        }
        root.putPOJO("errors", errorArray);
        return root.toString();
    }

    public ObjectNode getInviteNode(InviteModel inviteModel) {
        ObjectNode root = factory.objectNode();

        root.put(Constants.ID, inviteModel.getId());
        root.put(Constants.E_ID, inviteModel.getEventId());
        root.put(Constants.I_NAME, inviteModel.getName());
        root.put(Constants.INFO, inviteModel.getInfo());
        root.put(Constants.I_ISANSWERED, inviteModel.getComing());
        return root;
    }
}
