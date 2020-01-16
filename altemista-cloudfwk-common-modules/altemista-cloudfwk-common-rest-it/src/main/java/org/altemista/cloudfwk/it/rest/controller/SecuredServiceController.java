package org.altemista.cloudfwk.it.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

/*
 * #%L
 * altemista-cloud common: REST consumer utilitites integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.altemista.cloudfwk.it.rest.service.SecuredService;

/**
 * REST controller to check the web security module
 * @author NTT DATA
 */
@RestController
@RequestMapping(path = SecuredServiceController.MAPPING, method = RequestMethod.GET)
public class SecuredServiceController {

	/** The mapping of the controller */
	public static final String MAPPING = "/security";

	/** Example service for security integration tests */
	@Autowired
	private SecuredService service;

	/**
	 * BasicAuthRestTemplate that forwards the authentication.
	 * E.g.: if the "user:user" logs in, this is equivalent to <code>new BasicAuthRestTemplate("user", "user")</code>
	 */
	@Autowired
	@Qualifier("forwardedCredentialsBasicAuthRestTemplate")
	private RestOperations forwardedCredentialsBasicAuthRestTemplate;

	/**
	 * BasicAuthRestTemplate that forwards the authentication.
	 * E.g.: if the "user:user" logs in, this is equivalent to <code>new DigestAuthRestTemplate("user", "user")</code>
	 */
	@Autowired
	@Qualifier("forwardedCredentialsDigestAuthRestTemplate")
	private RestOperations forwardedCredentialsDigestAuthRestTemplate;

	//
	
	/**
	 * Unsecured mapping, unsecured service
	 * @return Dummy String.
	 */
	@RequestMapping("/no/1")
	public String notSecured() {
		
		return this.service.forEveryone("not secured");
	}

	/**
	 * Unsecured mapping, secured service
	 * @return Dummy String.
	 */
	@RequestMapping("/no/2")
	public String notSecuredAdmin() {
		
		return this.service.onlyForAdminUsers("not secured");
	}

	//
	
	/**
	 * Basic authentication secured mapping
	 * @return Dummy String.
	 */
	@RequestMapping("/basic/1")
	public String basic() {
		
		return this.service.forEveryone("basic");
	}
	
	/**
	 * Basic authentication secured mapping, secured service
	 * @return Dummy String.
	 */
	@RequestMapping("/basic/2")
	public String basicAdmin() {
		
		return this.service.onlyForAdminUsers("basic");
	}

	//
	
	/**
	 * Digest authentication secured mapping
	 * @return Dummy String.
	 */
	@RequestMapping("/digest/1")
	public String digest() {
		
		return this.service.forEveryone("digest");
	}
	
	/**
	 * Digest authentication secured mapping, secured service
	 * @return Dummy String.
	 */
	@RequestMapping("/digest/2")
	public String digestAdmin() {
		
		return this.service.onlyForAdminUsers("digest");
	}
	
	//

	/**
	 * Basic authentication secured mapping that will forward to another basic authentication service
	 * @param request HttpServletRequest
	 * @param redirect Last part of the path to forward the request to
	 * @return Dummy String
	 */
	@RequestMapping("/basic/to/basic/{redirect}")
	public String basicForwardToBasic(HttpServletRequest request, @PathVariable("redirect") String redirect) {
		
		final String url = StringUtils.remove(request.getRequestURL().toString(), "/basic/to");
		
		try {
			ResponseEntity<String> response = forwardedCredentialsBasicAuthRestTemplate.getForEntity(url, String.class);
			return "Response from " + url + ": " + response.getBody();
			
		} catch (RestClientException e) { // NOSONAR
			return "RestClientException from " + url + ": " + e.getMessage();
			
		} catch (Exception e) { // NOSONAR
			return "Exception from " + url + ": " + e.getMessage();
		}
	}

	/**
	 * Basic authentication secured mapping that will forward to a digest based authentication service
	 * @param request HttpServletRequest
	 * @param redirect Last part of the path to forward the request to
	 * @return Dummy String
	 */
	@RequestMapping("/basic/to/digest/{redirect}")
	public String basicForwardToDigest(HttpServletRequest request, @PathVariable("redirect") String redirect) {
		
		final String url = StringUtils.remove(request.getRequestURL().toString(), "/basic/to");

		try {
			ResponseEntity<String> response = forwardedCredentialsDigestAuthRestTemplate.getForEntity(url, String.class);
			return "Response from " + url + ": " + response.getBody();
			
		} catch (RestClientException e) { // NOSONAR
			return "RestClientException from " + url + ": " + e.getMessage();
			
		} catch (Exception e) { // NOSONAR
			return "Exception from " + url + ": " + e.getMessage();
		}
	}
}
