package com.bfds.saec.web.model;

import java.util.ArrayList;
import java.util.List;

import com.bfds.saec.web.service.ClaimantService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageContext;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Contact;
import com.google.common.collect.Lists;


public class ContactViewModel  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	final static Logger log = LoggerFactory.getLogger(ContactViewModel.class);
	
	private Contact newContact = new Contact(); 
	
	private List<Contact> contactList = new ArrayList<Contact>();
	
	public List<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(final Claimant claimant) {
		Claimant claimant_=Claimant.findClaimant(claimant.getId(), null) ;
		this.contactList = claimant_.getContactsAsList();
	}
	
	public Contact getNewContact() {
		return newContact;
	}
	
	public void setNewContact(Contact newContact) {
		this.newContact = newContact;
	}
	
	public void reinit() {
		newContact = new Contact();
	}
	
	@Transactional
	public boolean saveContacts(final ClaimantViewModel claimantViewModel, final MessageContext messageContext, final ClaimantService claimantService) {
		Claimant c = claimantViewModel.getClaimant().merge();
		c.getPrimaryContact().setPrimaryContactOf(claimantViewModel.getClaimant());
		c.removeContacts(getContactsToRemove(claimantViewModel));
		c.addContacts(getContactsToAdd(claimantViewModel));				
		Claimant.save(c);
		if(log.isInfoEnabled())
			log.info(String.format("Claimant Id : %s is saved with specified contacts : %s",c.getId(),c.getContactsAsList().toString()));		
		return true;
	}

	private List<Contact> getContactsToAdd(final ClaimantViewModel claimantViewModel) {
		List<Contact> ret = Lists.newArrayList();
		for(Contact c2 : contactList) {
			if(c2.getId() == null) {
				ret.add(c2);
			}
		}
		if(log.isInfoEnabled()) 
			log.info(String.format("Contacts added : %s",ret.toString()));		
		return ret;
	}
	
	private List<Contact> getContactsToRemove(final ClaimantViewModel claimantViewModel) {
		List<Contact> ret = Lists.newArrayList();
		for(Contact c1 : claimantViewModel.getClaimant().getContacts()) {
			boolean delete = true;
			for(Contact c2 : contactList) {
				if(c1.getId().equals(c2.getId())) {
					delete = false;
					break;
				}
			}
			if(delete) {
				ret.add(c1);
			}
		}
		if(log.isInfoEnabled())
			log.info(String.format("Contacts deleted: %s",ret.toString()));		
		return ret;
	}		
}
