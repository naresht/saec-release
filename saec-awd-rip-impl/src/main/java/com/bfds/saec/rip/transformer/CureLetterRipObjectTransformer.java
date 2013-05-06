package com.bfds.saec.rip.transformer;

import java.util.Map;

import org.springframework.util.StringUtils;

import com.bfds.saec.rip.domain.CureLetterRipObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class CureLetterRipObjectTransformer extends AbstractRipObjectTransformer<CureLetterRipObject> {

	private static final String TEMPLATE_FILE = "vm/awd-cure-letter.vm";	
	
	@Override
	public String getClasspathTemplateFile() {				
		return TEMPLATE_FILE;
	}

	@Override
	public void preprocess(final CureLetterRipObject ropObject) {		
	}
	

}

