package com.bfds.wss.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.event.ValueChangeEvent;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import com.bfds.wss.domain.reference.AdditionalQuestions;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@RooToString(excludeFields = "claimEntryUserResponseGroup")
public class ClaimEntryUserResponses extends UserResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Claim_Entry_User_Response_Group_fk", nullable = false)
	private ClaimEntryUserResponseGroup claimEntryUserResponseGroup;

    @Override
    public UserResponseGroup getUserResponseGroup() {
        return claimEntryUserResponseGroup;
    }
}
