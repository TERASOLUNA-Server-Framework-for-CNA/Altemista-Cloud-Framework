
package cloud.altemista.fwk.core.mail.exception;

/*
 * #%L
 * altemista-cloud mail server
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import cloud.altemista.fwk.common.exception.FrameworkException;

/**
 * The Class MailServerException.
 * 
 * Exception for MailService module.
 * 
 * @author NTT DATA
 */
public class MailServerException extends FrameworkException {
	
	/** serialVersionUID long */
	private static final long serialVersionUID = -1017827249602312756L;

	/**
	 * Creates a new CoreException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public MailServerException(String code) {
		super(code);
	}
	
	/**
	 * Creates a new CoreException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	public MailServerException(String code, Object[] arguments) {
		super(code, arguments);
	}
	
	/**
	 * Creates a new CoreException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public MailServerException(String code, Throwable cause) {
		super(code, cause);
	}
	
	/**
	 * Creates a new CoreException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public MailServerException(String code, Object[] arguments, Throwable cause) {
		super(code, arguments, cause);
	}
}
