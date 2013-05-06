package com.bfds.saec.domain.listener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.ClaimantTaxInfo;
import com.bfds.saec.domain.Contact;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.Name;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.RegistrationLines;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.AddressChange;
import com.bfds.saec.domain.activity.AlternatePayeeActivity;
import com.bfds.saec.domain.activity.CheckActivity;
import com.bfds.saec.domain.activity.ContactChange;
import com.bfds.saec.domain.activity.NameChange;
import com.bfds.saec.domain.activity.TaxInfoChange;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.reference.RPOType;
import com.bfds.saec.domain.util.AccountContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
@Transactional(propagation = Propagation.REQUIRES_NEW)
// TODO: Should really do it with:
// @TransactionConfiguration(defaultRollback=true)
// So that tests are independent and don't commit
public class ActivityEventListenerTest{

	private Claimant claimantWithRegistrationAndAddress;
	 
	@Before
	public void createClaimantForTest() {
		claimantWithRegistrationAndAddress = newClaimant();
		claimantWithRegistrationAndAddress.persist();
		claimantWithRegistrationAndAddress.flush();
		claimantWithRegistrationAndAddress = Claimant
				.findClaimant(claimantWithRegistrationAndAddress.getId());
		claimantWithRegistrationAndAddress.getActivity().clear();
		claimantWithRegistrationAndAddress.persist();
		claimantWithRegistrationAndAddress.flush();
	}

	@After
	public void deleteClaimant() {
		claimantWithRegistrationAndAddress.remove();
	}

	@Test
	public void createNameChangeActivity() {
		assertNotNull(claimantWithRegistrationAndAddress.getId());
		RegistrationLines from = new RegistrationLines();
		claimantWithRegistrationAndAddress.getClaimantRegistration().getLines()
				.copyTo(from);

		claimantWithRegistrationAndAddress.getClaimantRegistration()
				.setRegistration1("r 1");
		claimantWithRegistrationAndAddress.getClaimantRegistration()
				.setRegistration2("r 2");
		claimantWithRegistrationAndAddress.getClaimantRegistration()
				.setRegistration3("r 3");
		claimantWithRegistrationAndAddress.getClaimantRegistration()
				.setRegistration4("r 4");
		claimantWithRegistrationAndAddress.getClaimantRegistration()
				.setRegistration5("r 5");
		claimantWithRegistrationAndAddress.getClaimantRegistration()
				.setRegistration6("r 6");

		RegistrationLines to = new RegistrationLines();
		claimantWithRegistrationAndAddress.getClaimantRegistration().getLines()
				.copyTo(to);

		claimantWithRegistrationAndAddress.merge().persist();
		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(1, activity.size());
		NameChange nameChange = (NameChange) activity.iterator().next();
		assertEquals(from, nameChange.getFrom());
		assertEquals(to, nameChange.getTo());
	}

	@Test
	public void createPrimaryAddressChangeActivity() {
		assertNotNull(claimantWithRegistrationAndAddress.getId());

		Address from = new Address();
		claimantWithRegistrationAndAddress.getAddressOfRecord().copyTo(from);

		claimantWithRegistrationAndAddress.getAddressOfRecord().setAddress1(
				"a 1");
		claimantWithRegistrationAndAddress.getAddressOfRecord().setAddress2(
				"a 1");
		claimantWithRegistrationAndAddress.getAddressOfRecord().setAddress3(
				"a 1");
		claimantWithRegistrationAndAddress.getAddressOfRecord().setAddress4(
				"a 1");
		claimantWithRegistrationAndAddress.getAddressOfRecord().setAddress5(
				"a 1");
		claimantWithRegistrationAndAddress.getAddressOfRecord().setAddress6(
				"a 1");
		claimantWithRegistrationAndAddress.getAddressOfRecord().setCity("c");
		claimantWithRegistrationAndAddress.getAddressOfRecord().setStateCode(
				"s");
		claimantWithRegistrationAndAddress.getAddressOfRecord().getZipCode()
				.setZip("1111");
		claimantWithRegistrationAndAddress.getAddressOfRecord().getZipCode()
				.setExt("1111");

		Address to = new Address();
		claimantWithRegistrationAndAddress.getAddressOfRecord().copyTo(to);

		claimantWithRegistrationAndAddress.merge().persist();

		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(1, activity.size());
		AddressChange addressChange = (AddressChange) activity.iterator()
				.next();

		assertEquals(from, addressChange.getFrom());
		assertEquals(to, addressChange.getTo());
	}
	
