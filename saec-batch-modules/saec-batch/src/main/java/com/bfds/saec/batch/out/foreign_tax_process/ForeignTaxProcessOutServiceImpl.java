package com.bfds.saec.batch.out.foreign_tax_process;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.bfds.saec.batch.file.domain.out.damasco_foreign.*;
import com.bfds.saec.batch.out.dsto_check_file.DSTOCheckFileOutputBatchServiceImpl;
import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentComponentType;

import static com.bfds.saec.domain.ClaimantTaxInfo.*;

@Service
public class ForeignTaxProcessOutServiceImpl implements ForeignTaxProcessOutService{
	final Logger log = LoggerFactory.getLogger(ForeignTaxProcessOutServiceImpl.class);
	
	@Override
	public ForeignTaxOutRec buildOutboundForeignTaxRecord(Payment payment)
	{
		payment=Payment.findPayment(payment.getId());
		Claimant claimant=payment.getPayTo();
		if(CERTIFICATION_TYPE_W4P.equals( claimant.getCertificationType()) && claimant.getAlternatePayee().isEmpty()){
				return null;
		}
		if(claimant.getAlternatePayee().size() > 1) {
			throw new IllegalStateException(String.format("Cannot process Payment %s for foreign tax processing. " +
					                                      "The Claimant has more than 1 alternate payee.", payment.getId()));
		}
		
		final ForeignTaxOutRec foreignTaxRec = new ForeignTaxOutRec();
		
		foreignTaxRec.setFundAccount(claimant.getFundAccountNo());
		foreignTaxRec.setBin(claimant.getBin());
		foreignTaxRec.setBrokerId(claimant.getBrokerId());
		foreignTaxRec.setParentReferenceNo(claimant.getReferenceNo());
		foreignTaxRec.setTin(claimant.getTin());
		foreignTaxRec.setPaymentComp1(payment.getPaymentCalc().getPaymentComp1());
		foreignTaxRec.setPaymentComp2(payment.getPaymentCalc().getPaymentComp2());
		foreignTaxRec.setPaymentComp3(payment.getPaymentCalc().getPaymentComp3());
		foreignTaxRec.setPaymentComp4(payment.getPaymentCalc().getPaymentComp4());
		foreignTaxRec.setPaymentComp5(payment.getPaymentCalc().getPaymentComp5());		
		foreignTaxRec.setParentCity(claimant.getMailingAddress().getCity());
		foreignTaxRec.setParentState(claimant.getMailingAddress().getStateCode());
		foreignTaxRec.setParentCountry(claimant.getMailingAddress().getCountryCode());
		foreignTaxRec.setForeignTaxClassification(claimant.getTaxInfo().getForeignTaxClassification());
		foreignTaxRec.setPaymentComp1Description(payment.getPaymentCalc()
				.getDescriptionByPaymentComponentType(
						PaymentComponentType.paymentComp1));
		foreignTaxRec.setPaymentComp2Description(payment.getPaymentCalc()
				.getDescriptionByPaymentComponentType(
						PaymentComponentType.paymentComp2));
		foreignTaxRec.setPaymentComp3Description(payment.getPaymentCalc()
				.getDescriptionByPaymentComponentType(
						PaymentComponentType.paymentComp3));
		foreignTaxRec.setPaymentComp4Description(payment.getPaymentCalc()
				.getDescriptionByPaymentComponentType(
						PaymentComponentType.paymentComp4));
		foreignTaxRec.setPaymentComp5Description(payment.getPaymentCalc()
				.getDescriptionByPaymentComponentType(
						PaymentComponentType.paymentComp5));
		
		final ClaimantAddress mailingAddress = claimant.getMailingAddress();
		if(mailingAddress.getZipCode() != null) {
			foreignTaxRec.setParentZip(mailingAddress.getZipCode().getZip());
			foreignTaxRec.setParentZipCodeExt(mailingAddress.getZipCode().getExt());
		}
				
		foreignTaxRec.setCheckNumber(payment.getIdentificatonNo());
		foreignTaxRec.setCheckDate(payment.getPaymentDate());
		
		updateRegistrationAndAddressesLines(payment, foreignTaxRec, false);
		
		if(CERTIFICATION_TYPE_W4P.equals(claimant.getCertificationType())){
			buildAlternatePayeeDetails(foreignTaxRec, payment,true);
		}
		
		payment.setSentOnTaxFile(Boolean.TRUE);
		payment.merge();
		
		return foreignTaxRec;
	}
	private void buildAlternatePayeeDetails(ForeignTaxOutRec foreignTaxRec,Payment payment,boolean regOrAddressFlag)
	{
		Claimant alternatePayee=payment.getPayTo().getAlternatePayee().get(0);
		foreignTaxRec.setAltPayeeCity(alternatePayee.getMailingAddress().getCity());
		foreignTaxRec.setAltPayeeState(alternatePayee.getMailingAddress().getStateCode());
		foreignTaxRec.setAltPayeeCountry(alternatePayee.getMailingAddress().getCountryCode());
		final ClaimantAddress mailingAddress = alternatePayee.getMailingAddress();
		if(mailingAddress.getZipCode() != null) {
			foreignTaxRec.setAltPayeeZip(mailingAddress.getZipCode().getZip());
			foreignTaxRec.setAltPayeeZipCodeExt(mailingAddress.getZipCode().getExt());
		}
		updateRegistrationAndAddressesLines(payment, foreignTaxRec,regOrAddressFlag);
	}
	
	
	private void updateRegistrationAndAddressesLines(Payment payment, ForeignTaxOutRec foreignTaxRec, boolean useAlternatePayeeRegistration) {
		List<String> registrationLines;
		if(useAlternatePayeeRegistration) {		
			registrationLines = DSTOCheckFileOutputBatchServiceImpl.
					                buildRegistrationLines(getCheckAddress(payment), 
					                payment.getPayTo().getAlternatePayee().get(0).getClaimantRegistration());
		}
		else {
			registrationLines = DSTOCheckFileOutputBatchServiceImpl.
	                				buildRegistrationLines(getCheckAddress(payment), 
	                				payment.getPayTo().getClaimantRegistration());			
		}		
		
		for(int index = 0; index < registrationLines.size() ; index++) {
			if(index > 6) { return; }
			final String line = registrationLines.get(index);
			if(useAlternatePayeeRegistration){
				setAlternatePayeeRegistrationLine(foreignTaxRec, line, index);
			}
			else
				setRegistrationLine(foreignTaxRec, line, index);
		}
	}
	
	
	
