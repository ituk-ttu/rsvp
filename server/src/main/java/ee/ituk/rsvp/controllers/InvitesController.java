package ee.ituk.rsvp.controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ee.ituk.rsvp.database.EventModel;
import ee.ituk.rsvp.database.EventRepo;
import ee.ituk.rsvp.database.InviteModel;
import ee.ituk.rsvp.database.InviteRepo;
import ee.ituk.rsvp.validation.InviteRequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/invites")
@CrossOrigin
public class InvitesController {
    private JsonNodeFactory factory;

    public InvitesController() {
        factory = new JsonNodeFactory(false);
    }

    @Resource
    private InviteRequestValidator validator;
    @Resource
    private ThinkerService thinkerService;
    @Resource
    private InviteRepo inviteRepo;
    @Resource
    private EventRepo eventRepo;

    /**
     * Returns all invites in database
     *
     * @return JSON String
     */
    @GetMapping(value = {"", "/", "/all"})
    public ResponseEntity<String> all() {
        try {
            ArrayNode invites = factory.arrayNode();
            for (InviteModel inviteModel : inviteRepo.findAll()) {
                invites.add(thinkerService.getInviteNode(inviteModel));
            }

            return ResponseEntity.status(HttpStatus.OK).body(invites.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Create invite. Always sets 'coming' to false.
     *
     * @param inviteModel Must contain 'int eventId', 'String name', 'String info', 'boolean coming'
     * @return JSON String
     */
    @PostMapping(value = {"/", "/create"})
    public ResponseEntity<String> create(@RequestBody InviteModel inviteModel, Errors errors) {
        try {
            System.out.println(inviteModel);
            validator.validate(inviteModel, errors);
            validator.checkForDataValidity(inviteModel, errors);

            if (errors.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(thinkerService.createValidationErrorNode(errors).toString());

            inviteModel.setIsComing(false);
            InviteModel savedModel = inviteRepo.save(inviteModel);

            String msg = factory.objectNode().put("inviteId", savedModel.getId()).toString();
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Update invite by overwriting database entry
     *
     * @param id Id of invite
     * @param inviteModel Must contain 'int eventId', 'String name', 'String info', 'boolean coming'
     * @return JSON String
     */
    @PutMapping(value = {"/{id}", "/edit/{id}"})
    public ResponseEntity<String> edit(@PathVariable String id, @RequestBody InviteModel inviteModel, Errors errors) {
        if (id == null) {
            String msg = factory.objectNode().put("error", "Invite id is null").toString();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        validator.validate(inviteModel, errors);
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(thinkerService.createValidationErrorNode(errors).toString());

        if (inviteRepo.existsById(id)) {
            try {
                inviteModel.hiddenIdSetter(id);
                inviteRepo.save(inviteModel);

                return ResponseEntity.status(HttpStatus.OK).body(null);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            String msg = factory.objectNode().put("error", "Invite not found").toString();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    /**
     * Delete invite
     *
     * @param id Id of invite
     * @return JSON String
     */
    @DeleteMapping(value = {"/{id}", "/delete/{id}"})
    public ResponseEntity<String> delete(@PathVariable String id) {
        if (id == null) {
            String msg = factory.objectNode().put("error", "Invite id is null").toString();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        Optional<InviteModel> oInviteModel = inviteRepo.findById(id);

        if (oInviteModel.isPresent()) {
            try {
                inviteRepo.delete(oInviteModel.get());
                return ResponseEntity.status(HttpStatus.OK).body(null);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            String msg = factory.objectNode().put("error", "Invite not found").toString();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    /**
     * Get invite info with accompanying event
     *
     * @param id Id of invite
     * @return JSON String
     */
    @PostMapping(value = "/get/{id}")
    public ResponseEntity<String> get(@PathVariable String id) {
        if (id == null) {
            String msg = factory.objectNode().put("error", "Invite id is null").toString();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        Optional<InviteModel> oInviteModel = inviteRepo.findById(id);
        if (!oInviteModel.isPresent()) {
            String msg = factory.objectNode().put("error", "Invite not found").toString();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }

        InviteModel inviteModel = oInviteModel.get();

        Optional<EventModel> oEventModel = eventRepo.findById(inviteModel.getEventId());
        if (!oEventModel.isPresent()) {
            String msg = factory.objectNode().put("error", "Event not found").toString();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }

        EventModel eventModel = oEventModel.get();

        ObjectNode root = factory.objectNode();
        root.putPOJO("invite", thinkerService.getInviteNode(inviteModel));
        root.putPOJO("event", thinkerService.getEventNode(eventModel));

        return ResponseEntity.status(HttpStatus.OK).body(root.toString());
    }
}
