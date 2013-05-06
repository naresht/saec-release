package com.bfds.saec.batch.out.infoage_corporate;

import javax.inject.Named;

import com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch;
import com.bfds.saec.batch.out.infoage.InfoAgeOutboundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.activity.AddressResearchChangeActivity;

/**
 * Corporate Account Address Research Outbound Process for InfoAge
 */
@Named(value = "corpAddressResearchOutboundService")
public class CorpInfoAgeOutboundServiceImpl extends InfoAgeOutboundService<OutboundCorporateAddressResearch> {

    final Logger log = LoggerFactory.getLogger(CorpInfoAgeOutboundServiceImpl.class);

    @Override
    protected OutboundCorporateAddressResearch newOutputObject() {
        return new OutboundCorporateAddressResearch();
    }

    @Override
    protected void setAdditionalProperties(Claimant account, OutboundCorporateAddressResearch research) {    	
    	
    	research.setFein(account.getTin() == null ? "" : account.getTin());    
    	
        if (StringUtils.hasText(account.getRegistrationLinesAsString(""))) {
            research.setCompanyName(retrieveCompanyName(account));
        }
    }
}