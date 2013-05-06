package com.bfds.saec.web.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.util.AccountContext;
import com.bfds.saec.rip.domain.SpecialhandlingRipObject;
import com.bfds.saec.rip.service.RipEventListener;

public class SpecialHandlingViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	final static Logger log = LoggerFactory.getLogger(SpecialHandlingViewModel.class);
	
	private SpecialhandlingRipObject ripObject;
	
	@Autowired
	private transient RipEventListener ripEventListener;
	
	public void setClaimant(final Claimant claimant) {
		ripObject = new SpecialhandlingRipObject();
		if(log.isInfoEnabled())
			log.info(String.format("Processing RIP object for Special Handling."));
		final ClaimantAddress address = claimant.getMailingAddress();
		ripObject.setAddress1(address.getAddress1());
		ripObject.setAddress2(address.getAddress2());
		ripObject.setAddress3(address.getAddress3());
		ripObject.setAddress4(address.getAddress4());
		ripObject.setAddress5(address.getAddress5());
		ripObject.setAddress6(address.getAddress6());
		ripObject.setCity(address.getCity());
		ripObject.setStateCode(address.getStateCode());
		if(address.getZipCode() != null) {
			ripObject.setZipCode(address.getZipCode().getZip());
			ripObject.setZipExt(address.getZipCode().getExt());
		}
		
		ripObject.setCreatedByUser(AccountContext.getCurrentUsername());
		ripObject.setReferenceNo(claimant.getReferenceNo());
		ripObject.setCorrelationId(claimant.getId());
		
		ripObject.setRegistration1(claimant.getClaimantRegistration().getRegistration1());
		ripObject.setRegistration2(claimant.getClaimantRegistration().getRegistration2());
		ripObject.setRegistration3(claimant.getClaimantRegistration().getRegistration3());
		ripObject.setRegistration4(claimant.getClaimantRegistration().getRegistration4());
		ripObject.setRegistration5(claimant.getClaimantRegistration().getRegistration5());
		ripObject.setRegistration6(claimant.getClaimantRegistration().getRegistration6());

	}
	
	public boolean save(final MessageContext messageContext) {
		if(StringUtils.hasText(ripObject.getSpecialInstructions())) {
			ripEventListener.sendSpecialHandlingInstructions(ripObject);
			return true;
		} else {
			//messageContext
		}		
		return false;
	}

	public SpecialhandlingRipObject getRipObject() {
		return ripObject;
	}

	public void setRipObject(SpecialhandlingRipObject ripObject) {
		this.ripObject = ripObject;
	}

	
	
	
}