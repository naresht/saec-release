package com.bfds.saec.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.StringUtils;

import com.bfds.saec.util.SaecStringUtils;

@RooJavaBean
@RooToString
@Embeddable
public class Name implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@NotNull
	private String firstName;

	private String lastName;

	private String middleName;

    public Name() {
    }

    public Name(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
    
    boolean isEmpty() {
		return !(StringUtils.hasText(firstName)
				|| StringUtils.hasText(lastName) || StringUtils
				.hasText(middleName));
	}
	
	public String getAsString() {
		String[] values = new String[3];
		values[0] = firstName;
		values[1] = middleName;
		values[2] = lastName;
		return SaecStringUtils.getArrayString(values, " ");
	}
	public Name copy() {
		Name ret = new Name();
		ret.setFirstName(firstName);
		ret.setMiddleName(middleName);
		ret.setLastName(lastName);
		return ret; 
		}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((middleName == null) ? 0 : middleName.hashCode());
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
		Name other = (Name) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		}
		else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		}
		else if (!lastName.equals(other.lastName))
			return false;
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		}
		else if (!middleName.equals(other.middleName))
			return false;
		return true;
	}
		

}