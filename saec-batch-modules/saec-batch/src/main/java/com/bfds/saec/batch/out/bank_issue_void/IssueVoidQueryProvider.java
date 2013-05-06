package com.bfds.saec.batch.out.bank_issue_void;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

/**
 * Query Provider for Issue Void.
 *
 * This class is not thread safe.
 */
public class IssueVoidQueryProvider extends AbstractJpaQueryProvider {

    public static final String PARAM_TRANCH_CODE = "tranchCode";

    @Value("#{stepExecution}")
    protected StepExecution stepExecution;

    private String[] tranchCodes;

	@Override
	public Query createQuery() {

		final StringBuilder sb = new StringBuilder(
				"select c from com.bfds.saec.domain.Payment as c left join c.tranch as tranch" +
						 " where c.paymentType = :paymentType" +
						 " and (c.sentOnBankIssueVoidFile = :sentOnVoidFile or c.sentOnBankIssueVoidFile is null)");

        final Map<String, Object> queryParametes = Maps.newHashMap();

        int parameterIndex = 0;

        parameterIndex = applyTranchFilter(sb, queryParametes, parameterIndex);

        applyPaymentCodeFilter(sb, queryParametes, parameterIndex);

		final Query query = this.getEntityManager().createQuery(sb.toString());
		query.setParameter("paymentType", PaymentType.CHECK);
		query.setParameter("sentOnVoidFile", Boolean.FALSE);


		for(Map.Entry<String, Object> param : queryParametes.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

		return query;
	}

    private void applyPaymentCodeFilter(StringBuilder sb, Map<String, Object> queryParametes, int parameterIndex) {
        final List<PaymentCode> eligiblePaymentCodes = getEligiblePaymentCodes();

        sb.append(" and (");
        for (int i = 0; i < eligiblePaymentCodes.size(); i++) {
            String parameterName = "paymentCode_" + parameterIndex++;
            queryParametes.put(parameterName, eligiblePaymentCodes.get(i));
            sb.append(" c.paymentCode = :").append(parameterName);
            if (i < eligiblePaymentCodes.size() - 1) {
                sb.append(" or ");
            }
        }
        sb.append(")");
    }

    private List<PaymentCode> getEligiblePaymentCodes() {
        final List<PaymentCode> eligiblePaymentCodes = Lists.newArrayList(PaymentCodeUtil.getCreatedCodes().iterator());
        eligiblePaymentCodes.addAll(PaymentCodeUtil.getVoidedCodes());
        eligiblePaymentCodes.addAll(PaymentCodeUtil.getVoidReissueRequestedCodes());
        //voidCodes.addAll(PaymentCodeUtil.getVoidReissueApprovedCodes());
        eligiblePaymentCodes.add(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW);
        return eligiblePaymentCodes;
    }


    /**
     * Tranching applies to ony {@link com.bfds.saec.domain.reference.PaymentCode#FIRST_ISSUE_OUTSTANDING_FI_FIO} only.
     *
     * @param sb
     * @param queryParametes
     * @param parameterIndex
     * @return
     */
    private int applyTranchFilter(StringBuilder sb, Map<String, Object> queryParametes, int parameterIndex) {
        if(hasTranchFilter()) {
            sb.append(" and ( tranch.code in (");
            for(int i=0; i < tranchCodes.length ; i++ ) {
                String parameterName = "tranchCode_" + parameterIndex++;
                queryParametes.put(parameterName, tranchCodes[i]);
                sb.append(" :").append(parameterName);
                if(i < tranchCodes.length - 1) {
                    sb.append(" , ");
                }
            }
            String parameterName = "paymentCode_" + parameterIndex++;
            queryParametes.put(parameterName, PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
            sb.append(") or c.paymentCode not in ( :").append(parameterName).append("))");

        }
        return parameterIndex;
    }

    private boolean hasTranchFilter() {
        return !(tranchCodes.length == 1 && "all".equalsIgnoreCase(tranchCodes[0]));
    }

    @Override
	public void afterPropertiesSet() throws Exception {
        if(this.tranchCodes == null) {
            Preconditions.checkNotNull(stepExecution, "stepExecution is null");
            String val = stepExecution.getJobParameters().getString(PARAM_TRANCH_CODE);
            Preconditions.checkState(StringUtils.hasText(val), "tranchCode cannot be empty.");
            this.setTranchCodes(val.split(","));
        }
	}

    public void setTranchCodes(String[] tranchCodes) {
        Preconditions.checkNotNull(tranchCodes, "tranchCodes is null");
        this.tranchCodes = tranchCodes;
        for(int i= 0; i< tranchCodes.length; i++) {
            tranchCodes[i] = tranchCodes[i].trim();
        }
    }
}
