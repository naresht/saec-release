package com.bfds.saec.batch.uploaded_doc_ripper.hydroxycut;

import com.bfds.saec.batch.uploaded_doc_ripper.AbstractUploadedDocServiceImpl;
import com.bfds.wss.domain.ClaimFileUploaded;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This is specific to the HydroxyCut event.
 *
 * This class is not thread safe.
 *
 */
@Service
public class UploadedDocServiceImpl extends AbstractUploadedDocServiceImpl {

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    private Map<Long, String> unitsPurchasedByClaimId = Maps.newHashMap();

    @Override
    protected void preProcess(List<? extends ClaimFileUploaded> claimFileUploadedList) {
        unitsPurchasedByClaimId.clear();
        if(claimFileUploadedList.size() > 1000) {
            throw new IllegalStateException("Cannot support a batch size > 1000. Size is : "+ claimFileUploadedList.size());
        }


        StringBuilder sb = new StringBuilder("select o.claimUserResponseGroup.claimantClaim.id , o.responseText from com.bfds.wss.domain.ClaimantUserResponses o" +
                                                " where o.additionalQuestions.questionCode = 'EU-NO-UNITS'" +
                                                " and o.claimUserResponseGroup.claimantClaim.id in (");

        for(int i=0; i < claimFileUploadedList.size() ; i++ ) {
            unitsPurchasedByClaimId.put(claimFileUploadedList.get(i).getClaimantClaim().getId(), null);
        }
        for(int i=0; i < unitsPurchasedByClaimId.size() ; i++ ) {
            sb.append(" :claimantClaimId_").append(i);
            if(i < claimFileUploadedList.size() - 1) {
                sb.append(" , ");
            }
        }
        sb.append(")");

        final Query query = entityManager.createQuery(sb.toString());
        int index = 0;
        for(final Iterator<Long> itr = unitsPurchasedByClaimId.keySet().iterator(); itr.hasNext();) {
            query.setParameter("claimantClaimId_"+(index++), itr.next());
        }

        List<Object[]> list = query.getResultList();

        for(Object[] rec : list) {
            unitsPurchasedByClaimId.put((Long) rec[0], (String) rec[1]);
        }
    }

    @Override
    protected String getWorkTypeForClaimForm(ClaimFileUploaded claimFileUploaded) {
        if(!ClaimFileUploaded.CLAIM_FORM_NAME.equals(claimFileUploaded.getUploadedFileName())) {
            return "DOCS";
        }
        String strUnitsPurchased = unitsPurchasedByClaimId.get(claimFileUploaded.getClaimantClaim().getId());
        int unitsPurchased = 0;
        try{
            unitsPurchased = Integer.parseInt(strUnitsPurchased);
        }catch(Exception e) {
            log.warn(String.format("Error converting units purchased for claim: %s. unitsPurchased is %s", claimFileUploaded.getClaimantClaim().getId(), unitsPurchased));
        }
        if(unitsPurchased == 1) {
            return "WEBCLAIM1";
        }
        return "WEBCLAIM2";
    }

    @Override
    protected void postProcess(List<? extends ClaimFileUploaded> claimFileUploadedList) {
    }

    @Override
    protected void preProcess(ClaimFileUploaded claimFileUploaded) {

    }

    @Override
    protected void postProcess(ClaimFileUploaded claimFileUploaded) {

    }
}
