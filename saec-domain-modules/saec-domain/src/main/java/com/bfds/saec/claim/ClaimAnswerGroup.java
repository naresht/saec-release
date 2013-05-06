package com.bfds.saec.claim;

import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.reference.AdditionalQuestions;
import com.bfds.wss.domain.reference.QuestionGroup;
import com.bfds.wss.domain.reference.QuestionGroupDisplayCode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.*;

/**
 *  A Grouping of {@link ClaimAnswer}s that are answers to {@link AdditionalQuestions} belonging to a {@link QuestionGroup}.
 *  Effectively there is a ClaimAnswerGroup fro every {@link QuestionGroup}.
 *
 *  In the case of a multi-occuring {@link QuestionGroup} there can be multiple {@link ClaimAnswer}s to the same {@link AdditionalQuestions}.
 *  Such {@link ClaimAnswer}s are further grouped by their {@link ClaimAnswer#rowId}.
 */
@RooJavaBean
@RooToString
public class ClaimAnswerGroup implements Serializable{

    /**
     * The id is used to identify the corresponding persistent entity. Currently we have following persistent entity types.
     * {@link com.bfds.wss.domain.ClaimEntryUserResponseGroup}
     * {@link com.bfds.wss.domain.ClaimUserResponseGroup}
     *
     * A value of '0' indicates that this does not have a corresponding persistent entity.
     */
    private Long id;

    /**
     * The corresponding question group.
     */
    private QuestionGroup questionGroup;

    private List<ClaimAnswer> answers = Lists.newArrayList();

    /**
     * {@link #answers} grouped by {@link ClaimAnswer#rowId}. It is important to note that this is built form
     * the {@link #answers} property and mut not be directly updated.
     */
    Map<Integer, Row> rows = Maps.newTreeMap();

    private ClaimProof claimProof;


    public List<Row> getRowList() {
        List<Row> ret = Lists.newArrayList();
        ret.addAll(rows.values());
        return ret;
    }
    /**
     * Factory to create a new ClaimAnswerGroup and it's constituent {@link ClaimAnswer}s for the given questionGroup.
     * The {@link ClaimAnswer}s are created from {@link QuestionGroup#additionalQuestions}
     * @param questionGroup
     * @return A new ClaimAnswerGroup for the given questionGroup.
     */
    public static ClaimAnswerGroup build(QuestionGroup questionGroup) {
        final List<ClaimAnswer> answers = processQuestionGroupAnswers(questionGroup);
        ClaimAnswerGroup answerGroup = new ClaimAnswerGroup();
        answerGroup.setQuestionGroup(questionGroup);
        answerGroup.setAnswers(answers);
        answerGroup.validate();
        if(answerGroup.isMultiOccuring()) {
            answerGroup.buildRows();
        }
        return answerGroup;
    }

    /**
     * Creates {@link ClaimAnswer}s for the given {@link QuestionGroup}.
     *
     * @see {@link #build(com.bfds.wss.domain.reference.QuestionGroup)}
     *
     * @param questionGroup - The {@link QuestionGroup} for which {@link ClaimAnswer}s are to be created.
     * @return
     */
    private static List<ClaimAnswer> processQuestionGroupAnswers(QuestionGroup questionGroup) {
        List<ClaimAnswer> answers = Lists.newArrayList();
        for(AdditionalQuestions question : questionGroup.getAdditionalQuestions()) {
            ClaimAnswer answer = new ClaimAnswer();
            answer.setQuestion(question);
            answer.getAnswerChoices().addAll(question.getAdditionalQuestionsResponses());
            answer.setClaimProof(ClaimProof.newClaimProof());
            answers.add(answer);
        }
        return answers;
    }

    public boolean isMultiOccuring() {
        return QuestionGroupDisplayCode.GROUP.equals(questionGroup.getGroupDisplayCode());
    }

