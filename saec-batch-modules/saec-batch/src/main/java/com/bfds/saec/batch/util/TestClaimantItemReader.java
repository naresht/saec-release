package com.bfds.saec.batch.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.Name;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.MailType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.reference.RPOType;
import com.bfds.saec.schema.util.claimant.Address;
import com.bfds.saec.schema.util.claimant.Claimant;
import com.bfds.saec.schema.util.claimant.Claimants;
import com.bfds.saec.schema.util.claimant.Letter;

public class TestClaimantItemReader implements
		ResourceAwareItemReaderItemStream<TestClaimantDto> {
	final Logger log = LoggerFactory.getLogger(TestClaimantItemReader.class);

	private Resource resource;

	private List<Claimant> claimants;

	private Iterator<Claimant> itr;

	@Override
	public void open(ExecutionContext executionContext)
			throws ItemStreamException {
		claimants = loadClaimants(resource);
		itr = claimants.iterator();
	}

	@Override
	public void update(ExecutionContext executionContext)
			throws ItemStreamException {
		// DO nothing.
	}

	@Override
	public void close() throws ItemStreamException {
		// TODO Auto-generated method stub

	}

	@Override
	public TestClaimantDto read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		if (itr.hasNext()) {
			final Claimant item = itr.next();
			final TestClaimantDto ret = new TestClaimantDto();
			ret.setClaimant(toClaimant(item));
			ret.setClaimantLetters(getLetters(item.getLetter()));
			return ret;
		}
		return null;
	}

	private com.bfds.saec.domain.Claimant toClaimant(Claimant item) {
		com.bfds.saec.domain.Claimant c = new com.bfds.saec.domain.Claimant();
		newClaimant(item, c);
		addPayments(item.getPayment(), c);
		return c;
	}

	private void addPayments(
			List<com.bfds.saec.schema.util.claimant.Payment> payments,
			com.bfds.saec.domain.Claimant claimant) {
		for (com.bfds.saec.schema.util.claimant.Payment payment : payments) {
			Payment c = Payment.newPayment(PaymentCode.valueOf(PaymentCode.class, payment.getPaymentCode().name()));
			c.setIdentificatonNo(payment.getIdentificatonNo());
			if (payment.getPaymentType() != null) {
				c.setPaymentType(PaymentType.valueOf(payment.getPaymentType()
						.name()));
			} else {
				c.setPaymentType(PaymentType.CHECK);
			}
			c.setPaymentAmount(new BigDecimal(payment.getPaymentAmount()));
			//c.setPaymentStatus(PaymentStatus.valueOf(payment.getPaymentStatus()));
			if (payment.getRpoType() != null) {
				c.setRpoType(RPOType.valueOf(payment.getRpoType().name()));
			}
			if (payment.getStatusChangeDate() != null) {
				c.setStatusChangeDate(payment.getStatusChangeDate().getTime());
			}
	
			c.setDstoCheckFileSentFlag(Boolean.FALSE);
			c.setDstoPrintFileSentFlag(Boolean.FALSE);
			claimant.addCheck(c);
		}

	}

	private List<com.bfds.saec.domain.Letter> getLetters(List<Letter> letters) {
		final List<com.bfds.saec.domain.Letter> ret = new ArrayList<com.bfds.saec.domain.Letter>();
		for (com.bfds.saec.schema.util.claimant.Letter letter : letters) {
			com.bfds.saec.domain.Letter _letter = new com.bfds.saec.domain.Letter();

			_letter.setDescription(letter.getDescription());
			_letter.setComments(letter.getComments());
			_letter.setMailDate(letter.getMailDate().getTime());
			_letter.setRpoDate(letter.getRpoDate().getTime());
			_letter.setMailingControlNo(letter.getMailingControlNo());
			_letter.setDstoPrintFileSentFlag(Boolean.FALSE);
			 
			 if(letter.isAuditable()!=null){
				 if(letter.isAuditable())
				 _letter.setAuditable(Boolean.TRUE);
			 }
				 
			if (letter.getMailType() != null) {
				_letter.setMailType(MailType.valueOf(letter.getMailType()
						.value()));
			}
			if (letter.getRpoType() != null) {
				_letter.setRpoType(RPOType.valueOf(letter.getRpoType().value()));
			}
			if (letter.getLetterStatus() != null) {
				_letter.setLetterStatus(com.bfds.saec.domain.reference.LetterStatus
						.valueOf(letter.getLetterStatus().value()));
			}
			_letter.setLetterCode(LetterCode.findByCode(letter.getLetterCode()));

			ret.add(_letter);
		}
		return ret;
	}

	private void newClaimant(Claimant item, com.bfds.saec.domain.Claimant c) {
		c.setName(new Name());
		c.getName().setFirstName(item.getFirstName());
		c.getName().setMiddleName(item.getMiddleName());
		c.getName().setLastName(item.getLastName());
		c.setClaimantRegistration(new ClaimantRegistration());
		c.getClaimantRegistration().setRegistration1(item.getRegistration1());
		c.getClaimantRegistration().setRegistration2(item.getRegistration2());
		c.getClaimantRegistration().setRegistration3(item.getRegistration3());
		c.getClaimantRegistration().setRegistration4(item.getRegistration4());
		c.getClaimantRegistration().setRegistration5(item.getRegistration5());
		c.getClaimantRegistration().setRegistration6(item.getRegistration6());
		c.setFundAccountNo(item.getFundAccountNo());
		c.setAccountStatus(item.getAccountStatus().name());
		c.setAccountType(item.getAccountType().name());
		c.setBrokerId(item.getBrokerId());
		c.setBin(item.getBin());
		c.setSsn(item.getSsn());
		c.setEin(item.getEin());
		c.setTin(item.getTin());
		c.setCorporate(item.isIsCorporate());
		for (Address a : item.getAddress()) {
			com.bfds.saec.domain.ClaimantAddress addr = new com.bfds.saec.domain.ClaimantAddress();
			addr.setAddressType(AddressType.valueOf(a.getAddressType().name()));
			addr.setAddress1(a.getLine1());
			addr.setAddress2(a.getLine2());
			addr.setAddress3(a.getLine3());
			addr.setAddress4(a.getLine4());
			addr.setAddress5(a.getLine5());
			addr.setAddress6(a.getLine6());
			addr.setCity(a.getCity());
			addr.setStateCode(a.getStateCode());
			// addr.setCountryCode(a.getCountryCode());
			addr.setZipCode(new ZipCode(a.getZip(),a.getZipExt()));
			// TODO: set prescrub flags.
			addr.setClaimant(c);
			c.addAddress(addr);
		}
		c.getAddressOfRecord().setMailingAddress(true);
	}

	private PaymentStatus toPaymentStatus(String paymentStatus) {
		return PaymentStatus.valueOf(paymentStatus);
	}

	@Override
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	private List<Claimant> loadClaimants(Resource resource2) {
		List<Claimant> claimantList = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(Claimants.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Claimants claimants = (Claimants) unmarshaller.unmarshal(resource
					.getInputStream());
			claimantList = claimants.getClaimant();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return claimantList == null ? java.util.Collections
				.<Claimant> emptyList() : claimantList;
	}
}