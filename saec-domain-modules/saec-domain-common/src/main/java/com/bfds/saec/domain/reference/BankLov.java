package com.bfds.saec.domain.reference;

import javax.persistence.DiscriminatorValue;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;


/**
 * This class will contain the Bank Code Information to be retrieved from database
 * •	Deutsche Bank = 021001033
	•	State Street = 011000028
	•	No bank required = Null

 * @author raj
 *
 */
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = { "findBankLovsByCode"})
@DiscriminatorValue("Bank")
public class BankLov extends Lov {


}
