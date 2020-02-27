
package cloud.altemista.fwk.core.batch.exception;

/*
 * #%L
 * altemista-cloud batch
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import cloud.altemista.fwk.common.exception.FrameworkException;

/**
 * Unchecked base exception for batch module and others related to it.
 * 
 * @author NTT DATA
 */
public class BatchException extends FrameworkException {
	
	/** serialVersionUID long */
	private static final long serialVersionUID = 7309573426246125990L;

	/**
	 * Creates a new BatchException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public BatchException(String code) {
		super(code);
	}

	/**
	 * Creates a new BatchException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	public BatchException(String code, Object[] arguments) {
		super(code, arguments);
	}

	/**
	 * Creates a new BatchException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public BatchException(String code, Throwable cause) {
		super(code, cause);
	}

	/**
	 * Creates a new BatchException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public BatchException(String code, Object[] arguments, Throwable cause) {
		super(code, arguments, cause);
	}
}
