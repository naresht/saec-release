package com.bfds.saec.rip.transformer;

import java.util.Map;

import org.springframework.util.StringUtils;

import com.bfds.saec.rip.domain.CureLetterRipObject;
import com.bfds.saec.rip.domain.FollowupRemindersRipObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class FollowupRemindersRipObjectTransformer extends AbstractRipObjectTransformer<FollowupRemindersRipObject> {

	private static final String TEMPLATE_FILE = "vm/awd-followup-reminders.vm";
	
	@Override
	public String getClasspathTemplateFile() {				
		return TEMPLATE_FILE;
	}

	@Override
	public void preprocess(final FollowupRemindersRipObject ropObject) {
		if(!StringUtils.hasText(ropObject.getWorkType())) {
			ropObject.setWorkType("Reject Email");
		}
	}
}

