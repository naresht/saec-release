package com.bfds.saec.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.StringUtils;

@RooToString
@Embeddable
public class RegistrationLines implements Serializable, IRegistrationLines {
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_REGISTRATION_LINE_SEPERATOR = ", ";
	
    private String registration1="";

    private String registration2="";

    private String registration3="";

    private String registration4="";

    private String registration5="";

    private String registration6="";        
	
	
	@Override
	public String getRegistration1() {
		return registration1;
	}

    @Override
	public void setRegistration1(String registration1) {
		this.registration1 = registration1;
	}

    @Override
	public String getRegistration2() {
		return registration2;
	}

    @Override
	public void setRegistration2(String registration2) {
		this.registration2 = registration2;
	}

    @Override
	public String getRegistration3() {
		return registration3;
	}

    @Override
	public void setRegistration3(String registration3) {
		this.registration3 = registration3;
	}

    @Override
	public String getRegistration4() {
		return registration4;
	}

    @Override
	public void setRegistration4(String registration4) {
		this.registration4 = registration4;
	}

    @Override
	public String getRegistration5() {
		return registration5;
	}

    @Override
	public void setRegistration5(String registration5) {
		this.registration5 = registration5;
	}

    @Override
	public String getRegistration6() {
		return registration6;
	}

    @Override
	public void setRegistration6(String registration6) {
		this.registration6 = registration6;
	}

    @Override
	public String getRegistrationLinesAsString() {
    	return getRegistrationLinesAsString(DEFAULT_REGISTRATION_LINE_SEPERATOR);
    }

    @Override
	public String getRegistrationLinesAsString(String lineSeperator) {
    	lineSeperator = lineSeperator != null ? lineSeperator : DEFAULT_REGISTRATION_LINE_SEPERATOR;
   	 StringBuilder sb = new StringBuilder();
     if(StringUtils.hasText(registration1)) {
     	sb.append(registration1);
     }
     if(StringUtils.hasText(registration2)) {
      	sb.append(lineSeperator).append(registration2);
      }                 
     if(StringUtils.hasText(registration3)) {
     	sb.append(lineSeperator).append(registration3);
     }          
     if(StringUtils.hasText(registration4)) {
      	sb.append(lineSeperator).append(registration4);
      }
      if(StringUtils.hasText(registration5)) {
       	sb.append(lineSeperator).append(registration5);
       }                 
      if(StringUtils.hasText(registration6)) {
      	sb.append(lineSeperator).append(registration6);
      }               
     return sb.toString().trim();
    }
    
    public void copyTo(final RegistrationLines to) {
    	to.setRegistration1(this.getRegistration1());
    	to.setRegistration2(this.getRegistration2());
    	to.setRegistration3(this.getRegistration3());
    	to.setRegistration4(this.getRegistration4());
    	to.setRegistration5(this.getRegistration5());
    	to.setRegistration6(this.getRegistration6());
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Registration1: ").append(getRegistration1()).append(", ");
        sb.append("Registration2: ").append(getRegistration2()).append(", ");
        sb.append("Registration3: ").append(getRegistration3()).append(", ");
        sb.append("Registration4: ").append(getRegistration4()).append(", ");
        sb.append("Registration5: ").append(getRegistration5()).append(", ");
        sb.append("Registration6: ").append(getRegistration6());
        return sb.toString();
    }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((registration1 == null) ? 0 : registration1.hashCode());
		result = prime * result
				+ ((registration2 == null) ? 0 : registration2.hashCode());
		result = prime * result
				+ ((registration3 == null) ? 0 : registration3.hashCode());
		result = prime * result
				+ ((registration4 == null) ? 0 : registration4.hashCode());
		result = prime * result
				+ ((registration5 == null) ? 0 : registration5.hashCode());
		result = prime * result
				+ ((registration6 == null) ? 0 : registration6.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		RegistrationLines other = (RegistrationLines) obj;
		if (registration1 == null) {
			if (other.registration1 != null)
				return false;
		} else if (!registration1.equals(other.registration1))
			return false;
		if (registration2 == null) {
			if (other.registration2 != null)
				return false;
		} else if (!registration2.equals(other.registration2))
			return false;
		if (registration3 == null) {
			if (other.registration3 != null)
				return false;
		} else if (!registration3.equals(other.registration3))
			return false;
		if (registration4 == null) {
			if (other.registration4 != null)
				return false;
		} else if (!registration4.equals(other.registration4))
			return false;
		if (registration5 == null) {
			if (other.registration5 != null)
				return false;
		} else if (!registration5.equals(other.registration5))
			return false;
		if (registration6 == null) {
			if (other.registration6 != null)
				return false;
		} else if (!registration6.equals(other.registration6))
			return false;
		return true;
	} 
       
}