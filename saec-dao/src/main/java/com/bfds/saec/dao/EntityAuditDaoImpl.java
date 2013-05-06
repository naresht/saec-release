package com.bfds.saec.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Repository;

import com.bfds.saec.domain.audit.EntityWithAuditVo;
import com.bfds.saec.domain.audit.RevisionInfo;
import com.bfds.saec.util.CollectionUtils;

@Repository("entityAuditDao")
public class EntityAuditDaoImpl implements EntityAuditDao {
	private EntityManager entityManager;

    @PersistenceContext(unitName="entityManagerFactory")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    
	/* (non-Javadoc)
	 * @see com.bfds.saec.dao.EntityAuditDao#getEntityVersionList(java.lang.Class, java.lang.Long)
	 */
	@Override
	public <E> List<EntityWithAuditVo<E>> getEntityVersionList( Class<E> type, Long id) {		
		AuditReader reader = AuditReaderFactory.get(getEntityManager());
		List<EntityWithAuditVo<E>> list = new ArrayList<EntityWithAuditVo<E>>();
		List<Number> revisions = reader.getRevisions(type, id);
		Map<Number, RevisionInfo> revisionMap = reader.findRevisions(RevisionInfo.class, new HashSet(revisions));
		for(Number revision : revisions) {
			list.add(new EntityWithAuditVo<E>(reader.find(type, id, revision), revisionMap.get(revision)));
		}
		return list;
	} 
	
	
	/* (non-Javadoc)
	 * @see com.bfds.saec.dao.EntityAuditDao#getLastRevisionInfo(java.lang.Class, java.lang.Long)
	 */
	@Override
	public <E> RevisionInfo getLastRevisionInfo( Class<E> type, Long id) {
		if(id == null){
			return null;
		}
		AuditReader reader = AuditReaderFactory.get(getEntityManager());
		Number lastRevision =(Number) CollectionUtils.max(reader.getRevisions(type, id));
		if(lastRevision != null) {
			return (RevisionInfo)reader.findRevision(type, lastRevision);
		}
		return null;
	} 	    
}
