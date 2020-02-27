/**
 * 
 */
package cloud.altemista.fwk.it.impl;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cloud.altemista.fwk.it.ItRequestProvider;
import cloud.altemista.fwk.it.WebDriverItRequest;

/**
 * Alternative implementation of ItRequestProvider that uses WebDriverItRequest
 * @author NTT DATA
 */
public class HtmlUnitDriverItRequestProvider implements ItRequestProvider<WebDriverItRequest> {

	/** The SLF4J logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlUnitDriverItRequestProvider.class);
	
	/** Both main SeleniumHQ interface and ItRequest */
	private WebDriverItRequest webDriver;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		this.reset();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.ItRequestProvider#reset()
	 */
	@Override
	public void reset() {
		
		if (this.webDriver != null) {
			this.webDriver.quit();
			this.webDriver = null;
		}
		
		// (disables javascript because HtmlUnitDriver fails if jquery-2.x.x is present)
		HtmlUnitDriverItRequest htmlUnitDriver = new HtmlUnitDriverItRequest();
		htmlUnitDriver.setJavascriptEnabled(false);
		
		this.webDriver = htmlUnitDriver;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.ItRequestProvider#invokeUrl(java.lang.String)
	 */
	@Override
	public WebDriverItRequest invokeUrl(String url) throws IOException {
		
		LOGGER.debug("Getting {}", url);
		this.webDriver.navigate().to(url);
		return this.webDriver;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		
		if (this.webDriver != null) {
			this.webDriver.quit();
		}
	}

}
