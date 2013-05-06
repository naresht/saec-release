package com.bfds.saec.batch.in.db_stop_confirmation;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.in.db_stop_confirmation.StopConfirmationRec;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class DbStopConfirmationFileRecordTestData {

    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager entityManager;

    @Transactional(value = "batchFilesTransactionManager")
	public void create() {
        StopConfirmationRec r = new StopConfirmationRec();
        r.setCheckNumber("4881918444");
        r.setDda("10001");
        r.setStopAmount(100d);
        r.setRejectCode("000");
        entityManager.persist(r);

        r = new StopConfirmationRec();
        r.setDda("10001");
        r.setCheckNumber("3608287156");
        r.setStopAmount(25.25d);
        r.setRejectCode("1");
        entityManager.persist(r);

        r = new StopConfirmationRec();
        r.setDda("10001");
        r.setCheckNumber("3608287157");
        r.setStopAmount(25.25d);
        r.setRejectCode("000");
        entityManager.persist(r);
        
        
        r = new StopConfirmationRec();
        r.setDda("10001");
        r.setCheckNumber("3608287158");
        r.setStopAmount(12.25d);
        r.setRejectCode("1");
        entityManager.persist(r);


        entityManager.flush();
	}
}
