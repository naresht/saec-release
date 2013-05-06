package com.bfds.saec.domain.activity;

import javax.persistence.Column;
import javax.persistence.Embedded;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.RegistrationLines;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 public class AlternatePayeeActivity extends Activity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public static final String ACTIVITY_TYPE_DESCRIPTION = "Alternate Payee Created";

    @Embedded
	private Address address;

    @Embedded
	private RegistrationLines registration;        
	
	private String referenceNo;
	
	@Column(nullable=false)
	private Long alternatePayeeId;

	public String getDescription() {
		StringBuilder sb = new StringBuilder("<b>Reference#</b>");
		//TODO: This is a very poor hack. Can cause memory leaks in the UI. Second the context root is hard coded.
		sb.append("<a href='/saec/app/claimant/edit?id=").append(alternatePayeeId).append("'>").append(referenceNo).append("</a>");
		sb.append("<br/>");
		sb.append("<b>Name:</b>");
		sb.append(registration.getRegistrationLinesAsString());
		sb.append("<br/>");
		sb.append("<b>Address:</b>");
		sb.append(address.getAddressLinesAsString());
		return sb.toString();
	}	
	
	public String getActivityTypeDescription() {
		return ACTIVITY_TYPE_DESCRIPTION;
	}		
		
}
