package com.bfds.saec.batch.out.infoage_individaul;

import javax.inject.Named;

import com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch;
import com.bfds.saec.batch.file.domain.out.infoage_individual.OutboundIndividualAddressResearch;
import com.bfds.saec.batch.out.infoage.InfoAgeOutboundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.activity.AddressResearchChangeActivity;
import com.bfds.saec.util.ConverterUtils;

/**
 * Individual Account Address Research Outbound Process for InfoAge
 */
@Named(value = "individualAddressResearchOutboundService")
public class IndividualInfoAgeOutboundServiceImpl extends InfoAgeOutboundService<OutboundIndividualAddressResearch> {

    final Logger log = LoggerFactory.getLogger(IndividualInfoAgeOutboundServiceImpl.class);

    @Override
    protected OutboundIndividualAddressResearch newOutputObject() {
        return new OutboundIndividualAddressResearch();
    }

    @Override
    protected void setAdditionalProperties(Claimant account, OutboundIndividualAddressResearch research) {
    	
    	research.setSsn(account.getSsn() == null ? "" : account.getSsn());            
    	
        if (account.getName() != null) {
            research.setFirstName(account.getName().getFirstName());
            research.setMiddleName(account.getName().getMiddleName());
            research.setLastName(account.getName().getLastName());
        }
    }
}
