/**
 * 
 */
package org.altemista.cloudfwk.common.model;

import java.io.Closeable;

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

/**
 * Common interface for elements that support writing content (with specific content type)
 * @author NTT DATA
 */
public interface ContentWritable extends Closeable {

	/**
	 * @return OutputStream that can be used to write the contents
	 */
	OutputStream getOutputStream();
	
	/**
	 * Sets the content type
	 * @param contentType the content type
	 */
	void setContentType(String contentType);
	
	/**
	 * Depending on the specific type of source, set the file path, the file name, etc. (optional operation)
	 * @param path the path
	 */
	void setPath(String path);
	
	/**
	 * Sets the content length (optional operation)
	 * @param contentLength the content length
	 */
	void setContentLength(long contentLength);

}
