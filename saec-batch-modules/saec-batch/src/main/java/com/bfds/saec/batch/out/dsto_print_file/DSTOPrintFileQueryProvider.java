package com.bfds.saec.batch.out.dsto_print_file;

import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;

public class DSTOPrintFileQueryProvider extends AbstractJpaQueryProvider {

	/**  
	 * Query to extract all letters which are having the malType as OUTGOING and dstoPrintFileSentFlag as FALSE,
	 * and lettercode range in between 1 to 100. Here we are not considering the lettertype while querying the records, 
	 * this is because lettertypes may vary for different events,so we will define those lettercodes in the range of 1 to 100 in event configuration.
	 */
	@Override
	public Query createQuery() {
		final Query query = this
				.getEntityManager()
				.createQuery(
						"select l from com.bfds.saec.domain.Letter l where l.mailType = :mailType and (l.dstoPrintFileSentFlag=:dstoprintfileflag " +
						"or l.dstoPrintFileSentFlag is null) and letterCode.code >= :letterCode1  and letterCode.code <= :letterCode2)");
		query.setParameter("mailType", MailType.OUTGOING);
		query.setParameter("dstoprintfileflag", Boolean.FALSE);
		query.setParameter("letterCode1", "001");
		query.setParameter("letterCode2", "100");

		return query;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// NO-OP
	}
}
