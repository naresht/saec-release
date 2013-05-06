package com.bfds.saec.batch.in.infoage_individaul;


import com.bfds.saec.batch.in.infoage.AddressResearchNotification;
import com.bfds.saec.batch.in.infoage.AddressResearchStatus;
import com.bfds.saec.batch.in.infoage.InfoAgeInboundService;
import com.bfds.saec.batch.in.infoage.AddressResearchUtils;
import com.bfds.saec.batch.file.domain.in.infoage_individual.IndividualAddressResearch;
import com.bfds.saec.domain.AddressResearchReturn;
import com.bfds.saec.domain.Claimant;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

/**
 * Individual Account Address Research Receive Process for InfoAge
 */
@Named(value = "individualAddressResearchInboundService")
public class IndividualInfoAgeInboundServiceImpl extends InfoAgeInboundService {

    final Logger log = LoggerFactory.getLogger(IndividualInfoAgeInboundServiceImpl.class);
    
    @Override
    public AddressResearchNotification initNotification() {
        notification = new AddressResearchNotification();
        return new AddressResearchNotification();
    }

    @Override
    public void updateAddressChange(final Claimant claimant, final AddressResearchStatus researchStatus) {
        Preconditions.checkNotNull(researchStatus, "researchStatus is null");
        Preconditions.checkNotNull(researchStatus.getAddressResearchUpdate(), "AddressResearchUpdate is null");
        Preconditions.checkArgument(researchStatus.getAddressResearchUpdate() instanceof IndividualAddressResearch, "Expecting %s. Found %s", IndividualAddressResearch.class, researchStatus.getAddressResearchUpdate().getClass());

        log.info(String.format("Updating Address Change for Claimaint[%s] with a Research [%s] ...", claimant.getReferenceNo(), researchStatus));

        IndividualAddressResearch research = (IndividualAddressResearch)researchStatus.getAddressResearchUpdate();

        // persist address research return
        final AddressResearchReturn researchReturn = createResearchReturn(research);
        researchReturn.setClaimant(claimant);
        researchReturn.persist();
        updateAddress(claimant, researchStatus, research, researchReturn.toString());


    }


    private AddressResearchReturn createResearchReturn(final IndividualAddressResearch research) {
        AddressResearchReturn researchReturn = new AddressResearchReturn();

        if(research.getMatchIndicator() != null) {
        	researchReturn.setMatchTag(research.getMatchIndicator().name());
        }
        researchReturn.setAccountHolder(research.getPrefix() + " " + research.getFirstName() + " " + research.getMiddleName() + " " + research.getLastName() + " " + research.getSuffix());
        researchReturn.setMaidenName(research.getMaternalName());
        researchReturn.setAddress(research.getAddress());
        researchReturn.setAddressDateReported(AddressResearchUtils.convertToDate(research.getAddressDateReported()));
        researchReturn.setSsn(research.getSsn());
        if(research.getDateOfBirth() != null) {
            researchReturn.setDob(research.getDateOfBirth().getTime());
        }
        researchReturn.setPhone(research.getPhone());
        researchReturn.setInvalidOrDeceasedSsnTag(research.getInvalidOrDeceasedSSNTag());
        researchReturn.setMatchAnalysisTag(research.getMatchAnalysisTag());
        researchReturn.setNameChangeTag(research.getNameChangeTag());
        researchReturn.setSsnMatchTag(research.getSsnMatchTag());
        researchReturn.setOfacIndicator(research.getOfacIndicator());
        log.info(String.format("Created Research Return for Individual Address Research..."));
        return researchReturn;
    }

}
