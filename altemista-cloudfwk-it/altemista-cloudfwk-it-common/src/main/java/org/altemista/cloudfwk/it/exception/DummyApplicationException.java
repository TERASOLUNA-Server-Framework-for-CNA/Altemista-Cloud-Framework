package org.altemista.cloudfwk.it.exception;

/*
 * #%L
 * altemista-cloud common: integration tests common utilities
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.altemista.cloudfwk.common.exception.ApplicationException;

/**
 * Dummy application exception for use in integration tests
 * @author NTT DATA
 */
public class DummyApplicationException extends ApplicationException {

	/** serialVersionUID long */
	private static final long serialVersionUID = -2703303937606721884L;

	/** The code of the DummyApplicationException */
	public static final String DEFAULT_CODE = "dummy";

	/**
	 * Constructor
	 */
	public DummyApplicationException() {
		this(DEFAULT_CODE);
	}

	/**
	 * Constructor
	 * @param code the code to be used to resolve the message of this exception
	 */
	public DummyApplicationException(String code) {
		super(code);
	}

	/**
	 * Constructor
	 * @param cause the cause 
	 */
	public DummyApplicationException(Throwable cause) {
		this(DEFAULT_CODE, cause);
	}

	/**
	 * Constructor
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	public DummyApplicationException(String code, Throwable cause) {
		super(code, cause);
	}
}
