package com.bfds.saec.batch.in.foreign_tax_processing;

import java.util.Date;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.bfds.saec.batch.file.domain.in.foreign_tax.ForeignTaxInRec;
import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.ClaimantTaxInfo;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.PaymentCalc;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.google.common.base.Preconditions;

public class ForeignTaxInboundWriter implements ItemWriter<ForeignTaxInRec> {

	@Value("#{stepExecution}")
	protected StepExecution stepExecution;
	
    @Autowired
    private JobExplorer jobExplorer;
	
	@Override
	public void write(List<? extends ForeignTaxInRec> items) throws Exception {
		for(ForeignTaxInRec rec:items){
			if(rec.canProcessW4P() && "W4P".equalsIgnoreCase(rec.getForeignTaxClassification())) {
				processW4PAccounts(rec);
			}
			else if(rec.canComputeAmoutForNonW4P()){
				processNonW4PAccounts(rec);
			}
		}
	}
	
	private void processNonW4PAccounts(ForeignTaxInRec rec) {
		Preconditions.checkNotNull(rec.getCheckNumber(), "check# is null");
		final Payment payment =  Payment.findPaymentIdentificationNo(rec.getCheckNumber());
		Claimant claimant = payment.getPayTo();
		Preconditions.checkState(claimant != null, "No Claimant for check#: %s", rec.getCheckNumber());

		addActivity(rec, claimant);

		payment.setPaymentCalc(createPaymentCalc(rec));
		payment.setPaymentType(PaymentType.CHECK);
		if(payment.isStop()) {
			payment.setPaymentCode(PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA);
		} else if(payment.isVoid()) {
			payment.setPaymentCode(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
		}else {
			throw new IllegalStateException("Payment code for "+rec.getCheckNumber()+"must be one of the void or stop codes. It is "+payment.getPaymentCode() );
		}
		payment.merge();
		claimant.merge();
	}

	private void processW4PAccounts(ForeignTaxInRec rec) {
		Preconditions.checkNotNull(rec.getCheckNumber(), "check# is null");
		final Payment payment =  Payment.findPaymentIdentificationNo(rec.getCheckNumber());
		Claimant claimant = payment.getPayTo();
		Preconditions.checkState(claimant != null, "No Claimant for check#: %s", rec.getCheckNumber());
	
		final Payment newPayment = createNewPaymentForAlternatePayee(rec, payment);
		final Claimant alternatePayee = createAlternatePayee(rec, payment);

		updatePaymentCode(rec, payment, newPayment);
		
		newPayment.setReissueOf(payment);
		alternatePayee.addCheck(newPayment);
		claimant.addAlternatePayee(alternatePayee);
		newPayment.persist();
		alternatePayee.persist();

		addActivity(rec, claimant);
		claimant.merge();
	}
	
	private PaymentCalc createPaymentCalc(ForeignTaxInRec rec) {
		PaymentCalc ret = new PaymentCalc();
		ret.setComponentValueByDescription(rec.getPaymentComp1Description(), rec.getPaymentComp1());
		ret.setComponentValueByDescription(rec.getPaymentComp2Description(), rec.getPaymentComp2());
		ret.setComponentValueByDescription(rec.getPaymentComp3Description(), rec.getPaymentComp3());
		ret.setFedWithholding(rec.getFederalTax());
		ret.setIntFedWithholding(rec.getInterestFederaltax());
		ret.setGrossAmount(rec.computeGrossAmount());
		ret.setNettAmount(rec.computeNettAmount());
		return ret;
	}
	
	private void addActivity(ForeignTaxInRec rec, final Claimant claimant) {
		Activity activity = new Activity();
		Activity.setActivityDefaults(activity);
		//TODO: get date from rec.
		activity.setActivityDate(getDateOfInboundTaxFileProcessing(rec.getJobExecutionId()));
		activity.setUserId("Damasco");
		activity.setActivityTypeDescription("File from Tax Processing Entity");
		activity.setDescription(rec.getDisposstion());
		claimant.addActivity(activity);
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
	private Claimant createAlternatePayee(ForeignTaxInRec rec,Payment payment) {
        final Claimant alternatePayee = new Claimant();
        ClaimantRegistration claimantRegistration =  payment.getPayTo().getClaimantRegistration();
        alternatePayee.setClaimantRegistration(new ClaimantRegistration());
        if(claimantRegistration!=null){
        alternatePayee.getClaimantRegistration().setRegistration1(claimantRegistration.getRegistration1());
        alternatePayee.getClaimantRegistration().setRegistration2(claimantRegistration.getRegistration2());
        alternatePayee.getClaimantRegistration().setRegistration3(claimantRegistration.getRegistration3());
        alternatePayee.getClaimantRegistration().setRegistration4(claimantRegistration.getRegistration4());
        alternatePayee.getClaimantRegistration().setRegistration5(claimantRegistration.getRegistration5());
        alternatePayee.getClaimantRegistration().setRegistration6(claimantRegistration.getRegistration6());
        }
        ClaimantAddress address = new ClaimantAddress();
        address.setAddress(createAddress(rec, payment));
        address.setAddressType(AddressType.ADDRESS_OF_RECORD);
        alternatePayee.setAddressOfRecord(address);
        alternatePayee.setTaxInfo(createTaxInfo(rec)); // Alternate payee may not have tax info however creating it to avoid null pointer exception in UI.  
        
        return alternatePayee;
    }

	private ClaimantTaxInfo createTaxInfo(ForeignTaxInRec rec)
	{
		ClaimantTaxInfo taxInfo = new ClaimantTaxInfo();
		//taxInfo.setUsCitizen( rec.getUsCitizen()!= null ? Boolean.valueOf(rec.getUsCitizen()):false);
		return taxInfo;
	}

	private Address createAddress(ForeignTaxInRec rec,Payment payment) {
		Address address = new Address();
		final Address altPayeeAddress=getCheckAddress(payment);
		address.setMailingAddress(true);
		if(altPayeeAddress!=null){
		address.setAddress1(altPayeeAddress.getAddress1());
		address.setAddress2(altPayeeAddress.getAddress2());
		address.setAddress3(altPayeeAddress.getAddress3());
		address.setAddress4(altPayeeAddress.getAddress4());
		address.setAddress5(altPayeeAddress.getAddress5());
		address.setAddress6(altPayeeAddress.getAddress6());
		}
		address.setCity(rec.getAltPayeeCity());
		address.setStateCode(rec.getAltPayeeState());
		if(StringUtils.hasText(rec.getAltPayeeZip())) {
			address.setZipCode(new ZipCode(rec.getAltPayeeZip(),rec.getAltPayeeZipCodeExt()));
		}
		address.setCountryCode(rec.getAltPayeeCountry());
		return address;
	}
	
	private Payment createNewPaymentForAlternatePayee(ForeignTaxInRec rec,Payment payment) {
		Payment ret =  Payment.newPayment(PaymentCode.ISSUANCE_CREATED_IS_IS);
		MailObjectAddress address = new MailObjectAddress();
		address.setAddress(createAddress(rec, payment));
		address.setAddressType(AddressType.PAYMENT_ADDRESS);
		ret.setPaymentCalc(createPaymentCalc(rec));
		ret.setPaymentType(PaymentType.CHECK);
		return ret;
	}
	
	private void updatePaymentCode(ForeignTaxInRec rec, final Payment payment, final Payment newPayment) {
		if(payment.isStop()) {
			newPayment.setPaymentCode(PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA);
		} else if(payment.isVoid()) {
			newPayment.setPaymentCode(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
		}else {
			throw new IllegalStateException("Payment code for "+rec.getCheckNumber()+"must be one of the void or stop codes. It is "+payment.getPaymentCode() );
		}
	}

	 private Address getCheckAddress(final Payment payment) {
	        if(payment.getCheckAddress() != null) {
	            return payment.getCheckAddress().getAddress();
	        }
	        return payment.getPayTo().getMailingAddress().getAddress();
	    }


}
