
package cloud.altemista.fwk.common.monitoring.exception;

/*
 * #%L
 * altemista-cloud monitoring: common interfaces
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import cloud.altemista.fwk.common.exception.FrameworkException;

/**
 * Unchecked base exception for monitoring module and others related to it.
 * @author NTT DATA
 */
public class MonitoringException extends FrameworkException {
	
	/** serialVersionUID long */
	private static final long serialVersionUID = -2754622658999687949L;

	/**
	 * Creates a new MonitoringException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	public MonitoringException(String code) {
		super(code);
	}
	
	/**
	 * Creates a new MonitoringException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	public MonitoringException(String code, Object[] arguments) {
		super(code, arguments);
	}
}
