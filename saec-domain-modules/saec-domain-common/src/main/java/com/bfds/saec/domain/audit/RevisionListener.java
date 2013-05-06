package com.bfds.saec.domain.audit;

import com.bfds.saec.domain.util.AccountContext;


public class RevisionListener implements org.hibernate.envers.RevisionListener {

	public void newRevision(Object revisionEntity) {
		RevisionInfo exampleRevEntity = (RevisionInfo) revisionEntity;
		exampleRevEntity.setUserName(AccountContext.getCurrentUsername());		
	}
}
