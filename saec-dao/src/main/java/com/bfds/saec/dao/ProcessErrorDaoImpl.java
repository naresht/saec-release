package com.bfds.saec.dao;

import java.util.List;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.ProcessError;
import com.bfds.saec.domain.reference.RPOType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Dao for ProcessError CRUD
 */
@Repository("processErrorDao")
public class ProcessErrorDaoImpl implements ProcessErrorDao {

	private Logger logger = LoggerFactory.getLogger(ProcessErrorDaoImpl.class);

	@PersistenceContext(unitName="entityManagerFactory")
	private EntityManager em;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(ProcessError error) {
		this.em.persist(error);
		this.em.flush();
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public ProcessError read(Long id) {
		return em.find(ProcessError.class, id);
	}	
}
