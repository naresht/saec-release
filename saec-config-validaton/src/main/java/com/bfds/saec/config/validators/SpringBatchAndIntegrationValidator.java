package com.bfds.saec.config.validators;

import com.bfds.validation.Validator;
import com.bfds.validation.message.ValidationMessageContext;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * A validator to ensure that all MailingLists are are valid.
 */
@Component
public class SpringBatchAndIntegrationValidator extends NamedParameterJdbcDaoSupport implements Validator {
    private static final String INVALID_MAIL_CONFIG = "Invalid Mail Config. host:%s, port: %s. Cannot send mail to %s.";

    @Autowired
    @Qualifier("dataSource")
    public void setDs(DataSource dataSource){
        super.setDataSource(dataSource);
    }

    @Override
    public void validate(final ValidationMessageContext messageContext) {
       verifyTableExists("BATCH_JOB_EXECUTION", messageContext);
       verifyTableExists("INT_MESSAGE", messageContext);
    }

    @VisibleForTesting
    protected void verifyTableExists(String tableName, ValidationMessageContext messageContext) {
        try {
            this.getJdbcTemplate().execute("select count(*) from " + tableName);
        }catch(DataAccessException e) {
            messageContext.error("%s table does not exist.", tableName);
        }
    }


}

