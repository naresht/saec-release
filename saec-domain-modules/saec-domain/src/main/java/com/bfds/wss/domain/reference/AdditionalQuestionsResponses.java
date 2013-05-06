package com.bfds.wss.domain.reference;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
public class AdditionalQuestionsResponses implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Additional_Questions_fk", nullable = false)
	private AdditionalQuestions additionalQuestions;

    /**
     * Narrative description of the response.
     */
	@Column(nullable = false)
	private String responseDescription;

    /**
     * Indicates whether or not the response requires supporting documentation.
     */
	@Column(nullable = false)
	private boolean proofRequiredInd;

    /**
     * The order in which this response should be displayed with respect to other responses to {@link #additionalQuestions}
     */
	@Column(nullable=true)
	private Byte sequence;

}
