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
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import cloud.altemista.fwk.it.ItRequest;

/**
 * Default implementation of ITRequest that uses SimpleClientHttpRequestFactory
 * @author NTT DATA
 */
public class ClientHttpItRequest implements ItRequest {
	
	/** The status code of the last response */
	private int statusCode;
	
	/** The HTTP headers of the last response */
	private Map<String, String> httpHeaders;
	
	/** The body of the last response */
	private String responseBody;

	/**
	 * Constructor
	 * @param url the URL to navigate to
	 * @throws IOException in case of exception
	 */
	public ClientHttpItRequest(String url) throws IOException {
		super();
		
		ClientHttpResponse response = null;
		try {
			response = new SimpleClientHttpRequestFactory().createRequest(URI.create(url), HttpMethod.GET).execute();
			this.statusCode = response.getRawStatusCode();
			this.httpHeaders = Collections.unmodifiableMap(response.getHeaders().toSingleValueMap());
			
			// (IOException reading the response body do not cause the method to fail)
			try {
				this.responseBody = IOUtils.toString(response.getBody(), Charset.defaultCharset());
			} catch (IOException e) {
				this.responseBody = null;
			}
				
		} finally {
			IOUtils.closeQuietly(response);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.ItRequest#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		
		return this.statusCode;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.ItRequest#getHttpHeaders()
	 */
	@Override
	public Map<String, String> getHttpHeaders() {
		
		return ObjectUtils.defaultIfNull(this.httpHeaders, Collections.<String, String> emptyMap());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.ItRequest#getResponseBody()
	 */
	@Override
	public String getResponseBody() {
		
		return this.responseBody;
	}

}
