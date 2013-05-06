package com.bfds.saec.batch.in.infoage_corporate;

import com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_corporate.HitIndicatorCorpType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
@Transactional("batchFilesTransactionManager")
public class InfoageCorporateReceiveFileRecordTestData {

    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager entityManager;

    public void create() {
        createSuccessfulAddressResearchReturn();

        CorporateAddressResearch corporateAddressResearch = newCorporateAddressResearch("100000006");
        corporateAddressResearch.setHitIndicator(HitIndicatorCorpType.N);
        corporateAddressResearch.setMatchAnalysisTag("A");
        entityManager.persist(corporateAddressResearch);
        entityManager.flush();
    }

    /**
     * Create {@link CorporateAddressResearch}s that represent a successful address reasearch.
     */
    private void createSuccessfulAddressResearchReturn() {
        CorporateAddressResearch corporateAddressResearch = newCorporateAddressResearch("100000001");
        corporateAddressResearch.setHitIndicator(HitIndicatorCorpType.Y);
        entityManager.persist(corporateAddressResearch);

        corporateAddressResearch = newCorporateAddressResearch("100000002");
        corporateAddressResearch.setHitIndicator(HitIndicatorCorpType.Y);
        entityManager.persist(corporateAddressResearch);

        corporateAddressResearch = newCorporateAddressResearch("100000003");
        corporateAddressResearch.setHitIndicator(HitIndicatorCorpType.Y);
        entityManager.persist(corporateAddressResearch);

        corporateAddressResearch = newCorporateAddressResearch("100000004");
        corporateAddressResearch.setHitIndicator(HitIndicatorCorpType.Y);
        entityManager.persist(corporateAddressResearch);

        corporateAddressResearch = newCorporateAddressResearch("100000005");
        corporateAddressResearch.setHitIndicator(HitIndicatorCorpType.Y);
        entityManager.persist(corporateAddressResearch);
    }

    /**
     * Created a new but incomplete {@link CorporateAddressResearch} for the given referenceNo. The client must set the following properties to make the object completed.
     * 1. {@link CorporateAddressResearch#hitIndicator}
     * 2. {@link CorporateAddressResearch#matchAnalysisTag} - This is optional
     * @param referenceNo - The Claimant referenceNo for which CorporateAddressResearch must be created.
     * @A new CorporateAddressResearch for the given referenceNo
     */
    private CorporateAddressResearch newCorporateAddressResearch(String referenceNo) {
        CorporateAddressResearch corporateAddressResearch = new CorporateAddressResearch();
        corporateAddressResearch.setUserRef(referenceNo);
        corporateAddressResearch.setCity("city-1");
        corporateAddressResearch.setState("state-1");
        corporateAddressResearch.setAddress("xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxx");
        corporateAddressResearch.setCompanyName("Company -1");
        corporateAddressResearch.setAddressDateReported("201201--");
        corporateAddressResearch.setDda("11111111");
        corporateAddressResearch.setFein("22222222");
        //corporateAddressResearch.setOfacIndicator();
        //corporateAddressResearch.setMatchAnalysisTag();
        //corporateAddressResearch.setPartialAddressIndicator();
        corporateAddressResearch.setPhone("11111111");
        corporateAddressResearch.setPhoneAreaCode("01");
        corporateAddressResearch.setPhoneExt("55");
        corporateAddressResearch.setZipCode("02142-0001");
        return corporateAddressResearch;
    }

    public void delete() {
        for(CorporateAddressResearch r : findAllFileRecords(CorporateAddressResearch.class))  {
            entityManager.remove(r);
        }
    }


    public <T> List<T> findAllFileRecords(Class<T> requiredType) {
        return (List<T>) entityManager.createQuery(" from "+requiredType.getName()).getResultList();
    }

}
