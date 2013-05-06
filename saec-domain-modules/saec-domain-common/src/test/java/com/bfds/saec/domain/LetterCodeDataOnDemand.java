package com.bfds.saec.domain;

import org.springframework.roo.addon.dod.RooDataOnDemand;

import com.bfds.saec.domain.reference.LetterType;

@RooDataOnDemand(entity = LetterCode.class)
public class LetterCodeDataOnDemand {
	
    public LetterCode getNewTransientLetterCode(int index) {
        LetterCode obj = new LetterCode("code_"+index, "desc_"+index, LetterType.CLAIM_FORM);
        return obj;
    }
}
