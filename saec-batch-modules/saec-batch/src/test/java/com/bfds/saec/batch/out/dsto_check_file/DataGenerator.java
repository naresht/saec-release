package com.bfds.saec.batch.out.dsto_check_file;

import java.math.BigDecimal;

import com.bfds.saec.domain.*;
import com.bfds.saec.domain.reference.BankLov;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

public class DataGenerator extends com.bfds.saec.batch.util.DataGenerator {
	
	public static Claimant getDSTOClaimant()
	{
		Claimant claimant = newClaimant();
		createDSTOEvent();
		Payment c;

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("1234561444");
		c.setPaymentAmount(new BigDecimal("100.75"));
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setDstoCheckFileSentFlag(Boolean.FALSE);
		c.setPayTo(claimant);
		c.setAuditable(Boolean.TRUE);
        PaymentLetterCode lc = new PaymentLetterCode("100", "L1 desc");
		lc.persist();
		lc.flush();
		c.setLetterCode(lc);
		claimant.addCheck(c);

		claimant.persist();
		return claimant;
	}	
}
