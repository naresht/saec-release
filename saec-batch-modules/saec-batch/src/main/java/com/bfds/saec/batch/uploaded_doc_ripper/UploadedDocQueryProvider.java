package com.bfds.saec.batch.uploaded_doc_ripper;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.util.SaecDateUtils;
import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

import javax.persistence.Query;
import java.util.Iterator;


public class UploadedDocQueryProvider extends AbstractJpaQueryProvider {

	@Override
	public Query createQuery() {
		final StringBuilder sb = new StringBuilder("select c from com.bfds.wss.domain.ClaimFileUploaded c where c.dateRipped is null");
		final Query query = this.getEntityManager().createQuery(sb.toString());
		return query;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// NO-OP
	}
}
