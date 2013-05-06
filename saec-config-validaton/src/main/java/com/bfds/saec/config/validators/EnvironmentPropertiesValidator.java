package com.bfds.saec.config.validators;

import static com.bfds.saec.config.validators.ValidationUtil.verifyCanWrite;
import static com.bfds.saec.config.validators.ValidationUtil.verifyPropertyEquals;
import static com.bfds.saec.config.validators.ValidationUtil.verifyPropertyExists;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.bfds.validation.Validator;
import com.bfds.validation.message.ValidationMessageContext;
import com.google.common.annotations.VisibleForTesting;

/**
 * A validator to ensure that the environment.type is not set.
 * "saec" is the only environment type currently supported outside of dev.
 * And it is the default.
 *
 * This class has no spring dependencies and can be run outside the spring container.
 */
@Component
public class EnvironmentPropertiesValidator implements Validator {
    private static final String INVALID_ENVIRONMNET_TYPE = "Invalid environment type : %s";
    public static final String SAEC_ENVIRONMENT_PROPERTIES = "META-INF/spring/saec-environment.properties";

    @Override
    public void validate(final ValidationMessageContext messageContext) {
        final String envType = getEnvironmentType();
        if(envType != null && !"saec".equals(envType)) {
            messageContext.error("Invalid environment type : %s", envType);
            return;
        }
        try {
            Properties saecProperties = loadSaecProperties();
            validateSaecProperties(saecProperties, messageContext);
        } catch (IOException e) {
            messageContext.fatal("Error reading %s. %s", SAEC_ENVIRONMENT_PROPERTIES, e.getMessage());
        }

    }

    private void validateSaecProperties(Properties saecProperties, ValidationMessageContext messageContext) {
        verifyCanWrite("awd.out.dir", saecProperties, messageContext);
        verifyPropertyEquals("database.driverClassName", saecProperties, "net.sourceforge.jtds.jdbc.Driver", messageContext);
        verifyPropertyEquals("event.admin.database.driverClassName", saecProperties, "net.sourceforge.jtds.jdbc.Driver", messageContext);

        verifyPropertyEquals("hibernate.hbm2ddl.auto", saecProperties, "validate", messageContext);
        verifyPropertyEquals("hibernate.dialect", saecProperties, "org.hibernate.dialect.SQLServer2008Dialect", messageContext);

        verifyPropertyEquals("event.admin.hibernate.hbm2ddl.auto", saecProperties, "validate", messageContext);
        verifyPropertyEquals("event.admin.hibernate.dialect", saecProperties, "org.hibernate.dialect.SQLServer2008Dialect", messageContext);

        verifyPropertyEquals("event_admin_enableRepositoryInitializer", saecProperties, "false", messageContext);
        verifyPropertyEquals("database.schema.suffix", saecProperties, "sqlserver", messageContext);

        verifyPropertyEquals("infrastructure.schema.create", saecProperties, "false", messageContext);
        verifyPropertyEquals("event.admin.database.schema.suffix", saecProperties, "sqlserver", messageContext);

        verifyPropertyEquals("event.admin.infrastructure.schema.create", saecProperties, "false", messageContext);

        verifyPropertyExists("awdRipHostName", saecProperties, messageContext);
        verifyPropertyExists("awdRipBusinessUnit", saecProperties, messageContext);

    }

    private Properties loadSaecProperties() throws IOException {
        Properties ret = new Properties();
        ret.load((new ClassPathResource(SAEC_ENVIRONMENT_PROPERTIES)).getInputStream());
        return ret;
    }

    @VisibleForTesting
    protected String getEnvironmentType() {
        return System.getProperty("environment.type");
    }
}
