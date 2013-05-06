package com.bfds.saec.domain.activity;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

public class CheckActivityTest {

	@Test
	public void OverrideActivityDescription() {
		CheckActivity activity = new CheckActivity();
		activity.setComments("some comments");
		activity.setToPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		activity.setIdentificationNo("12345");
		
		activity.setDescription("Overriding description...");		
		assertThat(activity.getDescription()).isEqualTo("Overriding description...");
	}
	
	@Test
	public void verifyActivityDescription() {
		CheckActivity activity = new CheckActivity();
		activity.setComments("some comments");
		activity.setToPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		activity.setIdentificationNo("12345");
		
		activity.setToPaymentType(PaymentType.CHECK);
		assertThat(activity.getDescription()).isEqualTo("<b>Check</b>#12345 Void Re-Issue Req - some comments");
		
		activity.setToPaymentType(PaymentType.WIRE);
		assertThat(activity.getDescription()).isEqualTo("<b>Wire</b>#12345 Void Re-Issue Req - some comments");
		
		activity.setToPaymentType(null);
		assertThat(activity.getDescription()).isEqualTo("<b>Payment</b>#12345 Void Re-Issue Req - some comments");
		
		activity.setIdentificationNo(null);
		assertThat(activity.getDescription()).isEqualTo("<b>Payment</b> Void Re-Issue Req - some comments");
		
		activity.setComments(null);
		assertThat(activity.getDescription()).isEqualTo("<b>Payment</b> Void Re-Issue Req");				
	}
	
	@Test
	public void verifyActivityTypeDescription() { 
		Activity activity = new CheckActivity();
		assertThat(activity.getActivityTypeDescription()).isEqualTo("Payment Management");
	}
}
