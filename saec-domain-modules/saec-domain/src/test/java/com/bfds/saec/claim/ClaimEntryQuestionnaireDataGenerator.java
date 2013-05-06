package com.bfds.saec.claim;

import com.bfds.wss.domain.reference.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;


@Component
public class ClaimEntryQuestionnaireDataGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    public void loadQuestionGroups() {

        QuestionGroup qg = new QuestionGroup();
        qg.setGroupDescription("Medical Plan");
        qg.setGroupDisplayCode(QuestionGroupDisplayCode.ROW) ;
        qg.setSequence(new Byte("1"));
        qg.persist();

        qg = new QuestionGroup();
        qg.setGroupDescription("Beneficiaries");
        qg.setGroupDisplayCode(QuestionGroupDisplayCode.GROUP) ;
        qg.setSequence(new Byte("2"));
        qg.setNoOfColumnsToDisplay(3) ;
        qg.persist();

        qg.flush();
        qg.clear();
    }

    public QuestionGroup getQuestionGroupByDescription(String groupDescription) {
        QuestionGroup qg = entityManager.createQuery("from com.bfds.wss.domain.reference.QuestionGroup g where g.groupDescription = :groupDescription", QuestionGroup.class)
                .setParameter("groupDescription", groupDescription).getSingleResult();
        return qg;
    }

    public void loadAdditionalQuestions() {
        QuestionGroup qg  =  getQuestionGroupByDescription("Medical Plan");
        AdditionalQuestions aq = new AdditionalQuestions();
        aq.setQuestionDescription("Date of Birth");
        aq.setSequence(new Byte("2"));
        aq.setRequired(true) ;
        aq.setQuestionGroup(qg);
        aq.setResponseDisplayCode(ResponseDisplayCode.DATE) ;
        loadAdditionalResponsesDOB(aq);
        aq.persist();

        aq = new AdditionalQuestions();
        aq.setQuestionDescription("Sex");
        aq.setSequence(new Byte("3"));
        aq.setQuestionGroup(qg);
        aq.setResponseDisplayCode(ResponseDisplayCode.SELECTION) ;
        loadAdditionalResponsesSex(aq);
        aq.persist();

        aq = new AdditionalQuestions();
        aq.setQuestionDescription("Name");
        aq.setSequence(new Byte("1"));
        aq.setQuestionGroup(qg);
        aq.setResponseDisplayCode(ResponseDisplayCode.TEXT) ;
        loadAdditionalResponsesText(aq, "PlanID") ;
        aq.persist();

        qg  =  getQuestionGroupByDescription("Beneficiaries");
        aq = new AdditionalQuestions();
        aq.setQuestionDescription("Plan ID");
        aq.setSequence(new Byte("2"));
        aq.setQuestionGroup(qg);
        aq.setResponseDisplayCode(ResponseDisplayCode.TEXT) ;
        loadAdditionalResponsesText(aq, "PlanID") ;
        aq.persist();

        aq = new AdditionalQuestions();
        aq.setQuestionDescription("Date Eligibility Change");
        aq.setQuestionGroup(qg);
        aq.setSequence(new Byte("1"));
        aq.setQuestionGroup(qg);
        aq.setResponseDisplayCode(ResponseDisplayCode.DATE) ;
        loadAdditionalResponsesDEC(aq);
        aq.persist();

        aq.flush();
        aq.clear();
    }
    private void loadAdditionalResponsesDOB(AdditionalQuestions aq) {

        List<AdditionalQuestionsResponses> additionalQuestionsResponseSet = aq
                .getAdditionalQuestionsResponses();

        AdditionalQuestionsResponses aqr = new AdditionalQuestionsResponses();
        aqr.setAdditionalQuestions(aq);
        aqr.setResponseDescription("Date of Birth");
        aqr.setProofRequiredInd(true);
        aqr.setSequence(new Byte("1"));
        additionalQuestionsResponseSet.add(aqr);

    }

    private void loadAdditionalResponsesSex(AdditionalQuestions aq) {

        List<AdditionalQuestionsResponses> additionalQuestionsResponseSet = aq
                .getAdditionalQuestionsResponses();


        AdditionalQuestionsResponses aqr = new AdditionalQuestionsResponses();
        aqr.setAdditionalQuestions(aq);
        aqr.setResponseDescription("Male");
        aqr.setProofRequiredInd(false);
        aqr.setSequence(new Byte("1"));
        additionalQuestionsResponseSet.add(aqr);

        AdditionalQuestionsResponses aqr1 = new AdditionalQuestionsResponses();
        aqr1.setAdditionalQuestions(aq);
        aqr1.setResponseDescription("Female");
        aqr1.setProofRequiredInd(false);
        aqr1.setSequence(new Byte("2"));
        additionalQuestionsResponseSet.add(aqr1);

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

    private void loadAdditionalResponsesDEC(AdditionalQuestions aq) {
        List<AdditionalQuestionsResponses> additionalQuestionsResponseSet = aq
                .getAdditionalQuestionsResponses();
        AdditionalQuestionsResponses aqr1 = new AdditionalQuestionsResponses();
        aqr1.setAdditionalQuestions(aq);
        aqr1.setResponseDescription("Date Eligibility Change");
        aqr1.setProofRequiredInd(false);
        aqr1.setSequence(new Byte("1"));
        additionalQuestionsResponseSet.add(aqr1);
    }

}
