package com.bfds.saec.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.util.PaymentCodeUtil;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @Audited
public class ClaimantRegistration implements IRegistrationLines, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Embedded
	private RegistrationLines lines;
	
	@OneToOne(optional = false, fetch = FetchType.LAZY, mappedBy = "claimantRegistration")
	private Claimant claimant;
	
	private String concatinatedLines;

	public ClaimantRegistration() {
		lines = new RegistrationLines();
	}

    public Long getId() {
		return id;
	}

	@Override
	public String getRegistration1() {
		return lines.getRegistration1();
	}
	@Override
	public void setRegistration1(String registration1) {
		lines.setRegistration1(registration1);
		
	}
	@Override
	public String getRegistration2() {
		return lines.getRegistration2();
	}
	@Override
	public void setRegistration2(String registration2) {
		lines.setRegistration2(registration2);
	}
	@Override
	public String getRegistration3() {
		return lines.getRegistration3();
	}
	@Override
	public void setRegistration3(String registration3) {
		lines.setRegistration3(registration3);
	}
	@Override
	public String getRegistration4() {
		return lines.getRegistration4();
	}
	@Override
	public void setRegistration4(String registration4) {
		lines.setRegistration4(registration4);
	}
	@Override
	public String getRegistration5() {
		return lines.getRegistration5();
	}
	@Override
	public void setRegistration5(String registration5) {
		lines.setRegistration5(registration5);
	}
	@Override
	public String getRegistration6() {
		return lines.getRegistration6();
	}
	@Override
	public void setRegistration6(String registration6) {
		lines.setRegistration6(registration6);
	}
	@Override
	public String getRegistrationLinesAsString() {
		return lines.getRegistrationLinesAsString();
	}
	@Override
	public String getRegistrationLinesAsString(String lineSeperator) {
		return lines.getRegistrationLinesAsString(lineSeperator);
	}

	/**
	 * Not part of client API. Do not use except in tests.
	 */
	public RegistrationLines getLines() {
		return lines;
	}

	public String toString() {
        return lines.toString();
    }	
	
	@PrePersist
	@PreUpdate
	public void concatinatedLined() {
		this.concatinatedLines=getRegistrationLinesAsString(" ");		
	}

	public List<String> getNonEmptyRegistrationLines() {
		final List<String> registrationLines = new ArrayList<String>(6);
		if (StringUtils.hasText(getRegistration1())) {
			registrationLines.add(getRegistration1());
		}
		if (StringUtils.hasText(getRegistration2())) {
			registrationLines.add(getRegistration2());
		}
		if (StringUtils.hasText(getRegistration3())) {
			registrationLines.add(getRegistration3());
		}
		if (StringUtils.hasText(getRegistration4())) {
			registrationLines.add(getRegistration4());
		}
		if (StringUtils.hasText(getRegistration5())) {
			registrationLines.add(getRegistration5());
		}
		if (StringUtils.hasText(getRegistration6())) {
			registrationLines.add(getRegistration6());
		}
		return registrationLines;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lines == null) ? 0 : lines.hashCode());
		return result;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClaimantRegistration other = (ClaimantRegistration) obj;
		if (lines == null) {
			if (other.lines != null)
				return false;
		}
		else if (!lines.equals(other.lines))
			return false;
		return true;
	}
	
	
}
