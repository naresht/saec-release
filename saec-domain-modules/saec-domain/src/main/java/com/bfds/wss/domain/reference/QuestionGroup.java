package com.bfds.wss.domain.reference;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@RooToString
public class QuestionGroup implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @NotNull
    @Column(nullable = false)
	private String groupDescription;

    /**
     * @see {@link QuestionGroupDisplayCode}
     */
    @Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 5)
	private QuestionGroupDisplayCode groupDisplayCode;

    /**
     * TODO: What is this ?
     */
    @Null
    @Column(nullable = true)
    private Byte sequence;

    /**
     * If this group contains 10  {@link AdditionalQuestions} and noOfColumnsToDisplay = 5, then questions
     * will be displayed in 2 rows.
     */
    @Null
    @Column(nullable=true)
    private int noOfColumnsToDisplay ;

    /**
     * Do {@link AdditionalQuestions} that belong to this group require a proof.
     */
    @Column(nullable = false)
    private boolean proofRequiredInd;
    
    @OneToMany(mappedBy = "questionGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AdditionalQuestions> additionalQuestions = new ArrayList<AdditionalQuestions>();
	
}
