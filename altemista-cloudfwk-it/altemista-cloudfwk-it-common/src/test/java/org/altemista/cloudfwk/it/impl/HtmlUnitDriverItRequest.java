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


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.altemista.cloudfwk.it.WebDriverItRequest;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Extends SeleniumHQ HtmlUnitDriver to comply with the ItRequest interface
 * (and, hence, this class will implement WebDriverItRequest)
 * @author NTT DATA
 */
public class HtmlUnitDriverItRequest extends HtmlUnitDriver implements WebDriverItRequest {

	/**
	 * Default constructor
	 */
	public HtmlUnitDriverItRequest() {
		super(DesiredCapabilities.htmlUnit());
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.ItRequest#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		
		return this.lastPage().getWebResponse().getStatusCode();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.ItRequest#getHttpHeaders()
	 */
	@Override
	public Map<String, String> getHttpHeaders() {
		
		List<NameValuePair> responseHeaders = this.lastPage().getWebResponse().getResponseHeaders();
		if ((responseHeaders == null) || responseHeaders.isEmpty()) {
			return Collections.emptyMap();
		}
		
		Map<String, String> httpHeaders = new LinkedHashMap<String, String>();
		for (NameValuePair responseHeader : responseHeaders) {
			httpHeaders.put(responseHeader.getName(), responseHeader.getValue());
		}
		return httpHeaders;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.ItRequest#getResponseBody()
	 */
	@Override
	public String getResponseBody() {
		
		return this.getPageSource();
	}

	
}
