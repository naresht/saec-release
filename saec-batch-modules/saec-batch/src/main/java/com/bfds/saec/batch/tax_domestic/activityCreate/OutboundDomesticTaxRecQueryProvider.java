package com.bfds.saec.batch.tax_domestic.activityCreate;


import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

import com.bfds.saec.domain.Event;


public class OutboundDomesticTaxRecQueryProvider extends AbstractJpaQueryProvider {

    @Override
    public Query createQuery() {

        final StringBuilder sb = new StringBuilder(
                "from com.bfds.saec.batch.file.domain.out.damasco_domestic.OutboundDomesticTaxRec  i" +
                        " where i.processed = :processed " +
                        " and (i.createdActivity = :createdActivity or i.createdActivity is null)" +
                        " and i.dda = :dda");      
        
        final Query query = this.getEntityManager().createQuery(sb.toString());
        query.setParameter("processed", Boolean.TRUE);
        query.setParameter("createdActivity", Boolean.FALSE);
        query.setParameter("dda", Event.getCurrentEventDda());
        return query;
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub		
	}
    
   
}
