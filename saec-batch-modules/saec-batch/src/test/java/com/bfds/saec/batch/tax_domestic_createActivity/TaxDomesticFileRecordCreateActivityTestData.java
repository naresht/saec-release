package com.bfds.saec.batch.tax_domestic_createActivity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.out.damasco_domestic.OutboundDomesticTaxRec;
import com.bfds.saec.domain.Event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class TaxDomesticFileRecordCreateActivityTestData {

    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager entityManager;

    @Transactional(value = "batchFilesTransactionManager")
	public void create() {
    	
    	OutboundDomesticTaxRec r = new OutboundDomesticTaxRec();
        r.setCheckNumber("1000001");
        r.setReferenceNo("1001");
        r.setPaymentId((long) 15);
        r.setDda("11111111");
        r.setDda("11111111");
        r.setCity("city1");
        r.setState("state1");
        r.setZip("11111-1111");
        r.setPaymentComp1(new BigDecimal(10));
        r.setPaymentComp2(new BigDecimal(20));
        r.setPaymentComp3(new BigDecimal(30));
        r.setPaymentComp1Description("Settlement Amount");
        r.setPaymentComp2Description("Fees");
        r.setPaymentComp3Description("Other Monies");
        r.setPaymentComp4Description("Other Monies");
        r.setPaymentComp5Description("Other Monies");
        r.setDateSent(new Date());
        r.setDda(Event.getCurrentEventDda());
        r.setProcessed(Boolean.TRUE); // file should be processed and not yet created Activity record for Claimant.
        entityManager.persist(r);



        entityManager.flush();
	}
}
