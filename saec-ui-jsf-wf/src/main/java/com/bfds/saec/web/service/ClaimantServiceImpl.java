/*
 * (c) Copyright 2005-2011 JAXIO - Generated by Celerio, a Jaxio tool. http://www.jaxio.com
 */
package com.bfds.saec.web.service;

import com.bfds.saec.dao.ClaimantDao;
import com.bfds.saec.dao.EntityAuditDao;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.audit.EntityWithAuditVo;
import com.bfds.saec.domain.dto.ClaimantSearchCriteriaDto;
import com.bfds.saec.domain.dto.ClaimantSearchRecordDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Implementation of the ClaimantService interface
 */
@Service("claimantService")
public class ClaimantServiceImpl  implements ClaimantService {

    @SuppressWarnings("unused")
    final private Logger logger = LoggerFactory.getLogger(ClaimantServiceImpl.class);

    protected ClaimantDao claimantDao;
    
    @Autowired
    protected EntityAuditDao entityAuditDaoDao;
    

    @Autowired
    public void setClaimantDao(ClaimantDao claimantDao) {
        this.claimantDao = claimantDao;
    }

    /**
     * Dao getter used by the GenericEntityServiceImpl.
     */
    public ClaimantDao getDao() {
        return claimantDao;
    }
    
    public List<ClaimantSearchRecordDto> getClaimantSearchResults(final ClaimantSearchCriteriaDto param) {
    	return claimantDao.getClaimantSearchResults(param);
    }
    
    public List<EntityWithAuditVo<ClaimantAddress>> getAddressVersionList(Long addressId) {
    	return entityAuditDaoDao.getEntityVersionList(ClaimantAddress.class, addressId);
    	
    	
    }

	@Override
	public Long getClaimantSearchResultsCount(ClaimantSearchCriteriaDto param) {
		return claimantDao.getClaimantSearchResultsCount(param);
	}
}