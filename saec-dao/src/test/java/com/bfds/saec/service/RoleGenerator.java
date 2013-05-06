/*
 * (c) Copyright 2005-2011 JAXIO - Generated by Celerio, a Jaxio tool. http://www.jaxio.com
 */
package com.bfds.saec.service;

import java.util.*;

import com.bfds.saec.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfds.saec.domain.Role;

/**
 * Helper class to create transient entities instance for testing purposes.
 * Simple properties are pre-filled with random values.
 */
@Service
public class RoleGenerator {

    /**
     * Returns a new Role instance filled with random values.
     */
    public Role getRole() {
        Role role = new Role();

        // simple attributes follows
        role.setRoleName("d");
        return role;
    }

}