package com.bfds.saec.batch.stale_date;

import java.util.Iterator;

import javax.persistence.Query;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.util.SaecDateUtils;


/**
 * QueryProvider to fetch all Checks ids, that can be stale dated. The Check objects are loaded in the writer. This is because, the {@link JpaPagingItemReader} flushes after every page read
 * and we will have to merge before we make any changes.
 *
 */
public class StaleDateQueryProvider extends AbstractJpaQueryProvider {

	@Override
	public Query createQuery() {
		final StringBuilder sb = new StringBuilder("select c.id from com.bfds.saec.domain.Payment c where c.paymentType = :paymentType "+
												   "and (c.staleDated is null or c.staleDated = :varFalse) " +
												   "and c.identificatonNo is not null " +
												   "and (c.staleByDate between :fromDate and :toDate) and (");
		
		for(int i=0; i < PaymentCodeUtil.getOutstandingCodes().size() ; i++ ) {
			sb.append(" c.paymentCode = :paymentCode_").append(i);
			if(i < PaymentCodeUtil.getOutstandingCodes().size() - 1) {
				sb.append(" or ");
			}
		}
		sb.append(")");
		
		final Query query = this.getEntityManager().createQuery(sb.toString());
		query.setParameter("paymentType", PaymentType.CHECK);
		query.setParameter("varFalse", Boolean.FALSE);
		query.setParameter("fromDate", SaecDateUtils.getMinIfNull(null));
		query.setParameter("toDate", SaecDateUtils.getDaysFromCurrent(-1));
		
		int index = 0;
		for(final Iterator<PaymentCode> itr = PaymentCodeUtil.getOutstandingCodes().iterator(); itr.hasNext();) {
			query.setParameter("paymentCode_"+(index++), itr.next());
		}
		
		return query;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// NO-OP
	}
}
