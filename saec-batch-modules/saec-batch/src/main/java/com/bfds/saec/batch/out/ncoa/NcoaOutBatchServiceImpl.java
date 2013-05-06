package com.bfds.saec.batch.out.ncoa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bfds.saec.batch.file.domain.out.ncoa_outbound.NCOAOutboundRec;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.activity.Activity;

@Service
public class NcoaOutBatchServiceImpl implements NcoaOutBatchService{

	final Logger log = LoggerFactory.getLogger(NcoaOutBatchServiceImpl.class);

	/**  this method prepares all outgoing records {@link Claimant} that need to be searched for
	 *   the new address in the NCOA.
	 *   this method overrides @see com.bfds.saec.batch.out.ncoa.NcoaOutBatchService#createNcoaAddressResearch(com.bfds.saec.domain.Claimant)
	 */
	@Override
	public NCOAOutboundRec createNcoaAddressResearch(Claimant claimant)
	{
        if(log.isInfoEnabled()) {
            log.info(String.format("Sending claimant %s for NCOA address research", claimant.getReferenceNo()));
        }
		claimant= Claimant.findClaimant(claimant.getReferenceNo());
		NCOAOutboundRec ncoaRec=buildNcoaAddressRecord(claimant);
		
		claimant.setAddressResearchSent(Boolean.TRUE);
		claimant.merge();
		
		Activity activity=new Activity();
		activity.setActivityTypeDescription("NCOA Address Research");
		activity.setDescription(" ");
		activity=Activity.setActivityDefaults(activity);
		claimant.addActivity(activity);
		return ncoaRec;
	}
	
	/**
	 * Sets the Claimant info in the Ncoa Outbound Record
	 * @param claimant used to get the address info
	 * @return NCOAOutboundRec used to store the info from claimant and sent for address research
	 */
	private NCOAOutboundRec buildNcoaAddressRecord(final Claimant claimant)
	{
		NCOAOutboundRec ncoaOutboundRec=new NCOAOutboundRec();
		if(claimant.getMailingAddress()==null)
		{
			log.debug("Address object is null");
			return ncoaOutboundRec;	
		}
		String eventCode=Event.getCurrentEventDda()!=null?Event.getCurrentEventDda():"";
		ncoaOutboundRec.setReferenceInformation(eventCode+""+claimant.getReferenceNo());
		if(claimant.getName()!=null)
		{
		ncoaOutboundRec.setName(claimant.getName().getAsString());
		}
		else{
			ncoaOutboundRec.setName(claimant.getClaimantRegistration().getRegistrationLinesAsString(" "));
		}
		
		ncoaOutboundRec.setAddress(claimant.getMailingAddress().getAddressLinesAsString(""));
		ncoaOutboundRec.setCity(claimant.getMailingAddress().getCity());
		ncoaOutboundRec.setState(claimant.getMailingAddress().getStateCode()) ;
		ncoaOutboundRec.setZip(claimant.getMailingAddress().getZipCode().toString());
		ncoaOutboundRec.setDda(eventCode);
		return ncoaOutboundRec;
	}
	 
}
