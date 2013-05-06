package com.bfds.saec.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.roo.addon.configurable.RooConfigurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.bfds.saec.domain.reference.PaymentComponentType;
import com.google.common.base.Preconditions;

@RooJavaBean
@RooConfigurable
@Embeddable
@RooToString
public class PaymentCalc implements java.io.Serializable, Cloneable, Comparable<PaymentCalc> {
    private static final long serialVersionUID = 1L;

    /**
     * The scale to use for aritematic operations.
     * @see {@link #DEFAULT_ROUNDING}
     */
    public static final int DEFAULT_SCALE = 2;

    /**
     * The rounding mode to use for aritematic operations.
     * @see {@link #DEFAULT_SCALE}
     */
    public static final int DEFAULT_ROUNDING = BigDecimal.ROUND_DOWN;

    private BigDecimal paymentComp1;
	private BigDecimal paymentComp2;
	private BigDecimal paymentComp3;
	private BigDecimal paymentComp4;
	private BigDecimal paymentComp5;
	private BigDecimal paymentComp6;
	
	private BigDecimal grossAmount;
	
	private BigDecimal stateWithholding;
	private BigDecimal fedWithholding;
	private BigDecimal nraWithholding;
	private BigDecimal intFedWithholding;
	private BigDecimal intStateWithholding;
	
	/**
	 * stateWitholding + fedWitholding + nraWitholding + intFedWithoding + intStateWithoding
	 */
	private BigDecimal backupWithholding;
	
	/**
	 * grossAmount - backupWithholding
	 */
	private BigDecimal nettAmount;
	
	private transient BeanWrapper beanWrapper;
	
	public void setGrossAmount(final BigDecimal grossAmount) {
		this.grossAmount = grossAmount;
		if(this.nettAmount == null || this.nettAmount == BigDecimal.ZERO) {
			this.nettAmount = this.grossAmount;
		}
	}
	
	public void setNettAmount(final BigDecimal nettAmount) {
		this.nettAmount = nettAmount;
		if(this.grossAmount == null || this.grossAmount == BigDecimal.ZERO) {
			this.grossAmount = this.nettAmount;
		}
	}
	
	/**
	 * Each of six payment components will have a description assigned at the event level. The value of the payment component associated with description is returned. 
	 * 
	 * @param description - The description given to the payment component. Cannot be null.
	 * 
	 * @return {@link BigDecimal} - one of the 6 payment components. 
	 * 
	 * @see {@link EventPaymentConfig}
	 * 
	 * @throws IllegalStateException if there is no mapping for the the given description
	 * @throws PropertyAccessException If a mapping exists but the property name derived from the mapping does not correspond to one of the properties of this class.  
	 *
	 */
	public BigDecimal getComponentValueByDescription(String description) {
		Preconditions.checkNotNull(description, "description is null");
		EventPaymentConfig config = EventPaymentConfig.findByDescription(description);
		Preconditions.checkState(config != null, "No EventPaymentConfig with desctiption  %s", description);			
		return (BigDecimal) getBeanWrapper().getPropertyValue(config.getPaymentComponentType().getCode());
	}
	
	/**
	 * @see #getComponentValueByDescription(String)
	 * 
	 * @param description
	 * @param val
	 */
	public void setComponentValueByDescription(String description, BigDecimal val) {
		Preconditions.checkNotNull(description, "description is null");
		EventPaymentConfig config = EventPaymentConfig.findByDescription(description);
		Preconditions.checkState(config != null, "No EventPaymentConfig with desctiption  %s", description);
		getBeanWrapper().setPropertyValue(config.getPaymentComponentType().getCode(), val);
	}
	
	/**
	 * 
	 * @param paymentComponentType - The property name of one of the 6 payment components
	 * @return The description associated with the property name
	 */
	public String getDescriptionByPaymentComponentType(PaymentComponentType paymentComponentType) {
		Preconditions.checkNotNull(paymentComponentType, "paymentComponentType is null");
		EventPaymentConfig config = EventPaymentConfig.findByPaymentComponentType(paymentComponentType);
		Preconditions.checkState(config != null, "No EventPaymentConfig with desctiption  %s", paymentComponentType);			
		return config.getDescription();
	}
	
	
	
