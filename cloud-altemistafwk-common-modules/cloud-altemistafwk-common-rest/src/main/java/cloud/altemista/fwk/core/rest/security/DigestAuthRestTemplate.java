
package cloud.altemista.fwk.core.rest.security;

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
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.BasicHttpContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * DigestAuthRestTemplate is RestClientTemplate for Digest Authentication.
 * This class could be assigned to user session and re-used until user session is valid.
 * Or used as singleton when client application uses only one Credential object to authenticate against REST services.
 * @author NTT DATA
 */
public class DigestAuthRestTemplate extends RestTemplate {
	
	/** The set of credentials */
	private final Credentials credentials;
	
	/**
	 * Constructor with a set of credentials.
	 * @param credentials the set of credentials, consisting of a security principal and a secret (password)
	 */
	public DigestAuthRestTemplate(Credentials credentials) {
		super();
		
		Assert.notNull(credentials, "The credentials must not be null");
		
		this.credentials = credentials;
		this.setErrorHandler(new DigestResponseErrorHandler(new DefaultResponseErrorHandler()));
	}

	/**
	 * Convenience constructor from user name and password; will use the same credentials for all requests.
	 * @param username the user name
	 * @param password the password
	 */
	public DigestAuthRestTemplate(String username, String password) {
		super();

		Assert.hasText(username, "The user name must not be null nor empty");
		
		this.credentials = new UsernamePasswordCredentials(username, password);
		this.setErrorHandler(new DigestResponseErrorHandler(new DefaultResponseErrorHandler()));
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.client.RestTemplate#setErrorHandler(org.springframework.web.client.ResponseErrorHandler)
	 */
	@Override
	public void setErrorHandler(ResponseErrorHandler errorHandler) {
		Assert.notNull(errorHandler, "The ResponseErrorHandler must not be null");
		
		// Ensures a DigestErrorHandlerWrapper will be the error handler
		if (errorHandler instanceof DigestResponseErrorHandler) {
			super.setErrorHandler(errorHandler);
			
		} else {
			super.setErrorHandler(new DigestResponseErrorHandler(errorHandler));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.client.RestTemplate#doExecute(java.net.URI, org.springframework.http.HttpMethod, org.springframework.web.client.RequestCallback, org.springframework.web.client.ResponseExtractor)
	 */
	@Override
	protected <T> T doExecute(URI url, HttpMethod method, RequestCallback pRequestCallback,
			ResponseExtractor<T> responseExtractor) {
		
		try {
			return super.doExecute(url, method, pRequestCallback, responseExtractor);
			
		} catch (UnauthorizedException unauthorizedException) { // NOSONAR
			
			try {
				// create solution by the new challenge
				Header solutionHeader = this.createSolution(url, method, unauthorizedException.getChallenge());
				
				// Call the API again with digest headers
				RequestCallback requestCallback = new DigestRequestCallback(pRequestCallback, solutionHeader);
				return super.doExecute(url, method, requestCallback, responseExtractor);
				
			} catch (MalformedChallengeException e) {
				throw new RestClientException(e.getMessage(), e);
				
			} catch (AuthenticationException e) {
				throw new RestClientException(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Creates the digest solution of an authentication challenge
	 * @param url the fully-expanded URL to connect to
	 * @param method the HTTP method to execute (GET, POST, etc.)
	 * @param challenge the challenge of the HTTP 401 Unauthorized response
	 * @return the solution header
	 * @throws MalformedChallengeException if the authentication challenge is malformed
	 * @throws AuthenticationException if authorization string cannot be generated
	 */
	protected Header createSolution(URI url, HttpMethod method, String challenge)
			throws MalformedChallengeException, AuthenticationException {
		
		DigestScheme digestSolutionProvider = new DigestScheme();
		digestSolutionProvider.processChallenge(new BasicHeader(HttpHeaders.WWW_AUTHENTICATE, challenge));
		return digestSolutionProvider.authenticate(credentials,
				new BasicHttpRequest(method.name(), url.getPath()), new BasicHttpContext());
	}

	/**
	 * RequestCallback wrapper that adds the digest solution header to the request headers
	 * @author NTT DATA
	 */
	protected static class DigestRequestCallback implements RequestCallback {
		
		/** The wrapped request callback. */
		private RequestCallback wrapped;

		/** The solution header. */
		private Header solutionHeader;
		
		/**
		 * Constructor
		 * @param solutionHeader the solution header
		 * @param wrapped the wrapped RequestCallback
		 */
		public DigestRequestCallback(RequestCallback wrapped, Header solutionHeader) {
			super();
			
			this.wrapped = wrapped;
			this.solutionHeader = solutionHeader;
		}

		/* (non-Javadoc)
		 * @see org.springframework.web.client.RequestCallback#doWithRequest(org.springframework.http.client.ClientHttpRequest)
		 */
		@Override
		public void doWithRequest(ClientHttpRequest request) throws IOException {
			
			// Adds the solution header to the request headers
			request.getHeaders().add(solutionHeader.getName(), solutionHeader.getValue());

			// Send request to the wrapped RequestCallback
			if (this.wrapped != null) {
				this.wrapped.doWithRequest(request);
			}
		}
	}

	/**
	 * ResponseErrorHandler wrapper that handles the HTTP 401 Unauthorized errors
	 * @author NTT DATA
	 */
	protected static class DigestResponseErrorHandler implements ResponseErrorHandler {
		
		/** The wrapped response error handler. */
		private ResponseErrorHandler wrapped;

		/**
		 * Constructor
		 * @param wrapped the wrapped response error handler
		 */
		public DigestResponseErrorHandler(ResponseErrorHandler wrapped) {
			super();
			
			Assert.notNull(wrapped, "The wrapped ResponseErrorHandler must not be null");
			this.wrapped = wrapped;
		}

		/* (non-Javadoc)
		 * @see org.springframework.web.client.ResponseErrorHandler#hasError(org.springframework.http.client.ClientHttpResponse)
		 */
		@Override
		public boolean hasError(ClientHttpResponse response) throws IOException {
			
			return HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())
					|| this.wrapped.hasError(response);
		}

		/* (non-Javadoc)
		 * @see org.springframework.web.client.ResponseErrorHandler#handleError(org.springframework.http.client.ClientHttpResponse)
		 */
		@Override
		public void handleError(ClientHttpResponse response) throws IOException {

			if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
				for (String challenge : response.getHeaders().get(HttpHeaders.WWW_AUTHENTICATE)) {
					if (challenge != null) {
						throw new UnauthorizedException(challenge);
					}
				}
			}

			this.wrapped.handleError(response);
		}
	}

	/**
	 * Exception thrown by the DigestResponseErrorHandler when HTTP 401 Unauthorized occurs 
	 * @author NTT DATA
	 */
	protected static class UnauthorizedException extends RestClientException {
		
		/** The serialVersionUID long */
		private static final long serialVersionUID = -8910130988417144547L;
		
		/** The challenge of the HTTP 401 Unauthorized response */
		private final String challenge;

		/**
		 * Constructor
		 * @param challenge The challenge of the HTTP 401 Unauthorized response
		 */
		public UnauthorizedException(String challenge) {
			super("unauthorized");
			
			this.challenge = challenge;
		}

		/**
		 * @return the challenge
		 */
		public String getChallenge() {
			return challenge;
		}

	}
}
