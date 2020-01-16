/**
 * 
 */
package org.altemista.cloudfwk.it;

/*
 * #%L
 * altemista-cloud common: integration tests common utilities
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


/**
 * Convenience helper that allows different implementations for resolve controller mappings as URIs and complete URLs.
 * @author NTT DATA
 */
public interface ItMappingResolver {
	
	/**
	 * Returns an URI for the REST controller with the specific mapping
	 * @param mapping String with the mapping of the REST controller
	 * @return String with the URI inside the application
	 */
	String getUri(String mapping);
	
	/**
	 * Returns an URL for the deployed application
	 * @param uri String with the URI inside the application
	 * @return String with the URL for the deployed application
	 */
	String getUrl(String uri);
}
