package com.bfds.saec.batch.tax_domestic.activityCreate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.ItemWriter;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.out.damasco_domestic.OutboundDomesticTaxRec;
import com.google.common.collect.Lists;

public class OutboundDomesticTaxRecWritter implements ItemWriter<OutboundDomesticTaxRec> {



	private EntityManagerFactory entityManagerFactory;


	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public void write(List<? extends OutboundDomesticTaxRec> items) throws Exception {
		saveOutboundDomesticTaxRec(items);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, value = "batchFilesTransactionManager")
	public void saveOutboundDomesticTaxRec(List<? extends OutboundDomesticTaxRec> items) {
		EntityManager entityManager = EntityManagerFactoryUtils
				.getTransactionalEntityManager(entityManagerFactory);
		if (entityManager == null) {
			throw new DataAccessResourceFailureException(
					"Unable to obtain a transactional EntityManager");
		}
        List<OutboundDomesticTaxRec> updatedItems = Lists.newArrayList();
        for (OutboundDomesticTaxRec rec : items) {
        	rec = entityManager.merge(rec);
            updatedItems.add(rec);
            rec.setCreatedActivity(Boolean.valueOf(true));
		}		
		entityManager.flush();
	}

}
