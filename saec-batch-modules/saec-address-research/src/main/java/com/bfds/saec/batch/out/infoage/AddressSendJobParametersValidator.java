package com.bfds.saec.batch.out.infoage;

import java.util.HashSet;
import java.util.Set;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

import com.bfds.saec.batch.jobParameters.AccountType;
import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.domain.Event;

/**
 *  Validator to verify that {@link Event} configuration rules are met for job execution.  
 */

public class AddressSendJobParametersValidator implements JobParametersValidator {
	
	public static final String INVALID_RESEARCH_TYPE = "Invalid researchType:";
	public static final String INVALID_ACCOUNT_TYPE = "Invalid accountType:";
	
	public static final String CORPORATE_PRESCRUB_NOT_ENABLED_FOR_EVENT = "Corporate Prescrub not enabled for event";
	public static final String CORPORATE_RESEARCH_NOT_ENABLED_FOR_EVENT = "Corporate Research not enabled for event";
	public static final String INDIVIDUAL_PRESCRUB_NOT_ENABLED_FOR_EVENT = "Individual Prescrub not enabled for event";
	public static final String INDIVIDUAL_RESEARCH_NOT_ENABLED_FOR_EVENT = "Individual Research not enabled for event";
	

	@Override
	public void validate(final JobParameters parameters) throws JobParametersInvalidException {
		validateParameterSyntax(parameters);		
		validateParameterRules(parameters);		
	}
	
	/**
	 * @param parameters - The {@link JobParameters}
	 * 
	 * Validates parameters against {@link Event} configuration.
	 * @throws JobParametersInvalidException 
	 */
	private void validateParameterRules(JobParameters parameters) throws JobParametersInvalidException {
		final Event event = getEvent();
		
		final boolean isCorporateAllowed = com.bfds.saec.domain.reference.AccountType.CORPORATION.equals(event.getAccountType()) 
				                           || com.bfds.saec.domain.reference.AccountType.INDIVIDUAL_AND_CORPORATION.equals(event.getAccountType());
		
		final boolean isIndividualAllowed = com.bfds.saec.domain.reference.AccountType.INDIVIDUAL.equals(event.getAccountType()) 
                || com.bfds.saec.domain.reference.AccountType.INDIVIDUAL_AND_CORPORATION.equals(event.getAccountType());
		
		final String researchType = parameters.getString("researchType");
		final String accountType = parameters.getString("accountType");
		
		if(ResearchType.PRESCRUB.equals(researchType)) {
			if(AccountType.CORPORATE.equals(accountType) && (!isCorporateAllowed || !event.getPreScrub())) {
				throw new AddressResearchJobParametersInvalidException(CORPORATE_PRESCRUB_NOT_ENABLED_FOR_EVENT);				
			} else if(AccountType.INDIVIDUAL.equals(accountType) && (!isIndividualAllowed || !event.getPreScrub())) {
				throw new AddressResearchJobParametersInvalidException(INDIVIDUAL_PRESCRUB_NOT_ENABLED_FOR_EVENT);									
			}
			
		} else if(ResearchType.RESEARCH.equals(researchType)) {
			if(AccountType.CORPORATE.equals(accountType) && (!isCorporateAllowed || !event.getAddressResearch())) {
				throw new AddressResearchJobParametersInvalidException(CORPORATE_RESEARCH_NOT_ENABLED_FOR_EVENT);			
			} else if(AccountType.INDIVIDUAL.equals(accountType) && (!isIndividualAllowed || !event.getAddressResearch())) {
				throw new AddressResearchJobParametersInvalidException(INDIVIDUAL_RESEARCH_NOT_ENABLED_FOR_EVENT);								
			}
		}
		
	}

	/**
	 * @param parameters - The {@link JobParameters}
	 * 
	 * Verifies that all required parameters are present and the parameters have permissible values.
	 * 
	 * @throws JobParametersInvalidException
	 */
	private void validateParameterSyntax(final JobParameters parameters) throws JobParametersInvalidException {
		Set<String> errors = new HashSet<String>();
		final String researchType = parameters.getString("researchType");
		final String accountType = parameters.getString("accountType");
		
		errors.add(ResearchType.PRESCRUB.equals(researchType) 
				   || ResearchType.RESEARCH.equals(researchType) ? null : INVALID_RESEARCH_TYPE + researchType);
		
		errors.add(AccountType.CORPORATE.equals(accountType) 
				   || AccountType.INDIVIDUAL.equals(accountType) ? null : INVALID_ACCOUNT_TYPE + accountType);

		
		errors.remove(null);
		if(errors.size() > 0 ) {
			
			throw new AddressResearchJobParametersInvalidException(errors);
		}
		
	}
	
	/**
	 * Test hook
	 */
	protected Event getEvent() {
		return Event.getCurrentEvent();
	}

}
