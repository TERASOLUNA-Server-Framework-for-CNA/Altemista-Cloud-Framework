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


import java.io.InputStream;

import com.google.common.base.Objects.ToStringHelper;

/**
 * ContentReadable implementation backed by an InputStream.
 * This implementation always returns the same InputStream on each call;
 * if a fresh InputStream is to be returned on each call, consider using StreamSourceContentReadable instead.
 * @author NTT DATA
 * @see cloud.altemista.fwk.common.model.StreamSourceContentReadable
 */
public class StreamContentReadable extends AbstractContent implements ContentReadable {

	/** InputStream that can be used to retrieve the contents */
	private InputStream inputStream;
	
	/**
	 * Default constructor
	 */
	public StreamContentReadable() {
		super();
	}

	/**
	 * Constructor
	 * @param inputStream that can be used to retrieve the contents
	 */
	public StreamContentReadable(InputStream inputStream) {
		super();
		this.inputStream = inputStream;
	}
	
	/**
	 * Constructor
	 * @param inputStream that can be used to retrieve the contents
	 * @param contentType the content type
	 * @param path depending on the specific type of source, returns the path, the file name, an URL, etc.
	 */
	public StreamContentReadable(InputStream inputStream, String contentType, String path) {
		super(contentType, path);
		this.inputStream = inputStream;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.ContentReadable#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		return this.inputStream;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.AbstractContent#toStringHelper()
	 */
	@Override
	public ToStringHelper toStringHelper() {
		
		return super.toStringHelper()
				.add("inputStream", this.inputStream);
	}

	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
