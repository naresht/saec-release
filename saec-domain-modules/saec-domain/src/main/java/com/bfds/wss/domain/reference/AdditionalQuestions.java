package com.bfds.wss.domain.reference;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@RooToString(excludeFields = "questionGroup")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"question_code"})})
public class AdditionalQuestions implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Column(name = "question_code", length = 20, nullable = false)
    private String questionCode ;
    /**
     * The {@link QuestionGroup} to which this AdditionalQuestions belongs.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Question_Group_fk", nullable = false)
    private QuestionGroup questionGroup;
    /**
     * Narrative Description of the question.
     */
    @NotNull
    @Column(nullable = false, length = 1000)
    private String questionDescription;

    /**
     * @see {@link ResponseDisplayCode}
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ResponseDisplayCode responseDisplayCode;

    /*	
     * The max length of the response. Applicable only if {@link #responseDisplayCode} =  {@link ResponseDisplayCode#TEXT}
     */
    @NotNull
    @Column(nullable = false)
    private Integer length = 20;

    /**
     * Is it mandatory to answer this question.
     */
    @Null
    @Column
    private Boolean required = Boolean.FALSE;;

    /**
     * The reletive order in which this question should be displayed with respect to other questions in the {@link QuestionGroup}
     */
    @Null
    @Column(nullable = true)
    private Byte sequence;

    @OneToMany(mappedBy = "additionalQuestions", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdditionalQuestionsResponses> additionalQuestionsResponses = new ArrayList<AdditionalQuestionsResponses>();

    /*
     * The reletive order in which this question should be displayed in the group summary with respect to other questions in the {@link QuestionGroup}
     */
    @Column
    private Short keyColumnSeq ;

    /**
     * Does this require a proof.
     */
    @Transient
    private boolean proofRequiredInd;

    /**
     * The width of the parent UI panel.
     */
    @NotNull
    @Column(nullable = false)
    private Integer displaySize = 18;

    @PostLoad
    public void updateResponses() {		
        for (AdditionalQuestionsResponses aqr : this.additionalQuestionsResponses)
        {
            if (aqr.isProofRequiredInd()) {
                this.proofRequiredInd = true ;
            }
        }
    }

}
