package com.bfds.saec.web.util;

import java.io.IOException;

import org.springframework.faces.security.FaceletsAuthorizeTagUtils;

import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.reference.RoleType;
import com.bfds.saec.domain.Event;

/**
 * Custom Security Tag library to map the Functions => Roles based on SAEC Authorization Policy.
 * This way, the UI need not be cluttered with multiple roles and role management can be decoupled
 * into one class.
 */
public abstract class SaecFaceletsAuthorizeTagUtils {

    private static final String CAN_SEARCH_ACCOUNT =
            RoleType.CSR.name() + "," +
            RoleType.OPERATIONS.name() + "," +
            RoleType.QUALITY.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.PROJECT_MANAGER.name() + "," +
            RoleType.OUTREACH.name() + "," +
            RoleType.OTHER_PARTIES.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_CREATE_NEW_ACCOUNT =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_RUN_STD_REPORTS =
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.PROJECT_MANAGER.name() + "," +
            RoleType.OUTREACH.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_RUN_ADHOC_REPORTS =
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.PROJECT_MANAGER.name() + "," +
            RoleType.OUTREACH.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_CHANGE_ADDRESS =
            RoleType.CSR.name() + "," +
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_CHANGE_NAME =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_VOID_CHECK =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_STOP_CHECK =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_STOP_LIFT =
    		RoleType.OPERATIONS.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_REISSUE_PAYMENTS =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();
    
    private static final String CAN_REISSUE_CHECK =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();
    
    private static final String CAN_STOP_REPLACE_CHECK =
		    RoleType.CSR.name() + "," +
			RoleType.OPERATIONS.name() + "," +
			RoleType.SUPERVISOR.name() + "," +
			RoleType.OPS_MANAGER.name() + "," +
			RoleType.ADMIN.name();

    private static final String CAN_REVERSE_VOID_STOPS =
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_UPDATE_TIN =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();
    
    private static final String CAN_ADD_ALTERNATE_PAYEEES =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();
    
    private static final String CAN_ADD_SPLIT_PAYMENTS =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();
    
	private static final String CAN_PROCESS_TAX = 
		RoleType.SUPERVISOR.name()
		+ "," + RoleType.OPS_MANAGER.name() + "," + 
		"," + RoleType.OPERATIONS.name() + "," + 
		RoleType.ADMIN.name();
	
    private static final String CAN_DO_WIRE =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.ADMIN.name() + "," +
            RoleType.OPS_MANAGER.name();
    
    private static final String CAN_DO_WIRE_RELEASE =
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_DO_ROF =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.ADMIN.name() + "," +
            RoleType.OPS_MANAGER.name();

    private static final String CAN_RELEASE_SINGLE_CHECK =
            RoleType.QUALITY.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_RELEASE_BULK_CHECK =
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_CREATE_CALL_LOGS =
            RoleType.CSR.name() + "," +
            RoleType.OPERATIONS.name() + "," +
            RoleType.QUALITY.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.PROJECT_MANAGER.name() + "," +
            RoleType.OUTREACH.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_DO_RPO_PROCESS =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_DO_CLAIM_PROCESS =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_DO_OPTOUT_PROCESS =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();

    private static final String CAN_DO_OBJECTIONS =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();
    
    private static final String CAN_MODIFY_NOTES =
            RoleType.CSR.name() + "," +
            RoleType.OPERATIONS.name() + "," +
            RoleType.QUALITY.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.PROJECT_MANAGER.name() + "," +
            RoleType.OUTREACH.name() + "," +
            RoleType.ADMIN.name();
    
    private static final String CAN_REQUEST_OUTGOING_CURE_LETTERS =
            RoleType.CSR.name() + "," +
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();    
            
    private static final String CAN_REQUEST_FULFILLMENT_MAILINGS =
            RoleType.CSR.name() + "," +
            RoleType.OPERATIONS.name() + "," +
            RoleType.QUALITY.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.PROJECT_MANAGER.name() + "," +
            RoleType.OUTREACH.name() + "," +
            RoleType.ADMIN.name();    
    
    private static final String CAN_SETUP_CURE_LETTERS =
            RoleType.OPERATIONS.name() + "," +
            RoleType.SUPERVISOR.name() + "," +
            RoleType.OPS_MANAGER.name() + "," +
            RoleType.ADMIN.name();        
            		
    private static final String CAN_DO_TRANCH_ASSIGNMENT =
            RoleType.ADMIN.name();

    private static final String CAN_DO_TRANCH_MAILINGS =
    		RoleType.PROJECT_MANAGER.name() + "," +
    		RoleType.OPERATIONS.name() ;
    
