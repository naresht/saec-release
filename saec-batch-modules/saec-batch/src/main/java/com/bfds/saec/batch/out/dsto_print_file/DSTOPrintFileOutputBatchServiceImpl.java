package com.bfds.saec.batch.out.dsto_print_file;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfds.saec.batch.file.domain.out.dsto.DstoRec;

import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.util.ConverterUtils;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;

@Service
public class DSTOPrintFileOutputBatchServiceImpl implements DSTOPrintFileOutputBatchService {

	final Logger log = LoggerFactory
			.getLogger(DSTOPrintFileOutputBatchServiceImpl.class);
	
	private Date stopPaymentDate;
	
	protected DstoPrintFileNotificationDto dstoPrintFileNotificationDto = new DstoPrintFileNotificationDto();
	
	@Autowired
	private IMailSender mailSender;

	@Override
	public DstoPrintFileNotificationDto initdstoprintFileNotification() {
		dstoPrintFileNotificationDto = new DstoPrintFileNotificationDto();
		
		  SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy"); 
		  String currentMonthDateYear = formatter.format(new Date());
		  String subject ="DSTO Print File_" + currentMonthDateYear + "";
		  dstoPrintFileNotificationDto.setMailSubject(subject);
		 
		return dstoPrintFileNotificationDto;
	}
	
	private void updateDstoPrintFilemailNotification()
	{
		dstoPrintFileNotificationDto.setTotalRecords(dstoPrintFileNotificationDto.getTotalRecords() + 1);
		dstoPrintFileNotificationDto.setFileDate(new Date());
		dstoPrintFileNotificationDto.setFileType("replacement file");
		dstoPrintFileNotificationDto.setMailSubject(getDSTOPrintfileMailSubject());

	}
	
