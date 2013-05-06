package com.bfds.saec.web.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.util.StringUtils;

import com.bfds.saec.domain.CorrespondenceDocument;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.RegistrationLines;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;
import com.bfds.saec.util.SaecStringUtils;

public class CorrespondenceLetterViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Letter letter;

	private List<SelectItem> requestTypeList = new ArrayList<SelectItem>();

	private final List<CorrespondenceDocument> igoCorrespondenceDocumentList = new ArrayList<CorrespondenceDocument>();

	private final List<CorrespondenceDocument> nigoCorrespondenceDocumentList = new ArrayList<CorrespondenceDocument>();

	private List<CorrespondenceLetterViewModel> inresponseToList = new ArrayList<CorrespondenceLetterViewModel>();

	private final String initialRequestType;

	private String description;

	private boolean selectedMissingDocs = false;

	private MailObjectAddress address;

	private RegistrationLines registration;

	private Long id;
	
	/**
	 * Created to pass the LetterTye as 'GENERAL_CORRESPONDENCE' for Incoming mail item 
	 * and 'GENERAL_CORRESPONDENCE_CURE_LETTER' for new Outgoing mail item , to show the 
	 * different Letter Codes/Description in Request Type Drop down in UI. 
	 */
	private LetterType letterType;

	public CorrespondenceLetterViewModel(final Letter letter) {
		this.letter = letter;
		this.initialRequestType = letter.getRequestType();
	}
	
	public Letter getLetter() {
		return letter;
	}

	public void setLetter(Letter letter) {
		this.letter = letter;
	}

	public boolean isEditable() {
		boolean editable = false;
		if (LetterStatus.IGO != letter.getLetterStatus() || isNew()) {
			editable = true;
		}
		return editable;
	}

	public boolean canEditMailType(boolean formInEditMode) {
		return this.isNew();
	}

	public boolean canEditMailDate(boolean formInEditMode) {
		return canEditField(formInEditMode);
	}

	public boolean canEditDescription(boolean formInEditMode) {
		return canEditDescAndComments(formInEditMode);
	}

	public boolean canShowDocs(boolean formInEditMode) {
		if (this.getLetter().getInResponseTo() != null) {
			return false;
		}
		if (isNew()) {
			if (this.getLetter().getLetterStatus() == null
					&& this.getLetter().getMailType() == MailType.OUTGOING) {
				return false;
			}
			else if (!this.isNew()
					&& this.getLetter().getMailType() == MailType.INCOMING
					&& !this.getLetter().getIgoCorrespondenceDocuments()
							.isEmpty()) {
				return false;
			}
			else {
				return true;
			}
		}
		else if (isNew()) {
			return true;
		}
		return false;
	}

	public boolean canEditReceivedDocs(boolean formInEditMode) {
		if (!formInEditMode) { // ie Form in Add/View mode.
			if (this.isNew()) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

	/**
	 * To show the Request Type of new {@link Letter} as drill down in UI.<p>
	 * To hide the Request Type of existing {@link Letter} as readonly in UI.
	 * @param formInEditMode
	 * @return
	 */
	public boolean canEditRequestType(boolean formInEditMode) {
		return this.getLetter().getMailType() != null;
	}

	public boolean canEditStatus(boolean formInEditMode) {
		if(isNew() && !formInEditMode){
			return true;
		}else if(!isNew() && this.getLetter().getMailType() == MailType.OUTGOING){
			return false;
		}
		return true;
	}

	
	/**
	 * To show or hide a field in UI, which can be done depending on the parameter passed to the method.
	 * @param formInEditMode
	 * @return
	 */
	private boolean canEditField(boolean formInEditMode) {
		if(isNew() && !formInEditMode){
			return true;
		}else if(!isNew() && formInEditMode && this.getLetter().getMailType() == MailType.OUTGOING){
			return true;
		}
		return false;
	}

	public boolean canEditComments(boolean formInEditMode) {
		// return canEditDescAndComments(formInEditMode);
		if (!formInEditMode) { // ie Form in Add/View mode.
			if (this.isNew()) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if ((this.getLetter().getInResponseTo() != null && this
					.getLetter().getDescription() == null)
					|| (this.getLetter().getInResponseTo() != null && this
							.getLetter().getLetterCode() == null)) { // This is
																		// an
																		// Outgoing
																		// correspondence
																		// in
																		// response
																		// to a
																		// nigo.
				return true;
			}
		}
		return !StringUtils.hasText(initialRequestType)
				&& this.getLetter().getMailType() == MailType.OUTGOING;
	}

	private boolean canEditDescAndComments(boolean formInEditMode) {
		//if (!this.getLetter().getInResponseTo().isEmpty() && this.getLetter().getRequestType() != null && this.getLetter().getDescription() != null) {return false;}
		
		if (this.isNew()
				&& this.getLetter().getMailType() == MailType.OUTGOING
				&& this.getLetter().getLetterStatus() == LetterStatus.NO_FURTHER_ACTION) {
			return false;
		}
		if (!formInEditMode) { // ie Form in Add/View mode.
			if (this.isNew()) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if (this.getLetter().getInResponseTo() != null
					&& this.getLetter().getDescription() == null) { // This is
																	// an
																	// Outgoing
																	// correspondence
																	// in
																	// response
																	// to a
																	// nigo.
				return true;
			}
		}
		return !StringUtils.hasText(initialRequestType)
				&& this.getLetter().getMailType() == MailType.OUTGOING;
	}

	public boolean isAuditEditable(final boolean formInEditMode) {
		return canEditAuditSpecialPullGroupMailCode();
	}

	public boolean isSpecialPullEditable(final boolean formInEditMode) {
		return canEditAuditSpecialPullGroupMailCode();
	}

	public boolean isGroupMailEditable(final boolean formInEditMode) {
		return canEditAuditSpecialPullGroupMailCode();
	}
	
	public boolean isRPOEditable(final boolean formInEditMode) {
		return canEditAuditSpecialPullGroupMailCode();
	}

	private boolean canEditAuditSpecialPullGroupMailCode() {
		if (this.getLetter().getMailType() == MailType.INCOMING) {
			return false;
		}
		else if (this.getLetter().getMailType() == MailType.OUTGOING) {
			return true;
		}
		return false;
	}
	
	public boolean showHideDocsReceivedAndMissing(){
		if (this.getLetter().getMailType() == MailType.INCOMING) {
			return false;
		}
		else if (this.getLetter().getMailType() == MailType.OUTGOING) {
			return true;
		}
		return false;
	}

	public boolean isNew() {
		return letter.getId() == null;
	}

	public boolean isOpen() {
		return LetterStatus.NIGO == letter.getLetterStatus();
	}

	public String getReceivedCorrespondenceDocumentsAsString(
			final String lineSeperator) {
		return SaecStringUtils.getAsString(letter
				.getCorrespondenceDocumentNames(letter
						.getIgoCorrespondenceDocuments()), lineSeperator);
	}

	public String getMissingCorrespondenceDocumentsAsString(
			final String lineSeperator) {
		return SaecStringUtils.getAsString(letter
				.getCorrespondenceDocumentNames(letter
						.getNigoCorrespondenceDocuments()), lineSeperator);
	}

	public List<CorrespondenceDocument> getReceivedCorrespondenceDocumentList() {
		if (letter.getIgoCorrespondenceDocuments() == null) {
			letter.setIgoCorrespondenceDocuments(new HashSet<CorrespondenceDocument>());
		}
		igoCorrespondenceDocumentList.clear();
		igoCorrespondenceDocumentList.addAll(letter
				.getIgoCorrespondenceDocuments());
		return igoCorrespondenceDocumentList;
	}

	public void setReceivedCorrespondenceDocumentList(
			List<CorrespondenceDocument> receivedCorrespondenceDocumentList) {
		if (letter.getIgoCorrespondenceDocuments() == null) {
			letter.setIgoCorrespondenceDocuments(new HashSet<CorrespondenceDocument>());
		}
		letter.getIgoCorrespondenceDocuments().clear();
		letter.getIgoCorrespondenceDocuments().addAll(
				receivedCorrespondenceDocumentList);
	}

	public List<CorrespondenceDocument> getMissingCorrespondenceDocumentList() {
		if (letter.getNigoCorrespondenceDocuments() == null) {
			letter.setNigoCorrespondenceDocuments(new HashSet<CorrespondenceDocument>());
		}
		nigoCorrespondenceDocumentList.clear();
		nigoCorrespondenceDocumentList.addAll(letter
				.getNigoCorrespondenceDocuments());
		return nigoCorrespondenceDocumentList;
	}

	public void setMissingCorrespondenceDocumentList(
			List<CorrespondenceDocument> missingCorrespondenceDocumentList) {

		if (letter.getNigoCorrespondenceDocuments() == null) {
			letter.setNigoCorrespondenceDocuments(new HashSet<CorrespondenceDocument>());
		}
		letter.getNigoCorrespondenceDocuments().clear();
		letter.getNigoCorrespondenceDocuments().addAll(
				missingCorrespondenceDocumentList);
	}
	
	
	/**
	 * To show the mailing control number for outgoing {@link Letter} in UI.<p>
	 * To hide the mailing control number for incoming {@link Letter} in UI.
	 * @param formInEditMode
	 * @return boolean
	 */
	public boolean hasMailingControlNo(final boolean formInEditMode){
		return this.getLetter().getMailType() == MailType.OUTGOING;
	}

	public List<CorrespondenceLetterViewModel> getInresponseToList() {
		return inresponseToList;
	}

	public void setInresponseToList(
			List<CorrespondenceLetterViewModel> inresponseToList) {
		this.inresponseToList = inresponseToList;
	}

	public String getDescription() {
		return description == null ? "" : description;
	}

	public void setDescription(String description) {
		this.description = description == null ? "" : description;
	}

	public boolean isSelectedMissingDocs() {
		return selectedMissingDocs;
	}

	public void setSelectedMissingDocs(boolean selectedMissingDocs) {
		this.selectedMissingDocs = selectedMissingDocs;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	/**
	 * @return the letterType
	 */
	public LetterType getLetterType() {
		return letterType;
	}

	/**
	 * @param letterType the letterType to set
	 */
	public void setLetterType(LetterType letterType) {
		this.letterType = letterType;
	}

	
	
	

}
