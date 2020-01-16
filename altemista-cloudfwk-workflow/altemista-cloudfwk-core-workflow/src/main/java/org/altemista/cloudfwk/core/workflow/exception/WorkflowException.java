
package org.altemista.cloudfwk.core.workflow.exception;

/*
 * #%L
 * altemista-cloud workflow
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.altemista.cloudfwk.common.exception.FrameworkException;


/**
 * Unchecked base exception for workflow module and others related to it.
 * @author NTT DATA
 */
public class WorkflowException extends FrameworkException {
	
	/** serialVersionUID long */
	private static final long serialVersionUID = 3124275633333722864L;

	/**
	 * Creates a new WorkflowException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public WorkflowException(String code) {
		super(code);
	}
	
	/**
	 * Creates a new WorkflowException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	public WorkflowException(String code, Object[] arguments) {
		super(code, arguments);
	}
	
	/**
	 * Creates a new WorkflowException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public WorkflowException(String code, Throwable cause) {
		super(code, cause);
	}
	
	/**
	 * Creates a new WorkflowException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public WorkflowException(String code, Object[] arguments, Throwable cause) {
		super(code, arguments, cause);
	}
}
