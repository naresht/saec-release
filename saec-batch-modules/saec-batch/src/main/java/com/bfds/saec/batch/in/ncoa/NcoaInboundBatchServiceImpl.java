package com.bfds.saec.batch.in.ncoa;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bfds.saec.batch.file.domain.in.ncoa_link_record.NCOALinkRec;
import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.AddressChange;
import com.bfds.saec.domain.activity.AddressResearchChangeActivity;
import com.bfds.saec.domain.reference.AddressType;
@Service
public class NcoaInboundBatchServiceImpl implements NcoaInboundBatchService{

	final Logger log = LoggerFactory
			.getLogger(NcoaInboundBatchServiceImpl.class);

	/**
	 * this method processes all {@link NCOALinkRec} records and update the SAEC system
	 * with the new address for a given claimant, It will update all activity history for the records.
	 * this method overrides @see com.bfds.saec.batch.in.ncoa.NcoaInboundBatchService#ProcessNcoaInboundRecords
	 * (java.util.List)
	 */
	@Override
	public void ProcessNcoaInboundRecords(List<? extends NCOALinkRec> items) {
		for (NCOALinkRec ncoalinkRec : items) {
			Claimant claimant = null;
			String claimantRefNum = ncoalinkRec.getClaimantRefNumber();
			if (StringUtils.hasText(claimantRefNum)) {
				claimant = Claimant.findClaimant(claimantRefNum);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Claimant Reference Number can not be NULL");
				}
				return;
			}
			
			if(StringUtils.hasText(ncoalinkRec.getOverallProbableCorrectness())){
				updateClaimantAddressforNcoa(ncoalinkRec,claimant);
				updateAddressResearhChangeActivity(ncoalinkRec,claimant);
				updateActivity(claimant,ncoalinkRec);
				updateAddressChangeEntity(claimant,ncoalinkRec);
				claimant.setAddressResearchSent(Boolean.FALSE);
			}
			else{
				updateAddressResearhChangeActivity(ncoalinkRec,claimant);
				updateActivity(claimant,ncoalinkRec);
				claimant.setAddressResearchSent(Boolean.FALSE);
			}
			claimant.merge();
		}
	}
	
	/**
	 * Update a {@link ClaimantAddress} with the new address from the Ncoa Address research 
	 * @param ncoalink , The address research record which is returned from NCOA
	 * @param c, Claimant to be updated in the SAEC system with new address
	 */
	private void updateClaimantAddressforNcoa(NCOALinkRec ncoalink,Claimant claimant) {
		String primary_add= ncoalink.getNewStandardPrimaryAddress();
		String secondary_add=ncoalink.getNewStandardSecondaryAddress();


		ClaimantAddress mailingAddress = claimant.getMailingAddress();
		if(mailingAddress == null) {
		log.error("Mailing address is null for Claimant %s. Skipping ncoa processing.", claimant.getReferenceNo());
		return ;
		}
		mailingAddress.setAddress1(primary_add);
		mailingAddress.setAddress2(secondary_add);
		mailingAddress.setCity(ncoalink.getNewStandardCity());
		mailingAddress.setStateCode(ncoalink.getNewStandardState());


		if(StringUtils.hasText(ncoalink.getNewStandardZipCode()) ){
		mailingAddress.setZipCode(new ZipCode(ncoalink.getNewStandardZipCode(),ncoalink.getNewStandardZipExtn()));
		}
		mailingAddress.merge();
		}

	

	/**
	 * Updates the  {@link ClaimantAddress} with activity information.
	 * @param ncoalink address research record which is returned from NCOA
	 * @param claimant Claimant used to the get the mailing address.
	 */
	private void updateAddressResearhChangeActivity(NCOALinkRec ncoalink,Claimant claimant)
	{
		ClaimantAddress cli_address=claimant.getMailingAddress();
		
		AddressResearchChangeActivity activity = new AddressResearchChangeActivity();
		activity.setAddressResearchDate(new Date());
		activity.setAddressResearchDescription("NCOA");
		activity.setResearchReturned(Boolean.TRUE);
		if(StringUtils.hasText(ncoalink.getOverallProbableCorrectness())){
		activity.setHit(Boolean.TRUE);
		}
		else{
			activity.setHit(Boolean.FALSE);
		}
		cli_address.getAddress().setResearchChangeActivity(activity);
		cli_address.merge();
	}

	/**
	 *  adds a activity record to the claimant with the given description  
	 * @param claimant used to add the {@link Activity}
	 * @param ncoalink address research record which is returned from NCOA
	 */
	private void updateActivity(Claimant claimant,NCOALinkRec ncoalink){
		Activity activity=new Activity();
		activity.setActivityTypeDescription("NCOA Address Research");
		if(StringUtils.hasText(ncoalink.getOverallProbableCorrectness())){
		activity.setDescription("NCOA indicates change of Address");
		}
		// Need a clarification on this,when this situation will occur.  
		/*else if(StringUtils.hasText(ncoalink.getNewStandardFirstName()) || StringUtils.hasText(ncoalink.getNewStandardPrimaryAddress()) ||
				StringUtils.hasText(ncoalink.getNewStandardSecondaryAddress()))
		{
			activity.setDescription("NCOA confirms existing Address with Correctness Level = "+ncoalink.getOverallProbableCorrectness());
		}*/
		else{
			activity.setDescription("No NCOA Match");
		}
		activity.setActivityDate(new Date());
		activity.setUserId("NCOA");
		claimant.addActivity(activity);
		claimant.merge();
	}
	
	/**
	 *  Updates the old and new address in the addressChange activity table. 
	 * @param claimant used to add the {@link AddressChange} activity
	 * @param ncoalink address research record which is returned from NCOA
	 */
	private void updateAddressChangeEntity(Claimant claimant,NCOALinkRec ncoalink)
	{
		String old_primary_address=ncoalink.getOldStandardPrimaryAddress();
		String old_secondary_address=ncoalink.getOldStandardSecondaryAddress();
		String old_city=ncoalink.getOldStandardCity();
		String old_state=ncoalink.getOldStandardState();
		String old_zip=ncoalink.getOldStandardZipCode();
		String old_zip_extn=ncoalink.getOldStandardZipExtn();
		
		Address address_from=new Address();
		if(StringUtils.hasText(old_primary_address)){
		address_from.setAddress1(old_primary_address);
		}
		if(StringUtils.hasText(old_secondary_address)){
			address_from.setAddress2(old_secondary_address);
		}
		address_from.setCity(old_city);
		address_from.setStateCode(old_state);
		address_from.setZipCode(new ZipCode(old_zip, old_zip_extn));
		
		
		String new_primary_address=ncoalink.getNewStandardPrimaryAddress();
		String new_secondary_address=ncoalink.getNewStandardSecondaryAddress();
		String new_city=ncoalink.getNewStandardCity();
		String new_state=ncoalink.getNewStandardState();
		String new_zip=ncoalink.getNewStandardZipCode();
		String new_zip_extn=ncoalink.getNewStandardZipExtn();
		
		Address address_to=new Address();
		if(StringUtils.hasText(new_primary_address)){
			address_to.setAddress1(new_primary_address);
		}
		if(StringUtils.hasText(new_secondary_address)){
			address_to.setAddress2(new_secondary_address);
		}
		address_to.setCity(new_city);
		address_to.setStateCode(new_state);
		address_to.setZipCode(new ZipCode(new_zip, new_zip_extn));
		
		address_to.setAddressType(claimant.getMailingAddress().getAddressType());
		
		AddressChange addressChange = new AddressChange() ;
		addressChange.setFrom(address_from);
		addressChange.setTo(address_to);
		
		addressChange.setUserId("NCOA");
		addressChange.setActivityDate(new Date());
		addressChange.setClaimant(claimant);
		claimant.addAddressChangeActivity(addressChange);
		
		claimant.merge();
		
	}
}
