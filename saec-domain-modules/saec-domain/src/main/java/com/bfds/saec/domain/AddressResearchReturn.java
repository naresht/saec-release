package com.bfds.saec.domain;

import java.util.Date;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.util.SaecDateUtils;

/**
 * Holds the crucial address research return data 
 */
@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = { "findAddressResearchReturnsByClaimant"})
public class AddressResearchReturn implements java.io.Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String matchTag;
    private String accountHolder;
    private String maidenName;
    private String address;
    private Date addressDateReported;
    private String ssn;
    private Date dob;
    private String phone;
    private String ssnMatchTag;
    private String invalidOrDeceasedSsnTag;
    private String nameChangeTag;
    private String matchAnalysisTag;
    private String ofacIndicator;
    
    @ManyToOne
    @JoinColumn
    private Claimant claimant;    
    
    public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("Match: ").append(getMatchTag()).append("\n");
       sb.append("Account Holder: ").append(getAccountHolder()).append("\n");
       sb.append("Maiden Name: ").append(getMaidenName()).append("\n");
       sb.append("Address: ").append(getAddress()).append("\n");
        if(getAddressDateReported() != null) {
            sb.append("Address Date Reported: ").append(SaecDateUtils.getDateFormatted(getAddressDateReported())).append("\n");
        }else {
            sb.append("Address Date Reported: NA").append("\n");
        }
       sb.append("Ssn: ").append(getSsn()).append("\n");

        if(getDob() != null) {
            sb.append("Date of Birth: ").append(SaecDateUtils.getDateFormatted(getDob())).append("\n");
        }else {
            sb.append("Date of Birth:NA ");
        }
       sb.append("Phone: ").append(getPhone()).append("\n");
       sb.append("Invalid Or Deceased Ssn Tag: ").append(getInvalidOrDeceasedSsnTag()).append("\n");
       sb.append("Match Analysis Tag: ").append(getMatchAnalysisTag()).append("\n");
       sb.append("Name Change Tag: ").append(getNameChangeTag()).append("\n");
       sb.append("Ssn Match Tag: ").append(getSsnMatchTag()).append("\n");
       sb.append("Ofac Indicator: ").append(getOfacIndicator()).append("\n");
       return sb.toString();
    }
    
}
