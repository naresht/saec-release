package com.bfds.saec.security.pojo;

/**
 * Simple non-Spring standalone LDAP Authenticator 
 * 
 */
public interface LDAPAuthenticator {
    
    void authenticate();
    
    String[] getGroups();
    
}
