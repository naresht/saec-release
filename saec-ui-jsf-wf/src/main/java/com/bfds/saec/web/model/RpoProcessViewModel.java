package com.bfds.saec.web.model;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;

import com.bfds.saec.dao.EntityAuditDao;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.audit.RevisionInfo;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.RPOType;

public class RpoProcessViewModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	final static Logger log = LoggerFactory.getLogger(RpoProcessViewModel.class);

	private String mailingControlNumber;

	private Letter mail;

	private ClaimantAddress address;

	private RevisionInfo addressRevisionInfo;

	private Claimant claimant;

	private boolean rpoTypeFlag;

	private boolean showAddressDetails;

	@Autowired
	private transient EntityAuditDao entityAuditDaoDao;

	public void reset() {
		mail = new Letter();
		mailingControlNumber = null;
	}

	public boolean loadMailByControlNumber(final MessageContext messageContext) {
		if(log.isInfoEnabled())
			log.info(String.format("Loading mail by Mailing Control Number."));
		if(this.mailingControlNumber == null){
			messageContext.addMessage(new MessageBuilder().error().source("form:mailing_ctrl_no").defaultText("Please, fill the Mailing Control Number").build());
			return false;
		}
		mail = findMailByControlNo(mailingControlNumber);
		if (mail == null) {
			messageContext
					.addMessage(new MessageBuilder()
							.error()
							.defaultText(
									"Invalid mailing control number, please enter the correct mailing control number")
							.build());
			if(log.isErrorEnabled())
				log.error(String.format("Invalid mailing control number."));
			return false;
		}

		claimant = Claimant.findClaimant(mail.getClaimant().getId(), Claimant.ASSOCIATION_ADDRESSES);
		if( mail.getRpoDate() == null ){
			mail.setRpoDate(new Date());
		}
		if (mail.getRpoType() == RPOType.FORWARDABLE) {
			mail.setLetterStatus(LetterStatus.RPO);			
		}
		loadClaimantAddress();

		showAddressDetails = mail.getRpoType() == RPOType.FORWARDABLE;
		return true;

	}

	private boolean loadClaimantAddress() {
		address = claimant.getMailingAddress();
		addressRevisionInfo = entityAuditDaoDao.getLastRevisionInfo(
				ClaimantAddress.class, address.getId());
		return true;
	}

	private Letter findMailByControlNo(final String mailingControlNumber) {
		return Letter.findByMailingControlNo(mailingControlNumber);
	}

	public ClaimantAddress getClaimantAddress() {
		return address;
	}

	public String getMailingControlNumber() {
		return mailingControlNumber;
	}

	public void setMailingControlNumber(String mailingControlNumber) {
		this.mailingControlNumber = mailingControlNumber;
	}

	public Letter getMail() {
		return mail;
	}

	public boolean save(final MessageContext messageContext) {
		if (mail.getRpoType() == RPOType.FORWARDABLE) {
			address.merge().persist();
		}
		mail = mail.merge();
		mail.persist();
		return true;
	}

	public void setMail(Letter mail) {
		this.mail = mail;
	}

	public ClaimantAddress getAddress() {
		return address;
	}

	public void setAddress(ClaimantAddress address) {
		this.address = address;
	}

	public Claimant getClaimant() {
		return claimant;
	}

	public void setClaimant(Claimant claimant) {
		this.claimant = claimant;
	}

	public void onChangeRpoType(String selectedValue) {
		showAddressDetails = mail.getRpoType() == RPOType.FORWARDABLE;
	}

	public boolean isRpoTypeFlag() {
		return rpoTypeFlag;
	}

	public void setRpoTypeFlag(boolean rpoTypeFlag) {
		this.rpoTypeFlag = rpoTypeFlag;
	}

	public boolean isShowAddressDetails() {
		return showAddressDetails;
	}

	public void setShowAddressDetails(boolean showAddressDetails) {
		this.showAddressDetails = showAddressDetails;
	}

	public RevisionInfo getAddressRevisionInfo() {
		return addressRevisionInfo;
	}

	public void setAddressRevisionInfo(RevisionInfo addressRevisionInfo) {
		this.addressRevisionInfo = addressRevisionInfo;
	}

}
