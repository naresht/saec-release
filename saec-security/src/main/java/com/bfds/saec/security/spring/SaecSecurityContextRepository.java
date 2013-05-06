package com.bfds.saec.security.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.bfds.saec.domain.User;

/**
 * A {@code HttpSessionSecurityContextRepository}  that attempts creates an {@code Authentication} if the {@code SecurityContext} does not have one. 
 *
 * The {@code Authentication} object can only be created if the request has a user id and a record exits in {@link User} table for that user id. The user id is retrieved from the following in the order op priority
 *  1. {@link HttpServletRequest#getRemoteUser()}
 *  2. {@link HttpServletRequest#getUserPrincipal().getName()}
 *
 *  It is assumed that the presence of a non-null user id implies a successful authentication by an external authentication provider. 
 */
public class SaecSecurityContextRepository extends HttpSessionSecurityContextRepository {

    private UserDetailsService userDetailsService;
    /**
     * Gets the security context for the current request (if available) and returns it if {@link SecurityContext#getAuthentication()} is not null.
     * If {@link SecurityContext#getAuthentication()} is null an attempt is made to construct a valid {@link Authentication} and update the security context 
     * before it is returned. 
     */
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        SecurityContext context = super.loadContext(requestResponseHolder);
        if(context.getAuthentication() == null) {
            context.setAuthentication(newAuthenticationForRemoteUser(requestResponseHolder));
        }

        return context;
    }


    private Authentication newAuthenticationForRemoteUser(HttpRequestResponseHolder requestResponseHolder) {
        Authentication ret = null;
        String userName = getUserName(requestResponseHolder);
        if(userName != null) {
            UserDetails userDetail = userDetailsService.loadUserByUsername(userName);
            if(userDetail != null) {
                ret = new UsernamePasswordAuthenticationToken(userDetail, userDetail.getPassword(), userDetail.getAuthorities());
            }
        }
        return ret;
    }


    private String getUserName(HttpRequestResponseHolder requestResponseHolder) {
        String user = requestResponseHolder.getRequest().getRemoteUser();
        if(user == null && requestResponseHolder.getRequest().getUserPrincipal() != null) {
            user = requestResponseHolder.getRequest().getUserPrincipal().getName();
        }
        if(user == null) {
            //TODO: This is there for testing outside the IAS environment.
            user = requestResponseHolder.getRequest().getHeader("saec-user-id");
        }
        return user;
    }


    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

}