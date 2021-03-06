/*
 * (c) Copyright 2005-2011 JAXIO - Generated by Celerio, a Jaxio tool. http://www.jaxio.com
 */
package com.bfds.saec.service;

import java.util.*;

import com.bfds.saec.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantRegistration;

/**
 * Helper class to create transient entities instance for testing purposes.
 * Simple properties are pre-filled with random values.
 */
@Service
public class ClaimantGenerator {

    /**
     * Returns a new Claimant instance filled with random values.
     */
    public Claimant getClaimant() {
        Claimant claimant = new Claimant();

        // simple attributes follows
        //claimant.setDtype("d");
        claimant.setAccountStatus("d");
        claimant.setAccountType("d");
        claimant.setBin("d");
        claimant.setBrokerId("d");
        claimant.setCertificationDate(new Date());
        claimant.setCertificationStatus("d");
        claimant.setDoAudit(true);
        claimant.setFundAccountNo("d");
        claimant.setMarketTimer(true);
        //claimant.setReferenceNo("d");
        claimant.setSpecialPull("d");
        claimant.setSsn("d");
        claimant.setTaxCountryCode("d");
        claimant.setTin("d");
        claimant.setUsCitizen(true);
        
        claimant.setClaimantRegistration(new ClaimantRegistration());
        claimant.getClaimantRegistration().setRegistration1("1r1r1r1r1r1");
        claimant.getClaimantRegistration().setRegistration3("r3r3r3r3r3");
        claimant.getClaimantRegistration().setRegistration5("5r5r5r5r5r");        
        return claimant;
    }

}