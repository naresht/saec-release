package com.bfds.saec.batch.in.foreign_tax_processing;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.in.foreign_tax.ForeignTaxInRec;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Component
public class ForeignTaxInboundFileRecordTestData {

    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager entityManager;
	
	 @Transactional(value = "batchFilesTransactionManager")
		public void create() {
		  ForeignTaxInRec rec=new ForeignTaxInRec();
		  rec.setDda("11111111");
		  rec.setForeignTaxClassification("W8");
		  rec.setFundAccount("888777");
		  rec.setBin("666777");
		  rec.setBrokerId("222444");
		  rec.setParentReferenceNo("101");
		  rec.setPaymentComp1(new BigDecimal(10));
		  rec.setPaymentComp2(new BigDecimal(10));
		  rec.setPaymentComp3(new BigDecimal(10));
		  rec.setPaymentComp4(new BigDecimal(10));
		  rec.setPaymentComp5(new BigDecimal(10));
		  rec.setPaymentComp1Description("component1");
		  rec.setPaymentComp2Description("component2");
		  rec.setPaymentComp3Description("component3");
		  rec.setPaymentComp4Description("component4");
		  rec.setPaymentComp5Description("component5");
		  rec.setParentReg1OrAdd1("parentReg1");
		  rec.setParentReg2OrAdd2("parentReg2");
		  rec.setParentReg3OrAdd3("parentReg3");
		  rec.setParentReg4OrAdd4("parentReg4");
		  rec.setParentReg5OrAdd5("parentReg5");
		  rec.setParentReg6OrAdd6("parentReg6");
		  rec.setParentCity("Parentcity");
		  rec.setParentState("Parentstate");
		  rec.setParentZip("12345");
		  rec.setParentZipCodeExt("6789");
		  rec.setParentCountry("Country1");
		  rec.setAltPayeeReg1OrAdd1("altPayeeReg1OrAdd1");
		  rec.setAltPayeeReg2OrAdd2("altPayeeReg2OrAdd2");
		  rec.setAltPayeeReg3OrAdd3("altPayeeReg3OrAdd3");
		  rec.setAltPayeeReg4OrAdd4("altPayeeReg4OrAdd4");
		  rec.setAltPayeeReg5OrAdd5("altPayeeReg5OrAdd5");
		  rec.setAltPayeeReg6OrAdd6("altPayeeReg6OrAdd6");
		  rec.setAltPayeeCity("altPayeeCity");
		  rec.setAltPayeeCountry("altPayeeCountry");
		  rec.setAltPayeeState("altPayeeState");
		  rec.setAltPayeeZip("44556");
		  rec.setAltPayeeZipCodeExt("7788");
		  rec.setCheckNumber("100001");
		  rec.setTaxesBasedOn("G");
		  rec.setWithHoldingRequest("Yes");
		  rec.setAltPayeeReferenceNo("100100");
		  rec.setDisposstion("Ok to reissue with withholding");
		  rec.setFederalTax(new BigDecimal(2));
		  rec.setInterestFederaltax(new BigDecimal(3));
         entityManager.persist(rec);
		  
		  rec=new ForeignTaxInRec();
		  rec.setDda("11112222");
		  rec.setForeignTaxClassification("W4P");
		  rec.setFundAccount("888777");
		  rec.setBin("666777");
		  rec.setBrokerId("222444");
		  rec.setParentReferenceNo("102");
		  rec.setPaymentComp1(new BigDecimal(10));
		  rec.setPaymentComp2(new BigDecimal(10));
		  rec.setPaymentComp3(new BigDecimal(10));
		  rec.setPaymentComp4(new BigDecimal(10));
		  rec.setPaymentComp5(new BigDecimal(10));
		  rec.setPaymentComp1Description("component1");
		  rec.setPaymentComp2Description("component2");
		  rec.setPaymentComp3Description("component3");
		  rec.setPaymentComp4Description("component4");
		  rec.setPaymentComp5Description("component5");
		  rec.setParentReg1OrAdd1("parentReg1");
		  rec.setParentReg2OrAdd2("parentReg2");
		  rec.setParentReg3OrAdd3("parentReg3");
		  rec.setParentReg4OrAdd4("parentReg4");
		  rec.setParentReg5OrAdd5("parentReg5");
		  rec.setParentReg6OrAdd6("parentReg6");
		  rec.setParentCity("Parentcity");
		  rec.setParentState("Parentstate");
		  rec.setParentZip("12345");
		  rec.setParentZipCodeExt("6789");
		  rec.setParentCountry("Country1");
		  rec.setAltPayeeReg1OrAdd1("altPayeeReg1OrAdd1");
		  rec.setAltPayeeReg2OrAdd2("altPayeeReg2OrAdd2");
		  rec.setAltPayeeReg3OrAdd3("altPayeeReg3OrAdd3");
		  rec.setAltPayeeReg4OrAdd4("altPayeeReg4OrAdd4");
		  rec.setAltPayeeReg5OrAdd5("altPayeeReg5OrAdd5");
		  rec.setAltPayeeReg6OrAdd6("altPayeeReg6OrAdd6");
		  rec.setAltPayeeCity("altPayeeCity");
		  rec.setAltPayeeCountry("altPayeeCountry");
		  rec.setAltPayeeState("altPayeeState");
		  rec.setAltPayeeZip("44556");
		  rec.setAltPayeeZipCodeExt("7788");
		  rec.setCheckNumber("100002");
		  rec.setTaxesBasedOn("G");
		  rec.setWithHoldingRequest("Yes");
		  rec.setAltPayeeReferenceNo("200200");
		  rec.setDisposstion("Ok to reissue with withholding");
		  rec.setFederalTax(new BigDecimal(2));
		  rec.setInterestFederaltax(new BigDecimal(3));
         entityManager.persist(rec);
		  
		  
		  rec=new ForeignTaxInRec();
		  rec.setDda("534534534");
		  rec.setCheckNumber("534553453");
		  rec.setParentReferenceNo("7567567567");
         entityManager.persist(rec);
         entityManager.flush();
		  
	 }
}
