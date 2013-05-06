package com.bfds.saec.batch.in.db_stop_confirmation;

import com.bfds.saec.batch.file.domain.in.db_stop_confirmation.StopConfirmationRec;
import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Batch Services for Deutsche Bank Input File
 * 
 */
@Service
public class StopConfirmationBatchServiceImpl implements StopConfirmationBatchService {

	final Logger log = LoggerFactory
			.getLogger(StopConfirmationBatchServiceImpl.class);

	@Autowired
	private PaymentDao paymentDao;

	private String dateString;

	private Date stopConfirmationDate;

	@Override
	public void processStopConfirmations(List<? extends StopConfirmationRec> items) {
		log.info("Processing Stop Confirmations ... ");

		for (StopConfirmationRec rec : items) {
			validate(rec);
			// TODO Change to enum?
			if ("T".equals(rec.getRecordType())) {
				log.debug("Trailer record found and skipping ...");
				continue;
			}
			final Payment check = Payment.findCheckByNumberAndAmount(rec.getCheckNumber(),rec.getStopAmount());

			if (check == null) {
				// TODO: Record error.
				log.error("check can't be null!");
				continue;
			}

			if ("000".equals(rec.getRejectCode())) {
				check.setPaymentCode(PaymentCodeUtil.getStopConfirmCodeForGivenStopRequestedCode(check.getPaymentCode()));
			} else {
				check.setPaymentCode(PaymentCodeUtil.getStopRejectedCodeForGivenStopRequestedCode(check.getPaymentCode()));

			}
			check.setStatusChangeDate(rec.getEffectiveDate());
		}
	}

	private void validate(StopConfirmationRec rec) {
		Preconditions
				.checkState(
						rec.getRejectCode() != null,
						String.format(
								"Reject Code for check -- %s -- cannot be null. It should be either 000 or >0",
								rec.getCheckNumber()));
	}
}