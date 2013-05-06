package com.bfds.saec.domain;


import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.reference.CallCode;
import com.bfds.saec.domain.reference.CallType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
@Transactional
public class PhoneCallTest{
	@Autowired
	ClaimantDataOnDemand claimantDod;
	
	@Before
	public void createCallCodes() {
		CallCode code = new CallCode();
		code.setCode("c1");
		code.setDescription("code 1");
		code.persist();
		code = new CallCode();
		code.setCode("c2");
		code.setDescription("code 2");
		code.persist();
		
	}
	
	@Test
	public void createCallLog() {
		Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		claimant.flush();
		
		PhoneCall call = claimant.merge().startCallLog("csr");
		call.getCallCode().addAll(CallCode.findAllCallCodes());
		call.persist();
		call.flush();
		call.clear();
		
		call = PhoneCall.findPhoneCall(call.getId());
		assertThat(call.getDescription()).contains("code 1");
		assertThat(call.getDescription()).contains("code 2");
		
		assertThat(call.getActivityTypeDescription()).isEqualTo("Call");
		
		call.setCallType(CallType.INBOUND);		
		assertThat(call.getActivityTypeDescription()).isEqualTo("Incoming Call");
		
		call.setCallType(CallType.OUTBOUND);		
		assertThat(call.getActivityTypeDescription()).isEqualTo("Outgoing Call");						
	}
	
	@Test
	@Ignore
	public void findAllCallsofUser() { 
		Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		claimant.flush();
		
		PhoneCall call = claimant.merge().startCallLog("csr");
		call.getCallCode().addAll(CallCode.findAllCallCodes());
		call.persist();
		call.flush();
		call.clear();
		
		assertThat(PhoneCall.findAllCallsofUser("abs")).isEmpty();
		
		assertThat(PhoneCall.findAllCallsofUser("csr")).isNotEmpty();
		
		assertThat(PhoneCall.findAllCallsofUser("csr")).onProperty("userId").containsOnly("csr");
		
	}
}
