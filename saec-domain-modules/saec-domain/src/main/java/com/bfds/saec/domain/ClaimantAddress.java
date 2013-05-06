package com.bfds.saec.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

import org.hibernate.envers.Audited;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.util.PaymentCodeUtil;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @Audited
public class ClaimantAddress implements IAddress, Serializable {
    private static final long serialVersionUID = 1L;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "claimant_fk")
    private Claimant claimant;

    public ClaimantAddress() {
        address = new Address();
    }
    
    //@PostPersist
    public void afterCreate() {
    	this.getClaimant().triggerAddressCreatedEvent(this);
    }

    public ClaimantAddress(final Address address) {
        this.address = address;
    }

    public Claimant getClaimant() {
        return claimant;
    }

    public void setClaimant(Claimant claimant) {
        this.claimant = claimant;
    }

    public Address getAddress() {
        return address;
    }

    public String getPhone() {
        return address.getPhone();
    }

    public void setPhone(String phone) {
        address.setPhone(phone);
    }

    public String getPhoneExt() {
        return address.getPhoneExt();
    }

    public void setPhoneExt(String phoneExt) {
        address.setPhoneExt(phoneExt);
    }

    @Override
    public AddressType getAddressType() {
        return address.getAddressType();
    }

    @Override
    public void setAddressType(AddressType addressType) {
        address.setAddressType(addressType);
    }

    public void setAddressTypeStr(String addressType) {
        if (StringUtils.hasLength(addressType)) {
            setAddressType(AddressType.valueOf(addressType));
        }
    }

    @Override
    public String getAddress1() {
        return address.getAddress1();
    }

    @Override
    public void setAddress1(String address1) {
        address.setAddress1(address1);
    }

    @Override
    public String getAddress2() {
        return address.getAddress2();
    }

    @Override
    public void setAddress2(String address2) {
        address.setAddress2(address2);
    }

    @Override
    public String getAddress3() {
        return address.getAddress3();
    }

    @Override
    public void setAddress3(String address3) {
        address.setAddress3(address3);
    }

    @Override
    public String getAddress4() {
        return address.getAddress4();
    }

    @Override
    public void setAddress4(String address4) {
        address.setAddress4(address4);
    }

    @Override
    public String getAddress5() {
        return address.getAddress5();
    }

    @Override
    public void setAddress5(String address5) {
        address.setAddress5(address5);
    }

    @Override
    public String getAddress6() {
        return address.getAddress6();
    }

    @Override
    public void setAddress6(String address6) {
        address.setAddress6(address6);
    }

    @Override
    public String getCity() {
        return address.getCity();
    }

    @Override
    public void setCity(String city) {
        address.setCity(city);
    }

    @Override
    public String getStateCode() {
        return address.getStateCode();
    }

    @Override
    public void setStateCode(String stateCode) {
        address.setStateCode(stateCode);
    }

    @Override
    public String getCountryCode() {
        return address.getCountryCode();
    }

    @Override
    public void setCountryCode(String countryCode) {
        address.setCountryCode(countryCode);
    }

    @Override
    public Date getValidFrom() {
        return address.getValidFrom();
    }

    @Override
    public void setValidFrom(Date validFrom) {
        address.setValidFrom(validFrom);
    }

    @Override
    public Date getValidTill() {
        return address.getValidTill();
    }

    @Override
    public void setValidTill(Date validTill) {
        address.setValidTill(validTill);

    }

    @Override
    public ZipCode getZipCode() {
        return address.getZipCode();
    }

    @Override
    public void setZipCode(ZipCode zipCode) {
        address.setZipCode(zipCode);
    }

    public void setZip(String zip) {
        if (getZipCode() == null) {
            setZipCode(new ZipCode());
        }
        getZipCode().setZip(zip);
    }

    public void setZipExt(String zipExt) {
        if (getZipCode() == null) {
            setZipCode(new ZipCode());
        }
        getZipCode().setExt(zipExt);
    }

    @Override
    public boolean isMailingAddress() {
        return address.isMailingAddress();
    }

    @Override
    public void setMailingAddress(boolean mailingAddress) {
        address.setMailingAddress(mailingAddress);
    }

    @Override
    public void setAddressLines(String[] addressLines) {
        address.setAddressLines(addressLines);
    }

    @Override
    public String getNonEmptyLine(int number) {
        return address.getNonEmptyLine(number);
    }

    @Override
    public String getAddressLinesAsString() {
        return address.getAddressLinesAsString();
    }

    @Override
    public String getAddressLinesAsString(String lineSeperator) {
        return address.getAddressLinesAsString(lineSeperator);
    }

    @Override
    public String getAddressAsString() {
        return address.getAddressAsString();
    }

    @Override
    public String getAddressAsString(String lineSeperator) {
        return address.getAddressAsString(lineSeperator);
    }

    @Override
    public boolean isEmpty() {
        return address.isEmpty();
    }

    @Override
    public void copyTo(IAddress addr) {
        address.copyTo(addr);
    }
 
    /**
	 * Set the {@link MailObjectAddress} of all {@link Letter}s and {@link Payment}s that are eligible for mailing
	 * to the mailing address of the {@link Claimant}
	 * 
	 */
    
	//@PostUpdate
	public void updateMailObjectAddress() {		
		if(this.getClaimant()!=null){
		for (Payment payment : this.getClaimant().getPaymentsEligibleForMailing()) {
				final MailObjectAddress checkAddress = new MailObjectAddress();
				this.getClaimant().getMailingAddress().copyTo(checkAddress);
				payment.setCheckAddress(checkAddress);
		}
 
		/*for (Letter letter : this.getClaimant().getLettersEligibleForMailing()) {
				final MailObjectAddress letterAddress = new MailObjectAddress();
				this.getClaimant().getMailingAddress().copyTo(letterAddress);
				letter.setAddress(letterAddress);
		
		}*/
		
		for (Payment payment : this.getClaimant().getPaymentsEligibleForMailing()) {
			if (PaymentCodeUtil.getCreatedCodes().contains(payment.getPaymentCode())) {
				  final RegistrationLines payToRegistration = new RegistrationLines();
				  this.getClaimant().getClaimantRegistration().getLines().copyTo(payToRegistration);
			        payment.setPayToRegistration(payToRegistration);
			}
		  }
		
		}
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
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
        ClaimantAddress other = (ClaimantAddress) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        return true;
    }

    public String toString() {
        return address.toString();
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
}
