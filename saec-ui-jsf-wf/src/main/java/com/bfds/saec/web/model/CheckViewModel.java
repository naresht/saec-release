package com.bfds.saec.web.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.Bank;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.RegistrationLines;
import com.bfds.saec.domain.WireInfo;
import com.bfds.saec.domain.WireOriginationInfo;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.web.util.JsfUtils;

public class CheckViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(CheckViewModel.class); 
	
	private Payment check;
	private MailObjectAddress onetimeMailingAddressForReissue;
	private Claimant claimant;
	private String stopType;
	private String voidType;
	
	/**
	 * When a Check is reissued as wire the transfer(paymentAmount) amount of the wire can be edited. However the
	 * paymentAmount the paymentAmount of the Check should not be updated.  
	 */
	private BigDecimal wireTransferAmount;

	/**
	 * Created, to hide the table on load. On clicking on Check the flag will be
	 * enabled
	 */
	private boolean showTable;

	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager entityManager;

	public CheckViewModel() {

	}

	public CheckViewModel(Claimant claimant) {
		this.claimant = claimant;
	}

	public Claimant getClaimant() {
		return claimant;
	}

	public void setClaimant(Claimant claimant) {
		this.claimant = claimant;
	}

	public Payment getCheck() {
		return check;
	}

	public void setCheck(Payment check) {
		this.check = check;
		this.claimant = check.getPayTo();
	}

	public boolean canEdit() {
		return check.isStoppable() || check.isVoidable()
				|| check.isReissueable() || check.canDoReturnOfFunding()
				|| check.canChangeToWire() || check.canUpdateWireInfo()
				|| check.canSplit() || check.canProcessTax();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void voidCheck() {
		if ("VOID".equals(this.voidType)) {
			check.voidCheck();
		} else if ("VOID_NO_RE_ISSUE".equals(this.voidType)) {
			check.noReissueVoidCheck();
		} else if ("VOID_DAMASCO".equals(this.voidType)) {
			check.damascoVoidCheck();
		} else if ("VOID_HOLD".equals(this.voidType)) {
			check.holdVoidCheck();
		} else {
			throw new IllegalStateException("Unkonwn void type :"
					+ this.voidType);
		}

		check = savePayment(check);
		
		if(log.isInfoEnabled())
			log.info(String.format("%s Check with payment Id : %s is saved.", this.voidType,this.check.getId()));		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void stopCheck() {
		if ("STOP".equals(this.stopType)) {
			check.stopCheck();
		} else if ("STOP_DAMASCO".equals(this.stopType)) {
			check.damascoStopCheck();
		} else {
			throw new IllegalStateException("Unkonwn stop type :"
					+ this.stopType);
		}
		check = savePayment(check);
		
		if(log.isInfoEnabled()) {
			log.info(String.format("%s Check with payment Id : %s is saved.", this.stopType,this.check.getId()));
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void stopReplaceCheck() {
		check.stopReplaceCheck();
		check = savePayment(check);				
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveWire() {
		// check.setPaymentStatus(PaymentStatus.STOP_REPLACE_REQUESTED);
		check = savePayment(check);
		
		if(log.isInfoEnabled()) {
			log.info(String.format("Wire requested is created for Check Id : %s.", this.check.getId()));
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean reissueCheck() {
		// TODO: Validate one time mailing address.
		MailObjectAddress address = getSelectedCheckAddressForReissue();
		check.reissueCheck(address);
	 
		check = savePayment(check);
		
		if(log.isInfoEnabled()) {
			log.info(String.format("Re-issued Check Id : %s is saved.", this.check.getId()));
		}
		return true;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	private Payment savePayment(final Payment payment) {
		claimant = Claimant.findClaimant(this.check.getPayTo().getId(),
				Claimant.ASSOCIATION_ADDRESSES);
		return (Payment) payment.merge();
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	private MailObjectAddress getSelectedCheckAddressForReissue() {
		MailObjectAddress address = new MailObjectAddress();
		String selectedAddressType = JsfUtils
				.getRequestParameter("addressToUse");
		Map m = JsfUtils.getRequestParameterMap();
		if ("AOR".equals(selectedAddressType)) {
			address = new MailObjectAddress();
			check.getPayTo().getAddressOfRecord().copyTo(address);
			return address;
		} else if ("SEASONAL".equals(selectedAddressType)) {
			address = new MailObjectAddress();
			check.getPayTo().getSeasonalAddress().copyTo(address);
			address.setMailingAddress(true);
			address.setAddressType(AddressType.SEASONAL_ADDRESS);
			return address;
		} else if ("ONE_TIME_MAILING".equals(selectedAddressType)) {
			address = new MailObjectAddress();
			onetimeMailingAddressForReissue.copyTo(address);
			address.setMailingAddress(true);
			address.setAddressType(AddressType.ONE_TIME_MAILING);
			return address;
		} else {
			throw new IllegalStateException("Unsupported address type: "
					+ selectedAddressType);
		}
	}

	public MailObjectAddress getOnetimeMailingAddressForReissue() {
		return onetimeMailingAddressForReissue;
	}

	public void setOnetimeMailingAddressForReissue(
			MailObjectAddress onetimeMailingAddressForReissue) {
		this.onetimeMailingAddressForReissue = onetimeMailingAddressForReissue;
		// this.onetimeMailingAddressForReissue.setAddress1("aa");
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void changeCheckToWire() {
		Payment wire = check.reissueCheckAsWire(wireTransferAmount);
		if (wire == check) {
			check = savePayment(wire);
			if(log.isInfoEnabled()) {
				log.info(String.format("Check Id : %s is saved as wire.", this.check.getId()));
			}
		} else {
			wire.setPayTo(entityManager.getReference(Claimant.class, wire
					.getPayTo().getId()));
			wire.persist();
			check = savePayment(check);
			if(log.isInfoEnabled()) {
				log.info(String.format("Wire Id : %s is saved.", this.check.getIdentificatonNo()));
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateWire() {
		check = savePayment(this.check);
	}

	public void prepareCheckReissueToWire() {

		createWireObjects();

		if (check.getWireInfo().getAuthorizedDate() == null) {
			check.getWireInfo().setAuthorizedDate(new Date());
		}		
		this.wireTransferAmount = check.getPaymentAmount();
	}

	/**
	 * Initialize the {@link WireInfo} object graph/tree to avoid
	 * {@link NullPointerException}s during JSF data binding. TODO : find a
	 * better solution.
	 */
	private void createWireObjects() {
		if (check.getWireInfo() == null) {
			check.setWireInfo(new WireInfo());
		}
		if (check.getWireInfo().getOriginationInfo() == null) {
			check.getWireInfo().setOriginationInfo(new WireOriginationInfo());
		}

		if (check.getWireInfo().getOriginationInfo().getLines() == null) {
			check.getWireInfo().getOriginationInfo()
					.setLines(new RegistrationLines());
		}

		if (check.getWireInfo().getReceivingBank() == null) {
			check.getWireInfo().setReceivingBank(new Bank());
		}

		if (check.getWireInfo().getReceivingBank().getRegistration() == null) {
			check.getWireInfo().getReceivingBank()
					.setRegistration(new RegistrationLines());
		}

		if (check.getWireInfo().getReceivingBank().getAddress() == null) {
			check.getWireInfo().getReceivingBank().setAddress(new Address());
		}
	}

	/**
	 * To Show the Address table on firing Check button.
	 * 
	 * @return - {@link Boolean}
	 */
	public boolean showAddressTable() {
		setShowTable(true);
		return true;
	}

	public void showRememberToCreateAlternatePayeeMsg(
			final String certificationType, final MessageContext messageContext) {
		if ("W4P".equals(certificationType)) {
			messageContext
					.addMessage(new MessageBuilder()
							.info()
							.defaultText(
									"Remember to Create an Alternate Payee for this Class Member")
							.build());
		}

	}

	/**
	 * @return the showTable
	 */
	public boolean isShowTable() {
		return showTable;
	}

	/**
	 * @param showTable
	 *            the showTable to set
	 */
	public void setShowTable(boolean showTable) {
		this.showTable = showTable;
	}

	public Bank getWireSendingBank() {
		return Event.getCurrentEvent().getBank();
	}

	public String getStopType() {
		return stopType;
	}

	public void setStopType(String stopType) {
		this.stopType = stopType;
	}

	public String getVoidType() {
		return voidType;
	}

	public void setVoidType(String voidType) {
		this.voidType = voidType;
	}
	
	
	
	public BigDecimal getWireTransferAmount() {
		return wireTransferAmount;
	}

	public void setWireTransferAmount(BigDecimal wireTransferAmount) {
		this.wireTransferAmount = wireTransferAmount;
	}

	/**
	 * this method is used to get the checkaddress based on the payment code,If a check is in CREATED payment mode then we will
	 * use only claimant mailing address as the checkaddress,otherwise we will use the checkaddress.
	 * @return checkAddress
	 */
	public String checkAddressAsString(String lineSeperator) {       
		if (PaymentCodeUtil.getCreatedCodes().contains(check.getPaymentCode()) 
				|| (check.getPaymentType().equals(PaymentType.WIRE)) && check.getPaymentCode().equals(PaymentCode.WIRE_REQUESTED_W_W)) {
			return claimant.getMailingAddress().getAddressAsString(lineSeperator);
		} else if(check.getCheckAddress() != null){
			return check.getCheckAddress().getAddressAsString(lineSeperator);
		}

		return null;
	}

	/**
	 * this method is used to get the payto name to be displayed on the check maintenance screen,here  getting name is depending on the paymentcode of
	 * the check,for Created status checks we will show only the claimant registration lines otherwise we will show the check registration lines.
	 */
	public String paytoNameAsString(String lineSeperator)
	{
		if (claimant.getClaimantRegistration() != null && PaymentCodeUtil.getCreatedCodes().contains(check.getPaymentCode())) {
			return claimant.getClaimantRegistration().getRegistrationLinesAsString(lineSeperator);
		}else if(check.getPayToRegistration() != null){
			return check.getPayToRegistration().getRegistrationLinesAsString(lineSeperator);
		}
		return null;
	} 


}
