package org.altemista.cloudfwk.core.util;

/*
 * #%L
 * altemista-cloud framework core
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.altemista.cloudfwk.common.exception.CommonException;
import org.altemista.cloudfwk.common.util.ResourceUtil;

/**
 * Utility class for environment-aware files.
 * @author NTT DATA
 */
public final class EnvironmentUtil {
	
	/** The name of the property that holds the host name: "environment". */
	public static final String ENVIRONMENT_SYSTEM_PROPERTY = "environment";
	
	/** The default value for the host name. */
	public static final String DEFAULT_ENVIRONMENT = "local";
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentUtil.class);
	
	/** Flag to avoid too much noise in logs */
	private static boolean warnNoCurrentEnvironment = true;
	
	/**
	 * Private default constructor (utility class).
	 */
	private EnvironmentUtil() {
		super();
	}
	
	/**
	 * Returns the host name from the current installation,
	 * as stored in the "environment" system property ("-Denvironment=pro").
	 * If the property is not defined in the system, returns a default value ("local").
	 * @return String with the current host name, or the default host name ("local") if the propety is not defined
	 */
	public static String getCurrentEnvironment() {
		
		if (StringUtils.hasText(System.getProperty(ENVIRONMENT_SYSTEM_PROPERTY))) {
			// (sets flag to warn again if the system property is removed) 
			warnNoCurrentEnvironment = true;
			return System.getProperty(ENVIRONMENT_SYSTEM_PROPERTY);
		}
		
		if (warnNoCurrentEnvironment) {
			LOGGER.info("\"{}\" property not found. Using default value \"{}\"",
					ENVIRONMENT_SYSTEM_PROPERTY, DEFAULT_ENVIRONMENT);
			// (resets flag to avoid too much noise in logs)
			warnNoCurrentEnvironment = false;
		}
		return DEFAULT_ENVIRONMENT;
	}
	
	/**
	 * Given a filename, calculates the filename for the specific environment using the current environment.
	 * @param defaultFilename String with the original filename (e.g.: "classpath:logback/logback.xml")
	 * @return specific environment filename (e.g.: "classpath:logback/logback.local.xml")
	 */
	public static String getFilename(String defaultFilename) {
		
		// (sanity check)
		if (!StringUtils.hasText(defaultFilename)) {
			return defaultFilename;
		}
		
		String strippedFilename = StringUtils.stripFilenameExtension(defaultFilename);
		String currentHostName = getCurrentEnvironment();
		String extension = StringUtils.getFilenameExtension(defaultFilename);
		return String.format(
				(extension == null) ? "%s.%s" : "%s.%s.%s", strippedFilename, currentHostName, extension);
	}

	/**
	 * Given a filename, calculates the filename for the specific environment using the current environment
	 * and then returns a InputStream to the specific environment file (if exists),
	 * the default file (if exists) or null if neither file exist.
	 * @param resourceLocation String with the original resource location (e.g.: "classpath:logback/logback.xml")
	 * @return an input stream for reading from the specific environment resource, or null
	 */
	public static InputStream getInputStream(String resourceLocation) {
		
		// (sanity check)
		if (!StringUtils.hasText(resourceLocation)) {
			return null;
		}
		
		String environmentFilename = getFilename(resourceLocation);
		for (String filename : new String[]{ environmentFilename, resourceLocation } ) {
		
			try {
				return ResourceUtil.getInputStream(filename);
				
			} catch (CommonException ignored) { // NOSONAR
				// (this "resource_not_found" exception is ignored; next file will be tried)
			}
		}
		
		// No InputStream could be returned
		return null;
	}
}
