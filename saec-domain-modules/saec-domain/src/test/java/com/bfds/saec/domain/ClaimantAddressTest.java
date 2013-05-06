package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
@Transactional
public class ClaimantAddressTest {

	@Autowired
	private ClaimantDataOnDemand claimantDod;

	@Autowired
	ContactDataOnDemand cod;

	private Claimant claimant;

	@org.junit.Before
	public void createClaimant() {
		createAnClaimant();
	}

	@Test
	public void testEquals() {
		ClaimantAddress address = new ClaimantAddress();
		address.setAddress1("add1");
		address.setAddress2("add2");
		address.setAddress3("add3");
		address.setAddress4("add4");
		address.setAddress5("add5");
		address.setAddress6("add6");
		address.setCity("city");
		address.setCountryCode("countryCode");
		address.setStateCode("stateCode");
		address.setZipCode(new ZipCode("1111", null));

		ClaimantAddress address1 = new ClaimantAddress();
		address1.setAddress1("add1");
		address1.setAddress2("add2");
		address1.setAddress3("add3");
		address1.setAddress4("add4");
		address1.setAddress5("add5");
		address1.setAddress6("add6");
		address1.setCity("city");
		address1.setCountryCode("countryCode");
		address1.setStateCode("stateCode");
		address1.setZipCode(new ZipCode("1111", null));

		assertThat(address).isEqualTo(address);
		assertThat(address).isEqualTo(address1);
	}

	@Test
	public void verifyHashcode() {
		ClaimantAddress address = new ClaimantAddress();
		address.setAddress1("add1");
		address.setAddress2("add2");
		address.setAddress2("add2");
		address.setAddress3("add3");
		address.setAddress4("add4");
		address.setAddress5("add5");
		address.setAddress6("add6");
		address.setCity("c1");
		address.setCountryCode("cc");
		address.setStateCode("sc");

		ClaimantAddress address1 = new ClaimantAddress();
		address1.setAddress1("add1");
		address1.setAddress2("add2");
		address1.setAddress3("add3");
		address1.setAddress4("add4");
		address1.setAddress5("add5");
		address1.setAddress6("add6");
		address1.setCity("c1");
		address1.setCountryCode("cc");
		address1.setStateCode("sc");

		assertThat(address.hashCode()).isEqualTo(address1.hashCode());
		assertThat(address1.hashCode()).isNotEqualTo(
				new ClaimantAddress().hashCode());
		assertThat((new ClaimantAddress()).hashCode()).isEqualTo(
				(new ClaimantAddress()).hashCode());
	}

	/**
	 * When the Mailing Address {@link ClaimantAddress} of the {@link Claimant}
	 * is updated, all the payments that have not been yet mailed should have
	 * their check Address set to the new mailing address has to be updated.
	 */
	@Test
	public void testMailObjectAddressUpdate() {
		
		Set<Payment> payments = claimant.getPayments();
		payments.add(addStopedPayment(claimant));
		this.claimant.setPayments(payments);

		ClaimantAddress claimantAddress = this.claimant.getMailingAddress();
		claimantAddress.setAddress1("Updated Address One");
		claimantAddress.setClaimant(this.claimant);
		claimantAddress.flush();

		for (Payment payment : payments) {
			MailObjectAddress mailAddress = payment.getCheckAddress();

			if (PaymentCodeUtil.getCreatedCodes().contains(payment.getPaymentCode())) {
				assertThat(mailAddress.getAddress1()).isEqualTo("Updated Address One");
			} else {
				assertThat(mailAddress.getAddress1()).isNotEqualTo("Updated Address One");
			}
		}

	}
	
	/**
	 * When the Mailing Address {@link ClaimantAddress} of the {@link Claimant}
	 * is updated, all the letters that have not been yet mailed should have
	 * their Address set to the new mailing address has to be updated.
	 */
	@Test
	public void testMailObjectAddressUpdateForLetters() {
		
		Set<Letter> letters = claimant.getLetters();
		this.claimant.setLetters(letters);

		ClaimantAddress claimantAddress = this.claimant.getMailingAddress();
		claimantAddress.setAddress1("Updated Address One");
		claimantAddress.setClaimant(this.claimant);
		claimantAddress.flush();
		
		for (Letter letter : letters) {
			MailObjectAddress mailAddress = letter.getAddress();

			if (letter.getDstoPrintFileSentFlag()==null) {
				assertThat(mailAddress.getAddress1()).isEqualTo("Updated Address One");
			} else {
				assertThat(mailAddress.getAddress1()).isNotEqualTo("Updated Address One");
			}
		}

	}

	/**
	 * Method to add one check to {@link Claimant} with {@link PaymentCode}'s
	 * that have been mailed.
	 */
	private Payment addStopedPayment(Claimant claimant) {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setIdentificatonNo("111111111");
		c.setMailingControlNo("11111");
		c.setPaymentCode(PaymentCode.STOP_DAMASCO_STOP_CONFIRMED_P_PC);
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(this.claimant);
		this.claimant.addCheck(c);
		return c;
	}

	private void createAnClaimant() {
		this.claimant = claimantDod.getNewTransientClaimant(100);
		this.claimant.persist();
		this.clearSession();
	}

	private void clearSession() {
		claimant.flush();
		claimant.clear();
	}

}
