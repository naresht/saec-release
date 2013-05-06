package com.bfds.saec.batch.in.db_cashed_check;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.in.db_cashed_check.CashedCheckRec;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class FileRecordTestData  {

    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager entityManager;

    @Transactional(value = "batchFilesTransactionManager")
	public void create() {

        CashedCheckRec r = new CashedCheckRec();
        r.setCheckNumber("4881918444");
        r.setCheckAmount(100d);
        r.setItemSeqNumber("1001");
        r.setDda("11111111"); // Have a record with a dda different from the current event. The record must not be processed.
        entityManager.persist(r);

        r = new CashedCheckRec();
        r.setCheckNumber("3608287156");
        r.setCheckAmount(25d);
        r.setItemSeqNumber("1002");
        r.setDda("11111111");
        entityManager.persist(r);
        
        r = new CashedCheckRec();
        r.setCheckNumber("3608287157");
        r.setCheckAmount(25d);
        r.setItemSeqNumber("1002");
        r.setDda("11111111");
        entityManager.persist(r);
        
        r = new CashedCheckRec();
        r.setCheckNumber("3608287158");
        r.setCheckAmount(25d);
        r.setItemSeqNumber("1002");
        r.setDda("11111111");
        entityManager.persist(r);
        
        r = new CashedCheckRec();
        r.setCheckNumber("3608287159");
        r.setCheckAmount(25d);
        r.setItemSeqNumber("1002");
        r.setDda("11111111");
        entityManager.persist(r);

        entityManager.flush();
	}
}
