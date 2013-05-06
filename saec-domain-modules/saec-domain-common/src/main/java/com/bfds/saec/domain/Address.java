package com.bfds.saec.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.envers.NotAudited;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.activity.AddressResearchChangeActivity;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.util.AddressUtils;
import com.bfds.saec.util.SaecStringUtils;

@Embeddable
public class Address implements Serializable, IAddress {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_ADDRESS_LINE_SEPERATOR = ", ";

    //@NotNull
	@Enumerated(EnumType.STRING)
	private AddressType addressType;

	private String address1;

	private String address2;

	private String address3;

	private String address4;

	private String address5;

	private String address6;

	private String city;

	private String stateCode;

	private String countryCode;

	private Date validFrom;

	private Date validTill;
    
    // Added for Address Research 
    // private String phoneAreaCode;                               
    private String phone;
    private String phoneExt;

	@Embedded
	private ZipCode zipCode;

	@NotAudited
	private boolean mailingAddress;
	
	/**
	 * The number of times this address has been updated by address research.
	 */
	@NotAudited
	private int addressResearchCount;
    
	@NotAudited
	private Boolean requiresAddressResearch;
    
    // TODO: Hack to pass event to ActivityEventListener. Need a better way to handle events
    @Embedded
    protected AddressResearchChangeActivity researchChangeActivity = new AddressResearchChangeActivity(); 
       