    private static final String CAN_SETUP_NEW_EVENTS =
            RoleType.ADMIN.name();

    private static final String CAN_CLOSEDOWN_NEW_EVENTS =
            RoleType.ADMIN.name();

    private static final String CAN_SETUP_USER_ACCOUNTS =
            RoleType.ADMIN.name() + "," + 
            RoleType.OPS_MANAGER.name() + "," + 
            RoleType.SUPERVISOR.name() ;
    
    private static final String CAN_DO_CORRESPONDENCE =
        	RoleType.CSR.name();
    
    private static final String CAN_VIEW_CORRESPONDENCE =
    		 RoleType.CSR.name() + "," +	
    		 RoleType.OPERATIONS.name() + "," +
    		 RoleType.SUPERVISOR.name() + "," +
    		 RoleType.OPS_MANAGER.name() + "," +
    		 RoleType.ADMIN.name();  
    
    private static final String CAN_SEE_DAILY_POSITIONS =
    		RoleType.OPERATIONS.name() + "," +
    		RoleType.SUPERVISOR.name() + "," +
    		RoleType.OPS_MANAGER.name() + "," +	
    		RoleType.PROJECT_MANAGER.name() + "," +
    		RoleType.ADMIN.name() + "," +	
            RoleType.QUALITY.name() + "," +
            RoleType.CSR.name();  
    
    
    private static final String CAN_SEE_CLAIMANT_REMINDERS =
    		RoleType.OPERATIONS.name() + "," +
    		RoleType.SUPERVISOR.name() + "," +
    		RoleType.OPS_MANAGER.name() + "," +	
    		RoleType.PROJECT_MANAGER.name() + "," +
    		RoleType.ADMIN.name() + "," +	
            RoleType.QUALITY.name() + "," +
            RoleType.CSR.name(); 
    
    private static final String HIDE_CLAIMANT_REMINDERS = 
    		RoleType.QUALITY.name() + "," +
    		RoleType.CSR.name();
    
    private static final String CAN_SHOW_CLAIMPROOFS =
    		RoleType.OPERATIONS.name() + "," +
    		RoleType.SUPERVISOR.name() + "," +
    		RoleType.OPS_MANAGER.name() + "," +	
    		RoleType.PROJECT_MANAGER.name() + "," +
    		RoleType.ADMIN.name() + "," +	
            RoleType.QUALITY.name() + "," +
            RoleType.CSR.name() + "," +
            RoleType.OTHER_PARTIES.name() + "," +
            RoleType.OUTREACH.name();
    
    private static final String CAN_HIDE_CLAIMPROOFS =
            RoleType.QUALITY.name() + "," +
            RoleType.CSR.name() + "," +
            RoleType.OTHER_PARTIES.name() + "," +
            RoleType.OUTREACH.name();
    
    private static final String CAN_VIEW_CLAIM_DETAILS =
    		RoleType.OPERATIONS.name() + "," +
    		RoleType.SUPERVISOR.name() + "," +
    		RoleType.OPS_MANAGER.name() + "," +	
    		RoleType.PROJECT_MANAGER.name() + "," +
    		RoleType.ADMIN.name() + "," +	
            RoleType.QUALITY.name() + "," +
            RoleType.CSR.name() + "," +
            RoleType.OUTREACH.name();
    
    private static final String CAN_EDIT_CLAIM_DETAILS =
    		RoleType.OPERATIONS.name() + "," +
    		RoleType.SUPERVISOR.name() + "," +
    		RoleType.OPS_MANAGER.name() + "," +	
    		RoleType.PROJECT_MANAGER.name() + "," +
    		RoleType.ADMIN.name();	
     
    private static final String CAN_EDIT_DATA_INTAKE_CLAIM_DETAILS =
    		RoleType.ADMIN.name();
            
    

