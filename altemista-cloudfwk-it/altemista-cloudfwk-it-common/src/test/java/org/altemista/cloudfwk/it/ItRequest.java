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


import java.util.Map;

/**
 * Result of the invocation that allows the test to check the response values
 * @author NTT DATA
 */
public interface ItRequest {
	
	/**
	 * @return the status code of the last response
	 */
	int getStatusCode();
	
	/**
	 * @return the HTTP headers of the last response
	 */
	Map<String, String> getHttpHeaders();
	
	/**
	 * @return the body of the last response
	 */
	String getResponseBody();
}
