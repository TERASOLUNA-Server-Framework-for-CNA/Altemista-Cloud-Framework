package org.altemista.cloudfwk.core.properties;

/*-
 * #%L
 * altemista-cloud framework core
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

/**
 * Convenience utility class to register the environment-aware property sources in a configurable application context.
 * @author NTT DATA
 * @see EnvironmentAwarePropertySource
 */
public final class EnvironmentAwarePropertiesRegistrar {

	/** Name of the environment-aware property source from the {@link CLASSPATH_LOCATION_PATTERN}: {@value} */
	public static final String CLASSPATH_PROPERTY_SOURCE_NAME = "environmentAwareClasspathProperties";

	/** Name of the optional environment-aware property source from an external location : {@value} */
	public static final String EXTERNAL_PROPERTY_SOURCE_NAME = "environmentAwareExternalProperties";
	
	
	/** The SLF4J logger */
	private static final Logger logger = LoggerFactory.getLogger(EnvironmentAwarePropertiesRegistrar.class);

	
	/** The filename of the properties files to be read */
	private static final String PROPERTIES_FILENAMES = "*.properties";

	/** Location pattern of the properties to be read from the classpath */
	private static final String CLASSPATH_LOCATION = "classpath*:config/properties/" + PROPERTIES_FILENAMES;
	
	/** Name of the system property that optionally contains the external path */
	private static final String EXTERNAL_LOCATION_SYSTEM_PROPERTY_NAME = "external.configuration.path";
	
	/** URL prefix for loading the external location from the file system: {@value} */
	private static final String EXTERNAL_LOCATION_FILE_URL_PREFIX = ResourceUtils.FILE_URL_PREFIX + "//";

	
	/**
	 * Registers the environment-aware property sources in a configurable application context:<ul>
	 * <li>Registers a {@link EnvironmentAwarePropertySource environment-aware property source}
	 * from the classpath location {@link CLASSPATH_LOCATION}</li>
	 * <li>If the system property {@link EXTERNAL_LOCATION_SYSTEM_PROPERTY_NAME} is set,
	 * registers a environment-aware property source from an external location</li>
	 * </ul>
	 * @param applicationContext the application to configure
	 */
	public static void registerEnvironmentAwarePropertySources(ConfigurableApplicationContext applicationContext) {
		
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		
		// Prepends a environment-aware property source from the classpath
		EnvironmentAwarePropertySource classpathPropertySource = environmentAwarePropertySourceFromLocation(
				applicationContext, CLASSPATH_PROPERTY_SOURCE_NAME, CLASSPATH_LOCATION);
		if (classpathPropertySource != null) {
			// (no properties to be read from the default location in the classpath)
			environment.getPropertySources().addFirst(classpathPropertySource);
		}
		
		// Reads the system property that contains the optional external location
		final String externalPath = System.getProperty(EXTERNAL_LOCATION_SYSTEM_PROPERTY_NAME);
		if (StringUtils.isEmpty(externalPath)) {
			// (no external location)
			return;
		}
		
		// (patches the external path to be a resource location)
		String externalLocation = null;
		if (ResourceUtils.isUrl(externalPath)) {
			externalLocation = StringUtils.appendIfMissing(externalPath, "/") + PROPERTIES_FILENAMES;
		} else {
			externalLocation = EXTERNAL_LOCATION_FILE_URL_PREFIX +
					FilenameUtils.separatorsToUnix(FilenameUtils.concat(externalPath, PROPERTIES_FILENAMES));
		}

		// Prepends a environment-aware property source from the external location
		EnvironmentAwarePropertySource externalPropertySource = environmentAwarePropertySourceFromLocation(
				applicationContext, EXTERNAL_PROPERTY_SOURCE_NAME, externalLocation);
		if (externalPropertySource != null) {
			environment.getPropertySources().addFirst(externalPropertySource);
		}
	}
	
	/**
	 * Builds a EnvironmentAwarePropertySource using a location pattern
	 * @param resolver ResourcePatternResolver, such as an ApplicationContext
	 * @param name PropertySource name
	 * @param locationPattern the location pattern to resolve
	 * @return EnvironmentAwarePropertySource from the Resource objects corresponding to the location,
	 * or null if the location pattern did not resolve to any location,
	 * or the Resource objects could not be loaded
	 */
	private static EnvironmentAwarePropertySource environmentAwarePropertySourceFromLocation(
			ResourcePatternResolver resolver, String name, String locationPattern) {
		
		// Builds the PropertiesFactoryBean
		PropertiesFactoryBean propertiesFactoryBean = propertiesFactoryBeanFromLocation(resolver, locationPattern);
		if (propertiesFactoryBean == null) {
			return null;
		}

		// Builds the EnvironmentAwarePropertySource
		return new EnvironmentAwarePropertySource(name, propertiesFactoryBean);
	}
	
	/**
	 * Builds a PropertiesFactoryBean using a location pattern
	 * @param resolver ResourcePatternResolver, such as an ApplicationContext
	 * @param locationPattern the location pattern to resolve
	 * @return PropertiesFactoryBean from the corresponding Resource objects,
	 * or null if the location pattern did not resolve to any location
	 */
	private static PropertiesFactoryBean propertiesFactoryBeanFromLocation(
			ResourcePatternResolver resolver, String locationPattern) {
		
		// Resolves the location into Resources
		Resource[] locations;
		try {
			locations = resolver.getResources(locationPattern);
			if (ArrayUtils.isEmpty(locations)) {
				if (logger.isWarnEnabled()) {
					logger.warn("Could not load properties from " + locationPattern + ": no resources to load");
				}
				return null;
			}
			
		} catch (IOException e) {
			if (logger.isWarnEnabled()) {
				logger.warn("Could not load properties from " + locationPattern + ": " + e.getMessage(), e);
			}
			return null;
		}
			
		// Initializes the PropertiesFactoryBean
		try {
			PropertiesFactoryBean source = new PropertiesFactoryBean();
			source.setLocations(locations);
			source.setIgnoreResourceNotFound(true);
			source.afterPropertiesSet();
			
			return source;
			
		} catch (IOException e) {
			// (should never happen due "ignoreResourceNotFound")
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Private default constructor (non-instanceable class)
	 */
	private EnvironmentAwarePropertiesRegistrar() {
		super();
	}
}
