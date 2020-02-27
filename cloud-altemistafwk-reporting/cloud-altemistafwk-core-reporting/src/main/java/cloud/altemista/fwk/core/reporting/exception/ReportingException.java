
package cloud.altemista.fwk.core.reporting.exception;

/*
 * #%L
 * altemista-cloud reporting
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import cloud.altemista.fwk.common.exception.FrameworkException;

/**
 * Unchecked base exception for reporting module. Indicates a report-related problem happened.
 * 
 * @author NTT DATA
 */
public class ReportingException extends FrameworkException {
	
	/** serialVersionUID long */
	private static final long serialVersionUID = 893160068926347351L;

	/**
	 * Creates a new ReportException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public ReportingException(String code) {
		super(code);
	}
	
	/**
	 * Creates a new ReportException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	public ReportingException(String code, Object[] arguments) {
		super(code, arguments);
	}
	
	/**
	 * Creates a new ReportException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public ReportingException(String code, Throwable cause) {
		super(code, cause);
	}
	
	/**
	 * Creates a new ReportException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public ReportingException(String code, Object[] arguments, Throwable cause) {
		super(code, arguments, cause);
	}
}
