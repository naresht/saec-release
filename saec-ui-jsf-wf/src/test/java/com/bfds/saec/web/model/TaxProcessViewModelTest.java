package com.bfds.saec.web.model;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import com.bfds.saec.batch.file.domain.out.damasco_domestic.OutboundDomesticTaxRec;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.EventPaymentConfig;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.PaymentCalc;
import com.bfds.saec.domain.PaymentComponentTypeLov;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.PaymentComponentType;
import com.bfds.saec.domain.reference.PaymentType;

@MockStaticEntityMethods
@org.junit.Ignore
public class TaxProcessViewModelTest {

	private static final String PAYMENT_ID_IS_NULL = "paymentId is null";

	private static final String PAYMENT_IS_NULL = "payment is null";

	private static final Long PAYMENT_ID = new Long(1);

	@Test
	public void shouldThrowExceptionWhenGetOutboundDomesticTaxRecOnNullPayment() {

		try {
			TaxProcessViewModel viewModel = new TaxProcessViewModel();
			viewModel.getOutboundDomesticTaxRec(null);
			fail();

		} catch (IllegalArgumentException ex) {
			assertThat(PAYMENT_ID_IS_NULL.equals(ex.getMessage()));
		}
	}

	@Test
	public void shouldThrowExceptionWhenGetOutboundDomesticTaxRecOnNoPayment() {
		final Long PAYMENT_ID = new Long(-1);

		TaxProcessViewModel viewModel = new TaxProcessViewModel();

		OutboundDomesticTaxRec.getRecord(PAYMENT_ID);
		AnnotationDrivenStaticEntityMockingControl
				.expectThrow(new RuntimeException());

		Payment.findPayment(PAYMENT_ID);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(null);

		AnnotationDrivenStaticEntityMockingControl.playback();

		try {
			viewModel.getOutboundDomesticTaxRec(PAYMENT_ID);
			fail();
		} catch (IllegalArgumentException ex) {
			assertThat(ex.getMessage()).isEqualTo(PAYMENT_IS_NULL);
		}

	}

	@Test
	public void shouldReturnRecordFromOutboundDomesticTaxRecTable() {

		final OutboundDomesticTaxRec expected = new OutboundDomesticTaxRec();
		final Long PAYMENT_ID = new Long(1);
		OutboundDomesticTaxRec.getRecord(PAYMENT_ID);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(expected);
		AnnotationDrivenStaticEntityMockingControl.playback();

		TaxProcessViewModel viewModel = new TaxProcessViewModel();
		OutboundDomesticTaxRec actual = viewModel
				.getOutboundDomesticTaxRec(PAYMENT_ID);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldBuildOutboundDomesticTaxRecFromGivenPayment() {

		@SuppressWarnings("deprecation")
		Payment inputPayment = new Payment();
		inputPayment.setId(PAYMENT_ID);
		inputPayment.setPaymentType(PaymentType.CHECK);

		Claimant payTo = new Claimant();
		ClaimantRegistration claimantRegistration = new ClaimantRegistration();
		claimantRegistration.setRegistration1("Bill");
		payTo.setClaimantRegistration(claimantRegistration);

		ClaimantAddress claimantAddress = new ClaimantAddress();
		claimantAddress.setAddress1("NJ 102");
		claimantAddress.setZipCode(new ZipCode());
		payTo.setAddressOfRecord(claimantAddress);

		PaymentCalc paymentCalc = new PaymentCalc();
		paymentCalc.setPaymentComp1(new BigDecimal(101));

		inputPayment.setPaymentCalc(paymentCalc);

		inputPayment.setPayTo(payTo);

		OutboundDomesticTaxRec.getRecord(PAYMENT_ID);
		AnnotationDrivenStaticEntityMockingControl
				.expectThrow(new RuntimeException());
		Payment.findPayment(PAYMENT_ID);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(inputPayment);
		Event.getCurrentEventDda();
		AnnotationDrivenStaticEntityMockingControl.expectReturn("11111111");

		EventPaymentConfig config1 = new EventPaymentConfig();

		config1.setDefaultDescription("Payment Component");
		PaymentComponentTypeLov paymentComponentType1 = new PaymentComponentTypeLov();
		config1.setPaymentComponentType(paymentComponentType1);

		EventPaymentConfig
				.findByPaymentComponentType(PaymentComponentType.paymentComp1);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(config1);

		EventPaymentConfig
				.findByPaymentComponentType(PaymentComponentType.paymentComp2);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(config1);

		EventPaymentConfig
				.findByPaymentComponentType(PaymentComponentType.paymentComp3);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(config1);

		EventPaymentConfig
				.findByPaymentComponentType(PaymentComponentType.paymentComp4);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(config1);

		EventPaymentConfig
				.findByPaymentComponentType(PaymentComponentType.paymentComp5);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(config1);

		AnnotationDrivenStaticEntityMockingControl.playback();
		TaxProcessViewModel viewModel = new TaxProcessViewModel();
		OutboundDomesticTaxRec actual = viewModel
				.getOutboundDomesticTaxRec(PAYMENT_ID);
		assertThat(actual).isNotNull();
		assertThat(actual.getAddress_1()).isEqualTo(
				inputPayment.getPayTo().getAddressOfRecord().getAddress1());
		assertThat(actual.getDda()).isEqualTo("11111111");
	}
}