	public BeanWrapper getBeanWrapper() {
		return beanWrapper == null ? PropertyAccessorFactory.forBeanPropertyAccess(this) : beanWrapper;
	}

	public void multiply(BigDecimal mult) {
		Preconditions.checkArgument(mult != null, "multiplicand cannot be null");
		this.paymentComp1 = mult( paymentComp1, mult);
		this.paymentComp2 = mult( paymentComp2, mult);
		this.paymentComp3 = mult( paymentComp3, mult);
		this.paymentComp4 = mult( paymentComp4, mult);
		this.paymentComp5 = mult( paymentComp5, mult);
		this.paymentComp6 = mult( paymentComp6, mult);
		this.grossAmount = mult( grossAmount, mult);		
		this.stateWithholding = mult( stateWithholding, mult);
		this.fedWithholding = mult( fedWithholding, mult);
		this.nraWithholding = mult( nraWithholding, mult);
		this.intFedWithholding = mult( intFedWithholding, mult);
		this.intStateWithholding = mult( intStateWithholding, mult);		
		this.backupWithholding = mult( backupWithholding, mult);
		this.nettAmount = mult( nettAmount, mult);
	}
	
	public void add(PaymentCalc calc) {
		Preconditions.checkArgument(calc != null, "PaymentCalc cannot be null");
		this.paymentComp1 = add( paymentComp1, calc.paymentComp1);
		this.paymentComp2 = add( paymentComp2, calc.paymentComp2);
		this.paymentComp3 = add( paymentComp3, calc.paymentComp3);
		this.paymentComp4 = add( paymentComp4, calc.paymentComp4);
		this.paymentComp5 = add( paymentComp5, calc.paymentComp5);
		this.paymentComp6 = add( paymentComp6, calc.paymentComp6);
		this.grossAmount = add( grossAmount, calc.grossAmount);		
		this.stateWithholding = add( stateWithholding, calc.stateWithholding);
		this.fedWithholding = add( fedWithholding, calc.fedWithholding);
		this.nraWithholding = add( nraWithholding, calc.nraWithholding);
		this.intFedWithholding = add( intFedWithholding, calc.intFedWithholding);
		this.intStateWithholding = add( intStateWithholding, calc.intStateWithholding);		
		this.backupWithholding = add( backupWithholding, calc.backupWithholding);
		this.nettAmount = add( nettAmount, calc.nettAmount);		
	}
	
	/**
	 * Compute the % of payment amount that equals val.
	 * {@link #nettAmount} is taken as the payment amount.
	 * @param val
	 * @return - % value.
	 */
	public BigDecimal percentageOfNettAmount(BigDecimal val) {
		Preconditions.checkArgument(val != null, "val cannot be null");
		if(nettAmount != null) {
            return round(val.multiply(new BigDecimal(100)).divide(nettAmount, DEFAULT_SCALE, DEFAULT_ROUNDING));
		}		
		throw new IllegalStateException("nettAmount ais null. cannot copute percentage.");		
	}
	
	/**
	 * Compares only the {@link #nettAmount}. If either of the {@link #nettAmount}s are null, the {@link #grossAmount}s are compared. 
	 * If either of the {@link #nettAmount}s are null, a comparison cannot be made.
	 * 
	 * @throws IllegalStateException if a comparison cannot be made.
	 */
	@Override
	public int compareTo(PaymentCalc calc) {
		Preconditions.checkArgument(calc != null, "PaymentCalc cannot be null");	

		if(nettAmount != null && calc.nettAmount != null) { 
			return nettAmount.compareTo(calc.nettAmount);
		}
		if(grossAmount != null && calc.grossAmount != null) { 
			return grossAmount.compareTo(calc.grossAmount);
		}
		throw new IllegalStateException("cannot compare between " + this + " and " + calc);
	}
	

	private BigDecimal add(BigDecimal to, BigDecimal val) {
		if(to == null && val == null) {
			return null;
		}
		if(val == null) {
			return to;
		}		
		if(to == null) {
			return val;
		}
		return round(to.add(val));
	}

