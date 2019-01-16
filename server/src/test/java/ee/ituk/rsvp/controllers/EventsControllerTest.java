package ee.ituk.rsvp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

//@RunWith(SpringRunner.class)
//@WebMvcTest(EventsController.class)
public class EventsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ThinkerService thinkerService;

}
