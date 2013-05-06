package com.bfds.saec.web.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.out.damasco_domestic.OutboundDomesticTaxRec;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.activity.ActivityCode;
import com.bfds.saec.domain.reference.CountryLov;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentComponentType;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.web.service.TaxProcessService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 
 * A view model for creating/editing outbound table contents.
 * 
 */
public class TaxProcessViewModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger log = LoggerFactory
			.getLogger(TaxProcessViewModel.class);

	@Autowired
	transient TaxProcessService taxProcessService;

	private static List<SelectItem> countryList;
	
	/**
	 * Should the {@link Payment} firt be voided or stopped befor being put on the tax file.
	 */
	private boolean paymentMustBeVoidedOrStopped;

	/**
	 * Get the {@link PaymentType}s for a given payment Id .
	 * 
	 * @exception {@link IllegalArgumentException} on no paymentId
	 * 
	 * @return - {@link PaymentType}
	 * 
	 */

	public PaymentType getPaymentType(final Long paymentId) {
		Preconditions.checkArgument(paymentId != null, "paymentId is null");
		final PaymentType paymentType = Payment.findPayment(paymentId)
				.getPaymentType();
		return paymentType;
	}

	/**
	 * @param paymentId
	 *            - The id of the {@link Payment}.
	 * @return The {@link OutboundDomesticTaxRec} for the given paymentId. If
	 *         one does not exist a new {@link OutboundDomesticTaxRec} is
	 *         returned with it's properties set to that of the {@link Payment}
	 *         and {@link Claimant}.
	 */
	public OutboundDomesticTaxRec getOutboundDomesticTaxRec(Long paymentId) {
		Preconditions.checkArgument(paymentId != null, "paymentId is null");
		OutboundDomesticTaxRec ret = null;
		try {
			ret = OutboundDomesticTaxRec.getRecord(paymentId);
			//If the Payment is already on the tax file then it is implied that is either already voided or stopped.
			paymentMustBeVoidedOrStopped = false;
		} catch (Exception ex) {
			final Payment payment = Payment.findPayment(paymentId);
			// when no records exist with that paymentId
			ret = buildOutboundDomesticTaxRec(payment);
			paymentMustBeVoidedOrStopped = !(payment.isVoid() || payment.isStop());
		}
		return ret;
	}

	/**
	 * @param payment
	 *            - {@link Payment}.
	 * @return The {@link OutboundDomesticTaxRec} constructs a new
	 *         OutboundDomesticTaxRec by populating the properties from
	 *         {@link Payment} and {@link Claimant} Defaults set in this entity
	 *         are Tax_Entity - DAMASCO and D - for Domestic User Type
	 * 
	 */
	private OutboundDomesticTaxRec buildOutboundDomesticTaxRec(
			final Payment payment) {
		Preconditions.checkArgument(payment != null, "payment is null");
		OutboundDomesticTaxRec ret;
		ret = new OutboundDomesticTaxRec();
		ret.setPaymentId(payment.getId());
		ret.setDda(Event.getCurrentEventDda());
		ret.setTaxEntity(Event.getCurrentEvent().getTaxVendor());
		ret.setReferenceNo(payment.getPayTo().getReferenceNo());
		ret.setDateAdded(new Date());

		// name1-6
		ret.setName_1(payment.getPayTo().getClaimantRegistration()
				.getRegistration1());
		ret.setName_2(payment.getPayTo().getClaimantRegistration()
				.getRegistration2());
		ret.setName_3(payment.getPayTo().getClaimantRegistration()
				.getRegistration3());
		ret.setName_4(payment.getPayTo().getClaimantRegistration()
				.getRegistration4());
		ret.setName_5(payment.getPayTo().getClaimantRegistration()
				.getRegistration5());
		ret.setName_6(payment.getPayTo().getClaimantRegistration()
				.getRegistration6());

		// Address 1-6
		final ClaimantAddress address = payment.getPayTo().getMailingAddress();
		ret.setAddress_1(address.getAddress1());
		ret.setAddress_2(address.getAddress2());
		ret.setAddress_3(address.getAddress3());
		ret.setAddress_4(address.getAddress4());
		ret.setAddress_5(address.getAddress5());
		ret.setAddress_6(address.getAddress6());

		ret.setCity(address.getCity());

		ret.setState(address.getStateCode());

		ret.setCountryOfResidency(address.getCountryCode());

		final ZipCode zipCode = address.getZipCode();

		ret.setZip(zipCode.getZip());

		ret.setTotalPaymentAmount(payment.getPaymentAmount());

		// payment type
		PaymentType paymentType = payment.getPaymentType();
		if (paymentType != null) {
			ret.setPaymentType(paymentType.toString());
		}

		// payment components 1-5
		ret.setPaymentComp1(payment.getPaymentCalc().getPaymentComp1());

		ret.setPaymentComp2(payment.getPaymentCalc().getPaymentComp2());

		ret.setPaymentComp2(payment.getPaymentCalc().getPaymentComp3());

		ret.setPaymentComp4(payment.getPaymentCalc().getPaymentComp4());

		ret.setPaymentComp5(payment.getPaymentCalc().getPaymentComp5());

		// Descriptions for payment components 1-5
		ret.setPaymentComp1Description(payment.getPaymentCalc()
				.getDescriptionByPaymentComponentType(
						PaymentComponentType.paymentComp1));
		ret.setPaymentComp2Description(payment.getPaymentCalc()
				.getDescriptionByPaymentComponentType(
						PaymentComponentType.paymentComp2));
		ret.setPaymentComp3Description(payment.getPaymentCalc()
				.getDescriptionByPaymentComponentType(
						PaymentComponentType.paymentComp3));
		ret.setPaymentComp4Description(payment.getPaymentCalc()
				.getDescriptionByPaymentComponentType(
						PaymentComponentType.paymentComp4));
		ret.setPaymentComp5Description(payment.getPaymentCalc()
				.getDescriptionByPaymentComponentType(
						PaymentComponentType.paymentComp5));

		// Tax Information

		ret.setTin(payment.getPayTo().getTaxInfo().getTaxIdentifier());

		ret.setUsCitizen(payment.getPayTo().getTaxInfo().getUsCitizen());

		ret.setCertificationStatus(payment.getPayTo().getTaxInfo()
				.getCertificationStatus());

		ret.setCertificationDate(payment.getPayTo().getCertificationDate());

		if (Boolean.TRUE.equals(payment.getPayTo().getOrganization())) {
			ret.setOrganizationOrIndividual("O");
		} else if (Boolean.FALSE.equals(payment.getPayTo().getOrganization())) {
			ret.setOrganizationOrIndividual("I");
		}// other wise blank

		ret.setOriginalAccountType(payment.getPayTo().getAccountType());

		ret.setCheckNumber(payment.getIdentificatonNo());
		// defaults
		ret.setUserType("D");// DOMESTIC
		if(log.isDebugEnabled()){
			log.debug(String.format("A new OutboundDomesticTaxRec is constructed for check#%s", ret.getCheckNumber()));
		}
		return ret;
	}

	/**
	 * @param outboundDomesticTaxRec
	 *            -{@link OutboundDomesticTaxRec}
	 * 
	 *            Before a record is saved/updated it is mandate for the
	 *            {@link Payment} to be voided or stopped. Records which are
	 *            already processed(outboundDomesticTaxRec .getProcessed()) are
	 *            not eligible for update.
	 * 
	 * @return - true on successfully creating an entry, false if the payment is
	 *         neither voided nor stopped
	 * 
	 */
	public boolean create(OutboundDomesticTaxRec outboundDomesticTaxRec,
			MessageContext messageContext) {
		if(log.isInfoEnabled())
			log.info(String.format("Creating Tax Processing Request for check#%s", outboundDomesticTaxRec.getCheckNumber()));
		
		if (paymentMustBeVoidedOrStopped) {
			messageContext
					.addMessage(new MessageBuilder().error()
							.defaultText("Void or Stop the Check before Save.")
							.build());
			return false;
		}
		if (outboundDomesticTaxRec.getId() != null) { // The record exists in
			// Damasco
			// table. Just update it.

			if (Boolean.TRUE.equals(outboundDomesticTaxRec.getProcessed())) {
				messageContext.addMessage(new MessageBuilder()
						.error()
						.defaultText(
								"Cannot update Tax Record for Check: "
										+ outboundDomesticTaxRec
												.getCheckNumber()
										+ " Record already sent to Tax Entity")
						.build());
				return false;
			}
			this.taxProcessService
					.updateOutboundDomesticTaxRec(outboundDomesticTaxRec);
			this.taxProcessService.addActivityForOutboundDomesticTaxRec(
					outboundDomesticTaxRec, ActivityCode.UPDATED);
		} else {
			this.taxProcessService
					.saveOutboundDomesticTaxRec(outboundDomesticTaxRec);
			this.taxProcessService.addActivityForOutboundDomesticTaxRec(
					outboundDomesticTaxRec, ActivityCode.CREATED);
		}
		messageContext
				.addMessage(new MessageBuilder()
						.info()
						.defaultText(
								"Tax Processing Request has been submitted Successfully.")
						.build());
		if(log.isInfoEnabled()){
			log.info(String.format("Tax Processing Request has been submitted Successfully for check#%s", outboundDomesticTaxRec.getCheckNumber()));
		}
		return true;
	}

	/**
	 * @param paymentId
	 *            - The id of the {@link Payment}.
	 * @param messageContext
	 *            -{@link MessageContext}
	 * 
	 * @return boolean on successfully persisting the payment code to
	 * @link{PaymentCode.VOID_DAMASCO_VOIDED_VD_VD
	 */
	@Transactional
	public boolean updatePaymentWithDamascoVoidStatus(Long paymentId,
			MessageContext messageContext) {
		Payment payment = Payment.findPayment(paymentId);
		if (payment.isStoppable()) {
			payment.setPaymentCode(PaymentCode.VOID_DAMASCO_VOIDED_VD_VD);
			payment.persist();
			messageContext.addMessage(new MessageBuilder()
					.info()
					.defaultText(
							"Check " + payment.getIdentificatonNo()
									+ " has been voided").build());
			paymentMustBeVoidedOrStopped = false;
			if(log.isInfoEnabled()){
				log.info(String.format("Payment:%s  with Check # %s PaymentCode is updated with %s", paymentId, payment.getIdentificatonNo(), PaymentCode.VOID_DAMASCO_VOIDED_VD_VD));
			}
			return true;
		}
		messageContext.addMessage(new MessageBuilder()
				.error()
				.defaultText(
						"Check " + payment.getIdentificatonNo()
								+ " is not eligible to Void").build());
		if(log.isInfoEnabled()){
			log.info(String.format("Payment:%s  with Check # %s is not eligible to Void.", paymentId, payment.getIdentificatonNo()));
		}
		return false;
	}

	/**
	 * @param paymentId
	 *            - The id of the {@link Payment}.
	 * @param messageContext
	 *            -{@link MessageContext}
	 * 
	 * @return boolean on successfully persisting the payment code to
	 * @link{PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR
	 */
	@Transactional
	public boolean updatePaymentWithDamascoStopStatus(Long paymentId,
			MessageContext messageContext) {
		Payment payment = Payment.findPayment(paymentId);
		if (payment.isVoidable()) {
			payment.setPaymentCode(PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR);
			payment.persist();
			messageContext.addMessage(new MessageBuilder()
					.info()
					.defaultText(
							"Check " + payment.getIdentificatonNo()
									+ " has been stopped").build());
			paymentMustBeVoidedOrStopped = false;
			if(log.isInfoEnabled()){
				log.info(String.format("Payment:%s  with Check # %s PaymentCode is updated with %s", paymentId, payment.getIdentificatonNo(), PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR));
			}
			return true;
		}
		messageContext.addMessage(new MessageBuilder()
				.error()
				.defaultText(
						"Check " + payment.getIdentificatonNo()
								+ " is not eligible to Stop").build());
		if(log.isInfoEnabled()){
			log.info(String.format("Payment:%s  with Check # %s is not eligible to stop.", paymentId, payment.getIdentificatonNo()));
		}
		return false;
	}



	/**
	 * Delete the {@link OutboundDomesticTaxRec}s for the given primary key of
	 * the table .
	 * 
	 * TODO: MISSING REQUIREMENT
	 * 
	 * @return - true if successfully deleted else false
	 * 
	 */
	public boolean delete(List<Long> damascoOutRecId) {
		// TODO: ADD IMPL
		return true;
	}

	public List<SelectItem> getCountryList() {
		if (countryList == null || countryList.size() == 0) {
			loadCountries();
		}
		return countryList;
	}

	public void setCountryList(List<SelectItem> countryList) {
		TaxProcessViewModel.countryList = countryList;
	}

	public void loadCountries() {

		if (countryList != null && countryList.size() > 0)
			return;

		List<CountryLov> blList = CountryLov.findAllCountryLovs();
		if (blList != null && blList.size() > 0) {
			Iterator<CountryLov> iter = blList.iterator();
			countryList = Lists.newArrayList();

			while (iter.hasNext()) {
				CountryLov countryLov = iter.next();
				countryList.add(new SelectItem(countryLov.getCode(), countryLov
						.getDescription()));
			}
		} else
			log.error("No Countries found in database");
	}

	public boolean isPaymentMustBeVoidedOrStopped() {
		return paymentMustBeVoidedOrStopped;
	}
	
	
}
