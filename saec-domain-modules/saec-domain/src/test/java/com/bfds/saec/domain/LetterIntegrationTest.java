package com.bfds.saec.domain;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

//@RooIntegrationTest(entity = Letter.class)
public class LetterIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
    
    @Test
    @Ignore("strange problem in roo genetrated test. The entity gets deleted from DB and yet the same entity is being returned by find()")
    public void testRemove() {
        
    }
}