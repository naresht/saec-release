/**
 * 
 */
package com.bfds.saec.batch.out.ifds_check_status;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.stereotype.Service;

import com.bfds.saec.batch.file.domain.out.ifds_check_status.IfdsCheckStatusRec;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

/**
 * @author dt83395
 * 
 */
@Service
public class IfdsCheckStatusBatchServiceImpl extends
		IfdsCheckStatusNotifyServiceImpl implements IfdsCheckStatusBatchService {
	final Logger log = LoggerFactory
			.getLogger(IfdsCheckStatusBatchServiceImpl.class);
	
	private PIfdsCheckStatusFileItemValidator validator = new PIfdsCheckStatusFileItemValidator();
	
	@Override
	public IfdsCheckStatusRec getCheckStatus(Payment check) {
		validator.validate(check);
		
		log.info(String.format("Processing IFDS Payment -  Type: %s, identification#: %s, paymentAmount: %s ", 
				 check.getPaymentType(), check.getIdentificatonNo(), check.getPaymentAmount()));
		
		final IfdsCheckStatusRec ifdsCheckStatusDto = new IfdsCheckStatusRec();

        if(check.getPaymentType().equals(PaymentType.ROF)){
            ifdsCheckStatusDto.setCheckNumber(check.getRofOf().getIdentificatonNo());
        } else if(check.getPaymentType().equals(PaymentType.WIRE) && check.getReissueOf() != null){
            ifdsCheckStatusDto.setCheckNumber(check.getReissueOf().getIdentificatonNo());
        }
        else if(check.getPaymentType().equals(PaymentType.WIRE) && check.getSplitOf() != null){
            ifdsCheckStatusDto.setCheckNumber(check.getSplitOf().getIdentificatonNo());
        }
        else{
            ifdsCheckStatusDto.setCheckNumber(check.getIdentificatonNo());
        }
		ifdsCheckStatusDto.setNetAmount(check.getPaymentAmount().doubleValue());
		ifdsCheckStatusDto.setStatusChangeDate(check.getPaymentDate());
		if (Event.getCurrentEventDda() != null) {
			ifdsCheckStatusDto.setDda(Event.getCurrentEvent().getDda());
		}
		if (check.getPaymentType().equals(PaymentType.CHECK)||check.getPaymentType().equals(PaymentType.ROF))
			ifdsCheckStatusDto.setCheckStatus(PaymentCode.valueOf(check.getPaymentCode().name()).toString());
		
		if (check.getPaymentType().equals(PaymentType.WIRE))
			ifdsCheckStatusDto.setWireStatus(PaymentCode.valueOf(check.getPaymentCode().name()).toString());
		
		ifdsCheckStatusDto.setAccountNumber(check.getPayTo().getReferenceNo());
		
		if (Event.getCurrentEvent().getBank().getAbaNo() != null)
			ifdsCheckStatusDto.setBankRouting(Event.getCurrentEvent().getBank().getAbaNo());
		else {
			ifdsCheckStatusDto.setBankRouting("");
		}
		Payment c = Payment.findPayment(check.getId());
		c.setIfdsSent(Boolean.TRUE);
		c.merge();
		
		BigDecimal totalAmount = ifdsCheckStatusNotificationDto
				.getTotalAmount();
		ifdsCheckStatusNotificationDto
				.setRecordCount(ifdsCheckStatusNotificationDto.getRecordCount() + 1);
		totalAmount = totalAmount.add(check.getPaymentAmount());
		ifdsCheckStatusNotificationDto.setTotalAmount(totalAmount);

		ifdsCheckStatusNotificationDto.setFileDate(new Date());
		return ifdsCheckStatusDto;
	}

	
	private static class PIfdsCheckStatusFileItemValidator implements Validator<Payment> {

		@Override
		public void validate(Payment payment) throws ValidationException {
			if(PaymentType.CHECK.equals(payment.getPaymentType())) {
				if(payment.getIdentificatonNo() == null) {					
					throw new ValidationException(String.format("Identification# is null for Check : paymentAmount: %s ", payment.getPaymentAmount()));
				}
			}						
		}		
	}

}
