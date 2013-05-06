package com.bfds.saec.batch.out.dsto_check_file;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bfds.saec.batch.file.domain.out.dsto.DstoRec;
import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.util.ConverterUtils;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;
import com.google.common.base.Preconditions;

@Service
public class DSTOCheckFileOutputBatchServiceImpl implements
		DSTOCheckFileOutputBatchService {
	
	public static final String RUN_TYPE_P = "P";
	public static final String RUN_TYPE_T = "T";
	public static final String RUN_TYPE_DEFAULT = RUN_TYPE_P;

	final Logger log = LoggerFactory
			.getLogger(DSTOCheckFileOutputBatchServiceImpl.class);

	private Date stopPaymentDate;
	
	protected DstoCheckFileNotificationDto dstoCheckFileNotificationDto = new DstoCheckFileNotificationDto();

	@Autowired
	private IMailSender mailSender;

	@Override
	public DstoCheckFileNotificationDto initdstocheckFileNotification() {
		dstoCheckFileNotificationDto = new DstoCheckFileNotificationDto();

		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "DSTO Check File_" + currentMonthDateYear + "";
		dstoCheckFileNotificationDto.setMailSubject(subject);

		return dstoCheckFileNotificationDto;
	}

	private void updateDstoCheckFilemailNotification(Payment payment) {
		BigDecimal totalAmount = dstoCheckFileNotificationDto.getTotalAmount();
		dstoCheckFileNotificationDto
				.setTotalRecords(dstoCheckFileNotificationDto.getTotalRecords() + 1);
		totalAmount = totalAmount.add(payment.getPaymentAmount());
		dstoCheckFileNotificationDto.setTotalAmount(totalAmount);
		dstoCheckFileNotificationDto.setFileDate(new Date());
		dstoCheckFileNotificationDto.setFileType("replacement file");
		dstoCheckFileNotificationDto.setMailSubject(getDSTOCheckfileMailSubject());

	}

	private String getDSTOCheckfileMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "DSTO Check File_" + currentMonthDateYear + "";
		return subject;
	}

	@Override
	public DstoCheckFileNotificationDto notifydstoCheckFile() {
		final DstoCheckFileNotificationDto ret = dstoCheckFileNotificationDto;
		mailSender.send(MailingList.findByCode("batch.jobs.dsto"),
				dstoCheckFileNotificationDto.getMailSubject(),
				getNotificationText(dstoCheckFileNotificationDto));
		return ret;

	}

	private String getNotificationText(
			DstoCheckFileNotificationDto notificationVo) {
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
				+ notificationVo.getTotalRecords() + "\n" + "Total Amount :: $"
				+ notificationVo.getTotalAmount();
	}

	/**
	 * In the method below currently using the dummy data.That needs to be
	 * replaced once the Payment object will be available with the actual data.
	 **/

	@Override
	public DstoRec generateCheckFile(Payment payment,String runType){
		Preconditions.checkNotNull(payment.getPaymentCalc(), "PaymentCalc is null. Payment : %s", payment.getIdentificatonNo());
		Preconditions.checkArgument(!StringUtils.hasText(runType) ||  RUN_TYPE_P.equals(runType) || RUN_TYPE_T.equals(runType), 
				String.format("runType must have a value of '%s' or '%s' or null/empty", RUN_TYPE_P, RUN_TYPE_T));
		
		log.info("Processing DSTO Check#....." +payment.getIdentificatonNo());

		updateDstoCheckFilemailNotification(payment);

		final Payment dstoPayment = Payment.findPayment(payment.getId());
		dstoPayment.setDstoCheckFileSentFlag(Boolean.TRUE);

		dstoPayment.merge();

		final DstoRec dstoCheckFile = new DstoRec();

		setCheckFileConstants(payment,dstoCheckFile);
		final Event currentEvent = Event.getCurrentEvent();
		dstoCheckFile.setFromLibID(currentEvent.getLibraryId());
		dstoCheckFile.setBankRouting(currentEvent.getBank().getAbaNo());
		dstoCheckFile.setDda(currentEvent.getDda());
		dstoCheckFile.setChkNum(payment.getIdentificatonNo());
		dstoCheckFile.setFromAccount(payment.getPayTo().getReferenceNo());
		dstoCheckFile.setExtAccountID(payment.getPayTo().getFundAccountNo());
		
		if (payment.getPayTo().getTaxInfo() != null) {
			dstoCheckFile.setTaxID(payment.getPayTo().getTaxInfo().getNumericSsn());
		}
		updateAddressFromPayment(getCheckAddress(payment), dstoCheckFile);

		// Override registration addresses with registration lines if non-empty
		updateRegistrationAndAddressesLines(payment, dstoCheckFile);

		dstoCheckFile.setChkDate(payment.getPaymentDate());
		dstoCheckFile.setChkDisbursementDate(payment.getPaymentDate());
		dstoCheckFile.setPrintCode(currentEvent.getCode());
		dstoCheckFile.setMsgOrLetterNum(payment.getLetterCode() != null ? payment.getLetterCode().getCode() : null);
		dstoCheckFile.setGroupOrSpecHandle(payment.getSpecialPullCode());
		dstoCheckFile.setBrokerNum(payment.getPayTo().getBrokerId());
		dstoCheckFile.setNRAAmount1(payment.getPaymentCalc().getNraWithholding());
		dstoCheckFile.setBUWAmount1(computeBwhamount(payment, dstoCheckFile.getNRAAmount1()));
		dstoCheckFile.setGrossAmount1(payment.getPaymentCalc().getGrossAmount());
		dstoCheckFile.setNetAmount1(payment.getPaymentCalc().getNettAmount());
        dstoCheckFile.setTotProceeds(payment.getPaymentCalc().getNettAmount());
        dstoCheckFile.setTotGross(payment.getPaymentCalc().getGrossAmount());
        dstoCheckFile.setTotTaxes(payment.getPaymentCalc().getBackupWithholding());
		if (payment.isAuditable()) {
			dstoCheckFile.setAuditCheckFlag("A");
		}

		setRegistrationLines(payment, dstoCheckFile);
			
		dstoCheckFile.setRunType(StringUtils.hasText(runType) ? runType : RUN_TYPE_DEFAULT);
		
		dstoCheckFile.setClientOrProductIndicator(currentEvent.getCode());
		dstoCheckFile.setSentDate(new Date());
		dstoCheckFile.setSentTime(new Date());

		setSmallCheckConfigData(dstoCheckFile,payment,currentEvent);
		return dstoCheckFile;
	}

	private void setRegistrationLines(Payment payment,final DstoRec dstoCheckFile) {
		
		dstoCheckFile.setPayeeRegistration1(payment.getPayTo().getClaimantRegistration().getRegistration1());
		dstoCheckFile.setPayeeRegistration2(payment.getPayTo().getClaimantRegistration().getRegistration2());
		dstoCheckFile.setPayeeRegistration3(payment.getPayTo().getClaimantRegistration().getRegistration3());
		dstoCheckFile.setPayeeRegistration4(payment.getPayTo().getClaimantRegistration().getRegistration4());
	}


	/**
	 * Computes GrossAmount for Payment amounts.
	 */
	private BigDecimal computeBwhamount(Payment payment,BigDecimal nraWithHolding)
	{

		BigDecimal stateWithholding = payment.getPaymentCalc().getStateWithholding();
		BigDecimal fedWithholding = payment.getPaymentCalc().getFedWithholding();
		BigDecimal intFedWithholding = payment.getPaymentCalc().getIntFedWithholding();
		BigDecimal intStateWithholding = payment.getPaymentCalc().getIntStateWithholding();
		BigDecimal nraWithholding_ = nraWithHolding;

		return add(stateWithholding, fedWithholding, nraWithholding_,intFedWithholding, intStateWithholding);
	}

	private BigDecimal add(BigDecimal... vals) {
		BigDecimal ret = new BigDecimal(0);
		for (BigDecimal val : vals) {
			if (val != null) {
				ret = ret.add(val);
			}
		}
		return ret.equals(new BigDecimal(0)) ? null : ret;
	}
	
	/**
	 * Setting Constants specific to DSTO Check File.
	 */
	
	private void setCheckFileConstants(Payment payment, final DstoRec dstoCheck_) {
		dstoCheck_.setCheckForm("Y");
		dstoCheck_.setDstoFileType(DstoRec.DstoFileType.CHECKFILE.toString());
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
	 * @param dstoCheckFile
	 */
	private void updateRegistrationAndAddressesLines(Payment payment, final DstoRec dstoCheckFile) {
 
		List<String> registrationLines = buildRegistrationLines(getCheckAddress(payment), payment.getPayTo().getClaimantRegistration());
		
        int index = 0;
        for(String line : registrationLines) {
            if(index > 6) { return;}
            setRegistrationLine(dstoCheckFile, line, index++);
        }        
	}
	
	/**
	 * 
	 * @param address
	 * @param claimantRegistration
	 * @return
	 * 
	 * TODO: Move to an appropriate location - Util or Domain class.
	 */
	public static List<String> buildRegistrationLines(Address address, ClaimantRegistration claimantRegistration) {
		Preconditions.checkArgument(address != null, "address is null");
		Preconditions.checkArgument(claimantRegistration != null, "claimantRegistration is null");
		final List<String> registrationLines = claimantRegistration.getNonEmptyRegistrationLines();				
		if(registrationLines.size() > 4) {
			registrationLines.addAll(4, address.getNonEmptyAddressLines());
		} else {
			registrationLines.addAll(address.getNonEmptyAddressLines());
		}
		
		int index = registrationLines.size();
		//Refer : https://bostonfinancial.atlassian.net/browse/PIA-218
		if(index <= 6 && !address.isUSAddress()) {
			String cityStateZip = DSTOCheckFileOutputBatchServiceImpl.getCityStateZip(address);
			registrationLines.add(cityStateZip);						
			if(++index  <= 6) {
				registrationLines.add(address.getCountryCode());								
			}
		}
		return registrationLines;
	}
	

    /**
     * Constructs a string containing the city, state, zip and zip ext with the following rules.
     *
     * 1. Ignores nulls
     * 2. Uses space as the delimiter
     * 3. If zip extension is present uses "-" as the seperator between zip and ext.
     *
     * @param address
     * @return A string containing the city, state, zip and zip ext.
     * 
     * TODO: Move to an appropriate location - Util or Domain class.
     */
    public static String getCityStateZip(Address address) {
      StringBuilder sb = new StringBuilder();
        if(StringUtils.hasText(address.getCity())) {
            sb.append(address.getCity()).append(" ");
        }
        if(StringUtils.hasText(address.getStateCode())) {
            sb.append(address.getStateCode()).append(" ");
        }
        if(address.getZipCode() != null) {
            sb.append(address.getZipCode().toString());
        }
        return sb.toString().trim();
    }

    private static Address getCheckAddress(final Payment payment) {
        if(payment.getCheckAddress() != null) {
            return payment.getCheckAddress().getAddress();
        }
        return payment.getPayTo().getMailingAddress().getAddress();
    }

	/**
	 * Set the address fields.
	 * 
	 * @param address
	 * @param dstoCheckFile
	 */
	private void updateAddressFromPayment(Address address,
			final DstoRec dstoCheckFile) {
        dstoCheckFile.setCity(address.getCity());
        dstoCheckFile.setState(address.getStateCode());
        dstoCheckFile.setPostalCode(address.getZipCode().getZip());
        dstoCheckFile.setPostalCodeExt(address.getZipCode().getExt());
        // A US/USA address must always be set as USA. Refer : https://bostonfinancial.atlassian.net/browse/PIA-218
        dstoCheckFile.setCountry(address.isUSAddress() ? "USA" : address.getCountryCode());
	}

	public DstoCheckFileNotificationDto getDstoCheckFileNotificationDto() {
		return dstoCheckFileNotificationDto;
	}

	public void setDstoCheckFileNotificationDto(
			DstoCheckFileNotificationDto dstoCheckFileNotificationDto) {
		this.dstoCheckFileNotificationDto = dstoCheckFileNotificationDto;
	}

	private void setRegistrationLine(DstoRec dstoCheckFile, String line, int i) {
		switch (i) {
		case 0:
			dstoCheckFile.setReg1OrAdd1(line);
			break;
		case 1:
			dstoCheckFile.setReg2OrAdd2(line);
			break;
		case 2:
			dstoCheckFile.setReg3OrAdd3(line);
			break;
		case 3:
			dstoCheckFile.setReg4OrAdd4(line);
			break;
		case 4:
			dstoCheckFile.setReg5OrAdd5(line);
			break;
		case 5:
			dstoCheckFile.setReg6OrAdd6(line);
			break;
		case 6:
			dstoCheckFile.setReg7OrAdd7(line);
			break;

		}
	}
	
	
	private void setSmallCheckConfigData(DstoRec dstoCheckFile,Payment payment,Event event)
	{
		 if(event.getSmallCheckConfig()!=null)
		 {
			 dstoCheckFile.setEventNameAddress1(event.getSmallCheckConfig().getEventNameAddress1());
			 dstoCheckFile.setEventNameAddress2(event.getSmallCheckConfig().getEventNameAddress2());
			 dstoCheckFile.setEventNameAddress3(event.getSmallCheckConfig().getEventNameAddress3());
			 dstoCheckFile.setEventNameAddress4(event.getSmallCheckConfig().getEventNameAddress4());
			 dstoCheckFile.setEventNameAddress5(event.getSmallCheckConfig().getEventNameAddress5());
			 dstoCheckFile.setEventNameAddress6(event.getSmallCheckConfig().getEventNameAddress6());
			 dstoCheckFile.setVariableVerbiage(event.getSmallCheckConfig().getVariableVerbiage());
			 dstoCheckFile.setBankInfo1(event.getSmallCheckConfig().getBankInfo1());
			 dstoCheckFile.setBankInfo2(event.getSmallCheckConfig().getBankInfo2());
			 dstoCheckFile.setBankInfo3(event.getSmallCheckConfig().getBankInfo3());
			 dstoCheckFile.setDistributionText(event.getSmallCheckConfig().getDistributionText());
			 dstoCheckFile.setAbaTop(event.getSmallCheckConfig().getAbaTop());
			 dstoCheckFile.setAbaBottom(event.getSmallCheckConfig().getAbaBottom());
			 
		 }
		 dstoCheckFile.setBarCode(getBarCode(payment));
	}
	
	private String getBarCode(Payment payment)
	{
		return "BFO3,"+"CKNM"+payment.getIdentificatonNo()+","+"AMTV"+getAmountAsString(payment.getPaymentCalc().getNettAmount())+","+"MISC"+payment.getPayTo().getReferenceNo();
	}
	
	private String getAmountAsString(BigDecimal amount)
	{
		return ConverterUtils.getFormattedString(amount, 5, " ", Boolean.TRUE);
	}
	

}