    /**
     * Groups {@link #answers} by their {@link ClaimAnswer#rowId} for convenient querying of {@link ClaimAnswer}s belonging to a
     * particual row.
     * Applicable to multi-occuring ClaimAnswerGroups only.
     */
    public void buildRows() {
        if(!this.isMultiOccuring()) {
            throw new UnsupportedOperationException("rowIds are only applicable to multi-occuring ClaimAnswerGroup");
        }
        this.rows = Row.buildRows(this.answers);
    }

    /**
     * Adds a new {@link Row} of {@link ClaimAnswer}s. to both {@link #answers} and also {@link #rows}.
     * Remember that {@link #rows} should always be derivable from {@link #answers}.
     *
     * Applicable to multi-occuring ClaimAnswerGroups only.
     *
     * @return The newly create {@link Row}.
     */
    public Row addRow() {
        if(!this.isMultiOccuring()) {
            throw new UnsupportedOperationException("Rows can only be added to a multi-occuring ClaimAnswerGroup");
        }
        ClaimAnswerGroup tempGroup =  build(this.getQuestionGroup());
        short newRowId = (short) (getMaxRowId() + 1);
        for(ClaimAnswer answer : tempGroup.getAnswers()) {
            answer.setRowId(newRowId);
        }
        this.getAnswers().addAll(tempGroup.getAnswers());
        final Row newRow = new Row(newRowId, tempGroup.getAnswers());
        newRow.prepareForRendering();
        rows.put((int)newRowId, newRow);
        return rows.get((int)newRowId);
    }

    /**
     * Get a {@link Row} given it's id.
     *
     * @param rowId
     * @return the {@link Row} or null if there is no {@link Row} with the given rowId.
     */
    public Row getRow(int rowId) {
        if(!this.isMultiOccuring()) {
            throw new UnsupportedOperationException("Rows can only be queried on a multi-occuring ClaimAnswerGroup");
        }
        return rows.get(rowId);
    }


    public void removeRow(int rowId) {
        rows.remove(rowId);
        for(Iterator<ClaimAnswer> itr = answers.iterator(); itr.hasNext();) {
            ClaimAnswer answer = itr.next();
            if(answer.getRowId() ==  rowId) {
                itr.remove();
            }
        }
    }

    /**
     * Get the max {@link ClaimAnswer#rowId} of the {@link ClaimAnswer}s in {@link #answers}
     * @return the largest rowId. -1 if there are no rows.
     */
    public int getMaxRowId() {
        if(!this.isMultiOccuring()) {
            throw new UnsupportedOperationException("rowIds are only applicable to multi-occuring ClaimAnswerGroup");
        }
        short ret = -1;
        for(ClaimAnswer answer : answers) {
            if(ret < answer.getRowId()) {
                ret = answer.getRowId();
            }
        }
        return ret;
    }

    /**
     *  Prepare for rendering. Following changes are made
     *  1. Sort all {@link ClaimAnswer}s
     *  3.
     */
    public void prepareForRendering() {
        for(ClaimAnswer answer : answers) {
            answer.prepareForRendering();
        }
        if(this.isMultiOccuring()) {
            deleteEmptyRows();
            this.addRow();
        }

        java.util.Collections.sort(this.getAnswers(), new Comparator<ClaimAnswer>() {
            @Override
            public int compare(ClaimAnswer o1, ClaimAnswer o2) {
                int ret =  o1.getRowId() - o2.getRowId();
                if(ret == 0) {
                    ret = o1.getQuestion().getSequence() - o2.getQuestion().getSequence();
                }
                return ret;
            }
        });
    }

    private void deleteEmptyRows() {
        List<Integer> emptyRows = Lists.newArrayList();
        for(Row row : rows.values()) {
            row.prepareForRendering();
            if(row.isEmpty()) {
                emptyRows.add(row.getRowId());

            }
        }
        if(emptyRows.size() >0) {
            for(int rowId : emptyRows) {
                removeRow(rowId);
            }
        }
    }

