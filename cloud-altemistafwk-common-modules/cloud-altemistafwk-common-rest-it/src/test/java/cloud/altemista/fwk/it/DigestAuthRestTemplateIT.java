/**
 * 
 */
package cloud.altemista.fwk.it;

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
import cloud.altemista.fwk.core.rest.security.DigestAuthRestTemplate;
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.rest.controller.SecuredServiceController;

/**
 * Tests the web security module through the REST controller
 * @author NTT DATA
 */
public class DigestAuthRestTemplateIT extends AbstractIT {
	
	/** The digestWrongPasswordTemplate RestTemplate */
	private static final RestTemplate digestUserTemplate = new DigestAuthRestTemplate("user", "user");
	
	/** The digestWrongPasswordTemplate RestTemplate */
	private static final RestTemplate digestWrongPasswordTemplate = new DigestAuthRestTemplate("user", "foo");
	
	/**
	 * No security
	 */
	@Test
	public void testNoSecurity() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(SecuredServiceController.MAPPING + "/no/1"));
		ResponseEntity<String> response = digestUserTemplate.getForEntity(url, String.class);
		
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
		ResponseEntity<String> response = digestUserTemplate.getForEntity(url, String.class);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertTrue(StringUtils.containsIgnoreCase(response.getBody(), "login page"));
	}


	/**
	 * Digest security, unauthorized (wrong password)
	 */
	@Test(expected = RestClientException.class) // Unauthorized
	public void testDigestWrongPassword() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(SecuredServiceController.MAPPING + "/digest/1"));
		digestWrongPasswordTemplate.getForEntity(url, String.class);
	}
}
