package com.bfds.saec.tranch.service;

/**
 * All Exceptions during tranch assignment.
 *
 */
public class TranchException  extends RuntimeException {

	private static final long serialVersionUID = 1L;
	/**
	 * Constructor for TranchException.
	 * @param msg the detail message
	 */
	
	private String[] detailMsgs; 
	
	public TranchException(String msg, String... detailMsgs) {
		super(msg);
		this.detailMsgs = detailMsgs;
	}

	public String[] getDetailMsgs() {
		return detailMsgs;
	}
	
}