	private String getDSTOPrintfileMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "DSTO Print File_" + currentMonthDateYear + "";
		return subject;
	}
	
	private String getPrintFileNotificationText(DstoPrintFileNotificationDto notificationVo) {
		if (notificationVo != null) {
			if (notificationVo.getFileDate() != null) {
				stopPaymentDate = notificationVo.getFileDate();
			} else {
				stopPaymentDate = new Date();
			}
		}
		return "File Type :: "
				+ notificationVo.getFileType()
				+ "\n"
				+ "File date :: "
				+ ConverterUtils.getFormatedYearDate(stopPaymentDate,
						"MM/dd/yyyy") + "\n" + "Total Records :: "
				+ notificationVo.getTotalRecords();
	}

	public DstoPrintFileNotificationDto notifydstoprintFiles() {
		final DstoPrintFileNotificationDto ret = dstoPrintFileNotificationDto;
		mailSender.send(MailingList.findByCode("batch.jobs.dsto"), dstoPrintFileNotificationDto.getMailSubject(), getPrintFileNotificationText(dstoPrintFileNotificationDto));
		return ret;

	}	

	/**
	 * In the method below currently using the dummy data.That needs to be
	 * replaced once the Payment object will be available with the actual data.
	 **/

	@Override
	public DstoRec generatePrintFile(Letter letter) {
		log.info("Processing DSTO Letter with MailingControl Number....."+letter.getMailingControlNo());
		updateDstoPrintFilemailNotification();
		final DstoRec dstoLetter = new DstoRec();
		
		setPrintFileConstants(letter,dstoLetter);	
		setRegistrationLines(letter, dstoLetter);

		final Letter letterobj = Letter.findLetter(letter.getId(),
		                 (String[]) null);

		 letterobj.setDstoPrintFileSentFlag(Boolean.TRUE);
		 
		 letterobj.merge();
		 		
		dstoLetter.setFromLibID(Event.getCurrentEvent().getLibraryId());
		dstoLetter.setBankRouting(Event.getCurrentEvent().getBank().getAbaNo());
		dstoLetter.setDda(Event.getCurrentEvent().getDda());
		dstoLetter.setChkNum(letter.getMailingControlNo());
		dstoLetter.setFromAccount(letter.getClaimant().getReferenceNo());		 
		dstoLetter.setExtAccountID(letter.getClaimant().getFundAccountNo());
		 if(letter.getClaimant().getTaxInfo()!=null){
			 dstoLetter.setTaxID( letter.getClaimant().getTaxInfo().getNumericSsn()) ;
		 }
		// Updates the Address
		updateAddressFromLetter(letter, dstoLetter);
		
		// Updates registration fields
		updateRegistration(letter, dstoLetter);	
		
		if (letter.isAuditable()) {
			dstoLetter.setAuditCheckFlag("A");
		}
		if (letter.isSpecialPull()) {
			dstoLetter.setGroupOrSpecHandle("SPL");
		}	
		dstoLetter.setChkDate(letter.getMailDate());
		dstoLetter.setChkDisbursementDate(letter.getMailDate());
		dstoLetter.setPrintCode(Event.getCurrentEvent().getName()) ;
		dstoLetter.setMsgOrLetterNum(letter.getLetterCode().getCode());
		dstoLetter.setBrokerNum(letter.getClaimant().getBrokerId());
		dstoLetter.setClientOrProductIndicator(Event.getCurrentEvent().getName());
		dstoLetter.setBarCode(getBarCode(letter));
		dstoLetter.setSentDate(new Date());
		dstoLetter.setSentTime(new Date());		
		
		return dstoLetter;
	}
	
	private String getBarCode(Letter letter)
	{
		return String.format("BFO3,MCON%s,MISC%s", letter.getMailingControlNo(),letter.getClaimant().getReferenceNo());
	}

	private void setRegistrationLines(Letter letter, final DstoRec dstoLetter) {
		dstoLetter.setPayeeRegistration1(letter.getClaimant().getClaimantRegistration().getRegistration1());
		dstoLetter.setPayeeRegistration2(letter.getClaimant().getClaimantRegistration().getRegistration2());
		dstoLetter.setPayeeRegistration3(letter.getClaimant().getClaimantRegistration().getRegistration3());
		dstoLetter.setPayeeRegistration4(letter.getClaimant().getClaimantRegistration().getRegistration4());
	}

	/**
	 * Updates the address from Letter
	 * @param letter
	 * @param dstoLetter
	 */
	private void updateAddressFromLetter(Letter letter, final DstoRec dstoLetter) {
		if(letter.getAddress() !=null)	        
		{
			MailObjectAddress mailAddress = letter.getAddress();
			dstoLetter.setCity(mailAddress.getCity());
			dstoLetter.setState(mailAddress.getStateCode());
			dstoLetter.setPostalCode(mailAddress.getZipCode().getZip()); 
			dstoLetter.setPostalCodeExt(mailAddress.getZipCode().getExt());
			dstoLetter.setCountry(mailAddress.getCountryCode()); 
		}
		else{
			// Get it from Cliamant as no letter Address 
			updateAddressFromClaimant(letter, dstoLetter) ;
		}
	}

	
	/**
	 * Updates address from Claimant
	 * @param letter
	 * @param dstoLetter
	 */
	private void updateAddressFromClaimant(Letter letter, final DstoRec dstoLetter) {
		if(letter.getClaimant().getMailingAddress() !=null)
	        
		{
			ClaimantAddress mailAddress = letter.getClaimant().getMailingAddress();
			dstoLetter.setCity(mailAddress.getCity());
			dstoLetter.setState(mailAddress.getStateCode());
			dstoLetter.setPostalCode(mailAddress.getZipCode().getZip()); 
			dstoLetter.setPostalCodeExt(mailAddress.getZipCode().getExt());
			dstoLetter.setCountry(mailAddress.getCountryCode()); 
		}
	}

	/**
	 * Updates Registration fields with names and Addresses
	 * @param letter
	 * @param dstoLetter
	 */
	private void updateRegistration(Letter letter,
			final DstoRec dstoLetter) {

		int totalRegistraionLinesToSet = 7;
		List<String> registrationLines1 = letter.getClaimant()
				.getClaimantRegistration().getNonEmptyRegistrationLines();
		for (int i = 0; i < registrationLines1.size(); i++) {
			setRegistrationLine(dstoLetter, registrationLines1.get(i), i);
		}
		
		int remainingLinesToSet = totalRegistraionLinesToSet
				- registrationLines1.size();
		if (remainingLinesToSet > 0) {
			List<String> registrationLines2 = letter.getClaimant().getMailingAddress().getNonEmptyAddressLines();
			for (int i = registrationLines1.size(), j = 0; j < remainingLinesToSet
					&& j < registrationLines2.size(); i++, j++) {
				setRegistrationLine(dstoLetter, registrationLines2.get(j), i);
			}
		}
	}
	
	/**
	 * Setting Constants specific to DSTO Print File.
	 */
	
	private void setPrintFileConstants(Letter letter,DstoRec dstoLetter_){
		dstoLetter_.setDstoFileType(DstoRec.DstoFileType.PRINTFILE.toString());
		dstoLetter_.setCheckForm("N");
	}
	
	private void setRegistrationLine(DstoRec dstoLetter_, String line, int i) {
		switch (i) {
		case 0:
			dstoLetter_.setReg1OrAdd1(line);
			break;
		case 1:
			dstoLetter_.setReg2OrAdd2(line);
			break;
		case 2:
			dstoLetter_.setReg3OrAdd3(line);
			break;
		case 3:
			dstoLetter_.setReg4OrAdd4(line);
			break;
		case 4:
			dstoLetter_.setReg5OrAdd5(line);
			break;
		case 5:
			dstoLetter_.setReg6OrAdd6(line);
			break;
		case 6:
			dstoLetter_.setReg7OrAdd7(line);
			break;	

		}
	}
}
