package com.bfds.saec.batch.out.bank_issue_void;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import com.bfds.saec.batch.file.domain.out.db_issue_void.BankIssueVoidRec;
import com.bfds.saec.batch.file.domain.out.ss_issue_void.SsIssueVoidRec;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bfds.saec.batch.file.domain.out.db_issue_void.DbIssueVoidRec;
import com.bfds.saec.batch.out.dto.IssueVoidNotificationDto;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.RegistrationLines;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.google.common.collect.Sets;

/**
 * Batch Services for Deutsche Bank Input File
 */
@Service
public class IssueVoidBatchServiceImpl implements IssueVoidBatchService {

	final Logger log = LoggerFactory.getLogger(IssueVoidBatchServiceImpl.class);

    /**
     *  Void {@link PaymentCode}s that are eligible to be sent on issue void file.
     */
    static final Set<PaymentCode> eligibleVoidPaymentCodes;

    @Autowired
    private IMailSender mailSender;

    protected IssueVoidNotificationDto issueVoidNotification = new IssueVoidNotificationDto();

    static {
        eligibleVoidPaymentCodes = Sets.newHashSet();
        eligibleVoidPaymentCodes.addAll(PaymentCodeUtil.getVoidedCodes());
        eligibleVoidPaymentCodes.addAll(PaymentCodeUtil.getVoidReissueRequestedCodes());
        eligibleVoidPaymentCodes.add(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW);
    }

	@Override
	public BankIssueVoidRec issueVoid(Payment check, String bank) {
        Preconditions.checkArgument("SS".equals(bank) || "DB".equals(bank), "bank must be either 'SS' or 'DB'. Is %s", bank);

		check = Payment.findPayment(check.getId());
		log.info(String.format("Processing %s Issue Void check : %s", bank, check.getIdentificatonNo()));

        if (!StringUtils.hasText(check.getIdentificatonNo()) && PaymentCodeUtil.getCreatedCodes().contains(check.getPaymentCode())) {
            // Try to assign a new check# if a created status check does not have a check#.
        	if("SS".equals(bank) && Boolean.TRUE.equals(Event.getCurrentEvent().getUseBottomLineForCheckNoAssignment())) {
        	 	// This check will be processed after bottom line process assigns it a new check#. 
        	 	return null;
        	 }
            assignNewCheckNo(check, bank);
        }
        Preconditions.checkState(StringUtils.hasText(check.getIdentificatonNo()), "Check# not assigned for Payment: %s", check.getId());
        if(check.getPaymentAmount() == null 
        || check.getPaymentAmount().compareTo(new BigDecimal(0))==0) {
            throw new IllegalStateException(String.format("Payment amount is zero for %s Issue Void check : %s", bank, check.getId()));
        }

		updateDBIssueVoidNotification(check);

		final BankIssueVoidRec issueVoid = newBankIssueVoidRec(bank);

		if(getEligibleVoidCodes().contains(check.getPaymentCode())) {
            issueVoid.setVoidIndicator("V");
		}

        issueVoid.setCheckNumber(check.getIdentificatonNo());
        issueVoid.setCheckAmount(check.getPaymentAmount().doubleValue());
        issueVoid.setIssueDate(check.getPaymentDate());
        issueVoid.setPayeeLine1(check.getPayTo().getClaimantRegistration().getRegistration1());
        issueVoid.setPayeeLine2(check.getPayTo().getClaimantRegistration().getRegistration2());

        issueVoid.setDda(Event.getCurrentEventDda());

        if (PaymentCodeUtil.getCreatedCodes().contains(check.getPaymentCode())) {
            check.setPaymentCode(PaymentCodeUtil.getOutStandingCodeForGivenCreatedCode(check.getPaymentCode()));
            if(check.getCheckAddress().getAddressType() != AddressType.ONE_TIME_MAILING && check.getCheckAddress().getAddressType() != AddressType.SEASONAL_ADDRESS ){
                MailObjectAddress address = new MailObjectAddress();
                check.getPayTo().getMailingAddress().copyTo(address);
                check.setCheckAddress(address);
            }
            /**  here we are copying the registrationlines from claimant to the check paytoregistration since we need to show the updated registration
                 data for a check in payTo section of UI when it went to outstanding state.         */
            RegistrationLines payToRegistration = new RegistrationLines();
			check.getPayTo().getClaimantRegistration().getLines()
					.copyTo(payToRegistration);
            check.setPayToRegistration(payToRegistration);
        }
        if("SS".equals(bank)) {
            final String additionalData = check.getPayTo().getRegistrationLinesAsString("");
            if (StringUtils.hasText(additionalData) && additionalData.length() > 160) {
                issueVoid.setAdditionalData(additionalData.substring(0, 160));
            } else {
                issueVoid.setAdditionalData(additionalData);
            }
        }
        check.setSentOnBankIssueVoidFile(Boolean.TRUE);
        check.merge();

		return issueVoid;
	}

