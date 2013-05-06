package com.bfds.saec.domain;

import javax.persistence.DiscriminatorValue;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import com.bfds.saec.domain.reference.Lov;

/**
 *  A code that indicates if/why account should not be included in distribution tranche. 
 */
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @DiscriminatorValue("HoldCode")
public final class HoldCode  extends Lov {
	
	private static final long serialVersionUID = 1L;	

}
