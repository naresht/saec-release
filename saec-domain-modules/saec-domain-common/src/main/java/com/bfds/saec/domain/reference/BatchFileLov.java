package com.bfds.saec.domain.reference;

import javax.persistence.DiscriminatorValue;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;


/**
 * This class will contain the Bank Code Information to be retrieved from database
		Bank 
		IFDS  
		InfoAge 
		Damasco 
		DSTO 
		Encorr  
		RPO File 

 * @author raj
 *
 */
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @DiscriminatorValue("BatchFile")
public class BatchFileLov extends Lov {


}
