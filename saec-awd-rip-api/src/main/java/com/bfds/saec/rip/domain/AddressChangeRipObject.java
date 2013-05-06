package com.bfds.saec.rip.domain;

import javax.persistence.Column;

import org.springframework.roo.addon.configurable.RooConfigurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", table="awd_rip_address_change")
@RooJavaBean
@RooConfigurable
@RooToString
public class AddressChangeRipObject extends RipObject {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String referenceNo;

	private String fromAddressType;

	private String fromAddress1;

	private String fromAddress2;

	private String fromAddress3;

	private String fromAddress4;

	private String fromAddress5;

	private String fromAddress6;

	private String fromCity;

	private String fromStateCode;

	private String fromCountryCode;

	private String fromZipCode;

	private String fromZipExt;

	private String toAddressType;

	private String toAddress1;

	private String toAddress2;

	private String toAddress3;

	private String toAddress4;

	private String toAddress5;

	private String toAddress6;

	private String toCity;

	private String toStateCode;

	private String toCountryCode;

	private String toZipCode;

	private String toZipExt;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((fromAddress1 == null) ? 0 : fromAddress1.hashCode());
		result = prime * result
				+ ((fromAddress2 == null) ? 0 : fromAddress2.hashCode());
		result = prime * result
				+ ((fromAddress3 == null) ? 0 : fromAddress3.hashCode());
		result = prime * result
				+ ((fromAddress4 == null) ? 0 : fromAddress4.hashCode());
		result = prime * result
				+ ((fromAddress5 == null) ? 0 : fromAddress5.hashCode());
		result = prime * result
				+ ((fromAddress6 == null) ? 0 : fromAddress6.hashCode());
		result = prime * result
				+ ((fromAddressType == null) ? 0 : fromAddressType.hashCode());
		result = prime * result
				+ ((fromCity == null) ? 0 : fromCity.hashCode());
		result = prime * result
				+ ((fromCountryCode == null) ? 0 : fromCountryCode.hashCode());
		result = prime * result
				+ ((fromStateCode == null) ? 0 : fromStateCode.hashCode());
		result = prime * result
				+ ((fromZipCode == null) ? 0 : fromZipCode.hashCode());
		result = prime * result
				+ ((fromZipExt == null) ? 0 : fromZipExt.hashCode());
		result = prime * result
				+ ((referenceNo == null) ? 0 : referenceNo.hashCode());
		result = prime * result
				+ ((toAddress1 == null) ? 0 : toAddress1.hashCode());
		result = prime * result
				+ ((toAddress2 == null) ? 0 : toAddress2.hashCode());
		result = prime * result
				+ ((toAddress3 == null) ? 0 : toAddress3.hashCode());
		result = prime * result
				+ ((toAddress4 == null) ? 0 : toAddress4.hashCode());
		result = prime * result
				+ ((toAddress5 == null) ? 0 : toAddress5.hashCode());
		result = prime * result
				+ ((toAddress6 == null) ? 0 : toAddress6.hashCode());
		result = prime * result
				+ ((toAddressType == null) ? 0 : toAddressType.hashCode());
		result = prime * result + ((toCity == null) ? 0 : toCity.hashCode());
		result = prime * result
				+ ((toCountryCode == null) ? 0 : toCountryCode.hashCode());
		result = prime * result
				+ ((toStateCode == null) ? 0 : toStateCode.hashCode());
		result = prime * result
				+ ((toZipCode == null) ? 0 : toZipCode.hashCode());
		result = prime * result
				+ ((toZipExt == null) ? 0 : toZipExt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AddressChangeRipObject other = (AddressChangeRipObject) obj;
		if (fromAddress1 == null) {
			if (other.fromAddress1 != null) {
				return false;
			}
		} else if (!fromAddress1.equals(other.fromAddress1)) {
			return false;
		}
		if (fromAddress2 == null) {
			if (other.fromAddress2 != null) {
				return false;
			}
		} else if (!fromAddress2.equals(other.fromAddress2)) {
			return false;
		}
		if (fromAddress3 == null) {
			if (other.fromAddress3 != null) {
				return false;
			}
		} else if (!fromAddress3.equals(other.fromAddress3)) {
			return false;
		}
		if (fromAddress4 == null) {
			if (other.fromAddress4 != null) {
				return false;
			}
		} else if (!fromAddress4.equals(other.fromAddress4)) {
			return false;
		}
		if (fromAddress5 == null) {
			if (other.fromAddress5 != null) {
				return false;
			}
		} else if (!fromAddress5.equals(other.fromAddress5)) {
			return false;
		}
		if (fromAddress6 == null) {
			if (other.fromAddress6 != null) {
				return false;
			}
		} else if (!fromAddress6.equals(other.fromAddress6)) {
			return false;
		}
		if (fromAddressType == null) {
			if (other.fromAddressType != null) {
				return false;
			}
		} else if (!fromAddressType.equals(other.fromAddressType)) {
			return false;
		}
		if (fromCity == null) {
			if (other.fromCity != null) {
				return false;
			}
		} else if (!fromCity.equals(other.fromCity)) {
			return false;
		}
		if (fromCountryCode == null) {
			if (other.fromCountryCode != null) {
				return false;
			}
		} else if (!fromCountryCode.equals(other.fromCountryCode)) {
			return false;
		}
		if (fromStateCode == null) {
			if (other.fromStateCode != null) {
				return false;
			}
		} else if (!fromStateCode.equals(other.fromStateCode)) {
			return false;
		}
		if (fromZipCode == null) {
			if (other.fromZipCode != null) {
				return false;
			}
		} else if (!fromZipCode.equals(other.fromZipCode)) {
			return false;
		}
		if (fromZipExt == null) {
			if (other.fromZipExt != null) {
				return false;
			}
		} else if (!fromZipExt.equals(other.fromZipExt)) {
			return false;
		}
		if (referenceNo == null) {
			if (other.referenceNo != null) {
				return false;
			}
		} else if (!referenceNo.equals(other.referenceNo)) {
			return false;
		}
		if (toAddress1 == null) {
			if (other.toAddress1 != null) {
				return false;
			}
		} else if (!toAddress1.equals(other.toAddress1)) {
			return false;
		}
		if (toAddress2 == null) {
			if (other.toAddress2 != null) {
				return false;
			}
		} else if (!toAddress2.equals(other.toAddress2)) {
			return false;
		}
		if (toAddress3 == null) {
			if (other.toAddress3 != null) {
				return false;
			}
		} else if (!toAddress3.equals(other.toAddress3)) {
			return false;
		}
		if (toAddress4 == null) {
			if (other.toAddress4 != null) {
				return false;
			}
		} else if (!toAddress4.equals(other.toAddress4)) {
			return false;
		}
		if (toAddress5 == null) {
			if (other.toAddress5 != null) {
				return false;
			}
		} else if (!toAddress5.equals(other.toAddress5)) {
			return false;
		}
		if (toAddress6 == null) {
			if (other.toAddress6 != null) {
				return false;
			}
		} else if (!toAddress6.equals(other.toAddress6)) {
			return false;
		}
		if (toAddressType == null) {
			if (other.toAddressType != null) {
				return false;
			}
		} else if (!toAddressType.equals(other.toAddressType)) {
			return false;
		}
		if (toCity == null) {
			if (other.toCity != null) {
				return false;
			}
		} else if (!toCity.equals(other.toCity)) {
			return false;
		}
		if (toCountryCode == null) {
			if (other.toCountryCode != null) {
				return false;
			}
		} else if (!toCountryCode.equals(other.toCountryCode)) {
			return false;
		}
		if (toStateCode == null) {
			if (other.toStateCode != null) {
				return false;
			}
		} else if (!toStateCode.equals(other.toStateCode)) {
			return false;
		}
		if (toZipCode == null) {
			if (other.toZipCode != null) {
				return false;
			}
		} else if (!toZipCode.equals(other.toZipCode)) {
			return false;
		}
		if (toZipExt == null) {
			if (other.toZipExt != null) {
				return false;
			}
		} else if (!toZipExt.equals(other.toZipExt)) {
			return false;
		}
		return true;
	}
	
	
}
