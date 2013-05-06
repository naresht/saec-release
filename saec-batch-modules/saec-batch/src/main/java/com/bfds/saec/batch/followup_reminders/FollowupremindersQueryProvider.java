package com.bfds.saec.batch.followup_reminders;


import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;

public class FollowupremindersQueryProvider extends AbstractJpaQueryProvider {

	@Override
	public Query createQuery() {
		final StringBuilder sb = new StringBuilder("select c from com.bfds.wss.domain.ClaimantReminder c"); 
		
		final Query query = this.getEntityManager().createQuery(sb.toString());
		
		return query;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// NO-OP
	}
}
