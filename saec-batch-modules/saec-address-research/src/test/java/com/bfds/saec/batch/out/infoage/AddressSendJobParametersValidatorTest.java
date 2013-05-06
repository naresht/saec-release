package com.bfds.saec.batch.out.infoage;

import static com.bfds.saec.batch.out.infoage.AddressSendJobParametersValidator.INVALID_ACCOUNT_TYPE;
import static com.bfds.saec.batch.out.infoage.AddressSendJobParametersValidator.INVALID_RESEARCH_TYPE;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import com.bfds.saec.batch.out.infoage.AddressSendJobParametersValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;

import com.bfds.saec.batch.out.infoage.AddressResearchJobParametersInvalidException;
import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.reference.AccountType;

@RunWith(MockitoJUnitRunner.class)
public class AddressSendJobParametersValidatorTest {
	
	/**
     * Verifies that all required parameters are present and the parameters have permissible values.
     * A {@link JobParametersInvalidException} is thrown to indicate errors.
     */

	@Test
    public void correctJobParametersMustBepreset() throws Exception {

        JobParameters jobParameters = newJobParameters("123", "abc", "def");
        Collection<String> errors = getErrors(newEvent(false, false, AccountType.INDIVIDUAL_AND_CORPORATION), jobParameters);
        assertThat(errors).containsOnly(INVALID_RESEARCH_TYPE + "abc", INVALID_ACCOUNT_TYPE + "def");
        
        jobParameters = newJobParameters("123", ResearchType.PRESCRUB, "def");                
        errors = getErrors(newEvent(false, false, AccountType.INDIVIDUAL_AND_CORPORATION), jobParameters);      
        assertThat(errors).containsOnly(INVALID_ACCOUNT_TYPE + "def"); 
        
        jobParameters = newJobParameters("123", "abc", com.bfds.saec.batch.jobParameters.AccountType.INDIVIDUAL);
        errors = getErrors(newEvent(false, false, AccountType.INDIVIDUAL_AND_CORPORATION), jobParameters);      
        assertThat(errors).containsOnly(INVALID_RESEARCH_TYPE + "abc"); 
        
        jobParameters = newJobParameters("123", ResearchType.PRESCRUB, com.bfds.saec.batch.jobParameters.AccountType.INDIVIDUAL);
        errors = getErrors(newEvent(false, false, AccountType.INDIVIDUAL_AND_CORPORATION), jobParameters);      
        assertThat(errors).isEmpty();
       
    }
	
    /**
     * Test to verify that the job does not execute if the {@link Event} level rules for research are not met.
     * A {@link JobParametersInvalidException} is thrown to indicate that the job cannot be executed as a result of {@link Event} configuration rules.
     */

	@Test
    public void individualPrescrubShouldNotRunIfNotConfiguredForEvent() throws Exception {
				
        JobParameters jobParameters = newJobParameters("file:target/a.xml", ResearchType.PRESCRUB, com.bfds.saec.batch.jobParameters.AccountType.INDIVIDUAL);
        
        JobParametersInvalidException ex = getException(newEvent(false, false, AccountType.INDIVIDUAL_AND_CORPORATION), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.INDIVIDUAL_PRESCRUB_NOT_ENABLED_FOR_EVENT);
        
        ex = getException(newEvent(false, true, AccountType.INDIVIDUAL), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.INDIVIDUAL_PRESCRUB_NOT_ENABLED_FOR_EVENT);
        
        ex = getException(newEvent(true, true, AccountType.CORPORATION), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.INDIVIDUAL_PRESCRUB_NOT_ENABLED_FOR_EVENT);
       
    }
	
    /**
     * Test to verify that the job does not execute if the {@link Event} level rules for research are not met.
     * A {@link JobParametersInvalidException} is thrown to indicate that the job cannot be executed as a result of {@link Event} configuration rules.
     */

	@Test
    public void individualResearchShouldNotRunIfNotConfiguredForEvent() throws Exception {
				
        JobParameters jobParameters = newJobParameters("file:target/a.xml", ResearchType.RESEARCH, com.bfds.saec.batch.jobParameters.AccountType.INDIVIDUAL);
        
        JobParametersInvalidException ex = getException(newEvent(false, false, AccountType.INDIVIDUAL_AND_CORPORATION), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.INDIVIDUAL_RESEARCH_NOT_ENABLED_FOR_EVENT);
        
        ex = getException(newEvent(true, false, AccountType.INDIVIDUAL), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.INDIVIDUAL_RESEARCH_NOT_ENABLED_FOR_EVENT);
        
        ex = getException(newEvent(true, true, AccountType.CORPORATION), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.INDIVIDUAL_RESEARCH_NOT_ENABLED_FOR_EVENT);
       
    }
	
	
	
