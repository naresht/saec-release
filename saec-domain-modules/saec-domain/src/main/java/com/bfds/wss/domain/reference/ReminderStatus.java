package com.bfds.wss.domain.reference;

/**
 * The status of a {@link com.bfds.wss.domain.ClaimantReminder}
 */

public enum ReminderStatus {
    PENDING,
    COMPLETE,
    AUTO_RESOLVED,
    NO_ADDRESS_AVAILABLE,
    INSUFFICIENT_AMOUNT;
}
