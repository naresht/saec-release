package com.bfds.saec.config.validators;

import com.bfds.validation.message.DefaultValidationMessageContext;
import com.bfds.validation.message.ValidationMessageContext;
import org.junit.Test;
import org.springframework.binding.message.Severity;

import static org.fest.assertions.Assertions.assertThat;
public class LogbackConfigValidatorTest {

    @Test
    public void testMailAppender() {
        LogbackConfigValidator v = new LogbackConfigValidator();
        final ValidationMessageContext mc = new DefaultValidationMessageContext();
        v.validate(mc);
        assertThat(mc.getMessagesBySeverity(Severity.ERROR)).hasSize(1);
    }
}
