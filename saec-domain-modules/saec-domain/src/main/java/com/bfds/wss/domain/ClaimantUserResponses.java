package com.bfds.wss.domain;

import java.util.Date;

import javax.persistence.*;

import com.bfds.wss.domain.reference.AdditionalQuestionsResponses;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@Audited
public class ClaimantUserResponses extends UserResponse implements java.io.Serializable {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Claim_User_Response_Group_fk", nullable = false)
	private ClaimUserResponseGroup claimUserResponseGroup;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "claim_proof_fk", nullable = true)
    private ClaimProof proof;

    @Override
    public UserResponseGroup getUserResponseGroup() {
        return claimUserResponseGroup;
    }

}
