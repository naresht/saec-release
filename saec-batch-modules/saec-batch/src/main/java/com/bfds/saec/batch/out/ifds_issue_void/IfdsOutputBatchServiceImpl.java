package com.bfds.saec.batch.out.ifds_issue_void;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bfds.saec.batch.file.domain.out.ifds_issue_void.IfdsIssueVoidRec;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.util.ConverterUtils;
import com.google.common.base.Preconditions;

@Service
public class IfdsOutputBatchServiceImpl implements IfdsOutputBatchService {
	final Logger log = LoggerFactory
			.getLogger(IfdsOutputBatchServiceImpl.class);

	protected IfdsIssueVoidNotificationDto ifdsIssueVoidNotificationDto = new IfdsIssueVoidNotificationDto();

	@Autowired
	private IMailSender mailSender;

	private Date checkIssueDate;

	@Override
	public IfdsIssueVoidRec issueVoid(Payment check) {
		final String dda = Event.getCurrentEventDda();
		Preconditions.checkState(dda != null,
				"DDA must be set at event level. DDA is null");

		if (check.getIdentificatonNo() == null) {
			return null;
		}
		//
		BigDecimal totalAmount = ifdsIssueVoidNotificationDto.getAccountTotalAmount();
		totalAmount = totalAmount.add(check.getPaymentAmount());
		ifdsIssueVoidNotificationDto.setAccountTotalAmount(totalAmount);
		ifdsIssueVoidNotificationDto.setIssueDate(new Date());
		ifdsIssueVoidNotificationDto.setAccountTotalItems(ifdsIssueVoidNotificationDto.getAccountTotalItems() + 1);
		//
		log.info(String.format("Processing check id: %s, check#:%s ", check.getId(), check.getIdentificatonNo()));

		final IfdsIssueVoidRec ifdsissueVoid = new IfdsIssueVoidRec();

		if (isCreated(check) || isOutstanding(check)) {
			ifdsissueVoid.setFlag(" ");
		} else if(isVoid(check)){
			ifdsissueVoid.setFlag(PaymentCode.valueOf(check.getPaymentCode().name()).toString());
		}
		ifdsissueVoid.setCheckNumber(check.getIdentificatonNo());
		final String offsetCheckNo = getOffsetchecknumber(check);
		if (StringUtils.hasText(offsetCheckNo)) {
			ifdsissueVoid.setOffsetchecknumber(offsetCheckNo);
		} else {
			ifdsissueVoid.setOffsetchecknumber(""); // TODO: An issue will arise at this point.When there is no offsetCheckNumber,
													// instead of having blanks in the file, 0s will be printed. Need to fix.
		}
		ifdsissueVoid.setAmount(check.getPaymentAmount().doubleValue());
		ifdsissueVoid.setIssuedate(check.getPaymentDate());
		ifdsissueVoid.setPayeeline1(check.getPayTo().getClaimantRegistration().getRegistration1());
		ifdsissueVoid.setPayeeline2(check.getPayTo().getClaimantRegistration().getRegistration2());
		ifdsissueVoid.setDda(dda);

		final Payment c = Payment.findPayment(check.getId());
		c.setIfdsSent(Boolean.TRUE);
		c.merge();

		return ifdsissueVoid;
	}

	
	
