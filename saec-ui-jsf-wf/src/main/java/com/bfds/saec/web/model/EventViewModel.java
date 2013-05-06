package com.bfds.saec.web.model;

import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.primefaces.component.dialog.Dialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;

import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.EventPaymentConfig;
import com.bfds.saec.domain.FileNotificationConfig;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.ReportCode;
import com.bfds.saec.domain.SmallCheckConfig;
import com.bfds.saec.domain.TaxTypeLov;
import com.bfds.saec.domain.reference.BankLov;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.Lov;
import com.bfds.saec.domain.reference.ReportCategory;
import com.bfds.saec.web.util.JsfUtils;
import com.google.common.collect.Lists;



public class EventViewModel  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	final Logger log = LoggerFactory.getLogger(EventViewModel.class);
	
	private Event event;
	
	//public EventViewModel() {
		
	//}
	
	private List<EventPaymentConfig> paymentConfigList;
	
	private List<FileNotificationConfig> fileNotificationConfigList;
	
	private List<LetterCode> letterCodeList;
	
	private List<ReportCode> reportCodeList;
	
	private static List<SelectItem> taxTypePrimaryList  ;
	
	private static List<SelectItem> bankList  ;
	
	private List<SelectItem> taxTypeSecondaryList ;
		
	private List<Lov> lovList;
	
	private boolean includeAllReportsOnPage;
	
	private boolean includeAllLettersOnPage;
	
	private String newLetterCode ;
	
	private String newLetterDescription;
	
	@Enumerated(EnumType.STRING)
	private LetterType newLetterType ;
	
	private String newReportCode ;
	
	private String newReportDescription ;
	
	@Enumerated(EnumType.STRING)
	private ReportCategory newReportCategory ;
	
	public Event loadEvent() {
		loadPrimaryTaxTypes() ; 
		loadBanks() ;
		loadAllLetterCodes() ;
		Event ret =  Event.getCurrentEvent();
		if(ret.getSmallCheckConfig() == null) {
			ret.setSmallCheckConfig(new SmallCheckConfig());
		}
		return ret;		
	}
	
	public void saveEvent(final Event eventt, final MessageContext messageContext) {
	
		/* SmallCheckConfig is a secondary table inside the Event, so every time if a Event object merges then it is trying to insert a
	    new row  inside smallcheckconfig table,because we are creating a new object for smallCheckConfig in loadEvent() method while initial loading time.*/  
	
		if(eventt.getSmallCheckConfig()!=null && eventt.getSmallCheckConfig().isEmpty())
		{
			eventt.setSmallCheckConfig(null);
		}
		this.event = eventt ;
		eventt.merge();
	}	
	
	public void saveEventLetterCodes(final MessageContext messageContext) {
		Iterator iter = this.letterCodeList.iterator() ;
		while (iter.hasNext())
		{
			LetterCode lc = (LetterCode) iter.next() ;
			lc.merge() ;
		}
	}	
	
	public void saveEventReportCodes(final MessageContext messageContext) {
		Iterator iter = this.reportCodeList.iterator() ;
		while (iter.hasNext())
		{
			ReportCode lc = (ReportCode) iter.next() ;
			lc.merge() ;
		}
	}	
	
	public void saveEvent() {
		this.event.merge();
	}	
	
	public void savePaymentConfigList() {
		
		Iterator<EventPaymentConfig> it = this.paymentConfigList.iterator() ;
		while (it.hasNext()) {
			EventPaymentConfig pm = (EventPaymentConfig) it.next() ;
			if(pm.getEvent().getSmallCheckConfig()!=null && pm.getEvent().getSmallCheckConfig().isEmpty())
			{
				pm.getEvent().setSmallCheckConfig(null);
			}
			pm.merge() ;
		}
	}	
	
	public void saveFileNotificationConfigList() {
		Iterator<FileNotificationConfig> it = this.fileNotificationConfigList.iterator() ;
		while (it.hasNext()) {
			FileNotificationConfig pm = (FileNotificationConfig) it.next() ;
			if(pm.getEvent().getSmallCheckConfig()!=null && pm.getEvent().getSmallCheckConfig().isEmpty())
			{
				pm.getEvent().setSmallCheckConfig(null);
			}
			pm.merge() ;
		}
	}	
	
	// @Transactional
	public List<EventPaymentConfig> getEventPaymentConfigList(Event eventt) {
		List<EventPaymentConfig> ret = Lists.newArrayList();
		eventt=validateSmallCheckConfig(eventt);
		eventt = eventt.merge() ;
		ret.addAll(eventt.getPaymentConfigs());
		return paymentConfigList = ret;
	}

	/**
	 * @param eventt
	 * this method id used to restrict the duplicate key insertion on the child table i.e SmallCheckConfig whenever we merged the parent table Event.
	 */
	private Event validateSmallCheckConfig(Event eventt) {
		if(eventt.getSmallCheckConfig()!=null && eventt.getSmallCheckConfig().isEmpty())
		{
			eventt.setSmallCheckConfig(null);
		}
		return eventt;
	}

	public List<FileNotificationConfig> getEventFileNotificationConfigList(Event eventt) {
		List<FileNotificationConfig> ret = Lists.newArrayList();
		eventt=validateSmallCheckConfig(eventt);
		eventt = eventt.merge() ;
		ret.addAll(eventt.getFileNotificationConfigs()) ;
		return fileNotificationConfigList = ret;
	}

	public List<EventPaymentConfig> getPaymentConfigList() {
		return paymentConfigList;
	}

	public void setPaymentConfigList(List<EventPaymentConfig> paymentConfigList) {
		this.paymentConfigList = paymentConfigList;
	}

	public List<FileNotificationConfig> getFileNotificationConfigList() {
		return fileNotificationConfigList;
	}

	public void setFileNotificationConfigList(
			List<FileNotificationConfig> fileNotificationConfigList) {
		this.fileNotificationConfigList = fileNotificationConfigList;
	}
	
	public void loadPrimaryTaxTypes() {
		
		if (taxTypePrimaryList != null && taxTypePrimaryList.size() > 0)
			return ;
		
		List ttsecList = TaxTypeLov.findDistinctTaxTypeCategories() ;
		if (ttsecList != null && ttsecList.size() > 0) {
			Iterator iter = ttsecList.iterator() ;
			taxTypePrimaryList = Lists.newArrayList();
					
			while (iter.hasNext())
			{
				String taxTypePrimary = (String) iter.next() ;
				taxTypePrimaryList.add(new SelectItem(taxTypePrimary, taxTypePrimary));
			}
			if(log.isDebugEnabled())
				log.debug(String.format("Primary TaxTypes found in database : %s",taxTypePrimaryList.size()));			
		}
		else
			log.error("No Primary TaxTypes found in database for Event : %s ", Event.getCurrentEventDescription() ) ;
	}	
	
	public void loadAllLetterCodes() {
		
		List<LetterCode> lcList = LetterCode.findAllLetterCodes() ;
		if (lcList != null && lcList.size() > 0) {
			Iterator iter = lcList.iterator() ;
			letterCodeList = Lists.newArrayList();
					
			while (iter.hasNext())
			{
				LetterCode lc = (LetterCode) iter.next() ;
				letterCodeList.add(lc);
			}
			if(log.isDebugEnabled())
				log.debug(String.format("Letter Codes found in database : %s",letterCodeList.size()));
		}
		else
			log.error("No Letter Codes found in database" ) ;
	}	
	
	public void loadAllReportCodes() {
		
		List<ReportCode> lcList = ReportCode.findAllReportCodes() ;
		if (lcList != null && lcList.size() > 0) {
			Iterator iter = lcList.iterator() ;
			reportCodeList = Lists.newArrayList();
					
			while (iter.hasNext())
			{
				ReportCode lc = (ReportCode) iter.next() ;
				reportCodeList.add(lc);
			}
			if(log.isDebugEnabled())
				log.debug(String.format(" BankLovs found in database : %s",reportCodeList.size()));
		}
		else
			log.error("No Report Codes found in database" ) ;
	}	
	
	public void loadBanks() {
		
		if (bankList != null && bankList.size() > 0)
			return ;
		
		List<BankLov> blList = BankLov.findAllBankLovs() ;
		if (blList != null && blList.size() > 0) {
			Iterator iter = blList.iterator() ;
			bankList = Lists.newArrayList();
					
			while (iter.hasNext())
			{
				BankLov bankLov = (BankLov) iter.next() ;
				bankList.add(new SelectItem(bankLov.getCode(), bankLov.getDescription()));
			}
			if(log.isDebugEnabled())
				log.debug(String.format(" BankLovs found in database : %s",bankList.size()));
		}
		else
			log.error("No BankLovs found in database" ) ;
	}	
	
	public void onChangeTaxType(String selectedTaxValue) {
		List<TaxTypeLov> ttsecList = TaxTypeLov.findTaxTypesByCategory(selectedTaxValue) ;
		if (ttsecList != null && ttsecList.size() > 0) {
			Iterator iter = ttsecList.iterator() ;
			taxTypeSecondaryList = Lists.newArrayList();
					
			while (iter.hasNext())
			{
				TaxTypeLov lov = (TaxTypeLov) iter.next() ;
				taxTypeSecondaryList.add(new SelectItem(lov.getCode(), lov.getDescription()));
			}
			if(log.isDebugEnabled())
				log.debug(String.format("Secondary TaxTypes found in database : %s",taxTypeSecondaryList.size()));
		}
		else
			log.error("No Secondary TaxTypes found for primary : " + selectedTaxValue) ;
	}	
	
	/*
	public EventViewModel(Event eventt) {
		this.event = eventt;
	}
	*/
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<Lov> getLovList() {
		return lovList;
	}

	public void setLovList(List<Lov> argLovList) {
		lovList = argLovList;
	}

	public List<SelectItem> getTaxTypeSecondaryList() {
		return taxTypeSecondaryList;
	}

	public void setTaxTypeSecondaryList2(List<SelectItem> taxTypeSecondaryList) {
		this.taxTypeSecondaryList = taxTypeSecondaryList;
	}

	public List<SelectItem> getTaxTypePrimaryList() {
		return taxTypePrimaryList;
	}

	public void setTaxTypePrimaryList(List<SelectItem> taxTypePrimaryList) {
		this.taxTypePrimaryList = taxTypePrimaryList;
	}

	public List<SelectItem> getBankList() {
		return bankList;
	}

	public void setBankList(List<SelectItem> bankList) {
		EventViewModel.bankList = bankList;
	}

	public List<LetterCode> getLetterCodeList() {
		return letterCodeList;
	}

	public void setLetterCodeList(List<LetterCode> letterCodeList) {
		this.letterCodeList = letterCodeList;
	}

	public List<ReportCode> getReportCodeList() {
		return reportCodeList;
	}

	public void setReportCodeList(List<ReportCode> reportCodeList) {
		this.reportCodeList = reportCodeList;
	}

	public boolean isIncludeAllReportsOnPage() {
		return includeAllReportsOnPage;
	}

	public void setIncludeAllReportsOnPage(boolean includeAllReportsOnPage) {
		this.includeAllReportsOnPage = includeAllReportsOnPage;
	}

	public void toggleReportsSelection() {
		toggleReportsSelection(!this.isIncludeAllReportsOnPage());
	}
	
	public void toggleReportsSelection(boolean flag) {
		for(final ReportCode reportCode : reportCodeList) {
			reportCode.setActiveForEvent(flag) ;
		}
	}
	
	public void selectReportForEvent(final String argCode) {
		final List<ReportCode> currentReportList = (List<ReportCode>) this.getReportCodeList();
		for(final ReportCode reportCode : currentReportList) {
			if (reportCode.getCode().equals(argCode)) {
				reportCode.setActiveForEvent(true) ;
			}
		}
	}

	public boolean isIncludeAllLettersOnPage() {
		return includeAllLettersOnPage;
	}

	public void setIncludeAllLettersOnPage(boolean includeAllLettersOnPage) {
		this.includeAllLettersOnPage = includeAllLettersOnPage;
	}
	
	public void toggleLettersSelection() {
		toggleLettersSelection(!this.isIncludeAllLettersOnPage());
	}
	
	public void toggleLettersSelection(boolean flag) {
		for(final LetterCode letterCode : letterCodeList) {
			letterCode.setActiveForEvent(flag) ;
		}
	}
	
	public void selectLetterForEvent(final String argCode) {
		final List<LetterCode> currentLetterList = (List<LetterCode>) this.getLetterCodeList();
		for(final LetterCode letterCode : currentLetterList) {
			if (letterCode.getCode().equals(argCode)) {
				letterCode.setActiveForEvent(true) ;
			}
		}
	}
	
	public boolean addNewLetter(final MessageContext messageContext) {
		setNewLetterVisibility(true) ;
		return true ;
	}
	
	public boolean saveNewLetter(final MessageContext messageContext) {
		LetterCode lcExist = LetterCode.findByCode(newLetterCode) ;
		if (lcExist == null)
		{
			saveNewLetter() ;
			messageContext.addMessage(new MessageBuilder()
			.info()
			.defaultText(
					"Letter Code added: "
							+ this.newLetterCode).build()) ;
		}
		else
		{
			messageContext.addMessage(new MessageBuilder()
			.info().source("form:eventLetterAddDialog")
			.defaultText(
					"Letter Code already exist: "
							+ this.newLetterCode).build()) ;
		}
		return true ;		
	}
	
	public boolean saveNewLetter() {

		LetterCode lc = new LetterCode(newLetterCode, newLetterDescription, newLetterType) ;
		lc.persist() ;
		loadAllLetterCodes() ;
		setNewLetterVisibility(false) ;
		return true ;
	}
	
	private void setNewLetterVisibility(final boolean visible) {
		final Dialog letterDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "eventLetterAddDialog");
		letterDialog.setVisible(visible);
	}

	////////////////////////////////////
	
	public boolean addNewReport(final MessageContext messageContext) {
		setNewReportVisibility(true) ;
		return true ;
	}
	
	public boolean saveNewReport(final MessageContext messageContext) {
		ReportCode lcExist = ReportCode.findByCode(newReportCode) ;
		if (lcExist == null)
		{
			saveNewReport() ;
			messageContext.addMessage(new MessageBuilder()
			.info()
			.defaultText(
					"Report Code added: "
							+ this.newReportCode).build()) ;
		}
		else
		{
			messageContext.addMessage(new MessageBuilder()
			.info()
			.defaultText(
					"Report Code already exist: "
							+ this.newReportCode).build()) ;
		}
		return true ;		
	}
	
	public boolean saveNewReport() {

		ReportCode lc = new ReportCode(newReportCode, newReportDescription, newReportCategory) ;
		lc.persist() ;
		loadAllReportCodes() ;
		setNewReportVisibility(false) ;
		return true ;
	}
	
	private void setNewReportVisibility(final boolean visible) {
		final Dialog reportDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "eventReportAddDialog");
		reportDialog.setVisible(visible);
	}

	public boolean saveSmallCheckConfig(final Event event, final MessageContext messageContext){
		if(event.getSmallCheckConfig()!=null && event.getSmallCheckConfig().isEmpty()){
			event.setSmallCheckConfig(null);
		}
		event.merge();
		return true;
	}
	
	public String getNewLetterCode() {
		return newLetterCode;
	}

	public void setNewLetterCode(String newLetterCode) {
		this.newLetterCode = newLetterCode;
	}

	public String getNewLetterDescription() {
		return newLetterDescription;
	}

	public void setNewLetterDescription(String newLetterDescription) {
		this.newLetterDescription = newLetterDescription;
	}

	public LetterType getNewLetterType() {
		return newLetterType;
	}

	public void setNewLetterType(LetterType newLetterType) {
		this.newLetterType = newLetterType;
	}

	public String getNewReportCode() {
		return newReportCode;
	}

	public void setNewReportCode(String newReportCode) {
		this.newReportCode = newReportCode;
	}

	public String getNewReportDescription() {
		return newReportDescription;
	}

	public void setNewReportDescription(String newReportDescription) {
		this.newReportDescription = newReportDescription;
	}

	public ReportCategory getNewReportCategory() {
		return newReportCategory;
	}

	public void setNewReportCategory(ReportCategory newReportCategory) {
		this.newReportCategory = newReportCategory;
	}
}
