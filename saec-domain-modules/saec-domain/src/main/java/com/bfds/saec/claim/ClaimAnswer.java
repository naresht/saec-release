package com.bfds.saec.claim;

import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.reference.AdditionalQuestions;
import com.bfds.wss.domain.reference.AdditionalQuestionsResponses;
import com.bfds.wss.domain.reference.ResponseDisplayCode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A question an its textAnswer.
 */
@RooJavaBean
@RooToString
public class ClaimAnswer implements Serializable {

    /**
     * The id is used to identify the corresponding persistent entity. Currently we have following persistent entity types.
     * {@link com.bfds.wss.domain.ClaimEntryUserResponses}
     * {@link com.bfds.wss.domain.ClaimantUserResponses}
     *
     * A value of '0' indicates that this does not have a corresponding persistent entity.
     */
    private Long id;
    /**
     * The question to which this is the textAnswer.
     */
    private AdditionalQuestions question;

    /**
     * The answer if the question expects a written answer.
     */
    private String textAnswer;

    /**
     * The date answer if the question expects a date.
     */
    private Date dateAnswer;

    /**
     *
     */
    private List<AdditionalQuestionsResponses> answerChoices = Lists.newArrayList();

    /**
     * If a multi-occuring question, we include a row id
     */
    private short rowId;

    private ClaimProof claimProof;

    /**
     *
     * @return true if it is mandatory to have a valid textAnswer.
     */
    public boolean isMandatory() {
       return question.getRequired();
    }

    public void setQuestion(AdditionalQuestions question) {
        this.question = question;

    }

    /**
     *
     * @return true if all the {@link ClaimAnswer}s in this row do not have answers, else false.
     */
    public boolean isEmpty() {
        if(this.getQuestion().getResponseDisplayCode() == ResponseDisplayCode.TEXT) {
            return !StringUtils.hasText(this.getTextAnswer());
        } else if(this.getQuestion().getResponseDisplayCode() == ResponseDisplayCode.SELECTION) {
        	return (this.getTextAnswer() == null || this.getTextAnswer().equalsIgnoreCase("") || this.getTextAnswer().equalsIgnoreCase("Select One"));
    	} else if(this.getQuestion().getResponseDisplayCode() == ResponseDisplayCode.DATE) {
            return this.getDateAnswer() == null;
        } else {
            throw new IllegalStateException("Unsupported ResponseDisplayCode : " + this.getQuestion().getResponseDisplayCode());
        }
    }


    public void prepareForRendering() {
        if (this.getQuestion().getResponseDisplayCode() == ResponseDisplayCode.DATE && this.getDateAnswer() != null) {
            this.convertDateToString() ;
        }
        java.util.Collections.sort(this.answerChoices, new Comparator<AdditionalQuestionsResponses>() {
            @Override
            public int compare(AdditionalQuestionsResponses o1, AdditionalQuestionsResponses o2) {
                return o1.getSequence() - o2.getSequence();
            }
        });
    }

    public void prepareForPersistence() {
        if (this.getQuestion().getResponseDisplayCode() == ResponseDisplayCode.DATE && this.getDateAnswer() != null) {
            this.convertDateToString() ;
        }
    }

    public void convertDateToString() {
        if (this.getDateAnswer() != null) {
            SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("MM/dd/yyyy");
            StringBuilder nowYYYYMMDD = new StringBuilder( dateformatYYYYMMDD.format( getDateAnswer() ) );
            textAnswer = nowYYYYMMDD.toString() ;
        }
    }

}
