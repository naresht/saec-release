package com.bfds.saec.domain.reference;

/**
 * The source of an Address change or Name Change.
 * 
 * @see {@link com.bfds.saec.domain.activity.AddressChange}
 * @See {@link com.bfds.saec.domain.activity.NameChange}
 */
public enum ChangeSource {

	/**
	 * An address change due to processing of an InfoAge file.
	 */
	RESEARCH,
	/**
	 * An address change not from InfoAge file, and letter.rpo_type or
	 * payment.rpo_type is 'Forwardable', and is the first address update since
	 * the most recent 'forwardable' rpo_date (letter or payment).
	 */
	FORWARD_RPO,
	/**
	 * An address or name change by an OPs associate and is not 'Forward RPO' as
	 * defined above.
	 */
	CORRESPONDENCE,
	/**
	 * An address change by a CSR.
	 */
	PHONE_CALL,

	/**
	 * An address or name change from the outreach process (alternate payee
	 * would be created) � this would need further discussion as this feature
	 * has yet to be developed.
	 */
	BROKER_OUTREACH,
	/**
	 *  If address or name change due to anything else.
	 */
	OTHER;

}
