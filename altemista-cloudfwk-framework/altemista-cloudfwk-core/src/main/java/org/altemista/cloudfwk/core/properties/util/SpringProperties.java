package org.altemista.cloudfwk.core.properties.util;

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


import java.util.Properties;

import org.springframework.core.env.PropertyResolver;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.util.ApplicationContextUtil;

/**
 * Backs up all the properties resolved by Spring so they can be directly read by beans programmatically,
 * without the need to inject the properties.
 * @author NTT DATA
 * @deprecated Use {@link ApplicationContextUtil#getEnvironment()}.{@code getProperty(...)} instead
 */
@Deprecated
public final class SpringProperties {

	/**
	 * Private default constructor (utility class).
	 */
	private SpringProperties() {
		super();
	}

	/**
	 * Searches for the property with the specified key in this property list.
	 * @param key the property key
	 * @return the value in this property list with the specified key value.
	 * @deprecated Use {@link ApplicationContextUtil#getEnvironment()}
	 * .{@link PropertyResolver#getProperty(String) getProperty(key)} instead
	 */
	@Deprecated
	public static String getProperty(String key) {
		
		Assert.notNull(key, "key is null");
		return ApplicationContextUtil.getEnvironment().getProperty(key);
	}

	/**
	 * Searches for the property with the specified key in this property list.
	 * @param key the property key
	 * @param defaultValue a default value.
	 * @return the value in this property list with the specified key value.
	 * @deprecated Use {@link ApplicationContextUtil#getEnvironment()}
	 * .{@link PropertyResolver#getProperty(String, String) getProperty(key, defaultValue)} instead
	 */
	@Deprecated
	public static String getProperty(String key, String defaultValue) {

		Assert.notNull(key, "key is null");
		return ApplicationContextUtil.getEnvironment().getProperty(key, defaultValue);
	}

	/**
	 * Adds the properties to Spring properties.
	 * @param pProperties the properties
	 * @deprecated No longer supported
	 */
	@Deprecated
	public static void putAll(Properties pProperties) {

		// (no-operation method)
	}

	/**
	 * Removes all the properties.
	 * @deprecated No longer supported
	 */
	@Deprecated
	public static void clear() {

		// (no-operation method)
	}
}
