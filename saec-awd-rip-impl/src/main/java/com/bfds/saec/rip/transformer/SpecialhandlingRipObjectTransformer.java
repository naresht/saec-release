package com.bfds.saec.rip.transformer;

import org.springframework.util.StringUtils;

import com.bfds.saec.rip.domain.SpecialhandlingRipObject;


public class SpecialhandlingRipObjectTransformer extends AbstractRipObjectTransformer<SpecialhandlingRipObject> {

	private static final String TEMPLATE_FILE = "vm/awd-special-handling.vm";
	public static final String DEFAULT_WORK_TYPE = "SPECHANDL";

	@Override
	public String getClasspathTemplateFile() {		
		return TEMPLATE_FILE;
	}

	@Override
	public void preprocess(final SpecialhandlingRipObject ropObject) {
		if(!StringUtils.hasText(ropObject.getWorkType())) {
			ropObject.setWorkType(DEFAULT_WORK_TYPE);
		}
	}
	

}
