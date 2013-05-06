package com.bfds.saec.batch.out.infoage;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.batch.jobParameters.AccountType;
import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.RPOType;
import com.google.common.base.Preconditions;

/**
 * Selection Criteria
 */
@RooJavaBean
public class AddressResearchClaimantSelectionCriteria extends AbstractJpaQueryProvider {

    final Logger log = LoggerFactory.getLogger(AddressResearchClaimantSelectionCriteria.class);
    
    @Value("#{jobParameters['researchType']}")
    private String researchType;
    
    @Value("#{jobParameters['accountType']}")
    private String accountType;
   
    @Override
    public Query createQuery() {    	
    	
    	if(ResearchType.PRESCRUB.equals(researchType)) {
    		return preScrubQuery();
    	}
    	
    	return researchQuery();
       
    }

	private Query researchQuery() {
		final Query query = this
                .getEntityManager()
                .createQuery("select c from Claimant c " +
                        " where " +
                        " c.corporate = :isCorporate"+
                        " and (c.addressResearchSent = :addressResearchSent or c.addressResearchSent is null) " +
                        "and (exists (select cm from Payment cm where cm.payTo = c and cm.paymentCode=:paymentRpoType )" +
                        " or exists (select l from Letter l where l.claimant = c and l.rpoType =:letterRpoType ))")
                .setParameter("paymentRpoType", PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN)
                .setParameter("letterRpoType", RPOType.NONFORWARDABLE)
                .setParameter("addressResearchSent", Boolean.FALSE)
                .setParameter("isCorporate", AccountType.CORPORATE.equals(accountType) ? true : false);
        
        return query;
	}

	private Query preScrubQuery() {
		final Query query = this
                .getEntityManager()
                .createQuery(
                        "select c from Claimant c where c.corporate = :corporate")
                .setParameter("corporate", AccountType.CORPORATE.equals(accountType) ? true : false);
       
        return query;
	}

    @Override
    public void afterPropertiesSet() throws Exception {
    	Preconditions.checkState(researchType != null, "research type is null");
    	Preconditions.checkState(accountType != null, "accountType type is null");
    	Preconditions.checkState(ResearchType.PRESCRUB.equals(researchType) || ResearchType.RESEARCH.equals(researchType), "unsupported research type : %s", researchType);
    	Preconditions.checkState(AccountType.CORPORATE.equals(accountType) || AccountType.INDIVIDUAL.equals(accountType), "unsupported accountType type : %s", accountType);
    }
}
