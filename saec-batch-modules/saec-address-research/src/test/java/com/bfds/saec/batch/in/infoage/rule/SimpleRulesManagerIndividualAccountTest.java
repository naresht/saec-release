package com.bfds.saec.batch.in.infoage.rule;


import com.bfds.saec.batch.in.infoage.AddressResearchStatus;
import com.bfds.saec.batch.in.infoage.rule.type.MatchAnalysisType;
import com.bfds.saec.batch.in.infoage.rule.type.OFACHitIndicatorType;
import com.bfds.saec.batch.file.domain.in.infoage_individual.HitIndicatorType;
import com.bfds.saec.batch.file.domain.in.infoage_individual.IndividualAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_individual.MatchIndicatorType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleRulesManagerIndividualAccountTest {

    SimpleRulesManager rulesManager = new SimpleRulesManager();

    @Test
    public void testHIT_Y_OFAC() {
        IndividualAddressResearch account = testData();
        account.setHitIndicator(HitIndicatorType.Y);
        account.setOfacIndicator(OFACHitIndicatorType.O.name());

        AddressResearchStatus status = rulesManager.fireAllRules(account);

        assertNotNull(status);
        assertFalse(status.isAddressUpdated());
        assertFalse(status.isRpoObjectUpdated());
    }

    @Test
    public void testHIT_N_OFAC() {
        IndividualAddressResearch account = testData();
        account.setHitIndicator(HitIndicatorType.N);
        account.setOfacIndicator(OFACHitIndicatorType.O.name());

        AddressResearchStatus status = rulesManager.fireAllRules(account);

        assertNotNull(status);
        assertFalse(status.isAddressUpdated());
        assertFalse(status.isRpoObjectUpdated());
    }

    @Test
    public void testHIT_Y_Match_M() {
        IndividualAddressResearch account = testData();
        account.setHitIndicator(HitIndicatorType.Y);
        account.setMatchIndicator(MatchIndicatorType.M);

        AddressResearchStatus status = rulesManager.fireAllRules(account);

        assertNotNull(status);
        assertTrue(status.isAddressUpdated());
        assertTrue(status.isRpoObjectUpdated());
    }

    @Test
    public void testHIT_Y_Match_H() {
        IndividualAddressResearch account = testData();
        account.setHitIndicator(HitIndicatorType.Y);
        account.setMatchIndicator(MatchIndicatorType.H);

        AddressResearchStatus status = rulesManager.fireAllRules(account);

        assertNotNull(status);
        assertTrue(status.isAddressUpdated());
        assertTrue(status.isRpoObjectUpdated());
    }

    @Test
    public void testHIT_N_Match_N() {
        IndividualAddressResearch account = testData();
        account.setHitIndicator(HitIndicatorType.N);
        account.setMatchIndicator(MatchIndicatorType.N);

        AddressResearchStatus status = rulesManager.fireAllRules(account);

        assertNotNull(status);
        assertFalse(status.isAddressUpdated());
        assertFalse(status.isRpoObjectUpdated());
    }

    @Test
    public void testHIT_Y_Match_M_MatchAnalysis_A() {
        IndividualAddressResearch account = testData();
        account.setHitIndicator(HitIndicatorType.Y);
        account.setMatchIndicator(MatchIndicatorType.M);
        account.setMatchAnalysisTag(MatchAnalysisType.A.name());

        AddressResearchStatus status = rulesManager.fireAllRules(account);

        assertNotNull(status);
        assertFalse(status.isAddressUpdated());
        assertFalse(status.isRpoObjectUpdated());
    }

    @Test
    public void testHIT_Y_Match_H_MatchAnalysis_A() {
        IndividualAddressResearch account = testData();
        account.setHitIndicator(HitIndicatorType.Y);
        account.setMatchIndicator(MatchIndicatorType.M);
        account.setMatchAnalysisTag(MatchAnalysisType.A.name());

        AddressResearchStatus status = rulesManager.fireAllRules(account);

        assertNotNull(status);
        assertFalse(status.isAddressUpdated());
        assertFalse(status.isRpoObjectUpdated());
    }


    private IndividualAddressResearch testData() {
        IndividualAddressResearch account = new IndividualAddressResearch();
        account.setUserRef("10000001");
        account.setAddressDateReported("201201--");
        return account;
    }

}
