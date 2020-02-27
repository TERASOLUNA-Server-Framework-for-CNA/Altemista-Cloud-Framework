/**
 * 
 */
package cloud.altemista.fwk.it;

/*
 * #%L
 * altemista-cloud web application integration tests
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
	 * Validates the container is not serving resources not meant to be served
	 */
	@Test
	public void testContainerResources() {
		
		// Resources not meant to be served
		this.testUri("/WEB-INF/notServed.html", HttpStatus.SC_NOT_FOUND);
		this.testUri("/WEB-INF/resources/webInfServed.html", HttpStatus.SC_NOT_FOUND);

		// Resources by the default Servlet of the Servlet container 
		this.testUri("/index.html", HttpStatus.SC_NOT_FOUND);
	}
	
	/**
	 * Validates the welcome file is being served at the root of the application
	 */
	@Test
	public void testContainerRoot() {
		
		// First, test this mapping is not returning 404
		final String actualResponseBody = this.testUri("/").getResponseBody();

		// Then, tests this mapping is returning the same content than "/index.jsp"
		final String expectedResponseBody = this.testUri("/index.jsp").getResponseBody();
		
		Assert.assertEquals(expectedResponseBody, actualResponseBody);
	}
	
	/**
	 * Validates Spring is serving the expected META-INF/web-resources and WEB-INF/web-resources
	 * (tests &lt;mvc:resources mapping="/**" location="classpath:/META-INF/web-resources/,/WEB-INF/web-resources/" /&gt;)
	 */
	@Test
	public void testSpringResources() {
		
		this.testUri("/metaInfServed.html");
		this.testUri("/webInfServed.html");
	}
	
//	/**
//	 * Validates Spring is not serving resources not meant to be served
//	 */
//	@Test
//	public void testSpringHiddenResource() {
//		
//		// Served by the container, not server by Spring
//		this.testMapping("/served.html", HttpStatus.SC_NOT_FOUND);
//		
//		// Not server by neither the container nor Spring
//		this.testMapping("/WEB-INF/notServed.html", HttpStatus.SC_NOT_FOUND);
//	}
	
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
		
		this.testUri("/nonExistent.html", HttpStatus.SC_NOT_FOUND);
//		this.testMapping("/nonExistent.html", HttpStatus.SC_NOT_FOUND);
	}
}
