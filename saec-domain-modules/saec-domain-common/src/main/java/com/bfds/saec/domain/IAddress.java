package com.bfds.saec.domain;

import java.util.Date;

import com.bfds.saec.domain.reference.AddressType;

public interface IAddress {

	public abstract AddressType getAddressType();

	public abstract void setAddressType(AddressType addressType);

	public abstract String getAddress1();

	public abstract void setAddress1(String address1);

	public abstract String getAddress2();

	public abstract void setAddress2(String address2);

	public abstract String getAddress3();

	public abstract void setAddress3(String address3);

	public abstract String getAddress4();

	public abstract void setAddress4(String address4);

	public abstract String getAddress5();

	public abstract void setAddress5(String address5);

	public abstract String getAddress6();

	public abstract void setAddress6(String address6);

	public abstract String getCity();

	public abstract void setCity(String city);

	public abstract String getStateCode();

	public abstract void setStateCode(String stateCode);

	public abstract String getCountryCode();

	public abstract void setCountryCode(String countryCode);

	public abstract Date getValidFrom();

	public abstract void setValidFrom(Date validFrom);

	public abstract Date getValidTill();

	public abstract void setValidTill(Date validTill);

	public abstract ZipCode getZipCode();

	public abstract void setZipCode(ZipCode zipCode);

	public abstract boolean isMailingAddress();

	public abstract void setMailingAddress(boolean mailingAddress);

	public abstract void setAddressLines(String[] addressLines);

	/**
	 * @param number  - the not empty line number 
	 * @return - The the not empty line bye number. If the 1st 3rd and 5th lines are no empty and number=2, the 5th line is returned,
	 *  else returns null.
	 */
	public abstract String getNonEmptyLine(int number);
	
	/**
	 * @return All fields of address concatenated with default line separator. 
	 */	
	public abstract String getAddressAsString();
	
	/**
	 * @param lineSeperator - the String to use as the address fields separator. If lineSeperator is null the default lineSeperator is used.
	 * @return All fields of address concatenated with lineSeperator as the separator. 
	 */		
	public abstract String getAddressAsString(String lineSeperator);

	/**
	 * @return All 6 lines of address concatenated with default line separator. 
	 */	
	public abstract String getAddressLinesAsString();

	/**
	 * @param lineSeperator - the String to use as the address line separator. If lineSeperator is null the default lineSeperator is used.
	 * @return All 6 lines of address concatenated with lineSeperator as the separator. 
	 */
	public abstract String getAddressLinesAsString(String lineSeperator);

	public abstract boolean isEmpty();

	public abstract void copyTo(IAddress addr);

}