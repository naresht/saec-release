package com.bfds.saec.batch.out.foreign_tax_process;

import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

public class ForeignTaxProcesOutQueryProvider  extends AbstractJpaQueryProvider {

	@Override
	public Query createQuery() {
		final Query query = this
        .getEntityManager()
        .createQuery("select p from com.bfds.saec.domain.Payment p inner join p.payTo as c where c.taxInfo.certificationType <> :certificationType "+
        						"and c.taxInfo.foreignTax = :foreignTax and p.identificatonNo is not null and (p.sentOnTaxFile =:taxprocessed or p.sentOnTaxFile is null)");
		query.setParameter("certificationType", "W9-US Citizen");
		query.setParameter("foreignTax", Boolean.TRUE);
		query.setParameter("taxprocessed", Boolean.FALSE);
      return query;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}
}
