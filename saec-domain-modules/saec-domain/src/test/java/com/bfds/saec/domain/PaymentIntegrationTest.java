package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.PaymentType;

@RooIntegrationTest(entity = Payment.class)
public class PaymentIntegrationTest {

	@Autowired
	private PaymentDataOnDemand paymentDod;
	
	@Autowired
	private ClaimantDataOnDemand claimantDod;	

	@Test
	public void testMarkerMethod() {
	}
	
    @Test
    @Ignore("An FK violation from CheckActivity")
    public void testRemove() {
        
    }

	@Test
	public void testPaymentIdenitificationNoGeneration() {
		String identificationNo_1 = Payment.generateNewCheckNo();
		assertThat(identificationNo_1).isNotEmpty();
		String identificationNo_2 = Payment.generateNewCheckNo();
		assertThat(identificationNo_2).isNotEmpty();
		assertThat(identificationNo_1).isNotEqualTo(identificationNo_2);
	}

	@Test 
	   public void findReissueApprovedChecksOlderThan(){
		  Payment payment = paymentDod.getNewTransientPayment(1001);
		  payment.setPaymentCode(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
		  payment.setPaymentType(PaymentType.CHECK);
		  payment.setStatusChangeDate(new Date(111, 11, 8));
		  payment.persist();
		  
		  payment = paymentDod.getNewTransientPayment(1002);
		  payment.setPaymentCode(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
		  payment.setPaymentType(PaymentType.CHECK);
		  payment.setStatusChangeDate(new Date());
		  payment.persist();
		  
		  payment = paymentDod.getNewTransientPayment(1003);
		  payment.setPaymentCode(PaymentCode.STOP_REPLACE_APPROVED_R_SRA);
		  payment.setPaymentType(PaymentType.CHECK);
		  payment.setStatusChangeDate(new Date(111, 11, 8));
		  payment.persist();

		  payment = paymentDod.getNewTransientPayment(1004);
		  payment.setPaymentCode(PaymentCode.NEW_ISSUE_CASHED_C_NIC);
		  payment.setPaymentType(PaymentType.CHECK);
		  payment.setStatusChangeDate(new Date(111, 11, 8));
		  payment.persist();
		
		  payment = paymentDod.getNewTransientPayment(1005);
		  payment.setPaymentCode(PaymentCode.STOP_REPLACE_APPROVED_R_SRA);
		  payment.setPaymentType(PaymentType.WIRE);
		  payment.setStatusChangeDate(new Date(111, 11, 8));
		  payment.persist();
		  
		  payment.flush();
		  
		  List<Payment> checks = Payment.findReissueApprovedChecksOlderThan(2);
		  assertThat(checks).hasSize(2);
		  
		  assertThat(checks).onProperty("identificatonNo").containsOnly("identificatonNo_1001", "identificatonNo_1003");		  		  
	   }
	
	@Test 
	   public void findAllCreatedStatusChecks(){
		  Payment payment = paymentDod.getNewTransientPayment(11);
		  payment.setPaymentCode(PaymentCode.NEW_ISSUE_CASHED_C_NIC);
		  payment.setPaymentType(PaymentType.CHECK);
		  payment.persist();
		  		  
		  payment = paymentDod.getNewTransientPayment(22);
		  payment.setPaymentCode(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
		  payment.setPaymentType(PaymentType.CHECK);
		  payment.persist();

		  payment = paymentDod.getNewTransientPayment(33);
		  payment.setPaymentCode(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		  payment.setPaymentType(PaymentType.WIRE);
		  payment.persist();
		  
		  payment.flush();
		  
		  List<Payment> checks = Payment.findAllCreatedStatusChecks();
		  
		  assertThat(checks).onProperty("identificatonNo").contains("identificatonNo_22");
		  
		  assertThat(checks).onProperty("identificatonNo").isNotIn("identificatonNo_11", "identificatonNo_33");
	   }
	
	@Test 
	   public void findCheckByNumberAndAmount(){
		  Payment payment = paymentDod.getNewTransientPayment(111);
		  payment.setPaymentType(PaymentType.CHECK);
		  payment.setPaymentAmount(new BigDecimal(100));
		  payment.persist();
		  		  
		  payment = paymentDod.getNewTransientPayment(222);
		  payment.setPaymentAmount(new BigDecimal(100));
		  payment.setPaymentType(PaymentType.WIRE);
		  payment.persist();
		  
		  payment = paymentDod.getNewTransientPayment(333);
		  payment.setPaymentAmount(new BigDecimal(200));
		  payment.persist();		  
		  
		  payment.flush();
		  
		  Payment check = Payment.findCheckByNumberAndAmount("identificatonNo_111", 100);
		  assertThat(check.getIdentificatonNo()).isEqualTo("identificatonNo_111");
		  
		  check = Payment.findCheckByNumberAndAmount("identificatonNo_222", 100);
		  assertThat(check).isNull();
		  
		  check = Payment.findCheckByNumberAndAmount("identificatonNo_333", 100);
		  assertThat(check).isNull();		  
	   }
	
	@Test 
	   public void findCheck() {
		  final Claimant claimant = claimantDod.getNewTransientClaimant(10);
		  claimant.persist();
		  
		  Payment payment = paymentDod.getNewTransientPayment(101);
		  payment.setPaymentType(PaymentType.CHECK);
		  payment.setPaymentAmount(new BigDecimal(100));
		  claimant.addCheck(payment);		  		  		  
		  
//		  payment = paymentDod.getNewTransientPayment(2);
//		  payment.setPaymentType(PaymentType.CHECK);
//		  payment.setPaymentAmount(new BigDecimal(200));
//		  claimant.addCheck(payment);
		  
		  claimant.persist();		  		  
		  claimant.flush();
		  
		  Payment check = Payment.findChecksByAccountNoAndNettAmount(claimant.getReferenceNo(), 100).get(0);
		  assertThat(check.getIdentificatonNo()).isEqualTo("identificatonNo_101");
		  
		  List<Payment> checks= Payment.findChecksByAccountNoAndNettAmount(claimant.getReferenceNo(), 200);
		  assertThat(checks).isEmpty();
//		  
//		  check = Payment.findCheckByNumberAndAmount("identificatonNo_3", 100);
//		  assertThat(check).isNull();		  
	   }
}
