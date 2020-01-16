
package org.altemista.cloudfwk.core.mail.model;

import java.io.Serializable;

import org.altemista.cloudfwk.core.mail.exception.MailServerException;

/*
 * #%L
 * altemista-cloud mail server
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


/**
 * Possible status for asynchronously sent mails.
 * @author NTT DATA
 */
public class MailStatus implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = -7059988078297455279L;

	/** The mail was successfully sent */
	private final boolean sent;

	/** The exception if the mail was not successfully sent */
	private final MailServerException exception;

	/**
	 * Default constructor (when the mail was successfully sent)
	 */
	public MailStatus() {
		this(null);
	}
	
	/**
	 * Constructor
	 * @param exception MailServerException if the mail was not successfully sent
	 */
	public MailStatus(MailServerException exception) {
		super();
		
		this.sent = exception == null;
		this.exception = exception;
	}
	
	/**
	 * @return the sent
	 */
	public boolean isSent() {
		return sent;
	}

	/**
	 * @return the exception
	 */
	public MailServerException getException() {
		return exception;
	}
}
