package com.bfds.saec.rip.transformer;

import org.springframework.util.StringUtils;

import com.bfds.saec.rip.domain.AddressChangeRipObject;


public class AddressChangeRipObjectTransformer extends AbstractRipObjectTransformer<AddressChangeRipObject> {

	private static final String TEMPLATE_FILE = "vm/awd-address-change.vm";
	public static final String DEFAULT_WORK_TYPE = "TADDRSCHNG";

	@Override
	public String getClasspathTemplateFile() {		
		return TEMPLATE_FILE;
	}

	@Override
	public void preprocess(final AddressChangeRipObject ropObject) {
		if(!StringUtils.hasText(ropObject.getWorkType())) {
			ropObject.setWorkType(DEFAULT_WORK_TYPE);
		}
	}
	

}
