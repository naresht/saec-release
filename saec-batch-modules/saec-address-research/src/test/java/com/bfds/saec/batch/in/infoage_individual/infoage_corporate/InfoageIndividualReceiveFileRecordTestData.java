package com.bfds.saec.batch.in.infoage_individual.infoage_corporate;

import com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_corporate.HitIndicatorCorpType;
import com.bfds.saec.batch.file.domain.in.infoage_individual.HitIndicatorType;
import com.bfds.saec.batch.file.domain.in.infoage_individual.IndividualAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_individual.MatchIndicatorType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Transactional("batchFilesTransactionManager")
public class InfoageIndividualReceiveFileRecordTestData {


    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager entityManager;

    public void create() {
        createSuccessfulAddressResearchReturn();

        IndividualAddressResearch individualAddressResearch = newIndividualAddressResearch("100000006");
        individualAddressResearch.setHitIndicator(HitIndicatorType.N);
        individualAddressResearch.setMatchAnalysisTag("A");
        entityManager.persist(individualAddressResearch);
        entityManager.flush();
    }

    /**
     * Create {@link IndividualAddressResearch}s that represent a successful address reasearch.
     */
    private void createSuccessfulAddressResearchReturn() {
        IndividualAddressResearch individualAddressResearch = newIndividualAddressResearch("100000001");
        individualAddressResearch.setHitIndicator(HitIndicatorType.Y);
        entityManager.persist(individualAddressResearch);

        individualAddressResearch = newIndividualAddressResearch("100000002");
        individualAddressResearch.setHitIndicator(HitIndicatorType.Y);
        entityManager.persist(individualAddressResearch);

        individualAddressResearch = newIndividualAddressResearch("100000003");
        individualAddressResearch.setHitIndicator(HitIndicatorType.Y);
        entityManager.persist(individualAddressResearch);

        individualAddressResearch = newIndividualAddressResearch("100000004");
        individualAddressResearch.setHitIndicator(HitIndicatorType.Y);
        entityManager.persist(individualAddressResearch);

        individualAddressResearch = newIndividualAddressResearch("100000005");
        individualAddressResearch.setHitIndicator(HitIndicatorType.Y);
        entityManager.persist(individualAddressResearch);
    }

    /**
     * Created a new but incomplete {@link IndividualAddressResearch} for the given referenceNo. The client must set the following properties to make the object completed.
     * 1. {@link com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch#hitIndicator}
     * 2. {@link com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch#matchAnalysisTag} - This is optional
     * @param referenceNo - The Claimant referenceNo for which CorporateAddressResearch must be created.
     * @A new CorporateAddressResearch for the given referenceNo
     */
    private IndividualAddressResearch newIndividualAddressResearch(String referenceNo) {
        IndividualAddressResearch individualAddressResearch = new IndividualAddressResearch();
        individualAddressResearch.setUserRef(referenceNo);
        individualAddressResearch.setCity("city-1");
        individualAddressResearch.setState("state-1");
       //--- individualAddressResearch.setAddress("xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxx");
        //---individualAddressResearch.setCompanyName("Company -1");
        individualAddressResearch.setAddressDateReported("201201--");
        individualAddressResearch.setDda("11111111");
        // ---individualAddressResearch.setFein("22222222");
        //corporateAddressResearch.setOfacIndicator();
        //corporateAddressResearch.setMatchAnalysisTag();
        //corporateAddressResearch.setPartialAddressIndicator();
        ////////
        individualAddressResearch.setMatchIndicator(MatchIndicatorType.M);
        Calendar dob = Calendar.getInstance();
        dob.set(111, 1, 1);
        individualAddressResearch.setDateOfBirth(dob);
        individualAddressResearch.setHouseNumber("H-1234");
        individualAddressResearch.setPreDirection("PRE-DIR");
        individualAddressResearch.setThoroughfareName("TH-FARE-NM");
        individualAddressResearch.setPostDirection("PST-DIR");
        individualAddressResearch.setThoroughfareType("TH-FARE=-TYPE");
        individualAddressResearch.setApartmentNumber("301-C");
        //////
        individualAddressResearch.setPhone("11111111");
        individualAddressResearch.setPhoneAreaCode("01");
        individualAddressResearch.setPhoneExt("55");
        individualAddressResearch.setZipCode("02142-0001");
        return individualAddressResearch;
    }

    public void delete() {
        for(IndividualAddressResearch r : findAllFileRecords(IndividualAddressResearch.class) )  {
            entityManager.remove(r);
        }
    }

    public <T> List<T> findAllFileRecords(Class<T> requiredType) {
        return (List<T>) entityManager.createQuery(" from "+requiredType.getName()).getResultList();
    }
}
