package com.bfds.saec.batch.out.ss_bottom_line;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfds.saec.batch.file.domain.out.ss_bottomline.SsBottomLineOutRec;
import com.bfds.saec.batch.util.RegistrationAndAddressLinesUtil;
import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;

@Service
public class BottomLineOutBoundBatchServiceImpl implements BottomLineOutBoundBatchService {

	final Logger log = LoggerFactory.getLogger(BottomLineOutBoundBatchServiceImpl.class);	

	@Autowired
	private IMailSender mailSender;	 
	 
	protected BottomLineOutBoundNotificationDto obBottomLineOutNotification = new BottomLineOutBoundNotificationDto();	

	@Override
	public SsBottomLineOutRec issueBottomLine(Payment check) {
		
		check = Payment.findPayment(check.getId());
		check.setSentOnBottomLineFile(Boolean.TRUE);
		check = check.merge();

		updateNotificationInfo(check);

		// Update remaining props
		final SsBottomLineOutRec obBottomlineOut = new SsBottomLineOutRec();
		obBottomlineOut.setAccountNumber(check.getPayTo().getReferenceNo());
		obBottomlineOut.setCheckAmount(check.getPaymentAmount().doubleValue());
		obBottomlineOut.setPayableDate(check.getPaymentDate());
		obBottomlineOut.setFundCode(check.getPayTo().getFundAccountNo());

		updateRegistrationAndAddressesLines(check,obBottomlineOut);

		obBottomlineOut.setCheckName(Event.getCurrentEvent().getCheckNameforBottomlineOutFile());
		obBottomlineOut.setDda(Event.getCurrentEventDda());
		obBottomlineOut.setSystem(Event.getCurrentEventCode());
		obBottomlineOut.setOffsettingDda(Event.getCurrentEventDda());

		return obBottomlineOut;
	}

	private void updateNotificationInfo(Payment check) {
		BigDecimal totalAmount = obBottomLineOutNotification.getTotalAmount();
		totalAmount = totalAmount.add(check.getPaymentAmount());
		obBottomLineOutNotification.setTotalAmount(totalAmount);
		obBottomLineOutNotification.setRecordCount(obBottomLineOutNotification.getRecordCount() + 1);		
	}

	
	private String getSsBottomlineOutMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "BottomLinefile_" + currentMonthDateYear + "";
		return subject;
	}
	
	@Override
	public BottomLineOutBoundNotificationDto initOBBottomLineOutNotification() {
		obBottomLineOutNotification = new BottomLineOutBoundNotificationDto();		
		return obBottomLineOutNotification;
	}
	
	@Override
    public BottomLineOutBoundNotificationDto notifySsBottomLineOut() {
		final BottomLineOutBoundNotificationDto ret = obBottomLineOutNotification;
		obBottomLineOutNotification.setMailSubject(getSsBottomlineOutMailSubject());
		obBottomLineOutNotification.setDda(Event.getCurrentEventDda());
		obBottomLineOutNotification.setFileDate(new Date());
        mailSender.send(MailingList.findByCode("batch.jobs.statestreetbank"), obBottomLineOutNotification.getMailSubject(),
        		BottomLineOutBoundNotificationDto.getNotificationText(obBottomLineOutNotification));
        log.info("Sent mail for SsBottomLineOut Notification....");
        obBottomLineOutNotification = new BottomLineOutBoundNotificationDto();
        return obBottomLineOutNotification;
    }
	
	/**
	 * Updates Registration fields with name and addresses
     * The file has 7 lines to hold both the registration and address.
     * First the registration lines must be filled followed by the address lines. However the registration lines should not occupy more than 4 lines. 
     * 
     *
     * Eg:- if "aaa", "bbb" are the registration lines and "ccc", "ddd" are address lines then file object will have the following
     *
     * "aaa", "bbb", "ccc", "ddd", null, null, null.
     * 
     * Eg:- if "aaa", "bbb", "ccc", "ddd", "eee" are the registration lines and "fff", "ggg" are address lines then file object will have the following
     *
     * "aaa", "bbb", "ccc", "ddd", "fff", "ggg", null.
     *
	 * @param payment
	 * @param obBottomlineOut
	 */
	private void updateRegistrationAndAddressesLines(Payment payment,final SsBottomLineOutRec obBottomlineOut) {

		List<String> registrationLines = RegistrationAndAddressLinesUtil
										.buildRegistrationLines(getCheckAddress(payment), payment.getPayTo().getClaimantRegistration());

		int index = 0;
		for (String line : registrationLines) {
			if (index > 6) {
				return;
			}
			setRegistrationLine(obBottomlineOut, line, index++);
		}
	}

	private void setRegistrationLine(SsBottomLineOutRec obBottomlineOut,
			String line, int i) {
		switch (i) {
		case 0:
			obBottomlineOut.setRegistration1(line);
			break;
		case 1:
			obBottomlineOut.setRegistration2(line);
			break;
		case 2:
			obBottomlineOut.setRegistration3(line);
			break;
		case 3:
			obBottomlineOut.setRegistration4(line);
			break;
		case 4:
			obBottomlineOut.setRegistration5(line);
			break;
		case 5:
			obBottomlineOut.setRegistration6(line);
			break;
		case 6:
			obBottomlineOut.setRegistration7(line);
			break;

		}
	}
		
	/**
     * returns an address If the the check has a corresponding address object other wise the corresponding claimant's address will be returned. 
     * @param payment
     * @return  address
     */
    private static Address getCheckAddress(final Payment payment) {
        if(payment.getCheckAddress() != null) {
            return payment.getCheckAddress().getAddress();
        }
        return payment.getPayTo().getMailingAddress().getAddress();
    }

}
