

package cloud.altemista.fwk.common.cryptography.exception;

/*
 * #%L
 * altemista-cloud common: cryptography utilities
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import cloud.altemista.fwk.common.exception.FrameworkException;

/**
 * Cryptography related exception originated in the core module.
 * @author NTT DATA
 */
public class CryptographyException extends FrameworkException {
	
	/** serialVersionUID long */
	private static final long serialVersionUID = -8828261743754060986L;

	/**
	 * Creates a new CryptographyException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public CryptographyException(String code) {
		super(code);
	}
	
	/**
	 * Creates a new CryptographyException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	public CryptographyException(String code, Object[] arguments) {
		super(code, arguments);
	}
	
	/**
	 * Creates a new CryptographyException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public CryptographyException(String code, Throwable cause) {
		super(code, cause);
	}
	
	/**
	 * Creates a new CryptographyException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public CryptographyException(String code, Object[] arguments, Throwable cause) {
		super(code, arguments, cause);
	}
}
