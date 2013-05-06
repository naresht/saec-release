package com.bfds.saec.batch.in.infoage.rule;

import javax.inject.Named;

import com.bfds.saec.batch.file.domain.in.infoage.AddressResearchUpdate;
import com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_individual.HitIndicatorType;
import com.bfds.saec.batch.file.domain.in.infoage_individual.IndividualAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_individual.MatchIndicatorType;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfds.saec.batch.in.infoage.AddressResearchStatus;
import com.bfds.saec.batch.in.infoage.rule.type.MatchAnalysisType;
import com.bfds.saec.batch.in.infoage.rule.type.OFACHitIndicatorType;
import com.bfds.saec.batch.in.infoage.rule.type.SSNInvalidOrDeceasedType;

/**
 * Pojo based Rule Manager for Corporate Accounts
 */
@Named
public class SimpleRulesManager implements RulesManager {

    final Logger log = LoggerFactory.getLogger(SimpleRulesManager.class);

    @Override
    public AddressResearchStatus fireAllRules(AddressResearchUpdate addressResearchUpdate) {
        Preconditions.checkNotNull(addressResearchUpdate, "addressResearchUpdate is null");
        if(addressResearchUpdate instanceof IndividualAddressResearch) {
            return fireAllRules((IndividualAddressResearch) addressResearchUpdate);
        } else if(addressResearchUpdate instanceof CorporateAddressResearch) {
            return fireAllRules((CorporateAddressResearch) addressResearchUpdate);
        } else {
            throw new IllegalArgumentException("unsupported type "+addressResearchUpdate.getClass() );
        }
    }

    // This logic is NOT scalable and will break if the spec changes.
    private AddressResearchStatus fireAllRules(IndividualAddressResearch account) {
        log.info(String.format("Firing All Individual Account Rules for Account[%s]: ", account));

        AddressResearchStatus result = new AddressResearchStatus();
      
        // Update required fields
        result.setAddressResearchUpdate(account);

        // OFAC = O
        if (OFACHitIndicatorType.O.name().equals(account.getOfacIndicator())) {
            result.setAddressUpdated(false);
            result.setRpoObjectUpdated(false);
        }
       
        // HIT = N, Match Indicator = N
        else if (HitIndicatorType.N.equals(account.getHitIndicator()) &&
                MatchIndicatorType.N.equals(account.getMatchIndicator())) {

            result.setAddressUpdated(false);
            result.setRpoObjectUpdated(false);
        }

        // HIT = Y, Match Indicator = M or H
        else if (HitIndicatorType.Y.equals(account.getHitIndicator()) &&
                (MatchIndicatorType.M.equals(account.getMatchIndicator()) ||
                        MatchIndicatorType.H.equals(account.getMatchIndicator()))) {

            // Match Analysis = A
            if (MatchAnalysisType.A.name().equals(account.getMatchAnalysisTag())) {
                result.setAddressUpdated(false);
                result.setRpoObjectUpdated(false);
            } else {
                result.setAddressUpdated(true);
                result.setRpoObjectUpdated(true);
            }
        }

        // HIT = Y, Match Indicator = M, InvalidOrDiseased SSN = D or I
        else if (HitIndicatorType.Y.equals(account.getHitIndicator()) &&
                MatchIndicatorType.M.equals(account.getMatchIndicator()) &&
                (SSNInvalidOrDeceasedType.D.name().equals(account.getInvalidOrDeceasedSSNTag()) ||
                        SSNInvalidOrDeceasedType.I.name().equals(account.getInvalidOrDeceasedSSNTag()))) {

            // Match Analysis = A
            if (MatchAnalysisType.A.name().equals(account.getMatchAnalysisTag())) {
                result.setAddressUpdated(false);
                result.setRpoObjectUpdated(false);
            } else {
                result.setAddressUpdated(true);
                result.setRpoObjectUpdated(true);
            }
        }

        // HIT = Y, Match Indicator = H, InvalidOrDiseased SSN = D or I
        else if (HitIndicatorType.Y.equals(account.getHitIndicator()) &&
                MatchIndicatorType.H.equals(account.getMatchIndicator()) &&
                (SSNInvalidOrDeceasedType.D.name().equals(account.getInvalidOrDeceasedSSNTag()) ||
                        SSNInvalidOrDeceasedType.I.name().equals(account.getInvalidOrDeceasedSSNTag()))) {

            // Match Analysis = A
            if (MatchAnalysisType.A.name().equals(account.getMatchAnalysisTag())) {
                result.setAddressUpdated(false);
                result.setRpoObjectUpdated(false);
            } else {
                result.setAddressUpdated(true);
                result.setRpoObjectUpdated(true);
            }
        }
        
        log.info("All Individual Account Rules fired. Result: " + result);

        return result;
    }


    private AddressResearchStatus fireAllRules(CorporateAddressResearch account) {
        log.info(String.format("Firing All Corporate Account Rules for Account[%s]: ", account.getUserRef()));
        final AddressResearchStatus result = new AddressResearchStatus();
        
        // Update required fields
        result.setAddressResearchUpdate(account);
        // HIT='Y'
        if (account.isHit() && !MatchAnalysisType.A.name().equals(account.getMatchAnalysisTag())) {
            result.setAddressUpdated(true);
            result.setRpoObjectUpdated(true);
        } else { // HIT='N'
            result.setAddressUpdated(false);
            result.setRpoObjectUpdated(false);
        }

        log.info("All Corporate Account Rules fired. Result: " + result);

        return result;
    }
}
