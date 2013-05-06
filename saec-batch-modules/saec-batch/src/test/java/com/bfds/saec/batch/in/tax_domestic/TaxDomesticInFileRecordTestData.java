package com.bfds.saec.batch.in.tax_domestic;

import com.bfds.saec.batch.file.domain.in.damasco_domestic.DamascoInRec;
import com.bfds.saec.batch.file.domain.in.ss_cashed_check.SsCashedCheckRec;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

@Component
public class TaxDomesticInFileRecordTestData {

    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager entityManager;

    @Transactional(value = "batchFilesTransactionManager")
	public void create() {
        DamascoInRec r = new DamascoInRec();
        r.setCheckNumber("100001");
        r.setReferenceNo("101");
        r.setDda("11111111");
        r.setName1("name11");
        r.setName2("name12");
        r.setName3("name13");
        r.setName4("name14");
        r.setName5("name15");
        r.setName6("name16");
        r.setDda("11111111");
        r.setAddressLine1("addr11");
        r.setAddressLine2("addr12");
        r.setAddressLine3("addr13");
        r.setAddressLine4("addr14");
        r.setAddressLine5("addr15");
        r.setAddressLine6("addr16");
        r.setCity("city1");
        r.setState("state1");
        r.setCountryOfTaxResidency("us");
        r.setZip("11111-1111");
        r.setPaymentComp1(new BigDecimal(10));
        r.setPaymentComp2(new BigDecimal(20));
        r.setPaymentComp3(new BigDecimal(30));
        //r.setPaymentComp4(new BigDecimal(40));
        //r.setPaymentComp5(new BigDecimal(50));
        //r.setPaymentComp6(new BigDecimal(100));
        r.setInterestFederaltax(new BigDecimal(5));
        r.setInterestStatetax(new BigDecimal(10));
        r.setFederalTax(new BigDecimal(1));
        r.setStateTax(new BigDecimal(2));
        r.setTaxesBasedOn("G");
        r.setDisposition("Ok to reissue with withholding");
        r.setPaymentComp1Description("Settlement Amount");
        r.setPaymentComp2Description("Fees");
        r.setPaymentComp3Description("Other Monies");
        entityManager.persist(r);

        r = new DamascoInRec();
        r.setCheckNumber("100002");
        r.setReferenceNo("102");
        r.setDda("11111111");
        r.setName1("name21");
        r.setName2("name22");
        r.setName3("name23");
        r.setName4("name24");
        r.setName5("name25");
        r.setName6("name26");
        r.setAddressLine1("addr21");
        r.setAddressLine2("addr22");
        r.setAddressLine3("addr23");
        r.setAddressLine4("addr24");
        r.setAddressLine5("addr25");
        r.setAddressLine6("addr26");
        r.setCity("city2");
        r.setState("state2");
        r.setCountryOfTaxResidency("us");
        r.setZip("22222-2222");
        r.setPaymentComp1(new BigDecimal(10));
        r.setPaymentComp2(new BigDecimal(20));
        r.setPaymentComp3(new BigDecimal(30));
        //r.setPaymentComp4(new BigDecimal(40));
        //r.setPaymentComp5(new BigDecimal(50));
        //r.setPaymentComp6(new BigDecimal(100));
        r.setInterestFederaltax(new BigDecimal(5));
        r.setInterestStatetax(new BigDecimal(10));
        r.setFederalTax(new BigDecimal(1));
        r.setStateTax(new BigDecimal(2));
        r.setTaxesBasedOn("XXXX");
        r.setDisposition("Ok to reissue without withholding");
        r.setPaymentComp1Description("Settlement Amount");
        r.setPaymentComp2Description("Fees");
        r.setPaymentComp3Description("Other Monies");
        entityManager.persist(r);

        r = new DamascoInRec();
        r.setCheckNumber("103");
        r.setReferenceNo("100000003");
        r.setDda("11111111");
        r.setDisposition("blah blah blah");
        r.setPaymentComp1Description("Settlement Amount");
        r.setPaymentComp2Description("Fees");
        r.setPaymentComp3Description("Other Monies");
        entityManager.persist(r);

        entityManager.flush();
	}
}
