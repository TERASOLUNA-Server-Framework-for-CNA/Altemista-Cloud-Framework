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
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.altemista.cloudfwk.it.rest.controller.SecuredServiceController;

/**
 * Tests the web security module through the REST controller
 * @author NTT DATA
 */
public class ForwardRestTemplatesIT extends AbstractWebDriverIT {
	
	/**
	 * Basic security, forwarded to another basic security
	 */
	@Test
	public void testBasicToBasic1() {
		
		WebDriverItRequest request = this.navigateAndLogin(
				SecuredServiceController.MAPPING + "/basic/to/basic/1", "user", "user");
		
		// Asserts the navigation has been forwarded to the correct page
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertFalse(request.getTitle(),
				StringUtils.equalsIgnoreCase(request.getTitle(), "Login page"));
		Assert.assertTrue(request.getResponseBody(),
				StringUtils.containsIgnoreCase(request.getResponseBody(), "Hello, basic"));
	}
	
	/**
	 * Basic security, forwarded to another basic security
	 */
	@Test
	public void testBasicToBasic2() {
		
		WebDriverItRequest request = this.navigateAndLogin(
				SecuredServiceController.MAPPING + "/basic/to/basic/2", "user", "user");
		
		// Asserts the navigation has been forwarded to the correct page
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertFalse(request.getTitle(),
				StringUtils.equalsIgnoreCase(request.getTitle(), "Login page"));
		Assert.assertTrue(request.getResponseBody(),
				StringUtils.containsIgnoreCase(request.getResponseBody(), "RestClientException from"));
	}
	

	/**
	 * Basic security, forwarded to a digest security
	 */
	@Test
	public void testBasicToDigest2() {
		
		WebDriverItRequest request = this.navigateAndLogin(
				SecuredServiceController.MAPPING + "/basic/to/digest/2", "user", "user");
		
		// Asserts the navigation has been forwarded to the correct page
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertFalse(request.getTitle(),
				StringUtils.equalsIgnoreCase(request.getTitle(), "Login page"));
		Assert.assertTrue(request.getResponseBody(),
				StringUtils.containsIgnoreCase(request.getResponseBody(), "RestClientException from"));
	}
}