	/**
	 * This method looks for the payments which  are in ISO status and reissued from it's parent payment,Here we will recursively check for the top most parent
	 * of the payment and will get the identificationno of that.In case of split payments we won't get the identificationno of it's parent,
	 * it will be blank in the actualfile layout.
	 * The OffsetcheckNumber will come in 2 ways here,One is  a normal check reissue process and the other is Split payment process,So based on the 
	 * conditions we will pick the parent checknumber if it has a spilt account.
	 * @param check
	 * 
	 */
	private String getOffsetchecknumber(Payment check) {
		check=check.merge(); // loading the payment object again to get the child relations of it.
		String ret = null;
		if(check.getPaymentCode() == PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO) {
			if (check.getSplitOf() == null) {
				Payment parentPayment = check.getReissueOf();
				if(parentPayment == null) {
					throw new IllegalStateException(String.format(
							"corresponding First Issuance check not found for Re Issuance Check : %s, with code %s",
							check.getIdentificatonNo(),
							check.getPaymentCode()));
				}						
				for(; check.getReissueOf() != null; parentPayment = check.getReissueOf()) {					
					ret = parentPayment.getIdentificatonNo();	
					if(StringUtils.hasText(ret)) {					
						break;
					}
				}
				if(parentPayment.getPaymentType()==PaymentType.CHECK){
					if(!StringUtils.hasText(parentPayment.getIdentificatonNo())) {
						throw new IllegalStateException(String.format(
								"corresponding First Issuance check for Re Issuance Check : %s, with code %s, does not have a check#",
								check.getIdentificatonNo(),
								check.getPaymentCode()));
					}	
				}
			} 
			
			else {
				Payment parentPayment = check.getSplitOf();
				if(parentPayment == null) {
					throw new IllegalStateException(String.format(
							"corresponding First Issuance check not found for Re Issuance Check : %s, with code %s",
							check.getIdentificatonNo(),
							check.getPaymentCode()));
				}						
				for(; check.getSplitOf() != null; parentPayment = check.getSplitOf()) {					
					ret = parentPayment.getIdentificatonNo();	
					if(StringUtils.hasText(ret)) {					
						break;
					}
				}
				if(parentPayment.getPaymentType()==PaymentType.CHECK){
					if(!StringUtils.hasText(parentPayment.getIdentificatonNo())) {
						throw new IllegalStateException(String.format(
								"corresponding First Issuance check for Re Issuance Check : %s, with code %s, does not have a check#",
								check.getIdentificatonNo(),
								check.getPaymentCode()));
					}	
				}
			}
		}		 				
		else if (check.getPaymentCode() == PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO) {
				throw new IllegalStateException("NIO PaymentCode is not supported");
			// TODO: Refer IFDS spec.
		}
		return ret;
	}


	@Override
	public void notifyIssueVoid() {
		ifdsIssueVoidNotificationDto.setDda(Event.getCurrentEventDda());
		mailSender.send(MailingList.findByCode("batch.jobs.ifds"), getIFDSIssuevoidMailSubject(), getNotificationText(ifdsIssueVoidNotificationDto));
		ifdsIssueVoidNotificationDto.setAccountTotalAmount(new BigDecimal(0));
		ifdsIssueVoidNotificationDto.setAccountTotalItems(0);
	}
	
	private String getIFDSIssuevoidMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "IFDS issue/void " + currentMonthDateYear + "";
		return subject;
	}

	protected String getNotificationText(
			IfdsIssueVoidNotificationDto ifdsIssueVoidNotificationDto) {
		if (ifdsIssueVoidNotificationDto != null) {
			if (ifdsIssueVoidNotificationDto.getIssueDate() != null) {
				checkIssueDate = ifdsIssueVoidNotificationDto.getIssueDate();
			} else {
				checkIssueDate = new Date();
			}
		}
		return "DDA :: "
				+ ifdsIssueVoidNotificationDto.getDda()
				+ "\n"
				+ "Issue date ::"
				+ ConverterUtils
						.getFormatedYearDate(checkIssueDate, "MM/dd/yy") + "\n"
				+ "Account total amount ::"
				+ ifdsIssueVoidNotificationDto.getAccountTotalAmount() + "\n"
				+ "Account total items ::"
				+ ifdsIssueVoidNotificationDto.getAccountTotalItems();
	}

	private boolean isOutstanding(Payment check) {
		return PaymentCodeUtil.getOutstandingCodes().contains(check.getPaymentCode());
	}

	private boolean isCreated(Payment check) {
		return PaymentCodeUtil.getCreatedCodes().contains(check.getPaymentCode());
	}

	/*
	 * Including void codes as well as void_reissue_requested codes.
	 */
	private boolean isVoid(Payment check) {
		return PaymentCodeUtil.getVoidedCodes().contains(check.getPaymentCode()) || PaymentCodeUtil.getVoidReissueRequestedCodes().contains(check.getPaymentCode());
	}
}
