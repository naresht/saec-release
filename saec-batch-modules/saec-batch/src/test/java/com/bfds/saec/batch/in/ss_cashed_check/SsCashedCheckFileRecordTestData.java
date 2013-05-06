package com.bfds.saec.batch.in.ss_cashed_check;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.in.ss_cashed_check.SsCashedCheckRec;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class SsCashedCheckFileRecordTestData {

    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager entityManager;

    @Transactional(value = "batchFilesTransactionManager")
	public void create() {
        SsCashedCheckRec r = new SsCashedCheckRec();
        r.setCheckNumber("1");
        r.setCheckAmount(100d);
        r.setDda("11111111");
        entityManager.persist(r);

        r = new SsCashedCheckRec();
        r.setCheckNumber("2");
        r.setCheckAmount(200d);
        r.setDda("11111111");
        entityManager.persist(r);
        
        r = new SsCashedCheckRec();
        r.setCheckNumber("3");
        r.setCheckAmount(300d);
        r.setDda("11111111");
        entityManager.persist(r);

        entityManager.flush();
	}
}
