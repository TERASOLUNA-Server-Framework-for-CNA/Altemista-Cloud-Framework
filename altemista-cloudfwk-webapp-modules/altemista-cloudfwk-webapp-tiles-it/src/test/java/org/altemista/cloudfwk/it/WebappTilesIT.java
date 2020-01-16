/**
 * 
 */
package org.altemista.cloudfwk.it;

/*
 * #%L
 * altemista-cloud web application: JSP implementation (with Apache Tiles) integration tests
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
import org.altemista.cloudfwk.it.controller.TilesAliveController;

/**
 * Simple, base integration test to check the application has been deployed
 * @author NTT DATA
 */
public class WebappTilesIT extends AbstractIT {
	
//	@Test
//	public void testContainerRoot() {
//		
//		// Returns index.html
//		String body = this.testUri("/").getResponseBody();
//		
//		// This page does not have the tiles structure
//		Assert.assertFalse(body.contains("<section>"));
//	}
	
	@Test
	public void testSpringController() {
		
		// Returns a view...
		String body = this.testMapping(TilesAliveController.MAPPING).getResponseBody();
		
		// ...rendered by tiles (so the response should have the tiles structure)
		Assert.assertTrue(body.contains("<section>"));
	}
	
//	@Test
//	public void testSpringRoot() {
//		
//		// First, test this mapping is not returning 404
//		final String actualResponseBody = this.testMapping("/").getResponseBody();
//
//		// Then, tests this mapping is returning the same content as the controller"
//		final String expectedResponseBody = this.invokeMapping(TilesAliveController.MAPPING).getResponseBody();
//		
//		// (does not compare actual response bodies
//		// because they can differ on the jsessionid values of the links to css and js)  
////		Assert.assertEquals(expectedResponseBody, actualResponseBody);
//		Assert.assertEquals(expectedResponseBody.length(), actualResponseBody.length());
//	}
	
	@Test
	public void testException() {
		
		String body = this.testMapping(TilesAliveController.MAPPING + "/exception", HttpStatus.SC_CONFLICT)
				.getResponseBody();
		
		Assert.assertTrue(body.contains(Integer.toString(HttpStatus.SC_CONFLICT)));
	}
	
	@Test
	public void testServiceException() {
		
		String body = this.testMapping(TilesAliveController.MAPPING + "/serviceException", HttpStatus.SC_CONFLICT)
				.getResponseBody();
		
		Assert.assertTrue(body.contains(Integer.toString(HttpStatus.SC_CONFLICT)));
	}
	
	@Test
	public void testNullpointerException() {
		
		String body = this.testMapping(
				TilesAliveController.MAPPING + "/nullpointerException", HttpStatus.SC_INTERNAL_SERVER_ERROR)
				.getResponseBody();
		
		Assert.assertTrue(body.contains(Integer.toString(HttpStatus.SC_INTERNAL_SERVER_ERROR)));
	}
}