    /**
     * Test to verify that the job does not execute if the {@link Event} level rules for research are not met.
     * A {@link JobParametersInvalidException} is thrown to indicate that the job cannot be executed as a result of {@link Event} configuration rules.
     */

	@Test
    public void corporatePrescrubShouldNotRunIfNotConfiguredForEvent() throws Exception {
				
        JobParameters jobParameters = newJobParameters("file:target/a.xml", ResearchType.PRESCRUB, com.bfds.saec.batch.jobParameters.AccountType.CORPORATE);
        
        JobParametersInvalidException ex = getException(newEvent(false, false, AccountType.INDIVIDUAL_AND_CORPORATION), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.CORPORATE_PRESCRUB_NOT_ENABLED_FOR_EVENT);
        
        ex = getException(newEvent(false, true, AccountType.CORPORATION), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.CORPORATE_PRESCRUB_NOT_ENABLED_FOR_EVENT);
        
        ex = getException(newEvent(true, true, AccountType.CORPORATION), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.CORPORATE_PRESCRUB_NOT_ENABLED_FOR_EVENT);
       
    }
	
    /**
     * Test to verify that the job does not execute if the {@link Event} level rules for research are not met.
     * A {@link JobParametersInvalidException} is thrown to indicate that the job cannot be executed as a result of {@link Event} configuration rules.
     */

	@Test
    public void corporateResearchShouldNotRunIfNotConfiguredForEvent() throws Exception {
				
        JobParameters jobParameters = newJobParameters("file:target/a.xml", ResearchType.RESEARCH, com.bfds.saec.batch.jobParameters.AccountType.CORPORATE);
        
        JobParametersInvalidException ex = getException(newEvent(false, false, AccountType.INDIVIDUAL_AND_CORPORATION), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.CORPORATE_RESEARCH_NOT_ENABLED_FOR_EVENT);
        
        ex = getException(newEvent(true, false, AccountType.CORPORATION), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.CORPORATE_RESEARCH_NOT_ENABLED_FOR_EVENT);
        
        ex = getException(newEvent(true, true, AccountType.CORPORATION), jobParameters);        
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).startsWith(AddressSendJobParametersValidator.CORPORATE_RESEARCH_NOT_ENABLED_FOR_EVENT);
       
    }


	private JobParameters newJobParameters(String outputResource, String researchType, String accountType) {
		JobParameters jobParameters = new JobParametersBuilder()
        		.addString("outputResource", outputResource  )
                .addString("researchType", researchType)
                .addString("accountType", accountType)
                .toJobParameters();
		return jobParameters;
	}
	

	private JobParametersInvalidException getException(final Event e,
			JobParameters jobParameters) {
		JobParametersInvalidException ex = null;
        try {
			newAddressSendJobParametersValidator(e).validate(jobParameters);
		} catch (final JobParametersInvalidException exception) {
			ex = exception;
		}
		return ex;
	}
	
	private Collection<String> getErrors(final Event e,
			JobParameters jobParameters) {
		JobParametersInvalidException ex = null;
        try {
			newAddressSendJobParametersValidator(e).validate(jobParameters);
		} catch (final JobParametersInvalidException exception) {
			ex = exception;
		}
		return ex == null ? (new AddressResearchJobParametersInvalidException("")).getErrors() : ((AddressResearchJobParametersInvalidException)ex).getErrors();
	}

	private Event newEvent(boolean preScrubAllowed, boolean researchAllowed, AccountType accountType) {
		Event e = mock(Event.class);
    	when(e.getPreScrub()).thenReturn(false);
    	when(e.getAddressResearch()).thenReturn(false);
    	when(e.getAccountType()).thenReturn(AccountType.INDIVIDUAL_AND_CORPORATION);
		return e;
	}
    
	private AddressSendJobParametersValidator newAddressSendJobParametersValidator(final Event event) {
		final AddressSendJobParametersValidator validator = new AddressSendJobParametersValidator() {

			@Override
			protected Event getEvent() {
				return event;
			}
			
		};
		
		return validator;
	}

}