	/**
	 * Added Test for GitHub Issue#4,Generating an Activity in case if
	 * AddressType changes from Primary to Seasonal Or Vice Versa.
	 */
	@Test
	public void createAddressTypeChangeActivity() {
		assertNotNull(claimantWithRegistrationAndAddress.getId());
		
		ClaimantAddress address = claimantWithRegistrationAndAddress.getAddressOfRecord();
		claimantWithRegistrationAndAddress.setMailingAddressByType(AddressType.ADDRESS_OF_RECORD);
		address.setAddressType(AddressType.SEASONAL_ADDRESS);
		claimantWithRegistrationAndAddress.merge().persist();
		
		Set<Activity> activity = Claimant.findClaimant(claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals("Seasonal Address Selected",activity.iterator().next().getDescription());
		assertEquals(1, activity.size());
		
	}

	@Test
	public void changeAddressPropertiesThatDoNotGenerateAddressChangeActivity() {
		assertNotNull(claimantWithRegistrationAndAddress.getId());

		Address from = new Address();
		claimantWithRegistrationAndAddress.getAddressOfRecord().copyTo(from);

		claimantWithRegistrationAndAddress.getAddressOfRecord().getAddress()
				.setAddressResearchCount(10);
		claimantWithRegistrationAndAddress.getAddressOfRecord().getAddress()
				.setRequiresAddressResearch(true);

		Address to = new Address();
		claimantWithRegistrationAndAddress.getAddressOfRecord().copyTo(to);

		claimantWithRegistrationAndAddress.merge().persist();

		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(0, activity.size());
	}

	@Test
	public void createAlternatePayeeActivity() {
		assertNotNull(claimantWithRegistrationAndAddress.getId());
		Claimant payee1 = newClaimant();
		payee1.setParentClaimant(claimantWithRegistrationAndAddress);
		payee1.persist();
		Claimant payee2 = newClaimant();
		payee2.setParentClaimant(claimantWithRegistrationAndAddress);
		payee2.persist();
		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(3, activity.size());
		for (Activity currentActivity : activity) {
			if (currentActivity instanceof AlternatePayeeActivity) {
				AlternatePayeeActivity alternatePayeeActivity = (AlternatePayeeActivity) currentActivity;

				assertNotNull(alternatePayeeActivity.getReferenceNo());
			}
		}
		activity = Claimant.findClaimant(payee1.getId()).getActivity();
		assertEquals(2, activity.size());
		Activity childActivity = (Activity) activity.iterator().next();

		assertNotNull(childActivity.getDescription());

		payee1.remove();
		payee2.remove();
	}

	@Test
	public void createNewCheckActivity() {
		assertNotNull(claimantWithRegistrationAndAddress.getId());
		Payment check = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		check.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		claimantWithRegistrationAndAddress.addCheck(check);
		// claimantWithRegistrationAndAddress.merge().map();
		check.merge().persist();
		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(2, activity.size());
		for (Activity currentActivity : activity) {
			if (currentActivity instanceof CheckActivity) {
				CheckActivity checkActivity = (CheckActivity) currentActivity;

				assertNotNull(checkActivity.getToPaymentCode());
			}
		}}

	@Test
	public void createUpdateCheckActivity() {
		assertNotNull(claimantWithRegistrationAndAddress.getId());
		Payment check = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        check.setPaymentType(PaymentType.CHECK);
		check.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		claimantWithRegistrationAndAddress.addCheck(check);
		// claimantWithRegistrationAndAddress.merge().map();
		check.merge().persist();
		check.flush();
		claimantWithRegistrationAndAddress = Claimant
				.findClaimant(claimantWithRegistrationAndAddress.getId());
		check = claimantWithRegistrationAndAddress.getPayments().iterator()
				.next();
		check.setPaymentCode(PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
		check.merge().persist();
		
		// Added one time mailing address to insure GitHub issue #292 {One time
		// mailing address is not getting displayed after saved in Re issue page}
		
		MailObjectAddress mailObjAddress=new MailObjectAddress();
		mailObjAddress.copyTo(claimantWithRegistrationAndAddress.getMailingAddress());
		mailObjAddress.setAddressType(AddressType.ONE_TIME_MAILING);
		check.setCheckAddress(mailObjAddress);
		check.merge().persist();
	
		Set<Activity> activity = Claimant.findClaimant(claimantWithRegistrationAndAddress.getId()).getActivity();

		Boolean hasOneTimeMailingActivity=Boolean.FALSE;
		
		for (Activity currentActivity : activity) {
			if (currentActivity.getDescription().contains("Payment mailed to a one time mailing address:")) {
				hasOneTimeMailingActivity = Boolean.TRUE;
				break;
			}
		}

		assertEquals(5, activity.size());
		assertEquals(hasOneTimeMailingActivity,Boolean.TRUE);
		

	}

	@Test
	public void createPaymentChangedFromCheckToWire() {
		assertNotNull(claimantWithRegistrationAndAddress.getId());
		Payment check = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		check.setPaymentCode(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		claimantWithRegistrationAndAddress.addCheck(check);
		check.merge().persist();
		check.flush();
		claimantWithRegistrationAndAddress = Claimant
				.findClaimant(claimantWithRegistrationAndAddress.getId());
		check = claimantWithRegistrationAndAddress.getPayments().iterator()
				.next();
		check.setPaymentType(PaymentType.WIRE);
		check.setPaymentCode(PaymentCode.WIRE_REQUESTED_W_W);
		check.merge().persist();
		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(3, activity.size());

	}

	@Test
	public void updateTaxInformation() {
		ClaimantTaxInfo from = new ClaimantTaxInfo();
		from = claimantWithRegistrationAndAddress.getTaxInfo().clone();

		claimantWithRegistrationAndAddress.setSsn("1");
		claimantWithRegistrationAndAddress.setCertificationType("xxxxx");
		claimantWithRegistrationAndAddress.setCertificationStatus("yyyy");
		claimantWithRegistrationAndAddress.setUsCitizen(false);
		claimantWithRegistrationAndAddress.setTaxCountryCode("CA");
		claimantWithRegistrationAndAddress.setCertificationDate(new Date());

		ClaimantTaxInfo to = new ClaimantTaxInfo();
		to = claimantWithRegistrationAndAddress.getTaxInfo().clone();

		claimantWithRegistrationAndAddress.merge().persist();

		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(1, activity.size());
		TaxInfoChange taxInfoChange = (TaxInfoChange) activity.iterator()
				.next();

		assertEquals(from, taxInfoChange.getFrom());
		assertEquals(to, taxInfoChange.getTo());
	}

	/**
	 * Test for Primary Contact Change Activity
	 */
	@Test
	public void testGenerateClaimantPrimaryContactUpdateActivity() {
		Contact primaryContact = claimantWithRegistrationAndAddress
				.getPrimaryContact();
		primaryContact.setName(new Name());
		primaryContact.setPhoneNo("0");
		primaryContact.setCellPhoneNo("1");
		primaryContact.setWorkPhoneNo("2");
		primaryContact.setEmail("@");
		claimantWithRegistrationAndAddress.merge().persist();
		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(1, activity.size());

		ContactChange contactChange = (ContactChange) activity.iterator()
				.next();
		assertEquals(ContactChange.ACTIVITY_TYPE_DESCRIPTION_PRIMARY_CONTACT,
				contactChange.getActivityTypeDescription());
	}
	
	/**
	 * Test for Additional Contact Created Activity
	 */
	@Test
	public void testGenerateClaimantAdditionalContactCreateActivity() {
		Contact additionalContact = newContact();
		additionalContact.setCellPhoneNo("111-111-1111");
		additionalContact.setName(new Name("Test Name",null,null));
		additionalContact.merge().persist();

		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(1, activity.size());

		ContactChange contactChange = (ContactChange) activity.iterator()
				.next();
		assertEquals(ContactChange.ACTIVITY_TYPE_DESCRIPTION_CONTACT_ADD,
				contactChange.getActivityTypeDescription());
	}

	@Test
	public void testStopLift() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentCode(PaymentCode.STOP_STOP_REQUESTED_S_SR);
		c.setPaymentType(PaymentType.CHECK);

		claimantWithRegistrationAndAddress.addCheck(c);
		claimantWithRegistrationAndAddress.merge().persist();

		claimantWithRegistrationAndAddress = Claimant
				.findClaimant(claimantWithRegistrationAndAddress.getId());
		claimantWithRegistrationAndAddress.getActivity().clear();
		claimantWithRegistrationAndAddress.merge().persist();

		c = Claimant.findClaimant(claimantWithRegistrationAndAddress.getId())
				.getPayments().iterator().next();
		c.doStopLift();
		c.persist();
		c.flush();
		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(1, activity.size());
		CheckActivity checkActivity = (CheckActivity) activity.iterator()
				.next();

		assertEquals("Stop lifted", checkActivity.getComments());

	}

	@Test
	public void testVoidReversal() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentCode(PaymentCode.VOID_VOIDED_V_V);
		c.setPaymentType(PaymentType.CHECK);

		claimantWithRegistrationAndAddress.addCheck(c);
		claimantWithRegistrationAndAddress.merge().persist();

		claimantWithRegistrationAndAddress = Claimant
				.findClaimant(claimantWithRegistrationAndAddress.getId());
		claimantWithRegistrationAndAddress.getActivity().clear();
		claimantWithRegistrationAndAddress.merge().persist();

		c = Claimant.findClaimant(claimantWithRegistrationAndAddress.getId())
				.getPayments().iterator().next();
		c.doStopVoidReverse(c.getPaymentCode());
		c.persist();
		c.flush();
		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(1, activity.size());
		CheckActivity checkActivity = (CheckActivity) activity.iterator()
				.next();

		assertEquals("Void reversed", checkActivity.getComments());

		c = Claimant.findClaimant(claimantWithRegistrationAndAddress.getId())
				.getPayments().iterator().next();
		assertEquals(c.getPaymentCode(), PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);

	}

	@Test
	public void testStopReversal() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentCode(PaymentCode.STOP_STOP_REQUESTED_S_SR);
		c.setPaymentType(PaymentType.CHECK);

		claimantWithRegistrationAndAddress.addCheck(c);
		claimantWithRegistrationAndAddress.merge().persist();

		claimantWithRegistrationAndAddress = Claimant
				.findClaimant(claimantWithRegistrationAndAddress.getId());
		claimantWithRegistrationAndAddress.getActivity().clear();
		claimantWithRegistrationAndAddress.merge().persist();

		c = Claimant.findClaimant(claimantWithRegistrationAndAddress.getId())
				.getPayments().iterator().next();
		c.doStopVoidReverse(c.getPaymentCode());
		c.persist();
		c.flush();
		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(1, activity.size());
		CheckActivity checkActivity = (CheckActivity) activity.iterator()
				.next();

		assertEquals("Stop reversed", checkActivity.getComments());
	}

