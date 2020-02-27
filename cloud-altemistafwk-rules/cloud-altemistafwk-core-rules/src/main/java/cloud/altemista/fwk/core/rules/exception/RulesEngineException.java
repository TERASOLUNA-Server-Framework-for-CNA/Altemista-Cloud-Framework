
package cloud.altemista.fwk.core.rules.exception;

/*
 * #%L
 * altemista-cloud rules engine
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import cloud.altemista.fwk.common.exception.FrameworkException;

/**
 * The Class RulesEngineException.
 * 
 * @author NTT DATA
 */
public class RulesEngineException extends FrameworkException {
	
	/** serialVersionUID long */
	private static final long serialVersionUID = -2193497331339577339L;

	/**
	 * Creates a new RulesEngineException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public RulesEngineException(String code) {
		super(code);
	}
	
	/**
	 * Creates a new RulesEngineException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	public RulesEngineException(String code, Object[] arguments) {
		super(code, arguments);
	}
	
	/**
	 * Creates a new RulesEngineException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public RulesEngineException(String code, Throwable cause) {
		super(code, cause);
	}
	
	/**
	 * Creates a new RulesEngineException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public RulesEngineException(String code, Object[] arguments, Throwable cause) {
		super(code, arguments, cause);
	}
}
