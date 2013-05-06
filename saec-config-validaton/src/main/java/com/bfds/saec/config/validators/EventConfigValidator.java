package com.bfds.saec.config.validators;

import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.EventPaymentConfig;
import com.bfds.scheduling.domain.JobConfig;
import com.bfds.validation.Validator;
import com.bfds.validation.message.ValidationMessageContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Validates Event configurations.
 *
 */
@Component
public class EventConfigValidator implements Validator {

    @Override
    public void validate(ValidationMessageContext messageContext) {
        final Event event = Event.getCurrentEvent();
        if(!StringUtils.hasText(event.getDda())) {
            messageContext.warn("Event does not have a dda.");
        } else {
            validateJobConfigDDAParameter(event.getDda(), messageContext);
        }
        validateEventPaymentConfig(event.getPaymentConfigs(), messageContext);
    }

    private void validateEventPaymentConfig(Set<EventPaymentConfig> paymentConfigs, ValidationMessageContext messageContext) {
        if(paymentConfigs.size() != 6) {
            messageContext.error("EventPaymentConfig has %s records. Expected 6", paymentConfigs.size());
            return;
        }
    }


    private void validateJobConfigDDAParameter(String eventDda, ValidationMessageContext messageContext) {
        final List<JobConfig> jobConfigList = JobConfig.findAllJobConfigs();
        for(JobConfig jobConfig : jobConfigList) {
            Map<String, String> jobParameters = jobConfig.getJobParameters();
            if(jobParameters != null && jobParameters.containsKey("dda")) {
                final String jobDda = jobParameters.get("dda");
                if(!eventDda.equals(jobDda)) {
                    messageContext.warn("DDA: %s of %s job must be same as Event dda:%s.", jobConfig.getJobId(), jobDda, eventDda);
                }
            }
        }
    }


}
