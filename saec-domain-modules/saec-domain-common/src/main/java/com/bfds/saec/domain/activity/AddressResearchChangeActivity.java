package com.bfds.saec.domain.activity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import org.springframework.roo.addon.javabean.RooJavaBean;


/**
 * Holds results of Address Research Return. Can be embedded in Address and AddressChange
 */
@Embeddable
@RooJavaBean
public class AddressResearchChangeActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ACTIVITY_TYPE_ADDRESS_RESEARCH_PRESCRUB_SENT = "Address Pre-Scrub";
    public static final String ACTIVITY_TYPE_ADDRESS_RESEARCH_SENT = "Address Research Requested";

    public static final String ACTIVITY_TYPE_ADDRESS_RESEARCH_RETURN = "Address Research Return";
    public static final String ADDRESS_RESEARCH_RETURN_TYPE_HIT = "HIT";
    public static final String ADDRESS_RESEARCH_RETURN_TYPE_NO_HIT = "NO HIT";

    public static final String ADDRESS_RESEARCH_SENT_TYPE_CHECK = "Check";
    public static final String ADDRESS_RESEARCH_SENT_TYPE_LETTER = "Letter";
    public static final String ADDRESS_RESEARCH_SENT_TYPE_LETTER_AND_CHECK = "Letter and Check";

    // indicates if address has changed from research results; is set to TRUE upon this class creation
    @Column(nullable=true) // This is required. Else, a not-null column is being created.
    private boolean researchReturned;

    // indicates if account has been sent for address research
    @Column(nullable=true) // This is required. Else, a not-null column is being created.
    private boolean researchSent;

    @Column(nullable=true) // This is required. Else, a not-null column is being created.
    private boolean hit;

    @Lob
    // @Column(columnDefinition="TEXT", length = 2000)
    private String addressResearchDescription;

    private String status;

    private Date addressResearchDate;
    
 // Fluent method for HIT
    public AddressResearchChangeActivity markReturnedAsHit(final String researchReturnText) {
        this.researchReturned = true;
        this.hit = true;
        this.addressResearchDescription = ACTIVITY_TYPE_ADDRESS_RESEARCH_RETURN + "... \n" + researchReturnText;
        this.status = ADDRESS_RESEARCH_RETURN_TYPE_HIT;

        return this;
    }

    // Fluent method for NO HIT
    public AddressResearchChangeActivity markReturnedAsNoHit(final String researchReturnText) {
        this.researchReturned = true;
        this.hit = false;
        this.addressResearchDescription = ACTIVITY_TYPE_ADDRESS_RESEARCH_RETURN + "... \n" + researchReturnText;
        this.status = ADDRESS_RESEARCH_RETURN_TYPE_NO_HIT;

        return this;
    }

    // Fluent method to indicate account has been sent for research   
    public AddressResearchChangeActivity markSentAsPrescrubResearch() {
        this.researchSent = true;
        this.hit = false;
        this.addressResearchDescription = ACTIVITY_TYPE_ADDRESS_RESEARCH_PRESCRUB_SENT;

        return this;
    }

    /**
     * Fluent method to indicate account has been sent for research
     *
     * @param type ADDRESS_RESEARCH_SENT_TYPE_CHECK or ADDRESS_RESEARCH_SENT_TYPE_LETTER
     * @return
     */
    public AddressResearchChangeActivity markSentAsAddressResearch(String type) {
        this.researchSent = true;
        this.hit = false;
        this.addressResearchDescription = ACTIVITY_TYPE_ADDRESS_RESEARCH_SENT;
        this.status = type;

        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((addressResearchDate == null) ? 0 : addressResearchDate
                .hashCode());
        result = prime
                * result
                + ((addressResearchDescription == null) ? 0
                : addressResearchDescription.hashCode());
        result = prime * result + (hit ? 1231 : 1237);
        result = prime * result + (researchReturned ? 1231 : 1237);
        result = prime * result + (researchSent ? 1231 : 1237);
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AddressResearchChangeActivity other = (AddressResearchChangeActivity) obj;
        if (addressResearchDate == null) {
            if (other.addressResearchDate != null)
                return false;
        } else if (!addressResearchDate.equals(other.addressResearchDate))
            return false;
        if (addressResearchDescription == null) {
            if (other.addressResearchDescription != null)
                return false;
        } else if (!addressResearchDescription
                .equals(other.addressResearchDescription))
            return false;
        if (hit != other.hit)
            return false;
        if (researchReturned != other.researchReturned)
            return false;
        if (researchSent != other.researchSent)
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        return true;
    }
}
