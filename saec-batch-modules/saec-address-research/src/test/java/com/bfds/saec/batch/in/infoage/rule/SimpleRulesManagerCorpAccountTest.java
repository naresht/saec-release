package com.bfds.saec.batch.in.infoage.rule;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_corporate.HitIndicatorCorpType;
import org.junit.Test;

import com.bfds.saec.batch.in.infoage.AddressResearchStatus;



public class SimpleRulesManagerCorpAccountTest {

    SimpleRulesManager rulesManager = new SimpleRulesManager();

    @Test
    public void testHIT_Y() {
        CorporateAddressResearch account = testData();
        account.setHitIndicator(HitIndicatorCorpType.Y);

        AddressResearchStatus status = rulesManager.fireAllRules(account);

        assertNotNull(status);
        assertTrue(status.isAddressUpdated());
        assertTrue(status.isRpoObjectUpdated());
    }

    @Test
    public void testHIT_Y_MatchAnalysis_A() {
        CorporateAddressResearch account = testData();
        account.setHitIndicator(HitIndicatorCorpType.Y);
        account.setMatchAnalysisTag("A");

        AddressResearchStatus status = rulesManager.fireAllRules(account);
        assertNotNull(status);
        assertFalse(status.isAddressUpdated());
        assertFalse(status.isRpoObjectUpdated());
    }

    @Test
    public void testHIT_N() {
        CorporateAddressResearch account = testData();
        account.setHitIndicator(HitIndicatorCorpType.N);

        AddressResearchStatus status = rulesManager.fireAllRules(account);

        assertNotNull(status);
        assertFalse(status.isAddressUpdated());
        assertFalse(status.isRpoObjectUpdated());
    }

    private CorporateAddressResearch testData() {
        CorporateAddressResearch account = new CorporateAddressResearch();
        account.setUserRef("10000001");
        account.setAddressDateReported("201201--");
        return account;
    }

}
