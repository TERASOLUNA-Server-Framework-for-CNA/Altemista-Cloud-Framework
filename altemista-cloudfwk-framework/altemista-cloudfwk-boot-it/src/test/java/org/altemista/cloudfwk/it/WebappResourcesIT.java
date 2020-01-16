/**
 * 
 */
package org.altemista.cloudfwk.it;

/*
 * #%L
 * altemista-cloud Spring Boot application integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

/**
 * Simple, base integration test to check the application has been deployed
 * @author NTT DATA
 */
public class WebappResourcesIT extends AbstractIT {
	
	/**
	 * Validates the welcome file is being served at the root of the application
	 */
	@Test
	public void testContainerRoot() {
		
		// First, test this mapping is not returning 404
		final String actualResponseBody = this.testMapping("/").getResponseBody();

		// Then, tests this mapping is returning the same content than "/index.html"
		final String expectedResponseBody = this.testMapping("/index.html").getResponseBody();
		
		Assert.assertEquals(expectedResponseBody, actualResponseBody);
	}
	
	/**
	 * Validates Spring is serving the expected META-INF/web-resources and WEB-INF/web-resources
	 * (tests &lt;mvc:resources mapping="/**" location="classpath:/META-INF/web-resources/,/WEB-INF/web-resources/" /&gt;)
	 */
	@Test
	public void testSpringResources() {
		
		this.testMapping("/metaInfServed.html");
		this.testMapping("/publicServed.html");
		this.testMapping("/resourcesServed.html");
		this.testMapping("/staticServed.html");
	}
	
	/**
	 * Validates Spring is not serving resources not meant to be served
	 */
	@Test
	public void testSpringHiddenResource() {
		
		// Not server by neither the container nor Spring
		this.testMapping("/notServed.html", HttpStatus.SC_NOT_FOUND);
	}
	
//	/**
//	 * Validates the welcome file is being served at the root of the application
//	 */
//	@Test
//	public void testSpringRoot() {
//		
//		// First, test this mapping is not returning 404
//		final String actualResponseBody = this.testMapping("/").getResponseBody();
//
//		// Then, tests this mapping is returning the same content than "/index.html"
//		final String expectedResponseBody = this.testUri("/").getResponseBody();
//		
//		Assert.assertEquals(expectedResponseBody, actualResponseBody);
//	}
	
	/**
	 * If this test fails means the integration tests are not working as expected
	 */
	@Test
	public void test404NotFound() {
		
		this.testMapping("/nonExistent.html", HttpStatus.SC_NOT_FOUND);
	}
}
