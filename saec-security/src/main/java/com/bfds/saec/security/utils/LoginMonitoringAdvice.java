package com.bfds.saec.security.utils;

import com.bfds.saec.domain.User;
import com.bfds.saec.domain.UserLoginAudit;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.Date;

/**
 * Aspect that monitors each login and persists to db for auditing/security
 */
@Aspect
@Named
public class LoginMonitoringAdvice {

    static final private Logger logger = LoggerFactory.getLogger(LoginMonitoringAdvice.class);

    @Pointcut("execution(* com.bfds.saec.security.spring.AccountDetailsServiceImpl.loadUserByUsername(..))")
    public void process() {

    }

    /*@Pointcut("execution(* org.springframework.security.ldap.authentication.LdapAuthenticationProvider.authenticate(..))")
    public void process() {

    }*/


    @AfterReturning(pointcut = "process()", returning = "retVal")
    public void processReturningValue(
            final Object retVal) {

        if (retVal != null && retVal instanceof User) {
            User user = (User) retVal;
            logger.debug("processing login user to database.");
            UserLoginAudit userLogin = new UserLoginAudit(user, new Date(System.currentTimeMillis()));
            userLogin.persist();
        }
    }

    /*@AfterReturning(pointcut = "process()", returning = "retVal")
   public void processReturningValue(
             final Object retVal) {
  
         if (retVal != null && retVal instanceof Authentication) {
             // User user = (User) retVal;
             Authentication user = (Authentication) retVal;
             logger.debug("processing login user to database.");
             UserLoginAudit userLogin = new UserLoginAudit(new User((String)user.getCredentials(), (String)user.getPrincipal()), new Date(System.currentTimeMillis()));
             userLogin.persist();
         }
   } */

}
