package com.bfds.saec.rip.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
public class ClaimFormSupportingDocRipObject extends RipObject {
	
	private static final long serialVersionUID = 1L;
	

	private String claimIdentifier;

	private String controlNo;

    private String fileName;

    private String fileExt;

    private String filePath;

}
