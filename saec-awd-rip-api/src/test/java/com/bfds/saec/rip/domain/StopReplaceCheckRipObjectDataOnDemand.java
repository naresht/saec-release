package com.bfds.saec.rip.domain;

import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity=StopReplaceCheckRipObject.class)
public class StopReplaceCheckRipObjectDataOnDemand {

    public void setStatus(StopReplaceCheckRipObject obj, int index) {
        
        obj.setStatus(RipObjectStatus.NOT_SENT);
    }
  
}
