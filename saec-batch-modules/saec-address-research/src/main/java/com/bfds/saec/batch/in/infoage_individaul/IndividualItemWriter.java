package com.bfds.saec.batch.in.infoage_individaul;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.bfds.saec.batch.file.domain.in.infoage.AddressResearchUpdate;
import com.bfds.saec.batch.file.domain.in.infoage_individual.IndividualAddressResearch;
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
public class IndividualItemWriter implements ItemWriter<IndividualAddressResearch> {

    final Logger log = LoggerFactory.getLogger(IndividualItemWriter.class);

    // Holds the notification object in between chunks in the same step
    final static String NOTIFICATION_KEY = "ind_notification";

    @Inject
    @Named("individualAddressResearchInboundService")
    private AddressResearchInboundService service;

    private StepExecution stepExecution;
    
    @Value("#{jobParameters['researchType']}")
    private String researchType;



    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(List<? extends IndividualAddressResearch> individualAddressResearches)
            throws Exception {

        log.debug("Executing Individual Account PreScrub Writer ... ");


    	 service.processResearchedAddresses((List<AddressResearchUpdate>)individualAddressResearches);
    }

}
