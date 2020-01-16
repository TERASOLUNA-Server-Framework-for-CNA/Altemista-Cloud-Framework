/**
 * 
 */
package org.altemista.cloudfwk.common.model;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

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


/**
 * Common class for implementing ContentReadable and ContentWritable objects
 * that do not need special treatment to the content type and the path (i.e.: they can be plain Strings).
 * This class does not implements ContentReadable neither ContentWritable.
 * @author NTT DATA
 * @see org.altemista.cloudfwk.common.model.ContentReadable
 * @see org.altemista.cloudfwk.common.model.ContentWritable
 */
public abstract class AbstractContent {

	/** The content type */
	private String contentType;
	
	/** Depending on the specific type of source, returns the path, the file name, an URL, etc. */
	private String path;
	
	/**
	 * Default constructor
	 */
	public AbstractContent() {
		super();
	}
	
	/**
	 * Constructor
	 * @param contentType the content type
	 * @param path depending on the specific type of source, returns the path, the file name, an URL, etc.
	 */
	public AbstractContent(String contentType, String path) {
		super();
		this.contentType = contentType;
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		
		return toStringHelper().toString();
	}
	
	/**
	 * Convenience class to extend ToStringHelper in subclasses
	 * @return ToStringHelper
	 */
	protected ToStringHelper toStringHelper() {
		
		return Objects.toStringHelper(this)
				.add("contentType", this.contentType)
				.add("path", this.path);
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
