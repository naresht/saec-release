package com.bfds.saec.claim;


import com.bfds.wss.domain.ClaimEntry;
import com.bfds.wss.domain.ClaimEntryUserResponseGroup;
import com.bfds.wss.domain.UserResponseGroup;
import com.bfds.wss.domain.reference.QuestionGroup;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
/*
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
@Transactional*/
public class ClaimEntryQuestionnaireBuilderTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    ClaimEntryQuestionnaireDataGenerator dataGenerator;

    @Before
    public void before() {
        dataGenerator.loadQuestionGroups() ;
        dataGenerator.loadAdditionalQuestions();

        ClaimEntry claimEntry = new ClaimEntry();
        claimEntry.setClaimIdentifier("WEB-101");
        claimEntry.setControlNumber(1234);
        claimEntry.setFirstName("aaaaaa");
        claimEntry.setAddress1("skfjsdfl asflksfj ");
        claimEntry.persist();
        claimEntry.flush();
        claimEntry.clear();

    }

    /**
     *  Create a new Questionnaire. A new Questionnaire is created only from a list of {@link QuestionGroup}s
     */
 //   @Test
    public void testNewNonMultiOccuringQuestionnaire() {
        final ClaimEntryQuestionnaireBuilder builder = new ClaimEntryQuestionnaireBuilder();
        builder.setQuestionGroups(Lists.newArrayList(dataGenerator.getQuestionGroupByDescription("Medical Plan")));
        final ClaimQuestionnaire questionnaire = builder.build();
        assertThat(questionnaire.getAnswerGroups()).hasSize(1);
        final ClaimAnswerGroup answerGroup = questionnaire.getAnswerGroups().get(0);
        assertThat(answerGroup.getQuestionGroup().getGroupDescription()).isEqualTo("Medical Plan");

        assertThat(answerGroup.isMultiOccuring()).isFalse();

        List<ClaimAnswer> answers = answerGroup.getAnswers();

        assertThat(answers).hasSize(3);

      //  assertThat(answers).onProperty("id").isNull();
        assertThat(answers).onProperty("question").onProperty("questionDescription").containsSequence("Name", "Date of Birth", "Sex");

    }

    /**
     *  Create a new Questionnaire. A new Questionnaire is created only from a list of {@link QuestionGroup}s
     */
  //  @Test
    public void testNewMultiOccuringQuestionnaire() {
        final ClaimEntryQuestionnaireBuilder builder = new ClaimEntryQuestionnaireBuilder();
        builder.setQuestionGroups(Lists.newArrayList(dataGenerator.getQuestionGroupByDescription("Beneficiaries")));
        final ClaimQuestionnaire questionnaire = builder.build();
        assertThat(questionnaire.getAnswerGroups()).hasSize(1);
        final ClaimAnswerGroup answerGroup = questionnaire.getAnswerGroups().get(0);
        assertThat(answerGroup.getQuestionGroup().getGroupDescription()).isEqualTo("Beneficiaries");

        assertThat(answerGroup.isMultiOccuring()).isTrue();

        List<ClaimAnswer> answers = answerGroup.getAnswers();

        assertThat(answers).hasSize(2);

        //  assertThat(answers).onProperty("id").isNull();
        assertThat(answers).onProperty("question").onProperty("questionDescription").containsSequence("Date Eligibility Change", "Plan ID");

    }

    /**
     *
     */
 //   @Test
    public void addNewRowsToMultiOccuringQuestionnaire() {
        final ClaimEntryQuestionnaireBuilder builder = new ClaimEntryQuestionnaireBuilder();
        builder.setQuestionGroups(Lists.newArrayList(dataGenerator.getQuestionGroupByDescription("Beneficiaries")));
        final ClaimQuestionnaire questionnaire = builder.build();
        final ClaimAnswerGroup answerGroup = questionnaire.getAnswerGroups().get(0);
        assertThat(answerGroup.getAnswers()).hasSize(2);
        assertThat(answerGroup.getRow(0).getAnswers()).hasSize(2);
        assertThat(answerGroup.getMaxRowId()).isEqualTo((short)0);

        assertThat(answerGroup.addRow().getRowId()).isEqualTo(1);
        assertThat(answerGroup.getAnswers()).hasSize(4);
        assertThat(answerGroup.getRow(1).getAnswers()).hasSize(2);

        assertThat(answerGroup.getMaxRowId()).isEqualTo((short)1);
    }

    /**
     *
     */
 //   @Test
    public void removeRowsFromMultiOccuringQuestionnaire() {
        final ClaimEntryQuestionnaireBuilder builder = new ClaimEntryQuestionnaireBuilder();
        builder.setQuestionGroups(Lists.newArrayList(dataGenerator.getQuestionGroupByDescription("Beneficiaries")));
        final ClaimQuestionnaire questionnaire = builder.build();
        final ClaimAnswerGroup answerGroup = questionnaire.getAnswerGroups().get(0);
        answerGroup.addRow();
        answerGroup.addRow();
        assertThat(answerGroup.getRows().size()).isEqualTo(3);
        assertThat(answerGroup.getMaxRowId()).isEqualTo(2);
        answerGroup.removeRow(1);
        assertThat(answerGroup.getRows().size()).isEqualTo(2);

    }

    /**
     *  Load a saved {@link ClaimEntry} that has a single non-multi-occuring {@link ClaimEntryUserResponseGroup}.
     *  Create a {@link ClaimQuestionnaire} for the {@link ClaimEntry} and verify that the {@link ClaimQuestionnaire}
     *  has been built correctly.
     */
  //  @Test
    public void saveNonMultiOccuringQuestionnaire() {
        ClaimEntryQuestionnaireBuilder builder = new ClaimEntryQuestionnaireBuilder();
        builder.setQuestionGroups(Lists.newArrayList(dataGenerator.getQuestionGroupByDescription("Medical Plan")));
        ClaimQuestionnaire questionnaire = builder.build();

        questionnaire.getAnswerGroups().get(0).getAnswers().get(0).setTextAnswer("John");
        questionnaire.getAnswerGroups().get(0).getAnswers().get(1).setDateAnswer(new Date(112, 1, 1));
        questionnaire.getAnswerGroups().get(0).getAnswers().get(2).setTextAnswer("Male");

        ClaimQuestionnaireToUserResponseGroupMapper mapper = new ClaimQuestionnaireToClaimEntryUserResponseGroupMapper();

        List<UserResponseGroup>  userResponseGroups = mapper.map(questionnaire);
        saveClaimEntyWithUserResponses(ClaimEntry.findAllClaimEntrys().get(0), userResponseGroups);
        userResponseGroups.clear();
        userResponseGroups.addAll(ClaimEntry.findAllClaimEntrys().get(0).getClaimEntryUserResponseGroups());

        builder = new ClaimEntryQuestionnaireBuilder();
        builder.setQuestionGroups(Lists.newArrayList(dataGenerator.getQuestionGroupByDescription("Medical Plan")));
        builder.setResponseGroups(userResponseGroups);
        questionnaire = builder.build();

        assertThat(questionnaire.getAnswerGroups()).hasSize(1);
        ClaimAnswerGroup answerGroup = questionnaire.getAnswerGroups().get(0);

        assertThat(answerGroup.getQuestionGroup().getGroupDescription()).isEqualTo("Medical Plan");

        assertThat(answerGroup.getAnswers()).onProperty("question").onProperty("questionDescription").containsSequence("Name", "Date of Birth", "Sex");
        assertThat(answerGroup.getAnswers()).onProperty("textAnswer").contains("John", "Male");
        assertThat(answerGroup.getAnswers()).onProperty("dateAnswer").contains(new Date(112, 1, 1));

    }

  //  @Test
    public void saveMultiOccuringQuestionnaire() {
        ClaimEntryQuestionnaireBuilder builder = new ClaimEntryQuestionnaireBuilder();
        builder.setQuestionGroups(Lists.newArrayList(dataGenerator.getQuestionGroupByDescription("Beneficiaries")));
        ClaimQuestionnaire questionnaire = builder.build();

        questionnaire.getAnswerGroups().get(0).getRow(0).getAnswers().get(0).setTextAnswer("PlanID-1");
        questionnaire.getAnswerGroups().get(0).getRow(0).getAnswers().get(1).setDateAnswer(new Date(112, 1, 1));

        ClaimAnswerGroup.Row newRow = questionnaire.getAnswerGroups().get(0).addRow();

        newRow.getAnswers().get(0).setTextAnswer("PlanID-2");
        newRow.getAnswers().get(1).setDateAnswer(new Date(112, 2, 2));
        questionnaire.getAnswerGroups().get(0).addRow();
        ClaimQuestionnaireToUserResponseGroupMapper mapper = new ClaimQuestionnaireToClaimEntryUserResponseGroupMapper();

        List<UserResponseGroup>  userResponseGroups = mapper.map(questionnaire);
        saveClaimEntyWithUserResponses(ClaimEntry.findAllClaimEntrys().get(0), userResponseGroups);
        userResponseGroups.clear();
        userResponseGroups.addAll(ClaimEntry.findAllClaimEntrys().get(0).getClaimEntryUserResponseGroups());
        builder = new ClaimEntryQuestionnaireBuilder();
        builder.setQuestionGroups(Lists.newArrayList(dataGenerator.getQuestionGroupByDescription("Beneficiaries")));
        builder.setResponseGroups(userResponseGroups);
        questionnaire = builder.build();

        assertThat(questionnaire.getAnswerGroups()).hasSize(1);
        ClaimAnswerGroup answerGroup = questionnaire.getAnswerGroups().get(0);

        assertThat(answerGroup.getQuestionGroup().getGroupDescription()).isEqualTo("Beneficiaries");

        assertThat(answerGroup.getAnswers()).onProperty("question").onProperty("questionDescription").contains("Plan ID", "Date Eligibility Change");
        assertThat(answerGroup.getAnswers()).onProperty("textAnswer").contains("PlanID-1", "PlanID-2");
        assertThat(answerGroup.getAnswers()).onProperty("dateAnswer").contains(new Date(112, 1, 1), new Date(112, 2, 2));

        // Now delete an existing row then add another row and save.
        (new ClaimEntry()).flush();
        (new ClaimEntry()).clear();
        answerGroup.removeRow(0);
        newRow = answerGroup.addRow();

        newRow.getAnswers().get(0).setTextAnswer("PlanID-3");
        newRow.getAnswers().get(1).setDateAnswer(new Date(112, 3, 3));

        mapper = new ClaimQuestionnaireToClaimEntryUserResponseGroupMapper();

        userResponseGroups = mapper.map(questionnaire);
        saveClaimEntyWithUserResponses(ClaimEntry.findAllClaimEntrys().get(0), userResponseGroups);
        userResponseGroups.clear();


    }


    private void saveClaimEntyWithUserResponses(ClaimEntry claimEntry, List<UserResponseGroup> userResponseGroups) {
        claimEntry.getClaimEntryUserResponseGroups().clear();
        for(UserResponseGroup userResponseGroup : userResponseGroups) {
            ClaimEntryUserResponseGroup claimEntryUserResponseGroup = (ClaimEntryUserResponseGroup)userResponseGroup;
            claimEntryUserResponseGroup.setClaimEntry(claimEntry);
            claimEntry.getClaimEntryUserResponseGroups().add(claimEntryUserResponseGroup);
        }
        claimEntry.persist();
        claimEntry.flush();
        claimEntry.clear();
    }

}
