/**
 * 
 */
package org.altemista.cloudfwk.common.model;

import java.io.IOException;

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


import java.io.OutputStream;

import com.google.common.base.Objects.ToStringHelper;

/**
 * ContentWritable implementation to write to any OutputStream
 * @author NTT DATA
 */
public class StreamContentWritable extends AbstractContent implements ContentWritable {

	/** OutputStream that can be used to write the contents to the file. */
	private OutputStream outputStream;
	
	/**
	 * Default constructor
	 */
	public StreamContentWritable() {
		super();
	}

	/**
	 * Constructor
	 * @param outputStream that can be used to write the contents to the file
	 */
	public StreamContentWritable(OutputStream outputStream) {
		super();
		this.outputStream = outputStream;
	}
	
	/**
	 * Constructor
	 * @param outputStream that can be used to write the contents to the file
	 * @param contentType the content type
	 * @param path depending on the specific type of source, returns the path, the file name, an URL, etc.
	 */
	public StreamContentWritable(OutputStream outputStream, String contentType, String path) {
		super(contentType, path);
		this.outputStream = outputStream;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.model.ContentWritable#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() {
		return this.outputStream;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.model.ContentWritable#setContentLength(long)
	 */
	@Override
	public void setContentLength(long contentLength) {
		// (unimplemented: optional operation)
	}

	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		
		if (this.outputStream != null) {
			this.outputStream.close();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.model.AbstractContent#toStringHelper()
	 */
	@Override
	public ToStringHelper toStringHelper() {
		
		return super.toStringHelper()
				.add("outputStream", this.outputStream);
	}
	
	/**
	 * @param outputStream the outputStream to set
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
}
