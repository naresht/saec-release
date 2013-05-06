package com.bfds.saec.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext-test.xml" })
@Transactional
public class EventTest{

    @Test
    public void testGetCurrentEvent() {
        newEvent();
        List<Event> allEvents = Event.findAllEvents();
        assertNotNull(allEvents);
    }

    private Event newEvent() {
        Event event = new Event();
        event.setBank(new Bank());
        event.setBankCode("bc");
        event.setCheckNameforBottomlineOutFile("CHKNAMESSCBLOUTFILE");
        event.setCanChangeStausOfStaleCheck(true);
        event.setCheckStaleByDate(new Date(111, 1, 1));
        event.setCheckStaleInDays(2);
        event.setCheckStartingNo(1L);
        event.setCode("code_0");
        event.setCorrespondenceThresholdLimit(1.0);
        event.setDda("7777777");
        event.setDescription("description_0");
        event.setInfoAgeReviewMailDistributionList("sa");
        event.setMailDistributionList("DistList");
        event.setMailingControlSequence(1L);
        event.setName("N1");
        event.setNotificationEmailTo("e2");
        event.setRequiresAddressPrescrub(true);
        event.setVersion(1);
        event.persist();

        return event;

    }

    @Test
    public void varifyGetCurrentEventCode() {
        newEvent();
        assertNotNull(Event.getCurrentEventCode());
        //assertEquals("code_0", Event.getCurrentEventCode());

    }

    @Test
    public void varifyGetCurrentEventDescription() {
        newEvent();
        assertNotNull(Event.getCurrentEventDescription());
        //assertEquals("description_0", Event.getCurrentEventDescription());

    }

    @Test
    public void varifyGetNextCheckSequenceNumber() {
        Event event = newEvent();
        event.getNextCheckSequenceNumber();
        assertNotNull(event.getNextCheckSequenceNumber());
        assertEquals("4", event.getNextCheckSequenceNumber());
    }

}
