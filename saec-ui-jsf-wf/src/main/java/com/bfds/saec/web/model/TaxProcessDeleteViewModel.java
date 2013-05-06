package com.bfds.saec.web.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.out.damasco_domestic.OutboundDomesticTaxRec;
import com.bfds.saec.web.service.TaxProcessService;

/**
 * 
 * A view model for deleting outbound table contents.
 * 
 */
public class TaxProcessDeleteViewModel
		extends
			AbstractDeleteModel<OutboundDomesticTaxRec> implements Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger log = LoggerFactory
			.getLogger(TaxProcessDeleteViewModel.class);

	@Autowired
	transient TaxProcessService taxProcessService;

	/**
	 * Gets all the {@link OutboundDomesticTaxRec}s that are on
	 * OutboundDomesticTaxRec table which are not processed yet.
	 * 
	 * @return - A {@link List} of {@link OutboundDomesticTaxRec}s
	 * 
	 */
	public List<OutboundDomesticTaxRec> loadAllOutboundDomesticTaxRecs() {
		if(log.isInfoEnabled())
			log.info(String.format("Processing all outbound domestinc tax records..."));
		return this.taxProcessService.getAllUnProcessedOutboundDomesticTaxRec();
	}

	@Override
	protected void persistDelete(Collection<OutboundDomesticTaxRec> values) {
        taxProcessService.deleteAll(values);
		if(log.isDebugEnabled())
			log.debug(String.format("Deleted Outbound Domestic Tax Records: %s.",values.size()));
	}
}