    private BankIssueVoidRec newBankIssueVoidRec(String bank) {
        if("SS".equals(bank)) {
           return new SsIssueVoidRec();
        } else if("DB".equals(bank)) {
            return  new DbIssueVoidRec();
        }
        throw new IllegalArgumentException("unknown bank " + bank);
    }

    private void assignNewCheckNo(Payment check, String bank) {
        final String newCheckNo = Event.getCurrentEvent().getNextCheckSequenceNumber();
        if (!StringUtils.hasText(newCheckNo)) {
            throw new IllegalStateException("Generated check# is null. Payment ID:" + check.getId());
        }
        if(log.isInfoEnabled()) {
            log.info(String.format("Assigning new Check#: %s to check ID: %s.", newCheckNo, check.getId()));
        }
        check.setIdentificatonNo(newCheckNo);
    }

    private Set<PaymentCode> getEligibleVoidCodes() {
        return eligibleVoidPaymentCodes;
    }

	private void updateDBIssueVoidNotification(Payment check) {
		BigDecimal totalAmount = issueVoidNotification.getAccountTotalAmount();
		totalAmount = totalAmount.add(check.getPaymentAmount());
		issueVoidNotification.setAccountTotalAmount(totalAmount);
        issueVoidNotification.setAccountTotalItems(issueVoidNotification.getAccountTotalItems() + 1);
	}

	private String getIssueVoidMailSubject(String bank) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
        String currentMonthDateYear = formatter.format(new Date());
        if("DB".equals(bank)) {
            return  "DB issue/void_" + currentMonthDateYear;
        } else {
            return  "SSC issue/void/cancel_" + currentMonthDateYear;
        }
	}

	@Override
	public IssueVoidNotificationDto intiDBIssueVoidNotification(String bank) {
        Preconditions.checkArgument("SS".equals(bank) || "DB".equals(bank), "bank must be either 'SS' or 'DB'. Is %s", bank);
		issueVoidNotification = new IssueVoidNotificationDto();
		return issueVoidNotification;
	}

    @Override
    public IssueVoidNotificationDto notifyIssueVoid(String bank) {
        Preconditions.checkArgument("SS".equals(bank) || "DB".equals(bank), "bank must be either 'SS' or 'DB'. Is %s", bank);
        final IssueVoidNotificationDto ret = issueVoidNotification;
        issueVoidNotification.setMailSubject(getIssueVoidMailSubject(bank));
        issueVoidNotification.setDda(Event.getCurrentEventDda());
        issueVoidNotification.setIssueDate(new Date());
        mailSender.send(getMailingList(bank), issueVoidNotification.getMailSubject(),
                IssueVoidNotificationDto.getNotificationText(issueVoidNotification));
        log.info("Sent mail for Issue Void  Notification....");
        issueVoidNotification = new IssueVoidNotificationDto();
        return issueVoidNotification;
    }

    private MailingList getMailingList(String bank) {
        if("DB".equals(bank)) {
            return  MailingList.findByCode("batch.jobs.deutschebank");
        } else {
            return  MailingList.findByCode("batch.jobs.statestreetbank");
        }
    }
}