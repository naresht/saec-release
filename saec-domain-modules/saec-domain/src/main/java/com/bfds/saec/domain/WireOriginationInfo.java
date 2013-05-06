package com.bfds.saec.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@Embeddable
public class WireOriginationInfo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String referenceNo="";
    
   
    @AttributeOverrides( {
        @AttributeOverride(name="registration1", column = @Column(name="wire_origination_registration1") ),
        @AttributeOverride(name="registration2", column = @Column(name="wire_origination_registration2") ),
        @AttributeOverride(name="registration3", column = @Column(name="wire_origination_registration3") ),
        @AttributeOverride(name="registration4", column = @Column(name="wire_origination_registration4") ),
        @AttributeOverride(name="registration5", column = @Column(name="wire_origination_registration5") ),
        @AttributeOverride(name="registration6", column = @Column(name="wire_origination_registration6") )        
    } )
    
     @Embedded 
    private RegistrationLines lines;
    
    public WireOriginationInfo() {
	 lines = new RegistrationLines();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lines == null) ? 0 : lines.hashCode());
		result = prime * result
				+ ((referenceNo == null) ? 0 : referenceNo.hashCode());
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
		WireOriginationInfo other = (WireOriginationInfo) obj;
		if (lines == null) {
			if (other.lines != null)
				return false;
		} else if (!lines.equals(other.lines))
			return false;
		if (referenceNo == null) {
			if (other.referenceNo != null)
				return false;
		} else if (!referenceNo.equals(other.referenceNo))
			return false;
		return true;
	}
    
}
