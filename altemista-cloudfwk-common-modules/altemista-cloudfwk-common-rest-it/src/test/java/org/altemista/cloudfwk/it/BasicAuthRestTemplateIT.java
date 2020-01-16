/**
 * 
 */
package org.altemista.cloudfwk.it;

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


import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.altemista.cloudfwk.core.rest.security.BasicAuthRestTemplate;
import org.altemista.cloudfwk.it.rest.controller.SecuredServiceController;

/**
 * Tests the web security module through the REST controller
 * @author NTT DATA
 */
public class BasicAuthRestTemplateIT extends AbstractIT {
	
	/** The basicUserTemplate RestTemplate */
	private static final RestTemplate basicUserTemplate = new BasicAuthRestTemplate("user", "user");
	
	/** The basicWrongPasswordTemplate RestTemplate */
	private static final RestTemplate basicWrongPasswordTemplate = new BasicAuthRestTemplate("user", "foo");
	
	/**
	 * No security
	 */
	@Test
	public void testNoSecurity() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(SecuredServiceController.MAPPING + "/no/1"));
		ResponseEntity<String> response = basicUserTemplate.getForEntity(url, String.class);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals("Hello, not secured", response.getBody());
	}

	/**
	 * Basic security
	 */
	@Test
	public void testBasic() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(SecuredServiceController.MAPPING + "/basic/1"));
		ResponseEntity<String> response = basicUserTemplate.getForEntity(url, String.class);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals("Hello, basic", response.getBody());
	}

	/**
	 * Basic security, unauthorized (wrong password)
	 */
	@Test(expected = RestClientException.class) // Unauthorized
	public void testBasicWrongPassword() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(SecuredServiceController.MAPPING + "/basic/1"));
		basicWrongPasswordTemplate.getForEntity(url, String.class);
	}

	/**
	 * Basic security, forbidden
	 */
	@Test(expected = RestClientException.class) // Forbidden
	public void testBasicForbidden() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(SecuredServiceController.MAPPING + "/basic/2"));
		System.err.println(
				basicUserTemplate.getForEntity(url, String.class)
			);
	}

	/**
	 * Digest security, unauthorized
	 */
	@Test(expected = RestClientException.class) // Unauthorized
	public void testDigest() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(SecuredServiceController.MAPPING + "/digest/1"));
		basicUserTemplate.getForEntity(url, String.class);
	}
}
