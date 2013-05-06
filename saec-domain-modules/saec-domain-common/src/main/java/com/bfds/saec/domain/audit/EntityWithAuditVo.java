package com.bfds.saec.domain.audit;

import java.util.Date;

public final class EntityWithAuditVo<E> implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private final E e;
	private final RevisionInfo revisionInfo;

	public EntityWithAuditVo(final E e, final RevisionInfo revisionInfo) {
		super();
		this.e = e;
		this.revisionInfo = revisionInfo;
	}

	public E getEntity() {
		return e;
	}

	public String getUserName() {
		return revisionInfo.getUserName();
	}

	public Date getRevisionDate() {
		return revisionInfo.getRevisionDate();
	}

	public RevisionInfo getRevisionInfo() {
		return revisionInfo;
	}
	
	

}