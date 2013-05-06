package com.bfds.saec.domain;

import javax.persistence.DiscriminatorValue;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.domain.reference.Lov;

/**
 * This class will contain the Bank Code Information to be retrieved from database
	Settlement Amount
	Fees
	Interest
	Other Monies 1
	Other MOnies 2
	

 * @author raj
 *
 */
 
@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @DiscriminatorValue("PaymentComponentType")
public class PaymentComponentTypeLov extends Lov {


}
