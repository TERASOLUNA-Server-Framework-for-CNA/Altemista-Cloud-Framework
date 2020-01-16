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


import java.io.InputStream;

/**
 * Common interface for elements that offer content (with specific content type) for reading
 * @author NTT DATA
 */
public interface ContentReadable {

	/**
	 * @return InputStream that can be used to retrieve the actual contents
	 */
	InputStream getInputStream();
	
	/**
	 * @return the content type
	 */
	String getContentType();
	
	/**
	 * Depending on the specific type of source, returns the path, the file name, an URL, etc. (optional operation)
	 * @return the path
	 */
	String getPath();

}
