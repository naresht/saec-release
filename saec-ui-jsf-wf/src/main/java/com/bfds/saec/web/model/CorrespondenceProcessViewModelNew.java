package com.bfds.saec.web.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.dialog.Dialog;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.CorrespondenceDocument;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.RegistrationLines;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;
import com.bfds.saec.domain.util.AccountContext;
import com.bfds.saec.util.SaecStringUtils;
import com.bfds.saec.web.ui.components.model.CorrespondenceDocumentDataModel;
import com.bfds.saec.web.ui.components.model.CorrespondenceLetterDataModel;
import com.bfds.saec.web.util.JsfUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.bfds.saec.domain.reference.RoleType;

public class CorrespondenceProcessViewModelNew implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private CorrespondenceLetterViewModel newCorrespondence;

	private List<CorrespondenceLetterViewModel> correspondenceList = new ArrayList<CorrespondenceLetterViewModel>();
	
	private List<CorrespondenceDocument> correspondenceDocumentsList = new ArrayList<CorrespondenceDocument>();

	private transient CorrespondenceDocumentDataModel correspondenceDocumentList;

	private transient CorrespondenceLetterDataModel correspondenceIncomingNIGOList;
	
	private CorrespondenceDocument[] selectedReceivedCorrespondence;
	
	/**
	 * Added to store the old values of selected received documents
	 * Because when we click Save from UI the selected selectedReceivedCorrespondence is getting clear
	 */
	private CorrespondenceDocument[] tempSelectedReceivedCorrespondence;

	private CorrespondenceDocument[] selectedMissingCorrespondence;
	
	/**
	 * Added to store the old values of selected missing documents
	 * Because when we click Save from UI the selected selectedMissingCorrespondence is getting clear 
	 */
	private CorrespondenceDocument[] tempSelectedMissingCorrespondence;

	private boolean expandFlag;

	private boolean collapseFlag;

	private boolean canEdit;

	private Claimant claimant;

	private MailObjectAddress address = new MailObjectAddress();

	private RegistrationLines registration = new RegistrationLines();

	private boolean enableAddNewPanel = false;
	
	/**
	 * To load all the {@link Letter}s of the claimant
	 * @param claimantId
	 * @return
	 */
	public boolean loadCorrespondenceList(final Long claimantId) {
		correspondenceList = transformToLetterVo(getCorrespondenceList(claimantId));
		claimant = Claimant.findClaimant(claimantId,
				Claimant.ASSOCIATION_ADDRESSES);
		return true;
	}

	private List<Letter> getCorrespondenceList(Long claimantId) {
		return Letter.findCorrespondenceOfClaimant(claimantId,
				Letter.ASSOCIATION_IGO_CORRESPONDENCE_DOCUMENTS,
				Letter.ASSOCIATION_NIGO_CORRESPONDENCE_DOCUMENTS);
	}

	private List<CorrespondenceLetterViewModel> transformToLetterVo(
			List<Letter> letterList) {
		List<CorrespondenceLetterViewModel> ret = new ArrayList<CorrespondenceLetterViewModel>();
		for (Letter letter : letterList) {
			CorrespondenceLetterViewModel vo = new CorrespondenceLetterViewModel(
					letter);
			ret.add(vo);
		}
		Collections.sort(ret, new CorrespondenceComparator());
		return ret;
	}

	public static class CorrespondenceComparator implements
			java.util.Comparator<CorrespondenceLetterViewModel> {
		@Override
		public int compare(CorrespondenceLetterViewModel o1,
				CorrespondenceLetterViewModel o2) {
			if (o1.getLetter().getMailDate() == null) {
				if (o2.getLetter().getMailDate() == null) {
					return 0;
				} else {
					return -1;
				}
			} else if (o2.getLetter().getMailDate() == null) {
				return 1;
			} else {
				return 0 - o1.getLetter().getMailDate()
						.compareTo(o2.getLetter().getMailDate());
			}
		}
	}

	/**
	 * Add New Functionality<p> 
	 * Generates new {@link Letter} in the separate panel in UI, to create a new correspondence {@link Letter}<p> 
	 * and will add to the {@link List} correpondenceList .<p> 
	 * @return boolean
	 */
	public boolean addNewCorrespondence() {
		resetPopups();
		resetValues();
		setCanEdit(false);
		setEnableAddNewPanel(true);
		this.setNewCorrespondence(new CorrespondenceLetterViewModel(Letter.newCorrespondence()));
		return true;
	}
	
	/**
	 * To edit an existing record, the user should click on the desired record.<p>
	 * Date entry form will appear with data pre-population of  {@link Letter} for edit
	 * @param mail
	 */
	public void loadSelectedMail(CorrespondenceLetterViewModel mail){
		resetPopups();
		resetValues();
		setCanEdit(true);
		this.setNewCorrespondence(mail);
		setLetterTypeOfMail(mail.getLetter().getMailType().toString(), mail);
		
		Set<CorrespondenceDocument> alreadySavedCorrespondenceDocument = mail.getLetter().getIgoCorrespondenceDocuments();
		Set<CorrespondenceDocument> alreadySavedMissingCorrespondenceDocument = mail.getLetter().getNigoCorrespondenceDocuments();
		this.selectedReceivedCorrespondence = new CorrespondenceDocument[alreadySavedCorrespondenceDocument.size()];
		this.selectedMissingCorrespondence = new CorrespondenceDocument[alreadySavedMissingCorrespondenceDocument.size()];
		int i=0;
		int j=0;
		for(CorrespondenceDocument correspondenceDocument :alreadySavedCorrespondenceDocument){
			this.selectedReceivedCorrespondence[i]=correspondenceDocument;
			i++;
		}
		for(CorrespondenceDocument correspondenceDocument :alreadySavedMissingCorrespondenceDocument){
			this.selectedMissingCorrespondence[j]=correspondenceDocument;
			j++;
		}
		this.tempSelectedReceivedCorrespondence = this.selectedReceivedCorrespondence;
		this.tempSelectedMissingCorrespondence = this.selectedMissingCorrespondence;
		setEnableAddNewPanel(true);
	}
	
	
	
	/**
	 *	The �Incoming� or �Outgoing� {@link MailType} value will determine the selections that display in the Request type drop down box.<p>
	 *	If the {@link MailType} �Incoming� the mailDate should be default to system date.<p>
	 *  If the {@link MailType} �Outgoing� the mailDate should be default to blank and generates a mailing control number.<p>
	 * @param selectedValue
	 * @param letterViewModel
	 */
	public void onChangeMailType(final String mailType,CorrespondenceLetterViewModel letterViewModel) {
		resetPopups();
		Dialog awdDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "awdDialog");
		if (StringUtils.hasText(mailType)) {
			final MailType selectedMailType = MailType.valueOf(mailType);
			awdDialog.setVisible(selectedMailType == MailType.OUTGOING);
			CorrespondenceLetterViewModel correspondence = letterViewModel;
			if (MailType.OUTGOING == MailType.valueOf(mailType)) {
				correspondence.getLetter().setMailDate(null);
				clearTheCorrespondenceDocuments();
				//For CSR, "Are you working from AWD Object?" pop up should not come, and RIP should generate.
				if(AccountContext.isUserInRole(RoleType.CSR)){
					correspondence.getLetter().setCorrespondenceHasAwdObject(false);
					showAlternateAddressDialog();
				}
			}
			else if (MailType.INCOMING == MailType.valueOf(mailType)) {
				correspondence.getLetter().setMailDate(new Date());
				this.tempSelectedReceivedCorrespondence = this.selectedReceivedCorrespondence;
				this.tempSelectedMissingCorrespondence = this.selectedMissingCorrespondence;
			}
		}
		else if (!StringUtils.hasText(mailType)) {
			clearTheCorrespondenceDocuments();
		}
		generateMailingControlNumber(letterViewModel);
		setLetterTypeOfMail(mailType, letterViewModel);
	}

	/**
	 * Toggle {@link MailType} from outgoing to incoming or blank, clearing the selected received and missing documents.
	 */
	private void clearTheCorrespondenceDocuments() {
		this.tempSelectedReceivedCorrespondence = new CorrespondenceDocument[0];
		this.tempSelectedMissingCorrespondence = new CorrespondenceDocument[0];
	}

	/**
	 * Generates a mailing control number for Outgoing {@link MailType}
	 * @param letterViewModel
	 */
	private void generateMailingControlNumber(
			CorrespondenceLetterViewModel letterViewModel) {
		if(!StringUtils.hasText(letterViewModel.getLetter().getMailingControlNo()) && 
				letterViewModel.getLetter().getId() == null && 
				letterViewModel.getLetter().getMailType() == MailType.OUTGOING){
			letterViewModel.getLetter().setMailingControlNo((new Letter()).getNextMailingControlNumber());
		}else{
			//toggle from Outgoing to Incoming setting controlNo to null
			letterViewModel.getLetter().setMailingControlNo(null);
		}
	}
	
	/**
	 * Loads the {@link LetterCode}s for Incoming {@link MailType} and Outgoing {@link MailType} 
	 * @param selectedValue
	 * @param letterViewModel
	 */
	private void setLetterTypeOfMail(final String mailType,
			CorrespondenceLetterViewModel letterViewModel) {
		if(!StringUtils.hasText(mailType)){letterViewModel.setLetterType(null);}
		else if(MailType.valueOf(mailType) == MailType.INCOMING){
			letterViewModel.setLetterType(LetterType.GENERAL_CORRESPONDENCE);
			//letterViewModel.getLetter().setLetterStatus(null);
		}else{
			letterViewModel.setLetterType(LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER);
		}
	}
	
	
	
	/**
	 * To Load Received {@link CorrespondenceDocument}s for the new correspondence and editing correspondence
	 * @param letterViewModel
	 */
	public void getReceivedCorrespondenceDocuments(CorrespondenceLetterViewModel letterViewModel){
		resetPopups();
		clearSelectedReceivedDocuments(this.selectedReceivedCorrespondence);
		hideAndShowIgoNigoDialogs(true,false);
	}
	
	/**
	 *  To Load Missing {@link CorrespondenceDocument}s for the new correspondence and editing correspondence
	 * @param letterViewModel 
	 */

	public void getMissingCorrespondenceDocuments(CorrespondenceLetterViewModel letterViewModel){
		resetPopups();
		clearSelectedMissingDocuments(this.selectedMissingCorrespondence);
		hideAndShowIgoNigoDialogs(false,true);
	}

	private void hideAndShowIgoNigoDialogs(boolean igoDoc,boolean nigoDoc) {
		Dialog igoDocsDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "igoDocsDialog");
		igoDocsDialog.setVisible(igoDoc);
		Dialog nigoDocsDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "nigoDocsDialog");
		nigoDocsDialog.setVisible(nigoDoc);
	}
	
	/**
	 * To clear the selected received {@link CorrespondenceDocument} from the igoDocsDialog pop up
	 * @param documents
	 */
	public void clearSelectedReceivedDocuments(CorrespondenceDocument[] documents){
		DataTable table = (DataTable) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "igoDocs");
		table.setSelection(documents);

	}
	
	/**
	 * To clear the selected missing {@link CorrespondenceDocument} from the nigoDocsDialog pop up
	 * @param documents
	 */
	public void clearSelectedMissingDocuments(CorrespondenceDocument[] documents){
		DataTable table = (DataTable) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "nigoDocs");
		table.setSelection(documents);

	}

	/**
	 * Method to populate the selected  {@link CorrespondenceDocument}s as Received documents
	 * @return
	 */
	public boolean updateReceivedDocumentsOnSelectedCorrespondence() {
		this.tempSelectedReceivedCorrespondence = this.selectedReceivedCorrespondence;
		return true;
	}

	/**
	 *  Method to populate the selected {@link CorrespondenceDocument}s as Missing documents
	 * @return boolean
	 */
	public boolean updateMissingDocumentsOnSelectedCorrespondence() {
		this.tempSelectedMissingCorrespondence = this.selectedMissingCorrespondence;
		return true;
	}
	
	
	/**
	 * When the user creates an Outgoing letter, he/she is prompted �Are you working from an AWD Work Object�?
	 * If the user answers �No� then the letter will RIP to AWD
	 * @return
	 */
	public boolean hasAWDObject() {
		getNewCorrespondence().getLetter().setCorrespondenceHasAwdObject(false);
		hideAwdDialog();
		showAlternateAddressDialog();
		return true;
	}
	
	/**
	 * After AWD process<p>
	 * The user should then be prompted with �Add Alternate Addressee?� 
	 */
	private void showAlternateAddressDialog() {
		Dialog alternateAddressesDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "alternateAddressesDialog");
		alternateAddressesDialog.setVisible(true);
	}

	/**
	 * When the user creates an Outgoing letter, he/she is prompted �Are you working from an AWD Work Object�?
	 * If the user answers �Yes� then the letter will not RIP
	 * @return
	 */
	public boolean hasNoAWDObject() {
		getNewCorrespondence().getLetter().setCorrespondenceHasAwdObject(true);
		hideAwdDialog();
		showAlternateAddressDialog();
		return true;
	}
	
	/**
	 * copying the claimant mailing address and registrations<p>
	 *  
	 * @return
	 */
	public boolean loadAddress(){
		claimant.getMailingAddress().copyTo(address);
		claimant.getClaimantRegistration().getLines().copyTo(registration);
		return true;
	}
	
	/**
	 * Saving the one time mailing address of Alternate Recipient<p> 
	 * and updating the account activity section 
	 * @return
	 */
	public boolean saveAddress(){
		MailObjectAddress mailAddress = new MailObjectAddress();
		RegistrationLines registrationLines = new RegistrationLines();
		address.copyTo(mailAddress);
		registration.copyTo(registrationLines);
		this.newCorrespondence.setAddress(mailAddress);
		this.newCorrespondence.setRegistration(registrationLines);
		if(!claimant.getMailingAddress().getAddressAsString().equals(this.newCorrespondence.getAddress().getAddressAsString())){
			newCorrespondence.getAddress().setAddressType(AddressType.ONE_TIME_MAILING);
			this.newCorrespondence.getLetter().setAddress(this.newCorrespondence.getAddress());
			this.newCorrespondence.getLetter().setPayToRegistration(this.newCorrespondence.getRegistration());
		}
		return true;
	}
	
	/**
	 * If the user does change the RPO status to Forwardable or Non- Forwardable, the RPO date should default to the current date.<p>  
	 * The user can however change the date if necessary
	 * @param rpoStatus
	 */
	public void onChangeRPOStatus(String rpoStatus){
		resetPopups();
		if("FORWARDABLE".equals(rpoStatus) || "NONFORWARDABLE".equals(rpoStatus)){
			getNewCorrespondence().getLetter().setRpoDate(new Date());
		}else{
			getNewCorrespondence().getLetter().setRpoDate(null);
		}
	}
	
	/**
	 * Provides the visibility of  extra columns like Audit,Special Pull etc.. in Mailing Activity table<p>
	 * And Replaces the Expand button with Collapse
	 * @return
	 */
	public boolean expand() {
		resetPopups();
		setExpandFlag(true);
		setCollapseFlag(false);
		return true;
	}

	/**
	 * Removes the visibility of  extra columns like Audit,Special Pull etc.. in Mailing Activity table<p>
	 * And Replaces the Collapse button with Expand
	 * @return
	 */
	public boolean collapse() {
		resetPopups();
		setExpandFlag(false);
		setCollapseFlag(true);
		return true;
	}
	
	private void hideAwdDialog() {
		Dialog awdDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "awdDialog");
		awdDialog.setVisible(false);
	}
	
	/**
	 * To clear all the popup's in UI for every action.
	 */
	private void resetPopups(){
		Dialog igoDocsDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "igoDocsDialog");
		igoDocsDialog.setVisible(false);
		Dialog nigoDocsDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "nigoDocsDialog");
		nigoDocsDialog.setVisible(false);
		Dialog alternateAddressesDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "alternateAddressesDialog");
		alternateAddressesDialog.setVisible(false);
		Dialog awdDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "awdDialog");
		awdDialog.setVisible(false);
	}
	
	/**
	 * To clear all the documents received and missing
	 */
	private void resetValues(){
		this.selectedReceivedCorrespondence = new CorrespondenceDocument[0];
		this.selectedMissingCorrespondence = new CorrespondenceDocument[0];
		clearTheCorrespondenceDocuments();
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

	/**
	 * @return the newCorrespondence
	 */
	public CorrespondenceLetterViewModel getNewCorrespondence() {
		return newCorrespondence;
	}

	/**
	 * @param newCorrespondence the newCorrespondence to set
	 */
	public void setNewCorrespondence(CorrespondenceLetterViewModel newCorrespondence) {
		this.newCorrespondence = newCorrespondence;
	}

	/**
	 * @return the correspondenceList
	 */
	public List<CorrespondenceLetterViewModel> getCorrespondenceList() {
		return correspondenceList;
	}

	/**
	 * @param correspondenceList the correspondenceList to set
	 */
	public void setCorrespondenceList(
			List<CorrespondenceLetterViewModel> correspondenceList) {
		this.correspondenceList = correspondenceList;
	}

	/**
	 * Loads all the {@link CorrespondenceDocument}s <p>
	 * Any {@link CorrespondenceDocument}s selected as received documents should not appear in missing documents list.<p>
	 * Any {@link CorrespondenceDocument}s selected as missing documents should not appear in received documents list.
	 * @return the correspondenceDocumentList
	 */
	public CorrespondenceDocumentDataModel getCorrespondenceReceivedDocumentList() {
			List<CorrespondenceDocument> correspondenceDocuments = CorrespondenceDocument.findCorrespondenceDocuments(null);
			if(this.tempSelectedMissingCorrespondence != null ){
				prepareDocmentsRecivedAndMissing(correspondenceDocuments,this.tempSelectedMissingCorrespondence);
			}
			correspondenceDocumentList = new CorrespondenceDocumentDataModel(correspondenceDocuments);
			
		return correspondenceDocumentList;
	}
	
	public CorrespondenceDocumentDataModel getCorrespondenceMissingDocumentList() {
			List<CorrespondenceDocument> correspondenceDocuments = CorrespondenceDocument.findCorrespondenceDocuments(null);
			if(this.tempSelectedReceivedCorrespondence != null){
				prepareDocmentsRecivedAndMissing(correspondenceDocuments,this.tempSelectedReceivedCorrespondence);
			}
			correspondenceDocumentList = new CorrespondenceDocumentDataModel(correspondenceDocuments);
			
		return correspondenceDocumentList;
	}
	
	private void prepareDocmentsRecivedAndMissing(List<CorrespondenceDocument> correspondenceDocuments,CorrespondenceDocument[] selectedDocuments) {
		List<CorrespondenceDocument> selectedRecDocs = Arrays.asList(selectedDocuments);
		for(CorrespondenceDocument obj : selectedRecDocs){
			correspondenceDocuments.remove(obj);
		}
	}

	/**
	 * Saves the correspondences with the corresponding status.
	 * @param messageContext
	 * @param claimant
	 * @return
	 */
	@Transactional
	public boolean saveCorrespondence(final MessageContext messageContext,
			final Claimant claimant) {
		resetPopups();
		//adding selected documents to original list
		addSelectedDocsToOriginalIgoList(newCorrespondence.getLetter(),this.tempSelectedReceivedCorrespondence);
		addSelectedDocsToOriginalNigoList(newCorrespondence.getLetter(),this.tempSelectedMissingCorrespondence);
		List<Letter> letterList = new ArrayList<Letter>();
		if(!validateCorrespondence(newCorrespondence,messageContext)){
			return false;
		}
		if(!canEdit){
			this.getCorrespondenceList().add(newCorrespondence);
		}
		if (this.getCorrespondenceList() != null && !this.getCorrespondenceList().isEmpty()) {
			
			for (CorrespondenceLetterViewModel letterVo : this.getCorrespondenceList()) {
				if( ( letterVo.getLetter().getLetterStatus() == null ) && letterVo.getLetter().getMailType() == MailType.OUTGOING
						&& (letterVo.getLetter().getDescription() == null || !StringUtils.hasText(letterVo.getLetter().getDescription())
								|| letterVo.getLetter().getLetterCode() == null) && letterVo.isNew() ){
					letterVo.getLetter().setLetterStatus(LetterStatus.PENDING);
				}
				letterList.add(letterVo.getLetter());
			}
			
			saveCorrespondence(letterList, claimant);
			setEnableAddNewPanel(false);
		}
		return true;
	}

	
	/**
	 * Adding the selected received {@link CorrespondenceDocument}s to the original {@link CorrespondenceDocument}s {@link List}
	 * @param letter
	 * @param selectedDocuments
	 */
	private void addSelectedDocsToOriginalIgoList(Letter letter,CorrespondenceDocument[] selectedDocuments) {
		if(letter.getIgoCorrespondenceDocuments() == null){
			letter.setIgoCorrespondenceDocuments(new HashSet<CorrespondenceDocument>());
		}
		letter.getIgoCorrespondenceDocuments().clear();
		for (CorrespondenceDocument doc : selectedDocuments) {
			letter.getIgoCorrespondenceDocuments().add(doc);
		}
		
	}
	
	/**
	 * Adding the selected missing {@link CorrespondenceDocument}s to the original {@link CorrespondenceDocument}s {@link List}
	 * @param letter
	 * @param selectedDocuments
	 */
	private void addSelectedDocsToOriginalNigoList(Letter letter,CorrespondenceDocument[] selectedDocuments) {
		if(letter.getNigoCorrespondenceDocuments() == null){
			letter.setNigoCorrespondenceDocuments(new HashSet<CorrespondenceDocument>());
		}
		letter.getNigoCorrespondenceDocuments().clear();
		for (CorrespondenceDocument doc : selectedDocuments) {
			letter.getNigoCorrespondenceDocuments().add(doc);
		}
	}
	
	@Transactional
	private void saveCorrespondence(List<Letter> letterList,
			final Claimant claimant) {
		for (Letter letter : letterList) {
			claimant.addLetter(letter);
			if (letter.getId() != null) {
				letter.merge();
			}else{
			letter.persist();
			}
		}
	}
	
	
	/**
	 * Method to validate is there any missing {@link CorrespondenceDocument}s for {@link LetterStatus} IGO to throw error message in UI. <p>
	 * And validate is there any received {@link CorrespondenceDocument}s for {@link LetterStatus} NIGO to throw error message in UI. 
	 * @param letterVo
	 * @param messageContext
	 * @return
	 */
	private boolean validateCorrespondence(CorrespondenceLetterViewModel letterVo,MessageContext messageContext) {
			if (letterVo.getLetter().getLetterStatus() == LetterStatus.IGO
					&& !letterVo.getLetter().getNigoCorrespondenceDocuments().isEmpty()
					&& letterVo.getLetter().getNigoCorrespondenceDocuments() != null) {
				messageContext.addMessage(new MessageBuilder().error().defaultText("IGO Status should not have Missing Documents.").build());
				return false;
			}else if(letterVo.getLetter().getLetterStatus() == LetterStatus.NIGO){
				if( letterVo.getLetter().getIgoCorrespondenceDocuments() != null
						&&!letterVo.getLetter().getIgoCorrespondenceDocuments().isEmpty()
						&& letterVo.getLetter().getNigoCorrespondenceDocuments().isEmpty()){
					messageContext.addMessage(new MessageBuilder().error().defaultText("NIGO Status should have Missing Documents.").build());
					return false;
				}
			}
			if(letterVo.getLetter().getMailType() == null){
				messageContext.addMessage(new MessageBuilder().error().source("form:mailing_type").defaultText("Please fill Mail Type").build());
				return false;
			}
			if(letterVo.getLetter().getLetterCode() == null){
				messageContext.addMessage(new MessageBuilder().error().source("form:claimPanel").defaultText("Please fill Request Type").build());
				return false;
			}
		return true;
	}
	
	
	/**
	 * @param lineSeperator
	 * @return 
	 */
	public String getReceivedCorrespondenceDocumentsAsString(
			final String lineSeperator) {
		if (this.tempSelectedReceivedCorrespondence!= null) {
			return SaecStringUtils.getAsString(newCorrespondence.getLetter()
					.getCorrespondenceDocumentNames(Sets.newHashSet(this.tempSelectedReceivedCorrespondence)), lineSeperator);
		}
		return SaecStringUtils.getAsString(Lists.newArrayList(), lineSeperator);
	}
	
	/**
	 * @param lineSeperator
	 * @return
	 */
	public String getMissingCorrespondenceDocumentsAsString(
			final String lineSeperator) {
		if (this.tempSelectedMissingCorrespondence != null) {
			return SaecStringUtils.getAsString(newCorrespondence.getLetter()
					.getCorrespondenceDocumentNames(Sets.newHashSet(this.tempSelectedMissingCorrespondence)), lineSeperator);
		}
		return SaecStringUtils.getAsString(Lists.newArrayList(), lineSeperator);
	}

	/**
	 * @param correspondenceDocumentList the correspondenceDocumentList to set
	 */
	public void setCorrespondenceDocumentList(
			CorrespondenceDocumentDataModel correspondenceDocumentList) {
		this.correspondenceDocumentList = correspondenceDocumentList;
	}

	/**
	 * @return the correspondenceIncomingNIGOList
	 */
	public CorrespondenceLetterDataModel getCorrespondenceIncomingNIGOList() {
		return correspondenceIncomingNIGOList;
	}

	/**
	 * @param correspondenceIncomingNIGOList the correspondenceIncomingNIGOList to set
	 */
	public void setCorrespondenceIncomingNIGOList(
			CorrespondenceLetterDataModel correspondenceIncomingNIGOList) {
		this.correspondenceIncomingNIGOList = correspondenceIncomingNIGOList;
	}

	/**
	 * @return the selectedReceivedCorrespondence
	 */
	public CorrespondenceDocument[] getSelectedReceivedCorrespondence() {
		return selectedReceivedCorrespondence;
	}

	/**
	 * @param selectedReceivedCorrespondence the selectedReceivedCorrespondence to set
	 */
	public void setSelectedReceivedCorrespondence(
			CorrespondenceDocument[] selectedReceivedCorrespondence) {
		this.selectedReceivedCorrespondence = selectedReceivedCorrespondence;
	}

	/**
	 * @return the selectedMissingCorrespondence
	 */
	public CorrespondenceDocument[] getSelectedMissingCorrespondence() {
		return selectedMissingCorrespondence;
	}

	/**
	 * @param selectedMissingCorrespondence the selectedMissingCorrespondence to set
	 */
	public void setSelectedMissingCorrespondence(
			CorrespondenceDocument[] selectedMissingCorrespondence) {
		this.selectedMissingCorrespondence = selectedMissingCorrespondence;
	}

	/**
	 * @return the expandFlag
	 */
	public boolean isExpandFlag() {
		return expandFlag;
	}

	/**
	 * @param expandFlag the expandFlag to set
	 */
	public void setExpandFlag(boolean expandFlag) {
		this.expandFlag = expandFlag;
	}

	/**
	 * @return the collapseFlag
	 */
	public boolean isCollapseFlag() {
		return collapseFlag;
	}

	/**
	 * @param collapseFlag the collapseFlag to set
	 */
	public void setCollapseFlag(boolean collapseFlag) {
		this.collapseFlag = collapseFlag;
	}

	/**
	 * @return the canEdit
	 */
	public boolean isCanEdit() {
		return canEdit;
	}

	/**
	 * @param canEdit the canEdit to set
	 */
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	/**
	 * @return the claimant
	 */
	public Claimant getClaimant() {
		return claimant;
	}

	/**
	 * @param claimant the claimant to set
	 */
	public void setClaimant(Claimant claimant) {
		this.claimant = claimant;
	}

	/**
	 * @return the address
	 */
	public MailObjectAddress getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(MailObjectAddress address) {
		this.address = address;
	}

	/**
	 * @return the registration
	 */
	public RegistrationLines getRegistration() {
		return registration;
	}

	/**
	 * @param registration the registration to set
	 */
	public void setRegistration(RegistrationLines registration) {
		this.registration = registration;
	}

	/**
	 * @return the enableAddNewPanel
	 */
	public boolean isEnableAddNewPanel() {
		return enableAddNewPanel;
	}

	/**
	 * @param enableAddNewPanel the enableAddNewPanel to set
	 */
	public void setEnableAddNewPanel(boolean enableAddNewPanel) {
		this.enableAddNewPanel = enableAddNewPanel;
	}

	/**
	 * @return the correspondenceDocumentsList
	 */
	public List<CorrespondenceDocument> getCorrespondenceDocumentsList() {
		return correspondenceDocumentsList;
	}

	/**
	 * @param correspondenceDocumentsList the correspondenceDocumentsList to set
	 */
	public void setCorrespondenceDocumentsList(
			List<CorrespondenceDocument> correspondenceDocumentsList) {
		this.correspondenceDocumentsList = correspondenceDocumentsList;
	}

	
	

}