package com.bfds.saec.batch.util;

import java.util.List;

import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.Payment;
import com.google.common.base.Preconditions;

public class RegistrationAndAddressLinesUtil {

	/**
	 * This method will return a list of strings by putting registrationlines and Addresslines together based on the below conditions.
	 * 1.If Claimant Reg is > 4 lines start the address on line 5 (otherwise start when you have come to the end of the Claimant Reg).
	 * 2.If Claimant Reg and Claimant Address together are more than 6 lines AND Country = US, enter City State Zip Ext (if availalbe) on line 7
	 * 3.If Claimant Reg and Claimant Address together are more than 5 lines AND Country <> US, then City State Zip Ext (if available) on line 6 and country code on line 7
	 * @param address
	 * @param claimantRegistration
	 * @return List<String>
	 * 
	 * TODO: Move to an appropriate location - Util or Domain class.
	 */
	public static List<String> buildRegistrationLines(Address address, ClaimantRegistration claimantRegistration) {
		Preconditions.checkArgument(address != null, "address is null");
		Preconditions.checkArgument(claimantRegistration != null, "claimantRegistration is null");
		final List<String> registrationLines = claimantRegistration.getNonEmptyRegistrationLines();				
		while(registrationLines.size() > 4) {			
			registrationLines.remove(registrationLines.size() - 1);
		} 
		registrationLines.addAll(address.getNonEmptyAddressLines());
		
		registrationLines.add(getCityStateZip(address));
		registrationLines.add(address.getCountryCode());

		int indexToRemove = -1;
		if(address.isUSAddress()) {			
			registrationLines.remove(registrationLines.size() - 1);
			if(registrationLines.size() >7){
				 indexToRemove = registrationLines.size() - 2;
			 } 
		} else {
			indexToRemove = registrationLines.size() - 3;
		}

		while(registrationLines.size() > 7) {				
			registrationLines.remove(indexToRemove);
			indexToRemove--; 
		} 

		return registrationLines;
	}


    /**
     * Constructs a string containing the city, state, zip and zip ext with the following rules.
     *
     * 1. Ignores nulls
     * 2. Uses space as the delimiter
     * 3. If zip extension is present uses "-" as the seperator between zip and ext.
     *
     * @param address
     * @return A string containing the city, state, zip and zip ext.
     * 
     */
    private static String getCityStateZip(Address address) {
      StringBuilder sb = new StringBuilder();
        if(StringUtils.hasText(address.getCity())) {
            sb.append(address.getCity()).append(" ");
        }
        if(StringUtils.hasText(address.getStateCode())) {
            sb.append(address.getStateCode()).append(" ");
        }
        if(address.getZipCode()!=null) {
            sb.append(address.getZipCode().toString());
        }
       
        return sb.toString().trim();
    }

    



}