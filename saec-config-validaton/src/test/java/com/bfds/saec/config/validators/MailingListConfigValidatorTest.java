package com.bfds.saec.config.validators;


import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;
import com.bfds.validation.message.DefaultValidationMessageContext;
import com.bfds.validation.message.ValidationMessageContext;
import com.google.common.collect.Lists;

@MockStaticEntityMethods
@RunWith(MockitoJUnitRunner.class)
public class MailingListConfigValidatorTest {

    @Test
    public void test() {
        IMailSender mailSenderMock = mock(IMailSender.class);
        MailingList.findAllMailingLists();
        AnnotationDrivenStaticEntityMockingControl.expectReturn(getTestMailingLists());
        AnnotationDrivenStaticEntityMockingControl.playback();

        MailingListConfigValidator validator = new MailingListConfigValidator();
        validator.setJavaMailSender(mailSenderMock);
        ValidationMessageContext messageContext = new DefaultValidationMessageContext();
        validator.validate(messageContext);

        assertThat(messageContext.getAllMessages()).hasSize(3);

    }


    private List<MailingList> getTestMailingLists() {
        List<MailingList> ret = Lists.newArrayList();
        ret.add(newMailingList("batch.jobs.infoage", "a@a.com"));
        ret.add(newMailingList("batch.jobs.deutschebank", "a@a.com"));

        return ret;
    }

    private MailingList newMailingList(String code, String toMailId) {
        MailingList ret = new MailingList();
        ret.setCode(code);
        ret.setTo(toMailId);
        return ret;
    }


}
