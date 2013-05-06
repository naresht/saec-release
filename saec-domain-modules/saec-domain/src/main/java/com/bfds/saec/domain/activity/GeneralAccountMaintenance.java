package com.bfds.saec.domain.activity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.bfds.saec.util.SaecStringUtils;

@Embeddable
public class GeneralAccountMaintenance  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fundAccountNo;
	
	 private String accountStatus;
	 
	 private String accountType;

	 private String brokerId;
	 
	 private String bin;
	 
	// private Boolean omniBus;
	 
	 //private Boolean organization;

	public GeneralAccountMaintenance() {}

	public GeneralAccountMaintenance(com.bfds.saec.domain.Claimant claimant) {
		super();
		this.fundAccountNo = claimant.getFundAccountNo();
		this.accountStatus = claimant.getAccountStatus();
		this.accountType = claimant.getAccountType();
		this.brokerId = claimant.getBrokerId();
		this.bin = claimant.getBin();
		//this.omniBus = claimant.getOmniBus();
		//this.organization = claimant.getOrganization();
	}
	
	public String getAsString() {
		String[] values = new String[5];
		values[0] = fundAccountNo;
		values[1] = accountStatus;
		values[2] = accountType;
		values[3] = brokerId;
		values[4] = bin;
		
		return SaecStringUtils.getArrayString(values, ", ");
	}

	public String getFundAccountNo() {
		return fundAccountNo;
	}

	public void setFundAccountNo(String fundAccountNo) {
		this.fundAccountNo = fundAccountNo;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 * 
	 * Generated method. Do not hand edit.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountStatus == null) ? 0 : accountStatus.hashCode());
		result = prime * result
				+ ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((bin == null) ? 0 : bin.hashCode());
		result = prime * result
				+ ((brokerId == null) ? 0 : brokerId.hashCode());
		result = prime * result
				+ ((fundAccountNo == null) ? 0 : fundAccountNo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * Generated method. Do not hand edit.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneralAccountMaintenance other = (GeneralAccountMaintenance) obj;
		if (accountStatus == null) {
			if (other.accountStatus != null)
				return false;
		} else if (!accountStatus.equals(other.accountStatus))
			return false;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (bin == null) {
			if (other.bin != null)
				return false;
		} else if (!bin.equals(other.bin))
			return false;
		if (brokerId == null) {
			if (other.brokerId != null)
				return false;
		} else if (!brokerId.equals(other.brokerId))
			return false;
		if (fundAccountNo == null) {
			if (other.fundAccountNo != null)
				return false;
		} else if (!fundAccountNo.equals(other.fundAccountNo))
			return false;
		return true;
	}

	
	
}