    public void prepareForPersistence() {
        if(this.isMultiOccuring()) {
            deleteEmptyRows();
        }
        for(ClaimAnswer answer : answers) {
            answer.prepareForPersistence();
        }
    }

    public void setQuestionGroup(QuestionGroup questionGroup) {
        Preconditions.checkNotNull(questionGroup, "questionGroup is null");
        this.questionGroup = questionGroup;
    }


    public void validate() {
        if(this.isMultiOccuring()) {
            if(this.getAnswers().size() % this.getQuestionGroup().getAdditionalQuestions().size() != 0) {
                throw new IllegalStateException(String.format("The number of answers(%s) in a multi-occuring ClaimAnswerGroup must be a multiple to the number of questions(%s) in its corresponding QuestionGroup",
                        this.getAnswers().size(), this.getQuestionGroup().getAdditionalQuestions().size()));
            }
        } else {
            if(this.getAnswers().size() != this.getQuestionGroup().getAdditionalQuestions().size()) {
                throw new IllegalStateException(String.format("The number of answers(%s) in a non-multi-occuring ClaimAnswerGroup must be equal to the number of questions(%s) in its corresponding QuestionGroup",
                        this.getAnswers().size(), this.getQuestionGroup().getAdditionalQuestions().size()));
            }
        }
    }

    /**
     * A grouping of {@link ClaimAnswer}s that have the same {@link ClaimAnswer#rowId}.
     */
    public static class Row implements Serializable {
        private final List<ClaimAnswer> answers;
        private final int rowId;
        private String displayResponsesDescription;

        public static Map<Integer, Row> buildRows(List<ClaimAnswer> answers) {
            Map<Integer, Row> rows = Maps.newHashMap();
            Map<Integer, List<ClaimAnswer>> answersGroupedByRow = Maps.newHashMap();
            for(ClaimAnswer answer : answers) {
               List<ClaimAnswer>  rowAnswers = answersGroupedByRow.get((int)answer.getRowId());
               if(rowAnswers == null) {
                   rowAnswers = Lists.newArrayList(answer);
                   answersGroupedByRow.put((int)answer.getRowId(), rowAnswers);
               } else {
                   rowAnswers.add(answer);
               }
            }
            for(Map.Entry<Integer, List<ClaimAnswer>> entry : answersGroupedByRow.entrySet()) {
                rows.put(entry.getKey(), new Row(entry.getKey(), entry.getValue()));
            }
            return rows;
        }

        public Row(int rowId, List<ClaimAnswer> answers) {
            Preconditions.checkArgument(answers!= null && answers.size() > 0, "answers is empty or null");
            Preconditions.checkArgument(rowId >= 0, "rowId cannot be less tah 0: %s", rowId);
            this.rowId = rowId;
            this.answers = Collections.unmodifiableList(answers);
        }

        public void prepareForRendering() {
            if(this.isEmpty()) {
                displayResponsesDescription = "Add New";
                return;
            }
            StringBuilder sb = new StringBuilder();
            for(ClaimAnswer answer : answers) {
                if (answer.getQuestion().getKeyColumnSeq() != null && answer.getQuestion().getKeyColumnSeq() > 0) {
                    if(sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(answer.getQuestion().getQuestionDescription()) ;
                    sb.append(":") ;
                    if(StringUtils.hasText(answer.getTextAnswer())) {
                        sb.append(answer.getTextAnswer()) ;
                    }
                }
            }
            displayResponsesDescription = sb.toString();
        }

        // Are all the answeres in the Row empty.
        public boolean isEmpty() {
            for(ClaimAnswer answer : this.answers) {
               if(!answer.isEmpty()) {
                    return false;
               }
            }
            return true;
        }

        public List<ClaimAnswer> getAnswers() {
            return answers;
        }

        public int getRowId() {
            return rowId;
        }

        public String getDisplayResponsesDescription() {
            return displayResponsesDescription;
        }
    }
}
