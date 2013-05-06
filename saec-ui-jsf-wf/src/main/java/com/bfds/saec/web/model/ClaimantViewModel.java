package com.bfds.saec.web.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.dao.EntityAuditDao;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.ClaimantTaxInfo;
import com.bfds.saec.domain.Contact;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.Name;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.audit.EntityWithAuditVo;
import com.bfds.saec.domain.audit.RevisionInfo;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.CountryLov;
import com.bfds.saec.domain.util.AccountContext;
import com.bfds.saec.util.CollectionUtils;
import com.bfds.saec.util.SaecDateUtils;
import com.bfds.saec.web.service.ClaimantService;
import com.bfds.saec.web.util.JsfUtils;
import com.bfds.wss.domain.ClaimActivity;
import com.google.common.collect.Lists;

public class ClaimantViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	final static Logger log = LoggerFactory.getLogger(ClaimantViewModel.class);

	private Claimant claimant;

	private List<EntityWithAuditVo<ClaimantAddress>> aorList;

	private List<EntityWithAuditVo<ClaimantAddress>> seasonalList;

	private AddressType mailingAddressType;

	private transient Payment selectedCheck;

	private boolean expandFlag;

	private boolean collapseFlag;

	private boolean requiredField;
	
	private static List<SelectItem> countryList  ;
	
	public ClaimantViewModel() {

	}

	public ClaimantViewModel(Claimant claimant) {
		this.claimant = claimant;
		// Create the embedded JPA instances to avoid a nullpionter in JSF.
		if (claimant.getClaimantRegistration() == null) {
			claimant.setClaimantRegistration(new ClaimantRegistration());
		}
		if(claimant.getName() == null ){
			claimant.setName(new Name());
		}
		if (claimant.getPrimaryContact() == null) {
			claimant.setPrimaryContact(new Contact());
		}
		if (claimant.getTaxInfo() == null) {
			claimant.setTaxInfo(new ClaimantTaxInfo());
		}
		
	}

	public boolean isExpandFlag() {
		return expandFlag;
	}

	public void setExpandFlag(boolean expandFlag) {
		this.expandFlag = expandFlag;
	}

	public boolean isCollapseFlag() {
		return collapseFlag;
	}

	public void setCollapseFlag(boolean collapseFlag) {
		this.collapseFlag = collapseFlag;
	}

	public boolean expand() {
		setExpandFlag(true);
		setCollapseFlag(false);
		return true;
	}

	public boolean collapse() {
		setExpandFlag(false);
		setCollapseFlag(true);
		return true;
	}

	/*
	 * One of SSN, EIN or TIN. This field is used when creating a new account
	 * and alternate payee.
	 */
	private String identificationType;

	public Long getPrimaryKey() {
		return this.claimant.getId();
	}

	public String getRegistrationText() {
		return claimant.getClaimantRegistration().getRegistrationLinesAsString(
				"<br/>");
	}

	public String getOrganizationOrIndividual() {
		if (this.getClaimant().getOrganization() != null) {
			return Boolean.TRUE.equals(this.getClaimant().getOrganization()) ? "O"
					: "I";
		}
		return null;

	}

	public boolean create(Contact contact, MessageContext messageContext,
			ClaimantService claimantService) {

		claimant.setPrimaryContact(contact);
		claimant.setCreatedBy(AccountContext.getCurrentUsername());
		claimant.setCreatedDate(new Date());
		claimant.persist();
		messageContext.addMessage(new MessageBuilder()
				.info()
				.defaultText(
						"A new account has been created with the reference #"
								+ this.getClaimant().getReferenceNo()).build());
		return true;
	}

	public boolean saveAddress(final ClaimantService claimantService,
			MessageContext context) {
	
		updateMailingAddress();

		this.claimant = this.getClaimant().merge();
		/*
		 * If mailingAddress is 'seasonal Address' and leaving it as blank then we are inserting the blank record in to DB
		 * As per our understanding the Empty records my impact the system.    
		 */
		if (isAddressEmpty() && mailingAddressType == AddressType.ADDRESS_OF_RECORD ) {
			claimant.removeAddress(claimant.getSeasonalAddress());
		}
		this.getClaimant().merge();
		
		getAddressVersions(claimantService);
		//Create the embedded JPA instances to avoid a NPE in JSF.
		if(claimant.getSeasonalAddress() == null){
			getClaimant().setSeasonalAddress(new ClaimantAddress());
		}
//		this.claimant.getMailingAddress().updateMailObjectAddress();
		if(log.isInfoEnabled())
			log.info(String.format("Address saved for Claimant Id: %s",this.getClaimant().getId()));		
		return true;

	}

	/*
	 * Added method to avoid the blank record insertion in seasonalList, without updating any field 
	 * Refer #742
	 */
	private boolean isAddressEmpty() {
		return claimant.getSeasonalAddress().isEmpty() && claimant.getSeasonalAddress().getCountryCode() != null;
	}

	
	public String getAddressText() {
		ClaimantAddress address = this.getClaimant().getMailingAddress();
		if (address != null) {
			return address.getAddressAsString("<br/>");
		}
		return null;
	}

	public void saveRegistration(final ClaimantService claimantService) {
		this.claimant = this.getClaimant().merge();
	}

	public void saveGam(final ClaimantService claimantService) {
		this.claimant = this.getClaimant().merge();
	}
	public void saveTax(final ClaimantService claimantService) {
		if ("SSN".equals(identificationType)) {
			this.claimant.setEin(null);
			this.claimant.setTin(null);
		} else if ("EIN".equals(identificationType)) {
			this.claimant.setSsn(null);
			this.claimant.setTin(null);
		} else if ("TIN".equals(identificationType)) {
			this.claimant.setSsn(null);
			this.claimant.setEin(null);
		}
		this.claimant = this.getClaimant().merge();
		if(log.isInfoEnabled())
			log.info(String.format("%s is saved for Claimant : %s",identificationType,this.getClaimant().getReferenceNo()));			
	}

	public void reloadClaimant() {
		this.claimant = Claimant.findClaimant(this.getClaimant().getId(),
				Claimant.ASSOCIATION_ALL);
	}

	private void updateMailingAddress() {
		if (mailingAddressType != null) {
			this.getClaimant().setMailingAddressByType(mailingAddressType);
		}
		if(log.isInfoEnabled())
			log.info(String.format("%s Address Updated.",mailingAddressType.toString()));		
	}

	public RevisionInfo loadClaimantRegistrationRevisionInfo(EntityAuditDao dao) {
		return dao.getLastRevisionInfo(ClaimantRegistration.class, this
				.getClaimant().getClaimantRegistration().getId());
	}

	public boolean prepareSeasonalAddress() {
		if (getClaimant().getSeasonalAddress() == null) {
			getClaimant().setSeasonalAddress(new ClaimantAddress());
		}
		if(log.isInfoEnabled())
			log.info(String.format("Seasonal Address Created."));		
		return true;
	}

	public void getAddressVersions(ClaimantService claimantService) {
		aorList = loadAddressVersionList(claimantService, getClaimant()
				.getAddressOfRecord());
		seasonalList = loadAddressVersionList(claimantService, getClaimant()
				.getSeasonalAddress());
	}
	

	List<EntityWithAuditVo<ClaimantAddress>> loadAddressVersionList(
			final ClaimantService claimantService, ClaimantAddress addr) {
		List<EntityWithAuditVo<ClaimantAddress>> ret = new ArrayList<EntityWithAuditVo<ClaimantAddress>>();
		if (addr != null && addr.getId() != null) {
			ret.addAll(claimantService.getAddressVersionList(addr.getId()));
			Collections.sort(ret, new EntityWithAuditVoComparator());
		}
		return ret;
	}

	public List<EntityWithAuditVo<ClaimantAddress>> getAorList() {
		return aorList;
	}

	public List<EntityWithAuditVo<ClaimantAddress>> getSeasonalList() {
		return seasonalList;
	}

	public Claimant getClaimant() {
		return claimant;
	}

	public void setClaimant(Claimant claimant) {
		this.claimant = claimant;
	}

	public AddressType getMailingAddressType() {
		if (this.mailingAddressType == null) {
			if (this.getClaimant().getMailingAddress() != null) {
				this.mailingAddressType = this.getClaimant()
						.getMailingAddress().getAddressType();
			}
		}
		return this.mailingAddressType = this.mailingAddressType == null ? AddressType.ADDRESS_OF_RECORD
				: this.mailingAddressType;
	}

	public void setMailingAddressType(AddressType mailingAddressType) {
		this.mailingAddressType = mailingAddressType;
	}

	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}

	public String getIdentificationType() {
		if (StringUtils.hasText(this.identificationType)) {
			return identificationType;
		} else if (StringUtils.hasText(this.getClaimant().getSsn())) {
			return "SSN";
		} else if (StringUtils.hasText(this.getClaimant().getTin())) {
			return "TIN";
		} else if (StringUtils.hasText(this.getClaimant().getEin())) {
			return "EIN";
		}
		return "SSN";
	}

	public String getIdentificationString() {
		if (StringUtils.hasText(this.getClaimant().getSsn())) {
			return this.getClaimant().getSsn();
		} else if (StringUtils.hasText(this.getClaimant().getTin())) {
			return this.getClaimant().getTin();
		} else if (StringUtils.hasText(this.getClaimant().getEin())) {
			return this.getClaimant().getEin();
		}
		return null;
	}

	public String getIdentification1String() {
		if (this.getClaimant().getPrimaryContact() != null) {
			return this.getClaimant().getPrimaryContact().getPhoneNo();
		}

		return null;
	}

	public String getIdentification2String() {

		if (this.getClaimant().getPrimaryContact() != null) {
			return this.getClaimant().getPrimaryContact().getWorkPhoneNo();
		}

		return null;
	}

	public String getIdentification3String() {

		if (this.getClaimant().getPrimaryContact() != null) {
			return this.getClaimant().getPrimaryContact().getCellPhoneNo();
		}

		return null;
	}

	public AddressType getPrimaryAddressType() {
		return AddressType.ADDRESS_OF_RECORD;
	}

	public AddressType getSeasonalAddressType() {
		return AddressType.SEASONAL_ADDRESS;
	}

	public List<PhoneCall> getRecentCalls() {
		return sortPhoneCallsByDate(CollectionUtils.firstN(getAllCalls(), 5));
	}

	private List<PhoneCall> sortPhoneCallsByDate(List<PhoneCall> calls) {
		Collections.sort(calls, new Claimant.ActivityComparator());
		return calls;
	}

	public int getPhoneCallCount() {
		return this.getClaimant().getAllCalls().size();
	}

	public void showAllCallLogs(final ActionEvent actionEvent) {
		DataTable callLogTable = (DataTable) JsfUtils.findComponent(
				JsfUtils.getUIViewRoot(), "calLog");
		callLogTable.setValue(this.getAllCalls());
		CommandButton button = (CommandButton) JsfUtils.findComponent(
				JsfUtils.getUIViewRoot(), "callLogsDisplayButton");
		button.setDisabled(true);
	}

	public List<PhoneCall> getAllCalls() {
		return sortPhoneCallsByDate(this.getClaimant().getAllCalls());
	}

	public int getPaymentCount() {
		return this.getClaimant().getPayments().size();
	}

	public int getReIssuePaymentCount() {
		return this.getClaimant().getReissuablePayments().size();
	}

	public Long getMailCount() {
		return claimant.getMailCount();
	}

	public List<Letter> getAllMails() {
		List<Letter> mails = new ArrayList<Letter>();
		mails.addAll(claimant.getMails());
		Collections.sort(mails, new MailsComparator());
		return mails;
	}

	@Transactional
	public List<Payment> getAllPayments() {
		List<Payment> payments = new ArrayList<Payment>();
		payments.addAll(this.getClaimant().getPayments());
		Collections.sort(payments, new PaymentComparator());
		return payments;
	}
	
	@Transactional
	public List<ClaimActivity> getAllClaims() {
		List<ClaimActivity> claims = new ArrayList<ClaimActivity>();
		if(this.getClaimant().getSingleClaimantClaim()!=null)
		{
		claims.addAll(this.getClaimant().getSingleClaimantClaim().getClaimActivities());
		Collections.sort(claims, new ClaimsComparator());
		}
		return claims;
	}

	public List<Payment> getReissuablePayments() {
		List<Payment> payments = new ArrayList<Payment>();
		for (Payment c : this.getClaimant().getReissuablePayments()) {
			payments.add(c);
		}
		Collections.sort(payments, new PaymentComparator());
		return payments;
	}

	public List<Activity> getRecentActivity() {
		return sortActivityByDate(CollectionUtils.firstN(this.getClaimant()
				.getAllActivity(), 5));
	}

	private List<Activity> sortActivityByDate(List<Activity> calls) {
		Collections.sort(calls, new Claimant.ActivityComparator());
		return calls;
	}

	public int getActivityCount() {
		return this.getClaimant().getAllActivity().size();
	}
	
	public int getClaimsCount() {
		int count=0;
		if(this.getClaimant().getSingleClaimantClaim() != null){
		 count=	this.getClaimant().getSingleClaimantClaim().getClaimActivities().size();
		}
		return count;
	}

	public void showAllActivity(final ActionEvent actionEvent) {
		DataTable callLogTable = (DataTable) JsfUtils.findComponent(
				JsfUtils.getUIViewRoot(), "activityLog");
		callLogTable.setValue(this.getClaimant().getAllActivity());
		CommandButton button = (CommandButton) JsfUtils.findComponent(
				JsfUtils.getUIViewRoot(), "activityDisplayButton");
		button.setDisabled(true);
	}

	public ClaimantPhoneCallModel startCallLog(String userName) {
		return new ClaimantPhoneCallModel(claimant.startCallLog(userName));
	}

	public static class EntityWithAuditVoComparator implements
			java.util.Comparator<EntityWithAuditVo<ClaimantAddress>> {

		@Override
		public int compare(EntityWithAuditVo<ClaimantAddress> o1,
				EntityWithAuditVo<ClaimantAddress> o2) {
			return 0 - o1.getRevisionDate().compareTo(o2.getRevisionDate());
		}
	}

	public static class PaymentComparator implements
			java.util.Comparator<Payment> {
		
		@Override
		public int compare(Payment o1, Payment o2) {
			if (o1.getPaymentDate() == null) {
				if (o2.getPaymentDate() == null) {
					return 0;
				} else {
					return -1;
				}
			} else if (o2.getPaymentDate() == null) {
				return 1;
			} else {
				return 0 - o1.getPaymentDate().compareTo(o2.getPaymentDate());
			}
		}
		
	}

	public static class MailsComparator implements java.util.Comparator<Letter> {
		@Override
		public int compare(Letter o1, Letter o2) {
			if (o1.getMailDate() == null) {
				if (o2.getMailDate() == null) {
					return 0;
				} else {
					return -1;
				}
			} else if (o2.getMailDate() == null) {
				return 1;
			} else {
				return 0 - o1.getMailDate().compareTo(o2.getMailDate());
			}

		}
	}
	
	public static class ClaimsComparator implements
			java.util.Comparator<ClaimActivity> {
		@Override
		public int compare(ClaimActivity o1, ClaimActivity o2) {
			return 0 - o1.getActivityDate().compareTo(o2.getActivityDate());
		}
	}

	public Payment getSelectedCheck() {
		return selectedCheck;
	}

	public void setSelectedCheck(Payment selectedCheck) {
		this.selectedCheck = selectedCheck;
	}

	public List<Contact> getContactList() {
		List<Contact> ret = new ArrayList<Contact>();
		ret.addAll(claimant.getContacts());
		return ret;
	}

	public static ClaimantViewModel getClaimantByPrimayKey(final Long id) {
		Claimant claimant = Claimant.findClaimant(id);
		return new ClaimantViewModel(claimant == null ? new Claimant()
				: claimant);
	}

	public static ClaimantViewModel getNewClaimant() {
		Claimant claimant = getNewClaimantWithDefaults();
		return new ClaimantViewModel(claimant == null ? new Claimant()
				: claimant);
	}

	public static Claimant getNewClaimantWithDefaults() {
		Claimant claimant = new Claimant();
		claimant.setAddressOfRecord(new ClaimantAddress());
		claimant.setMailingAddressByType(AddressType.ADDRESS_OF_RECORD);
		claimant.getAddressOfRecord().setZipCode(new ZipCode());
		claimant.setPrimaryContact(new Contact());
		return claimant;
	}

	public static void save(final ClaimantViewModel model) {
		Claimant.save(model.getClaimant());
	}
	
	public void validateContactInformation(ComponentSystemEvent event) {

		FacesContext fc = FacesContext.getCurrentInstance();
		UIComponent components = event.getComponent();

		UIInput homePhone = (UIInput) components
				.findComponent("claimant_HomePhone");
		UIInput cellPhone = (UIInput) components
				.findComponent("claimant_CellPhone");
		UIInput workPhone = (UIInput) components
				.findComponent("claimant_WorkPhone");
		UIInput email = (UIInput) components.findComponent("claimant_Email");
		StringBuilder errors = new StringBuilder();

		if (!(StringUtils.hasText((String) homePhone.getLocalValue())
				|| StringUtils.hasText((String) cellPhone.getLocalValue()) || StringUtils
				.hasText((String) workPhone.getLocalValue()))) {
			errors.append("Please enter one of the contact numbers.");
			components.setRendererType("Text");
		}

		String enteredEmail = (String) email.getLocalValue();
		// Set the email pattern string
		Pattern p = Pattern
				.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

		// Match the given string with the pattern

		Matcher m = p.matcher(enteredEmail == null ? "" : enteredEmail);

		// Check whether match is found
		boolean matchFound = m.matches();

		if (enteredEmail != null && !matchFound) {
			errors.append("Email not valid");
		}
		if (errors.length() > 0) {
			FacesMessage msg = new FacesMessage(errors.toString());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(components.getClientId(), msg);
			components.setRendererType("Text");
			fc.renderResponse();
		}

	}
	
	public void loadCountries() {
		
		if (countryList != null && countryList.size() > 0)
			return ;
		
		List<CountryLov> blList = CountryLov.findAllCountryLovs() ;
		if (blList != null && blList.size() > 0) {
			Iterator iter = blList.iterator() ;
			countryList = Lists.newArrayList();
					
			while (iter.hasNext())
			{
				CountryLov countryLov = (CountryLov) iter.next() ;
				countryList.add(new SelectItem(countryLov.getCode(), countryLov.getDescription()));
			}
		}
		else
			log.error("No Countries found in database" ) ;
	}	

	public boolean isRequiredField() {
		return requiredField;
	}

	public void setRequiredField(boolean requiredField) {
		this.requiredField = requiredField;
	}

	public String getFilterDateOfCall(PhoneCall item) {
		return SaecDateUtils.filterDate(item.getActivityDate());
	}

	public String getFilterDateOfAccount(Activity item) {
		return SaecDateUtils.filterDate(item.getActivityDate());
	}

	public String getFilterDateOfPayment(Payment item) {
		return SaecDateUtils.filterDate(item.getPaymentDate());
	}

	public String getFilterDateOfMail(Letter item) {
		return SaecDateUtils.filterDate(item.getMailDate());
	}

	public  List<SelectItem> getCountryList() {
		if (countryList == null || countryList.size() == 0) {
			loadCountries() ;
		}
		return countryList;
	}

	public  void setCountryList(List<SelectItem> countryList) {
		ClaimantViewModel.countryList = countryList;
	}
	
	/**
	 * Converts the String to integer for sorting
	 * For sorting, taking the number as String, so the sorting is not happening correctly.
	 * @param item
	 * @return
	 */
	public Long getSortMailingControlNo(String mailingControlNo){
		return Long.parseLong(mailingControlNo == null ? "0" : mailingControlNo);
	}

}