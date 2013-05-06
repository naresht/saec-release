package com.bfds.saec.claim;

import com.bfds.wss.domain.UserResponse;
import com.bfds.wss.domain.UserResponseGroup;
import com.bfds.wss.domain.reference.QuestionGroup;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.roo.addon.javabean.RooJavaBean;

import java.util.List;
import java.util.Set;

@RooJavaBean
public abstract class ClaimQuestionnaireBuilder {
    protected List<QuestionGroup> questionGroups;

    private List<UserResponseGroup> responseGroups;
    /**
     * The ids of {@link com.bfds.wss.domain.reference.QuestionGroup}s that already have a {@link com.bfds.wss.domain.ClaimEntryUserResponseGroup} persisted.
     */
    protected Set<Long> questionGroupsThatHaveResponseGroups = Sets.newHashSet();

    public ClaimQuestionnaire build() {

        Preconditions.checkNotNull(questionGroups, "questionGroups is null");
        Preconditions.checkState(questionGroups.size() > 0, "questionGroups is empty");

        ClaimQuestionnaire ret = new ClaimQuestionnaire();
        processResponseGroups(ret);

        processQuestionGroups(ret);

        ret.prepareForRendering();
        return ret;
    }

    protected void processResponseGroups(final ClaimQuestionnaire ret) {
        if(responseGroups == null || responseGroups.size() == 0) {
            return;
        }
        for(UserResponseGroup responseGroup : responseGroups) {
            List<ClaimAnswer> answers = processResponseGroupAnswers(responseGroup);
            ClaimAnswerGroup answerGroup = new ClaimAnswerGroup();
            answerGroup.setId(responseGroup.getId());
            answerGroup.setQuestionGroup(responseGroup.getQuestionGroup());
            questionGroupsThatHaveResponseGroups.add(responseGroup.getQuestionGroup().getId());
            answerGroup.setAnswers(answers);
            postConstruct(answerGroup, responseGroup);
            ret.getAnswerGroups().add(answerGroup);
            if(answerGroup.isMultiOccuring()) {
                answerGroup.buildRows();
            }
            answerGroup.validate();
        }
    }

    protected void postConstruct(ClaimAnswerGroup newClaimAnswerGroup, UserResponseGroup userResponseGroup) {

    }

    private List<ClaimAnswer> processResponseGroupAnswers(UserResponseGroup responseGroup) {
        List<ClaimAnswer> answers = Lists.newArrayList();
        for(UserResponse response : responseGroup.getUserResponses()) {
            ClaimAnswer answer = new ClaimAnswer();
            answer.setId(response.getId());
            answer.setQuestion(response.getAdditionalQuestions());
            answer.setRowId(response.getRowId());
            answer.setTextAnswer(response.getResponseText());
            answer.setDateAnswer(response.getResponseDate());
            answer.getAnswerChoices().addAll(response.getAdditionalQuestions().getAdditionalQuestionsResponses());
            postConstruct(answer, response);
            answers.add(answer);
        }
        return answers;
    }

    protected void postConstruct(ClaimAnswer newClaimAnswer, UserResponse userResponse) {

    }

    protected void processQuestionGroups(final ClaimQuestionnaire ret) {
        if(questionGroups == null || questionGroups.size() == 0) {
            return;
        }
        for(QuestionGroup questionGroup : questionGroups) {
            if(questionGroupsThatHaveResponseGroups.contains(questionGroup.getId())) {
                continue;
            }
            final ClaimAnswerGroup claimAnswerGroup = ClaimAnswerGroup.build(questionGroup);
            postConstruct(claimAnswerGroup, questionGroup);

            for(ClaimAnswer claimAnswer : claimAnswerGroup.getAnswers()) {
                 postConstruct(claimAnswer, questionGroup);
            }
            ret.getAnswerGroups().add(claimAnswerGroup);
        }
    }

    protected void postConstruct(ClaimAnswerGroup newClaimAnswerGroup, QuestionGroup questionGroup) {

    }

    protected void postConstruct(ClaimAnswer newClaimAnswer, QuestionGroup questionGroup) {

    }
}
