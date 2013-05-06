package com.bfds.saec.domain;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
@Embeddable
public class Bank implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String abaNo;
    private String accountNo;
    private String attn;

    @Embedded
    private Address address = new Address();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "registration1", column = @Column(name = "bank_registration1")),
            @AttributeOverride(name = "registration2", column = @Column(name = "bank_registration2")),
            @AttributeOverride(name = "registration3", column = @Column(name = "bank_registration3")),
            @AttributeOverride(name = "registration4", column = @Column(name = "bank_registration4")),
            @AttributeOverride(name = "registration5", column = @Column(name = "bank_registration5")),
            @AttributeOverride(name = "registration6", column = @Column(name = "bank_registration6"))
    })
    private RegistrationLines registration = new RegistrationLines();

    private BigDecimal forFurtherCredit;

    private String inReferenceTo;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abaNo == null) ? 0 : abaNo.hashCode());
		result = prime * result
				+ ((accountNo == null) ? 0 : accountNo.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((attn == null) ? 0 : attn.hashCode());
		result = prime
				* result
				+ ((forFurtherCredit == null) ? 0 : forFurtherCredit.hashCode());
		result = prime * result
				+ ((inReferenceTo == null) ? 0 : inReferenceTo.hashCode());
		result = prime * result
				+ ((registration == null) ? 0 : registration.hashCode());
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
		Bank other = (Bank) obj;
		if (abaNo == null) {
			if (other.abaNo != null)
				return false;
		} else if (!abaNo.equals(other.abaNo))
			return false;
		if (accountNo == null) {
			if (other.accountNo != null)
				return false;
		} else if (!accountNo.equals(other.accountNo))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (attn == null) {
			if (other.attn != null)
				return false;
		} else if (!attn.equals(other.attn))
			return false;
		if (forFurtherCredit == null) {
			if (other.forFurtherCredit != null)
				return false;
		} else if (!forFurtherCredit.equals(other.forFurtherCredit))
			return false;
		if (inReferenceTo == null) {
			if (other.inReferenceTo != null)
				return false;
		} else if (!inReferenceTo.equals(other.inReferenceTo))
			return false;
		if (registration == null) {
			if (other.registration != null)
				return false;
		} else if (!registration.equals(other.registration))
			return false;
		return true;
	}

}
