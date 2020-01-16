/**
 * 
 */
package org.altemista.cloudfwk.common.model;

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


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.springframework.core.io.InputStreamSource;

import com.google.common.base.Objects;

/**
 * ContentReadable implementation backed by an in-memory byte array.
 * This implementation returns a fresh InputStream on each call.
 * @author NTT DATA
 */
public class ByteArrayContentReadable extends AbstractContent
		implements ContentReadable, InputStreamSource, Serializable {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = -8630688640884191835L;

	/** An empty byte array to avoid NPE when instantiating the InputStream */
	private static final byte[] EMPTY_BUFFER = new byte[0];
	
	/** The byte array */
	private byte[] buffer;
	
	/**
	 * Default constructor
	 */
	public ByteArrayContentReadable() {
		super();
	}
	
	/**
	 * Constructor
	 * @param buffer the byte array
	 */
	public ByteArrayContentReadable(byte[] buffer) {
		super();
		
		this.setBuffer(buffer);
	}
	
	/**
	 * Constructor
	 * @param buffer the byte array
	 * @param contentType the content type
	 * @param path depending on the specific type of source, returns the path, the file name, an URL, etc.
	 */
	public ByteArrayContentReadable(byte[] buffer, String contentType, String path) {
		super(contentType, path);
		
		this.setBuffer(buffer);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.model.ContentReadable#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		
		return new ByteArrayInputStream(Objects.firstNonNull(this.buffer, EMPTY_BUFFER));
	}

	/**
	 * @param buffer the buffer to set
	 */
	public void setBuffer(byte[] buffer) {
		
		this.buffer = (buffer == null) ? null : Arrays.copyOf(buffer, buffer.length);
	}
}