	private BigDecimal round(BigDecimal subtract) {
		return subtract.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING);
	}

	private BigDecimal mult(BigDecimal val1, BigDecimal val2) {
		if(val1 == null || val2 == null) {
			return null;
		}		
		return round(val1.multiply(val2));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((backupWithholding == null) ? 0 : backupWithholding
						.hashCode());
		result = prime * result
				+ ((fedWithholding == null) ? 0 : fedWithholding.hashCode());
		result = prime * result
				+ ((grossAmount == null) ? 0 : grossAmount.hashCode());
		result = prime
				* result
				+ ((intFedWithholding == null) ? 0 : intFedWithholding
						.hashCode());
		result = prime
				* result
				+ ((intStateWithholding == null) ? 0 : intStateWithholding
						.hashCode());
		result = prime * result
				+ ((nettAmount == null) ? 0 : nettAmount.hashCode());
		result = prime * result
				+ ((nraWithholding == null) ? 0 : nraWithholding.hashCode());
		result = prime * result
				+ ((paymentComp1 == null) ? 0 : paymentComp1.hashCode());
		result = prime * result
				+ ((paymentComp2 == null) ? 0 : paymentComp2.hashCode());
		result = prime * result
				+ ((paymentComp3 == null) ? 0 : paymentComp3.hashCode());
		result = prime * result
				+ ((paymentComp4 == null) ? 0 : paymentComp4.hashCode());
		result = prime * result
				+ ((paymentComp5 == null) ? 0 : paymentComp5.hashCode());
		result = prime * result
				+ ((paymentComp6 == null) ? 0 : paymentComp6.hashCode());
		result = prime
				* result
				+ ((stateWithholding == null) ? 0 : stateWithholding.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentCalc other = (PaymentCalc) obj;
		if (backupWithholding == null) {
			if (other.backupWithholding != null)
				return false;
		} else if (!backupWithholding.equals(other.backupWithholding))
			return false;
		if (fedWithholding == null) {
			if (other.fedWithholding != null)
				return false;
		} else if (!fedWithholding.equals(other.fedWithholding))
			return false;
		if (grossAmount == null) {
			if (other.grossAmount != null)
				return false;
		} else if (!grossAmount.equals(other.grossAmount))
			return false;
		if (intFedWithholding == null) {
			if (other.intFedWithholding != null)
				return false;
		} else if (!intFedWithholding.equals(other.intFedWithholding))
			return false;
		if (intStateWithholding == null) {
			if (other.intStateWithholding != null)
				return false;
		} else if (!intStateWithholding.equals(other.intStateWithholding))
			return false;
		if (nettAmount == null) {
			if (other.nettAmount != null)
				return false;
		} else if (!nettAmount.equals(other.nettAmount))
			return false;
		if (nraWithholding == null) {
			if (other.nraWithholding != null)
				return false;
		} else if (!nraWithholding.equals(other.nraWithholding))
			return false;
		if (paymentComp1 == null) {
			if (other.paymentComp1 != null)
				return false;
		} else if (!paymentComp1.equals(other.paymentComp1))
			return false;
		if (paymentComp2 == null) {
			if (other.paymentComp2 != null)
				return false;
		} else if (!paymentComp2.equals(other.paymentComp2))
			return false;
		if (paymentComp3 == null) {
			if (other.paymentComp3 != null)
				return false;
		} else if (!paymentComp3.equals(other.paymentComp3))
			return false;
		if (paymentComp4 == null) {
			if (other.paymentComp4 != null)
				return false;
		} else if (!paymentComp4.equals(other.paymentComp4))
			return false;
		if (paymentComp5 == null) {
			if (other.paymentComp5 != null)
				return false;
		} else if (!paymentComp5.equals(other.paymentComp5))
			return false;
		if (paymentComp6 == null) {
			if (other.paymentComp6 != null)
				return false;
		} else if (!paymentComp6.equals(other.paymentComp6))
			return false;
		if (stateWithholding == null) {
			if (other.stateWithholding != null)
				return false;
		} else if (!stateWithholding.equals(other.stateWithholding))
			return false;
		return true;
	}

	@Override
	public PaymentCalc clone() {
		try {
			return (PaymentCalc) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
	}	

}
