package com.bfds.saec.domain;

public interface IRegistrationLines {

	public abstract String getRegistration1();

	public abstract void setRegistration1(String registration1);

	public abstract String getRegistration2();

	public abstract void setRegistration2(String registration2);

	public abstract String getRegistration3();

	public abstract void setRegistration3(String registration3);

	public abstract String getRegistration4();

	public abstract void setRegistration4(String registration4);

	public abstract String getRegistration5();

	public abstract void setRegistration5(String registration5);

	public abstract String getRegistration6();

	public abstract void setRegistration6(String registration6);

	/**
	 * @return All 6 registration lines concatenated with default line separator. 
	 */
	public abstract String getRegistrationLinesAsString();

	/**
	 * @param lineSeperator - the String to use as the registration line line separator. If lineSeperator is null the default lineSeperator is used.
	 * @return All 6 registration lines of address concatenated with lineSeperator as the separator. 
	 */	
	public abstract String getRegistrationLinesAsString(String lineSeperator);

}