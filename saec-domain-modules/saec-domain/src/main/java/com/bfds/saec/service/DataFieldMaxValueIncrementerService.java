package com.bfds.saec.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
@Component("dataFieldMaxValueIncrementer")
public class DataFieldMaxValueIncrementerService implements InitializingBean {

	@Autowired
	private DataFieldMaxValueIncrementer mailingControlNoIncrementer;
	
	@Autowired
	private DataFieldMaxValueIncrementer claimIdentifierIncrementer;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(mailingControlNoIncrementer);
		Assert.notNull(claimIdentifierIncrementer);
	}
	
	public String getNextMailingControlNumber() {
		return mailingControlNoIncrementer.nextStringValue();
	}

	public String getNextClaimtIdentifierNumber() {
		return claimIdentifierIncrementer.nextStringValue();
	}
	
}
