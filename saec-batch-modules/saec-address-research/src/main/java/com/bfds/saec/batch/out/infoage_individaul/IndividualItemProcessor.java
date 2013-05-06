package com.bfds.saec.batch.out.infoage_individaul;


import com.bfds.saec.batch.out.infoage.AddressResearchOutboundService;
import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.batch.file.domain.out.infoage_individual.OutboundIndividualAddressResearch;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;

import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;

public class IndividualItemProcessor implements
        ItemProcessor<Claimant, OutboundIndividualAddressResearch> {
	
    @Value("#{jobParameters['researchType']}")
    private String researchType;

    @Inject
    @Named("individualAddressResearchOutboundService")
    private AddressResearchOutboundService<OutboundIndividualAddressResearch> batchService;

    @Override
    public OutboundIndividualAddressResearch process(Claimant account) throws Exception {
    	OutboundIndividualAddressResearch ret = null;      
        if(ResearchType.PRESCRUB.equals(researchType)) {
        	 ret =  batchService.createPreScrub(account);
    	} else {
        	ret = batchService.createAddressResearch(account);
    	}
        if(ret != null) {
        	ret.setDda(Event.getCurrentEventDda());
        }
        return ret;
    }


}
