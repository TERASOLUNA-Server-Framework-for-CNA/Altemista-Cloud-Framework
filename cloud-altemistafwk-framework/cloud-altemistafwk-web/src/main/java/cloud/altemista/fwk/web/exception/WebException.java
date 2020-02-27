/**
 * 
 */
package cloud.altemista.fwk.web.exception;

/*
 * #%L
 * altemista-cloud web
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import cloud.altemista.fwk.common.exception.FrameworkException;

/**
 * Framework exception originated in the web module.
 * @author NTT DATA
 */
public class WebException extends FrameworkException {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = -7420097819914561048L;

	/**
	 * Creates a new WebException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public WebException(String code) {
		super(code);
	}
	
	/**
	 * Creates a new WebException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	public WebException(String code, Object[] arguments) {
		super(code, arguments);
	}
	
	/**
	 * Creates a new WebException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public WebException(String code, Throwable cause) {
		super(code, cause);
	}
	
	/**
	 * Creates a new WebException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public WebException(String code, Object[] arguments, Throwable cause) {
		super(code, arguments, cause);
	}
}