	@Test
	public void createLetterCreatedActivity() {
		Letter letter = new Letter();
		letter.setLetterCode(newLetterCode());
		claimantWithRegistrationAndAddress.addLetter(letter);
		letter.merge().persist();

		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(2, activity.size());

	}
	
	@Test
	public void createAddressCreatedActivity() {
		ClaimantAddress address = new ClaimantAddress();
		address.setAddressType(AddressType.SEASONAL_ADDRESS);
		claimantWithRegistrationAndAddress.getAddressOfRecord().setAddress1("testSeasonalAddr1");
		claimantWithRegistrationAndAddress.getAddressOfRecord().setAddress2("testSeasonAddr2");
		claimantWithRegistrationAndAddress.getAddressOfRecord().setAddress6("testSeasonAddr3");
		claimantWithRegistrationAndAddress.getAddressOfRecord().setCity("testSeasonCity");
		claimantWithRegistrationAndAddress.getAddressOfRecord().setStateCode("testSeasonState");
		claimantWithRegistrationAndAddress.getAddressOfRecord().getZipCode().setZip("12345");
		claimantWithRegistrationAndAddress.getAddressOfRecord().getZipCode().setExt("6789");
		claimantWithRegistrationAndAddress.addAddress(address);
		address.merge().persist();

		Set<Activity> activity = Claimant.findClaimant(claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(1, activity.size());

	}

	@Test
	public void createLetterChangeActivity() {
		LetterCode lc = new LetterCode("100", "Claim Form - Signed 1", LetterType.CLAIM_FORM);
		lc.persist();
		lc.flush();
		lc.clear();
		Letter letter = new Letter();
		letter.setRpoType(RPOType.FORWARDABLE);
		letter.setLetterCode(LetterCode.findByCode("100"));
		letter.setDescription("Before Update");
		claimantWithRegistrationAndAddress.addLetter(letter);
		letter.merge().persist();

		letter.setDescription("After Update");
		letter.setRpoType(RPOType.NONFORWARDABLE);

		claimantWithRegistrationAndAddress.addLetter(letter);
		letter.merge().persist();

		Set<Activity> activity = Claimant.findClaimant(
				claimantWithRegistrationAndAddress.getId()).getActivity();
		assertEquals(3, activity.size());

	}

	private LetterCode newLetterCode() {
		LetterCode lc = new LetterCode("700", "Claim Form - Signed", LetterType.CLAIM_FORM);
		lc.persist();
		return lc;
	}

	private Claimant newClaimant() {
		Claimant p = new Claimant();
		String id = "100";
		p.setFundAccountNo(id + "12345" + id);
		p.setAccountStatus("Open");
		p.setAccountType("Custodial"); // ???
		p.setBrokerId("BRK-" + id + "121212" + id);
		p.setBin("BIN-" + id + "34334" + id);
		p.setOmniBus(Integer.parseInt(id) % 50 == 0);
		p.setSsn("123-123-1234");
		p.setCertificationStatus("Certified"); // E
		p.setCertificationType("W9-US Citizen");// E
		p.setUsCitizen(Boolean.TRUE);
		p.setTaxCountryCode("US");
		p.setCertificationDate(new Date());
		p.setCreatedBy(AccountContext.getCurrentUsername());
		p.setOrganization(Integer.parseInt(id) % 10 == 0);
		p.setCreatedDate(new Date());
		p.setMarketTimer(Integer.parseInt(id) % 5 == 0);
		p.setClaimantRegistration(new ClaimantRegistration());
		p.getClaimantRegistration().setRegistration1("John Smith");

		newPrimaryAddress(p);
		p.setPrimaryContact(newContact());
		return p;
	}

	private Contact newContact() {
		Contact c = new Contact();
		//c.setEmail("test@mail.com");
		//c.setPhoneNo("111-111-1234");
		return c;
	}

	private void newPrimaryAddress(Claimant p) {
		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a.setAddress1("6 Cambridge Center");
		a.setCity("Cambridge");
		a.setStateCode("MA");
		a.setCountryCode("US");
		a.setZipCode(new ZipCode("02142", null));
		a.setClaimant(p);
		p.addAddress(a);
	}

}