    public static boolean canSetupCureLetters() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_SETUP_CURE_LETTERS);
    }
    
    public static boolean canRequestOutgoingCureLetters() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_REQUEST_OUTGOING_CURE_LETTERS);
    }
    
    public static boolean canRequestFulfillmentMailings() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_REQUEST_FULFILLMENT_MAILINGS);
    }
    
	 private static final String CAN_UPDATE_BATCH_CONFIG =
            RoleType.ADMIN.name();
   
    public static boolean canSearchAccount() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_SEARCH_ACCOUNT);
    }

    public static boolean canCreateNewAccount() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_CREATE_NEW_ACCOUNT);
    }

    public static boolean canRunStandardReports() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_RUN_STD_REPORTS);
    }

    public static boolean canRunAdhocReports() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_RUN_ADHOC_REPORTS);
    }

    public static boolean canChangeAddress() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_CHANGE_ADDRESS);
    }

    public static boolean canChangeName() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_CHANGE_NAME);
    }

    public static boolean canVoidCheck() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_VOID_CHECK);
    }

    public static boolean canStopCheck() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_STOP_CHECK);
    }

    public static boolean canStopLift() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_STOP_LIFT);
    }

    public static boolean canStopReplaceCheck() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_STOP_REPLACE_CHECK);
    }
    
    public static boolean canReissueCheck() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_REISSUE_CHECK);
    }

    public static boolean canReverseVoidStop() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_REVERSE_VOID_STOPS);
    }

    public static boolean canUpdateTIN() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_UPDATE_TIN);
    }
   
    public static boolean canAddAlternatePayees() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_ADD_ALTERNATE_PAYEEES);
    }
    
    public static boolean canAddSplitPayments() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_ADD_SPLIT_PAYMENTS);
    }
    
	public static boolean canProcessTax() throws IOException {
		return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_PROCESS_TAX);
	}

    public static boolean canDoWire() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_DO_WIRE);
    }
    
    public static boolean canDoWireRelease() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_DO_WIRE_RELEASE);
    }

    public static boolean canDoWireOrCheckRelease(String argPaymentType) throws IOException {
    	if (argPaymentType.equalsIgnoreCase(PaymentType.CHECK.name())) {
    		return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_RELEASE_SINGLE_CHECK);
    	} else if (argPaymentType.equalsIgnoreCase(PaymentType.WIRE.name())) {
    		return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_DO_WIRE_RELEASE);
    	}
    	return false ;
    }

    public static boolean canDoROF() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_DO_ROF);
    }

    public static boolean canReleaseSingleCheck() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_RELEASE_SINGLE_CHECK);
    }

    public static boolean canReleaseBulkCheck() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_RELEASE_BULK_CHECK);
    }

    public static boolean canCreateCallLog() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_CREATE_CALL_LOGS);
    }

    public static boolean canExitCallLog() throws IOException {
        return FaceletsAuthorizeTagUtils.areNotGranted(RoleType.CSR.name());
    }

    public static boolean canDoRpoProcess() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_DO_RPO_PROCESS);
    }

    public static boolean canDoClaimProcess() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_DO_CLAIM_PROCESS);
    }

    public static boolean canDoOptOutProcess() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_DO_OPTOUT_PROCESS);
    }
    
    public static boolean canDoObjections() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_DO_OBJECTIONS);
    }
    
    public static boolean canModifyNotes() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_MODIFY_NOTES);
    }
    
    public static boolean canDoTranchAssignment() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_DO_TRANCH_ASSIGNMENT);
    }

    public static boolean canDoTranchMailings() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_DO_TRANCH_MAILINGS);
    }
    
    public static boolean canReissuePayments() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_REISSUE_PAYMENTS);
    }

    public static boolean canSetupNewEvents() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_SETUP_NEW_EVENTS);
    }

    public static boolean canClosedownNewEvents() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_CLOSEDOWN_NEW_EVENTS);
    }

    public static boolean canSetupUserAccounts() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_SETUP_USER_ACCOUNTS);
    }

	public static boolean canUpdateBatchConfig() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_UPDATE_BATCH_CONFIG);
    }
	
	public static boolean canDoCorrespondence() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_DO_CORRESPONDENCE);
    }
	
	public static boolean canViewCorrespondence() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_VIEW_CORRESPONDENCE);
    }
	
	public static boolean canSeeDailyPosition() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_SEE_DAILY_POSITIONS);
    }

	public static boolean canSeeClaimantReminders() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_SEE_CLAIMANT_REMINDERS);
    }
	
	public static boolean hideClaimantReminders() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(HIDE_CLAIMANT_REMINDERS);
    }
	
	public static boolean canShowClaimProofs() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_SHOW_CLAIMPROOFS);
    }
	
	public static boolean canHideClaimProofs() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_HIDE_CLAIMPROOFS);
    }
	
	public static boolean canViewClaimDetails() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_VIEW_CLAIM_DETAILS);
    }
	
	public static boolean canEditClaimDetails() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_EDIT_CLAIM_DETAILS);
    }
	
	public static boolean canEditDataIntakeClaimDetails() throws IOException {
        return FaceletsAuthorizeTagUtils.areAnyGranted(CAN_EDIT_DATA_INTAKE_CLAIM_DETAILS);
    }
	public static boolean canseeClaimantRemindersByEvent() throws IOException {
		if(Event.getCurrentEvent().getHideReminders()!=null)
		{
			return Event.getCurrentEvent().getHideReminders();
		}
		return false;
	}
}
