package com.bfds.saec.domain;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.id.IdentifierGenerator;
//import org.hibernate.impl.SessionFactoryImpl;
//import org.hibernate.impl.SessionImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.internal.SessionImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * 
 * See {@link Claimant#generateReferenceNumber()}
 * 
 * The {@link Claimant}s reference# must be generated when a new {@link Claimant} is created. The reference# is not the PK. 
 * We use and Entity just for the generation of reference#. Is there a better way ?
 */
@Entity
@Configurable
public class ClaimantReferenceNumber implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	@TableGenerator(name = "claimantReferenceNumberGen", table = "Claimant_Reference_Number_Gen", pkColumnName = "GEN_KEY", valueColumnName = "GEN_VALUE", pkColumnValue = "Claimant_Reference_Number_ID", allocationSize = 1, initialValue=1000)	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "claimantReferenceNumberGen")
	Long id;
    @PersistenceContext(unitName="entityManagerFactory")
    @Transient
    private EntityManager entityManager;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNextValue() {
		EntityPersister persister = ((SessionFactoryImpl)getCurrentSession(entityManager).getSessionFactory()).getEntityPersister( ClaimantReferenceNumber.class.getName() );
		IdentifierGenerator idGen =  persister.getIdentifierGenerator();
		ClaimantReferenceNumber refNum = new ClaimantReferenceNumber();		
		return (Long) idGen.generate((SessionImpl)getCurrentSession(entityManager), refNum);
	}
	
    private static Session getCurrentSession( EntityManager entityManager) {
        return (Session) entityManager.getDelegate();
    }
    

}
