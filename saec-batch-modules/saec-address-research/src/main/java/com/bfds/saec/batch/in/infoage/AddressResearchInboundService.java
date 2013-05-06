package com.bfds.saec.batch.in.infoage;

import java.util.List;

import com.bfds.saec.batch.file.domain.in.infoage.AddressResearchUpdate;

/**
 * Core Services of Address Research Business Process
 */
public interface AddressResearchInboundService {

    /**
     * Initialize notification
     *
     * @return
     */
    AddressResearchNotification initNotification();

    /**
     * Process Address Research results from vendor. Updates Address Changed, Account history, Mailing history,
     * and Payment history objects
     *
     * @param addressResearchUpdates accounts map if accounts researched. Key is user reference and value is the list of updated research statuses
     *                 (after business rule updates)
     */
    void processResearchedAddresses(List<AddressResearchUpdate> addressResearchUpdates);

    /**
     * Notify overall status of Address Research
     */
    void notifyStatus();
}
