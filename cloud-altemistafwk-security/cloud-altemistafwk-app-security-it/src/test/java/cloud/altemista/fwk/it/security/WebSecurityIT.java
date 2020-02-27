/**
 * 
 */
package cloud.altemista.fwk.it.security;

/*
 * #%L
 * altemista-cloud web and Spring Boot-based applications security integration tests
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
import org.junit.Assume;
import org.junit.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import cloud.altemista.fwk.it.AbstractWebDriverIT;
import cloud.altemista.fwk.it.WebDriverItRequest;
import cloud.altemista.fwk.it.security.controller.WebAliveController;

/**
 * Simple, base integration test to check the application has been deployed
 * @author NTT DATA
 */
public class WebSecurityIT extends AbstractWebDriverIT {
	
	/**
	 * Validates non secured parts of the application
	 */
	@Test
	public void alive() {
		
		final String body = this.testMapping(WebAliveController.MAPPING).getResponseBody();
		Assert.assertTrue(body, StringUtils.containsIgnoreCase(body, WebAliveController.ALIVE_MESSAGE));
	}
	
	/**
	 * Validates the application is secured
	 */
	@Test
	public void aliveSecure() {
		
		WebDriverItRequest request = this.invokeMapping(WebAliveController.MAPPING_SECURE);
		Assume.assumeTrue(request.getStatusCode() == HttpStatus.SC_OK);
		
		// Asserts the navigation has been redirected to the login page
		Assert.assertTrue(StringUtils.equalsIgnoreCase(request.getTitle(), "Login page"));
	}
	
	/**
	 * Validates the application is secured
	 */
	@Test
	public void aliveSecureLogin() {
		
		WebDriverItRequest request = this.navigateAndLogin(WebAliveController.MAPPING_SECURE, "user", "password");

		// Asserts has navigated to a page that is the desired page
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertFalse(StringUtils.equalsIgnoreCase(request.getTitle(), "Login page"));
		Assert.assertTrue(StringUtils.containsIgnoreCase(
				request.getResponseBody(), WebAliveController.SECURE_ALIVE_MESSAGE));
	}
	
	/**
	 * Validates the application is secured
	 */
	@Test
	public void aliveSecureBadLogin() {
		
		WebDriverItRequest request = this.navigateAndLogin(WebAliveController.MAPPING_SECURE, "user", "wrong");

		// Asserts has navigated to a page that is the login page again
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertTrue(StringUtils.equalsIgnoreCase(request.getTitle(), "Login page"));
		Assert.assertTrue(StringUtils.containsIgnoreCase(request.getResponseBody(), "Bad credentials"));
	}
}
