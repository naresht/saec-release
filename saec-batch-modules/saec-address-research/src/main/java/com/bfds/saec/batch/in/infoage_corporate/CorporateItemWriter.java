package com.bfds.saec.batch.in.infoage_corporate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.bfds.saec.batch.in.infoage.rule.RulesManager;
import com.bfds.saec.batch.file.domain.in.infoage.AddressResearchUpdate;
import com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;

import com.bfds.saec.batch.in.infoage.AddressResearchInboundService;

/**
 * ItemWriter to collect PreScrub results and notify
 */
public class CorporateItemWriter implements ItemWriter<CorporateAddressResearch> {

    final Logger log = LoggerFactory.getLogger(CorporateItemWriter.class);

    // Holds the notification object in between chunks in the same step
    final static String NOTIFICATION_KEY = "corp_notification";

    @Inject
    @Named("corpAddressResearchInboundService")
    private AddressResearchInboundService service;

    private StepExecution stepExecution;
    
    @Value("#{jobParameters['researchType']}")
    private String researchType;

    @Inject
    @Named("simpleRulesManager")
    private RulesManager rulesManager;
    

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(List<? extends CorporateAddressResearch> corporateAddressResearches)
            throws Exception {

        log.debug("Executing Corporate Account Writer ... ");
		service.processResearchedAddresses((List<AddressResearchUpdate>)corporateAddressResearches);
    }


}