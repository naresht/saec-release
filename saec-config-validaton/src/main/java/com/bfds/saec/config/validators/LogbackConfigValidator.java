package com.bfds.saec.config.validators;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.net.SMTPAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.bfds.saec.util.MailSenderImpl;
import com.bfds.validation.Validator;
import com.bfds.validation.message.ValidationMessageContext;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Iterator;
import java.util.List;

/**
 * A validator to ensure that logback configuration is available and set-up correctly.
 */
public class LogbackConfigValidator implements Validator {
    org.slf4j.Logger logger = LoggerFactory.getLogger(LogbackConfigValidator.class);
    private static final String INVALID_MAIL_CONFIG = "Invalid SMTPAppender Config. host:%s, port: %s. Cannot send mail to %s.";
    @Override
    public void validate(final ValidationMessageContext messageContext) {
        LoggerContext  loggerContext =  (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggers = loggerContext.getLoggerList();
        // The logback config xml must have alteast one smtp appender.
        //If there is no smtp appender we assume that there no logback.xml or logback-test.xml on classpath.
        boolean smtpAppenderFound = false;
        for(Logger logger : loggers) {
            for(Iterator<Appender<ILoggingEvent>> itr = logger.iteratorForAppenders(); itr.hasNext(); ) {
                Appender<ILoggingEvent> appender = itr.next();
                if(smtpAppenderFound = (appender instanceof SMTPAppender)) {
                    logger.info("Found SMTPAppender. Validating the appender.");
                    validate((SMTPAppender) appender, messageContext);
                }
            }
        }
        if(!smtpAppenderFound) {
            logger.info("Invalid logback config. Logback config must have at least one smtp appender. Either logback.xml/logback-test.xml are not available on the classpath or are configured without an smtp appender.");
        }
    }

    private void validate(SMTPAppender appender, ValidationMessageContext messageContext) {
        JavaMailSenderImpl mailSenderTarget = new JavaMailSenderImpl();
        mailSenderTarget.setHost(appender.getSMTPHost());
        mailSenderTarget.setPort(appender.getSmtpPort());
        MailSenderImpl mailSender  = new MailSenderImpl(mailSenderTarget);
        String to = getTo(appender.getToAsListOfString());

       if(!mailSender.sendMail(appender.getFrom(), to, appender.getSubject(), "Test mail. Kindly ignore.")) {
           messageContext.error(INVALID_MAIL_CONFIG, appender.getSMTPHost(), appender.getSmtpPort(), to);
       }
    }


    private String getTo(List<String> toAsListOfString) {
        StringBuilder sb = new StringBuilder();
        for(String to : toAsListOfString) {
            sb.append(to).append(",");
        }
        return sb.toString();
    }


}

