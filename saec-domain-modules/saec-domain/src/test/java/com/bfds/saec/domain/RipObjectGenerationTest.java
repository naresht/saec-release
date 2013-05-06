package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.util.AccountContext;
import com.bfds.saec.rip.domain.CureLetterRipObject;

/*
 * Test to compare the expected RipObject with the generated RipObject for CureLetters. 
 * Need to extend this to another rip objects also when time permits.
 */

public class RipObjectGenerationTest {
	/*
	 * Comparing the expected RipObject with the generated RipObject for
	 * CureLetters. Both should be equal
	 */
	@Test
	public void testCureLetterRipObject() {
		CureLetterRipObject ripObject = createExpectedCureLetterRipObject();
		Letter letter = createLetter();

		assertThat(letter.newCureLetterRipObject()).isEqualTo(ripObject);

	}

	private Letter createLetter() {
		LetterCode lc = new LetterCode("101", "", LetterType.CLAIM_FORM);
		lc.setWorkType(new WorkType("workType1"));
		Letter letter = new Letter();
		letter.setClaimant(createClaimant());
		letter.setPayToRegistration(createRegistrationLines());
		letter.setAddress(createMailingAddress());
		letter.setLetterCode(lc);
		
		letter.setComments("Some Comments");
		
		return letter;
	}

	/*
	 * Setting Claimant and Registration Number for Letter
	 */
	private static Claimant createClaimant() {
		Claimant p = new Claimant() {

			@Override
			public PhoneCall findCallInProgess() {
				PhoneCall activeCall = new PhoneCall();
				activeCall.setId(new Long(123));
				return activeCall;
			}

		};
		p.setReferenceNo("100000001");
		return p;
	}

	/*
	 * Setting RegistrationLines for Letter
	 */
	private static RegistrationLines createRegistrationLines() {
		RegistrationLines reg = new RegistrationLines();

		reg.setRegistration1("RegistrationName1");
		reg.setRegistration2("RegistrationName2");
		reg.setRegistration3("RegistrationName3");
		reg.setRegistration4("RegistrationName4");
		reg.setRegistration5("RegistrationName5");
		reg.setRegistration6("RegistrationName6");
		return reg;
	}

	/*
	 * Setting MailingObjectAddress for Letter
	 */
	private static MailObjectAddress createMailingAddress() {
		MailObjectAddress a = new MailObjectAddress();
		a.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a.setAddress1("address 1");
		a.setAddress2("address 2");
		a.setAddress3("address 3");
		a.setAddress4("address 4");
		a.setAddress5("address 5");
		a.setAddress6("address 6");
		a.setCity("CITY");
		a.setStateCode("SC");
		a.setCountryCode("CNT");
		a.setZipCode(new ZipCode("23456", "4455"));
		return a;
	}

	/*
	 * Setting data for expected rip object for the cure letter created above.
	 */
	public static CureLetterRipObject createExpectedCureLetterRipObject() {
		CureLetterRipObject ripObject = new CureLetterRipObject();
		ripObject.setAddress1("address 1");
		ripObject.setAddress2("address 2");
		ripObject.setAddress3("address 3");
		ripObject.setAddress4("address 4");
		ripObject.setAddress5("address 5");
		ripObject.setAddress6("address 6");
		ripObject.setCity("CITY");
		ripObject.setStateCode("SC");
		ripObject.setZipCode("23456");
		ripObject.setZipExt("4455");
		ripObject.setCreatedByUser(AccountContext.getCurrentUsername());
		ripObject.setReferenceNo("100000001");
		ripObject.setCorrelationId(new Long(123));
		ripObject.setRegistration1("RegistrationName1");
		ripObject.setRegistration2("RegistrationName2");
		ripObject.setRegistration3("RegistrationName3");
		ripObject.setRegistration4("RegistrationName4");
		ripObject.setRegistration5("RegistrationName5");
		ripObject.setRegistration6("RegistrationName6");
		ripObject.setSpecialInstructions("");
		ripObject.setLetterCode("101");
		ripObject.setWorkType("workType1");
		return ripObject;

	}

}
