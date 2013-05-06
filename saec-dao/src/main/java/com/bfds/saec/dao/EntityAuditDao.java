package com.bfds.saec.dao;

import java.util.List;

import com.bfds.saec.domain.audit.EntityWithAuditVo;
import com.bfds.saec.domain.audit.RevisionInfo;

public interface EntityAuditDao {

	public abstract <E> List<EntityWithAuditVo<E>> getEntityVersionList(
			Class<E> type, Long id);

	public abstract <E> RevisionInfo getLastRevisionInfo(Class<E> type, Long id);

}