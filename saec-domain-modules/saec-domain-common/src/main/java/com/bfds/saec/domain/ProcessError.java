package com.bfds.saec.domain;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.reference.ProcessName;

/**
 * Captures process errors across buiness processes. To be used by Services /
 * Batch Programs / Webflow controllers to populate exact error message
 */
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @RooJavaBean
@RooToString
public class ProcessError implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(ProcessError.class);
	// Claimant Account# if any
	private String referenceNo;

	// Date on which erro occured
	private Date date;

	// Process in which error occured
	@Enumerated(EnumType.STRING)
	private ProcessName processName;

	@Enumerated(EnumType.STRING)
	private ErrorSeverity severity;

	@Enumerated(EnumType.STRING)
	private ErrorType errorType;

	private boolean isBatch;

	// Error message
	private String message;

	// Error Exception
	private String exception;
	
	private Long errorId;
	

	public Long getErrorId() {
		return errorId;
	}

	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public static ProcessError warn(String message, ProcessName processName,
			ErrorType errorType, boolean isBatch, Throwable e) {

		return persistError(message, processName, errorType,
				ErrorSeverity.WARN, isBatch, e);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public static ProcessError fatal(String message, ProcessName processName,
			ErrorType errorType, boolean isBatch, Throwable e) {

		return persistError(message, processName, errorType,
				ErrorSeverity.FATAL, isBatch, e);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public static ProcessError validation(String message,
			ProcessName processName, ErrorType errorType, boolean isBatch,
			Throwable e) {

		return persistError(message, processName, errorType,
				ErrorSeverity.VALIDATION, isBatch, e);
	}

	private static ProcessError persistError(String message,
			ProcessName processName, ErrorType errorType,
			ErrorSeverity errorSeverity, boolean isBatch, Throwable e) {
		ProcessError error = new ProcessError();
		log.error("Unhandled " + e.getClass().getSimpleName()
				+ ". EXCEPTION unique id:" + e.hashCode(), e);

		error.setDate(new Date());
		error.setException(e.getClass().getSimpleName());
		error.setErrorId((long) e.hashCode());
		error.setIsBatch(isBatch);
		error.setMessage(message);
		error.setProcessName(processName);
		error.setSeverity(errorSeverity);
		error.setErrorType(errorType);
		error.persist();
		return error;
	}

	/**
	 * List of Error Types. Currently System or Business Process
	 */
	public enum ErrorType {
		SYSTEM, BUSINESS_PROCESS;
	}

	/**
	 * List of Error Severities
	 */
	public enum ErrorSeverity {
		WARN, VALIDATION, FATAL;
	}

}