	public Address() {
		zipCode = new ZipCode();
	}

//    public String getPhoneAreaCode() {
//        return phoneAreaCode;
//    }
//
//    public void setPhoneAreaCode(String phoneAreaCode) {
//        this.phoneAreaCode = phoneAreaCode;
//    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneExt() {
        return phoneExt;
    }

    public void setPhoneExt(String phoneExt) {
        this.phoneExt = phoneExt;
    }

    @Override
	public AddressType getAddressType() {
		return addressType;
	}

	@Override
	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}        

    @Override
	public String getAddress1() {
		return address1;
	}

	@Override
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@Override
	public String getAddress2() {
		return address2;
	}

	@Override
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Override
	public String getAddress3() {
		return address3;
	}

	@Override
	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	@Override
	public String getAddress4() {
		return address4;
	}

	@Override
	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	@Override
	public String getAddress5() {
		return address5;
	}

	@Override
	public void setAddress5(String address5) {
		this.address5 = address5;
	}

    @Override
	public String getAddress6() {
		return address6;
	}

	@Override
	public void setAddress6(String address6) {
		this.address6 = address6;
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String getStateCode() {
		return stateCode;
	}

	@Override
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	@Override
	public String getCountryCode() {
		if (countryCode == null || countryCode.length() == 0)
			return "US" ;
		else
			return countryCode;
	}

	@Override
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public Date getValidFrom() {
		return validFrom;
	}

	@Override
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	@Override
	public Date getValidTill() {
		return validTill;
	}

	@Override
	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

	@Override
	public ZipCode getZipCode() {
		if(zipCode == null) {
			zipCode = new ZipCode();
		}
		return zipCode;
	}

	@Override
	public void setZipCode(ZipCode zipCode) {
		this.zipCode = zipCode;
	}       

	@Override
	public boolean isMailingAddress() {
		return mailingAddress;
	}

	@Override
	public void setMailingAddress(boolean mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	@Override
	public void setAddressLines(String[] addressLines) {
		this.addressLines = addressLines;
	}

	public int getAddressResearchCount() {
		return addressResearchCount;
	}

	public void setAddressResearchCount(int addressResearchCount) {
		this.addressResearchCount = addressResearchCount;
	}

	public Boolean getRequiresAddressResearch() {
		return requiresAddressResearch;
	}

	public void setRequiresAddressResearch(Boolean requiresAddressResearch) {
		this.requiresAddressResearch = requiresAddressResearch;
	}

	private transient String[] addressLines;

    public AddressResearchChangeActivity getResearchChangeActivity() {
        return researchChangeActivity;
    }

    public void setResearchChangeActivity(AddressResearchChangeActivity researchChangeActivity) {
        this.researchChangeActivity = researchChangeActivity;
    }
    
    /* (non-Javadoc)
     * @see com.bfds.saec.domain.IAddress#getNonEmptyLine(int)
     */
    @Override
	public String getNonEmptyLine(int number) {
		int count = 0;
		for (String line : getAddressLines()) {
			if (StringUtils.hasText(line)) {
				if (++count == number) {
					return line;
				}
			}
		}
		return null;
	}

	@Override
	public String getAddressLinesAsString() {
		return getAddressLinesAsString(DEFAULT_ADDRESS_LINE_SEPERATOR);
	}

	@Override
	public String getAddressLinesAsString(String lineSeperator) {
		return SaecStringUtils.getArrayString(getAddressLines(), lineSeperator != null ? lineSeperator
				: DEFAULT_ADDRESS_LINE_SEPERATOR);
	}

	@Override
	public String getAddressAsString() {
		return getAddressAsString(DEFAULT_ADDRESS_LINE_SEPERATOR);
	}

	@Override
	public String getAddressAsString(String lineSeperator) {
		final String[] addressLines = getAddressLines();
		String address = AddressUtils.getAddressAsString(addressLines, this.getCity(),
				this.getStateCode(), this.getZipCode().getZip(), this.getZipCode().getExt(), this
						.getCountryCode(),
						lineSeperator != null ? lineSeperator
						: DEFAULT_ADDRESS_LINE_SEPERATOR);
        
        return address;
	}

	private String[] getAddressLines() {
		if (addressLines == null) {
			addressLines = new String[6];
		}
		addressLines[0] = address1;
		addressLines[1] = address2;
		addressLines[2] = address3;
		addressLines[3] = address4;
		addressLines[4] = address5;
		addressLines[5] = address6;

		return addressLines;
	}
	
	public List<String> getNonEmptyAddressLines() {
		final List<String>addressLines = new ArrayList<String>(6);
		if (StringUtils.hasText(getAddress1())) {
			addressLines.add(getAddress1());
		}
		if (StringUtils.hasText(getAddress2())) {
			addressLines.add(getAddress2());
		}
		if (StringUtils.hasText(getAddress3())) {
			addressLines.add(getAddress3());
		}
		if (StringUtils.hasText(getAddress4())) {
			addressLines.add(getAddress4());
		}
		if (StringUtils.hasText(getAddress5())) {
			addressLines.add(getAddress5());
		}
		if (StringUtils.hasText(getAddress6())) {
			addressLines.add(getAddress6());
		}
		return addressLines;
	}
	
	/**
	 * 
	 * @return true if this is a US/USA address else false.
	 */
	public boolean isUSAddress() {
		return "US".equalsIgnoreCase(this.getCountryCode()) ||
		"USA".equalsIgnoreCase(this.getCountryCode());
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bfds.saec.domain.IAddress#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		for (String line : this.getAddressLines()) {
			if (StringUtils.hasText(line)) {
				return false;
			}
		}
		return !(StringUtils.hasText(this.city)
				|| StringUtils.hasText(this.stateCode) || (this.zipCode != null && !this.zipCode
				.isEmpty()));

	}

	public boolean hasSameAddress(IAddress addr) {
		return false;
	}

	@Override
	public void copyTo(IAddress addr) {
		addr.setAddressType(this.getAddressType());
		addr.setAddress1(this.getAddress1());
		addr.setAddress2(this.getAddress2());
		addr.setAddress3(this.getAddress3());
		addr.setAddress4(this.getAddress4());
		addr.setAddress5(this.getAddress5());
		addr.setAddress6(this.getAddress6());
		addr.setCity(this.getCity());
		addr.setCountryCode(this.getCountryCode());
		addr.setStateCode(this.getStateCode());
       if (this.getZipCode() != null) {
			addr.setZipCode(this.getZipCode().clone());
		}
                  
        if (addr instanceof Address) {   
            Address _addr = (Address)(addr);
            
            if (this.getResearchChangeActivity() != null) {
                _addr.setResearchChangeActivity(this.getResearchChangeActivity());
            }
            
            // Update Phone
//            if (StringUtils.hasText(this.getPhoneAreaCode())) {
//               _addr.setPhoneAreaCode(this.getPhoneAreaCode());
//            }
            
            if (StringUtils.hasText(this.getPhone())) {
                _addr.setPhone(this.getPhone());
            }
            
            if (StringUtils.hasText(this.getPhoneExt())) {
                _addr.setPhoneExt(this.getPhoneExt());
            }
        }
      
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((address1 == null) ? 0 : address1.hashCode());
		result = prime * result
				+ ((address2 == null) ? 0 : address2.hashCode());
		result = prime * result
				+ ((address3 == null) ? 0 : address3.hashCode());
		result = prime * result
				+ ((address4 == null) ? 0 : address4.hashCode());
		result = prime * result
				+ ((address5 == null) ? 0 : address5.hashCode());
		result = prime * result
				+ ((address6 == null) ? 0 : address6.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((countryCode == null) ? 0 : countryCode.hashCode());
		result = prime * result
				+ ((stateCode == null) ? 0 : stateCode.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		Address other = (Address) obj;
		if (address1 == null) {
			if (other.address1 != null)
				return false;
		} else if (!address1.equals(other.address1))
			return false;
		if (address2 == null) {
			if (other.address2 != null)
				return false;
		} else if (!address2.equals(other.address2))
			return false;
		if (address3 == null) {
			if (other.address3 != null)
				return false;
		} else if (!address3.equals(other.address3))
			return false;
		if (address4 == null) {
			if (other.address4 != null)
				return false;
		} else if (!address4.equals(other.address4))
			return false;
		if (address5 == null) {
			if (other.address5 != null)
				return false;
		} else if (!address5.equals(other.address5))
			return false;
		if (address6 == null) {
			if (other.address6 != null)
				return false;
		} else if (!address6.equals(other.address6))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!countryCode.equals(other.countryCode))
			return false;
		if (stateCode == null) {
			if (other.stateCode != null)
				return false;
		} else if (!stateCode.equals(other.stateCode))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Address [addressType=" + addressType + ", address1=" + address1
				+ ", address2=" + address2 + ", address3=" + address3
				+ ", address4=" + address4 + ", address5=" + address5
				+ ", address6=" + address6 + ", city=" + city + ", stateCode="
				+ stateCode + ", countryCode=" + countryCode + ", validFrom="
				+ validFrom + ", validTill=" + validTill + ", zipCode="
				+ zipCode + ", mailingAddress=" + mailingAddress + "]";
	}
}
