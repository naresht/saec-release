package com.bfds.saec.batch.out.infoage;

import java.util.Collection;
import java.util.Collections;

import org.springframework.batch.core.JobParametersInvalidException;

public class AddressResearchJobParametersInvalidException extends
		JobParametersInvalidException {
	
	private static final long serialVersionUID = 1L;
	
	private Collection<String> errors;
	
	public AddressResearchJobParametersInvalidException(String msg) {
		super(msg);	
	}
	
	public AddressResearchJobParametersInvalidException(final Collection<String> errors) {
		super(errors.toString());
		this.errors = errors;
	}

	public Collection<String> getErrors() {
		return errors == null ? Collections.<String>emptySet(): errors ;
	}	

}
