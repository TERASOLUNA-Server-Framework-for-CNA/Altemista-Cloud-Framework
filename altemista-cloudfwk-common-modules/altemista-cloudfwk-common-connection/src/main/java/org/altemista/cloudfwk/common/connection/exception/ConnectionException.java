package org.altemista.cloudfwk.common.connection.exception;

/*
 * #%L
 * altemista-cloud common: connectivity utilities
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.altemista.cloudfwk.common.exception.CommonException;

/**
 * Connectivity exception.
 * @author NTT DATA
 */
public class ConnectionException extends CommonException {

	/** serialVersionUID long */
	private static final long serialVersionUID = 6307377985459946671L;

	/**
	 * Creates a new ConnectionException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public ConnectionException(String code) {
		super(code);
	}
	
	/**
	 * Creates a new ConnectionException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public ConnectionException(String code, Throwable cause) {
		super(code, cause);
	}

}
