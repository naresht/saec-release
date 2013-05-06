package com.bfds.wss.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import com.bfds.wss.domain.reference.QuestionGroup;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@Audited
public class ClaimUserResponseGroup extends UserResponseGroup implements java.io.Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Claimant_Claim_fk", nullable = false)
	private ClaimantClaim claimantClaim;
	
    @OneToMany(mappedBy = "claimUserResponseGroup", cascade = CascadeType.ALL)
    private List<ClaimantUserResponses> claimUserResponses = new ArrayList<ClaimantUserResponses>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "claim_proof_fk", nullable = true)
    private ClaimProof proof;

    @Override
    public List<UserResponse> getUserResponses() {
        List<UserResponse> ret = Lists.newArrayList();
        ret.addAll(claimUserResponses);
        return java.util.Collections.unmodifiableList(ret);
    }

    @Override
    public void addUserResponse(UserResponse userResponse) {
        Preconditions.checkState(userResponse instanceof ClaimantUserResponses, "userResponse %s must be of type ClaimantUserResponses.", userResponse);
        ClaimantUserResponses entryUserResponses = (ClaimantUserResponses)userResponse;
        entryUserResponses.setClaimUserResponseGroup(this);
        this.getClaimUserResponses().add(entryUserResponses);
    }

    @Override
    public void removeUserResponse(UserResponse userResponse) {
        Preconditions.checkState(userResponse instanceof ClaimantUserResponses, "userResponse %s must be of type ClaimantUserResponses.", userResponse);
        ClaimantUserResponses entryUserResponses = (ClaimantUserResponses)userResponse;
        entryUserResponses.setClaimUserResponseGroup(null);
        this.getClaimUserResponses().remove(entryUserResponses);
    }

}
