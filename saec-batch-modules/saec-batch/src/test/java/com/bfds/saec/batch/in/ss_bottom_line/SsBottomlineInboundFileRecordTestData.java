package com.bfds.saec.batch.in.ss_bottom_line;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.in.ss_bottom_line.SsBottomLineInRec;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class SsBottomlineInboundFileRecordTestData {

    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager entityManager;

    @Transactional(value = "batchFilesTransactionManager")
	public void create() {
        SsBottomLineInRec r = new SsBottomLineInRec();
        r.setCheckNumber("1");
        r.setCheckAmount(100d);
        r.setAccountNumber("100007");
        r.setFundNumber("66666");
        r.setDda("11111111");
        entityManager.persist(r);

        r = new SsBottomLineInRec();
        r.setCheckNumber("2");
        r.setCheckAmount(200d);
        r.setAccountNumber("100007");
        r.setFundNumber("66666");
        r.setDda("11111111");
        entityManager.persist(r);
        
        r = new SsBottomLineInRec();
        r.setCheckNumber("3");
        r.setCheckAmount(300d);
        r.setAccountNumber("100007");
        r.setFundNumber("66666");
        r.setDda("11111111");
        entityManager.persist(r);

        entityManager.flush();
	}
}
