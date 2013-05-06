package com.bfds.saec.web.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.primefaces.component.dialog.Dialog;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.CorrespondenceDocument;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.RegistrationLines;
import com.bfds.saec.domain.RequestType;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;
import com.bfds.saec.web.ui.components.model.CorrespondenceDocumentDataModel;
import com.bfds.saec.web.ui.components.model.CorrespondenceLetterDataModel;
import com.bfds.saec.web.util.JsfUtils;
 
public class CorrespondenceProcessViewModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private List<CorrespondenceLetterViewModel> correspondenceList = new ArrayList<CorrespondenceLetterViewModel>();

	private transient CorrespondenceDocumentDataModel correspondenceDocumentList;
	
	private transient CorrespondenceLetterDataModel correspondenceIncomingNIGOList;
	
	private List<CorrespondenceLetterViewModel> selectedMissingDocuments = new ArrayList<CorrespondenceLetterViewModel>();
	
	private List<CorrespondenceDocument> correspondenceDocumentsList = new ArrayList<CorrespondenceDocument>();

	private CorrespondenceLetterViewModel currentEditingCorrespondence;

	private CorrespondenceDocument[] selectedReceivedCorrespondence;
	
	private CorrespondenceDocument[] tempSelectedReceivedCorrespondence;
	
	private CorrespondenceDocument[] selectedMissingCorrespondence;
	
	private CorrespondenceDocument[] tempSelectedMissingCorrespondence;
	
	private CorrespondenceLetterViewModel selectedExistingCureMails;

	private boolean expandFlag;

	private boolean collapseFlag;

	private boolean canEdit;
	
	private boolean disableEdit;
	
	private boolean disableNew;
	
	private String documentsReceived;
	
	private String documentsMissing;
	
	private Long claimantId;
	
	private Claimant claimant;
	
	private MailObjectAddress address = new MailObjectAddress();
	
	private RegistrationLines registration = new RegistrationLines();
	
	
	public boolean loadCorrespondenceList(final Long claimantId) {
		this.claimantId = claimantId;
		correspondenceList = transformToLetterVo(getCorrespondenceList(claimantId));
		claimant = Claimant.findClaimant(claimantId, Claimant.ASSOCIATION_ADDRESSES);
		return true;
	}
	
	private List<Letter> getCorrespondenceList(Long claimantId) {
		return Letter.findCorrespondenceOfClaimant(claimantId, Letter.ASSOCIATION_IGO_CORRESPONDENCE_DOCUMENTS, Letter.ASSOCIATION_NIGO_CORRESPONDENCE_DOCUMENTS);
	}

	public double getPaymentThresholdForCorrespondenceProcess() {
		return Event.getCurrentEvent().getCorrespondenceThresholdLimit();
	}

	private List<CorrespondenceLetterViewModel> transformToLetterVo(List<Letter> letterList) {
		List<CorrespondenceLetterViewModel> ret = new ArrayList<CorrespondenceLetterViewModel>();
		for (Letter letter : letterList) {
			CorrespondenceLetterViewModel vo = new CorrespondenceLetterViewModel(letter);
			ret.add(vo);
		}
		Collections.sort(ret, new CorrespondenceComparator());
		return ret;
	}
	
	public static class CorrespondenceComparator implements java.util.Comparator<CorrespondenceLetterViewModel> {
		@Override
		public int compare(CorrespondenceLetterViewModel o1, CorrespondenceLetterViewModel o2) {
			if( o1.getLetter().getMailDate() == null ){
				if( o2.getLetter().getMailDate() == null  ){
					return 0;
				}else{
					return -1;
				}
			}else if( o2.getLetter().getMailDate() == null ){
				return 1;
			}else{
				return  0 - o1.getLetter().getMailDate().compareTo(o2.getLetter().getMailDate());
			}
		}
	}

	/**
	 * Add New Functionality
	 * Letter One Process : Generates new Incoming row in the mailing activity table.
	 * Letter Two Process : Where the system identifies an existing mail object with the �Incoming� mailing type and a status of NIGO, 
	 * 						a pop up box will display as Is this item in reference/related to an existing cure? 
	 * @return boolean 
	 */
	public boolean addNewCorrespondence() {
		resetPopups();
		setDisableEdit(true);
		newCorrespondence();
		return true;
	}
	
	/**
	 * Generates a new incoming row
	 * @return boolean
	 */
	public boolean newCorrespondence(){
		correspondenceList.add(new CorrespondenceLetterViewModel(Letter.newCorrespondence()));
		return true;
	}
	
	/**
	 * Method to make Incomplete mailing object as editable 
	 * @return boolean
	 */
	public boolean editCorrespondence() {
		setCanEdit(true);
		setDisableNew(true);
		for (CorrespondenceLetterViewModel let : correspondenceList) {
			loadLetterCodesByLetterType(let.getLetter().getMailType().name(), let);
		}

		return true;
	}

	/**  AWD window visible logic
	 * Method to pop up AWD window onselection of Outgoing in MailType and corresponding request types in Request Type selection box,
	 * onselection of Incoming in MailType pre fills the corresponding request types in Request Type selection box
	 *  
	 */
	public void onChangeMailType(final String selectedValue,CorrespondenceLetterViewModel letterViewModel) {
		Dialog awdDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "awdDialog");
		resetPopups();
		this.currentEditingCorrespondence = letterViewModel;
		if (StringUtils.hasText(selectedValue)) {
			final MailType selectedMailType = MailType.valueOf(selectedValue);
			awdDialog.setVisible(selectedMailType == MailType.OUTGOING);
			CorrespondenceLetterViewModel correspondence = letterViewModel;
			if (MailType.OUTGOING == MailType.valueOf(selectedValue)) {
				correspondence.getLetter().setMailDate(null);
			}
			else if (MailType.INCOMING == MailType.valueOf(selectedValue)) {
				loadRelatedMailObjects(); 
				correspondence.getLetter().setMailDate(new Date());
			}
		}
		else if ("".equals(selectedValue)) {
			currentEditingCorrespondence.getLetter()
					.setIgoCorrespondenceDocuments(null);
			currentEditingCorrespondence.getLetter()
					.setNigoCorrespondenceDocuments(null);
			awdDialog.setVisible(!"".equals(selectedValue));
		}
		generateMailingControlNumber(letterViewModel);
		loadLetterCodesByLetterType(selectedValue, letterViewModel);
	}
	
	/*
	 * Generates a Mailing Control Number for a outgoing mail item.
	 */
	private void generateMailingControlNumber(
			CorrespondenceLetterViewModel letterViewModel) {
		if(!StringUtils.hasText(letterViewModel.getLetter().getMailingControlNo()) && 
				letterViewModel.getLetter().getId() == null && 
				letterViewModel.getLetter().getMailType() == MailType.OUTGOING){
			letterViewModel.getLetter().setMailingControlNo((new Letter()).getNextMailingControlNumber());
		}
	}

	private void loadLetterCodesByLetterType(final String selectedValue,
			CorrespondenceLetterViewModel letterViewModel) {
		if("".equals(selectedValue)){letterViewModel.setLetterType(null);}
		else if(MailType.valueOf(selectedValue) == MailType.INCOMING && letterViewModel.isNew()){
			letterViewModel.setLetterType(LetterType.GENERAL_CORRESPONDENCE);
			letterViewModel.getLetter().setLetterStatus(null);
		}else{
			letterViewModel.setLetterType(LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER);
		}
	}


	/*
	 * AWD Object availability logic
	 */
	public boolean createAWDObject() {
		currentEditingCorrespondence.getLetter().setCorrespondenceHasAwdObject(false);
		awdDialogVisible();
		//loadRelatedMailObjects();
		claimant.getMailingAddress().copyTo(address);
		claimant.getClaimantRegistration().getLines().copyTo(registration);
		loadAddressWindow();
		return true;
	}

	public boolean notCreateAWDObject() {
		currentEditingCorrespondence.getLetter().setCorrespondenceHasAwdObject(true);
		awdDialogVisible();
		//loadRelatedMailObjects();
		claimant.getMailingAddress().copyTo(address);
		claimant.getClaimantRegistration().getLines().copyTo(registration);
		loadAddressWindow();
		return true;
	}
	
	private void loadAddressWindow() {
		Dialog addressDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "addressDialog");
		addressDialog.setVisible(currentEditingCorrespondence.getLetter().getMailType() == MailType.OUTGOING);
	}

	
	public boolean saveAddress(){
		resetPopups();
		MailObjectAddress mailAddress = new MailObjectAddress();
		RegistrationLines registrationLines = new RegistrationLines();
		address.copyTo(mailAddress);
		registration.copyTo(registrationLines);
		this.currentEditingCorrespondence.setAddress(mailAddress);
		this.currentEditingCorrespondence.setRegistration(registrationLines);
		if(!claimant.getMailingAddress().getAddressAsString().equals(this.currentEditingCorrespondence.getAddress().getAddressAsString())){
			this.currentEditingCorrespondence.getAddress().setAddressType(AddressType.ONE_TIME_MAILING);
			this.currentEditingCorrespondence.getLetter().setAddress(this.currentEditingCorrespondence.getAddress());
			this.currentEditingCorrespondence.getLetter().setPayToRegistration(this.currentEditingCorrespondence.getRegistration());
		}
		loadRelatedMailObjects();
		return true;
	}
	
	/**
	 * On Changing the Request Type value a Pop up box will display with the event �Threshold level�: 
	 * Pop up box will pull the value from the threshold field at the event level
	 * Pop up box will ask the associate if the request amount is greater than the threshold displayed
	 * Associate answers Y/N question on the pop up box, 'Y' will pre fill the comments column as Threshold = Y & 'N' will pre fill the comments column as Threshold = N
	 * @param selectedValue
	 * @param letterViewModel
	 */

	public void onChangeRequestType(String selectedValue, CorrespondenceLetterViewModel letterViewModel) {
		//assertIndex(row, letterViewModel);
		resetPopups();
		this.currentEditingCorrespondence = letterViewModel;//getcurrentEditingCorrespondence(row);
		currentEditingCorrespondence.getLetter().setRequestType(selectedValue);
		if ("".equals(selectedValue) && !isCanEdit()) {
			currentEditingCorrespondence.getLetter().setIgoCorrespondenceDocuments(null);
			currentEditingCorrespondence.getLetter().setNigoCorrespondenceDocuments(null);
			if(letterViewModel.getLetter().getLetterStatus() == LetterStatus.NIGO && letterViewModel.isNew()) {
				copyCorrespondenceDocument(this.currentEditingCorrespondence.getLetter(), letterViewModel.getInresponseToList().get(0).getLetter());
			}
		}
		Dialog thresholdDialog = (Dialog) JsfUtils.findComponent(
				JsfUtils.getUIViewRoot(), "thresholdDialog");
		if (StringUtils.hasText(selectedValue)) {
			if(letterViewModel.getLetter().getInResponseTo() != null || letterViewModel.getLetter().getMailType() == MailType.OUTGOING){
				thresholdDialog.setVisible(false);
			}else{
				thresholdDialog.setVisible(true);
			}
					
		}
		
	}

	/**
	 * 
	 * @param selectedValue
	 * @param letterViewModel
	 */
	public void onChangeStatus(String selectedValue, CorrespondenceLetterViewModel letterViewModel) {
		resetPopups();
		//assertIndex(row, x);
		this.currentEditingCorrespondence = letterViewModel;// getcurrentEditingCorrespondence(row);
		if (!"".equals(selectedValue)
				&& LetterStatus.NIGO == LetterStatus.valueOf(selectedValue)) {
			if(letterViewModel.getInresponseToList().size() == 0) {
				CorrespondenceLetterViewModel model = newOutgoingCorrespondenceLetterViewModelForIncomingNigo();
				//model.getLetter().setDescription(this.currentEditingCorrespondence.getLetter().getDescription());
				//model.getLetter().setRequestType(this.currentEditingCorrespondence.getLetter().getRequestType());
				letterViewModel.getInresponseToList().add(model);
			} else {
				// For letter 1 process, there will be only one object in the list.
				copyCorrespondenceDocument(this.currentEditingCorrespondence.getLetter(), letterViewModel.getInresponseToList().get(0).getLetter());
			}
			//correspondenceList.add(model);
		}
	}

	private CorrespondenceLetterViewModel newOutgoingCorrespondenceLetterViewModelForIncomingNigo() {
		Letter let = this.currentEditingCorrespondence.getLetter().newOutgoingCorrespondenceForIncomingNigo();
		CorrespondenceLetterViewModel model = new CorrespondenceLetterViewModel(let);
		model.setLetterType(LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER);
		return model;
	}
	
	private void copyCorrespondenceDocument(Letter from, Letter to) {
		to.setIgoCorrespondenceDocuments(from.getIgoCorrespondenceDocuments());
		to.setNigoCorrespondenceDocuments(from.getNigoCorrespondenceDocuments());
	}
	
	/**
	 * Saves the correspondences with the corresponding status.
	 * @param messageContext
	 * @param claimant
	 * @return
	 */

	public boolean saveCorrespondence(final MessageContext messageContext,
			final Claimant claimant) {
		List<Letter> letterList = new ArrayList<Letter>();
		if (this.getCorrespondenceList() != null
				&& !this.getCorrespondenceList().isEmpty()) {
			if(!validateCorrespondence(this.getCorrespondenceList(),messageContext)){
				return false;
			}
			for (CorrespondenceLetterViewModel letterVo : this.getCorrespondenceList()) {
				if((letterVo.getLetter().getLetterStatus() != LetterStatus.NIGO && letterVo.getLetter().getLetterStatus() != LetterStatus.IGO) 
						&& letterVo.getLetter().getMailType() == MailType.OUTGOING
						&& (letterVo.getLetter().getDescription() == null || !StringUtils.hasText(letterVo.getLetter().getDescription())
								|| letterVo.getLetter().getLetterCode() == null )
								&& letterVo.isNew()){
					letterVo.getLetter().setLetterStatus(LetterStatus.PENDING);
				}
				letterList.add(letterVo.getLetter());
			}
			saveCorrespondence(letterList, claimant);
		}
		return true;
	}

	

	private boolean validateCorrespondence(List<CorrespondenceLetterViewModel> correspondenceList,
			MessageContext messageContext) {
		for (CorrespondenceLetterViewModel letterVo : this.getCorrespondenceList()) {
			
			if (letterVo.getLetter().getLetterStatus() == LetterStatus.IGO
					&& !letterVo.getLetter().getNigoCorrespondenceDocuments().isEmpty()
					&& letterVo.getLetter().getNigoCorrespondenceDocuments() != null) {
				messageContext.addMessage(new MessageBuilder().error()
								.defaultText("IGO Status should not have Missing Documents.")
								.build());
				return false;
			}else if(letterVo.getLetter().getLetterStatus() == LetterStatus.NIGO){
				if( letterVo.getLetter().getIgoCorrespondenceDocuments() != null
						&&!letterVo.getLetter().getIgoCorrespondenceDocuments().isEmpty()
						&& letterVo.getLetter().getNigoCorrespondenceDocuments().isEmpty()){
					messageContext.addMessage(new MessageBuilder().error()
							.defaultText("NIGO Status should have Missing Documents.")
							.build());
					return false;
				}
			}
			
		}
		return true;
	}

	private void saveCorrespondence(List<Letter> letterList,
			final Claimant claimant) {
		for (Letter letter : letterList) {
			if(letter.getLetterCode() == null) {				
				if(letter.getMailType() == MailType.OUTGOING) {
					//TODO: Map via RequestType
					letter.setLetterCode(LetterCode.findByCode("701"));
				} else if(letter.getMailType() == MailType.INCOMING) {
					//TODO: Map via RequestType
					letter.setLetterCode(LetterCode.findByCode("109"));
				}
			} 	
			claimant.addLetter(letter);
			Letter inResponseTo = letter.getInResponseTo() ;
			if (letter.getId() == null) {
				letter.persist();
			}
			if (inResponseTo != null) {
				inResponseTo.setClaimant(claimant);
				letter = letter.merge();
			}
			if(inResponseTo == null && letter.getMailType() == MailType.OUTGOING){
				letter = letter.merge();
			}
		}
	}

	/*
	 * Pop up to select letter types for given threshold and request type
	 */
	public boolean loadDocumentConfiguration() {
		thresholdDialogVisible();
		CorrespondenceLetterViewModel obj = getCurrentEditingCorrespondence();
		obj.setDescription(Letter.THRESHOLD_YES);
		loadDocumentConfiguration(obj);
		return true;
	}

	public boolean loadDocumentConfigurationForLessAmount() {
		thresholdDialogVisible();
		CorrespondenceLetterViewModel obj = getCurrentEditingCorrespondence();
		obj.setDescription(Letter.THRESHOLD_NO);
		return true;
	}

	private void thresholdDialogVisible() {
		Dialog thresholdDialog = (Dialog) JsfUtils.findComponent(
				JsfUtils.getUIViewRoot(), "thresholdDialog");
		thresholdDialog.setVisible(false);
	}
	
	private void awdDialogVisible() {
		Dialog awdDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "awdDialog");
		awdDialog.setVisible(false);
	}
	
	private void loadDocumentConfiguration(CorrespondenceLetterViewModel letterViewModel) {
		//correspondenceDocumentList = CorrespondenceDocument.findCorrespondenceDocuments(obj.getLetter().getRequestType());
		resetPopups();
	}

	private CorrespondenceLetterViewModel getCurrentEditingCorrespondence() {
		return currentEditingCorrespondence;
	}

	/**
	 * To load Received Documents for the current editing correspondence
	 * @param letterViewModel
	 */
	public void getReceivedCorrespondenceDocuments(CorrespondenceLetterViewModel letterViewModel){
		resetPopups();
		this.currentEditingCorrespondence = letterViewModel;
		loadDocumentConfiguration(this.currentEditingCorrespondence);
		Dialog igoDocsDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "igoDocsDialog");
		igoDocsDialog.setVisible(true);
	}
	
	/**
	 * To load Missing Documents for the current editing correspondence
	 * @param letterViewModel 
	 */
	
	public void getMissingCorrespondenceDocuments(CorrespondenceLetterViewModel letterViewModel){
		resetPopups();
		this.currentEditingCorrespondence = letterViewModel;
		loadDocumentConfiguration(this.currentEditingCorrespondence);
		Dialog nigoDocsDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "nigoDocsDialog");
		nigoDocsDialog.setRendered(true);
		nigoDocsDialog.setVisible(true);
	}
	
	/**
	 * Method to load the Received Documents of the current editing correspondence to show in a pop up box
	 * @return boolean
	 */
	public boolean updateReceivedDocumentsOnSelectedCorrespondence() {
		final CorrespondenceLetterViewModel letterViewModel = getCurrentEditingCorrespondence();
		setSelectedReceivedCorrespondence(this.selectedReceivedCorrespondence);
		tempSelectedReceivedCorrespondence = this.selectedReceivedCorrespondence;
		if (letterViewModel.getLetter().getIgoCorrespondenceDocuments() == null) {
			letterViewModel.getLetter().setIgoCorrespondenceDocuments(new HashSet<CorrespondenceDocument>());
		}
		letterViewModel.getLetter().getIgoCorrespondenceDocuments().clear();
			for (CorrespondenceDocument doc : this.selectedReceivedCorrespondence) {
				letterViewModel.getLetter().getIgoCorrespondenceDocuments().add(doc);
			}
		return true;
	}
	
	/**
	 * Method to load the Missing Documents of the current editing correspondence to show in a pop up box
	 * @return boolean
	 */
	public boolean updateMissingDocumentsOnSelectedCorrespondence() {
		final CorrespondenceLetterViewModel letterViewModel = getCurrentEditingCorrespondence();
		setSelectedMissingCorrespondence(this.selectedMissingCorrespondence);
		tempSelectedMissingCorrespondence = this.selectedMissingCorrespondence;
		if (letterViewModel.getLetter().getNigoCorrespondenceDocuments() == null) {
			letterViewModel.getLetter().setNigoCorrespondenceDocuments(new HashSet<CorrespondenceDocument>());
		}
		letterViewModel.getLetter().getNigoCorrespondenceDocuments().clear();
			for (CorrespondenceDocument doc : this.selectedMissingCorrespondence) {
				letterViewModel.getLetter().getNigoCorrespondenceDocuments().add(doc);
			}
		return true;
	}
	

	public boolean expand() {
		resetPopups();
		setExpandFlag(true);
		setCollapseFlag(false);
		return true;
	}

	public boolean collapse() {
		resetPopups();
		setExpandFlag(false);
		setCollapseFlag(true);
		return true;
	}
	
	public boolean loadMailsRelatedToExistingCure(){
		resetPopups();
		//correspondenceList = getCorrespondenceList();
		Dialog existingCureDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "existingCureDialog");
		existingCureDialog.setVisible(true);
		return true;
	}
	
	/**
	 * Method identifies an existing mail object with the �Incoming� mailing type and a status of NIGO, 
	 * then displays pop up box with the message, Is this item in reference/related to an existing cure? 
	 * @return boolean 
	 * 
	 */
	private boolean loadRelatedMailObjects() {
		if(this.currentEditingCorrespondence.getLetter().getMailType() == MailType.INCOMING){
			for(CorrespondenceLetterViewModel correspondence : correspondenceList){
				if(correspondence.getLetter().getMailType() == MailType.INCOMING && correspondence.getLetter().getLetterStatus() == LetterStatus.NIGO){
					Dialog relatedMailsDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "relatedMailsDialog");
					relatedMailsDialog.setVisible(true);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Selected �Incoming� mailing type and a status of NIGO item Missing Documents and Received Documents will populate in the pop up box.
	 * @return boolean
	 */
	
	public boolean missingDocumnetsOfExistingNIGO(final MessageContext messageContext){
		resetPopups();
		correspondenceDocumentsList.clear();
		//for(CorrespondenceLetterViewModel obj : selectedExistingCureMails){
			if(selectedExistingCureMails.getLetter().getNigoCorrespondenceDocuments() != null){
				correspondenceDocumentsList.addAll(selectedExistingCureMails.getLetter().getNigoCorrespondenceDocuments());
				setCorrespondenceDocumentsList(correspondenceDocumentsList);
			}
		//}
		//selectedExistingCureMails = new CorrespondenceLetterViewModel();//After assigning the values to a list , clearing the array 
		Dialog missingDocExistingCureDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "missingDocExistingCureDialog");
		missingDocExistingCureDialog.setVisible(true);
		return true;
	}
	
	/**
	 * Method to show a confirmation pop up for IGO / NIGO
	 * @return boolean
	 */
	public boolean igoNigoConfirmation(final MessageContext messageContext){
		resetPopups();
		Dialog itemIGODialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "itemIGODialog");
		itemIGODialog.setVisible(true);
		return true;
		
	}

	private boolean validateIGOCorrespondenceDocuments(
			final MessageContext messageContext) {
		for(CorrespondenceDocument doc : getCorrespondenceDocumentsList()){
			if(doc.isSelectedMissingDocs() == false){
				messageContext.addMessage(new MessageBuilder().error()
						.defaultText("IGO Status should not have Missing Documents.")
						.build());
				Dialog missingDocExistingCureDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "missingDocExistingCureDialog");
				missingDocExistingCureDialog.setVisible(true);
				return false;
			}
		}
		return true;
	}
	
	private boolean validateNIGOCorrespondenceDocuments(
			final MessageContext messageContext) {
		int count = 0;
		for(CorrespondenceDocument doc : getCorrespondenceDocumentsList()){
			if(doc.isSelectedMissingDocs() == true){
				count++;
			}
		}
		
		for(CorrespondenceDocument doc : getCorrespondenceDocumentsList()){
			if(doc.isSelectedMissingDocs() == true && count != getCorrespondenceDocumentsList().size()){
				return true;
			}else if(count == getCorrespondenceDocumentsList().size()){
				messageContext.addMessage(new MessageBuilder().error()
						.defaultText("NIGO Status should have Missing Documents.")
						.build());
				Dialog missingDocExistingCureDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "missingDocExistingCureDialog");
				missingDocExistingCureDialog.setVisible(true);
				return false;
			}/*else{
				messageContext.addMessage(new MessageBuilder().error()
						.defaultText("NIGO Status should not have Received Documents.")
						.build());
				Dialog missingDocExistingCureDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "missingDocExistingCureDialog");
				missingDocExistingCureDialog.setVisible(true);
				return false;
			}*/
		}
		return true;
	}
	
	/**
	 * Incoming item can be IGO which will resolve the existing NIGO item and result in only a new incoming mail activity object
	 * Listing of missing documents will have editable check boxes display
	 * Any new items checked will not be updated/edited in the original NIGO (related NIGO items selected by associate).  
	 * Any change (i.e. - new box(es) selected, will populate to the current mail activity line
	 * @return boolean
	 */
	
	public boolean saveSelectedMissingDocumnetsToLinkAsIGO(final MessageContext messageContext){
		//Here First adding the documents received to the list
		resetPopups();
		List<CorrespondenceDocument> receivedDocs = new ArrayList<CorrespondenceDocument>();
		if(!validateIGOCorrespondenceDocuments(messageContext)){
			Dialog itemIGODialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "itemIGODialog");
			itemIGODialog.setVisible(false);
			return false;
		}else{
		for(CorrespondenceLetterViewModel received : getCorrespondenceIncomingNIGOList()){
			for(CorrespondenceDocument doc : received.getReceivedCorrespondenceDocumentList()){
				receivedDocs.add(doc);
			}
			this.currentEditingCorrespondence.getLetter().setDescription(received.getLetter().getDescription());
			this.currentEditingCorrespondence.getLetter().setRequestType(received.getLetter().getRequestType());
			this.currentEditingCorrespondence.getLetter().setLetterCode(received.getLetter().getLetterCode());
		}
		Set<CorrespondenceDocument> igoCorrespondenceDocuments = new HashSet<CorrespondenceDocument>();
		//Here adding the documents missing to the list
		for(CorrespondenceDocument doc : correspondenceDocumentsList){
			if(doc.isSelectedMissingDocs()){
				receivedDocs.add(doc);
			}
		}
		igoCorrespondenceDocuments.addAll(receivedDocs);
		this.currentEditingCorrespondence.getLetter().setIgoCorrespondenceDocuments(igoCorrespondenceDocuments);
		
		if(!(this.currentEditingCorrespondence.getLetter().getMailType() == null)){
			this.currentEditingCorrespondence.getLetter().setLetterStatus(LetterStatus.IGO);
		}
		this.currentEditingCorrespondence.getLetter().setInResponseTo(this.currentEditingCorrespondence.getLetter());
		
		//setDisableNew(true);
		return true;
		}
	}
	
	/**
	 * Incoming item can be NIGO which does not resolve the existing NIGO item and results in a new 
	 * incoming mail activity object as well as an additional outgoing mail activity object
	 * @return boolean
	 */
	public boolean saveSelectedMissingDocumnetsToLinkAsNIGO(final MessageContext messageContext){
		resetPopups();
		if(!validateNIGOCorrespondenceDocuments(messageContext)){
			Dialog itemIGODialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "itemIGODialog");
			itemIGODialog.setVisible(false);
			return false;
		}else{
		List<CorrespondenceDocument> receivedDocs = new ArrayList<CorrespondenceDocument>();
		for(CorrespondenceLetterViewModel received : getCorrespondenceIncomingNIGOList()){
			for(CorrespondenceDocument doc : received.getReceivedCorrespondenceDocumentList()){
				receivedDocs.add(doc);
			}
			this.currentEditingCorrespondence.getLetter().setLetterCode(received.getLetter().getLetterCode());
		}
		Set<CorrespondenceDocument> igoCorrespondenceDocuments = new HashSet<CorrespondenceDocument>();
		Set<CorrespondenceDocument> nigoCorrespondenceDocuments = new HashSet<CorrespondenceDocument>();
		//Here adding the documents missing to the list
		for(CorrespondenceDocument doc : correspondenceDocumentsList){
			if(doc.isSelectedMissingDocs()){
				receivedDocs.add(doc);
				//igoCorrespondenceDocuments.add(doc);
			}else{
				nigoCorrespondenceDocuments.add(doc);
			}
		}
		igoCorrespondenceDocuments.addAll(receivedDocs);
		nigoCorrespondenceDocuments.addAll(nigoCorrespondenceDocuments);//missing docs
		//nigoCorrespondenceDocuments.addAll(nigoCorrespondenceDocuments);//missing docs
		
		this.currentEditingCorrespondence.getLetter().setIgoCorrespondenceDocuments(igoCorrespondenceDocuments);
		this.currentEditingCorrespondence.getLetter().setNigoCorrespondenceDocuments(nigoCorrespondenceDocuments);
		this.currentEditingCorrespondence.getLetter().setLetterStatus(LetterStatus.NIGO);
		
		CorrespondenceLetterViewModel model = newOutgoingMailObjectForNIGO(igoCorrespondenceDocuments , nigoCorrespondenceDocuments);
		model.getLetter().setInResponseTo(this.currentEditingCorrespondence.getLetter()) ;
		correspondenceList.add(model);
		
	//	this.currentEditingCorrespondence.getLetter().setInResponseTo(this.currentEditingCorrespondence.getLetter());
		this.currentEditingCorrespondence.getLetter().setInResponseTo(selectedExistingCureMails.getLetter());
		
		//setDisableNew(true);
		return true;
		}
	}

	private CorrespondenceLetterViewModel newOutgoingMailObjectForNIGO(Set<CorrespondenceDocument> igoCorrespondenceDocuments, Set<CorrespondenceDocument> nigoCorrespondenceDocuments) {
		Letter let = new Letter().newOutgoingMailObjectForNIGO();
		if(cureLetterSendLimitExceeded()){
			let.setDescription("NFA - Cure Limit");
			let.setLetterStatus(LetterStatus.NO_FURTHER_ACTION);
		}
		let.setIgoCorrespondenceDocuments(igoCorrespondenceDocuments);
		let.setNigoCorrespondenceDocuments(nigoCorrespondenceDocuments);
		//let.setDescription(getCorrespondenceIncomingNIGOList().get(0).getLetter().getDescription());
		//let.setRequestType(getCorrespondenceIncomingNIGOList().get(0).getLetter().getRequestType());
		CorrespondenceLetterViewModel model = new CorrespondenceLetterViewModel(let);
		model.setLetterType(LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER);
		return model;
	}


	public List<CorrespondenceLetterViewModel> getCorrespondenceList() {
		List<CorrespondenceLetterViewModel> ret = new ArrayList<CorrespondenceLetterViewModel>();
		for(CorrespondenceLetterViewModel letterViewModel : correspondenceList) {
			ret.add(letterViewModel);
			if(letterViewModel.isNew() && letterViewModel.getLetter().getMailType() == MailType.INCOMING 
					&& letterViewModel.getLetter().getLetterStatus() == LetterStatus.NIGO) {
				ret.addAll(letterViewModel.getInresponseToList());
			}
		}
		return ret;
	}

	public void setCorrespondenceList(List<CorrespondenceLetterViewModel> correspondenceList) {
		this.correspondenceList = correspondenceList;
	}

	public CorrespondenceDocumentDataModel getDocConfigList() {
		return correspondenceDocumentList;
	}

	public void setDocConfigList(CorrespondenceDocumentDataModel docConfigList) {
		this.correspondenceDocumentList = docConfigList;
	}

	public CorrespondenceDocumentDataModel getCorrespondenceDocumentList() {
		if(correspondenceDocumentList == null) {
			List<CorrespondenceDocument> correspondenceDocuments = CorrespondenceDocument.findCorrespondenceDocuments(null);
			if(this.tempSelectedReceivedCorrespondence != null ){
				prepareDocmentsRecivedAndMissing(correspondenceDocuments,this.tempSelectedReceivedCorrespondence);
			}
			if(this.tempSelectedMissingCorrespondence != null){
				prepareDocmentsRecivedAndMissing(correspondenceDocuments,this.tempSelectedMissingCorrespondence);
			}
			correspondenceDocumentList = new CorrespondenceDocumentDataModel(correspondenceDocuments);
		}
			
		return correspondenceDocumentList;
	}

	private void prepareDocmentsRecivedAndMissing(List<CorrespondenceDocument> correspondenceDocuments,CorrespondenceDocument[] selectedDocuments) {
		List<CorrespondenceDocument> selectedRecDocs = Arrays.asList(selectedDocuments);
		for(CorrespondenceDocument obj : selectedRecDocs){
			correspondenceDocuments.remove(obj);
		}
	}

	public void setCorrespondenceDocumentList(
			CorrespondenceDocumentDataModel correspondenceDocumentList) {
		this.correspondenceDocumentList = correspondenceDocumentList;
	}
	
	/**
	 * Method identifies an existing mail object with the �Incoming� mailing type and a status of NIGO
	 * @return
	 */
	public CorrespondenceLetterDataModel getCorrespondenceIncomingNIGOList() {
		List<CorrespondenceLetterViewModel> ret = new ArrayList<CorrespondenceLetterViewModel>();
		for(CorrespondenceLetterViewModel letterViewModel : correspondenceList) {
			if(letterViewModel.getLetter().getMailType() == MailType.INCOMING 
					&& letterViewModel.getLetter().getLetterStatus() == LetterStatus.NIGO) {
				ret.add(letterViewModel);
			}
		}
		return new CorrespondenceLetterDataModel(ret);
	}

	public boolean cureLetterSendLimitExceeded() {
		return Letter.cureLetterSendLimitExceeded(claimantId);
	}
	
	/*private boolean loadClaimantAddress() {
		claimant = Claimant.findClaimant(claimantId,Claimant.ASSOCIATION_ADDRESSES);
		address = claimant.getMailingAddress();
		addressRevisionInfo = entityAuditDaoDao.getLastRevisionInfo(
				ClaimantAddress.class, address.getId());
		return true;
	}*/
	
	public void setCorrespondenceIncomingNIGOList(
			CorrespondenceLetterDataModel correspondenceIncomingNIGOList) {
		this.correspondenceIncomingNIGOList = correspondenceIncomingNIGOList;
	}

	public MailType getOutgoingMailType() {
		return MailType.OUTGOING;
	}

	public MailType getIncomingMailType() {
		return MailType.INCOMING;
	}

	public LetterStatus getIgoLetterStatus() {
		return LetterStatus.IGO;
	}

	public LetterStatus getNigoLetterStatus() {
		return LetterStatus.NIGO;
	}

	public CorrespondenceDocument[] getSelectedReceivedCorrespondence() {
		return selectedReceivedCorrespondence;
	}

	public void setSelectedReceivedCorrespondence(
			CorrespondenceDocument[] selectedReceivedCorrespondence) {
		this.selectedReceivedCorrespondence = selectedReceivedCorrespondence;
	}
	
	public CorrespondenceDocument[] getSelectedMissingCorrespondence() {
		return selectedMissingCorrespondence;
	}

	public void setSelectedMissingCorrespondence(
			CorrespondenceDocument[] selectedMissingCorrespondence) {
		this.selectedMissingCorrespondence = selectedMissingCorrespondence;
	}
	
	

	public CorrespondenceLetterViewModel getSelectedExistingCureMails() {
		return selectedExistingCureMails;
	}

	public void setSelectedExistingCureMails(
			CorrespondenceLetterViewModel selectedExistingCureMails) {
		this.selectedExistingCureMails = selectedExistingCureMails;
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

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public boolean isDisableEdit() {
		return disableEdit;
	}

	public void setDisableEdit(boolean disableEdit) {
		this.disableEdit = disableEdit;
	}

	public boolean isDisableNew() {
		return disableNew;
	}

	public void setDisableNew(boolean disableNew) {
		this.disableNew = disableNew;
	}
	
	public String getDocumentsReceived() {
		return documentsReceived;
	}

	public void setDocumentsReceived(String documentsReceived) {
		this.documentsReceived = documentsReceived;
	}

	public String getDocumentsMissing() {
		return documentsMissing;
	}

	public void setDocumentsMissing(String documentsMissing) {
		this.documentsMissing = documentsMissing;
	}
	
	public List<CorrespondenceLetterViewModel> getSelectedMissingDocuments() {
		return selectedMissingDocuments;
	}

	public void setSelectedMissingDocuments(
			List<CorrespondenceLetterViewModel> selectedMissingDocuments) {
		this.selectedMissingDocuments = selectedMissingDocuments;
	}

	/**
	 * To Clear all the popup's while loading the page
	 */
	public void resetPopups() {
		selectedReceivedCorrespondence = new CorrespondenceDocument[0];
		selectedMissingCorrespondence = new CorrespondenceDocument[0];
		
		Dialog awdDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "awdDialog");
		awdDialog.setVisible(false);

		thresholdDialogVisible();

		Dialog nigoDocsDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "nigoDocsDialog");
		nigoDocsDialog.setVisible(false);
		
		Dialog igoDocsDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "igoDocsDialog");
		igoDocsDialog.setVisible(false);
		
		Dialog relatedMailsDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "relatedMailsDialog");
		relatedMailsDialog.setVisible(false);
		
		Dialog existingCureDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "existingCureDialog");
		existingCureDialog.setVisible(false);
		
		Dialog missingDocExistingCureDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "missingDocExistingCureDialog");
		missingDocExistingCureDialog.setVisible(false);
		
		Dialog itemIGODialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "itemIGODialog");
		itemIGODialog.setVisible(false);
		
		Dialog addressDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "addressDialog");
		addressDialog.setVisible(false);
	}
	
	public Claimant getClaimant() {
		return claimant;
	}

	public void setClaimant(Claimant claimant) {
		this.claimant = claimant;
	}

	public MailObjectAddress getAddress() {
		return address;
	}

	public void setAddress(MailObjectAddress address) {
		this.address = address;
	}

	public RegistrationLines getRegistration() {
		return registration;
	}

	public void setRegistration(RegistrationLines registration) {
		this.registration = registration;
	}

	public List<CorrespondenceDocument> getCorrespondenceDocumentsList() {
		return correspondenceDocumentsList;
	}

	public void setCorrespondenceDocumentsList(
			List<CorrespondenceDocument> correspondenceDocumentsList) {
		this.correspondenceDocumentsList = correspondenceDocumentsList;
	}

}