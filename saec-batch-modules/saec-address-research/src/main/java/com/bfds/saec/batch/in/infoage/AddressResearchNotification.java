package com.bfds.saec.batch.in.infoage;

import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * Notifcation Bean to hold Address Research Statistics
 */
@RooJavaBean
public class AddressResearchNotification {

    private int recordCount;
    private int noOfHits;
    private int noOfNoHits;

    @Override
    public String toString() {
        return "[Address Research Summary] - " +
                "Record Count = " + recordCount + "," +
                "No Of Hits = " + noOfHits + "," +
                "No Of NO Hits = " + noOfNoHits + "\n";
    }
}
