package com.bfds.saec.rip.transformer;

import org.springframework.util.StringUtils;

import com.bfds.saec.rip.domain.StopReplaceCheckRipObject;

public class StopReplaceCheckRipObjectTransformer extends AbstractRipObjectTransformer<StopReplaceCheckRipObject> {

	private static final String TEMPLATE_FILE = "vm/awd-stop-replace-check.vm";
	private static final String DEFAULT_WORK_TYPE = "TCHKRPLACE";

	@Override
	public String getClasspathTemplateFile() {		
		return TEMPLATE_FILE;
	}

	@Override
	public void preprocess(final StopReplaceCheckRipObject ropObject) {
		if(!StringUtils.hasText(ropObject.getWorkType())) {
			ropObject.setWorkType(DEFAULT_WORK_TYPE);
		}
	}
	

}