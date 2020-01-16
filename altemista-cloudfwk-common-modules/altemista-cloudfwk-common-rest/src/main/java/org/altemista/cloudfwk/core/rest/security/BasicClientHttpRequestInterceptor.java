/**
 * 
 */
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


import java.io.IOException;

import org.apache.http.HttpHeaders;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

/**
 * Client-side HTTP requests interceptor that adds basic authentication
 * @author NTT DATA
 */
public class BasicClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
	/** The set of credentials */
	private final Credentials credentials;

	/**
	 * Constructor
	 * @param credentials the set of credentials, consisting of a security principal and a secret (password)
	 */
	public BasicClientHttpRequestInterceptor(Credentials credentials) {
		super();
		
		Assert.notNull(credentials, "The credentials must not be null");
		
		this.credentials = credentials;
	}

	/**
	 * Convenience constructor from an user name and a password
	 * @param username the user name
	 * @param password the password
	 */
	public BasicClientHttpRequestInterceptor(String username, String password) {
		super();

		Assert.hasText(username, "The user name must not be null nor empty");
		
		this.credentials = new UsernamePasswordCredentials(username, password);
	}

	/* (non-Javadoc)
	 * @see org.springframework.http.client.ClientHttpRequestInterceptor#intercept(org.springframework.http.HttpRequest, byte[], org.springframework.http.client.ClientHttpRequestExecution)
	 */
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		
		final byte[] usernameAndPassword = String.format("%s:%s",
				this.credentials.getUserPrincipal().getName(), this.credentials.getPassword()).getBytes("UTF-8");
		final String authorization = String.format("Basic %s", Base64Utils.encodeToString(usernameAndPassword));
		request.getHeaders().add(HttpHeaders.AUTHORIZATION, authorization);
		
		return execution.execute(request, body);
	}

}
