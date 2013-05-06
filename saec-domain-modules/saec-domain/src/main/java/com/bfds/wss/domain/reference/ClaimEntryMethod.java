package com.bfds.wss.domain.reference;

/**
 *  The method by which a {@link com.bfds.wss.domain.ClaimantClaim} has been submitted.
 */
public enum ClaimEntryMethod {

    /**
     * The claim has been created in WSS.
     */
    WEB,
    /**
     * The claim has been created in SASEC.
     */
    SASEC,
    /**
     * The claim has been created by the data intake process..
     */
    DATA_INTAKE;
}
