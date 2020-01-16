/**
 * 
 */
package org.altemista.cloudfwk.it.impl;

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

import org.altemista.cloudfwk.it.ItRequestProvider;

/**
 * Default implementation of ItRequestProvider that uses ClientHttpItRequest
 * @author NTT DATA
 */
public class ClientHttpItRequestProvider implements ItRequestProvider<ClientHttpItRequest> {

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.ItRequestProvider#reset()
	 */
	@Override
	public void reset() {
		
		// (nothing to do)
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		// (nothing to do)
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.ItRequestProvider#invokeUrl(java.lang.String)
	 */
	@Override
	public ClientHttpItRequest invokeUrl(String url) throws IOException {
		
		return new ClientHttpItRequest(url);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		
		// (nothing to do)
	}
}
