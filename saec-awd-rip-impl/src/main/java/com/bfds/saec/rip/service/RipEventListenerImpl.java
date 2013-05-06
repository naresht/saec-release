package com.bfds.saec.rip.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bfds.saec.rip.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.User;
import com.bfds.saec.rip.dto.PhoneCallDto;
import com.bfds.saec.rip.integration.RipGateway;
import com.bfds.saec.rip.transformer.AbstractRipObjectTransformer;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

@Service("ripEventListener")
public class RipEventListenerImpl implements RipEventListener, InitializingBean {
	final Logger log = LoggerFactory.getLogger(RipEventListenerImpl.class);
			
	@Autowired
	private PlatformTransactionManager transactionManager;

	private TransactionTemplate txTemplate;	
	
	@Autowired
	private RipGateway ripGateway;
	

	@Override
	@Transactional
	@Async
	public void followupreminderCreated(final FollowupRemindersRipObject ripObject) {
		 sendRip(ripObject);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		checkState(transactionManager != null, "TransactionManager is null");
		txTemplate = new TransactionTemplate(transactionManager);		
	}
	
	@Override
	@Transactional
	@Async
	public void addressChanged(final AddressChangeRipObject ripObject) {		
		processRipObject(ripObject);

	}

	@Override
	@Transactional
	@Async
	public void cureLetterCreated(final CureLetterRipObject ripObject) {
		processRipObject(ripObject);
	}

	@Override
	@Transactional
	@Async
	public void stopReplaceCheckRequested(
			final StopReplaceCheckRipObject ripObject) {
		processRipObject(ripObject);
	}
	
	@Override
	@Transactional
	@Async
	public void sendSpecialHandlingInstructions(
			SpecialhandlingRipObject ripObject) {
		if(isSystemgenerated(ripObject)) {
			// Do not RIP system generated changes.
			return;
		}
		validate(ripObject);
		log.info(format("Received ripObject. Type : %s, corelationId : %s, userId : %s", ripObject.getClass(), ripObject.getCorrelationId(), ripObject.getCreatedByUser()));
		// Just sent the RIP.			
		sendRip(ripObject);
		
	}

    @Override
    //@Transactional
    @Async
    public void sendClaimFormSupportingDocument(ClaimFormSupportingDocRipObject ripObject) {
        sendRip(ripObject);
    }

	private boolean isSystemgenerated(RipObject ripObject) {
		return "system!".equalsIgnoreCase(ripObject.getCreatedByUser());
	}	

	protected void processRipObject(final RipObject ripObject) {
		if(isSystemgenerated(ripObject)) {
			// Do not RIP system generated changes.
			return;
		}
		validate(ripObject);
		log.info(format("Received ripObject. Type : %s, corelationId : %s, userId : %s", ripObject.getClass(), ripObject.getCorrelationId(), ripObject.getCreatedByUser()));
		if (userIsCSR(ripObject.getCreatedByUser())) {
			// Just sent the RIP.			
			sendRip(ripObject);
		} else {
			// Save the RIP, might have to send later.			
			saveRip(ripObject);
		}
	}

	private void validate(final RipObject ripObject) {
		checkArgument(
				(StringUtils.hasText(ripObject.getCreatedByUser())),
				format("createdByUser must be provided for RipObject - %s.",
						ripObject.getClass()));
	}

	protected void saveRip(final RipObject ripObject) {
		log.info(format("Saving ripObject. Type : %s, corelationId : %s, userId : %s", ripObject.getClass(), ripObject.getCorrelationId(), ripObject.getCreatedByUser()));
		ripObject.setStatus(RipObjectStatus.NOT_SENT);
		ripObject.persist();
	}

	protected void sendRip(final RipObject ripObject) {		
		log.info(format("Sending ripObject. Type : %s, corelationId : %s, userId : %s", ripObject.getClass(), ripObject.getCorrelationId(), ripObject.getCreatedByUser()));
		getRipGateway().sendRipObject(ripObject);
	}

	protected boolean userIsCSR(final String createdByUser) {
		final User user = User.findUserByName(createdByUser);
		Preconditions.checkState(user != null , String.format("User not found with Id: %s", createdByUser));
		Set<String> roles = Sets.newHashSet();
		roles.addAll(user.getRoleNames());
		Preconditions.checkState(roles.size() > 0 , String.format("User has no roles: %s", createdByUser));
		if(roles.size() > 1) {
			// Could be a CSR, but has other roles as well. 
			return false;
		}
		
		return "csr".equalsIgnoreCase(roles.iterator().next());
	}

	@Override
	@Async
	public void phoneCallReceived(final PhoneCallDto callDto) {
		checkArgument(callDto.getPhoneCallId() != null, "PhoneCall id is null");

		final List<RipObject> ripObjects = RipObject
				.findByCorrelationId(callDto.getPhoneCallId());

		if (ripObjects.size() == 0) {
			return;
		}
		for (final RipObject ripObject : ripObjects) {
			if (ripObject.getStatus() == RipObjectStatus.SENT) { 
				continue;
			}
			falgRipObjectAsSending(ripObject);	
			log.info(format("Sending ripObject. Type : %s, corelationId : %s, userId : %s", ripObject.getClass(), ripObject.getCorrelationId(), ripObject.getCreatedByUser()));
			flagRipObjectAsSent(ripObject);	
			log.info(format("Sent ripObject. Type : %s, corelationId : %s, userId : %s", ripObject.getClass(), ripObject.getCorrelationId(), ripObject.getCreatedByUser()));
		}
	}


    private void flagRipObjectAsSent(final RipObject ripObject) {
		getTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(
					TransactionStatus status) {
				RipEventListenerImpl.this.sendRip(ripObject);
				ripObject.setStatus(RipObjectStatus.SENT);
				ripObject.merge().flush();
			}
		});
	}

	private void falgRipObjectAsSending(final RipObject ripObject) {
		getTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(
					TransactionStatus status) {				
				ripObject.setStatus(RipObjectStatus.SENDING);
				ripObject.merge().flush();
			}
		});
	}

	public  Map<Class<? extends RipObject>, AbstractRipObjectTransformer<?>> getRipbuilders() {
		return null;
	}

	public TransactionTemplate getTxTemplate() {
		return txTemplate;
	}

	public RipGateway getRipGateway() {
		return ripGateway;
	}

	public void setRipGateway(RipGateway ripGateway) {
		this.ripGateway = ripGateway;
	}	

}
