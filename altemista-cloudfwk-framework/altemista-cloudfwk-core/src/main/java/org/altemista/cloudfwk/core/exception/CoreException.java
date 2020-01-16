package org.altemista.cloudfwk.core.exception;

import org.altemista.cloudfwk.common.exception.FrameworkException;

/*
 * #%L
 * altemista-cloud framework core
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


/**
 * Framework exception originated in the core module.
 * @author NTT DATA
 */
public class CoreException extends FrameworkException {
	
	/** serialVersionUID long */
	private static final long serialVersionUID = 3124275633333722864L;

	/**
	 * Creates a new CoreException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public CoreException(String code) {
		super(code);
	}
	
	/**
	 * Creates a new CoreException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	public CoreException(String code, Object[] arguments) {
		super(code, arguments);
	}
	
	/**
	 * Creates a new CoreException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public CoreException(String code, Throwable cause) {
		super(code, cause);
	}
	
	/**
	 * Creates a new CoreException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public CoreException(String code, Object[] arguments, Throwable cause) {
		super(code, arguments, cause);
	}
	
}
