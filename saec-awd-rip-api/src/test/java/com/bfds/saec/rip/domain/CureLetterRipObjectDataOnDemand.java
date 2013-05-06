package com.bfds.saec.rip.domain;

import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity=CureLetterRipObject.class)
public class CureLetterRipObjectDataOnDemand {

    public void setStatus(CureLetterRipObject obj, int index) {
  
    	obj.setStatus(RipObjectStatus.NOT_SENT);
    }
}
