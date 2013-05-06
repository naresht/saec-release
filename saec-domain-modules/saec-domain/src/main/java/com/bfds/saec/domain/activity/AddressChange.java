package com.bfds.saec.domain.activity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.reference.ChangeSource;
import com.bfds.saec.domain.util.AccountContext;
import com.bfds.saec.rip.domain.AddressChangeRipObject;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = {"findAddressChange", "findAddressChangeEntries"})
public class AddressChange extends Activity implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    
    public static final String ACTIVITY_TYPE_DESCRIPTION = "Address Change";
           
    @Embedded
    private Address from;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "phone", column = @Column(name = "to_phone")),
            @AttributeOverride(name = "phoneExt", column = @Column(name = "to_phoneExt")),
            @AttributeOverride(name = "addressType", column = @Column(name = "to_addressType")),
            @AttributeOverride(name = "address1", column = @Column(name = "to_address1")),
            @AttributeOverride(name = "address2", column = @Column(name = "to_address2")),
            @AttributeOverride(name = "address3", column = @Column(name = "to_address3")),
            @AttributeOverride(name = "address4", column = @Column(name = "to_address4")),
            @AttributeOverride(name = "address5", column = @Column(name = "to_address5")),
            @AttributeOverride(name = "address6", column = @Column(name = "to_address6")),
            @AttributeOverride(name = "city", column = @Column(name = "to_city")),
            @AttributeOverride(name = "stateCode", column = @Column(name = "to_stateCode")),
            @AttributeOverride(name = "countryCode", column = @Column(name = "to_countryCode")),
            @AttributeOverride(name = "validFrom", column = @Column(name = "to_validFrom")),
            @AttributeOverride(name = "validTill", column = @Column(name = "to_validTill")),
            @AttributeOverride(name = "zipCode.zip", column = @Column(name = "to_zip")),
            @AttributeOverride(name = "zipCode.ext", column = @Column(name = "to_ext")),
            @AttributeOverride(name = "mailingAddress", column = @Column(name = "to_mailingAddress")),
            @AttributeOverride(name = "addressResearchCount", column = @Column(name = "to_addressResearchCount")),
            @AttributeOverride(name = "requiresAddressResearch", column = @Column(name = "to_requiresAddressResearch")),
            @AttributeOverride(name = "researchChangeActivity.researchReturned", column = @Column(name = "to_researchReturned")),
            @AttributeOverride(name = "researchChangeActivity.researchSent", column = @Column(name = "to_researchSent")),
            @AttributeOverride(name = "researchChangeActivity.hit", column = @Column(name = "to_hit")),
            @AttributeOverride(name = "researchChangeActivity.addressResearchDescription", column = @Column(name = "to_addressResearchDescription")),
            @AttributeOverride(name = "researchChangeActivity.status", column = @Column(name = "to_status")),
            @AttributeOverride(name = "researchChangeActivity.addressResearchDate", column = @Column(name = "to_addressResearchDate")) })
    private Address to;

    @Enumerated(EnumType.STRING)
    private ChangeSource source;

    public String getDescription() {
        String ret = getAddressResearchDescription();        
        if(ret != null) { 
        	return ret;
        }
        StringBuilder sb = new StringBuilder();
        if (from != null) {
        	sb.append(" From : ");
            sb.append(from.getAddressLinesAsString());
        }
        if (to != null) {
            sb.append(" To : ");
            sb.append(to.getAddressLinesAsString());
        }
        return sb.toString();
    }

	private String getAddressResearchDescription() {
		if (to.getResearchChangeActivity() != null && (to.getResearchChangeActivity().isResearchReturned() 
                || to.getResearchChangeActivity().isResearchSent())) {
           return to.getResearchChangeActivity().getAddressResearchDescription();
        }
		return null;
	}

    public String getActivityTypeDescription() {
        String ret = getAddressResearchTypeDescription();
        if(ret != null) { 
        	return ret;
        }
        return ACTIVITY_TYPE_DESCRIPTION;
    }

	private String getAddressResearchTypeDescription() {
		if (to.getResearchChangeActivity() != null && (to.getResearchChangeActivity().isResearchReturned() 
                || to.getResearchChangeActivity().isResearchSent())) {
            return to.getResearchChangeActivity().getStatus();
        }
		return null;
	}

    public AddressChangeRipObject getAddressChangeRipObject() {
    	AddressChangeRipObject ripObject = new AddressChangeRipObject();
    	
    	if(this.getFrom().getAddressType()!= null){
    		ripObject.setFromAddressType(this.getFrom().getAddressType().name());
    	}
        ripObject.setFromAddress1(this.getFrom().getAddress1());
        ripObject.setFromAddress2(this.getFrom().getAddress2());
        ripObject.setFromAddress3(this.getFrom().getAddress3());
        ripObject.setFromAddress4(this.getFrom().getAddress4());
        ripObject.setFromAddress5(this.getFrom().getAddress5());
        ripObject.setFromAddress6(this.getFrom().getAddress6());
        ripObject.setFromCity(this.getFrom().getCity());
        ripObject.setFromStateCode(this.getFrom().getStateCode());
        ripObject.setFromCountryCode(this.getFrom().getCountryCode());
        if (this.getFrom().getZipCode() != null) {
            ripObject.setFromZipCode(this.getFrom().getZipCode().getZip());
            ripObject.setFromZipExt(this.getFrom().getZipCode().getExt());
        }
        ripObject.setToAddressType(this.getTo().getAddressType().name());
        ripObject.setToAddress1(this.getTo().getAddress1());
        ripObject.setToAddress2(this.getTo().getAddress2());
        ripObject.setToAddress3(this.getTo().getAddress3());
        ripObject.setToAddress4(this.getTo().getAddress4());
        ripObject.setToAddress5(this.getTo().getAddress5());
        ripObject.setToAddress6(this.getTo().getAddress6());
        ripObject.setToCity(this.getTo().getCity());
        ripObject.setToStateCode(this.getTo().getStateCode());
        ripObject.setToCountryCode(this.getTo().getCountryCode());
        if (this.getTo().getZipCode() != null) {
            ripObject.setToZipCode(this.getTo().getZipCode().getZip());
            ripObject.setToZipExt(this.getTo().getZipCode().getExt());
        }
        ripObject.setCreatedByUser(AccountContext.getCurrentUsername());
        return ripObject;
    }
}
