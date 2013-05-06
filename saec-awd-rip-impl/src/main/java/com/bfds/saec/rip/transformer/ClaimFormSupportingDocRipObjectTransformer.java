package com.bfds.saec.rip.transformer;

import com.bfds.saec.rip.domain.ClaimFormSupportingDocRipObject;
import com.bfds.saec.rip.domain.SpecialhandlingRipObject;
import org.springframework.util.StringUtils;


public class ClaimFormSupportingDocRipObjectTransformer extends AbstractRipObjectTransformer<ClaimFormSupportingDocRipObject> {

	private static final String TEMPLATE_FILE = "vm/awd-claim-supporting-doc.vm";

	@Override
	public String getClasspathTemplateFile() {		
		return TEMPLATE_FILE;
	}
}
