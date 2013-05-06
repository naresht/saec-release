package com.bfds.saec.batch.in.infoage.rule;

import com.bfds.saec.batch.in.infoage.AddressResearchStatus;
import com.bfds.saec.batch.file.domain.in.infoage.AddressResearchUpdate;


/**
 * Rule Manager responsible for running Address Research rules.
 */
public interface RulesManager {

    /**
     * Fire all Rules for Individual Accounts
     *
     * @param account A single account on which rules should apply
     * @return status Status after applying rules. This status wraps the underlying research data.
     */
    AddressResearchStatus fireAllRules(AddressResearchUpdate account);

}
