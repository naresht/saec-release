package com.bfds.saec.batch.in.infoage_corporate;


import com.bfds.saec.batch.in.infoage.AddressResearchNotification;
import com.bfds.saec.batch.in.infoage.AddressResearchStatus;
import com.bfds.saec.batch.in.infoage.InfoAgeInboundService;
import com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch;
import com.bfds.saec.domain.Claimant;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import javax.inject.Named;

/**
 * Corporate Account Address Research Receive Process for InfoAge
 */
@Named(value = "corpAddressResearchInboundService")
public class CorpInfoAgeInboundServiceImpl extends InfoAgeInboundService {

    final Logger log = LoggerFactory.getLogger(CorpInfoAgeInboundServiceImpl.class);

    @Override
    public AddressResearchNotification initNotification() {
        notification = new AddressResearchNotification();
        return notification;
    }

    @Override
    public void updateAddressChange(final Claimant claimant, final AddressResearchStatus researchStatus) {
    	if (log.isInfoEnabled())
    		log.info("Updating Address change for claimant : %s ",claimant.getReferenceNo());
        Preconditions.checkNotNull(researchStatus, "researchStatus is null");
        Preconditions.checkNotNull(researchStatus.getAddressResearchUpdate(), "AddressResearchUpdate is null");
        Preconditions.checkArgument(researchStatus.getAddressResearchUpdate() instanceof CorporateAddressResearch, "Expecting %s. Found %s", CorporateAddressResearch.class, researchStatus.getAddressResearchUpdate().getClass());

        final CorporateAddressResearch research = (CorporateAddressResearch)researchStatus.getAddressResearchUpdate();
        updateAddress(claimant, researchStatus, research, createCorporateAddressResearch(research).toText());
    }

    private com.bfds.saec.batch.in.infoage_corporate.CorporateAddressResearch createCorporateAddressResearch(
    		CorporateAddressResearch research) {
    	com.bfds.saec.batch.in.infoage_corporate.CorporateAddressResearch ret = new com.bfds.saec.batch.in.infoage_corporate.CorporateAddressResearch();
    	
    	BeanUtils.copyProperties(research, ret);
		return ret;
	}
}
