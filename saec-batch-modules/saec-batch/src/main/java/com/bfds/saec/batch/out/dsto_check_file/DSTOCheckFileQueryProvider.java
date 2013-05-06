package com.bfds.saec.batch.out.dsto_check_file;

import javax.persistence.Query;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

public class DSTOCheckFileQueryProvider extends AbstractJpaQueryProvider {

    public static final String PARAM_PICK_FIRST_ISSUANCES = "pickFirstIssuances";

    @Value("#{stepExecution}")
    protected StepExecution stepExecution;

    private Boolean pickFirstIssuances;

	@Override
	public Query createQuery() {
		final StringBuilder sb = new StringBuilder(
						"select c from com.bfds.saec.domain.Payment c where c.paymentType = :paymentType " +
						"and (c.dstoCheckFileSentFlag=:dstocheckfileflag or c.dstoCheckFileSentFlag is null) " +
						"and c.identificatonNo is not null " +
                        "and c.paymentCode in ( " );

        final Map<String, Object> queryParametes = Maps.newHashMap();
        queryParametes.put("paymentType", PaymentType.CHECK);
        queryParametes.put("dstocheckfileflag", Boolean.FALSE);
        int parameterIndex = 0;

        List<PaymentCode> paymentCodes = Lists.newArrayList();

        if(Boolean.TRUE.equals(pickFirstIssuances)) {
            paymentCodes.add(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
        }  else {
            paymentCodes.add(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
            paymentCodes.add(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
        }

        for(int i=0; i < paymentCodes.size() ; i++ ) {
            String parameterName = "paymentCode_" + parameterIndex++;
            queryParametes.put(parameterName, paymentCodes.get(i));
            sb.append(" :").append(parameterName);
            if(i < paymentCodes.size() - 1) {
                sb.append(" , ");
            }
        }
        sb.append(") ");

        final Query query = this.getEntityManager().createQuery(sb.toString() );

        for(Map.Entry<String, Object> param : queryParametes.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
		return query;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
        if(this.pickFirstIssuances == null) {
            Preconditions.checkNotNull(stepExecution, "stepExecution is null");
            String val = stepExecution.getJobParameters().getString(PARAM_PICK_FIRST_ISSUANCES);
            Preconditions.checkState("true".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val), "pickFirstIssuances job parameter must be present and must have a value of 'true' or 'false'. Value is %s.", val);
            this.setPickFirstIssuances(Boolean.valueOf(val));
        }
	}

    public void setPickFirstIssuances(Boolean pickFirstIssuances) {
        this.pickFirstIssuances = pickFirstIssuances;
    }
}