	private Address getCheckAddress(final Payment payment) {
		if(payment.getCheckAddress() != null) {
			return payment.getCheckAddress().getAddress();
		}
		return payment.getPayTo().getMailingAddress().getAddress();
	}
	
	private void setRegistrationLine(ForeignTaxOutRec foreignTaxRec, String line, int i) {
		switch (i) {
		case 0:
			foreignTaxRec.setParentReg1OrAdd1(line);
			break;
		case 1:
			foreignTaxRec.setParentReg2OrAdd2(line);
			break;
		case 2:
			foreignTaxRec.setParentReg3OrAdd3(line);
			break;
		case 3:
			foreignTaxRec.setParentReg4OrAdd4(line);
			break;
		case 4:
			foreignTaxRec.setParentReg5OrAdd5(line);
			break;
		case 5:
			foreignTaxRec.setParentReg6OrAdd6(line);
			break;
		}
	}


	private void setAlternatePayeeRegistrationLine(ForeignTaxOutRec foreignTaxRec, String line, int i) {
		switch (i) {
		case 0:
			foreignTaxRec.setAltPayeeReg1OrAdd1(line);
			break;
		case 1:
			foreignTaxRec.setAltPayeeReg2OrAdd2(line);
			break;
		case 2:
			foreignTaxRec.setAltPayeeReg3OrAdd3(line);
			break;
		case 3:
			foreignTaxRec.setAltPayeeReg4OrAdd4(line);
			break;
		case 4:
			foreignTaxRec.setAltPayeeReg5OrAdd5(line);
			break;
		case 5:
			foreignTaxRec.setAltPayeeReg6OrAdd6(line);
			break;
		}
	}

}
