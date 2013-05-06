package com.bfds.saec.config.validators;

import com.bfds.validation.message.DefaultValidationMessageContext;
import com.bfds.validation.message.ValidationMessageContext;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class SpringBatchAndIntegrationValidatorTest {

    @Test
    public void test() {

        SpringBatchAndIntegrationValidator validator = new SpringBatchAndIntegrationValidator() {
            @Override
            protected void verifyTableExists(String tableName, ValidationMessageContext messageContext) {
                messageContext.error(tableName);
            }
        };
        ValidationMessageContext messageContext = new DefaultValidationMessageContext();
        validator.validate(messageContext);

        assertThat(messageContext.getAllMessages()).onProperty("text").containsOnly("BATCH_JOB_EXECUTION", "INT_MESSAGE");
    }
}
