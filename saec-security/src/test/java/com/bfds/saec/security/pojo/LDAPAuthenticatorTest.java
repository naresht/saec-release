package com.bfds.saec.security.pojo;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NOTE!!! The Real test will only work in BFDS Intranet with Windows NTLM Authentication!
 */
public class LDAPAuthenticatorTest {

    static final private Logger logger = LoggerFactory.getLogger(LDAPAuthenticatorImpl.class);

    @Test
    @Ignore // uncomment to test in BFDS env
    public void testAuthentication() throws Exception {
        String userIds[] = {
                "bostonfinancial\\pmudivarti"
        };
        String testGroup = "Gtd DEVELOPERS";

        for (int i = 0; i < userIds.length; i++) {
            LDAPAuthenticatorImpl authenticator = new LDAPAuthenticatorImpl(userIds[i]);
            authenticator.authenticate();
            logger.debug(authenticator.toString());
            if (authenticator.getGroups() == null) {
                throw new Exception(authenticator +
                        "User is not authorized!!!!!!");
            }

            String usersGroups[] = authenticator.getGroups();

            logger.debug(String.format("User [%s] is a member of [%s] ... [%s]" + authenticator.getUserId(), testGroup, authenticator.
                    IsMemberOf(testGroup)));
            logger.debug("Group as List: " + authenticator.getGroupsInList());
            logger.debug("Group as array:");
            for (int x = 0; x < usersGroups.length; x++) {
                logger.debug(x + ") " + usersGroups[x]);
            }
        }
    }
}