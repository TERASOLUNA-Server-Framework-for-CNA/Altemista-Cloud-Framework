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


import java.io.IOException;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Convenience helper that allows different implementations for invoking complete URLs from integration tests.
 * @author NTT DATA
 */
public interface ItRequestProvider<R extends ItRequest> extends InitializingBean, DisposableBean {
	
	/**
	 * Resets the provided ItRequest to its initial state
	 */
	void reset();

	/**
	 * Navigates to an URL 
	 * @param url the URL to navigate to
	 * @return R to allow the test to check the response values
	 * @throws IOException in case of error
	 */
	R invokeUrl(String url) throws IOException;
}
