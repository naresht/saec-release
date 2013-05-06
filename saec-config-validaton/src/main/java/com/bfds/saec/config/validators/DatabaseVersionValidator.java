package com.bfds.saec.config.validators;

import com.bfds.validation.Validator;
import com.bfds.validation.message.ValidationMessageContext;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * A validator to ensure that database version is within the rage of the application's supported database versions.
 *
 */
@Component
public class DatabaseVersionValidator extends NamedParameterJdbcDaoSupport implements Validator {
    private static final String INVALID_MAIL_CONFIG = "Invalid Mail Config. host:%s, port: %s. Cannot send mail to %s.";


    @Value("${saec.db.version.min}")
    private int minVersion;

    @Value("${saec.db.version.max}")
    private int maxVersion;

    @Autowired
    @Qualifier("dataSource")
    public void setDs(DataSource dataSource){
        super.setDataSource(dataSource);
    }

    @Override
    public void validate(final ValidationMessageContext messageContext) {
        int dbVersion = -1;
        try {
            dbVersion = Integer.parseInt(getJdbcTemplate().queryForObject("select max(convert(INT, id)) from DATABASECHANGELOG", String.class));
        }catch(DataAccessException e) {
            messageContext.error("Error fetching DB version." + e.getMessage());
        }

        if(dbVersion < minVersion || dbVersion > maxVersion) {
            messageContext.error("Unsupported DB version "+dbVersion+". Supported version range is "+minVersion+" to "+maxVersion);
        }
    }
}

