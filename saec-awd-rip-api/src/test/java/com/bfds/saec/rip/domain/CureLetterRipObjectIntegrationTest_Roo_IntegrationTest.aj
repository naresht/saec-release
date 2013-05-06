// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.rip.domain;

import com.bfds.saec.rip.domain.CureLetterRipObject;
import com.bfds.saec.rip.domain.CureLetterRipObjectDataOnDemand;
import com.bfds.saec.rip.domain.CureLetterRipObjectIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CureLetterRipObjectIntegrationTest_Roo_IntegrationTest {
    
    declare @type: CureLetterRipObjectIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: CureLetterRipObjectIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: CureLetterRipObjectIntegrationTest: @Transactional;
    
    @Autowired
    private CureLetterRipObjectDataOnDemand CureLetterRipObjectIntegrationTest.dod;
    
    @Test
    public void CureLetterRipObjectIntegrationTest.testCountCureLetterRipObjects() {
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to initialize correctly", dod.getRandomCureLetterRipObject());
        long count = CureLetterRipObject.countCureLetterRipObjects();
        Assert.assertTrue("Counter for 'CureLetterRipObject' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void CureLetterRipObjectIntegrationTest.testFindCureLetterRipObject() {
        CureLetterRipObject obj = dod.getRandomCureLetterRipObject();
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to provide an identifier", id);
        obj = CureLetterRipObject.findCureLetterRipObject(id);
        Assert.assertNotNull("Find method for 'CureLetterRipObject' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'CureLetterRipObject' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void CureLetterRipObjectIntegrationTest.testFindAllCureLetterRipObjects() {
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to initialize correctly", dod.getRandomCureLetterRipObject());
        long count = CureLetterRipObject.countCureLetterRipObjects();
        Assert.assertTrue("Too expensive to perform a find all test for 'CureLetterRipObject', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<CureLetterRipObject> result = CureLetterRipObject.findAllCureLetterRipObjects();
        Assert.assertNotNull("Find all method for 'CureLetterRipObject' illegally returned null", result);
        Assert.assertTrue("Find all method for 'CureLetterRipObject' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void CureLetterRipObjectIntegrationTest.testFindCureLetterRipObjectEntries() {
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to initialize correctly", dod.getRandomCureLetterRipObject());
        long count = CureLetterRipObject.countCureLetterRipObjects();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<CureLetterRipObject> result = CureLetterRipObject.findCureLetterRipObjectEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'CureLetterRipObject' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'CureLetterRipObject' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void CureLetterRipObjectIntegrationTest.testFlush() {
        CureLetterRipObject obj = dod.getRandomCureLetterRipObject();
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to provide an identifier", id);
        obj = CureLetterRipObject.findCureLetterRipObject(id);
        Assert.assertNotNull("Find method for 'CureLetterRipObject' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyCureLetterRipObject(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'CureLetterRipObject' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void CureLetterRipObjectIntegrationTest.testMergeUpdate() {
        CureLetterRipObject obj = dod.getRandomCureLetterRipObject();
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to provide an identifier", id);
        obj = CureLetterRipObject.findCureLetterRipObject(id);
        boolean modified =  dod.modifyCureLetterRipObject(obj);
        Integer currentVersion = obj.getVersion();
        CureLetterRipObject merged = (CureLetterRipObject)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'CureLetterRipObject' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void CureLetterRipObjectIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to initialize correctly", dod.getRandomCureLetterRipObject());
        CureLetterRipObject obj = dod.getNewTransientCureLetterRipObject(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'CureLetterRipObject' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'CureLetterRipObject' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void CureLetterRipObjectIntegrationTest.testRemove() {
        CureLetterRipObject obj = dod.getRandomCureLetterRipObject();
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'CureLetterRipObject' failed to provide an identifier", id);
        obj = CureLetterRipObject.findCureLetterRipObject(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'CureLetterRipObject' with identifier '" + id + "'", CureLetterRipObject.findCureLetterRipObject(id));
    }
    
}