package org.altemista.cloudfwk.common.exception;

/*
 * #%L
 * altemista-cloud common
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


/**
 * Framework exception originated in the common utility project.
 * @author NTT DATA
 */
public class CommonException extends FrameworkException {
	
	/** serialVersionUID long */
	private static final long serialVersionUID = 3124275633333722864L;

	/**
	 * Creates a new CommonException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public CommonException(String code) {
		super(code);
	}
	
	/**
	 * Creates a new CommonException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	public CommonException(String code, Object[] arguments) {
		super(code, arguments);
	}
	
	/**
	 * Creates a new CommonException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public CommonException(String code, Throwable cause) {
		super(code, cause);
	}
	
	/**
	 * Creates a new CommonException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public CommonException(String code, Object[] arguments, Throwable cause) {
		super(code, arguments, cause);
	}
}
