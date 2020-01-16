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


import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.altemista.cloudfwk.it.AbstractIT;
import org.altemista.cloudfwk.it.rest.controller.SecuredServiceController;

/**
 * Tests the web security module through the REST controller
 * @author NTT DATA
 */
public class RestTemplateIT extends AbstractIT {
	
	/** The noUserTemplate RestTemplate */
	private static final RestTemplate noUserTemplate = new RestTemplate();
	
	/**
	 * No security
	 */
	@Test
	public void testNoSecurity() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(SecuredServiceController.MAPPING + "/no/1"));
		ResponseEntity<String> response = noUserTemplate.getForEntity(url, String.class);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals("Hello, not secured", response.getBody());
	}

	/**
	 * No security, service secured
	 */
	@Test
	public void testNoSecurityWithSecuredService() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(SecuredServiceController.MAPPING + "/no/2"));
		ResponseEntity<String> response = noUserTemplate.getForEntity(url, String.class);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertTrue(response.getBody(), StringUtils.containsIgnoreCase(response.getBody(), "login page"));
	}

	/**
	 * Basic security, unauthorized
	 */
	@Test
	public void testBasic() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(SecuredServiceController.MAPPING + "/basic/1"));
		ResponseEntity<String> response = noUserTemplate.getForEntity(url, String.class);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertTrue(response.getBody(), StringUtils.containsIgnoreCase(response.getBody(), "login page"));
	}

	/**
	 * Digest security, unauthorized
	 */
	@Test(expected = RestClientException.class) // Unauthorized
	public void testDigest() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(SecuredServiceController.MAPPING + "/digest/1"));
		noUserTemplate.getForEntity(url, String.class);
	}
}
