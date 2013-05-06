/**
 * 
 */
package com.bfds.saec.batch.out.ifds_check_status;

import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

/**
 * @author dt83395
 * 
 */
public class IfdscheckStatusQueryProvider extends AbstractJpaQueryProvider {

	@Override
	public Query createQuery() {
		final Query query = this
				.getEntityManager()
				.createQuery(
						"select c from com.bfds.saec.domain.Payment c "
				                + "where "
								+ "(c.paymentCode =:s "
				                + "or c.paymentCode =:p "
								+ "or paymentCode=:vl "
				                + "or paymentCode=:l "
								+ "or c.paymentCode=:i1 "
				                + "or c.paymentCode=:i2 "
								+ "or c.paymentCode=:i3 "
								+ "or c.paymentCode=:i4 "
								+ "or c.paymentCode=:i5 "
								+ "or paymentCode=:vw "
								+ "or paymentCode=:r "
								+ "or paymentCode=:sw "
								+ "or paymentCode=:rfr "
								+ "or paymentCode=:rf "
								+ "or paymentCode=:rpr "
								+ "or paymentCode=:rp "
								+ "or paymentCode=:x "
								+ "or paymentCode=:w "
								+ "or paymentCode=:wa "
								+ "or paymentCode=:wr "
								+ "or paymentCode=:sj "
								+ "or paymentCode=:srr "
								+ "or paymentCode=:prr "
								+ "or paymentCode=:pj "
								+ "or paymentCode=:pra "
								+ "or paymentCode=:int) "
								+ "and ((c.paymentType = :paymentTypeCheck and c.identificatonNo is not null) or (c.paymentType = :paymentTypewire) or ( c.paymentType = :paymentTyperof)) "
								+ "and (c.ifdsSent = :ifdsentparam or c.ifdsSent is null) ");


		query.setParameter("s", PaymentCode.STOP_STOP_REQUESTED_S_SR);
		query.setParameter("srr", PaymentCode.STOP_REPLACE_REQUESTED_R_SRR);
		query.setParameter("sj", PaymentCode.STOP_STOP_REJECTED_SR_SJ);
		query.setParameter("p", PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR);
		query.setParameter("pra", PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA);
		query.setParameter("pj", PaymentCode.STOP_DAMASCO_STOP_REJECTED_SR_PJ);
		query.setParameter("prr", PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR);

		query.setParameter("vl", PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		query.setParameter("l", PaymentCode.STOP_LIFT_OUTSTANDING_L_L);

		query.setParameter("i1",
				PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
		query.setParameter("i2",
				PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA);
		query.setParameter("i3",
				PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA);
		query.setParameter("i4", PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA);
		query.setParameter("i5", PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);

		query.setParameter("vw", PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW);

		query.setParameter("r", PaymentCode.STOP_REPLACE_APPROVED_R_SRA);

		query.setParameter("sw",
				PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW);

		query.setParameter("rfr",
				PaymentCode.ROF_FULL_RESIDUAL_PROCESSED_RFR_RFR);
		query.setParameter("rf", PaymentCode.ROF_FULL_PROCESSED_RF_RF);
		query.setParameter("rpr",
				PaymentCode.ROF_PARTIAL_RESIDUAL_PROCESSED_RPR_RPR);
		query.setParameter("rp", PaymentCode.ROF_PARTIAL_PROCESSED_RP_RP);

		query.setParameter("x", PaymentCode.STALE_DATE_OUTSTANDING_X_X);

		query.setParameter("w", PaymentCode.WIRE_REQUESTED_W_W);

		query.setParameter("wa", PaymentCode.WIRE_APPROVED_WA_WA);

		query.setParameter("wr", PaymentCode.WIRE_REJECTED_WR_WR);
		query.setParameter("int", PaymentCode.ROF_INTEREST_PROCESSED_INT_INT);

		query.setParameter("paymentTypeCheck", PaymentType.CHECK);
		query.setParameter("paymentTypewire", PaymentType.WIRE);
		query.setParameter("paymentTyperof", PaymentType.ROF); 
		query.setParameter("ifdsentparam", Boolean.FALSE);
		return query;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}
}
