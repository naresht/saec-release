package com.bfds.saec.security.waffle;

import com.bfds.saec.domain.User;
import com.bfds.saec.security.spring.SpringSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import waffle.servlet.WindowsPrincipal;

import java.util.Collection;
import java.util.List;

/**
 * Custom Authentication Token to comply with custom Waffle Authentication mechanism.
 * Variation of Waffle's WindowsAuthenticationToken
 */
public class SAECAuthenticationToken implements Authentication {

    Logger logger = LoggerFactory.getLogger(SAECAuthenticationToken.class);

    private static final long serialVersionUID = 1L;
    private WindowsPrincipal windowsPrincipal = null;
    private org.springframework.security.core.userdetails.User principal = null;
    private Collection<GrantedAuthority> authorities = null;

    // TODO: Move this to a config
    public static final String SAEC_DOMAIN = "BOSTONFINANCIAL";

    /**
     * Constructor that fully initializes the principal
     *
     * @param windowsPrincipal windows principal
     */
    public SAECAuthenticationToken(WindowsPrincipal windowsPrincipal) {
        this.windowsPrincipal = windowsPrincipal;
        // Strip <domainName>\\ to get mapped username in SAEC
        String username = windowsPrincipal.getName().substring(SAEC_DOMAIN.length()+1);
        logger.debug("SAEC Login Info derived from ActiveDirectory: " + username);

        // Load roles
        Collection<GrantedAuthority> authorities = loadAuthorities(username);

        // Create UserDetails object
        this.principal = new org.springframework.security.core.userdetails.User(username, "", true, true,
                true, true, authorities);
    }

    /**
     * Loads granted authorities by looking up User=>Roles.
     * Throws UsernameNotFoundException if principal isn't mapped mapped to Db
     * @return
     */
    private Collection<GrantedAuthority> loadAuthorities(String username) {

        // Match User Account from Db
        User account = null;
        try {
            account = User.findUserByName(username);
        }
        catch (Exception e) {
            logger.info("A unique account " + username + " could not be found: " + e.getMessage());
            throw new UsernameNotFoundException("A unique account " + username + " could not be found");
        }

        // Load User Roles
        List<String> roles = account.getRoleNames();
        logger.debug("Loaded Roles from Database: " + roles.toString());
        Collection<GrantedAuthority> grantedAuthorities =
                SpringSecurityContext.toGrantedAuthorities(roles);
        this.authorities = grantedAuthorities;
        return authorities;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Object getCredentials() {
        return null;
    }

    public Object getDetails() {
        return windowsPrincipal;
    }

    public Object getPrincipal() {
        return principal;
    }

    public boolean isAuthenticated() {
        return (principal != null);
    }

    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }

    public String getName() {
        return principal.getUsername();
    }
}