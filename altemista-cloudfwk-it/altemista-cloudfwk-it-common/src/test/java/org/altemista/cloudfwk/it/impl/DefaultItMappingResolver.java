/**
 * 
 */
package org.altemista.cloudfwk.it.impl;

/*
 * #%L
 * Integration tests utilities for web applications (.war)
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.altemista.cloudfwk.it.ItMappingResolver;

/**
 * Resolve controller mappings as URIs and complete URLs when testing deployed web applications 
 * @author NTT DATA
 */
@Component
public class DefaultItMappingResolver implements ItMappingResolver {
	
	/** System property that will contain the base URL of the application */
	private static final String INTEGRATION_TEST_URL_SYSTEM_PROPERTY = "integration-test.url";
	
	/** URL separator */
	private static final String SEPARATOR = "/";
	
	/**
	 * Returns an URI for the REST controller with the specific mapping
	 * @param mapping String with the mapping of the REST controller
	 * @return String with the URI inside the application
	 */
	@Override
	public String getUri(String mapping) {
		
		// e.g.: "/mapping"
		return StringUtils.prependIfMissing(mapping, SEPARATOR);
	}
	
	/**
	 * Returns an URL for the deployed application
	 * @param uri String with the URI inside the application
	 * @return String with the URL for the deployed application
	 */
	@Override
	public String getUrl(String uri) {
		
		// e.g.: "http://localhost:8080/altemista-cloudfwk-core-it-1.0.0.RELEASE/mapping"
		String baseUrl = System.getProperty(INTEGRATION_TEST_URL_SYSTEM_PROPERTY);
		return
				StringUtils.removeEnd(baseUrl, SEPARATOR)
				+ SEPARATOR
				+ StringUtils.removeStart(uri, SEPARATOR);
	}
}
