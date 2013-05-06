package com.bfds.saec.batch.in.tax_domestic;


import com.bfds.saec.batch.file.domain.in.damasco_domestic.DamascoInRec;
import com.bfds.saec.domain.*;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

public class TaxDomesticInItemWriter implements ItemWriter<DamascoInRec> {

    private static final Logger logger = LoggerFactory.getLogger(TaxDomesticInItemWriter.class);

    @Autowired
    private JobExplorer jobExplorer;

    @Value("#{stepExecution}")
    protected StepExecution stepExecution;


    @Override
    public void write(List<? extends DamascoInRec> items) throws Exception {
        for(DamascoInRec rec : items) {
            process(rec);
        }
    }

    private void process(final DamascoInRec rec) {
        Preconditions.checkNotNull(rec, "rec is null");
        if(!rec.canProcess()) {
            if(logger.isInfoEnabled()) {
                logger.info("Record : "+rec.getCheckNumber()+", "+rec.getDda()+", not eligible for processing.");
            }
            return;
        }
        Preconditions.checkNotNull(rec.getCheckNumber(), "check# is null");
        final Payment payment =  Payment.findPaymentIdentificationNo(rec.getCheckNumber());
        final Claimant claimant = payment.getPayTo();
        payment.setHasBeenSplit(true);
        Preconditions.checkState(claimant != null, "No Claimant for check#: %s", rec.getCheckNumber());

        final Claimant alternatePayee = createAlternatePayee(rec);
        final Payment newPayment = createNewPaymentForAlternatePayee(rec);
        
        newPayment.setPaymentCode(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
        newPayment.setSplitFromPayment(payment);
         
        newPayment.setReissueOf(payment);
        alternatePayee.addCheck(newPayment);
        claimant.addAlternatePayee(alternatePayee);
        newPayment.persist();
        alternatePayee.persist();
        

        Activity activity = new Activity();
        Activity.setActivityDefaults(activity);
        activity.setActivityDate(getDateOfInboundTaxFileProcessing(rec.getJobExecutionId()));
        activity.setUserId("Damasco");
        activity.setActivityTypeDescription("File from Tax Processing Entity");
        activity.setDescription(rec.getDisposition());
        claimant.addActivity(activity);

        alternatePayee.flush();
    }

    private Claimant createAlternatePayee(DamascoInRec rec) {
        final Claimant alternatePayee = new Claimant();
        alternatePayee.setClaimantRegistration(new ClaimantRegistration());
        alternatePayee.getClaimantRegistration().setRegistration1(rec.getName1());
        alternatePayee.getClaimantRegistration().setRegistration2(rec.getName2());
        alternatePayee.getClaimantRegistration().setRegistration3(rec.getName3());
        alternatePayee.getClaimantRegistration().setRegistration4(rec.getName4());
        alternatePayee.getClaimantRegistration().setRegistration5(rec.getName5());
        alternatePayee.getClaimantRegistration().setRegistration6(rec.getName6());

        ClaimantAddress address = new ClaimantAddress();
        address.setAddress(createAddress(rec));
        address.setAddressType(AddressType.ADDRESS_OF_RECORD);
        alternatePayee.setAddressOfRecord(address);
        alternatePayee.setTaxInfo(createTaxInfo(rec)); // Alternative may not have tax info however creating it to avoid null pointer exception in UI.  
        
        return alternatePayee;
    }

    private Date getDateOfInboundTaxFileProcessing(Long jobExecutionId) {
        Date ret = (Date) stepExecution.getExecutionContext().get("dateOfInboundTaxFileProcessing");
        if(ret == null) {
            JobExecution jobExecution = jobExplorer.getJobExecution(jobExecutionId);
            if(jobExecution != null) {
                ret = jobExecution.getStartTime();
            }
            ret = ret == null ? new Date() : ret;
            stepExecution.getExecutionContext().put("dateOfInboundTaxFileProcessing", ret);
        }
        return ret;
    }

    private Address createAddress(DamascoInRec rec) {
        Address address = new Address();
        address.setMailingAddress(true);
        address.setAddress1(rec.getAddressLine1());
        address.setAddress2(rec.getAddressLine2());
        address.setAddress3(rec.getAddressLine3());
        address.setAddress4(rec.getAddressLine4());
        address.setAddress5(rec.getAddressLine5());
        address.setAddress6(rec.getAddressLine6());
        address.setCity(rec.getCity());
        address.setStateCode(rec.getState());
        if(StringUtils.hasText(rec.getZip())) {
            address.setZipCode(newZipCode(rec.getZip()));
        }
        address.setCountryCode(rec.getCountryOfTaxResidency());
        return address;
    }

    private ZipCode newZipCode(String zip) {
        Preconditions.checkNotNull(zip, "zip is null");
        String[] zipArray = zip.split("-");
        ZipCode ret = new ZipCode();
        ret.setZip(zipArray[0]);
        if(zipArray.length == 2) {
            ret.setExt(zipArray[1]);
        }
        return ret;
    }

    private Payment createNewPaymentForAlternatePayee(DamascoInRec rec) {
        Payment ret =  Payment.newPayment(PaymentCode.ISSUANCE_CREATED_IS_IS);
        MailObjectAddress address = new MailObjectAddress();
        address.setAddress(createAddress(rec));
        address.setAddressType(AddressType.PAYMENT_ADDRESS);
        ret.setPaymentCalc(createPaymentCalc(rec));
        ret.setPaymentType(PaymentType.CHECK);
        return ret;
    }

    private PaymentCalc createPaymentCalc(DamascoInRec rec) {
        PaymentCalc ret = new PaymentCalc();
        ret.setComponentValueByDescription(rec.getPaymentComp1Description(), rec.getPaymentComp1());
        ret.setComponentValueByDescription(rec.getPaymentComp2Description(), rec.getPaymentComp2());
        ret.setComponentValueByDescription(rec.getPaymentComp3Description(), rec.getPaymentComp3());
        //ret.setComponentValueByDescription(rec.getPaymentComp4Description(), rec.getPaymentComp1());
        //ret.setComponentValueByDescription(rec.getPaymentComp5Description(), rec.getPaymentComp1());
        ret.setFedWithholding(rec.getFederalTax());
        ret.setStateWithholding(rec.getStateTax());
        ret.setIntFedWithholding(rec.getInterestFederaltax());
        ret.setIntStateWithholding(rec.getInterestStatetax());
        ret.setGrossAmount(rec.computeGrossAmount());
        ret.setNettAmount(rec.computeNettAmount());
        return ret;
    }
    
    private ClaimantTaxInfo createTaxInfo(DamascoInRec rec)
    {
    	
    	 ClaimantTaxInfo taxInfo = new ClaimantTaxInfo();
         taxInfo.setUsCitizen( rec.getUsCitizen()!= null ? Boolean.valueOf(rec.getUsCitizen()):false);
         return taxInfo;
    }
    

}

