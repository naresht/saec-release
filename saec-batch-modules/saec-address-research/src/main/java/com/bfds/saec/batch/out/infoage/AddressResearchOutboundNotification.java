package com.bfds.saec.batch.out.infoage;

import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * Notifcation Bean to hold Address Research Outbounf file statistics
 */
@RooJavaBean
public class AddressResearchOutboundNotification {

    private boolean success;
    private int recordCount;

    @Override
    public String toString() {
        return "[Address Research Outbound: Summary]\n" +
                "success= " + success + "\n" +
                "Record Count = " + recordCount + "\n";
    }
}
