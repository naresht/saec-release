package com.bfds.saec.batch.in.ncoa;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.in.ncoa_link_record.NCOALinkRec;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class NcoaInboundRecordData {

    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager entityManager;

	@Transactional(value = "batchFilesTransactionManager")
	public void create() {
		NCOALinkRec r = new NCOALinkRec();
		r.setClientSpecificData("Sample C100000001lient Specific Data                                                              ");
		r.setBusinessNameIndicator("B");
		r.setBusinessNameLocation("N");
		r.setName("Giridhar Sirigiri");
		r.setOriginalPrimaryAddress("Chinnachowk");
		r.setOriginalSecondaryAddress("NGOColony");
		r.setCity("Kadapa");
		r.setState("AP");
		r.setZipCode("51600");
		r.setZipExtn("2");
		r.setOldStandardPrimaryAddress("NGOClony kadapa 516002");
		r.setOldStandardSecondaryAddress("Chinnachowk Nellore 500123");
		r.setOldStandardCity("Hyderabad");
		r.setOldStandardState("AP");
		r.setOldStandardZipCode("50003");
		r.setOldStandardZipExtn("2");
		r.setZipCorrectionReasonCode("A");
		r.setZipCorrectionActionCode("A");
		r.setOverallProbableCorrectness("0");
		r.setAlternateAddressSchemeIndicator("U");
		r.setNewStandardFirstName("Giridhar");
		r.setNewStandardSurName("Sirigiri");
		r.setNewStandardPrimaryAddress("Test Primary Address");
		r.setNewStandardSecondaryAddress("TestSecondary Address");
		r.setNewStandardCity("TestCity");
		r.setNewStandardState("TS");
		r.setNewStandardZipCode("21453");
		r.setNewStandardZipExtn("44");
		r.setDda("DDA-1");

        entityManager.persist(r);
		
		r = new NCOALinkRec();
		r.setClientSpecificData("Sample C100000002lient Specific Data                                                              ");
		r.setBusinessNameIndicator("B");
		r.setBusinessNameLocation("N");
		r.setName("Giridhar Sirigiri");
		r.setOriginalPrimaryAddress("Chinnachowk");
		r.setOriginalSecondaryAddress("NGOColony");
		r.setCity("Kadapa");
		r.setState("AP");
		r.setZipCode("51600");
		r.setZipExtn("2");
		r.setOldStandardPrimaryAddress("NGOClony kadapa 516002");
		r.setOldStandardSecondaryAddress("Chinnachowk Nellore 500123");
		r.setOldStandardCity("Hyderabad");
		r.setOldStandardState("AP");
		r.setOldStandardZipCode("50003");
		r.setOldStandardZipExtn("2");
		r.setZipCorrectionReasonCode("A");
		r.setZipCorrectionActionCode("A");
		r.setOverallProbableCorrectness(" ");
		r.setAlternateAddressSchemeIndicator("U");
		r.setNewStandardFirstName("Giridhar");
		r.setNewStandardSurName("Sirigiri");
		r.setNewStandardPrimaryAddress("Test Primary Address");
		r.setNewStandardSecondaryAddress("");
		r.setNewStandardCity("TestCity");
		r.setNewStandardState("TS");
		r.setNewStandardZipCode("21453");
		r.setNewStandardZipExtn("44");
		r.setDda("DDA-1");

        entityManager.persist(r);

        entityManager.flush();
	}
}
