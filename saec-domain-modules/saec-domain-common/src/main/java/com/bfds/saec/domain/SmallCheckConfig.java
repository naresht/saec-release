package com.bfds.saec.domain;

import com.bfds.saec.domain.reference.Lov;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A bunch of fields that go on the DSTO check file. These fields are set at the event level.
 */

@RooJavaBean
@Embeddable
@RooToString
public final class SmallCheckConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @Column(length = 30)
    private String eventNameAddress1;

    @Column(length = 30)
    private String eventNameAddress2;

    @Column(length = 30)
    private String eventNameAddress3;

    @Column(length = 30)
    private String eventNameAddress4;

    @Column(length = 30)
    private String eventNameAddress5;

    @Column(length = 30)
    private String eventNameAddress6;

    @Column(length = 200)
    private String variableVerbiage;

    @Column(length = 30)
    private String bankInfo1;

    @Column(length = 30)
    private String bankInfo2;

    @Column(length = 30)
    private String bankInfo3;

    @Column(length = 100)
    private String distributionText;

    @Column(length = 10)
    private String abaTop;

    @Column(length = 10)
    private String abaBottom;

    @Column(length = 50)
    private String voidAfterDays;
    
   public boolean isEmpty()
    {
    	return !(StringUtils.hasText(eventNameAddress1)
				|| StringUtils.hasText(eventNameAddress2) || StringUtils.hasText(eventNameAddress3) || StringUtils.hasText(eventNameAddress4) 
				|| StringUtils.hasText(eventNameAddress5) || StringUtils.hasText(eventNameAddress6)
				|| StringUtils.hasText(variableVerbiage) || StringUtils.hasText(bankInfo1) || StringUtils.hasText(bankInfo2) 
				|| StringUtils.hasText(bankInfo3) || StringUtils.hasText(distributionText) || StringUtils.hasText(abaTop)
				|| StringUtils.hasText(abaBottom) || StringUtils.hasText(voidAfterDays));
    }
}
