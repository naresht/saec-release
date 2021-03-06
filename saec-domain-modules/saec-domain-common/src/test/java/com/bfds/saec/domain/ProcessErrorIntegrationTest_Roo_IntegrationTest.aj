// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.ProcessError;
import com.bfds.saec.domain.ProcessErrorDataOnDemand;
import com.bfds.saec.domain.ProcessErrorIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ProcessErrorIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ProcessErrorIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ProcessErrorIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ProcessErrorIntegrationTest: @Transactional;
    
    @Autowired
    private ProcessErrorDataOnDemand ProcessErrorIntegrationTest.dod;
    
    @Test
    public void ProcessErrorIntegrationTest.testCountProcessErrors() {
        Assert.assertNotNull("Data on demand for 'ProcessError' failed to initialize correctly", dod.getRandomProcessError());
        long count = ProcessError.countProcessErrors();
        Assert.assertTrue("Counter for 'ProcessError' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ProcessErrorIntegrationTest.testFindProcessError() {
        ProcessError obj = dod.getRandomProcessError();
        Assert.assertNotNull("Data on demand for 'ProcessError' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProcessError' failed to provide an identifier", id);
        obj = ProcessError.findProcessError(id);
        Assert.assertNotNull("Find method for 'ProcessError' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ProcessError' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ProcessErrorIntegrationTest.testFindAllProcessErrors() {
        Assert.assertNotNull("Data on demand for 'ProcessError' failed to initialize correctly", dod.getRandomProcessError());
        long count = ProcessError.countProcessErrors();
        Assert.assertTrue("Too expensive to perform a find all test for 'ProcessError', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ProcessError> result = ProcessError.findAllProcessErrors();
        Assert.assertNotNull("Find all method for 'ProcessError' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ProcessError' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ProcessErrorIntegrationTest.testFindProcessErrorEntries() {
        Assert.assertNotNull("Data on demand for 'ProcessError' failed to initialize correctly", dod.getRandomProcessError());
        long count = ProcessError.countProcessErrors();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ProcessError> result = ProcessError.findProcessErrorEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ProcessError' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ProcessError' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ProcessErrorIntegrationTest.testFlush() {
        ProcessError obj = dod.getRandomProcessError();
        Assert.assertNotNull("Data on demand for 'ProcessError' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProcessError' failed to provide an identifier", id);
        obj = ProcessError.findProcessError(id);
        Assert.assertNotNull("Find method for 'ProcessError' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyProcessError(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ProcessError' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ProcessErrorIntegrationTest.testMergeUpdate() {
        ProcessError obj = dod.getRandomProcessError();
        Assert.assertNotNull("Data on demand for 'ProcessError' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProcessError' failed to provide an identifier", id);
        obj = ProcessError.findProcessError(id);
        boolean modified =  dod.modifyProcessError(obj);
        Integer currentVersion = obj.getVersion();
        ProcessError merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ProcessError' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ProcessErrorIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'ProcessError' failed to initialize correctly", dod.getRandomProcessError());
        ProcessError obj = dod.getNewTransientProcessError(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ProcessError' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ProcessError' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'ProcessError' identifier to no longer be null", obj.getId());
    }
    
}
