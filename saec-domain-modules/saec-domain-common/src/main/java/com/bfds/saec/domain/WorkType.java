package com.bfds.saec.domain;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit = "entityManagerFactory")
public class WorkType implements java.io.Serializable{

	@Column(nullable=false)
	private String description;
	
	public WorkType(){
		
	}

	public WorkType(String description){
		this.description = description;		
	}
	
	public static WorkType findWorkTypeByDescription(String description) {
		if (description == null)
			throw new IllegalArgumentException(
					"The description argument is required");
		EntityManager em = WorkType.entityManager();
		TypedQuery<WorkType> q = em.createQuery("SELECT o FROM WorkType AS o WHERE o.description = :description",WorkType.class);
		q.setParameter("description", description);
		return q.getSingleResult();
	}
}
