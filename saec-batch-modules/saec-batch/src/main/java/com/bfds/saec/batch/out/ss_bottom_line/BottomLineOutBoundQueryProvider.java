package com.bfds.saec.batch.out.ss_bottom_line;


import java.util.Iterator;

import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;

public class BottomLineOutBoundQueryProvider extends AbstractJpaQueryProvider {
	@Override
	public Query createQuery() {
		final StringBuilder sb = new StringBuilder(
                "select c from com.bfds.saec.domain.Payment c" +
                " where (c.sentOnBottomLineFile = :sentOnBottomLineFile or c.sentOnBottomLineFile is null) " +
                " and c.paymentType = :paymentType and c.identificatonNo is null and (");
		
		
		for(int i=0; i < PaymentCodeUtil.getCreatedCodes().size() ; i++ ) {
			sb.append(" c.paymentCode = :paymentCode_").append(i);
			if(i < PaymentCodeUtil.getCreatedCodes().size() - 1) {
				sb.append(" or ");
			}
		}
		sb.append(")");
		final Query query = this.getEntityManager().createQuery(sb.toString());
        query.setParameter("sentOnBottomLineFile", Boolean.FALSE);		
		query.setParameter("paymentType", PaymentType.CHECK);
		
		int index = 0;
		for(final Iterator<PaymentCode> itr = PaymentCodeUtil.getCreatedCodes().iterator(); itr.hasNext();) {
			query.setParameter("paymentCode_"+(index++), itr.next());
		}
		return query;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

}
