package ee.ituk.rsvp.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


public class ControllersLoadTest {
    @Autowired
    private EventsController eventsController;
    @Autowired
    private InvitesController invitesController;


    public void eventsControllerContextLoads() throws Exception {
        assertThat(eventsController).isNotNull();
    }


    public void invitesControllerContextLoads() throws Exception {
        assertThat(invitesController).isNotNull();
    }
}
