package com.bfds.saec.config.validators;

import static com.bfds.saec.config.validators.ValidationUtil.*;

import com.bfds.scheduling.domain.MailingList;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import com.bfds.scheduling.domain.JobConfig;
import com.bfds.validation.Validator;
import com.bfds.validation.message.ValidationMessageContext;

import java.util.List;
import java.util.Set;

@Component
public class JobConfigValidator implements Validator {

	@Override
	public void validate(ValidationMessageContext messageContext) {
        List<JobConfig> jobConfigList =  JobConfig.findAllJobConfigs();
        verifyExpectedjobConfigList(jobConfigList, messageContext);
        validateFileConfig(jobConfigList, messageContext);
    }


    private void verifyExpectedjobConfigList(List<JobConfig> jobConfigList, ValidationMessageContext messageContext) {
        Set<String> expectedMailingLists = Sets.newHashSet("corporateAddressRecevieJob", "individualAddressReceiveJob", "corporateAddressSendJob",
                "individualAddressSendJob", "corporateAddressSendJob", "individualAddressSendJob", "awdRpoUploadJob", "encorrUploadJob", "bankIssueVoidJob",
                "dbStopPaymentJob", "dstoCheckFileJob", "dstoPrintFileJob", "IfdsCheckStatusJob", "IfdsIssueVoidJob", "ssStopPaymentJob","bottomlineOutJob",
                "ncoaAddressSendJob", "dbCashedCheckJob", "dbStopConfirmationJob", "ssCashedCheckJob", "ncoaInboundAddressResearchJob", "taxDomesticInJob", "staleDateJob",
                "uploadedDocRipperJob", "followupremindersJob", "ssBottomlineInJob","outboundDomesticTaxReccActivityCreateJob");

        Set<String> actualMailingLists = Sets.newHashSet();

        for(JobConfig m : jobConfigList) {
            actualMailingLists.add(m.getJobId());
        }

        Set<String> missingMailingLists = subtract(expectedMailingLists, actualMailingLists);

        if(missingMailingLists.size() >0 ) {
            messageContext.error("Missing jobConfigs - %s", missingMailingLists);
        }

        Set<String> unexpectedMailingLists = subtract(actualMailingLists, expectedMailingLists);

        if(unexpectedMailingLists.size() >0 ) {
            messageContext.warn("Unexpected jobConfigs - %s", unexpectedMailingLists);
        }
    }

    private void validateFileConfig(List<JobConfig> jobConfigList, ValidationMessageContext messageContext) {
        for(JobConfig jobConfig : jobConfigList) {
            if(jobConfig.getFileConfig() != null) {
                if(!Boolean.TRUE.equals(jobConfig.getIncoming())) {
                    verifyCanWrite(jobConfig.getJobId() + "_out.dir", jobConfig.getFileConfig().getLocationResourcePathFolder(), messageContext);
                } else {
                    verifyCanRead(jobConfig.getJobId() + "_out.dir", jobConfig.getFileConfig().getLocationResourcePathFolder(), messageContext);
                }
            }
        }
    }


}
