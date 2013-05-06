package com.bfds.saec.batch.out.infoage_corporate;

import javax.inject.Inject;
import javax.inject.Named;

import com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.batch.out.infoage.AddressResearchOutboundService;
import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.google.common.base.Preconditions;

@RooJavaBean
public class CorporateItemProcessor implements
        ItemProcessor<Claimant, OutboundCorporateAddressResearch>, InitializingBean {

    @Inject
    @Named("corpAddressResearchOutboundService")
    private AddressResearchOutboundService<OutboundCorporateAddressResearch> batchService;
    
    @Value("#{jobParameters['researchType']}")
    private String researchType; 

    @Override
    public OutboundCorporateAddressResearch process(Claimant account) throws Exception {
    	OutboundCorporateAddressResearch ret = null;
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

    @Override
    public void afterPropertiesSet() throws Exception {
    	Preconditions.checkState(researchType != null, "research type is null");
    	Preconditions.checkState(ResearchType.PRESCRUB.equals(researchType) || ResearchType.RESEARCH.equals(researchType), "unsupported research type : %s", researchType);
    }

}
