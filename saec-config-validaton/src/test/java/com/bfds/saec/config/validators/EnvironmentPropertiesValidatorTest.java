package com.bfds.saec.config.validators;


import com.bfds.validation.message.DefaultValidationMessageContext;
import com.bfds.validation.message.ValidationMessageContext;
import org.junit.Test;
import org.springframework.binding.message.Severity;

import static org.fest.assertions.Assertions.assertThat;
public class EnvironmentPropertiesValidatorTest {

    @Test
    public void unsupportedEnvironmentTypeValidation() {
        EnvironmentPropertiesValidator v = new EnvironmentPropertiesValidator() {
            @Override
            protected String getEnvironmentType() {
                return "xyz";
            }
        };

        final ValidationMessageContext mc = new DefaultValidationMessageContext();
        v.validate(mc);
        assertThat(mc.getAllMessages()).hasSize(1);
        assertThat(mc.getMessagesBySeverity(Severity.ERROR)).hasSize(1);
    }

    @Test
    public void nullEnvironmentTypeMustBeAllowed() {
        EnvironmentPropertiesValidator v = new EnvironmentPropertiesValidator() {
            @Override
            protected String getEnvironmentType() {
                return null;
            }
        };

        final ValidationMessageContext mc = new DefaultValidationMessageContext();
        v.validate(mc);
        assertThat(mc.getAllMessages()).hasSize(1);
        assertThat(mc.getMessagesBySeverity(Severity.FATAL)).hasSize(1);
    }

    @Test
    public void saecEnvironmentTypeMustBeAllowed() {
        EnvironmentPropertiesValidator v = new EnvironmentPropertiesValidator() {
            @Override
            protected String getEnvironmentType() {
                return "saec";
            }
        };

        final ValidationMessageContext mc = new DefaultValidationMessageContext();
        v.validate(mc);
        assertThat(mc.getAllMessages()).hasSize(1);
        assertThat(mc.getMessagesBySeverity(Severity.FATAL)).hasSize(1);
    }
}
