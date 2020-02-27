/**
 * 
 */
package cloud.altemista.fwk.common.model;

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


import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamSource;
import cloud.altemista.fwk.common.exception.CommonException;

import com.google.common.base.Objects.ToStringHelper;

/**
 * ContentReadable implementation backed by an InputStreamSource.
 * This implementation offers more control over the underlying InputStream than StreamContentReadable. 
 * @author NTT DATA
 * @see cloud.altemista.fwk.common.model.StreamContentReadable
 */
public class StreamSourceContentReadable extends AbstractContent implements ContentReadable, InputStreamSource {
	
	/** InputStreamSource that can be used to retrieve the contents */
	private InputStreamSource inputStreamSource;

	/**
	 * Default constructor
	 */
	public StreamSourceContentReadable() {
		super();
	}
	
	/**
	 * Constructor
	 * @param inputStreamSource that can be used to retrieve the contents
	 */
	public StreamSourceContentReadable(InputStreamSource inputStreamSource) {
		super();
		this.inputStreamSource = inputStreamSource;
	}
	
	/**
	 * Constructor
	 * @param inputStreamSource that can be used to retrieve the contents
	 * @param contentType the content type
	 * @param path depending on the specific type of source, returns the path, the file name, an URL, etc.
	 */
	public StreamSourceContentReadable(InputStreamSource inputStreamSource, String contentType, String path) {
		super(contentType, path);
		this.inputStreamSource = inputStreamSource;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.ContentReadable#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		
		// (sanity check)
		if (this.inputStreamSource == null) {
			return null;
		}
		
		try {
			return this.inputStreamSource.getInputStream();
			
		} catch (IOException e) {
			throw new CommonException("input_stream_source", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.AbstractContent#toStringHelper()
	 */
	@Override
	public ToStringHelper toStringHelper() {
		
		return super.toStringHelper()
				.add("inputStreamSource", this.inputStreamSource);
	}
	
	/**
	 * @return the inputStreamSource
	 */
	public InputStreamSource getInputStreamSource() {
		return inputStreamSource;
	}

	/**
	 * @param inputStreamSource the inputStreamSource to set
	 */
	public void setInputStreamSource(InputStreamSource inputStreamSource) {
		this.inputStreamSource = inputStreamSource;
	}
}
