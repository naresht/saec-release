// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.claim;

import com.bfds.saec.claim.ClaimAnswer;
import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.reference.AdditionalQuestions;
import com.bfds.wss.domain.reference.AdditionalQuestionsResponses;
import java.util.Date;
import java.util.List;

privileged aspect ClaimAnswer_Roo_JavaBean {
    
    public Long ClaimAnswer.getId() {
        return this.id;
    }
    
    public void ClaimAnswer.setId(Long id) {
        this.id = id;
    }
    
    public AdditionalQuestions ClaimAnswer.getQuestion() {
        return this.question;
    }
    
    public String ClaimAnswer.getTextAnswer() {
        return this.textAnswer;
    }
    
    public void ClaimAnswer.setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }
    
    public Date ClaimAnswer.getDateAnswer() {
        return this.dateAnswer;
    }
    
    public void ClaimAnswer.setDateAnswer(Date dateAnswer) {
        this.dateAnswer = dateAnswer;
    }
    
    public List<AdditionalQuestionsResponses> ClaimAnswer.getAnswerChoices() {
        return this.answerChoices;
    }
    
    public void ClaimAnswer.setAnswerChoices(List<AdditionalQuestionsResponses> answerChoices) {
        this.answerChoices = answerChoices;
    }
    
    public short ClaimAnswer.getRowId() {
        return this.rowId;
    }
    
    public void ClaimAnswer.setRowId(short rowId) {
        this.rowId = rowId;
    }
    
    public ClaimProof ClaimAnswer.getClaimProof() {
        return this.claimProof;
    }
    
    public void ClaimAnswer.setClaimProof(ClaimProof claimProof) {
        this.claimProof = claimProof;
    }
    
}
