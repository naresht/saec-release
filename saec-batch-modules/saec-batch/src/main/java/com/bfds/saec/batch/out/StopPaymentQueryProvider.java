package com.bfds.saec.batch.out;

import java.util.Iterator;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.google.common.collect.Sets;

/**
 * TODO Note this is common for both SS & Db
 */
public class StopPaymentQueryProvider extends AbstractJpaQueryProvider {

	@Override
	public Query createQuery() {
		final StringBuilder sb = new StringBuilder(
				"from com.bfds.saec.domain.Payment c "
						+ "where c.paymentType = :paymentType and "
						+ "c.identificatonNo is not null "
						+ "and (c.sentOnBankStopFile = :sentOnStopFile or c.sentOnBankStopFile is null)"
						+ "and (");

		final Set<PaymentCode> stopCodes = Sets.newHashSet();
		stopCodes.addAll(PaymentCodeUtil.getStopRequestedCodes());
		stopCodes.addAll(PaymentCodeUtil.getStopReplaceRequestedCodes());
		stopCodes.addAll(PaymentCodeUtil.getStopReplaceApprovedCodes());
		stopCodes.add(PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW);
		for (int i = 0; i < stopCodes.size(); i++) {
			sb.append(" c.paymentCode = :paymentCode_").append(i);
			if (i < stopCodes.size() - 1) {
				sb.append(" or ");
			}
		}
		sb.append(")");
		final Query query = this.getEntityManager().createQuery(sb.toString());
		query.setParameter("paymentType", PaymentType.CHECK);
		query.setParameter("sentOnStopFile", Boolean.FALSE);
		int index = 0;
		for (final Iterator<PaymentCode> itr = stopCodes.iterator(); itr
				.hasNext();) {
			query.setParameter("paymentCode_" + (index++), itr.next());
		}

		return query;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// NO-OP
	}
}
