package com.bfds.saec.web.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.web.util.JsfUtils;

public class AlternatePayeeViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	final Logger log = LoggerFactory.getLogger(AlternatePayeeViewModel.class);

	public boolean save(final MessageContext messageContext,
			final ClaimantViewModel parent,
			final ClaimantViewModel claimantViewModel_1) {
		if (!valid(claimantViewModel_1.getClaimant(), messageContext)) {
			return false;
		}
		final Claimant parentClaimant = Claimant.findClaimant(parent
				.getClaimant().getId());
		String referenceNo_1 = save(claimantViewModel_1.getClaimant(),
				parentClaimant);
		messageContext.addMessage(new MessageBuilder()
				.info()
				.defaultText(
						"A New Alternate Payee is created with Reference #"
								+ referenceNo_1).build());
		if(log.isInfoEnabled())
			log.info(String.format("A New Alternate Payee with Claimant Id : %s is created.",claimantViewModel_1.getClaimant().getId()));
		
		return true;

	}

	@Transactional
	public String save(final Claimant claimant, final Claimant parent) {
		parent.addAlternatePayee(claimant);
		claimant.persist();
		return claimant.getReferenceNo();
	}

	public boolean valid(final Claimant claimant, MessageContext messageContext) {
		boolean ret = true;
		if (Event.getCurrentEvent().getRequiresTaxInfo()) {
			if (claimant.getUsCitizen() == null) {
				messageContext.addMessage(new MessageBuilder().error()
						.source(JsfUtils.getClientId("claimant_USCitizen"))
						.defaultText("US citizen is mandatory.").build());
				ret = false;
			}

			if (!StringUtils.hasText(claimant.getTaxCountryCode())) {
				messageContext.addMessage(new MessageBuilder().error()
						.source(JsfUtils.getClientId("claimant_TaxCountry"))
						.defaultText("Tax country is mandatory.").build());
				ret = false;
			}
		}
		return ret;

	}
}
