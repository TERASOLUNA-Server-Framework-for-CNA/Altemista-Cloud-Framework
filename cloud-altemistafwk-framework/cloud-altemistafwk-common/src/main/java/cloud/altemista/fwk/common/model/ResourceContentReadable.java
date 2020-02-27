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
import java.io.Serializable;

import org.springframework.core.io.InputStreamSource;
import cloud.altemista.fwk.common.util.ResourceUtil;

import com.google.common.base.Objects;

/**
 * ContentReadable implementation backed by a Spring Resource.
 * This implementation returns a fresh InputStream on each call.
 * @author NTT DATA
 */
public class ResourceContentReadable implements ContentReadable, InputStreamSource, Serializable {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = 1678866397751737044L;
	
	/** The resource location: either a "classpath:" pseudo URL, a "file:" URL, or a plain file path */
	private String resourceLocation;

	/** The content type. */
	private String contentType;
	
	/**
	 * Default constructor
	 */
	public ResourceContentReadable() {
		super();
	}
	
	/**
	 * Constructor
	 * @param resourceLocation the resource location: either a "classpath:" pseudo URL, a "file:" URL, or a plain file path
	 */
	public ResourceContentReadable(String resourceLocation) {
		super();
		this.resourceLocation = resourceLocation;
	}
	
	/**
	 * Constructor
	 * @param resourceLocation the resource location: either a "classpath:" pseudo URL, a "file:" URL, or a plain file path
	 * @param contentType the content type
	 */
	public ResourceContentReadable(String resourceLocation, String contentType) {
		super();
		this.resourceLocation = resourceLocation;
		this.contentType = contentType;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.ContentReadable#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		
		return ResourceUtil.getInputStream(this.resourceLocation);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.ContentReadable#getContentType()
	 */
	@Override
	public String getContentType() {
		return contentType;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.ContentReadable#getPath()
	 */
	@Override
	public String getPath() {
		return resourceLocation;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.StreamContentReadable#toString()
	 */
	@Override
	public String toString() {
		
		return Objects.toStringHelper(this)
				.add("contentType", this.contentType)
				.add("resourceLocation", this.resourceLocation)
				.toString();
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the resourceLocation
	 */
	public String getResourceLocation() {
		return resourceLocation;
	}

	/**
	 * @param resourceLocation the resourceLocation to set
	 */
	public void setResourceLocation(String resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

}
