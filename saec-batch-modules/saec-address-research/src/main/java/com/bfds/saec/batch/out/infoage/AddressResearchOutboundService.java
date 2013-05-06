package com.bfds.saec.batch.out.infoage;

import com.bfds.saec.batch.file.domain.out.infoage.AddressResearchOut;
import com.bfds.saec.batch.out.infoage.AddressResearchOutboundNotification;
import com.bfds.saec.domain.Claimant;

/**
 * Core Services of Sending Address Research File to a third party
 * 
 * T represents either Corporate or Individual Account
 */
public interface AddressResearchOutboundService<T extends AddressResearchOut> {


    /**
     * Create an Address Research Pre Scrub file (prior to any mailings)
     *
     * @param account given Claimant account
     */
    T createPreScrub(Claimant account);

    /**
     * Create Address Research file (following mailings)
     *
     * @param account Claimant account
     */
    T createAddressResearch(Claimant account);
 
}
                                                                                