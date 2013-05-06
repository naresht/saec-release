/**
 * 
 */
package com.bfds.saec.batch.out.ifds_issue_void;

import java.util.Iterator;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.google.common.collect.Sets;


public class IfdsFrontierVoidQueryProvider extends AbstractJpaQueryProvider {
	@Override
	public Query createQuery() {
		final StringBuilder sb = new StringBuilder( "select c from com.bfds.saec.domain.Payment c where "+
													"c.paymentType = :paymentType"+
													" and (c.ifdsSent = :ifdsentparam or c.ifdsSent is null)"+
													" and c.identificatonNo is not null"+
													" and ( ");
		
		final Set<PaymentCode> codes = Sets.newHashSet();
		codes.add(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		codes.add(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
		codes.add(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
		codes.add(PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);
		codes.addAll(PaymentCodeUtil.getVoidedCodes());		
		codes.addAll(PaymentCodeUtil.getVoidReissueRequestedCodes());
		for(int i = 0; i < codes.size() ; i++ ) {
			sb.append(" c.paymentCode = :paymentCode_").append(i);
			if(i < codes.size() - 1) {
				sb.append(" or ");
			}
		}
		
		sb.append(")");
		
		final Query query = this.getEntityManager().createQuery(sb.toString());
		
		query.setParameter("paymentType", PaymentType.CHECK);
		query.setParameter("ifdsentparam",Boolean.FALSE);
		
		int index = 0;
		for(final Iterator<PaymentCode> itr = codes.iterator(); itr.hasNext();) {
			query.setParameter("paymentCode_"+(index++), itr.next());
		}
		return query;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}
}
