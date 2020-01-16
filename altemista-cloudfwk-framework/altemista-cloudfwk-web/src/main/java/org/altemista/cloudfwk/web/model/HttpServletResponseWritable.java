/**
 * 
 */
package org.altemista.cloudfwk.web.model;

/*
 * #%L
 * altemista-cloud web
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.common.model.ContentWritable;
import org.altemista.cloudfwk.web.exception.WebException;

/**
 * ContentWritable implementation to write to an HttpServletResponse
 * @author NTT DATA
 */
public class HttpServletResponseWritable implements ContentWritable {
	
	/** HttpServletResponse that backs up this ContentWritable */
	private HttpServletResponse response;
	
	/** Indicates if the content is to be shown inline or downloaded as an attachment */
	private boolean inline;
	
	/**
	 * Constructor
	 * @param response the HttpServletResponse
	 */
	public HttpServletResponseWritable(HttpServletResponse response) {
		this(response, false);
	}
	
	/**
	 * Constructor
	 * @param response the HttpServletResponse
	 * @param inline if the content is to be shown inline or downloaded as an attachment
	 */
	public HttpServletResponseWritable(HttpServletResponse response, boolean inline) {
		super();
		
		Assert.notNull(response, "The HttpServletResponse is required");
		
		this.response = response;
		this.inline = inline;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.model.ContentWritable#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() {
		
		try {
			return this.response.getOutputStream();
			
		} catch (IOException e) {
			throw new WebException("io_exception", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.model.ContentWritable#setContentType(java.lang.String)
	 */
	@Override
	public void setContentType(String contentType) {
		
		this.response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.model.ContentWritable#setPath(java.lang.String)
	 */
	@Override
	public void setPath(String pPath) {
		
		StringBuilder contentDisposition = new StringBuilder();
		contentDisposition.append(this.inline ? "inline" : "attachment");
		
		String path = StringUtils.trimToNull(pPath);
		if (path != null) {
			contentDisposition.append(String.format("; filename=%s", path));
		}
		
		this.response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.model.ContentWritable#setContentLength(long)
	 */
	@Override
	public void setContentLength(long contentLength) {
		
		this.response.setHeader(HttpHeaders.CONTENT_LENGTH, Long.toString(contentLength));
	}
	
	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		
		this.response.getOutputStream().close();
	}
}
