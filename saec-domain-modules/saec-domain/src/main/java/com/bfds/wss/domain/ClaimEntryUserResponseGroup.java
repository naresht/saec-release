package com.bfds.wss.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import com.bfds.wss.domain.reference.QuestionGroup;
import com.bfds.wss.domain.reference.QuestionGroupDisplayCode;
import com.bfds.wss.domain.reference.ResponseDisplayCode;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@RooToString
public class ClaimEntryUserResponseGroup extends UserResponseGroup implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Claim_Entry_fk", nullable = false)
	private ClaimEntry claimEntry;

	@OneToMany(mappedBy = "claimEntryUserResponseGroup", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval=true)
    private List<ClaimEntryUserResponses> claimEntryUserResponses = new ArrayList<ClaimEntryUserResponses>();


    @Override
    public List<UserResponse> getUserResponses() {
        List<UserResponse> ret = Lists.newArrayList();
        ret.addAll(claimEntryUserResponses);
        return java.util.Collections.unmodifiableList(ret);
    }

    @Override
    public void addUserResponse(UserResponse userResponse) {
        Preconditions.checkState(userResponse instanceof ClaimEntryUserResponses, "userResponse %s must be of type ClaimEntryUserResponses.", userResponse);
        ClaimEntryUserResponses entryUserResponses = (ClaimEntryUserResponses)userResponse;

        entryUserResponses.setClaimEntryUserResponseGroup(this);

        this.getClaimEntryUserResponses().add(entryUserResponses);
    }

    @Override
    public void removeUserResponse(UserResponse userResponse) {
        Preconditions.checkState(userResponse instanceof ClaimEntryUserResponses, "userResponse %s must be of type ClaimEntryUserResponses.", userResponse);
        ClaimEntryUserResponses entryUserResponses = (ClaimEntryUserResponses)userResponse;
        entryUserResponses.setClaimEntryUserResponseGroup(null);
        this.getClaimEntryUserResponses().remove(entryUserResponses);
    }
}
