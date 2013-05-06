package com.bfds.saec.config.validators;

import com.bfds.saec.util.IMailSender;
import com.bfds.saec.util.MailSenderImpl;
import com.bfds.scheduling.domain.MailingList;
import com.bfds.validation.Validator;
import com.bfds.validation.message.ValidationMessageContext;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import static com.bfds.saec.config.validators.ValidationUtil.*;

import java.util.List;
import java.util.Set;

/**
 * A validator to ensure that all MailingLists are are valid.
 */
@Component
public class MailingListConfigValidator implements Validator {
    private static final String INVALID_MAIL_CONFIG = "Error in sending test mail to %s.";

    @Autowired
    @Qualifier("mailSender")
    IMailSender mailSender;

    @Override
    public void validate(final ValidationMessageContext messageContext) {
        List<MailingList> mailingLists = MailingList.findAllMailingLists();
        verifyExpectedMailingLists(mailingLists, messageContext);
        for(MailingList mailingList : mailingLists) {
            validate(mailingList, messageContext);
        }
    }

    private void verifyExpectedMailingLists(List<MailingList> mailingLists, ValidationMessageContext messageContext) {
        Set<String> expectedMailingLists = Sets.newHashSet("batch.jobs.infoage", "batch.jobs.deutschebank", "batch.jobs.statestreetbank",
                "batch.jobs.dsto", "batch.jobs.ifds", "batch.jobs.checkstaledate", "batch.jobs.encore", "batch.jobs.rpo");

        Set<String> actualMailingLists = Sets.newHashSet();

        for(MailingList m : mailingLists) {
            actualMailingLists.add(m.getCode());
        }

        Set<String> missingMailingLists = subtract(expectedMailingLists, actualMailingLists);

        if(missingMailingLists.size() >0 ) {
            messageContext.warn("Missing mailingLists - %s", missingMailingLists);
        }

        Set<String> unexpectedMailingLists = subtract(actualMailingLists, expectedMailingLists);

        if(unexpectedMailingLists.size() >0 ) {
            messageContext.warn("Unexpected mailingLists - %s", unexpectedMailingLists);
        }
    }



    private void validate(MailingList mailingList, ValidationMessageContext messageContext) {
        if(!StringUtils.hasText(mailingList.getTo())) {
            messageContext.warn("To address is empty for mailingList %s", mailingList.getCode());
        }
        if(!mailSender.send(mailingList, "Test mail", "Test mail. Kindly ignore.")) {
            messageContext.warn(INVALID_MAIL_CONFIG,  mailingList.getTo());
        }
    }

    public void setJavaMailSender(IMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }
}

