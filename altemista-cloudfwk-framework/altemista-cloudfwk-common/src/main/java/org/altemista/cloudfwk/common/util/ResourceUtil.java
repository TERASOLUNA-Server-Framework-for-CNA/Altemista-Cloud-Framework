package org.altemista.cloudfwk.common.util;

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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.altemista.cloudfwk.common.exception.CommonException;

/**
 * Utility methods for resolving resource locations to files in the file system.
 * Adds convenience methods over org.springframework.util.ResourceUtils
 * @author NTT DATA
 */
public final class ResourceUtil {

	/**
	 * Private default constructor (utility class);
	 */
	private ResourceUtil() {
		super();
	}
	
	/**
	 * Resolve the given resource location to a File that exists, is not a directory and can be read.
	 * @param resourceLocation the resource location to resolve: either a
	 * "classpath:" pseudo URL, a "file:" URL, or a plain file path
	 * @return a corresponding File object or null if the file does not exists, is a directory or can't be read
	 */
	public static File getFile(String resourceLocation) {
		
		// (sanity check)
		if (!StringUtils.hasText(resourceLocation)) {
			return null;
		}
		
		try {
			File file = ResourceUtils.getFile(resourceLocation);
			if (file.exists() && (!file.isDirectory()) && file.canRead()) {
				return file;
			}
			return null;
			
		} catch (FileNotFoundException e) { // NOSONAR
			return null;
		}
	}
	
	/**
	 * Opens a connection to the resource in the given resource location.
	 * @param resourceLocation resourceLocation the resource location to resolve:
	 * either a "classpath:" pseudo URL, a "file:" URL, or a plain file path
	 * @return an input stream for reading from resource
	 * @throws CommonException if the resource cannot be resolved or an I/O exception occurs.
	 */
	public static InputStream getInputStream(String resourceLocation) {
		
		try {
			return ResourceUtils.getURL(resourceLocation).openStream();
			
		} catch (IOException e) {
			throw new CommonException("resource_not_found", new Object[]{ resourceLocation }, e);
		}
	}

}
