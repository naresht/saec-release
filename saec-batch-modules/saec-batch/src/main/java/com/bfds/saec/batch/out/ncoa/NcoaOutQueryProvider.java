package com.bfds.saec.batch.out.ncoa;

import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;


public class NcoaOutQueryProvider  extends AbstractJpaQueryProvider {

	@Override
	public Query createQuery() {
		final Query query = this
        .getEntityManager()
        .createQuery("select c from Claimant c where (c.addressResearchSent = :addsent or c.addressResearchSent is null)");
		query.setParameter("addsent", Boolean.FALSE);
      return query;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}
}
