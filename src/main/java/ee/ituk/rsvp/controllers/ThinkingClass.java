package ee.ituk.rsvp.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ee.ituk.rsvp.util.Constants;
import ee.ituk.rsvp.database.EventModel;
import ee.ituk.rsvp.database.EventRepo;
import ee.ituk.rsvp.database.InviteModel;
import ee.ituk.rsvp.database.InviteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThinkingClass {
    private JsonNodeFactory factory;

    public ThinkingClass() {
        factory = new JsonNodeFactory(false);
    }

    @Autowired
    EventRepo eventRepo;
    @Autowired
    InviteRepo inviteRepo;

    public ObjectNode getInviteNode(InviteModel inviteModel) {
        ObjectNode root = factory.objectNode();
        this.populateInviteNode(inviteModel, root);
        return root;
    }

    public void populateInviteNode(InviteModel inviteModel, ObjectNode root) {
        root.put(Constants.ID, inviteModel.getId());
        root.put(Constants.E_ID, inviteModel.getEventId());
        root.put(Constants.I_NAME, inviteModel.getName());
        root.put(Constants.INFO, inviteModel.getInfo());
        root.put(Constants.I_ISANSWERED, inviteModel.isComing());
    }

    public ObjectNode getEventNode(EventModel eventModel) {
        ObjectNode root = factory.objectNode();
        this.populateEventNode(eventModel, root);
        return root;
    }

    public void populateEventNode(EventModel eventModel, ObjectNode root) {
        root.put(Constants.ID, eventModel.getId());
        root.put(Constants.E_ISPUBLIC, eventModel.isPublic());
        root.put(Constants.E_CREATORID, eventModel.getCreatorId());
        root.put(Constants.E_NAME, eventModel.getEventName());
        root.put(Constants.E_TIME, eventModel.getEventTime());
        root.put(Constants.E_PLACE, eventModel.getEventPlace());
        root.put(Constants.INFO, eventModel.getInfo());
        root.put(Constants.E_EXPIRE, eventModel.getInviteExpire());
    }

    public ObjectNode getErrorNode(Exception e) {
        ObjectNode root = factory.objectNode();
        this.populateErrorNode(e, root);
        return root;
    }

    public void populateErrorNode(Exception e, ObjectNode root) {
        root.put(Constants.STATUS, Constants.STATUS_NOTOK);
        root.put(Constants.ERROR, e.getClass().getSimpleName());
    }
}
