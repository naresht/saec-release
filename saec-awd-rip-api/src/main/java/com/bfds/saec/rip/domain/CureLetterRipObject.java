package com.bfds.saec.rip.domain;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.roo.addon.configurable.RooConfigurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", table="awd_rip_cure_letter")
@RooJavaBean
@RooConfigurable
@RooToString
public class CureLetterRipObject extends RipObject {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String referenceNo;

	private String registration1;

	private String registration2;

	private String registration3;

	private String registration4;

	private String registration5;

	private String registration6;

	private String address1;

	private String address2;

	private String address3;

	private String address4;

	private String address5;

	private String address6;

	private String city;

	private String stateCode;

	private String zipCode;

	private String zipExt;
	
	
	private String specialInstructions;
	
	@Column(nullable=false)
	private String letterCode;	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((address1 == null) ? 0 : address1.hashCode());
		result = prime * result
				+ ((address2 == null) ? 0 : address2.hashCode());
		result = prime * result
				+ ((address3 == null) ? 0 : address3.hashCode());
		result = prime * result
				+ ((address4 == null) ? 0 : address4.hashCode());
		result = prime * result
				+ ((address5 == null) ? 0 : address5.hashCode());
		result = prime * result
				+ ((address6 == null) ? 0 : address6.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((letterCode == null) ? 0 : letterCode.hashCode());
		result = prime * result
				+ ((referenceNo == null) ? 0 : referenceNo.hashCode());
		result = prime * result
				+ ((registration1 == null) ? 0 : registration1.hashCode());
		result = prime * result
				+ ((registration2 == null) ? 0 : registration2.hashCode());
		result = prime * result
				+ ((registration3 == null) ? 0 : registration3.hashCode());
		result = prime * result
				+ ((registration4 == null) ? 0 : registration4.hashCode());
		result = prime * result
				+ ((registration5 == null) ? 0 : registration5.hashCode());
		result = prime * result
				+ ((registration6 == null) ? 0 : registration6.hashCode());
		result = prime
				* result
				+ ((specialInstructions == null) ? 0 : specialInstructions
						.hashCode());
		result = prime * result
				+ ((stateCode == null) ? 0 : stateCode.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		result = prime * result + ((zipExt == null) ? 0 : zipExt.hashCode());
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
		CureLetterRipObject other = (CureLetterRipObject) obj;
		if (address1 == null) {
			if (other.address1 != null) {
				return false;
			}
		} else if (!address1.equals(other.address1)) {
			return false;
		}
		if (address2 == null) {
			if (other.address2 != null) {
				return false;
			}
		} else if (!address2.equals(other.address2)) {
			return false;
		}
		if (address3 == null) {
			if (other.address3 != null) {
				return false;
			}
		} else if (!address3.equals(other.address3)) {
			return false;
		}
		if (address4 == null) {
			if (other.address4 != null) {
				return false;
			}
		} else if (!address4.equals(other.address4)) {
			return false;
		}
		if (address5 == null) {
			if (other.address5 != null) {
				return false;
			}
		} else if (!address5.equals(other.address5)) {
			return false;
		}
		if (address6 == null) {
			if (other.address6 != null) {
				return false;
			}
		} else if (!address6.equals(other.address6)) {
			return false;
		}
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (letterCode != other.letterCode) {
			return false;
		}
		if (referenceNo == null) {
			if (other.referenceNo != null) {
				return false;
			}
		} else if (!referenceNo.equals(other.referenceNo)) {
			return false;
		}
		if (registration1 == null) {
			if (other.registration1 != null) {
				return false;
			}
		} else if (!registration1.equals(other.registration1)) {
			return false;
		}
		if (registration2 == null) {
			if (other.registration2 != null) {
				return false;
			}
		} else if (!registration2.equals(other.registration2)) {
			return false;
		}
		if (registration3 == null) {
			if (other.registration3 != null) {
				return false;
			}
		} else if (!registration3.equals(other.registration3)) {
			return false;
		}
		if (registration4 == null) {
			if (other.registration4 != null) {
				return false;
			}
		} else if (!registration4.equals(other.registration4)) {
			return false;
		}
		if (registration5 == null) {
			if (other.registration5 != null) {
				return false;
			}
		} else if (!registration5.equals(other.registration5)) {
			return false;
		}
		if (registration6 == null) {
			if (other.registration6 != null) {
				return false;
			}
		} else if (!registration6.equals(other.registration6)) {
			return false;
		}
		if (specialInstructions == null) {
			if (other.specialInstructions != null) {
				return false;
			}
		} else if (!specialInstructions.equals(other.specialInstructions)) {
			return false;
		}
		if (stateCode == null) {
			if (other.stateCode != null) {
				return false;
			}
		} else if (!stateCode.equals(other.stateCode)) {
			return false;
		}
		if (zipCode == null) {
			if (other.zipCode != null) {
				return false;
			}
		} else if (!zipCode.equals(other.zipCode)) {
			return false;
		}
		if (zipExt == null) {
			if (other.zipExt != null) {
				return false;
			}
		} else if (!zipExt.equals(other.zipExt)) {
			return false;
		}
		return true;
	}
	
}
