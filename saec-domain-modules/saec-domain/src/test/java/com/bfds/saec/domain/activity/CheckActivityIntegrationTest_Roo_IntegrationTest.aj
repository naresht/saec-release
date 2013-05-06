// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.activity;

import com.bfds.saec.domain.activity.CheckActivity;
import com.bfds.saec.domain.activity.CheckActivityDataOnDemand;
import com.bfds.saec.domain.activity.CheckActivityIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CheckActivityIntegrationTest_Roo_IntegrationTest {
    
    declare @type: CheckActivityIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: CheckActivityIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: CheckActivityIntegrationTest: @Transactional;
    
    @Autowired
    private CheckActivityDataOnDemand CheckActivityIntegrationTest.dod;
    
    @Test
    public void CheckActivityIntegrationTest.testCountCheckActivitys() {
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to initialize correctly", dod.getRandomCheckActivity());
        long count = CheckActivity.countCheckActivitys();
        Assert.assertTrue("Counter for 'CheckActivity' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void CheckActivityIntegrationTest.testFindCheckActivity() {
        CheckActivity obj = dod.getRandomCheckActivity();
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to provide an identifier", id);
        obj = CheckActivity.findCheckActivity(id);
        Assert.assertNotNull("Find method for 'CheckActivity' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'CheckActivity' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void CheckActivityIntegrationTest.testFindAllCheckActivitys() {
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to initialize correctly", dod.getRandomCheckActivity());
        long count = CheckActivity.countCheckActivitys();
        Assert.assertTrue("Too expensive to perform a find all test for 'CheckActivity', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<CheckActivity> result = CheckActivity.findAllCheckActivitys();
        Assert.assertNotNull("Find all method for 'CheckActivity' illegally returned null", result);
        Assert.assertTrue("Find all method for 'CheckActivity' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void CheckActivityIntegrationTest.testFindCheckActivityEntries() {
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to initialize correctly", dod.getRandomCheckActivity());
        long count = CheckActivity.countCheckActivitys();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<CheckActivity> result = CheckActivity.findCheckActivityEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'CheckActivity' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'CheckActivity' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void CheckActivityIntegrationTest.testFlush() {
        CheckActivity obj = dod.getRandomCheckActivity();
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to provide an identifier", id);
        obj = CheckActivity.findCheckActivity(id);
        Assert.assertNotNull("Find method for 'CheckActivity' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyCheckActivity(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'CheckActivity' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void CheckActivityIntegrationTest.testMergeUpdate() {
        CheckActivity obj = dod.getRandomCheckActivity();
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to provide an identifier", id);
        obj = CheckActivity.findCheckActivity(id);
        boolean modified =  dod.modifyCheckActivity(obj);
        Integer currentVersion = obj.getVersion();
        CheckActivity merged = (CheckActivity)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'CheckActivity' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void CheckActivityIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to initialize correctly", dod.getRandomCheckActivity());
        CheckActivity obj = dod.getNewTransientCheckActivity(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'CheckActivity' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'CheckActivity' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void CheckActivityIntegrationTest.testRemove() {
        CheckActivity obj = dod.getRandomCheckActivity();
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'CheckActivity' failed to provide an identifier", id);
        obj = CheckActivity.findCheckActivity(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'CheckActivity' with identifier '" + id + "'", CheckActivity.findCheckActivity(id));
    }
    
}
