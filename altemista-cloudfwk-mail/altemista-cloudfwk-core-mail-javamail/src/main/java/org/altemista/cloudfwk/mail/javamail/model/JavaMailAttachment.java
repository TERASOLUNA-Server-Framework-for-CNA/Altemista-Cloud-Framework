/**
 * 
 */
package org.altemista.cloudfwk.mail.javamail.model;

/*
 * #%L
 * altemista-cloud mail server: JavaMail implementation
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import org.altemista.cloudfwk.core.mail.model.Attachment;

/**
 * Wrapper for attachments that implements DataSource
 * @author NTT DATA
 */
public class JavaMailAttachment implements DataSource {
	
	/** The original attachment */
	private Attachment attachment;
	
	/**
	 * Constructor
	 * @param attachment The original attachment
	 */
	public JavaMailAttachment(Attachment attachment) {
		super();
		this.attachment = attachment;
	}

	/* (non-Javadoc)
	 * @see javax.activation.DataSource#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		
		// Contract of addInline() and addAttachment() of MimeMessageHelper requires a fresh InputStream on each call;_
		// this method is valid for the provided implementations of Attachment (that return a fresh InputStream),
		// but maybe fail if newer implementations of Attachment do not follow this principle 
		return this.attachment.getInputStream();
	}

	/* (non-Javadoc)
	 * @see javax.activation.DataSource#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see javax.activation.DataSource#getContentType()
	 */
	@Override
	public String getContentType() {
		return this.attachment.getContentType();
	}

	/* (non-Javadoc)
	 * @see javax.activation.DataSource#getName()
	 */
	@Override
	public String getName() {
		return this.attachment.getPath();
	}
}
