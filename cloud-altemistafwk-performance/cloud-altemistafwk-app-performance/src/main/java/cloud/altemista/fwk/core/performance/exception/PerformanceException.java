/**
 * 
 */
package cloud.altemista.fwk.core.performance.exception;

/*
 * #%L
 * altemista-cloud performance: execution performance statistics
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import cloud.altemista.fwk.common.exception.FrameworkException;

/**
 * Exception class for the performance statistics module
 * @author NTT DATA
 */
public class PerformanceException extends FrameworkException {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = -1314908482959772098L;

	/**
	 * Creates a new PerformanceException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public PerformanceException(String code) {
		super(code);
	}
	
	/**
	 * Creates a new PerformanceException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	public PerformanceException(String code, Object[] arguments) {
		super(code, arguments);
	}
	
	/**
	 * Creates a new PerformanceException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public PerformanceException(String code, Throwable cause) {
		super(code, cause);
	}
	
	/**
	 * Creates a new PerformanceException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public PerformanceException(String code, Object[] arguments, Throwable cause) {
		super(code, arguments, cause);
	}
}
