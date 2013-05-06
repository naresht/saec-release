package com.bfds.saec.batch.uploaded_doc_ripper;

import com.bfds.saec.domain.*;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.wss.domain.*;
import com.bfds.wss.domain.reference.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;


@Component
public class UploadedDocEventTestData extends com.bfds.saec.batch.util.DataGenerator {


    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

	@Transactional
	public void create() {
        createQuestionGroups();
        createClaimants();
    }

    public void createQuestionGroups() {

        QuestionGroup qg = new QuestionGroup();
        qg.setGroupDescription("Group - 1");
        qg.setGroupDisplayCode(QuestionGroupDisplayCode.ROW) ;
        qg.setSequence(new Byte("1"));
        qg.persist();

        AdditionalQuestions aq = new AdditionalQuestions();
        aq.setQuestionCode("EU-NO-UNITS");
        aq.setQuestionDescription("Name");
        aq.setSequence(new Byte("1"));
        aq.setQuestionGroup(qg);
        aq.setResponseDisplayCode(ResponseDisplayCode.TEXT) ;
        loadAdditionalResponsesText(aq, "PlanID") ;
        aq.persist();

        qg.flush();
        qg.clear();
    }

    private void loadAdditionalResponsesText(AdditionalQuestions aq, String label) {
        List<AdditionalQuestionsResponses> additionalQuestionsResponseSet = aq
                .getAdditionalQuestionsResponses();

        AdditionalQuestionsResponses aqr = new AdditionalQuestionsResponses();
        aqr.setAdditionalQuestions(aq);
        aqr.setResponseDescription(label);
        aqr.setProofRequiredInd(false);
        aqr.setSequence(new Byte("1"));
        additionalQuestionsResponseSet.add(aqr);
    }


    private void createClaimants() {
        for(int i = 0; i< 5; i++) {
            Claimant claimant  = getNewClaimantWithDefaults();
            ClaimantClaim cc = new ClaimantClaim() ;
            cc.setDateFiled(new Date()) ;
            cc.setEntryMethod(ClaimEntryMethod.WEB) ;
            claimant.getClaimantClaims().add(cc) ;
            cc.setStatus(ClaimStatus.PENDING) ;
            cc.setClaimant(claimant) ;
            addClaimUserResponseGroups(cc, i);
            claimant.persist();

            ClaimFileUploaded fileUploaded = new ClaimFileUploaded();
            fileUploaded.setAbsoluteFileName("xxx");
            fileUploaded.setUploadedFileName(ClaimFileUploaded.CLAIM_FORM_NAME);
            fileUploaded.setRipFileName("yyy");
            fileUploaded.setClaimantClaim(cc);
            ClaimFileLob lob = new ClaimFileLob();
            lob.setData(new byte[] {0, 0, 0});
            fileUploaded.setFileData(lob);
            fileUploaded.setDateUploaded(new Date());
            fileUploaded.persist();
            claimant.flush();
        }
    }

    private void  addClaimUserResponseGroups(ClaimantClaim cc, int index) {
        ClaimUserResponseGroup responseGroup = new ClaimUserResponseGroup();
        cc.getClaimUserResponseGroups().add(responseGroup);
        responseGroup.setClaimantClaim(cc);
        responseGroup.setQuestionGroup((QuestionGroup) find(QuestionGroup.class, "groupDescription", "Group - 1"));

        ClaimantUserResponses userResponse = new ClaimantUserResponses();
        responseGroup.getClaimUserResponses().add(userResponse);
        userResponse.setClaimUserResponseGroup(responseGroup);
        userResponse.setAdditionalQuestions((AdditionalQuestions) find(AdditionalQuestions.class, "questionCode", "EU-NO-UNITS"));
        userResponse.setResponseText(index % 2 == 0 ? "1" : "2");
        userResponse.setResponseDate(new Date());
    }

    private Object find(Class clazz, String propertyName, Object propertyValue) {
        Object ret = entityManager.createQuery("from "+clazz.getName()+" c where c."+propertyName+" = :"+propertyName, clazz)
                .setParameter(propertyName, propertyValue).getSingleResult();
        return ret;
    }

    public static Claimant getNewClaimantWithDefaults() {
        Claimant claimant = new Claimant();
        Contact contact = new Contact() ;
        ClaimantAddress address = new ClaimantAddress() ;
        address.setClaimant(claimant) ;
        // contact.setClaimant(claimant) ;
        claimant.setAddressOfRecord(address);
        claimant.setMailingAddressByType(AddressType.ADDRESS_OF_RECORD);
        claimant.getAddressOfRecord().setZipCode(new ZipCode());
        claimant.setPrimaryContact(contact);
        claimant.setClaimantRegistration(new ClaimantRegistration()) ;
        return claimant;
    }

}
