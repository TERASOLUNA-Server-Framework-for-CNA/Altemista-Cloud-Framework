package org.altemista.cloudfwk.core.rest.security;

/*
 * #%L
 * altemista-cloud common: REST consumer utilitites
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Collections;

import org.apache.http.auth.Credentials;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * BasicAuthRestTemplate is RestTemplate that uses Basic Authentication.
 * This class could be assigned to user session and re-used until user session is valid.
 * Or used as singleton when client application uses only one Credential object to authenticate against REST services.
 * @author NTT DATA
 */
public class BasicAuthRestTemplate extends RestTemplate {
	
	/**
	 * Constructor with a set of credentials.
	 * @param credentials the set of credentials, consisting of a security principal and a secret (password)
	 */
	public BasicAuthRestTemplate(Credentials credentials) {
		super();
		
		this.setClientHttpRequestInterceptor(new BasicClientHttpRequestInterceptor(credentials));
	}

	/**
	 * Convenience constructor from user name and password; will use the same credentials for all requests.
	 * @param username the user name
	 * @param password the password
	 */
	public BasicAuthRestTemplate(String username, String password) {
		super();
		
		this.setClientHttpRequestInterceptor(new BasicClientHttpRequestInterceptor(username, password));
	}

	/**
	 * Sets the ClientHttpRequestInterceptor into the request factory of this RestTemplate
	 * @param clientHttpRequestInterceptor the ClientHttpRequestInterceptor to set
	 */
	private void setClientHttpRequestInterceptor(ClientHttpRequestInterceptor clientHttpRequestInterceptor) {
		
		this.setRequestFactory(new InterceptingClientHttpRequestFactory(this.getRequestFactory(),
				Collections.singletonList(clientHttpRequestInterceptor)));
	}
}
